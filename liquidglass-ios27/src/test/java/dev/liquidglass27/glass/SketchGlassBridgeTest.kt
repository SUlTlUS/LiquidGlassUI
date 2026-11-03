package dev.liquidglass27.glass

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Regression for the startup crash when Sketch's lens() receives a generic
 * Shape and throws "Only RoundedRectangularShape or CornerBasedShape".
 */
class SketchGlassBridgeTest {
    @Test
    fun sketchGooShapeIsAcceptedBySketchLens() {
        val shape = SketchGlassShape(null, CornerSize(24.dp))
        assertTrue(shape is CornerBasedShape)
    }

    @Test
    fun internalSketchClipIsWholeBoundsEvenWhenOpticalCornerIsRounded() {
        val shape = SketchGlassShape(null, CornerSize(24.dp))
        val outline = shape.createOutline(
            Size(240f, 120f), LayoutDirection.Ltr, Density(2f),
        )
        assertTrue(outline is Outline.Rectangle)
        assertEquals(Rect(0f, 0f, 240f, 120f), (outline as Outline.Rectangle).rect)
        // The animated Goo/RSuperellipse is clipped separately at draw time.
        // A fixed inner layout clip must not freeze mid-animation.
    }

    @Test
    fun submenuOpticsUseActualOutlineRadiusNotDefaultCapsule() {
        val bounds = Rect(20f, 30f, 180f, 210f)
        val submenu = GlassOutlineRect(bounds, radius = 42f)
        assertEquals(42f, sketchOpticalCornerPx(submenu, null, 1000f, 2f), .0001f)
        val morph = GlassMorphShape(bounds, 35f, bounds, 8f)
        assertEquals(35f, sketchOpticalCornerPx(null, morph, 1000f, 2f), .0001f)
        assertEquals(48f, sketchOpticalCornerPx(null, null, 24f, 2f), .0001f)
    }

    @Test
    fun cornerCopyRemainsSupportedAndPreservesAllFourRadii() {
        val shape = SketchGlassShape(null, CornerSize(20.dp))
        val copy = shape.copy(
            CornerSize(10.dp),
            CornerSize(12.dp),
            CornerSize(14.dp),
            CornerSize(16.dp),
        )
        assertTrue(copy is SketchGlassShape)
        assertEquals(CornerSize(10.dp), copy.topStart)
        assertEquals(CornerSize(12.dp), copy.topEnd)
        assertEquals(CornerSize(14.dp), copy.bottomEnd)
        assertEquals(CornerSize(16.dp), copy.bottomStart)
    }
}
