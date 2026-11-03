# Liquid Glass 27 — FlClash 一致性移植实施计划与执行记录

工程：`C:\IDE\Android\liquidglass27`  
原版参考：`FlClash/lib/ui/popup.dart`、`FlClash/lib/ui/liquid_glass.dart`、`FlClash/lib/ui/goo.dart`、`FlClash/shaders/liquid_glass.frag`

## 2026-11-03 补齐渲染调度与资源复用（本轮）

- **根菜单动画**：`MenuRootMotion` 持有同一条原版开场/回收计算；组合层只观察显示和命中阈值。`SketchMenuRootSurface` 在测量/绘制阶段共享当帧几何，阴影、裁切、光环及采样区域同步；源图标位移、内容缩放/透明度在 placement/graphicsLayer 更新。保留 36dp 采样边距、形变曲线和中途回收快照。`lastRenderedFrame` 改为绘制快照，避免反向触发 UI 重组。备用非 Sketch 渲染路径保留。
- **高光与光环**：共用控件高光移入 graphicsLayer 更新，shader uniform 上传代码保持原样；完整输入相同则复用 RenderEffect。菜单光环按几何和光照分别保留路径与完整 Picture，包含渐变、画笔和浮点颜色滤镜；平移补偿保留外侧暗线，不把光照强度降为 8 位 alpha。
- **统一 shader 准备**：`GlassShaderPrograms` 在应用启动时异步预加载源码并准备实例；源码按资产名进程内共享，预备实例只交给一个控件，后续控件持有自己的可变 uniform。Activity 重建不重复预热。
- **Sketch 共享缓存适配**：源码核对确认 `LayerBackdrop` 已共享背景录制层，因此保留其采样通路；增加每个场景的 `SketchMaterialCache`，按尺寸、光学圆角、密度、字体缩放、方向及材质类型复用至多 32 组不可变效果。仍调用相同 Sketch blur/lens/vibrancy/colorControls。旧位图 Atlas 继续属于非 Sketch 后端，未叠加到 Sketch 采样链路。
- **验证与交付**：Debug 构建、**324 项 JVM 测试 / 0 failures / 0 errors**、Lint **0 errors / 22 warnings / 1 hint** 通过；全部五个 shader 资源逐字节一致。PLQ110 `192.168.1.148:36935` 已安装 `artifacts/20261103-retained-rendering/liquidglass27-retained-rendering-debug.apk`，SHA-256 `6C785851D1F2347247C95A20797B681216E1767B35AC0DE8119EA10BA4B7C925`。菜单、多级展开/整体回收、快速返回、主题切换和弹窗完成运行检查；外观由用户验收。
- **性能证据**：本轮相同三次菜单开关，前版 P95 44ms / 卡顿 36/328（10.98%）；正式构建首轮 P95 30ms / 卡顿 12/362（3.31%）。单次短测不代表所有设备或所有场景收益；完整复测、方法采样与限制见 `artifacts/20261103-retained-rendering/README.md`。模拟器未启动，构建排除 QQ 自动发送。

## 2026-11-03 参照 FlClash 优化几何热路径（上一轮）

- **参考依据**：`liquid_glass.dart` 的持有层按几何/样式键复用轮廓、光环和录制结果；`goo.dart::_bridge.reach` 在一次射线步进中复用已经计算的 platter 距离。移植到 Compose 的 `remember` 和每个 pane 独立缓存，不修改本地 FlClash 或导入的 Sketch 模块。
- **重复工作**：菜单阴影与 Sketch 裁切共享同一份 Goo 轮廓，裁切只做局部坐标平移；保持 Sketch 内部矩形 shape 稳定，由外层实时轮廓负责裁切；九层菜单光环按几何复用路径和相邻边界，不可见时不构建。
- **控件高光**：每个 pane 的主轮廓和六个胶囊按宽/高/半径分别缓存 Flutter 圆角拟合，移动/触点变化不重算未变形邻居；复用 uniform 数组，避免每帧切片分配。光学 shader、材料及光照参数未改。
- **Goo 射线**：每组轮廓预计算两矩形的中心/半宽/半高/有效圆角；一次步进不再重复求 platter 距离；长度使用原版 sqrt 形式并内联，保留 Double 中间精度。射线数、自适应细分、容差、四次二分和弹簧均不变。新增测试以旧距离算法对照 50,000 对坐标及 20,000 个矩形采样点，Float 结果完全一致。
- **验证**：全量 **322 项 JVM 测试 / 0 失败 / 0 错误**，Debug 构建及 Lint **0 errors / 21 warnings / 1 hint** 通过；五个 APK shader 资源与优化前逐字节一致。已安装 PLQ110 `192.168.1.148:36935`，模拟器未启动，构建排除 QQ 自动发送。
- **帧耗时**：无方法采样器、重启应用后三次菜单开关，紧邻的旧版 P95 **48ms**、卡顿 **48/305（15.74%）**；新版两轮 P95 **44 / 46ms**、卡顿 **35/328（10.67%）/ 37/329（11.25%）**。早期旧版也曾测到 P95 32ms，说明跨轮波动明显；本轮只确认消除了重复工作，短测支持有限改善，仍有长帧，不能宣称固定卡顿已消失。
- **交付与待验收**：`artifacts/20261103-performance/liquidglass27-performance-debug.apk`，SHA-256 `53EFEEC3087EDE47FB894B953C8E2ABFD9568D90C358B209766294CAB9F58CEF`。完整证据与限制见该目录 `README.md`；用户按 `docs/verification-2026-11-03.md` 的性能回归清单检查实际顺滑度与外观。

## 2026-11-03 截图中的 Goo 顶底短亮条（上一轮）

- **直接原因**：深色 Regular 的 `hairline=0 / hairlineSide=0.75`。上一版改正了暗线覆盖亮边的混合方式，却保留 `darken > 0` 的错误分支。胶囊直边的法线 X 分量为 0，那里跳过覆盖；刚进入圆角便执行覆盖，于是顶部、底部各残留一段与直边等宽的亮条。原版检查的是全局暗线配置，而非当前像素的方向强度。
- **修复**：`flclash_control_highlight.agsl` 改回 `(uEdge.x > 0 || uEdge.y > 0) && line > 0`，同步修正备用 `flclash_glass.agsl` 的同类判断。备用 shader 的整数 `clamp` 改为等价的浮点转换再转回整数，以通过 Skia 144.1 的真实编译检查，采样次数不变。未改高光强度、轮廓、Sketch 采样或材质。
- **真实 shader 回归**：新增 `tools/check_control_highlight.py`，使用 Skia 144.1 直接执行生产 shader，以恒定材质隔离高光合成。四个 240×182px 胶囊、3.5 倍密度，检查上下共 16 个直边/圆角交界。旧版深色 Regular 最大像素跳变 **0.245280**，修复后 **1.49e-8**；深色 Tinted 同样通过，浅色和无暗线样式行为保留，内侧亮带仍存在。四组回归通过，前后结果在 `artifacts/20261103-goo-highlight-seam/`。
- **构建与真机**：Debug 打包通过，已安装 PLQ110 `192.168.1.148:43923`，完成启动、切深色及 Goo 按压拖动的运行检查，日志无 shader 加载失败或应用崩溃。本轮未改 Kotlin，未重跑上一轮 316 项 JVM 测试；截图外观由用户复验。
- **交付**：`artifacts/20261103-goo-highlight-seam/liquidglass27-goo-highlight-seam-debug.apk`，SHA-256 `D9EB69C89C75F23A9E7712445CB77B5965541A9B0EE24BD2E7EF6F95E4FE2FB4`。构建排除 QQ 自动发送，模拟器未启动。

## 2026-11-03 高光共用实现修正（上一轮）

- **控件/Goo/底栏**：原高光 shader 使用固定幂次圆角距离，混用了窄亮边和长尾的几何。现按 `liquid_glass.frag` 分开：圆形 rounded-box 法线与长尾、Flutter RSuperellipse 双八分圆角的边缘距离；Goo 使用最近胶囊的边缘修正，并扣除融合处的距离增益，避免连接处出现多余光圈。任意非 Goo 轮廓仍使用原 64 射线样条。
- **侧边暗线与合成**：恢复 `hairlineSide` 默认跟随 `hairline`，补回 Pill 和 Lens 两侧原本缺失的暗线；暗线按原版顺序覆盖亮边，底色继续来自 Sketch 材质结果。
- **菜单/弹窗**：移植 `LiquidGlassRim.paintLine/_paintShine` 的外侧 0.67dp 暗线与九层 even-odd 填充环。撤掉内部裁切暗线、细描边代替填充环的做法；光带强度通过浮点颜色矩阵保留，避免较弱光带先被 ARGB8 截断。
- **按压光斑**：普通按钮使用独立 120ms 入场 / 360ms 退场 easeOut，松手时扩散；Goo 改为原版 `lift × 0.16`、高度 / √2 范围及触点优先定位。Sketch 采样、模糊、折射及色彩处理未改变。
- **自动检查**：Debug 打包、全量 **316 项单测 / 0 failures / 0 errors**、Lint **0 errors / 21 warnings / 1 hint** 通过。真机首轮发现 AGSL helper 不能动态索引 uniform 数组，改为常量索引分支后重新打包并安装，最终 shader 编译和 uniform 设置未报错。
- **真机范围**：PLQ110 `192.168.1.148:43923` 完成启动、普通按钮按压、Goo 拖动、底栏拖动、深浅色切换及菜单打开的运行检查；最终进程日志未见高光加载失败、ART VerifyError 或应用崩溃。连续三次菜单开关的帧统计在开始输入前发现前台已变成其他应用，保护中止，未取得新帧统计；未完成弹窗回归或视觉验收。
- **交付**：`artifacts/20261103-highlight-fix/liquidglass27-highlight-fix-debug.apk`，SHA-256 `B17521D3155D2ED850321D250DE20B7FA1DC3A4E2EE5776E88EC1EB070433309`，已安装。构建排除 QQ 自动发送；模拟器未启动。证据与机器可读摘要在同目录，视觉验收仍待用户执行 [高光专项清单](docs/verification-2026-11-03.md)。此前“机制补齐”不等于高光已视觉对齐，本轮修复的是用户反馈后核对出的共用实现差异。

## 2026-11-03 补齐其余六类机制（上一轮）

- **边界**：保留 Sketch 采样、模糊、折射与材质；其余机制按本地 FlClash 源码移植，采用 Compose 插槽、状态、Modifier、焦点及 IME 接口。视觉效果由用户验收。
- **通用弹窗**：`GlassDialog` 提供任意内容、标题尾部、输入框模式、危险动作、多动作与长按钮自适应布局、整卡弹性、内容滚动、焦点约束和关闭回调；`GlassAlert` 保持原接口。
- **通用 Popup**：`GlassPopupState<T>.show()/dismiss(result)` 在回收完成后交付结果；支持任意源前景快照、内容插槽、模态/非模态、独立焦点策略、浮层主题、回源开关。`GlassPopupAnchor` 跟踪真实布局；定位考虑安全区、IME、避让区域及内容尺寸变化时固定右上角。
- **底栏**：补齐 8dp 命中扩展、统一标签缩放与省略提示、光晕扩散、鼠标释放后的悬停恢复、焦点标记与单份无障碍语义。保持原有连续弹簧、速度投影、RTL、触觉反馈及 Sketch 多层背景。
- **颜色与图标**：新增 HCT 种子主题（Vivid/Tinted/Soft/Monochrome）、纯黑表面层级与浮层角色；生成完整 124 个参考静态矢量图标，补充播放/暂停和侧栏连续变形、平台返回图标。`python tools/generate_glass_glyphs.py --check` 校验目录。
- **真机发现并修复**：菜单大 Compose 闭包触发 ART `VerifyError`，拆出 `MenuRouteState` 并把菜单主体改为可组合成员，避免生成数十个捕获参数的方法；非模态指针共享延伸至场景根节点，关闭时只为取得过焦点的 Popup 恢复源焦点。
- **验收入口**：主页 `•••` → **完整机制验证**。完整步骤见 [用户验证清单](docs/verification-2026-11-03.md)。该页可切换种子色、主题模式、纯黑、RTL、大字，测试五类弹窗、通用 Popup、图标目录和长标签底栏。
- **用户反馈的开场卡顿**：用户确认发生在“刚从按钮展开时”。ART 1ms 采样显示，默认 `glassColors()` 在每个新挂载菜单行分别生成 HCT 配色，三次开关累计约 263.7ms。固定浅/深色默认方案改为共享惰性实例；自定义种子主题仍由 `GlassTheme` 按参数记忆。修复后相同采样不再出现配色生成，菜单内容单次采样 CPU 峰值 107.7→25.1ms。关闭采样器后，在设备端连续开关三次：370 帧，P50 13ms / P95 23ms / P99 30ms，9 个 janky frames（2.43%）。这不是逐帧视觉验收，也不代表零掉帧；原始证据见 `artifacts/20261103-menu-opening-profile/`。
- **自动检查**：最终 Debug 打包成功，全量 **314 tests / 0 failures / 0 errors**。Lint **0 errors / 20 warnings / 1 hint**。图标生成器核对 **124 项**通过。单测曾因宿主测试进程启动超时而失败，重跑后完整通过。构建一直排除 `:app:sendNamedDebugApkToQQ`，本轮未发送 QQ。
- **真机功能证据**：PLQ110 / API 37，`192.168.1.148:43923`；模拟器保持关闭。本轮确认普通弹窗确定、文本输入、Popup 字符串与协程结果、非模态背景计数只加一次、模态背景计数不增加；补齐包复核四个底栏语义节点、拖动至设置、主页菜单及验证页入口。开场修复包已覆盖安装，进程 PID 16564 的 `AndroidRuntime:E` / `LiquidGlass27:E` 无输出。
- **最终 APK**：[liquidglass27-completion-opening-fix-debug.apk](artifacts/20261103-flclash-completion/liquidglass27-completion-opening-fix-debug.apk)，SHA-256：`526E449E8E6639F2D0DB4422F986AB95A8397FE54EE121AB88A6D2C3038C6F8E`。全部视觉、鼠标/键盘、TalkBack、旋转及系统取消验收仍由用户按清单确认。

## 2026-11-03 Compose 交互、底栏、矢量图标与弹窗；PLQ110 真机回归（上一阶段）

- **当前范围**：用户确认保留 Sketch 的采样与材质，其他机制向 FlClash 对齐；优先 Compose 接口，不引入额外防御层。最终视觉效果由用户验收，以下功能检查不代表像素或逐帧一致性验收。
- **弹性按压**：`FlClashElasticMotion` 复用连续弹簧，在首次达到 .975 时处理快速松手，拖动和归位保留速度；统一按钮前景与 Goo 轮廓的乘法缩放、橡皮筋和悬停。指针命中保持布局坐标，绘制变换放在其后。
- **底栏**：`FlClashDockMotion` 与 Compose `GlassNavigationDock` 实现连续位置透镜、最小 84dp 宽度、16dp 抬升扩张、速度/制动驱动的 jelly、释放投影、延迟落下、RTL、触觉反馈、悬停及双层裁切前景。Sketch 背景录制排除透镜自身和前景；继续使用 Sketch 材质。
- **图标与弹窗**：新增 Compose Canvas/Path 的 `GlassGlyphIcon`、`AnimatedGlassGlyph` 及主要底栏/菜单矢量图标；菜单保留字符串入口并增加 `vectorGlyph`。弹窗采用 404ms 路由、275ms 临界弹簧曲线，入场 1.2→1、退场保持缩放并淡出；增加系统返回、窗口/键盘边距和原版按钮布局。
- **构建**：Kotlin、Debug APK、Lint 成功；全量 **307 tests / 0 failures / 0 errors**，Lint **0 errors / 15 warnings / 1 hint**。构建显式排除 `:app:sendNamedDebugApkToQQ`，本轮未发 QQ。安装 APK SHA-256：`5B6644FF36E143C03542A26F0DF55B2259AC9109FCC594E69D51C4D255AA54E2`。
- **真机功能证据**：连接 `192.168.1.148:43923`，PLQ110 / API 37，安装及冷启动成功；已关闭 `emulator-5554`。底栏从首页拖至设置，语义选中状态及提示更新；一级、二级菜单可打开；二级标题持续长按 1.8 秒后停留一级，没有重新展开；关于弹窗确定及系统返回可关闭；深浅色开关生效。操作后应用进程仍在，检查的 `AndroidRuntime:E` / `LiquidGlass27:E` 日志无输出。
- **尚未完成的范围**：FlClash 全部图标目录、通用 CommonDialog 内容/输入和自适应操作布局、底栏动态主题色与部分悬停细节、所有系统取消/无障碍路径仍需继续对齐。未做与 Flutter 的逐帧并排验收；用户接管视觉效果检查。

## 2026-11-02 修复长按二级标题返回一级后再次自动展开（二次复发）
- [x] **复核本次真正竞态**：原来的 `FlClashMenuPress.foldedToParent()` 仅在 `_foldSpring` 完全结束后尝试把 `_pressed=Header` 转为父级入口 Row，并保持 `_landed=true`；但 `GlassMenu.kt` 在进度 `<= .08f` 时就把正在收起的子级 Header 从 `gestureLevels` 命中列表撤除，而原 Flutter `_pop()` 直到动画结束仍挂载 `_buildRow` Header。处于同一物理长按时，Compose 的 `pointerInput` 每个 Move 都调用 `held.move(hitAt(last))`，会在 Fold Completion 前把 Header 误改为父级 Row/Back/Blank，立刻 `landed=false` 并重新 `armDwell`。等 `foldedToParent` 到达时 `_pressed` 已不是 Header，因校验失败无法再修正，从而在仍按同一个入口时反复打开二级。单纯在折叠完成发布交接事件不足以防止这一竞态。
- [x] **完整 Fold 期间保持已经获胜的手势归属**：在既有 `FlClashMenuPress` 内新增 `foldingHeaderDepth`（**不新增 GlassMorphMenu 巨型 Compose lambda 的独立捕获**）：长按 400ms 完成 `ToggleHeader` 时锁定原 Header(depth) 和 `landed=true`；`move()` 在 Fold 完成前无视因卡片收缩导致的父菜单/空白/Back/Outside 临时命中变化，不会再执行 `_hold` 或重启计时器；收到匹配 depth 的 `MenuFoldPressReturn` 后在同一手势内一次性交给原父 Row(index)，清除折叠锁但仍 `landed=true`。此后在相同入口上的 Move 不会重新计时；**移动至另一个真实菜单项**才按 Flutter `_slidePress` 的索引变化重新选取并允许停留展开。`start()` 会清除上一个手势的锁；普通短按标题返回、二级打开之后拖动选择依旧采用原有路径。
- [x] **仅真实物理 Move 可改变高亮/触发新 400ms 计时**：正常 `pointerInput` 的 `update(hitAt(last))` 增加 `change.position != change.previousPosition` 条件；`GlassCarriedPress.Move` 同样检查 `event.position != position`。避免被布局/动画推进产生的零位移 PointerEvent 错当成手指从二级标题走到父级入口。
- [x] **回归**：`MenuFoldPressHandoffTest` 追加 5 个状态、交接、展开不回归和源码双输入路径测试；更新 `FlClashHeaderPressTest` 原先错误地要求折叠中父行立即抢走 `_landed` 的断言。覆盖多达 160 次模拟折叠过程的 Header/Blank/Back/Outside/同一入口/其它行临时命中，完成交接后再在同一父入口重复 100 次 Move 不重新展开；确认其它行仍能接管。完整 Kotlin 编译和全量 JVM 单测通过。
- [x] **再修 Fold Completion 后一帧旧层命中**：在 `FlClashMenuPress` 内补充 `restoringParentDepth`，因为 `MenuFoldPressHandoff.completed` 发布与 `gestureLevels` 变成新父层可能分属相邻 Compose 帧，`foldedToParent` 虽成功将 `_pressed` 交接到父 Row，但下一帧仍可能命中旧 Header/Blank 再清除 `_landed`。现在交接后、在渲染的 topDepth 仍比目标父 depth 深时忽略旧子层命中；直到父层真正成为 top 才恢复 `_slidePress`。此状态只放在原 `FlClashMenuPress`，不向 `GlassMorphMenu` 添加额外闭包捕获。新增“交接后延迟 35 帧仍命中旧 Header/Blank/Back，不得重新计时”的回归测试。
- [x] **正式构建及自动 QQ**：完整 `./gradlew :app:compileDebugKotlin :app:testDebugUnitTest :app:assembleDebug --offline --console=plain --max-workers=1` 返回 `BUILD SUCCESSFUL`，XML 汇总 **287 项 JVM 测试、0 failures、0 errors**。最终改名 APK `app/build/outputs/apk/debug/liquidglass27-debug-20261102-214805-235.apk`（47MB），`:app:sendNamedDebugApkToQQ` 日志 `Send script finished`，未独立确认 QQ 对端实际接收。已尝试再次启动 Windows API 36.1 模拟器进行安装验证，但 Android 系统启动尚未完成，未获得本构建版本的完整 ADB 启动及触摸验收证据。
- [ ] **真实设备验收**：一级入口长按打开二级→同手指长按二级标题折回→持续按住不动超过 400ms（不重新展开），再移动到另一个行后回到入口（允许新长按）、松手重新按下（允许新长按），及三级以上重复。Android 模拟器此前存在 host 侧主动退出，仍需用新 APK 对真机帧事件验证。

## 2026-11-02 纠正二级菜单展开/折叠时按住遮罩圆角被错误动画化（上一阶段）
- [x] **严格核对 FlClash 的两层形状**：`FlClash/lib/ui/shape.dart::AppCorner.md=16`；`FlClash/lib/ui/popup.dart::_buildRow` 的 `ShapeDecoration(shape: AppShape.all(_itemRadius))` 在所有展开/折叠帧的圆角均为**固定 16dp Flutter 超椭圆**，没有对 item radius 的动画控制；仅 `PopupMenuCard._radius=lerpDouble(_itemRadius,_cardRadius,_grow)` 随菜单卡片本身的 `_grow` 从 16dp 变至 24dp。前一阶段为解决外层裁切，在 `MenuPressedHeaderVisibleRadius(visible, card, cardRadiusPx, itemRadiusPx)` 中错误按 `(cardRadiusPx - minInset)` 动态改变**遮罩自身**的半径，导致用户看到遮罩圆角在展开/收缩中明显变化。
- [x] **只回退错误半径动画，保留边界裁切修复**：`MenuPressedMaskMorph.kt::menuPressedHeaderVisibleRadius(visible,itemRadiusPx)` 现在不再接收任何菜单卡片半径、当前进度、内边距或方向参数，始终固定返回 `itemRadiusPx=16f*density`（唯一例外是可见矩形短边小于 32px 时的物理半宽保护，防止无效形状）；此前用于不让动画过程中遮罩被截成平角的 `menuPressedHeaderVisibleBounds` 求交集保持不变。`GlassSubmenu.kt` 的 `headerMaskPath` 只记忆 visible rect、envelope、density，使用现有 `gooPlatterOutline` 画固定 16dp Flutter RSuperellipse，仍与当前菜单外部 `contentOutline` 共用一条剪裁路径，阴影、Sketch 材质和 `_fold/_unfold` 弹簧都不改。
- [x] **覆盖开合全程回归**：修订旧的“半径随菜单变化”测试为固定 16dp 的进度 0..120 与倒序（**242 帧采样**）断言，明确检查外围菜单半径仍从 16dp→24dp、遮罩始终 16dp；额外新增窄区域物理半宽截断和源码结构禁止再引入 cardRadius/progress 的测试。全量 **281 项 JVM 测试，0 失败、0 错误**；完整 Kotlin 编译/Debug DEX 打包通过。
- [x] **Android 启动验证**：修复版 `:app:packageDebug` 安装到 API 36.1 模拟器，清理 Logcat 并启动，启动 4 秒后进程 PID **3636** 仍存活，`AndroidRuntime:E` 未发现之前的 `VerifyError`。随后尝试连续打开菜单，模拟器 host 再次报告 `error: closed`、ADB offline，无法获得稳定的展开/折叠逐帧图像；未发现可确认的应用 Java 堆栈，不能据此断言触摸完整验证成功。
- [x] **最终构建、测试、发送**：完整 `./gradlew :app:compileDebugKotlin :app:testDebugUnitTest :app:assembleDebug --offline --console=plain --max-workers=1` 运行成功，Gradle `BUILD SUCCESSFUL`，测试 XML 汇总 **281 tests / 0 failures / 0 errors**。自动改名 APK：`app/build/outputs/apk/debug/liquidglass27-debug-20261102-211321-989.apk`（47MB）；`:app:sendNamedDebugApkToQQ` 日志 `Send script finished`，QQ 对端是否实际接收尚未核实。`adb devices` 此刻无设备，模拟器在后续交互时意外退出。
- [ ] **实机逐帧验收**：长按一级入口→二级打开→按住二级标题返回，检查遮罩圆角始终固定而外层玻璃卡圆角变化；有剩余边界裁切时仍沿 16dp 曲率绘制，不能出现阶段性变大、变小、四角被削平或折叠末帧跳变。此前不重复弹出的 `_landed` 规则与多级菜单仍要确认。

## 2026-11-02 修复按住遮罩圆角与菜单不同、折叠时被裁平（上一阶段；其中动态遮罩半径的处理已被本轮纠正）
- [x] **对照 FlClash 的真实圆角**：`popup.dart::_itemRadius=AppCorner.md`，`_cardRadius=_itemRadius+_cardInset`，`PopupMenuCard._radius=lerp(_itemRadius,_cardRadius,_grow)`，两者均由 `AppShape.all()` 的 Flutter 超椭圆形状绘制。当前移植子菜单 Header、子行和根行使用 `RoundedCornerShape(10.dp)`，相对于 `GlassSubmenu` 外轮廓 16dp→24dp 的 Flutter RSuperellipse 不一致。收缩到初始行高度时，标题矩形与卡片轮廓重合，却因为 10dp 圆角和 16dp 超椭圆不同被硬裁掉四角。
- [x] **统一曲率**：新增 `MenuPressedRowSuperellipse : Shape`，取 `gooPlatterOutline(Rect.Zero..size,16*density)` 的现有 `flutterRSuperellipseOutline`，统一替换根菜单和子菜单行 10dp 普通圆角。不新建另一套贝塞尔计算，也不改变 Sketch 玻璃 shader/材质。
- [x] **先确定可见遮罩再绘制**：子菜单 Header 早期内容宽度按 `settled/.30` 增长，而卡片是按整个弹簧进度增长，因此中间帧 `headerMask` 两端越过 `panel`。旧逻辑先在 Row 画长矩形，再使用外部 `clipPath(contentOutline)` 剪掉露出的圆角，造成切平。新增 `menuPressedHeaderVisibleBounds(raw, panel)` 求交集，`menuPressedHeaderVisibleRadius(visible,panel,cardRadius,itemRadius)` 随实际卡片半径和可见间距连续计算（**这一步动态计算圆角后来被确认与 FlClash 原版不符，已在最新章节修正为固定 16dp**），进度 0 时 16dp 与卡片一致、进度 1 时 24dp 卡片内缩 8dp 的标题仍为 16dp。使用 `gooPlatterOutline` 在菜单现有 `drawWithContent{clipPath(contentOutline){...}}` 绘制 Header 形变填充，再绘制文本；避免裁掉超出区域后才变出不完整圆角，保留一条材质录制和唯一卡片剪裁路径，文字位置/宽度仍走原进度。
- [x] **回归验证**：在 `MenuPressedMaskMorphTest` 基础上新增 3 项，覆盖 `.1` 进度下原始 header 遮罩两侧超出面板、求交后的可见范围、0~1 全段卡片与 item 半径、与 Flutter 拟合超椭圆曲线一致及源码接线。完整 Kotlin 编译及 **279 项 JVM 测试（0 失败、0 错误）**通过。
- [x] **Android 启动及二级菜单 smoke test**：新构建 `app-debug.apk` 安装 API 36.1 模拟器，启动 `pidof=2227`，通过 ADB 点击 `•••` 进入一级菜单，再点 `玻璃参数` 成功进入二级、看到 `Regular · 清透` 等项目，进程存活，`AndroidRuntime:E` 无新的 FATAL / VerifyError。在模拟器上继续点击二级标题收回时，模拟器本体再次 `error: closed`/设备 offline 并退出，未取得可靠的收缩过程录像或应用 Java 崩溃证据，因此不能宣称折叠视觉已经真机验收。
- [x] **正式构建及 QQ 自动发送**：`./gradlew :app:compileDebugKotlin :app:testDebugUnitTest :app:assembleDebug --offline --console=plain --max-workers=1` 返回 `BUILD SUCCESSFUL`，测试 XML **279 tests / 0 failures / 0 errors**。改名 APK `app/build/outputs/apk/debug/liquidglass27-debug-20261102-195125-797.apk`（约 47MB）；`:app:sendNamedDebugApkToQQ` 日志返回 `Send script finished`，对端实际接收未独立核实。`adb devices` 此刻无设备。
- [ ] **真机画面验证**：按住二级标题缓慢折叠、快速折叠、连续进出三级，检查遮罩角部是否与整体超椭圆外形对齐且不会在收缩中裁成直角；重点对比 FlClash 60/120Hz 连续帧。模拟器 host 断开无法替代真实设备验证。

