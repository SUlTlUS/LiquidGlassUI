# Tianyin LiquidGlass 完整实现提取指南

本文是从当前 `tianyinwallpaper` 工作树直接提取的实现说明，不是重新设计的 LiquidGlass 方案。后续 Agent 需要把这种风格用于新 App 时，应优先复用文中列出的原文件和调用顺序；不要把它简化成普通半透明 `Card`，也不要只复制某一个按钮组件。

## 0. 当前基线和上游状态

- 本地应用基线：分支 `景深`，提交 `e67b07adebcb5e0d8d0fb2bf1924070109c372d9`。
- 上游远端：`https://github.com/Kyant0/AndroidLiquidGlass.git`。
- 已抓取分支：`android-liquid-glass/kmp`。
- 上游当前提交：`65ab177e90e5c1d8c62e70cf7755841982da65f6`，tag `2.0.1`。
- 本地依赖已更新：`io.github.kyant0:backdrop:2.0.1`、`io.github.kyant0:shapes:1.2.1`。
- 上游 `2.0.0 -> 2.0.1` 的实际差异是 Kotlin/AGP/Compose/Gradle 等构建依赖和发布坐标调整，没有 Backdrop 或 catalog 源码变化。因此本项目没有用上游目录覆盖 Tianyin 的定制代码。

本项目的实现是两层组合：

1. `backdrop/**` 是直接放进应用包内的底层渲染实现，包名已改为 `com.zeaze.tianyinwallpaper.backdrop`，负责录制图层、坐标换算、RenderEffect、RuntimeShader、高光和阴影。
2. `catalog/components/**` 是基于底层实现做出的产品组件，保留上游 LiquidButton/LiquidSlider/LiquidToggle/LiquidBottomTabs 的结构，但加入 Tianyin 的主题、降级、亮度采样、尺寸 token、触摸和页面状态处理。

另外，底层实现通过 `com.kyant.backdrop.RuntimeShader`、`asAndroidRuntimeShader()`、`asComposeShader()` 等 API 接入上游 `backdrop` 库；所以“本地底层源码”和“上游运行时库”都不能漏掉。

完整原样代码副本已放在 [`liquid-glass-extracted/`](liquid-glass-extracted/)；其中 `source/` 与应用源目录保持相同的相对路径，并已逐文件做 SHA-256 核对。本文负责解释结构和接入边界，代码副本负责让后续 Agent 可以直接复制和比对。

## 1. 运行链路：从背景到玻璃表面

一个完整玻璃控件的真实绘制顺序如下：

```text
页面背景/内容
    │
    ├─ rememberLayerBackdrop { drawRect(background); drawContent() }
    │       └─ layerBackdrop(backdrop) 录制 GraphicsLayer
    │
    └─ 控件 .drawBackdrop(
           backdrop = parentBackdrop,
           shape = { Capsule() / RoundedRectangle(...) },
           effects = { colorControls; blur; lens },
           highlight = { ... }, shadow = { ... }, innerShadow = { ... }
       )
             │
             ├─ 按控件坐标把背景 GraphicsLayer 平移到当前控件
             ├─ 组合 Android RenderEffect 链
             ├─ 用 AGSL RuntimeShader 做透镜折射/色散/高光
             ├─ 录制带 padding 的离屏 GraphicsLayer
             ├─ 绘制 backdrop
             ├─ 绘制 surface tint
             ├─ 绘制控件 content
             └─ 绘制高光、前景和导出的子层
```

核心原则是：玻璃控件采样的是外部已录制的 layer，不采样自己正在绘制的内容。尤其是嵌套弹窗、BottomSheet、滑块 thumb 和 Toggle thumb，必须给它们单独的 `LayerBackdrop` 或通过 `exportedBackdrop` 导出，避免递归采样和黑屏。

## 2. 底层文件：完整原实现清单

以下目录是后续 Agent 应整体复制的底层边界。路径和源码本身比本文摘要更权威。

### 2.1 Backdrop 抽象、录制和坐标

