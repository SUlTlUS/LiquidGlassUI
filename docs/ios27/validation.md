# 动画独立切换

GlassEffects.animation 与设置页新增默认 / 26 / 27。默认保留按钮 26、菜单和模态 27；明确选择后覆盖按钮、Goo、菜单、Popup、子菜单、模态动画。26 弹窗弹簧取自原 LiquidMorphController（.73/120，关闭初速度 -2.5），继续驱动现有框架的形变路径；27 按钮复用 FlClashElasticMotion，输入仍由原材质按钮持有。MeloX 底栏与开关、滑块的共用动画保持不变。

Debug APK 与三个 Release AAR、本地 Maven 包已构建。按用户要求未运行测试、Lint、验证脚本或真机交互；补充了组合策略和弹簧路由检查源码。ADB 无设备，尚未安装，视觉验收由用户完成。

<!-- animation-switch-end -->
# 整体标准与效果单独切换

新增 GlassEffects(material, highlight) 与 GlassEffectScope；null 跟随整体，明确值只覆盖该效果。菜单颜色、来源阴影、底栏形状与高光解耦，26 按钮及 Goo 连接可独立使用 27 高光。设置持久化单项选择，整体切换会清除覆盖。按压/拖拽及共用动画不变；开关、滑块保持两标准共用实现。

Debug APK、三个 AAR、源码与本地 Maven 分发包构建通过。按用户要求不运行测试、Lint、验证脚本或真机交互；已补路由组合测试源码。ADB 未发现设备，尚未安装，效果待用户验收。

<!-- effect-switch-end -->
# 组件库 API 2.0

公开 API 迁入 dev.liquidglass；主题/26-27 路由下沉到库，底栏使用 GlassTab + GlassTabBarLayout，删除 Dock 别名、重复 Scaffold、独立旧演示模块、低版本回退控件和无效按钮参数。原始光学、拖动/按压、同色 Goo、阴影与采样修复保留。

Debug APK 与三个 Release AAR、源码包、本地 Maven 元数据已构建。只检查发布产物结构和传递依赖；按用户要求未运行测试、Lint、验证脚本和真机交互。安装状态以 verification-results.json 为准，视觉与交互待用户验收。

新 API 见 [组件库指南](../component-library.md)；文件更名/删除及前后哈希见 [变更清单](../component-library-changes.json)。没有修改原始提取快照，没有远程发布。

<!-- component-library-api-end -->
# 标签栏演示顶部模糊与底色

移除标签栏演示页 rememberPopupBackground / popupBackground，不再读取全局图片。页面保持 colors.page 底色，彩色列表内容继续用于观察材质。顶部改用通用 ExampleTopChrome，底色由固定黑色改为 colors.page，scrollDriven=true，按列表开始 30dp 滚动距离从 0 渐显至 1，首项离屏后保持 1。顶部模糊和返回按钮仍在采样层外，公共模糊、底栏与参数开关实现不改。

Debug 构建通过；更新了对应检查但按用户要求未运行测试、Lint、验证脚本或真机交互。滚动与外观待用户验收。

[APK](../../dist/tabbar-top-blur-20261103/LiquidGlassSketch-tabbar-top-blur-debug.apk)；SHA-256 `aabd8ed81442698f7284b105759830077ab5b73da78b830853f7936de3c8d675`。

---

# 标签栏功能与布局参数调节

一级“标签栏”直接进入唯一合并演示页，调整项就在演示页内。用户确认只需要现有功能与布局，不包含底层玻璃数值。布尔项全部使用 Ios27SwitchRow / LiquidToggle，尺寸使用玻璃滑块；支持紧凑、收缩、自动收缩/展开、毛玻璃、明暗采样、播放器、搜索、安全区、图标/文字、字体，以及玻璃标准/预设/主题/选中颜色、标签数量与名称、展开高度/水平边距/底边距/搜索间距和恢复默认。参数为页面本地状态；标准和预设默认跟随设置。新统一接口仅转接现有功能，默认不改主界面效果。普通/分离搜索/收缩播放器三个入口合并，旧示例键与组件 API 兼容转接，清理 NativeExamplePage 重复标签栏状态与绘制；保留原稿对照。行内玻璃控件使用独立 CanvasBackdrop，底栏仍采样兄弟页面，未修改光学、拖拽及颜色融合机制。

Debug 构建通过；已维护检查用例但按用户要求未运行测试、Lint、验证脚本或真机交互。实际交互与外观待用户验收。

[APK](../../dist/tabbar-controls-20261103/LiquidGlassSketch-tabbar-controls-debug.apk)；SHA-256 `46997fc86fa0f483dbff491b1c5b0ec4c4950a3de60f6976fd7bc2623065f654`。

---

# 27 同色覆盖按钮的 Goo 连接颜色

用户确认相邻透明按钮使用相同覆盖色，融合处也必须覆盖 27 默认色，26 不处理。旧代码按整组 GlassStyle 差异启用单按钮颜色裁切；现在按实际 tint 判断，混色组内的连续同色按钮共享包含连接处的 Goo 颜色轮廓，异色边界仍不混色。颜色只绘制一次，不在连接处额外叠加默认色。缓存按同色段隔离，保留轮廓复用。26、拖拽参数、材质采样和高光未修改。

补充同色段/异色隔离回归用例，按用户要求未运行测试、Lint、验证脚本或真机交互。Debug 构建通过，视觉验收待用户。

[APK](../../dist/same-tint-goo-20261103/LiquidGlassSketch-same-tint-goo-debug.apk)；SHA-256 `4997484f6ac7e32d2d7090592b37c9e5d1146633790c651bb3369015ad8d31ed`。

---

# 27 按钮拖拽幅度回调

针对上一版正常拖动也过小的反馈，将向量限幅响应距离从一个长边改为三个长边；外扩余量前 65% 保留原幅度，最后 35% 使用 tanh 平滑衰减。16dp / 8dp 外扩限制、按压放大、释放弹簧和 26 实现不变。参数为本地适配。

