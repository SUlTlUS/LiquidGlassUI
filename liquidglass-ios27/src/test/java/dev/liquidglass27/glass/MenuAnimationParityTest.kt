package dev.liquidglass27.glass

import androidx.compose.ui.geometry.Rect
import java.io.File
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/** Source/geometry checks for the specifically requested FlClash items 7-14,18,19. */
class MenuAnimationParityTest {
    private fun code(relative: String): String {
        val local = File(relative)
        return (if (local.isFile) local else File("liquidglass-ios27", relative)).readText()
    }
    private val menu by lazy {
        code("src/main/java/dev/liquidglass27/glass/GlassMenu.kt")
    }
    private val card by lazy {
        code("src/main/java/dev/liquidglass27/glass/GlassSubmenu.kt")
    }

    @Test fun oneUnboundedSpringOwnsEveryOpenMenuDepth() {
        assertTrue(menu.contains("val submenuMotion = remember { Animatable(1f) }"))
        assertFalse(menu.contains("nestedMotions"))
        assertTrue(menu.contains("submenuMotion.snapTo(0f)"))
        assertTrue(menu.contains("motion.snapTo(1f)"))
        assertTrue(menu.contains("FlClashMenuSprings.unfold()"))
        assertTrue(menu.contains("FlClashMenuSprings.fold()"))
    }

    @Test fun recedingDistanceMatchesFlutterForAllAncestorLevels() {
        for (depth in 1..6) {
            for (topProgress in listOf(0f, .15f, .6f, 1f, 1.08f)) {
                assertEquals(0f, menuRecedingDistance(depth, depth, topProgress), 0f)
                for (cardDepth in 0 until depth) {
                    val expected = depth - cardDepth - 1f + topProgress
                    assertEquals(expected,
                        menuRecedingDistance(cardDepth, depth, topProgress), .000001f)
                    assertEquals(1f, menuLevelProgress(cardDepth, depth, topProgress), 0f)
                }
                assertEquals(topProgress,
                    menuLevelProgress(depth, depth, topProgress), 0f)
            }
        }
    }

    @Test fun foldingTopLevelKeepsAncestorDistancesContinuousAcrossPop() {
        // Before: top level N has just folded to _unfold=0. After:
        // path.pop makes N-1 the top and FlClash resets _unfold=1.
        // Every ancestor and its scale/shadow MUST see the same distance.
        for (oldDepth in 2..6) {
            for (ancestor in 0 until oldDepth - 1) {
                val before = menuRecedingDistance(ancestor, oldDepth, 0f)
                val after = menuRecedingDistance(ancestor, oldDepth - 1, 1f)
                assertEquals(before, after, 0f)
                assertEquals(menuRecedingScale(before), menuRecedingScale(after), 0f)
                assertEquals(menuRecedingShadow(before), menuRecedingShadow(after), 0f)
            }
        }
        assertTrue(menu.contains("parentRestoring = true"))
        assertTrue(menu.contains("val topUnfold = menuVisibleTopUnfold("))
        assertEquals(1f, menuVisibleTopUnfold(
            springValue = 0f, topDepth = 2, firstIndex = 0,
            animatedFirstIndex = 0, firstOpening = true,
            pendingNestedDepth = -1, restoringParent = true,
        ), 0f)
    }

    @Test fun pivotUsesTopOfOpeningItemAndHitTestUsesTheSamePoint() {
        assertTrue(menu.contains("selectedRow.top else card.top"))
        assertTrue(menu.contains("Offset(firstGeometry.target.right, selectedRow.top)"))
        assertTrue(menu.contains("Offset(geometry.target.right, row.top)"))
        assertTrue(card.contains("((origin.top - envelope.top) / envelope.height)"))
        assertFalse(card.contains("((origin.center.y - envelope.top) / envelope.height)"))
    }

    @Test fun ancestorShadowAndDimHaveOriginalDistanceDecay() {
        assertEquals(1f, menuRecedingShadow(0f), 0f)
        assertEquals(2f / 3f, menuRecedingShadow(1f), .00001f)
        assertEquals(1f / 3f, menuRecedingShadow(2f), .00001f)
        assertEquals(1f / 6f, menuRecedingShadow(3f), .00001f)
        assertEquals(1f / 6f, menuRecedingShadow(5f), .00001f)
        assertEquals(.95f, menuRecedingScale(1f), 0f)
        assertEquals(.4f, menuRecedingDim(2f), 0f)
        assertEquals(.4f, menuRecedingDim(8f), 0f)
        val shadow = code("src/main/java/dev/liquidglass27/glass/MenuCardShadow.kt")
        assertTrue(shadow.contains("29f * .57735f * density"))
        assertTrue(shadow.contains("8f * density"))
        assertTrue(card.contains("strength = menuCardShadowStrength("))
        assertTrue(menu.contains("strength = menuCardShadowStrength("))
        // FlClash's top-card shadow is progress-based, so it fades rather
        // than remaining full strength until the child composable disappears.
        assertEquals(.3f, menuCardShadowStrength(.3f, false, 0f), 0f)
        assertEquals(2f / 3f, menuCardShadowStrength(.2f, true, 1f), .00001f)
    }

