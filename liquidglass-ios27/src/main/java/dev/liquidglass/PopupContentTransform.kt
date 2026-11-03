package dev.liquidglass

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import kotlin.math.min

internal data class PopupContentTransform(val scale: Float, val translation: Offset)

/** Foreground may be smaller than its button. Scale by the button, never fit text to it. */
internal fun popupSourceContentTransform(source: Size, anchor: Size, droplet: Rect): PopupContentTransform {
    val scale = popupContentTransform(anchor, droplet, fitInside = true).scale
    return PopupContentTransform(scale, droplet.center - Offset(source.width, source.height) * (scale / 2f))
}

/** Flutter _contentTransform: keep text aspect ratio and center it in the live platter. */
internal fun popupContentTransform(source: Size, target: Rect, fitInside: Boolean = false): PopupContentTransform {
    if (source.width <= 0f || source.height <= 0f || target.width <= 0f || target.height <= 0f) {
        return PopupContentTransform(0f, target.center)
    }
    val scale = if (fitInside) min(target.width / source.width, target.height / source.height)
        else target.width / source.width
    return PopupContentTransform(scale, target.center - Offset(source.width, source.height) * (scale / 2f))
}
