# 来源与许可

来源目录：`C:\IDE\Android\liquidglass27`，读取日期 2026-11-03。源根不是 Git 仓库，Compose 移植由 [merge-manifest.json](merge-manifest.json) 的逐文件 SHA-256 标识。

参考 FlClash：chen08209 与贡献者，commit `7fd417fb9a9a277c6a9339a8ac87f907df7c25f7`。沿用源项目 GPL-3.0 声明与版权信息，完整许可见 [liquidglass-ios27/LICENSE](../../liquidglass-ios27/LICENSE)，源根许可另存 [source-LICENSE](source-LICENSE)。Sketch 原有来源和许可继续保留。

图标生成输入固定在 `tools/ios27/reference/app_glyphs.dart`，来源为该 FlClash commit 的 `lib/ui/icons/app_glyphs.dart`；生成器只读取仓库内的固定输入，不访问源项目。此文件与生成 Kotlin 均保留原声明和几何数据。

69 个 Kotlin 机制文件、5 个 AGSL、56 个单元测试文件全部纳入清单。初次合并仅增加 API 注解；本轮按用户反馈修改 `GlassPopup.kt` 和 `GlassControls.kt`，并新增标准底栏适配器、等比内容变换及回归测试，均独立记录，原来源哈希不变。26 核心在 `LiquidBottomTabs.kt` 和 `DrawBackdropModifier.kt` 增加可选表面钩子，原行为为默认值，改动前后哈希见 `reviewedLegacyChanges` 与旧库变更清单。根据用户要求，原两个演示文件已删除，`demoFiles` 为空，其原哈希保存在 `removedDemoFiles`。七项组件示例在 Sketch sample 包内编写。未导入源应用资源/配置、Flutter 业务实现、开发缓存或源码树外构建依赖。

底栏截图反馈后的材质层修正同时涉及 `LiquidBottomTabs.kt` 和 `DrawBackdropModifier.kt` 两个核心文件；新参数默认 null，基线与授权改动分别记录。见 [修正记录](dock-popup-correction.md)。

通用 Goo 分组为本项目新增适配：沿用原胶囊、同排及融合量规则，按真实布局注册普通按钮；未修改源 shader 或 iOS 26 核心。来源和修改前哈希保存在 `gooButtonsRevision`。

底栏外圈适配新增 256 射线模式，涉及 `GlassRadialProfile.kt`、`FlClashControlHighlight.kt`、`GlassShaderPrograms.kt` 与 control highlight shader。源基线仍保留，其他控件默认 64 射线。逐文件变更见 `dockOutlineRevision`。
