# 听音模式组件

入口：组件 → 手机示例 → 听音模式 → 四档听音模式。

公开组件在 `liquidglass/src/main/java/dev/liquidglass/components/ListeningModeSelector.kt`，可以直接随现有 `liquidglass` module/AAR 复用。没有改动旧玻璃运行源码、MeloX renderer 或 Toolbars - Top。

```kotlin
import dev.liquidglass.components.ListeningMode
import dev.liquidglass.components.ListeningModeSelector

var mode by rememberSaveable { mutableStateOf(ListeningMode.NoiseCancellation) }
ListeningModeSelector(
    selectedMode = mode,
    onModeSelected = { mode = it },
    modifier = Modifier.fillMaxWidth(),
)
```

这是受控组件，调用者负责更新 `selectedMode`。再次点击当前项不重复调用。示例只显示一个可保存状态的实例，跟随应用配色设置切换浅色/深色；用户图片的两种配色只是参考，不是上下同时显示的布局。不调用真实耳机降噪接口，不表示耳机硬件已经切换。

四档顺序固定为关闭、通透模式、自适应、主动降噪。图标在胶囊内，标签在胶囊外；点图标或标签都能选择。当前项相邻的竖线隐藏，其他竖线保留。选中胶囊平移与图标按压缩放使用 Compose spring，重入时能转向；系统动画倍率由 Compose 自身处理。每项使用 RadioButton 选择语义，有中文辅助说明和 disabled 状态。

默认主题跟随宿主 MaterialTheme（包含应用内主题切换），也可指定 `darkTheme`。可覆盖 `colors`、`title`（null 隐藏标题）、`labelStyle`、`icon`、`enabled` 和 `hapticsEnabled`。自定义图标必须使用传入的 Modifier 和 Color，避免图标脱离命中区。字体使用系统字体，不修改设备字体/字号；大字号允许外置标签自然换行。

尺寸和颜色依据本次用户提供的 JPG 截图估算，不来自 Figma 或 Apple 官方数值。基准宽度按 402 设计点映射：页面侧距约 18，标题额外缩进 18，胶囊高度 68、选中内距 4、图标视口 40，标签上距 6。各项等分容器宽度；浅色轨道 #E3E3E8、选中白色；深色轨道 #1C1C1E、选中 #4B4B50。图标是依据截图新建的矢量轮廓，不宣称是 Apple 原始 SF Symbol 路径。参考是纯色分段控件，故没有附加液态玻璃或背景采样。

示例通过既有 ExampleRecordedDesignPage、顶部渐进模糊和返回按钮接入；纯色组件处于内容层，顶部玻璃仍是独立兄弟层。

## Android 行为适配（2026-10-08）

仅调整听音模式组件的行为层，不将页面换成 Material 默认外观，不升级或替换玻璃组件。依据当前 [Android Compose 无障碍默认行为](https://developer.android.com/develop/ui/compose/accessibility/api-defaults)、[语义规范](https://developer.android.com/develop/ui/compose/accessibility/semantics)、[焦点分组](https://developer.android.com/develop/ui/compose/touch-input/focus/focus-group) 与 [触觉类型](https://developer.android.com/reference/kotlin/androidx/compose/ui/hapticfeedback/HapticFeedbackType)。这是定制组件的行为适配，不表示整套应用已经符合全部 Android 最新设计要求。

- 标题使用 heading 语义；每个选项仅由父级提供一次名称和 RadioButton 的选中/禁用语义。内置图标及显示标签的语义清空，防止重复朗读；它们的绘制完全保留。
- 点击区域包含图标和外置标签，最小 48dp。容器宽度不小于 192dp 时仍按原宽度四等分；更窄时内部宽度保底 192dp 并提供横向滚动，不把四个触区压小或相互重叠。此极端宽度降级属于本地适配方案，不是 Android 要求固定使用 192dp。
- 使用 Foundation selectable 与 focusGroup 保留系统键盘/方向键导航；仅在键盘输入模式的焦点项上绘制 2dp 内置轮廓，触屏普通显示不出现。焦点移动不会自行提交模式，激活选项才调用回调。
- RTL 下分隔线跟随项目镜像；选中胶囊原有相对起始边 offset 不变。
- 模式切换改用 SegmentTick；再次点击当前项仍无重复回调或触觉，禁用项交由 selectable 拦截。组件仍不读写全局设置或耳机服务。
- 受控状态、示例 rememberSaveable、系统字体缩放、Compose 原有 spring 以及单实例随宿主主题切换均保留。没有改动系统动画倍率、主题或字体设置。

普通触屏/LTR 的视觉锁定：原配色与整个图标绘制函数使用修改前 SHA-256 回归；68dp 胶囊、4dp 选中内距、40dp 图标、14/18sp 标签与 6dp 标签上距均有检查。焦点提示、RTL 和极窄容器是新增行为情境；源码回归不等于已经完成真机逐像素截图对比，设备视觉仍由用户验收。

校验：

```powershell
$env:QQ_APK_AUTO_SEND='0'
node tools/verify-extraction.mjs
node tools/generate-ios27-tokens.mjs --check
node --test tools/listening-mode.test.mjs tools/melox-tabbar-extraction.test.mjs
.\gradlew.bat :verifyLiquidGlassSpec :liquidglass:testDebugUnitTest :sample:assembleDebug --no-daemon --console=plain
node tools/check-listening-mode-device.mjs <adb设备序列号>
```

原有 extraction 校验在 ProgressiveBlurContent 的登记哈希处失败，旧 native-framework 套件存在 17/31 既有失败；本次不恢复用户文件或刷新旧哈希掩盖它们。新组件验证报告单独保存在 `docs/listening-mode-validation/`。编译不等于与参考逐像素一致。
