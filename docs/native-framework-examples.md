# 可操作手机示例（1.0.19）

涉及图片背景、Inline 日历或玻璃 Popup 时，先读 [玻璃采样接入方法](glass-backdrop-integration.md)。纯色列表控件与透图面板使用不同的独立采样源，不能混用。

## 默认打开真实页面

底部“组件”页直接承载手机示例库 → 分类 → 变体，现在默认进入 `NativeExamplePage`，不再保留旧组件演示页，也不把 `FigmaSceneView` 当作应用页面。

“原稿对照”才打开离线矢量参考；点“返回可操作页面”返回，控件状态保留。56 个原稿入口映射到各类框架示例。当前重点是实际操作、主题和状态连接，**不是所有变体的原稿布局都已逐像素还原**。

原稿的生成 JSON、图片、字形和提取基线没有被覆盖。历史截图与矢量导出工具仍可用于后续逐项对齐。

## 可供新页面直接调用的框架接口

新增文件：`liquidglass/src/main/java/dev/liquidglass/ios27/Ios27InteractiveControls.kt`。

| 接口 | 行为 |
| --- | --- |
| `Ios27SliderRow` | Float 数值、释放回调、数值无障碍操作；`showLabel=false` 为 52dp 紧凑行 |
| `Ios27SwitchRow` | 真实开关及无障碍状态；同一回调接收触摸与辅助操作；可传 `showStateText=false` 隐藏「已开启/已关闭」，原三参数接口保持默认显示 |
| `Ios27StepperRow` | 在范围内增减，边界按钮自动禁用 |
| `Ios27EditableTextField` | 示例文本框的四态实现（Placeholder、Focused、Typing、Value Entered），统一处理系统输入法、光标、清空按钮、占位文字和主题颜色 |
| `Ios27ActionButton` | 原有 LiquidButton；低版本使用普通按钮 |

Tab Bar 不使用普通按钮或自绘胶囊。新页面直接复用 sample 宿主的
`Ios27TabBarExample`，由它统一调用库里的 `LiquidBottomTabsRegular`、
`LiquidBottomTabsSplitSearch` 和 `LiquidBottomTab` 子项：

```kotlin
Ios27TabBarExample(
    file = entry.file,
    selectedTabIndex = tab,
    onTabSelected = { tab = it },
    backdrop = pageBackdrop,
    isLightTheme = !dark,
    splitSearch = entry.variant == "Split Search=True",
    onSearchClick = { searchOpen = true },
)
```

组件库现在固化了三种布局入口：`LiquidBottomTabsCompact` 对应 Apple
紧凑宽度，项目根据可用总宽度分配；`LiquidBottomTabsRegular` 对应常规宽度，
按 `itemWidth + itemSpacing` 计算整组宽度并居中；`LiquidBottomTabsSplitSearch`
把两项 Tab 组和独立搜索玻璃按钮作为一个可复用组件。示例页调用常规入口，
`Split Search=False` 使用连续 Tab；`Split Search=True` 调用 split-search 入口。

### 文本输入统一入口

应用内所有真正可编辑的文本输入都必须调用
`sample/src/main/java/dev/liquidglass/sample/Ios27TextFieldsExample.kt` 中的
`Ios27EditableTextField`，不要在页面中新增 Material `TextField`、
`BasicTextField` 或旧的 `Ios27TextFieldRow` 调用。页面只负责持有 `value`、
实现 `onValueChange`，并按场景传入 `description`、`placeholder` 和
`keyboardOptions`；示例组件内部会统一提供四态显示、焦点光标、右侧清空按钮、
无障碍描述和深浅色适配。`BasicTextField` 仅允许保留在这个示例组件的内部，作为
系统输入法的底层编辑节点。

已有输入页面（Keyboard、List、Menus、Context Menu、Sheets 和 Alert）均应遵循
同一入口，这样新增页面不会再次出现输入框样式分叉。

`Action Sheet` 示例进入即显示操作面板。宿主下层只保留背景内容，不再放置
“打开操作面板”入口、“最近操作”状态区或底部 Snackbar；操作面板内部按钮的
反馈仍通过回调更新状态，但不在该示例下层显示提示。
不要在页面里重新拼接 `Row`、按钮
或自绘胶囊。

