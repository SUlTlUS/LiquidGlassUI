# FlClash popup.dart → liquidglass27 Compose：全文件逐段移植对照

日期：2026-11-02。源文件：`FlClash/lib/ui/popup.dart`，共 **2,980 行**。宿主：`app/src/main/java/dev/liquidglass27`。

## 2026-11-03 通用接口补齐

已增加 `GlassDialog`、`GlassPopupState<T>`、`GlassPopupBox`、`GlassPopupSourceContent`、`GlassPopupAnchor`、`GlassPopupProperties` 与 `GlassTheme`，覆盖任意内容、结果返回、输入/焦点、非模态、锚点、安全区/IME 和主题角色。菜单主体拆为可组合成员，消除大捕获闭包引起的真机 DEX 校验错误。此前“尚未完成”描述属于历史阶段；本轮范围与最终构建证据见 [实施记录](../plan.md)，用户验收见 [验证清单](verification-2026-11-03.md)。

## 2026-11-03 真机回归补充（上一阶段）

当前版本已安装至用户指定的 PLQ110（API 37）；模拟器已关闭。一级/二级菜单可打开，二级标题持续长按 1.8 秒，折回一级后未再次展开。菜单“关于此移植”可打开弹窗，确定按钮与系统返回可关闭。全量 307 项 JVM 测试通过。新增菜单矢量图标与宽度缓存支持、Compose 弹窗转场和布局；详细范围见 `../plan.md` 最新章节。

用户明确接管视觉效果验收。上述结果只证明已执行的功能路径，不替代更深层菜单、同指跨行、系统取消或逐帧视觉验收；历史章节的“待真机”应结合本次具体覆盖范围理解。

## 2026-11-02 二级标题长按折叠后重复打开：修复 Fold 中的命中竞争

- 前一阶段在 Fold Completion 时才发布 `MenuFoldPressHandoff`，原版 Flutter `_pop()` 在整段折叠中保持 `_pressed=_headerIndex` 和 `_landed=true`，最后将标题映射到父级原开门行。Compose 虽然保留了 `_landed`，但子菜单的 gesture level 只在 `motion > .08f` 时存在；在剩余最后几帧父级 Row/Blank/Back 先被 `hitAt` 命中，`FlClashMenuPress.move()` 提前清除 `_landed` 并重新触发 400ms。最终 FoldReturn 无法匹配已丢失的 Header，造成用户仍按着父级同入口时自动进入二级。
- 现在只改**已有的** `FlClashMenuPress` 状态：`completeDwell(ToggleHeader)` 设置 `foldingHeaderDepth`，在折叠真正结束前任何由于缩小/移除导致的 Header/Blank/Back/Outside/父行变化均不改变已获胜手势；`foldedToParent` 校验深度后映射到父级旧入口，保持 `_landed=true`，直到明确移到另一个行才可能重新计时。短按返回及 OpenChild→拖入新子菜单选择继续有效。正常触摸和携带触摸都阻止**位置未变**的指针事件触发重新命中/重置 `_pressed`。没有向 `GlassMorphMenu` 大闭包加入新的 `remember`，避开历史 DEX `VerifyError` 回归风险。
- Fold Completion 发布后还有一个细微竞态：旧子卡 `gestureLevels` 可能比 Handoff 迟一帧退出，初次成功转为父 Row 的 `_pressed` 又被旧 Header/Blank 命中清掉 `_landed`。补充 `FlClashMenuPress.restoringParentDepth`，父层真正成为最上层前忽略迟到的子层命中，之后正常允许移至不同的父菜单项。没有增加主 Compose 大闭包新捕获。
- 新增 6 项可确定性复现状态竞态的回归测试，修订一项先前与原版相悖的旧断言。正式 **287 项 JVM 测试、0 失败、0 错误**，Gradle `BUILD SUCCESSFUL`；最新 APK `liquidglass27-debug-20261102-214805-235.apk`（47MB），自动 QQ 脚本执行成功，对端收件尚未独立确认。仍需真机确认完整折叠及零/小位移的 Android 指针事件时序。

## 2026-11-02 遮罩圆角固定 16dp，不参与子菜单开合动画

- 逐段核对 `FlClash/lib/ui/shape.dart` 的 `AppCorner.md=16` 以及 `popup.dart::_buildRow`：原版的按住遮罩为 `ShapeDecoration(shape: AppShape.all(_itemRadius))`，**任何时刻都不对 itemRadius 插值**。只有卡片 `PopupMenuCard._radius` 使用 `_grow` 从 `_itemRadius=16dp` 插值到 `_cardRadius=24dp`。上一版本把 `menuPressedHeaderVisibleRadius` 设为 `max(itemRadius, cardRadius - inset)`，虽然避免了外部裁剪的平角，却引入错误的遮罩圆角动画，使圆角在伸缩中变化。
- 新版 `menuPressedHeaderVisibleRadius(visible,itemRadiusPx)` 只接收当前可见矩形与 16dp 固定行圆角；普通 44dp 行高始终返回 16dp，极端窄矩形仅按短边的一半保护合法路径。遮罩的宽度和位置仍服从原 `.30` 内容伸展路径，外部卡片圆角仍服从原 16→24dp 的弹簧进度；保留原 `menuPressedHeaderVisibleBounds` 求交保护，避免回到收缩期被截平的问题。`headerMaskPath` 使用相同 Flutter RSuperellipse 拟合，无额外 shader/采样、无需新增动画器。
- 测试将旧的“遮罩随 cardRadius 变化”期望换成 242 个展开与逆向采样进度均固定 16dp，还验证外部卡片独立增大、狭小图形物理截断和源代码不能捕获 `cardRadius`/`progress`。**281 项 JVM 测试，0 失败、0 错误**，Kotlin/DEX 打包及完整 `:app:assembleDebug` 通过。正式 APK 自动改名为 `liquidglass27-debug-20261102-211321-989.apk`（47MB）并调用 QQ 发送脚本成功（实际接收未独立核实）。修复 APK 已在 API 36.1 模拟器安装并启动、PID=3636，无新的 ART `VerifyError`；连续菜单点击时模拟器本体异常退出，缺少长按折叠视觉录像，不能声称真机完成验收。

