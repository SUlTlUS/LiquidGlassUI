# 听音模式验证

2026-10-08 新增并完成 Android 行为适配。只修改听音模式组件、单元测试、示例页与目录入口；没有修改旧玻璃运行源码或 Toolbars - Top。按用户澄清，示例为单实例，跟随应用主题；图片中的深浅色仅为配色参考。此次补充 48dp 触区下限、标题/单选语义、键盘焦点与 RTL 分隔线适配，保留正常触屏的原有样式参数。

- `:verifyLiquidGlassSpec :liquidglass:testDebugUnitTest :sample:assembleDebug` 通过；新组件 5 项单元测试通过，覆盖正常宽度不变、窄容器下限和 RTL 分隔线位置。
- `node --test tools/listening-mode.test.mjs tools/melox-tabbar-extraction.test.mjs`：16 项通过，包含修改前配色及图标函数 SHA-256 回归。
- token 生成检查通过。
- 旧框架静态套件仍为相同的 17/31 失败，没有新增失败。原有 ProgressiveBlurContent 提取哈希登记失败未修复或掩盖。
- Android 行为适配版本已重新打包。本次 `adb devices -l` 无在线设备，重新连接最近的 `192.168.1.148:44647` 超时，最新 APK 未安装。此前已安装的单实例版本不包含本次行为适配。用户明确表示由其验证，本次没有自动点击或变更主题设置；键盘、TalkBack、窄容器、RTL 及浅深色视觉验收待用户确认，不宣称截图逐像素一致。

APK SHA-256：`0AF03A41F506539760E3A7CA627032871E81E05497F7EF978362B730E21ECE1D`。

若用户后续要求自动验收，先安装最新 APK，重新确认在线设备序列号，解锁后运行 `node tools/check-listening-mode-device.mjs <在线设备序列号>`。脚本验证单实例、图标/标签点击、实际回调结果、目录入口和本次进程崩溃日志；报告与截图输出在本目录。键盘焦点、TalkBack 重复朗读与极窄容器需要另行进行实际 UI 验收；当前脚本不覆盖这些项目。两种主题可分别在应用设置切换后验证，不并列显示。参考图形为截图估算的新矢量，未宣称原稿像素相等。
