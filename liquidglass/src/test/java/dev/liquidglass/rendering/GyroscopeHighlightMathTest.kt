package dev.liquidglass.rendering

import org.junit.Assert.*
import org.junit.Test
import kotlin.math.sqrt

class GyroscopeHighlightMathTest {
    @Test fun flatDeviceKeepsUprightLight() {
        val math = GyroscopeHighlightMath()
        assertFalse(math.sample(.01f, .01f))
        val light = math.light(0, 0f)
        assertEquals(0f, light[0], .00001f)
        assertTrue(light[1] < -.99f)
    }

    @Test fun firstSampleIsImmediateThenUsesFifteenPercentFilter() {
        val math = GyroscopeHighlightMath()
        assertTrue(math.sample(1f, 0f))
        math.sample(0f, 1f)
        assertEquals(.85f, math.gravityX, .00001f)
        assertEquals(.15f, math.gravityY, .00001f)
    }

    @Test fun stationaryAndSmallDirectionChangesDoNotRepaint() {
        val math = GyroscopeHighlightMath()
        assertTrue(math.sample(1f, 0f))
        repeat(10) { assertFalse(math.sample(1f, 0f)) }
        assertFalse(math.sample(1f, .001f))
    }

    @Test fun allDisplayRotationsRemapDeviceGravity() {
        val math = GyroscopeHighlightMath()
        math.sample(1f, 0f)
        val expected = arrayOf(1f to 0f, 0f to 1f, -1f to 0f, 0f to -1f)
        expected.forEachIndexed { rotation, (x, y) ->
            val light = math.light(rotation, 0f)
            assertEquals(x, light[0], .002f)
            assertEquals(y, light[1], .002f)
            assertEquals(1f, sqrt(light.sumOf { (it * it).toDouble() }.toFloat()), .00001f)
        }
    }

    @Test fun panelAndIndicatorUseTheirOwnLightRotation() {
        val math = GyroscopeHighlightMath()
        math.sample(0f, -1f)
        val panel = math.light(0, -45f)
        assertTrue(panel[0] < -.7f && panel[1] < -.7f)
        val indicator = math.light(0, 90f)
        assertTrue(indicator[0] > .99f)
        assertEquals(0f, indicator[1], .00001f)
    }

    @Test fun resetRestoresFlatFallbackAndFirstSample() {
        val math = GyroscopeHighlightMath()
        math.sample(1f, 0f)
        math.reset()
        assertEquals(0f, math.gravityX, 0f)
        assertTrue(math.light(0, 0f)[1] < -.99f)
        math.sample(-1f, 0f)
        assertEquals(-1f, math.gravityX, 0f)
    }

    @Test fun invalidSamplesDoNotPoisonState() {
        val math = GyroscopeHighlightMath()
        assertFalse(math.sample(Float.NaN, 0f))
        assertFalse(math.sample(0f, Float.POSITIVE_INFINITY))
        math.sample(1f, 0f)
        assertEquals(1f, math.gravityX, 0f)
    }
}
