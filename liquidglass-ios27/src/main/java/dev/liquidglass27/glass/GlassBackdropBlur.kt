package dev.liquidglass27.glass

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.exp
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

/** One band of the scene-wide, reduced-resolution frosted backdrop. */
internal data class GlassBlurLevel(val sigmaPx: Float, val image: ImageBitmap)

/**
 * Static-backdrop equivalent of a small multi-band frost atlas.
 *
 * Prepare the 1/4-resolution source just ONCE, then Gaussian-convolve it
 * at three blur radii. A menu growing from a button can select the nearest
 * cached sigma not larger than its current blur, avoiding 24 or 64 repeated
 * Vogel taps over most of its transition.
 *
 * This is a CPU prepared static pyramid, NOT FlClash's GPU band atlas. It
 * runs from Dispatchers.Default only, never in a per-pane paint callback.
 */
internal fun createSharedBackdropBlurLevels(
    source: ImageBitmap,
    maxSigmaPx: Float,
): List<GlassBlurLevel> {
    if (!maxSigmaPx.isFinite() || maxSigmaPx < 1f ||
        source.width <= 0 || source.height <= 0
    ) return emptyList()

    val downsample = 4
    val width = max(1, source.width / downsample)
    val height = max(1, source.height / downsample)
    if (width.toLong() * height.toLong() > 2_000_000L) return emptyList()
    val targetSigmas = listOf(
        maxSigmaPx * .25f, maxSigmaPx * .6f, maxSigmaPx,
    ).filter { it >= 1f }.distinct()

    var cpu: Bitmap? = null
    var small: Bitmap? = null
    return try {
        cpu = source.asAndroidBitmap().copy(Bitmap.Config.ARGB_8888, false)
            ?: return emptyList()
        small = Bitmap.createScaledBitmap(cpu, width, height, true)
        val input = IntArray(width * height)
        small.getPixels(input, 0, width, 0, 0, width, height)

        fun pack(a: Float, r: Float, g: Float, b: Float): Int {
            fun byte(value: Float) = value.roundToInt().coerceIn(0, 255)
            // ARGB int pixels are straight-alpha. Filter in premultiplied
            // channels, then unpremultiply before creating the Bitmap.
            val straight = if (a > .001f) 255f / a else 0f
            return (byte(a) shl 24) or
                (byte(r * straight) shl 16) or
                (byte(g * straight) shl 8) or byte(b * straight)
        }

        fun blur(sigma: Float): ImageBitmap {
            val radius = ceil(3f * sigma).toInt().coerceIn(1, 36)
            val weights = FloatArray(radius + 1) { k ->
                exp(-.5f * k * k / (sigma * sigma))
            }
            var total = weights[0]
            for (k in 1..radius) total += 2f * weights[k]
            for (k in weights.indices) weights[k] /= total

            val h = IntArray(input.size)
            val v = IntArray(input.size)
            for (y in 0 until height) {
                val offset = y * width
                for (x in 0 until width) {
                    var a = 0f; var r = 0f; var g = 0f; var b = 0f
                    for (k in -radius..radius) {
                        val pixel = input[offset + (x + k).coerceIn(0, width - 1)]
                        val weight = weights[abs(k)]
                        val alpha = (pixel ushr 24).toFloat()
                        val premul = alpha / 255f * weight
                        a += alpha * weight
                        r += ((pixel ushr 16) and 255) * premul
                        g += ((pixel ushr 8) and 255) * premul
                        b += (pixel and 255) * premul
                    }
                    h[offset + x] = pack(a, r, g, b)
                }
            }
            for (y in 0 until height) {
                for (x in 0 until width) {
                    var a = 0f; var r = 0f; var g = 0f; var b = 0f
                    for (k in -radius..radius) {
                        val pixel = h[(y + k).coerceIn(0, height - 1) * width + x]
                        val weight = weights[abs(k)]
                        val alpha = (pixel ushr 24).toFloat()
                        val premul = alpha / 255f * weight
                        a += alpha * weight
                        r += ((pixel ushr 16) and 255) * premul
                        g += ((pixel ushr 8) and 255) * premul
                        b += (pixel and 255) * premul
                    }
                    v[y * width + x] = pack(a, r, g, b)
                }
            }
            return Bitmap.createBitmap(v, width, height, Bitmap.Config.ARGB_8888)
                .asImageBitmap()
        }

        targetSigmas.map { sigmaPx ->
            GlassBlurLevel(
                sigmaPx = sigmaPx,
                image = blur(sigmaPx * width.toFloat() / source.width),
            )
        }
    } catch (failure: RuntimeException) {
        Log.w("LiquidGlass27", "Shared background frost pyramid unavailable", failure)
        emptyList()
    } finally {
        if (small !== cpu) small?.recycle()
        cpu?.recycle()
    }
}
