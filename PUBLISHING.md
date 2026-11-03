# GitHub 源码分发

此仓库发布 LiquidGlass UI 当前源码、示例、生成资源与来源清单，保留远端历史。公开 API 和构建方式见 README.md。

分发排除本地构建产物、运行日志、临时目录、系统资源表、Figma decoded.json 和生成对照预览。历史开发文档中引用的本机产物可能未随源码发布。

```powershell
git clone --recurse-submodules https://github.com/SUlTlUS/LiquidGlassUI.git
cd LiquidGlassUI
.\gradlew.bat :sample:assembleDebug --no-daemon --console=plain
.\gradlew.bat packageLibrary --no-daemon --console=plain
```

配置 JDK 21 和 Android SDK 37 后构建，最低支持 Android 13。普通构建不自动发送文件。此次同步未运行构建、测试、Lint 或设备交互；构建和视觉验收情况以对应开发记录为准。

上游通过子模块固定版本，各模块保留来源与许可证。
