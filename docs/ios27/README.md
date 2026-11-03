# iOS 26 / iOS 27 标准

当前 API 已升级为组件库 2.0：统一包 `dev.liquidglass`，使用 `GlassTheme(standard = GlassStandard.Ios26 / Ios27)` 与 `GlassScene` / `GlassBackdropProvider`。底栏统一为 `GlassTabBar`，旧 `Unified*`、`Ios27*Dock` 与 Demo 别名删除。消费者只需 `dev.liquidglass:liquidglass-ui:2.0.0`；[接入和完整迁移表](../component-library.md)、[删除与移动记录](../component-library-changes.json)。下文保留合并来源和历次修复记录，其中旧 API / 路径只用于追溯，不能作为当前接入方式。

2026-11-03 按用户要求：本次合并前的实现归为 **iOS 26**；`liquidglass27` 的全部非演示机制归为 **iOS 27**。这是本项目的版本划分，不表示 Apple 官方实现或像素级认证。

| 标准 | Gradle 依赖 | 接口与用途 |
| --- | --- | --- |
| iOS 26 | `:liquidglass`；新模态宿主同时依赖 `:liquidglass-ios27` | 原有控件；弹窗/模态采用 27 框架和 26 高光，MeloX / ColorOS 独立接入 |
| iOS 27 | `implementation(project(":liquidglass-ios27"))` | 新几何、高光、弹性动作、Goo、Dock、多级菜单、Popup、Alert / Dialog、主题、124 图标与性能机制；传递依赖原 Sketch 光学核心 |

旧 `dev.liquidglass.ios27` 包、`Ios27*` 符号、tokens 生成器和 Figma 文件名是兼容性名称，仍代表旧 iOS 26 实现/参考数据。没有批量改名或重新生成原稿，以免破坏已有宿主。新增标准选择使用 `dev.liquidglass.standard.LiquidGlassStandard`；新的机制 API 在 `dev.liquidglass27.glass`。

机制库不包含 demo UI。示例统一收入现有 sample：Popup、主题与图标在“通用演示”，多级菜单在“菜单”。原独立 27 按钮、Goo、底栏、对话框演示页已删除，机制保留并接入现有示例。MeloX 与通用底栏现已合并：在“标签栏”分类保留“收缩、搜索与播放器”示例。Listening Mode 从一级列表直接打开并保持原版；ColorOS 17 保持原版，收入二级分类。统一复用现有页面框架、返回按钮和 tokens；没有导入源项目 `GlassShowcase` / `GlassVerificationPage`。

通用菜单、Popup 和图标实现在 `sample/src/main/java/dev/liquidglass/sample/Ios27NativeExamples.kt`；`DemoGlassStandard.kt` 统一为其余示例接入设置中的 26/27。行内控件采样独立背景；浮层在页面录制层之外，Popup 内按钮采样父背景与面板表面的合成层。菜单保留多级选择和长列表，Popup 保留保存/取消及模态切换，图标页保留全部图标。浅深色继续跟随应用设置。

透明/彩色按钮的 Goo 不再按颜色拆组。27 的同排按钮共享轮廓与背景，相邻按钮覆盖色相同时，覆盖色绘制到完整融合轮廓（含连接处），替换 27 默认颜色；即使同组还有异色按钮，也保留这一行为。仅异色边界按各自轮廓裁切，不做渐变或颜色混合，不跨不同采样源连接。26 保持原按钮和现有连接绘制，本次同色覆盖修正不改 26。按压/拖动和 Popup 源按钮隐藏机制保留。

27 按钮的拖拽保留 26 按压几何和释放弹簧，拖动向量以按钮长边三倍作为 `tanh` 响应距离；平移与拉伸共同消耗外扩余量，在剩余空间的前 65% 不额外衰减，最后 35% 平滑减速。这样保留普通距离的拉伸，避免远距离持续线性放大。普通按钮/Goo 分组沿用 16dp 外扩上限；`GooGlassRow` 的轮廓和前景同时使用其已有 8dp 留白。这些响应参数为本地手感适配。原 26 按钮、底栏、Popup 面板弹簧及颜色不变。静止按压放大不受拖拽衰减影响。