已补充普通拖动保留原拉伸幅度的回归用例，按用户要求未运行测试、Lint、验证脚本或设备交互。Debug 构建通过，视觉效果待用户验收。

[APK](../../dist/button-drag-retune-20261103/LiquidGlassSketch-button-drag-retune-debug.apk)；SHA-256 `ad9822e3bc8bac35c8f9561c4cc7bba48f74609fe2431509e9d3a9652926df11`。

---

# 27 按钮拖拽距离衰减

原因是旧移植公式仅对平移使用 tanh，拉伸项仍乘原始拖拽距离，可能线性增大并超出 Goo 容器。共用 ios26ButtonDragTransform 现在先对拖动向量作平滑限幅，再让平移和拉伸共同消耗外扩余量；静止按压和悬停贡献先扣除，已有按压放大及释放弹簧保持。普通按钮/分组采用现有 16dp 参数，GooGlassRow 轮廓与前景同时使用已有 8dp 留白。没有扩大容器或新增裁切。

新增距离限幅、响应衰减、方向对称、密度等价、小容器与按压不变的回归用例，按用户要求未执行。Debug 构建通过，未运行测试、Lint、验证脚本或设备交互，视觉效果待用户验收。

[APK](../../dist/button-drag-resistance-20261103/LiquidGlassSketch-button-drag-resistance-debug.apk)；SHA-256 `f46b02e49519547dffd20eafe91f192201bdf1e6b71878f44e5d0750752fcfaa`。

---

# 透明与彩色按钮 Goo 连接

原因是 Goo 以完整材质作为分组条件，颜色不同就拆组。27 改为同排同采样源共享轮廓，异色分组的颜色分别按自身实时轮廓裁切，连接处不铺色。26 忽略分组键中的颜色差异，仍检查采样、光学参数及亮度状态；原按钮继续自己绘制，连接覆盖边缘时分别恢复两端颜色。遵从用户“彩色留在原按钮内”：没有渐变、颜色混合、新光学着色器或多层材质合成。保留同色路径、六形状上限、物理排序和 Popup 隐藏来源机制。

Debug 构建通过；未执行测试、Lint、验证脚本或设备交互，效果待用户验收。

[APK](../../dist/mixed-goo-20261103/LiquidGlassSketch-mixed-goo-debug.apk)；SHA-256 `4935fa695265104cde7442f7eaceb0306817afd4e2373760908a3a0c52eb8202`。

---

# 旧底栏重复实现清理

运行源码净减少 971 行（包含注释）。移除无调用的旧通用底栏绘制、Compact / Regular / SplitSearch 封装及样式类；删除无调用的 Ios27BottomTabsSplitSearch 和宿主 Ios27StandardDock。GlassNavigationDock 改为兼容转接，删除另一套绘制和手势。LiquidBottomTabs.kt 仅保留共享高光层枚举；分段选择器仍使用的 LiquidBottomTab 及其缩放 provider 保留。

当前 UnifiedGlassTabBar、MeloXBottomChrome、MeloXBackdropComponents、MeloXTabBar、GlassNavigationStyle、Ios27NavigationDock 的运行实现均未修改，参数预设、26/27 高光、紧凑模式、收缩、搜索和播放器保持原调用路径。原始源码及历史哈希不刷新，两个核心文件的用户授权修改和三个文件的删除单独登记。

Debug 示例和独立 MeloX demo 构建通过；未运行测试、Lint、验证脚本或设备交互。构建/安装不等于视觉或交互验收。

[APK](../../dist/deduplicate-tabbar-20261103/LiquidGlassSketch-deduplicate-tabbar-debug.apk)；SHA-256 `4ee9e8a0b422516ab612011ac6bad7a099b7a4ca4b4847aac2137a6a889bdaa2`。

---

# 底栏紧凑宽度参数

UnifiedGlassTabBar、Scaffold 和 Ios27NavigationDock 新增 compact，默认 false。开启时按 64dp × 标签数量、主体内距、独立搜索和外边距限制宽度，并居中；窄屏不溢出。明暗采样边界跟随收窄后的底栏；与 minimized 独立。标签栏的收缩/搜索/播放器示例提供开关，主界面默认保持原宽度。

Debug 构建通过。未运行测试、Lint、验证脚本或设备交互，实际效果待用户验收。

[APK](../../dist/compact-tabbar-20261103/LiquidGlassSketch-compact-tabbar-debug.apk)；SHA-256 `bf9c6d44632672a9689e2ca034f0a2183b9bae4187acfc1dc2eba0169fbeae8e`。

---

# 统一底栏：MeloX / 通用参数

主界面、普通和分离搜索底栏、收缩与播放器示例统一使用 UnifiedGlassTabBar。设置 → 底栏玻璃参数可切换 MeloX / 通用，默认 MeloX；26 / 27 高光独立选择。保留三层采样、水珠拖动、收缩、独立搜索与播放器选项，MeloX 光学调用改用 Sketch 共享核心。旧 Ios27NavigationDock 作为兼容入口；MeloX 深链仍可打开，入口合并进标签栏分类。

Debug 示例与独立 MeloX demo 构建通过；未执行测试、Lint、验证脚本或设备交互。不把构建/安装成功视为材质、动画或交互已验收。

[APK](../../dist/unified-tabbar-20261103/LiquidGlassSketch-unified-tabbar-debug.apk)；SHA-256 `713cd1fa4f4e184b66b60c8ca035773764007cbd8f70d3a95acf682b93b94ece`。

---

# 26 玻璃模态按钮：保留采样颜色

用户确认截图中的按钮未按压，静止主体仍显得更亮；此前归因为按压白光不成立。本次修正静态颜色路径，不把尚未真机验收的亮差根因写为已证实。

