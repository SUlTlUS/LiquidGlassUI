package dev.liquidglass

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class GlassGeometryTest {
    @Test fun smoothMinReturnsCloserShapeWithoutBlend() {
        assertEquals(-2f, GlassMotion.smoothMinimum(-2f, 4f, 0f), .0001f)
        assertEquals(-2f, GlassMotion.smoothMinimum(4f, -2f, 0f), .0001f)
    }

    @Test fun smoothMinBuildsNeckWhenPillsApproach() {
        assertEquals(-2f, GlassMotion.smoothMinimum(0f, 0f, 8f), .001f)
        assertEquals(0f, GlassMotion.smoothMinimum(0f, 0f, 0f), .001f)
    }

    @Test fun pressGrowthIsCapped() {
        assertEquals(.25f, GlassMotion.pressGrowth(20f, 16f), .0001f)
        assertEquals(.125f, GlassMotion.pressGrowth(128f, 16f), .0001f)
    }

    @Test fun rubberBandIsSymmetricAndBounded() {
        assertEquals(0f, GlassMotion.rubberBand(0f, 20f), .0001f)
        assertEquals(
            -GlassMotion.rubberBand(50f, 20f),
            GlassMotion.rubberBand(-50f, 20f),
            .0001f,
        )
        assertTrue(GlassMotion.rubberBand(1000f, 20f) < 20.001f)
    }

    @Test fun gooOnlyMergesObjectsOnSameRow() {
        val a = GlassPill(40f, 20f, 25f, 20f)
        assertTrue(GlassMotion.inSameRow(a, GlassPill(95f, 24f, 25f, 20f)))
        assertFalse(GlassMotion.inSameRow(a, GlassPill(95f, 60f, 25f, 20f)))
    }
}
