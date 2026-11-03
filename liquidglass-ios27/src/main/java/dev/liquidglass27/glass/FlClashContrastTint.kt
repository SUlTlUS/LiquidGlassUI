package dev.liquidglass27.glass

import com.zeaze.tianyinwallpaper.backdrop.BackdropEffectScope
import com.zeaze.tianyinwallpaper.backdrop.effects.runtimeShaderEffect

/**
 * Sketch provides the material pixels; FlClash supplies the tint/contrast
 * equation. A raw backdrop probe cannot be read from Sketch's composited
 * RenderEffect, so the five taps sample the optical input instead.
 *
 * This only replaces the earlier unconditional solid tint overlay.
 * It never adds or replaces any Sketch/FlClash rim highlight.
 */
internal fun BackdropEffectScope.flClashContrastTint(
    shaderText: String,
    style: GlassStyle,
) {
    if (style.tint.alpha <= 0f && style.contrast == 0f) return
    val panelSize = size
    runtimeShaderEffect(
        key = "FlClashSketchContrastTint",
        shaderString = shaderText,
        uniformShaderName = "uContent",
    ) {
        setFloatUniform("uSize", panelSize.width, panelSize.height)
        setFloatUniform(
            "uTint",
            style.tint.red, style.tint.green,
            style.tint.blue, style.tint.alpha,
        )
        setFloatUniform("uContrast", style.contrast)
    }
}

/** A constant cover in FlClash equals src-over only when contrast=0. */
internal fun flClashContrastCover(
    alpha: Float,
    contrast: Float,
    tintLuma: Float,
    backgroundLuma: Float,
): Float {
    val base = alpha.coerceIn(0f, 1f)
    if (contrast == 0f) return base
    val sign = if (contrast > 0f) 1f else -1f
    val clash = ((sign * (tintLuma - backgroundLuma) - .3f) / .5f)
        .coerceIn(0f, 1f)
    return (base + (1f - base) * clash * kotlin.math.abs(contrast))
        .coerceIn(0f, 1f)
}