一级“标签栏”直接进入唯一的合并演示页，原普通、分离搜索、收缩/播放器三个演示不再分别列出。开关和调整项就在演示页内：紧凑、收缩、滚动收缩、选择后展开、毛玻璃、自动明暗、播放器、搜索显示/可点击/选中、安全区、图标/文字、字体，以及标准、材质预设、主题、选中颜色、标签数量/名称和高度/边距/搜索间距。布尔项复用 `Ios27SwitchRow` 的 `LiquidToggle`，数值项复用玻璃滑块；行内控件读取独立 `CanvasBackdrop`，底栏仍为页面采样层的兄弟。参数保存在本页，支持恢复默认；标准和预设默认跟随全局设置，不修改底层光学数值。`UnifiedGlassTabBar` 新增默认兼容的布局、搜索可用性与字体转接，`layoutOverride` 非空时完整接管布局字段，未指定时保持原行为。旧两个示例键与组件 API 路由到同一演示页，原稿资源与显式原稿对照入口保留；旧 NativeExamplePage 中重复的标签栏状态和绘制已删除。

标签栏演示页只使用主题页面底色，不读取全局背景图片；列表内用于观察材质的彩色内容仍保留。顶部复用 `ExampleTopChrome`，使用 `colors.page` 作为模糊底色，按列表起始 30dp 滚动距离渐显，回到顶部归零；模糊与返回按钮仍在独立采样层之外。不修改公共渐进模糊实现。

设置 → 玻璃标准控制普通示例的按钮、底栏、菜单与模态高光；26 保留原按钮，27 按钮保留 27 材质和高光，按压/拖拽采用 26。底栏统一为 `UnifiedGlassTabBar`，采用 MeloX 布局/交互和 Sketch 光学核心；设置 → 底栏玻璃参数可切换 MeloX / 通用，默认 MeloX，并独立跟随 26 / 27 高光选择。27 通过 `rememberIos27DockAppearance` 注入高光和轮廓。弹窗和模态共用 27 框架，高光按所选标准切换。开关、滑块等无独立 27 实现的组件共用原实现。安装下限为 API 33，默认 27。

底栏合并的依赖方向为 `:liquidglass-ios27 → :melox-tabbar → :liquidglass`，没有反向依赖或第二份光学核心。旧 `Ios27NavigationDock` 为统一入口的兼容封装。主界面保留三项导航；独立搜索、滚动收缩和播放器按调用者选择启用。原提取来源与哈希保持冻结，MeloX 授权变更另记在 `docs/melox-tabbar-merged-files.json`。

重复底栏清理：已移除未使用的 `LiquidBottomTabs` / Compact / Regular / SplitSearch 函数、`LiquidBottomTabsStyle`、`Ios27BottomTabsSplitSearch` 和未使用的宿主 `Ios27StandardDock`。`GlassNavigationDock` 也转接统一入口，删除原来的独立 Canvas / 手势实现。`LiquidBottomTabs.kt` 仅保留高光层枚举，`LiquidBottomTab` 仍供分段选择器使用。当前 MeloX 渲染器、参数预设、26 / 27 高光和紧凑模式代码均未修改；不再为旧入口保留重复绘制路径。来源快照继续保留作溯源，未编入产物。

Popup 已与菜单共用 `SketchMenuRootSurface` 及 `menuRootCornerPower`，包括源按钮高光到面板边缘高光的连续交接。前景快照和内容都保持宽高比例；内容转换对齐 FlClash `popup.dart` 的 `_contentTransform`，居中定位并按实时菜单轮廓裁切，避免把按钮字形随面板宽高分别拉伸。修改依据与验收见 [底栏与 Popup 修正](dock-popup-correction.md)。

## 接入 iOS 27