## 2026-11-02 修复按住遮罩圆角曲线和收缩期裁切（上一阶段，动态半径已纠正）

- FlClash `_buildRow` 的 `ShapeDecoration` 以 `_itemRadius=AppCorner.md` 的 superellipse 绘制，`PopupMenuCard._radius` 从同样的 itemRadius 随 `_grow` 增至 `_cardRadius=itemRadius+8`，所以当 grow=0 时标题按住形状与整个折叠成行的卡片完全重合。之前 Compose 普通 Row 遮罩是 `RoundedCornerShape(10.dp)`；菜单卡片本身已经采用 Flutter 拟合 superellipse 16dp→24dp，因此收缩时两个拐角不同，遮罩被外部 `clipPath` 截成平角。现使用相同的 `gooPlatterOutline` 定义 `MenuPressedRowSuperellipse : Shape`，根菜单/子项遮罩均采用 Flutter RSuperellipse 16dp。
- `MenuPressedHeaderMaskBounds` 的宽度增长与 FlClash 内容约束一致，发生在进度 0～0.3；菜单外轮廓仍整个进度伸展。在中间帧遮罩实际宽度超出卡片，使原 `RoundedCornerShape` 先在越界位置构造圆角再被卡片 clip 截断。现新增 `menuPressedHeaderVisibleBounds` 做矩形与当前 panel 交集，并根据当前卡片 radius 和真实 inset 计算 `menuPressedHeaderVisibleRadius`，使可见 mask 本身就具有正确的 Flutter 拟合圆角（**后续核对发现这会错误地让行遮罩半径随卡片变化，已由最新阶段改为固定 16dp**）。通过 `GlassSubmenu` 现有 `drawWithContent` 层先画 mask superellipse，再绘制文本，统一 `clipPath(contentOutline)`；不增设背景采样、单独 Compose 玻璃窗或遮罩动画器。
- 单测 **279 项 / 0 失败错误**，其中新增 3 项针对圆角/裁切形状。Debug APK 构建成功并改名为 `liquidglass27-debug-20261102-195125-797.apk`，QQ 发送脚本执行完成，未独立核实实际送达。Android API 36.1 模拟器本次构建正常安装启动，打开一级/二级菜单并列出二级菜单项无 `VerifyError`；进一步点击标题收缩时模拟器 host 退出，尚无可靠逐帧视觉或真机验收依据。

## 2026-11-02 按住行遮罩在子菜单展开/折叠时跟随形变

- 原版 `popup.dart::_buildRow` 的 `pressed ? Theme.of(context).highlightColor : null` 是 `DecoratedBox/ShapeDecoration`，与菜单项内容一起被 `PopupMenuCard(grow:_unfold.value)` 进行几何变形/裁切；不是单独淡入的浮层。`_buildLevel` 通过 `_widthSpan=0.3` 的约束宽度增长和 `depth/_unfold` 位移把父入口的高亮行连续变成当前子菜单标题。
- Compose 旧 `GlassSubmenu.kt` 强制 `settled>=.85` 才允许 Header 高亮、`settled>=.94` 才允许行高亮，而且 fold 时 `interactive=false` 把所有 hovered 值清为 -1，造成开门时高亮空白、收门时提前消失。现新增 `MenuPressedMaskMorph.kt` 的 `menuPressedHeaderMaskBounds()` 用同一原版 `.3` width span、当前滚动/入口坐标及 `glassSubmenuHeaderTopPx` 计算标题位置/宽度；将这些结果重新用于标题 Row 的 layout。剪裁仍使用玻璃卡唯一 `contentOutline`，遮罩保持原 RoundedCornerShape/黑白 alpha 与行 `background`，删除两处进度硬阈值，不创建独立贴图或新的 Shader。
- `menuPressedMaskHoverForLevel` 允许**折叠中的非交互 Header**继续显示当前手指的按住遮罩，不允许被覆盖的普通行显示不属于自己的触摸高亮。首次/深层 Fold Completion 在移除子卡前，同时将画面高亮索引从标题转交到原来打开该子级的父入口；与之前 `MenuFoldPressHandoff` 的按压语义和 `_landed` 保护完全一致，避免仍按住时重复打开。
- 新增 `MenuPressedMaskMorphTest` 8 项验证布局几何连续、反向同轨、延迟阈值删除、折叠期遮罩以及父级交接；全量 **276 项 JVM 测试 0 失败**。Android 16/API 36.1 模拟器可安装、启动并实际进入二级菜单且无 ART VerifyError；模拟器在进一步长按标题时 host 退出，无可靠折叠视频，真机/120Hz 视觉验收仍需。

## 2026-11-02 ART DEX 校验闪退：主 Compose Lambda 捕获过多

