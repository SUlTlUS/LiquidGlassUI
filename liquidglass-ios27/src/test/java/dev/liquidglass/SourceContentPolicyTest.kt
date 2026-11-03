package dev.liquidglass

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SourceContentPolicyTest {
    private fun accepted(
        width: Int = 100,
        height: Int = 80,
        key: Int = 3,
        captured: Long = 1_000_000_000L,
        expectedWidth: Float = 100f,
        expectedHeight: Float = 80f,
        expectedKey: Int = 3,
        now: Long = 1_200_000_000L,
    ): Boolean = canUseSourceContentFrame(
        width, height, key, captured,
        expectedWidth, expectedHeight, expectedKey, now,
    )

    @Test fun recentSameThemeAndWithinTwoPixelsCanUseSourceIcon() {
        assertTrue(accepted())
        assertTrue(accepted(width = 102, height = 78))
        assertTrue(accepted(now = 1_350_000_000L))
    }

    @Test fun staleOrFutureFramesAreNeverShown() {
        assertFalse(accepted(now = 1_350_000_001L))
        assertFalse(accepted(now = 900_000_000L))
        assertFalse(accepted(captured = Long.MAX_VALUE))
    }

    @Test fun themeSizeAndUnavailableDimensionsInvalidateCapture() {
        assertFalse(accepted(key = 2))
        assertFalse(accepted(width = 103))
        assertFalse(accepted(height = 77))
        assertFalse(accepted(width = 0))
        assertFalse(accepted(expectedWidth = Float.NaN))
        assertFalse(accepted(expectedHeight = 0f))
        assertFalse(accepted(expectedKey = -1))
    }
}
