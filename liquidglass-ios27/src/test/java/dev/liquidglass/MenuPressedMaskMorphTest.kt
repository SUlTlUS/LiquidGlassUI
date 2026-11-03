package dev.liquidglass

import androidx.compose.ui.geometry.Rect
import java.io.File
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * popup.dart::_buildRow paints the held ShapeDecoration INSIDE the growing
 * PopupMenuCard. The same rounded mask stretches/moves in both directions,
 * including progress < .85, while the held header itself is noninteractive.
 */
class MenuPressedMaskMorphTest {
    private val parent = Rect(180f, 130f, 500f, 510f)
    private val source = Rect(188f, 250f, 492f, 294f)
    private val target = Rect(90f, 30f, 600f, 670f)
    private val inset = 8f
    private val headerHeight = 44f
    private fun at(progress: Float): Rect = menuPressedHeaderMaskBounds(
        parent, source, target, progress, inset, headerHeight,
    )
    private fun sourceCode() = File(
        "src/main/java/dev/liquidglass/GlassSubmenu.kt",
    ).readText()

    @Test fun sourceRowAndFirstChildHeaderHaveIdenticalMaskBoundsAtZero() {
        assertEquals(source, at(0f))
        assertEquals(source.width, at(0f).width, 0f)
        assertEquals(source.height, at(0f).height, 0f)
    }

    @Test fun fullyOpenedHeaderHasTargetInsetAndSourceTextHeight() {
        assertEquals(Rect(98f, 38f, 592f, 82f), at(1f))
    }

    @Test fun pressedMaskMorphsContinuouslyWithout85PercentVisibilityJump() {
        val samples = (0..100).map { at(it / 100f) }
        assertTrue(samples[1].left != samples[0].left)
        assertTrue(samples[1].top != samples[0].top)
        assertTrue(samples[15].width > samples[0].width)
        assertTrue(samples[25].width > samples[15].width)
        assertTrue(samples[30].width >= samples[25].width)
        for (i in 1 until samples.size) {
            assertTrue(samples[i].left.isFinite() && samples[i].top.isFinite())
            assertTrue(samples[i].width > 0f && samples[i].height > 0f)
            assertTrue("position jumped at $i",
                kotlin.math.abs(samples[i].top - samples[i-1].top) < 5f)
            assertTrue("width jumped at $i",
                kotlin.math.abs(samples[i].width - samples[i-1].width) < 12f)
        }
    }

    @Test fun foldingExactlyRetracesOpeningMaskGeometry() {
        for (frame in 0..100) {
            val p = frame / 100f
            val opening = at(p)
            val closing = at(1f - (100 - frame) / 100f)
            // Two algebraically equivalent progress expressions can differ
            // by one Float ULP. The geometry itself has no direction state.
            assertEquals(opening.left, closing.left, .0001f)
            assertEquals(opening.right, closing.right, .0001f)
            assertEquals(opening.top, closing.top, .0001f)
        }
        assertEquals(source, at(0f))
    }

    @Test fun pressedTitleRemainsVisibleDuringFoldEvenThoughNotClickable() {
        assertEquals(-2, menuPressedMaskHoverForLevel(true, -2))
        assertEquals(-2, menuPressedMaskHoverForLevel(false, -2))
        assertEquals(1, menuPressedMaskHoverForLevel(true, 1))
        assertEquals(-1, menuPressedMaskHoverForLevel(false, 1))
        assertEquals(-1, menuPressedMaskHoverForLevel(false, -1))
    }

    @Test fun renderCodeUsesOneSharedMaskAndContentPathWithoutLateThreshold() {
        val source = sourceCode()
        assertTrue(source.contains("menuPressedHeaderMaskBounds("))
        assertTrue(source.contains("val contentWidth = headerMask.width + 2f * inset"))
        assertTrue(source.contains("val contentLeft = headerMask.left - inset"))
        assertTrue(source.contains("val contentTop = headerMask.top - inset"))
        assertTrue(source.contains("menuPressedMaskHoverForLevel(interactive, gestureHoveredRow)"))
        assertTrue(source.contains("if ((headerPressed || hoveredRow == -2) &&"))
        assertTrue(source.contains("if (pressed || hoveredRow == index)"))
        assertFalse(source.contains("hoveredRow == -2) && settled >= .85f"))
        assertFalse(source.contains("hoveredRow == index) && settled >= .94f"))
        assertTrue(source.contains("clipPath(contentOutline)"))
        assertTrue(source.contains("drawPath(headerMaskPath, pressedMaskColor)"))
        assertTrue(source.contains("menuPressedHeaderVisibleBounds(headerMask, panel)"))
        assertFalse(source.contains("RoundedCornerShape(10.dp)"))
    }

    @Test fun foldCompletionRebindsPressedMaskToParentInSameFrame() {
        val menu = File(
            "src/main/java/dev/liquidglass/GlassMenu.kt",
        ).readText()
        val nested = menu.substringAfter("fun popNested(").substringBefore("var latestNested")
        assertTrue(nested.contains("gestureDepth = foldingDepth - 1"))
        assertTrue(nested.contains("gestureHovered = returnRow"))
        assertTrue(nested.indexOf("gestureHovered = returnRow") <
            nested.indexOf("nestedPath = nestedPath.dropLast(1)"))
        val first = menu.substringAfter("LaunchedEffect(submenuOpen, submenuIndex)")
            .substringBefore("BackHandler(enabled")
        assertTrue(first.contains("gestureDepth = 0"))
        assertTrue(first.contains("rootHovered = submenuIndex"))
        assertTrue(first.indexOf("rootHovered = submenuIndex") <
            first.indexOf("submenuIndex = -1"))
    }

