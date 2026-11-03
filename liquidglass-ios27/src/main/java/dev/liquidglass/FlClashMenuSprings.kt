package dev.liquidglass

import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.spring
import kotlin.math.PI

/**
 * FlClash popup.dart uses Flutter's SpringDescription.withDurationAndBounce.
 * Flutter computes:
 *   stiffness = 4 * PI^2 / durationSeconds^2  (mass 1)
 *   dampingRatio = bounce > 0 ? 1 - bounce : 1 / (1 + bounce)
 *
 * Compose's spring(stiffness, dampingRatio) describes the same canonical
 * second-order system, but DOES NOT take duration/bounce directly. These
 * exact conversions preserve FlClash's intended physical parameters.
 *
 * IMPORTANT: Flutter's SpringSimulation uses Tolerance.defaultTolerance:
 * distance = 0.001 AND velocity = 0.001. Compose's default Float spring
 * visibilityThreshold is 0.01. That ends the .15-bounce spring near its
 * 1.006 overshoot peak, then snaps the menu bottom to the final edge in
 * one frame. Use the matching 0.001 position threshold so the complete
 * rebound is drawn rather than losing its last ~0.6% of travel abruptly.
 */
internal object FlClashMenuSprings {
    const val UNFOLD_DURATION_MS = 420
    const val UNFOLD_BOUNCE = .15f
    const val FOLD_DURATION_MS = 340
    const val FOLD_BOUNCE = 0f
    const val FLUTTER_POSITION_TOLERANCE = .001f

    fun stiffnessFor(durationMs: Int): Float {
        require(durationMs > 0)
        val seconds = durationMs / 1000.0
        return (4.0 * PI * PI / (seconds * seconds)).toFloat()
    }

    fun dampingRatioFor(bounce: Float): Float {
        require(bounce.isFinite() && bounce > -1f && bounce <= 1f)
        return if (bounce > 0f) 1f - bounce else 1f / (1f + bounce)
    }

    fun unfold(): SpringSpec<Float> = spring(
        dampingRatio = dampingRatioFor(UNFOLD_BOUNCE),
        stiffness = stiffnessFor(UNFOLD_DURATION_MS),
        visibilityThreshold = FLUTTER_POSITION_TOLERANCE,
    )

    fun fold(): SpringSpec<Float> = spring(
        dampingRatio = dampingRatioFor(FOLD_BOUNCE),
        stiffness = stiffnessFor(FOLD_DURATION_MS),
        visibilityThreshold = FLUTTER_POSITION_TOLERANCE,
    )
}