## 2026-11-02 对齐 FlClash 按住遮罩随二级菜单展开/收起形变（上一阶段）
- [x] **原版实现查证**：`FlClash/lib/ui/popup.dart::_buildRow` 的按住区域不是一张独立固定位置遮罩，而是 `DecoratedBox(ShapeDecoration(color: _pressed == index ? highlightColor : null, shape: AppShape.all(_itemRadius)))` 直接属于 `PopupMenuCard` 子内容。展开/折叠时同一个 `_unfold` 弹簧驱动整卡形状、宽度（`_widthSpan=.3`）、内容和按住行的动态形状。以前 Compose `GlassSubmenu` 对标题按住色添加 `settled>=.85` 硬性阈值、对子行添加 `settled>=.94` 阈值，而且 `!interactive` 后清空 `hoveredRow`，所以标题按住遮罩只在展开末段突然出现，在折叠一开始立即消失，完全缺少随菜单变化的形变。
- [x] **按住遮罩使用与标题相同的真实位置/宽度轨迹**：新增 `MenuPressedMaskMorph.kt::menuPressedHeaderMaskBounds`，复用原版 `.3` width span 与 `glassSubmenuHeaderTopPx()`，计算 `progress=0` 时与原父菜单入口同宽同位置、`progress=1` 时与子菜单标题同位置的连续矩形。`GlassSubmenu.kt` 从同一几何返回值推导 contentWidth/contentLeft/contentTop，避免文字和按住圆角矩形各走一套曲线。按住色直接作为 Row 的 clip/background，在 `GlassCaptureBehind` 的统一 `contentOutline` 内跟随整体子菜单 `glassSubmenuInterpolatedShape` 及进度变化，打开和关闭的同一进度使用完全相同的路径；删除 header `.85` 和 row `.94` 两处强制延迟遮罩开关，没有引入额外遮罩 Animator、独立玻璃 pane 或光学采样。
- [x] **折叠时持续按住与末帧父行接力**：新增 `menuPressedMaskHoverForLevel(interactive,gestureHoveredRow)`，即使折叠使 `interactive=false`，仍允许手指按住的 Header (`-2`) 在卡片缩回期间画出遮罩，但非交互的其他行不被错误高亮；完全折叠后，在首次子菜单移除前把 `gestureDepth/gestureHovered/rootHovered` 同步转到父级原入口；深层 `popNested()` 在 dropLast 前同样同步到其父级行。`MenuFoldPressHandoff` 和 `_landed` 仍保持上一阶段修复后的状态，避免手指一直不动时重新触发 400ms 打开。只有按住的标题会接力；手指已松开或系统返回不凭空新增遮罩。
- [x] **回归**：新增 `MenuPressedMaskMorphTest` 8 项验证入口/终点矩形、无 `.85` 突变的连续宽度位置、双向同路、非交互折叠期仍有标题 mask、真实渲染接线/剪裁、一级和深层折叠的父行接力以及无额外 Shader/Animator。JVM 计数本阶段 **276 项测试、0 失败**。保留原版 `FlClashMenuSprings` / Sketch 玻璃采样与 Goo 阴影。
- [x] **Android 运行时 smoke 验证**：重新构建并安装到 Windows Android API 36.1 模拟器，`am start` 后 `pidof` 返回 **2504**，`AndroidRuntime:E` 无新的 FATAL；通过 `adb shell input` 已打开一级菜单，再点 `玻璃参数` 展开二级菜单，UIAutomator 已确证 `Regular · 清透` 等二级项目且 PID 仍为 2504。尝试通过模拟器长按二级标题时模拟器 host 进程再次退出（`adb` 变 offline/随后无设备），未有可确认的应用 Java 崩溃；所以折叠遮罩**尚未进行可靠的实机/逐帧视频验收**。
- [x] **本轮最终构建/发送**：`./gradlew :app:compileDebugKotlin :app:testDebugUnitTest :app:assembleDebug --offline --console=plain --max-workers=1` 返回 `BUILD SUCCESSFUL`；测试 XML 统计 **276 tests / 0 failures / 0 errors**；最终 Debug APK `app/build/outputs/apk/debug/liquidglass27-debug-20261102-192932-420.apk`（47MB），`sendNamedDebugApkToQQ` 日志返回 `Send script finished`；对端实际送达未独立核实。ADB 此刻无设备。
- [ ] **真机最终验收**：按住一级入口停留 400ms→二级从入口伸展为标题，按住二级标题 400ms→遮罩连贯回缩到一级入口并在手指仍按着时保留；检查三级菜单、快速反转、真实 120Hz 和阴影融合，确保和 Flutter 原版视频逐帧对齐。

## 2026-11-02 修复新版 APK 启动即闪退：ART VerifyError（上一阶段）
- [x] **确切崩溃定位，已由真实 Android 系统复现**：先在 Windows 的 `Medium_Phone_API_36.1` 模拟器安装 `liquidglass27-debug-20261102-190257-965.apk`，`am start -n dev.liquidglass27/.MainActivity` 之后无存活 PID；`logcat` 明确报告 `FATAL EXCEPTION: main` → `java.lang.VerifyError: Verifier rejected class dev.liquidglass27.glass.GlassMenuKt` → `GlassMorphMenu$lambda$104` → `type Float unexpected as arg to if-eqz/if-nez`（DEX `classes5.dex`，offset 0xEC8）。这不是 Kotlin 编译错误或 NPE，而是 Android ART 加载巨大 Compose 生成 lambda 时拒绝 DEX 类型校验，原 JVM 单测和 Gradle build 无法检测。额外安装上一版 `liquidglass27-debug-20261102-185332-904.apk` 并启动，能获得正常 PID（4019），证实错误是在最新新增的 handoff 捕获之后触发。
- [x] **精准修复且保留原版长按交接语义**：上一版本 `GlassMorphMenu` 引入 `val foldPressHandoff = remember { MenuFoldPressHandoff() }`，把额外一个对象引用捕获进已很庞大的 `GlassMorphMenu$lambda$104`，导致当前 Compose/D8 生成无效寄存器类型映射。改为把 `MenuFoldPressHandoff` 放到**本来已被主 Compose lambda 捕获且 remembered 的 `NestedMenuMotionJobs` 所有者**中：`NestedMenuMotionJobs.foldPressHandoff`。折叠完成通知、事件代次以及 `FlClashMenuPress.foldedToParent` 的逻辑和时机不变；一/二/三级菜单复用同一 handoff。新增静态结构回归断言，避免再次把独立 handoff reference 添加到巨大的 Compose 闭包。
- [x] **复测 Android 真正启动**：修复后用 `:app:packageDebug` 构建，`adb install -r app-debug.apk` 到同一 API 36.1 模拟器，`am force-stop`、`logcat -c`、`am start`，启动 4 秒后 `pidof dev.liquidglass27` 返回 **4425**，`AndroidRuntime:E` 无新的 FATAL；通过 UIAutomator 读取到 `GlassShowcase` 的按钮并使用 `adb shell input tap 780 403` 打开一级菜单，进程仍为 4425，UI 显示 `玻璃参数/切换深浅色/测试长列表` 等实际菜单项。继续深入模拟时模拟器本身断开并退出，**未取得二级长按的模拟器或真机完整验证**，不能声称全面通过。模拟器退出时没有可确认的应用 Java 崩溃堆栈。
- [x] **测试、打包与自动发送**：新增 `MenuFoldPressHandoffTest` 1 项针对闭包捕获的回归校验，完整 `./gradlew :app:compileDebugKotlin :app:testDebugUnitTest :app:assembleDebug --offline --console=plain --max-workers=1` 成功。**268 项 JVM 测试、0 失败、0 错误**。最终修复 APK：`app/build/outputs/apk/debug/liquidglass27-debug-20261102-191437-623.apk`（47 MB），`sendNamedDebugApkToQQ` 返回 `Send script finished`。QQ 实际对端送达尚未核实。此刻 `adb devices` 为空，模拟器已经退出。
- [ ] **额外实机回归**：用户侧安装此修复版后确认首屏加载、打开一级菜单、二级标题长按返回不自动再展开及二级/三级多次往返。后续如还有不同崩溃须采集新 Logcat 判断，不能混同本次已修的 ART VerifyError。

## 2026-11-02 修复长按子菜单标题折回后同手指重复展开（上一阶段）
- [x] **严格对照 FlClash `_pop()`**：`FlClash/lib/ui/popup.dart` 在折叠弹簧真正结束后执行 `final step = _path.removeLast(); if (_pressed == _headerIndex) _pressed = step.index; _unfold.value = 1`，**没有**重新运行 `_hold()`，也没有把 `_landed=true` 改回 false。于是手指始终按在原入口上，`_slidePress` 因 `index == _pressed` 不会启动新的 400ms 定时器；只有移到不同索引后再移回才可能重新长按展开。旧版 Compose `FlClashMenuPress` 在 ToggleHeader 完成后始终停留在 Header（index=-1），折叠结束接下来的 Move 命中父级原入口，错误地作为新的行引发 `_hold()`/dwell，使二级菜单自动再展开。
- [x] **恢复原版按压索引归属**：新建 `MenuFoldPressHandoff.kt`，在首次子菜单 `LaunchedEffect(submenuOpen,submenuIndex)` 折叠完成、`submenuIndex=-1` **之前**，以及深层 `popNested()` 折叠完成、`nestedPath.dropLast(1)` **之前**发布带 sequence、原层级 depth 与入口行 index 的 FoldReturn。`GlassMenuGestures.kt` 新增 `FlClashMenuPress.foldedToParent()`：只有**同一层级**的仍在 `_landed` 中的 Header 才从 Header 转成父级 Row(index)，**保留 landed=true**，不会触发 haptic/新 dwell。普通 pointerInput 及源按钮 GlassCarriedPress 两条通道在后续 Move/Up 命中测试前消化各自本次手势的最新 FoldReturn；新的 Down 记下当前 sequence，拒绝重放旧事件。这样仍按同一入口不再进入二级；主动移到另一项再回来仍可重新选择。
- [x] **回归**：新增 `MenuFoldPressHandoffTest` 9 项包括第一次标题长按折回原入口连续 30 次 Move 不重开、真正移动其他行可重新起算、三级/更深层回到正确父行、点击折叠不干扰、不同层级和旧 sequence 不串入当前手指、无效事件拒绝、新 Down 重置、一级及深层 fold 发布时机和普通/携带触摸双通道接线。Kotlin 编译及全量 JVM 单测均通过。
- [x] **最终构建与自动发送**：`./gradlew :app:compileDebugKotlin :app:testDebugUnitTest :app:assembleDebug --offline --console=plain --max-workers=1` 返回 `BUILD SUCCESSFUL`，XML 统计 **267 项测试 / 0 failures / 0 errors**（此前 258 项，本轮新增 9 项）。APK 已改名为 `app/build/outputs/apk/debug/liquidglass27-debug-20261102-190257-965.apk`（约 47 MB），`:app:sendNamedDebugApkToQQ` 返回 `Send script finished`，未独立确认 QQ 对端实际送达；`adb devices` 为空，未能安装/录屏实测。
- [ ] **设备验收**：长按二级标题（手指始终不抬、位置不移动）直到收回并持续按住超过 400ms，父级同一入口不应重新展开；移到不同项再移回、松开重新按住、三级子菜单折回同样检查。当前需真机实测 Android 事件分发与原版画面逐帧一致性。

## 2026-11-02 长按二级菜单标题收起后父卡末帧额外放大（上一阶段）
- [x] **定位并验证真实跳变来源**：不是 Flutter `_foldSpring` 的 stiffness/damping 出错。以前在二级菜单刚完成折叠、`submenuIndex=-1` / `nestedPath.dropLast(1)` 的那一帧，整张菜单 `ElasticPress` 的参考 `menuFlutterStackLayoutBounds(root, childTargets)` 突然缩减为只含父卡的 `root`。长按标题时 `_lift` 仍接近 1，`menuElasticTransform` 的 `GlassMotion.pressGrowth(longestSide,maxGrowth)` 随较短边界立即增大，加上缩放轴心跳变，父卡就会在正常 0.95→1.0 的回位末尾再瞬间放大一次。构造父卡 280×260px/子卡 375×540px 的测试可稳定复现旧版 Scale 差 > 1%，与按压弹簧物理参数无关。
- [x] **修正 Fold 的 press 参考区域生命周期**：新增 `MenuFoldPressGeometry.kt`。在**首次子菜单折叠**的 `LaunchedEffect(submenuOpen, submenuIndex)` 分支开始前，以及**更深层** `popNested` 启动折叠前，检查仍有按压/拉伸/悬停形变则锁定最后一帧实际菜单 Stack 布局矩形 `menuBouncePanel`，保持 `menuPressPanel()` 返回相同框。子菜单折叠到 0 并从 Compose 树移除后，父卡仍在当前按压中心与长边归一化基准上连续放大，不被切换到小矩形引发二次跳放；`graphicsLayer`、变换逆 hit test、惯性形变预算使用同一参考区域。正常子菜单展开、未按压折叠和原 spring 曲线均不锁定。
- [x] **消除解锁时的次生跳变**：锁定的参考区域会延续至 `menuPressActive=false` 且 `menuLift`（含 hover）及 X/Y 拉伸均回到 ±.001 以内，随后通过 `snapshotFlow` 在自然静止时自动解锁；不在标题 Up、_foldSpring 结束或 pop 路径切换的瞬间释放，避免把突变从折叠末帧转移到松手末帧。锁定仅影响整体形变参考系，不改变子菜单实际布局、阴影、玻璃录制及 Sketch 材质。
- [x] **测试与回归**：新增 `MenuFoldPressGeometryTest` 8 项覆盖旧版复现、折叠最后帧 Scale/center 连续、快点按压未结束、拉伸尚未归位、连续层级变更、没有形变不加锁、锁定期间逆命中精度、首次/深层折叠接线；更新两项旧布局路径断言，全量测试及 Kotlin 编译通过。
- [x] **最终构建/QQ**：`./gradlew :app:compileDebugKotlin :app:testDebugUnitTest :app:assembleDebug --offline --console=plain --max-workers=1` 返回 `BUILD SUCCESSFUL`。JVM 测试 XML 汇总 **258 项、0 失败**（上轮 250 项，本轮新增 8 项）；最终 debug APK `app/build/outputs/apk/debug/liquidglass27-debug-20261102-185332-904.apk`（约 47MB）。`sendNamedDebugApkToQQ` 日志 `Send script finished`，QQ 对端是否实际收到尚未独立确认。`adb devices` 为空，未安装真机。
- [ ] **真机验收**：长按二级和三级标题直到折叠结束仍不松手、慢速/快速松手与快速反转展开折叠，对比 FlClash 原版收尾帧、阴影与视觉轴心；当前 ADB 未连接，数值/结构测试不能替代实机逐帧录像。

## 2026-11-02 差异 7：Flutter Gesture Arena 菜单/滚动/源按钮竞争（上一阶段）
- [x] **核对原版的胜出时机**：`FlClash/lib/ui/popup.dart::_RowPressRecognizer` 的 `_startPress/_slidePress` 观察触点与行高亮但并不立刻 accept；只有 `PointerUp` 才 `resolve(GestureDisposition.accepted)`，如在此之前滚动/其他识别器获胜则 `rejectGesture→_cancelPress`。旧 Compose `GlassMenuGestureSurface` 在 Down 就 `consume()`，且在每次移动中继续消费，父滚动以及其他 PointerInput 根本不能竞争。
- [x] **待判定/滚动获胜/被其他处理器拒绝状态机**：新增 `MenuGestureArena.kt`：在 `Down` 固定真实的 scroll owner，仅当纵向位移超过 touchSlop 且不小于横向位移时该层赢得 Scroll；Down 和 pending Move 都不 consume，在 Up 且未被竞争者消费时才触发 row/header/parent/backdrop。`PointerEventPass.Final` 捕获 Main 之后才消费触点的外部识别器，阻止重复触发菜单操作。任何已被处理的 Up、第二根手指、消失的 pointer 都不再产生行选择。
- [x] **原版核心手势路径保持**：禁用行仍可起始滑动与滑到可用行；只有从真实行/标题 Down 才建立选行候选，菜单留白和外部落点仅可按压/点击外部，不会因拖入某一行产生 400ms 展开或错误选择。列表滚动始终优先于 row-release，并且只由 Down 的原始层级滚动，避免由后续高亮行/快速切层把滚动归属转移到父级；子菜单标题保持独立可点和 400ms dwell。
- [x] **400ms 子菜单交接与滚动来源**：`arena.levelTransition()` 在长按触发子菜单 push/fold 后撤销旧父层 Scroll 的资格，但保持 `_landed` 与 `FlClashMenuPress` 的跨层手指状态，后续移动到子菜单新行/标题仍可重新选取；滚动视口内部的 section/divider 空白允许启动滚动，外边距/固定标题不可误触发。
- [x] **源按钮的 100ms 竞争**：`GlassControls.kt` 在 Down 已被消费时不弹出；`popupPressDelayMillis` 内的未接受 Move 继续检查 touchSlop、Main 和 Final 的外部消费，父滚动接管后取消待执行弹出；Up 若未接受且已被其他处理器消费则不强制打开。已被源按钮接受的指针仍沿用原 GlassCarriedPress 的事件代次与 coalescing，不制造第二次 Down 或重置手指。
- [x] **回归**：新增 `MenuGestureArenaParityTest` 14 项涵盖 row Up 才胜出、纵向 vs 横向拖动、Down 固定层级、不可滚动行、其他识别器消费、第二指取消、外部点击/最后一次 Up 位移、Back 原始触点、禁用行、滚动区间分隔符、源按钮消费与源码连接、长按展开后不让旧父 Scroll 抢走选择。旧 `MenuDragDismissParityTest` 和 `MenuElasticFullParityTest` 的源码断言同步适配。完整 Kotlin 编译、全量 JVM 测试通过，尚未宣称 Android/Flutter 手势框架逐事件或真机的绝对等效。
- [x] **构建、测试、自动 QQ 发送**：`./gradlew :app:compileDebugKotlin :app:testDebugUnitTest :app:assembleDebug --offline --console=plain --max-workers=1` 返回 `BUILD SUCCESSFUL`，XML 汇总 **250 项 JVM 测试、0 失败**（比上轮 236 项新增 14 项）；自动改名产物 `app/build/outputs/apk/debug/liquidglass27-debug-20261102-183548-000.apk`，约 47MB。Gradle `sendNamedDebugApkToQQ` 返回 `Send script finished`，QQ 对端送达未独立核实。`adb devices` 为空，未进行目标设备手势回放与性能记录。
- [ ] **真机验收**：在可滚动父容器里分别做小于 100ms 的轻点、轻扫、超过 100ms 的长按；菜单内部纵向快速滚动、跨行水平拖动、400ms 进入二三级、Up 正好在外部、多指取消及 60/120Hz 连续动作；需核对手势抢占时间线。Compose PointerInput 的竞争模型仍非 Flutter 的内部 Gesture Arena，真机事件顺序不能由 JVM 测试代替。

## 2026-11-02 差异 6：按压/拖拽/惯性冲击动画叠加与中断（上一阶段）
- [x] **修复双 Y 运动通道叠加**：旧实现 `GlassMenu.kt` 把 `menuPullY`（手指拖拽）与单独的 `menuEdgeBounceY: Animatable`（惯性边界）直接相加。新触摸中断/层级切换取消独立 Animatable，可能造成剩余位置冻结、速度清零或双弹簧位移和速度不连续。现移除 `menuEdgeBounceY`，惯性边界碰撞直接将冲击速度添加到**同一个 `MenuRetargetSpring` Y 通道**，在 `addVelocityImpulse` 中保留现有位移和移动速度；仍沿用 `menuEdgeImpactVelocityPx` 方向与预算及 `menuPressSpring()` 的归位物理。玻璃/阴影/文字与点击逆矩阵仍共用同一个整体 Stack 变换。
- [x] **所有权与竞态处理**：新增 `MenuMorphMomentum.kt`，单例 remembered 协调器管理共享 Y 弹簧、是否正在按压和单调递增的 fling epoch。每次新指针、重新按压、菜单层级切换、退出都会使旧 epoch 失效，旧 fling 即使迟到也不能追加碰撞速度；真实 PointerDown 同时取消 `activeScrollFling` 和滚动队列。手指仍按压时拒绝惯性冲击，收起菜单时 Y 平滑回原点而非硬置零；对子菜单展开过程中仍在持续的手指拖拽，不因切层被直接重置位置。
- [x] **X/Y 与按压缩放的真实速度衔接**：新按住时 `menuPullX.springTo(menuPullX.value, menuPullTrackSpec)`，Y 在 `menuMorphMomentum.press(menuPullTrackSpec)` 中也保持当前位移/速度，只将动画物理改为原版 120ms 临界跟手；释放时两个轴改为原 311/.593 的带速度归位。缩放 `menuLift` 保留原版 .975 快点触发阈值和初始速度继承，不在惯性撞边界时强行清零。菜单在按住状态被关闭时显式调用 `endMenuPress()`，避免丢失 PointerUp/Cancel 后缩放滞留。
- [x] **确定性回归**：新增 `MenuMorphMomentumTest` 10 项，覆盖拖拽释放时惯性速度叠加不跳位、撞边界回弹时新手指继承位移/速度、过时代次拒绝、手指按住拒绝冲击、子菜单层级变化保持当前手指、dismiss 不跳位归零、连续反向冲击、60 次按压/冲击交替的矩阵有限性和逆命中精度、非法/溢出速度保护及独立通道消除检查。同步更新 `MenuDragReleaseParityTest` 和 `MenuEdgeBounceTest` 对统一通道的源码断言。**236 项 JVM 测试（0 失败、0 错误）**，Kotlin 编译通过。
- [x] **完整构建与发布**：`./gradlew :app:compileDebugKotlin :app:testDebugUnitTest :app:assembleDebug --offline --console=plain --max-workers=1` 返回 `BUILD SUCCESSFUL`；XML 统计 **236 tests / 0 failures / 0 errors**；改名 APK `app/build/outputs/apk/debug/liquidglass27-debug-20261102-174724-106.apk` 约 47 MB；`sendNamedDebugApkToQQ` 返回 `Send script finished`，未独立确认 QQ 实际送达。`adb devices` 为空，因此没有实机安装或录屏证据。
- [ ] **目标设备验收**：对照 60/120Hz 快速甩动撞底→回弹中立即按住并拖出→松手→连续切换二三级→关闭；检查实际光影帧序、手指坐标及不同设备的系统取消事件。全量 JVM/编译验证不等于真实设备已达到 Flutter 的逐帧等效。

## 2026-11-02 修复拖拽离开菜单时错误收回（上一阶段）
- [x] **真实差异**：`FlClash/lib/ui/popup.dart::_RowPressRecognizer/_endPress` 只会在触摸结束时命中具有 `onTap` 的行才执行对应操作，拖出菜单外 `_rowAt` 返回 null，仅触发 `_cancelPress`，不会关掉弹窗。Android `GlassMenuGestures.kt` 之前 `TargetKind.Outside -> dismiss()` 无条件执行，导致从菜单行/留白向外拉出后松手错误关闭；`TargetKind.Back -> back()` 也可能使拖到被覆盖父卡时误折叠二级菜单。
- [x] **区分拖拽与外部点击**：在一个稳定 pointerInput 手势内保留 `downTarget`、自首次 Down 起的 `movedBeyondTapSlop`；`menuReleaseDismisses` 仅允许 `Outside Down` + `Outside Up` + 未超过触摸 slop 的独立背景点击关闭。菜单内部拖到外部松手只执行 `finishPress()`，整个菜单弹簧自然归位，保持弹窗及层级不变。
- [x] **修复同一手势的对称误触发**：`menuReleaseFoldsParent` 仅允许直接按住被覆盖父卡且未拖动时 Back；从子菜单行拖到父卡不再误收回。`menuCanReleaseRowFrom` 仅允许从菜单行、禁用行或标题起始的行选择手势在目标行抬手选择；从外部或内部留白起始的跨区拖入不会误选中。持续拖动跨行选取、400ms 展开、可滚动列表惯性和用户新增的整卡片惯性边界回弹保持原状。
- [x] **回归测试**：新增 `MenuDragDismissParityTest` 5 项覆盖菜单内部各触点外拖不关闭、真正外部点击关闭、外部拖动不关闭、子菜单父卡直接点击/拖动区别、从外部或留白拖入不能选行；更新 `MenuElasticPressTest` 中旧的 Down 捕获结构断言。全量 Kotlin 编译与 **226 项 JVM 测试（0 失败、0 错误）**通过。
- [x] **打包及 QQ 自动发送**：`:app:compileDebugKotlin :app:testDebugUnitTest :app:assembleDebug` 完整成功；XML 汇总 **226 tests / 0 failures / 0 errors**。改名 APK：`app/build/outputs/apk/debug/liquidglass27-debug-20261102-172232-675.apk`（约 47MB）；Gradle `sendNamedDebugApkToQQ` 日志返回 `Send script finished`。QQ 对端实际送达未独立核实，`adb devices` 当前无设备，未真机安装。
- [ ] **设备验收**：需要真机测试菜单内部按住→拖出屏幕/弹窗边缘→松手后菜单保留、二级菜单拖到父卡不折叠、外部直接点击正常关闭；本阶段的数值/源码单测不能替代触摸事件的真实分发检查。

## 2026-11-02 差异清单 1→2→3→4：整卡片触发/轴心/连续弹簧/悬停（上一阶段）
- [x] **① 整张菜单的按压区域**：`GlassMenuGestures.kt` 统一用 `menuCanElasticPress()` 判断菜单区域，原先仅在 `Row/DisabledRow/Header` 起始才激活形变，现在包括卡片内部留白/分组间隙 `Blank` 和被覆盖的父菜单 `Back`。`Outside` 与尚未启用 `ElasticPress` 的 platter 阶段不激活，保留原菜单选择、标题返回、点击外部关闭与 400ms dwell 路径；从源按钮携带的指针亦使用同一判定。
- [x] **② 栈级布局框而非子卡片 union**：依据 `FlClash/lib/ui/popup.dart` 的 `Stack(alignment:Alignment.topRight)` 和 `ElasticPress(panelOf:null)`，新增 `menuFlutterStackLayoutBounds()`：以根卡片的上边/右边为固定布局锚点，使用各级最终布局宽度的最大值及其最大底部求整个 Stack 的矩形；不再简单 union 各级目标卡片的涂绘坐标。菜单整体缩放、移动、触摸逆变换和惯性撞边界的形变预算共用这一基准，二级/三级展开不跟随即时裁剪边界跳变。
- [x] **③ 逐帧连续弹簧重定向**：新增 `MenuRetargetSpring.kt`，X/Y 拉伸从 `animateFloatAsState` 切换为 Flutter `navigation_dock.dart::_Spring` 等价的持续帧驱动：每次 `springTo` 从当前位移/速度重定向，不取消旧协程或重开动画计时器；按欠阻尼/临界/过阻尼解析解更新，按 Flutter `_Spring(rest=1e-3)` 的位置及速度双重阈值收敛。手指移动跟随 120ms 临界弹簧，松手立即用 311/.593 弹簧回到 0，沿用前一次瞬时速度。空闲时暂停帧驱动，不持续占用 60/120Hz。按压缩放 `_lift` 的既有 .975 反转机制不动。
- [x] **④ 独立悬停弹簧**：新增原版 `ElasticHover` 的 500ms、bounce=.32，即 stiffness≈157.91、damping=.68、目标 hover=.4，按 `press + (1-press)*hover` 混合整体缩放。通过不消费事件的额外 pointerInput 只对 Mouse/Stylus 的 Enter/Move/Exit 作悬停检测，移出卡片回到零；移动触摸不会触发 hover。**区别说明**：原版 Flutter 提供可选 `ElasticHover`，`popup.dart` 本身未显式把它包在菜单外；这里按用户要求使悬停作用于整个菜单区域，属于用户选定的扩展，而非宣称原版每个菜单都有这个悬停。关闭菜单会撤销悬停目标。
- [x] **测试**：新增 `MenuElasticFullParityTest` 8 项覆盖空白/禁用/覆盖父级触发、Stack 右上布局框、按压+悬停混合公式、hover 参数与鼠标过滤、120Hz 每帧连续跟手、松手速度继承、60/120Hz 解析解不受帧率影响、热路径不用重复 animateTo；同步更新 `MenuDragReleaseParityTest`、`MenuEdgeBounceTest`、`FlClashSubmenuMotionVisualParityTest` 的旧实现路径断言。**221 项 JVM 单测全部通过、0 失败**；Kotlin 编译成功。
- [x] **最终构建/发送**：`./gradlew :app:compileDebugKotlin :app:testDebugUnitTest :app:assembleDebug --offline --max-workers=1` 完整成功；**221 项测试，0 失败、0 错误**；`app/build/outputs/apk/debug/liquidglass27-debug-20261102-171522-434.apk`（约 47MB）已自动改名，`sendNamedDebugApkToQQ` 日志显示 `Send script finished`，QQ 对端送达未独立核实；`adb devices` 为空，未安装到实机。当前工作目录并非 Git 仓库，`show_changes` 不可用。
- [ ] **实机验收仍需**：根菜单留白按压、各级子菜单同时显示时的弹性轴心、极快连续 Move→Up→再次 Down、鼠标/手写笔进入离开时的 hover、真实 120Hz 帧时间，及与 Flutter RenderBox 的像素级偏差。Flutter 与 Compose 的事件调度仍不是同一框架；单测不证明逐帧绝对一致。