| 文件 | 原实现职责 |
| --- | --- |
| [`backdrop/Backdrop.kt`](liquid-glass-extracted/source/backdrop/Backdrop.kt) | `Backdrop` 接口；暴露 `isCoordinatesDependent` 和 `DrawScope.drawBackdrop(...)`。 |
| [`backdrop/BackdropEffectScope.kt`](liquid-glass-extracted/source/backdrop/BackdropEffectScope.kt) | 效果 DSL 的运行时状态：density、fontScale、size、layoutDirection、padding、`android.graphics.RenderEffect`，并持有 RuntimeShader 缓存。 |
| [`backdrop/DrawBackdropModifier.kt`](liquid-glass-extracted/source/backdrop/DrawBackdropModifier.kt) | `drawPlainBackdrop`、`drawBackdrop`、ModifierNode、离屏 layer 录制、效果观察、导出 layer 和最终绘制顺序。 |
| [`backdrop/LayerRecorder.kt`](liquid-glass-extracted/source/backdrop/LayerRecorder.kt) | 在 `GraphicsLayer.record` 时临时同步正确的 Compose density，并保证 finally 恢复原 density。 |
| [`backdrop/InverseLayerScope.kt`](liquid-glass-extracted/source/backdrop/InverseLayerScope.kt) | 把控件的 scale、rotationZ、translation 等 `GraphicsLayerScope` 变换反算到背景采样坐标。 |
| [`backdrop/ShapeProvider.kt`](liquid-glass-extracted/source/backdrop/ShapeProvider.kt) | 缓存 shape 的 Outline，按 shape、size、layoutDirection、density 失效。 |
| [`backdrop/Outline.kt`](liquid-glass-extracted/source/backdrop/Outline.kt) | 将矩形、圆角矩形和任意 Path Outline 转换为 Canvas clip。 |
| [`backdrop/RuntimeShaderCache.kt`](liquid-glass-extracted/source/backdrop/RuntimeShaderCache.kt) | 按 key 缓存 `RuntimeShader`，节点 detach 时清理。 |
| [`backdrop/Shaders.kt`](liquid-glass-extracted/source/backdrop/Shaders.kt) | AGSL 字符串：圆角矩形 SDF、普通折射、带色散折射、Default/Ambient 高光和 Gamma 调整。 |

`drawBackdrop` 的参数是完整风格的关键入口：`backdrop`、`shape`、`effects`、可选的 `highlight`、`shadow`、`innerShadow`、`layerBlock`、`exportedBackdrop`、`onDrawBehind`、`onDrawBackdrop`、`onDrawSurface`、`onDrawFront` 都有实际用途。若新 App 只保留 `blur` 和一个半透明背景，视觉和交互都不等价。

### 2.2 Backdrop 类型和组合

| 文件 | 原实现职责 |
| --- | --- |
| [`backdrop/backdrops/Backdrop.kt`](liquid-glass-extracted/source/backdrop/backdrops/Backdrop.kt) | `rememberBackdrop(backdrop, onDraw)` 包装已有 backdrop，在绘制前后插入变换或自定义绘制。 |
| [`backdrop/backdrops/LayerBackdrop.kt`](liquid-glass-extracted/source/backdrop/backdrops/LayerBackdrop.kt) | `rememberLayerBackdrop(...)`；保存 `GraphicsLayer`，依据 `LayoutCoordinates.localPositionOf` 计算背景到控件的偏移。 |
| [`backdrop/backdrops/LayerBackdropModifier.kt`](liquid-glass-extracted/source/backdrop/backdrops/LayerBackdropModifier.kt) | `layerBackdrop(backdrop)`；先 `drawContent()`，再 `recordLayer(...)` 录制 layer，并在 attach/detach 时维护坐标。 |
| [`backdrop/backdrops/CanvasBackdrop.kt`](liquid-glass-extracted/source/backdrop/backdrops/CanvasBackdrop.kt) | `rememberCanvasBackdrop { ... }`；用于固定颜色或静态 Canvas 内容的背景/低版本 fallback。 |
| [`backdrop/backdrops/CombinedBackdrop.kt`](liquid-glass-extracted/source/backdrop/backdrops/CombinedBackdrop.kt) | 2 个、3 个或 vararg backdrop 顺序合成，并正确传播 `isCoordinatesDependent`。 |
| [`backdrop/backdrops/EmptyBackdrop.kt`](liquid-glass-extracted/source/backdrop/backdrops/EmptyBackdrop.kt) | 不绘制任何内容的稳定空 backdrop。 |

