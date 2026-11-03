package com.lladlam.melox.ui
import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.*
import com.lladlam.melox.ui.settings.MeloXSettingsRuntime

@Stable
class MeloXTabBarScrollState internal constructor() {
    var minimized by mutableStateOf(false)
    internal var scrollAccumulator by mutableFloatStateOf(0f)
    fun expand() { minimized = false; scrollAccumulator = 0f }
}
@Composable
fun rememberMeloXTabBarScrollState() = remember { MeloXTabBarScrollState() }
@Composable
fun MeloXTabBarScrollState.nestedScrollConnection(): NestedScrollConnection {
    val tabBarMinimizeConnection = remember(this) {
        object : NestedScrollConnection {
            override fun onPreScroll(
                available: Offset,
                source: NestedScrollSource,
            ): Offset {
                if (source != NestedScrollSource.UserInput) return Offset.Zero
                if (MeloXSettingsRuntime.disableAutomaticTabBarShrink) {
                    scrollAccumulator = 0f
                    return Offset.Zero
                }

                if (available.y < 0f) {
                    if (scrollAccumulator > 0f) scrollAccumulator = 0f
                    scrollAccumulator += available.y
                    if (scrollAccumulator <= -18f) {
                        minimized = true
                        scrollAccumulator = 0f
                    }
                } else if (available.y > 0f) {
                    if (scrollAccumulator < 0f) scrollAccumulator = 0f
                    scrollAccumulator += available.y
                    if (scrollAccumulator >= 18f) {
                        minimized = false
                        scrollAccumulator = 0f
                    }
                }
                return Offset.Zero
            }
        }
    }
    return tabBarMinimizeConnection
}