## 2026-11-02 菜单拖拽回位弹跳与 FlClash ElasticPress 严格对齐（上一阶段）
- [x] **对照真实原版参数定位两处偏差**：`FlClash/lib/ui/navigation_dock.dart::_rubberBand` 使用 `limit * (1-1/(abs(distance)*.55/limit+1))*sign`，现有 Android 菜单却调用了项目通用的 tanh `GlassMotion.rubberBand`；100px 外拖、70px 上限时两者位移差异明显（旧更猛，回位显得过弹）。原版拖动 `_trackSpring` 为 `SpringDescription.withDurationAndBounce(120ms,bounce=0)`，松手 `_pressSpring` 为 `SpringDescription.withDampingRatio(stiffness=311,ratio=.593)`；旧 Compose 跟手/回位均错误使用 stiffness350、ratio.8。
- [x] **独立恢复菜单专用橡皮筋公式**：`MenuElasticTransform.kt` 新增 `menuElasticRubberBand()` 完整移植 Flutter 原公式，并令 `menuElasticPull()` 的 X/Y 均使用该公式；保留 `GlassMotion.rubberBand()` 供其他设计元素使用，不更改 Sketch/镜头/玻璃采样，也不破坏用户之前要求的滚动惯性边界整卡片回弹。
- [x] **跟手和释放切换真实物理**：新增 `menuTrackSpring()`，stiffness=`4π²/.12²`、ratio=1、distance threshold=.001，`menuPressSpring()` 沿用原 stiffness311/ratio.593、阈值改成 .001 对应 Flutter `_Spring(rest=1e-3)`；`GlassMenu.kt` 的两个 `animateFloatAsState` 在 `menuPressActive` 为真用跟手弹簧，松手切换回弹弹簧并把目标从实时外拖位移改为零。由 Compose 连续动画保留即时位置和速度，不另开孤立的归零动作。此前菜单整体缩放 `_lift` 路径不变。
- [x] **数值回归验证**：新增 `MenuDragReleaseParityTest` 5 项：原版倒数拉伸阻尼取样与区别旧 tanh、两弹簧物理精确值、release 穿越静止位形成有限自然回弹、带初速度的 Flutter 欠阻尼解析解逐时间点验证、Compose 主逻辑确实切换 spec 且未额外 snap。**213 项 JVM 测试、0 失败、0 错误**，Kotlin 离线编译与 Debug APK 打包成功。
- [x] **自动改名 QQ 发送**：`app/build/outputs/apk/debug/liquidglass27-debug-20261102-162103-789.apk`，约 47MB；Gradle `sendNamedDebugApkToQQ` 返回 `Send script finished`，未独立核对 QQ 对端实际送达。`adb devices` 为空，未安装设备测试。
- [ ] **真机视觉对照**：将手指拖出菜单边缘 40、100、200px 后松手，比较原版跟手阻尼/回弹超调峰值、回位时机与 60/120Hz 帧率。数值物理和 JVM 测试不能替代实机逐帧视觉验收。

## 2026-11-02 惯性撞顶/撞底后驱动菜单整体拉伸回弹（上一阶段）
- [x] **确认用户要的作用范围与原版差异**：FlClash 的菜单 `ElasticPress` 支持手指拖出菜单外的整卡片拉伸回位，但其 `ClampingScrollPhysics` 不会在列表惯性撞边界时自动触发整卡片形变。本项是用户明确指定的新交互：**列表内容仍钳制，不移动单独的内容层，不改变原版展开形状**；让整个包含玻璃、阴影、文字和菜单层级的 Stack 随惯性冲击形变。
- [x] **连接惯性剩余速度**：`GlassMenu.kt` 在 `ScrollableDefaults.flingBehavior().performFling` 执行后读取剩余速度，只有 ScrollState/LazyListState 确实触及相应顶/底边界且剩余速度大于阈值时，才调用 `menuEdgeImpactVelocityPx`。速度转换遵循原有列表 scroll 坐标与按压方向反向关系，幅度受已有 `ElasticPress` 的 `min(shortSide)*7/32` 预算约束。
- [x] **共用原有 ElasticPress 整体画面**：`MenuElasticTransform.kt` 新增 `menuEdgeImpactVelocityPx`，把冲击换成受限的 Y 方向初速度；新增 `menuEdgeBounceY` Animatable 用 `menuPressSpring()` 从当前 0 位移带初速度飞出再回弹，与现有 `menuPullY` 在 `currentMenuPressTransform` 中相加。原先画整个菜单 Stack 的 `graphicsLayer` 是唯一变形层，hit test 仍走同一逆矩阵，Sketch 的材料/采样、玻璃与阴影没有新增渲染层；没有对 LazyColumn/Column 增加 `overscrollEffect`。
- [x] **并发/生命周期**：新指针开始立即取消旧惯性、清除积压的滚动事件，并把形变平滑带回；切换子菜单层级或 dismiss 时同样取消旧惯性、令变形回到零。`menuBouncePanel` 采用菜单各级最终矩形 union，防止动态展开导致回弹极值和轴心突变。
- [x] **数值及源码回归**：新增 `MenuEdgeBounceTest` 五项覆盖顶部/底部方向、原版弹性极限预算、从零位移带初速度而非跳变、与菜单整体 transform/触摸逆变换共用、退出生命周期。全量 Kotlin 编译成功，**208 项 JVM 测试（0 失败、0 错误）**通过，Debug APK 构建成功。
- [x] **新产物及发送**：`app/build/outputs/apk/debug/liquidglass27-debug-20261102-160650-587.apk`，约 47MB；`:app:assembleDebug` 成功，自动改名和 `sendNamedDebugApkToQQ` 执行成功（日志 `Send script finished`）；没有核实 QQ 对端送达。执行 `adb devices` 返回空列表，没有安装/录制实机动画。
- [ ] **真机体验待验**：快速甩动长列表到顶/底时整体回弹峰值、120Hz 阴影/高光是否同步、冲击动画与先前手指按压释放弹簧相叠、快滑到边界后立即按下/关菜单的打断轨迹。此功能为用户要求的 **FlClash 原版之上新增**，不是原版自带效果。

## 2026-11-02 长菜单手指松开后缺乏惯性：ClampingScrollPhysics 对应修复（上一阶段）
- [x] **原版行为核实**：`FlClash/lib/ui/popup.dart` 的根 `ListView.builder` 和普通 `SingleChildScrollView` 都指定 `ClampingScrollPhysics()`，所以原版手指快速甩动后有非越界的减速惯性；不是松手即停。
- [x] **修复统一手势路径**：`GlassMenuGestures.kt` 使用 Compose `VelocityTracker` 按 pointer uptime 采样 Y 速度，并以 Android `ViewConfiguration` 的最小/最大 fling 速度决定是否触发；手指速度符号取反转换为列表滚动坐标。松手后只在有效滚动且同一层级仍可交互时触发，不影响 400ms dwell、禁用项、菜单选择/Back。
- [x] **非弹跳惯性及中断**：`MenuScrollConflator` 将 fling 排在最后一个 `scrollBy` 事件后，不与手指位移合并或丢弃；`GlassMenu.kt` 在原 UI 滚动消费者中，使用 `ScrollableDefaults.flingBehavior()` 的 Android spline 衰减配合 `ScrollableState.scroll` 滑行，沿用同一 LazyListState/ScrollState 的边界钳制，无越界橡皮筋。新手指按下、切换菜单层级、关闭菜单时取消旧惯性及待处理事件。普通菜单和长菜单共用该路径。
- [x] **回归测试**：`MenuScrollConflatorTest` 新增 fling 顺序、跨代失效、速度阈值与方向检查，现有拖动数据合并/多级手势测试继续执行；Kotlin 编译、**203 项 JVM 测试（0 失败、0 错误）**通过；`:app:assembleDebug` 成功。
- [x] **产物与 QQ 自动发送**：`app/build/outputs/apk/debug/liquidglass27-debug-20261102-154604-926.apk`，约 47 MB，Gradle QQ 发送脚本成功退出并报告 `Send script finished`，对端实际送达未独立核对。
- [ ] **待设备验收**：实机快速甩动的实际减速曲线、列表边界、120Hz、二级菜单展开中快速松手与 Flutter ClampingScrollPhysics 的逐帧差异。Compose Android spline 与 Flutter clamping 都具备边界限制和惯性，但不宣称两者轨迹完全相同。

## 2026-11-02 二级菜单打开时底边弹跳回缩生硬：Flutter 弹簧停止容差修复（上一阶段）
- [x] **定位真实结束瞬间的跳变**：本地核对 `FlClash/lib/ui/popup.dart` 展开使用 `SpringDescription.withDurationAndBounce(duration:420ms,bounce:.15)` 与 `SpringSimulation`；再核对本机 `C:\flutter\packages\flutter\lib\src\physics\tolerance.dart`：`Tolerance.defaultTolerance` 的距离和速度均为 **0.001**。Compose `spring<Float>` 默认 `visibilityThreshold=0.01`，大 10 倍，虽然刚度与阻尼完全一致，但在底边首次超出目标边界附近就提前停止动画。`TargetBasedAnimation` 实测旧容差 `.01` 的终止时长约 412ms、前 16.7ms 进度 1.006278；完成帧直接跳到 1，造成底边快速缩回。
- [x] **只调整动画结束条件、不乱改物理**：`FlClashMenuSprings.kt` 的 `unfold()` 与 `fold()` 明确配置 `visibilityThreshold = .001f`，保持原版 420ms/.15 与 340ms/0 的 stiffness、dampingRatio 不变。一级与深层的 `submenuMotion.animateTo(1f, unfold())` 之后移除多余 `snapTo(1f)`；保留折叠完成后恢复父层状态的必要 `snapTo`，不改 Sketch 玻璃采样、Material、Lens、Blur/AGSL、高光或任何行布局/展开形状公式。
- [x] **数值回归**：`MenuSpringSettlingDiagnosticTest` 3 项检查源码容差、两个 SpringSpec 均按 Flutter 位置精度、0–390ms 物理轨迹不变、末端回弹残差从约 0.00628 降至 0.0012 左右，以及一级/深层不再额外重复归位。**200 项 JVM 单测、0 失败、0 错误**；Kotlin 离线编译、Debug APK 构建成功。
- [x] **产物与发送**：`app/build/outputs/apk/debug/liquidglass27-debug-20261102-153713-023.apk`，约 47 MB；Gradle `sendNamedDebugApkToQQ` 返回 `Send script finished`，退出码 0；没有独立核对 QQ 对端是否收到。
- [ ] **视觉验收**：需要在实机对照二级/三级菜单底边初次展开、弹跳峰值、归位末段、快速点开后关闭，以及 60/120Hz 时阴影/玻璃边沿是否同步。JVM 采样和构建不能单独证明像素级一致。

## 2026-11-02 FlClash 根菜单长列表惰性加载（上一阶段）
- [x] **严格移植触发条件**：逐行对照 `FlClash/lib/ui/popup.dart::_buildsLazily`：仅 `maxVisibleItems != null`、根层且所有项无 `subItems`、无 `subtitle`、无 `section` 时采用惰性行构建；普通菜单、二三级、带副标题或分组菜单继续保留当前 Column 与准确可变行高。新增 `GlassMorphMenu(maxVisibleItems: Int? = null)`，缺省原版 7.5 行高度与非惰性行为不变。
- [x] **真实 LazyColumn**：根菜单通过 Compose `LazyColumn(items(count,key))` 只实例化可见条目；复用相同 `renderRootRow` 及点击、按住高亮、禁止激活、选中语义代码；`LazyListState` 独立于旧 `ScrollState`，不更换全屏统一手势协调器，不改变 Sketch/玻璃/阴影。`userScrollEnabled=false` 避免 LazyColumn 与既有全屏 pointerInput 形成双重滚动源，滚动由同一 `MenuScrollConflator` 串行提交。
- [x] **所有命中/动画读相同绝对滚动位置**：新增 `MenuLazyRows.kt`，把 `LazyListState.firstVisibleItemIndex/ScrollOffset` 转换成与原 `ScrollState.value` 完全一致的逻辑像素偏移，供源行矩形、二级展开锚点、命中行、覆盖高亮、Sketch contentVersion 共用。保留 Flutter 的 7.5 行半行露出与 `checked - visible~/2` 初始整行定位，按上限像素滚动并拆分成 `scrollToItem(index, offset)`；空菜单不调用 `scrollToItem`。
- [x] **演示入口**：正常菜单新增「测试长列表（62 项）」，切换到 62 个无子级/无副标题/无分组根项，设置 `maxVisibleItems=7`，中间选中项、顶底返回入口可检验初始定位及长距离滚动；下一次打开生效。
- [x] **测试/构建与 QQ**：新增 `MenuLazyRowsParityTest` 六项，检查启用/禁用条件、半行上限、勾选初始位置、item-index+offset 转换，以及 UI 代码共用坐标。**197 项 JVM 单测、0 失败、0 错误**；离线 Kotlin 编译与 `:app:assembleDebug` 均成功，自动改名 `app/build/outputs/apk/debug/liquidglass27-debug-20261102-151528-775.apk`（约 47MB），Gradle QQ 发送脚本返回 `Send script finished`，退出码 0；QQ 对端实际收到仍未独立检查。
- [ ] **真机测试及余项**：真实设备观察首次打开 62 项时的帧耗时、飞快滑动、根层滚动恢复、改变可见行上限、焦点/无障碍滚动；Flutter 的 ListView 原生滚动辅助功能与 Android 全屏 pointerInput 交互仍待验证。后续完整 Glyph 需移植 `FlClash/lib/ui/icons/glyph.dart`（24×24 矢量图形，body/detail/line）和 `glyph_painter.dart`（1.8dp round cap/填充）以及 `app_glyphs.dart` 的路径定义；**原版不是图标字体，不能简单换成文字或 Material Icon 冒充**，另有通用 Popup 容器待做。

## 2026-11-02 继续移植 FlClash 菜单字幕、Glyph、checked 语义和 PopupValueText（上一阶段）
- [x] **字幕和图标 API**：`GlassMenuAction` 新增 `subtitle: String?`、`glyph: String?`；共享 `MenuItemContent.kt` 将 20dp 左侧图标、15sp/20sp 标题、13sp/18sp 最多两行字幕、右侧 16dp chevron/check 按原版 `_buildRow` 顺序排列，旧 `symbol` 作为文本 Glyph 兼容入口。当前 Glyph 为字符串/文字绘制替代，**并非完整 FlClash Glyph 类型和字体图形资产**。
- [x] **动态行高、滚动和形变共用测量**：`rememberMenuContentRows` 使用 Compose `TextMeasurer` 按实际卡片内容宽度判断副标题 1/2 行，行高严格为 24dp 内边距 + 20sp 标题行高 + 18sp×字幕行数；无字幕保持 44dp。`MenuContentRows` 携带每行偏移和高度，根菜单和所有子菜单的内容绘制、源行矩形、展开目标卡片高度、卷动、命中测试与关闭返回快照统一使用同一套数据，避免“画对了却点错行”。
- [x] **宽度与缓存依赖纠正**：`menuMeasuredWidth` 纳入文字、字幕、前置 Glyph 与尾部状态标记的宽度；`MenuLabelWidthCache` 以文本、字幕、Glyph、mark 状态组合缓存，不再把 UI 发生结构变化的行错误命中旧尺寸。Demo 中根菜单和一级子菜单各有示例字幕。
- [x] **checked/role 语义**：只为 selected 行提供 `ToggleableState.On`；点击行与子菜单返回标题明确 `Role.Button`，保留禁用 clickable 的语义。不影响手势全屏协调器。
- [x] **PopupValueText 初版源码对应**：新增 `GlassPopupValueText.kt` 和 demo 状态文字入口，采用 FlClash 300ms 时间窗、入场 90–300ms easeOut、退场 180–300ms easeIn、4dp 进退模糊以及 300ms easeOutCubic 尺寸切换，允许跨字串画面重叠、无裁剪。Flutter AnimatedSwitcher 的旧值零布局尺寸/RenderObject 交接尚无真机像素等效证据。
- [x] **测试编译和 APK 打包**：新增 `MenuSubtitleGlyphParityTest` 5 项、`PopupValueTextAndCheckedSemanticsTest` 2 项，更新宽度缓存和先前文字复用静态断言；**191 项 JVM 单测，0 失败、0 错误**；Kotlin 编译和 `:app:assembleDebug` 成功。已生成 `app/build/outputs/apk/debug/liquidglass27-debug-20261102-144411-800.apk`（约47 MB）。
- [ ] **QQ 发送阻塞（并非 APK 构建失败）**：构建后的 `sendNamedDebugApkToQQ` 已改名复制 APK，但用户现有脚本返回 `QQ window is too small for reliable automation`（窗口宽低于 700 或高低于 500），手动重试同样报错；QQ 接收端没有送达确认，需把聊天窗口展开到至少 700×500 后重试。保持用户发送脚本和 Sketch 材质不变，不伪称成功。
- [ ] **下一阶段**：完整 typed Glyph 资源及悬停/焦点语义、根菜单 LazyColumn 对应 FlClash `ListView.builder` 的长列表按需渲染、Popup Route 非模态与 `_PopupPin`，并用设备录屏验证字幕/无障碍/返回动画；Sketch 光学链不修改。

## 2026-11-02 二级菜单开启后一级菜单压暗颜色对齐（上一阶段）
- [x] **确认差异根因是暗层底色，而不是弹簧/透明度**：FlClash `popup.dart::_buildLevel` 以 `colorScheme.surface.withValues(alpha: min(.4, .2*distance))` 在子内容绘制后、卡片裁剪范围内叠色。Android 的 `menuRecedingDim` 与距离公式已经一致，但一级、深层父菜单硬编码 `#24252B`（接近深色卡片 tone15）或纯白，和参考 `seededColorScheme` 的深色 page surface tone6、浅色 page surface tone98 不同。
- [x] **统一根菜单和多级菜单的压暗底色**：新增 `menuRecedingSurfaceColor(dark)` 使用独立 demo 的近中性 surface 近似值：深色 `#121316`（近 tone6）、浅色 `#F9F9FB`（近 tone98）；同时替换 `GlassMenu.kt` 根层压暗与 `GlassSubmenu.kt` 父层压暗，保留原始 `.2*distance` / `.4` clamp、绘制在子内容之上及已存在的轮廓裁剪。没有修改 Sketch 的背景采样、玻璃材质、Lens 或 Blur。
- [x] **验证**：增加 `MenuParentDimParityTest` 3 项检查强度曲线、父级/深层共享配色与避免旧卡片色；离线 Kotlin 编译、全套 **184 项 JVM 单测（0 失败/0 错误）**、Debug APK 构建均成功；产物 `liquidglass27-debug-20261102-141802-670.apk` 约 47MB，Gradle QQ 发送脚本完成。
- [ ] **精确主题色与实机视觉验证**：FlClash `surface` 由主题种子色、色彩模式及纯黑模式动态决定；当前独立 Compose 演示仅有明/暗开关，因此两个色值是接近参考默认中性色阶的近似值，并非在所有自选主题下像素精确。仍需真机确认压暗程度/阴影及静止、动画过渡视觉。

## 2026-11-02 FlClash 菜单根层外阴影、父层高光与文字视觉对齐（上一阶段）
- [x] **修复父菜单高光被错误淡出**：FlClash `popup.dart::_buildLevel` 中根层和退让层的 `rim` 本身不会乘子菜单 `_unfold` 的 `(1-progress)`；Compose 曾额外以 `(1-nestedProgress)` 强制淡出根菜单高光，产生父子层边缘突然变化。现改用 `menuRootRimStrength`：父层保留原始 rim，开场仍由 Goo 融合深度与 route settled 控制。
- [x] **修复返回源按钮时额外画出的 rim**：FlClash `_RenderPopupMorph._paintReturn` 只绘制 Goo 表面和阴影，未向 `_pane.paint` 提供 rim，亦不补画 rim 线。Compose 在 `returning && returnIntoSource` 时明确令 root native rim 为零；普通无源退场不沿用该路径。
- [x] **根层 Goo 外阴影形状与内域裁剪**：FlClash `_paintFrame/_paintReturn` 先为 platter + droplet 生成融合形状，再 `_paintShadows` 使用同一个完整 `path`，且 EVEN_ODD 剪掉内部。Compose 原实现只用 platter 轮廓，现仅在 `rootPlatterActive` 时以 `gooPairOutline(p,d,blend)` 生成阴影 path，并使用偶奇外域裁剪；固定卡片的既有阴影路径不改变。Sketch 采样、材质、Lens、Blur 均未修改。
- [x] **菜单项文字粗细一致性**：Flutter `_buildRow` 使用 `AppTextStyle.subheadline(15,20)` 的 `FontWeight.w400`，无论是否勾选，标题返回行同样如此；Compose 移除根/子/退场内容中无依据的 SemiBold，维持 checkmark 表达选中。
- [x] **回归与打包发送**：新增 `MenuRimAndGooShadowParityTest` 四项，更新根阴影断言；`compileDebugKotlin`、`testDebugUnitTest` 和 `assembleDebug` 全部成功，**181 项 JVM 单测、0 失败 / 0 错误**。自动命名 `liquidglass27-debug-20261102-141105-157.apk`（约 47MB），Gradle QQ 发送脚本返回成功；未核实 QQ 接收端。
- [ ] **真机仍需对照**：整套 shadow path 需观察卡片吸收小液滴时是否有局部黑边、阴影内域光斑、高光在二三级层级间是否贴合轮廓；检查 120Hz 多级弹出帧耗时，未用设备录屏/Perfetto 验证。不标记实际视觉像素级一致。

## 2026-11-02 二级菜单展开体感过猛：严格对照 Flutter 弹簧与菜单栈尺寸（上一阶段）
- [x] **物理参数核实而非主观调软**：逐行核对本地 `FlClash/lib/ui/popup.dart::_unfoldSpring` 与 Flutter `SpringDescription.withDurationAndBounce` 的实际公式；原版 420ms、bounce=.15、stiffness≈223.8、dampingRatio=.85。当前 `FlClashMenuSprings.unfold()` 已正确移植这些物理值，因此**没有擅改原版 stiffness/bounce/持续时间**；强行降低弹簧刚度反而会偏离 FlClash。
- [x] **修复菜单膨胀的动态轴心放大**：原 `menuPressPanel()` 使用 `gestureLevels.last().bounds`，随着子菜单从源行长大，ElasticPress 每帧把更大的视觉缩放施加到不断改变的 top card 基准与中心，造成按压缩放与二级弹簧叠加、看起来弹得过猛。Flutter `navigation_dock.dart::_RenderPressTransform` 在菜单未设置 panelOf 时使用**整个 Stack 的 layout size**。Compose 现在将已挂载各菜单级的**最终布局矩形**做 union（`menuPopupStackBounds`），统一用于整体 press scale、pivot、pull 与命中逆变换；不让 pivot 随子菜单动画剪裁轮廓震荡。
- [x] **移除菜单几何额外放大的行高差**：Flutter 原版 `_rowExtentOf` 是 24dp 垂直 padding + `AppTextStyle.subheadline.leading=20dp`，常规为 **44dp**；旧 Kotlin 人为最小 53dp，放大了卡片目标高度和展开/过冲的绝对位移。现在统一根菜单、各级子菜单及返回内容为 `24+20*fontScale`，并把 row/header 文本调整到 Flutter 对应 15sp/20sp；section footnote 对齐 13sp/18sp。不改 Sketch 玻璃、FlClash 高光和弹簧参数。
- [x] **迁移期间的 Section/命中补齐**：前一阶段进行中的 `GlassMenuSection`、`MenuContentRows`、`MenuSectionDecoration` 已同步把根菜单及多级菜单的分组标题、分隔线映射到渲染和精确行偏移，采用二分查找避免每帧 O(N) 扫描触点。禁用项与选中首帧偏移继续有效，源 `popup.dart` 全行段映射记录保留。
- [x] **离线验证**：新增 `FlClashSubmenuMotionVisualParityTest`（原弹簧参数、稳定 Stack press 基准、44dp 行高及防缩放叠加）和 `MenuSectionParityTest`（分组真实高度、点击间隔、二分命中）；全量 Kotlin 编译与 **177 项 JVM 单元测试、0 失败**；`:app:assembleDebug --offline` 成功，已自动生成 `liquidglass27-debug-20261102-134209-099.apk`（约 47MB），并调用 `liquidglass_sketch/tools/send-apk-to-qq.ps1` 返回退出码 0。QQ 对端接收未独立核对。
- [ ] **设备逐帧仍未验证**：对照 FlClash 二级弹出的 0/100/200/350/420ms，核对形变大小、阴影、菜单整体按压膨胀和按住松手时序。设备未安装测试，不以单测替代肉眼/60–120Hz 视频与 Perfetto 证据。

## 2026-11-02 FlClash popup.dart 全文件逐段对应移植（上一阶段，持续按缺口推进）
- [x] **已建立完整源码范围索引**：`docs/flclash_popup_port_coverage_2026-11-02.md` 对 FlClash `lib/ui/popup.dart` 的全部 **1–2980 行**按连续区间登记 Compose 对应文件、已映射/部分/缺口/按约束保留 Sketch。文档明确区分“源码范围有覆盖审计”和“行为/设备等效已证明”，不能把部分项勾成完成。
- [x] **禁用项**：`GlassMenuAction.onClick` 可为 null；根菜单、子菜单的 clickable、全屏指针命中、层级激活及辅助功能均不会激活没有 callback 的普通项；禁用行以原版约 30% 前景强度显示，但允许从该行开始滚动长列表。带 `subActions` 的行即使 onClick 为 null 仍能正常展开。子菜单子级箭头在选中标记前显示，符合 FlClash `_buildRow` 优先级。
- [x] **选中初始定位**：`menuCheckedInitialScrollPx` 在根菜单每次开启时将已选项尽可能置于 7.5 行窗口中间，遵循原版 lazy/checked 的整行居中偏移意图；不以动画每帧反复滚动，行命中与画面共用 `rootScroll`。
- [x] **Popup 三方向定位**：新增 `GlassMenuPlacement` 和 `MenuPopupPlacement.kt`；支持 `OverAnchorEnd`（默认）、`OverAnchorStart`、`BelowPoint` 和 `avoid` 上下 8dp 避让，覆盖源 `_PopupLayoutDelegate.getPositionForChild/_yBelowPoint` 的主要规则；_PopupPin/Keyboard safeInsets 仍单列未完成。
- [x] **回归**：新增 `FlClashMenuItemParityTest` 与 `MenuPopupPlacementParityTest`；更新受源码重排影响的阴影/覆盖行静态断言。Kotlin 编译及全量 JVM 单测通过，后续执行完整 Debug APK 打包与 QQ 自动发送。
- [ ] **仍未逐行完成**：`PopupValueText` 独立过渡；菜单项 subtitle/section/glyph/checked 语义的完整 Compose 对应；长列表 LazyColumn 惰性构建；通用非模态 PopupRoute、弹出层 safeInsets/_PopupPin；Flutter Gesture Arena 的完整相互竞争行为。详细源码行段与实现顺序见上述映射文档，不能将本阶段视为整体 100% 完成。
- [ ] **真机验收**：新定位模式、禁用行长列表滑动、选中滚动首帧、120Hz 连续进入子菜单、按住回退、阴影/高光。保留用户既定不主动使用 ADB 的约束；Debug 构建成功后继续按最新授权自动改名发送 QQ。

