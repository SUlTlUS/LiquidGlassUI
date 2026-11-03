package dev.liquidglass27.glass

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class FlutterRSuperellipseFitTest {
    @Test fun canonicalQuarterCircleUsesFlutterEngineBaseFit() {
        val fit = flutterOctantFit(2.0)
        assertEquals(2.0, fit.exponent, 1e-8)
        assertEquals(1 - 1.0 / 1.13276676, fit.jointFraction, 1e-8)
    }

    @Test fun intermediateFlClashCornerInterpolatesTheEngineTable() {
        val fit = flutterOctantFit(2.25)
        assertEquals((2.33888662 + 2.48660575) / 2, fit.exponent, 1e-8)
        val k = (1.28698796 + 1.36351941) / 2
        assertEquals(1 - 1.0 / k, fit.jointFraction, 1e-8)
    }

    @Test fun topOfLongPillUsesExtrapolatedFlutterOctant() {
        val fit = flutterOctantFit(8.0)
        assertEquals(6.43023796 + 1.559599389 * 3, fit.exponent, 1e-8)
        val k = 2.98020421 + .522807185 * 3
        assertEquals(1.0 - 1.0 / k, fit.jointFraction, 1e-8)
    }

    @Test fun squarePillCornerUniformsMatchCircleArc() {
        val curves = flutterUniformCornerCurves(54f, 54f, 27f)
        assertEquals(12, curves.size)
        assertEquals(2f, curves[0], .0001f)
        assertEquals(2f, curves[4], .0001f)
        assertEquals(27f, curves[3], .02f)
        assertEquals(27f, curves[7], .02f)
        assertTrue(curves.all { it.isFinite() })
    }

    @Test fun rectangularPaneUsesDifferentAlongAndAcrossOctants() {
        val curves = flutterUniformCornerCurves(200f, 54f, 27f)
        assertTrue(curves[0] > curves[4])
        assertEquals(2f, curves[4], .0001f)
        assertTrue(curves.all { it.isFinite() })
    }

    @Test fun sharpCornerAndInvalidSizesGiveEmptyUniforms() {
        assertTrue(flutterUniformCornerCurves(100f, 50f, 0f).all { it == 0f })
        assertTrue(flutterUniformCornerCurves(0f, 50f, 10f).all { it == 0f })
    }

    @Test fun fitIsMonotonicAndFiniteAtAllReferenceBoundaries() {
        var oldExponent = 0.0
        var oldJoint = 0.0
        for (step in 0..280) {
            val ratio = 2.0 + step / 20.0
            val fit = flutterOctantFit(ratio)
            assertTrue(fit.exponent >= oldExponent - 1e-8)
            assertTrue(fit.jointFraction >= oldJoint - 1e-8)
            assertTrue(fit.exponent.isFinite())
            assertTrue(fit.jointFraction in 0.0..1.0)
            oldExponent = fit.exponent
            oldJoint = fit.jointFraction
        }
    }
}