启动时可调用 `prepareIos27Glass(applicationContext)`。全部模块安装下限为 API 33（Android 13），无需低版本渲染分支；宿主已移除 `overrideLibrary`。

```kotlin
if (Build.VERSION.SDK_INT >= 33) {
    GlassTheme(dark = dark) {
        Ios27GlassScene(
            modifier = Modifier.fillMaxSize(),
            background = { PageBackground() },
        ) {
            ElasticGlassButton(
                modifier = Modifier.size(56.dp),
                style = GlassStyle.regular(dark),
                onClick = onAction,
            ) { Text("Action") }
        }
    }
} else {
    ExistingIos26Content()
}
```

`Ios27GlassScene` 固定启用 Sketch 光学。原始 `GlassScene` API 仍保留完整实验分支，默认 `sketchMaterial=false`；直接调用它时必须明确选择。实验 capture planner / GPU atlas 完整迁入但不强制启用。

已有独立 `Backdrop` 时，用 `Ios27BackdropScope(backdrop) { ... }` 提供场景级材质缓存与上下文。该 scope 不重新录制背景、不缩放整图；传入 source 必须不含它自己的玻璃。菜单和 Popup 所在场景需覆盖根窗口，锚点使用 `positionInRoot()` 的像素值。具体层级见 [背景接入约束](../glass-backdrop-integration.md)。

| 机制 | 公开 API |
| --- | --- |
| 容器与材质 | `Ios27GlassScene`, `Ios27BackdropScope`, `LiquidGlass`, `GlassPane`, `GlassStyle` |
| 弹性与融合 | `ElasticGlassButton`, `rememberElasticPressState`, `elasticPress`, `GooGlassRow`, `GooGlassGroup` |
| 标准导航 | `Ios27NavigationDock`, `GlassDestination`；原完整机制 `GlassNavigationDock` 继续保留 |
| 多级菜单 | `GlassMorphMenu`, `GlassMenuAction`；保留锚点、按压接力、回源、长列表与滚动回弹 |
| Popup 生命周期 | `GlassPopupState<T>`, `GlassPopupBox`, `GlassPopupSourceContent`, `GlassPopup` |
| Alert / Dialog | `GlassAlert`, `GlassDialog`, `GlassDialogAction`, `GlassDialogTextField` |
| 主题与图标 | `GlassTheme`, `glassColorScheme`, `GlassColorStyle`, `GlassGlyphs` |

源码中的场景级 32 项材质缓存、单 renderer shader 所有权、异步预热、九层 Picture 高光缓存、绘制阶段动画、曲线/轮廓/文本宽度缓存、惰性长菜单及滚动合并均成组保留。新桥接关闭旧 Sketch 默认高光，旧控件默认参数不变。没有复制第二份 Sketch 核心。

## 来源与验证

### iOS 26 多级菜单

手机示例库新增 **iOS 26 → 多级菜单**，沿用 27 的来源按钮展开、逐级返回、嵌套采样、长列表与选择回调，包含三级排序示例。调用 `GlassMorphMenu(highlightStyle = GlassMenuHighlight.Ios26, ...)` 即可选用 26 高光，默认仍为 `Ios27`。

两种菜单均在 `Ios27BackdropScope` / `Ios27GlassScene` 内使用（Android 13+）。26 版本通过共享库的 `drawGlassHighlight` 直接复用 `Highlight.Default`、原有陀螺仪监听与生命周期；根菜单、各级子菜单都沿动态路径绘制，不再叠加 27 ControlHighlight 或原生菜单边缘。主体、动画与菜单路由仍为 27 机制；共享光学依赖方向不变，没有新增 Flutter 依赖。

26 示例的来源按钮也使用现有 `LiquidButton`。当前最低 API 33。本轮按用户要求只构建、安装，不运行测试或启动应用；既有示例路由检查源码同步更新但未执行。全部弹窗迁移范围见 [模态框架迁移](modal-framework.md)。

### 普通按钮加入 Goo

