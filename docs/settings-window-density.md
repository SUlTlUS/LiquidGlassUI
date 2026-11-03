# 设置页：系统密度与窗口短边缩放

此页记录用户授权的设置页宿主适配，不替换原有 Figma/iOS 设计规范。其他示例、说明页、底栏和玻璃源码均保持现有密度策略。

入口：`sample/src/main/java/dev/liquidglass/sample/MainActivity.kt`。在系统 `LocalDensity` 下、应用安全区内距之前的 `BoxWithConstraints` 中读取当前窗口 `maxWidth/maxHeight`（系统 dp），调用框架的 `ios27WindowDensity`：

```kotlin
val shortSide = minOf(widthDp, heightDp)
val scale = (shortSide / 402f).coerceIn(0.85f, 1f)
val density = Density(systemDensity.density * scale, systemDensity.fontScale)
```

仅 `page == 2` 的设置内容由 `Ios27AndroidDesignBehavior(settingsDensity)` 包裹：dp 随窗口缩放，sp 保留系统字体比例；框架按密度比换算不可见的最小触摸目标。不要把 `fontScale` 写成 1，也不要给可见按钮增加 padding 来代替触摸策略。

- 363.408dp 短边得到 0.904；这是公式示例，不是本次真机测量。
- 402dp 及以上短边使用 1，平板复用 `Ios27PageContent` 的 `ReadableWidth`（672dp）上限。
- 横竖屏互换宽高保持相同比例；分屏随实际约束重新计算，最小 0.85。
- 不根据物理屏幕像素、屏幕型号、可滚动内容高度或已缩放的 dp 再次计算比例。
- 系统安全区与底栏仍是系统密度，设置内容中的顶部和 108dp 底部预留先换算为物理像素，再换算为内容 dp，避免被缩小后遮挡。
- 背景录制层和玻璃浮层继续保持独立兄弟关系；颜色、形状、阴影、玻璃参数、动画和原稿资源未改。

校验：`Ios27WindowDensityTest` 覆盖手机、平板、旋转、分屏、下限和系统字体比例；`node --test tools/settings-window-density.test.mjs` 检查系统约束计算顺序、仅设置页启用、物理安全区预留与内容宽度上限。构建或静态检查不等于设备视觉验收。

## 本次变更与验证（2026-10-10）

变更仅含宿主 `MainActivity.kt`、新增框架 `Ios27WindowDensity.kt`、对应 6 个 JVM 单测、4 个静态检查及本文档。UTF-8 安全编辑；原有规范说明、tokens、示例绘制和玻璃源码未修改。

- `:verifyLiquidGlassSpec :liquidglass:testDebugUnitTest :sample:assembleDebug` 成功；38 个 JVM 测试零失败。
- 专项静态检查 30 项中 29 项通过；未通过的是既有冻结文档哈希：`docs/native-framework-examples.md` 与 `docs/liquid-glass-merged-files.json`。这两个文件本次未改，冻结的 visual 绘制项与源码/资源树均匹配。未刷新基线。
- tokens 校验通过；`verify-extraction.mjs` 仍因既有 `ui/commom/ProgressiveBlurContent.kt` 哈希问题失败，未改源码或登记哈希掩盖此问题。
- APK：`sample/build/outputs/apk/debug/sample-debug.apk`，SHA-256 `78D8E5137157D50A21EDAB90DEB984BF1A9E72CCA8897B4E2DEE0C7FEE726712`。
- 未自动安装、操作真机或更改系统字体/密度；手机、横屏、分屏及大字体实际显示由用户验收。
