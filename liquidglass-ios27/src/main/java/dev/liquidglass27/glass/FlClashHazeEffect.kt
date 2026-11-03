package dev.liquidglass27.glass

import android.graphics.BlendMode
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.RenderEffect
import android.graphics.Shader
import com.zeaze.tianyinwallpaper.backdrop.BackdropEffectScope
import com.zeaze.tianyinwallpaper.backdrop.effects.blur
import kotlin.math.max

/**
 * FlClash's original glass:
 *
 *   base = sampleFrosted(shift, style.blur)
 *   glass = mix(base, sampleHaze(shift), style.haze)
 *
 * Sketch's single blur(2dp) was not equivalent for Pill's 0dp base and
 * 15.6dp haze. Split the SAME incoming GPU layer into two independent
 * RenderEffect branches, weight their premultiplied alpha, and recombine.
 * No CPU/GPU readback, second geometry clip, second tint or highlight.
 *
 * Android BlendMode.PLUS adds premultiplied RGBA; branch alpha weights make
 * the sum an ordinary weighted mix rather than the alpha-over operation.
 */
@androidx.annotation.RequiresApi(33)
internal fun BackdropEffectScope.flClashFrost(style: GlassStyle) {
    val basePx = style.blur.coerceAtLeast(0f) * density
    val hazePx = style.hazeBlur.coerceAtLeast(0f) * density
    val haze = style.haze.coerceIn(0f, 1f)
    if (haze <= .00001f || hazePx <= 0f) {
        blur(basePx)
        return
    }

    // Preserve the upstream effect (if any) as the input to BOTH forks.
    // Never blur the base by hazePx on top of its original radius.
    val incoming = renderEffect
    fun branch(radius: Float): RenderEffect {
        if (radius <= .00001f) {
            return incoming ?: RenderEffect.createOffsetEffect(0f, 0f)
        }
        return if (incoming != null) {
            RenderEffect.createBlurEffect(
                radius, radius, incoming, Shader.TileMode.CLAMP,
            )
        } else {
            RenderEffect.createBlurEffect(radius, radius, Shader.TileMode.CLAMP)
        }
    }
    fun scaled(input: RenderEffect, fraction: Float): RenderEffect {
        val matrix = ColorMatrix(
            floatArrayOf(
                1f, 0f, 0f, 0f, 0f,
                0f, 1f, 0f, 0f, 0f,
                0f, 0f, 1f, 0f, 0f,
                0f, 0f, 0f, fraction, 0f,
            ),
        )
        return RenderEffect.createColorFilterEffect(
            ColorMatrixColorFilter(matrix), input,
        )
    }
    renderEffect = RenderEffect.createBlendModeEffect(
        scaled(branch(hazePx), haze),
        scaled(branch(basePx), 1f - haze),
        BlendMode.PLUS,
    )
    // Blur branches need extra source pixels so the sampled texture does
    // not clamp against the pane boundary. Sketch uses the same padding
    // convention when blur follows an existing color/lens effect.
    padding = max(padding, max(basePx, hazePx))
}

internal fun flClashHazeWeights(style: GlassStyle): Pair<Float, Float> {
    val t = if (style.hazeBlur > 0f) style.haze.coerceIn(0f, 1f) else 0f
    return (1f - t) to t
}
