package dev.liquidglass.sample

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.layout.onGloballyPositioned

internal class PickerSourceCapture {
    private val layers = mutableMapOf<Any, Pair<Rect, GraphicsLayer>>()
    private val surfaces = mutableMapOf<Any, Pair<Rect, GraphicsLayer>>()
    fun update(key: Any, bounds: Rect, layer: GraphicsLayer, surface: Boolean) {
        (if (surface) surfaces else layers)[key] = bounds to layer
    }
    fun remove(key: Any) { layers.remove(key); surfaces.remove(key) }
    suspend fun capture(bounds: Rect, surface: Boolean = false): ImageBitmap? =
        (if (surface) surfaces else layers).values.firstOrNull {
        (it.first.center - bounds.center).getDistance() < 2f
    }?.second?.toImageBitmap()
}

internal val LocalPickerSourceCapture = staticCompositionLocalOf<PickerSourceCapture?> { null }

/** Attach only to the foreground; the pill/calendar material must remain outside. */
internal fun Modifier.pickerSourceForeground(surface: Boolean = false): Modifier = composed {
    val capture = LocalPickerSourceCapture.current
    val key = remember { Any() }
    val layer = rememberGraphicsLayer()
    DisposableEffect(capture, key) { onDispose { capture?.remove(key) } }
    this.onGloballyPositioned { capture?.update(key, Rect(it.positionInRoot(), it.size.toSize()), layer, surface) }
        .drawWithContent {
            layer.record { this@drawWithContent.drawContent() }
            drawLayer(layer)
        }
}
