# ColorOS 17 原组件提取与调用

用户边界：**不重新实现控件，只在 ColorOS 17 中调用原组件**。本项目新增的是分类目录、运行时加载桥接、演示数据和回调，不是 Compose 版 COUI，不更换原液态玻璃组件或既有 iOS 示例的视觉。

## 入口与分类

应用“组件”页的手机示例列表最后新增一组 `ColorOS 17 / …` 分类。沿用现有“分类 → 变体 → 示例”导航；二级页仍隐藏底栏，返回按钮复用框架入口，不改变已有示例路由。

| 分类 | 本轮调用入口 |
| --- | --- |
| 顶部工具栏 | COUIToolbar |
| 底部工具栏 | COUIBottomFloatingToolbar |
| 标签栏 | COUINavigationView、COUIBottomFloatingNavigationBar、COUITabLayout、COUISegmentButtonLayout |
| 提示弹窗 | COUIAlertDialogBuilder |
| 模态页面 | COUIBottomSheetDialog |
| 菜单 | COUIPopupMenu |
| 按钮 | COUIButton、COUILoadingButton |
| 开关 | COUISwitch、COUICheckBox |
| 滑块 | COUISeekBar、COUISectionSeekBar、COUIIntentSeekBar |
| 步进器 | COUIStepperView |
| 文本框 | COUIEditText、COUIInputView、COUISearchBar |
| 日期与时间 | COUIDatePicker、COUITimePicker、COUINumberPicker |
| 列表 | COUIPreference、COUISwitchPreference、COUIJumpPreference |
| 进度与反馈 | COUIHorizontalProgressBar、COUICircularProgressBar、COUILoadingView |

共 14 类、29 个演示入口。图标和文本是演示数据，不宣称复制了 Settings 各业务页面。Figma 的颜色选择、空状态、分享面板等分类没有因此获得一套 ColorOS 对等组件；本轮只登记该 APK 内已确认的原类，不编造对应实现。

## 原始资料与边界

- 来源：`D:/下载/设置_17.0.0.apk`，包名 `com.android.settings`、版本 `17.0.0`、minSdk 33。
- APK SHA-256：`E285088674907EEDEF89B4BA9862165DD070699CBA4303C4B05B969F99C4261D`。
- 提取目录：[settings-17.0.0](coloros17-extracted/settings-17.0.0/)。
- 原样反编译输出：`sources/`，678 个 Java 文件；包括组件类、同包辅助类、动画/样式桥接类。JADX 1.5.6 设置 `inlineMethods=false`、`inlineAnonymousClasses=false`；没有手工修正方法体。
- 原始 ZIP 条目：`resources/raw/`，647 个文件，包含 `coui_*` 资源、原 `resources.arsc`、二进制 Manifest。它们不是可直接交给 Gradle 的 XML 源码，也不是独立可编译的完整资源闭包。
- 解读用资源表：`resources/resource-table.txt`，由 aapt2 dump 生成。里面可查 `Theme.COUI`、`Theme.COUI.Dark`、布局和尺寸等原始资源配置；不能把此文本当作原始 values XML。
- [组件到源码映射](coloros17-extracted/settings-17.0.0/catalog.md)；[来源、签名接口和哈希索引](coloros17-extracted/settings-17.0.0/component-index.json)。索引中的 `publicSignatures` 是反编译文本摘录，不保证恢复了全部继承方法或原始 Kotlin 签名。

**提取 ≠ 独立库 ≠ 普通应用已确认能调用。** 这些 COUI 类确实定义在 Settings DEX 中，与先前由固件提供的 `OplusRenderEffect` 不同。普通应用需要加载已安装设置包的 ClassLoader 与资源，不能只写 `Class.forName("com.coui…")` 就认定能用。本轮提取并未复制所有 AndroidX、Material、Oplus 和系统权限依赖。

有方法带 `JADX WARN` / `JADX ERROR` / `Method not decompiled`。原样保留并在索引 `decompileLimited` 标记；不把它们称为完整可编译源码。运行时不执行这些反编译文件，而执行手机上已安装设置包的原始字节码。

这些厂商代码/资源不视为本项目开源许可证的一部分，不应未经许可公开分发为第三方 SDK。本项目 APK 不内嵌上述提取文件，也不分发设置 APK。

## 其他应用的调用方式

依赖本项目 `liquidglass` 库，入口：

- `dev.liquidglass.coloros17.ColorOs17ComponentCatalog`：分类与原类白名单。
- `ColorOs17SettingsComponents.open(activity, dark)`：异步检测和创建资源/代码上下文。
- `session.createView(spec, onValue)`：主线程构造原 View 或原 Preference 并绑定原布局。
- `session.showPopup(spec, attachedAnchor, onValue)`：主线程调用原 Dialog、Sheet 或 PopupMenu。
- `session.close()`：主线程离页清理已登记的窗口和原组件 release 方法。

