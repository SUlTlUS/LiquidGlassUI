package dev.liquidglass27.glass

import android.graphics.RectF
import android.graphics.RenderEffect
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import kotlin.math.min
import kotlin.math.max

/**
 * FlClash uses radial outlineGeometry for outlined non-Goo panes only.
 * Goo uses the smooth-min field regardless of whether its union happens
 * to be star-shaped. The same rule applies to Sketch's Goo refraction.
 */
internal fun mayUseRadialProfileForHighlight(
    hasNativePath: Boolean,
    hasMorph: Boolean,
    pillCount: Int,
): Boolean = hasNativePath && !hasMorph && pillCount == 0

/**
 * The original FlClash button/Goo highlighter, separated from the optical
 * material. This is an Android RenderEffect that accepts the ACTUAL Sketch
 * material as the child shader, then applies FlClash's control rim maths.
 *
 * In particular it does not capture pixels, apply blur/lens twice, use the
 * Sketch highlight, or draw an unrelated white stroke over the edge.
 */
@androidx.annotation.RequiresApi(33)
@Composable
internal fun rememberFlClashControlHighlight(
    style: GlassStyle,
    cornerDp: Float,
    pills: List<GlassPill>,
    blendsDp: List<Float>,
    morph: GlassMorphShape?,
    outline: GlassOutlineRect?,
    nativePath: android.graphics.Path?,
    glow: Offset,
    glowStrength: Float,
    glowReachDp: Float? = null,
    glowWash: Float = 0f,
): Modifier {
    val context = LocalContext.current
    val density = LocalDensity.current.density
    val renderer = remember(context) { ControlHighlightRenderer(context) }
    return Modifier.graphicsLayer {
        renderEffect = renderer.effect(ControlHighlightInput(
            style, cornerDp, pills, blendsDp, morph, outline, nativePath,
            glow, glowStrength, glowReachDp, glowWash, density,
            IntSize(size.width.toInt(), size.height.toInt()),
        ))
    }
}

internal data class ControlHighlightInput(
    val style: GlassStyle, val cornerDp: Float,
    val pills: List<GlassPill>, val blendsDp: List<Float>,
    val morph: GlassMorphShape?, val outline: GlassOutlineRect?,
    val nativePath: android.graphics.Path?, val glow: Offset,
    val glowStrength: Float, val glowReachDp: Float?, val glowWash: Float,
    val density: Float, val size: IntSize,
)

