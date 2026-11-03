package dev.liquidglass.sample

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import dev.liquidglass.ios27.Ios27PageTheme
import dev.liquidglass.ios27.Ios27Tokens
import dev.liquidglass.ios27.ios27AndroidPane
import kotlinx.coroutines.flow.collect
import top.yukonga.miuix.kmp.nav.gesture.PredictiveBackHandler as MiuixPredictiveBackHandler

/**
 * Public descriptor for one extracted iOS 27 example.
 *
 * These descriptors are intentionally stable so callers never need to know the
 * backing JSON asset path or the internal sample router.
 */
@Immutable
data class Ios27ExampleSpec(
    val key: String,
    val category: String,
    val variant: String,
    val file: String,
)

/** Stable keys for all 56 extracted examples. */
object Ios27ExampleKeys {
    const val TopToolbarTitle2LineLeft = "12740-23997"
    const val TopToolbarTitle2Line = "12740-24005"
    const val TopToolbarLargeTitle = "12740-24013"
    const val TopToolbarCompactLargeTitle = "12740-24021"
    const val TopToolbarDefault = "12740-24029"
    const val TextFieldsDefault = "12740-35194"
    const val BottomToolbarSearch = "12740-24039"
    const val BottomToolbarSymbols = "12740-24048"
    const val BottomToolbarText = "12740-24062"
    const val SwitchesDefault = "12740-35193"
    const val TabBarDefault = "12740-24077"
    const val TabBarSplitSearch = "12740-24086"
    const val SliderDefault = "12740-35186"
    const val StepperDefault = "12740-35192"
    const val SheetFullscreen = "12740-24098"
    const val SheetFullscreenStack = "12740-24110"
    const val SheetInspector = "12740-24122"
    const val PickerInline = "12740-24135"
    const val PickerCompact = "12740-24143"
    const val MenuEditMedium = "12740-24157"
    const val MenuDefault = "12740-24173"
    const val ListInset = "12740-24203"
    const val ListFullWidth = "12740-24224"
    const val ListSectioned = "12740-24239"
    const val KeyboardLowercase = "12740-24269"
    const val KeyboardUppercase = "12740-24284"
    const val KeyboardPunctuation = "12740-24299"
    const val KeyboardNumeric = "12740-24314"
    const val KeyboardFind = "12740-24329"
    const val KeyboardFindAndReplace = "12740-24340"
    const val KeyboardEmail = "12740-24351"
    const val KeyboardWebSearch = "12740-24360"
    const val KeyboardUrl = "12740-24371"
    const val KeyboardNumberPad = "12740-24382"
    const val KeyboardEmoji = "12740-24391"
    const val FaceIdAuthenticating = "12740-24401"
    const val FaceIdSuccess = "12740-24409"
    const val NotificationsExpanded = "12740-24418"
    const val NotificationsCollapsed = "12740-24428"
    const val EmptyStateTitleAndSubtitle = "12740-24439"
    const val EmptyStateSymbolInCircle = "12740-24450"
    const val EmptyStateLargeSymbol = "12740-24462"
    const val LockScreenDefault = "12740-35191"
    const val ContextMenuDefault = "12740-35183"
    const val WidgetSmall = "12740-24471"
    const val WidgetMedium = "12740-24474"
    const val WidgetLarge = "12740-24477"
    const val WidgetExtraLarge = "12740-24480"
    const val ColorPickerDefault = "12740-35184"
    const val HomeScreenQuickActionsDefault = "12740-35187"
    const val ActionSheetDefault = "12740-35185"
    const val HomeScreenDefault = "12740-35189"
    const val AlertSideBySide = "12740-24488"
    const val AlertStacked = "12740-24497"
    const val ControlCenterDefault = "12740-35188"
    const val ActivityViewDefault = "12740-35190"
}

