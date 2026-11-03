package dev.liquidglass

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GlassMenuPlacementTest {
    @Test
    fun centeredOrOverlappingAnchorsStayInsideAvailableWindow() {
        for (screen in listOf(20f, 36f, 64f, 180f, 480f, 1080f)) {
            for (extent in listOf(1f, 10f, 20f, 100f, 280f, 450f)) {
                if (extent > screen) continue
                for (desired in listOf(-1000f, -16f, 0f, 15f, 128f, 900f)) {
                    val coordinate = clampMenuPlacement(desired, extent, screen, 32f)
                    assertTrue("window=$screen width=$extent x=$coordinate", coordinate >= 0f)
                    assertTrue(
                        "window=$screen width=$extent x=$coordinate",
                        coordinate + extent <= screen + .0001f,
                    )
                }
            }
        }
    }

    @Test
    fun regularInsetsAndExistingPositionArePreserved() {
        assertEquals(32f, clampMenuPlacement(12f, 280f, 1000f, 32f), 0f)
        assertEquals(400f, clampMenuPlacement(400f, 280f, 1000f, 32f), 0f)
        assertEquals(688f, clampMenuPlacement(980f, 280f, 1000f, 32f), 0f)
    }

    @Test
    fun narrowWindowsReduceInsetInsteadOfThrowing() {
        assertEquals(9.5f, clampMenuPlacement(400f, 1f, 20f, 32f), 0f)
        assertEquals(0f, clampMenuPlacement(400f, 24f, 20f, 32f), 0f)
        assertEquals(0f, clampMenuPlacement(Float.NaN, 24f, 200f, 32f), 0f)
    }
}