/** One shader owner. Geometry/uniform changes are observed in the drawing layer. */
@androidx.annotation.RequiresApi(33)
internal class ControlHighlightRenderer(
    private val context: android.content.Context,
    private val profileSamples: Int = 64,
) {
    private val shader by lazy {
        runCatching { GlassShaderPrograms.obtainControl(context, profileSamples) }
            .onFailure { Log.e("LiquidGlass27", "FlClash control highlight compile failed", it) }
            .getOrNull()
    }
    private val cornerCache = GlassCornerCurveCache()
    private val pillCurves = FloatArray(24)
    private val pillCenters = FloatArray(12)
    private val emptyProfile = FloatArray(profileSamples)
    private var previous: ControlHighlightInput? = null
    private var previousEffect: androidx.compose.ui.graphics.RenderEffect? = null
    private var profiledPath: android.graphics.Path? = null
    private var profiledCenter = Offset.Unspecified
    private var profile: GlassRadialProfile? = null

    fun effect(input: ControlHighlightInput): androidx.compose.ui.graphics.RenderEffect? = with(input) {
        if (previous == input) return previousEffect
        val hasControlLight = style.rim > .0001f || style.hairline > .0001f ||
            style.hairlineSide > .0001f || glowStrength > .0001f
        if (!hasControlLight || size.width <= 0 || size.height <= 0) return null
        val shader = shader ?: return null
        val allowProfile = mayUseRadialProfileForHighlight(
            nativePath != null, morph != null, pills.size,
        )
        val center = if (allowProfile && nativePath != null) {
            outline?.bounds?.center ?: RectF().also { nativePath.computeBounds(it, true) }
                .let { Offset(it.centerX(), it.centerY()) }
        } else Offset.Unspecified
        if (nativePath !== profiledPath || center != profiledCenter) {
            profile = if (allowProfile && nativePath != null)
                liquidGlassRadialProfile(nativePath, center, profileSamples) else null
            profiledPath = nativePath
            profiledCenter = center
        }
        val profile = profile
        val radius = min(cornerDp * density, min(size.width, size.height) * .5f)
        val result = runCatching {
            shader.setFloatUniform(
                "uPanelSize", size.width.toFloat(), size.height.toFloat(),
            )
            shader.setFloatUniform("uRadius", radius)
            val corners = cornerCache.curve(
                0,
                outline?.bounds?.width ?: size.width.toFloat(),
                outline?.bounds?.height ?: size.height.toFloat(),
                outline?.radius ?: radius,
            )
            shader.setFloatUniform("uCornerAlong", corners[0], corners[1], corners[2], corners[3])
            shader.setFloatUniform("uCornerAcross", corners[4], corners[5], corners[6], corners[7])
            shader.setFloatUniform("uCornerCenters", corners[8], corners[9], corners[10], corners[11])
            shader.setFloatUniform("uDensity", density)
            shader.setFloatUniform("uRim",
                style.rim, style.rimWidth * density,
                style.rimTail, style.rimFocus)
            shader.setFloatUniform("uRimAdded", style.rimAdded)
            shader.setFloatUniform("uEdge",
                style.hairline, style.hairlineSide, style.endShade)
            shader.setFloatUniform("uRefraction", style.refraction)
            shader.setFloatUniform("uGlow",
                if (glow == Offset.Unspecified) size.width * .5f else glow.x,
                if (glow == Offset.Unspecified) size.height * .5f else glow.y,
                glowStrength,
                glowReachDp?.times(density) ?: (min(size.width, size.height) * .7f),
            )
            shader.setFloatUniform("uGlowWash", glowWash)
            shader.setIntUniform("uMorphEnabled", if (morph != null) 1 else 0)
            shader.setIntUniform("uOutlineEnabled", if (outline != null) 1 else 0)
            val panel = morph?.platter ?: Rect.Zero
            val drop = morph?.droplet ?: Rect.Zero
            shader.setFloatUniform("uMorphPlatter",
                panel.center.x, panel.center.y,
                panel.width * .5f, panel.height * .5f)
            shader.setFloatUniform("uMorphDroplet",
                drop.center.x, drop.center.y,
                drop.width * .5f, drop.height * .5f)
            shader.setFloatUniform("uMorphSpec",
                morph?.radius ?: 0f, morph?.blend ?: 0f)
            val shape = outline?.bounds ?: Rect.Zero
            shader.setFloatUniform("uOutlineRect",
                shape.center.x, shape.center.y,
                shape.width * .5f, shape.height * .5f)
            shader.setFloatUniform("uOutlineRadius", outline?.radius ?: 0f)
            shader.setIntUniform("uCount", min(6, pills.size))
            for (i in 0..5) {
                val pill = pills.getOrNull(i)
                shader.setFloatUniform("uPill$i",
                    pill?.centerX ?: 0f, pill?.centerY ?: 0f,
                    pill?.halfWidth ?: 0f, pill?.halfHeight ?: 0f)
                val width = if (morph != null && i == 1) drop.width
                    else (pill?.halfWidth ?: 0f) * 2f
                val height = if (morph != null && i == 1) drop.height
                    else (pill?.halfHeight ?: 0f) * 2f
                val curve = cornerCache.curve(
                    i + 1,
                    max(width, height), min(width, height), min(width, height) * .5f,
                )
                curve.copyInto(pillCurves, i * 4, 0, 4)
                curve.copyInto(pillCenters, i * 2, 8, 10)
            }
            shader.setFloatUniform("uPillCurves", pillCurves)
            shader.setFloatUniform("uPillCenters", pillCenters)
            shader.setFloatUniform("uBlends",
                (blendsDp.getOrNull(0) ?: 0f) * density,
                (blendsDp.getOrNull(1) ?: 0f) * density,
                (blendsDp.getOrNull(2) ?: 0f) * density,
                (blendsDp.getOrNull(3) ?: 0f) * density)
            shader.setFloatUniform("uBlendLast",
                (blendsDp.getOrNull(4) ?: 0f) * density)
            shader.setIntUniform("uProfileEnabled", if (profile == null) 0 else 1)
            shader.setFloatUniform("uProfileCenter",
                profile?.center?.x ?: 0f, profile?.center?.y ?: 0f)
            shader.setFloatUniform("uProfile", profile?.radii ?: emptyProfile)
            RenderEffect.createRuntimeShaderEffect(shader, "uContent")
                .asComposeRenderEffect()
        }.onFailure {
            Log.e("LiquidGlass27", "FlClash control highlight setup failed", it)
        }.getOrNull()
        previous = input
        previousEffect = result
        result
    }
}
