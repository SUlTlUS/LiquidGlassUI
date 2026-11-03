package dev.liquidglass27.glass

import kotlin.math.log2

/** FlClash's base blur is independent of the optional Haze branch. */
internal fun sketchBaseBlurDp(style: GlassStyle): Float =
    style.blur.coerceAtLeast(0f)

internal fun sketchBaseSaturation(style: GlassStyle): Float =
    style.saturation.coerceAtLeast(0f)

/** Sketch's exposureAdjustment(ev) scales RGB by 2^(ev/2.2).
 * Convert FlClash's multiplicative brightness exactly into that scale.
 */
internal fun sketchBrightnessExposureEv(style: GlassStyle): Float =
    2.2f * log2(style.brightness.coerceAtLeast(.001f))