/** Catalog matching sample/src/main/assets/iphone-examples/catalog.json. */
object Ios27ExampleCatalog {
    val all: List<Ios27ExampleSpec> = listOf(
        Ios27ExampleSpec(Ios27ExampleKeys.TopToolbarTitle2LineLeft, "Toolbars - Top", "Type=Title 2 Line Left", "12740-23997.json"),
        Ios27ExampleSpec(Ios27ExampleKeys.TopToolbarTitle2Line, "Toolbars - Top", "Type=Title 2 Line", "12740-24005.json"),
        Ios27ExampleSpec(Ios27ExampleKeys.TopToolbarLargeTitle, "Toolbars - Top", "Type=Large Title", "12740-24013.json"),
        Ios27ExampleSpec(Ios27ExampleKeys.TopToolbarCompactLargeTitle, "Toolbars - Top", "Type=Compact Large Title", "12740-24021.json"),
        Ios27ExampleSpec(Ios27ExampleKeys.TopToolbarDefault, "Toolbars - Top", "Type=Default", "12740-24029.json"),
        Ios27ExampleSpec(Ios27ExampleKeys.TextFieldsDefault, "Text Fields", "Default", "12740-35194.json"),
        Ios27ExampleSpec(Ios27ExampleKeys.BottomToolbarSearch, "Toolbars - Bottom", "Type=Search", "12740-24039.json"),
        Ios27ExampleSpec(Ios27ExampleKeys.BottomToolbarSymbols, "Toolbars - Bottom", "Type=Symbols", "12740-24048.json"),
        Ios27ExampleSpec(Ios27ExampleKeys.BottomToolbarText, "Toolbars - Bottom", "Type=Text", "12740-24062.json"),
        Ios27ExampleSpec(Ios27ExampleKeys.SwitchesDefault, "Toggles (Switches)", "Default", "12740-35193.json"),
        Ios27ExampleSpec(Ios27ExampleKeys.TabBarDefault, "Tab Bar", "Split Search=False", "12740-24077.json"),
        Ios27ExampleSpec(Ios27ExampleKeys.TabBarSplitSearch, "Tab Bar", "Split Search=True", "12740-24086.json"),
        Ios27ExampleSpec(Ios27ExampleKeys.SliderDefault, "Slider", "Default", "12740-35186.json"),
        Ios27ExampleSpec(Ios27ExampleKeys.StepperDefault, "Steppers", "Default", "12740-35192.json"),
        Ios27ExampleSpec(Ios27ExampleKeys.SheetFullscreen, "Sheets (Modals)", "Type=Fullscreen", "12740-24098.json"),
        Ios27ExampleSpec(Ios27ExampleKeys.SheetFullscreenStack, "Sheets (Modals)", "Type=Fullscreen Stack", "12740-24110.json"),
        Ios27ExampleSpec(Ios27ExampleKeys.SheetInspector, "Sheets (Modals)", "Type=Inspector", "12740-24122.json"),
        Ios27ExampleSpec(Ios27ExampleKeys.PickerInline, "Picker", "Type=Inline", "12740-24135.json"),
        Ios27ExampleSpec(Ios27ExampleKeys.PickerCompact, "Picker", "Type=Compact", "12740-24143.json"),
        Ios27ExampleSpec(Ios27ExampleKeys.MenuEditMedium, "Menus", "Type=Edit Medium", "12740-24157.json"),
        Ios27ExampleSpec(Ios27ExampleKeys.MenuDefault, "Menus", "Type=Menu", "12740-24173.json"),
        Ios27ExampleSpec(Ios27ExampleKeys.ListInset, "List", "Type=Inset", "12740-24203.json"),
        Ios27ExampleSpec(Ios27ExampleKeys.ListFullWidth, "List", "Type=Full Width", "12740-24224.json"),
        Ios27ExampleSpec(Ios27ExampleKeys.ListSectioned, "List", "Type=Full Width + Section Headers", "12740-24239.json"),
        Ios27ExampleSpec(Ios27ExampleKeys.KeyboardLowercase, "Keyboard", "Type=Lowercase", "12740-24269.json"),
        Ios27ExampleSpec(Ios27ExampleKeys.KeyboardUppercase, "Keyboard", "Type=Uppercase", "12740-24284.json"),
        Ios27ExampleSpec(Ios27ExampleKeys.KeyboardPunctuation, "Keyboard", "Type=Punctuation", "12740-24299.json"),
        Ios27ExampleSpec(Ios27ExampleKeys.KeyboardNumeric, "Keyboard", "Type=Numberic", "12740-24314.json"),
        Ios27ExampleSpec(Ios27ExampleKeys.KeyboardFind, "Keyboard", "Type=Find", "12740-24329.json"),
        Ios27ExampleSpec(Ios27ExampleKeys.KeyboardFindAndReplace, "Keyboard", "Type=Find and Replace", "12740-24340.json"),
        Ios27ExampleSpec(Ios27ExampleKeys.KeyboardEmail, "Keyboard", "Type=Email", "12740-24351.json"),
        Ios27ExampleSpec(Ios27ExampleKeys.KeyboardWebSearch, "Keyboard", "Type=Web Search", "12740-24360.json"),
        Ios27ExampleSpec(Ios27ExampleKeys.KeyboardUrl, "Keyboard", "Type=URL", "12740-24371.json"),
        Ios27ExampleSpec(Ios27ExampleKeys.KeyboardNumberPad, "Keyboard", "Type=Number Pad", "12740-24382.json"),
        Ios27ExampleSpec(Ios27ExampleKeys.KeyboardEmoji, "Keyboard", "Type=Emoji", "12740-24391.json"),
        Ios27ExampleSpec(Ios27ExampleKeys.FaceIdAuthenticating, "Face ID", "State=Authenticating", "12740-24401.json"),
        Ios27ExampleSpec(Ios27ExampleKeys.FaceIdSuccess, "Face ID", "State=Success", "12740-24409.json"),
        Ios27ExampleSpec(Ios27ExampleKeys.NotificationsExpanded, "Notifications", "Expanded=True", "12740-24418.json"),
        Ios27ExampleSpec(Ios27ExampleKeys.NotificationsCollapsed, "Notifications", "Expanded=False", "12740-24428.json"),
        Ios27ExampleSpec(Ios27ExampleKeys.EmptyStateTitleAndSubtitle, "Empty States", "Config=Title and Subtitle", "12740-24439.json"),
        Ios27ExampleSpec(Ios27ExampleKeys.EmptyStateSymbolInCircle, "Empty States", "Config=Symbol in Circle", "12740-24450.json"),
        Ios27ExampleSpec(Ios27ExampleKeys.EmptyStateLargeSymbol, "Empty States", "Config=Large Symbol", "12740-24462.json"),
        Ios27ExampleSpec(Ios27ExampleKeys.LockScreenDefault, "Lock Screen", "Default", "12740-35191.json"),
        Ios27ExampleSpec(Ios27ExampleKeys.ContextMenuDefault, "Context Menu", "Default", "12740-35183.json"),
        Ios27ExampleSpec(Ios27ExampleKeys.WidgetSmall, "Home Screen Widgets", "Size=Small", "12740-24471.json"),
        Ios27ExampleSpec(Ios27ExampleKeys.WidgetMedium, "Home Screen Widgets", "Size=Medium", "12740-24474.json"),
        Ios27ExampleSpec(Ios27ExampleKeys.WidgetLarge, "Home Screen Widgets", "Size=Large", "12740-24477.json"),
        Ios27ExampleSpec(Ios27ExampleKeys.WidgetExtraLarge, "Home Screen Widgets", "Size=XL", "12740-24480.json"),
        Ios27ExampleSpec(Ios27ExampleKeys.ColorPickerDefault, "Color Picker", "Default", "12740-35184.json"),
        Ios27ExampleSpec(Ios27ExampleKeys.HomeScreenQuickActionsDefault, "Home Screen Quick Actions", "Default", "12740-35187.json"),
        Ios27ExampleSpec(Ios27ExampleKeys.ActionSheetDefault, "Action Sheet", "Default", "12740-35185.json"),
        Ios27ExampleSpec(Ios27ExampleKeys.HomeScreenDefault, "Home Screen", "Default", "12740-35189.json"),
        Ios27ExampleSpec(Ios27ExampleKeys.AlertSideBySide, "Alert", "Type=Side-by-side", "12740-24488.json"),
        Ios27ExampleSpec(Ios27ExampleKeys.AlertStacked, "Alert", "Type=Stacked", "12740-24497.json"),
        Ios27ExampleSpec(Ios27ExampleKeys.ControlCenterDefault, "Control Center", "Default", "12740-35188.json"),
        Ios27ExampleSpec(Ios27ExampleKeys.ActivityViewDefault, "Activity View", "Default", "12740-35190.json"),
    )

