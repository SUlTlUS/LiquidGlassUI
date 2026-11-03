package dev.liquidglass

import kotlin.math.abs
import kotlin.math.exp
import kotlin.math.max
import kotlin.math.min

/** Pixel-space pane, center and half extents, as in FlClash's uPills. */
data class GlassPill(
    val centerX: Float,
    val centerY: Float,
    val halfWidth: Float,
    val halfHeight: Float,
    /** Button contours follow the motion preset, never the material renderer. */
    val buttonEffect: GlassStandard? = null,
    val restHalfWidth: Float = halfWidth,
    val restHalfHeight: Float = halfHeight,
)

internal fun glassPillScales(pills: List<GlassPill>): FloatArray = FloatArray(12) { index ->
    val pill = pills.getOrNull(index / 2)
    if (pill == null || !usesStretchedButtonContour(pill.buttonEffect)) 1f
    else if (index % 2 == 0) pill.halfWidth / pill.restHalfWidth.coerceAtLeast(.001f)
    else pill.halfHeight / pill.restHalfHeight.coerceAtLeast(.001f)
}

object GlassMotion {
    const val PRESS_STIFFNESS = 165f
    const val PRESS_DAMPING = .8f
    const val POPUP_STIFFNESS = 400f
    const val POPUP_DAMPING = .754f
    const val CORNERS_STIFFNESS = 324f
    const val CORNERS_DAMPING = .75f

    fun pressGrowth(longestSidePx: Float, maxGrowthPx: Float): Float =
        min(.25f, maxGrowthPx / max(longestSidePx, 1f))

    fun rubberBand(pull: Float, limit: Float): Float {
        val constrainedLimit = max(limit, .001f)
        return constrainedLimit * (1f - 2f / (exp(2f * pull / constrainedLimit) + 1f))
    }

    /** Same polynomial smooth minimum used by FlClash's goo SDF. */
    fun smoothMinimum(a: Float, b: Float, blend: Float): Float {
        if (blend <= 0f) return min(a, b)
        val h = (.5f + .5f * (b - a) / blend).coerceIn(0f, 1f)
        return b + (a - b) * h - blend * h * (1f - h)
    }

    fun inSameRow(a: GlassPill, b: GlassPill): Boolean =
        abs(a.centerY - b.centerY) < min(a.halfHeight, b.halfHeight)
}
