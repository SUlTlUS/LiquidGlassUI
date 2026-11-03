package dev.liquidglass

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import org.junit.Assert.*
import org.junit.Test

class ButtonContourRoutingTest {
    @Test fun standaloneRimUsesAnalyticGeometryBeforeTheOuterMotionTransform() {
        val size = androidx.compose.ui.geometry.Size(360f, 48f)
        for (effect in GlassStandard.entries) {
            val pill = buttonLocalHighlightPill(size, 1.25f, .8f, effect)
            val scales = glassPillScales(listOf(pill))
            assertEquals(180f, pill.centerX, 0f)
            assertEquals(24f, pill.centerY, 0f)
            // Local shader coordinates must cancel the outer layer only for
            // the redrawn contour; stretched 26 retains its resting geometry.
            assertEquals(if (effect == GlassStandard.Ios26) 1f else 1f / 1.25f, scales[0], .00001f)
            assertEquals(if (effect == GlassStandard.Ios26) 1f else 1f / .8f, scales[1], .00001f)
            val resting = buttonLocalHighlightPill(size, 1f, 1f, effect)
            assertEquals(180f, resting.restHalfWidth, 0f)
            assertEquals(24f, resting.restHalfHeight, 0f)
        }
    }

    @Test fun materialNeverSelectsTheButtonContour() {
        val bounds = Rect(0f, 0f, 48f, 48f)
        val frame = MenuElasticTransform(bounds.center, 1.8f, 1.1f, Offset(6f, 0f))
        for (material in GlassStandard.entries) for (effect in GlassStandard.entries) {
            val effects = GlassEffects(material = material, buttonDragEffect = effect)
            val pill = buttonPill(bounds, frame, effects.buttonAnimation(material))
            assertEquals(effect, pill.buttonEffect)
            assertEquals(material, effects.materialFor(material))
            assertEquals(24f, pill.restHalfWidth, 0f)
            val scales = glassPillScales(listOf(pill))
            assertEquals(if (effect == GlassStandard.Ios26) 1.8f else 1f, scales[0], .00001f)
            assertEquals(if (effect == GlassStandard.Ios26) 1.1f else 1f, scales[1], .00001f)
        }
    }

    @Test fun samePaintedBoundsCannotReuseADifferentMotionContour() {
        val slots = GooGeometrySlots<GlassPill, Any>(1)
        val first = GlassPill(24f, 24f, 36f, 24f, GlassStandard.Ios26, 24f, 24f)
        val second = first.copy(buttonEffect = GlassStandard.Ios27)
        val a = slots.get(0, first) { Any() }
        assertSame(a, slots.get(0, first) { Any() })
        assertNotSame(a, slots.get(0, second) { Any() })
    }
}