- 对上个构建 `liquidglass27-debug-20261102-190257-965.apk` 在 **API 36.1 Android 模拟器**实际 `adb install`→`am start` 复现应用启动时 `FATAL EXCEPTION: main`，`java.lang.VerifyError` 指向 `GlassMenuKt.GlassMorphMenu$lambda$104`，异常文本 `type Float unexpected as arg to if-eqz/if-nez`；`classes5.dex` 的 DEX 字节码被 ART 拒绝。上一版 `185332-904.apk` 在同一模拟器可启动，Kotlin/JVM 单测都不覆盖 DEX 的 ART 类型校验。
- `MenuFoldPressHandoff` 原为单独 `remember` 状态并成为巨型 Compose 子 lambda 的新捕获字段。修复为 `NestedMenuMotionJobs.foldPressHandoff`，由**已有捕获且 remembered 的 owner**管理，避免 Compose/D8 新增闭包参数，同时保留 Flutter `_pop` 索引回填事件、两条触摸流、事件代次与所有长按逻辑。新增 `MenuFoldPressHandoffTest` 防回归断言。
- 修复 APK `:app:packageDebug` 安装至同一模拟器，重启应用后 `pidof dev.liquidglass27=4425`，已成功显示主页且 adb 点击打开一级菜单后 PID 仍存在，`AndroidRuntime:E` 没有 FATAL。模拟器随后退出，未验收二级标题交互的全过程。全量 **268 项 JVM 单测、0 失败/错误**、最终 APK `liquidglass27-debug-20261102-191437-623.apk` 构建并调用 QQ 发送脚本成功；QQ 实际送达未独立验证。应以真实 Android 启动/交互 smoke test 补足只靠 JVM 测试的缺陷。

## 2026-11-02 长按二级标题折回后，同一手指不得自动重新展开同一入口

- **源码根因**：`popup.dart::_pop()` 在 Fold 完成并 `_path.removeLast()` 时，如果 `_pressed == _headerIndex` 就将 `_pressed` 改为 `step.index`（原本打开子菜单的父级行索引），不清理 `_landed`。所以 `_slidePress` 再读到同一行索引便直接 return，不会重新 `_hold` 或触发 400ms 展开。Compose 原来仅完成 `held.completeDwell(ToggleHeader)` 保留 Header/-1，随层级回到父级后新的 Move 把入口 index 当成“变更行”，清除 landed 并启用新 dwell，于是一直按住也可能重复展开。
- **修复**：添加 `MenuFoldPressHandoff` 发布折叠完成的子层 depth 与开门父行 index，首次层折叠发布在 `submenuIndex=-1` 前、深层 `popNested` 发布在 `nestedPath.dropLast(1)` 前。`FlClashMenuPress.foldedToParent` 只在仍按着对应 Header 且 `_landed=true` 时将其更新为父层开门 Row，保留 landed，等待手指真正移动到另一个 row 才允许新的 `_hold`。两个手势路径（正常 pointerInput 与 GlassCarriedPress）都在处理移动/抬手前按 sequence 消费 FoldReturn，避免层级重建带来的回放。按钮点击/不长按的 header 不会被误转换。
- 新增 `MenuFoldPressHandoffTest` 9 项；所有原有测试保留。**267 项 JVM 测试、0 失败/错误**；Kotlin/Debug APK 构建成功，自动改名为 `liquidglass27-debug-20261102-190257-965.apk` 并调用 QQ 发送脚本（日志 `Send script finished`）。无 ADB 设备，待实际设备验证长按标题折回后继续静止大于 400ms 无新展开、三级返回及跨行重新选取。未改动 Sketch 玻璃、形变参考区域或动画物理。

## 2026-11-02 长按二级菜单标题折叠后父卡额外放大一次

- **根因**：`popup.dart::_buildLevel` 中父卡自身缩放连续地由 `_unfold` 0.95→1，但 Compose 原先 `menuPressPanel()` 每帧依据挂载的目标子卡重算 Stack 矩形；折叠结束刚移除子卡的同帧，`menuFlutterStackLayoutBounds(root,targets)` 突然缩小到 `root`，仍在按压中的 `menuElasticTransform(..., lift=1)` 的 `growthOf(longestSide)` 立即增大，缩放中心也骤变，造成父卡多放大一次。
- **处理**：新增 `MenuFoldPressGeometry`，只在子卡开始折叠且按压/拉伸/悬停仍有实际形变时，冻结 `menuBouncePanel` 所记的完整 Stack press 参考矩形；首层 `LaunchedEffect(submenuOpen,submenuIndex)` 和深层 `popNested()` 的 fold 分支均在弹簧启动/子卡移除之前调用 `pinForFold`。用于整体 graphicsLayer、逆命中与惯性冲击同一参考；新子菜单的布局/玻璃/阴影不受影响。不会仅因触摸松开或层级已 pop 就解锁，而是等待 press/hover 与拉伸弹簧全部接近 0 后平滑解除锁定，以防再次末帧跳动。
- 回归 `MenuFoldPressGeometryTest` 8 项包括旧版跳变数值重现、按压末帧零跳变、无 press 快速折叠、连续深层折叠、坐标逆矩阵与接线；原布局的两项源码结构断言更新。真机真实 60/120Hz 视觉验收未完成。Sketch 光学/采样完全未改。

## 2026-11-02 差异 7：菜单手势识别竞争（Gesture Arena 行为对应）

