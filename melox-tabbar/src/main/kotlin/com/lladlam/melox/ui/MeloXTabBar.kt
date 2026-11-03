package com.lladlam.melox.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
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
import com.kyant.backdrop.Backdrop
import com.kyant.backdrop.backdrops.layerBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import com.lladlam.melox.R
import com.lladlam.melox.ui.glass.*
import com.lladlam.melox.ui.settings.LocalMeloXFrostedGlass
import com.lladlam.melox.ui.theme.isMeloXDarkTheme

typealias MeloXTabIcon = @Composable (modifier: Modifier, color: Color, selected: Boolean) -> Unit
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
 * Original MeloX chrome. State/navigation belong to the caller.
 * [backdrop] must capture a sibling background, never this bar's parent tree.
 * The bar itself already applies navigationBarsPadding and the original 8dp bottom gap.
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
    miniPlayer: @Composable (compactProgress: Float) -> Unit = {},
) {
    validateMeloXTabs(tabs, selectedTab)
    val systemDark = isMeloXDarkTheme()
    var measuredModifier = modifier
    val tone = if (automaticTone) {
        val toneState = rememberBottomBarToneState(systemDark)
        DisposableEffect(toneState) { onDispose { toneState.release() } }
        measuredModifier = modifier.onGloballyPositioned { toneState.updateBounds(it) }
        toneState.darkness(systemDark)
    } else if (systemDark) 1f else 0f
    CompositionLocalProvider(
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
                )
            }
        }
    }
}

/** Safe default integration: background capture and all glass readers are siblings. */
@Composable
fun MeloXTabBarScaffold(
    selectedTab: AppTab,
    onTabSelected: (AppTab) -> Unit,
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
    miniPlayer: @Composable (compactProgress: Float) -> Unit = {},
    content: @Composable BoxScope.() -> Unit,
) {
    val background = rememberLayerBackdrop()
    Box(modifier) {
        Box(Modifier.fillMaxSize().layerBackdrop(background), content = content)
        MeloXTabBar(
            selectedTab = selectedTab, onTabSelected = onTabSelected, backdrop = background,
            modifier = Modifier.align(Alignment.BottomCenter), tabs = tabs,
            minimized = minimized, frostedGlass = frostedGlass, automaticTone = automaticTone,
            labels = labels, icons = icons, fontFamily = fontFamily,
            hasMedia = hasMedia, miniPlayer = miniPlayer, searchEnabled = searchEnabled,
        )
    }
}