    fun byKey(key: String): Ios27ExampleSpec? = all.firstOrNull { it.key == key }

    fun find(category: String, variant: String): Ios27ExampleSpec? = all.firstOrNull {
        it.category == category && (it.variant == variant || it.variant.substringAfter('=') == variant)
    }

    fun variants(category: String): List<Ios27ExampleSpec> = all.filter { it.category == category }
}

private fun Ios27ExampleSpec.toInternalEntry() = IphoneExample(key, category, variant, file)

/**
 * Render any live example by stable key.
 *
 * This is a full-page component. It owns its page colors and keeps the host's
 * Material palette except for the iOS accent used by glass controls.
 */
@Composable
fun Ios27Example(
    key: String,
    modifier: Modifier = Modifier.fillMaxSize(),
    darkTheme: Boolean? = null,
    onBack: () -> Unit = {},
    onReference: () -> Unit = {},
) {
    val spec = remember(key) {
        requireNotNull(Ios27ExampleCatalog.byKey(key)) { "Unknown iOS 27 example key: $key" }
    }
    Ios27Example(spec, modifier, darkTheme, onBack, onReference)
}

/** Render any live example by category and source variant name. */
@Composable
fun Ios27Example(
    category: String,
    variant: String,
    modifier: Modifier = Modifier.fillMaxSize(),
    darkTheme: Boolean? = null,
    onBack: () -> Unit = {},
    onReference: () -> Unit = {},
) {
    val spec = remember(category, variant) {
        requireNotNull(Ios27ExampleCatalog.find(category, variant)) {
            "Unknown iOS 27 example: category=$category, variant=$variant"
        }
    }
    Ios27Example(spec, modifier, darkTheme, onBack, onReference)
}