- `popup.dart::_RowPressRecognizer` 在 Down 只 startTrackingPointer，不立即 `GestureDisposition.accepted`；行 _hold/_slidePress 在等待中运行，只有 Up 且其它候选未赢才接受。旧 `GlassMenuGestures.kt` 直接 `down.consume()`、未决 Move 也 `change.consume()`，使菜单抢赢可滚动父级。现在抽取 `MenuGestureArena`：`Pending` 阶段不消费 Down/Move，超过 touchSlop、纵向优势才由同一 Down 记录的滚动层接管；被其它手势在 Main/Final 消费时转 `Rejected` 并取消 dwell/行激活。Up 若被其他识别器消费也不会执行菜单操作。
- 防止跨级错判：`menuScrollOwner` 的层级来自 Down 时 hitTest，不跟随 `_pressed` 高亮行变化；Scroll 只能在最初的合法列表层获胜，标题和卡片外边距不能滚动，分组间隔的真实滚动视口允许滚动。400ms dwell 推/折子层后用 `arena.levelTransition()` 禁止旧父层 Scroll 在同一携带手势下抢赢，仍允许 `_landed` 状态后移动到子菜单另一个行进行选择。禁用行仍可启动滚动但不能执行 onClick。
- `GlassControls.kt` 的 `_PopupPressRecognizer` 对应：零延迟由按钮直接接管；可滚动父容器内的 100ms 延迟阶段不消费手势，Main/Final 消费或 touchSlop 越界即拒绝，Down 已被消费与 Up 被其他 handler 消费时不强制打开；通过 GlassCarriedPress 沿用已接管源按钮的原始指针事件。
- 新增 `MenuGestureArenaParityTest` 14 项确定性判定/源码接线覆盖；同步更新两项旧结构断言。全量 **250 项 JVM 测试，0 失败**；Kotlin/Debug APK 构建成功，自动改名并调用 QQ 发送脚本（结果 `Send script finished`），无 ADB 设备。**Compose 实现 Flutter 对应行为，但并没有、也无法直接调用 Flutter 引擎的 Gesture Arena**。真机竞态、系统触控取消和跨框架帧调度仍待验证。保持 Sketch 玻璃采样、光学与 Goo Shader 不变。

## 2026-11-02 差异 6：多条形变动画的叠加与中断

- **确认并去除双通道竞态**：`GlassMenu.kt` 原以 `menuPullY + menuEdgeBounceY.value` 组合两个独立回弹轴，两个不同的动画 Job 分别管理 Y 位置；当惯性在碰撞后被 PointerDown/切层取消时，原 `Animatable` 可能停在非零位置或丢弃速度，造成整体视觉不连续。现在 `MenuMorphMomentum.pullY` 是手指跟手和额外撞边界冲击的**唯一 Y 物理通道**；`MenuRetargetSpring.addVelocityImpulse` 原位添加瞬时速度，不会先移动一截或重启动画时钟。
- **有条件接受冲击**：`MenuMorphMomentum` 管理 fling epoch、`pointerHeld`。新指针、press、subnav 切层、dismiss 撤销旧 epoch；只有同一代 fling 完成、已松手和对应边界确实触碰时，才允许把受限冲击速度注入 Y 通道。过期弹簧不影响正在跟手的菜单。新 press 用原来的位移/速度切换到 120ms 临界跟手，X 轴同步继承；放开后切 311/.593 返回。Lift .975 的弹簧反转保持原逻辑，光学采样/Sketch 没动。
- **异常生命周期**：关闭菜单撤销 pending impact 并让 X/Y 平滑归零；即使指针 overlay 被销毁而未收到 Up/Cancel，也执行 `endMenuPress()`，不遗留按压缩放。重进按压、两次反向碰撞和菜单切层对先前坐标/速度保持连续。
- `MenuMorphMomentumTest` 新增 10 个确定性状态/动力学场景，更新两个旧结构断言，全量 **236 项 JVM 测试、0 失败/错误**、编译通过。该项目按用户要求添加的惯性撞边界整体回弹仍是 FlClash **原版之外的功能**；真机 120Hz 光影与性能未验收。

## 2026-11-02 拖拽菜单不应导致收回：FlClash 释放语义修正

- 原版 `popup.dart::_RowPressRecognizer` 只在 Down 命中 `_MenuRow` 时启动，Up 时 `_endPress` 读取 `_rowAt(position)?.onTap`；滑到菜单外则返回 null，只取消 press，**不会关闭弹窗**。原本 Compose `GlassMenuGestures` 的 `TargetKind.Outside -> dismiss()` 在所有 Up 上直接执行，导致内部拖出后误关闭。
- 新增 `menuReleaseDismisses(down,up,movedBeyondTapSlop)`，严格要求 Outside→Outside 且从未超过 tap slop，保留背景 tap 关闭；内部拖出至 Outside 则只让整菜单 ElasticPress 弹簧归位。`menuReleaseFoldsParent` 限制被覆盖父菜单的折回只能由父卡直接 tap 触发，排除从子行横向滑到父卡造成误折回。
- `menuCanReleaseRowFrom` 保证 `_RowPressRecognizer` 只接收从菜单行/禁用行/标题启动的选行手势；外部/菜单留白拖进可点击行不触发误选。新增 `MenuDragDismissParityTest` 5 个判定及源码集成测试，同步更新旧 Down 断言；**226 项 JVM 测试通过**。未更改 Sketch 材质、菜单阴影、弹簧和额外惯性撞边界形变。

## 2026-11-02 差异编号 1–4 源码收敛

- **1 触发范围**：统一 `menuCanElasticPress`，菜单行/禁用行/返回标题/被覆盖父卡/卡片留白均属于 Flutter `ElasticPress` 的绘制子树；Outside 与 platter 不激活。原 `GlassMenuGestureSurface` 的普通指针和 CarriedPress 指针都共用这一门槛。
- **2 布局基准**：`menuFlutterStackLayoutBounds` 按 `popup.dart` 的 `Stack(alignment:topRight)` 还原布局 Rect：根卡上/右边锚点、所有已挂载子级最终宽度与最大底部，替代绘制 box 的动态 union。整卡片 graphicsLayer 与逆命中、额外惯性冲击预算使用一致基准；真机需要核对 Flutter layout 本身的像素测量。
- **3 连续重定向**：新 `MenuRetargetSpring` 不创建每次 Move 的 Animatable Job；沿用连续 `withFrameNanos` ticker，每次 `springTo` 保留当前位置与速度，解析欠阻尼/临界/过阻尼方程，使用 Flutter `_Spring` 位置容差 `.001`、速度容差 `.001*sqrt(stiffness)`。跟手 120ms/ratio=1，松手 311/ratio=.593，空闲自动暂停。原 `_lift` press-snap 分支保持。
- **4 Hover**：补独立 `menuHoverSpring()` 500ms/bounce .32/目标 .4，通过 `press+(1-press)*hover` 与按压缩放合并。鼠标与悬浮笔 Enter/Move/Exit 使用不消费事件的监听；触屏不触发。FlClash 原版 `ElasticHover` 是可选包装器，`popup.dart` 没有显式把它包在菜单上，因此整菜单悬停是按用户指定补加的体验，而非原版 popup 自带的默认行为。
- `MenuElasticFullParityTest` 新增 8 个独立测试，旧 3 个源码结构断言已更新。全量 221 项 JVM 测试、0 失败；尚缺真实设备上的 60/120Hz、鼠标/触笔和阴影高光视频对照。Sketch 玻璃采样/材质未改。