## 2026-11-02 根菜单外阴影开场突然出现：Goo 与卡片交接连续性（上一轮）
- [x] **根因修复：Goo 阶段根菜单没有外阴影**。此前 `GlassMenu.kt` 的 `MenuCardShadow` 仅在 `!rootPlatterActive` 时创建；当 `clock` 完成从 0 到 1 的开场、底板切换为固定卡片，阴影从完全没有直接切成接近 100%，引起明显突然出现。对照 `FlClash/lib/ui/popup.dart::_RenderPopupMorph._paintFrame`，原版在 Goo platter 从第一帧就调用 `_paintShadows(..., strength=settled)`；现已消除 `!rootPlatterActive` 的 shadow 条件分支，连续采用 `menuCardShadowStrength(progress=frame.settled,receding=...,distance=...)`。顶层在 0–1 开场渐显；进入二级时，祖先阴影由 `menuRecedingShadow(0)=1` 无缝接管；关闭沿同一曲线渐弱。没有添加额外延迟或独立动画。
- [x] **阴影不再受玻璃局部裁剪范围影响**。FlClash 外阴影 blurRadius=29dp，Blur sigma 及 8dp 偏移的实际覆盖范围可超出局部 36dp 预留边距。现在把根阴影绘制移动到菜单全屏根坐标的独立 `MenuCardShadow(Modifier.fillMaxSize())`，随 `rootPlatterActive` 选择动态 `frame.platter` 或稳定 `card` 的超椭圆轮廓，并复用同一个角曲率 `rootPower`；底板、菜单内容、Root/Sketch 光学采样区域的 `pad=36dp` 和 `GlassPane` 不变，不允许第二份交接阴影。
- [x] **回归测试与构建**：新增 `MenuRootShadowContinuityTest.kt` 4 项，验证开场从 0 增强、Goo→固定卡片强度连续、绘制在局部玻璃 Box 外且仅一份、完整关闭回收时渐弱。原版 450ms 弹簧进度可能约 0.9994 而非精确 1，测试按真实弹簧值比较。全项目 **159 项 JVM 测试，0 失败**。离线编译、`:app:assembleDebug` 成功；自动复制 `app/build/outputs/apk/debug/liquidglass27-debug-20261102-130420-009.apk`（约 47MB），调用 `C:\IDE\Android\liquidglass_sketch\tools\send-apk-to-qq.ps1`，Gradle 记载 QQ 脚本成功退出。
- [ ] **真机视觉验收**：确认根菜单开场 0–450ms 阴影逐渐浮现，Goo/固定卡片交接瞬间没有阴影跳闪、截断或重影，并检查二三级返回的阴影连续性与性能。未进行 ADB 安装，QQ 客户端对端接收也未独立核对。仅确认源码、JVM 和 Gradle/发送脚本通过。

## 2026-11-02 菜单短点击放大延续时间修复（上一轮）
- [x] **按 FlClash 的 97.5% 门槛切换回弹**：对照 `FlClash/lib/ui/navigation_dock.dart::_ElasticPressState._release/_settle`。原版按下时启动 `_lift.springTo(1, _pressSpring)`；手指松开仅设置 `_releasing=true`，保持向上弹簧继续运行；`_lift` 第一次达到 `_liftSnap=.975` 便立即把目标切为 0，而不是等待弹簧达到 1 后彻底稳定。旧 Android `endMenuPress` 在 Up 处取消上升并重新 `animateTo(1)` 到**完全结束**，使点击放大额外停留过久。现在新增 `FLCLASH_MENU_LIFT_SNAP=.975f`、`shouldSettleMenuPress`，在 Up 后用 `snapshotFlow{menuLift.value to menuLift.velocity}` 等待首次越过 .975，随即 `cancelAndJoin` 原上升 Job，并沿该瞬间原始速度按同一 `311/.593` spring 反向至 0。长按已超过门槛时松手立即反弹。**没有新增固定延迟，不修改按压幅度或 Sketch/FlClash 玻璃光学效果。**
- [x] **反复点击的速度连续性**：新 Down 如遇到上一轮回弹，先捕获 `menuLift.velocity` 再取消上一 Job，新上升弹簧使用 `initialVelocity=inheritedVelocity`，避免速度突变。按住仍保持放大，松手才触发阈值回弹。
- [x] **回归与构建**：`MenuElasticPressTest.kt` 新增门槛边界、逐帧首次跨越 .975、原上升弹簧与速度保留、回弹中重新按下速度继承等单元测试；全项目 **155 项 JVM 测试通过、0 失败**。离线 `:app:compileDebugKotlin :app:testDebugUnitTest :app:assembleDebug` 成功，APK 自动复制为 `app/build/outputs/apk/debug/liquidglass27-debug-20261102-125310-276.apk`（约 47MB），Gradle 通过用户指定的 `C:\IDE\Android\liquidglass_sketch\tools\send-apk-to-qq.ps1` 自动发送，脚本返回退出码 0。
- [ ] **真机待验**：慢按松手、极短点击、回弹中再次点击和按住拖动需在目标设备核对 Flutter 首次越过 0.975 的画面时序与 Android 60/120Hz 帧采样；离线测试不能保证绝对毫秒级等效。未操作 ADB，也未独立确认 QQ 对端已接收。

## 2026-11-02 二级关闭阴影突变与菜单按压放大（上一轮）
- [x] **顶层阴影沿弹簧收放**：对照 `FlClash/lib/ui/popup.dart::_buildLevel`：顶层子菜单 `shadow = settled`，退后的父级才用 `max(1/6, 1-distance/3)`。此前 `GlassSubmenu` 无论折叠进度如何都采用 `menuRecedingShadow(0)=1`，因此阴影一直满强度到 `submenuIndex=-1` 才突兀消失。新增 `menuCardShadowStrength(progress,receding,distance)`，子级展开/折叠时按 progress 从 0→1→0 平滑衰减；父级继续按距离公式。根菜单阴影也遵循相同规则。不修改已存在的阴影 blur、颜色或 Sketch 玻璃材质。
- [x] **菜单整体按住/点击弹性放大**：FlClash 是外层 `ElasticPress(enabled=!platter,pullPastEdges=true)` 缩放整个 Popup Stack，并非仅给被按行着色。新增 `MenuElasticTransform.kt`，遵循 FlClash `growth=min(.25,16dp/longestSide)`、按包围盒中心缩放、越界橡皮筋 pull（7/32 最短边、pullStretch .5）以及 press spring 311/.593。整个菜单及阴影放在单一 `graphicsLayer` 下，背景和全屏手势接收器不随之缩放；从源按钮连续携带的拖动与新手指按下均可启动膨胀，短点击先涨足到 1 再弹回到 0。
- [x] **放大后的命中逆变换**：菜单按压层缩放后，原始指针坐标需要先应用与绘制一致的矩阵逆变换，再执行已经存在的父级退让 `paintScale/paintPivot` 命中测试。 `GlassMenuGestureSurface` 的携带事件和独立 Down/Move/Up 都使用统一 `hitAt`；橡皮筋 pull 使用当前变换回到菜单局部坐标后再计算，防止高亮和标题点击再次错位。根菜单 Route 的 _platter 阶段禁止新按压放大，保持 FlClash 原行为。
- [x] **离线回归/打包**：新增 `MenuElasticPressTest.kt` 5 项，覆盖顶层阴影连续性、父级阴影衰减、完整菜单膨胀比例、拉出边缘的 rubberBand 和缩放/位移逆命中；更新 `MenuAnimationParityTest.kt` 阴影断言。全部 **152 项 JVM 测试通过、0 失败**，编译与 `:app:assembleDebug` 成功。自动复制为 `liquidglass27-debug-20261102-123313-568.apk`（约 47MB）并调用 `C:\IDE\Android\liquidglass_sketch\tools\send-apk-to-qq.ps1`，发送脚本退出码 0，Gradle `BUILD SUCCESSFUL`。
- [ ] **真机验收**：逐帧检查二级菜单折叠到 0 过程阴影是否与轮廓同步，长按/快速点击菜单是否整张菜单一起膨胀回弹、越界拖拽时阴影与命中是否同步；尤其核对高刷设备下的 press spring 速度和从源按钮携带指针交接。未用 ADB/未安装，也未独立核对 QQ 对端接收，因此不宣称视频级/设备级完全等效。

## 2026-11-02 二级菜单展开掉帧：非玻璃热路径优化（上一轮）
- [x] **目标布局静态缓存**：`GlassMenu.kt` 的一级目标和深层目标 `Rect` 通过 `remember(parent, origin, actions, screenSize, density, fontScale, labelWidthCache)` 计算一次，动画只调用纯 `glassSubmenuInterpolatedShape` 插值；`GlassSubmenu.kt` 子组件独立缓存相同目标。原实现每帧至少父/子两次 `glassSubmenuGeometry`，且主菜单额外为关闭快照计算一次。本轮关闭快照直接复用 `firstGeometry`，不再第三次计算布局与测量文字。
- [x] **消除静态菜单项版本重复创建**：`GlassSubmenu.kt` 对 `owner.subActions` 选项 `(text,selected)` 版本列表做 `remember`；根菜单对 `actions` 做相同缓存，避免 120Hz 动画过程反复建立大量 Pair/List。仅变化的悬停、进度、滚动和轮廓保持实时更新，没有触碰 `GlassCaptureBehind` 或 Sketch 录制/采样规则。
- [x] **阴影原生状态重用**：`MenuCardShadow.kt` 新增 `MenuShadowPaintState`，缓存 Paint 的 blur/offset/ARGB 参数；参数未变化的帧不再反复 `setShadowLayer + clearShadowLayer`。不改变阴影大小、透明度、颜色、几何或 FlClash 高光。
- [x] **前轮闪帧修复保留**：新一级 owner 或新深度发布前的 pending progress=0 保持生效，不让上一级已经结束的 spring=1 先画出一帧完整菜单再重新展开。
- [x] **代码构建/回归**：新增 `MenuOpenPerformanceTest.kt` 4 项，累计 147 项 JVM 测试通过、0 失败。离线编译 + `:app:assembleDebug` 构建成功（2026-11-02 12:15），标准 APK 及 `liquidglass27-debug-20261102-121550-635.apk`（约 47MB）已生成；Gradle 执行 `sendNamedDebugApkToQQ`，调用用户提供的 `C:\IDE\Android\liquidglass_sketch\tools\send-apk-to-qq.ps1` 返回成功退出码。
- [ ] **性能实机验收**：还需在目标设备用 Perfetto/JankStats 采集二级菜单打开 420ms 内的实际帧耗时、CPU main/render 线程耗时、掉帧数与 GPU 绘制时长。离线测试只能证明计算路径减少，不能声称设备端已不掉帧。未修改 Sketch 的 sampling、Blur、Lens、Material、renderEffect、FlClash 高光，也未使用 ADB；用户最新授权的 QQ 发送流程仍按构建后自动执行。

## 2026-11-02 二级菜单首帧闪现修复 + Debug 自动改名发送 QQ（上一轮）
- [x] **一级二级展开闪现根因修复**：原来根菜单 `submenuIndex=index; submenuOpen=true` 同步发布了新菜单，但 `LaunchedEffect(submenuOpen,submenuIndex)` 在后续协程才对共享 `submenuMotion` 执行 `snapTo(0f)`，旧弹簧通常等于 1，导致二级菜单先全尺寸显示一帧再缩回重播。新增 `menuVisibleTopUnfold`：新一级 owner 与 `firstOwnerForMotion` 不同时在首帧直接用 0 参与全部几何、材质渐进、父层退让和命中计算；弹簧真正确认归零后再恢复读取真实进度。保留正常折叠中途反向时的原速度与位置。
- [x] **三级及更深层同步修复**：`pushNested` 在修改 `nestedPath` 之前预先设置 `pendingNestedDepth`，新路径首次参与绘制时强制 progress=0。只有 `submenuMotion.snapTo(0f)` 完成后才释放保护并启动原 `FlClashMenuSprings.unfold()`。正常折回、完整关闭和全新路由会清理 pending，避免深层菜单继承旧弹簧 1 的瞬间全尺寸闪帧。
- [x] **回归测试与构建**：新增 `MenuOpenFirstFrameTest.kt` 共 5 项，分别覆盖新一级 owner、同一级保持的弹簧、深度 2–7 首帧、祖先层视觉退让连续性、源代码状态顺序；同步更新已有 `MenuAnimationParityTest` 旧断言。离线 `:app:compileDebugKotlin :app:testDebugUnitTest` 通过，共 **143 项 JVM 测试、0 失败**，Debug APK 构建成功。
- [x] **用户最新要求覆盖历史 QQ 禁止项：每次 Debug APK 构建后改名并发送 QQ**。已在 `app/build.gradle.kts` 配置 `assembleDebug.finalizedBy(sendNamedDebugApkToQQ)`；仅打包成功、APK 存在时自动复制成 `liquidglass27-debug-yyyyMMdd-HHmmss-SSS.apk`，保留标准 `app-debug.apk` 供 AGP 使用，再执行用户指定的 `C:\IDE\Android\liquidglass_sketch\tools\send-apk-to-qq.ps1 -ApkPath <命名副本>`。未来通过 `:app:assembleDebug` 构建同样自动执行；QQ 脚本发送到**当前会话**，需确保 QQ 桌面已登录且当前聊天是目标联系人。
- [x] **本轮实际执行**：成功构建、得到 `app/build/outputs/apk/debug/liquidglass27-debug-20261102-120356-713.apk`（47MB）；QQ 原脚本被调用并返回退出码 0，Gradle 记录 `Send script finished`，构建记录 `BUILD SUCCESSFUL`；不能仅根据脚本退出码确认 QQ 服务器或手机端已完成接收。
- [ ] **实机视觉验收**：连续从根菜单进入二级、跨多个一级 owner 切换、二级到三级、快速滑入新层和标题折回。单测证明旧进度首帧隔离，不等于高刷新率设备必然无其它绘制闪烁。
> 本轮未改 Sketch 采样/Blur/Lens/材质、高光公式，也没有 ADB 操作；用户明确授权本轮及以后 Debug 构建后使用 QQ 自动发送，覆盖本文件较早的禁止 QQ 历史表述。

## 2026-11-02 第 19 项完成：单一 PopupRoute 进度与中断反向
- [x] **代码移植**：对照 `FlClash/lib/ui/popup.dart` 的 `CommonPopupRoute.didPop`、`_RenderPopupMorph._handleStatusChanged`、`paint`。删除独立重开弹簧与帧混合函数；同一动画进度 0–1 可前进/反转，按反转起点值归一化关闭绘制时间，保留未完全退出的多级菜单路径。
- [x] **连续快速切换回归**：覆盖打开到 50/150/300/450ms 后关闭、关闭 50/150/300ms 中重新打开、反向后再次关闭、无源图像 200ms 和无残留变换、子菜单路径仅完全退出清理；全项目 138 项 JVM 测试、0 失败，离线构建 Debug APK 成功。
- [ ] **真机逐帧检验**：检查关闭 50/150/300ms 时重新打开的图像首帧、菜单层级显示、透明度/缩放跳变。Flutter 真实 PopRoute 被 pop 后不是原路由重启，不能将 Android 重新激活旧组件理解为 Flutter 同一已弹出 route 的生命周期复活；这里严格对应控制器的时序模型，完整视觉等价仍待真机。

## 2026-11-02 用户指定：菜单动画差异 7–14、18、19（执行记录）
> 范围仅菜单几何、动画、内容绘制、触发状态与非采样阴影。不得更改 Sketch `drawBackdrop`、Lens、Blur、材质、GPU 采样，亦不得改 FlClash 控件高光公式。以下编号沿用逐项差异清单；完成一项后才打勾，测试仅可证明源码/计算逻辑，真机视觉另行验收。

- [x] **7 多级弹簧统一（源码完成）**：`GlassMenu.kt` 将二级至更深层统一交给单一 `submenuMotion: Animatable(1f)`；新顶层 push 时置 0 并按 420ms/0.15 bounce 弹簧展开，pop 从当前值及速度按 340ms 弹簧返回。完成折叠时通过 `parentRestoring` 将路径减一与弹簧归位视为同一视觉状态，避免祖先层瞬间缩放跳动；删除 `nestedMotions` 独立进度列表，保留 Job 生命周期管理，不改 Sketch。
- [x] **8 逐层累计退让（源码完成）**：新增 `MenuLayerMotion.kt` 中 `menuRecedingDistance(cardDepth, topDepth, topUnfold)`，精确采用 `topDepth-cardDepth-1+unfold`，所有祖先层重新计算当前距离，顶层独占展开进度，祖先卡片均取 settled=1；同一距离用于缩放、暗化、阴影与对应命中。
- [x] **9 退让缩放轴心（源码完成）**：根、一级及更深菜单的 `graphicsLayer`/`GlassMenuHitLevel.paintPivot` 改为源行 `top` 而非 `center.y`；与原版 `Alignment.topRight + step.itemTop` 坐标定义对应。
- [x] **10 菜单阴影衰减（数值/Android 绘制已接入）**：`menuRecedingShadow=max(1/6,1-distance/3)` 与 `menuRecedingDim=min(.4,.2*distance)`；新增独立 `MenuCardShadow` Canvas，使用原版 29dp blurRadius、8dp 下移、亮色 .118/暗色 ×1.5 的 alpha，再乘衰减系数。仅画在 Sketch 材质下方；Flutter `BoxShadow` 与 Android `setShadowLayer` 的像素等价尚待设备验收。
- [x] **11 二级标题淡入（源码完成）**：删除 `headerIn` 与标题额外的 `.graphicsLayer { alpha = headerIn }`，让标题按卡片的动态超椭圆路径自然露出；选项行的独立 5–40% 透明度保留。
- [x] **12 原始被覆盖行交接（源码完成）**：当 `submenuIndex` 对应的子菜单存在时，根菜单被覆盖行直接 alpha=0，取消基于子弹簧 `.065` 的渐隐过渡；标题接管显示。
- [x] **13 无界弹簧过冲几何（源码完成）**：`glassSubmenuInterpolatedShape` 以真实 progress 做 `Rect` 和半径插值，不再裁到 `[-.08,1.12]`；header Y 同步无界进度；静态 envelope 增加按 source-target 行程 20% 的边界裕度，防止过冲裁断；非有限值安全回退。
- [x] **14 无源图像关闭动画（源码完成，独立参数分支）**：`GlassMorphMenu(returnIntoSource=false)` 使用 200ms `Curves.easeOut` 近似 `1-(1-t)^2`、围绕 anchor 缩至 .8 + 透明度归零，保留关闭时原卡片画面，不跑液滴；默认 `true` 继续保留 500ms 回归源图像的路线。参数指是否具有源图像路由，不等同于截图异步加载时临时为 null。
- [x] **18 关闭时深层文字等比缩放（源码完成）**：`GlassReturningSubmenuContent` 的 `graphicsLayer` 使用单一 `min(framePlatter.width/panel.width,framePlatter.height/panel.height)` 同时设置 scaleX/scaleY，不再分轴扭曲文字。
- [x] **19 关闭中再次打开（单路由进度控制器已实现，真机视觉待验）**：新增 `FlClashPopupRouteMotion.kt`，采用单一标准化 `clock: Animatable(0..1)` 与 FlClash `CommonPopupRoute` 相同的正反方向、450ms 全开场时长；打开中途反转时冻结关闭帧和 `closingFrom=clock.value`，按 `didPop(): reverseDuration = (500ms/200ms) / max(closingFrom,0.001)` 等价计算本次需要的真实回收时间，绘制映射 `dismissal=1-clock.value/closingFrom`，保证 50/150/300ms 等开场时机的关闭仍走完整回收；关闭中再次打开从当前 `clock.value` 正向走 `(1-value)*450ms`，不重置进度、不再使用 `reopeningMix` / `menuReopeningFrame` 二次弹簧混合；关闭期间保留各级子菜单路径，只有完整退出才清理；普通 200ms 关闭中反向时恢复图层 scale/alpha。`GlassMenuReturnContinuityTest` 与 `MenuAnimationParityTest` 更新后全项目 **138 项 JVM 测试、0 失败**，Debug APK 已构建。**边界**：Flutter PopupRoute 真正 `pop` 后是路由生命周期结束，而 Android 可在旧菜单尚未卸载时将 `expanded` 重新设为 true；这里对齐的是同一个控制器的时间/方向/绘制规则，不保证在没有真机对帧前图像无跳变或与 Flutter 的路由栈生命周期完全等价。
- [x] **最终离线验证（本轮）**：新增 `MenuAnimationParityTest.kt` 10 项，更新 `GlassMenuReturnContinuityTest.kt`，用纯函数测试 shared-depth、折叠后祖先层几何连续性、shadow/dim、过冲、200ms easeOut、等比缩放与重开首帧冻结；`compileDebugKotlin + testDebugUnitTest + assembleDebug --offline --no-daemon --no-configuration-cache` 全部通过，**134 项 JVM 单测、0 失败**，最新 APK 为 `app/build/outputs/apk/debug/app-debug.apk`（该数字为上一轮记录；第 19 项后最新为 138 项，详见下文）。
- [ ] **真机逐帧比对（未执行）**：首次→二级→三级→标题折回、半程折叠反向、50/150/300ms 关闭中重开、显式无源图像退出；需检查阴影渲染、屏幕移动的渐变、触摸命中与帧时序。未安装到设备、未调 ADB/QQ，不宣称已经逐帧等效。

## 2026-11-02 快速滑动错位专项（上一轮）
- [x] **源按钮事件坐标与弹性绘制变换一致**：此前从按钮外侧的布局坐标换算内层 pointerInput 的位置，遗漏内层弹性 graphicsLayer 的缩放/平移。现在在 GlassControls.kt 的 graphicsLayer **之后**获取 pointer 节点 LayoutCoordinates，直接 localToRoot 输出；GlassShowcase.kt 不再用外层坐标叠加修正实时 Move/Up。普通按钮保留原局部位置接口，只有弹出菜单来源按钮开启 sourcePointerInRoot。
- [x] **100ms 延迟确认时不重投影历史 Down**：指针初次按下时、state.pressed 变化前就记录 rootDown；延迟识别后复用该时刻的根坐标，避免用已经弹性放大的矩阵投影旧的 down.position，导致整个快划手势起点漂移。
- [x] **避免快速划动 Move 积压**：GlassCarriedPress 保留 Down / Up / Cancel 的 FIFO 顺序，Move 最多只排一个令牌并取最新指尖位置；新增 generation 过滤快速重开旧事件。补充 10,000 次移动只产生一个待处理 Move、实际最后 Up 不丢、已消费事件位置不可被后续移动修改的测试。
- [x] **缩小/退场图层的命中跟随实际绘制**：GlassMenuHitLevel 引入 paintPivot / paintScale；hitMenu 对根菜单和各级退场子菜单按 graphicsLayer 同一 pivot/scale 先逆变换屏幕坐标，再检查标题、行和裁切区域；新增缩小后父级 Back 命中和动态标题命中测试。
- [x] **构建与测试**：离线 compileDebugKotlin / testDebugUnitTest / assembleDebug 完成，124 项 JVM 测试、0 失败；APK 在 app/build/outputs/apk/debug/app-debug.apk。本轮未修改 Sketch 采样、模糊、Lens、材质或 FlClash 高光与弹簧。
- [ ] **真机待验**：按住源按钮快速斜滑至子菜单/标题，重复快慢滑动、父级缩小中的跨层选择与松手确认；目前尚无触点时间戳、设备录屏和帧耗时证据，不声称已在真机完全复现/解决。

## 2026-11-02 FlClash 标题返回：按原始 RowPressRecognizer 与真实绘制位置纠偏（上一轮）
- [x] **撤销先前对“标题滑入即返回”的错误近似**：重新逐段对照 `FlClash/lib/ui/popup.dart` 的 `_RowPressRecognizer`、`_slidePress`、`_hold`、`_endPress`、`_toggleTop`。原版手指 400ms 停留在可展开行后执行 `_push`，立刻设 `_pressed = _headerIndex`、`_landed = true`；**在新标题上继续停着不会立即折回**。必须先经过另一个有效行/离开旧按压，再重新进入标题并按 400ms 或松手；新手指直接按标题则可停留或短按返回；折叠中的标题只允许松手反向，不自动重新触发停留。
- [x] **按压身份与 FlClash 完全同一粒度**：`GlassMenuGestures.kt` 新增 `flClashPressedIndex`，`FlClashMenuPress.move` 仅比较行 index（标题始终是 `-1`，非行是 `null`），不再比较 Compose 的 `kind/depth/row` 整个对象。保留等待新层首帧可命中的保护及 landed 抑制重复触发；避免跨级时无意义地重置计时/landing。
- [x] **命中标题必须跟随正在画出的卡片，而不是最终目标位置**：`GlassSubmenu.kt` 的内容布局与 `GlassMenu.kt` 的一级/深层手势命中共用 `glassSubmenuHeaderTopPx`，标题 Y 按源行→目标卡插值（含 inset），修复动画中手指已经移动到标题但 hit test 仍落到原位置的问题。
- [x] **标题与内容行分离命中生命周期**：一级和更深子菜单弹簧超过 .08 时继续发布其标题，包含折叠过程中；未就绪的内容行不可选择，并保留真实 action 列表与明确 `rowsEnabled` 标志。期间短按标题可以调 `_toggleTop` 反向，标题 dwell 在 `headerFolding=true` 时禁用；禁止背后父行误触发。只更改菜单几何、触摸、事件状态，不修改 Sketch 光学链或 FlClash 高光。
- [x] **回归/构建**：增加跨层按压 index 等价、非行 nil、动画过程标题实际命中位置、折叠中标题可按而行不可选等单测；离线 `compileDebugKotlin/testDebugUnitTest/assembleDebug`（`--no-daemon --offline`）最终通过，**120 项 JVM 测试，0 失败**。Debug APK：`app/build/outputs/apk/debug/app-debug.apk`。验证期间旧 Gradle daemon 曾报告 EOF/并发报告文件错误；停止旧 daemon 后单独打包，再完整构建通过。
- [ ] **真机未验**：连续从源按钮保持按压→展开二级→离开旧标题后重新进入→停留/松手返回，以及新手指直接点标题、折叠中点标题反向、ElasticPress 的变换后命中仍需目标设备验证。不能因离线 JVM 测试声称与 FlClash 逐帧等效。

## 当前生效约束：仅推进非玻璃部分（用户再次明确，2026-11-01）
> **以本节为准，覆盖下方 ②→③→④→⑤→① 的历史任务文字。** 用户要求：玻璃**使用 Sketch 自己的采样和材质效果**；不要再把 FlClash Haze、折射、色散、对比度等材质公式接入 Sketch。FlClash 只提供 Goo 实际轮廓、原有高光、形变动画、菜单交互及非材质侧的性能优化。**① 整体光学一致性及 ⑤ 中的玻璃采样/图集优化暂停**，除非以后明确重新授权。
>
> **当前实际渲染链**：`SketchGlassBridge.kt` 使用 Sketch 模块的 `drawBackdrop` + `blur` + `lens` + `vibrancy/colorControls`；FlClash `gooPairOutline/gooRowOutline` 用于裁剪，`rememberFlClashControlHighlight` 和 `GlassMenuRim` 用于高光。Sketch Highlight 始终 `null`，只保留单次 tint 覆盖。之前额外引入的 `FlClashHazeEffect` / `FlClashContrastTint` / `SketchGooRefraction` 及其 AGSL **不再从玻璃桥调用，属于未启用的历史实验代码**，此前的 89 项光学测试和 APK 完成情况只作为历史日志，不能代表当前采用 FlClash 材质。