选中胶囊和隐藏参考层必须使用与实际 Tab 槽相同的 `tabWidth`，不能用父轨道比例
单独计算；否则选中第一项和最后一项时两侧外边距会不一致。

Tab Bar 的页面级 `LiquidBottomTabsStyle` 固定为外层轨道 `62dp`、选中指示器
`54dp`；分离搜索按钮外层也固定为 `62dp`，不能直接使用 `54dp`，否则会比
Tab 轨道短一截。当前两项 Tab 的原稿宽度为 `236dp`，与搜索按钮之间为 `16dp`，
由 `LiquidBottomTabsSplitSearch` 内部维护。常规三项示例使用 `94dp` itemWidth
和 `-8dp` itemSpacing，得到 `266dp` 的主胶囊宽度。

Stepper 原稿页使用 sample 宿主的 `Ios27StepperExample`，不要用通用的 `Ios27StepperRow` 代替像素布局：行是 370×52dp，标题为 `Stepper`，右侧 accessory 是 92×32dp 的 16dp 胶囊；减号和加号各占 46×32dp，中间分隔线为 1×24dp。数值只保存在状态中，不绘制额外的 `0/1` 文本；边界状态通过按钮 enabled 语义体现。点击反馈必须裁剪到可见胶囊：减号只使用左侧圆角，加号只使用右侧圆角，禁止让 clickable indication 以矩形透出。

日期/时间示例中的 `ValuePill`、inline 时间胶囊和 compact 日期/时间 accessory 也必须把点击反馈裁剪到自身的 50% 胶囊形状；不要再套用 6dp 方角点击外框。可见背景、点击反馈和语义点击区域应保持同一外形，避免按下时出现方形遮罩。

### Sheets (Modals)

Sheets 页面直接复用 sample 宿主的 `Ios27SheetsModal`，不要改回通用
`ModalBottomSheetLayout`。入口按原稿的 `entry.variant` 自动选择三种结构：

| 原稿变体 | 结构 | 关键几何 |
| --- | --- | --- |
| `Type=Fullscreen` | 底部模态页，带 20% 黑色遮罩 | 390×459dp，左右 6dp、底部 6dp；上圆角 34dp、下圆角 58dp |
| `Type=Inspector` | 底部模态页，无遮罩 | 与 Fullscreen 相同 |
| `Type=Fullscreen Stack` | 顶部叠放的全宽模态页 | y=72dp、宽度撑满、842dp 高；上圆角 38dp、下圆角 10dp |

面板头部固定为 58×4dp grabber、左侧 44×44dp 圆形叉号关闭按钮、居中 `Title` 和右侧
44×44dp 蓝色操作按钮。左侧叉号负责关闭当前页面，操作按钮只报告演示状态；后续业务
接入应继续通过 `onAction` 回调，不要把业务状态写进玻璃组件。`Ios27SheetsModal`
使用页面的独立 `LayerBackdrop` 作为兄弟层采样源，面板自身不得成为录制树的一部分，
否则会重新引入 RenderThread 递归闪退。低于 API 33 时保留同一几何并降级为半透明纯色。

面板内部的 Label/操作按钮不能继续复用页面 `LayerBackdrop`；它们使用颜色一致的独立
`CanvasBackdrop`，避免按钮越过模态层级直接采样页面。原稿 Symbol 的坐标按其真实节点
宽度缩放（Sheets 的 Symbol 为 36×36dp），不能套用旧的固定 32pt 缩放因子。

Sheets 页面需要背景图时，背景选择器仍放在页面内容层；打开面板后，底部玻璃读取它
下面的实际页面内容，不能新建空白 backdrop 或把面板包进 backdrop 容器。

### Menus / Context Menu

菜单示例使用 sample 宿主的 `Ios27MenuExample`，不要再接入 Material
`DropdownMenu`。它按原稿变体选择布局，并复用页面 `LayerBackdrop` 作为菜单面板的兄弟
采样源：