## 严格完成标准

- 本表按源文件**连续且无遗漏的行号区间**登记实现责任；每一行落在某个区间内。行号包括空行、import、注释、类型声明和 Flutter 框架胶水。它是**覆盖审计**，不等同于声称每行都已经实现。
- 状态为**已映射**：功能在 Compose 中有明确对应位置；**部分**：有对应基础但仍有差异；**缺口**：当前无对应功能；**保留 Sketch**：用户明确要求不照搬 Flutter 的材质/采样，实现交给 Sketch 的 Compose 后端。凡标为部分/缺口的，均不得称“逐行移植完成”。
- 验收必须包括可重复的功能/状态测试、APK 构建，以及用户实际设备上的动画/着色验证。源码中名称相近、测试仅检索字符串、或 Gradle 成功都不足以宣称像素/时序一致。
- 参考项目只读。既定边界：**Sketch 实现玻璃背景采样、镜头、模糊、材质；FlClash 负责菜单物理、层级、几何、交互、阴影与高光**。不重新把 FlClash 材质 Shader 注入 Sketch。

## 源码行号到 Compose 对应关系

| FlClash 行号 | 原版责任 | Compose 对应位置 / 本轮状态 |
|---|---|---|
| 1–27 | Dart/Flutter imports | **Compose 替代**：Kotlin import、Compose UI/Foundation/Animation、Android Canvas；不复制语言级依赖 |
| 28–127 | Anchor/placement 定义；尺寸、阴影、Goo、开关动画、dwell、unfold/fold 常数 | **部分**：`FlClashPopupRouteMotion.kt`、`FlClashMenuSprings.kt`、`GlassMenu.kt`、`MenuLayerMotion.kt`、`MenuElasticTransform.kt`、`MenuPopupPlacement.kt`；字体/主题与 Flutter 的标量仍须逐项核对 |
| 128–252 | `CommonPopupRoute`：单 route、modal、source image、动画反向时长、Platter 状态、延后执行 | **部分**：`GlassMorphMenu` 的 `clock/mounted/returning/pendingAction` 和单一路由进度；尚非 Navigator `PopupRoute<T>`，缺泛型返回结果、非模态/焦点模式 |
| 253–364 | Barrier、PopupScope、ElevatedSurface、定位、Goo 与玻璃根层组合 | **部分 + 保留 Sketch**：全屏 `GlassMenuGestureSurface`/关闭层、`GlassScene`、`SketchGlassBridge`；没有独立 `elevated/modal/avoid` 的完整 Flutter Route 语义，`avoid` 已可用于菜单定位 |
| 365–414 | Unfold 通知、共享 CarryPress/Surface、InheritedWidget 传递接口 | **Compose 替代**：`GlassCarriedPress`、CompositionLocal Sketch backdrop、`GlassMorphMenu` 参数和 remembered 状态；通知不照搬 widget tree 机制 |
| 415–474 | `_PopupMorph` RenderObject 创建/更新及字段 | **Compose 替代**：`GlassMorphMenu` 的 Compose 状态 + `graphicsLayer`/Canvas；无需 `RenderObject` 的 setters |
| 475–663 | MorphFrame/Opening、RenderProxyBox 生命周期、动画监听、closing 快照、pane 释放 | **部分**：`MenuMorphFrame`、`closingStartFrame/closingFrom/closingNested`、`DisposableEffect`、`GlassScene`；Flutter attach/detach 与 Android 组件卸载不是一一同构 |
| 664–757 | 正向、带源返回、无源 200ms 缩放消失；`_openingOf` 春弧 | **已映射（源码级）**：`menuOpeningFrame/menuReturningFrame`、`FlClashPopupRouteMotion`、Compose 动画进度、关闭无源缩放；关键帧真机待验 |
| 758–880 | Platter 与子卡交接、Goo、内容裁剪、源图像和光学/阴影顺序 | **部分 + 保留 Sketch**：`GlassMenu.kt`、`GooPairOutline.kt`、`MenuCardShadow.kt`、`GlassMenuRim.kt`；Blur/Lens/材质改用 Sketch；源图像空值策略与原版不完全相同 |
| 881–1032 | 返场 Goo、水滴、回弹、内容退场、Shader/Rim 与 source 交接 | **部分 + 保留 Sketch**：`menuReturningFrame`、`GlassReturningSubmenuContent`、`GlassSourceContentSnapshot`、`SketchGlassBridge`；视觉/滤镜顺序不能宣称像素一致 |
| 1033–1131 | `_paintShadows` 排除内部阴影、`_paintSource`、`_paintContent` 的透明度/模糊/坐标 | **部分**：`MenuCardShadow.kt` + 内容透明度/变换层及透明源图；FlClash 的 evenOdd 内部阴影挖空、逐帧源 blur 并未逐像素照搬 |
| 1132–1191 | AnchorTracker、post-frame 更新、安全区/键盘 inset | **部分**：示例 `onGloballyPositioned` 读取 root 坐标，`GlassMorphMenu.anchor`；尚缺自动追踪窗口 inset 变化和通用锚点 resolver |
| 1192–1313 | PopupLayoutDelegate 的三种位置、avoid 上下避让、clamp、_PopupPin | **本轮补齐三种位置和 avoid**：`MenuPopupPlacement.kt`、`GlassMorphMenu(placement, avoid)`；`_PopupPin` 保持已定位右上角与安全区的全部细节**仍部分** |
| 1314–1556 | CommonPopupBox：源按钮、可启用判断、透明图截取、Pointer 接力、销毁后显露 | **部分**：`ElasticGlassButton`、`GlassCarriedPress`、`SourceContentPolicy.kt`、`GlassShowcase`；通用任意 targetBuilder 与 Flutter Navigator cancel 机制并未完全通用化 |
| 1557–1626 | `PopupValueText` 的内容切换、blur/scale 过渡 | **源码级已映射、效果待验**：`GlassPopupValueText.kt` 300ms size、fade/blur 时间窗和状态文字示例；Flutter 旧值零尺寸 Stack 布局/平台模糊差异真机未验 |
| 1627–1731 | `_PopupPressRecognizer`：单主指、滚动容器延迟、Gesture Arena、Up/Cancel | **部分**：`GlassControls.kt` 的 `popupPressDelayMillis` 与指针传递；Compose PointerInput 不能直接声称与 Flutter Gesture Arena 完全相同 |
| 1732–1766 | MenuItem 字段：subtitle/glyph/onPressed/danger/checked/section/subItems | **部分**：已增 `subtitle`、`glyph` 字符串、checked ToggleableState、禁用与 Section、点击/多级结构；仍缺 Flutter 基于矢量 Path 的 typed GlyphShapes/GlyphPainter 对应实现、完整焦点/无障碍 |
| 1767–1826 | Section/层级数据模型、min/max 宽、可见行 cap、_Span | **部分**：`GlassMenuSection`、`MenuContentRows`、`GlassSubmenuGeometry`、`MenuLayerMotion`、`GlassMenu(maxVisibleItems)`；根菜单 cap 与符合条件的真正 LazyColumn 已映射，其他层级与字体缩放仍待设备核对 |
| 1827–1909 | _CommonPopupMenuState：unbounded Spring、pressed/dwell、路径、滚动、初始化/卸载 | **已映射（源码级）**：`GlassMenu.kt`、`GlassMenuGestures.kt`、`NestedMenuMotionJobs.kt` 和 `Animatable`；真实系统返回/Pointer 仍待设备验 |
| 1910–1976 | `_buildsLazily`、`_revealChecked`、行高、全局文字测量 LRU | **部分**：`MenuLazyRows.kt` 严格跟随仅有显式 cap、根菜单、纯固定行的惰性触发条件；LazyListState + checked 初始整行滚动、`MenuLabelWidthCache.kt` 已移植；完整 Flutter TextScaler/全局缓存与平台字体测量仍需验 |
| 1977–2006 | `_buildLazyContent`、`ListView.builder`、首帧可见行和特殊滚动初始值 | **已映射（源码级）**：根菜单满足条件时采用 Compose `LazyColumn(items(count))` 真正按需合成；按 visible+.5 行控制高度、checked 位置拆成 index/offset；`MenuScrollConflator` 滚动与动画/命中共用统一像素坐标。Flutter 系统辅助功能滚动行为和高刷性能尚未实机核验 |
| 2007–2059 | `_resolveLevels`、`_cardKeyAt`、顶层卡片范围 | **已映射（源码级）**：`submenuIndex`、`nestedPath`、`LiveNestedSurface` 和 `GlassSubmenu`；Flutter GlobalKey 改为 Compose remember keys |
| 2060–2147 | `_push/_pop/_toggleTop`、反向 Spring、选择后再 dismiss | **已映射（源码级）**：`pushNested/popNested`、共享 `submenuMotion`、`pendingAction`；用户原先指定的跨层弹簧已移植，设备仍未验 |
| 2148–2257 | `_rowAt/_slidePress/_hold/_endPress`、400ms dwell、Carried touch | **已映射（源码级）**：`hitMenu/FlClashMenuPress/GlassMenuGestureSurface`；本轮禁用行保留可滚动、禁止激活；Gesture Arena 精确仲裁仍部分 |
| 2258–2387 | `_buildRow/_buildItem`：disabled、danger、checked、图标、字幕、焦点、Semantics | **部分**：共享 `MenuItemContent.kt` 绘制左侧 20dp 文本 Glyph、15sp 标题、13sp 最多两行副标题、尾部 16dp 箭头/勾选；根/子行的可变高度、disabled 和 selected 状态语义已对应；Flutter GlyphPainter 路径图形和全面焦点语义未完成 |
| 2388–2467 | Section title/divider、被覆盖行隐藏、子级标题箭头/选中、内容构造 | **部分**：隐藏被覆盖行、标题 pinned/旋转、Section 与字幕按真实测量高度进入根/多级菜单行起点、动画源锚点与触点二分命中；文字 Glyph 已有，Flutter 24×24 矢量 GlyphPainter / 焦点语义待补 |
| 2468–2582 | _buildLevel：单顶层、累计退让、阴影、Glass/Rim、子卡从行长出、内容裁剪 | **已映射 + 保留 Sketch**：`GlassSubmenu.kt`、`MenuLayerMotion.kt`、`GlassMenu.kt`、`MenuCardShadow.kt`、`GlassMenuRim.kt`；材质按用户要求独立 |
| 2583–2637 | PopScope 系统返回、ElasticPress、RawGestureDetector、多级栈合成 | **已映射（源码级）**：`BackHandler`、`MenuElasticTransform.kt`、`GlassMenuGestureSurface`、各级 Compose Box；真机交互需继续检查 |
| 2638–2731 | _MenuRow/_RowPressRecognizer：pointer accept/reject/cancel 和滚动竞争 | **部分**：`GlassMenuHitLevel`、`GlassCarriedPress`、`FlClashMenuPress`、Compose PointerInput；缺独立 Gesture Arena 仲裁意义上的逐事件等价 |
| 2732–2791 | PopupMenuCard 的 RenderObject 创建/更新、全部状态入参 | **Compose 替代**：`GlassSubmenu`/GlassPane/Surface 层 + remember state，不需手动 RenderObject setters |
| 2792–2980 | _RenderPopupMenuCard：grow rect/radius、hitTest、超椭圆、阴影、Glass、clip、父级 dim | **部分 + 保留 Sketch**：`glassSubmenuInterpolatedShape`、`FlutterRSuperellipse.kt`、`GlassSubmenu`、`GlassMenuHitLevel`、`MenuCardShadow`；Android/Flutter GPU 栅格差异设备待验 |

