package dev.liquidglass27.glass

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection

@Stable
class GlassPopupAnchor {
    var bounds: Rect by mutableStateOf(Rect.Zero)
        internal set
}

@Composable
fun rememberGlassPopupAnchor() = remember { GlassPopupAnchor() }

/** Apply before elastic drawing transforms: the source's resting layout is the anchor. */
fun Modifier.glassPopupAnchor(anchor: GlassPopupAnchor): Modifier = onGloballyPositioned {
    anchor.bounds = it.boundsInRoot()
}

@Composable
internal fun popupSafeBounds(width: Float, height: Float): Rect {
    val density = LocalDensity.current
    val direction = LocalLayoutDirection.current
    val insets = WindowInsets.safeDrawing.union(WindowInsets.ime)
    return Rect(insets.getLeft(density, direction).toFloat(), insets.getTop(density).toFloat(),
        width - insets.getRight(density, direction), height - insets.getBottom(density))
}