    @Test fun headerIsNotIndependentlyFadedAndCoveredRowDoesNotGhost() {
        assertFalse(card.contains("headerIn ="))
        assertFalse(card.contains("alpha = headerIn"))
        assertTrue(card.contains("val itemsIn = ((settled - .05f) / .35f)"))
        assertTrue(menu.contains("index == submenuIndex -> 0f"))
    }

    @Test fun geometryAllowsRealOvershootOutsideZeroAndOne() {
        val target = Rect(80f, 110f, 380f, 450f)
        val row = Rect(94f, 210f, 365f, 263f)
        fun at(t: Float) = glassSubmenuInterpolatedShape(row, target, t, 2f)
        val initial = at(0f)
        val full = at(1f)
        val over = at(1.2f)
        val back = at(-.15f)
        assertTrue(over.panel.width != full.panel.width || over.panel.height != full.panel.height)
        assertTrue(back.panel.width != initial.panel.width || back.panel.height != initial.panel.height)
        assertTrue(over.radius > full.radius)
        assertTrue(back.radius < initial.radius)
        assertFalse(card.contains("progress.coerceIn(-.08f, 1.12f)"))
    }

    @Test fun noSourcePopupUses200msEaseOutShrinkInsteadOf500msDroplet() {
        assertEquals(0f, menuNoSourceDismissFraction(0f), 0f)
        assertEquals(.75f, menuNoSourceDismissFraction(.1f), .00001f)
        assertEquals(1f, menuNoSourceDismissFraction(.2f), 0f)
        assertEquals(1f, menuNoSourceDismissFraction(.5f), 0f)
        assertTrue(menu.contains("returnIntoSource: Boolean = true"))
        assertTrue(menu.contains("FlClashPopupRouteMotion.reverseTravelMillis("))
        assertTrue(menu.contains("scaleX = 1f - .2f * vanish"))
        assertTrue(menu.contains("alpha = 1f - vanish"))
    }

    @Test fun returningSubmenuTextScalesUniformlyAndCannotStretch() {
        assertTrue(card.contains("val uniform = min("))
        assertTrue(card.contains("framePlatter.width / panel.width"))
        assertTrue(card.contains("framePlatter.height / panel.height"))
        assertTrue(card.contains("scaleX = uniform"))
        assertTrue(card.contains("scaleY = uniform"))
    }

    @Test fun cancelingReturnPreservesNestedHierarchyUntilDismissActuallyFinishes() {
        // Flutter route status reversal never rewrites _path. Keep the
        // Compose menu hierarchy alive during reverse->forward as well.
        val reverseSection = menu.substringAfter("} else if (routeState.mounted) {")
            .substringBefore("// LaunchedEffect is cancelled")
        assertFalse(reverseSection.contains("nestedPath = emptyList()"))
        val completedSection = menu.substringAfter(
            "// LaunchedEffect is cancelled on a new expanded=true",
        )
        assertTrue(completedSection.contains("nestedPath = emptyList()"))
        assertTrue(menu.contains("if (!routeState.mounted) {"))
    }

    @Test fun plainPopupRestoresUnscaledUndimmedDrawingAfterReverse() {
        assertTrue(menu.contains("if (routeState.returning && !returnIntoSource)"))
        assertTrue(menu.contains("transformOrigin = TransformOrigin.Center"))
        assertTrue(menu.contains(
            "scaleX = 1f\n                scaleY = 1f\n                alpha = 1f",
        ))
    }

    @Test fun interruptedReturnUsesOneNormalizedRouteControllerNotTwoOscillators() {
        assertTrue(menu.contains("val clock = remember { Animatable(0f) }"))
        assertTrue(menu.contains("FlClashPopupRouteMotion.openingRemainingMillis(clock.value)"))
        assertTrue(menu.contains("FlClashPopupRouteMotion.reverseTravelMillis("))
        assertTrue(menu.contains("FlClashPopupRouteMotion.dismissalTimeSeconds("))
        assertFalse(menu.contains("reopeningMix"))
        assertFalse(menu.contains("reopeningFrom"))
        assertFalse(menu.contains("reopeningNested"))
        assertFalse(menu.contains("menuReopeningFrame("))
        assertFalse(menu.contains("menuReopeningHandoff("))
        // A reverse->forward status change MUST NOT snap the route position.
        assertTrue(menu.contains("if (!routeState.mounted) {"))
        val elapsed = FlClashPopupRouteMotion.openingTimeSeconds(.75f)
        assertEquals(.3375f, elapsed, .000001f)
        assertEquals(113, FlClashPopupRouteMotion.openingRemainingMillis(.75f))
    }
}
