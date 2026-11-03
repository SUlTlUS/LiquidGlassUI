# LiquidGlass 框架使用说明

本文说明本项目中 `liquidglass` Android Compose 框架的接入和使用方式，重点覆盖两层 API：

- **页面层**：`dev.liquidglass.ios27`，负责页面主题、列表、按压反馈、常用控件和弹窗外壳。
- **玻璃组件层**：`com.zeaze.tianyinwallpaper`，负责真实 Liquid Glass 的背景采样、折射、模糊、高光、阴影和交互组件。

如果只是开发普通业务页面，优先使用页面层；只有需要自定义玻璃外观时，再直接使用底层 Backdrop API。

---

## 1. 环境要求

当前 library 模块配置：

- Android `minSdk = 24`
- `compileSdk = 37`
- Java / JVM 21
- Jetpack Compose
- `androidx.compose.foundation:foundation:1.11.2`
- `androidx.compose.material:material:1.11.2`
- `io.github.kyant0:backdrop:2.0.1`
- `io.github.kyant0:shapes:1.2.1`

真实玻璃效果主要在 API 33+ 使用。页面层中的部分组件已经提供低版本普通控件降级，直接调用底层玻璃组件时应自行考虑低版本行为。

---

## 2. 接入框架

### 2.1 同仓库模块接入

`settings.gradle.kts` 中包含：

```kotlin
include(":liquidglass")
```

应用模块：

```kotlin
dependencies {
    implementation(project(":liquidglass"))
}
```

然后即可直接导入：

```kotlin
import dev.liquidglass.ios27.*
```

以及底层组件：

```kotlin
import com.zeaze.tianyinwallpaper.catalog.components.*
```

### 2.2 直接使用 AAR

构建：

```powershell
.\gradlew.bat :liquidglass:assembleDebug
```

AAR 输出位于：

```text
liquidglass/build/outputs/aar/
```

如果脱离当前 Gradle 工程直接引入裸 AAR，需要同时保证 Compose、Backdrop 和 Shapes 等依赖存在。

---

## 3. 推荐的页面结构

一个普通页面建议由：

1. `Ios27PageTheme`
2. `Ios27PageContent`
3. `Ios27PageHeader`
4. 一个或多个 `Ios27ListSection`

组成。

示例：

```kotlin
@Composable
fun SettingsPage() {
    Ios27PageTheme {
        Ios27PageContent {
            Ios27PageHeader(
                title = "设置",
                subtitle = "LiquidGlass 示例",
            )

            Ios27ListSection {
                Ios27ListRows(
                    listOf("通用", "显示", "声音")
                ) { item ->
                    Ios27ListRow(
                        title = item,
                        onClick = { /* navigate */ },
                    )
                }
            }
        }
    }
}
```

### 3.1 页面主题

```kotlin
Ios27PageTheme(
    darkTheme = dark,
    surface = Ios27PageSurface.Grouped,
) {
    // page
}
```

主题不会覆盖整个 `MaterialTheme`，只通过 `LocalIos27PageColors` 给本框架页面组件提供颜色。

可读取：

```kotlin
val colors = LocalIos27PageColors.current

colors.page
colors.group
colors.label
colors.secondaryLabel
colors.separator
colors.pressed
colors.accent
```

特殊 Slider 原稿页可以使用：

```kotlin
Ios27PageTheme(
    surface = Ios27PageSurface.SliderReference,
) {
    // ...
}
```

普通业务页面不要使用这个特殊 surface。

### 3.2 页面底色与分组颜色

普通页面必须使用 `Ios27PageTheme` 提供的页面颜色，不要在业务页面里重新写一套深浅色常量。

| 页面类型 | 模式 | 页面底色 `colors.page` | 分组底色 `colors.group` |
| --- | --- | --- | --- |
| 有列表 / 分组 | 浅色 | `#F2F2F7` | `#FFFFFF` |
| 无列表普通页面 | 浅色 | `#FFFFFF` | `#FFFFFF` |
| 任意普通页面 | 深色 | `#000000` | `#1C1C1E` |

对应 Token：

```kotlin
Ios27Tokens.PageLight
Ios27Tokens.GroupLight
Ios27Tokens.PageDark
Ios27Tokens.GroupedSurfaceDark
```

有列表页面使用默认 `Grouped`：

```kotlin
Ios27PageTheme(
    darkTheme = dark,
    surface = Ios27PageSurface.Grouped,
) {
    // 列表 / 分组页面
}
```

没有列表的普通页面使用 `Plain`：

```kotlin
Ios27PageTheme(
    darkTheme = dark,
    surface = Ios27PageSurface.Plain,
) {
    // 浅色页面背景为 #FFFFFF
    // 深色页面背景仍为 #000000
}
```

标准页面应直接从主题读取：

```kotlin
Ios27PageTheme(darkTheme = dark) {
    val colors = LocalIos27PageColors.current

    Box(
        Modifier
            .fillMaxSize()
            .background(colors.page)
    ) {
        Ios27PageContent {
            Ios27ListSection {
                // Ios27ListSection 会使用 colors.group
            }
        }
    }
}
```

不要在页面里写：

```kotlin
Color.White
Color.Black
Color(0xFFF2F2F7)
```

来替代框架主题色。只有明确属于业务内容本身的颜色才应单独声明。

### 3.3 页面统一使用页面框架

应用级页面不要直接从裸 `Column` / `LazyColumn` 开始拼布局。推荐统一结构：

```text
Ios27PageTheme
└─ 页面根 Box（colors.page）
   ├─ 被录制的页面内容层
   │  └─ Ios27PageContent
   │     ├─ Ios27PageHeader
   │     └─ Ios27ListSection / 页面内容
   ├─ 顶部 Top Toolbar（内部/下层配合 ProgressiveBlurContent）
   └─ 底部 Tab Bar / 其他浮动玻璃导航
```