/** Render a previously resolved descriptor. */
@Composable
fun Ios27Example(
    spec: Ios27ExampleSpec,
    modifier: Modifier = Modifier.fillMaxSize(),
    darkTheme: Boolean? = null,
    onBack: () -> Unit = {},
    onReference: () -> Unit = {},
) {
    MiuixPredictiveBackHandler(
        enabled = true,
        onProgress = { events -> events.collect { } },
        onCommit = { onBack() },
        onCancel = {},
    )
    Ios27ExampleContent(
        spec = spec,
        modifier = modifier,
        darkTheme = darkTheme,
        onBack = onBack,
        onReference = onReference,
    )
}

@Composable
internal fun Ios27ExampleContent(
    spec: Ios27ExampleSpec,
    modifier: Modifier = Modifier.fillMaxSize(),
    darkTheme: Boolean? = null,
    onBack: () -> Unit = {},
    onReference: () -> Unit = {},
) {
    val dark = darkTheme ?: !MaterialTheme.colors.isLight
    val themedMaterialColors = MaterialTheme.colors.copy(
        primary = if (dark) Ios27Tokens.BlueDark else Ios27Tokens.BlueLight,
    )
    MaterialTheme(colors = themedMaterialColors) {
        Ios27PageTheme(dark) {
            Box(modifier.ios27AndroidPane(spec.category + " · " + spec.variant)) {
                key(spec.key) {
                    if (spec.category == "Tab Bar") {
                        MeloXTabBarExamplePage(onBack = onBack)
                    } else if (spec.key == Ios27ExampleKeys.ListInset) {
                        ListDetailPage(dark = dark, onBack = onBack)
                    } else {
                        NativeExamplePage(
                            entry = spec.toInternalEntry(),
                            onBack = onBack,
                            onReference = onReference,
                        )
                    }
                }
            }
        }
    }
}