**2026-11-01 用户反馈修复：长按源按钮 → 展开二级菜单 → 触碰二级标题无法返回上一级**
- [x] **修复连续长按手势的标题返回路径**：在 `GlassMenuGestures.kt` 中确认原因：源按钮携带的 `Down/Move/Up` 经过菜单行 400ms dwell 展开下一级后，`unfoldedDepth` 只允许刻意移入下一级 `Row`，所有下一级 `Header` 都被 `continue` 忽略；松手时又仅处理 `Row`，导致按住源按钮一路滑入二级标题再松开无法 Back。
- [x] **统一源携带与普通菜单的延续逻辑**：新增 `mayFinishDwellInChild`，要求手指从触发展开的位置主动移动超过 touchSlop、目标是**更深一层**的 `Row` 或 `Header`。源携带手势进入二级标题后在 Up 调用 `back()`；普通覆盖层指针（在菜单行停留 400ms 后延续）也可在有意滑入新标题后返回；不移动则不会因为新标题出现在原手指位置而自动返回。源携带在 Outside 的松手仍不关闭整个菜单。
- [x] **补充回归**：`GlassMenuGestureHitTest.kt` 增加 3 项：源按钮连续长按进入二级标题、展开但原位置不动禁止误返回、深层菜单只可返回自己的直接父级。完整 `:app:compileDebugKotlin :app:testDebugUnitTest :app:assembleDebug --offline` **通过，109 项 JVM 测试、0 失败**；新 APK 位于 `app/build/outputs/apk/debug/app-debug.apk`。
- [ ] **需实机复测**：连续按住源按钮→悬停二级行 400ms→滑到二级标题→松手返回一级；以及松开手指后用新手指直接点击二级标题、二级→三级的反向路径。当前未安装/运行设备，单测不能证明物理触摸和触觉反馈效果。此修复不改 Sketch 的模糊、折射、材质或 FlClash 高光。

**2026-11-01 20:43 非采样绘制性能继续推进：菜单宽度测量与静止轮廓复用**
- [x] **消除动画期间反复测量菜单文字**：新增 `MenuLabelWidthCache.kt`，用菜单实际标签文本、density 和 fontScale 作键，最多保留 24 组测量结果（LRU）。每个 `GlassMorphMenu` 实例 `remember` 自己的缓存，根菜单、一级和更深层菜单的 `glassSubmenuGeometry` 及 `GlassSubmenu` 共享它；不因按压、滚动、弹簧更新、选中标记或 onClick lambda 更新而重新创建 `android.graphics.Paint` 和逐一测量所有文本。标题、密度、字体倍率变化时自动失效，原菜单尺寸公式与约束完全不变。
- [x] **静止菜单轮廓复用**：根菜单从 450ms 开场转入完全展开、且不是返回动画时，`GlassMenu.kt` 以 `remember(rootPlatterActive, rootSurface, rootRadius)` 复用稳定卡片的 `GlassOutlineRect` 与 Flutter RSuperellipse Path；列表滚动、菜单内部按钮悬停或深层弹簧改变时不再重复构造相同的 native Path。开场/返回的动态 Goo 路径仍逐帧按真实参数计算，不复用过期轮廓。
- [x] **回归测试与产物**：新增 `MenuLabelWidthCacheTest` 5 项，覆盖 5000 帧标签测量仅一次、文字/密度/字体放大失效、选中态/回调不导致重复测量、LRU 驱逐顺序及缓存上限。完整离线 `:app:compileDebugKotlin :app:testDebugUnitTest :app:assembleDebug --offline` **通过，106 项 JVM 测试、0 失败**，Debug APK 已更新。
- [ ] **待真机性能证据**：通过 Perfetto/JankStats 对比多级菜单弹簧与列表滚动的 UI Thread 帧耗时、路径拟合频次及 Jank 分布。源码减少不必要计算，但尚未量化 FPS 提升。
- **边界**：本次没有改 `SketchGlassBridge.kt` 的玻璃采样、镜头、模糊、材质与 RenderEffect；没有改 FlClash Goo 高光/弹簧物理参数；未操作手机、ADB 或 QQ。

**2026-11-01 本轮非玻璃推进：深层返回、误选防护与弹簧协程收尾**
- [x] **系统 Back 与标题反向的语义分离**：对照 `FlClash/lib/ui/popup.dart`，原版 `_pop` 遇到正在折叠直接忽略，只有 `_toggleTop`（再次点击标题）会反向重新展开。现在 `GlassMenu.kt` 的 `popNested(allowHeaderReverse)` 区分两种来源，系统 Back 传 `false`，标题/触摸返回保持 `true`，避免用户连续按系统返回时把正在关闭的菜单意外重新打开。
- [x] **退场菜单行不再误触发动作**：新增 `canActivateMenuRow`，仅当前最上层的可交互行能够触发选择。一级子菜单正在展开/折叠时禁止后台根行、第二层以上展开时禁止父级行；最深层折叠时禁止通过触摸行执行操作。`GlassSubmenu.kt` 同时将辅助功能的 row clickable 绑定 `interactive && settled >= .94f`，避免语义点击绕过全屏命中防护。标题 Back 仍能主动反向。
- [x] **释放关闭后仍运行的深层弹簧任务**：新增 `NestedMenuMotionJobs.kt`，为每层的 unfold/fold 动画协程持有 Job；反向时先取消旧层作业，再启动新作业，关闭/重开根菜单或离开 Compose composition 时全部取消，避免不可见旧 Animatable 在后台继续计算并耗费 UI 线程。完成折叠时从任务栈移除对应条目。
- [x] **测试和 APK**：新增 `GlassMenuTopLevelActivationTest` 4 项及 `NestedMenuMotionJobsTest` 4 项，覆盖最上层触控判定、系统 Back 不反向、标题可反向、替换 Job 取消顺序、根菜单关闭后的旧协程清理和完成折叠移除。完整离线 `:app:compileDebugKotlin :app:testDebugUnitTest :app:assembleDebug` 成功，**101 项 JVM 测试、0 失败**，新 Debug APK 已生成。
- [ ] **仍需目标设备验收**：深层反向过程中动画视觉连续性、系统返回连按、辅助功能触控、120Hz 滚动/动画帧耗时。此轮没有修改 Sketch 采样/模糊/Lens/材质，也没有改 FlClash 高光公式；无 ADB / 安装 / 设备运行。
 
**2026-11-01 18:52 后续推进：多级菜单滚动隔离与高频事件性能（仅非玻璃源码）**
- [x] **首次子菜单切换不继承上次滚动位置**：`GlassMenu.kt` 将一级子菜单滚动状态改为 `remember(submenuIndex) { ScrollState(0) }`，在选择不同一级菜单/关闭后重新打开时生成新滚动状态，保留根菜单自己的 `rootScroll`。同步把串行滚动消费者的 `LaunchedEffect` 加上 `firstSubScroll` 键，防止消费者协程继续持有已失效的旧状态。
- [x] **旧层级的“已取出”滚动批次也会失效**：`MenuScrollConflator.kt` 增加 `MenuScrollBatch(generation, events)`；每次 `clear()` 使代际加一，`GlassMenu.kt` 在逐条应用前检测 `isCurrent(batch)`，折叠/换层后即使事件已离开队列也不继续滚动复用深度。此前只清理未读取的 pending 队列，仍有事件竞态窗口。
- [x] **连续手指移动减少分配**：队列内部由每帧创建不可变 `MenuScrollDelta` 改为同层连续移动就地累积的 `PendingMove`，消费时才创建不可变快照；净位移为零自动抵消，非有限输入不进入队列，保留跨层事件顺序与队列清理行为。
- [x] **离线回归**：扩展 `MenuScrollConflatorTest`，新增旧批次失效、切换前后的新一代独立、跨层应用途中失效，以及 10,000 次移动合并与快照不变性。最终离线 `:app:compileDebugKotlin :app:testDebugUnitTest :app:assembleDebug` **通过，93 项 JVM 测试、0 失败**，Debug APK 已更新。
- [ ] **仍待真机**：快速切换不同一级子菜单后的初始滚动位置、滑动中关闭/返回的触控竞争、复杂场景下的 UI 帧耗时。以上不涉及 Sketch `drawBackdrop`、镜头、模糊、材质或 GPU 图集；未执行 ADB、安装、QQ 或手机端测试。

**2026-11-01 17:51 后续推进：菜单反向与跨级手势状态（仅代码级完成）**
- [x] **源按钮弹性形变中的指针坐标**：演示页记录当前 `LayoutCoordinates`；携带式指针使用 Compose `localToRoot(local)` 做真实坐标转换，代替 `menuAnchor.topLeft + local` 的纯平移假设。这样缩放/位移中的按钮会把同一根手指准确投射到菜单场景；LayoutCoordinates 断开时保留旧位置兜底。未改变 Sketch 或源按钮弹簧曲线。
- [x] **跨层滚动队列生命周期**：`GlassMenu.kt` 在首次子菜单索引和多级 `nestedPath` 变化时清除尚未消费的上层滚动 delta；`pushNested`、`popNested` 进入结构切换之前也同步清理，避免旧深度滚动误施加到刚复用的子菜单 `ScrollState`。保留既有串行消费者及每根手指 scroll owner。
- [x] **阻止未展开完成时快速连开多级**：新增纯函数 `canOpenAnotherMenuLevel`，要求根菜单仍展开、一级子菜单仍打开、当前最深父层已达到与 HitLevel 相同的 0.87f 可交互阈值、没有正在折叠、深度未超过 6 且进度有效。`pushNested` 统一执行守卫，避免迟到的点击在正在关闭的父菜单上创建虚影层。新增 `GlassMenuNestedOpeningTest` 2 项覆盖临界阈值、折叠/关闭、非法进度和上限。
- [x] **验证**：`compileDebugKotlin`、`testDebugUnitTest` 和 `assembleDebug` 离线通过；当前总计 **90 项 JVM 测试，0 失败**，APK 已生成。这里的事件和几何验收是源代码/单测，不代表多点触控与 transform 在真机已完全通过。
- [ ] **待真机**：菜单在父滚动容器内按压 100ms 后跨层拖动、ElasticPress 放大/平移时的实际命中位置、折叠中快速反复打开与关闭、120Hz 帧序连续性。设备与 Sketch 玻璃渲染均未操作。

**2026-11-01 本轮非玻璃继续执行（仅代码级完成）**
- [x] **Goo 几何局部缓存**：新增 `GooRowPathCache.kt`，仅为当前组成中的 Goo 行分别缓存最多 6 个 pill 超椭圆 Path 与最多 5 个相邻颈部 Path（含“无颈部”的 null 命中）；只有实际变化的按钮及相邻颈部重建。Rect、blend 和 density 作为几何键，`SketchGlassBridge` 用 `remember(density)` 将缓存限定在本 pane 生命周期内；**不缓存/不修改 Sketch 背景和光学材质**。总 Path 的真实 UNION 仍依赖当帧各段，绝不把旧成品轮廓生硬复用到移动状态。
- [x] **Goo 菜单连接并集回退**：`GooPairOutline.kt` 在正常状态严格依照 FlClash `Path.combine(union, platter..addPath(pill), bridge)`；若极小弹簧帧使 Android Path.op 失败，不再 `outline.addPath(bridge)` 造成白色 X/交叉内洞，而是回退两 pill 的 union（继续失败则保存无颈部的两片轮廓），下一帧重试。对失败后的 Path 清空残留再回退。
- [x] **回归验证**：新增 5 项 `GooGeometrySlotsTest`，验证固定兄弟 pill 不重建、只重建移动 pill 的两侧颈部、null 缓存/Blend/密度失效、6/5 槽位上限，以及 Path 并集异常不追加交叉桥；离线 Kotlin/测试/Debug APK 构建通过。
- [ ] **需真机视觉与性能验证**：按压中颈部是否仍闪现白色 X，及 Goo 单帧几何耗时和缓存命中改善。未进行 ADB/真机测试，不宣称已经获得 FPS 提升。

**实际后续执行顺序（不涉及玻璃采样）：**
- [ ] **② Goo 形变几何与 FlClash 高光**：已处理局部缓存与不安全 UNION 失败回退；仍需核对动态剪裁/高光在按压和颈部连接时的真机连续性。不改 Sketch Lens/Blur/采样。
- [ ] **③ 菜单动画**：原版 Flutter 弹簧、快开快关、源前景接续和深层 Job 生命周期清理已完成源码适配；本轮进一步修复系统 Back 连按导致折叠反向，仍需真机验证逐帧连续。
- [x] **④ 复杂交互当前子项**：新修复二级菜单折叠中拖动时，旧滚动不再意外落到根菜单；以 `menuScrollOwner` 持有最上层可滚动行，折叠或消失即停止发送该流的剩余 delta，增加二项回归测试
- [ ] **④ 其他交互**：已实现层级切换旧滚动清理、嵌套可交互阈值守卫、后台/退场行激活禁止与语义点击限制；长按/跨级选择、多指与辅助功能设备验收仍待执行。
- [ ] **⑤ 非采样性能**：Goo Path 局部缓存、滚动队列热路径、不可见旧弹簧协程取消、菜单文字测量 LRU 与稳定根卡片轮廓复用均已实施并通过单测；待真机核对 Compose 绘制耗时与实际掉帧，不改 Sketch 的 GPU 背景记录、模糊图集或材质 RenderEffect
- [ ] **设备验收**：Goo 接缝、跨级选择、快速开关、实际 Jank。仅离线测试不能代替设备录屏与 RenderThread 结果。

**本次纠偏执行结果**：已撤销正在生效的自制 FlClash 材质效果，把玻璃桥恢复为 Sketch 原生效果，并保留 FlClash 高光和 Goo 几何；新增的非玻璃滚动归属回归已执行。**83 项 JVM 单测全部通过，Debug APK 已构建**，未操作设备、未改参考项目。

## 历史计划：原版对齐顺序（2 → 3 → 4 → 5 → 1，已由上方约束覆盖）
> 本节是**新的待执行计划**，与上文已完成的源码里程碑区分。严格按 **② Goo 光学 → ③ 菜单动画 → ④ 复杂交互 → ⑤ GPU 性能 → ① 整体光学一致性** 推进。每项完成后更新状态和证据，再进入下一项；不要因 Gradle 构建成功直接标记真机视觉或性能通过。**已从 2026-11-01 按此顺序开始执行；源码阶段结果与设备验收结果严格分开**，当前进展见下方“本轮执行记录”。

### 第一阶段｜② Goo 光学一致性
- [ ] 对照 `FlClash/lib/ui/goo.dart`、`FlClash/shaders/liquid_glass.frag`，逐一核对 Goo 按钮与连接颈部的 union 轮廓、距离场、平滑融合、法线、折射偏移和内缘光带，识别当前 Android 中几何路径与 shader 距离场不一致之处。
- [ ] 统一 **Goo 最终裁剪轮廓、折射法线、高光轮廓** 的几何来源；保留 FlClash 原有高光算法与数值，不用 Sketch Highlight 替代。排查连接处白色 X、竖直切口、灰色重影、按压回弹时光学表面不连续。
- [ ] 补充几何/参数/JVM 回归测试，能用本地 GPU 测量时再验证真实 shader；对照同一按压距离和动画帧的截图/录像。
- **完成标准**：按住、拖动相邻 Goo、形成颈部和松手回弹时，裁剪/折射/高光来自一致轮廓；没有可见内部接缝。未获得设备视觉证据前，只标记“源码完成、真机待验”。

### 第二阶段｜③ 菜单动画一致性
- [ ] 对照 `FlClash/lib/ui/popup.dart` 的开场、回收、`_unfoldSpring`、`_foldSpring`、内容出现/消失区间，核对 Compose 的曲线参数；不可仅凭 stiffness/damping 接近就宣称与 Flutter 的 duration/bounce 弹簧相同。
- [ ] 对齐根菜单和二/三级菜单的几何过渡、Goo 连接、源图标/文字交接、最深层关闭回收、折叠中反向展开；保留前面修复的快速关闭首帧连续性。
- [ ] 建立 50/150/300/450ms 开场后关闭、半程关→开→关、二三级返回等确定性动画状态测试；有运行视频后再做同时间点逐帧对照。
- **完成标准**：形变、透明度和子菜单内容在开闭/反向时无突跳，弹簧轨迹与原版基准尽可能匹配；未实测前不宣称逐帧一致。

### 第三阶段｜④ 复杂交互一致性
- [ ] 核对 Flutter Gesture Arena 与 Compose Pointer Input 的行为差异：源按钮位于可滚动容器时的 100ms 识别、菜单行 400ms 停留展开、短按/长按、拖动跨层与松手确认、滚动竞争。
- [ ] 检查二级到更深层的标题返回、父卡返回、折叠中重开、极端快速连点、列表滚动后触摸命中、手势取消/多指中断及状态清理。
- [ ] 完善事件顺序和命中区回归测试；真机录制“源按钮按下→多级展开→移动→选择/返回”的整条路径。
- **完成标准**：单次指针由正确层持有，无重复触发、误选、回不去或滚动抢事件；Android 平台行为差异要单独记录。

### 第四阶段｜⑤ GPU 性能一致性
- [ ] 对照 FlClash 的层采样、Picture/Shader 缓存、模糊 band/atlas 复用和失效规则，审查现有 Android GPU 常驻图集及其他可选优化路径；不要把“代码存在”当成 GPU 正常工作。
- [ ] 基于有证据的场景测量静态背景、动态背景、Goo 拖动、多级菜单动画：**帧耗时分布、Jank、GPU/CPU 时间、内存/显存趋势、缓存命中与失效**；分别对照 60Hz/120Hz（设备实际支持时）。
- [ ] 根据测量调整分层复用、缓存/纹理生命周期与降级策略，避免过期背景、频繁 GPU readback、重复滤镜或显存无界增长；未验证的实验后端保持默认关闭。
- **完成标准**：关键动画场景在目标设备上稳定运行，无明显卡顿、纹理错帧或异常内存增长，并记录可复现的基线和前后指标；设备未测则保留待验。

### 第五阶段｜① 整体玻璃光学一致性（最后进行）
- [ ] 以 **Sketch 仅为背景采样/材质输入基础设施、FlClash 负责高光** 的既定边界为前提，对照 `FlClash/lib/ui/liquid_glass.dart` 与 `FlClash/shaders/liquid_glass.frag`，核查按钮、菜单、Lens/Frosted 的 blur、contrast、tint/Alpha、色散、边缘暗线和高光合成顺序。
- [ ] 对齐根菜单和多级菜单的前后景层关系、实时透明背景及动态内容采样；严禁自采样、重复折射或叠加 Sketch Highlight。检视 Sketch 光学预设与 FlClash 原参数不一致造成的白块、过亮、边界错位。
- [ ] 结合第一至第四阶段成果做静态/动态背景、多层玻璃和主题模式的最终并排截图/逐帧对比；修正可复现差异，列出因 Flutter 与 Android 渲染栈不同而仍无法等价的边界。
- **完成标准**：FlClash 光学规则及高光主导权不被 Sketch 替换，关键场景已在设备上验证，剩余像素/平台差异有明确记录；未获得设备证据不得标记“与原版完全一致”。

**共同执行规则**：不改动 FlClash 原仓库与 `liquidglass_sketch` 原始源码；每阶段先源码对照和测试，随后在用户允许时进行设备验证；保持 `plan.md` 单一任务状态记录。已完成的“非采样源码收尾”是上一阶段结果，并不意味着上述精度对齐新阶段自动完成。

## 本轮按 ②→③→④→⑤→① 实际执行记录（2026-11-01）

> **执行范围**：五阶段依次完成可独立验证的源码修正；按之前的约束未操作设备，没有把五阶段整体标为验收完成。以下“完成”只表示具体改动/离线测试已完成，不代表整阶段的全部任务或视觉验收完成。

### ② Goo 光学一致性｜已落地的源码修正
- [x] 对比 `FlClash/shaders/liquid_glass.frag` 的 `gooGeometry` 和 Android 端两份 AGSL，发现 Goo 融合时 **高光优先使用 64 射线 profile，而折射使用 smooth-min 法线**。原版 Goo 使用一条 smooth-min 距离场，只有普通非 Goo 独立 outline 才使用 radial profile。
- [x] `FlClashControlHighlight.kt` 增加 `mayUseRadialProfileForHighlight`，对 Goo row / morph 禁用 radial profile；`flclash_control_highlight.agsl` 将 morph/row 分支移到 profile 之前、统一与 `sketch_goo_lens.agsl` 相同的 guarded smooth-min 法线混合。**只修正高光使用哪种法线，不修改 FlClash 高光强度/带宽/算法，也不启用 Sketch Highlight。**
- [x] 新增 `GooOpticsParityTest`（3 项）锁定 Goo 优先级、两 shader 的几何路径以及禁用 Sketch 高光，离线通过。
- [ ] **后续剩余**：CPU union 轮廓与 smooth-min 边缘光的亚像素误差、真实 Goo 颈部折射/高光、白色 X 和灰影的真机逐帧对照。

### ③ 菜单动画一致性｜已落地的源码修正
- [x] 依据 Flutter `SpringDescription.withDurationAndBounce` 的实际实现精确转换到 Compose：`stiffness = 4π² / durationSeconds²`，`dampingRatio = 1 - bounce`（bounce > 0）；`FlClashMenuSprings.kt` 统一 420ms / bounce 0.15 展开、340ms / bounce 0 折叠。
- [x] `GlassMenu.kt` 中根→二级及全部深层展开/折叠分支采用相同配置，保留动画被中断后的当前 `Animatable.velocity`；新增 `FlClashMenuSpringsTest` 3 项。
- [ ] **后续剩余**：Flutter/Compose 动画结束阈值、源按钮/文字与形变曲线的全部关键帧、反向重复开闭真机视频逐帧核验。

### ④ 复杂交互一致性｜已落地的源码修正
- [x] `GlassControls.kt` 的源按钮交接和 `GlassMenuGestures.kt` 的全屏手势：第二根手指按下时立即取消原先单指选择/400ms dwell/滚动，杜绝多指释放后误触发旧菜单项；原 100ms 识别和 400ms dwell 保留。
- [x] `GlassMenu.kt` 改为**单一串行 scrollBy 消费者**，用 `MenuScrollConflator` 合并相同层的连续移动而**不丢失净位移**；避免先前每一帧创建独立 coroutine、`ScrollState` MutatorMutex 互相取消而漏滚。新增 4 项滚动合并/跨级顺序/会话隔离测试。
- [ ] **后续剩余**：多指取消、源按钮与滚动父容器的 Gesture Arena 差异、标题触控、无障碍和跨级松手在目标设备上复验。

### ⑤ GPU 性能一致性｜已落地的源码修正
- [x] `GlassScene.kt` GPU 图集模式从有 pane 变成空场景时，除了清空公开帧引用，还调用 `GlassGpuResidentAtlasCache.clear()`，释放内部持有的硬件图集、band 和源背景引用；`prune` 对空计划做同样清理。
- [x] 新增 `GlassGpuAtlasStats.plus` 与 `beginFrame`，在每个 level 分步构建时统计复用/新建 band 和整层图集的**整帧总量**，不再被最后一层覆盖；新增对应 JVM 统计测试。
- [ ] **后续剩余**：实际 60/120Hz 帧耗时、RenderThread 性能、显存/内存和缓存命中率的真机量测。GPU 常驻图集仍是实验开关、默认关闭，不声称性能已达到 Flutter。

### ① 整体玻璃光学一致性｜已落地的源码修正
- [x] `SketchGlassBridge.kt` 不再通过 `style.tint.red < .45` 推断暗色主题（蓝色 tint 在浅色也会误判、透明 Lens 无法区分）；实际采用调用方已选好的 `GlassStyle` 光学参数，而非引入与视觉无关的冗余主题参数（后一方案在继续核查时撤销）。
- [x] Sketch 的基础 blur/saturation 输入改为 `GlassStyle` 的源数值（普通 2.5dp、菜单 12dp、Alert 16dp、Lens 0dp），而非固定 Sketch 的暗/浅 8/16dp、控制 2dp 或 vibrancy 1.5。**后续已撤销 Pill 的 2dp 近似 Haze**，改为原版基本模糊与 15.6dp Haze 两路 GPU 混合。FlClash 控件 rim/菜单九层高光保持原样。
- [x] 新增 `SketchOpticalInputsTest`，验证主题误判情形、基础 blur/saturation 和 brightness 曝光映射的数值一致性；`exposureAdjustment(ev)` 以 `ev=2.2*log2(brightness)` 保持 FlClash RGB 亮度倍数。
- [x] **继续核对 2026-11-01：Goo 专用折射取消写死的 Sketch `uLens` (12/24 或 24/48dp)，改从 `GlassStyle.refraction`/`bevel` 按 density 写入 `uOptics`。`sketch_goo_lens.agsl` 的 shift 参照原版 `liquid_glass.frag` 与 `flclash_glass.agsl`：正 refraction 按圆弧 bevel 深度沿法线反向读取，负 refraction 用正向凸起 Lens 的两个 smoothstep，宽度按 halfMinor 截断。未触及 FlClash 高光，仍只做一次 Goo 折射。** 新增 `GooRefractionAlignmentTest` 4 项（参数来源与正/负分支数学边界），以及 brightness 映射 1 项测试。
- [x] **继续 2026-11-01：Haze、contrast、dispersion**。新增 `FlClashHazeEffect.kt`，分别从相同 Sketch GPU 背景生成基础 blur 与 Haze blur，按各自 alpha 权重用 `BlendMode.PLUS` 合成，Pill 的 0dp + 15.6dp × 72% 不再用单次 2dp blur 近似。新增 `sketch_contrast_tint.agsl` / `FlClashContrastTint.kt`，5 点背景亮度的 `clash/cover` 公式替代全场固定 srcOver 染色，RenderEffect 故障时退回单次 tint 而不是叠双层。扩大原 `sketch_goo_lens.agsl` 与 Kotlin uniform：普通按钮、菜单 Outline 和 Goo 共享单次 FlClash 正/负 refraction + bevel，色散按法线交换分量分离红蓝通道并在 7–10dp 衰减，Lens 的 `endShade` 使用原版 3px 衰减。渲染顺序调整为**基本模糊/Haze → 折射/色散 → saturation/brightness → tint/contrast → FlClash 单独高光**。
- [ ] **重要剩余差异**：Sketch RenderEffect 两路 Haze 使用同一实时源，但不等价于 FlClash 的独立多 Sigma Frost 图集；对比度的 5 个采样点读取 Sketch 的光学输入而非原版未经模糊的 Raw Atlas；未完全实现 veil、端部真实边缘采样以及广义色彩空间一致性。所有 RuntimeShader 的实机编译和 5 次探测造成的 GPU 帧耗时尚待测量；不能仅凭 JVM 测试宣称原版像素/60–120Hz 完全一致。
- [ ] **后续剩余**：多 Sigma Haze 真正的视觉等价、原版 Raw Atlas 的背景亮度探测、veil/alpha/色彩空间、Goo 和多级前景完整实时采样、动态背景与 GPU 性能的设备逐帧验证。**目前不能声明原版等效光学实现。**

### 本轮源码验证与限制
- [x] 离线 `:app:compileDebugKotlin :app:testDebugUnitTest :app:assembleDebug` 已通过；新增 Haze 两路、对比度公式、折射色散数值/源码和动画权重回归，最终 **89 项 JVM 测试、0 失败**；Debug APK：`app/build/outputs/apk/debug/app-debug.apk`。注意该数字代表 JVM 单测，不是 AGSL 设备 GPU 测试。
- [ ] 未取得手机 RuntimeShader 编译、视觉、帧时长或帧率数据；不将设备验收项目标为完成。后续按上面的原顺序依次处理每阶段尚未证实的差异。

## 本轮后续执行日志（2026-11-01）
- [x] 核对 Sketch 光学颜色映射时，移除先前仅用于替代 tint.red 判断、但没有实际着色用途的冗余 `darkGlassTheme` Local。直接根据已选中的 `GlassStyle` 取 blur/saturation，RGB brightness 通过 Sketch 原生曝光倍率变换；增加 brightness 与源倍数一致的回归测试。
- [x] 继续 ① 阶段，发现 Goo refraction 使用固定 Sketch Lens 强度而非 FlClash `refraction/bevel`；改写 Goo Shader 正/负折射及镜头边缘衰减，对齐原始 FlClash 的光学位移公式并增加独立数学/源码测试。
- [x] 同日下一轮：修正 Pill 2dp Haze 近似为基于同一 Sketch GPU 输入的独立基础/Haze blur 双分支；增加 5 点采样的 FlClash 风格动态 tint 对比度（失败回退固定 tint，不叠加）；将单次 FlClash 正/负 refraction + bevel 扩展到普通按钮/菜单 Outline/Goo，并加入 7–10dp RGB 色散与 Lens 3px 端部渐暗，校正材料计算顺序；补充纯 JVM 参数/阶段依赖/Shader 源码测试。**最终 89 项 JVM 测试、0 失败、APK 成功**。
- [ ] 仍然不包含真机 AGSL / 截图 / 帧耗时验收；尚未声明 ②③④⑤① 五阶段完全验收。继续依次收敛仍未验证的项目。

