# Split Search 右侧按钮材质一致性

2026-10-23：组合接口新增 `searchEnabled: Boolean = true`，供 EPD 刷入入口使用。禁用时对整个独立按钮设置 disabled 语义、阻止回调并关闭交互动效；低版本点击同样禁用。默认行为、尺寸、材质和玻璃运行源码保持原样。实际 Android 禁用点击及导航回归见 `C:/IDE/Android/huawei-epd-pn532/tools/check_tabbar_ui.py --split-action`。

2026-10-11：用户反馈 `Split Search=True` 右侧按钮对下方内容的视觉效果与主 Tab Bar 不一致。

原入口主栏使用 `LiquidBottomTabsStyle`（8dp 模糊、24/24dp lens、浅/深色各自 40% 表面蒙色），右侧只传尺寸、使用 `LiquidButtonStyle.Default`（2dp 模糊、12/24dp lens、未传表面蒙色）。尺寸虽同为 62dp，静止背景光学处理不同。

修复在框架层新增 `dev/liquidglass/ios27/Ios27SplitSearchTabBar.kt` 的 `Ios27BottomTabsSplitSearch`。仅组合现有 `LiquidBottomTabsRegular` 与 `LiquidButton`；右侧 style 从主栏 track style 映射，surfaceColor 使用同一 `containerColor`，两个控件直接读取同一个宿主 backdrop。没有新增着色器、重复录制、自采样或叠加玻璃。

sample `Ios27TabBar.kt` 用 import alias 接入新框架入口，现有调用几何和回调不变：外框 62dp、指示器 54dp、间距 16dp、搜索图标仍在按钮 content 内随按钮动画移动。保留 `LiquidButton` 原有按压/拖拽动效；低版本同用主栏 fallbackContainerColor。两种控件各自的交互动作不宣称完全相同。

原始玻璃源码、提取基线、Figma 资源及旧规范文档未改。新应用应直接调用 `Ios27BottomTabsSplitSearch`，不要继续依赖旧入口搜索按钮的默认材质，也不要在页面另画玻璃覆盖来掩盖参数差异。

校验：`Ios27SplitSearchTabBarTest` 验证两种主题及自定义参数映射、原按压参数保留；`node --test tools/tabbar-split-material.test.mjs` 验证共同采样源、表面蒙色、内容插槽和原尺寸/间距。编译与源码测试不等于真机光学验收。

本次验证：规范检查、JVM 单测及 APK 构建成功；新增 3 个 JVM 测试通过，与设置密度/列表遮罩一起运行的 13 项静态检查通过，tokens 检查通过。旧 native-framework 检查为 32 项中 15 通过、17 失败；冻结文档哈希检查仍有 1 项失败，玻璃提取校验仍报 `ProgressiveBlurContent.kt` 的既有哈希差异，未修改这些基线。

APK：`sample/build/outputs/apk/debug/sample-debug.apk`，SHA-256 `66139DEC6DFEADF2382E6106557B72DC858DBBD870AECD3E221DBA466423F137`。本次 ADB 无在线设备，未安装或验证实际背景透视；需用户在同一背景下检查浅/深色的两块玻璃。
