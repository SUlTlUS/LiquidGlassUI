package com.lladlam.melox.ui

import dev.liquidglass.*

import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import com.zeaze.tianyinwallpaper.backdrop.Backdrop
import com.lladlam.melox.R
import com.lladlam.melox.ui.glass.*
import com.lladlam.melox.ui.settings.LocalMeloXFrostedGlass
import com.lladlam.melox.ui.theme.isMeloXDarkTheme

typealias MeloXTabIcon = @Composable (modifier: Modifier, color: Color, selected: Boolean) -> Unit
typealias MeloXTabContent = @Composable (color: Color, selected: Boolean) -> Unit
typealias MeloXPanelDecoration = androidx.compose.ui.graphics.drawscope.DrawScope.() -> Unit
internal val LocalMeloXPanelDecoration = staticCompositionLocalOf<MeloXPanelDecoration?> { null }
internal val LocalMeloXActionTabs = staticCompositionLocalOf<Set<AppTab>> { emptySet() }
internal val LocalMeloXShowLabels = staticCompositionLocalOf { true }
internal val LocalMeloXTabContents = staticCompositionLocalOf<Map<AppTab, MeloXTabContent>> { emptyMap() }
internal val LocalMeloXTabLabels = staticCompositionLocalOf<Map<AppTab, String>> { emptyMap() }
internal val LocalMeloXTabIcons = staticCompositionLocalOf<Map<AppTab, MeloXTabIcon>> { emptyMap() }
val MeloXOriginalTabFont: FontFamily
    get() = FontFamily(Font(R.font.mi_lan_pro))
val MeloXDefaultTabs = listOf(AppTab.Home, AppTab.Explore, AppTab.Library, AppTab.Settings)

internal fun validateMeloXTabs(tabs: List<AppTab>, selected: AppTab) {
    require(tabs.isNotEmpty()) { "At least one primary tab is required." }
    require(tabs.distinct().size == tabs.size) { "Primary tabs must be unique." }
    require(AppTab.Search !in tabs) { "Search has its own original capsule; do not add it to primary tabs." }
    require(selected == AppTab.Search || selected in tabs) { "Selected tab must be visible (or Search)." }
}

/**
 * Shared MeloX chrome with selectable glass parameters. State/navigation belong to the caller.
 * [backdrop] must capture a sibling background, never this bar's parent tree.
 * The default layout applies navigationBarsPadding and the original 8dp bottom gap.
 * A tap while compact should set minimized=false in [onTabSelected], as the original host does.
 */
@Composable
fun MeloXTabBar(
    selectedTab: AppTab,
    onTabSelected: (AppTab) -> Unit,
    backdrop: Backdrop?,
    modifier: Modifier = Modifier,
    tabs: List<AppTab> = MeloXDefaultTabs,
    minimized: Boolean = false,
    frostedGlass: Boolean = false,
    automaticTone: Boolean = true,
    labels: Map<AppTab, String> = emptyMap(),
    icons: Map<AppTab, MeloXTabIcon> = emptyMap(),
    fontFamily: FontFamily = MeloXOriginalTabFont,
    hasMedia: Boolean = false,
    searchEnabled: Boolean = true,
    preset: GlassTabBarPreset = GlassTabBarPreset.MeloX,
    layout: GlassTabBarLayout = GlassTabBarLayout(),
    /** Shared material overlay for the navigation bar, search and mini-player. */
    surfaceColor: Color = Color.Unspecified,
    surfaceShape: androidx.compose.ui.graphics.Shape? = null,
    surfaceLayer: (androidx.compose.ui.graphics.GraphicsLayerScope.(com.zeaze.tianyinwallpaper.catalog.components.LiquidBottomTabsSurface, Float) -> Unit)? = null,
    accentColor: Color? = null,
    tabContents: Map<AppTab, MeloXTabContent> = emptyMap(),
    onToneChanged: (Float) -> Unit = {},
    showLabels: Boolean = true,
    panelDecoration: MeloXPanelDecoration? = null,
    actionTabs: Set<AppTab> = emptySet(),
    animateSearchVisibility: Boolean = false,
    miniPlayer: @Composable (compactProgress: Float) -> Unit = {},
) {
    validateMeloXTabs(tabs, selectedTab)
    val systemDark = isMeloXDarkTheme()
    // Local compact layout: 64dp per tab, plus track padding and optional search.
    val compactWidth = 64.dp * tabs.size +
        (if (preset == GlassTabBarPreset.MeloX) 20.dp else 8.dp) +
        layout.horizontalMargin * 2 +
        (if (layout.showSearchButton) layout.height + layout.searchSpacing else 0.dp)
    // Release a host's fillMaxWidth minimum, then constrain the actual chrome.
    // Tone sampling measures the narrowed chrome, excluding the empty side margins.
    var measuredModifier = modifier.then(if (layout.compact)
        Modifier.wrapContentWidth(Alignment.CenterHorizontally).widthIn(max = compactWidth)
        else Modifier)
    val tone = if (automaticTone) {
        val toneState = rememberBottomBarToneState(systemDark)
        DisposableEffect(toneState) { onDispose { toneState.release() } }
        measuredModifier = measuredModifier.onGloballyPositioned { toneState.updateBounds(it) }
        toneState.darkness(systemDark)
    } else if (systemDark) 1f else 0f
    SideEffect { onToneChanged(tone) }
    CompositionLocalProvider(
        LocalBottomBarSurfaceColor provides surfaceColor,
        LocalNavigationPreset provides preset,
        LocalNavigationShape provides surfaceShape,
        LocalNavigationLight provides surfaceLayer,
        LocalNavigationAccent provides accentColor,
        LocalMeloXTabContents provides tabContents,
        LocalMeloXPanelDecoration provides panelDecoration,
        LocalMeloXActionTabs provides actionTabs,
        LocalMeloXShowLabels provides showLabels,
        LocalMeloXBackdrop provides backdrop,
        LocalBottomBarTone provides tone,
        LocalMeloXFrostedGlass provides frostedGlass,
        LocalMeloXTabLabels provides labels,
        LocalMeloXTabIcons provides icons,
    ) {
        MaterialTheme(typography = MaterialTheme.typography.copy(
            // Original MeloXTypography.body; do not inherit M3's 0.5sp letter spacing.
            bodyLarge = TextStyle(fontFamily = fontFamily, fontWeight = FontWeight.Normal,
                fontSize = 17.sp, lineHeight = 22.sp),
        )) {
            BottomBarToneTheme(tone) {
                MeloXBottomChrome(
                    selectedTab = selectedTab, onSelect = onTabSelected,
                    hasMedia = hasMedia, minimized = minimized, searchEnabled = searchEnabled,
                    visibleRootTabs = tabs, modifier = measuredModifier, miniPlayer = miniPlayer,
                    layout = layout,
                    animateSearchVisibility = animateSearchVisibility,
                )
            }
        }
    }
}

