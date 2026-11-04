package dev.liquidglass

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalWindowInfo

@Stable
class GlassPopupAnchor(initialBounds: Rect = Rect.Zero) {
    var bounds: Rect by mutableStateOf(initialBounds)
        internal set
}

@Composable
fun rememberGlassPopupAnchor() = remember { GlassPopupAnchor() }

/** Apply before elastic drawing transforms: the source's resting layout is the anchor. */
fun Modifier.glassPopupAnchor(anchor: GlassPopupAnchor): Modifier = onGloballyPositioned {
    anchor.bounds = it.boundsInRoot()
}

@Composable
internal fun popupSafeBounds(width: Float, height: Float, hostOffset: Offset = Offset.Zero): Rect {
    val density = LocalDensity.current
    val direction = LocalLayoutDirection.current
    val insets = WindowInsets.safeDrawing.union(WindowInsets.ime)
    val window = LocalWindowInfo.current.containerSize
    val safeWindow = Rect(
        insets.getLeft(density, direction).toFloat(), insets.getTop(density).toFloat(),
        window.width - insets.getRight(density, direction).toFloat(),
        window.height - insets.getBottom(density).toFloat(),
    )
    return popupSafeBoundsInHost(Rect(hostOffset, androidx.compose.ui.geometry.Size(width, height)), safeWindow)
}

/** Insets belong to the window; a resized/padded host may already exclude the IME. */
internal fun popupSafeBoundsInHost(host: Rect, safeWindow: Rect): Rect {
    val left = (safeWindow.left - host.left).coerceIn(0f, host.width)
    val top = (safeWindow.top - host.top).coerceIn(0f, host.height)
    val right = (safeWindow.right - host.left).coerceIn(left, host.width)
    val bottom = (safeWindow.bottom - host.top).coerceIn(top, host.height)
    return Rect(left, top, right, bottom)
}
