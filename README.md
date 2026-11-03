# Tianyin LiquidGlass / iOS 26 与 iOS 27

2026-11-03：现有实现保留为 **iOS 26**，`liquidglass27` 的全部非演示机制已纳入独立 **iOS 27** 库 `:liquidglass-ios27`。包含 69 个原机制文件、5 个 shader、多级菜单、Popup、弹窗、Goo、Dock、主题/图标及配套缓存。“手机示例 → iOS 27”提供七个融入现有框架的组件示例，沿用当前导航、页面配色、分组与返回方式，不搬入源项目的整页演示。现有 sample 的导航可在“设置 → 玻璃标准”切换，旧组件示例按 iOS 26 保留。旧 `Ios27*` 名称保留兼容，标准区分与 API 入口见 [iOS 26 / iOS 27 接入说明](docs/ios27/README.md)。

本次验证与尚待真机验收的边界见 [合并验证记录](docs/ios27/validation.md)。以下旧版本记录中的 iOS 27 命名为历史页面/Figma 名称，不表示本次新增机制。

新增 MeloX 原版 TabBar 独立组件与示例页：组件 → MeloX TabBar → 原版交互。直接提取原版三层玻璃、拖动水珠、独立搜索、滚动收缩和明暗采样；播放器补回原宿主的共享封面/玻璃壳转场、可逆拖拽与弹簧回弹。不替换现有底栏，接入和来源见 [MeloX TabBar 说明](docs/melox-tabbar.md)。

1.0.19 将手机示例的默认入口接入现有框架：真实滑块、开关、步进器、输入、菜单和面板，原稿另设对照入口。复用方法与验证范围见 [可操作框架示例](docs/native-framework-examples.md)。仍在逐步对齐原稿布局，不代表全套像素验收完成。

1.0.18 新增“组件 → 手机示例库”：已接入全套 iPhone Examples 的 56 个原稿变体，并修复颜色变量、字形与遮罩渲染。当前仍有真实玻璃效果和交互待接入，**不能视为全部像素级验收完成**。代码入口、验证方法及未完成项见 [手机示例实现说明](docs/iphone-examples.md)。下方为早期版本记录。

修改前先阅读项目强制规范：[AGENTS.md](AGENTS.md)。其中固定了玻璃源码边界、背景采样防递归、Figma 配色用途、分隔线规则、Slider 阴影与端点规则，以及必须执行的校验命令。

框架接入、页面组件、Backdrop、Dialog、玻璃动画和常见问题见：[LiquidGlass 框架使用说明](docs/framework-usage-guide.md)。

TabBar 的酷安参考配置命名为[酷安参数](docs/coolapk-parameters.md)，包含 APK 提取数值、现有字段映射和接入示例。

当前 1.0.11：修正 Slider 停止时把手超出轨道的问题，左右端点改为把手实体边缘与轨道对齐；保留 1.0.10 双层阴影。新增端点、RTL 和越界回归测试。以下为历史版本说明。

当前 1.0.10：经用户授权，仅调整 Slider 静止态为原稿双层阴影，按下后渐变回原拖拽态阴影。45 个文件原样、2 个上游修复、1 个本地阴影修改，旧提取快照仍保留。[阴影修改记录](docs/slider-shadow-1.0.10.md)。以下为历史版本说明。

当前玻璃同步版本 1.0.9：保留本地定制，合入上游 `7c37be4` 的两处布局坐标引用清理修复。旧提取快照没有覆盖，46 个文件原样保留、2 个文件按合并清单校验；详见 [本次融合记录](docs/upstream-merge-2026-09-13.md)。下方 1.0.8 为页面样式版本记录。

当前 1.0.8：普通深色页面恢复 #1C1C1E 分组底色；滑块参考页单独使用原稿黑色填充。分隔线只在行间显示，玻璃组件保持原样。详见下方 Figma 适配说明。

新增：[Figma 页面适配与复用说明（1.0.5）](docs/figma-page-adaptation.md)。已读取本地 Untitled.fig，按实际节点采用 26dp 分组圆角、10dp 组间距，加入两组三条滑块演示。只调整页面层，保留全部原始玻璃源码。

- [原样代码包与复用说明](docs/liquid-glass-extracted/README.md)
- [完整实现指南](docs/liquid-glass-implementation-guide.md)
- [上游同步说明](docs/liquid-glass-sync.md)
- [iOS 27 页面与列表适配说明](docs/ios27-page-adaptation.md)

原始代码位于 `docs/liquid-glass-extracted/source/`，运行代码位于 `liquidglass/src/main/java/com/zeaze/tianyinwallpaper/`，共 48 个 Kotlin 文件（原有 47 个，加上补齐的顶部 `ProgressiveBlurContent.kt`）。保留原包名；目前 46 个文件与原提取一致，2 个底层文件合入已记录的上游修复，均通过 SHA-256 校验。

`liquidglass` 是可构建 Android library，`sample` 是外围页面/列表的演示宿主。iOS27 参考只用于页面底色、列表行、分组、文字、间距和可访问性；LiquidButton、LiquidSlider、LiquidToggle、LiquidBottomTabs 等玻璃组件的源码、默认参数、着色器、折射和交互没有被改写。

构建：`gradlew.bat :liquidglass:assembleDebug :liquidglass:testDebugUnitTest :sample:assembleDebug`。

`sample:assembleDebug` 会自动检查 AAR 的 classes.jar 及 APK DEX 类定义，防止遗漏运行时代码。[启动闪退与校验记录](docs/startup-crash-2026-09-12.md)。

首次获取源码与构建说明见 [PUBLISHING.md](PUBLISHING.md)。请使用 `--recurse-submodules` 克隆，构建时排除 `:sample:sendDebugApkToQQ`。