其中：

- 页面内容负责滚动。
- 顶部 Top Toolbar（及其渐进模糊）和底部 Tab Bar 必须作为页面内容录制层的**兄弟层**。
- 玻璃导航不能被录进自己要采样的 `LayerBackdrop`。
- 页面安全区使用 `WindowInsets.safeDrawing`，不要复制固定状态栏高度。

sample 中的标准实现可直接参考：

```text
sample/src/main/java/dev/liquidglass/sample/ExamplePageScaffold.kt
sample/src/main/java/dev/liquidglass/sample/MainActivity.kt
```

### 3.4 顶部优先使用 Top Toolbar

页面顶部如果存在标题、返回、右侧操作或导航语义，**优先使用现成的 Top Toolbar（`Toolbars - Top`）**，不要自己用 `Row + Text + IconButton` 拼一套顶部栏。

推荐关系：

```text
ProgressiveBlurContent
        ↓
Top Toolbar / ExampleToolbarDesignPage
```

二者**可以同时使用，而且普通可滚动页面通常推荐同时使用**：渐进模糊负责顶部内容过渡，Top Toolbar 负责标题、返回和操作按钮。只有确实不需要 Toolbar 或不需要顶部模糊的特殊页面，才单独使用其中一个。

sample 页面优先使用：

```kotlin
ExampleToolbarDesignPage(
    background = colors.page,
    isLightTheme = !dark,
    toolbarFile = "12740-24029.json",
    onBack = onBack,
) { topInset, backdrop ->
    // 页面内容
}
```

`ExampleToolbarDesignPage` 已经统一处理并同时使用：

- 页面录制层
- 顶部渐进模糊
- Top Toolbar
- `safeDrawing` 顶部安全区
- Toolbar 与被采样页面内容的兄弟层关系
- Backdrop 防递归

如果页面已经自行管理录制层，则复用：

```kotlin
ExampleTopChrome(
    backdrop = backdrop,
    isLightTheme = !dark,
    toolbarFile = "12740-24029.json",
    onBack = onBack,
    fallbackTitle = "标题",
)
```

其中 `ExampleTopChrome` 会先绘制渐进模糊，再绘制 Top Toolbar；这就是推荐的标准顶部组合，而不是二选一。

组件示例入口为：

```kotlin
Ios27Examples.TopToolbar(
    variant = Ios27TopToolbarVariant.Default,
    onBack = onBack,
)
```

现有 Top Toolbar 变体包括：

```text
Title2LineLeft
Title2Line
LargeTitle
CompactLargeTitle
Default
```

业务页面应根据标题层级选择最接近的现成变体，**不要因为标题文字不同就重新实现 Toolbar**。

#### 单独使用渐进模糊

渐进模糊可以和 Top Toolbar 同时使用；如果页面顶部没有标题、返回按钮和操作按钮，也可以单独使用：

```kotlin
import com.zeaze.tianyinwallpaper.ui.commom.ProgressiveBlurContent
```

基本调用：

```kotlin
ProgressiveBlurContent(
    modifier = Modifier
        .align(Alignment.TopCenter)
        .fillMaxWidth(),
    backdrop = backdrop,
    isLightTheme = !dark,
)
```

当前 `ProgressiveBlurContent` 高度为 `128.dp`，顶部 blur 较强，向内容方向逐渐衰减，不应改成一条硬边半透明矩形。

推荐随滚动逐渐显示：

```kotlin
val scrollState = rememberScrollState()
val revealDistance = with(LocalDensity.current) { 48.dp.toPx() }
val blurAlpha = (scrollState.value / revealDistance).coerceIn(0f, 1f)

ProgressiveBlurContent(
    modifier = Modifier
        .align(Alignment.TopCenter)
        .fillMaxWidth()
        .alpha(blurAlpha),
    backdrop = backdrop,
    isLightTheme = !dark,
)
```

如果不能直接使用 `ExampleToolbarDesignPage`，则优先复用 `ExampleTopChrome`；它已经保证渐进模糊和 Top Toolbar 都位于页面录制层之外：

```kotlin
ExampleTopChrome(
    backdrop = backdrop,
    isLightTheme = !dark,
    blurAlpha = blurAlpha,
)
```

注意：`ExampleToolbarDesignPage`、`ExampleTopChrome` 和 `Ios27Examples.TopToolbar` 当前属于 sample 宿主/示例适配层。独立应用接入 library 时，应复用同样的 Top Toolbar 结构；确实没有 Toolbar 的页面才直接使用 `ProgressiveBlurContent`。

### 3.5 主页统一使用 Tab Bar

应用主页的一级页面切换使用 LiquidGlass Tab Bar，不使用普通 `BottomNavigation`，也不要手绘胶囊底栏。

当前 sample 主页使用：

```kotlin
Ios27TabBarExample(
    file = "12740-23997.json",
    selectedTabIndex = tab,
    onTabSelected = { tab = it },
    backdrop = if (Build.VERSION.SDK_INT >= 33) backdrop else null,
    isLightTheme = !dark,
    splitSearch = false,
    labels = listOf("组件", "说明", "设置"),
)
```

`Ios27TabBarExample` 是 sample 层适配器，内部调用框架的：

```kotlin
LiquidBottomTabsRegular(...)
```

如果主页还需要独立搜索按钮，则使用：

```kotlin
LiquidBottomTabsSplitSearch(...)
```

主页 Tab Bar 必须：

- 固定在页面内容之上的浮动层。
- 与页面内容使用同一个 `LayerBackdrop`，但自身不能被录进该 backdrop。
- 使用 `colors.accent` 表示当前选中项。
- 使用 `colors.secondaryLabel` 表示未选中项。
- 页面内容底部预留足够空间，避免被 Tab Bar 遮挡；当前 sample 使用约 `108.dp` 的底部内容留白。
- 遵守系统底部安全区，不写死导航栏高度。

