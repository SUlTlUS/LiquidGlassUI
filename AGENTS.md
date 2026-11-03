# LiquidGlass Sketch 项目规范

本文件是后续 agent 修改本项目时的强制约束。用户最新明确授权可以修改某个组件时，授权范围优先于本文件，但仍必须更新变更清单和测试。

## 修改边界

- 2026-11-03 标准划分：本次合并前的 `:liquidglass` 运行实现归为 **iOS 26**；新增 `:liquidglass-ios27` 中 `dev.liquidglass27.glass` 的全部非演示机制归为 **iOS 27**。旧 `dev.liquidglass.ios27`、`Ios27*`、tokens/Figma 文件名是保留的兼容名称，不代表新版机制。
- iOS 27 标准入口使用 `Ios27GlassScene`（固定 `sketchMaterial=true`）或 `Ios27BackdropScope`（已有独立 Backdrop）。共享光学仍由 `:liquidglass` 提供，禁止形成反向依赖或复制第二份核心。API 33 以下由宿主切回 iOS 26；不得压制 `NewApi` 或用 `overrideLibrary` 绕过安装下限。
- 按用户 2026-11-03 后续修正，标准 27 底栏使用 `Ios27NavigationDock`：复用 26 的 `LiquidBottomTabsRegular` 主体，仅注入 27 高光。`LiquidBottomTabs.kt` 的可选 `surfaceLayer` 与 `DrawBackdropModifier.kt` 的材质层钩子是本次授权的核心扩展，默认 null 保留 26 行为；原哈希保留并在 `reviewedLegacyChanges` 中逐项审核，不刷新旧基线。Popup 与菜单共用 `SketchMenuRootSurface`、圆角过渡和高光；内容等比缩放，不使用宽高独立拉伸。
- 底栏截图反馈后，`DrawBackdropModifier.kt` 增加默认 null 的 `surfaceLayerBlock`：27 高光必须放在外阴影之后的独立材质层，不得再注入参与背景逆变换的 `layerBlock`，也不得用外层裁切隐藏矩形阴影。两个已授权核心文件的改动均保留基线并单独审核。
- 机制库不包含演示 UI。按用户最新“演示要融入当前项目，不要直接把原来的搬过来”的要求，七个组件示例由 `sample/src/main/java/dev/liquidglass/sample/Ios27NativeExamples.kt` 实现，从“手机示例 → iOS 27”进入，复用现有导航、页面框架、tokens 与新机制库；禁止重新导入源项目的 `GlassShowcase` / `GlassVerificationPage`。新机制源码、资产、工具、测试及宿主改动哈希在 `docs/ios27/merge-manifest.json`；旧运行源码哈希不可通过更新清单掩盖修改。新机制修改后需维护清单并执行 `node tools/verify-ios27-merge.mjs`、`node --test tools/ios27/native-examples.test.mjs`、新库单测、Lint 和产物校验。接入说明见 `docs/ios27/README.md`。用户本次明确要求先完成代码和构建，真机由用户验收。

- `liquidglass/src/main/java/com/zeaze/tianyinwallpaper/` 是玻璃运行源码。默认只读，不得整目录覆盖。
- 原始基线在 `docs/liquid-glass-extracted/source/`，不能刷新或覆盖来掩盖改动。
- 允许改玻璃源码时，只改用户明确授权的文件；把文件、原因、baseline SHA-256、merged SHA-256 写入 `docs/liquid-glass-merged-files.json`。
- 页面、列表、颜色、间距和 Figma 适配放在 `dev/liquidglass/ios27` 或 sample 宿主；不要通过修改玻璃组件解决页面布局问题。

## 背景采样安全

- 按用户“所有按钮都可以 goo”的要求，iOS 27 通用 `ElasticGlassButton` 可通过 `GooGlassGroup` 参与融合，前景与输入保留在按钮自身。同排同材质融合，组内按物理位置排序并遵守每批六个 shader 上限。Popup 隐藏来源时必须同步退出共享 Goo 材质，不能只隐藏前景；分组背景仍使用独立兄弟采样层。

- 页面背景先用独立 `LayerBackdrop` 录制，玻璃控件和浮动底栏作为兄弟层采样。
- 禁止让包含玻璃控件的父层通过 `layerBackdrop` 录制自身，否则可能触发 RenderThread/Skia 递归闪退。
- 纯色列表内的行内玻璃控件使用颜色一致的独立 `CanvasBackdrop`。需要透出图片的 Inline 面板必须读取独立背景兄弟层的 `LayerBackdrop`，不得读取录制了自身的父页面。
- 顶部 `ProgressiveBlurContent` 和底部玻璃栏必须位于被采样页面之外。
- 修改图片背景、Inline 或 Popup 前，先读 `docs/glass-backdrop-integration.md`，按其中的源码入口复用。禁止把整张图片缩放到玻璃尺寸伪装成位置正确的背景采样。
- 外部阴影需要 `drawBackdrop(...).clip(shape)` 的顺序；禁止在玻璃绘制外层先 `clip(shape)` 裁掉阴影。
- 参数必须注明来自 Figma、上游 Dialog 或本地适配；不能把上游的 24/48dp 折射称为 Figma 原值。
- 视觉验收必须检查玻璃覆盖范围内的对应内容。截图仍像实色卡片或滚动未产生位移时，不得宣称透视或滚动验收完成。

