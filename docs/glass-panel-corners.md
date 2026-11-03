# 共用玻璃面板圆角绘制修复

日期：2026-10-17。用户报告颜色选择器等多个模态面板两侧圆角边缘异常，随后确认允许仅修改 `Shaders.kt` 与 `highlight/HighlightModifier.kt` 两个受保护的运行源码文件。

## 原因与修改

1. `HighlightModifier.configurePaint` 原先把 `shapeProvider.shape` 传给高光着色器。这是用于缓存轮廓的匿名 `Shape`，不是 `CornerBasedShape`；`HighlightStyle.getCornerRadii` 因此进入非圆角类型的回退分支，把各角按 `min(width, height) / 2` 的胶囊半径计算。现在只把着色器参数改为 `shapeProvider.innerShape`，保留真实圆角类型。轮廓裁切仍使用原缓存包装器，没有另建裁切路径。
2. `Shaders.kt` 中两种折射与两种高光着色器原先调用 `radiusAt(coord, cornerRadii)`，但 `radiusAt` 是按坐标正负区分左上/右上/右下/左下的函数。局部画布坐标通常为正，导致大部分位置误选右下角半径。四处调用改用已经计算好的 `centeredCoord`；折射的该坐标还包含录制层 padding offset。

这两处都是共用绘制逻辑，不是 Figma 圆角尺寸错误。底部面板顶部 32dp、设备推导的底部圆角、弹窗 34dp 等调用值均未修改。模糊、24/48dp 折射、高光宽度/alpha/blur、表面颜色、阴影、按压和背景采样层级也没有改动。

## 适用范围

- Color Picker、Fullscreen/Inspector 等复用 `Ios27BottomSheetDialog → LiquidDialogLayer` 的玻璃面板。
- 提示弹窗、操作面板、菜单及其他使用相同 `drawBackdrop` 高光/折射实现的控件自动使用修复，不需要逐个页面改参数。
- 纯色分享面板、无玻璃的堆叠页不改；ColorOS 17 设置原组件使用外包实现，不受这两份本地着色器影响。
- 修复共用高光会使同引擎按钮等控件使用真实圆角法线，这是预期修正；不是保证这些控件的亮暗分布与旧错误输出逐像素相同。
- 这里保留现有 `CornerBasedShape` 及其他形状的支持边界，不声称补齐了所有第三方自定义 Shape 的圆角解析。

后续不得为了补偿此问题而在单独页面关闭高光、改胶囊半径、降低折射强度或加假描边。保持 `drawBackdrop` 在内容裁切之前，保留外部阴影。原始提取目录未改，baseline/merged SHA-256 已记录在 `docs/liquid-glass-merged-files.json`。

## 回归验证

```powershell
node --test tools/glass-panel-corners.test.mjs
node tools/verify-extraction.mjs
node tools/generate-ios27-tokens.mjs --check
$env:QQ_APK_AUTO_SEND='0'
.\gradlew.bat :verifyLiquidGlassSpec :liquidglass:testDebugUnitTest :sample:assembleDebug --no-daemon --console=plain
```

专项 Node 测试执行源码中的 `radiusAt` 函数，核对四角不同半径、32dp 顶角配不同底角、padding offset 和等半径情况；另对照原始文件确认运行源码只有预期五处替换。修复前 1/7 通过、6/7 失败，修复后 7/7 通过。

JVM 测试 `GlassPanelCornersTest` 检查真实 ShapeProvider 的类型传递、不同密度下的四个原始圆角，以及编译后的四份着色器字符串。上述数学与源码检查不是 GPU 图像验收。

真机沿用用户自行验证的选择，不自动连接、安装或操作。应在浅/深色、有文字或图片的对应背景下检查四角高光/折射是否连续、外阴影是否保留，打开/关闭和旋转后有无残留；最后检查 crash 日志。不能把构建成功或本地数学测试通过等同于截图异常已经在目标设备消失。

### 本轮结果

- 圆角专项 Node 7/7、JVM 3/3 通过；ColorOS 原组件和顶部模糊专项静态检查合计 10/10 通过。
- 提取基线校验通过：41 个运行文件与原始提取相同，7 个已登记修改的运行文件匹配保留的 baseline/merged 哈希；原提取目录未改。
- tokens、玻璃规范、库单测、sample debug 构建与 AAR/APK 定义检查通过。
- 全示例静态回归仍为既有 15 通过 / 17 失败，未改无关断言。
- APK：`sample/build/outputs/apk/debug/sample-debug.apk`；SHA-256 `5C627EEF90837D4385A62CB3A28C2021C4B9F3B763496CD9130FC59CF2E29784`。未发送 QQ、未安装或操作设备；目标设备视觉验收未完成。