最常用的两种搭配：

```kotlin
val pageBackdrop = rememberLayerBackdrop()
Box(Modifier.layerBackdrop(pageBackdrop)) { pageContent() }

Modifier.drawBackdrop(
    backdrop = pageBackdrop,
    shape = { Capsule() },
    effects = {
        colorControls(brightness = 0.05f, saturation = 1.2f)
        blur(8.dp.toPx())
        lens(10.dp.toPx(), 24.dp.toPx(), chromaticAberration = true)
    }
)
```

上面只是原实现的调用形状示例；实际参数和层级应直接参考现有组件，不要抽象成另一套 API。

## 3. 效果实现：blur、color、lens、shader

### 3.1 `BackdropEffectScope`

`BackdropEffectScopeImpl.apply(effects)` 每次更新时先清空 `padding` 和 `renderEffect`，再按调用顺序重新构造效果链。`DrawBackdropNode` 通过 `observeReads` 观察 effects DSL 读取的 Compose 状态；尺寸、density、字体缩放或布局方向变化时重新应用。

### 3.2 效果文件

| 文件 | API | 实际实现 |
| --- | --- | --- |
| [`effects/Blur.kt`](liquid-glass-extracted/source/backdrop/effects/Blur.kt) | `blur(radius, edgeTreatment)` | API 31+ 使用 `RenderEffect.createBlurEffect`；必要时增加离屏 padding；效果按当前链嵌套。 |
| [`effects/ColorFilter.kt`](liquid-glass-extracted/source/backdrop/effects/ColorFilter.kt) | `colorFilter`、`opacity`、`colorControls`、`vibrancy`、`exposureAdjustment`、`gammaAdjustment` | ColorMatrix 负责亮度/对比度/饱和度/曝光；Gamma 通过 RuntimeShader；API 31+ 才挂载 Android RenderEffect。 |
| [`effects/Lens.kt`](liquid-glass-extracted/source/backdrop/effects/Lens.kt) | `lens(refractionHeight, refractionAmount, depthEffect, chromaticAberration)` | API 33+ 使用圆角矩形 SDF AGSL，传入 size、offset、四角半径、折射高度/强度；可启用深度项和 RGB 色散。只支持 `RoundedRectangularShape`、`AbsoluteRoundedCornerShape`、`CornerBasedShape`。 |
| [`effects/RuntimeShaderEffect.kt`](liquid-glass-extracted/source/backdrop/effects/RuntimeShaderEffect.kt) | `runtimeShaderEffect(key, shaderString, uniformShaderName, block)` | API 33+ 从缓存取得 RuntimeShader，设置 uniforms，再以 `RenderEffect.createRuntimeShaderEffect(..., "content")` 接入链。 |

效果顺序不是无关紧要的：现有组件通常先做 `colorControls` 或 `vibrancy`，再 `blur`，最后 `lens`；修改顺序会改变折射内容和色彩结果。

### 3.3 AGSL 和缓存要求

- `Shaders.kt` 中的 shader 字符串必须与 `Lens.kt`、`HighlightStyle.kt` 中设置的 uniform 名称保持一致。
- RuntimeShader 以 key 缓存，但每次绘制仍会更新 size、cornerRadii、offset、angle、falloff 等 uniform。
- `lens` 依赖可解析的圆角半径；新组件必须使用 Capsule、RoundedRectangle 或 Compose CornerBasedShape。
- API 33 以下不能依赖 RuntimeShader；调用方法本身会 return，但产品层仍应走 `LiquidGlassPrefs.isSupported` 和普通控件 fallback。

## 4. 高光、外阴影、内阴影

