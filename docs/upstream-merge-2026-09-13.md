# 上游融合记录（1.0.9）

## 对比基准

仓库：https://github.com/Kyant0/AndroidLiquidGlass 。本次完整克隆保存在 `vendor/AndroidLiquidGlass`，默认分支 `kmp`，实际 HEAD 为 `65ab177e90e5c1d8c62e70cf7755841982da65f6`，tag 2.0.1。与旧同步文档记录相同，没有比该基准更新的提交；但本地提取代码仍漏掉了一项已有修复。

## 实际合入

上游提交 `7c37be469afe478071f5107b8613158ca3b8b43d`（Fix LayoutCoordinates reference leaks in #101）：

- `backdrop/backdrops/LayerBackdropModifier.kt`：替换 backdrop 时先清空旧对象的 layerCoordinates；对象没变则不清空。
- `backdrop/DrawBackdropModifier.kt`：替换 exportedBackdrop 时先清空旧导出对象的 layerCoordinates，并保留空值兼容。

仅合入以上两个原始补丁逻辑，不更换包名、不整文件覆盖。效果是避免已被替换的背景对象继续持有旧布局坐标引用；它不是此前自我采样递归闪退的替代修复，原先的兄弟层采样结构仍需保留。

## 保留的本地代码

- Slider：外部主题、rememberUpdatedState 回调、整轨拖动、完成通知和零宽保护。
- Toggle：外部主题、触觉反馈和拖动开始/结束回调。
- Button / BottomTabs：样式参数、亮度自适应、低版本降级、选中状态同步与边界保护。
- Android 专用 RuntimeShader / RenderEffect 路径及相关版本判断；不直接套用上游 KMP expect/actual 拆分。
- 页面颜色用途划分、分隔线位置、顶部渐进模糊及底栏背景连接。

上游 Slider 仍使用一层 4dp、5% 黑色阴影，本地也相同；没有将苹果 Figma 的双层阴影伪装成上游更新。此处按用户要求不改。

## 依赖处理

保留当前 backdrop 2.0.1 / shapes 1.2.1。上游构建使用 AGP 9.3.2、Kotlin 2.4.10 和 Compose Multiplatform 1.12.0，属于其 KMP 工程的构建配置，不作为本 Android 工程必须整体照搬的运行时修复。本次不升级整套工具链。

## 基线和校验

`docs/liquid-glass-extracted/source` 仍是原始 48 文件快照，未被更新覆盖。合入的两个文件及其前后 SHA256 记录在 `docs/liquid-glass-merged-files.json`。`node tools/verify-extraction.mjs` 同时验证旧基线和已审阅合并文件，其余 46 个文件必须保持逐字节一致。不要仅为了让校验通过而刷新哈希。

复查命令：

```powershell
node tools/verify-extraction.mjs
node tools/generate-ios27-tokens.mjs --check
.\gradlew.bat :liquidglass:testDebugUnitTest :sample:assembleDebug --no-daemon --console=plain
```

设备验收仍需连接手机，检查背景切换、Slider 左右拖动释放、底栏透视、顶部模糊和崩溃日志；构建通过不代表这些真机行为已验证。