## 2026-11-02 菜单 ElasticPress 拖拽回位弹跳真实源码对齐

- `navigation_dock.dart::_ElasticPressState` 中跟手 `_pullX/_pullY.springTo(_rubberBand(...),_trackSpring)` 与释放 `_pullX/_pullY.springTo(0,_pressSpring)` 是两套 spring，并且 `_Spring.springTo()` 保留当前速度。旧 Compose 同时为跟手与释放使用 `spring(dampingRatio=.8, stiffness=350)`，与原版不同。
- 原版 `_rubberBand` 是渐进倒数 `limit*(1-1/(abs(delta)*.55/limit+1))*sign`，不是项目通用 tanh 阻尼；Compose `menuElasticPull` 改为专用 `menuElasticRubberBand`，准确计算菜单整体平移和缩放。
- 新增 `menuTrackSpring`（120ms、临界阻尼、stiffness=`4π²/0.12²`），menu release 为原 `311/.593` 并使用 `.001` 精度；`GlassMenu.kt` 的 `menuPullX/menuPullY` 随 `menuPressActive` 在两种物理间切换，动画当前位移和速度连续承接。未调整 Sketch 光学或用户新增的惯性撞边界额外回弹。
- `MenuDragReleaseParityTest` 5 项验证拉伸阻尼、两弹簧参数、回位超调、非零初速度的欠阻尼精确解析轨迹和代码连接；全量 **213 项 JVM 测试通过**，Debug APK 构建和 QQ 脚本执行完成。真机录像未采集。