### 4.1 高光

- [`highlight/Highlight.kt`](liquid-glass-extracted/source/backdrop/highlight/Highlight.kt) 定义宽度、模糊半径、alpha 和 style，提供 `Default`、`Ambient`、`Plain`。
- [`highlight/HighlightStyle.kt`](liquid-glass-extracted/source/backdrop/highlight/HighlightStyle.kt) 定义颜色、BlendMode 和 shader 创建；Default/Ambient 在 API 33+ 使用 `Shaders.kt` 的 AGSL。
- [`highlight/HighlightModifier.kt`](liquid-glass-extracted/source/backdrop/highlight/HighlightModifier.kt) 用 Offscreen GraphicsLayer 录制 shape 的 stroke，再应用 blur mask、shader、alpha 和 blendMode。
- [`highlight/RenderEffect.kt`](liquid-glass-extracted/source/backdrop/highlight/RenderEffect.kt) 提供把 Android RenderEffect 串入 `BackdropEffectScope` 的适配函数。

### 4.2 阴影

- [`shadow/Shadow.kt`](liquid-glass-extracted/source/backdrop/shadow/Shadow.kt) 是外阴影数据类，默认 radius `24.dp`、黑色 alpha `0.1`、offset 为 `radius / 6`。
- [`shadow/ShadowModifier.kt`](liquid-glass-extracted/source/backdrop/shadow/ShadowModifier.kt) 在离屏 layer 中绘制 shape，再用 `BlurMaskFilter` 和 `BlendMode.Clear` 得到外阴影。
- [`shadow/InnerShadow.kt`](liquid-glass-extracted/source/backdrop/shadow/InnerShadow.kt) 是内阴影数据类并提供 `lerp`，适合随按压进度插值。
- [`shadow/InnerShadowModifier.kt`](liquid-glass-extracted/source/backdrop/shadow/InnerShadowModifier.kt) API 31+ 使用 BlurEffect、clipOutline、Offscreen layer 和 Clear mask 生成内阴影。

`drawBackdrop` 默认会启用 `Highlight.Default` 和 `Shadow.Default`，`innerShadow` 默认关闭。若想得到现有按钮/滑块的按压质感，必须显式传入根据动画 progress 返回的 Highlight、Shadow 和 InnerShadow，而不是只加一个静态 border。

## 5. 产品组件层：原文件和真实行为

### 5.1 按钮

- [`LiquidButton.kt`](liquid-glass-extracted/source/catalog/components/LiquidButton.kt) 是对外入口。
- [`LiquidButtonCore.kt`](liquid-glass-extracted/source/catalog/components/LiquidButtonCore.kt) 是实际绘制：Capsule、colorControls/vibrancy、blur、lens、InteractiveHighlight、按压 scale/translation、tint 和 surface。
- [`LiquidButtonGroup.kt`](liquid-glass-extracted/source/catalog/components/LiquidButtonGroup.kt) 把多个按钮作为一个玻璃组绘制，并支持亮度状态、交互高光和自定义宽高。
- [`LiquidGlassStyles.kt`](liquid-glass-extracted/source/catalog/components/LiquidGlassStyles.kt) 集中保存 `LiquidButtonStyle` 和 `LiquidBottomTabsStyle`，包括高度、padding、blur、lens、按压扩展量和底栏尺寸。

按钮的核心参数不是单一颜色：

```text
父级 Backdrop
  -> Capsule clipping
  -> colorControls 或 vibrancy
  -> blur
  -> lens
  -> InteractiveHighlight 驱动 layerBlock 的位移/scale
  -> DrawBackdrop 的 surface tint
  -> Row content
```

### 5.2 Slider

[`LiquidSlider.kt`](liquid-glass-extracted/source/catalog/components/LiquidSlider.kt) 的真实结构是“track layer + thumb layer”：