    @Test fun shrinkMaskIsClampedInsideCurrentCardBeforeShapeGeneration() {
        val panel = glassSubmenuInterpolatedShape(source, target, .1f, 1f)
        val raw = at(.1f)
        // The content grows on the 0.30 width span while the card grows
        // through the entire spring. Previously the row's 10dp corners
        // were CUT by the narrower superellipse of the card.
        assertTrue(raw.left < panel.panel.left)
        assertTrue(raw.right > panel.panel.right)
        val visible = menuPressedHeaderVisibleBounds(raw, panel.panel)
        assertEquals(panel.panel.left, visible.left, .0001f)
        assertEquals(panel.panel.right, visible.right, .0001f)
        assertTrue(visible.top >= panel.panel.top)
        assertTrue(visible.bottom <= panel.panel.bottom)
        // The card is already 16.8dp, but the held row stays at 16dp.
        assertEquals(16f, menuPressedHeaderVisibleRadius(visible, 16f), .0001f)
        assertTrue(panel.radius > menuPressedHeaderVisibleRadius(visible, 16f))
    }

    @Test fun heldMaskRadiusIsConstantAcrossEveryUnfoldAndFoldFrame() {
        // The surrounding glass card animates from radius 16 to 24dp;
        // Flutter _buildRow(ShapeDecoration) stays at _itemRadius=16dp.
        val progression = (0..120).map { it / 120f }
        for (p in progression + progression.asReversed()) {
            val card = glassSubmenuInterpolatedShape(source, target, p, 1f)
            val visible = menuPressedHeaderVisibleBounds(at(p), card.panel)
            assertTrue("valid visible mask at progress $p", visible.width > 0f)
            assertEquals("row radius changed at progress $p",
                16f, menuPressedHeaderVisibleRadius(visible, 16f), .0001f)
            assertTrue("card radius must grow independently",
                card.radius >= 16f && card.radius <= 24f)
        }
        assertEquals(16f, glassSubmenuInterpolatedShape(source, target, 0f, 1f).radius, 0f)
        assertEquals(24f, glassSubmenuInterpolatedShape(source, target, 1f, 1f).radius, 0f)
    }

    @Test fun fixedRadiusOnlyClampsWhenVisibleHeightIsPhysicallyTooShort() {
        val normal = Rect(10f, 10f, 110f, 54f)
        assertEquals(16f, menuPressedHeaderVisibleRadius(normal, 16f), 0f)
        assertEquals(20f, menuPressedHeaderVisibleRadius(normal, 20f), 0f)
        val tiny = Rect(10f, 10f, 13f, 14f)
        assertEquals(1.5f, menuPressedHeaderVisibleRadius(tiny, 16f), 0f)
        assertEquals(0f, menuPressedHeaderVisibleRadius(Rect.Zero, 16f), 0f)
        assertEquals(0f, menuPressedHeaderVisibleRadius(normal, Float.NaN), 0f)
    }

    @Test fun maskRadiusCalculationDoesNotReadCardRadiusOrAnimationProgress() {
        val painter = sourceCode()
        val shape = File("src/main/java/dev/liquidglass/MenuPressedMaskMorph.kt").readText()
        assertTrue(painter.contains(
            "menuPressedHeaderVisibleRadius(visibleHeaderMask, 16f * density)"))
        assertTrue(painter.contains("remember(visibleHeaderMask, envelope, density)"))
        assertFalse(painter.contains("menuPressedHeaderVisibleRadius(\n"))
        val radiusImplementation = shape.substringAfter(
            "internal fun menuPressedHeaderVisibleRadius(").substringBefore(
            "/** _pressed follows")
        assertFalse(radiusImplementation.contains("cardRadiusPx"))
        assertFalse(radiusImplementation.contains("progress"))
        assertFalse(radiusImplementation.contains("card: Rect"))
    }

    @Test fun clippedMaskAndItemCornerShareFlutterSuperellipseNotTenDpCircle() {
        val shape = File("src/main/java/dev/liquidglass/MenuPressedMaskMorph.kt")
            .readText()
        val root = File("src/main/java/dev/liquidglass/GlassMenu.kt")
            .readText()
        assertTrue(shape.contains("internal object MenuPressedRowSuperellipse : Shape"))
        assertTrue(shape.contains("gooPlatterOutline("))
        assertTrue(shape.contains("16f * density.density"))
        assertTrue(root.contains(".clip(MenuPressedRowSuperellipse)"))
        assertTrue(sourceCode().contains(".clip(MenuPressedRowSuperellipse)"))
        assertFalse(root.contains(".clip(RoundedCornerShape(10.dp))"))
    }

    @Test fun noStandaloneMaskAnimatorsOrExtraGlassSamplingWereAdded() {
        val source = sourceCode()
        assertFalse(source.contains("pressMaskAnimatable"))
        assertFalse(source.contains("pressMaskCoroutine"))
        assertTrue(source.contains("GlassCaptureBehind("))
        assertTrue(source.contains("menuPressedHeaderMaskBounds("))
    }
}
