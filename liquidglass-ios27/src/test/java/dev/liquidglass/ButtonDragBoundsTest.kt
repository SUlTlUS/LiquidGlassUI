package dev.liquidglass

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import kotlin.math.abs
import org.junit.Assert.*
import org.junit.Test

class ButtonDragBoundsTest {
    private val bounds = Rect(0f, 0f, 96f, 48f)
    private fun extent(transform: MenuElasticTransform, width: Float = 96f) =
        abs(transform.pull.x) + (transform.scaleX - 1f) * width / 2f

    @Test fun restingPressScaleDoesNotChange() {
        for (press in listOf(0f, .5f, 1f, 1.15f)) {
            val t = buttonDragTransform(GlassStandard.Ios27, bounds, press, Offset.Zero, 1f)
            assertEquals(1f + 4f / 48f * press, t.scaleX, .00001f)
            assertEquals(t.scaleX, t.scaleY, .00001f)
            assertEquals(Offset.Zero, t.pull)
        }
    }

    @Test fun increasingDragCannotExhaustTheReservedMargin() {
        for (distance in listOf(10f, 100f, 1000f, 10000f, 1000000f)) {
            val t = buttonDragTransform(GlassStandard.Ios27, bounds, 1f, Offset(distance, distance), 1f)
            assertTrue(extent(t) <= 16.001f)
            assertTrue(abs(t.pull.y) + (t.scaleY - 1f) * 24f <= 16.001f)
        }
    }

    @Test fun longDragResponseDecaysWithoutAHardClamp() {
        fun at(x: Float) = extent(buttonDragTransform(GlassStandard.Ios27, bounds, 1f, Offset(x, 0f), 1f))
        val near = at(20f) - at(10f)
        val far = at(210f) - at(200f)
        assertTrue(near > far)
        assertTrue(far > 0f)
    }

    @Test fun ordinaryDragRetainsMostOfTheOriginalStretch() {
        val rest = buttonDragTransform(GlassStandard.Ios27, bounds, 1f, Offset.Zero, 1f)
        val dragged = buttonDragTransform(GlassStandard.Ios27, bounds, 1f, Offset(bounds.width, 0f), 1f)
        val originalStretch = 4f / bounds.height
        assertTrue(dragged.scaleX - rest.scaleX >= originalStretch * .9f)
        assertTrue(extent(dragged) <= 16.001f)
    }

    @Test fun oppositeDirectionsUseTheSameDeformationAndMirrorPull() {
        val a = buttonDragTransform(GlassStandard.Ios27, bounds, 1f, Offset(500f, 300f), 1f)
        val b = buttonDragTransform(GlassStandard.Ios27, bounds, 1f, Offset(-500f, -300f), 1f)
        assertEquals(a.scaleX, b.scaleX, .00001f)
        assertEquals(a.scaleY, b.scaleY, .00001f)
        assertEquals(a.pull.x, -b.pull.x, .00001f)
        assertEquals(a.pull.y, -b.pull.y, .00001f)
    }

    @Test fun gooRowCanUseItsSmallerEightDpMargin() {
        val t = buttonDragTransform(GlassStandard.Ios27, bounds, 1f, Offset(5000f, 5000f), 1f, maxGrowthPx = 8f)
        assertTrue(extent(t) <= 8.001f)
        assertTrue(abs(t.pull.y) + (t.scaleY - 1f) * 24f <= 8.001f)
    }

    @Test fun resistanceIsDensityIndependent() {
        val a = buttonDragTransform(GlassStandard.Ios27, bounds, 1f, Offset(250f, 100f), 1f)
        val b = buttonDragTransform(GlassStandard.Ios27, Rect(0f, 0f, 288f, 144f), 1f, Offset(750f, 300f), 3f)
        assertEquals(a.scaleX, b.scaleX, .00001f)
        assertEquals(a.scaleY, b.scaleY, .00001f)
        assertEquals(a.pull.x * 3f, b.pull.x, .0001f)
        assertEquals(a.pull.y * 3f, b.pull.y, .0001f)
    }
}