- track 使用 `rememberLayerBackdrop()`，先记录灰色 track 和 accent progress。
- thumb 使用 `rememberCombinedBackdrop(parentBackdrop, rememberBackdrop(trackBackdrop) { ... })`，把页面背景和 track 背景同时折射。
- 拖动时由 [`DampedDragAnimation.kt`](liquid-glass-extracted/source/catalog/utils/DampedDragAnimation.kt) 管理 target、progress、velocity、pressProgress、scale 和 modifier。
- thumb 的 lens、Ambient highlight、外阴影、内阴影、velocity 形变和 surface 白色覆盖都随 `pressProgress` 变化。
- 点击 track 会将位置映射到 `valueRange`；拖动完成后才调用 `onValueChangeFinished`，因此业务若需要 commit-on-release 应继续使用该回调。

### 5.3 Toggle

[`LiquidToggle.kt`](liquid-glass-extracted/source/catalog/components/LiquidToggle.kt) 也是 track + thumb：

- track 是 `64.dp x 28.dp` Capsule，颜色从灰色到浅色/深色绿色 accent 插值。
- thumb 通过 CombinedBackdrop 采样外部 backdrop 和 trackBackdrop。
- `fraction` 在 `0f..1f` 之间拖动；松手时以 `0.5f` 为阈值归并到开或关。
- 保留 haptic feedback、`Role.Switch` semantics、拖动开始/结束回调和外部 selected 状态同步。

### 5.4 Bottom Tabs

- [`LiquidBottomTab.kt`](liquid-glass-extracted/source/catalog/components/LiquidBottomTab.kt) 只负责单个 tab 内容。
- [`LiquidBottomTabs.kt`](liquid-glass-extracted/source/catalog/components/LiquidBottomTabs.kt) 负责 track、indicator、拖动、弹性 panel、速度形变、`pendingSelectionIndex` 和外部选中状态同步。
- 底栏在 `backdrop == null` 时走 fallback；玻璃路径使用 `tabsBackdrop` 和 `rememberCombinedBackdrop`。
- `LiquidBottomTabsStyle` 的默认 accent 是浅色 `#0088FF`、深色 `#0091FF`；容器分别是低 alpha 的浅色/深色。
- `safeTabsCount`、宽度为 0 的约束保护和选中回写顺序是现有实现的一部分，不要用一个简单 `Row + clickable` 替换。

### 5.5 分段选择器和附加视觉组件

- [`LiquidSegmentedSelector.kt`](liquid-glass-extracted/source/catalog/components/LiquidSegmentedSelector.kt) 是设置页等场景使用的 segmented control；track 用 CanvasBackdrop，indicator 用 track + tabs CombinedBackdrop，并带拖动高光、lens、外/内阴影。
- [`StripedGlass.kt`](liquid-glass-extracted/source/catalog/components/StripedGlass.kt) 是条纹/波纹玻璃的独立扩展，内部包含 phase 动画、fade mask 和 API 33+ refraction shader。
- [`PlainFallbackControls.kt`](liquid-glass-extracted/source/catalog/components/PlainFallbackControls.kt) 是低版本或关闭玻璃时的按钮、开关和滑块，不承担玻璃采样。
- [`WheelPicker.kt`](C:/IDE/Android/tianyinwallpaper/app/src/main/java/com/zeaze/tianyinwallpaper/catalog/components/WheelPicker.kt) 是与玻璃组件配套的分页/滚轮控件，不是底层 LiquidGlass 必需项。

## 6. 交互与动画辅助类

