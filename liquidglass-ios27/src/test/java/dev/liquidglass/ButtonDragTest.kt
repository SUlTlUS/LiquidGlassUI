package dev.liquidglass

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import org.junit.Assert.*
import org.junit.Test

class ButtonDragTest {
    private val bounds = Rect(0f, 0f, 48f, 48f)

    @Test fun unlimitedProfilesShareTheSameMotionButKeepTheirContourPolicy() {
        for (drag in listOf(Offset.Zero, Offset(40f, 20f), Offset(1000f, -500f))) {
            assertEquals(buttonDragTransform(GlassStandard.Ios26, bounds, 1f, drag, 1f, limitDrag = false),
                buttonDragTransform(GlassStandard.Ios27, bounds, 1f, drag, 1f, limitDrag = false))
        }
        assertTrue(usesStretchedButtonContour(GlassStandard.Ios26))
        assertFalse(usesStretchedButtonContour(GlassStandard.Ios27))
    }

    @Test fun profile26OnlyStretchesTheDraggedAxisBeyondPressScale() {
        val rest = buttonDragTransform(GlassStandard.Ios26, bounds, 1f, Offset.Zero, 1f)
        val x = buttonDragTransform(GlassStandard.Ios26, bounds, 1f, Offset(48f, 0f), 1f)
        val y = buttonDragTransform(GlassStandard.Ios26, bounds, 1f, Offset(0f, -48f), 1f)
        assertEquals(rest.scaleY, x.scaleY, .00001f)
        assertEquals(rest.scaleX, y.scaleX, .00001f)
        assertTrue(x.scaleX > rest.scaleX)
        assertEquals(x.scaleX, y.scaleY, .00001f)
        assertEquals(x.pull.x, -y.pull.y, .00001f)
    }

    @Test fun profile26KeepsNormalStretchAndLimitsExtremePulls() {
        val rest = 1f + 4f / 48f
        val normal = buttonDragTransform(GlassStandard.Ios26, bounds, 1f, Offset(48f, 0f), 1f)
        assertTrue(normal.scaleX - rest >= (4f / 48f) * .95f)
        for (distance in listOf(100f, 1000f, 1000000f)) {
            val frame = buttonDragTransform(GlassStandard.Ios26, bounds, 1f, Offset(distance, 0f), 1f)
            assertTrue(frame.scaleX <= rest + (4f / 48f) * 2.7f + .00001f)
            assertEquals(rest, frame.scaleY, .00001f)
        }
    }

    @Test fun profile26TranslationCannotContinueAfterDistortionReachesItsLimit() {
        val nearLimit = buttonDragTransform(GlassStandard.Ios26, bounds, 1f, Offset(1000f, 0f), 1f)
        val extreme = buttonDragTransform(GlassStandard.Ios26, bounds, 1f, Offset(1000000f, 0f), 1f)
        assertEquals(nearLimit.scaleX, extreme.scaleX, .00001f)
        assertEquals(nearLimit.pull.x, extreme.pull.x, .0001f)
        assertTrue(extreme.pull.x < 7f)
        assertTrue(extreme.pull.x > 6f)
    }

    @Test fun disablingLimitRestoresBothDisplacementAndDistortion() {
        val drag = Offset(480f, 0f)
        for (standard in GlassStandard.entries) {
            val limited = buttonDragTransform(standard, bounds, 1f, drag, 1f)
            val original = buttonDragTransform(standard, bounds, 1f, drag, 1f, limitDrag = false)
            assertEquals(1f + 4f / 48f + (4f / 48f) * 10f, original.scaleX, .00001f)
            assertEquals(48f * kotlin.math.tanh(.5f), original.pull.x, .00001f)
            assertTrue(original.pull.x > limited.pull.x)
            assertTrue(original.scaleX > limited.scaleX)
            assertEquals(original.scaleY, limited.scaleY, .00001f)
        }
    }

    @Test fun limitSwitchDoesNotChangeStationaryPress() {
        for (standard in GlassStandard.entries) {
            assertEquals(buttonDragTransform(standard, bounds, 1f, Offset.Zero, 1f),
                buttonDragTransform(standard, bounds, 1f, Offset.Zero, 1f, limitDrag = false))
        }
    }
}
