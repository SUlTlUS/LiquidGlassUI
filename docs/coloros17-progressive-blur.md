# ColorOS 17 顶部渐进模糊调用与复用

## 已接入的入口

继续调用 `com.zeaze.tianyinwallpaper.ui.commom.ProgressiveBlurContent`，不用每个页面检测 ROM。库在运行时选择系统实现；系统接口不可访问时回退到原 Compose 模糊算法。框架宿主现在根据顶部实际布局给出覆盖范围，工具栏仍保持在模糊层上面；未提供顶部高度的旧调用保留 128dp 兼容值。

```kotlin
ProgressiveBlurContent(
    modifier = Modifier.align(Alignment.TopCenter).fillMaxWidth(),
    backdrop = pageBackdrop,
    isLightTheme = isLight,
    strength = 1f, // 模糊半径强度；普通滚动不要修改它
    visibility = scrollBlurAlpha, // 0..1，单独淡入淡出，不在外面套 Modifier.alpha
    surfaceColor = pageBackground, // 框架也会通过 CompositionLocal 自动传入实际底色
    appBarHeight = topInset + toolbarHeight, // 当前页面真实顶部区域，不含模糊的延伸段
    scrollDriven = true, // 滚动模式延伸 24dp；固定模式延伸 8dp
)
```

现有不传 `strength` / `visibility` 的调用都保持默认 1。不传 `surfaceColor` 时先读取 `LocalProgressiveBlurSurface`，没有宿主提供时才使用浅白/深灰兼容值。`surfaceColor` 用于原生渐变和无 backdrop 时的回退颜色；它不是采样源，不得用不透明背景替代系统背景模糊。

共享 `ExampleTopChrome` 将 `blurAlpha` 传给 `visibility`，不再把滚动显现绑定到半径强度。系统路径使用独立 View 的 `alpha`；Compose 路径使用 `strength * visibility` 的透明度。显式设置 `strength` 仍支持半径缩放与背景渐变强度缩放，与 APK 的 `updateGradientBlurFraction` 路径对应。普通工具栏、按钮、底栏和其他玻璃渲染器没有替换。

### 自动传递，防止新页面再用错

- `ExampleDesignPage(background = ...)` 和 `ExampleRecordedResponsivePage(background = ...)` 将实际背景注入 `LocalProgressiveBlurSurface`，覆盖 grouped 灰底、ungrouped 白底和深色黑底。它们的内容和顶部 overlay 仍是兄弟采样关系。
- `ExampleTopChrome` 自动读取该颜色，并根据安全区高度和当前工具栏的 48dp 布局高度计算顶部区域。**48dp 是本项目既有 `ExampleNavBar` 的高度，不是 ColorOS 官方高度。** 没有工具栏的根页面仅计算顶部安全区；手工放置返回按钮的宿主需显式给出 `appBarHeight`。
- 听音模式和 MeloX 的手工返回按钮页面已明确传入 `topInset + 48.dp`；MeloX 顶部底色使用实际黑色背景，不再依赖白色默认值。
- 根组件页、说明/设置页和普通滚动示例使用 30dp 的显现阈值，并标记 `scrollDriven=true`。其他非滚动顶部使用固定模式。涉及特殊大标题折叠的页面，仍须由宿主传入当前可见的 AppBar 范围，不应把整段正文标题当作固定工具栏。

## 来源与不能冒充的部分

来源为用户提供的 `D:/下载/设置_17.0.0.apk`：

- APK SHA-256：`E285088674907EEDEF89B4BA9862165DD070699CBA4303C4B05B969F99C4261D`。
- 原样反编译调用代码保存在 `docs/progressive-blur-extracted/settings-17.0.0/sources/`。
- 重点查看 `com/coui/appcompat/toolbar/AppBarBlurHelper.java`、`AppBarBlurParamsExtKt.java`、`GradientBackgroundDrawable.java` 和 `com/coui/appcompat/COUIMaterialEffect.java`、`COUIEffectVersionUtil.java`、`COUIMaterialStateManager.java`。
- 改动前当前项目的 Compose 实现保存在 `docs/progressive-blur-extracted/current-project/source/`；原始提取基线 `docs/liquid-glass-extracted/source/` 不变。

新的运行桥接是本项目适配代码，**不是 APK 的原始 Kotlin 源码**。APK 的反编译 Java 原样保留，未直接加入编译。Oplus 两个渲染类的实现不在此 APK 中，由设备固件提供；不复制 Settings APK、不使用它的签名，也不把厂商 SDK 桩打包成假渲染器。

## 系统接口的实际调用

从 APK 提取的核心调用等价于：

```java
RenderEffect effect = OplusRenderEffect.createGradientBlurEffect(
    30.0f * strength, 0.0f * strength, true, 3.0f, 0, 0, 0
);
OplusViewBackgroundRenderEffect.setBackgroundRenderEffect(effect, overlayView);
// 页面销毁或效果停用：
OplusViewBackgroundRenderEffect.setBackgroundRenderEffect(null, overlayView);
```

