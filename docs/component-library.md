# LiquidGlass UI 2.0 接入与迁移

本次直接替换公开 API，不保留旧别名。要求 Android 13（API 33）以上，Compose 与 Kotlin 版本请参考仓库 Gradle 配置。`packageLibrary` 生成本地分发仓库，不上传到远端。

## 一个依赖

运行 `gradlew.bat packageLibrary`，将 `dist/maven` 作为 Maven 仓库添加到消费者项目，然后依赖 `dev.liquidglass:liquidglass-ui:2.0.1`。仓库包含 `liquidglass-ui`、`liquidglass-navigation` 和 `liquidglass-core`，Gradle 根据 POM / module 元数据自动获取后两者。不要只拷贝顶层 AAR，它不内嵌依赖库。

2.0.1 为 `GlassDialogTextField` 增加密码掩码、禁用、错误提示、标签和尾部按钮的标准参数，记录见 [ToScreen 输入接入](toscreen-input-2.0.1.md)。底栏独立按钮可用 `searchItem = GlassTab("添加设备", icon = ...)` 和 `onSearchClick` 设置图标、无障碍名称及操作；`layout.showSearchButton = true` 控制显示。

## 主题、采样和受控状态

整套切换用 `GlassTheme(standard = GlassStandard.Ios27)`。需要混搭时添加 `effects`：

```kotlin
GlassTheme(
    standard = GlassStandard.Ios26,
    effects = GlassEffects(highlight = GlassStandard.Ios27),
) { /* 26 材质 + 27 高光 */ }

// 仅覆盖某个组件或区域，继承其他效果及当前配色、采样源。
GlassEffectScope(effects = LocalGlassEffects.current.copy(material = GlassStandard.Ios27)) {
    GlassButton(onClick = {}) { Text("局部材质") }
}
```

`GlassEffects.material`、`highlight` 的 null 表示跟随整体标准，非 null 表示固定使用该项的 26 或 27 实现。嵌套主题默认继承覆盖；传 `GlassEffects()` 清除覆盖。`GlassTabBar` / `GlassTabBarScaffold` 也可直接传 `effects`。高光覆盖只改变光照，菜单颜色叠加、来源按钮阴影、底栏轮廓等仍跟随材质，不被高光带动。底栏 MeloX / 通用光学参数仍由 `preset` 独立选择。

`GlassMenuMaterialScope { ... }` 默认读取当前材质标准，不再把 `currentGlassHighlight()` 当作材质参数。需要显式指定时传 `GlassStandard`。

设置页提供“整体标准”和“单项覆盖”。整体切换会清除三项覆盖；玻璃材质、高光、按钮拖拽效果都选择“跟随整体 / iOS 26 / iOS 27”，并保存在本机。按钮拖拽效果只切换按钮整套按压、位移和扭曲。

```kotlin
GlassTheme(
    standard = GlassStandard.Ios26,
    effects = GlassEffects(highlight = GlassStandard.Ios27, buttonDragEffect = GlassStandard.Ios27),
) { /* 26 材质，27 高光和按钮拖拽效果 */ }
```

`buttonDragEffect = null` 时按钮跟随整体标准。按用户纠正，26/27 的整套对应关系已对调：26 拉伸原始轮廓，27 使用按实时尺寸重画轮廓的适配效果。原版 27 的轻微形变分支已删除。按压、位移和扭曲作为整套运动切换，两种效果都可配任意材质，按钮前景和 Goo 轮廓读取同一形变函数；输入仍由各材质原组件处理。

菜单、Popup、子菜单和模态始终共用 27 动画，不读取按钮拖拽效果，也不随材质或整体标准改变。菜单/Popup 使用 27 路由时钟和子菜单弹簧；模态使用 27 Alert/BottomSheet 动画。先前添加的 26 弹窗弹簧分支及旧 `animation` 参数已删除，不保留别名。

按钮拖拽效果也决定实际轮廓：26 拉伸按钮的原始轮廓，27 按实时宽高重画胶囊。该选择独立于玻璃材质，26/27 材质均支持两种轮廓；单独按钮与 Goo 分组读取相同规则。只改变位移和缩放公式而让材质分支继续决定轮廓，会导致独立选项看起来只能改变限制，属于错误接入。