LiquidButtonStyle 增加 preserveBackdropColors，默认 false。仅 26 Fullscreen / Inspector 的按钮启用，避免对已处理的面板材质再次执行 vibrancy 或自适应颜色滤镜；独立模糊、折射、高光、阴影及按压反馈保留。融合连接处遵守相同选项，避免按钮与连接处颜色不匹配。采样继续保留完整坐标变换，但改为导出面板处理后的材质，不包含面板边缘高光，防止再次折射该高光。没有添加白色/黑色覆盖层或降低亮度的补偿参数。

保留两个核心文件的旧基线哈希，并逐项登记本次用户授权修改。其余按钮默认行为、27 模态按钮、纯色 Fullscreen Stack、Popup 动画不变。Debug 构建通过；未执行测试、Lint、验证脚本或设备交互。静止亮差是否消除待用户验收。

[APK](../../dist/modal-static-color-20261103/LiquidGlassSketch-modal-static-color-debug.apk)；SHA-256 `9625eb3b670b7f12690847f7db38544a9d223194fb60eb93386b9f2ffb362c18`。

---

# Popup 按压采样与 26 玻璃模态按钮

Popup 的父页面与面板表面分别包装为 TransformAwareBackdrop，再合成供内容中的按钮采样。通过 LayoutCoordinates.localPositionOf 的原点与两条基向量计算每一来源到绘制目标的二维仿射变换，同时处理按钮缩放与 Popup 父级按压变换；不再只偏移来源原点，也不重复应用 layerBlock 的逆变换。该适配位于 dev/liquidglass/rendering，旧核心源码与默认采样行为不变。

按用户澄清，仅调整 26 Fullscreen / Inspector：显式导出面板最终绘制表面（包含高光），按钮读取该独立兄弟采样层，并使用原 26 默认按钮材质、44dp 高度和零内容内边距。取消这两个页面的工具栏专用 6dp 模糊与 30/21dp 折射覆盖，没有新增颜色叠层。27 模态按钮和纯色 Fullscreen Stack 保持原参数。

Debug 构建通过。新增矩阵用例覆盖中心按压、共同祖先缩放、旋转非均匀缩放及退化输入；按用户要求未执行单测、Lint、验证脚本或设备交互。实际视觉效果待用户验收。

[APK](../../dist/nested-glass-20261103/LiquidGlassSketch-nested-glass-debug.apk)；SHA-256 `9baa030dc6c5d73082f10d1ff6b8e1440ef0f0d37e912af1d98efa4e40adeb57`。

---

# Popup 收回阴影裁切回归

通用演示接入 26 Popup 时将按钮完整材质录制为按钮尺寸的位图，外扩阴影在录制阶段被裁掉。收回末尾交接到该位图时出现矩形边界。

GlassPopup 新增可选 sourceSurface 实时来源材质槽；通用 Popup 的 26 分支使用原按钮与独立预览背景。透明度层在原按钮四周额外测量 Shadow.Default.radius × 2 + 最大阴影偏移的留白（当前 52dp），缩放仍按原按钮中心及目标液滴尺寸计算。保持现有交接时机、材质、动画曲线和内容采样层级。27 分支与其他位图来源调用保持现状。未修改旧核心运行源码。

Debug 构建通过。静态用例更新为实时来源及阴影留白契约；按用户要求未执行测试、Lint、验证脚本或设备交互。阴影实际效果待用户验收。

[APK](../../dist/popup-shadow-20261103/LiquidGlassSketch-popup-shadow-debug.apk)；SHA-256 `24a11a68624184b2caeeb4b1d9cdf9c73803c41d665a2f34872a9e4fe0f3b8db`。

---

# 通用演示与标准切换

移除独立 27 按钮、Goo、底栏、对话框演示页，保留机制。多级菜单收入菜单分类；Popup、主题与图标收入通用演示。MeloX TabBar 与 Listening Mode 从一级列表直接打开，ColorOS 17 收入二级分类，三者保持原版。

普通示例通过 DemoStandardScope / DemoGlassButton 跟随设置的 26/27；26 委托原按钮，27 使用新按钮。开关、滑块等无独立 27 实现的组件共用原实现。菜单、模态共用框架，高光随标准切换；原有纯色/玻璃面板规则不变。底栏普通与搜索分体布局保持既有几何，27 注入统一高光轮廓。

融合组显式读取独立背景，额外绘制空间不改变布局；Popup 内按钮继续读取合成采样层。来源隐藏时同时注销 26/27 融合，26 Popup 捕获原按钮材质用于返回交接。未修改 com/zeaze 下核心源码。

构建通过；测试脚本仅更新路由契约，未执行测试、Lint、验证脚本或设备交互。实际显示及交互待用户验收。

[APK](../../dist/common-demos-20261103/LiquidGlassSketch-common-demos-debug.apk)；SHA-256 `61b620e5e9285a6bed7dd9e6d0434891a85b20328283c8eb8205ca3aa7f68a66`。

---

# 27 按钮：按压放大使用 26

按钮新增独立 buttonPress 动画，按下与松手均采用 InteractiveHighlight 的 spring(0.5, 300, 0.001)。放大比例为 1 + LiquidButtonStyle.Default.pressedExpansion / 按钮高度 × 按压进度，即原 26 的 4dp 等比扩张，不再使用 27 的触摸按压膨胀公式。

普通按钮、GooButtonGroup 和 GooGlassRow 使用相同按压进度，保持玻璃外形与前景一致。原 27 鼠标悬停贡献保留；拖拽计算、材质、颜色、高光、融合参数、点击识别及菜单/底栏不变。没有修改 26 核心源码。

构建通过；按用户要求未运行测试、Lint、验证脚本或设备交互。按压手感待用户验收。

[APK](../../dist/ios27-button-press26-20261103/LiquidGlassSketch-ios27-button-press26-debug.apk)；SHA-256 `d13dc0b002f982affd7e9694bedfd21067aa2ecea33cccba3c5ac6d60c23136b`。

