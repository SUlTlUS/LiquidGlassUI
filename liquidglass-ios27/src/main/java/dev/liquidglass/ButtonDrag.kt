package dev.liquidglass

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import com.zeaze.tianyinwallpaper.catalog.components.LiquidButtonStyle
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin
import kotlin.math.tanh

internal fun buttonDragTransform(
    standard: GlassStandard, bounds: Rect, press: Float, drag: Offset, density: Float,
    hover: Float = 0f, maxGrowthPx: Float = 16f * density,
    pressedExpansionPx: Float = LiquidButtonStyle.Default.pressedExpansion.value * density,
    dragInitialDerivative: Float = LiquidButtonStyle.Default.dragInitialDerivative,
    limitDrag: Boolean = true,
): MenuElasticTransform {
    val base = menuElasticTransform(bounds, (1f - press) * hover, Offset.Zero, maxGrowthPx)
    if (bounds.width <= 0f || bounds.height <= 0f) return base
    val shortest = minOf(bounds.width, bounds.height)
    val longest = maxOf(bounds.width, bounds.height)
    val amount = pressedExpansionPx / bounds.height
    val angle = atan2(drag.y, drag.x)
    // Translation and distortion are one motion: both consume this same vector.
    // Apply resistance before either calculation, preserving the drag direction.
    val responseDistance = longest * if (standard == GlassStandard.Ios26) 2.7f else 3f
    val distance = hypot(drag.x / responseDistance, drag.y / responseDistance)
    val response = if (limitDrag && distance > 0f) tanh(distance) / distance else 1f
    val resistedDrag = drag * response
    val pullX = shortest * tanh(dragInitialDerivative * resistedDrag.x / shortest)
    val pullY = shortest * tanh(dragInitialDerivative * resistedDrag.y / shortest)
    val stretchX = amount * abs(cos(angle) * resistedDrag.x / longest) *
        (bounds.width / bounds.height).coerceAtMost(1f)
    val stretchY = amount * abs(sin(angle) * resistedDrag.y / longest) *
        (bounds.height / bounds.width).coerceAtMost(1f)
    val restX = base.scaleX + amount * press
    val restY = base.scaleY + amount * press
    // Only 27 additionally budgets translation and stretch against the paint margin.
    fun resistance(pull: Float, stretch: Float, side: Float, restScale: Float): Float {
        if (!limitDrag || standard == GlassStandard.Ios26) return 1f
        val remaining = (maxGrowthPx - ((restScale - 1f) * side * .5f).coerceAtLeast(0f))
            .coerceAtLeast(0f)
        val extent = abs(pull) + stretch * side * .5f
        if (extent <= 0f) return 1f
        if (remaining <= 0f || !extent.isFinite()) return 0f
        val knee = remaining * .65f
        if (extent <= knee) return 1f
        val tail = remaining - knee
        return (knee + tail * tanh((extent - knee) / tail)) / extent
    }
    val resistanceX = resistance(pullX, stretchX, bounds.width, restX)
    val resistanceY = resistance(pullY, stretchY, bounds.height, restY)
    return base.copy(
        pull = Offset(pullX * resistanceX, pullY * resistanceY),
        scaleX = restX + stretchX * resistanceX,
        scaleY = restY + stretchY * resistanceY,
    )
}

@Composable
internal fun buttonAnimationStandard() = LocalGlassEffects.current.buttonAnimation(LocalGlassStandard.current)

internal fun elasticButtonTransform(state: ElasticPressState, bounds: Rect, density: Float, maxGrowthPx: Float = 16f * density) =
    buttonDragTransform(state.animationStandard, bounds,
        state.buttonPress.value, state.buttonDrag.value, density, hover = state.motion.hover.value,
        maxGrowthPx = maxGrowthPx, limitDrag = state.limitButtonDrag)