| 文件 | 用途 |
| --- | --- |
| [`catalog/utils/DampedDragAnimation.kt`](liquid-glass-extracted/source/catalog/utils/DampedDragAnimation.kt) | 弹簧/阻尼拖动，统一提供 `value`、`targetValue`、`progress`、`velocity`、`pressProgress`、`scaleX/Y` 和 Modifier。 |
| [`catalog/utils/InteractiveHighlight.kt`](liquid-glass-extracted/source/catalog/utils/InteractiveHighlight.kt) | 记录按下/拖动位置，API 33+ 用 RuntimeShader 生成交互高光，同时提供 gestureModifier。 |
| [`catalog/utils/AdaptiveLuminanceGlass.kt`](liquid-glass-extracted/source/catalog/utils/AdaptiveLuminanceGlass.kt) | 采样玻璃区域亮度并动画计算 content color、brightness、contrast、saturation；按钮将结果传给 `colorControls`。 |
| [`catalog/utils/LiquidMorph.kt`](liquid-glass-extracted/source/catalog/utils/LiquidMorph.kt) | 产品层的液态形变状态、物理参数和 controller；不是 Backdrop 采样本身。 |
| [`catalog/utils/SdfShader.kt`](liquid-glass-extracted/source/catalog/utils/SdfShader.kt) | 以 Bitmap/SDF 作为 RuntimeShader 输入的扩展。 |
| [`catalog/utils/DragGestureInspector.kt`](liquid-glass-extracted/source/catalog/utils/DragGestureInspector.kt) | 调试/检查拖动手势。 |
| [`catalog/utils/ProgressConverter.kt`](liquid-glass-extracted/source/catalog/utils/ProgressConverter.kt) | 进度映射接口。 |
| [`catalog/utils/UISensor.kt`](liquid-glass-extracted/source/catalog/utils/UISensor.kt) | UI 传感器辅助，不是玻璃底层必需项。 |

## 7. 主题、颜色和尺寸 token

当前应用不是通过 XML theme 生成玻璃，而是在 Compose `MaterialTheme` 外层传入颜色，再把 `isLightTheme` 和 `accentColor` 传到组件。

- MainActivity 以系统/用户 theme mode 计算 `useDarkTheme`。
- 浅色页面使用 Material background；深色 LiquidGlass 根背景使用 `Color(0xFF0A0A0C)`。
- 设置/分组常用深色容器约为 `Color(0xFF1C1C20)`，再按场景调 alpha。
- 组件默认蓝色 accent 为 `#0088FF` / `#0091FF`；Toggle accent 为浅色 `#34C759`、深色 `#30D158`。
- track 常用浅色 `Color(0xFF787878).copy(alpha = 0.2f)`，深色 `Color(0xFF787880).copy(alpha = 0.36f)`。
- `LiquidGlassStyles.kt` 中的 token 优先级高于组件内局部默认值；新页面应传入 style，而不是复制魔法数字。

## 8. 功能开关和 API 兼容

[`utils/LiquidGlassPrefs.kt`](liquid-glass-extracted/source/utils/LiquidGlassPrefs.kt) 是唯一的开关/能力判断来源：

```kotlin
const val PREF_ENABLE_LIQUID_GLASS = "enableLiquidGlass"
isSupported == Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
isEnabled(context) == isSupported && isSwitchEnabled(context)
```

实际行为：

| 条件 | 产品层行为 |
| --- | --- |
| Android 13/API 33 及以上 + 开关开启 | 创建 LayerBackdrop，使用 RuntimeShader lens/highlight，并使用 LiquidButton/LiquidSlider/LiquidToggle/LiquidBottomTabs。 |
| Android 12/API 31-32 | 底层 blur/shadow 部分具备系统能力，但产品开关按当前实现视为不支持，走普通控件。 |
| Android 11/API 30 及以下 | 不创建玻璃效果，使用 PlainFallbackControls 或 CanvasBackdrop。 |
| 用户关闭开关 | `enableLiquidGlass = false`，页面使用 null backdrop 和普通半透明/fallback 控件。 |

项目当前 `minSdk = 24`，所以不能把 API 31/33 调用移到无 guard 的初始化路径。底层文件自身有 API guard；产品层还要保留 `LiquidGlassPrefs.isSupported`，两层都不能省。

## 9. 应用接入点：从 MainActivity 传播到页面

### 9.1 根层

[`MainActivity.kt`](C:/IDE/Android/tianyinwallpaper/app/src/main/java/com/zeaze/tianyinwallpaper/MainActivity.kt) 的关键边界：