---

# Popup 内部玻璃按钮采样层级

GlassPopup 原先让内容继承页面的 LocalSketchGlassBackdrop，内部按钮跳过弹窗表面。现在只在独立的表面 Box 上录制 popupSurfaceBackdrop，并为内容提供父背景与该表面的组合采样源。来源按钮、来源快照、输入内容和玻璃按钮均在录制层之外，采样保持页面→弹窗表面→内部按钮的单向依赖。

Popup 内容清空继承的页面 Goo 注册组，避免跨表面合批；内容内部仍可自建 GooGlassGroup。未修改玻璃参数、高光、按钮拖拽或 Popup 动画。无 Sketch 背景的兼容路径继续使用原有捕获管线。

构建通过；按用户要求未运行测试、Lint、验证脚本、启动或设备交互。实际采样效果待用户验收。

[APK](../../dist/popup-content-backdrop-20261103/LiquidGlassSketch-popup-content-backdrop-debug.apk)；SHA-256 `3405e4eed3f647f27940c087f4f7f8321612d7441d25afd04ff6cbdd3194ce09`。

---

# 27 按钮：仅拖拽采用 26 形变

ElasticGlassButton 与 GooGlassRow 的拖拽使用原始指针位移，复用 LiquidButtonStyle.Default 的 4dp 拉伸量与 0.05 位移导数，按 LiquidButtonCore 的 tanh 位移、方向角及宽高比计算形变。松手后的位移归零采用 InteractiveHighlight 的 spring(0.5, 300)；按压/悬停放大仍使用原 27 lift。

elasticPress 的新分支默认关闭，仅按钮调用启用。GooButtonGroup 和 GooGlassRow 共用同一按钮形变函数，使共享玻璃外形和按钮前景一致。材质、颜色、高光、点击识别、菜单/底栏及其他控件不变。没有修改 26 核心源码。

构建通过；按用户要求未运行测试、Lint、验证脚本或设备交互。拖拽手感待用户验收。

[APK](../../dist/ios27-button-drag26-20261103/LiquidGlassSketch-ios27-button-drag26-debug.apk)；SHA-256 `067f990a0ea68338bd99cea3a7913e578c6741db375b7e6d5f9f960b3793342f`。

---

# 菜单收缩：消除内部交叉孔洞

FlutterRSuperellipse 从底边向右、上、左绘制；squircleRect 的中间幂次轮廓从顶边向右、下、左绘制。此前直接 addPath 两者，再与 bridge 做并集，会先按 WINDING 填充规则抵消重叠区域，剩余孔洞又被 26 高光描边，表现为文字后方白色交叉轮廓。

现在先独立对 platter 和 droplet 做 Path.Op.UNION，再对已合并实体与 bridge 做并集。桥接运算失败时回退至实体并集；实体并集失败时暂保留面积较大的实体，禁止重新拼接反向重叠轮廓。不修改弹簧、采样、颜色或高光样式，影响共用 GooPairOutline 的菜单形变路径。

构建通过；按用户要求未运行测试、Lint、验证脚本或设备交互。截图对应收缩帧的实际结果待用户验收。

[APK](../../dist/menu-winding-union-20261103/LiquidGlassSketch-menu-winding-union-debug.apk)；SHA-256 `846aaf005d8682eb6781e2c5ddc3af8337e4087fe1884271e1ae95840dd3bdfe`。

---

# iOS26 menus: no surface tint

Remove the color wash from iOS26 root and nested menu surfaces, including ordinary/context menus and text selection menus. GlassMenuMaterialScope keeps the policy local to menus; iOS27 menu tint and dialog/sheet materials are unchanged. Existing blur, refraction, shadows and iOS26 highlights remain. Legacy text menu optics also omit surface/overlay tint.

Build passed. Tests, Lint, verification scripts and device interaction were not run at user request. Visual acceptance remains with the user; installation status is recorded in verification-results.json.

[APK](../../dist/ios26-menu-no-tint-20261103/LiquidGlassSketch-ios26-menu-no-tint-debug.apk); SHA-256 08df77a91c5bdb3784749fa1b8d1b7a682135f138c3383eca26b9134b10075c5.

---

# 26 菜单材质交接：外阴影绘制范围

sourceSurface 淡入时 graphicsLayer 的 alpha 会产生离屏绘制，原图层仅有按钮本体大小，导致外阴影被矩形截断。现在按 Shadow.Default 的两倍半径加最大偏移预留四周空间（当前为 52dp），padding 放在 alpha 层内，原按钮仍保持 anchor 的尺寸。自定义测量和放置保持按钮中心及形变中心一致，并避免扩大的图层受视口约束挤压按钮。

未修改阴影参数、圆角、材质、回弹和背景采样。构建通过；按用户要求不执行测试、Lint、验证脚本或真机交互。安装结果见 verification-results.json，收回末尾外观待用户验收。

[APK](../../dist/menu-shadow-bounds-20261103/LiquidGlassSketch-menu-shadow-bounds-debug.apk)；SHA-256 `54d4bcaaf67270fc411bec8ec4a02c43bfb58489a6e622715d26012ca0ebbd61`。

---

# 26 多级菜单：返回原按钮材质

GlassMorphMenu 新增默认 null 的 sourceSurface，宿主为 26 菜单提供与来源一致的 LiquidButton：相同独立 previewBackdrop、尺寸、文字和默认材质。收回最后 180ms（320–500ms）将菜单材质及来源文字渐隐，同时渐显实际来源按钮，动画结束前完成材质交接。展开初段进行反向交接。时间参数沿用本项目 Popup 的本地材质交接，不改变 27 弹簧或菜单几何。

来源表面仅在交接时组成，去除重复无障碍语义；实时读取独立背景，不录制自身、不使用截图替代玻璃。默认 null 保留 27 和其他调用者行为。未修改 26 核心材质或拖拽。

