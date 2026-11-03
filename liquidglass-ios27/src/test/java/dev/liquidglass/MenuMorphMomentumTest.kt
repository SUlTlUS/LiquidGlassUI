package dev.liquidglass

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import java.io.File
import kotlin.math.abs
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Continuous ownership across press -> drag -> release -> inertial boundary
 * -> another press, including submenu/dismiss invalidation.
 *
 * These are physical state-machine tests, not source-string-only assertions.
 */
class MenuMorphMomentumTest {
    private val stack = Rect(20f, 60f, 320f, 540f)

    @Test
    fun dragReleaseAndBottomImpactAddVelocityWithoutPositionJump() {
        val motion = MenuMorphMomentum()
        val track = menuTrackSpring()
        val release = menuPressSpring()
        motion.beginGesture()
        motion.press(track)
        motion.pullY.springTo(-40f, track)
        repeat(15) { motion.pullY.advance(1f / 120f) }
        motion.release()
        motion.pullY.springTo(0f, release)
        motion.pullY.advance(1f / 120f)
        val before = motion.pullY.value
        val oldVelocity = motion.pullY.velocity
        val ticket = motion.startFling()
        val impact = menuEdgeImpactVelocityPx(2200f, stack)
        assertTrue(impact < 0f)
        assertTrue(motion.edgeImpact(ticket, 2200f, stack, release))
        assertEquals(before, motion.pullY.value, 0f)
        assertEquals(oldVelocity + impact, motion.pullY.velocity, .0001f)
        // The entire card uses the same pullY, not a second translated layer.
        val transformed = menuElasticTransform(
            stack, .5f, Offset(0f, motion.pullY.value), 16f,
        )
        assertEquals(before, transformed.pull.y, 0f)
    }

    @Test
    fun freshTouchInterruptsCollisionAndInheritsItsPositionAndVelocity() {
        val motion = MenuMorphMomentum()
        val release = menuPressSpring()
        val ticket = motion.startFling()
        assertTrue(motion.edgeImpact(ticket, -2600f, stack, release))
        motion.pullY.advance(.027f)
        val position = motion.pullY.value
        val velocity = motion.pullY.velocity
        assertTrue(position > 0f)
        motion.beginGesture()
        motion.press(menuTrackSpring())
        assertTrue(motion.pointerHeld)
        assertEquals(position, motion.pullY.value, 0f)
        assertEquals(velocity, motion.pullY.velocity, 0f)
        motion.pullY.advance(.008f)
        assertTrue(motion.pullY.value.isFinite())
        assertFalse(motion.edgeImpact(ticket, 2000f, stack, release))
        motion.release()
        motion.pullY.springTo(0f, release)
        assertFalse(motion.pointerHeld)
    }

    @Test
    fun newGestureInvalidatesDelayedOldFlingCompletion() {
        val motion = MenuMorphMomentum()
        val first = motion.startFling()
        motion.beginGesture()
        assertFalse(motion.edgeImpact(first, 5000f, stack, menuPressSpring()))
        assertEquals(0f, motion.pullY.value, 0f)
        assertEquals(0f, motion.pullY.velocity, 0f)
        val second = motion.startFling()
        assertFalse(motion.edgeImpact(first, -5000f, stack, menuPressSpring()))
        assertTrue(motion.edgeImpact(second, -1800f, stack, menuPressSpring()))
    }

    @Test
    fun collisionCannotStartWhileFingerIsStillDragging() {
        val motion = MenuMorphMomentum()
        val release = menuPressSpring()
        val ticket = motion.startFling()
        motion.press(menuTrackSpring())
        assertFalse(motion.edgeImpact(ticket, -2500f, stack, release))
        motion.release()
        // Even after Up, a fling from before the new press is stale.
        assertFalse(motion.edgeImpact(ticket, -2500f, stack, release))
        val afterRelease = motion.startFling()
        assertTrue(motion.edgeImpact(afterRelease, -2500f, stack, release))
    }

    @Test
    fun hierarchyChangeInvalidatesFlingButPreservesActiveFinger() {
        val motion = MenuMorphMomentum()
        val track = menuTrackSpring()
        val release = menuPressSpring()
        val ticket = motion.startFling()
        motion.press(track)
        motion.pullY.springTo(40f, track)
        motion.pullY.advance(1f / 60f)
        val position = motion.pullY.value
        val velocity = motion.pullY.velocity
        motion.invalidate()
        assertTrue(motion.pointerHeld)
        assertEquals(position, motion.pullY.value, 0f)
        assertEquals(velocity, motion.pullY.velocity, 0f)
        assertFalse(motion.edgeImpact(ticket, 1800f, stack, release))
    }