## 2026-11-02 用户新增：惯性撞边界时的整卡片回弹（原版之外）

- FlClash 原版 `ClampingScrollPhysics` 的滚动内容不能越界，原版 `ElasticPress` 仅在手指按住/外拉时可使整个菜单 Stack 做拉伸；原版**不因滚动惯性到达边界自动触发**整体形变。用户明确要求把后者连接起来，不能把该行为记为 FlClash 原版对应项。
- 现改为 `GlassMenu.kt` 的 fling 结束残余速度 + 相应 `canScrollForward/canScrollBackward` 边界判定触发 `menuEdgeImpactVelocityPx`。该函数在 `MenuElasticTransform.kt` 将 scroll 正负冲量转换为沿手指方向的 Y 初速度，冲量上限使用原 `ElasticPress` 最短边×7/32 弹性预算；新增 `menuEdgeBounceY` 以原 `menuPressSpring()` 收敛，复用整 Stack `graphicsLayer` 和逆命中矩阵，不增加滚动内容 overscroll，也不动 Sketch 采样/玻璃材质。
- 初始位置为 0，带冲击初速度，因此不会在碰撞帧立刻跳位；手指重新触摸、中途切换菜单层级及关闭菜单取消旧惯性并将变形带回零。数值/JVM 测试涵盖冲击方向、范围、弹簧轨迹、整体变换与 lifecycle。真机视觉、120Hz 和与 FlClash 原版 `ElasticPress` 按压同时作用的混合效果仍待观察。

## 2026-11-02 滚动惯性缺失修正

- **原版确认**：根长列表 `ListView.builder` 与普通菜单 `SingleChildScrollView` 统一使用 `ClampingScrollPhysics`，支持甩动后的 ballistic 减速并钳制上下边界。Compose 此前的全屏 pointerInput 只按 `onScroll` 更新 `scrollBy`，没有收集松手速度和发起 fling，致使惯性完全缺失。
- **对应实现**：`GlassMenuGestures` 的 VelocityTracker + Android ViewConfiguration 速度阈值，`MenuScrollConflator.offerFling` 的尾部排队和会话代次防错，`GlassMenu` 的 `ScrollableDefaults.flingBehavior()` / `ScrollableState.scroll` 将 fling 交给原 LazyListState/ScrollState；新按下、菜单切层或退出立即取消正在滑行的 Job。Android 平台样条惯性与 Flutter Clamping 不保证逐帧等效，需真机验。

## 2026-11-02 二级菜单底边回弹末端跳变修正