## 当前非玻璃采样计划收尾状态（2026-11-01）
- **源码与离线检查收尾：已完成。** FlClash Goo/RSuperellipse、源内容交接、多级菜单的触摸/滚动/返回、开场与回收动画、极窄窗口定位、Alert 关闭生命周期均已实施并补充回归测试；最后在工程运行 59 项 JVM 测试 **0 失败**、`:app:assembleDebug` **成功**、`:app:lintDebug --rerun-tasks` **成功**（0 Error，15 Warning，1 Hint）。
- **未勾选的真机验收继续保留**：没有在目标手机上安装、运行、录屏或读取 Logcat。离线源码检查不能确认 RenderThread Shader、Pixel-perfect、120Hz、触摸手势事件流或 GPU 性能已通过。详见 `docs/non_sampling_closeout_2026-11-01.md`。
- **不在本轮范围**：玻璃背景采样、GPU 图集、Sketch 的光学材质与高光修改、外部视频每帧源时间戳（需视频源 callback）及向 `liquidglass_sketch` 反向正式合并。原 FlClash / Sketch 工程源码均保持不变。

## 约束
- 不修改 `FlClash` 原始仓库。
- 按用户要求，打开菜单**不压暗全屏背景**；允许父菜单卡片局部退让和局部蒙层。
- 只在 DevSpace 构建，不安装、不运行设备或模拟器。**Gradle 构建通过不能证明 AGSL 运行时通过，也不能证明与原版视觉一致。**

## 顺序执行与完成情况

### 1. 玻璃层生命周期和遮罩切换 — [x] 代码完成
- [x] 仿照 `_platter` 生命周期：开场由 Goo platter 承载玻璃，展开后切换为根菜单卡片 outline，避免同一根主体重复折射。
- [x] 关闭时恢复 Goo platter，并冻结最上层子菜单轮廓用于返回源按钮。
- [x] 父菜单后退约 5%，局部暗化约 20%；无全屏暗幕。
- 位置：`GlassMenu.kt`。
- 验收：Kotlin 编译成功；层切换的逐帧视觉仍待设备观察。

### 2. contrast / Alpha 光学混合 — [x] 核心代码完成
- [x] 将 `GlassStyle.contrast` 通过 `uContrast` 传至 AGSL；参考原版的背景采样思路，综合 5 处背景亮度调整 tint 覆盖率。
- [x] 对背景采样颜色按预乘 Alpha 计算，调整最终透明度与光能上限。
- [x] 修正 `materialize` 的 haze、暗边及 endShade 缩放。
- 位置：`GlassStyle.kt`、`GlassRenderer.kt`、`assets/shaders/flclash_glass.agsl`。
- [x] 追加 `LocalGlassVeil` / `GlassScene.backdropVeil`：支持在采样中按原版 `veiled(color)` **预乘 Alpha** 混合缺失背景层。全景 GraphicsLayer 捕获默认透明 veil，不会无故给场景加灰幕。
- [x] `uNativeClip`：CPU `gooPairOutline` / `GlassOutlineRect` 已经进行抗锯齿裁剪时，AGSL 不再对相同像素二次透明衰减；普通未裁剪的圆角/按钮仍由 AGSL 覆盖率渲染。
- [x] 超椭圆主体与二级 outline 的法线从 1px 中心差分改为按圆角指数**解析求导**；Goo 桥继续用已经移植的独立法线融合。
- [x] 参照原版 `sampleBehind`：低模糊对比度探针采样改为四个偏移样点的平均；Veil 应用在光学主体、对比度及发暗边缘取样三个路径上。
- [x] 色散在内缘 7–10dp 逐步收拢，保持像素密度缩放；不使用固定 6/10 物理像素阈值。
- **剩余差异**：Flutter 的纹理图集/多级捕获仍未移植；64 方向 profile 已接入，但边缘亚像素内缩和 Flutter 精确 RSuperellipse 曲率并非逐像素等价。AGSL 真机运行和视觉尚未验证。

### 3. 源按钮与 Goo 接管 — [x] 独立内容捕获及动态快照缓存已实现；真机读回效果待验收
- [x] 对照 FlClash `popup.dart`：它使用 `LiquidGlass.withoutPanes` 捕获**没有玻璃背景**的源按钮图像。Goo 与 LiquidGlassPane 则在弹窗整个打开/关闭过程中负责**唯一玻璃主体**。
- [x] 移除当前 `GraphicsLayer.toImageBitmap` 的**整张按钮截图**以及 `photoBlend`，避免原图里自带的圆形玻璃与菜单绘制的超椭圆玻璃重叠。
- [x] 当前示例的源内容只是固定的 `•••` 图标，直接在 Goo 水滴里绘制 `sourceContent`，使用原版接近的图标入场与出场曲线。
- [x] 菜单打开时隐藏源按钮；**仅在返回动画的 `mounted=false` 之后通过 `onReturnComplete` 恢复**，不再由独立的 `delay(500)` 恢复。
- [x] 已为 `ElasticGlassButton` 增加可选 `sourceContentLayer: GraphicsLayer?`，将内容放入独立的透明 `drawWithContent` 捕获层；下方的 `GlassPane` **不会进入截图**。
- [x] `GlassMorphMenu` 新增 `sourceContentImage: ImageBitmap?`，只在 Goo 水滴上绘制透明图像；读回失败时退回原生 `sourceContent`，不会重复绘制整张玻璃按钮。
- [x] `GlassShowcase` 在按钮闲置时预读取不含玻璃的内容图片，点击打开时冻结本次使用的位图，避免动画中途切换纹理；不阻塞手势开启。
- [x] 动态源控件的基础快照刷新：源按钮未被菜单遮挡时，按约 120ms 的有界间隔刷新透明前景位图；按钮进入菜单或执行返回动画时停止读取。
- [x] 快照现在关联像素宽高、单调时钟时间戳和主题标识；打开时仅采用**≤350ms、同主题且与源按钮尺寸误差不超过 2px** 的快照，并冻结整次动画使用的帧。
- [x] 快照三次读取失败后停止本轮主动读取，回退 `sourceContent`，避免长时间无效 GPU readback。
- [ ] 真机位图透明度/抗锯齿仍待用户验收；对于**视频帧逐帧同步（60/120fps）**，120ms 的预缓存不构成逐帧同一时刻采样，未来如需严格视频同步要另接帧时间戳/源回调。
- 位置：`GlassShowcase.kt`、`GlassMenu.kt`。
- **真机风险**：末帧差异与高刷设备上 Compose/AGSL 提交时序仍需用户验证；构建通过不代表完全不闪。

### 4. 行内按压、跨行选择、停留展开 — [x] 源按钮至多级菜单的手势交接代码已完成（待真机验收）
- [x] 新增 `GlassMenuTouchLayer`：按住跟踪命中行、拖动跨行更新高亮、松手执行当前行。
- [x] 带子菜单的行停留约 400ms 后展开，支持触觉反馈。
- [x] 允许长菜单的手势转为滚动；保留可访问的行点击语义、返回键。
- [x] 改为单个常驻的 `GlassMenuGestureSurface` 持有触摸流，按屏幕坐标重新命中当前最顶层菜单；展开下一层不销毁正在跟踪的 pointer ID。
- [x] 展开后继续移动到子菜单项可直接松手选择，且源行的松手不会误点新菜单的返回标题；根层、子层滚动使用共享 `ScrollState`。
- [x] 保留菜单行的无障碍点击语义；带下级的行能由无障碍点击正确打开子菜单。
- [x] **原版按压仲裁时序**：对照 `FlClash/lib/ui/popup.dart` 的 `_PopupPressRecognizer`，在非滚动容器中按下即打开；滚动容器等待 Flutter `kPressTimeout`（100ms），期间超过 touchSlop 取消弹窗让滚动接管；短触摸松开则在 Up 打开。示例按钮位于 `verticalScroll`，现传入 `popupPressDelayMillis = 100L`。
- [x] **源按钮手势交接**：`ElasticGlassButton` 的 `openOnPressDown` 保持兼容旧接口但使用可配置延迟，只有识别器正式接受后才向 `GlassCarriedPress` 发送 Down 并打开；后续继续发送 Move / Up / Cancel。
- [x] **跨挂载缓冲**：新增 `GlassCarriedPress`，用持久事件队列保存按下到弹窗动画可接收事件前的指针轨迹；`GlassMenuGestureSurface` 可以在无第二次 Down 的情况下消费同一手指。
- [x] **防误触**：源按钮轻触抬起打开菜单但不选首行；若弹窗已展开，则已接受的按压可继续拖动选行；等待识别期间的提前拖动只用于滚动，不打开菜单。
- [x] **滚动竞争与取消**：源按钮接管 Down 和移动事件，防止页面 `verticalScroll` 抢走正在进行的选择；指针节点被销毁时主动 Cancel。
- [ ] **真机待验证**：图片快照读取与开场动画之间的延迟、松手时刻、按住进入二/三级菜单的连续性，以及父层滚动抢事件的特殊情况。代码已编译并打包，不等于完成设备体验验收。
- 位置：新增 `GlassMenuGestures.kt`，`GlassMenu.kt`、`GlassSubmenu.kt`。
- 验收：Kotlin 编译通过；滚动竞争、按压连续性及触觉需用户实际测试。

### 5. 超椭圆、融合、高光几何 — [x] 共享轮廓已实现
- [x] CPU Goo 桥接、圆角指数、原生 rim、画面裁剪共用已移植几何。
- [x] 二级菜单玻璃主体、九层高光、文字裁剪使用同一条生成的超椭圆路径。
- [x] 主菜单回收时，rim 与主体的曲率同步从 3.5 向圆形指数 2 过渡。
- [x] 父菜单后退时的局部蒙层改为同一路径裁剪，避免圆角双边缘。
- 位置：`GooPairOutline.kt`、`GlassMenuRim.kt`、`GlassRenderer.kt`、`GlassMenu.kt`、`GlassSubmenu.kt`。
- [x] 已移植 FlClash `liquidGlassProfile` 的 **64 等角度射线轮廓采样**：通过 Android `PathMeasure` 沿真实路径取样，向 GPU 上传 `uProfile[16]`（64 个半径），以 Catmull–Rom 插值的导数同步计算边缘距离与法线。
- [x] 对完整 Goo 轮廓增加**单射线交点判定**：只有从轮廓中心射出的 64 条射线都恰好命中一段边界时启用 profile；拆开的双瓣、不满足星形约束的阶段保留原有 Goo SDF 和形状混合法线。
- [x] 背景颜色、暗边和 rim 仍由同一 AGSL 处理，CPU Path 负责实际裁剪，不再把 profile 误作第二重裁剪或重复 AA。
- **后续更新（2026-11-01）**：已在 `FlutterRSuperellipse.kt` 移植 Flutter Engine 的 octant 分段曲线及参数拟合，正常玻璃/胶囊的 CPU Path 不再是 12 点折线近似；API 34+ 使用原生 conic，API 33 使用切线连续的多段三次曲线。AGSL 已补充 `cornerInset` 原版边缘函数；64 方向 profile 及运行时逐像素视觉仍需设备验证，不能声称完全等价。

### 6. 多级菜单与自适应布局 — [x] 代码完成
- [x] 二级及后续菜单采用路径栈和独立弹簧；当前允许继续下钻最多 6 个附加层级，返回时逐级折叠。
- [x] 菜单宽度依据实际 Android 字形测量并在可用宽度内约束，根菜单最多显示 7.5 行后可滚动。
- [x] 行高和点击区域随系统字体比例扩大；从滚动列表进入子菜单时传递滚动偏移。
- [x] 加入「玻璃参数 → 更多显示选项 → 查看高光参数」三级示例。
- [x] 从深层直接关闭时尽可能使用当前最顶层轮廓返回源按钮。
- 位置：`GlassMenu.kt`、`GlassSubmenu.kt`、`GlassMenuGestures.kt`、`GlassShowcase.kt`。
- **风险**：边缘布局/高倍率字体及深层折叠中途逆转尚未进行设备行为测试。

## Goo 融合恢复：严格以 FlClash 为准（2026-10-31）
- [x] 用户明确要求**完全参照 FlClash，不自行调整效果**。重新对照 `FlClash/lib/ui/goo.dart` 的 `GooSurface.paint`、`gooOutline` 和 `_neck`，以及 `navigation_dock.dart` 的 `ElasticPress.growthOf`。
- [x] 撤回此前擅自将混合强度改为 `gap * 2.5 + 10 * press` 的做法，精确恢复原版 `_restBlend=8`、`_pressBlend=8`；FlClash 的 10dp 默认间距设计上**静止不融合**，随按压膨胀后靠近才形成颈部。
- [x] `GooRowOutline.kt` 改成对原版 `gooOutline(pills, blends)` 和 `_neck(a,b,blend)` 的 Android Path 移植：保留原版每 0.5dp 步进、两侧边缘各 16 次二分、流动区判定、起终点 tip 二分、仅 union 相邻按钮的真实 neck。**2026-11-01 实机截图确认：此前为性能优化改成 WINDING addPath 导致颈部空洞；已恢复 FlClash 每段 `PathOperation.union` 的真实布尔并集（见下方新专项）。**
- [x] `GlassRenderer` 对 Goo 行的 GPU 绘制与无 Shader 回退均使用同一 `gooRowOutline` 真实轮廓；不再在 fallback 中画一排独立 roundRect，亦不使用自行设计的 BitmapShader 双材质回退。
- [x] Goo 行的 `ElasticPress` 恢复原版内容与 Pane 同步膨胀的交互，取消曾经设置的 `maxGrowth = 0.dp`。
- [x] 未启用此前试写的 `flclash_glass_compat.agsl`，不新增第二套主观光学效果参数；仍使用 FlClash 对照迁移的主 AGSL。
- [x] 完整 `:app:assembleDebug` 构建通过；未做设备测试。
- [ ] Flutter fitted-conic 的 Android 33 **自适应 Hermite cubic** 已实现并通过纯 JVM 采样误差回归；AGSL 运行时、长按时 Goo 颈部、菜单开关流动的逐帧视觉仍待用户真机验收。不得将 Gradle 构建成功当作真机与原版完全一致。

## 高光缺失修复：恢复 FlClash 原版着色流程（2026-10-31）
- 用户反馈：Goo 恢复后，玻璃的高光也不显示，要求继续**严格对照原版 FlClash**，不能自行增加高光强度或替换材质。
- [x] 对照原版 `FlClash/shaders/liquid_glass.frag` 的 `sampleFrosted/FROST_DISC`、`shineAcross`、`shineAround`、控制边缘 `shine` 与 `LiquidGlassRim._uniforms`，保留原版控制高光与九档菜单高光的数学公式，不另外创造白边。
- [x] 去掉之前 AGSL 为预模糊增加的 **3 路额外 `uniform shader`**，还原类似 FlClash **单纹理图集入口**的采样形式（Compose 目前传原始背景；GPU 图集直接绑定仍未移植）。模糊使用原版 Vogel 金角序列、16/32/画质档位采样与指数衰减权重，避免此前独立方案中的额外采样分支。
- [x] 64 方向 Profile 也恢复原版 `addTurn/outlineGeometry` 的 16 组**静态索引遍历**，避免以前改成片元动态数组索引造成平台兼容风险；距离和法线继续用于同一高光/折射路径。
- [x] `GlassRenderer` 删除已不再存在的三个子 Shader 的 `setInputShader`/缓存 sigma uniform 设置；`GlassShowcase` 停止为已不参与着色的三档图集生成多余 GPU/CPU 模糊图像，并移除无效的静态 CPU/GPU 切换选项。
- [x] `:app:assembleDebug` BUILD SUCCESSFUL；**未做设备或 AGSL 运行时编译测试**。
- [ ] 需用户真机核对常规按钮高光、Goo 按下连接时的内侧反射、主菜单及二三级菜单九层边缘光。如果仍全为纯色，读取 Logcat `LiquidGlass27` 的 `AGSL compilation failed` 或 `AGSL rendering failed` 具体异常定位 Android RuntimeShader 错误，不能凭猜测调参数。
- **限制**：此处只恢复接近原版的光学输入和采样公式，尚未移植 Flutter 真正 GPU 常驻多级图集；不能声称与原版像素完全一致。

## 普通按钮高光专项：FlClash control rim（2026-10-31）
- 用户确认**菜单九层高光仍正常，只有普通按钮高光消失**。源码核查：`GlassStyle.regular` 的 `rim=.28`、`rimWidth=1.2`、`rimTail=.17`、`rimFocus=1`、`rimAdded=.38` 均已匹配 `FlClash/lib/ui/liquid_glass.dart`，不允许为追求视觉而随意调大这些参数。
- [x] 发现 Android 特有的回退遗漏：原来 `GlassPane` 在 AGSL 或背景图不可用时只画 **纯色 Tint**；菜单可额外走 `GlassMenuRim` 的九层原生高光，而普通按钮原版高光只在着色器里，故容易出现“只有按钮无高光”。
- [x] 新增 `GlassControlFallback.kt`：将 `FlClash/shaders/liquid_glass.frag` 的**控件专属** `facing/band/tail/shine` 与 `rimAdded` 算式用 Kotlin 重现，保持原版参数；若有真实背景快照，则读取同一场景像素并按原版对比度、折射、透明 Tint、预乘 Alpha 和高光贡献重建按钮材质；无背景数据时安全回退原有 Tint。没有将菜单的九层 rim 误用到按钮。
- [x] `GlassBackdropTextures.pixelSnapshot()` 为所有回退按钮缓存同一份软件像素；通过 `Dispatchers.Default` 按需准备，**仅在高级 AGSL 无法绘制时**执行，正常路径不承担读回开销；生成按钮位图按尺寸、样式、源背景和 Goo 形态缓存。
- [x] 修复 `GlassRenderer` 原来 `shaderDisabled = true` 后永久不再尝试着色的 bug：现在只对**失败的当前背景版本**禁用，下一版背景截图可以自动恢复正常 AGSL；按三种故障原因分别写 `LiquidGlass27` 日志（编译失败、uniform/绘制失败、背景尚未捕获）。
- [x] 不改变原版菜单高光算法，不修改 FlClash 或 `liquidglass_sketch`；两轮 `:app:assembleDebug` 均返回 BUILD SUCCESSFUL。
- [ ] 真机确认普通 Regular、Tinted、Lens 和 Goo 行的边缘光，以及失败时 `LiquidGlass27` 的精确原因；**原生兜底不是完整的 Flutter GPU 模糊图集**，Gradle 成功不能代替 AGSL 真机验收。

## 构建与交付
- [x] 2026-10-31 在 DevSpace 运行 `./gradlew :app:assembleDebug --offline --no-daemon --no-watch-fs --console=plain --max-workers=1 -Dorg.gradle.jvmargs=-Xmx3072m -Pkotlin.compiler.execution.strategy=in-process`。
- [x] Gradle 日志 `BUILD SUCCESSFUL`。
- [x] APK：`app/build/outputs/apk/debug/app-debug.apk`。
- [x] 未触碰原版 FlClash 仓库；未安装或启动设备测试。

## 设备验收建议（由用户执行）
1. 普通菜单展开/关闭，观察根玻璃层是否双重折射，背景亮度是否保持不变。
2. 一级→二级→三级、每级返回，以及直接点击外部返回源按钮，观察层级交接和轮廓。
3. 从按钮回收到最后一帧，观察单一 Goo 玻璃与真实按钮重新显示的交接是否连续，尤其刚打开就立即关闭。
4. 手指按住菜单上下滑动、停留 400ms 展开、长列表拖动滚动、返回途中再次点标题。
5. 深色/浅色、快速/标准/高画质、超大字体，检查 AGSL、边缘高光、滚动布局。
6. 若玻璃变成纯色，请查看 Logcat 标签 `LiquidGlass27` 中的 `AGSL compilation failed` 或 `AGSL rendering failed`。

## 未完成/后续精度工作
- `_CarriedPress` 源按钮交接与多层追踪已实现为 Android Compose 事件桥，但**尚未经真机行为验收**；与 Flutter 原版的 gesture arena 和精确时序未做逐帧对照。
- Android 的 Flutter `RSuperellipse` 路径生成已接入原版 fitted octant；Android 33 conic 回退仍有近似差距；64 方向 `uProfile` 的亚像素精度以及新增 AGSL `cornerInset` 的运行时编译尚未验证。GPU atlas 已有独立可选代码路径，默认未启用且未测帧率。
- 真机逐帧视觉基准、触摸/字体/窗口边界系统性验收。

## 二级菜单重复轮廓专项修复（2026-10-31）
- [x] 根据用户截图发现：二级玻璃卡片角部有外层细线与内部材质视觉边界不一致，特别是左上、左下、右下角；前期共享 Path 仍无法消除。
- [x] 将二级菜单 AGSL 正常路径改为**同一绘制通道同时输出玻璃填充、暗边线与九层高光**，不再叠加独立原生 Canvas 外圈。
- [x] 修复 `menuLineStrength` 的 `uOutlineRadius` 取值错误：二级菜单使用真实子卡片半径，而非固定外层绘制容器的圆角。
- [x] 原生九层高光保留为 **AGSL/背景纹理不可用时的回退绘制**；两条路径避免同时显示。
- [x] 子菜单打开时让后层一级菜单的独立 rim 随进度消失，减少后层曲线透出导致的“第二条边”。
- [x] 重新构建 APK：`BUILD SUCCESSFUL`。
- [ ] **真机视觉未验证**：需重点观察卡片三个角部、菜单展开/折叠中的边缘，以及 Logcat 的 `LiquidGlass27` AGSL 错误日志。

## liquidglass_sketch 后续集成准备（2026-10-31 历史计划；后来已改变方向）
- [x] 阅读目标项目 `AGENTS.md`、`docs/glass-backdrop-integration.md` 以及 `LayerBackdrop` / `drawBackdrop` 源码。
- [x] 识别目标项目的独立背景源与窗口坐标映射方案，确认其不等于 `liquidglass27` 当前的 85ms 位图快照接口。
- [x] 整理采样后端选择、未来移植顺序、目标项目写入边界、风险和验收规范。
- [x] 用户在 2026-11-01 已明确要求把 **Sketch 玻璃库接入 liquidglass27**（见“直接接入”专项）；2026-10-31 这里仅记录旧准备阶段。**反向将 liquidglass27 移植到 liquidglass_sketch** 仍未获本次授权，不在非采样收尾范围。本轮没有修改源项目。
- 详细准备文档：`docs/liquidglass_sketch_sampling_preparation.md`。

## FlClash 操作录像对照基准（用户提供 217737.mp4）
- 录像展示的是原版 **FlClash「代理」页面**，并非本 Android 移植版的实际运行视频；不要把录像当作本工程已经通过的视觉验收。
- 约 2.8–3.2 秒：主菜单「排序」行处于按压高亮状态；约 3.3 秒开始展开下级玻璃卡片，约 4.0 秒下级基本到位。
- 展开时，受按压的原行转变成下级菜单的**顶部标题行**，新玻璃主体由该行长出；一级菜单缩到后层并局部压暗，而整张页面不压暗。
- 约 7.5–8.5 秒演示「布局」下级的相同结构，回退时由子菜单向原行收拢；上层高光、主体、头部应沿同一形变路径移动。
- 与源代码的独立时序核对：
  - **源按钮**：`_PopupPressRecognizer`，非滚动父层按下即打开；滚动父层延迟 `kPressTimeout`，未到期便滚动则取消；短触在 Up 打开。
  - **菜单行**：`_hold` 与 `_dwellDuration = 400ms`，按住高亮约 400ms 后展开带子项的行；短触 Up 可直接打开该行。**这两个时钟不能混用**。
  - **子菜单动画**：`_unfoldSpring` duration 420ms / bounce .15，原版还在卡片几何计算里使用 `_glassSpan = 0..0.35` 与 `_items = 0.05..0.4` 的分段进度。
- [x] 源码审查：当前 Compose 的菜单行停留时间为 400ms，子菜单 Spring 与玻璃/内容分段比例已对应此结构。
- [ ] **还未验证**：从实际 `liquidglass27` 视频逐帧对照触摸高亮、下级开始时刻、边缘高光和回收路径；不能仅凭原版录像判定移植成功。
- [ ] 未来只在用户明确要求后才将此行为合入 `liquidglass_sketch`。

## 二级菜单标题返回问题专项修复（2026-10-31）
- [x] 修复「点击二级菜单标题无法返回上一级」：此前 `GlassSubmenu` 标题行的 `clickable` 被最后绘制的全屏 `GlassMenuGestureSurface` 覆盖，实际点击需要由共享手势层识别，易因点击消费状态/坐标偏差而丢失。
- [x] 在 `GlassMenuGestureSurface` 最上方为**当前最深子菜单标题**额外增加与可见标题对应的独立透明 `clickable` 命中区，点击统一调用 `onBack`，按菜单栈逐级返回。
- [x] 将 `GlassSubmenu` 的滚动容器从整体 Column 下移到**选项列表**，标题保持固定，不再产生滚动后的标题位置与全屏命中框偏差。
- [x] `:app:assembleDebug` 构建通过，APK 已更新。
- [ ] 设备行为待用户验证：一级→二级标题返回、三级→二级标题返回、滚动列表后返回、标题附近短按/长按，验证独立命中区与原手势并存。

## 快速打开后立即关闭：圆形/超椭圆双重底板专项修复（2026-10-31）
- [x] 用户截图出现「•••」圆形玻璃外套着一块超椭圆玻璃底板。
- [x] 找到结构性原因：此前将整个原按钮（包含圆形玻璃底色）的截图全程绘制在 Goo 菜单玻璃上，导致同一位置有两种材质轮廓重叠。
- [x] 旧版尝试过整张源图与 Goo 玻璃互补透明度交接（最后约 80ms）；**用户确认仍短暂闪烁，此方案已被最新的单玻璃主体实现替换，勿恢复 `photoBlend`。**
- [x] 当展开未完成就关闭，返回动画的初始超椭圆指数取当前开场进度对应的值（从 2 到 3.5），而不是固定 3.5。
- [x] 已执行 assembleDebug，Gradle BUILD SUCCESSFUL；未安装/测试设备。
- [ ] 用户验收：快速点开后立即点外部关闭、打开约 50/150/300ms 关闭、完整展开后关闭，以及连续重复操作，确认不再有「圆套方」、白边闪现或末帧跳变。

## FlClash 单玻璃主体与真实关闭时机（再次复查，2026-10-31）
- 用户反馈使用 `photoBlend` 把闪烁缩短到几十毫秒，但**没有彻底消除**。
- 源码查证：`FlClash/lib/ui/popup.dart`：
  - `_captureSource` 在首帧之后用 `LiquidGlass.withoutPanes` 获取**不含玻璃层**的内容图像，并在快照有效后隐藏实际源控件。
  - `_paintFrame` / `_paintReturn` 让 `LiquidGlassPane` + `gooPairOutline` 始终负责玻璃；只把内容图像画在 droplet 中，**不交叉淡入淡出两块完整玻璃**。
  - `CommonPopupRoute._updatePlatter` 在返回时切回 platter；`_handleStatusChanged` 冻结当前开场几何；`didPop` 调整 reverse duration，让快速关闭仍保留完整收回时间。
  - `CommonPopupBox._open` 仅当 `AnimationStatus.dismissed` 或 route.completed 后才 reveal 源按钮，而不是在外部固定延迟后展示。
- [x] 目前的固定「•••」示例采用源文字直接绘制的内容水滴，移除自带玻璃的整张快照与 `photoBlend`；菜单 Goo 为唯一玻璃。
- [x] 增加 `GlassMorphMenu.onReturnComplete`，在完整 500ms 返回动画结束、菜单取消挂载时恢复真实源按钮。
- [x] 打包成功；原版 FlClash 和 `liquidglass_sketch` 没有改动。
- [ ] 真机测试仍由用户完成：快速打开→直接关闭、半程返回、返回最后一帧背景透镜/高光的连续性。