推荐主页层级：

```text
Root Box
├─ LayerBackdrop 页面内容
│  └─ 当前 Tab 页面
├─ Top Toolbar / ProgressiveBlurContent
└─ LiquidBottomTabsRegular
```

这样 Top Toolbar、顶部渐进模糊和底部 Tab Bar 都采样同一份真实页面内容，同时不会形成递归采样。

---

## 4. 列表和分组

### 4.1 分组列表

```kotlin
Ios27ListSection {
    Ios27ListRows(items) { item ->
        Ios27ListRow(
            title = item.title,
            subtitle = item.subtitle,
            detail = item.value,
            onClick = { onItemClick(item) },
        )
    }
}
```

`Ios27ListRows` 会自动：

- 只在相邻行之间画分隔线
- 避免首行顶部出现多余分割线
- 为首行、末行生成正确的按压遮罩圆角
- 保持整组连续，而不是每一行都变成独立胶囊

不要手工给每行都套 `RoundedCornerShape`。

### 4.2 带标题和说明的分组

```kotlin
Ios27ListSection(
    title = "网络",
    footer = "更改网络设置可能影响连接。",
) {
    // rows
}
```

标题强度：

```kotlin
Ios27SectionEmphasis.Nested
Ios27SectionEmphasis.Prominent
Ios27SectionEmphasis.ExtraProminent
```

### 4.3 列表行

```kotlin
Ios27ListRow(
    title = "Wi-Fi",
    subtitle = "当前已连接",
    detail = "Home",
    leading = {
        Ios27IconTile(color = Color.Blue) {
            // icon
        }
    },
    onClick = { /* open */ },
)
```

只要提供 `onClick`，默认就会显示 disclosure indicator。

不需要箭头时：

```kotlin
Ios27ListRow(
    title = "版本",
    detail = "1.0",
    disclosure = false,
)
```

---

## 5. 普通控件

页面层已经对常见控件进行了封装。业务页面优先使用这些接口，不必直接处理 Backdrop。

### 5.1 Slider

```kotlin
var value by rememberSaveable { mutableFloatStateOf(0.5f) }

Ios27SliderRow(
    label = "音量",
    value = value,
    onValueChange = { value = it },
    onValueChangeFinished = {
        // 按业务需要持久化
    },
)
```

可选：

```kotlin
Ios27SliderRow(
    label = "亮度",
    value = value,
    onValueChange = { value = it },
    showLabel = false,
    ticks = true,
    leading = { /* icon */ },
    trailing = { /* icon */ },
)
```

### 5.2 Switch

```kotlin
var enabled by rememberSaveable { mutableStateOf(true) }

Ios27SwitchRow(
    label = "启用",
    checked = enabled,
    onCheckedChange = { enabled = it },
)
```

### 5.3 Stepper

```kotlin
var count by rememberSaveable { mutableIntStateOf(0) }

Ios27StepperRow(
    label = "数量",
    value = count,
    onValueChange = { count = it },
    range = 0..10,
)
```

### 5.4 普通操作按钮

```kotlin
Ios27ActionButton(
    label = "继续",
    onClick = { /* action */ },
)
```

API 33+ 使用 `LiquidButton`，低版本自动降级到普通按钮。

---

## 6. 原始玻璃组件

需要更自由的布局时，可以直接调用底层组件。

### 6.1 LiquidButton

```kotlin
LiquidButton(
    onClick = { /* action */ },
    backdrop = backdrop,
) {
    Text("确定")
}
```

常用参数：

```kotlin
LiquidButton(
    onClick = {},
    backdrop = backdrop,
    tint = Color.Unspecified,
    surfaceColor = Color.Unspecified,
    isInteractive = true,
    enableMetaballDrag = false,
    buttonHeight = 44.dp,
) {
    Text("Button")
}
```

`enableMetaballDrag` 是额外的拖拽 / metaball 行为，普通按钮不要默认打开。

### 6.2 LiquidSlider

```kotlin
LiquidSlider(
    value = { value },
    onValueChange = { value = it },
    valueRange = 0f..1f,
    visibilityThreshold = 0.001f,
    backdrop = backdrop,
    isLightTheme = !dark,
    dragWholeTrack = true,
    onValueChangeFinished = {
        // finished
    },
)
```

### 6.3 LiquidToggle

```kotlin
LiquidToggle(
    selected = { checked },
    onSelect = { checked = it },
    backdrop = backdrop,
    isLightTheme = !dark,
)
```

### 6.4 Bottom Tabs

基础入口：

```kotlin
LiquidBottomTabs(
    selectedTabIndex = { selectedIndex },
    onTabSelected = { selectedIndex = it },
    backdrop = backdrop,
    tabsCount = 3,
    isLightTheme = !dark,
) {
    // LiquidBottomTab(...)
}
```

项目还提供：

- `LiquidBottomTabsCompact`
- `LiquidBottomTabsRegular`
- `LiquidBottomTabsSplitSearch`
- `LiquidBottomTab`

新页面优先使用对应布局入口，不要自己复制 Tab 指示器的几何计算。

#### 酷安参数

[酷安参数](coolapk-parameters.md)记录酷安 16.6.4 的 TabBar 几何、底色、折射和阴影配置，并提供 `LiquidBottomTabsStyle` 的字段映射与部分接入示例。它是可选的参考配置；框架默认值不变。尚未暴露的参数在说明中单独列出。

---

## 7. Backdrop：玻璃效果的核心

Liquid Glass 不是一张半透明白色卡片。框架会从指定的 `Backdrop` 中采样真实背景，再进行 blur、lens、vibrancy、高光和阴影处理。

Backdrop 的选取是否正确，直接决定玻璃是否正常。

---

## 8. CanvasBackdrop：纯色或可自行绘制的背景