## Figma 页面规范

### 可操作示例入口

- 1.0.19 起，“手机示例库”默认必须进入 `NativeExamplePage`，复用 `dev/liquidglass/ios27/Ios27InteractiveControls.kt` 和已有玻璃控件。禁止再次把矢量查看器作为默认应用页面。
- `FigmaSceneView` 只用于显式“原稿对照”。对照往返使用 `SaveableStateProvider(entry.key)` 保留原页面状态。
- 点击测试必须验证实际结果：数值变化、开关状态、输入提交、面板保存、菜单动作；仅验证能打开页面或出现控件不够。
- 新增页面色值仍来自 tokens；紧凑滑块行采用 `showLabel=false`，保持 52dp 行高。控件的主题由页面颜色决定，不要求调用者猜玻璃参数。
- 新接口与验证入口见 `docs/native-framework-examples.md`。新增静态检查：`node --test tools/figma-reader/native-framework.test.mjs`；操作回归：`node tools/figma-reader/check-native-interactions.mjs <设备序列号>`。

全套手机示例的新增范围来自用户提供的 `iOS and iPadOS 27 (Community).fig`，解包在 `docs/figma-community-source/`。iPhone Examples 共 56 个原稿变体，入口是组件页的“手机示例库”。历史 `Untitled.fig` 的 Slider 规范仍适用于旧参考页，但不再代表整个项目的示例范围。

- 手机示例资源由 `node tools/figma-reader/export-examples.mjs` 生成到 `sample/src/main/assets/iphone-examples/`，不得手工改生成 JSON 掩盖解析错误。
- 原稿颜色变量、实例覆盖、字形轮廓、独立四角圆角和遮罩必须保留；实例身份用 `instancePath`，不能用重复的源节点 ID 存交互状态。
- 原稿矢量渲染不等于已有 LiquidGlass 组件的真实折射。当前缺口见 `docs/iphone-examples.md`，不得把 56 页可打开宣称为全部像素级或交互验收通过。
- 修改示例解析器后必须执行 `node --test tools/figma-reader/examples.test.mjs`，并在重新构建安装后重拍受影响页面。`docs/iphone-examples-previews/` 是简化渲染，不是 Figma 原图金标准。

早期 Slider 参考页的本地原稿是 `docs/Untitled.fig`，目标为 `Examples/Slider`。这一单页仍使用 `tools/figma-reader/read.mjs` 读取；新增全套手机示例使用上面的完整原稿与导出工具，不要混用来源或凭截图猜参数。

- 页面浅色 `#F2F2F7`，普通分组浅色 `#FFFFFF`。
- 页面深色 `#000000`，普通分组深色 `#1C1C1E`。
- Slider 原稿参考页可单独使用黑色分组，必须通过 `Ios27PageSurface.SliderReference` 选择，不能污染普通分组页。
- 普通行 52dp，双行 68dp，水平内容内距 16dp。
- 本地 Slider 示例分组圆角 26dp、组间距 10dp；不可将这两个值误套到玻璃按钮或其他页面。
- 分隔线只放在行与行之间，左右各缩进 16dp，厚度 1dp；每组第一行上方禁止画线。
- 多行连续列表优先使用 `Ios27ListRows(items) { ... }`：它自动套用规范分隔线，首行不会出现多余顶线；只有跨行自定义布局才直接使用 `ios27RowTopSeparator`。
- 颜色必须来自 `Ios27Tokens.kt`，该文件只能由 `node tools/generate-ios27-tokens.mjs` 生成。

## Slider 规范

- 把手静止定位范围是 `0 .. trackWidth - thumbWidth`，左右端点是把手实体边缘与轨道边缘对齐。
- 不得使用会让把手中心在端点停留的旧范围 `-thumbWidth/4 .. trackWidth-thumbWidth*3/4`。
- 40dp 把手的静止位置不得超出轨道；按压放大产生的视觉阴影外扩不等于静止实体越界。
- 当前静止阴影为本地 Figma `Knob` 的两层：13dp/下移6dp/黑12%，以及 4dp/下移0.5dp/黑12%。按压态恢复原有 4dp/黑5% 近层。
- Slider 的数值映射、Float 状态、左右释放和 `onValueChangeFinished` 必须一起测试。

## 上游同步规范

- 上游仓库是 `vendor/AndroidLiquidGlass`，不要只升级 Gradle 版本号。
- 先对比上游 commit，再手工融合；不得直接覆盖本地 Button、Toggle、Slider、BottomTabs 的产品定制。
- 保留低版本降级、外部主题参数、触觉反馈、回调、边界保护和本项目防递归层级。
- 上游同步和用户授权的本地组件修改都必须进入 `docs/liquid-glass-merged-files.json`。

## 必须执行的校验

```powershell
node tools/verify-extraction.mjs
node tools/generate-ios27-tokens.mjs --check
\.\gradlew.bat :verifyLiquidGlassSpec :liquidglass:testDebugUnitTest :sample:assembleDebug --no-daemon --console=plain
```

构建成功不等于真机验收成功。连接设备后还要检查启动、浅/深色、滚动时顶部模糊、底栏透视、Slider 两端停靠和 `logcat` 崩溃日志。