## 性能专项：原版缓存机制与 Android 帧开销（2026-10-31）
- 用户反馈接入 64 方向 profile 后操作明显卡顿。原版 `FlClash/lib/ui/liquid_glass.dart` 主要通过 `_captureKey`/缓存 Picture、`_frostKey`、已模糊的背景图集、以及 Path 对象变化时才算 `liquidGlassProfile` 来复用昂贵结果；并不是不断 CPU 读取整张屏幕像素。
- [x] **静态场景停止定时全屏 GPU→CPU readback**：`GlassScene` 增加 `backgroundKey` 和 `captureIntervalMs <= 0` 单次捕获模式，主题/尺寸变化时重新捕获。演示背景 `AtmosphericBackground` 是纯静态，因此使用 `captureIntervalMs=0L, backgroundKey=dark`。动态背景保留原先正数间隔策略。
- [x] **减少轮廓 CPU 计算**：`GlassRadialProfile` 将之前的「每条 64 射线遍历全部轮廓段」改为近似 FlClash 的按边段角度范围扫描；路径取样限制在 256–512 点，并保留不满足单交点条件的 Goo SDF 回退。
- [x] **减少轮廓 GPU 计算**：64 方向轮廓保持均匀采样，但逐像素 Catmull–Rom 求值只读取**相邻四个样点**，以三次多项式及导数替代遍历全部 64 点；数学插值方法不变，减少 fragment 循环成本。
- [x] **固定源内容停止定时读取**：当前演示「•••」图标不动，成功采集透明内容后退出轮询；主题/可见性变化会重新执行捕获。动态源控件可继续使用可配置轮询策略，未来需要拆成通用封装。
- [x] `:app:assembleDebug` BUILD SUCCESSFUL，保留 Standard(24) / Ultra(64) 等现有画质档位，未为性能而降低模糊采样数。
- [ ] **待用户真机验证**：屏幕/AGSL Shader 实际帧率、profile 动态数组索引的设备兼容性、动态背景延迟及视觉。Gradle 只能检验 Kotlin 和资源打包，不能检验 Android RuntimeShader 的运行时编译。
- [x] **共享预模糊第一阶段**：`GlassScene` 持有原始背景及 12dp 预模糊版本，两种 `BitmapShader` 在场景层创建一次，由所有 `GlassPane` 共用。静态背景在 `Dispatchers.Default` 进行一次 1/4 分辨率高斯模糊；菜单直接采样预模糊图层，额外模糊按方差补算。清透玻璃仍使用原始背景，不叠加额外磨砂。
- [ ] 剩余大项：Flutter 的完整 `LiquidGlassHostLayer` GPU 多级图集、动态背景分组捕获和更细致的 Picture 缓存还未移植；本轮不修改 `liquidglass_sketch`。

## 共享预模糊纹理与 Shader 资源复用（2026-10-31）
- [x] 新建 `GlassBackdropBlur.kt`：静态背景截图后在非 UI 线程生成 1/4 分辨率的高斯模糊纹理；处理 ARGB 预乘 Alpha，避免透明背景模糊后出现边缘光晕。尺寸/主题变化会刷新，不为动态背景反复生成。
- [x] `GlassFrame.textures` 集中管理原始图、可选预模糊图及各自的 Android BitmapShader；消除每个 GlassPane 为同一张背景反复创建 BitmapShader 的开销。
- [x] AGSL 新增 `uBackdropFrosted` / `uBackdropFrostScale` / `uBackdropFrostSigma`：磨砂 sigma 不小于缓存 sigma 时直接采样模糊纹理；更大的 sigma 按 `sqrt(target² - cached²)` 计算剩余需要的采样；缓存尚不可用时完整回退已有实时采样。
- [x] 演示静态背景设置 `preblurSigmaDp=12f` 对齐菜单 Frosted 材质。其他动态页面默认关闭预模糊，避免 CPU 工作量反增。
- [x] `:app:assembleDebug` 返回 `BUILD SUCCESSFUL`；Gradle 编译不代表真机 RuntimeShader 验证。
- [ ] 真机验证：第二个 shader 纹理的运行时编译、模糊缓存完成时是否略有观感切换、高低画质、多个子菜单同时出现的 FPS。
- [ ] 后续真正的多级 GPU 背景图集及与动态渲染同步的捕获仍需专门实现。

## 多档共享模糊与动态按需捕获（2026-10-31）
- [x] 核对 FlClash 的 `liquid_glass_capture.dart`：原版按玻璃组件的 sigma 和遮挡层级划分 band，缓存相同内容的 band，并通过 GPU 在合成阶段生成图集；它不等同于全屏 CPU 快照。
- [x] 静态场景多档模糊：`GlassBackdropBlur.kt` 共享一次 1/4 尺寸原图，生成最多三档高斯模糊（目标 sigma 的 0.25/0.60/1.00 倍），在 `Dispatchers.Default` 后台准备，不阻塞页面动画主线程。
- [x] `GlassFrame.textures.frostBands` 统一管理三档图像与 BitmapShader；AGSL 增加 `uBackdropFrostLow`、`uBackdropFrostMid`，按当前模糊半径选择不超过它的最强档位，再计算高斯方差差对应的剩余模糊。普通清透玻璃仍采样原始背景。
- [x] 动态页面新增 `GlassScene.captureIntervalMs < 0` 事件模式：`backgroundKey` 应是背景内容的变化版本；`snapshotFlow().conflate()` 丢弃过期的刷新请求，并限制连续 GPU 截图的频率（例如 `captureIntervalMs=-66L` 每次间隔至少 66ms）。不取消进行中的 readback，失败保留上次成功纹理。
- [x] 兼容原有静态 `captureIntervalMs=0L` 和正数周期模式；当前演示背景静态，仍使用 `0L`。
- [x] 完整 `:app:assembleDebug` 构建成功，未安装/测试设备。
- [ ] **真实 GPU band 图集仍未实现**：缺少 Flutter 的 GPU 按组件分组采样、重叠层级融合、内容不变 band 的局部复用。本轮是 Android 兼容中间阶段，不宣称已达到 FlClash 的图集实现。
- [ ] 用户真机验收：AGSL 多 child shader 编译/画面、三档磨砂过渡是否跳变、CPU 缓存准备耗时和动态事件模式实际延迟。

## GPU 预模糊与动态背景同步（2026-10-31）
- [x] 新增 `GlassGpuFrostAtlas.kt`：使用 Compose `GraphicsLayer.record` 与 Android `BlurEffect`，以 1/4 分辨率 GPU 图层尝试生成模糊快照。静态场景可显式启用 `staticGpuFrost=true`，分三个显示帧生成 25%/60%/100% sigma 三档，失败时回退已有后台 CPU 高斯图集。该选项默认关闭，避免未验证的三次 GPU readback 影响原有静态性能。
- [x] `GlassScene` 新增可选 `dynamicGpuFrost`；事件驱动动态模式最多约 5Hz 更新单个 GPU 高斯 band，原始图和对应 band 从**同一次场景捕获**生成并原子发布，避免不同时间帧混用。
- [x] 动态 GPU 处理失败两次时，改为仅更新原图，让 AGSL 继续原有实时采样，不再无休止重试 GPU 读回。
- [x] 演示「玻璃参数」提供「静态切换 CPU/GPU 模糊」和「动态采样测试」两个开关；动态漂移背景内容以 10Hz 更新、GPU 图集最多 5Hz。默认静态 CPU 模糊，可自行对比静态 CPU、实验性静态 GPU 与动态 GPU 模式。
- [x] 修正 GPU atlas 文件的无效 `drawImage` 导入；修复 Gradle 增量 Dex 缓存缺失，通过 `:app:clean :app:assembleDebug` 全量重建，`BUILD SUCCESSFUL`，未安装或运行设备测试。
- [ ] **真机验收**：RenderEffect 在 `GraphicsLayer.toImageBitmap()` 中的实际模糊效果、动态背景时的光学滞后和 GPU 帧时长、静态三档 GPU 图集切换是否闪烁。
- [ ] **边界**：这是 GPU 预处理加有界 Bitmap 读回，尚非 FlClash 的纯 GPU 常驻多层图集；如需真正去除读回，必须进一步接入直接 GPU 纹理源，而不是继续增加捕获任务。

## 单纹理共享模糊图集接入（2026-10-31，后续阶段）
- [x] 新增 `GlassCombinedAtlas.kt`：把**同一静态背景版本**的全分辨率原图和最多三档 1/4 分辨率模糊图打包到同一 BitmapShader。合成只在 `Dispatchers.Default` 处理一次，限制图集尺寸，不在每次玻璃绘制时重复复制。原始背景仍可单独供回退读取。
- [x] `GlassScene` 为静态背景的缓存生成流程增加原子发布的 `combinedAtlas`；异步生成时校验背景版本，旧版本结果不覆盖新背景。缓存生成失败时继续使用原始纹理和原有 Vogel 采样。
- [x] `flclash_glass.agsl` 只用一个 `uniform shader uBackdrop`，通过三档 tile 区域及 sigma uniform 读取缓存；按目标磨砂 sigma 选择不大于它的最高一档，以 `sqrt(target²-cached²)` 算剩余模糊并继续用原版 Vogel 圆盘采样。原图区域的纹理采样单独钳制，避免越界读到缓存 tile。
- [x] `GlassRenderer` 以同一份纹理绑定玻璃、折射和高光路径，透过 uniform 切换 raw/atlas；`GlassShowcase` 的玻璃参数新增「启用/关闭静态模糊图集」实验开关，**默认关闭**，不影响既有展示画质和高光参数。
- [x] `:app:compileDebugKotlin` 成功，重试后的 `:app:assembleDebug` BUILD SUCCESSFUL；没有安装、操控或测试真机。
- [ ] 这是 CPU 打包 + 单 Shader 图集，**并非** FlClash 的 GPU 常驻 `LiquidGlassHostLayer` 分层采集与增量 band 复用；动态背景仅在明确开启实验开关时使用 GPU 单档预模糊与 CPU 打包，其余情况仍走原始 Vogel 路径。启用后的 AGSL 运行时兼容性、模糊过渡和视觉一致性尚未验收。

## 动态背景单档 GPU 模糊接入单纹理图集（2026-10-31，继续）
- [x] `GlassScene` 在事件驱动动态背景上，把同一捕获版本的 GPU 单档模糊和原始画面交给 `combineBackdropFrostAtlas`，在 `Dispatchers.Default` 打包，再将带 `combinedAtlas` 的同版本纹理一次发布。未将前后两帧的 raw/blur 混合。
- [x] 发生 GPU 模糊/纹理打包失败时回退到本次原图与 AGSL Vogel 采样；保持已有两次失败后停用动态 GPU 模糊的保护，并保留 `backgroundKey` 事件合并及最短 200ms 捕获间隔。
- [x] `GlassShowcase` 复用「共享模糊图集」开关：静态背景用三档 16dp 图集；动态背景用单档 12dp，匹配普通菜单 12dp 模糊。默认关闭，不改变原版融合/高光数值和默认展示效果。
- [x] 首次 Gradle 调用超时后，单独运行 `:app:compileDebugKotlin`（BUILD SUCCESSFUL，55s）及增量 `:app:assembleDebug`（BUILD SUCCESSFUL，21s）；本轮 APK 已构建完成。没有执行任何 ADB、安装或真机测试。
- [ ] 后续仍需真正的 GPU 常驻多级 band 与按组件重叠关系的增量复用；实验路径需要单独设备验证（本轮未测试）。

## FlClash 分层采样规划器第一阶段（2026-10-31，继续）
- [x] 新增 `GlassCapturePlanner.kt`：按照原版 `liquid_glass_capture.dart` 的 `_gather/_place/_snapshot` 结构建立画面层级规划数据，输入为从后到前的 Pane 列表及场景边界。计算每个 Pane 的折射/模糊扩展采样区域、与此前玻璃重叠时的层级、裁剪后的捕获槽位，以及按层级+模糊 sigma 分组的 band。无遮蔽关系的 Pane 可以共享层级；同组 Pane 不互相提升层级；无模糊 Pane 独立成组；haze 单独列为模糊 band。
- [x] band 对 32px 图集尺寸取整；使用包含背景版本、层级、模糊参数和槽位的稳定键，标记**是否具备复用条件**，并在卸载 Pane 时移除其登记，避免把旧布局当成当前图集。尚无法可靠识别底层玻璃的独立内容变化，因此仅允许 level 0 计划标记 unchanged，上层一律失效；此处只做复用判定，尚未复用 GPU 像素或执行 Picture 重播。
- [x] `GlassScene` 增加可选 `capturePlannerEnabled`（默认 false）和场景坐标记录，实际捕获时可将规划快照随背景版本存入 `GlassBackdropTextures.capturePlan`；`GlassPane` 增加可选 `captureGroup` 及位置、模糊、折射信息登记。**未修改既有 AGSL 着色、融合形变、高光公式**。
- [x] `:app:compileDebugKotlin :app:assembleDebug --offline` 完整构建成功；未执行 ADB、设备运行或真机效果测试，未修改原版 FlClash 或 `liquidglass_sketch`。
- [x] **下一阶段前半部分完成**：为规划器增加 Compose 真实绘制层、离屏 root、变换后矩形、绘制版本及每层图集位置映射；见下方专项记录。实际 GPU 分层采样及可重用的 band 图像仍未完成，不把规划键相等误报成 GPU 已复用。

## Compose GPU 绘制层关联与动态图集排布（继续）
- [x] 新增 `GlassCaptureRoot.kt`：在 `capturePlannerEnabled=true` 时把 `GlassScene` 的可见玻璃内容置于真实 Compose `GraphicsLayer` 离屏绘制根，记录祖先 root 链、变换后真实绘制范围和 draw revision。其他模式与此前一致；默认关闭。可选的嵌套 `GlassCaptureRoot` 会形成额外 root 层级，供未来处理离屏祖先/子层。
- [x] `GlassRenderer.kt` 的每个 `GlassPane` 在开启规划器时使用 `rememberGraphicsLayer()` 记录自己的真实 GPU 绘制内容，并原位 `drawLayer`；注册表保留实际 layer 句柄，按绘制变更递增版本，退出组合时清理。**没有增加像素读回，也没有额外叠加一层玻璃**。旧的光学参数及正常关闭规划器时的渲染不变。
- [x] `GlassCapturePlanner.kt` 纳入最深离屏 root 的可见性约束、Pane/root 内容版本、变换后的 `boundsInRoot()` 与缩放光学影响。band 缓存键包含所依赖的底层绘制内容版本，避免把发生变化的底层玻璃误判为可复用。当前 `unchanged` 仍只是规划级候选资格，不代表实际缓存命中。
- [x] 参照 FlClash `_snapshot` 将同层同 sigma 的 Pane 装入垂直 band，按 `_atlasStep=32` 对齐尺寸；生成每个采样槽的全局场景矩形、目标图集矩形以及各层整体 atlas 的宽高。同时为规划结果关联 `GlassScene` 原有的**纯背景 GraphicsLayer** 和背景版本。
- [x] `GlassScene.kt` 通过合并后的绘制事件持续刷新 `frame.liveCapturePlan`，不再仅在后台截屏时生成静态布局快照；更新元数据无需额外 GPU readback。`capturePlannerEnabled` 继续默认 `false`，演示和原 FlClash 玻璃高光/融合公式不变。
- [x] `:app:compileDebugKotlin :app:assembleDebug --offline` BUILD SUCCESSFUL；本轮无 ADB、手机安装和运行测试，未修改原版 FlClash 或 `liquidglass_sketch`。
- [x] **已在下一阶段完成代码主路径**：按层重播前景、GPU hardware 图集、增量 band 复用、非玻璃内容显式捕获以及 AGSL 后景输入已经接通，参见下节；旧段仅保留阶段历史。Android 设备运行时仍未验收。

## GPU 常驻多级图集完整代码路径（2026-11-01）
- [x] 新增 `GlassGpuResidentAtlas.kt` 的真实 GPU atlas 后端：`GraphicsContext.createGraphicsLayer` 录制每层原图与模糊 tile；Android 28+ 的 `GraphicsLayer.toImageBitmap()` 用硬件加速生成 `Bitmap.Config.HARDWARE` 图像，结果校验 GPU 配置，按层保留 `BitmapShader` 供 AGSL 采样，不调用 `getPixels` 或 CPU 高斯卷积。每帧没有全屏 CPU 打包；启用模式仅按背景版本生成一次原始回退快照。
- [x] 实现**真正的 band 和整层 atlas 复用**：按稳定 `GlassCaptureBandKey` 命中保留硬件 band，未变 band 直接 GPU 复制拼回新 atlas；整层 keys、尺寸、图片引用都未变化时复用已有硬件 atlas 与 Shader。提供 `GlassGpuAtlasStats` 的 kept/new 计数，撤销的 pane/level 自动 prune，禁用模式清空缓存。
- [x] 根据 FlClash `_gather/_place/_snapshot` 的顺序记录玻璃 Pane、显式非玻璃 UI 片段与离屏祖先 root，在各 pane 的原图采样槽里仅重播**之前的内容**，剔除同组玻璃/之后绘制的组件；先完成低层硬件 atlas 并发布到画面，之后再合成高层。采样源包括真实绘制层而非猜测图层；采用 32px 图集尺寸步长、每 tile 单独 GPU `BlurEffect(TileMode.Clamp)` 以避免相邻 pane 的模糊跨带污染。
- [x] 处理 `boundsInRoot`、`localToRoot` 生成 2D 仿射变换，适配父层的旋转/非等比缩放/平移；在 GPU 后景重播使用相同仿射矩阵。额外支持源内容 alpha、裁剪、隐藏的弹出菜单按钮；`GlassCaptureBehind` 捕获原始 Modifier 绘制链（含 clip/alpha），而不是裸子内容。非玻璃前景通过显式 `GlassCaptureBehind` 包装接入当前演示按钮、Goo 图标、底栏、菜单及子菜单。
- [x] `GlassCapturePlanner` 以背景版本+背景尺寸、采样槽、root 路径、下层画面修订号（包含递归传播的下层依赖）构成缓存键；背景只有实际绘制完成才发布新版本。保留动画中的稳定槽位，对 GPU 预模糊 sigma 使用 8px 档位并交由 AGSL 补足剩余模糊方差，减少重复缓存。自身玻璃外形动画不会无故清除自己的原图缓存，但变化会正确失效上层折射。
- [x] `GlassScene` 新增可选 `gpuResidentAtlas`，与后台缓存/静态回退解耦，按层发布已完成的 GPU 图集，并在下一次画面绘制后生成上层内容；已构建的上一代且仍有效的层可继续显示，避免整层断档。`GlassRenderer` 从对应 pane 的 GPU 原图/磨砂 tile 写入同一个 AGSL 子 Shader；新增 `uSceneBasis`、原图槽位和三档模糊坐标 uniform，保留旧版 Vogel 及默认材质/高光公式，单个 GPU 图集失败时回退已有原始材质来源。
- [x] `GlassShowcase` 在「玻璃参数 → 更多显示选项」新增「启用/关闭 GPU 常驻图集」实验开关，默认关闭，不影响用户现有渲染和操作流程。保留 `liquidglass_sketch` 仅作为未来手动集成的参考，不自动合并。
- [x] `GlassCapturePlannerTest.kt` 覆盖交叠层级/同组隔离、硬件槽位 32px 布局、纯原图与 sigma band、动画保持槽位、背景或场景尺寸改变的失效、隐藏源、显式内容与跨层递归失效、旋转基变换。`:app:compileDebugKotlin :app:testDebugUnitTest :app:assembleDebug --offline` 完整 BUILD SUCCESSFUL，APK 可构建。
- [ ] **非代码交付项，未执行**：GPU 硬件实际运行、AGSL 在目标手机上的编译、高光和动画逐帧视觉、显存占用与帧率均未真机验证；用户此前要求不做 ADB 测试。本节的“代码完成”不表示已经证实与 Flutter 原版像素完全一致。对其他新界面的任意非玻璃内容，需要显式使用 `GlassCaptureBehind` 标记其绘制层。

## Flutter RSuperellipse 几何与边缘曲线对齐（2026-11-01，继续）
- [x] 对照 FlClash 调用的 Flutter `RSuperellipse` 与 Flutter Engine `round_superellipse_param.cc` / Web UI `rsuperellipse_param.dart`，新增 `FlutterRSuperellipse.kt`：按照原版比率查表/外插求每个 octant 的指数 n、J 接点、圆弧半径及中心；每 octant 使用两个加权二次曲线 + 一段圆弧三次贝塞尔，拼成统一半径的四象限 Path。普通椭圆胶囊/菜单由原版拟合取代旧 12 折线指数轮廓，过渡中的非标准自定义指数仍保留兼容回退。
- [x] Android API 34+ 调用 `Path.conicTo` 以保留加权二次曲线；最低支持 API 33。**11 月 1 日后续精度更新：已将固定四段 Hermite cubic 改为 `ConicCubicFitter.kt` 的自适应有理二次曲线拟合**，以 0.025 物理像素为采样误差目标，取 1/4、1/2、3/4 采样检测并递归二分，最大深度 7，精确保留端点和切线方向；曲线构建继续沿用 50 条 LRU 缓存，不触碰 API 34+ 原生 conic。
- [x] 与 Flutter Web UI 一样使用基于宽高/半径 0.01px 量化的最大 50 条规范化 Path LRU 缓存；每次使用复制再平移，避免布局变化时重新计算常见胶囊圆角，同时不会共享可被外部修改的 Path 实例。
- [x] `FlutterRSuperellipse` 输出 FlClash `_cornerCurve` 对应的 along/across/octant center 参数；`flclash_glass.agsl` 从原版移植 `octantInset/cornerInset`，仅用于普通控件窄边缘的 rim/hairline/shine 采样，不改变折射深度、Vogel、材质参数或 Goo 按压物理值。`GlassRenderer` 按 size、radius、density 缓存并上传 `uCornerAlong/uCornerAcross/uCornerCenters`。
- [x] 新增 `FlutterRSuperellipseFitTest` 覆盖引擎拟合端点、比率插值/外插、单圆角圆弧、矩形长短边分段和非法尺寸保护。`./gradlew :app:compileDebugKotlin :app:testDebugUnitTest :app:assembleDebug --offline --console=plain --max-workers=1` BUILD SUCCESSFUL；未进行任何 ADB 或设备安装、运行测试，不触碰 FlClash 原始仓库及 `liquidglass_sketch`。
- [ ] 未完成设备视觉验收：Android 33 自适应 cubic 与原生 conic 在真实 GPU 栅格化下的差距、Goo 颈部 path 联集、普通控件窄边缘闪光及新增 AGSL uniform 运行时兼容仍需实际设备检查。当前不能声称与 Flutter/FlClash 逐像素一致。

## 直接接入 liquidglass_sketch 的真实液态玻璃材质（2026-11-01）
- [x] 用户已明确要求将 **Sketch 的液态玻璃接入当前实验工程 `liquidglass27`**。这次是 `liquidglass_sketch`（来源）→ `liquidglass27`（宿主），不代表先前设想的 `liquidglass27` → `liquidglass_sketch` 正式移植。两个方向分别记录，不混淆。
- [x] 通过 Gradle 同级项目模块 `:sketch-glass` 直接引用 `../liquidglass_sketch/liquidglass` 的实际玻璃库源码；app 使用 `implementation(project(":sketch-glass"))`，补上 JitPack，`compileSdk=37`。这是**源码级跨工程模块依赖**而非复制 AAR，Sketch 原项目源码、抽取基线及 `liquid-glass-merged-files.json` 无任何更改。**当前要求两个项目维持既有同级目录结构**，不是可独立搬运的 vendored 模块。
- [x] 新增 `SketchGlassBridge.kt`，使用 Sketch 自带 `LayerBackdrop`、`drawBackdrop`、`vibrancy/colorControls`、`blur`、`lens` **作为材质与采样后端**，不使用 Sketch `Highlight`。`GlassPane` 在 Sketch 后端不会执行第二遍 FlClash 折射；保留 FlClash Goo union、超椭圆 native Path、弹簧、源内容交接和层级手势。Sketch 光学参数明确采用其 **按钮 2/12/24dp** 和 **Dialog 8-16/24/48dp** 预设，不能谎称这些参数与 FlClash 或 Figma 数值相同。
- [x] `GlassScene.sketchMaterial` 负责只在 **非玻璃背景兄弟层** 上使用 `Modifier.layerBackdrop(rememberLayerBackdrop())` 进行实时 GPU 层录制。Sketch 玻璃在其他兄弟层按框架原生坐标采样；跳过原有定时 `GraphicsLayer.toImageBitmap`、旧 GPU 图集预处理及二次 AGSL 着色，避免读回开销和自采样递归。
- [x] Sketch 材质内 `GlassPane` 原生 Path 转换为 Compose `Shape`，当已存在动态 Goo、长条 Goo 或卡片 outline 时读取同一路径，不再额外绘制旧的主体玻璃。**高光不允许由 Sketch 接管**：根菜单保留 FlClash `GlassMenuRim`，子菜单强制保留它的同一原版独立 rim fallback，Alert 的菜单 rim 单独补一次；普通控件的 FlClash facing/band/tail/rim 由 `FlClashControlHighlight` 后处理层对 Sketch 材质执行，绝不重复采样或折射。
- [x] `GlassShowcase` 默认启用 Sketch 材质，并在「玻璃参数 → 更多显示选项」提供 Sketch/FlClash 互切选项；启用原实验 GPU 常驻图集时自动切回 FlClash。FlClash 传统后台保留，以便对照或回滚。
- [x] 已运行 `:app:compileDebugKotlin :app:testDebugUnitTest :app:assembleDebug --offline`，**BUILD SUCCESSFUL**；APK 路径 `app/build/outputs/apk/debug/app-debug.apk`。未进行 ADB、安装或真机运行；**编译并不能证明材质视觉/性能正确**。
- [ ] 后续如要把**页面文字、滚动列表、独立内容组件**也包含在 Sketch 的 Popup 折射底层，必须把纯非玻璃页面内容另设 `LayerBackdrop` 作为 Popup 的独立源；当前 **普通控件** 采样 GlassScene 定义的 background sibling；多级菜单额外合成**之前各级菜单自身的录制层**，但其他未标记的页面前景仍不属于其采样源。不能把玻璃及其祖先整体录进源层来假装解决此问题。
- [ ] 目标设备真实折射、高光、动态背景、菜单根与子菜单的裁剪、拖动时坐标、切换两个后端及其 CPU/GPU 开销仍由用户安排真机验收。

## Sketch 接入后启动闪退修复（2026-11-01）
- [x] 定位一个明确的 RuntimeException 抛出路径：Sketch 的 `backdrop/effects/Lens.kt` 在 `lens()` 执行时，若 `shape` 既不是 `RoundedRectangularShape` 也不是 `CornerBasedShape`，直接抛出 `UnsupportedOperationException("Only RoundedRectangularShape or CornerBasedShape is supported in lens effects.")`。此前 `SketchGlassBridge.kt` 将 FlClash Goo Path 封装为普通 `Shape`，每个玻璃控件首次计算效果都有触发此异常的条件。
- [x] 将 `SketchGlassShape` 改为 `CornerBasedShape` 子类；四角 `CornerSize` 供 Sketch 的 lens/refraction 读取、copy 保持四个角参数；这解决了 Lens.kt 的受限 Shape 类型异常。**2026-11-01 截图反馈后进一步调整：内部 `createOutline` 仅输出稳定矩形，不再在 Sketch 布局层缓存动态 Goo 轮廓；实际 Goo/RSuperellipse 裁剪改为外侧 drawWithCache（详见下方新专项）。** 不修改 Sketch 原始库源码。
- [x] 新增 `SketchGlassBridgeTest` 覆盖 Shape 类型满足 Sketch lens 协议、四角 copy 不丢失；`./gradlew :app:compileDebugKotlin :app:testDebugUnitTest :app:assembleDebug --offline --console=plain --max-workers=1` **BUILD SUCCESSFUL**。目标 APK 在 `app/build/outputs/apk/debug/app-debug.apk`。
- [ ] 尚无用户设备 `FATAL EXCEPTION` / RenderThread 崩溃堆栈，不能证明这个异常是唯一的闪退原因，也不能宣称设备端已修复；依旧未执行 ADB、安装及真机测试。如替换新 APK 仍闪退，以 Logcat 崩溃栈定位其余问题。

