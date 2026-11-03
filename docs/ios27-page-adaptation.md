# iOS 27 页面外围适配

参考仓库：[`seunghan91/ios27-design-system`](https://github.com/seunghan91/ios27-design-system)，当前固定在本地 `vendor/ios27-design-system/`。它提供 token、列表规格和生成规则；本项目只移植页面外围，不把 iOS 的玻璃实现替换成 Android 版本。

## 边界

保留不动：`backdrop/` 全部渲染链、`LiquidButton`、`LiquidButtonGroup`、`LiquidBottomTabs`、`LiquidSlider`、`LiquidToggle`、`LiquidSegmentedSelector`、拖拽弹簧、SDF、lens、highlight、shadow 和 `LiquidGlassStyles`。从 1.0.3 起补齐原样的 `ProgressiveBlurContent.kt`，`tools/verify-extraction.mjs` 必须保持 `48` 个文件逐字节通过。

允许调整：页面背景、分组容器、列表行最小高度、标题/副标题层级、左右内边距、行内分隔线、前置图标槽位、详情列、页面最大可读宽度、系统深浅色颜色。

## 当前 Android 映射

| 页面元素 | iOS 27 token | 当前值 |
| --- | --- | --- |
| 浅色页面底色 | `backgroundsGrouped.primary.light` | `#F2F2F7` |
| 深色页面底色 | `backgroundsGrouped.primary.dark` | `#000000` |
| 浅色分组底色 | `backgroundsGrouped.secondary.light` | `#FFFFFF` |
| 深色分组底色 | `backgroundsGrouped.secondary.dark` | `#1C1C1E` |
| 普通列表行 | `components.listRow.heightRegular` | `52dp` |
| 双行/图片行 | `components.listRow.heightTall` | `68dp` |
| 行左右内边距 | `paddingHorizontal` | `16dp` |
| 行垂直内边距 | `paddingVertical` | `11dp` |
| 分隔线 | `separatorInset`, `separatorHeight` | `16dp`, `1pt`；Android 统一渲染为 `2dp` |
| 分组圆角 | semantic `card` | `12dp` |
| 分组之间间距 | `inset.xl` | `24dp` |
| 图标与标题间距 | `inset.md` | `16dp` |
| 标题与尾部间距 | `inset.xs` | `8dp` |
| 页面内容边距 | `contentMargin.iphone.horizontal` | `16dp` |
| 最大可读宽度 | `contentMargin.readableWidth.max` | `672dp` |

列表行高度采用 `heightIn(min = ...)`，不是硬编码固定高度：中文换行、Android 字体放大和辅助功能设置可以自然增加行高，不会裁切内容。分组内部不把每一行画成独立胶囊，只有整个分组使用圆角；分隔线从 16dp 处开始。

## 使用入口

```kotlin
Ios27PageTheme(darkTheme = isDark) {
    Ios27PageContent {
        Ios27PageHeader("设置", "说明文字")
        Ios27ListSection(title = "显示", footer = "附加说明") {
            Ios27ListRow("标题", subtitle = "副标题", detail = "详情")
            Ios27ListDivider()
            Ios27ListRow("开关", trailing = { /* 原有 LiquidToggle 或 fallback */ })
        }
    }
}
```

实现文件是 `liquidglass/src/main/java/dev/liquidglass/ios27/Ios27PageTheme.kt`、`Ios27Lists.kt` 和生成的 `Ios27Tokens.kt`。token 生成器只读取 `colors.json`、`spacing.json`、`typography.json`，会写入每个源文件 SHA-256；执行 `node tools/generate-ios27-tokens.mjs --check` 可检查是否与 vendor 快照一致。

## 宿主接入规则

顶部渐进模糊使用原项目 `ui/commom/ProgressiveBlurContent.kt`（路径拼写保留）。它在 128dp 区域绘制 4dp 模糊背景，并通过 AlphaMask 在下半段逐渐淡出；这是模糊结果透明度渐变，不是逐像素改变模糊半径。演示页把它作为页面录制层之外的顶部兄弟层，传入与底栏相同的页面 Backdrop。宿主按滚动距离在 0～48dp 内逐渐显示顶部层；状态栏的顶部 inset 放在滚动内容内部，列表能滚到状态栏下方。API 33 以下传 null，沿用原文件中的纯色渐变降级。

1.0.3 已在 PLQ110 / API 36 安装验证：滚动后的顶部图标、标题受到渐进模糊，下面的列表恢复清晰，底栏继续透出内容。见 [真机截图](screenshots/top-progressive-blur.png)。该次测试进程 PID 4083 存活，crash buffer 无该进程的新记录。

宿主先绘制页面底色和列表分组，再把背景传给现有玻璃组件。从 1.0.2 起，演示底栏使用 `rememberLayerBackdrop { drawRect(colors.page); drawContent() }`，在滚动容器外侧用 `layerBackdrop` 录制视口中的实际内容；底栏在录制容器之外作为兄弟层绘制。这样列表经过底栏下方时会被采样、模糊和折射。`CanvasBackdrop` 只保留给纯色列表行中的独立控件，不能拿纯色源替代需要透视滚动内容的底栏背景。API 33 以下沿用现有 fallback 控件；不要为页面样式新增一套玻璃开关，也不要用页面 token 覆盖玻璃组件默认 style。

2026-09-12 更正：先前把启动闪退归因于页面录制递归，没有设备堆栈支持。连接 PLQ110（API 36）后，样例应用 14:57、15:03、21:56 的日志均显示 `NoClassDefFoundError: Ios27PageThemeKt`，是 APK 缺少运行时类。替换 CanvasBackdrop 不能修复这个错误。具体证据和打包修复见 [启动闪退记录](startup-crash-2026-09-12.md)。

SF Pro、SF Symbols 和 iOS 系统 safe area 不随参考仓库复制。Android 使用系统字体，窗口由宿主处理 `WindowInsets.safeDrawing`；参考中的 iPhone 数值只作为内容 token，不应直接当作 Android 状态栏或导航栏高度。

验证：

```text
node tools/verify-extraction.mjs
node tools/generate-ios27-tokens.mjs --check
gradlew.bat :liquidglass:assembleDebug :liquidglass:testDebugUnitTest :sample:assembleDebug
./tools/verify-android-artifacts.ps1
```

1.0.2 真机验证（PLQ110 / API 36）：已覆盖安装并解锁检查。滚动说明页时，底栏下方的“分组间距”文字被实际采样并模糊透出，截图见 [内容透视](screenshots/bottom-bar-through-content.png) 和 [滚动后](screenshots/bottom-bar-scroll.png)。三个页签已实际打开；组件页开关变为“已关闭”、滑块从 60% 拖到 70%、分段选择变为“收藏”、按钮计数变为 1。测试期间进程 PID 30994 持续存活，该 PID 的 crash buffer 为空。47 个原始 Kotlin 文件哈希一致。
