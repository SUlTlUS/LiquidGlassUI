package dev.liquidglass.rendering

import androidx.compose.ui.geometry.Offset
import org.junit.Assert.*
import org.junit.Test

class TransformAwareBackdropTest {
    @Test fun pressedButtonSamplesTheSamePagePointUnderItsCenter() {
        // Button starts at page (100, 200), is 100x40, and expands 10% around its center.
        val scale = 1.1f
        val matrix = backdropAffineMatrix(
            Offset(-95f / scale, -198f / scale),
            Offset(1f / scale, 0f), Offset(0f, 1f / scale),
        )!!
        assertPoint(Offset(50f, 20f), matrix.map(Offset(150f, 220f)))
    }

    @Test fun commonPanelScaleCancelsForThePaintedSurfaceButNotForThePage() {
        val buttonScale = 1.1f
        val panelScale = 1.2f
        val surface = backdropAffineMatrix(Offset(-20f, -30f),
            Offset(1f / buttonScale, 0f), Offset(0f, 1f / buttonScale))!!
        val page = backdropAffineMatrix(Offset(-70f, -90f),
            Offset(1f / (buttonScale * panelScale), 0f),
            Offset(0f, 1f / (buttonScale * panelScale)))!!
        assertPoint(Offset(80f, 70f), surface.map(Offset(110f, 110f)))
        assertPoint(Offset(30f, 10f), page.map(Offset(132f, 132f)))
    }

    @Test fun supportsRotatedNonUniformSurfaces() {
        val matrix = backdropAffineMatrix(Offset(10f, 20f), Offset(0f, 2f), Offset(-3f, 0f))!!
        assertPoint(Offset(-2f, 26f), matrix.map(Offset(3f, 4f)))
    }

    @Test fun skipsDetachedOrCollapsedGeometry() {
        assertNull(backdropAffineMatrix(Offset(Float.NaN, 0f), Offset(1f, 0f), Offset(0f, 1f)))
        assertNull(backdropAffineMatrix(Offset.Zero, Offset(1f, 1f), Offset(2f, 2f)))
    }

    private fun assertPoint(expected: Offset, actual: Offset) {
        assertEquals(expected.x, actual.x, .0001f)
        assertEquals(expected.y, actual.y, .0001f)
    }
}