## Sketch 光学与 FlClash 高光严格分离（2026-11-01）
- [x] 按用户纠正，取消先前误接的 `Highlight.Default`；Sketch **仅提供采样/模糊/折射/材质**。不允许把 Sketch 高光替换到 FlClash 的普通按钮、Goo 或菜单上。
- [x] 恢复 `GlassMenu.kt` 根菜单原有的 `GlassMenuRim` 单独绘制；子菜单走 `onFallbackChanged(true)` 保留 FlClash rim；Alert 没有外部 rim，已在桥接层补且只绘制一次。保留菜单九层光带及原版暗边参数，未用 Sketch 描边代替。
- [x] 新增 `flclash_control_highlight.agsl` + `FlClashControlHighlight.kt`，由 Android RenderEffect 输入已绘制好的 Sketch 材质，按原版 facing / band / tail / focus / rimAdded 和 64 向路径 profile/Goo 法线计算控件光照，还原 FlClash 按压光斑。只对玻璃材质做轻量高光后处理，不再执行 FlClash 第二遍模糊与折射；Shader 初始化/设置异常有日志并回退到无后处理材质。
- [x] `SketchGlassBridge.kt` 明确 `highlight = null`；没有 Sketch `Highlight` 的引用，Root/子菜单原版 native rim 恢复。`:app:compileDebugKotlin :app:testDebugUnitTest :app:assembleDebug --offline` **BUILD SUCCESSFUL**。
- [ ] 由于 Sketch 光学层本身已与 FlClash 背景模型不同，原 FlClash 暗边的「原始背景采样」改为基于已合成的 Sketch 材质颜色计算；光照方向、衰减和强度参数仍按 FlClash。该组合与原版在有纹理的背景上可能有亚像素/颜色差异，需真机对照。新增 Postprocess AGSL 的运行时编译也仍需设备日志验证，本轮未安装或测试。

## Sketch 模式 Goo 融合与多级玻璃恢复（2026-11-01）
- [x] 查明二/三级菜单在 Sketch 模式失去叠层折射的代码原因：`GlassScene` 只输出纯背景 `LayerBackdrop`；`GlassMenu` 的各级 `GlassSubmenu` 继续读取同一个背景，前一级菜单的玻璃及菜单前景未进入后一级采样。当前已将根菜单、一级子菜单与最多六级嵌套菜单各自录制为 **只读、互相独立的 `LayerBackdrop`**。新的输入链遵循 **背景 → 已完成的上一级 → 当前菜单** 的严格单向依赖，不包含当前层或以后层；每级 `Modifier.layerBackdrop` 只包该级菜单的完整内容，避免父 Popup 自采样递归。使用 Sketch 原生 `rememberCombinedBackdrop` 直接按全局坐标合成，不引入 CPU Bitmap readback。
- [x] 修改 `LocalSketchGlassBackdrop` 与 `GlassPane.sketchBackdropOverride` 为通用 Sketch `Backdrop` 接口；`GlassSubmenu` 提供按层输入与单独输出。`SketchLayerOrder.kt` 抽出前置源选择纯函数，新增四项 `SketchLayerOrderTest` 覆盖第 2、3、多级菜单及错误深度，防止将当前 level 误作为自身输入。
- [x] 发现 Goo 丢失光学融合的代码原因：此前 `SketchGlassBridge` 只是用 Goo union `Path` 作 clip，Sketch 的原生 `lens()` 仍使用四角圆角矩形距离场，没有 Goo 颈部法线及折射。新增 `sketch_goo_lens.agsl` 和 `SketchGooRefraction.kt`，在 **Sketch 的实时背景和原生 color/blur 效果之后、shape clip 之前**，按照 FlClash 距离场/平滑最小值、融合表面法线及折射深度执行**单次 Goo 专用折射**。普通非 Goo 控件/子菜单仍使用 Sketch 自带 `lens()`，确保不存在双层镜头。Goo 源码 Path 和 FlClash rim/highlight 均没有修改或替换为 Sketch 高光。
- [x] `:app:compileDebugKotlin :app:testDebugUnitTest :app:assembleDebug --offline --console=plain --max-workers=1` **BUILD SUCCESSFUL**，已打包新 APK `app/build/outputs/apk/debug/app-debug.apk`。此验证只证明 Kotlin/资源/单测/打包，**不证明设备端 AGSL Shader 正常编译或真实动画/折射像素一致**。
- [ ] 仍需目标设备核对：Goo 颈部实时折射是否出现、一级菜单下的二级菜单是否真正折射父玻璃、二级向三级的单向合成、连续关闭/快速打开是否闪烁、RenderThread 与显存负载。用户此前不要求 ADB，因此本次未安装或读取 Logcat；如果仍无效果优先检查 `LiquidGlass27` 中的 Goo preflight / RuntimeShader setup 日志。

## 截图反馈：Goo 右侧重影与菜单玻璃错位 / 纯白（2026-11-01）
- [x] 用户提供两张真机截图：Goo 行第三按钮右侧出现灰色拖尾形状；菜单背景出现上下不等大的白色玻璃块、文字与材质范围错位，二级菜单看起来不透明。截图是问题实证，不应将此前的 Gradle 构建成功当成视觉通过。
- [x] **定位到动态裁剪生命周期不匹配**：Sketch `DrawBackdropModifier` 内部在 `LayoutModifierNode.measure` 的 `placeWithLayer(clip=true,shape=...)` 设置裁剪。Goo 和菜单的路径可逐帧改变但 Canvas 大小不变，因此该布局级 clip 可能停留在旧路径，造成玻璃表面缺一块、灰色旧轮廓残影或内容与玻璃不对齐。修复采用双层分工：`SketchGlassShape.createOutline` 只返回稳定的矩形布局裁剪、继续继承 `CornerBasedShape` 以满足 lens；**外侧 `Modifier.drawWithCache` 在绘制时用 FlClash 的实时 Goo/RSuperellipse Path 裁剪整张 Sketch 材质，且 `rememberUpdatedState(path)` 驱动 path 改变时的 cache invalidation**。同时裁掉 RenderEffect 后处理偶然越界的阴影，不再让 Sketch 的布局 clip 缓存动态轮廓。
- [x] **修正子菜单折射角半径**：带 `GlassOutlineRect` 的二级菜单原先 `cornerDp=1000f` 默认值被传给 Sketch lens，导致它把真实 24dp 级角当作全圆角胶囊。现在通过 `sketchOpticalCornerPx` 优先选择 `outline.radius`，其次 `morph.radius`，最后 fallback 的 cornerDp，供 Sketch lens 与 FlClash rim 一致使用。
- [x] **修正材质过度漂白**：此前 `SketchGlassBridge.onDrawSurface` 对玻璃绘制一次 `Hue` + 再绘制一次 0.75 倍 alpha 的同色层；父菜单和二级菜单叠加后变成接近不透明的白色。现仅按原 `GlassStyle.tint` 的 alpha 画一次 SrcOver 色层，不更改 FlClash 高光方向、衰减、参数或菜单九层 band，也不启用 Sketch Highlight。
- [x] `SketchGlassBridgeTest` 增加对固定内部矩形与真实二级菜单角半径的回归测试。已运行离线 Gradle 的 Kotlin 编译、单测、APK 打包，**BUILD SUCCESSFUL**。目标 APK：`app/build/outputs/apk/debug/app-debug.apk`。
- [ ] 用户重新验收：静止 Goo 4 按钮仍应按 FlClash 原设计分开；**按压 / 拖动贴近**应形成连通颈部，松手后不能有右侧灰色重影。二级菜单需检查完整玻璃轮廓、内容对齐与背后动态背景可见。未进行设备端重装/Logcat/录屏，实际渲染效果仍待验证。

## Goo 按压融合颈部尖角和直线接缝（2026-11-01 12:14 用户截图）
- [x] 用户新截图：第二、第三按钮在接近/按压时出现左右竖直截断以及上下交叉的白色三角形，中央不像一整块流体。该图证明此前仅恢复形状裁剪仍有内部交叉缝。
- [x] 逐行核对 `FlClash/lib/ui/goo.dart:gooOutline`：FlClash 先将各 RSuperellipse pill 放入基础 Path，**每生成一段 `_neck` 就调用 `Path.combine(PathOperation.union, outline, neck)`** 做精确布尔并集；但 Android `GooRowOutline.kt` 10 月 31 日优化成 `Path.FillType.WINDING + addPath(neck)`，与布尔 UNION **不等价**。颈部 Polygon 与按钮子路径在重合区域的方向/交叉可能产生内部空洞与白色尖角。
- [x] 已撤销该错误优化：`gooRowOutline()` 现在按原 FlClash 每段 `_neck` 使用 Android `Path.op(outline, neck, Path.Op.UNION)`；对失败的 native Path.op 保留上一有效路径，不再以 addPath 回退引入同类空洞。Goo 的 0.5dp 网格、16 次二分、8+8 原版 blend、按压 spring、Sketch 背景/折射和 **FlClash 控件高光**均不变。
- [x] 重新运行 Kotlin 编译、单测、Debug APK 打包，`BUILD SUCCESSFUL`，输出 `app/build/outputs/apk/debug/app-debug.apk`；此处是源码级精确匹配 FlClash 的布尔操作，尚无新 APK 真机运行图。
- [ ] 需设备核对 2/3 按钮按压合并的颈部是否不再出现内部横竖直线、白色 X 或透底小洞，以及其他按钮融合/回弹的 GPU 负载。

## 非采样计划继续推进：Android 33 曲线与多级菜单手势（2026-11-01）
- [x] 本轮明确排除玻璃采样、模糊图集、Sketch optical 后端及高光参数修改；非采样剩余事项以 **几何计算、弹窗交互稳定性** 为实施重点，真机 AGSL / 像素验证单列未完成。
- [x] 完善 Flutter RSuperellipse 在 **Android 33** 的加权圆锥曲线回退：新增纯 Kotlin `ConicCubicFitter.kt`，对每段 rational quadratic 的 0.25/0.50/0.75 分点检测拟合偏差；自适应二分，目标 0.025px、最多 7 层；每个 cubic 的端点与 Hermite 切线严格取自原曲线，不再无论尺度与 weight 固定切四段。`FlutterRSuperellipse.kt` 的 API34+ `Path.conicTo` 不变。保留 LRU，代码改动不影响 Goo 动画强度或玻璃着色。
- [x] 新增 `ConicCubicFitterTest.kt` 测试 weight=1 精确二次、不同尺度/权重的多点误差、连续切线与不合法 weight 防护；它是在纯 JVM 层验证**离散拟合误差**，不是 Android GPU 的最终像素误差保证。
- [x] 改进 `GlassMenuGestures.kt` 的滚动触发：此前把所有帧 `abs(dy)` 相加为 `travel`，手指在同一位置反复轻微抖动也可能被累计成滚动，取消 400ms 停留展开。现在触发 slop 由 `abs(currentY - downY)` 实际净位移决定，不动 400ms 原版停留、跨层选择与触觉反馈，避免抖动误抢滚动。使 `hitMenu()` 内部命中函数可从测试访问（行为不变）。
- [x] 新增 `GlassMenuGestureHitTest.kt` 覆盖二级标题优先、父卡返回与区域外、滚动后的行索引映射、拖动抖动不误触滚动；以上不改采样层或视觉高光。
- [x] 离线 `:app:compileDebugKotlin :app:testDebugUnitTest :app:assembleDebug` **BUILD SUCCESSFUL**，新 APK 在 `app/build/outputs/apk/debug/app-debug.apk`。未进行 ADB、安装、发送 QQ 或真机测试。
- [ ] 后续非采样验收：真机 33 的圆角动画无接缝、100ms 源按钮识别与滚动冲突、400ms 按住跨级展开、快速开闭/回收关键帧、长列表标题返回。均不能仅由 JVM 单元测试认定完成。

## 非采样计划继续推进：快速关闭接续与固定标题命中（2026-11-01）
- [x] 本轮严格跳过 **Sketch/FlClash 背景采样、图集、AGSL 材质与玻璃高光**，仅调整 `GlassMenu.kt` 的动画状态接续以及 `GlassMenuGestures.kt` 的菜单事件命中。保留既有开场 450ms、回收 500ms、原 Spring 与按住 400ms 停留展开。
- [x] 修复半程快速关闭瞬间 alpha、形态材质及 Goo 连接跳变：之前 `menuReturningFrame(time=0)` 把 `settled` 直接设为 1、源图标设为 0.2、内容 alpha 误设为 `from.settled`、Goo 融合强度设为零，导致开场约 50/150/300ms 立即关闭时首帧发生明显不连续。现在在**关闭首帧**严格继承 `menuOpeningFrame` 的 `platter` / `droplet` / radius / blend / settled / glyphAlpha / contentAlpha，接着通过 75ms 的 smoothstep 从旧开场覆盖率接入原有返回时间曲线，不替换原版弹簧或折射。
- [x] `GlassMenuHitLevel` 增加行列表的可见 viewport 上下边界；根菜单列表固定采用卡片 9dp 内边距区，二/三级菜单采用 pinned header 下方至卡片底部 8dp 内边距。`hitMenu` 只有在实际列表可见区域内才会映射 Row，避免滚动后已经滑入固定标题下、底部留白中的虚拟行仍可误点；子菜单 header / 父卡返回优先级不变。
- [x] 新增 `GlassMenuReturnContinuityTest` 验证开场 0/50/150/300/450ms 的闭合首帧轮廓/融合/透明度逐一连续以及完整 500ms 的数值有效性；扩充 `GlassMenuGestureHitTest` 覆盖长列表滚动后的 pinned header 与卡片边缘点击。已执行 `:app:compileDebugKotlin :app:testDebugUnitTest :app:assembleDebug --offline` **BUILD SUCCESSFUL**。
- [ ] 仍需真机验证快速点开即关闭、连点/取消反向动画、二三级嵌套标题返回以及滚动后可见行实际触控；此源码验收不能证明设备渲染帧不闪或真实触摸事件没有竞争。

## 非采样全部收尾：反向动画、边界布局、Alert 与源按钮事件（2026-11-01）
- [x] 菜单**关闭途中反向打开**：原 `LaunchedEffect(expanded)` 总是将 `clock` 重置为 0，使正在收拢的玻璃瞬间跳回源按钮。现记录 `lastRenderedFrame`，关闭时冻结 `closingStartFrame`，反向时从最后一张**已绘制帧**接续，并在前 120ms 平滑衔接正常开场。若正在关闭的是二三级菜单，保留它的实际顶层内容淡出，并让根内容淡入；如中途再次关闭仍使用当前帧，而非原始时间估算。融合/圆角指数读取当前 `frame.settled`，不再在快关后重进时突然切换指数。
- [x] 弹窗 Alert **不再固定 `delay(420ms)` 拆卸**：根据实际弹簧当前进度决定是否保持渲染，解决低帧率/快速重开时提前销毁和内容消失；原弹簧参数、背景蒙层和玻璃视觉实现均未改动。
- [x] **安全窗口位置**：新增 `clampMenuPlacement` 供根菜单/子菜单共用；分屏或窗口窄于原版 16dp 边距时自动缩小边距，不再对 Kotlin `coerceIn` 传入逆序的 min/max；正常窗口保持原位置规则。
- [x] **源内容元数据门禁**：增加 `SourceContentPolicy.kt`，冻结前检查透明前景位图年龄 ≤350ms、主题标识、宽高误差 ≤2px、尺寸正数与时间戳合法性；不将背景玻璃纳入截图，也未实现无视频源 callback 的伪逐帧同步。
- [x] **稳定手势桥及非采样回归**：扩展快速开关/二次反向、二级菜单内容渐变、极窄屏边界、Alert 生命周期、源指针 Down/Move/Up/Cancel 顺序与重复挂载时旧事件清理、源透明位图时效性等纯 JVM 测试。最终 **59 项测试全部通过、APK 成功、lintDebug 通过**，lint 仍有 15 warning + 1 hint（不以升级 targetSdk 或重写采样/高光换取无关警告消除）。
- [x] 生成 `docs/non_sampling_closeout_2026-11-01.md`，记录源码完成范围、测试命令、APK 输出、明确未完成的设备实测与本轮刻意排除的玻璃采样。
- [ ] **仅设备才能完成**：快速关闭与反向的真实帧时序、二/三级标题交接与滚动手势、Goo 白色 X 形接缝消失、Android33 像素边缘、AGSL 编译/高刷帧率。未安装、未运行 ADB 或 QQ；这些复核项不能无设备证据自行打勾。

## 执行日志
- 2026-11-01：按用户要求一次性完成非采样源码收尾：关闭中重开从实际帧平滑接续并保留子菜单内容交接；修复极窄屏窗口定位、Alert 动画寿命、源按钮快照元数据验证；扩充源事件/返回/布局回归。最终 59 项 JVM 测试通过、Debug APK 构建通过、lintDebug 0 错误（15 Warning / 1 Hint）；写入非采样验收文档。玻璃采样/高光未变，真机验收仍保留。
- 2026-11-01：推进非采样快速开关和菜单命中：让 return 动画从当前开场 frame 的 radius/Goo blend/material/glyph/content 值无跳变接续，子菜单滚动命中增加固定 viewport 约束；补充对应回归测试，Gradle 编译/单测/APK 全通过，不动玻璃采样或高光，未进行 ADB/安装。
- 2026-11-01：按用户要求暂停玻璃采样方向，仅推进 Android33 Flutter 曲线自适应有理二次→三次精度、菜单滚动触发净位移判定以及纯 JVM 误差/菜单命中回归测试；编译、单测及 APK 通过，不动采样、高光、Sketch 项目或设备。
- 2026-11-01：根据 12:14 Goo 截图，恢复 FlClash 的 `Path.combine(union)` 对应 Android `Path.op(UNION)`，撤销 WINDING-only 优化所致的颈部交叉填充缝；编译、单测、APK 成功，保持 FlClash 高光与 Sketch 材质分工。
- 2026-11-01：根据用户 Goo 和菜单截图，修复 Sketch 固定布局层动态 path clip 导致的轮廓冻结，将 Goo 和菜单裁剪交还 FlClash 的实时绘制层；修复菜单超大镜头圆角和双重白色染色。离线 Kotlin/单测/APK 通过，未主动安装或 ADB。
- 2026-11-01：修复 Sketch Goo clip-only 导致的颈部折射缺失与多级菜单只读背景的采样层缺失；增加 FlClash 几何 Goo 光学 Shader、逐层 Sketch GPU Backdrop 录制/单向组合和深度回归测试。全部 Kotlin/单测/APK 通过，未进行设备验收。
- 2026-11-01：恢复 FlClash 原版高光主导：Sketch 仅做光学材质，禁用 Highlight；根/子菜单恢复原 FlClash Rim，普通按钮/Goo 接入独立原公式控制光照 AGSL，Kotlin/单测/APK 成功，未动设备。
- 2026-11-01：修复 Sketch 首次绘制 lens 的受限 Shape 类型导致的明确异常路径；使用 CornerBasedShape 承载真实 Goo Path，添加两个回归测试，Kotlin/单测/APK 全通过，尚未真机验证。
- 2026-11-01：直接把 liquidglass_sketch 的原生 LayerBackdrop/drawBackdrop 液态玻璃模块接入 liquidglass27；Sketch 默认启用且保留 FlClash 回退，不更改 Sketch 项目源码，Kotlin/单测/APK 通过；尚未设备验收。
- 2026-11-01：推进 plan 的 Flutter RSuperellipse 精度项：完成原版 fitted octant Path、50 条曲线缓存、API33 回退、AGSL 角部边缘 inset 对照与参数单测；构建成功，未安装及设备测试。
- 2026-11-01：完成 GPU 常驻多级图集代码主路径：真实 hardware 图集、按层折射采样、GPU tile BlurEffect、未变 band 与整层复用、仿射变换与内容生命周期、版本防旧帧和 shader 输入对接；单元测试和 APK 构建通过。依用户要求未执行 ADB、安装或真机运行，运行时表现待用户另行验收。
- 2026-11-01：延续 FlClash 分层捕获路线：建立真实 Compose GPU picture/root 记录与生命周期，绘制版本作为 band 失效依据，按 FlClash 32px 步长排布分层图集，关联真实纯背景层；默认关闭，不改变现有画面。Kotlin 与 APK 构建成功，未操作设备。
- 2026-10-31：推进 FlClash 分层采样准备：加入 Pane 注册表、分层/采样范围/band 缓存键规划和可选接入；默认关闭且不更动着色输出。完成 Kotlin + APK 构建，没有真机操作。
- 2026-10-31：继续动态背景缓存：单档 GPU 模糊带与同帧原图统一打包到单输入 AGSL 图集，静态/动态缓存仍默认关闭；只进行源码修改和构建检查，不进行任何 ADB 或真机操作。构建最终状态见上方专项记录。
- 2026-10-31：继续 `plan.md` 未完成的缓存工作：新增静态共享单纹理模糊图集与 AGSL 读图路径、实验开关（默认关闭），完整 APK 构建通过；本轮按用户要求未做真机测试或安装。
- 2026-10-31：修复 Goo 融合按钮拖动卡顿：软件回退的整行逐像素光学/高光计算改为 `Dispatchers.Default` 单工作者处理，合并积压的过期形变请求，主线程只绘制最新完成的位图；保持原高光/折射公式不变。当时尝试 Goo 轮廓非零绕组填充替代 `Path.Op.UNION` 降低帧开销，**已于 2026-11-01 12:14 截图反馈后撤销该错误等价假设，恢复原 FlClash 的布尔 UNION**。待真机检查 AGSL/回退两个分支的流畅度与高光跟手性，原始 FlClash 和 `liquidglass_sketch` 未修改。
- 2026-10-31：实现可选动态 GPU 模糊缓存及可选静态 GPU 三档实验方案，增加 raw/blur 同源原子交接、动态 5Hz 限流和静态 CPU/GPU 与动态采样演示开关；静态默认后台 CPU 缓存以避免未经验证的 GPU 三次读回影响启动性能。待设备验收，FlClash 与 liquidglass_sketch 无修改。
- 2026-10-31：继续背景渲染性能工作：为静态背景增加 3 档共享磨砂缓存，并让 AGSL 在每个动画阶段选择最合适的 sigma 档；动态背景增加按 backgroundKey 版本变化触发、限频且合并过期请求的模式。Gradle `:app:assembleDebug` 成功，原版 FlClash / liquidglass_sketch 未修改，真机未测试；完整 GPU band atlas 仍待移植。
- 2026-10-31：继续优化 FlClash 性能结构：建立场景级共享原始/预模糊背景 BitmapShader 缓存，单次静态后台高斯模糊；AGSL 按目标 sigma 选原始或预模糊纹理，保留实时回退；完整 APK 构建成功。设备性能及 AGSL 真机编译待用户验证，未修改 liquidglass_sketch。
- 2026-10-31：性能专项，根据用户反馈和 FlClash 原版缓存机制，优化全屏背景静态捕获、Path 64 射线 O(N·64) 求交、Shader 四邻点样条、固定图标抓图等热点，构建成功，设备端表现待用户验证；原版 FlClash 与 liquidglass_sketch 未修改。
- 2026-10-31：继续第 5 项的 FlClash 64 方向轮廓采样。新增 `GlassRadialProfile.kt`，从当前原生 `Path` 建立 64 个半径并拒绝多交点非星形 Goo；AGSL 增加 `uProfile[16]` 和 `profileGeometry`，对有效轮廓用 Catmull–Rom 计算真实路径的距离与法线；`GlassRenderer` 缓存采样结果并将 FloatArray 上传到 shader。第一次构建仅 FloatArray setter 调用错误，已修复，完整 `:app:assembleDebug` BUILD SUCCESSFUL；AGSL 真机运行未测试。未修改 `liquidglass_sketch`。
- 2026-10-31：继续第 2/5 项光学精度修复：对照 FlClash `liquid_glass.frag` 的 `veiled`、`sampleBehind`、`roundedBoxNormal` 和边缘带宽；对有原生 Path 裁剪的菜单取消 shader 重复 AA，增加可选 `backdropVeil`、解析超椭圆法线、正确的四点低模糊对比度及 7–10dp 色散带。已经 `:app:assembleDebug` BUILD SUCCESSFUL；AGSL 真机运行及视觉尚未验证；不修改 `liquidglass_sketch`。
- 2026-10-31：继续完成第 3 项动态源内容基础更新机制：添加 `GlassSourceContentSnapshot`（透明图像、像素尺寸、单调时间戳和样式标识），在闲置状态以约 120ms 刷新，并在打开时检查 350ms 新鲜度与形状/主题一致性后冻结；无有效图时回退原生绘制，菜单过渡期间不作位图读取。`:app:assembleDebug` BUILD SUCCESSFUL；设备视觉及高帧率视频同步未测试。目标 `liquidglass_sketch` 没有合并或改动。
- 2026-10-31：继续完成计划第 3 项的通用源内容接管：`ElasticGlassButton` 新增独立前景 `GraphicsLayer` 捕获接口，`GlassShowcase` 闲置时只截图文字/图标并在打开时冻结快照，`GlassMorphMenu` 只把透明内容图绘制在 Goo 上；无截图时保留 Compose 内容回退。未引入额外折射/玻璃层，不修改 `liquidglass_sketch`。Gradle `:app:assembleDebug` 成功，设备视觉由用户验收。
- 2026-10-31：按 FlClash 的真实源内容捕获、单一玻璃主体与关闭完成事件重构移植版按钮接管；删除完整玻璃截图混合、取消独立 500ms 隐藏计时；`:app:assembleDebug` BUILD SUCCESSFUL，待用户验证。
- 2026-10-31：修复快速打开关闭时的「圆套方」双层玻璃和圆角曲率跳变，代码构建成功，设备视觉待用户确认。原始 FlClash 和 liquidglass_sketch 均未修改。
- 2026-10-31：用户反馈二级菜单标题不能返回，增加最顶层标题的独立点击处理并固定标题区域，Kotlin 编译与完整 APK 打包通过；未进行设备运行测试。保持 `liquidglass_sketch` 不变。
- 2026-10-31：用户提供原版 FlClash 演示视频，梳理源按钮识别与子菜单行 400ms 停留两套不同的时序，记录逐帧对照基准；仅更新计划文档，未修改渲染或手势源码，未重构建。
- 2026-10-31：用户指出 FlClash 不是一律 PointerDown 触发，核对 `FlClash/lib/ui/popup.dart` 的 `_PopupPressRecognizer` 和 `FlClash/test/widgets/popup_test.dart` 后纠正：非滚动场景立即打开；滚动场景先等待 `kPressTimeout`，快触 Up 打开、提前滚动取消。为 `elasticPress` 增加 `popupPressDelayMillis`，示例设置 100ms，继续保留源按压事件接力和离线预截图。Kotlin 编译与 APK 打包均成功，未运行设备测试。
- 2026-10-31：修复用户反馈“按下触发没生效”：定位 `GlassShowcase.kt` 中 `onClick` 虽从 PointerDown 执行，实际却等待 `sourceLayer.toImageBitmap()` 完成才设置 `menuOpen=true`，造成显著延迟甚至不显示。现在 **先在按钮 Down 同步打开菜单**，并将源按钮截图移入闲置状态 `LaunchedEffect` 预捕获；快照不足时使用 `sourceContent` 回退，不阻塞打开。构建 `:app:assembleDebug` 返回 `BUILD SUCCESSFUL`，仍需用户真机确认按下时序。不修改目标项目 `liquidglass_sketch`。
- 2026-10-31：继续实现源「•••」按钮 PointerDown → 菜单：新增按钮指针事件转交、跨菜单挂载事件缓存、源按钮拖动阈值和 Cancel 清理。当时 `onClick` 仍需先等待截图，导致按下并未立即出现菜单；下条记录已修复。已执行 `:app:compileDebugKotlin`、`:app:assembleDebug`，均 `BUILD SUCCESSFUL`；未安装测试。只改 `liquidglass27`，`liquidglass_sketch` 没有合并或改动。
- 2026-10-31：继续未完成的交互项，重构 `GlassMenuGestures.kt` 为唯一全屏手势持有者；`GlassMenu.kt` 和 `GlassSubmenu.kt` 使用共享滚动状态与顶层命中坐标。完成菜单内部跨层长按→展开→滑动→松手的源码链路，并修正子菜单行无障碍触发。执行 `:app:compileDebugKotlin` 与 `:app:assembleDebug`，均 `BUILD SUCCESSFUL`；未运行设备测试。目标 `liquidglass_sketch` 无修改，采样合并仍等待用户明确指令。
- 2026-10-31：先建立本文件，再按 1→6 顺序实现并编译；详细状态见各节。保留未验证项，不将构建成功描述为视觉一致。
- 2026-10-31：专项修复二级菜单多重 rim 与父层 rim 的重叠，代码构建通过，待设备核对。