如果玻璃后面本来就是稳定纯色，不需要录制真实页面，使用：

```kotlin
val backdrop = rememberCanvasBackdrop {
    drawRect(colors.group)
}
```

适合：

- 列表内的 Slider
- Switch
- 独立按钮
- 纯色面板里的小组件

这是最安全、成本最低的方式。

不要为了一个纯色列表按钮去录制整个页面。

---

## 9. LayerBackdrop：采样真实页面

如果玻璃必须透出：

- 壁纸
- 图片
- 滚动内容
- 页面真实背景

使用 `LayerBackdrop`。

### 9.1 正确结构

```kotlin
@Composable
fun GlassPage() {
    val backdrop = rememberLayerBackdrop {
        drawContent()
    }

    Box(Modifier.fillMaxSize()) {
        // 被采样的背景层
        Box(
            Modifier
                .fillMaxSize()
                .layerBackdrop(backdrop)
        ) {
            BackgroundContent()
        }

        // 玻璃必须是兄弟层
        LiquidButton(
            onClick = {},
            backdrop = backdrop,
            modifier = Modifier.align(Alignment.BottomCenter),
        ) {
            Text("Glass")
        }
    }
}
```

核心原则：

> **被采样内容和玻璃本身必须分开。**

### 9.2 严禁递归采样

错误示例：

```kotlin
Box(
    Modifier
        .fillMaxSize()
        .layerBackdrop(backdrop)
) {
    BackgroundContent()

    // 错误：这个玻璃自己也被录进 backdrop
    LiquidButton(
        backdrop = backdrop,
        onClick = {},
    ) {
        Text("Bad")
    }
}
```

这样可能形成：

```text
Backdrop 录制
  -> 绘制 LiquidGlass
      -> LiquidGlass 再读取 Backdrop
          -> 再次录制同一棵树
```

结果可能是：

- RenderThread / Skia 递归
- `RenderNode.beginRecording` 异常
- 黑屏
- 闪烁
- 崩溃

---

## 10. layerBackdrop 的 Modifier 顺序

`layerBackdrop` 只会录制它内部后续 Modifier / Content 绘制出的内容。

因此下面这种顺序可能让绘制内容落在录制层之外：

```kotlin
Modifier
    .drawBehind { /* wallpaper */ }
    .layerBackdrop(backdrop)
```

页面背景应优先采用：

```kotlin
Modifier
    .layerBackdrop(backdrop)
    .drawBehind {
        // wallpaper / page background
    }
```

sample 中可直接参考：

```text
sample/src/main/java/dev/liquidglass/sample/ExamplePageScaffold.kt
```

---

## 11. 自定义玻璃表面

需要自己做玻璃卡片时，可以直接使用 `drawBackdrop`：

```kotlin
Box(
    Modifier
        .size(280.dp, 160.dp)
        .drawBackdrop(
            backdrop = backdrop,
            shape = {
                RoundedRectangle(34.dp)
            },
            effects = {
                colorControls(
                    brightness = 0.2f,
                    saturation = 1.5f,
                )
                blur(16.dp.toPx())
                lens(
                    24.dp.toPx(),
                    48.dp.toPx(),
                    depthEffect = true,
                )
            },
            highlight = {
                Highlight.Default
            },
            shadow = {
                Shadow(
                    radius = 48.dp,
                    offset = DpOffset(0.dp, 8.dp),
                    color = Color.Black.copy(alpha = 0.25f),
                )
            },
            onDrawSurface = {
                drawRect(Color.White.copy(alpha = 0.70f))
            },
        )
)
```

只有确实需要定制时才这样写。普通弹窗应优先复用 `LiquidDialog` / `liquidDialogSurface`。

---

## 12. LiquidDialog

统一弹窗入口：

```kotlin
LiquidDialog(
    backdrop = backdrop,
    onDismissRequest = { showDialog = false },
    width = 302.dp,
    height = 300.dp,
    cornerRadius = 34.dp,
    surfaceColor = Color.White.copy(alpha = 0.74f),
    scrimColor = Color.Black.copy(alpha = 0.20f),
    dark = dark,
) {
    // content
}
```

默认结构：

```text
Root
├─ Scrim
└─ Glass Panel
   └─ Content
```

弹窗尺寸、业务内容和状态由调用者提供；玻璃、边缘高光和阴影统一由框架维护。

### 12.1 LiquidDialogButton

```kotlin
LiquidDialogButton(
    text = "确定",
    bgColor = colors.accent,
    textColor = Color.White,
    onClick = onConfirm,
)
```

---

## 13. 自定义弹窗表面

已经有自己的弹窗布局，只想复用玻璃材质：

```kotlin
Modifier.liquidDialogSurface(
    backdrop = backdrop,
    shape = RoundedCornerShape(34.dp),
    surface = Color.White.copy(alpha = 0.70f),
    dark = dark,
)
```

可控制：

- `saturation`
- `surfaceOverlay`
- `surfaceOverlayBlendMode`
- `exportedBackdrop`
- `depthEffect`
- `showHighlight`
- `showShadow`

如果动画过程中必须独立处理阴影：

```kotlin
Modifier.liquidDialogShadow(
    shape = RoundedCornerShape(34.dp),
    alpha = progress,
)
```

注意：阴影和裁剪的顺序非常重要。

---

## 14. 阴影与裁剪

普通静态玻璃：

```text
drawBackdrop
└─ shadow
└─ glass
```

如果需要外部阴影，不要先：

```kotlin
Modifier
    .clip(shape)
    .drawBackdrop(...)
```

因为 clip 会把外扩阴影裁掉。

对于带尺寸动画的 Context Menu / Popup，推荐拆层：

```text
Box
├─ Shadow sibling
└─ Clipped glass sibling
   └─ Content
```

不要让：

- `ShadowElement`
- `drawBackdrop`
- 动画 `graphicsLayer`

