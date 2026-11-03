# 2026-09-12 启动闪退与产物校验

设备为 PLQ110，Android API 36。应用包名 `dev.liquidglass.sample`。

设备 crash buffer 中应用在 14:57、15:03 和 21:56 的启动异常为：

```text
FATAL EXCEPTION: main
Process: dev.liquidglass.sample
java.lang.NoClassDefFoundError:
  Failed resolution of: Ldev/liquidglass/ios27/Ios27PageThemeKt;
Caused by: java.lang.ClassNotFoundException:
  Didn't find class "dev.liquidglass.ios27.Ios27PageThemeKt"
```

这不是证明玻璃递归的堆栈。之前通过 BUILD SUCCESSFUL 宣称闪退已经修复不充分，换为 CanvasBackdrop 的说明也不构成根因验证。

本地同时发现旧 AAR 仅 688 字节，只含 manifest、R.txt 和 aar metadata，没有 classes.jar。Kotlin 编译目录中存在主题类，但发布包并不完整。对 sample 和 library 做完整重建后，APK 的 DEX class_defs 中已包含主题、列表、玻璃控件及渲染实现。AAR 的 syncDebugLibJars 仍曾错误跳过；通过 `--rerun-tasks` 重新执行该打包任务才生成 classes.jar。没有改动原提取的 47 个 Kotlin 文件。

后续构建采用以下检查：

- `:sample:assembleDebug` 完成后自动运行 `:verifyDebugArtifacts`。
- 校验 AAR 内的 classes.jar，确认包含页面主题、列表与玻璃控件类。
- 解析 APK DEX 的 class_defs 表，确认实际类定义存在。仅搜索字符串会把未实现的引用误认为存在，不能用于此检查。
- 校验失败会令构建命令失败，即使编译和打包任务本身返回成功。

需要恢复不一致的中间产物时，在没有其他本项目构建同时运行的情况下执行：

```powershell
./gradlew.bat :sample:clean :liquidglass:clean :sample:assembleDebug --rerun-tasks --no-build-cache --no-daemon
./tools/verify-android-artifacts.ps1
node tools/verify-extraction.mjs
```

版本提升到 1.0.1（versionCode 2），便于确认覆盖安装的是新包。设备启动和界面交互是独立检查项，不能用 APK 或 AAR 编译成功代替。