这两个类不属于公开 Android SDK。本库使用 `Class.forName` / `getMethod` / `Method.invoke` 获取同名静态方法，**不调用 hidden-API 豁免、不使用 root、不修改系统设置**。被系统访问限制拦截、缺少类/方法、调用异常时移除系统层并自动回退。

| 参数 | 来源与含义 |
| --- | --- |
| 起始半径 30 / 结束半径 0 / 速度 3 | `AppBarBlurParamsExtKt` 的原始数值，不擅自换算成 dp |
| 第三个参数 `true` | 保留 APK 的取值，不猜测未公开的方向语义 |
| 三个混色整数 0 | `GradientBlurConfig` 的默认值 |
| 浅色位置 `[0, .4, 1]` / alpha `[0, .7, 1]` | APK 背景覆盖层，坐标从底部指向顶部 |
| 深色位置 `[0, 1]` / alpha `[0, .4]` | APK 深色覆盖层，同样从底部指向顶部 |

桥接使用 `GradientDrawable` 顶部向下的坐标，因此同时反转 alpha 并将位置变为 `1 - position`；不能只反转颜色而不反转位置。APK 的 `updateGradientBlurFraction` 使用 `1 - fraction`，本项目 `strength` 直接表示效果强度，不要再次取反。

### 官方覆盖层几何与滚动参数

来自同一 APK，而不是根据截图猜测：

| 项目 | 源码证据 | 当前适配 |
| --- | --- | --- |
| 覆盖层上边界 | `AppBarBlurOverlayBehavior.updateOverlayBounds`: `appBar.getTop()` | 顶部容器的原点；本框架的 AppBar 从页面顶部开始 |
| 覆盖层下边界 | `appBar.getBottom() + overlay.getExtraHeightPx()` | `appBarHeight + extraHeight`，不再把渐变铺到固定 128dp |
| 固定模式延伸 | `AppBarBlurOverlayView`: 8dp | 8dp |
| 滚动模式延伸 | `AppBarBlurOverlayView`: 24dp | 24dp |
| 滚动透明度阈值 | `AppBarBlurOverlayBehavior.initThresholdPx`: 30dp | 宿主转换成 px 后用于滚动距离计算 |
| 显现方法 | `applyOverlayBlurProperties`: `overlay.setAlpha(...)` | 原生 View 的 `alpha = visibility`，不是修改模糊核 |

`appBarHeight`、延伸和阈值使用各自调用作用域的 dp 密度：系统密度页为系统 dp；原稿坐标页为既有设计密度。API 的 30/0/3 仍保持原始数值，不乘 dp 密度。这里复用的是几何规则，不宣称全套原稿页面拥有与 ColorOS Settings 相同的布局尺寸。

不直接降低浅色 `[0, .7, 1]` alpha 来制造“更透”：它本身来自 APK。应该先检查底色、覆盖范围、滚动状态，以及是否真的调用系统接口。相同归一化渐变铺在不同高度上，会让同一屏幕位置显示不同的遮罩强度。

**禁止用 `View.setRenderEffect()` 代替 `OplusViewBackgroundRenderEffect`。** 前者处理 View 自身内容，不等同于处理 View 后面的页面。

## 检测和回退规则

1. Android API 至少 31，才进入包含 `RenderEffect` 的路径；API 24–30 保持旧实现。
2. 工作线程读取 `ro.build.version.oplusrom`，空值时检查 `ro.build.version.opporom`。版本必须明确是 `17` / `V17.x` / `ColorOS 17.x`；未知、16、18 不强行启用。反射读取失败时仅尝试有超时的 `/system/bin/getprop` 读取。
3. 不把 Android 版本或 Oplus SDK 数字当成 ColorOS 营销版本。真实设备使用的属性值仍需要现场确认。
4. 获取 `com.oplus.os.OplusBuild$VERSION.SDK_VERSION` / `SDK_SUB_VERSION`，复用 APK 的背景效果门槛：主版本大于 33，或等于 33 且子版本至少 34。
5. 读取 `Settings.System.system_material_blur_enable`，必须为 1；`Settings.Global.disable_window_blurs` 必须为 0。读取失败按不可用处理。
6. `persist.sys.oplus.anim_level` 必须是 1 或 2，与 APK 的 `!isLowAnimationLevel()` 一致。读不到不能猜成支持。
7. View 必须已挂载且使用硬件加速，两个系统方法必须可访问并成功调用。

模糊设置使用 `ContentObserver` 监听：关闭时清除系统效果、显示旧实现；重新打开时重新应用系统效果。页面脱离窗口时取消观察并清除系统效果。接口或硬件加速失败时，本次组件生命周期内固定回退，避免反复抛异常。

本项目并未移植 COUI 全部状态管理、资源配置或完整 AppBar 滚动 Behavior。此次复用了覆盖边界、8/24dp 延伸、30dp 阈值和 View alpha 显现规则，滚动状态及工具栏布局由本项目宿主提供。**这是使用设置 APK 系统 API 和覆盖层规则的桥接，不是整个设置页面像素级复制。**

