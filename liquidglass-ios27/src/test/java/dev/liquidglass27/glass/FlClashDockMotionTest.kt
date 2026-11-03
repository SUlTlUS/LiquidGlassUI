package dev.liquidglass27.glass

import androidx.compose.ui.geometry.Offset
import org.junit.Assert.*
import org.junit.Test

class FlClashDockMotionTest {
    @Test fun narrowBarKeeps84dpLensAndAdjustsDestinationPitch() {
        val layout = dockLayout(320f, 5, 1f)
        assertEquals(84f, layout.lens, 0f)
        assertEquals(59f, layout.pitch, 0f)
        val first = dockLensRect(0f, 0f, layout, 54f, 0f, 1f)
        val last = dockLensRect(4f, 0f, layout, 54f, 0f, 1f)
        assertEquals(0f, first.left, 0f)
        assertEquals(320f, last.right, 0f)
    }

    @Test fun singleDestinationOccupiesWholeInnerSpan() {
        val layout = dockLayout(92f, 1, 1f)
        assertEquals(92f, dockLensRect(0f, 0f, layout, 54f, 0f, 1f).width, 0f)
    }

    @Test fun liftExpandsBothEdgesAndJellySquashesHeight() {
        val layout = dockLayout(352f, 4, 1f)
        val rest = dockLensRect(2f, 0f, layout, 54f, 0f, 1f)
        val lifted = dockLensRect(2f, 0f, layout, 54f, 1f, 1f)
        assertEquals(rest.width + 16f, lifted.width, .0001f)
        assertEquals(rest.height + 16f, lifted.height, .0001f)
        val jelly = dockLensRect(2f, .1f, layout, 54f, 1f, 1f)
        assertEquals(lifted.width * 1.1f, jelly.width, .0001f)
        assertEquals(lifted.height * .86f, jelly.height, .0001f)
        assertEquals(lifted.center.x, jelly.center.x, .0001f)
        assertEquals(lifted.center.y, jelly.center.y, .0001f)
    }

    @Test fun pointerMovesContinuouslyAndOverdragHasAsymptote() {
        val layout = dockLayout(352f, 4, 1f)
        assertEquals(1.25f, dockPositionAt(4f + layout.inset + 1.75f * layout.pitch,
            layout, 4, 1f), .00001f)
        assertTrue(dockPositionAt(-10000f, layout, 4, 1f) > -.35f)
        assertTrue(dockPositionAt(10000f, layout, 4, 1f) < 3.35f)
    }

    @Test fun flickCanAdvanceAtMostOneDestination() {
        assertEquals(3, dockFlingIndex(2.1f, 2, 100000f, 86f, 5))
        assertEquals(1, dockFlingIndex(2.1f, 2, -100000f, 86f, 5))
        assertEquals(0, dockFlingIndex(0f, 0, -100000f, 86f, 5))
    }

    @Test fun shortTapWaitsForLiftAndArrivalBeforeDropping() {
        val motion = FlClashDockMotion(0, 4)
        motion.press(Offset(300f, 30f), 3f)
        assertEquals(3, motion.release(true, 0f, 86f))
        motion.select(3)
        repeat(10) { motion.advance(1f / 120) }
        assertEquals(1f, motion.lift.target, 0f)
        repeat(300) { motion.advance(1f / 120) }
        assertEquals(0f, motion.lift.value, 0f)
        assertEquals(3f, motion.lens.value, 0f)
    }

    @Test fun cancelReturnsToSelectedAndNeverCommits() {
        val motion = FlClashDockMotion(1, 4)
        motion.press(Offset(300f, 30f), 3f)
        motion.drag(Offset(270f, 30f), 2.7f, 360f, 62f, 18f)
        assertNull(motion.release(false, 2000f, 86f))
        assertEquals(1f, motion.lens.target, 0f)
    }

    @Test fun hoverLeansLensAndOnlyNearbyContent() {
        val motion = FlClashDockMotion(1, 4)
        motion.hover(1.4f)
        assertEquals(1.08f, motion.lens.target, .00001f)
        assertEquals(.04f, motion.parallax[1].target, .00001f)
        assertEquals(0f, motion.parallax[2].target, 0f)
        motion.hover(null)
        assertEquals(1f, motion.lens.target, 0f)
        assertEquals(0f, motion.parallax[1].target, 0f)
    }

    @Test fun dragThresholdAndFractionalTargetPreserveVelocity() {
        val motion = FlClashDockMotion(0, 4)
        motion.press(Offset(40f, 30f), 0f)
        motion.drag(Offset(48f, 30f), .1f, 360f, 62f, 18f)
        assertEquals(0f, motion.lens.target, 0f)
        motion.drag(Offset(140f, 30f), 1.25f, 360f, 62f, 18f)
        motion.advance(.03f)
        val speed = motion.lens.velocity
        motion.drag(Offset(150f, 30f), 1.4f, 360f, 62f, 18f)
        assertEquals(1.4f, motion.lens.target, 0f)
        assertEquals(speed, motion.lens.velocity, 0f)
        assertTrue(motion.jelly.value > 0f)
    }
}
