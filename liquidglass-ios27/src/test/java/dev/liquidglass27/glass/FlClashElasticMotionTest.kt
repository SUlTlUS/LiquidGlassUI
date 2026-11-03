package dev.liquidglass27.glass

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import kotlin.math.abs
import kotlin.math.exp
import kotlin.math.sin
import kotlin.math.cos
import kotlin.math.sqrt
import org.junit.Assert.*
import org.junit.Test

class FlClashElasticMotionTest {
    @Test fun pressMatchesFlutter311SpringAt100ms() {
        val motion = FlClashElasticMotion()
        motion.press()
        motion.advance(.1f)
        val omega = sqrt(311.0)
        val damping = .593
        val wd = omega * sqrt(1 - damping * damping)
        val expected = 1 - exp(-damping * omega * .1) *
            (cos(wd * .1) + damping * omega / wd * sin(wd * .1))
        assertEquals(expected.toFloat(), motion.lift.value, .00001f)
    }

    @Test fun immediateTapStillRisesAndTurnsAtFirstSnapCrossing() {
        val motion = FlClashElasticMotion()
        motion.press()
        motion.release()
        assertEquals(1f, motion.lift.target, 0f)
        var previous = 0f
        repeat(300) {
            if (motion.lift.target == 0f) return@repeat
            previous = motion.lift.value
            motion.advance(1f / 240)
        }
        assertTrue(previous < .975f)
        assertTrue(motion.lift.value >= .975f)
        assertEquals(0f, motion.lift.target, 0f)
        assertTrue(motion.lift.velocity > 0f)
        repeat(600) { motion.advance(1f / 240) }
        assertEquals(0f, motion.lift.value, 0f)
        assertFalse(motion.moving)
    }

    @Test fun longHoldReleaseStartsImmediatelyAndRepressKeepsVelocity() {
        val motion = FlClashElasticMotion()
        motion.press()
        repeat(240) { motion.advance(1f / 120) }
        motion.release()
        assertEquals(0f, motion.lift.target, 0f)
        motion.advance(.08f)
        val value = motion.lift.value
        val velocity = motion.lift.velocity
        motion.press()
        assertEquals(value, motion.lift.value, 0f)
        assertEquals(velocity, motion.lift.velocity, 0f)
        assertTrue(velocity < 0f)
    }

    @Test fun dragReleaseRetainsRenderedPullAndVelocity() {
        val motion = FlClashElasticMotion()
        motion.press()
        motion.drag(Offset(50f, -20f), 14f)
        assertEquals(14f * (1f - 1f / (50f * .55f / 14f + 1)),
            motion.x.target, .00001f)
        motion.advance(.04f)
        val pull = motion.pull
        val velocity = motion.x.velocity
        motion.release()
        assertEquals(pull, motion.pull)
        assertEquals(velocity, motion.x.velocity, 0f)
        assertEquals(0f, motion.x.target, 0f)
        assertEquals(0f, motion.y.target, 0f)
    }

    @Test fun hoverDoesNotCountAsPressOrDoubleGrowFullPress() {
        val motion = FlClashElasticMotion()
        motion.hover(true)
        repeat(300) { motion.advance(1f / 120) }
        assertEquals(.4f, motion.effectiveLift, 0f)
        assertEquals(0f, motion.lift.value, 0f)
        motion.press()
        repeat(300) { motion.advance(1f / 120) }
        assertEquals(1f, motion.effectiveLift, 0f)
    }

    @Test fun gooAndForegroundUseMultiplicativePullStretchEvenAfterLiftDrops() {
        val transform = menuElasticTransform(Rect(0f, 0f, 80f, 52f),
            .6f, Offset(10f, -5f), 16f)
        assertEquals(1.12f * 1.0625f, transform.scaleX, .000001f)
        assertEquals(1.12f * (1f + 5f / 52f * .5f), transform.scaleY, .000001f)
        val released = menuElasticTransform(Rect(0f, 0f, 80f, 52f),
            0f, Offset(10f, 0f), 16f)
        assertTrue(released.scaleX > 1f)
    }

    @Test fun sameElapsedTimeAt60And120HzProducesSameHeldPosition() {
        val slow = FlClashElasticMotion().apply { press() }
        val fast = FlClashElasticMotion().apply { press() }
        repeat(12) { slow.advance(1f / 60) }
        repeat(24) { fast.advance(1f / 120) }
        assertTrue(abs(slow.lift.value - fast.lift.value) < .00001f)
    }
}
