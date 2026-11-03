package dev.liquidglass27.glass

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import org.junit.Assert.assertEquals
import org.junit.Test

/** The three placement modes and avoid priority from popup.dart. */
class MenuPopupPlacementParityTest {
    private val source = Rect(400f, 200f, 500f, 260f)

    private fun position(
        anchor: Rect = source,
        placement: GlassMenuPlacement = GlassMenuPlacement.OverAnchorEnd,
        avoid: Rect? = null,
    ) = menuPopupPosition(
        anchor = anchor,
        cardWidth = 240f,
        cardHeight = 280f,
        screenWidth = 900f,
        screenHeight = 900f,
        density = 1f,
        placement = placement,
        avoid = avoid,
    )

    @Test
    fun overAnchorEndOverlapsSourceByEight() {
        assertEquals(Offset(260f, 192f), position())
    }

    @Test
    fun overAnchorStartUsesLeftEdgeAndPreservesTopOverlap() {
        assertEquals(Offset(400f, 192f),
            position(placement = GlassMenuPlacement.OverAnchorStart))
    }

    @Test
    fun belowPointPrefersEightPixelGapAboveAvoidThenBelow() {
        assertEquals(Offset(400f, 212f),
            position(placement = GlassMenuPlacement.BelowPoint,
                avoid = Rect(380f, 500f, 540f, 540f)))
        assertEquals(Offset(400f, 548f),
            position(placement = GlassMenuPlacement.BelowPoint,
                avoid = Rect(380f, 30f, 540f, 540f)))
    }

    @Test
    fun belowPointFallsBackToSourceOrFlipsAboveWhenNecessary() {
        assertEquals(Offset(400f, 260f),
            position(placement = GlassMenuPlacement.BelowPoint))
        val bottomSource = Rect(400f, 800f, 500f, 870f)
        assertEquals(Offset(400f, 520f),
            position(anchor = bottomSource,
                placement = GlassMenuPlacement.BelowPoint))
    }

    @Test
    fun topEdgeSourceMayKeepItsEightPixelOverlap() {
        assertEquals(Offset(260f, 0f),
            position(anchor = Rect(400f, 5f, 500f, 65f)))
    }
}