`GooGlassGroup` 内的 `ElasticGlassButton` 默认参与融合，支持文字、图标、着色内容和 `GlassPopupBox` 来源按钮，无需转换成 `GooAction`。继续使用按钮自身的点击、按压、拖动和来源前景录制。

```kotlin
GooGlassGroup(Modifier.fillMaxWidth()) {
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        ElasticGlassButton(Modifier.weight(1f).height(52.dp), GlassStyle.regular(dark),
            onClick = onSave) { Text("保存") }
        ElasticGlassButton(Modifier.weight(1f).height(52.dp), GlassStyle.regular(dark),
            onClick = onCancel) { Text("取消") }
    }
}
```

沿用源 Goo 的胶囊轮廓、同排判断和 8+8dp 按压融合量；相同材质的相邻按钮合成一个材质面，不跨行或跨不同颜色/光学材质连桥。按照实际布局位置排序，支持不同宽度、RTL 和动态增删；超过六个按钮按 shader 容量分批，每批最多六个，批次之间不连桥。分组内部预留 24dp 以容纳按压和拖动外扩。

`gooEnabled=false` 可让按钮独立绘制，适用于自定义非胶囊轮廓或宿主用 alpha 隐藏按钮时。`GlassPopupBox` 自动在回源完成前退出融合，避免分组绘制隐藏来源。组外按钮保持原有行为。分组必须放在已有独立背景 scope 中，不得将分组自身录入采样源。

宿主“弹性按钮”和“Goo 操作组”提供文字/图标混排、着色以及动态移除示例；“Popup”提供可与图标按钮融合的来源按钮。

- [逐文件清单](merge-manifest.json)：源/目标路径、baseline 与 merged SHA-256、本次修改前的旧运行文件哈希。
- [原合并指南](source-merge-guide.md)：保留原文；机制完整合并，演示由当前 sample 自行组织，未复制原演示 UI。
- [原验收清单](source-verification-2026-11-03.md)、[原性能报告](baseline/README.md)：均为源项目历史记录；其中相对链接以源工作区为基准，不能当成目标项目结果。
- [来源与许可](provenance.md)、[本次验证](validation.md)。

```powershell
node tools/verify-ios27-merge.mjs
node --test tools/ios27/native-examples.test.mjs
node tools/verify-extraction.mjs
node tools/generate-ios27-tokens.mjs --check
python tools/ios27/generate_glass_glyphs.py --check
python tools/ios27/check_control_highlight.py
.\gradlew.bat :verifyIos27Merge :verifyLiquidGlassSpec :liquidglass:testDebugUnitTest :liquidglass-ios27:testDebugUnitTest :liquidglass-ios27:lintDebug :sample:assembleDebug :verifyDebugArtifacts -x :sample:sendDebugApkToQQ --no-daemon --console=plain
```

新 AAR 依赖旧 `:liquidglass`、Compose、Material3 等，不能只复制一个裸 AAR 就假定依赖齐全。推荐 Gradle 项目依赖。当前迁入可完全脱离原 `C:\IDE\Android\liquidglass27` 目录构建和运行测试。此轮只完成代码及构建，真机由用户验收；设备脚本保留为可选入口，没有自动运行。

底栏截图反馈后的材质层修正同时涉及 `LiquidBottomTabs.kt` 和 `DrawBackdropModifier.kt` 两个核心文件；新参数默认 null，基线与授权改动分别记录。见 [修正记录](dock-popup-correction.md)。

底栏高光采用原版圆角解析分支，并以 Android Path 实现同一连续圆角轮廓，撤回 256 射线变体。`prepareIos27Glass` 在后台准备两份独立高光 RuntimeShader，由主体和指示器通过线程安全队列领取；预热未完成或库存耗尽时保留按需创建回退。实现仅使用 Kotlin / Compose / AGSL，不依赖 Flutter SDK 或引擎。26 的光学与交互保留，只有 27 底栏通过 `surfaceShape` 注入匹配的原生轮廓。Atlas / Capture Planner 仍按用户“跳过”保留 Sketch 旁路设计。