| 入口 | 面板几何 | 内容 |
| --- | --- | --- |
| `Menus / Type=Edit Medium` | `(16,180)`，`238×44dp`，圆角 34dp | 横向剪切、复制、粘贴和更多操作 |
| `Menus / Type=Menu` | `(16,180)`，`250×338dp`，圆角 34dp | 顶部三项快捷操作、双行项目、子菜单箭头 |
| `Context Menu` | `(16,247.5)`，`250×278dp`，圆角 34dp | 快捷操作、普通/禁用/破坏性项目 |

菜单玻璃沿用组件库的 `drawBackdrop`，包含 vibrancy、轻微 blur、lens、边缘高光和
阴影；不得把菜单自身放进 `LayerBackdrop` 的录制树。普通菜单的分组分隔留白为 21dp；
Context Menu 的玻璃只覆盖第一组项目，后面的 `Section Title`、`Submenu` 和第二组
`Action` 按原稿继续落在页面底色上。菜单外部点击关闭，复制/剪切/粘贴直接走 Android
剪贴板；禁用项保留灰色并不触发回调，子菜单项通过宿主回调报告动作。
后续页面接入时只需要传入 `IphoneExample`、页面 backdrop、主题颜色和 `onAction`，不要
在页面内重新拼面板尺寸或再次使用 Material 下拉菜单。

### Empty States

空状态示例使用 `Ios27EmptyStateExample`，按原稿的 `entry.variant` 保留三种布局：
`Config=Title and Subtitle`、`Config=Symbol in Circle` 和 `Config=Large Symbol`。
标题与说明从对应示例 JSON 的 `Message`/`Description` 节点读取，并用
`SceneGlyphText` 保留原稿字形；符号使用 `SceneGlyphIcon`，不能用普通文字或临时占位图标替代。

宿主坐标以原稿 402pt 页面为基准，内容区域为 330×786dp，左右内距 20dp；三种变体的
按钮统一复用同一组几何：330×52dp，宿主按钮顶部为内容区域 y=635dp。圆形符号为 50dp，Large Symbol
保持原稿 298×49dp 的节点比例。按钮文案和颜色按变体分别为 Create Note/#0088FF、
New Reminder/#FF2D55、Primary Action/#FFCC00。

Large Symbol 原稿没有顶部大标题工具栏，宿主必须隐藏这段额外标题；否则会把整个空状态内容和
底部按钮整体下移，造成三个变体的按钮不在同一底部基准。隐藏后给 Large Symbol 内容宿主补
67dp 原稿基准偏移，按钮仍复用同一个 635dp 位置常量。

按钮仍调用现有 `LiquidButton`，按钮采样源是颜色一致的独立 `CanvasBackdrop`；空状态
页面本身不参与采样，也不修改玻璃组件。点击按钮必须进入宿主已有的新建对话框，保存后切换
为真实列表，列表项仍可点击删除。这样既保留原稿几何，也保留可复用的实际行为和无障碍
`contentDescription`。

Empty States 使用不可滚动页面宿主；外层 `verticalScroll` 只对其他示例启用，避免空状态因安全区
和底部按钮布局产生无意义的上下回弹。

行内玻璃控件自动使用颜色一致的独立 `CanvasBackdrop`。调用者不用为每行搭建背景采样，也不能把行控件改成循环采样父页面。

```kotlin
var value by rememberSaveable { mutableFloatStateOf(0.5f) }
var enabled by rememberSaveable { mutableStateOf(true) }
Ios27PageTheme(darkTheme = dark) {
    Ios27ListSection {
        Ios27SliderRow("音量", value, { value = it },
            onValueChangeFinished = { /* 在此按业务需要保存 */ })
        Ios27SwitchRow("启用", enabled, { enabled = it })
    }
}
```

滑块/开关玻璃主题从页面颜色获取，避免只设置 `Ios27PageTheme` 时误读外层 Material 浅色主题。样例宿主也同步设置 MaterialTheme，使菜单、输入法输入区域和弹窗保持一致。

### List / Type=Full Width