1. 从 `LiquidGlassPrefs.isSwitchEnabled(this)` 读取持久化开关。
2. 用 `LiquidGlassPrefs.isSupported && liquidGlassEnabled` 得到 `enableLiquidGlass`。
3. 开启时创建 `rootBackdrop = rememberLayerBackdrop { drawRect(themeBackgroundColor); drawContent() }`。
4. 给 NavHost 使用 `layerBackdrop(rootBackdrop)`，使导航页和底栏都能采样根内容。
5. 把 `enableLiquidGlass`、`rootBackdrop`、`isLightTheme` 和 `accentColor` 传入主页面、设置页、关于页、预览页、底部 tabs 和按钮。
6. 修改开关时同时更新 Compose state 和 `App.TIANYIN` SharedPreferences 的 `enableLiquidGlass`。

### 9.2 设置页

[`ui/setting/SettingFragment.kt`](C:/IDE/Android/tianyinwallpaper/app/src/main/java/com/zeaze/tianyinwallpaper/ui/setting/SettingFragment.kt) 的设置页/子页面重复使用同一套模式：

- 页面先计算 `supportsLiquidGlass`、`enableLiquidGlass` 或 `useLiquidGlass`。
- 玻璃页面创建 `liquidBackdrop`/`topBackdrop`，并给包裹页面的 Box 使用 `layerBackdrop(...)`。
- 玻璃弹窗优先接收 LayerBackdrop；若没有玻璃，则用 `rememberCanvasBackdrop { drawRect(backgroundColor) }`。
- 开关行使用 LiquidToggle；开关关闭或不支持时使用 PlainSwitch。
- 颜色选择器、设置分组、对话框、顶部 overlay 都将 backdrop 作为参数继续向下传递。

### 9.3 已接入的其他场景

当前代码中可以看到实际接入的场景包括：

- `ui/main/MainTopBar.kt`、`MainRouteScreen.kt`、`MainPreviewScreen.kt`。
- `ui/setting/SettingFragment.kt`、`ui/about/AboutFragment.kt`、`ui/setting/DialogContent.kt`。
- `ui/raster/RasterPreviewScreen.kt`、`ui/depth/DepthPreviewScreen.kt.kt`、`DepthOnlineGenerateRoute.kt`。
- `update/UpdateDialog.kt`、`ui/commom/ProgressiveBlurContent.kt`、`LiquidConfirmOverlay.kt`。
- `ui/test/TestRouteScreen.kt`。

这组接入说明了一个重要边界：新 App 不应该只把玻璃组件放在一个 Demo 页面里，而要从页面根 layer 开始传播 backdrop，再由弹窗/控件按层级消费。

## 10. 新 App 原样复用顺序

后续 Agent 构建新 App 时按以下顺序操作，避免遗漏隐含依赖：

1. 复制 `backdrop/**` 的完整目录和包内源码；不要先自行写一个简化版 `GlassCard`。
2. 保留上游库依赖 `backdrop` 和 `shapes`，并保留 Kotlin context parameters 配置；当前应用的配置在 [`app/build.gradle.kts`](C:/IDE/Android/tianyinwallpaper/app/build.gradle.kts) 和 [`gradle/libs.versions.toml`](C:/IDE/Android/tianyinwallpaper/gradle/libs.versions.toml)。
3. 在页面根部创建 `rememberLayerBackdrop`，用 `layerBackdrop` 录制真实背景内容。
4. 将 `Backdrop` 作为参数向控件传递；嵌套区域创建自己的 layer 或使用 `exportedBackdrop`。
5. 直接复制需要的产品组件文件及辅助类：按钮至少要有 `LiquidButtonCore + InteractiveHighlight`；滑块和开关还要有 `DampedDragAnimation`；亮度自适应才加入 `AdaptiveLuminanceGlass`。
6. 组件内部保持 `Capsule/RoundedRectangle -> effects -> highlight/shadow/innerShadow -> surface/content` 的实际顺序。
7. 使用 `LiquidGlassPrefs` 同时控制持久化开关和 API 能力；不可只在 UI 上隐藏按钮而继续创建 RuntimeShader。
8. 为不支持平台保留 `PlainFallbackControls` 或等价的普通控件，并保证触摸目标不小于现有组件的约 48dp 语义尺寸。
9. 先做 `assembleDebug`，再安装到 API 33+ 和低版本设备分别检查：玻璃采样、拖动/点击、主题切换、开关持久化和 fallback。

