# 酷安参数

本配置来自用户提供的 `酷安.apk`：包名 `com.coolapk.market`，版本 `16.6.4`，版本代码 `2609291`。数值通过静态分析提取，记录于 2026-10-12；来源是酷安自定义样式及其调用处，不是 Figma 数值，也不是本框架默认值。不同版本或运行状态可能另有覆盖。

“酷安参数”是这份参考配置的名称。本次仅添加说明，不新增运行时预设、不修改组件外形或默认配置。下方示例只应用现有接口能表达的部分，完整视觉一致性仍需实际页面对照。

## 几何与文字

| 参数 | 酷安数值 | 说明 |
| --- | --- | --- |
| 底栏高度 | 56dp | 容器高度 |
| 内容内边距 | 4dp | 上下各 4dp |
| 可见内容 / 指示器高度 | 48dp | `56 - 4 × 2` |
| 选中背景采样高度比例 | 1.08 | 采样层高度 51.84dp，不是可见指示器高度 |
| 页面左右留白 | 20dp | 宿主布局配置 |
| 最大宽度 | 480dp | 宿主布局上限 |
| 图标大小 | 24dp | 图标加文字模式 |
| 标签字号 / 行高 | 10sp / 12sp | 图标加文字模式 |
| 标签字重 | 选中 Bold，未选中 Normal | 随选中状态切换 |
| 纯文字模式字号 | 选中 17sp，未选中 16sp | 与图标加文字模式分开配置 |

## 颜色

| 参数 | 浅色模式 | 深色模式 |
| --- | --- | --- |
| 玻璃容器底色 | `#FAFAFA`，alpha 0.63 | `#121212`，alpha 0.63 |
| 无玻璃效果时的底色 | `#FAFAFA`，alpha 0.90 | `#121212`，alpha 0.90 |
| 选中背景颜色 | 黑色，alpha 0.10 | 白色，alpha 0.10 |
| 普通内容颜色 | 黑色 | 白色 |
| 强调色 | 当前主题强调色 | 当前主题强调色 |
| 按下指示器叠色 | 透明 | 透明 |

无玻璃效果时的 0.90 是替换 alpha，不是 `0.63 × 0.90`。强调色没有固定为蓝色。

## 光学与交互

| 参数 | 酷安数值 |
| --- | --- |
| `highlightRadiusRatio` | 1.5 |
| `pressedHeightRatio` | 1.3928572 |
| `accentPressedScale` | 1.2 |
| `backgroundBlur` | 2dp |
| `backgroundLensHeight` | 16dp |
| `backgroundLensAmount` | 32dp |
| `restingThumbBackgroundRefractionHeight` | 7dp |
| `restingThumbBackgroundRefractionAmount` | 9.8dp |
| `pressedThumbBackgroundRefractionHeight` | 7dp |
| `pressedThumbBackgroundRefractionAmount` | 9.8dp |
| `pressedThumbContentRefractionHeight` | 7dp |
| `pressedThumbContentRefractionAmount` | 9.8dp |
| `restingThumbBackgroundBlur` | 2dp |
| `pressedThumbBackgroundBlur` | 0dp |
| `innerShadowRadius` | 8dp |
| `outerShadowRadius` | 24dp |
| `outerShadowOffsetY` | 4dp |
| `restingThumbHighlightAlpha` | 0 |
| `barPressHighlightAlpha` | 0 |
| `selectionContentPressHighlightAlpha` | 0 |

这三个高光 alpha 为 0 不表示所有玻璃边缘光效关闭；交互材质还有独立的光效路径。未确认固定的描边宽度或描边透明度，不应由这些数值推断。

## 本框架字段映射

`LiquidBottomTabsStyle` 定义在 `liquidglass/src/main/java/com/zeaze/tianyinwallpaper/catalog/components/LiquidGlassStyles.kt`，实际绘制在同目录的 `LiquidBottomTabs.kt`。