```kotlin
// 在 Activity 的生命周期作用域内；open 的探测在 IO，UI 构造在主线程。
val result = ColorOs17SettingsComponents.open(thisActivity, dark = false)
result.onSuccess { session ->
    val spec = ColorOs17ComponentCatalog.find("coloros17-switch")!!
    session.createView(spec) { value ->
        // 保存你自己的应用状态；不写入系统设置。
    }.onSuccess { nativeView ->
        parent.addView(nativeView)
    }.onFailure { error ->
        showUnavailable(ColorOs17SettingsComponents.failure(error))
    }
    // 在离页/销毁时先移除 View，再在主线程 session.close()。
}.onFailure { error ->
    showUnavailable(ColorOs17SettingsComponents.failure(error))
}
```

`thisActivity`、`parent`、`showUnavailable` 是调用方提供的宿主和错误提示，不是库内符号。Compose 的完整宿主例子见 `sample/src/main/java/dev/liquidglass/sample/ColorOs17ExamplePage.kt`；不要照搬离页后仍持有 Activity 的全局单例。

弹窗调用：

```kotlin
val spec = ColorOs17ComponentCatalog.find("coloros17-alert")!!
session.showPopup(spec, buttonAlreadyAttachedToWindow) { action ->
    // 处理演示动作，不授予 Settings 权限。
}.onFailure { showUnavailable(ColorOs17SettingsComponents.failure(it)) }
```

## 应用状态绑定接口

演示 `createView(spec)` 保留原有演示数据。业务宿主使用同一会话的以下接口，不能把“首页/发现/设置”或默认勾选值直接当作自己的设置：

- `createSwitch(checked, onCheckedChange)`：初始化后才注册回调；返回原 `CompoundButton`。
- `createSlider(max, progress, onProgress, onFinished)` / `updateSlider(view, progress)`：原 COUISeekBar；只将 `fromUser=true` 的数值变化通知宿主，停止拖动时提交。程序同步不回写业务状态。
- `createNavigation(items, selectedId, onSelected)` / `updateNavigation(view, selectedId)`：原 COUIBottomFloatingNavigationBar，通过外包原 Item.Builder 与 addItems 创建悬浮导航。使用宿主传入的 ID、文字和 Drawable；回调从原 item 的 getId 读取，不能将它强转成 Android MenuItem。`ColorOs17NavigationItem` 不跨边界传递外包 AndroidX 类型。
- `createActionButton(label, icon, onClick)`：原 COUIButton；可使用宿主 Drawable，图标取按钮原文字颜色。
- `createFloatingActionButton(label, icon, onClick)`：原 `com.coui.appcompat.floatingactionbutton.COUIFloatingButton`，使用 `setMainFabDrawable` / `setFloatingButtonClickListener`。这是圆形操作入口；不能用 COUIButton 的 compound drawable 假装成居中圆形按钮。图标由宿主提供，背景、圆形裁剪、阴影、描边和触摸反馈由原组件绘制。
- `showSingleChoiceDialog(title, choices, selected, onSelected, onDismiss)`：原 COUIAlertDialogBuilder 单选弹窗；选择后关闭，返回原 Dialog 供离页清理。此接口不是 Inspector 的视觉仿制。

这些接口复用原有 ROM、签名、资源和 Activity 检查，不另建加载器、不打包 COUI、不改原组件材质。关闭会话仍负责清理窗口；宿主负责保存自己的偏好及 Compose/View 状态同步。

会话第一次构造原组件前，在主线程调用原 `COUIMaterialStateManager.INSTANCE.registerMaterialStateObserver(context, FLAG_ALL_EFFECT)`。原管理器初始材质状态为 false，仅构造 View 不会自动完成此应用级初始化。共享外包 singleton 的重叠会话通过 `ColorOs17MaterialOwners` 管理引用计数，最后一个会话关闭才调用 `unregisterMaterialStateObserver`；避免旋转/主题变化时旧会话关闭导致新控件丢失材质。原管理器负责读取系统模糊、轮廓、主题、省电和硬件限制，不写系统设置。

**悬浮底栏使用 `setIsDrawableMaterialEffect(false)`。** `BaseBottomFloatingMenuView.updateBlurEffect()` 的 false 分支在系统模糊开启时调用 `TYPE_FRAMEWORK_BOTTOM_BAR` 并设置透明背景；true 分支会先调用 `TYPE_NO_EFFECT` 清除真实模糊，再调用 `applyPanelBackground()`。该参数不是“开启原生材质”的开关，不能因名字误传 true。原组件仍保留模糊关闭时的内部不透明回退。