全部塞在同一条绘制链里再切换结构，否则可能出现：

- 方形背景残影
- 动画最后一帧闪一下
- 动画过程中阴影消失
- backdrop 采样尺寸与视觉缩放尺寸不同步

---

## 15. Pressed Feedback

普通非玻璃控件使用：

```kotlin
Modifier.ios27PressedClickable(
    shape = RoundedCornerShape(12.dp),
    onClick = { /* action */ },
)
```

它会：

- 去掉 Android ripple
- 使用统一 pressed overlay
- 直接处理 PressInteraction：按下立即显示，松开 120ms 淡出，滑动取消立即清除；短按也能显示反馈
- 将反馈裁剪到传入 shape
- 保持调用方自己的 hit target

2026-10-07 按压反馈修正：`Ios27PressFeedback.kt` 改为由交互事件驱动遮罩透明度，避免同帧按下、松开被布尔状态合并。点击后控件转为禁用时应保留该 modifier、更新 `enabled`，让松开反馈完成。

玻璃控件本身已有 InteractiveHighlight，不要再额外套普通 pressed mask。

---

## 16. QWEA0 局部浮层动画

QWEA0 负责页面内部的局部浮层动画；Miuix 负责页面级导航和预测性返回。两套动画职责不同，可以同时存在。

推荐职责划分：

```text
页面进入 / 页面返回 / 预测性返回
    → Miuix NavDisplay + NavTransitions.MiuixDefault

Dialog / Context Menu / Picker / Action Sheet / Popup
    → QWEA0 风格的局部浮层动画
```

不要用 QWEA0 去替代整页导航，也不要用 Miuix 页面 transition 去做一个局部 Popup 的展开动画。

### 16.1 QWEA0 Dialog 出现 / 消失

项目参考 QWEA0 `LiquidGlassDialogBuilder` 的真实动画参数：

打开：

```text
alpha: 0 -> 1
scale: 0.86 -> 1.0
duration: 250 ms
DecelerateInterpolator(1.6)
```

关闭：

```text
alpha: 1 -> 0
scale: 1.0 -> 0.88
duration: 150 ms
AccelerateInterpolator(1.4)
```

QWEA0 对应实现入口包括：

```text
LiquidGlassDialogBuilder
├─ animateShow
├─ runEnterAnimation()
└─ dismissAnimated()
```

适合直接采用这套 show / dismiss 动画的组件：

- 普通 Dialog
- Alert
- Context Menu
- 不需要从某个具体控件形变展开的 Popup

Context Menu 当前即采用这套参数，但玻璃本体仍然使用项目自己的 Backdrop / LiquidGlass 表面，不因为动画来源是 QWEA0 就替换材质实现。

### 16.2 QWEA0 Shape Morph 动画

如果浮层是从一个明确的触发控件展开，例如：

- 时间胶囊 → Time Picker
- 月份标题 → Month / Year Picker
- 右上角按钮 → Action Sheet
- 小型控件 → 大型玻璃面板

优先使用 shape morph，而不是简单 fade + scale。

标准 morph 属性包括：

```text
start bounds
    x / y / width / height
    corner radius

        ↓ animation progress

end bounds
    x / y / width / height
    corner radius
```

也就是说动画过程中同时插值：

```kotlin
left
 top
 width
 height
 cornerRadius
```

而不是只做：

```kotlin
graphicsLayer {
    scaleX = progress
    scaleY = progress
}
```

触发位置必须来自真实点击控件的测量结果，例如：

```kotlin
Modifier.onGloballyPositioned { coordinates ->
    val position = coordinates.positionInRoot()
    val size = coordinates.size
    // 保存真实 start bounds
}
```

打开时从该 bounds 展开到最终面板；关闭时反向回到同一 bounds。

当前项目可参考：

```text
sample/src/main/java/dev/liquidglass/sample/Ios27PickerExample.kt
sample/src/main/java/dev/liquidglass/sample/Ios27ActionSheetExample.kt
```

Picker 当前从真实点击胶囊/月份标题展开，Action Sheet 从右上角触发按钮展开。

### 16.3 QWEA0 Morph 与 Backdrop 的组合规则

QWEA0 负责“几何怎么变”，项目 Backdrop 负责“玻璃怎么画”。两者不要混成同一职责。

推荐结构：

```text
Animatable progress
    ↓
真实 x / y / width / height / radius
    ↓
LiquidGlass / drawBackdrop
    ↓
内容
```

动画时尽量让玻璃每一帧都拿到真实当前尺寸，避免 Backdrop 仍以最终大矩形采样、视觉上却只做 GPU 缩放。

对于复杂 Popup，建议：

1. 外壳真实改变位置和尺寸。
2. shape 半径跟随 progress 插值。
3. 玻璃材质继续使用原来的 `drawBackdrop` / `liquidDialogSurface`。
4. 内容可以延迟淡入，但窗口本体不要在中途切另一套实现。
5. 关闭时使用完全反向的几何路径。

### 16.4 Tab Bar 页面切换动画

主页 Tab Bar 切换一级页面时，页面内容过渡跟随 QWEA0 `LiquidGlassTabBar.animateDropletTo()` 的真实 settle 节奏。

QWEA0 v2.0.10 的实际实现为：

```text
ValueAnimator: 380 ms
OvershootInterpolator: tension = 1.1
```

项目保留现有 Compose `LiquidBottomTabsRegular` 外观，不直接替换为 QWEA0 `LiquidGlassTabBar`，因为后者使用 Android `Drawable + TextView` 的 TabItem 布局，会破坏当前 iOS27 Tab Bar 的槽位、图标和间距。

页面切换统一通过：

```kotlin
QweaTabPageTransition(
    selectedTabIndex = tab,
) { page ->
    // 当前一级页面
}
```

行为：

