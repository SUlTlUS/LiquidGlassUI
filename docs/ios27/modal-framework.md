# iOS 26 弹出层统一使用 iOS 27 框架

用户要求：26 的全部弹窗和模态使用 27 框架，高光仍为 26；最低 SDK 改为 Android 13。当前五个模块 minSdk 均为 33，sample 删除旧 MeloX overrideLibrary。

## 已接入的宿主

| 原入口 | 当前框架 | 保留内容 |
| --- | --- | --- |
| Alert、编辑/提示弹窗、原稿变体选择 | GlassModal / GlassDialog | 输入、确认/取消、变体切换回调 |
| Action Sheet、通用操作面板 | GlassDialog / GlassModal | 破坏性操作、收藏、复制、移除 |
| Color Picker、Fullscreen、Inspector、Fullscreen Stack | GlassModal + GlassModalSurface | 配色控件、工具栏、页面操作 |
| Activity View | GlassModal + GlassModalSurface | 分享列表、缩略图、协作切换 |
| Context Menu、文本选择菜单、列表 Pop-up | GlassMorphMenu | 禁用、选择、多级子菜单、实际文本会话操作 |
| 日历内年月/时间弹窗 | GlassPopup | 独立窗口、来源位置、临时值、确定/取消 |
| 其他日期/时间入口 | GlassDialog | 嵌入系统日期/时间控件，取消不提交 |

`GlassModal` 从原 27 `GlassDialog` 抽出共享动画、焦点边界、返回键、遮罩和 IME 宿主，27 自身的 Dialog 也复用该实现。`Ios26ModalScope` 固定选择 26 高光；新框架默认仍为 27。菜单明确传入 `GlassMenuHighlight.Ios26`，Popup 和模态继承同一作用域。`drawGlassHighlight` 复用共享库原有 HighlightElement 和陀螺仪生命周期；26 路径不叠加 27 ControlHighlight/菜单边缘。

`GlassModalSurface` 接收现有页面轮廓。Fullscreen、颜色选择器与 Inspector 使用液态玻璃及 26 高光；Fullscreen Stack 与分享面板使用不透明底色，不使用模糊、折射或玻璃边缘。共享 27 阴影与宿主仍保留。玻璃面作为独立层采样页面；需要内部玻璃按钮的面板仅导出材质或纯色，不录制这些按钮。内嵌选择器弹窗使用独立窗口，避免被所在页面反向录制。26 主体页及系统 ColorOS 组件演示不属于迁移的弹出层，未替换为另一套页面。

上下文菜单使用 `BelowAnchorStart` 定位：Content Area 的根坐标先转换到菜单宿主坐标；菜单与区域左侧对齐，优先放在其下方 16dp，空间不足时改放上方，并限制在安全区域内。菜单宽度变化时仍保持左侧对齐。它不再沿用来源按钮的右端覆盖规则。

## 面板浮入动画

26 的回源菜单同时接管来源按钮的原有阴影：复用共享库 `Shadow.Default` 和 `ShadowElement`（24dp 模糊、下移 4dp、黑色 10%），随 morph 的 settled 进度与菜单阴影交接；返回时逆向恢复。阴影位于材质裁切之外，实际按钮继续在回源完成后显示。没有回源按钮的菜单不添加这层阴影。

模态页（含 Inspector）、分享面板和颜色选择器统一选择 `GlassModalTransition.BottomSheet`：从屏幕下方上移，保持实体大小与不透明度，位置保留弹簧过冲。直接复用 27 的 `FlClashMenuSprings.unfold()`（420ms / bounce 0.15 的物理参数换算），关闭时复用 `fold()`（340ms / bounce 0）。这些是弹簧描述参数，不是固定播放时长；中途反向继续使用同一个 Animatable。阴影和堆叠背板一同移动，起始位移包含 64dp 隐藏余量。普通 Alert 继续使用原有进入方式。

实现只用 Kotlin / Compose / Android View / AGSL，不依赖 Flutter。旧 `LiquidDialog` 及未使用的参考渲染函数保留兼容源码，当前 sample 的上述入口统一走新适配层。历史原稿的面板外观不是本轮逐像素目标；本轮统一使用 27 弹出机制、26 高光。

## 验收状态

按用户要求，只进行构建和自动安装。未运行单元测试、静态测试、Lint 或真机交互；构建和安装不代表视觉或交互验收。最低 SDK 和示例路由的既有检查源码已同步，未执行。产物与安装结果见 `verification-results.json`。