enum class Ios27TopToolbarVariant(val key: String) {
    Title2LineLeft(Ios27ExampleKeys.TopToolbarTitle2LineLeft),
    Title2Line(Ios27ExampleKeys.TopToolbarTitle2Line),
    LargeTitle(Ios27ExampleKeys.TopToolbarLargeTitle),
    CompactLargeTitle(Ios27ExampleKeys.TopToolbarCompactLargeTitle),
    Default(Ios27ExampleKeys.TopToolbarDefault),
}

enum class Ios27BottomToolbarVariant(val key: String) {
    Search(Ios27ExampleKeys.BottomToolbarSearch),
    Symbols(Ios27ExampleKeys.BottomToolbarSymbols),
    Text(Ios27ExampleKeys.BottomToolbarText),
}

enum class Ios27TabBarVariant(val key: String) {
    Default(Ios27ExampleKeys.TabBarDefault),
    SplitSearch(Ios27ExampleKeys.TabBarSplitSearch),
}

enum class Ios27SheetVariant(val key: String) {
    Fullscreen(Ios27ExampleKeys.SheetFullscreen),
    FullscreenStack(Ios27ExampleKeys.SheetFullscreenStack),
    Inspector(Ios27ExampleKeys.SheetInspector),
}

enum class Ios27PickerVariant(val key: String) {
    Inline(Ios27ExampleKeys.PickerInline),
    Compact(Ios27ExampleKeys.PickerCompact),
}

enum class Ios27MenuVariant(val key: String) {
    EditMedium(Ios27ExampleKeys.MenuEditMedium),
    Menu(Ios27ExampleKeys.MenuDefault),
}

enum class Ios27ListVariant(val key: String) {
    Inset(Ios27ExampleKeys.ListInset),
    FullWidth(Ios27ExampleKeys.ListFullWidth),
    Sectioned(Ios27ExampleKeys.ListSectioned),
}

enum class Ios27KeyboardVariant(val key: String) {
    Lowercase(Ios27ExampleKeys.KeyboardLowercase),
    Uppercase(Ios27ExampleKeys.KeyboardUppercase),
    Punctuation(Ios27ExampleKeys.KeyboardPunctuation),
    Numeric(Ios27ExampleKeys.KeyboardNumeric),
    Find(Ios27ExampleKeys.KeyboardFind),
    FindAndReplace(Ios27ExampleKeys.KeyboardFindAndReplace),
    Email(Ios27ExampleKeys.KeyboardEmail),
    WebSearch(Ios27ExampleKeys.KeyboardWebSearch),
    Url(Ios27ExampleKeys.KeyboardUrl),
    NumberPad(Ios27ExampleKeys.KeyboardNumberPad),
    Emoji(Ios27ExampleKeys.KeyboardEmoji),
}

enum class Ios27FaceIdState(val key: String) {
    Authenticating(Ios27ExampleKeys.FaceIdAuthenticating),
    Success(Ios27ExampleKeys.FaceIdSuccess),
}

enum class Ios27NotificationState(val key: String) {
    Expanded(Ios27ExampleKeys.NotificationsExpanded),
    Collapsed(Ios27ExampleKeys.NotificationsCollapsed),
}

