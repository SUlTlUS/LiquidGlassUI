package dev.liquidglass

import kotlin.math.PI
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test

class FlClashMenuSpringsTest {
    @Test
    fun flutterUnfoldMatchesExactDurationAndBounceParameters() {
        val sourceStiffness = 4.0 * PI * PI / (.420 * .420)
        assertEquals(sourceStiffness, FlClashMenuSprings.stiffnessFor(420).toDouble(), .0001)
        assertEquals(.85f, FlClashMenuSprings.dampingRatioFor(.15f), .00001f)
        assertEquals(sourceStiffness.toFloat(), FlClashMenuSprings.unfold().stiffness, .0001f)
        assertEquals(.85f, FlClashMenuSprings.unfold().dampingRatio, .00001f)
    }

    @Test
    fun flutterFoldUsesCriticallyDamped340msDescription() {
        val sourceStiffness = 4.0 * PI * PI / (.340 * .340)
        assertEquals(sourceStiffness, FlClashMenuSprings.stiffnessFor(340).toDouble(), .0001)
        assertEquals(1f, FlClashMenuSprings.dampingRatioFor(0f), 0f)
        assertEquals(sourceStiffness.toFloat(), FlClashMenuSprings.fold().stiffness, .0001f)
        assertEquals(1f, FlClashMenuSprings.fold().dampingRatio, 0f)
    }

    @Test
    fun invalidDurationsAndBounceAreRejected() {
        assertThrows(IllegalArgumentException::class.java) {
            FlClashMenuSprings.stiffnessFor(0)
        }
        assertThrows(IllegalArgumentException::class.java) {
            FlClashMenuSprings.dampingRatioFor(Float.NaN)
        }
        assertThrows(IllegalArgumentException::class.java) {
            FlClashMenuSprings.dampingRatioFor(-1f)
        }
        assertTrue(FlClashMenuSprings.stiffnessFor(340) > FlClashMenuSprings.stiffnessFor(420))
    }
}
