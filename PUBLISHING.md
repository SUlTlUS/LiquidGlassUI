# GitHub 源码快照

此仓库首次发布来自 LiquidGlass Sketch 的当前工作区，包含 iOS 26 / iOS 27 机制、宿主演示、Goo 与底栏修正、MeloX 模块、生成资源及来源清单。

首次提交只发布当前源码快照，不包含本地历史提交、APK/AAR、运行日志、系统资源表、设备截图集、Figma decoded.json 大型中间文件和生成的对照预览。历史文档中的本机路径、构建产物和截图链接只作为开发记录，不代表这些文件可从仓库下载。Figma 中间文件可从保留的原稿与解析工具重新生成。

```powershell
git clone --recurse-submodules https://github.com/SUlTlUS/LiquidGlassUI.git
cd LiquidGlassUI
.\gradlew.bat :sample:assembleDebug -x :sample:sendDebugApkToQQ --no-daemon --console=plain
```

配置 Android SDK 后构建。排除 `sendDebugApkToQQ` 可避免本机 QQ 自动发送步骤。当前版本已在原工作区构建并安装，最近两轮底栏修正按用户要求未运行测试；本次发布没有重新运行测试。视觉效果仍由用户验收。详细记录见 docs/ios27/validation.md。

上游采用子模块固定版本；各模块保留原版权与许可，iOS 27 机制的 GPL-3.0 许可见 liquidglass-ios27/LICENSE。
