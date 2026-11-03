package dev.liquidglass27.glass

import android.util.Log
import androidx.compose.ui.graphics.BlurEffect
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.runtime.withFrameNanos
import kotlinx.coroutines.CancellationException
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import kotlin.math.max

/**
 * Optional dynamic-backdrop GPU frost band.
 *
 * Render the same captured background to a quarter-size GraphicsLayer, apply
 * Android's hardware RenderEffect blur ONCE, and read back one small frosted
 * texture. Contrast/clear panes continue to use the unblurred source.
 * Unlike the static CPU three-band pyramid this intentionally generates ONE
 * band per change, at a bounded update rate: three full screen readbacks on
 * every animation tick would be slower than the original live frost path.
 *
 * This is still not FlClash's fully GPU-resident multi-pass atlas: Compose
 * RuntimeShader currently consumes a BitmapShader, so one small readback
 * remains. Failure returns null and the real-time shader path remains valid.
 */
internal suspend fun captureGpuFrostBand(
    source: ImageBitmap,
    requestedSigmaPx: Float,
    layer: GraphicsLayer,
    density: Density,
    layoutDirection: LayoutDirection,
): GlassBlurLevel? {
    if (source.width <= 0 || source.height <= 0 ||
        !requestedSigmaPx.isFinite() || requestedSigmaPx <= 1f
    ) return null
    val factor = 4
    val width = max(1, (source.width + factor - 1) / factor)
    val height = max(1, (source.height + factor - 1) / factor)
    if (width.toLong() * height.toLong() > 2_000_000L) return null
    val size = IntSize(width, height)
    return try {
        // Blur sigma is specified in the RESIZED image's physical pixels.
        // A fresh record clears the old layer content without changing the
        // stored raw bitmap. Keep this GraphicsLayer owned by GlassScene.
        val sigma = requestedSigmaPx * width / source.width
        layer.renderEffect = BlurEffect(
            radiusX = sigma, radiusY = sigma, edgeTreatment = TileMode.Clamp,
        )
        layer.record(density, layoutDirection, size) {
            drawImage(
                image = source,
                srcSize = IntSize(source.width, source.height),
                dstSize = size,
            )
        }
        GlassBlurLevel(
            sigmaPx = requestedSigmaPx,
            image = layer.toImageBitmap(),
        )
    } catch (cancelled: CancellationException) {
        throw cancelled
    } catch (failure: RuntimeException) {
        Log.w("LiquidGlass27", "Dynamic GPU frost capture unavailable", failure)
        null
    } finally {
        layer.renderEffect = null
    }
}

/** One coherent, 3-band GPU frost pyramid, computed across separate frames. */
internal suspend fun captureGpuFrostPyramid(
    source: ImageBitmap,
    maxSigmaPx: Float,
    layer: GraphicsLayer,
    density: Density,
    layoutDirection: LayoutDirection,
): List<GlassBlurLevel> {
    val levels = ArrayList<GlassBlurLevel>(3)
    for (factor in floatArrayOf(.25f, .6f, 1f)) {
        withFrameNanos { }
        val level = captureGpuFrostBand(
            source, maxSigmaPx * factor, layer, density, layoutDirection,
        ) ?: return emptyList()
        levels.add(level)
    }
    return levels
}
