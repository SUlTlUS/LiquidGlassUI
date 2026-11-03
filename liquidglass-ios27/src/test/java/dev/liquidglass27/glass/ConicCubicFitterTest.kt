package dev.liquidglass27.glass

import kotlin.math.abs
import kotlin.math.max
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ConicCubicFitterTest {
    @Test
    fun unitWeightQuadraticNeedsOneExactCubic() {
        val curve = RationalQuadratic(
            ConicPoint(0.0, 0.0),
            ConicPoint(50.0, 125.0),
            ConicPoint(150.0, 0.0),
            1.0,
        )
        val fit = fitRationalConic(curve)
        assertEquals(1, fit.size)
        for (step in 0..100) {
            val t = step / 100.0
            assertTrue(
                "quadratic exact at t=$t",
                fit[0].position(t).distanceTo(curve.position(t)) < 1e-9,
            )
        }
    }

    @Test
    fun largeOrHighlyWeightedCornersStaySubpixelAccurate() {
        val starts = listOf(
            ConicPoint(0.0, 0.0) to ConicPoint(240.0, 240.0),
            ConicPoint(0.0, 0.0) to ConicPoint(960.0, 200.0),
        )
        for ((start, end) in starts) {
            for (weight in listOf(0.45, 0.8, 1.0, 1.4, 2.4, 5.0, 8.0)) {
                val control = ConicPoint(0.0, end.y)
                val conic = RationalQuadratic(start, control, end, weight)
                val segments = fitRationalConic(conic)
                assertTrue("no run-away subdivision: weight=$weight", segments.size <= 128)
                assertEquals(0.0, segments.first().t0, 0.0)
                assertEquals(1.0, segments.last().t1, 0.0)
                for (segment in segments) {
                    assertTrue(segment.from.isFinite() && segment.to.isFinite())
                    assertTrue(segment.control1.isFinite() && segment.control2.isFinite())
                    for (step in 0..20) {
                        val fraction = step / 20.0
                        val actualT = segment.t0 + fraction * (segment.t1 - segment.t0)
                        val error = segment.position(fraction)
                            .distanceTo(conic.position(actualT))
                        assertTrue(
                            "weight=$weight t=$actualT, error=$error",
                            error <= 0.035,
                        )
                    }
                }
            }
        }
    }

    @Test
    fun allAdaptiveJoinsArePositionAndTangentContinuous() {
        val conic = RationalQuadratic(
            ConicPoint(0.0, 0.0),
            ConicPoint(15.0, 400.0),
            ConicPoint(600.0, 500.0),
            4.5,
        )
        val segments = fitRationalConic(conic)
        assertTrue(segments.size > 1)
        for (i in 1 until segments.size) {
            val prev = segments[i - 1]
            val next = segments[i]
            assertEquals(prev.t1, next.t0, 0.0)
            assertEquals(0.0, prev.to.distanceTo(next.from), 1e-12)
            val a = prev.to - prev.control2
            val b = next.control1 - next.from
            val cross = a.x * b.y - a.y * b.x
            val dot = a.x * b.x + a.y * b.y
            assertTrue("tangent faces forward at index=$i", dot > 0.0)
            assertTrue(
                "no tangent kink at index=$i",
                abs(cross) / max(1e-12, a.distanceTo(ConicPoint(0.0, 0.0)) *
                    b.distanceTo(ConicPoint(0.0, 0.0))) < 1e-9,
            )
        }
    }

    @Test
    fun rejectsInvalidConicParameters() {
        for (weight in listOf(0.0, -0.2, Double.NaN, Double.POSITIVE_INFINITY)) {
            try {
                RationalQuadratic(
                    ConicPoint(0.0, 0.0),
                    ConicPoint(25.0, 40.0),
                    ConicPoint(50.0, 0.0),
                    weight,
                )
                throw AssertionError("expected rejection: $weight")
            } catch (_: IllegalArgumentException) {
                // invalid weights must not reach Android's native Path API
            }
        }
    }
}