按钮拖拽效果不改变布局终点、弹窗方向、材质、颜色、高光或回调。底栏的 MeloX 运动、开关和滑块是两标准共用实现，继续共用。`GlassEffectScope(effects = LocalGlassEffects.current.copy(buttonDragEffect = ...))` 可单独覆盖某个区域。

`GlassEffects.limitButtonDrag` 默认 `true`，同时控制 26 和 27 按钮效果。设为 `false` 时取消对应实现额外的距离衰减及绘制余量限制，位移与扭曲一起恢复原响应；各自轮廓、按压尺寸和弹窗动画不变。设置页“按钮拖拽 → 限制拖拽幅度”使用液态玻璃开关并保存在本机，切换整体标准不会清除该选择。例如 `GlassEffects(buttonDragEffect = GlassStandard.Ios26, limitButtonDrag = false)` 对 26 效果取消附加限制，选择 27 时同样生效。

```kotlin
import dev.liquidglass.*

var checked by rememberSaveable { mutableStateOf(false) }
var volume by rememberSaveable { mutableFloatStateOf(.5f) }

GlassTheme(standard = GlassStandard.Ios27, darkTheme = false) {
    GlassScene(Modifier.fillMaxSize(), background = { BackgroundContent() }) {
        Column {
            GlassButton(onClick = { checked = !checked }) { Text("切换") }
            GlassSwitch(checked = checked, onCheckedChange = { checked = it })
            GlassSlider(value = volume, onValueChange = { volume = it })
            GlassButtonGroup {
                Row {
                    GlassButton(onClick = {}) { Text("左侧") }
                    GlassButton(onClick = {}) { Text("右侧") }
                }
            }
        }
    }
}
```

主题负责标准、深浅色、强调色与菜单高光。`GlassButton` 在 26 / 27 间路由，`animatePress` 只控制运动，`enabled` 控制输入。`surfaceColor` 覆盖材质色，`tint` 表示着色按钮；同色融合、异色不混色规则保持已有行为。`height` 是按钮高度，`contentPadding` 是内容内距。

开关和滑块由调用者持有业务状态。滑块默认 `valueRange = 0f..1f`、`dragAnywhere = true`，完成后通知 `onValueChangeFinished`。无共享采样源时，基础控件使用主题背景色的独立 CanvasBackdrop；需要透出真实背景时提供 `GlassScene` 或独立 Backdrop。

已有安全采样层用 `GlassBackdropProvider(backdrop) { ... }`。Popup / 模态中的按钮优先继承当前局部采样源，避免把底层页面的 Backdrop 穿透传入。不要将控件与它读取的背景录制在同一层。

## 标签栏

LiquidTab 接入保留既有独立键入口 `layout.showSearchButton` / `onSearchClick` / `searchEnabled`。
新增可选 `searchItem` 仅覆盖独立键文字和图标；`animateSearchVisibility` 同步动画化键与主栏宽度预算。
`actionIndices` 标记主栏中点击执行动作而不切换页面的槽位，拖动释放后指示器回到当前页。
`panelDecoration` 把宿主皮肤绘制到面板与折射捕获层；`showLabels` 控制默认文字，`itemContent` 继续用于完整原生槽位。
这些参数默认保持原行为，27 高光仍走独立 `surfaceLayerBlock`。本轮记录见 `liquidtab-integration-changes.json`。

```kotlin
var selected by rememberSaveable { mutableIntStateOf(0) }
GlassTabBarScaffold(
    items = listOf(GlassTab("首页"), GlassTab("设置")),
    selectedIndex = selected,
    onSelectedIndexChange = { selected = it },
    layout = GlassTabBarDefaults.layout(
        compact = true, showSearchButton = true, avoidNavigationBar = true,
    ),
    onSearchClick = { /* 打开搜索 */ },
) { PageContent(selected) }
```

`GlassTab.icon` 接收 Modifier、颜色和选中状态；整项定制用 `itemContent`。`GlassTabBar` 使用已有 Backdrop；`GlassTabBarScaffold` 将内容与底栏放在兄弟层。Scaffold 不替页面决定底部留白；滚动页按布局和播放器占用预留 padding。