仅构建和安装；按用户要求未运行测试、Lint、验证脚本、启动或设备交互。颜色衔接效果待用户验收。

[APK](../../dist/menu-source-handoff-20261103/LiquidGlassSketch-menu-source-handoff-debug.apk)；SHA-256 `66b665abac26b105edb2f547f1fe5aa6ee69ecc1b40dc21966a5e641a47f400d`。

---

# 多级菜单演示统一入口

移除独立 Menu26 示例项，仅保留“菜单 → 多级菜单”。MainActivity 将持久化的 glass_standard 状态通过导航传入示例：iOS 26 使用原 LiquidButton 来源按钮及 26 高光；iOS 27 使用 ElasticGlassButton 及 27 高光。两者继续共用 GlassMorphMenu 多级菜单、长列表和选择结果。标准切换时重建该示例的临时弹层，避免残留旧标准的菜单动画。

保留 ios27-menu 路由；旧 ios26-multilevel-menu 外部入口及已保存 Example 路由映射到同一页面。其他 27 专属演示保持原标准。本次只修改宿主导航和示例，不修改机制库、玻璃核心或背景采样关系。

构建通过；现有路由断言随入口更新，按用户要求未执行测试、Lint、验证脚本或设备交互。安装结果见 verification-results.json，设置切换与菜单视觉由用户验收。

[APK](../../dist/unified-menu-20261103/LiquidGlassSketch-unified-menu-debug.apk)；SHA-256 `4651d89f99e4686084ca543381f10ac32eacebc8433ed61d97f3181c00a33454`。

---

# 26 融合接缝：材质和高光渐变过渡

移除连接区域向按钮内部仅覆盖 0.75dp 的硬裁剪，改为对连接材质和高光整体应用预乘 alpha 渐变。连接中心保持覆盖，向原按钮内部以 1–6dp 过渡；旧内缘附近另保留 1–3dp 覆盖范围。连接影响范围根据两胶囊的距离差渐隐，融合外轮廓仍由原 Goo 路径裁剪。这些距离为本地接缝适配值，不是 Figma 参数。

画布预留 40dp（32dp 采样及 8dp 过渡）范围。原按钮本体的采样、材质、输入和拖拽逻辑本次没有修改。新 shader 仅处理连接绘制的透明度，不增加背景录制层。

构建通过；按用户要求不运行测试、Lint、验证脚本、应用启动或设备交互。安装结果见 verification-results.json；无缝程度和拖拽时的视觉效果待用户真机验收。

[APK](../../dist/ios26-fusion-seam-20261103/LiquidGlassSketch-ios26-fusion-seam-debug.apk)；SHA-256 `e16ddbfe04488f67f5c0262677b90d04ae38b9093551515f4c5a29d0ffd0b139`。

---

# 26 融合：只绘制连接区域

LiquidButtonCore 仅新增布局及原始 graphicsLayer 形变观察接口；没有替换原 drawBackdrop、Capsule、lens、InteractiveHighlight、clickable 或手势。移除观察接口新增文本后，源码 SHA-256 仍为原始 `85c716d3f55e3d7f9d8d1202f08bca6cdb662a081275c1a97f8cc8cea8845cdf`。

Ios26FusionGroup 接入顶部、底部和列表详情的相邻工具按钮。同背景、同材质、同排按钮靠近时，复用 27 gooRowOutline 和路径缓存，绘制融合轮廓减去原按钮轮廓之后的连接部分，接缝向内覆盖 0.75dp。连接处使用坐标对应的独立 Backdrop、Goo SDF 折射和 26 高光；保留原按钮独立采样和光学尺寸。不接触时没有连接绘制。原按钮阴影及内容继续由原组件绘制，没有生成整组替代材质。

连接画布预留 32dp 采样范围；其光学形状使用已有 CornerBasedShape 适配，避免再次把任意路径传入旧 lens。没有使用 Flutter，未建立旧库到新库的反向依赖。

仅构建并自动安装，未运行测试、Lint、应用启动或设备交互。接缝和拖动时的实际视觉效果待用户验收。

[APK](../../dist/ios26-fusion-neck-20261103/LiquidGlassSketch-ios26-fusion-neck-debug.apk)；SHA-256 `de8706b47067eeb58367e11982d549a3564ec3faf49d9f59b454149b702d410e`。

---

# 恢复 26 原版按钮玻璃与交互

错误接入把单按钮自身尺寸的折射改成了整组画布计算，并替换了按压/拖拽实现。现移除应用级 Ios26GooButtons 适配和 26 工具栏的 Goo 分组包装，删除新增的替代绘制器和共享注入入口。LiquidButtonCore 恢复为接入前的逐字节源码，SHA-256 `85c716d3f55e3d7f9d8d1202f08bca6cdb662a081275c1a97f8cc8cea8845cdf`。模糊、折射、采样坐标变换、着色、高光、阴影及原 InteractiveHighlight 交互均回到该实现。

26 的此次 Goo 接入已撤回，不宣称 26 Goo 已完成。27 原有 Goo 保留；文本菜单退出、上下文菜单返回、日期时间源材质交接等独立修复保留。原始提取基线未覆盖。

仅构建并自动安装，未测试、未启动应用或操作设备界面。实际效果待用户验收。

[APK](../../dist/ios26-original-buttons-20261103/LiquidGlassSketch-ios26-original-buttons-debug.apk)；SHA-256 `aebcf1629144fb1e47c744f4fa34b3fa6b92dbb7650a1cf08b8d014f57b12e6c`。

---

# 二级页闪退：26 Goo 光学形状修正

设备 crash buffer 记录：13:44:15，dev.liquidglass.sample，PID 26106。UnsupportedOperationException: Only RoundedRectangularShape or CornerBasedShape is supported in lens effects。调用来自 Ios26GooButtons.legacyButtonMaterial。