| 酷安参数 | 本框架配置 | 可表达范围 |
| --- | --- | --- |
| 高度 56dp / 48dp | `trackHeight` / `indicatorHeight` | 可配置 |
| 内边距 4dp | `trackInnerPadding`、`trackHorizontalPadding` | 可配置；页面留白另由宿主处理 |
| 容器底色 alpha 0.63 | `containerColor` | 可配置 |
| 回退底色 alpha 0.90 | `fallbackContainerColor` | 可配置 |
| 主题强调色 | `accentColor` | 使用宿主当前主题强调色 |
| 背景模糊 2dp | `trackBlurRadius` | 可配置 |
| 背景折射高度 16dp / 强度 32dp | `trackLensRadius` | 当前将一个值同时传给两个参数，不能独立设置 16 / 32 |
| 指示器折射高度 7dp / 强度 9.8dp | `indicatorLensWidth` / `indicatorLensHeight` | 对应当前指示器折射调用；不代表指示器实体宽高 |
| 内阴影半径 8dp | `indicatorInnerShadowRadius` | 可配置 |
| 按下强调内容缩放 1.2 | `hiddenContentPressedScale` | 对应选中内容层缩放 |
| 图标、标签字号与字重 | 宿主 Tab 内容 | 按模式自行配置 |
| 页面留白 20dp / 最大宽度 480dp | 宿主布局 | 按页面约束配置 |

框架底层 `lens(refractionHeight, refractionAmount, ...)` 的前两个参数分别是折射高度和折射强度，不能把酷安的 16 / 32dp 写成 X / Y 半径。当前 `trackLensRadius` 对两个参数使用同一数值；指示器的 `indicatorLensWidth` / `indicatorLensHeight` 虽然命名含宽高，实际传入的也是这两个光学参数。

目前没有独立样式字段表达采样高度比例、高光半径比例、按下高度比例、静止 / 按下模糊切换、选中背景 alpha，以及静止 / 按下的三组独立折射配置。外阴影也不是 `LiquidBottomTabsStyle` 的可配置字段。框架当前还有固定的按下黑色叠层，不能只通过下面的样式配置改为透明。

## 部分接入示例

以下配置复用现有 `LiquidBottomTabsStyle`，`themeAccent` 由宿主主题提供。未填写的字段保留框架默认值，尤其是底栏折射仍为默认的 24 / 24dp；此示例不等于完整复刻酷安材质。

```kotlin
val baseColor = if (isLightTheme) Color(0xFFFAFAFA) else Color(0xFF121212)
val coolapkParameters = LiquidBottomTabsStyle.default(isLightTheme).copy(
    accentColor = themeAccent,
    containerColor = baseColor.copy(alpha = 0.63f),
    fallbackContainerColor = baseColor.copy(alpha = 0.90f),
    trackHeight = 56.dp,
    indicatorHeight = 48.dp,
    trackHorizontalPadding = 4.dp,
    trackInnerPadding = 4.dp,
    trackBlurRadius = 2.dp,
    indicatorLensWidth = 7.dp,
    indicatorLensHeight = 9.8.dp,
    indicatorInnerShadowRadius = 8.dp,
    hiddenContentPressedScale = 1.2f,
)
```

将 `coolapkParameters` 传入组件的 `style` 参数。使用 Split Search 布局时，主底栏和右侧按钮继续共享宿主 Backdrop，并由现有适配器派生按钮材质；接入约束见 [Split Search 材质说明](tabbar-split-material.md)。

## 提取来源

- `CoolapkLiquidGlassStyles.kt`（APK 类名 `xm1`）：酷安颜色与光学样式覆盖。
- `LiquidGlassStyle.kt`（`wp8`）：基础颜色；`LiquidGlassTabPanel.kt`（`xr8` / `wr8`）：光学基础值与复制字段。
- `LiquidBottomNavigationBar.kt`（`qn8`、`Ϳ`）：底栏配置及图标 / 文字模式。
- `yr8` / `vr8`：几何计算、标签及图标默认值。
- `MainFragment.kt` 的 `MainLiquidHomeScaffold`：左右 20dp 和最大宽度 480dp。
- APK 资源 `liquid_bottom_navigation_height` / `liquid_bottom_navigation_content_padding`：56dp / 4dp。

这些类名用于定位这份 APK 的静态证据，可能随酷安版本及混淆规则变化。
