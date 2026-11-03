package dev.liquidglass

import androidx.compose.ui.geometry.Rect
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GlassCapturePlannerTest {
    private val scene = Rect(0f, 0f, 480f, 800f)

    private fun pane(
        name: String, bounds: Rect,
        order: Long, group: Any? = null,
        sigma: Float = 12f,
    ) = GlassCapturePane(
        id = name,
        bounds = bounds,
        blurSigmaPx = sigma,
        paintOrder = order,
        group = group,
    )

    @Test fun overlappingPanesCreateNextCompositingLevel() {
        val a = pane("under", Rect(80f, 100f, 180f, 200f), 1)
        val b = pane("over", Rect(120f, 140f, 240f, 240f), 2)
        val plan = planGlassCapture(scene, listOf(a, b), "background")
        assertEquals(2, plan.levels)
        assertEquals(0, plan.placements.first().level)
        assertEquals(1, plan.placements.last().level)
        assertEquals(2, plan.atlases.size)
    }

    @Test fun independentOrGroupedPanesStayAtSameLevel() {
        val a = pane("a", Rect(80f, 100f, 180f, 200f), 1, group = "dock")
        val b = pane("b", Rect(90f, 110f, 190f, 210f), 2, group = "dock")
        val c = pane("c", Rect(300f, 500f, 350f, 550f), 3)
        val plan = planGlassCapture(scene, listOf(a, b, c), "background")
        assertTrue(plan.placements.all { it.level == 0 })
    }

    @Test fun eachPaneHasUnblurredTileAndBlurredMaterial() {
        val a = pane("a", Rect(100f, 120f, 160f, 180f), 1)
        val plan = planGlassCapture(scene, listOf(a), "background")
        val kinds = plan.bands.map { it.key.kind }.toSet()
        assertTrue(GlassCaptureBandKind.Raw in kinds)
        assertTrue(GlassCaptureBandKind.Material in kinds)
        assertEquals(2, plan.bands.size)
        assertTrue(plan.bands.all {
            it.widthPx % 32 == 0 && it.heightPx % 32 == 0
        })
        assertEquals(plan.bands.sumOf { it.heightPx },
            plan.atlases.first().heightPx)
        assertEquals(plan.bands.maxOf { it.widthPx },
            plan.atlases.first().widthPx)
    }

    @Test fun unchangedBackgroundAndPlacementPreservesBandKeys() {
        val a = pane("a", Rect(100f, 120f, 160f, 180f), 1)
        val original = planGlassCapture(scene, listOf(a), 1)
        val next = planGlassCapture(scene, listOf(a), 1, original)
        assertTrue(next.bands.all { it.unchanged })
        val changed = planGlassCapture(scene, listOf(a), 2, next)
        assertTrue(changed.bands.none { it.unchanged })
    }

    @Test fun explicitNonGlassSourcesInvalidateOnlyEarlierBands() {
        val a = pane("a", Rect(100f, 120f, 160f, 180f), 4)
        val behind = GlassCaptureBehindContent(
            id = "label",
            bounds = Rect(80f, 90f, 180f, 190f),
            captureRoots = emptyList(),
            layer = null,
            revision = 1L,
            paintOrder = 1L,
        )
        val original = planGlassCapture(scene, listOf(a), 1,
            behindContent = listOf(behind))
        assertTrue(original.bands.all {
            ("label" to 1L) in it.key.underlayRevisions
        })
        val changed = planGlassCapture(scene, listOf(a), 1, original,
            behindContent = listOf(behind.copy(revision = 2L)))
        assertTrue(changed.bands.none { it.unchanged })

        val later = planGlassCapture(scene, listOf(a), 1, original,
            behindContent = listOf(behind.copy(revision = 2L, paintOrder = 9L)))
        assertFalse(later.bands.any { band ->
            band.key.underlayRevisions.any { it.first == "label" }
        })
    }

    @Test fun smallMotionHoldsCaptureAndLargeMotionInvalidates() {
        val a = pane("a", Rect(100f, 120f, 160f, 180f), 1)
        val first = planGlassCapture(scene, listOf(a), 1)
        val second = planGlassCapture(scene,
            listOf(a.copy(bounds = a.bounds.translate(3f, 0f))), 1, first)
        assertTrue(second.bands.all { it.unchanged })
        assertEquals(first.bands.first().key.slots,
            second.bands.first().key.slots)
        val third = planGlassCapture(scene,
            listOf(a.copy(bounds = a.bounds.translate(90f, 0f))), 1, second)
        assertTrue(third.bands.none { it.unchanged })
        assertNotEquals(second.bands.first().key.slots,
            third.bands.first().key.slots)
    }

    @Test fun residualFrostUsesStableQuantizedCaptureSigma() {
        val a = pane("a", Rect(100f, 120f, 160f, 180f), 1, sigma = 12f)
        val before = planGlassCapture(scene, listOf(a), 1)
        val after = planGlassCapture(scene,
            listOf(a.copy(blurSigmaPx = 14f)), 1, before)
        assertEquals(8f, before.bands.first {
            it.key.kind == GlassCaptureBandKind.Material
        }.key.sigmaPx, .001f)
        assertEquals(8f, after.bands.first {
            it.key.kind == GlassCaptureBandKind.Material
        }.key.sigmaPx, .001f)
        assertTrue(after.bands.all { it.unchanged })
    }

    @Test fun hiddenSourceCannotInfluenceUpperGlass() {
        val source = pane("source", Rect(100f, 120f, 180f, 200f), 1)
            .copy(captureAlpha = 0f)
        val top = pane("popup", Rect(110f, 125f, 240f, 300f), 2)
        val plan = planGlassCapture(scene, listOf(source, top), 1)
        assertEquals(1, plan.levels)
        assertEquals(listOf("popup"), plan.placements.map { it.pane.id })
    }

    @Test fun transitiveRefractedBackgroundInvalidatesUpperLevel() {
        val first = pane("lower", Rect(100f, 100f, 200f, 200f), 1)
            .copy(renderRevision = 10L)
        val second = pane("upper", Rect(165f, 140f, 250f, 220f), 2)
            .copy(renderRevision = 20L)
        // Intersects lower refraction sampling reach, but misses upper reach.
        val behind = GlassCaptureBehindContent(
            id = "underlay", bounds = Rect(72f, 125f, 80f, 140f),
            captureRoots = emptyList(), layer = null,
            revision = 4L, paintOrder = 0L,
        )
        val firstPlan = planGlassCapture(scene, listOf(first, second), 1,
            behindContent = listOf(behind))
        assertEquals(2, firstPlan.levels)
        val next = planGlassCapture(scene, listOf(first, second), 1,
            firstPlan, behindContent = listOf(behind.copy(revision = 5L)))
        assertTrue(next.bands.none { it.unchanged })
        assertTrue(next.bands.filter { it.key.level == 1 }.all {
            ("underlay" to 5L) in it.key.underlayRevisions
        })
    }

    @Test fun affineMatrixPreservesRotatedBasis() {
        val transform = GlassCaptureAffine.fromPoints(
            androidx.compose.ui.geometry.Offset(100f, 40f),
            androidx.compose.ui.geometry.Offset(100f, 140f),
            androidx.compose.ui.geometry.Offset(50f, 40f),
            100f, 50f,
        )
        assertTrue(transform.isValid())
        assertEquals(0f, transform.xx, .001f)
        assertEquals(1f, transform.yx, .001f)
        assertEquals(-1f, transform.xy, .001f)
        assertEquals(0f, transform.yy, .001f)
    }

    @Test fun resizeOfSceneInvalidatesMatchingPaneSlot() {
        val a = pane("a", Rect(100f, 120f, 160f, 180f), 1)
        val first = planGlassCapture(scene, listOf(a), 7)
        val second = planGlassCapture(Rect(0f, 0f, 600f, 800f),
            listOf(a), 7, first)
        assertTrue(second.bands.none { it.unchanged })
    }

    @Test fun visualChangeInLowerPaneInvalidatesUpperBand() {
        val a = pane("a", Rect(80f, 100f, 180f, 200f), 1)
            .copy(renderRevision = 3L)
        val b = pane("b", Rect(120f, 140f, 240f, 240f), 2)
        val first = planGlassCapture(scene, listOf(a, b), 7)
        val second = planGlassCapture(scene,
            listOf(a.copy(renderRevision = 4L, visualKey = "glow"), b),
            7, first)
        assertTrue(second.bands.filter { it.key.level == 0 }.all {
            it.unchanged
        })
        assertTrue(second.bands.filter { it.key.level == 1 }.none {
            it.unchanged
        })
    }

    @Test fun invalidGeometryDoesNotAllocateAnyAtlas() {
        val bad = pane("bad", Rect(40f, 30f, 20f, 10f), 1)
        val out = planGlassCapture(scene, listOf(bad), "unchanged")
        assertEquals(0, out.levels)
        assertTrue(out.bands.isEmpty())
    }
}
