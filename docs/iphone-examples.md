# iPhone Examples：实现与验收状态

**1.0.19 更新：默认入口已经改为可操作的框架页面，矢量原稿仅作“原稿对照”。下文主要记录 1.0.18 的矢量实现与验收边界；当前接入方式和测试见 [可操作框架示例](native-framework-examples.md)。**

## 范围和入口

来源是用户提供的 `D:/下载/iOS and iPadOS 27 (Community).fig`，不是早期的单页 `Untitled.fig`。

- 解码输入：`docs/figma-community-source/decoded.json`。
- 原稿 iPhone section：`12740:23995`。只提取此区域，不包含 iPad。
- 56 个变体，原始画布均为 402 × 874；41 张引用图片。
- canvas.fig SHA-256：`b2d3bbd6242c273bf5f5d87fb87de2e91bd16d18bd25f39a4152663fed6bd53e`。
- 应用入口：底部“组件”页直接承载手机示例库 → 分类 → 变体；不再经过旧组件演示页或额外跳转入口。
- 系统返回键返回上一级；长按页面打开同类变体列表。通知支持展开/收起切换，部分键盘模式支持通过对应键切换。Face ID 仅模拟认证中到成功的画面，不请求生物识别权限，也不代表实际认证成功。

## 代码位置

- `tools/figma-reader/export-examples.mjs`：展开原稿实例，应用覆盖和组件属性，解析颜色变量，导出原始矢量、字形、图片引用和布局约束。
- `sample/src/main/assets/iphone-examples/catalog.json`：完整变体目录。
- 同目录 `<key>.json`：每页绘制数据；`images/`：只复制实际引用的原图。
- `sample/src/main/java/dev/liquidglass/sample/IphoneExamples.kt`：目录、页面层级和示例路由。
- `sample/src/main/java/dev/liquidglass/sample/FigmaSceneView.kt`：离线原生 Canvas 渲染。没有把整页截图作为页面。

本次没有修改 `com/zeaze/tianyinwallpaper/` 下的玻璃运行源码，也没有刷新原始提取基线。按 Windows Kotlin 安全编辑规范，仅对宿主文件做局部补丁。

## 已修复的导出/显示问题

- 字形坐标为 Y-up，绘制时必须翻转 Y 轴；直接按屏幕坐标绘制会把文字上下颠倒。
- 数字键盘的缓存填充为白色，但 Light 模式 Labels/Primary 变量为黑色，必须优先解析变量。
- Figma 蒙版影响后续兄弟节点，不能把蒙版本身当作可见背景。标签栏的整块蓝色错误由此修复。
- 图片填充文字需要按字形轮廓裁切，不能直接画成矩形图片。
- 布尔合并节点有最终路径时，不重复绘制子操作数，避免填满孔洞和叠加边缘。
- 部分节点四个圆角不同，不能只读取统一 cornerRadius。
- 设计稿手机 Screen 的外框圆角不套用到 Android 应用窗口；固定行高和字形不随屏幕高度拉伸，底部和居中元素按原稿约束适配高度。
- 分组首行顶部分隔线由宿主过滤，原始导出数据仍保留。
- 彩色 emoji 的 .fig 字形是单色占位轮廓。当前使用 Android 彩色字形兜底；这不是 iOS emoji 的像素级复刻。

## 验证

```powershell
node tools/figma-reader/export-examples.mjs
node --test tools/figma-reader/examples.test.mjs
node tools/verify-extraction.mjs
node tools/generate-ios27-tokens.mjs --check
.\gradlew.bat :verifyLiquidGlassSpec :liquidglass:testDebugUnitTest :sample:assembleDebug --no-daemon --console=plain
```

安装 APK 后运行 `node tools/figma-reader/check-android-examples.mjs <设备序列号>`。脚本逐页冷启动，等待对应节点的实际绘制标记再截屏；结果在 `docs/iphone-examples-android/`，`launch-report.json` 是启动/绘制记录，`verification.json` 记录 APK 哈希，`contact-*.png` 是截图总览。此脚本不能替代视觉差异检查或交互测试。

`node tools/figma-reader/check-example-navigation.mjs <设备序列号>` 单独检查通知展开、返回目录、长按变体菜单以及模拟 Face ID 的路由。不要与截图脚本同时运行，避免两个脚本操作同一台设备。

调试包允许通过 MainActivity 的 `example_key` 字符串参数直接进入原稿变体；发行包忽略该参数。

### 1.0.18 本轮检查结果（2026-09-14）

- 新增导出回归测试：5/5 通过；玻璃提取校验、token 校验、项目规范校验、现有单元测试及 APK/AAR 类定义校验通过。
- `emulator-5556`：56/56 页面实际绘制并截图完成；5/5 导航检查通过；检查时 crash 日志缓冲区为空。
- APK：`sample/build/outputs/apk/debug/sample-debug.apk`，versionCode 19，versionName 1.0.18。
- APK SHA-256：`10947a40ee6af911a260d438452e0dd44b7a5f1c50e134bd49ff9171f9f6ec87`。
- 以上是构建、绘制及导航冒烟验证，不是原稿像素差异或 ColorOS 真机验收。

## 尚未完成的验收项

当前版本是完整目录和原稿绘制数据的可运行实现，**不是全部 56 页已经完成像素级复刻的结论**。

- 导出报告仍有 889 条未匹配的实例覆盖路径，缺失 symbol 为 0。需要逐项区分历史失效路径和仍影响外观的覆盖，不能直接删除报告。
- Figma 的 GLASS、背景/渐进模糊、内阴影和部分混合模式尚未完整实现。当前示例中的矢量玻璃外观不能替代现有 LiquidGlass 真实效果。
- 全部控件的交互尚未接齐，包括这些原稿场景内的滑块拖动、开关、步进器、编辑输入、菜单动作、日期滚轮。旧组件演示中的真实组件仍保留，不能用它们的测试结果冒充新场景已经可交互。
- 原稿默认颜色已解析；跟随应用浅/深色切换的全套场景还未验收。
- 原稿实例缩放、裁切变换、部分 Face ID / Action Sheet 的布局仍需核对。不要凭常识把它们改成另一套设计。
- 验证设备目前是 Android 模拟器，不是用户的 ColorOS 真机。构建、56 次启动和截图不代表真机玻璃效果验收完成。
- `docs/iphone-examples-previews/` 由简化 SVG 生成，缺少完整效果，不能作为像素比较的原稿截图。

后续优先完成真实玻璃组件接入和背景采样分层，再处理剩余布局差异、主题切换和控件状态；不要通过重写受保护组件或覆盖原始基线来解决宿主问题。
