package dev.liquidglass.sample

import android.os.Build
import android.os.Bundle
import android.content.Context
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zeaze.tianyinwallpaper.backdrop.backdrops.rememberLayerBackdrop
import com.zeaze.tianyinwallpaper.backdrop.backdrops.layerBackdrop
import dev.liquidglass.ios27.*
import dev.liquidglass.standard.LiquidGlassStandard
import dev.liquidglass27.glass.prepareIos27Glass
import com.zeaze.tianyinwallpaper.ui.commom.ProgressiveBlurContent

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        prepareIos27Glass(applicationContext)
        setContent {
            val debuggable = applicationInfo.flags and android.content.pm.ApplicationInfo.FLAG_DEBUGGABLE != 0
            SettingsSample(
                initialExample = if (debuggable) intent.getStringExtra("example_key") else null,
                initialReference = debuggable && intent.getBooleanExtra("example_reference", false),
            ) { dark ->
                WindowCompat.getInsetsController(window, window.decorView).apply {
                    isAppearanceLightStatusBars = !dark
                    isAppearanceLightNavigationBars = !dark
                }
            }
        }
    }
}

@Composable
private fun SettingsSample(initialExample: String? = null, initialReference: Boolean = false, onThemeChanged: (Boolean) -> Unit) {
    val context = LocalContext.current
    val preferences = remember(context) { context.getSharedPreferences("sample_appearance", Context.MODE_PRIVATE) }
    var themeMode by rememberSaveable { mutableIntStateOf(preferences.getInt("theme", 0).coerceIn(0, 2)) }
    var standardName by rememberSaveable {
        mutableStateOf(preferences.getString("glass_standard", LiquidGlassStandard.Ios27.name)!!)
    }
    val standard = (LiquidGlassStandard.entries.firstOrNull { it.name == standardName }
        ?: LiquidGlassStandard.Ios27).supportedOn(Build.VERSION.SDK_INT)
    var tab by rememberSaveable { mutableIntStateOf(0) }
    var examples by rememberSaveable { mutableStateOf(initialExample != null) }
    var examplesSecondary by rememberSaveable { mutableStateOf(false) }
    var backgroundError by rememberSaveable { mutableStateOf<String?>(null) }
    val popupBackground = rememberPopupBackground { backgroundError = it }
    val dark = when (themeMode) { 1 -> false; 2 -> true; else -> isSystemInDarkTheme() }
    SideEffect { onThemeChanged(dark) }
    // Existing MaterialTheme is configured by the host, independently of the new page theme.
    // Glass controls read MaterialTheme primary as their tint; keep it on the page accent.
    MaterialTheme(colors = if (dark) darkColors(primary = Ios27Tokens.BlueDark) else lightColors(primary = Ios27Tokens.BlueLight)) {
        Ios27PageTheme(dark) {
            val colors = LocalIos27PageColors.current
            val scrollState = rememberScrollState()
            if (examples) {
                IphoneExamples(
                    initialExample = if (examples) initialExample else null,
                    initialReference = if (examples) initialReference else false,
                    onBack = { if (examples) examples = false else tab = 0 },
                    onSecondaryStateChanged = { examplesSecondary = it },
                )
                return@Ios27PageTheme
            }
            val systemDensity = LocalDensity.current
            val topInset = WindowInsets.safeDrawing.asPaddingValues().calculateTopPadding()
            val blurRevealDistance = with(LocalDensity.current) { 30.dp.toPx() }
            // Capture the page color and scrolling content; the tabs are a sibling
            // outside this recording. Row controls use their own independent sources.
            val backdrop = rememberExamplePageBackdrop(colors.page)
            // Measure the current window in system dp, not in scaled content dp.
            // Insets/scrolling must not change the short side used for this calculation.
            BoxWithConstraints(Modifier.fillMaxSize()) {
                val settingsDensity = ios27WindowDensity(systemDensity, maxWidth.value, maxHeight.value)
                Box(Modifier.fillMaxSize().background(colors.page)
                    .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom))) {
                    // Record the complete tab-page transition once. The floating Tab Bar stays
                    // outside this layer and samples the actual animated page behind it.
                    ExampleBackdropLayer(
                        backdrop = backdrop,
                        background = colors.page,
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        QweaTabPageTransition(
                            selectedTabIndex = tab,
                            modifier = Modifier.fillMaxSize(),
                        ) { page ->
                            when (page) {
                                0 -> IphoneExamples(
                                    onBack = { tab = 0 },
                                    // This page owns internal glass chrome. Give it a separate
                                    // backdrop so it never records into the outer tab backdrop.
                                    rootBackdrop = null,
                                    rootScrollState = scrollState,
                                    onSecondaryStateChanged = { examplesSecondary = it },
                                )
                                1, 2 -> Box(Modifier.fillMaxSize().background(colors.page)) {
                                    val pageContent: @Composable () -> Unit = {
                                        // Chrome remains at system density. Preserve its physical
                                        // clearance when the settings content uses a smaller dp.
                                        val contentDensity = LocalDensity.current
                                        val contentTopInset = with(contentDensity) {
                                            with(systemDensity) { topInset.toPx() }.toDp()
                                        }
                                        val contentBottomInset = with(contentDensity) {
                                            with(systemDensity) { 108.dp.toPx() }.toDp()
                                        }
                                        Ios27PageContent(
                                            Modifier.fillMaxSize()
                                                .verticalScroll(scrollState)
                                                .padding(top = contentTopInset, bottom = contentBottomInset),
                                        ) {
                                            when (page) {
                                                1 -> NotesPage()
                                                2 -> SettingsPage(
                                                    standard = standard,
                                                    onStandardSelected = {
                                                        standardName = it.name
                                                        preferences.edit().putString("glass_standard", it.name).apply()
                                                    },
                                                    themeMode = themeMode,
                                                    popupBackground = popupBackground,
                                                    backgroundError = backgroundError,
                                                    onBackgroundErrorClear = { backgroundError = null },
                                                    onThemeSelected = { value ->
                                                        themeMode = value
                                                        preferences.edit().putInt("theme", value).apply()
                                                    },
                                                )
                                            }
                                        }
                                    }
                                    if (page == 2) {
                                        Ios27AndroidDesignBehavior(settingsDensity, content = pageContent)
                                    } else {
                                        pageContent()
                                    }
                                }
                            }
                        }
                    }
                    // Components own their top blur inside the root navigation route so it
                    // participates in forward/back transitions without leaking across routes.
                    if (tab != 0) {
                        ExampleTopChrome(
                            backdrop = if (Build.VERSION.SDK_INT >= 33) backdrop else null,
                            isLightTheme = !dark,
                            blurAlpha = (scrollState.value / blurRevealDistance).coerceIn(0f, 1f),
                            surfaceColor = colors.page,
                            scrollDriven = true,
                        )
                    }
                    if (!(tab == 0 && examplesSecondary)) {
                        Box(Modifier.align(Alignment.BottomCenter).widthIn(max = 640.dp)
                            .fillMaxWidth().padding(horizontal = 20.dp, vertical = 12.dp)) {
                            if (standard == LiquidGlassStandard.Ios27 && Build.VERSION.SDK_INT >= 33) {
                                Ios27StandardDock(backdrop, dark, tab, { tab = it })
                            } else Ios27TabBarExample(
                                file = "12740-23997.json",
                                selectedTabIndex = tab,
                                onTabSelected = { tab = it },
                                backdrop = if (Build.VERSION.SDK_INT >= 33) backdrop else null,
                                isLightTheme = !dark,
                                modifier = Modifier.align(Alignment.Center),
                                splitSearch = false,
                                labels = listOf("组件", "说明", "设置"),
                                labelContent = { index, label, tint ->
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        DemoIcon(listOf(1, 2, 0)[index], tint, Modifier.size(20.dp))
                                        Text(label, color = tint, fontSize = 11.sp, lineHeight = 14.sp)
                                    }
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingsPage(
    standard: LiquidGlassStandard,
    onStandardSelected: (LiquidGlassStandard) -> Unit,
    themeMode: Int,
    popupBackground: PopupBackground,
    backgroundError: String?,
    onBackgroundErrorClear: () -> Unit,
    onThemeSelected: (Int) -> Unit,
) {
    val colors = LocalIos27PageColors.current
    Ios27PageHeader("设置", "页面与列表的设计参考")
    Ios27ListSection(title = "玻璃标准", footer = "切换应用导航的玻璃风格。原有组件与原稿示例保留为 iOS 26。") {
        Ios27ListRows(LiquidGlassStandard.entries) { choice ->
            val available = choice.supportedOn(Build.VERSION.SDK_INT) == choice
            Ios27ListRow(choice.label,
                detail = if (!available) "需要 Android 13" else null,
                onClick = if (available) ({ onStandardSelected(choice) }) else null,
                disclosure = false,
                modifier = Modifier.semantics { selected = standard == choice },
                trailing = { if (standard == choice) DemoCheck(colors.accent) },
            )
        }
    }
    Ios27ListSection(title = "配色", footer = "选择会保存在本机。“跟随系统”自动适应系统深浅色。") {
        Ios27ListRows(listOf("跟随系统", "浅色", "深色").withIndex().toList()) { indexed ->
            val index = indexed.index
            val label = indexed.value
            Ios27ListRow(label, onClick = { onThemeSelected(index) }, disclosure = false,
                modifier = Modifier.semantics { selected = themeMode == index },
            trailing = { if (themeMode == index) DemoCheck(colors.accent) })
        }
    }
    Ios27ListSection(title = "背景图片", footer = "应用到支持背景采样的示例页面和液态玻璃弹窗。") {
        Ios27ListRow("当前背景", detail = if (popupBackground.image == null) "未选择" else "已选择")
        Ios27ActionButton("选择背景图片", {
            onBackgroundErrorClear()
            popupBackground.choose()
        })
        if (popupBackground.image != null) {
            Ios27ActionButton("移除背景图片", popupBackground.clear)
        }
        if (backgroundError != null) {
            Text(backgroundError, color = colors.secondaryLabel, style = Ios27Tokens.Type.Footnote,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp))
        }
    }
}

@Composable
private fun NotesPage() {
    val colors = LocalIos27PageColors.current
    Ios27PageHeader("使用说明", "布局层与玻璃层保持独立")
    Ios27ListSection {
        Ios27ListRow("界面与显示", subtitle = "统一底色、文字与内容间距",
            leading = { Ios27IconTile(colors.accent, tall = true) {
                DemoIcon(0, Color.White, Modifier.size(26.dp))
            } })
    }
    Ios27ListSection(title = "内容布局", emphasis = Ios27SectionEmphasis.Prominent,
        footer = "列表使用实色背景；玻璃导航和交互控件沿用原提取代码。") {
        Ios27ListRows(listOf(
            "标准列表" to "52 dp", "双行列表" to "68 dp", "页面边距" to "16 dp",
            "分组间距" to "10 dp", "分组圆角" to "26 dp",
        )) { (label, detail) ->
            Ios27ListRow(label, detail = detail,
                subtitle = if (label == "双行列表") "说明文字与标题自然分层" else null)
        }
    }
    Ios27ListSection(title = "阅读体验", emphasis = Ios27SectionEmphasis.ExtraProminent) {
        Ios27ListRow("大字体支持", subtitle = "行高随内容增长，保持完整显示；长标题与多行中文不会被固定高度截断。",
            leading = { Ios27IconTile(colors.accent) {
                Text("Aa", color = Color.White, style = Ios27Tokens.Type.Footnote)
            } })
    }
    Ios27ListSection(title = "布局层") {
        Ios27ListRow("Ios27PageTheme", subtitle = "只向页面和列表提供颜色，不覆盖 MaterialTheme。")
        Ios27ListDivider()
        Ios27ListRow("Ios27ListSection", subtitle = "整组圆角、连续行与内缩分隔线；无需为每行单独画胶囊。")
        Ios27ListDivider()
        Ios27ListRow("Ios27ListRow", subtitle = "支持标题、说明、详情、前置图标和现有交互控件。")
    }
    Ios27ListSection(title = "玻璃层", footer = "设计来源：seunghan91/ios27-design-system。Android 适配与参考数据的边界见项目文档。") {
        Ios27ListRow("48 个原始文件", subtitle = "保留原包名、默认参数、绘制流程与触摸动画，包含顶部渐进模糊，并提供逐文件哈希校验。")
        Ios27ListDivider()
        Ios27ListRow("宿主负责连接背景", subtitle = "录制内容与浮动玻璃导航为兄弟层，避免循环采样。")
    }
}

@Composable
private fun DemoCheck(color: Color) {
    Canvas(Modifier.size(20.dp).semantics { contentDescription = "已选择" }) {
        drawLine(color, Offset(2.dp.toPx(), 10.dp.toPx()), Offset(8.dp.toPx(), 16.dp.toPx()), 2.dp.toPx(), StrokeCap.Round)
        drawLine(color, Offset(8.dp.toPx(), 16.dp.toPx()), Offset(18.dp.toPx(), 4.dp.toPx()), 2.dp.toPx(), StrokeCap.Round)
    }
}

@Composable
private fun DemoIcon(kind: Int, color: Color, modifier: Modifier = Modifier) {
    Canvas(modifier) {
        val stroke = 1.7.dp.toPx()
        when (kind) {
            0 -> {
                drawCircle(color, radius = size.minDimension * 0.28f, style = Stroke(stroke))
                for (i in 0..7) {
                    val angle = i * Math.PI / 4
                    val direction = Offset(kotlin.math.cos(angle).toFloat(), kotlin.math.sin(angle).toFloat())
                    drawLine(color, center + direction * size.minDimension * 0.4f,
                        center + direction * size.minDimension * 0.49f, stroke, StrokeCap.Round)
                }
            }
            1 -> for (x in 0..1) for (y in 0..1) drawRect(color,
                topLeft = Offset(size.width * (0.05f + x * 0.53f), size.height * (0.05f + y * 0.53f)),
                size = androidx.compose.ui.geometry.Size(size.width * 0.37f, size.height * 0.37f), style = Stroke(stroke))
            else -> {
                drawCircle(color, radius = size.minDimension * 0.43f, style = Stroke(stroke))
                drawCircle(color, radius = stroke * 0.6f, center = Offset(center.x, size.height * 0.28f))
                drawLine(color, Offset(center.x, size.height * 0.47f), Offset(center.x, size.height * 0.75f), stroke, StrokeCap.Round)
            }
        }
    }
}
