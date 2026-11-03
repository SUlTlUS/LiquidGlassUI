package dev.liquidglass27.glass

import androidx.compose.animation.core.SpringSpec
import androidx.compose.ui.geometry.Rect

/**
 * Arbitrates pointer drag, menu press and inertial scroll impact.
 *
 * A single Y channel is used for BOTH the finger's rubber-band drag and the
 * extra user-requested boundary collision. Two independently translating
 * springs must never be added together: that duplicates velocity, creates
 * sudden target changes on Down, and can leave a canceled Animatable frozen
 * halfway between two positions.
 *
 * Epochs prevent a late, already-finished scroll task from firing an edge
 * collision after a new Down, submenu transition or popup dismissal.
 */
internal class MenuMorphMomentum {
    val pullY = MenuRetargetSpring()

    private var epoch: Long = 0L
    var pointerHeld: Boolean = false
        private set

    fun invalidate(): Long {
        epoch++
        return epoch
    }

    fun beginGesture(): Long = invalidate()

    fun startFling(): Long = invalidate()

    /** A fresh physical press gains ownership of the current Y spring. */
    fun press(trackSpring: SpringSpec<Float>) {
        // A carried pointer may start pressing without a fresh Down in
        // this overlay; revoke any earlier fling ticket here as well.
        invalidate()
        pointerHeld = true
        // Current displacement and velocity remain unchanged. Only the
        // autonomous return spring is replaced with critically damped track
        // physics until a real pointer Move supplies the next target.
        pullY.springTo(pullY.value, trackSpring)
    }

    fun release() { pointerHeld = false }

    /** A menu transition cancels the old scroll owner, not the Y position. */
    fun settle(releaseSpring: SpringSpec<Float>) {
        invalidate()
        pointerHeld = false
        pullY.springTo(0f, releaseSpring)
    }

    /** Called only by the fling belonging to the current menu generation. */
    fun edgeImpact(
        ticket: Long,
        remainingScrollVelocity: Float,
        stack: Rect,
        releaseSpring: SpringSpec<Float>,
    ): Boolean {
        if (ticket != epoch || pointerHeld) return false
        val impact = menuEdgeImpactVelocityPx(remainingScrollVelocity, stack)
        return pullY.addVelocityImpulse(impact, releaseSpring)
    }
}
