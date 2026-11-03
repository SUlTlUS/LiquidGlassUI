# MeloX TabBar 验证记录

源码基线：`723aa764462386440b76aed5df362dadf63a096f`。验证设备为本地 `emulator-5556`（API 36.1，1080×2400），没有使用或连接旧真机地址。保留设备已有字体缩放设置，未修改系统字体或字体比例。

## 已验证

- `node tools/extract-melox-tabbar.mjs --check`：原文件、提取文件、字体和矢量 SHA-256 与固定版本一致。
- `node --test tools/melox-tabbar-extraction.test.mjs`：11 项通过，包括 chrome 主体、三层材质、拖动/高光/明暗采样原文一致、独立示例路由与安全采样、MiniPlayer 逐字对照、原版 master timeline/共享封面/释放回弹、18px 滚动连接、播放按钮弹簧和 FlowingLight 服务注入边界。
- `:melox-tabbar:testDebugUnitTest`：9 项通过，检查主标签、搜索独立及错误入参、媒体队列与回调适配。
- `:verifyLiquidGlassSpec`、`:liquidglass:testDebugUnitTest`、`:melox-tabbar:assembleDebug`、`:sample:assembleDebug`：通过。`:verifyDebugArtifacts` 检查现有框架与 APK 类定义通过。
- 独立演示 `:melox-tabbar-demo:assembleDebug`：通过。
- `node tools/generate-ios27-tokens.mjs --check`：通过。
- 最新安装包在本地模拟器安装成功；实际标签选择、搜索往返、水珠拖动、播放器、收缩与点击展开、毛玻璃切换、滚动、目录入口共 23 个检查通过，包含从根目录点击实际可见的入口。详见 `interaction-report.json` 与各阶段 XML、PNG。应用 PID 存活，本次进程日志未发现 `FATAL EXCEPTION` / `Fatal signal`。

## 旧校验未通过的部分

`node tools/verify-extraction.mjs` 在原有 `ui/commom/ProgressiveBlurContent.kt` 的哈希登记上失败。这是开始本任务时就已经存在的修改文件，本任务未编辑它，也未更新旧清单去掩盖问题。

`tools/figma-reader/native-framework.test.mjs` 当前 31 项中有 17 项失败。`node tools/check-melox-host-regression.mjs` 将本任务的目录路由增量只在读取时内存反向替换，再运行同一旧套件；结果仍是相同 17 项失败。本任务没有增加该套件的失败项目，也没有恢复、重写这些旧页面以迁就静态检查。

该旧套件中目录计数的断言做了范围内的更新：允许旧 Figma-only 目录计数，或从总条目排除新增源码示例的计数；新增 MeloX 套件严格检查新目录不能把源码示例算进原稿数。其余旧断言未修改。

## 不代表已经完成的验收

没有安装原 MeloX APK 在同一设备、背景、字体、密度和系统版本上做逐像素对照；不能宣称 APK 视觉像素相等。源码材质一致及本地交互通过，与原 APK 的像素比较是不同证据。

sample 依赖解析使用已有 Backdrop 2.0.1 / Shapes 1.2.1；独立模块声明原版 2.0.0 / 1.2.0。字体和品牌资源的正式分发授权未独立确认，见 `../melox-tabbar.md`。

复跑交互（先安装最新 APK，再执行）：

```powershell
node tools/check-melox-device.mjs emulator-5556 C:/Users/29373/AppData/Local/Android/Sdk/platform-tools/adb.exe
```

脚本会重新启动 debug 的 `example_key=melox-tabbar` 路由；截图与报告都是生成验证输出，不是 Figma 原图或原 MeloX 截图。

## 2026-10-03 动画宿主遗漏修复

上一轮只接了原 MiniPlayer 外观，点标题仍是文字提示，且没有原应用宿主的滚动收缩连接。这不等于完整搬入动画。当前提取清单有 66 个输出，补入原应用 master timeline、共享转场目的层、拖拽 seek/settle、原版 Artwork 辅助内容、播放按钮弹簧、FlowingLight 与窗口/inset 布局 helper。

`miniplayer-expanded.png`、`miniplayer-playing.png`、`miniplayer-next.png` 与 XML 记录展开外观及实际 UI 状态变化；`player-expanded.png` 是实际 Artwork 目的页面，`player-back-to-mini.png` 与 `player-drag-dismissed.png` 是反向返回结果。使用上游应用图标作演示封面，不是参考歌曲的专辑图；没有真实音频播放。固定上游的音频指示条也不同于参考截图中的省略号。

23 项检查包含实际共享展开、系统返回反向转场、短拖回弹、长拖收起、向上滚动自动收缩、向下滚动自动展开。`compact.png` 记录收缩播放器与左右按钮同排。冷启动 UIAutomator 会出现空 root，需要有限重试；最终新鲜界面树和本次进程日志为准。旧框架静态套件仍是相同的 17/31 失败，提取旧哈希检查仍需独立处理。

采样服务默认值随后修正为报告 UI 的 isPlaying，避免播放状态已变但背景始终得到 Idle(false)；没有伪造节拍分析。封面共享缩放的宿主状态恢复为原版先 false、Artwork 目的层建立后 true。独立演示的进度/音量回调也接入可保存状态。这些是适配/宿主修复，不修改原版 renderer 的几何或动画曲线。最新安装包已复跑 23 项检查；因冷启动 UIAutomator 多次空 root，其中一次前台被其他应用切走，最终使用已确认前台的 warm 模式通过，没有把抓取失败当成视觉通过。

`animation.mp4` 是 540×1200 的实际模拟器录屏，记录展开、系统返回、短拖回弹、长拖收起和滚动收缩。`animation-report.json` 记录事件时间与图像测量；长拖的共享封面/壳过程识别出 3 种不同的中间标记尺寸。`flight-intermediate.png` 与 `drag-intermediate.png` 都来自长拖阶段，不是自动展开的中间帧。12fps 导出可能重复录制帧，7 张命中 PNG 不等于 7 个独立源帧。红色连通区域仅用作演示封面移动/缩小的近似标记，不是共享元素精确边界。

模拟器原始录屏约 5.6fps，不能据此宣称自动 360ms 转场平滑性或性能达到原 APK。当前能确认原版调度/曲线源码一致、最终交互结果正确，以及实际拖动中间状态可见；原 APK 同设备运动像素及帧时序比较仍未完成。

独立复跑录屏（先执行上面的设备回归，将页面留在默认入口）：

```powershell
node tools/check-melox-animation.mjs emulator-5556 D:/Application/ffmpeg-master-latest-win64-gpl-shared/bin/ffmpeg.exe
```

录屏与中间帧测量只证明动画连续运行，不是帧时序性能基准，也不是与原 APK 同设备逐像素相等。当前只提取 Artwork 动画目的层，未提取歌词、队列、音质、自动混音或音乐服务业务。

本次新 APK SHA-256：`7C7A7B82C82802B0BF924445E747E71CE8F2A5525DBED2624318639565BA0FA1`。
AAR SHA-256：`B7DBA62CF4D7876E4F85FB4420C6E43387FD958036D3604510F54CAB996DEFC1`。