- **根因**：`FlClash popup.dart::_unfoldSpring` 经 Flutter `SpringSimulation` 运行，系统 `Tolerance.defaultTolerance` 是距离/速度 `0.001`，而 Android Compose `spring<Float>` 默认 `visibilityThreshold=0.01`。相同物理参数在 Compose 中于约 412ms 接近超调峰值时提前结束，`animateTo` 完成帧归一到 1，底边从约 `1.00628` 的进度硬切回最终位置。
- **源码改动**：`FlClashMenuSprings.unfold/fold` 设 Flutter 对应的 `.001f` 位置阈值，保留原 420ms/.15、340ms/0 参数；`GlassMenu.kt` 一级和深层展开完成时移除冗余 `snapTo(1f)`，折叠/恢复的同步状态切换保留。没有修改形变计算、Sketch、玻璃光学链。
- **回归结果**：`TargetBasedAnimation` 对照确认 0–390ms 轨迹一致，终端残余跳变减少到约 1/5；200 项 JVM 测试通过，Debug 编译/打包成功，QQ 发送脚本已执行。未采集真机 60/120Hz 视频，不宣称最终视觉完全一致。

## 2026-11-02 父菜单压暗补充对照

- **参考公式和渲染阶段**：`_buildLevel` 在 `PopupMenuCard` 上传入 `colorScheme.surface.withValues(alpha:min(.4,.2*distance))`；`_RenderPopupMenuCard.paint` 在 `context.paintChild` 后，在相同 shape 裁剪路径内绘制该颜色。之前 Compose 强度公式无误，但深色固定 `#24252B` 更接近原版暗色卡片 tone15，不是应当压暗用的 page surface tone6。
- **新默认值**：独立 Compose demo 以 `menuRecedingSurfaceColor(dark)` 为一级及深层父级共享底色，明 `#F9F9FB`（约 tone98）、暗 `#121316`（约 tone6），保留原 `menuRecedingDim` 强度。参考 FlClash 有动态主题种子/纯黑模式，不能声称当前固定近中性色完全对应各种动态主题；仅按参考默认色阶接近。`MenuParentDimParityTest` 覆盖以上语义。
- **回归状态**：184 项 JVM 测试全部通过，Debug APK 构建、命名与 QQ 发送脚本执行成功；真机色彩采样及视频时序仍待验证。

## 2026-11-02 根菜单阴影、高光与字体视觉补充对照

- **根菜单在打开/收回时的阴影路径**：原 `_RenderPopupMorph._paintFrame/_paintReturn` 路径是完整 `gooPairOutline(platter,droplet,blend)`，不是单独的主 platter。Compose 在动态 Goo 期间共用该轮廓生成阴影；`MenuCardShadow` 用 EVEN_ODD 剪掉内部，固定菜单卡片仍走原 shadow 路径。由于 Android BlurMask/ShadowLayer 与 Flutter BoxShadow 的 GPU 实现不同，不能称像素等价。
- **父层 rim**：原 `_buildLevel` 的退让卡片只缩小、调暗和改变 shadow，未降低 rim 强度；删除 Android 自加的 `(1-nestedProgress)` 强度衰减。原 `_paintReturn` 没有 rim，返回源时因此关闭额外原生 rim；Sketch 材质链不动。
- **菜单条目字重**：原 `_buildRow` 所有标签共用 `subheadline` 默认 w400，即使 `checked` 或子菜单标题也不人为加粗。根、子层和返回内容均改为 Normal。
- **验证**：181 项 JVM 单测全部通过，离线 `assembleDebug` 完成，命名 APK 和 QQ 发送脚本执行成功；真实设备逐帧/性能仍未验。

## 截至前一阶段的新增对应实现

1. `CommonPopupMenuItem.onPressed == null` 对应 `GlassMenuAction.onClick == null`：禁用项仍占一行、仍能滚动其所属长列表，但不触发选择/展开；根/子菜单 clickable 和手势命中同样防止误激活。
2. `_revealChecked` / lazy 初始偏移对应 `menuCheckedInitialScrollPx`：首次展开时把已经勾选的根行尽量放到可见列表中部；不会在每一帧重复滚动。
3. `PopupPlacement` 对应 `GlassMenuPlacement`：`OverAnchorEnd`（保留默认）、`OverAnchorStart`、`BelowPoint`，包括 `avoid` 优先向上/向下的 8dp 间距回避策略；对应 `MenuPopupPlacementParityTest`。
4. 独立的 `FlClashMenuItemParityTest` 检查禁用、选中初始滚动、可交互性与被禁用行上发起滚动。

## 仍需继续移植的明确功能（按影响排序）

1. **菜单项结构与布局**：已完成 `section` 标题与间隔、最多两行 `subtitle`、20dp 文本 Glyph 与可变行高的渲染/命中/源锚点同步；`checked` 语义接入。仍缺完整 Flutter typed 矢量 Glyph 路径与绘制器、焦点/危险态所有平台变体与真机画面验收。
2. **长列表惰性实现（源码已完成，设备待验）**：已在显式 `maxVisibleItems` 且根层全部为固定无分组无字幕叶子项时用真正 `LazyColumn` 按需构建，保持 7.5 行半行提示、checked 初始定位、源行坐标、统一 gesture ScrollConflator；待验证大量项目飞快滚动、TalkBack 列表滚动焦点和 60/120Hz 帧耗时。
3. **Popup 容器能力**：`PopupValueText` 已有 300ms 状态过渡的 Compose 组件与 demo 入口（真机时序待验）；仍缺无源/非模态 route 的全面语义、通用 PopupBox source target、`_PopupPin`、系统 windowInsets/键盘/avoid 及屏幕变化。
4. **输入/焦点/辅助功能**：Flutter Gesture Arena 与 Compose pointerInput/scroll 协商的实际运行差异、禁用项和标题焦点语义、不同鼠标按钮/多指取消行为。
5. **视觉与性能真实验收**：根/子阴影、分层高光、Goo 颈部、快开快关、Android 33 曲线与 60/120Hz Jank；不在没有设备证据的情况下打勾。

本文件是后续移植的**基准覆盖清单**，不是“全部等价”证明。未映射完的项目应明确保留为缺口，不因历史计划中的源码已完成标签而自动视为完成。
