package dev.liquidglass

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import org.junit.Assert.*
import org.junit.Test

class GooPaintBoundsTest {
    private val viewport = IntSize(240, 96)
    private val rest = Rect(26f, 26f, 70f, 70f)

    @Test fun normalButtonRetainsTheExistingPaintArea() {
        val frame = buttonDragTransform(GlassStandard.Ios27, rest, 0f, Offset.Zero, 1f)
        assertEquals(IntRect(0, 0, 240, 96),
            gooPaintBounds(viewport, listOf(buttonPill(rest, frame, GlassStandard.Ios27)), emptyList(), 1f))
    }

    @Test fun unlimitedDragKeepsTheWholeContourInEveryDirection() {
        for (effect in GlassStandard.entries) for (drag in listOf(
            Offset(0f, 5000f), Offset(0f, -5000f), Offset(5000f, 0f), Offset(-5000f, 0f),
        )) {
            val frame = buttonDragTransform(effect, rest, 1f, drag, 1f, limitDrag = false)
            val pill = buttonPill(rest, frame, effect)
            val paint = gooPaintBounds(viewport, listOf(pill), emptyList(), 1f)
            assertTrue(paint.left <= pill.centerX - pill.halfWidth - 2f)
            assertTrue(paint.top <= pill.centerY - pill.halfHeight - 2f)
            assertTrue(paint.right >= pill.centerX + pill.halfWidth + 2f)
            assertTrue(paint.bottom >= pill.centerY + pill.halfHeight + 2f)
            assertTrue(paint.width > viewport.width || paint.height > viewport.height)
        }
    }

    @Test fun includesBothEndsAndTheBlendMarginWithoutShrinkingTheViewport() {
        val pills = listOf(GlassPill(-10f, 48f, 22f, 40f), GlassPill(250f, 96f, 30f, 50f))
        assertEquals(IntRect(-38, 0, 286, 152), gooPaintBounds(viewport, pills, listOf(16f), 1f))
    }
}