原因是新增 Goo 分组把任意路径 Shape 传入 lens。现复用 SketchGlassShape 的 CornerBasedShape 光学适配，保持内部矩形裁剪稳定，动态 Goo 轮廓在外层 drawWithCache 裁剪；26 高光、阴影仍沿真实轮廓绘制，阴影位于裁剪之外。单按钮的 Capsule 路径及原光学参数不变。

[崩溃证据](crash-evidence/ios26-goo-lens-20261103.log)。仅读取已有日志、构建和安装；未启动应用、未交互、未运行测试或 Lint。二级页恢复情况仍待用户验收。

[APK](../../dist/ios26-goo-shape-crash-20261103/LiquidGlassSketch-ios26-goo-shape-crash-debug.apk)；SHA-256 `9d03339af9b3645fa80316a2d7b9ae2f4122498641206eff9511f1fb72400c2a`。

---

# 26 按钮 Goo 与菜单返回动画

旧 LiquidButtonCore 增加可选宿主适配入口；本示例通过 Ios26GooButtons 为交互按钮接入 27 按压、拖动和弹簧回弹。顶部、底部及列表详情并排工具按钮使用 GooGlassGroup；同一背景和材质的同排按钮复用 27 Goo 几何融合。26 的 blur/lens、tint、surface、luminance、高光和阴影继续由共享旧渲染器提供，未建立旧库到 27 库的反向依赖。渲染边缘预留 32dp，不更改原布局尺寸。

Context Menu 改为返回 Content Area，返回完成前隐藏原区域并保留源画面。文本操作菜单使用无来源的圆角面板形状，不生成多余液滴；关闭文本会话或清除焦点时保留菜单到 200ms 退出完成，横向排列保留。父、子菜单的操作关闭同时触发退出。

日期时间记录源前景和源材质两张独立快照。返回的最后 180ms 平滑交接至原控件画面，消除销毁玻璃浮层时才换回原材质的跳变；独立背景/完整页面/同窗口覆盖层的采样关系保持不变。

仅构建并自动安装。未运行测试、Lint、应用启动或设备交互，视觉效果待用户验收。

[APK](../../dist/ios26-goo-return-20261103/LiquidGlassSketch-ios26-goo-return-debug.apk)；SHA-256 `bde99bbbb4f67ae669078550b4964a8a2ef2b8971a1fda222dc1759bc321bbcd`。

---

以下为历史记录。

# 菜单、日期时间覆盖层与模态按钮采样

菜单恢复横向快捷操作及原有 Context Menu 内容布局，继续使用 27 弹出框架与 26 高光。文字选择菜单的点外事件会被消费并清除焦点，整页编辑器在失焦时折叠选择区，保留文字内容。开关默认隐藏已开启/已关闭文字，无障碍开关状态保留。

日期时间弹窗从独立 Android Popup 改为同窗口页面兄弟覆盖层；根坐标减去覆盖层原点，保留设计密度与主题。日历读取独立背景，弹窗读取已完成的页面录制，避免自采样。打开前仅记录被点击源的文字与图标，动画不再重建字号、12/24 小时标签或日期格式。

玻璃模态页通过 drawBackdrop 的原生 exportedBackdrop 导出处理后的面板材质；按钮采样该材质，不含按钮、阴影或外层高光。纯色模态页继续只导出纯色填充。

仅完成 Debug 构建及自动安装；未执行测试、Lint、启动或交互，视觉由用户验收。

[APK](../../dist/ios26-overlay-layers-20261103/LiquidGlassSketch-ios26-overlay-layers-debug.apk)；SHA-256 `99500d38baf8a3b6d6cf33a2768ecbef7f496604cc5b5cb0a007f0caff9d4dc9`。

---

以下为历史记录。

# 当前修正：Fullscreen 玻璃与上下文菜单定位

Fullscreen 恢复液态玻璃，Fullscreen Stack 保持纯色；原有 27 底部浮入弹簧保留。上下文菜单改用 Content Area 左边对齐、下方优先的摆放规则，处理根坐标到宿主坐标转换；下方不够时移到上方，并约束于安全区域。用户已撤回操作面板问题，本轮未修改 Action Sheet。

构建成功，按要求自动安装；未执行测试、Lint 或设备启动/交互，视觉与位置待用户验收。

[当前 APK](../../dist/ios26-fullscreen-context-20261103/LiquidGlassSketch-ios26-fullscreen-context-debug.apk)；SHA-256 `69a74029b226ba7c82ac68daff6e77ca25f441fff433b3d1b632ef998d3a4dff`。安装状态见 [机器记录](verification-results.json)。

---

以下为历史记录。

# 当前修正：纯色全屏/分享面板与底部弹簧浮入

Fullscreen、Fullscreen Stack 和 Activity View 恢复不透明底色，纯色面板不执行玻璃模糊、折射或边缘高光。颜色选择器和 Inspector 保留玻璃及 26 高光。模态页、分享与颜色选择均使用 27 原有 unfold/fold 弹簧从下方浮入、向下退出，不再使用 Alert 的缩放淡入；堆叠背板与阴影一起移动。26 回源菜单接管来源按钮原有阴影，随 settled 进度与菜单阴影交接，避免来源按钮隐藏和恢复时出现阴影空档。

构建成功，按用户要求自动安装。未执行测试、Lint 或真机启动/交互，视觉待用户验收。

[当前 APK](../../dist/ios26-solid-sheets-20261103/LiquidGlassSketch-ios26-solid-sheets-debug.apk)；SHA-256 `bf1ca87bccf7f1466aaee122cb9ba6c2695b360addfca677f59c56675674e30d`。安装结果见 [机器记录](verification-results.json)。

---

以下为历史记录。

# 当前迁移：26 全部弹出层采用 27 框架，最低 API 33

26 的 Alert、Action Sheet、颜色选择器、各类模态页、分享面板、通用操作面板、编辑/提示框、文本选择/上下文/下拉菜单及日期时间弹窗统一接入 27 框架，保留 26 高光和陀螺仪响应。最低 SDK 在五个模块统一改为 33，删除宿主旧 overrideLibrary。具体入口与采样层级见 [迁移说明](modal-framework.md)。