`12740-24224` 使用 `FullWidthListExample`，不要套 `Ios27ListSection`。宿主通过
`Ios27PageContent(horizontalContentPadding = 0.dp)` 取消页面左右 16dp 内容边距，列表背景铺满
页面宽度；行内容仍由 `Ios27ListRow` 保持 16dp 内距，六行固定使用 `TallRowHeight = 68dp`。
`Ios27ListRows` 只在行与行之间绘制分割线，因此首行不会出现多余顶线。

```kotlin
FullWidthListExample(
    selectedAction = selectedAction,
    onRowClick = { label -> dialog = label },
    onActionSelected = { selectedAction = it },
)
```

每一行都使用横向 `swipeable`：向左拖动后，原行内容移动到 `x=-124dp`，右侧浮出三个真实按钮，
使用 `contentDescription = "Action 1..3"`；点选后更新选择状态并收回操作区，点击未展开的行则把
行标签回传宿主。拖动开始后操作区位于行内容层后方，由移动中的行内容作为遮罩，按钮按滑动进度逐步露出；
当前拖动行使用与分组底色合成后的主题 `pressed` 灰色，并隐藏该行上下两条分割线，收回后恢复原色和分割线。页面底色跟随 `LocalIos27PageColors.group`，
浅色和深色都不写死颜色。三个操作按钮使用不透明的灰、蓝、绿纯色填充。此页面没有玻璃控件，不应为列表背景增加 `LayerBackdrop` 或伪造玻璃。

## 页面状态与实际行为

- 滑块拖动改变 Float 状态，释放后报告当前值；开关、步进器直接更新状态。
- 输入框接受手机输入法，能提交、清空或替换；不会安装或切换手机输入法。
- 菜单的复制/粘贴使用 Android 剪贴板；面板保存把编辑内容回传父页面。
- 空状态页能创建内容；点击已有记录可移除；通知能展开、收起、清除和恢复。
- 日期/时间通过系统选择器回传所选值；底栏切换当前页状态并改变展示内容。
- 所有原稿入口仍在目录内；桌面、系统控件、Face ID 等是本地演示，不修改真实系统设置，不冒充真实身份认证。
- `rememberSaveableStateHolder` 按入口 key 保存页面状态，原稿对照往返与配置恢复不会把控件重置。不是持久化业务数据库；进程/任务被完全删除后的长期保存由调用方决定。

顶部渐进模糊、返回玻璃按钮、返回/对照玻璃按钮和浮动底栏位于内容录制层之外；它们采样滚动内容。组件库根页不显示返回按钮；手机示例分类页直接复用 `Toolbars - Top / Type=Large Title`（`12740-24013.json`）的 `ExampleNavBar` 和 402pt 设计密度；List / Type=Inset 保留自己的 `Ios27BackButton` 与 Examples/List 工具栏布局。不得再用文字“‹ 返回”或把返回按钮放进 `LayerBackdrop`。纯色列表内的行内控件采样独立纯色背景；需要透出图片的 Inline 日历读取独立背景兄弟层的 LayerBackdrop。未修改受保护的玻璃组件源码。

## 校验与证据

```powershell
node --test tools/figma-reader/examples.test.mjs tools/figma-reader/native-framework.test.mjs
node tools/verify-extraction.mjs
node tools/generate-ios27-tokens.mjs --check
.\gradlew.bat :verifyLiquidGlassSpec :liquidglass:testDebugUnitTest :sample:assembleDebug --no-daemon --console=plain
node tools/figma-reader/check-native-interactions.mjs emulator-5556
node tools/figma-reader/check-native-pages.mjs emulator-5556
```

最后两个脚本必须串行运行。交互报告、操作后截图和入口冒烟记录写入 `docs/iphone-examples-native/`。报告区分操作是否实际改变状态，以及页面是否只是启动成功；不能拿入口冒烟替代交互或像素验收。

旧 `check-android-examples.mjs` / `check-example-navigation.mjs` 是 1.0.18 默认矢量入口的检查脚本，不能原样用于验证现在的默认框架页面。

仍需逐个对齐各原稿变体的专有布局、完整系统示例交互和 ColorOS 真机效果。系统键盘、日期选择器按 Android 平台实现，不宣称与 iOS 系统控件像素一致。