enum class Ios27EmptyStateVariant(val key: String) {
    TitleAndSubtitle(Ios27ExampleKeys.EmptyStateTitleAndSubtitle),
    SymbolInCircle(Ios27ExampleKeys.EmptyStateSymbolInCircle),
    LargeSymbol(Ios27ExampleKeys.EmptyStateLargeSymbol),
}

enum class Ios27WidgetSize(val key: String) {
    Small(Ios27ExampleKeys.WidgetSmall),
    Medium(Ios27ExampleKeys.WidgetMedium),
    Large(Ios27ExampleKeys.WidgetLarge),
    ExtraLarge(Ios27ExampleKeys.WidgetExtraLarge),
}

enum class Ios27AlertVariant(val key: String) {
    SideBySide(Ios27ExampleKeys.AlertSideBySide),
    Stacked(Ios27ExampleKeys.AlertStacked),
}

/**
 * Named wrappers for the catalog. These keep call sites short while still
 * delegating to the single live implementation used by the example library.
 */
object Ios27Examples {
    @Composable
    fun TopToolbar(
        variant: Ios27TopToolbarVariant = Ios27TopToolbarVariant.Default,
        modifier: Modifier = Modifier.fillMaxSize(),
        onBack: () -> Unit = {},
        onReference: () -> Unit = {},
    ) = Ios27Example(variant.key, modifier, onBack = onBack, onReference = onReference)

    @Composable
    fun BottomToolbar(
        variant: Ios27BottomToolbarVariant = Ios27BottomToolbarVariant.Symbols,
        modifier: Modifier = Modifier.fillMaxSize(),
        onBack: () -> Unit = {},
        onReference: () -> Unit = {},
    ) = Ios27Example(variant.key, modifier, onBack = onBack, onReference = onReference)

    @Composable
    fun TabBar(
        variant: Ios27TabBarVariant = Ios27TabBarVariant.Default,
        modifier: Modifier = Modifier.fillMaxSize(),
        onBack: () -> Unit = {},
        onReference: () -> Unit = {},
    ) = Ios27Example(variant.key, modifier, onBack = onBack, onReference = onReference)

    @Composable
    fun TextFields(modifier: Modifier = Modifier.fillMaxSize(), onBack: () -> Unit = {}) =
        Ios27Example(Ios27ExampleKeys.TextFieldsDefault, modifier, onBack = onBack)

    @Composable
    fun Switches(modifier: Modifier = Modifier.fillMaxSize(), onBack: () -> Unit = {}) =
        Ios27Example(Ios27ExampleKeys.SwitchesDefault, modifier, onBack = onBack)

    @Composable
    fun Slider(modifier: Modifier = Modifier.fillMaxSize(), onBack: () -> Unit = {}) =
        Ios27Example(Ios27ExampleKeys.SliderDefault, modifier, onBack = onBack)

    @Composable
    fun Stepper(modifier: Modifier = Modifier.fillMaxSize(), onBack: () -> Unit = {}) =
        Ios27Example(Ios27ExampleKeys.StepperDefault, modifier, onBack = onBack)

    @Composable
    fun Sheet(
        variant: Ios27SheetVariant = Ios27SheetVariant.Fullscreen,
        modifier: Modifier = Modifier.fillMaxSize(),
        onBack: () -> Unit = {},
    ) = Ios27Example(variant.key, modifier, onBack = onBack)

    @Composable
    fun Picker(
        variant: Ios27PickerVariant = Ios27PickerVariant.Inline,
        modifier: Modifier = Modifier.fillMaxSize(),
        onBack: () -> Unit = {},
    ) = Ios27Example(variant.key, modifier, onBack = onBack)

    @Composable
    fun Menu(
        variant: Ios27MenuVariant = Ios27MenuVariant.Menu,
        modifier: Modifier = Modifier.fillMaxSize(),
        onBack: () -> Unit = {},
    ) = Ios27Example(variant.key, modifier, onBack = onBack)

