package dev.liquidglass27.glass

import android.graphics.Bitmap
import androidx.compose.ui.geometry.Offset
import kotlin.math.abs
import kotlin.math.exp
import kotlin.math.hypot
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt

/**
 * Native material fallback for FlClash's ordinary button rim.
 *
 * Menu/alert highlights use LiquidGlassRim's nine bands, while controls
 * use liquid_glass.frag's 'facing / band / tail / shine' equation. Unlike
 * the old solid-tint fallback, this path samples the scene when possible
 * and applies the SAME rim formula using the real pre-captured backdrop.
 *
 * It runs only when AGSL is unavailable, and caches output by immutable
 * style, source generation, size and Goo geometry. No extra glow strengths,
 * rounded outlines or invented light gradients are introduced.
 */
internal class GlassControlFallback {
    private data class Key(
        val width: Int,
        val height: Int,
        val style: GlassStyle,
        val radius: Float,
        val power: Float,
        val density: Float,
        val pills: List<GlassPill>,
        val blends: List<Float>,
        val backdrop: GlassBackdropPixels?,
        val origin: Offset,
    )

    private data class Field(val distance: Float, val nx: Float, val ny: Float)
    private data class Sample(val r: Float, val g: Float, val b: Float, val a: Float)

    private var cachedKey: Key? = null
    private var cachedImage: Bitmap? = null

    fun render(
        width: Int,
        height: Int,
        style: GlassStyle,
        radius: Float,
        power: Float,
        density: Float,
        pills: List<GlassPill>,
        blendsDp: List<Float>,
        backdrop: GlassBackdropPixels?,
        origin: Offset,
    ): Bitmap? {
        if (width <= 0 || height <= 0 ||
            width.toLong() * height.toLong() > 2_000_000L ||
            style.rim <= 0f ||
            (style.tint.alpha <= .001f && backdrop == null)
        ) return null

        val blends = blendsDp.map { it * density }
        val key = Key(
            width, height, style, radius, power, density,
            pills.toList(), blends, backdrop, origin,
        )
        if (cachedKey == key) return cachedImage
        val pixels = IntArray(width * height)
        val halfW = width * .5f
        val halfH = height * .5f
        val line = .67f * density
        val rimWidth = max(style.rimWidth * density, .001f)
        val tailReach = max(4.5f * density, .001f)
        val alphaTint = style.tint.alpha.coerceIn(0f, 1f)
        val tint = style.tint

        // Original uGlow.w contrast logic: five backdrop luminance probes,
        // blended before the tint, not a speculative brighter control rim.
        val clash = if (backdrop != null && abs(style.contrast) > .001f) {
            var average = 0f
            val points = arrayOf(
                Offset(halfW, halfH),
                Offset(halfW * .4f, halfH * .6f),
                Offset(halfW * 1.6f, halfH * .6f),
                Offset(halfW * .4f, halfH * 1.4f),
                Offset(halfW * 1.6f, halfH * 1.4f),
            )
            for (p in points) {
                val behind = sample(backdrop, origin.x + p.x, origin.y + p.y)
                average += luma(behind.r, behind.g, behind.b) / points.size
            }
            val sign = if (style.contrast < 0f) -1f else 1f
            ((sign * (luma(tint.red, tint.green, tint.blue) - average)
                - .3f) / .5f).coerceIn(0f, 1f)
        } else 0f
        val cover = if (backdrop != null) {
            alphaTint + (1f - alphaTint) * clash * abs(style.contrast)
        } else alphaTint

        fun shade(x: Float, y: Float): Int {
            val field = if (pills.isEmpty()) {
                rounded(x, y, halfW, halfH, halfW, halfH, radius, power)
            } else {
                var result = rounded(
                    x, y, pills[0].centerX, pills[0].centerY,
                    pills[0].halfWidth, pills[0].halfHeight,
                    min(pills[0].halfWidth, pills[0].halfHeight), 2f,
                )
                for (i in 1 until min(pills.size, 6)) {
                    val pill = pills[i]
                    val next = rounded(
                        x, y, pill.centerX, pill.centerY,
                        pill.halfWidth, pill.halfHeight,
                        min(pill.halfWidth, pill.halfHeight), 2f,
                    )
                    val blend = max(blends.getOrElse(i - 1) { 0f }, .001f)
                    val h = (.5f + .5f * (next.distance - result.distance) / blend)
                        .coerceIn(0f, 1f)
                    val nx = next.nx * (1f - h) + result.nx * h
                    val ny = next.ny * (1f - h) + result.ny * h
                    val length = hypot(nx, ny)
                    result = Field(
                        next.distance * (1f - h) + result.distance * h -
                            blend * h * (1f - h),
                        if (length > .0001f) nx / length else result.nx,
                        if (length > .0001f) ny / length else result.ny,
                    )
                }
                result
            }
            // FlClash clips Goo to its CPU Path. An ordinary button uses
            // smooth coverage in its actual shader instead.
            val coverage = if (pills.isNotEmpty()) 1f
                else (0.5f - field.distance).coerceIn(0f, 1f)
            if (coverage < .001f) return 0
            val inset = max(-field.distance, 0f)

            val scene: Sample = if (backdrop != null) {
                val minor = if (pills.isEmpty()) min(halfW, halfH)
                    else min(pills[0].halfWidth, pills[0].halfHeight)
                val bevel = max(min(style.bevel * density, minor * .5f), .001f)
                val rise = 1f - (inset / bevel).coerceIn(0f, 1f)
                val depth = if (style.refraction > 0f) {
                    min(style.refraction * density, minor) *
                        (1f - sqrt(max(1f - rise * rise, 0f)))
                } else {
                    style.refraction * density *
                        (1f - rise) * (1f - rise)
                }
                sample(
                    backdrop,
                    origin.x + x - field.nx * depth,
                    origin.y + y - field.ny * depth,
                )
            } else Sample(tint.red, tint.green, tint.blue, alphaTint)

            val alphaScene = scene.a
            val grey = luma(scene.r, scene.g, scene.b)
            val endShade = 1f - style.endShade * abs(field.nx) *
                exp(-inset / max(3f * density, .001f))
            val sr = (grey + (scene.r - grey) * style.saturation) *
                style.brightness * endShade
            val sg = (grey + (scene.g - grey) * style.saturation) *
                style.brightness * endShade
            val sb = (grey + (scene.b - grey) * style.saturation) *
                style.brightness * endShade

            val initialAlpha = if (backdrop != null) {
                alphaScene * (1f - cover) + cover
            } else alphaTint
            var r = if (backdrop != null)
                sr * alphaScene * (1f - cover) + tint.red * cover
            else tint.red * alphaTint
            var g = if (backdrop != null)
                sg * alphaScene * (1f - cover) + tint.green * cover
            else tint.green * alphaTint
            var b = if (backdrop != null)
                sb * alphaScene * (1f - cover) + tint.blue * cover
            else tint.blue * alphaTint

            // Verbatim structure of FlClash's control shine expression.
            val facing = if (style.refraction < 0f) {
                val value = ((abs(field.ny) - .65f) / .30f).coerceIn(0f, 1f)
                value * value * (3f - 2f * value)
            } else {
                val rise2 = field.ny * field.ny
                val rise4 = rise2 * rise2
                val rise16 = rise4 * rise4 * rise4 * rise4
                rise2 + (
                    .5f * rise2 * abs(field.ny) * (1f + rise16) - rise2
                ) * style.rimFocus
            }
            val inner = max(inset - line, 0f)
            val band = (1f - inner / rimWidth).coerceIn(0f, 1f)
            val tail = (1f - inner / tailReach).coerceIn(0f, 1f)
            val shine = (style.rim * facing *
                (band + (tail - band) * style.rimTail)).coerceIn(0f, 1f)
            r += (initialAlpha - (1f - style.rimAdded) * r) * shine
            g += (initialAlpha - (1f - style.rimAdded) * g) * shine
            b += (initialAlpha - (1f - style.rimAdded) * b) * shine

            val outAlpha = initialAlpha * coverage
            if (outAlpha <= .001f) return 0
            fun channel(v: Float) = (255f * v.coerceIn(0f, initialAlpha) /
                max(initialAlpha, .0001f)).roundToInt().coerceIn(0, 255)
            return ((outAlpha * 255f).roundToInt().coerceIn(0, 255) shl 24) or
                (channel(r) shl 16) or (channel(g) shl 8) or channel(b)
        }

        for (y in 0 until height) {
            val start = y * width
            for (x in 0 until width) pixels[start + x] = shade(x + .5f, y + .5f)
        }
        val result = Bitmap.createBitmap(
            pixels, width, height, Bitmap.Config.ARGB_8888,
        )
        // Old display lists may still reference the previous frame's
        // bitmap until the UI thread presents the replacement; don't recycle.
        cachedImage = result
        cachedKey = key
        return result
    }

