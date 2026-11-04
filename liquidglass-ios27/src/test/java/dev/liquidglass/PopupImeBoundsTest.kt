package dev.liquidglass

import androidx.compose.ui.geometry.Rect
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PopupImeBoundsTest {
    private val fullWindow = Rect(0f, 0f, 400f, 900f)
    private val keyboardSafeWindow = Rect(0f, 28f, 400f, 550f)

    @Test fun fullWindowHostAvoidsKeyboardOnce() {
        assertEquals(keyboardSafeWindow, popupSafeBoundsInHost(fullWindow, keyboardSafeWindow))
    }

    @Test fun resizedHostDoesNotSubtractKeyboardAgain() {
        val resized = Rect(0f, 0f, 400f, 550f)
        assertEquals(keyboardSafeWindow, popupSafeBoundsInHost(resized, keyboardSafeWindow))
    }

    @Test fun insetHostUsesLocalCoordinatesWithoutChargingConsumedInsetsAgain() {
        val inset = Rect(12f, 28f, 388f, 550f)
        assertEquals(Rect(0f, 0f, 376f, 522f), popupSafeBoundsInHost(inset, keyboardSafeWindow))
    }

    @Test fun popupCanKeepAllControlsAboveKeyboardInBothHostModes() {
        val anchor = Rect(24f, 330f, 304f, 382f)
        for (host in listOf(fullWindow, Rect(0f, 0f, 400f, 550f))) {
            val safe = popupSafeBoundsInHost(host, keyboardSafeWindow)
            val position = menuPopupPosition(anchor, 324f, 282f, host.width, host.height,
                1f, GlassMenuPlacement.OverAnchorEnd, safeBounds = safe)
            assertEquals(252f, position.y, 0f)
            assertTrue(position.y >= safe.top)
            assertTrue(position.y + 282f <= keyboardSafeWindow.bottom - 16f)
        }
    }

    @Test fun keyboardHideRestoresFullHeightAndHostOutsideSafeAreaStaysNonNegative() {
        assertEquals(Rect(0f, 28f, 400f, 876f),
            popupSafeBoundsInHost(fullWindow, Rect(0f, 28f, 400f, 876f)))
        val covered = popupSafeBoundsInHost(Rect(0f, 600f, 400f, 900f), keyboardSafeWindow)
        assertEquals(0f, covered.height, 0f)
        assertTrue(covered.width >= 0f)
    }
}
