package dev.liquidglass.rendering

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.GraphicsLayerScope
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.unit.Density
import com.zeaze.tianyinwallpaper.backdrop.Backdrop
import com.zeaze.tianyinwallpaper.backdrop.backdrops.LayerBackdrop

/** Opt-in sampling for nested moving surfaces. The original LayerBackdrop stays unchanged. */
@Composable
fun rememberTransformAwareBackdrop(backdrop: Backdrop): Backdrop = remember(backdrop) {
    if (backdrop is LayerBackdrop) TransformAwareLayerBackdrop(backdrop) else backdrop
}

private class TransformAwareLayerBackdrop(private val source: LayerBackdrop) : Backdrop {
    override val isCoordinatesDependent = true

    override fun DrawScope.drawBackdrop(
        density: Density,
        coordinates: LayoutCoordinates?,
        layerBlock: (GraphicsLayerScope.() -> Unit)?,
    ) {
        val target = coordinates?.takeIf { it.isAttached } ?: return
        val sourceCoordinates = source.layerCoordinates?.takeIf { it.isAttached } ?: return
        // Resolve each source separately: a popup's painted surface and its page
        // have different ancestors. Translation alone cannot align either during scale.
        val probe = 64f
        val origin: Offset
        val x: Offset
        val y: Offset
        try {
            origin = target.localPositionOf(sourceCoordinates, Offset.Zero)
            x = (target.localPositionOf(sourceCoordinates, Offset(probe, 0f)) - origin) / probe
            y = (target.localPositionOf(sourceCoordinates, Offset(0f, probe)) - origin) / probe
        } catch (_: IllegalStateException) {
            return // Source detached during a route transition.
        }
        val matrix = backdropAffineMatrix(origin, x, y) ?: return
        // localPositionOf already includes the caller's actual graphics layers,
        // including layerBlock. Applying its inverse again would double-compensate.
        withTransform({ transform(matrix) }) { drawLayer(source.graphicsLayer) }
    }
}

internal fun backdropAffineMatrix(origin: Offset, xAxis: Offset, yAxis: Offset): Matrix? {
    if (!origin.x.isFinite() || !origin.y.isFinite() ||
        !xAxis.x.isFinite() || !xAxis.y.isFinite() ||
        !yAxis.x.isFinite() || !yAxis.y.isFinite()) return null
    if (kotlin.math.abs(xAxis.x * yAxis.y - xAxis.y * yAxis.x) < 1e-6f) return null
    return Matrix(floatArrayOf(
        xAxis.x, xAxis.y, 0f, 0f,
        yAxis.x, yAxis.y, 0f, 0f,
        0f, 0f, 1f, 0f,
        origin.x, origin.y, 0f, 1f,
    ))
}
