package dev.liquidglass27.glass

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.sign
import kotlin.math.PI

/**
 * Flutter ElasticPress._RenderPressTransform._transform, in menu-root
 * coordinates. FlClash swells the ENTIRE stack, not just the highlighted row.
 *
 * The same matrix must be inverted for all physical row/header hit tests:
 * otherwise a pressed or rubber-banded popup looks right but hits the old
 * coordinates.
 */
internal data class MenuElasticTransform(
    val center: Offset,
    val scaleX: Float,
    val scaleY: Float,
    val pull: Offset,
) {
    fun inverse(point: Offset): Offset =
        Offset(
            center.x + (point.x - center.x - pull.x) / scaleX,
            center.y + (point.y - center.y - pull.y) / scaleY,
        )
}

internal fun menuElasticTransform(
    panel: Rect,
    lift: Float,
    pull: Offset,
    maxGrowthPx: Float,
): MenuElasticTransform {
    val longest = max(max(panel.width, panel.height), 1f)
    val swell = 1f + GlassMotion.pressGrowth(longest, maxGrowthPx) * lift
    val scaleX = swell * (1f + abs(pull.x) / max(panel.width, 1f) * .5f)
    val scaleY = swell * (1f + abs(pull.y) / max(panel.height, 1f) * .5f)
    return MenuElasticTransform(panel.center, scaleX, scaleY, pull)
}

/**
 * _RenderPressTransform.panelOf is null in FlClash's CommonPopupMenu, so its
 * spring acts on the whole Stack rather than only the animated top card.
 * Use the final layout bounds of each mounted level, not the morphing path:
 * a changing press pivot amplifies the submenu's motion on every frame.
 */
internal fun menuPopupStackBounds(levelBounds: List<Rect>): Rect {
    if (levelBounds.isEmpty()) return Rect.Zero
    var left = Float.POSITIVE_INFINITY
    var top = Float.POSITIVE_INFINITY
    var right = Float.NEGATIVE_INFINITY
    var bottom = Float.NEGATIVE_INFINITY
    for (panel in levelBounds) {
        left = minOf(left, panel.left)
        top = minOf(top, panel.top)
        right = maxOf(right, panel.right)
        bottom = maxOf(bottom, panel.bottom)
    }
    return Rect(left, top, right, bottom)
}

/**
 * Reconstruct the Flutter Stack's LAYOUT box, rather than unioning its
 * children's painted/screen-clamped boxes. popup.dart uses Stack aligned
 * topRight: every level starts at y >= root.top, widths expand LEFT from
 * the root's right edge, and the Stack grows DOWN as needed. ElasticPress's
 * panelOf is null, so _RenderPressTransform measures that complete layout
 * box (not the active row, current animated silhouette or full viewport).
 */
internal fun menuFlutterStackLayoutBounds(root: Rect, levelTargets: List<Rect>): Rect {
    val width = (listOf(root) + levelTargets).maxOf { it.width }
    val bottom = (listOf(root) + levelTargets).maxOf { it.bottom }
    return Rect(root.right - width, root.top, root.right, maxOf(root.bottom, bottom))
}

/** FlClash combines mouse hover with press, without double-growing it. */
internal fun menuEffectiveLift(press: Float, hover: Float): Float =
    press + (1f - press) * hover

/**
 * FlClash navigation_dock.dart::_rubberBand (NOT GlassMotion's tanh helper).
 * The original asymptote is approached gradually:
 *   limit * (1 - 1 / (abs(overshoot) * 0.55 / limit + 1)) * sign
 * A tanh approximation grows almost twice as much for a normal drag and
 * makes the subsequent spring back look abnormally abrupt.
 */
internal fun menuElasticRubberBand(overshoot: Float, limit: Float): Float {
    if (!overshoot.isFinite() || !limit.isFinite() || limit <= 0f) return 0f
    val growth = kotlin.math.abs(overshoot) * .55f / limit
    return limit * (1f - 1f / (growth + 1f)) * sign(overshoot)
}

/** Flutter pullPastEdges: never shift while the finger stays inside. */
internal fun menuElasticPull(panel: Rect, finger: Offset): Offset {
    if (panel.width <= 0f || panel.height <= 0f) return Offset.Zero
    val outside = Offset(
        finger.x - finger.x.coerceIn(panel.left, panel.right),
        finger.y - finger.y.coerceIn(panel.top, panel.bottom),
    )
    val limit = minOf(panel.width, panel.height) * (7f / 32f)
    return Offset(
        menuElasticRubberBand(outside.x, limit),
        menuElasticRubberBand(outside.y, limit),
    )
}

/**
 * When a list's inertial scroll hits its clamp, send the remaining momentum
 * into the SAME whole-stack ElasticPress transform used by finger pulls.
 * The impulse uses the finger's direction (opposite scroll-coordinate sign),
 * with the identical shortestSide * 7/32 rubber-band displacement budget.
 * This is an intentional extension of FlClash, which only pulls on a pointer.
 *
 * The spring starts at zero displacement with a velocity, rather than first
 * jumping to a displaced card rect and playing a separate bounce animation.
 */
internal fun menuEdgeImpactVelocityPx(
    remainingScrollVelocityPxPerSecond: Float,
    stack: Rect,
): Float {
    if (!remainingScrollVelocityPxPerSecond.isFinite() ||
        stack.width <= 0f || stack.height <= 0f) return 0f
    val limit = minOf(stack.width, stack.height) * (7f / 32f)
    return -sign(remainingScrollVelocityPxPerSecond) * minOf(
        kotlin.math.abs(remainingScrollVelocityPxPerSecond) * .18f,
        limit * 14f,
    )
}

/**
 * FlClash navigation_dock.dart _ElasticPressState._settle(): return starts
 * on the FIRST lift tick reaching _liftSnap=.975 after finger-up, not after
 * the spring settles at its target of 1. A quick tap must still reach this
 * threshold; a long press that has already passed it returns immediately.
 */
internal const val FLCLASH_MENU_LIFT_SNAP = .975f

internal fun shouldSettleMenuPress(releasing: Boolean, lift: Float): Boolean =
    releasing && lift >= FLCLASH_MENU_LIFT_SNAP

/**
 * Original ElasticPress pointer tracking: _trackSpring uses
 * SpringDescription.withDurationAndBounce(duration: 120ms), bounce 0.
 * Flutter's conversion is 4π²/(.12)² with critical damping ratio 1.
 */
internal fun menuTrackSpring() = spring<Float>(
    dampingRatio = 1f,
    stiffness = (4.0 * PI * PI / (.12 * .12)).toFloat(),
    visibilityThreshold = .001f,
)

/**
 * _settleSpring for ElasticHover: 500 ms with bounce .32, i.e.
 * dampingRatio = 1 - .32 and stiffness = (2π/.5)^2, mass 1.
 */
internal fun menuHoverSpring() = spring<Float>(
    dampingRatio = .68f,
    stiffness = (4.0 * PI * PI / (.5 * .5)).toFloat(),
    visibilityThreshold = .001f,
)

/**
 * Original ElasticPress release and press: mass 1, stiffness 311,
 * damping ratio .593. Flutter _Spring(rest=1e-3) stops only near rest.
 */
internal fun menuPressSpring() = spring<Float>(
    dampingRatio = .593f,
    stiffness = 311f,
    visibilityThreshold = .001f,
)