    @Composable
    fun List(
        variant: Ios27ListVariant = Ios27ListVariant.Inset,
        modifier: Modifier = Modifier.fillMaxSize(),
        onBack: () -> Unit = {},
    ) = Ios27Example(variant.key, modifier, onBack = onBack)

    @Composable
    fun Keyboard(
        variant: Ios27KeyboardVariant = Ios27KeyboardVariant.Lowercase,
        modifier: Modifier = Modifier.fillMaxSize(),
        onBack: () -> Unit = {},
    ) = Ios27Example(variant.key, modifier, onBack = onBack)

    @Composable
    fun FaceId(
        state: Ios27FaceIdState = Ios27FaceIdState.Authenticating,
        modifier: Modifier = Modifier.fillMaxSize(),
        onBack: () -> Unit = {},
    ) = Ios27Example(state.key, modifier, onBack = onBack)

    @Composable
    fun Notifications(
        state: Ios27NotificationState = Ios27NotificationState.Expanded,
        modifier: Modifier = Modifier.fillMaxSize(),
        onBack: () -> Unit = {},
    ) = Ios27Example(state.key, modifier, onBack = onBack)

    @Composable
    fun EmptyState(
        variant: Ios27EmptyStateVariant = Ios27EmptyStateVariant.TitleAndSubtitle,
        modifier: Modifier = Modifier.fillMaxSize(),
        onBack: () -> Unit = {},
    ) = Ios27Example(variant.key, modifier, onBack = onBack)

    @Composable
    fun LockScreen(modifier: Modifier = Modifier.fillMaxSize(), onBack: () -> Unit = {}) =
        Ios27Example(Ios27ExampleKeys.LockScreenDefault, modifier, onBack = onBack)

    @Composable
    fun ContextMenu(modifier: Modifier = Modifier.fillMaxSize(), onBack: () -> Unit = {}) =
        Ios27Example(Ios27ExampleKeys.ContextMenuDefault, modifier, onBack = onBack)

    @Composable
    fun Widget(
        size: Ios27WidgetSize = Ios27WidgetSize.Small,
        modifier: Modifier = Modifier.fillMaxSize(),
        onBack: () -> Unit = {},
    ) = Ios27Example(size.key, modifier, onBack = onBack)

    @Composable
    fun ColorPicker(modifier: Modifier = Modifier.fillMaxSize(), onBack: () -> Unit = {}) =
        Ios27Example(Ios27ExampleKeys.ColorPickerDefault, modifier, onBack = onBack)

    @Composable
    fun HomeScreenQuickActions(modifier: Modifier = Modifier.fillMaxSize(), onBack: () -> Unit = {}) =
        Ios27Example(Ios27ExampleKeys.HomeScreenQuickActionsDefault, modifier, onBack = onBack)

    @Composable
    fun ActionSheet(modifier: Modifier = Modifier.fillMaxSize(), onBack: () -> Unit = {}) =
        Ios27Example(Ios27ExampleKeys.ActionSheetDefault, modifier, onBack = onBack)

    @Composable
    fun HomeScreen(modifier: Modifier = Modifier.fillMaxSize(), onBack: () -> Unit = {}) =
        Ios27Example(Ios27ExampleKeys.HomeScreenDefault, modifier, onBack = onBack)

    @Composable
    fun Alert(
        variant: Ios27AlertVariant = Ios27AlertVariant.SideBySide,
        modifier: Modifier = Modifier.fillMaxSize(),
        onBack: () -> Unit = {},
    ) = Ios27Example(variant.key, modifier, onBack = onBack)

    @Composable
    fun ControlCenter(modifier: Modifier = Modifier.fillMaxSize(), onBack: () -> Unit = {}) =
        Ios27Example(Ios27ExampleKeys.ControlCenterDefault, modifier, onBack = onBack)

    @Composable
    fun ActivityView(modifier: Modifier = Modifier.fillMaxSize(), onBack: () -> Unit = {}) =
        Ios27Example(Ios27ExampleKeys.ActivityViewDefault, modifier, onBack = onBack)
}