## 采样层级

```text
页面容器
├─ 独立页面背景和内容 → LayerBackdrop（不含顶部模糊/工具栏）
├─ ProgressiveBlurContent
│  ├─ ColorOS 17：独立 AndroidView，系统采样 View 后面的窗口内容
│  └─ 回退：原 Compose drawPlainBackdrop，读取页面 LayerBackdrop
└─ 原工具栏与返回按钮
```

两个实现只负责顶部模糊，不录制工具栏、不处理按钮、不录制自身父页面。调用者不得给系统 View 包含自身的采样层，也不要在外层强制离屏合成；否则系统背景效果可能读到透明层或错误层。

`backdrop` 仍必须提供，供非 ColorOS、检测失败和系统拒绝访问时使用。系统路径不读取 Compose `LayerBackdrop`，它读取系统窗口后方内容；两条路径的采样机制不同。

## 验证与排错

```powershell
node tools/verify-extraction.mjs
node tools/generate-ios27-tokens.mjs --check
node --test tools/coloros17-progressive-blur.test.mjs
$env:QQ_APK_AUTO_SEND='0'
.\gradlew.bat :verifyLiquidGlassSpec :liquidglass:testDebugUnitTest :sample:assembleDebug --no-daemon --console=plain
adb shell getprop ro.build.version.oplusrom
adb shell getprop ro.build.version.opporom
adb shell getprop persist.sys.oplus.anim_level
adb shell settings get system system_material_blur_enable
adb shell settings get global disable_window_blurs
adb logcat -d -s ColorOs17TopBlur
```

日志 `backend=coloros17-system` 只说明接口调用返回成功，不证明固件实际渲染正确；`backend=compose-fallback` 带异常原因说明接口不可用。新增 `decision=` 日志可区分 ROM 检测失败、原生 pending/disabled、原生成功和接口失败，防止把自动回退误认成系统效果。成功日志同时显示 `surface` 和当时的 `visibility`；它不是持续滚动跟踪。

必须在 ColorOS 17 真机上查看滚动文字或图片在顶部同一位置的变化，检查浅/深色、工具栏清晰度、触摸穿透、返回重进与关闭/重新打开模糊设置，并检查 crash 日志。模拟器只能验证回退，不足以验收 Oplus 系统效果。没有真机证据时不得宣称系统渐进模糊效果已验证。

## 2026-10-11 本次验证记录

- 提取基线校验通过：43 个原始文件保持不变，5 个已记录的运行源码改动通过哈希校验。顶部模糊的新增授权记录同时列出了本次修改前哈希，不覆盖原始基线或改动前快照。
- tokens 校验、玻璃规范检查、库单测、sample debug 构建和 AAR/APK 类定义检查通过。
- 新增策略 JVM 单测 4/4 通过，桥接静态回归 4/4 通过。静态回归不证明固件执行效果。
- 全示例静态回归仍为此前的 15 通过 / 17 失败；未修改这些无关断言或页面来掩盖失败。
- APK：`sample/build/outputs/apk/debug/sample-debug.apk`，SHA-256 `47F79FC861E5E52FD86CE4674C98D64AC059414DCF6A1BB6BFBFDCE101AD0FA2`。未自动发送到 QQ。
- ADB 无在线设备；未安装真机或模拟器，ColorOS 17 属性、接口访问权限和实际窗口背景采样均待真机验证。

## 2026-10-12 对齐与验证记录

- 修正前一版的三处宿主差异：真实页面底色没有传入、覆盖范围固定 128dp、滚动显现同时改变半径。现在自动传递底色，按 AppBar 范围加 8/24dp 计算覆盖，滚动单独使用原生 View alpha。
- 浅色/深色渐变 alpha 和系统 API 的 30/0/3 原值没有降低；工具栏、返回按钮、底栏和其他玻璃组件没有改变实现。
- 代码策略单测 5/5、专项静态检查 5/5 通过；提取基线、tokens、玻璃规范、库单测、debug 构建及 AAR/APK 类定义检查通过。
- 全示例静态回归复查仍为 15 通过 / 17 失败；这些既有失败未修复，也没有修改无关断言来掩盖结果。
- 真机读取到 `ro.build.version.oplusrom=V17.0.0`、动画等级 1、材质开关 1、禁用窗口模糊开关 0。这些是 ADB shell 读取结果，不等同于普通应用内接口访问成功。
- 新 APK：`sample/build/outputs/apk/debug/sample-debug.apk`，SHA-256 `BB7C619EF89B81D4E67C48A77C4E3EF901683CDF66EE80390CDA1A71ED7836CF`，未发送 QQ。
- 用户选择自行验收；未安装本轮 APK、未继续操作真机。系统接口执行成功、浅/深色实际采样、旋转/分屏及与 Settings 同位置对比均未验收。请使用上面的 `ColorOs17TopBlur` 日志区分原生路径与回退路径。
