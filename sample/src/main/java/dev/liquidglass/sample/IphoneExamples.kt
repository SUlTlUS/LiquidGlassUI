package dev.liquidglass.sample

import android.os.Build
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.foundation.ScrollState
import com.zeaze.tianyinwallpaper.backdrop.backdrops.LayerBackdrop
import com.zeaze.tianyinwallpaper.backdrop.backdrops.layerBackdrop
import com.zeaze.tianyinwallpaper.backdrop.backdrops.rememberLayerBackdrop
import com.zeaze.tianyinwallpaper.backdrop.Backdrop
import com.zeaze.tianyinwallpaper.catalog.components.LiquidButton
import com.zeaze.tianyinwallpaper.ui.commom.ProgressiveBlurContent
import dev.liquidglass.ios27.*
import dev.liquidglass.coloros17.ColorOs17ComponentCatalog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import kotlinx.serialization.Serializable
import top.yukonga.miuix.kmp.nav.core.NavDisplay
import top.yukonga.miuix.kmp.nav.core.NavKey
import top.yukonga.miuix.kmp.nav.core.rememberNavBackStack
import top.yukonga.miuix.kmp.nav.transition.NavTransitions

internal data class IphoneExample(val key: String, val category: String, val variant: String, val file: String)

@Serializable
private sealed interface IphoneRoute : NavKey {
    @Serializable
    data object Root : IphoneRoute

    @Serializable
    data class Category(val name: String) : IphoneRoute

    @Serializable
    data class Example(val key: String) : IphoneRoute

    @Serializable
    data class Reference(val key: String) : IphoneRoute
}

private const val CategoryLargeTitleToolbarSceneFile = "12740-24013.json"
internal const val MeloXTabBarExampleKey = "melox-tabbar"
private val MeloXTabBarExampleEntry = IphoneExample(
    MeloXTabBarExampleKey, "MeloX TabBar", "原版交互", "",
)
internal const val ListeningModeExampleKey = "listening-mode"
private val ListeningModeExampleEntry = IphoneExample(
    ListeningModeExampleKey, "Listening Mode", "四档听音模式", "",
)
private val Ios27MechanismExamples = Ios27MechanismExample.entries.map {
    IphoneExample(it.key, "iOS 27", it.title, "")
}

// These system-surface examples are removed from the product-facing library.
// Their extracted assets and native implementations remain available for tooling.
private val HiddenPhoneExampleCategories = setOf(
    "Lock Screen",
    "Home Screen",
    "Home Screen Quick Actions",
    "Control Center",
    "Face ID",
    "Notifications",
    "Keyboard",
    "Home Screen Widgets",
)

@Composable
internal fun Ios27BackButton(
    onBack: () -> Unit,
    backdrop: Backdrop?,
    modifier: Modifier = Modifier,
    backGlyph: (@Composable () -> Unit)? = null,
) {
    val colors = LocalIos27PageColors.current
    val buttonModifier = modifier.then(Modifier.size(44.dp))
    val glyphContent: @Composable BoxScope.() -> Unit = {
        if (backGlyph != null) {
            backGlyph()
        } else {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Ios27IndicatorGlyph(
                    variant = Ios27IndicatorVariant.Back,
                    color = colors.label,
                )
            }
        }
    }
    if (backdrop != null) {
        LiquidButton(
            onBack,
            backdrop,
            buttonModifier.semantics { contentDescription = "返回" },
            contentPadding = PaddingValues(0.dp),
            content = { Box(Modifier.fillMaxSize()) { glyphContent() } },
        )
    } else {
        val shape = RoundedCornerShape(22.dp)
        Box(
            buttonModifier
                .background(colors.group, shape)
                .ios27PressedClickable(shape = shape, onClick = onBack)
                .semantics { contentDescription = "返回" },
        ) { glyphContent() }
    }
}

