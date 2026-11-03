package dev.liquidglass27.glass

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import org.junit.Assert.*
import org.junit.Test
import java.io.File

class PopupPresentationTest {
    @Test fun smallerForegroundReturnsAtItsActualSizeInsteadOfFillingButton() {
        val button = Rect(24f, 140f, 304f, 194f)
        for (foreground in listOf(Size(248f, 22f), Size(280f, 54f), Size(24f, 24f))) {
            val transform = popupSourceContentTransform(foreground, button.size, button)
            assertEquals(1f, transform.scale, 0f)
            val painted = Rect(transform.translation, foreground * transform.scale)
            assertEquals(foreground.width, painted.width, .0001f)
            assertEquals(foreground.height, painted.height, .0001f)
            assertEquals(button.center, painted.center)
        }
    }

    @Test fun foregroundScaleTracksButtonGeometryThroughoutReturn() {
        val button = Rect(24f, 140f, 304f, 194f)
        val opened = menuOpeningFrame(.45f, button, Rect(16f, 140f, 340f, 540f), 1f)
        for (time in listOf(0f, .1f, .25f, .4f, .49f, .5f)) {
            val drop = menuReturningFrame(time, opened, button, 1f).droplet
            val padded = popupSourceContentTransform(button.size, button.size, drop)
            val compact = popupSourceContentTransform(Size(248f, 22f), button.size, drop)
            assertEquals(padded.scale, compact.scale, 0f)
            if (time == .5f) assertEquals(1f, compact.scale, .00001f)
        }
    }

    @Test fun snapshotIncludesCallerPaddingBeforeCapture() {
        val popup = File("src/main/java/dev/liquidglass27/glass/GlassPopup.kt").readText()
        assertTrue(popup.contains("Box(Modifier.drawWithContent"))
        assertTrue(popup.contains("drawLayer(layer) }.then(modifier)"))
        assertTrue(popup.contains("popupSourceContentTransform(sourceSize, origin.size, frame.droplet)"))
    }
    private val anchor = Rect(24f, 140f, 304f, 194f)
    private val card = Rect(16f, 140f, 340f, 540f)

    @Test fun popupTextKeepsItsAspectRatioAndCenterThroughoutOpening() {
        for (time in listOf(0f, .02f, .05f, .10f, .20f, .35f, .6f)) {
            val frame = menuOpeningFrame(time, anchor, card, 1f)
            val transform = popupContentTransform(card.size, frame.platter)
            val painted = Rect(transform.translation, card.size * transform.scale)
            assertEquals(card.width / card.height, painted.width / painted.height, .0001f)
            assertEquals(frame.platter.center.x, painted.center.x, .0001f)
            assertEquals(frame.platter.center.y, painted.center.y, .0001f)
        }
    }

    @Test fun returningTextKeepsItsAspectRatioEvenWhenCardChangesHeight() {
        val opened = menuOpeningFrame(.6f, anchor, card, 1f)
        for (time in listOf(0f, .05f, .15f, .3f, .5f)) {
            val frame = menuReturningFrame(time, opened, anchor, 1f)
            val transform = popupContentTransform(card.size, frame.platter)
            assertEquals(frame.platter.center,
                transform.translation + Offset(card.width, card.height) * (transform.scale / 2f))
        }
    }

    @Test fun foregroundCaptureFitsWithoutStretchingOrUpsizingOneAxis() {
        val source = Size(250f, 22f)
        val drop = Rect(100f, 120f, 240f, 147f)
        val transform = popupContentTransform(source, drop, fitInside = true)
        assertEquals(.56f, transform.scale, .0001f)
        val painted = Rect(transform.translation, source * transform.scale)
        assertTrue(painted.width <= drop.width + .001f && painted.height <= drop.height + .001f)
        assertEquals(source.width / source.height, painted.width / painted.height, .0001f)
    }

    @Test fun missingMeasurementProducesFiniteInvisibleTransform() {
        val transform = popupContentTransform(Size.Zero, card)
        assertEquals(0f, transform.scale, 0f)
        assertEquals(card.center, transform.translation)
    }

    @Test fun popupUsesTheMenuSurfaceAndCornerScheduleInsteadOfASeparateRim() {
        val popup = File("src/main/java/dev/liquidglass27/glass/GlassPopup.kt").readText()
        assertTrue(popup.contains("SketchMenuRootSurface(motion, card"))
        assertTrue(popup.contains("menuRootCornerPower(frame.settled, returning, time)"))
        assertFalse(popup.contains("ContentScale.FillBounds"))
        assertFalse(popup.contains("scaleY = frame.platter.height / card.height"))
        assertEquals(2f, menuRootCornerPower(0f, false, 0f), 0f)
        assertEquals(3.5f, menuRootCornerPower(1f, false, 0f), 0f)
        // Regular control light exists before the final menu-rim stage.
        for (dark in listOf(false, true)) {
            val style = GlassStyle.interpolate(GlassStyle.regular(dark), GlassStyle.menu(dark), .1f)
            assertTrue(style.rim > 0f)
        }
    }
}