构建成功，按用户要求自动安装；没有运行测试、Lint、设备启动或交互。视觉和操作效果仍待用户验收。检查源码的最低 SDK 与路由约束已更新但未执行。

[当前 APK](../../dist/ios26-all-modals-api33-20261103/LiquidGlassSketch-ios26-all-modals-api33-debug.apk)；SHA-256 `a0e98a38e8b894d1afa12330bf08dc1e4b717fbb4c8194e2f5060d829cac39be`。安装状态见 [机器记录](verification-results.json)。

---

以下为历史记录。

# 当前新增：iOS 26 多级菜单

入口：手机示例库 → iOS 26 → 多级菜单。复用 27 的菜单路由、Goo 展开、逐级返回、嵌套采样和长列表，通过 `GlassMenuHighlight.Ios26` 复用 26 原有 `Highlight.Default`，包括陀螺仪生命周期。根菜单和子菜单使用实时轮廓，不叠加 27 高光。来源按钮使用 26 LiquidButton。新入口需要 Android 13+，旧入口不变。

构建成功；只构建并安装，未执行测试、Lint 或设备交互，未启动应用。视觉与交互待用户验收。既有示例路由检查源码已适配但未执行。

[当前 APK](../../dist/ios26-multilevel-menu-20261103/LiquidGlassSketch-ios26-multilevel-menu-debug.apk)；SHA-256 `9026f0d6551ac178e7b3424451e4f96d3cf1071b6f1d56628011caf6cb43cf4a`。安装结果见 [机器记录](verification-results.json)。

---

以下为历史记录。

# 当前修正：原版底栏高光的原生移植

底栏改用原版 LiquidGlassCorners 的解析边缘距离、法线和连续圆角修正，关闭自定义路径 profile 分支。用 Kotlin 计算 Android Path，作为 27 专用 surfaceShape 同时提供给 26 主体的裁切、阴影和光学半径；26 默认参数保持 Capsule。没有 Flutter SDK、引擎或组件依赖。

撤销 256 射线扩展及其额外路径采样；保留两份独立 shader 的后台预热。隐藏文字参考层继续不加高光，指示器高光随按压进度退场。主体采样、模糊、折射、拖动和文字逻辑仍为 26；材质输入是 Sketch 的结果，不能宣称整套 Flutter 光学输出逐像素等同。

构建成功，按用户要求自动安装；未运行任何测试、Lint、shader 渲染或设备交互，视觉效果待用户验收。

[当前 APK](../../dist/ios27-dock-native-original-20261103/LiquidGlassSketch-ios27-dock-native-original-debug.apk)；SHA-256 `69d22ce3b88571e0e1665b8b48a90e43b007b2b73cb436a2727b3ea7b366b15b`。安装结果见 [机器记录](verification-results.json)。

---

以下为先前版本记录，不代表当前版本已执行测试。

# 当前修正：256 射线底栏预热

启动预热任务在后台准备两份独立的底栏高光 shader，使用线程安全队列独占移交给主体和指示器；预热尚未完成或实例已被领取时仍保留原有创建回退，不阻塞等待后台任务。材质及轮廓采样参数不变。Atlas / Capture Planner 接入按用户“跳过”处理，保留当前 Sketch 后端。

仅完成构建与自动安装，未运行单测、Lint、静态测试、shader 渲染或真机性能/交互测试；没有实测提速结论。安装状态见 [机器记录](verification-results.json)。

[当前 APK](../../dist/ios27-dock-prewarm-20261103/LiquidGlassSketch-ios27-dock-prewarm-debug.apk)；SHA-256 `ef07bd28f73df6f722a0d258a7f55739687200438c9eaaf6ca5e843d47f003e6`。

---

以下是先前版本记录，不代表当前版本已执行测试。

# 当前修正：底栏外圈采样精度

底栏高光改为 256 射线，原轮廓路径采样加密至 1024–4096 段，仍读取 26 主体的同一个连续胶囊形状。其他控件维持 64 射线；原有 27 明暗、高光宽度、材质和主体几何不变。采样仅在轮廓变化时重建。

构建成功，按用户要求自动安装；未运行任何测试、Lint、shader 渲染验证或设备启动操作。运行时表现与边缘视觉效果待用户验收，不能以编译成功替代。安装结果见 [机器记录](verification-results.json)。

[当前 APK](../../dist/ios27-dock-outline-20261103/LiquidGlassSketch-ios27-dock-outline-debug.apk)，SHA-256 `d534959300788916e5b014e33521bdbf1cbe70951ceced24837d11c43ec89d18`。

---

下文为先前版本记录，不代表当前版本的测试结果。

# 当前修正：底栏重复边线

移除选中文字隐藏参考层上的 27 高光，防止选中胶囊采样整条参考层的上下边线；选中胶囊的 hairlineSide 与按压进度同步归零。仅修改 27 适配器，保留 26 主体。

用户要求自动安装到 `192.168.1.148:45149`，不要测试。本轮仅执行 `:sample:assembleDebug`（排除 QQ 发送），构建成功；未运行单测、Lint、静态测试、启动或交互检查。安装状态见 [机器记录](verification-results.json)。

[当前 APK](../../dist/ios27-dock-rim-20261103/LiquidGlassSketch-ios27-dock-rim-debug.apk)；SHA-256 `daee3beb2e534dc42d1ca24c27899d9f2b724f0e97a539fbc79764d529fea481`。当前修正的真机效果待用户验收。

---

以下均为上一版 Goo APK 的历史验证，不是当前修正的测试结果；其中 AAR 与 APK 链接保留历史版本。

# 2026-11-03 合并、原生示例与底栏 / Popup 修正验证

此文件只记录 `liquidglass_sketch` 本次合并结果。`baseline/` 中源项目的 324 单测、真机帧时间与截图不计为本次验收。

