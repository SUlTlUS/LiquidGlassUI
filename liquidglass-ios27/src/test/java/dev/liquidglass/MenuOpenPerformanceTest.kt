package dev.liquidglass

import androidx.compose.ui.geometry.Rect
import java.io.File
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Non-optical hot-path regression tests for opening a child menu.
 * Never relax the Sketch glass renderer, dynamic outline, or Flutter spring.
 */
class MenuOpenPerformanceTest {
    private fun source(path: String) = File("src/main/java/dev/liquidglass/$path")
        .readText()

    @Test
    fun fixedTargetCanBeReusedAcrossHundredsOfAnimatedFrames() {
        val origin = Rect(90f, 190f, 290f, 244f)
        val target = Rect(40f, 160f, 340f, 610f)
        val seenTargets = mutableSetOf<Rect>()
        for (step in -20..120) {
            val t = step / 100f
            val current = glassSubmenuInterpolatedShape(origin, target, t, 2f)
            assertEquals(target, current.target)
            seenTargets.add(current.target)
            assertEquals(
                origin.left + (target.left - origin.left) * t,
                current.panel.left, .0005f,
            )
            assertEquals(
                origin.bottom + (target.bottom - origin.bottom) * t,
                current.panel.bottom, .0005f,
            )
            assertTrue(current.radius.isFinite())
        }
        assertEquals(1, seenTargets.size)
    }

    @Test
    fun parentAndChildCacheStableDestinationSeparatelyFromAnimatedOutline() {
        val parent = source("GlassMenu.kt")
        val child = source("GlassSubmenu.kt")
        assertTrue(parent.contains("val firstTarget = remember("))
        assertTrue(parent.contains("val geometry = glassSubmenuInterpolatedShape("))
        assertTrue(parent.contains("val firstGeometry = glassSubmenuInterpolatedShape("))
        assertTrue(child.contains("val target = remember("))
        assertTrue(child.contains("val layout = glassSubmenuInterpolatedShape("))
        // The old snapshot was recomputing width/target for the third time
        // every animation frame. Reuse firstGeometry for the closing surface.
        assertTrue(parent.contains(
            "submenuIndex, firstGeometry, firstProgress, firstOwner,"
        ))
        assertFalse(parent.contains(
            "val top = glassSubmenuGeometry(\n                card, selectedRow"
        ))
    }

    @Test
    fun immutableContentSelectionsAndShadowSettingsDoNotRebuildEachFrame() {
        val child = source("GlassSubmenu.kt")
        val root = source("GlassMenu.kt")
        val shadow = source("MenuCardShadow.kt")
        assertTrue(child.contains("val contentActionVersion = remember(owner.subActions)"))
        assertTrue(root.contains("val rootActionVersion = remember(actions)"))
        assertTrue(shadow.contains("if (lastDensity == density && lastShadeColor == shadeColor) return"))
        assertFalse(shadow.contains("paint.clearShadowLayer()"))
    }

    @Test
    fun newlyPushedChildStillStartsAtZeroWithoutChangingOriginalSpring() {
        assertEquals(0f, menuVisibleTopUnfold(
            springValue = 1f, topDepth = 1,
            firstIndex = 1, animatedFirstIndex = -1,
            firstOpening = true, pendingNestedDepth = -1,
            restoringParent = false,
        ), 0f)
        assertEquals(0f, menuVisibleTopUnfold(
            springValue = 1f, topDepth = 2,
            firstIndex = 1, animatedFirstIndex = 1,
            firstOpening = true, pendingNestedDepth = 2,
            restoringParent = false,
        ), 0f)
    }
}