- 向更大的 Tab index 切换时，新页从右侧进入。
- 向更小的 Tab index 切换时，新页从左侧进入。
- 位移和轻微 scale 使用 QWEA0 TabBar 的 380ms overshoot settle 曲线。
- 页面过渡位于唯一的页面 `LayerBackdrop` 内。
- Tab Bar 本身保持在录制层外，继续采样真实动画中的页面。
- 不允许给新旧页分别挂同一个 `LayerBackdrop`，否则过渡期间会同时写同一个录制源。

当前实现参考：

```text
sample/src/main/java/dev/liquidglass/sample/Ios27TabBar.kt
sample/src/main/java/dev/liquidglass/sample/MainActivity.kt
```

### 16.5 Backdrop 元素不要只做 GPU scale

对普通 View / 非 backdrop 内容：

```kotlin
Modifier.graphicsLayer {
    scaleX = scale
    scaleY = scale
}
```

通常没有问题。

但 `drawBackdrop` 元素在复杂缩放动画中，布局尺寸仍可能保持原值，采样层与视觉尺寸不一致。

这类组件更稳的方式是：

1. 动画真实 `width / height / x / y`
2. 让 backdrop 每一帧都拿到真实当前尺寸
3. 内容如果需要保持整体缩放感，再在内部单独 `graphicsLayer(scale)`
4. 阴影使用独立兄弟层

Context Menu 当前实现可作为参考：

```text
sample/src/main/java/dev/liquidglass/sample/Ios27MenuExamples.kt
```

### 16.6 不要在最后一帧切换绘制结构

避免：

```kotlin
if (progress < 0.999f) {
    AnimatedGlass()
} else {
    StaticGlass()
}
```

即使两个视觉参数完全一样，最后一帧重新创建：

- shadow layer
- drawBackdrop
- clip
- graphicsLayer

仍可能产生一帧闪烁。

应让动画开始、结束和静止状态尽量使用**同一棵 Compose 节点结构**，只改变数值。

---

## 17. Context Menu 的推荐层级

当前 Context Menu 已采用：

```text
Page
├─ dismiss hit target
├─ shadow sibling
└─ clipped glass panel
   └─ scaled content
```

行为：

- 长按 Context Area 触发
- `HapticFeedbackType.LongPress`
- Content Area 有按压缩放
- 菜单使用 QWEA0 Dialog 风格的 show / hide 参数
- 退出动画结束后才卸载
- 阴影和 backdrop 完全分层
- 静止态和动画态不切换节点结构

新增 Context Menu 时建议复用这套模式，而不是重新使用 Material `DropdownMenu`。

---

## 18. 日期 / 时间 Picker

sample 已提供自定义 Picker 示例：

```text
sample/src/main/java/dev/liquidglass/sample/Ios27PickerExample.kt
```

特点：

- 年 / 月使用 `WheelPicker`
- 时 / 分使用 `WheelPicker`
- 确定后再提交值
- 取消不修改业务状态
- 弹窗从真实点击区域 bounds 展开
- 关闭时反向回到点击遮罩
- 最终圆角使用统一 `34dp`

`WheelPicker` 当前位于：

```text
sample/src/main/java/com/zeaze/tianyinwallpaper/catalog/components/WheelPicker.kt
```

如果后续需要把它正式变成 library 公共 API，应先迁入 `liquidglass` 模块，不要从其他项目复制多个版本。

---

## 19. QWEA0 与本框架的关系

sample 当前还依赖：

```kotlin
implementation("com.github.QWEA0:liquidglass:v2.0.10")
```

它在当前 sample 中主要承担局部浮层动画参考/实现来源：

- `LiquidGlassDialogBuilder` 的 Dialog show / dismiss 动画
- shape morph
- `LiquidGlassView` 相关实验能力

但 **QWEA0 不是当前 `liquidglass` library 的基础依赖**，也不是页面材质层的替代品。

框架职责保持分离：

```text
项目 LiquidGlass / Backdrop
    → 玻璃材质、模糊、折射、高光、阴影

QWEA0
    → 局部浮层的 show / dismiss / shape morph 动画

Miuix
    → 页面级导航与预测性返回
```

普通页面本身不需要为了显示玻璃而额外接入 QWEA0。只有需要 QWEA0 局部浮层动画能力的宿主模块才需要对应依赖。

尤其不要为了加“出现动画”就替换原有窗口材质。正确做法应是：

> 保持原窗口本体和 Backdrop 材质不变，只用 QWEA0 驱动窗口几何或 presentation 动画。

---

## 20. API 33 以下

页面层中的部分控件已有降级：

- `Ios27SliderRow` -> Material Slider
- `Ios27SwitchRow` -> Material Switch
- `Ios27ActionButton` -> TextButton
- 弹窗 / 面板应保留同样几何并使用普通半透明 surface

如果直接使用底层 `drawBackdrop` / Liquid 组件，请自行判断：

```kotlin
if (Build.VERSION.SDK_INT >= 33) {
    // glass
} else {
    // fallback
}
```

不要让低版本因为追求玻璃效果而失去可用性。

---

## 21. Tokens

所有 iOS27 页面级尺寸、颜色和文字样式统一从：

```text
dev.liquidglass.ios27.Ios27Tokens
```

读取。

例如：

```kotlin
Ios27Tokens.GroupRadius
Ios27Tokens.GroupGap
Ios27Tokens.RowHeight
Ios27Tokens.RowPadding
Ios27Tokens.Type.Body
Ios27Tokens.Type.Title2
```

`Ios27Tokens.kt` 是生成文件，不应手工修改。

生成 / 检查：

```powershell
node tools/generate-ios27-tokens.mjs --check
```

---

## 22. Insets 和设备圆角

不要把 iPhone 截图里的状态栏高度、底部安全区直接写死到 Android。

页面安全区由宿主读取：