    @Test
    fun dismissalSetsPointerFreeAndReturnsHomeWithoutSnapping() {
        val motion = MenuMorphMomentum()
        motion.beginGesture()
        motion.press(menuTrackSpring())
        motion.pullY.springTo(52f, menuTrackSpring())
        repeat(10) { motion.pullY.advance(1f / 120f) }
        val before = motion.pullY.value
        val velocity = motion.pullY.velocity
        val previous = motion.startFling()
        motion.settle(menuPressSpring())
        assertFalse(motion.pointerHeld)
        assertEquals(before, motion.pullY.value, 0f)
        assertEquals(velocity, motion.pullY.velocity, 0f)
        assertFalse(motion.edgeImpact(previous, -3000f, stack, menuPressSpring()))
        repeat(240) { motion.pullY.advance(1f / 120f) }
        assertEquals(0f, motion.pullY.value, .0011f)
        assertEquals(0f, motion.pullY.velocity, .0011f)
    }

    @Test
    fun repeatedEdgeImpactsAddVelocityRatherThanResettingAnimation() {
        val motion = MenuMorphMomentum()
        val release = menuPressSpring()
        val first = motion.startFling()
        assertTrue(motion.edgeImpact(first, 2800f, stack, release))
        motion.pullY.advance(.016f)
        val offset = motion.pullY.value
        val preceding = motion.pullY.velocity
        val second = motion.startFling()
        val newImpulse = menuEdgeImpactVelocityPx(-1200f, stack)
        assertTrue(motion.edgeImpact(second, -1200f, stack, release))
        assertEquals(offset, motion.pullY.value, 0f)
        assertEquals(preceding + newImpulse, motion.pullY.velocity, .0001f)
        assertFalse(motion.edgeImpact(first, 2800f, stack, release))
    }

    @Test
    fun pressLiftAndCombinedPullStayFiniteUnderRapidAlternation() {
        val motion = MenuMorphMomentum()
        val release = menuPressSpring()
        val track = menuTrackSpring()
        var lift = 0f
        for (round in 0 until 60) {
            motion.beginGesture()
            motion.press(track)
            lift = if (round % 2 == 0) .93f else .17f
            motion.pullY.springTo(if (round % 2 == 0) 40f else -40f, track)
            motion.pullY.advance(1f / 120f)
            motion.release()
            motion.pullY.springTo(0f, release)
            val ticket = motion.startFling()
            assertTrue(motion.edgeImpact(
                ticket, if (round % 2 == 0) 1800f else -1800f,
                stack, release,
            ))
            motion.pullY.advance(1f / 120f)
            val transform = menuElasticTransform(
                stack, menuEffectiveLift(lift, .1f),
                Offset(3f, motion.pullY.value), 16f,
            )
            assertTrue(transform.scaleX.isFinite() && transform.scaleY.isFinite())
            assertTrue(transform.pull.y.isFinite())
            val touch = Offset(180f, 220f)
            val painted = Offset(
                transform.center.x + (touch.x - transform.center.x) *
                    transform.scaleX + transform.pull.x,
                transform.center.y + (touch.y - transform.center.y) *
                    transform.scaleY + transform.pull.y,
            )
            assertEquals(touch.x, transform.inverse(painted).x, .002f)
            assertEquals(touch.y, transform.inverse(painted).y, .002f)
        }
        assertTrue(abs(lift) <= 1f)
    }

    @Test
    fun invalidOrUnboundedImpulseCannotCorruptSharedSpring() {
        val spring = MenuRetargetSpring()
        val release = menuPressSpring()
        assertFalse(spring.addVelocityImpulse(Float.NaN, release))
        assertFalse(spring.addVelocityImpulse(Float.POSITIVE_INFINITY, release))
        assertFalse(spring.addVelocityImpulse(0f, release))
        assertFalse(spring.addVelocityImpulse(100f, release, Float.NaN))
        assertEquals(0f, spring.value, 0f)
        assertEquals(0f, spring.velocity, 0f)
        assertTrue(spring.addVelocityImpulse(1000f, release))
        assertTrue(spring.addVelocityImpulse(Float.MAX_VALUE, release))
        assertFalse(spring.addVelocityImpulse(Float.MAX_VALUE, release))
        assertEquals(Float.MAX_VALUE, spring.velocity, 0f)
    }

    @Test
    fun integrationUsesOnlyOneYPositionAndRejectsStaleImpacts() {
        val source = File(
            "src/main/java/dev/liquidglass/GlassMenu.kt",
        ).readText()
        assertTrue(source.contains("val menuPullY = menuMorphMomentum.pullY"))
        assertTrue(source.contains("val flingTicket = menuMorphMomentum.startFling()"))
        assertTrue(source.contains("menuMorphMomentum.edgeImpact("))
        assertTrue(source.contains("menuMorphMomentum.beginGesture()"))
        assertTrue(source.contains("menuMorphMomentum.press(menuPullTrackSpec)"))
        assertTrue(source.contains("menuPullX.springTo(menuPullX.value, menuPullTrackSpec)"))
        assertTrue(source.contains("menuMorphMomentum.release()"))
        assertTrue(source.contains("menuMorphMomentum.invalidate()"))
        assertTrue(source.contains("Offset(menuPullX.value, menuPullY.value)"))
        assertFalse(source.contains("menuEdgeBounceY"))
    }
}