## 通过的检查

| 检查 | 本次结果 |
| --- | --- |
| 完整迁入与来源哈希 | 69 个机制文件、5 个 shader、56 个原测试文件；86 个旧基线保留，其中 84 个文件逐字节不变，`LiquidBottomTabs.kt` / `DrawBackdropModifier.kt` 的可选高光钩子按本轮授权单独审核 |
| 原提取边界、页面 tokens、项目规范 | 通过；保留原提取基线，未重生成 Figma 资产 |
| 新库 JVM 单测 | 341 / 341 通过，无跳过；含 17 项新增 Popup 文字比例、共用圆角/高光、底栏主体及通用 Goo 分组回归 |
| 旧库与版本分流 JVM 单测 | 68 / 68 通过，无跳过 |
| 新库 Lint | 0 错误，18 警告 |
| 宿主 Lint | 0 错误，102 警告；新宿主页复用 Material 2 页面框架、机制库使用 Material 3，存在混用提示，未压制；此前旧 ControlCenter 未使用 scope 的 BoxWithConstraints 已改为同尺寸 Box |
| 新示例结构回归 | 4 / 4 通过；检查七个路由、宿主框架复用、单向采样边界、源演示源码缺席 |
| 图标生成器 | 124 个图标全部匹配固定输入 |
| 实际 AGSL 渲染检查 | dark regular / tinted、light regular、无 hairline 四组全部通过；[明细](shader-validation.json) |
| Debug 构建与打包 | 通过；AAR 类定义、APK DEX 定义、5 个 shader、原生示例入口均存在；APK 无 `dev.liquidglass27.demo` 类定义 |
| APK 签名与对齐 | APK Signature Scheme v2 通过；`zipalign -c -P 16 -v 4` 通过 |
| 宿主依赖解析 | Compose UI / Foundation 1.12.0、Material3 1.4.0；沿用依赖解析结果，未增加 force / enforcedPlatform |

新示例入口为 **手机示例 → iOS 27**，分别为弹性按钮、Goo 操作组、导航底栏、多级菜单、Popup、对话框、主题与图标。使用现有分类路由、页面框架、tokens、分组与返回按钮；原项目的两个整页演示已经删除。行内控件读取独立预览背景，顶部与浮层读取完整页面，保持单向采样。原有示例按 iOS 26 保留。

初次合并的 69 个机制与源项目去除 API 注解后完全一致。本轮按用户反馈修改 `GlassPopup.kt` 和 `GlassControls.kt`，其余 67 个原机制保持一致；新增的标准底栏适配器和内容转换单独纳入清单。临时为原演示开放的 `canUseSourceContentFrame` 保持源项目 `internal`，该文件哈希与源基线相同。[本轮改动与来源依据](dock-popup-correction.md)。

## 已有失败与验收边界

`node --test tools/figma-reader/native-framework.test.mjs` 为 15 通过、17 失败。合并前后失败名称一致：使用初始 SHA-256 匹配的 MainActivity/IphoneExamples 原文件重跑基线，其他输入未改变；不是把 HEAD 当成原工作区。没有删除测试或修改旧组件去迎合过时源码断言。失败名称保存在 [机器报告](verification-results.json)。因此不声明全仓所有静态测试通过。

早期复制式演示 APK 曾安装到 PLQ110（API 37），其中间版本的自动导航被系统浮层/前台切换干扰，结果断言未通过。这些历史尝试不计为当前版本验收。用户明确要求“先完成代码和构建，真机由我验收”，因此本轮底栏 / Popup 修正版 APK **未安装、未启动、未操作手机**。重点待验收：26/27 切换后的底栏拖动和选中、Popup 展开 / 中途反向时文字比例、源按钮与面板高光连续性、菜单与 Popup 圆角一致性。

API 24–32 真机启动、完整多级/快速反向菜单、Popup 键盘/旋转、Goo 动态融合、两套主题视觉、背景对应位置透视、滚动和 Slider 两端回归均仍待真机验收。这里的合并与测试通过不替代用户视觉结论，源项目的历史帧时间不转记为目标性能结果。

## 产物

- [底栏 / Popup 修正版 APK](../../dist/ios27-goo-buttons-20261103/LiquidGlassSketch-ios27-goo-buttons-debug.apk)，47,536,098 字节；SHA-256 `bcae9a3fb7a27ef9d83e3605825cc96b6bb5bf59e6bb51ffc8279590d2bf6f35`。
- [iOS 27 AAR](../../dist/ios27-goo-buttons-20261103/liquidglass-ios27-debug.aar) 与 [iOS 26 AAR](../../dist/ios27-goo-buttons-20261103/liquidglass-ios26-debug.aar)；裸 AAR 不包含传递依赖，优先使用 Gradle 项目依赖。本轮增加旧库可选钩子，因此两份库应一同更新。此前 `ios27-merge-20261103` / `ios27-native-examples-20261103` 是历史快照。
- [源代码清单](merge-manifest.json)、[测试/产物机器报告](verification-results.json)。

未执行 Git 提交、清理、同步或 QQ 发送，原有暂存和未暂存内容保留。

本次追加：修正来源快照遗漏 padding 和按前景尺寸放大造成的末帧字号变化，新增三项回归。未进行设备安装和交互，文字视觉结果待用户验收。

底栏截图反馈修正：27 高光移至阴影内侧的独立材质层，背景逆变换不再调用高光回调；新增图层顺序回归通过。未安装操作手机，修正后的边缘效果待用户验收。

通用按钮 Goo：新增六项物理顺序、行隔离、材质边界、容量与空组回归；普通文字/图标/着色和 Popup 来源示例接入 `GooGlassGroup`。同排同材质分组，最多六个一批；新公开入口通过 AAR/APK 类定义检查。未安装操作手机，融合、增删后点击与 Popup 回源效果待用户验收。