| 参数 | 含义 |
| --- | --- |
| `layout.compact` | 按标签数量约束宽度，不铺满 |
| `layout.height` | 展开时高度 |
| `layout.horizontalMargin` / `bottomMargin` | 外边距 |
| `layout.showSearchButton` / `searchSpacing` | 搜索显示与间距 |
| `layout.avoidNavigationBar` | 避让系统底部导航区 |
| `collapsed` | 由宿主管理的收缩状态 |
| `frosted` / `adaptToBackground` | 毛玻璃 / 自动背景明暗 |
| `searchSelected` / `searchEnabled` | 搜索选中 / 是否响应点击 |
| `player` | 播放器插槽，null 表示不显示 |

默认 MeloX 材质；`preset = GlassTabBarPreset.Standard` 选择通用材质，26/27 高光独立。`GlassTabBarDefaults.layout(preset)` 保留原预设数值，局部调整用 `.copy(height = 64.dp)`。布局只有一个来源，不再有分散开关覆盖 layout 的问题。

## 菜单、Popup 与模态

`GlassMorphMenu`、`GlassPopup`、`GlassModal`、`GlassDialog` 直接迁入 `dev.liquidglass`，没有复制封装。菜单动作使用 `GlassMenuAction`；Popup 使用 `rememberGlassPopupState<T>()`、锚点与 `GlassPopupSourceContent` 管理按钮变形和返回。Popup 宿主需覆盖锚点所在根窗口，不能放进按钮自身大小的裁切层。

可运行接入位于 `sample/.../Ios27NativeExamples.kt`、`Ios26ModalFramework.kt`。历史文件名不决定标准。关闭时通过 `onReturnComplete` 等生命周期接口卸载，不能提前恢复原按钮，否则会出现材质与阴影跳变。高光继承主题；光学和运动参数保留原实现。

## 迁移表

本轮内部精简不改变 `GlassButton`、`GlassTabBar` 等组件的调用方式。26/27 按钮共享拖动计算，仍各自保留距离响应和轮廓策略；`limitButtonDrag` 控制两者。底栏与核心控件共享阻尼状态及按压高光，底栏继续接收已被同链拖动层消费的事件，搜索返回只弹动位置。未接入标准采样入口的 Atlas / 捕获规划机制继续保留。文件删除和前后哈希见 `deep-cleanup-changes.json`。

| 旧入口或参数 | 新入口或参数 |
| --- | --- |
| `dev.liquidglass27.glass.*` | `dev.liquidglass.*` |
| `LiquidGlassStandard` | `GlassStandard` |
| `Ios27GlassScene` / `Ios27BackdropScope` | `GlassScene` / `GlassBackdropProvider` |
| `prepareIos27Glass` | `prewarmGlass` |
| `DemoGlassButton` / `DemoFusionGroup` | `GlassButton` / `GlassButtonGroup` |
| `UnifiedGlassTabBar` / 旧 Dock 别名 | `GlassTabBar` |
| `UnifiedGlassTabBarScaffold` / `MeloXTabBarScaffold` | `GlassTabBarScaffold` |
| `GlassNavigationPreset` / `GlassNavigationLayout` | `GlassTabBarPreset` / `GlassTabBarLayout` |
| `labels` + `icons` | `items: List<GlassTab>` |
| `onSelect`（公开底栏） | `onSelectedIndexChange` |
| `layoutOverride`、分散的布局开关 | `layout` |
| `minimized` / `frostedGlass` / `automaticTone`（公开底栏） | `collapsed` / `frosted` / `adaptToBackground` |
| `hasMedia` + `miniPlayer`（公开底栏） | 可空 `player` |
| `buttonHeight` / `isInteractive`（公开按钮） | `height` / `animatePress` |
| `GlassTheme(dark, seed, style)` | `GlassTheme(darkTheme, accentColor, colorStyle)` |

底层 `LiquidButton` / `LiquidToggle` / `LiquidSlider` 仍是共享渲染实现，不是兼容别名。旧 Ios27 Figma 页面组件仍承担布局职责。原始源码快照、归属与许可证不删，不刷新基线掩盖改动。已导入的 Atlas / 捕获规划等机制保留；标准入口不叠加第二套采样后端。
