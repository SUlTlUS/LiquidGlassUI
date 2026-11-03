package dev.liquidglass

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

/**
 * Regression for the one-frame full-size second/third-level flash.
 *
 * The old top spring was already at 1 when Compose published a new
 * submenuIndex/nestedPath. LaunchedEffect or menuScope.launch called
 * Animatable.snapTo(0) AFTER the newly composed card had been drawn.
 * FlClash _push resets _unfold to zero BEFORE setState adds the new path.
 */
class MenuOpenFirstFrameTest {
    @Test
    fun newFirstOwnerMustRenderAtZeroEvenIfOldSpringIsAlreadyOne() {
        assertEquals(0f, menuVisibleTopUnfold(
            springValue = 1f,
            topDepth = 1,
            firstIndex = 2,
            animatedFirstIndex = -1,
            firstOpening = true,
            pendingNestedDepth = -1,
            restoringParent = false,
        ), 0f)
        assertEquals(0f, menuVisibleTopUnfold(
            springValue = 1f,
            topDepth = 1,
            firstIndex = 3,
            animatedFirstIndex = 2,
            firstOpening = true,
            pendingNestedDepth = -1,
            restoringParent = false,
        ), 0f)
        assertEquals(0f, menuVisibleTopUnfold(
            springValue = 0f,
            topDepth = 1,
            firstIndex = 2,
            animatedFirstIndex = 2,
            firstOpening = true,
            pendingNestedDepth = -1,
            restoringParent = false,
        ), 0f)
    }

    @Test
    fun settledFirstOwnerAndPartialReverseKeepTheRealSpringPosition() {
        for (p in listOf(0f, .08f, .35f, .9f, 1f, 1.13f)) {
            assertEquals(p, menuVisibleTopUnfold(
                springValue = p,
                topDepth = 1,
                firstIndex = 1,
                animatedFirstIndex = 1,
                firstOpening = true,
                pendingNestedDepth = -1,
                restoringParent = false,
            ), 0f)
            // During fold, owner mismatch must not force the card to zero.
            assertEquals(p, menuVisibleTopUnfold(
                springValue = p,
                topDepth = 1,
                firstIndex = 1,
                animatedFirstIndex = -1,
                firstOpening = false,
                pendingNestedDepth = -1,
                restoringParent = false,
            ), 0f)
        }
    }

    @Test
    fun newlyPushedDeepLevelNeverInheritsOneFromItsParent() {
        for (depth in 2..7) {
            assertEquals("new depth $depth", 0f, menuVisibleTopUnfold(
                springValue = 1f,
                topDepth = depth,
                firstIndex = 0,
                animatedFirstIndex = 0,
                firstOpening = true,
                pendingNestedDepth = depth,
                restoringParent = false,
            ), 0f)
            // Once Animatable.snapTo(0f) has actually finished, remove the
            // pending guard and allow the physical spring to animate.
            for (p in listOf(0f, .06f, .42f, 1f, 1.14f)) {
                assertEquals(p, menuVisibleTopUnfold(
                    springValue = p,
                    topDepth = depth,
                    firstIndex = 0,
                    animatedFirstIndex = 0,
                    firstOpening = true,
                    pendingNestedDepth = -1,
                    restoringParent = false,
                ), 0f)
            }
        }
    }

    @Test
    fun ancestorDistancesStartAtTheirPrePushRestPose() {
        for (depth in 2..7) {
            for (ancestor in 0 until depth - 1) {
                val before = menuRecedingDistance(ancestor, depth - 1, 1f)
                val after = menuRecedingDistance(ancestor, depth, 0f)
                assertEquals("depth=$depth ancestor=$ancestor", before, after, 0f)
            }
        }
    }

    @Test
    fun sourceMarksPendingBeforePublishingPathAndClearsOnlyAfterSnap() {
        val source = File("src/main/java/dev/liquidglass/GlassMenu.kt")
            .readText()
        val push = source.substringAfter("fun pushNested(")
            .substringBefore("fun popNested(")
        val pending = push.indexOf("pendingNestedDepth = nestedPath.size + 2")
        val path = push.indexOf("nestedPath = nestedPath + index")
        val snap = push.indexOf("submenuMotion.snapTo(0f)")
        val released = push.indexOf("pendingNestedDepth = -1")
        assertTrue(pending >= 0 && path > pending && snap > path && released > snap)

        val first = source.substringAfter("LaunchedEffect(submenuOpen, submenuIndex)")
            .substringBefore("BackHandler(")
        assertTrue(first.indexOf("submenuMotion.snapTo(0f)") <
            first.indexOf("firstOwnerForMotion = submenuIndex"))

        assertTrue(source.contains("val topUnfold = menuVisibleTopUnfold("))
    }
}
