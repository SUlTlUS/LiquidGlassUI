package dev.liquidglass

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import com.lladlam.melox.ui.*
import com.zeaze.tianyinwallpaper.backdrop.Backdrop
import com.zeaze.tianyinwallpaper.backdrop.backdrops.layerBackdrop
import com.zeaze.tianyinwallpaper.backdrop.backdrops.rememberLayerBackdrop
import dev.liquidglass.GlassStandard

val LocalGlassTabBarPreset = staticCompositionLocalOf { GlassTabBarPreset.MeloX }

/** Label and icon travel together, so changing item count cannot misalign parallel lists. */
data class GlassTab(val label: String,
    val icon: (@Composable (modifier: Modifier, color: Color, selected: Boolean) -> Unit)? = null)

/** One renderer. Theme supplies defaults; [layout] is the sole owner of geometry and visibility. */
@Composable
fun GlassTabBar(
    items: List<GlassTab>, selectedIndex: Int, onSelectedIndexChange: (Int) -> Unit,
    modifier: Modifier = Modifier, backdrop: Backdrop? = currentGlassBackdrop(),
    darkTheme: Boolean = isGlassDarkTheme(), standard: GlassStandard = LocalGlassStandard.current,
    effects: GlassEffects = LocalGlassEffects.current,
    preset: GlassTabBarPreset = LocalGlassTabBarPreset.current,
    layout: GlassTabBarLayout = GlassTabBarDefaults.layout(preset),
    accentColor: Color = if (darkTheme) Color(0xFF0091FF) else Color(0xFF0088FF),
    collapsed: Boolean = false, frosted: Boolean = false, adaptToBackground: Boolean = false,
    searchSelected: Boolean = false, searchEnabled: Boolean = true, onSearchClick: () -> Unit = {},
    fontFamily: FontFamily = MeloXOriginalTabFont,
    itemContent: (@Composable (index: Int, color: Color, selected: Boolean) -> Unit)? = null,
    player: (@Composable (collapseProgress: Float) -> Unit)? = null,
    showLabels: Boolean = true,
    searchItem: GlassTab? = null,
    actionIndices: Set<Int> = emptySet(),
    panelDecoration: (androidx.compose.ui.graphics.drawscope.DrawScope.() -> Unit)? = null,
    animateSearchVisibility: Boolean = false,
) {
    if (items.isEmpty()) return
    val tabs = remember(items.size) { List(items.size) { AppTab.custom(it.toString()) } }
    var tone by remember(darkTheme) { mutableFloatStateOf(if (darkTheme) 1f else 0f) }
    val material27 = effects.materialFor(standard) == GlassStandard.Ios27
    val highlight27 = effects.highlightFor(standard) == GlassStandard.Ios27
    val appearance = if (material27 || highlight27) rememberDockAppearance(darkTheme) { tone } else null
    MaterialTheme(colorScheme = if (darkTheme) darkColorScheme() else lightColorScheme()) {
        MeloXTabBar(
            selectedTab = if (layout.showSearchButton && searchSelected) AppTab.Search else tabs[selectedIndex.coerceIn(tabs.indices)],
            onTabSelected = { if (it == AppTab.Search) onSearchClick() else onSelectedIndexChange(tabs.indexOf(it)) },
            backdrop = backdrop, modifier = modifier, tabs = tabs,
            labels = tabs.zip(items.map { it.label }).toMap() +
                (searchItem?.let { mapOf(AppTab.Search to it.label) } ?: emptyMap()),
            icons = tabs.mapIndexedNotNull { index, tab -> items[index].icon?.let { tab to it } }.toMap() +
                (searchItem?.icon?.let { mapOf(AppTab.Search to it) } ?: emptyMap()),
            tabContents = if (itemContent == null) emptyMap() else tabs.mapIndexed { index, tab ->
                val content: MeloXTabContent = { color, selected -> itemContent(index, color, selected) }
                tab to content
            }.toMap(),
            preset = preset, layout = layout,
            surfaceColor = if (material27) GlassStyle.interpolate(
                GlassStyle.regular(false), GlassStyle.regular(true), tone).tint else Color.Unspecified,
            surfaceShape = if (material27) appearance?.shape else null,
            surfaceLayer = if (highlight27) appearance?.layer else null,
            accentColor = accentColor, minimized = collapsed, frostedGlass = frosted, automaticTone = adaptToBackground,
            onToneChanged = { tone = it }, hasMedia = player != null, miniPlayer = { player?.invoke(it) },
            searchEnabled = searchEnabled, fontFamily = fontFamily,
            showLabels = showLabels, panelDecoration = panelDecoration,
            actionTabs = actionIndices.mapNotNull { tabs.getOrNull(it) }.toSet(),
            animateSearchVisibility = animateSearchVisibility,
        )
    }
}

/** Records content once; the tab bar remains a sibling and cannot sample itself. */
@Composable
fun GlassTabBarScaffold(
    items: List<GlassTab>, selectedIndex: Int, onSelectedIndexChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    darkTheme: Boolean = isGlassDarkTheme(), standard: GlassStandard = LocalGlassStandard.current,
    effects: GlassEffects = LocalGlassEffects.current,
    preset: GlassTabBarPreset = LocalGlassTabBarPreset.current,
    layout: GlassTabBarLayout = GlassTabBarDefaults.layout(preset, avoidNavigationBar = true),
    accentColor: Color = if (darkTheme) Color(0xFF0091FF) else Color(0xFF0088FF),
    collapsed: Boolean = false, frosted: Boolean = false, adaptToBackground: Boolean = false,
    searchSelected: Boolean = false, searchEnabled: Boolean = true, onSearchClick: () -> Unit = {},
    fontFamily: FontFamily = MeloXOriginalTabFont,
    itemContent: (@Composable (index: Int, color: Color, selected: Boolean) -> Unit)? = null,
    player: (@Composable (collapseProgress: Float) -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit,
) {
    val backdrop = rememberLayerBackdrop()
    Box(modifier) {
        Box(Modifier.fillMaxSize().layerBackdrop(backdrop), content = content)
        GlassTabBar(items, selectedIndex, onSelectedIndexChange,
            modifier = Modifier.align(Alignment.BottomCenter), backdrop = backdrop,
            darkTheme = darkTheme, standard = standard, effects = effects, preset = preset, layout = layout, accentColor = accentColor,
            collapsed = collapsed, frosted = frosted, adaptToBackground = adaptToBackground,
            searchSelected = searchSelected, searchEnabled = searchEnabled, onSearchClick = onSearchClick,
            fontFamily = fontFamily, itemContent = itemContent, player = player)
    }
}