## 11. 常见错误

- 只添加 `Modifier.alpha(...).background(...)`：这没有 layer sampling、折射、色散、高光和阴影。
- 把控件自己的 layer 作为自己的 backdrop：会造成递归采样、闪烁或黑色内容。
- 只复制 `LiquidButton.kt`：真正绘制在 `LiquidButtonCore.kt`，交互又依赖 `InteractiveHighlight.kt` 和底层 backdrop。
- 只复制 `backdrop` Maven 依赖：当前应用组件导入的是本地 `com.zeaze.tianyinwallpaper.backdrop`，还需要复制本地底层目录；反过来只复制本地目录也会缺少 `com.kyant.backdrop.RuntimeShader` 适配。
- 在 API 24-32 直接初始化 `android.graphics.RuntimeShader` 或 lens RenderEffect：必须保留 API guard 和产品 fallback。
- 把上游 catalog 示例整文件覆盖到 Tianyin：会丢失 `backdrop == null` fallback、外部 `isLightTheme`、亮度自适应、页面状态同步和现有手势保护。
- 修改组件参数却不更新 `LiquidGlassStyles.kt`：会重新产生分散的魔法数字和浅深色不一致。

## 12. 维护和验证边界

- 上游同步先看 `android-liquid-glass/kmp` 的 Backdrop 源码和发布版本，再手动比较本地 `backdrop/**`；不要用上游 `app/` 的 catalog 组件覆盖产品层。
- 当前上游 `2.0.1` 已抓取到本地远端，且本次依赖版本已更新；上游仓库的 Kotlin `2.4.10`、AGP `9.3.2`、Gradle `9.7.1` 是上游自身构建环境，不等于本应用必须整体升级。
- `git diff --check` 只证明空白字符正确，不证明玻璃真实可见。
- 构建成功只证明代码能编译；最终验收必须另行确认 API 33+ 的视觉效果、实际拖动/点击、API 低版本 fallback 和开关持久化。
- 这个实现依赖真实的 Compose 绘制层和坐标关系，后续修改页面层级后应优先检查 backdrop 是否仍挂在正确的祖先/兄弟层，而不是先调颜色 alpha。

## 13. 最小原文件集合

如果新 App 只需要完整按钮/开关/滑块/底栏风格，最小集合仍然不是单文件：

```text
app/src/main/java/com/zeaze/tianyinwallpaper/backdrop/**
app/src/main/java/com/zeaze/tianyinwallpaper/catalog/components/LiquidButton.kt
app/src/main/java/com/zeaze/tianyinwallpaper/catalog/components/LiquidButtonCore.kt
app/src/main/java/com/zeaze/tianyinwallpaper/catalog/components/LiquidButtonGroup.kt
app/src/main/java/com/zeaze/tianyinwallpaper/catalog/components/LiquidBottomTab.kt
app/src/main/java/com/zeaze/tianyinwallpaper/catalog/components/LiquidBottomTabs.kt
app/src/main/java/com/zeaze/tianyinwallpaper/catalog/components/LiquidSlider.kt
app/src/main/java/com/zeaze/tianyinwallpaper/catalog/components/LiquidToggle.kt
app/src/main/java/com/zeaze/tianyinwallpaper/catalog/components/LiquidGlassStyles.kt
app/src/main/java/com/zeaze/tianyinwallpaper/catalog/utils/DampedDragAnimation.kt
app/src/main/java/com/zeaze/tianyinwallpaper/catalog/utils/InteractiveHighlight.kt
app/src/main/java/com/zeaze/tianyinwallpaper/catalog/components/PlainFallbackControls.kt
app/src/main/java/com/zeaze/tianyinwallpaper/utils/LiquidGlassPrefs.kt
```

若目标页面需要亮度自适应、条纹玻璃、SDF 或液态形变，再按第 6 节追加对应文件。上述清单是“原样提取”的复用边界，不是重新实现建议。
