package dev.liquidglass27.glass

import kotlin.math.max
import kotlin.math.roundToInt

/**
 * FlClash CommonPopupRoute uses ONE normalized AnimationController (0..1).
 * popup.dart:
 *   transitionDuration = 450ms
 *   didPop(): reverseDuration = (500ms or 200ms) / max(value, .001)
 *   reverse paint: dismissal = 1 - animation.value / _closingFrom
 *
 * Flutter's reverse() then travels the remaining portion of reverseDuration;
 * multiplying it by the captured starting value yields the FULL closing
 * duration, even if the route was only partway open when dismissed.
 *
 * No additional re-opening spring/crossfade is part of this route controller.
 * A reverse->forward transition simply animates that SAME position to 1.
 */
internal object FlClashPopupRouteMotion {
    const val OPEN_DURATION_MS = 450
    const val SOURCE_DISMISS_MS = 500
    const val PLAIN_DISMISS_MS = 200

    fun openingTimeSeconds(position: Float): Float =
        position.coerceIn(0f, 1f) * (OPEN_DURATION_MS / 1000f)

    fun openingRemainingMillis(position: Float): Int =
        ((1f - position.coerceIn(0f, 1f)) * OPEN_DURATION_MS)
            .roundToInt().coerceAtLeast(0)

    fun reverseDurationOverrideMillis(atPop: Float, returnIntoSource: Boolean): Float {
        val base = if (returnIntoSource) SOURCE_DISMISS_MS else PLAIN_DISMISS_MS
        return base / max(atPop, 1e-3f)
    }

    fun reverseTravelMillis(atPop: Float, returnIntoSource: Boolean): Int {
        if (atPop <= 0f) return 0
        // Flutter reverse() travels "atPop" of the scaled reverseDuration.
        return (reverseDurationOverrideMillis(atPop, returnIntoSource) *
            atPop.coerceIn(0f, 1f)).roundToInt()
    }

    fun dismissalFraction(position: Float, atPop: Float): Float =
        if (atPop <= 0f) 1f else (1f - position / atPop).coerceIn(0f, 1f)

    fun dismissalTimeSeconds(
        position: Float,
        atPop: Float,
        returnIntoSource: Boolean,
    ): Float {
        val duration = if (returnIntoSource) SOURCE_DISMISS_MS else PLAIN_DISMISS_MS
        return dismissalFraction(position, atPop) * duration / 1000f
    }
}
