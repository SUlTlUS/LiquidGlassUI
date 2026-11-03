package dev.liquidglass

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/** Route lifetime and reversal snapshot share one owner across the menu layers. */
internal class MenuRouteState(initiallyExpanded: Boolean) {
    var mounted by mutableStateOf(initiallyExpanded)
    var returning by mutableStateOf(false)
    var closingFrom by mutableFloatStateOf(1f)
    var closingStartFrame by mutableStateOf<MenuMorphFrame?>(null)
    // Draw-phase snapshot for reversal; this is not an observable UI input.
    var lastRenderedFrame: MenuMorphFrame? = null
    var pendingAction by mutableStateOf<(() -> Unit)?>(null)
}