    private fun sample(data: GlassBackdropPixels, x: Float, y: Float): Sample {
        val ix = x.toInt().coerceIn(0, data.width - 1)
        val iy = y.toInt().coerceIn(0, data.height - 1)
        val pixel = data.pixels[iy * data.width + ix]
        return Sample(
            ((pixel ushr 16) and 255) / 255f,
            ((pixel ushr 8) and 255) / 255f,
            (pixel and 255) / 255f,
            (pixel ushr 24) / 255f,
        )
    }

    private fun luma(r: Float, g: Float, b: Float): Float =
        r * .2126f + g * .7152f + b * .0722f

    private fun rounded(
        x: Float, y: Float,
        cx: Float, cy: Float,
        halfW: Float, halfH: Float,
        radius: Float, exponent: Float,
    ): Field {
        val dx = x - cx
        val dy = y - cy
        val qx = abs(dx) - halfW + radius
        val qy = abs(dy) - halfH + radius
        val px = max(qx, 0f)
        val py = max(qy, 0f)
        val magnitude = if (exponent == 2f) hypot(px, py)
        else (px.pow(exponent) + py.pow(exponent)).pow(1f / exponent)
        val distance = magnitude + min(max(qx, qy), 0f) - radius
        val nx: Float
        val ny: Float
        if (qx > 0f && qy > 0f) {
            val gx = qx.pow(exponent - 1f)
            val gy = qy.pow(exponent - 1f)
            val length = max(hypot(gx, gy), .00001f)
            nx = gx / length
            ny = gy / length
        } else if (qx > qy) {
            nx = 1f
            ny = 0f
        } else {
            nx = 0f
            ny = 1f
        }
        return Field(
            distance,
            if (dx < 0f) -nx else nx,
            if (dy < 0f) -ny else ny,
        )
    }
}
