package dev.liquidglass

import com.zeaze.tianyinwallpaper.backdrop.BackdropEffectScope
import com.zeaze.tianyinwallpaper.backdrop.effects.runtimeShaderEffect

/**
 * Uses Sketch's live, coordinate-mapped background and native color/blur
 * stages, replacing the default Sketch Lens with FlClash's refraction
 * formula for ordinary rectangles, outlines and Goo fused geometry.
 *
 * Goo has its own smooth-min SDF; ordinary panes use the outlined or
 * rounded-box SDF. Chromatic splitting remains within the same shader,
 * never as a second refracting overlay.
 *
 * This single refraction stage operates BEFORE the Goo shape clips the
 * background. It never adds a second lens or any replacement highlights.
 */
internal fun BackdropEffectScope.gooAwareSketchLens(
    shaderText: String,
    morph: GlassMorphShape?,
    outline: GlassOutlineRect?,
    pills: List<GlassPill>,
    blendsDp: List<Float>,
    densityPx: Float,
    cornerPower: Float,
    radiusPx: Float,
    refractionDp: Float,
    bevelDp: Float,
    dispersionDp: Float,
    endShade: Float,
) {
    if (refractionDp == 0f && dispersionDp <= 0f) return
    val panelSize = size
    // Keep a stable key within Sketch's RuntimeShaderCache. Changes to
    // uniforms do not change compiled shader identity.
    runtimeShaderEffect(
        key = "FlClashGooRefraction",
        shaderString = shaderText,
        uniformShaderName = "uContent",
    ) {
        setFloatUniform("uSize", panelSize.width, panelSize.height)
        setFloatUniform(
            "uOptics", refractionDp * densityPx, bevelDp * densityPx,
        )
        setFloatUniform("uDispersion", dispersionDp * densityPx)
        setFloatUniform("uEndShade", endShade)
        setFloatUniform("uDensity", densityPx)
        setFloatUniform("uRadius", radiusPx)
        setFloatUniform("uCornerPower", cornerPower)
        setIntUniform("uOutlineEnabled", if (outline != null) 1 else 0)
        val bounds = outline?.bounds
        setFloatUniform(
            "uOutlineRect",
            bounds?.center?.x ?: 0f, bounds?.center?.y ?: 0f,
            (bounds?.width ?: 0f) * .5f, (bounds?.height ?: 0f) * .5f,
        )
        setFloatUniform("uOutlineRadius", outline?.radius ?: 0f)
        setIntUniform("uMorphEnabled", if (morph != null) 1 else 0)
        val platter = morph?.platter
        val droplet = morph?.droplet
        setFloatUniform(
            "uMorphPlatter",
            platter?.center?.x ?: 0f, platter?.center?.y ?: 0f,
            (platter?.width ?: 0f) * .5f, (platter?.height ?: 0f) * .5f,
        )
        setFloatUniform(
            "uMorphDroplet",
            droplet?.center?.x ?: 0f, droplet?.center?.y ?: 0f,
            (droplet?.width ?: 0f) * .5f,
            (droplet?.height ?: 0f) * .5f,
        )
        setFloatUniform(
            "uMorphSpec", morph?.radius ?: 0f, morph?.blend ?: 0f,
        )
        setIntUniform("uCount", minOf(6, pills.size))
        setFloatUniform("uPillScales", glassPillScales(pills))
        for (index in 0..5) {
            val pill = pills.getOrNull(index)
            setFloatUniform(
                "uPill$index",
                pill?.centerX ?: 0f, pill?.centerY ?: 0f,
                pill?.halfWidth ?: 0f, pill?.halfHeight ?: 0f,
            )
        }
        setFloatUniform(
            "uBlends",
            (blendsDp.getOrNull(0) ?: 0f) * densityPx,
            (blendsDp.getOrNull(1) ?: 0f) * densityPx,
            (blendsDp.getOrNull(2) ?: 0f) * densityPx,
            (blendsDp.getOrNull(3) ?: 0f) * densityPx,
        )
        setFloatUniform(
            "uBlendLast",
            (blendsDp.getOrNull(4) ?: 0f) * densityPx,
        )
    }
}