```kotlin
WindowInsets.safeDrawing
```

需要使用设备真实圆角的 Modal / Sheet，也应从 Android 设备信息获得，不要复制某台手机的固定值。

---

## 23. 状态管理

框架组件遵循 Compose 单向数据流。

推荐：

```kotlin
var checked by rememberSaveable { mutableStateOf(false) }

Ios27SwitchRow(
    label = "Enabled",
    checked = checked,
    onCheckedChange = { checked = it },
)
```

弹窗临时状态建议分为：

```text
committed state
pending state
visible / mounted state
```

例如：

```kotlin
var value by rememberSaveable { mutableStateOf(0) }
var pending by rememberSaveable { mutableStateOf(value) }

onConfirm = {
    value = pending
    close()
}

onCancel = {
    close()
}
```

这样取消不会污染业务值。

---

## 24. 导航与 Miuix 预测性返回

二级页面和多级页面导航统一优先使用 Miuix Navigation，不使用普通 `BackHandler` + 立即 `pop` 来替代预测性返回动画。

当前 sample 依赖：

```kotlin
implementation("top.yukonga.miuix.kmp:miuix-nav-android:0.9.4-rc01")
```

页面级导航使用：

```kotlin
val backStack = rememberNavBackStack<AppRoute>(AppRoute.Root)

NavDisplay(
    backStack = backStack,
    modifier = Modifier.fillMaxSize(),
    onBack = {
        if (backStack.size > 1) {
            backStack.removeAt(backStack.lastIndex)
        }
    },
    transition = NavTransitions.MiuixDefault,
) {
    entry<AppRoute.Root> {
        RootPage(
            onOpenDetail = {
                backStack.add(AppRoute.Detail)
            },
        )
    }

    entry<AppRoute.Detail> {
        DetailPage()
    }
}
```

关键点是：

```kotlin
transition = NavTransitions.MiuixDefault
```

页面进入、普通返回和系统预测性返回都由同一套 Miuix 导航状态管理，不要再额外叠一套自定义页面位移动画。

### 24.1 浮层优先拦截返回

页面内如果存在：

- Dialog
- Sheet
- Action Sheet
- Color Picker
- Menu / Context Menu
- Activity View
- 其他临时浮层

返回手势应该先关闭最上层浮层，而不是直接退出当前页面。

使用：

```kotlin
import top.yukonga.miuix.kmp.nav.gesture.PredictiveBackHandler as MiuixPredictiveBackHandler

MiuixPredictiveBackHandler(
    enabled = dialogVisible,
    onProgress = { events ->
        events.collect {
            // 如无特殊视觉需求，可以仅消费进度。
        }
    },
    onCommit = {
        dialogVisible = false
    },
    onCancel = {
        // 手势取消，不改变状态。
    },
)
```

当前项目中的 `NativeExamplePage` 已采用这种模式：先计算当前最上层的 `modalBackAction`，只要浮层还在，就由 Miuix PredictiveBackHandler 消费返回；浮层关闭后，返回事件才继续交给页面级 `NavDisplay`。

推荐顺序：

```text
系统返回手势
    ↓
当前是否有 Dialog / Sheet / Menu 等浮层？
    ├─ 有 → MiuixPredictiveBackHandler 关闭最上层浮层
    └─ 无 → NavDisplay + NavTransitions.MiuixDefault 返回上一页
```

### 24.2 不要在 onProgress 里直接 pop 页面

错误做法：

```kotlin
onProgress = {
    backStack.removeLast()
}
```

预测性返回的 `onProgress` 会连续收到手势进度；在这里修改导航栈会导致：

- 页面提前消失
- 动画被打断
- 返回取消后无法恢复
- 多级导航连续 pop

页面栈只应在 Miuix 导航的返回提交阶段改变；临时浮层也只在 `onCommit` 关闭。

### 24.3 不要同时叠加多套返回动画

如果页面已经使用：

```kotlin
NavTransitions.MiuixDefault
```

就不要再同时给整页增加：

```kotlin
graphicsLayer { translationX = ... }
AnimatedContent(...)
AnimatedVisibility(...)
```

来模拟另一套返回过渡。否则很容易出现：

- 页面位移两次
- 前一页短暂变黑
- 返回动画结束时跳帧
- 手势取消位置不一致

页面级 transition 交给 Miuix；组件内部自己的展开、收缩和 Dialog 动画仍可以独立存在。

### 24.4 当前项目参考入口

```text
sample/src/main/java/dev/liquidglass/sample/IphoneExamples.kt
```

这里使用：

```kotlin
rememberNavBackStack
NavDisplay
NavTransitions.MiuixDefault
```

页面/组件浮层返回参考：

```text
sample/src/main/java/dev/liquidglass/sample/NativeExamplePage.kt
sample/src/main/java/dev/liquidglass/sample/Ios27ExampleComponents.kt
```

其中使用：

```kotlin
MiuixPredictiveBackHandler
```

因此新页面的默认导航组合应理解为：

```text
Ios27PageTheme
+ Top Toolbar + ProgressiveBlurContent
+ Miuix NavDisplay
+ Miuix Predictive Back
+ QWEA0 局部浮层动画
+ Bottom Tab Bar（仅主页一级导航）
```

---

## 25. 无障碍

框架已有的列表和交互封装会尽量提供：

- `Role.Button`
- `Role.Switch`
- `contentDescription`
- `stateDescription`
- Slider 的 `progressBarRangeInfo`
- 标题 `heading()`

自定义组件时不要为了像素布局移除这些语义。

图片或只有 Symbol 的按钮尤其要提供 `contentDescription`。

---

## 26. 推荐的源码参考入口

