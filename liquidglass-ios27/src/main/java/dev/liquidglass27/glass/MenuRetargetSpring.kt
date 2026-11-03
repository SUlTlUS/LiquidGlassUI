package dev.liquidglass27.glass

import androidx.compose.animation.core.SpringSpec
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.withFrameNanos
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.exp
import kotlin.math.sin
import kotlin.math.sqrt
import kotlinx.coroutines.flow.first

/**
 * Flutter navigation_dock.dart::_Spring, adapted to Compose's frame clock.
 *
 * Flutter does not call AnimationController.animateWith() on each Move: that
 * would restart the ticker and never move while the pointer is moving.
 * Instead springTo() retargets the same simulation at its last rendered value
 * AND instantaneous velocity, without restarting the active frame clock.
 *
 * This channel advances by the analytic solution of the damped spring (not
 * Euler steps), which also keeps a drag followed immediately by Up continuous.
 * Unlike Animatable.animateTo(), a new target never cancels a coroutine.
 */
internal class MenuRetargetSpring(initial: Float = 0f, private val rest: Float = .001f) {
    var value by mutableFloatStateOf(initial)
        private set
    var velocity by mutableFloatStateOf(0f)
        private set
    var moving by mutableStateOf(false)
        private set
    var target by mutableFloatStateOf(initial)
        private set
    private var stiffness = 311f
    private var dampingRatio = .593f
    val acceleration: Float get() =
        -stiffness * (value - target) - 2f * dampingRatio * sqrt(stiffness) * velocity

    fun jumpTo(next: Float) {
        value = next
        target = next
        velocity = 0f
        moving = false
    }

    fun springTo(nextTarget: Float, spec: SpringSpec<Float>) {
        if (!nextTarget.isFinite()) return
        target = nextTarget
        stiffness = spec.stiffness
        dampingRatio = spec.dampingRatio
        // Flutter's _Spring leaves a running ticker intact; an idle one
        // starts anew only when the current position/velocity warrants it.
        moving = abs(value - target) > rest || abs(velocity) > rest *
            sqrt(stiffness.coerceAtLeast(.001f))
        if (!moving) {
            value = target
            velocity = 0f
        }
    }

    /**
     * Inject an inertial boundary collision into this SAME physical channel.
     * Keeping the displacement and adding the instantaneous velocity makes
     * the collision continuous even while a finger-release spring is active.
     * A following finger Move retargets this channel rather than competing
     * with an independent bounce animator.
     */
    fun addVelocityImpulse(
        velocityDelta: Float,
        spec: SpringSpec<Float>,
        returnTarget: Float = 0f,
    ): Boolean {
        if (!velocityDelta.isFinite() || velocityDelta == 0f ||
            !returnTarget.isFinite()) return false
        val combined = velocity + velocityDelta
        if (!combined.isFinite()) return false
        velocity = combined
        springTo(returnTarget, spec)
        return true
    }

    /** Step from the previous rendered frame; returns whether still moving. */
    fun advance(seconds: Float): Boolean {
        if (!moving) return false
        if (!seconds.isFinite() || seconds <= 0f) return moving
        val dt = seconds.toDouble()
        val omega = sqrt(stiffness.toDouble())
        val z = dampingRatio.toDouble()
        val start = (value - target).toDouble()
        val v = velocity.toDouble()
        val end: Double
        val endVelocity: Double
        when {
            z < 1.0 - 1e-7 -> {
                val wd = omega * sqrt(1.0 - z * z)
                val a = start
                val b = (v + z * omega * start) / wd
                val e = exp(-z * omega * dt)
                val c = cos(wd * dt)
                val s = sin(wd * dt)
                end = e * (a * c + b * s)
                endVelocity = e * (
                    -z * omega * (a * c + b * s) +
                        wd * (-a * s + b * c)
                )
            }
            z > 1.0 + 1e-7 -> {
                val root = sqrt(z * z - 1.0)
                val slow = -omega * (z - root)
                val fast = -omega * (z + root)
                val a = (v - fast * start) / (slow - fast)
                val b = start - a
                val slowVal = a * exp(slow * dt)
                val fastVal = b * exp(fast * dt)
                end = slowVal + fastVal
                endVelocity = slow * slowVal + fast * fastVal
            }
            else -> {
                val c = v + omega * start
                val e = exp(-omega * dt)
                end = e * (start + c * dt)
                endVelocity = e * (v - omega * c * dt)
            }
        }
        value = (target.toDouble() + end).toFloat()
        velocity = endVelocity.toFloat()
        // navigation_dock.dart::_Spring(rest=1e-3) passes both tolerances:
        // distance .001, velocity .001 * sqrt(stiffness / mass).
        if (abs(value - target) < rest &&
            abs(velocity) < rest * omega.toFloat()) {
            value = target
            velocity = 0f
            moving = false
        }
        return moving
    }
}

/** One long-lived frame driver for X/Y menu drag and mouse hover springs. */
internal suspend fun driveMenuRetargetSprings(
    x: MenuRetargetSpring,
    y: MenuRetargetSpring,
    hover: MenuRetargetSpring,
) {
    val springs = listOf(x, y, hover)
    while (true) {
        // Idle menus do not recompose/animate on every 60/120 Hz frame.
        snapshotFlow { springs.any { it.moving } }.first { it }
        var lastFrame = withFrameNanos { it }
        while (springs.any { it.moving }) {
            val frame = withFrameNanos { it }
            val delta = ((frame - lastFrame) / 1_000_000_000.0)
                .coerceAtLeast(0.0).toFloat()
            lastFrame = frame
            for (spring in springs) spring.advance(delta)
        }
    }
}
