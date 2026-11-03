package dev.liquidglass.sample

import dev.liquidglass.*

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.zeaze.tianyinwallpaper.backdrop.backdrops.layerBackdrop
import dev.liquidglass.ios27.LocalIos27PageColors
import dev.liquidglass.ios27.ios27AndroidPane
import com.lladlam.melox.ui.AppTab
import dev.liquidglass.GlassTabBarPreset
import com.lladlam.melox.ui.MeloXOriginalTabFont
import dev.liquidglass.GlassStandard
import dev.liquidglass.ios27.Ios27Tokens
import dev.liquidglass.GlassTabBarScaffold
import dev.liquidglass.LocalGlassTabBarPreset
import com.lladlam.melox.ui.MeloXNavigationIcon
import com.lladlam.melox.ui.MeloXDefaultTabs
import com.lladlam.melox.ui.MeloXTabIcon
import com.lladlam.melox.ui.rememberMeloXTabBarScrollState
import com.lladlam.melox.ui.nestedScrollConnection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.lladlam.melox.ui.player.MeloXPlayerScaffold
import com.lladlam.melox.ui.player.MeloXPlaybackUiState
import com.lladlam.melox.ui.player.MeloXQueueEntry
import androidx.compose.material3.MaterialTheme as MeloXMaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme

/** Standalone catalog route; never nests the new bar inside its own capture layer. */
@Composable
internal fun MeloXTabBarExamplePage(onBack: () -> Unit) {
    val colors = LocalIos27PageColors.current
    val dark = !MaterialTheme.colors.isLight
    val topInset = WindowInsets.safeDrawing.asPaddingValues().calculateTopPadding()
    var selected by rememberSaveable { mutableStateOf(AppTab.Home) }
    val tabBarScrollState = rememberMeloXTabBarScrollState()
    var minimized by tabBarScrollState::minimized
    val tabBarMinimizeConnection = tabBarScrollState.nestedScrollConnection()
    var options by rememberSaveable { mutableStateOf(TabBarDemoOptions()) }
    val preset = when (options.presetChoice) {
        1 -> GlassTabBarPreset.MeloX
        2 -> GlassTabBarPreset.Standard
        else -> LocalGlassTabBarPreset.current
    }
    val standard = when (options.standardChoice) {
        1 -> GlassStandard.Ios26
        2 -> GlassStandard.Ios27
        else -> LocalGlassStandard.current
    }
    val barDark = when (options.themeChoice) { 1 -> false; 2 -> true; else -> dark }
    val accent = when (options.accentChoice) {
        1 -> if (barDark) Ios27Tokens.RedDark else Ios27Tokens.RedLight
        2 -> if (barDark) Ios27Tokens.GreenDark else Ios27Tokens.GreenLight
        3 -> if (barDark) Ios27Tokens.OrangeDark else Ios27Tokens.OrangeLight
        else -> if (barDark) Ios27Tokens.BlueDark else Ios27Tokens.BlueLight
    }
    val visibleTabs = MeloXDefaultTabs.take(options.tabCount)
    val barFont = if (options.originalFont) MeloXOriginalTabFont else FontFamily.Default
    LaunchedEffect(options.tabCount, options.showSearchButton) {
        if ((!options.showSearchButton && selected == AppTab.Search) ||
            (selected != AppTab.Search && selected !in visibleTabs)) selected = visibleTabs.first()
    }
    var playing by rememberSaveable { mutableStateOf(false) }
    var trackIndex by rememberSaveable { mutableIntStateOf(0) }
    var position by rememberSaveable { mutableLongStateOf(0L) }
    var volume by rememberSaveable { mutableFloatStateOf(0.5f) }
    var status by rememberSaveable { mutableStateOf("") }
    val queue = remember {
        val artwork = "android.resource://dev.liquidglass.sample/${com.lladlam.melox.R.drawable.melox_demo_artwork}"
        listOf(
            MeloXQueueEntry(0, "demo-0", "demo-0", "空耳", "ナナツカゼ", artwork),
            MeloXQueueEntry(1, "demo-1", "demo-1", "下一首示例", "MeloX", artwork),
        )
    }
    val playback = MeloXPlaybackUiState(
        queue = queue, currentIndex = trackIndex, isPlaying = playing, repeatMode = 2,
        onTogglePlayPause = { playing = !playing; status = if (playing) "播放状态：播放" else "播放状态：暂停" },
        onNext = { trackIndex = (trackIndex + 1) % queue.size },
        onPrevious = { trackIndex = (trackIndex + queue.size - 1) % queue.size },
        positionMs = position, volume = volume,
        onSeek = { position = it }, onVolumeChange = { volume = it },
    )
    // Existing back/blur use their own capture; no protected toolbar implementation changes.
    val backBackdrop = rememberExamplePageBackdrop(colors.page)
    val listState = androidx.compose.foundation.lazy.rememberLazyListState()
    val blurRevealPx = with(LocalDensity.current) { 30.dp.toPx() }
    val topBlurAlpha by remember(listState, blurRevealPx) {
        derivedStateOf {
            if (listState.firstVisibleItemIndex > 0) 1f
            else (listState.firstVisibleItemScrollOffset / blurRevealPx).coerceIn(0f, 1f)
        }
    }
    Box(Modifier.fillMaxSize().semantics { contentDescription = "底栏收缩、搜索与播放器示例" }
        .ios27AndroidPane("底栏收缩、搜索与播放器示例")) {
        MeloXMaterialTheme(colorScheme = if (dark) darkColorScheme() else lightColorScheme()) {
            MeloXPlayerScaffold(state = playback, onShowActions = { status = "当前曲目：${playback.title}" }) { miniPlayer ->
            Box(Modifier.fillMaxSize()) {
            GlassTabBarScaffold(
                modifier = Modifier.fillMaxSize(),
                items = visibleTabs.mapIndexed { index, originalTab ->
                    GlassTab(options.labels[index]) { modifier, tint, selected ->
                        if (options.showIcons) MeloXNavigationIcon(originalTab, modifier, tint, selected)
                    }
                },
                selectedIndex = visibleTabs.indexOf(selected).coerceAtLeast(0),
                onSelectedIndexChange = { selected = visibleTabs[it]; if (options.expandOnSelect) tabBarScrollState.expand() },
                darkTheme = barDark, standard = standard, preset = preset, accentColor = accent,
                searchSelected = selected == AppTab.Search,
                onSearchClick = { selected = AppTab.Search; if (options.expandOnSelect) tabBarScrollState.expand() },
                searchEnabled = options.searchEnabled, layout = options.layout(preset),
                fontFamily = barFont,
                itemContent = if (options.showIcons && options.showLabels) null else { index, tint, active ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        if (options.showIcons) MeloXNavigationIcon(visibleTabs[index], Modifier.size(24.dp), tint, active)
                        if (options.showLabels) Text(options.labels[index], color = tint, fontFamily = barFont, fontSize = 11.sp)
                    }
                },
                collapsed = minimized, frosted = options.frosted, adaptToBackground = options.automaticTone,
                player = if (options.media) miniPlayer else null,
            ) {
                Box(Modifier.fillMaxSize().layerBackdrop(backBackdrop)
                    .background(colors.page)) {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize().then(if (options.scrollMinimize)
                            Modifier.nestedScroll(tabBarMinimizeConnection) else Modifier),
                        contentPadding = PaddingValues(start = 16.dp, end = 16.dp,
                            top = topInset + 62.dp, bottom = if (options.media) 200.dp else 140.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        item {
                            Column {
                                Text("标签栏", style = dev.liquidglass.ios27.Ios27Tokens.Type.LargeTitle, color = colors.label)
                                Text("${preset.label} 参数 · ${standard.label}", color = colors.secondaryLabel)
                                Text("当前：${selected.name} · 拖动水珠切换标签", color = colors.secondaryLabel)
                                if (status.isNotEmpty()) Text(status, color = colors.secondaryLabel)
                            }
                        }
                        item {
                            TabBarParameterPanel(options, { options = it }, minimized,
                                onMinimizedChange = { if (it) minimized = true else tabBarScrollState.expand() },
                                searchSelected = selected == AppTab.Search,
                                onSearchSelectedChange = { selected = if (it) AppTab.Search else visibleTabs.first() },
                                onReset = {
                                    options = TabBarDemoOptions()
                                    selected = AppTab.Home
                                    tabBarScrollState.expand()
                                    status = "已恢复默认参数"
                                })
                        }
                        items((1..20).toList()) { index ->
                            val strip = if (index % 2 == 0) listOf(Color(0xFF173B61), Color(0xFF601A52))
                                else listOf(Color(0xFFFFBC72), Color(0xFF83D4FB))
                            Box(Modifier.fillMaxWidth().height(110.dp)
                                .background(Brush.horizontalGradient(strip)).padding(16.dp)) {
                                Text("${selected.name} · 内容 $index", color = if (index % 2 == 0) Color.White else Color.Black)
                            }
                        }
                    }
                }
            }
        // These overlays sample only non-glass content and stay outside both captures.
        ExampleTopChrome(
            backdrop = backBackdrop,
            isLightTheme = !dark,
            appBarHeight = topInset + 48.dp,
            surfaceColor = colors.page,
            blurAlpha = topBlurAlpha,
            scrollDriven = true,
        )
        Ios27BackButton(onBack, backBackdrop,
            Modifier.align(Alignment.TopStart).padding(start = 16.dp, top = topInset + 4.dp))
            }
            }
        }
    }
}