原资源中可见底栏高度为 56dp，导航宿主最小高度为 88dp，宿主已在可见底栏下保留 16dp。Soundbar 的 ColorOS 分支在系统底部安全区内不再叠加原玻璃底栏的 16dp 外距；不缩放可见底栏，也不移除系统安全区。

Soundbar 的“设置 → 界面 → 液态玻璃”默认开启。关闭时开关、滑块、底部导航、圆形重启按钮及日志弹窗使用上述接口，原控件保持系统密度并位于背景采样层之外。不可用时显示错误与切回入口，不静默替换成仿制控件。开关仅保存应用界面偏好，不改变 LSPosed/SystemUI 音量面板配置。

变更清单：`ColorOs17SettingsComponents.kt` 增加上述业务绑定 API 与导航数据类型；`ColorOs17SliderBinding.kt` 区分触摸拖动与键盘/无障碍修改；`ColorOs17MaterialOwners.kt` 管理材质观察器生命周期。JVM 测试覆盖松手提交、程序同步不回写、无触摸提交、旋转重叠会话、重新进入和不同 singleton 的独立释放。原演示入口和提取基线不变。验证包含组件门槛/提取哈希检查、库测试及调用方编译；跨包 attach/draw 与交互须由目标设备验收。

## 运行限制与安全门槛

