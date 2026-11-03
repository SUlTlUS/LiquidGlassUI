package dev.liquidglass

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

enum class GlassTabBarPreset(val label: String) { MeloX("MeloX"), Standard("通用") }

/** Every dimension is in dp. Compact width and collapsed interaction are independent. */
@Immutable
data class GlassTabBarLayout(
    val showSearchButton: Boolean = false,
    val avoidNavigationBar: Boolean = false,
    val horizontalMargin: Dp = 0.dp,
    val bottomMargin: Dp = 0.dp,
    val searchSpacing: Dp = 7.dp,
    val height: Dp = 57.dp,
    val compact: Boolean = false,
)

object GlassTabBarDefaults {
    fun layout(preset: GlassTabBarPreset = GlassTabBarPreset.MeloX,
        compact: Boolean = false, showSearchButton: Boolean = false,
        avoidNavigationBar: Boolean = false) = GlassTabBarLayout(
        compact = compact, showSearchButton = showSearchButton, avoidNavigationBar = avoidNavigationBar,
        horizontalMargin = if (avoidNavigationBar) 12.dp else 0.dp,
        bottomMargin = if (avoidNavigationBar) 8.dp else 0.dp,
        height = if (preset == GlassTabBarPreset.MeloX) 57.dp else 62.dp,
        searchSpacing = if (preset == GlassTabBarPreset.MeloX) 7.dp else 16.dp,
    )
}