/**
 * Top chrome replicating the original Toolbar - Top: 44pt glass circles at x=16 / x=342,
 * centered glyph title. The reference view needs no overlay because the scene already
 * contains these elements. Long-pressing the title opens the reference view.
 * Must be called inside the page's pt-aligned density (1dp = 1 design pt).
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun BoxScope.ExampleNavBar(
    file: String,
    onBack: () -> Unit,
    onReference: () -> Unit,
    onTrailing: () -> Unit,
    backdrop: com.zeaze.tianyinwallpaper.backdrop.Backdrop?,
    fallbackTitle: String? = null,
    trailingOverflow: Boolean = false,
    showTrailing: Boolean = true,
) {
    val colors = LocalIos27PageColors.current
    val dark = !MaterialTheme.colors.isLight
    val toolbar = rememberSceneToolbar(file)
    // When a toolbar owns a large title, the page draws it in the content layer.
    // Do not fall back to the first nested Title or it will appear twice.
    val title = toolbar?.title ?: if (toolbar == null) rememberSceneText(file, "Title") else null
    Box(Modifier.align(Alignment.TopCenter).fillMaxWidth()
        .padding(top = WindowInsets.safeDrawing.asPaddingValues().calculateTopPadding())
        .height(48.dp)) {
        val visibleButtons = toolbar?.buttons?.let { buttons ->
            if (showTrailing) buttons else buttons.take(1)
        }.orEmpty()
        visibleButtons.forEach { button ->
            Box(Modifier.align(Alignment.TopStart).offset(button.x.dp, button.y.dp).size(button.w.dp, button.h.dp)) {
                // Buttons on the left half navigate back; the rest are demonstrated actions.
                val onClick = if (button.x < 201f) onBack else onTrailing
                // Glyphs live inside the button content so press scale/translation carries them along.
                val glyphContent: @Composable () -> Unit = {
                    if (trailingOverflow && button.x >= 201f) {
                        Canvas(Modifier.fillMaxSize()) {
                            val dotColor = if (dark) colors.label else Color(0xFF1C1C1E)
                            val radius = 2.dp.toPx()
                            val gap = 7.dp.toPx()
                            drawCircle(dotColor, radius, Offset(size.width / 2f - gap, size.height / 2f))
                            drawCircle(dotColor, radius, Offset(size.width / 2f, size.height / 2f))
                            drawCircle(dotColor, radius, Offset(size.width / 2f + gap, size.height / 2f))
                        }
                    } else {
                        SceneToolbarButtonContent(
                            button = button,
                            tint = colors.label,
                            useSourceColor = !dark || button.tinted,
                        )
                    }
                }
                if (backdrop != null) {
                    LiquidButton(
                        onClick,
                        backdrop,
                        Modifier.fillMaxSize(),
                        tint = if (button.tinted) colors.accent else Color.Unspecified,
                        contentPadding = PaddingValues(0.dp),
                        content = { Box(Modifier.fillMaxSize()) { glyphContent() } },
                    )
                } else {
                    val shape = RoundedCornerShape(button.h.dp / 2)
                    Box(
                        Modifier.fillMaxSize()
                            .background(colors.group, shape)
                            .ios27PressedClickable(shape = shape, onClick = onClick),
                    ) { glyphContent() }
                }
            }
        }
        if (title != null) {
            val titleTint = if (dark) colors.label else title.color ?: colors.label
            val titleModifier = if (toolbar?.titleX != null && toolbar.titleY != null) {
                Modifier.align(Alignment.TopStart).offset(toolbar.titleX.dp, toolbar.titleY.dp)
            } else Modifier.align(Alignment.Center)
            Box(
                titleModifier.combinedClickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = {},
                    onLongClick = onReference,
                ),
            ) {
                SceneGlyphText(title, titleTint)
            }
            toolbar?.subtitle?.let { subtitle ->
                Box(Modifier.offset((toolbar.subtitleX ?: toolbar.titleX ?: 0f).dp,
                    (toolbar.subtitleY ?: ((toolbar.titleY ?: 0f) + title.height)).dp)) {
                    SceneGlyphText(subtitle, if (dark) colors.label else subtitle.color ?: colors.secondaryLabel)
                }
            }
        } else if (fallbackTitle != null) {
            Box(
                Modifier.align(Alignment.Center).combinedClickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = {},
                    onLongClick = onReference,
                ),
            ) {
                Text(fallbackTitle, color = colors.label, style = Ios27Tokens.Type.Headline)
            }
        }
    }
}

@Composable
internal fun IphoneExamples(
    initialExample: String? = null,
    initialReference: Boolean = false,
    onBack: () -> Unit,
    rootBackdrop: LayerBackdrop? = null,
    rootScrollState: ScrollState? = null,
    onSecondaryStateChanged: (Boolean) -> Unit = {},
) {
    IphoneExamplesMiuix(
        initialExample = initialExample,
        initialReference = initialReference,
        onBack = onBack,
        rootBackdrop = rootBackdrop,
        rootScrollState = rootScrollState,
        onSecondaryStateChanged = onSecondaryStateChanged,
    )
}

@Composable
private fun IphoneExamplesMiuix(
    initialExample: String?,
    initialReference: Boolean,
    onBack: () -> Unit,
    rootBackdrop: LayerBackdrop?,
    rootScrollState: ScrollState?,
    onSecondaryStateChanged: (Boolean) -> Unit,
) {
    val context = LocalContext.current
    val colors = LocalIos27PageColors.current
    val entries by produceState<List<IphoneExample>>(emptyList()) {
        value = withContext(Dispatchers.IO) {
            val array = JSONArray(
                context.assets.open("iphone-examples/catalog.json")
                    .bufferedReader()
                    .use { it.readText() },
            )
            List(array.length()) { index ->
                array.getJSONObject(index).let {
                    IphoneExample(
                        it.getString("key"),
                        it.getString("category"),
                        it.getString("variant"),
                        it.getString("file"),
                    )
                }
            }.filterNot { it.category in HiddenPhoneExampleCategories } + MeloXTabBarExampleEntry + ListeningModeExampleEntry +
                Ios27MechanismExamples +
                ColorOs17ComponentCatalog.entries.map { IphoneExample(it.key, it.category, it.title, "") }
        }
    }

    if (entries.isEmpty()) {
        Box(
            Modifier.fillMaxSize().background(colors.page),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator()
        }
        return
    }

    val initialEntry = entries.firstOrNull { it.key == initialExample }
    val initialRoutes = remember(entries, initialExample, initialReference) {
        buildList<IphoneRoute> {
            add(IphoneRoute.Root)
            if (initialEntry != null) {
                add(IphoneRoute.Category(initialEntry.category))
                add(IphoneRoute.Example(initialEntry.key))
                if (initialReference && initialEntry.file.isNotBlank()) {
                    add(IphoneRoute.Reference(initialEntry.key))
                }
            }
        }
    }
    val backStack = rememberNavBackStack<IphoneRoute>(*initialRoutes.toTypedArray())

    val popBack: () -> Unit = {
        if (backStack.size > 1) {
            backStack.removeAt(backStack.lastIndex)
        } else {
            onBack()
        }
    }

    LaunchedEffect(backStack.size) {
        onSecondaryStateChanged(backStack.size > 1)
    }

    NavDisplay(
        backStack = backStack,
        modifier = Modifier.fillMaxSize(),
        onBack = popBack,
        transition = NavTransitions.MiuixDefault,
    ) {
        entry<IphoneRoute.Root> {
            IphoneRootBackPreview(
                entries = entries,
                colors = colors,
                scrollState = rootScrollState,
                rootBackdrop = rootBackdrop,
                onCategory = { name ->
                    backStack.add(IphoneRoute.Category(name))
                },
            )
        }

        entry<IphoneRoute.Category> { route ->
            IphoneCategoryBackPreview(
                category = route.name,
                entries = entries,
                colors = colors,
                scrollState = rootScrollState,
                onBack = popBack,
                onExample = { example ->
                    backStack.add(IphoneRoute.Example(example.key))
                },
            )
        }

        entry<IphoneRoute.Example> { route ->
            val entry = entries.firstOrNull { it.key == route.key }
            if (entry?.category == "iOS 27") {
                Ios27MechanismExamplePage(entry.key, popBack)
            } else if (entry?.key == MeloXTabBarExampleKey) {
                MeloXTabBarExamplePage(onBack = popBack)
            } else if (entry?.key == ListeningModeExampleKey) {
                ListeningModeExamplePage(onBack = popBack)
            } else if (entry != null && ColorOs17ComponentCatalog.find(entry.key) != null) {
                ColorOs17ExamplePage(ColorOs17ComponentCatalog.find(entry.key)!!, onBack = popBack)
            } else if (entry != null) {
                Ios27ExampleContent(
                    spec = Ios27ExampleSpec(
                        entry.key,
                        entry.category,
                        entry.variant,
                        entry.file,
                    ),
                    onBack = popBack,
                    onReference = {
                        backStack.add(IphoneRoute.Reference(entry.key))
                    },
                )
            }
        }

        entry<IphoneRoute.Reference> { route ->
            val entry = entries.firstOrNull { it.key == route.key }
            if (entry != null) {
                IphoneReferenceRoute(
                    entry = entry,
                    entries = entries,
                    colors = colors,
                    onBack = popBack,
                    onSelectVariant = { key ->
                        val top = backStack.lastIndex
                        if (top > 0 && backStack[top] is IphoneRoute.Reference) {
                            if (backStack[top - 1] is IphoneRoute.Example) {
                                backStack[top - 1] = IphoneRoute.Example(key)
                            }
                            backStack[top] = IphoneRoute.Reference(key)
                        }
                    },
                    onBackToCategory = {
                        while (backStack.size > 1 &&
                            backStack.last() !is IphoneRoute.Category
                        ) {
                            backStack.removeAt(backStack.lastIndex)
                        }
                    },
                )
            }
        }
    }
}

@Composable
private fun IphoneCategoryBackPreview(
    category: String,
    entries: List<IphoneExample>,
    colors: Ios27PageColors,
    scrollState: ScrollState?,
    onBack: () -> Unit = {},
    onExample: (IphoneExample) -> Unit = {},
) {
    val previewScroll = rememberScrollState(scrollState?.value ?: 0)

    ExampleToolbarDesignPage(
        background = colors.page,
        isLightTheme = MaterialTheme.colors.isLight,
        toolbarFile = CategoryLargeTitleToolbarSceneFile,
        onBack = onBack,
        showTrailing = false,
        showBlur = false,
        content = { topInset, _ ->
            Ios27PageContent(
                Modifier.fillMaxSize()
                    .verticalScroll(previewScroll)
                    .padding(top = topInset + 54.dp, bottom = 24.dp),
            ) {
                Box(Modifier.fillMaxWidth().height(61.dp)) {
                    Text(
                        categoryTitle(category),
                        style = Ios27Tokens.Type.LargeTitle.copy(fontWeight = FontWeight.Bold),
                        color = colors.label,
                    )
                }
                Ios27ListSection {
                    Ios27ListRows(entries.filter { it.category == category }) { example ->
                        Ios27ListRow(
                            example.variant.substringAfter('='),
                            onClick = { onExample(example) },
                        )
                    }
                }
                Spacer(Modifier.height(24.dp))
            }
        },
    )
}

@Composable
private fun IphoneReferenceRoute(
    entry: IphoneExample,
    entries: List<IphoneExample>,
    colors: Ios27PageColors,
    onBack: () -> Unit,
    onSelectVariant: (String) -> Unit,
    onBackToCategory: () -> Unit,
) {
    val context = LocalContext.current
    var showControls by remember { mutableStateOf(false) }
    val scene by produceState<JSONObject?>(null, entry.key) {
        value = withContext(Dispatchers.IO) {
            JSONObject(
                context.assets.open("iphone-examples/${entry.file}")
                    .bufferedReader()
                    .use { it.readText() },
            )
        }
    }

    Box(
        Modifier.fillMaxSize()
            .background(colors.page)
            .safeDrawingPadding(),
    ) {
        AndroidView(
            factory = { FigmaSceneView(it) },
            modifier = Modifier.fillMaxSize(),
            update = { view ->
                if (view.scene !== scene) view.scene = scene
                view.contentDescription = "${entry.category}: ${entry.variant}"
                view.onShowVariants = { showControls = true }
                view.onSceneAction = { nodes ->
                    val names = nodes.map { it.optString("name") }
                    val texts = nodes.map { it.optString("text") }
                    if (
                        names.any {
                            it.equals("Back", true) ||
                                it.contains("Back Button", true) ||
                                it.contains("Close", true) ||
                                it.contains("Dismiss", true)
                        } ||
                        texts.any { it in setOf("Cancel", "Done", "Close") }
                    ) {
                        onBack()
                    }
                }
            },
        )
        if (scene == null) {
            CircularProgressIndicator(Modifier.align(Alignment.Center))
        }
    }

    if (showControls) {
        AlertDialog(
            onDismissRequest = { showControls = false },
            title = { Text(categoryTitle(entry.category)) },
            text = {
                Column {
                    Text("长按页面切换原稿变体；系统返回键回到目录。")
                    entries.filter { it.category == entry.category }.forEach { variant ->
                        TextButton(
                            onClick = {
                                onSelectVariant(variant.key)
                                showControls = false
                            },
                        ) {
                            Text(variant.variant.substringAfter('='))
                        }
                    }
                    TextButton(
                        onClick = {
                            onBackToCategory()
                            showControls = false
                        },
                    ) {
                        Text("返回示例目录")
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showControls = false }) {
                    Text("关闭")
                }
            },
        )
    }
}

@Composable
private fun IphoneRootBackPreview(
    entries: List<IphoneExample>,
    colors: Ios27PageColors,
    scrollState: ScrollState?,
    rootBackdrop: LayerBackdrop? = null,
    onCategory: (String) -> Unit = {},
) {
    val previewScroll = scrollState ?: rememberScrollState()
    val blurRevealDistance = with(LocalDensity.current) { 30.dp.toPx() }
    val pageBackdrop = rootBackdrop ?: rememberExamplePageBackdrop(colors.page)
    val categories = entries.groupBy { it.category }
    val categoryGroups = listOf(
        listOf("iOS 27"),
        listOf(
            "Toolbars - Top",
            "Toolbars - Bottom",
            "Tab Bar",
            "MeloX TabBar",
            "Listening Mode",
            "Sheets (Modals)",
            "Control Center",
            "Activity View",
        ),
        listOf(
            "Alert",
            "Action Sheet",
            "Color Picker",
            "Face ID",
            "Notifications",
        ),
        listOf(
            "Menus",
            "Context Menu",
            "Home Screen Quick Actions",
        ),
        listOf(
            "Text Fields",
            "Toggles (Switches)",
            "Slider",
            "Steppers",
            "Picker",
            "List",
            "Keyboard",
            "Empty States",
            "Lock Screen",
            "Home Screen Widgets",
            "Home Screen",
        ),
    ).plus(listOf(ColorOs17ComponentCatalog.entries.map { it.category }.distinct())).map { group ->
        group.mapNotNull { name -> categories[name]?.let { variants -> name to variants } }
    }.filter { it.isNotEmpty() }

    ExampleRecordedResponsivePage(
        background = colors.page,
        backdrop = pageBackdrop,
        content = { topInset, _ ->
            Ios27PageContent(
                Modifier.fillMaxSize()
                    .verticalScroll(previewScroll)
                    .padding(top = topInset, bottom = 108.dp),
            ) {
                Ios27PageHeader(
                    "手机示例",
                    if (entries.isNotEmpty()) "iOS 27 新机制 · iOS 26 原有示例\n${entries.count { it.file.isNotBlank() }} 个原稿变体 · ${entries.count { it.file.isBlank() }} 个源码示例" else null,
                )
                categoryGroups.forEachIndexed { index, group ->
                    Ios27ListSection {
                        Ios27ListRows(group) { (name, variants) ->
                            Ios27ListRow(
                                categoryTitle(name),
                                detail = "${variants.size}",
                                onClick = { onCategory(name) },
                            )
                        }
                    }
                    if (index != categoryGroups.lastIndex) {
                        Spacer(Modifier.height(Ios27Tokens.GroupGap.dp))
                    }
                }
                Spacer(Modifier.height(24.dp))
            }
        },
        overlay = { _, backdrop ->
            ExampleTopChrome(
                backdrop = if (Build.VERSION.SDK_INT >= 33) backdrop else null,
                isLightTheme = MaterialTheme.colors.isLight,
                blurAlpha = (previewScroll.value / blurRevealDistance).coerceIn(0f, 1f),
                scrollDriven = true,
            )
        },
    )
}

internal fun categoryTitle(name: String): String = mapOf(
    "Listening Mode" to "听音模式",
    "Toolbars - Top" to "顶部工具栏", "Toolbars - Bottom" to "底部工具栏", "Tab Bar" to "标签栏",
    "Sheets (Modals)" to "模态页面", "Picker" to "日期与时间", "Menus" to "菜单", "List" to "列表",
    "Keyboard" to "键盘", "Face ID" to "面容识别", "Notifications" to "通知", "Empty States" to "空状态",
    "Home Screen Widgets" to "桌面小组件", "Alert" to "提示弹窗", "Context Menu" to "上下文菜单",
    "Color Picker" to "颜色选择", "Action Sheet" to "操作面板", "Slider" to "滑块",
    "Home Screen Quick Actions" to "桌面快捷操作", "Control Center" to "控制中心", "Home Screen" to "桌面",
    "Activity View" to "分享面板", "Lock Screen" to "锁屏", "Steppers" to "步进器",
    "Toggles (Switches)" to "开关", "Text Fields" to "文本框",
)[name] ?: name
