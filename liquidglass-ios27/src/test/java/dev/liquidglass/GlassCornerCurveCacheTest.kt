package dev.liquidglass

import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertNotSame
import org.junit.Assert.assertSame
import org.junit.Test

class GlassCornerCurveCacheTest {
    @Test fun repeatedFramesReuseTheExactFit() {
        val cache = GlassCornerCurveCache()
        val fit = cache.curve(0, 240f, 182f, 91f)
        repeat(120) { assertSame(fit, cache.curve(0, 240f, 182f, 91f)) }
        assertArrayEquals(flutterUniformCornerCurves(240f, 182f, 91f), fit, 0f)
    }

    @Test fun animatingOnePillDoesNotInvalidateItsNeighbors() {
        val cache = GlassCornerCurveCache()
        val neighbor = cache.curve(2, 240f, 182f, 91f)
        repeat(120) { i ->
            cache.curve(1, 240f + i, 182f, 91f)
            assertSame(neighbor, cache.curve(2, 240f, 182f, 91f))
        }
    }

    @Test fun everyShapeDimensionInvalidatesItsSlot() {
        val cache = GlassCornerCurveCache()
        var last = cache.curve(0, 240f, 182f, 91f)
        for ((width, height, radius) in listOf(
            Triple(241f, 182f, 91f), Triple(241f, 183f, 91f), Triple(241f, 183f, 90f),
        )) {
            val next = cache.curve(0, width, height, radius)
            assertNotSame(last, next)
            assertArrayEquals(flutterUniformCornerCurves(width, height, radius), next, 0f)
            last = next
        }
    }

    @Test fun emptyPillCanBecomeVisibleAndEmptyAgain() {
        val cache = GlassCornerCurveCache()
        assertArrayEquals(FloatArray(12), cache.curve(6, 0f, 0f, 0f), 0f)
        assertArrayEquals(flutterUniformCornerCurves(54f, 54f, 27f),
            cache.curve(6, 54f, 54f, 27f), 0f)
        assertArrayEquals(FloatArray(12), cache.curve(6, 0f, 0f, 0f), 0f)
    }
}
