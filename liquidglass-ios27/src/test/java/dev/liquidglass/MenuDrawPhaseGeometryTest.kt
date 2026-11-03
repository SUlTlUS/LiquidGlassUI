package dev.liquidglass

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import org.junit.Assert.assertEquals
import org.junit.Test

class MenuDrawPhaseGeometryTest {
    @Test fun samplingWindowRetainsTheOriginalPaddingThroughoutBothDirections() {
        for (density in listOf(1f, 2.75f, 3.5f)) {
            val viewport = Size(400f * density, 900f * density)
            val anchor = Rect(260f * density, 140f * density, 314f * density, 194f * density)
            val card = Rect(86f * density, 132f * density, 366f * density, 440f * density)
            val settled = menuOpeningFrame(FlClashPopupRouteMotion.openingTimeSeconds(1f), anchor, card, density)
            for (i in 0..100) {
                val time = i / 100f
                val opening = menuOpeningFrame(FlClashPopupRouteMotion.openingTimeSeconds(time), anchor, card, density)
                val closing = menuReturningFrame(time * .5f, settled, anchor, density)
                for (frame in listOf(opening, closing)) {
                    val p = frame.platter
                    val d = frame.droplet
                    val padding = 36f * density
                    assertEquals(Rect(
                        (minOf(p.left, d.left) - padding).coerceAtLeast(0f),
                        (minOf(p.top, d.top) - padding).coerceAtLeast(0f),
                        (maxOf(p.right, d.right) + padding).coerceAtMost(viewport.width),
                        (maxOf(p.bottom, d.bottom) + padding).coerceAtMost(viewport.height),
                    ), menuRootSampleBounds(frame, viewport, density))
                }
            }
        }
    }

    @Test fun interruptedReturnStartsAtTheCurrentCornerAndEndsCircular() {
        for (settled in listOf(0f, .05f, .1f, .2f, .5f, 1f)) {
            assertEquals(menuRootCornerPower(settled, false, 0f),
                menuRootCornerPower(settled, true, 0f), 0f)
            assertEquals(2f, menuRootCornerPower(settled, true, .5f), 0f)
        }
        assertEquals(2f, menuRootCornerPower(0f, false, 0f), 0f)
        assertEquals(3.5f, menuRootCornerPower(1f, false, 0f), 0f)
    }
}
