# 系统设置列表字体适配

`LocalIos27ListTypography` 允许宿主调整列表标题的文字样式与分组标题字重，默认保留框架原有视觉。它不改变行高、内距、按压遮罩或分隔线。

Soundbar 对照实机 `/system_ext/priv-app/Settings/Settings.apk`：`COUIPreferenceTitleStyle` → `TextAppearance.COUI.List.Title` → `couiTextAppearanceHeadline6` 使用 `sans-serif-medium`；分组标题 `couiTextAppearanceSmallButton` 同样使用 `sans-serif-medium`。说明文字 `couiTextAppearanceArticleBody` 继承 `sans-serif` Regular。

宿主通过 `Ios27Tokens.Type.Body.copy(fontWeight = FontWeight.Medium)` 和 `sectionWeight = FontWeight.Medium` 配置列表。`FontFamily.SansSerif` 保留 Android 系统字体解析，使用系统的 Medium 字重，不打包字体文件。字号、行距、字距和系统字体缩放保持宿主原值；页面大标题沿用 Bold，说明与右侧值沿用 Regular。

变更文件：`Ios27ListTypography.kt` 新增宿主配置入口；`Ios27Lists.kt` 在主文字与分组标题读取配置。生成的 `Ios27Tokens.kt` 和玻璃运行源码未修改。