| 需求 | 参考文件 |
| --- | --- |
| 页面 / 列表 | `dev/liquidglass/ios27/Ios27Lists.kt` |
| 页面主题 | `dev/liquidglass/ios27/Ios27PageTheme.kt` |
| Tokens | `dev/liquidglass/ios27/Ios27Tokens.kt` |
| 按压反馈 | `dev/liquidglass/ios27/Ios27PressFeedback.kt` |
| 常用交互控件 | `dev/liquidglass/ios27/Ios27InteractiveControls.kt` |
| Dialog | `dev/liquidglass/ios27/LiquidDialog.kt` |
| Button | `catalog/components/LiquidButton.kt` |
| Slider | `catalog/components/LiquidSlider.kt` |
| Toggle | `catalog/components/LiquidToggle.kt` |
| Tab Bar | `catalog/components/LiquidBottomTabs.kt` |
| 页面 Backdrop 宿主 | `sample/.../ExamplePageScaffold.kt` |
| Menu / Context Menu | `sample/.../Ios27MenuExamples.kt` |
| Picker | `sample/.../Ios27PickerExample.kt` |
| Sheets | `sample/.../Ios27SheetsModal.kt` |

完整路径前缀分别为：

```text
liquidglass/src/main/java/
sample/src/main/java/dev/liquidglass/sample/
```

---

## 27. 常见问题

### 26.1 玻璃看起来像纯白卡片

优先检查：

1. Backdrop 是否真的包含目标背景
2. `layerBackdrop` 是否挂在正确的背景兄弟层
3. 玻璃是否错误地录进了自己的 backdrop
4. surface alpha 是否过高
5. 背景本身是否没有纹理 / 明暗变化

不要第一反应就加大透明度。

### 26.2 滚动时玻璃里的背景不动

说明采样的很可能不是实际滚动内容，而是：

- 静态 CanvasBackdrop
- 错误层级
- 一张被缩放进玻璃尺寸的伪背景图

应该让 glass 读取真实滚动内容对应的 LayerBackdrop。

### 26.3 动画时出现方形残影

常见原因：

- 只使用 `graphicsLayer(scale)`，但 backdrop 仍按原布局尺寸采样
- shadow / clip / drawBackdrop 在同一 offscreen layer
- 动画结束时切换成另一套节点结构

处理方式见本文“动画建议”。

### 26.4 动画时阴影消失

通常是：

```kotlin
clip(shape)
```

放在了阴影外层。

对于动画玻璃，建议把阴影独立成兄弟层。

### 26.5 最后一帧闪一下

检查是否存在类似：

```kotlin
if (progress < 1f) AnimatedVersion() else StaticVersion()
```

尽量保持节点树不变。

### 26.6 Popup / Dialog 崩溃或 RenderNode recording 异常

检查 backdrop 是否录制了包含自身 glass 的 Compose 树。

Popup 本身如果采样 Activity 内容，也必须保证被采样树里不包含当前 glass 的递归路径。

---

## 28. 开发约束

修改项目之前应阅读：

```text
AGENTS.md
```

核心约束：

- 原始玻璃源码默认只读
- 页面布局问题放在 `dev/liquidglass/ios27` 或 sample 层解决
- 不要为修页面位置去改 shader / refraction 核心
- LayerBackdrop 必须防止递归采样
- Figma token 不手写
- Slider 等已定制组件不能被上游代码直接覆盖

---

## 29. 构建与校验

基础构建：

```powershell
.\gradlew.bat :liquidglass:assembleDebug :sample:assembleDebug --no-daemon --console=plain
```

完整框架校验：

```powershell
node tools/verify-extraction.mjs
node tools/generate-ios27-tokens.mjs --check
.\gradlew.bat :verifyLiquidGlassSpec :liquidglass:testDebugUnitTest :sample:assembleDebug --no-daemon --console=plain
```

`sample:assembleDebug` 当前还会：

1. 生成 APK
2. 校验 AAR / APK 必需类
3. 校验 LiquidGlass 项目规范
4. 最后触发 QQ APK 自动发送

可以通过环境变量关闭自动发送：

```powershell
$env:QQ_APK_AUTO_SEND = "0"
```

---

## 30. 最小完整示例

下面是一份推荐的新页面写法：

```kotlin
@Composable
fun DemoPage() {
    var slider by rememberSaveable { mutableFloatStateOf(0.5f) }
    var enabled by rememberSaveable { mutableStateOf(true) }

    Ios27PageTheme {
        val colors = LocalIos27PageColors.current

        Box(
            Modifier
                .fillMaxSize()
                .background(colors.page)
        ) {
            Ios27PageContent {
                Ios27PageHeader("Demo")

                Ios27ListSection {
                    Ios27ListRows(
                        listOf("Account", "About")
                    ) { label ->
                        Ios27ListRow(
                            title = label,
                            onClick = {},
                        )
                    }
                }

                Ios27ListSection {
                    Ios27SliderRow(
                        label = "Volume",
                        value = slider,
                        onValueChange = { slider = it },
                    )

                    Ios27SwitchRow(
                        label = "Enabled",
                        checked = enabled,
                        onCheckedChange = { enabled = it },
                    )
                }
            }
        }
    }
}
```

如果这个页面后续需要加入真实透景玻璃，再在现有页面结构外增加独立 `LayerBackdrop` 背景兄弟层，而不是重写页面组件。

---

## 31. 使用原则总结

优先级建议：

```text
Ios27 页面封装
    ↓ 不够用
已有 Liquid 组件
    ↓ 不够用
liquidDialogSurface / drawBackdrop
    ↓ 最后才考虑
修改底层玻璃实现
```

最重要的三条规则：

1. **玻璃和被采样背景必须是兄弟层，禁止递归采样。**
2. **页面问题优先在页面层解决，不要随意修改底层玻璃材质。**
3. **动画时保持节点结构稳定；Backdrop、阴影、裁剪最好分层处理。**

按照这三个原则接入，绝大多数页面都可以复用现有框架，而不需要重新实现玻璃效果。
