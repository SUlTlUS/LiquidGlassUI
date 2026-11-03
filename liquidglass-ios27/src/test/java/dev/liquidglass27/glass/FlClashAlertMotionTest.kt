package dev.liquidglass27.glass

import kotlin.math.PI
import kotlin.math.exp
import org.junit.Assert.*
import org.junit.Test

class FlClashAlertMotionTest {
    @Test fun entryShrinksFrom120PercentToRest() {
        assertEquals(1.2f, alertEntryScale(0f), 0f)
        assertEquals(1f, alertEntryScale(1f), .000001f)
        assertTrue(alertEntryScale(.5f) > 1f)
    }

    @Test fun composeSpringMatchesOriginalCriticalCurveAndResidual() {
        val omega = 2 * PI / .275
        fun original(t: Double): Double = 1 - (1 + omega * t) * exp(-omega * t)
        for (millis in listOf(0, 50, 100, 200, 300, 404)) {
            val fraction = millis / 404f
            val expected = original(millis / 1000.0) + (1 - original(.404)) * fraction
            assertEquals("at $millis ms", expected.toFloat(),
                FlClashAlertEasing.transform(fraction), .0005f)
        }
    }

    @Test fun alertBarrierUsesOriginalThemeOpacity() {
        assertEquals(.2f, alertScrimAlpha(false), 0f)
        assertEquals(.48f, alertScrimAlpha(true), 0f)
    }
}
