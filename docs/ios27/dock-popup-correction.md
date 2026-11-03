# 2026-11-03 底栏与 Popup 修正

用户要求：27 底栏使用 26 主体加 27 高光；Popup 圆角与菜单一致，修正动画文字拉伸和高光在动画结束才出现。

## 改动

- 标准 `Ios27NavigationDock` 调用旧库 `LiquidBottomTabsRegular`，保留其宽度、轨道、选中指示器、拖动、回调与光学。`LiquidBottomTabs.kt` 只增加可选表面层钩子；未提供时仍使用原高光及交互效果。27 模式在轨道、隐藏内容层、指示器各自应用独立的 `ControlHighlightRenderer`，使用旧 Capsule 实际轮廓，关闭同层旧高光以免叠加。主导航及七项示例中的底栏均接入此标准入口。
- Popup 的 Sketch 分支复用菜单的 `SketchMenuRootSurface`，使用相同的 24dp 最终圆角、2 → 3.5 连续角形状和 regular → menu 高光过渡。原先直接给整个 morph 使用 menu 材质，缺失动画初段的 control rim；不再维持一套独立实现。
- 内容不再分别以宽、高比例缩放，而是等比缩放后居中到实时 platter，裁切也使用对应的圆角轮廓。源前景快照保持原始比例，避免测量尺寸不同造成字形拉伸。无测量或零尺寸时返回有限且不可见的转换。

## 对照依据

参考源 `C:/IDE/Android/liquidglass27/FlClash/lib/ui/popup.dart`：`_paintOpening` 在约 790–850 行插值 regular/menu 材质并逐步绘制菜单 rim；`_contentTransform` 在约 1124 行使用同一个 scale 缩放 X/Y，围绕目标和内容的中心定位。目标已有菜单的 `SketchMenuRootSurface.kt` 提供相应 Sketch 光学接法，本次直接复用。没有将源项目历史真机结果记作当前版本验收。

所有初始基线保留；`merge-manifest.json` 的 `reviewedLegacyChanges` 记录获授权的旧核心单文件扩展。原 `docs/liquid-glass-extracted/source/` 未更改。

验证结果和当前 APK 见 [验证记录](validation.md)。本轮只进行代码、单测、Lint 和打包检查，真机仍由用户验收。

## 后续：动画结束文字缩小

用户反馈动画结束后按钮文字仍缩小一次。定位到 `GlassPopupSourceContent` 在调用者 padding 内侧录制，得到的前景尺寸小于完整按钮；上一版按前景尺寸适配水滴会将字形放大，回源后切换到真实按钮产生字号跳变。现在在 padding 外侧录制，并单独按“水滴 / 来源按钮”比例缩放前景，前景自身尺寸不参与放大比例。即使调用方只录制紧凑文字或图标，回源末帧也必须是原始大小。新增三项回归覆盖紧凑/完整前景、回源全程与录制顺序；真机视觉仍待用户确认。

## 后续：底栏矩形底影与高光错位

用户截图显示胶囊外矩形底影和不贴合轮廓的高光。上一版将高光 RenderEffect 放进 `drawBackdrop.layerBlock`，包住了外阴影，且该回调还被背景逆变换调用。现在增加默认 null 的 `surfaceLayerBlock`，插在阴影/原高光之后、实际材质之前；底栏三层仅在此处配置新高光。外阴影留在效果外，原 `layerBlock` 只保留原有缩放及逆变换。26 调用不传新参数，行为不变；未修改 shader 或用外层 clip 裁掉阴影。新增回归限制图层顺序与逆变换隔离。截图只作为问题证据，修正版仍待用户真机确认。

## 后续：选中胶囊重复边线

第二张用户截图中矩形底影消失，但上下重复边线仍在。`Content` 隐藏参考层被 `CombinedBackdrop` 采入选中胶囊，而 27 适配器错误地给它套用了完整 pill 高光。现在该层不设置高光效果；可见轨道与指示器继续各自绘制。`GlassStyle.materialize` 未缩放 `hairlineSide`，因此在此适配器中单独乘按压进度，静止时不残留细边线。

本轮按用户要求仅构建并自动安装，不运行测试、不启动应用；不将上一版测试通过计为本轮验证。

## 后续：外圈不均匀

底栏采用 Kyant Continuous Capsule 原轮廓。现有 64 射线加三次插值会近似重建轮廓，本轮为底栏启用 256 射线，并将路径离散段数提高至 1024–4096；普通控件默认精度和明暗公式保留。shader 在编译前用常量专门化，数组容量、角度周期和导数倍率一起更新。未改主体尺寸、颜色或圆角。按用户要求只构建和安装，不执行测试；视觉及运行时 shader 验收由用户完成。