1. Android API 33+；实际读到的 ROM 属性必须为 ColorOS 17。沿用已有 `ColorOs17BlurPolicy.isColorOs17`，不能拿 Oplus SDK 或 Android API 数字替代营销版本。属性读取失败按不可用处理。
2. 只加载固定包名 `com.android.settings`；要求包带系统/更新系统标记、与 `android` 平台签名匹配、Settings 版本以 `17.` 开头。拒绝调用方任意传入 APK 路径或类名。平台签名检查是信任边界，不代表与这份参考 APK 完全同版。
3. 使用 Android 文档中的 `createPackageContext(CONTEXT_INCLUDE_CODE | CONTEXT_IGNORE_SECURITY)`。此标志允许跨包代码进入**本应用进程**，不授予 Settings UID、签名权限或 hidden API 豁免。Android 官方明确警告跨包加载应谨慎使用，因此固定系统包和平台签名检查必须保留。[Android Context API](https://developer.android.com/reference/android/content/Context#createPackageContext(java.lang.String,%20int))
4. 保留外包 ClassLoader 身份，使用它自己的 COUI 主题、资源和 PreferenceViewHolder；不能把外包 AndroidX 对象强转成本项目同名 AndroidX 类。跨加载器共享的边界仅使用 Android 框架 View、Dialog、Menu、Context 等类型。
5. UI Context 的基础链连接宿主 Activity，资源/Assets/ClassLoader 来自设置包；窗口与输入服务来自宿主，不用设置包 Context 的无 Activity 窗口 token 弹窗。
6. 动态样式选择 `Theme.COUI` / `Theme.COUI.Dark`，跟随示例配色。Native 控件保留当前窗口系统 dp/sp 和系统 fontScale，不套 402pt Figma 密度，不设置假圆角/阴影/颜色来补齐缺失原组件。
7. Preference 演示明确 `setPersistent(false)`，不启动设置 Activity，不运行设置业务 Controller，不修改蓝牙、亮度、时间或其他系统配置。
8. 构造、配置、弹窗反射失败或 LinkageError 返回错误，页面显示原因；非 ColorOS 17 也显示不可用。没有 Material/iOS/LiquidGlass 伪装回退。原库可能在 attach/draw/异步回调中依赖内部固件或权限；同步 Result 不能保证捕获这些阶段的错误，所以须逐项真机验证后再投入产品。

库 Manifest 已提供固定包查询声明；其他应用独立复制桥接代码时也需要：

```xml
<queries>
    <package android:name="com.android.settings" />
</queries>
```

版本更新、配置/窗口变化或浅深色切换时重建会话，离页关闭旧窗口。不缓存一个跨 Activity 的会话；不让运行期升级后的源码与旧资源上下文混用。

## 原方法与演示参数

按钮只设演示文案与点击回调；加载按钮调用 `switchToLoadingState` / `resetButtonState`，不重写加载动画。开关用 CompoundButton 回调；复选框用原 `setOnStateChangeListener`，不额外 toggle 导致重复切换。滑块用原 `setMax` / `setProgress` 和监听器；连续示例 0–100，分段示例 0–5。步进器用原范围 0–10、初始值 5。数字选择 0–59、初值 30；时间选择用 `setTimePicker(Calendar.getInstance())` 初始化。

标签页使用原 `newTab/addTab`，分段使用原 `setSegmentButtons`，底栏使用原 Menu 或原 Item.Builder。三项“首页/发现/设置”和 Android 演示图标属于示例数据，**不是 Settings 官方样例布局数据**。底部浮动工具栏回调使用原 `registerItemClick`，离页调用 `unRegisterItemClick`。

列表通过原 `getLayoutResource/getWidgetLayoutResource` inflate，并用外包 `PreferenceViewHolder.createInstanceForTests`、`onBindViewHolder` 绑定。该方法在不同固件的 AndroidX 版本可能不可用；失败就报告，不手写列表仿制。这里不是完整 PreferenceFragment/PreferenceManager 业务页面。

弹窗用原 Builder 的 `create`，设置 host owner Activity 后 `show`；底部面板用原 COUIBottomSheetDialog 并塞入原 COUIInputView；菜单用原 COUIPopupMenu、Menu 与锚点。文案/动作仅为本项目演示，不宣称完整复制设置中的业务弹窗。

## 采样层级

```text
示例页
├─ 纯页面背景 → 独立 LayerBackdrop
├─ 原 COUI View / Preference（不录制进上述 source）
├─ 框架顶部模糊与返回按钮 → 读取独立背景
└─ 原 COUI Dialog / Popup 原生窗口
```

COUI 原生材质使用它自己的窗口背景效果；禁止给原 View 外套 LiquidGlass `drawBackdrop`，禁止把可能带原生模糊的 COUI 内容录进再被自身/玻璃兄弟读取的 source。原按钮按压、阴影、高光和材质开关由它自己的实现与固件决定；不要为了“看起来可用”关闭它们或另画一套。顶部已有 ColorOS 桥接不在本轮改写。

## 提取复查与验收

原源码分两次用现有 `tools/ExtractApkBlur.java` 按 COUI 包名导出到新目录；该工具拒绝覆盖已有源码。资源提取命令：

```powershell
& 'C:/Program Files/Android/Android Studio/jbr/bin/java.exe' tools/ExtractColorOs17Resources.java 'D:/下载/设置_17.0.0.apk' docs/coloros17-extracted/settings-17.0.0/resources/raw
node tools/index-coloros17-components.mjs 'D:/下载/设置_17.0.0.apk' C:/Users/29373/AppData/Local/Android/Sdk/build-tools/37.0.0/aapt2.exe
node tools/index-coloros17-components.mjs --check
node --test tools/coloros17-components.test.mjs tools/coloros17-progressive-blur.test.mjs
node tools/verify-extraction.mjs
node tools/generate-ios27-tokens.mjs --check
$env:QQ_APK_AUTO_SEND='0'
.\gradlew.bat :verifyLiquidGlassSpec :liquidglass:testDebugUnitTest :sample:assembleDebug --no-daemon --console=plain
```

日志：`adb logcat -d -s ColorOs17Components ColorOs17TopBlur`。`created=` / `invoked=` 仅代表对应原接口返回成功；不是视觉/交互全部通过。不同 ROM 即便版本都为 17，私有 API、布局、主题和 Settings 版本也可能不同。不要把此调用能力称为 ColorOS 公共稳定 SDK。

本轮不安装、不连接或操作真机，沿用用户自行验证的选择。验收需逐项检查原类名与版本、浅深色、触摸结果、返回关闭窗口、旋转/分屏、键盘输入与 crash 日志。模拟器/其他 ROM 应显示“无法调用原组件”，不能把它们的错误页截图当作 COUI 效果验收。

### 2026-10-13 构建和检查记录

- 原样 Java 678 个、原始资源条目 647 个；目录 14 类 / 29 个原组件调用示例，来源与逐文件 SHA-256 已登记。
- 原组件专项静态检查 5/5、目录 JVM 单测 3/3 通过；原顶部渐进模糊专项 5/5 也通过。
- 原玻璃提取校验、tokens 校验、玻璃规范、库单测、sample debug 构建、AAR/APK 类定义检查通过。
- 全示例静态回归仍为既有的 15 通过 / 17 失败，未修改无关页面或断言来掩盖失败。
- APK DEX 查到 42 个 `dev.liquidglass.coloros17` 桥接/目录相关定义，`com.coui.appcompat` 原组件定义为 0；提取目录也没有进 APK。运行时必须由已安装设置包提供原实现。
- APK：`sample/build/outputs/apk/debug/sample-debug.apk`；SHA-256 `92E7870C8A1D73914B3228CC195806419A224092CBB699D88D3BEFBE15D205C2`。未发送 QQ、未安装设备。
- **未确认真机跨包加载、29 个组件 attach/draw、浅深色或交互全部可用。** 上述检查不覆盖固件私有接口/签名权限和运行时资源依赖；由用户安装后逐项验收。
