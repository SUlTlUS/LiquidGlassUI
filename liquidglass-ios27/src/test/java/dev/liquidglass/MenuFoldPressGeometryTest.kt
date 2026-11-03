package dev.liquidglass

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import java.io.File
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Regression: holding a child header during fold must not produce a second
 * scale-up on the exact frame its card disappears from the root Stack.
 */
class MenuFoldPressGeometryTest {
    private val parent = Rect(240f, 100f, 520f, 360f)
    private val child = Rect(145f, 130f, 520f, 670f)
    private val full = menuFlutterStackLayoutBounds(parent, listOf(child))
    private val lift = 1f
    private val noPull = Offset.Zero

    @Test fun reproducesThePreviousExtraScaleAtEndOfFold() {
        val fullScale = menuElasticTransform(full, lift, noPull, 16f).scaleX
        val afterRemoval = menuElasticTransform(parent, lift, noPull, 16f).scaleX
        assertTrue("old frame jump must be measurable", afterRemoval > fullScale + .01f)
        assertNotEquals(full.center, parent.center)
    }

    @Test fun heldTitleFoldKeepsScaleAndPivotContinuousAtRemoval() {
        val memory = MenuFoldPressGeometry()
        assertTrue(memory.pinForFold(full, pressed = true, lift = 1f, pull = noPull))
        val justBefore = menuElasticTransform(memory.panel(full), lift, noPull, 16f)
        val justAfter = menuElasticTransform(memory.panel(parent), lift, noPull, 16f)
        assertEquals(justBefore.center, justAfter.center)
        assertEquals(justBefore.scaleX, justAfter.scaleX, 0f)
        assertEquals(justBefore.scaleY, justAfter.scaleY, 0f)
        assertEquals(justBefore.pull, justAfter.pull)
    }

    @Test fun quickTapFoldAlsoKeepsBasisUntilPressReleaseFinishes() {
        val memory = MenuFoldPressGeometry()
        assertTrue(memory.pinForFold(full, pressed = false, lift = .78f, pull = noPull))
        assertFalse(memory.unpinWhenResting(false, .24f, noPull))
        assertEquals(full, memory.panel(parent))
        assertTrue(memory.unpinWhenResting(false, 0f, noPull))
        assertEquals(parent, memory.panel(parent))
        // With zero lift/pull the change of reference basis is not painted.
        val before = menuElasticTransform(full, 0f, noPull, 16f)
        val after = menuElasticTransform(parent, 0f, noPull, 16f)
        assertEquals(before.scaleX, after.scaleX, 0f)
        assertEquals(before.scaleY, after.scaleY, 0f)
        assertEquals(before.pull, after.pull)
    }

    @Test fun dragSpringMustAlsoSettleBeforeUnlock() {
        val memory = MenuFoldPressGeometry()
        val pulled = Offset(5f, -9f)
        assertTrue(memory.pinForFold(full, pressed = true, lift = .98f, pull = pulled))
        assertFalse(memory.unpinWhenResting(true, 0f, Offset.Zero))
        assertFalse(memory.unpinWhenResting(false, 0f, Offset(0f, -.4f)))
        assertFalse(memory.unpinWhenResting(false, .015f, Offset.Zero))
        assertEquals(full, memory.panel(parent))
        assertTrue(memory.unpinWhenResting(false, .0003f, Offset(.0002f, -.0002f)))
        assertEquals(parent, memory.panel(parent))
    }

    @Test fun singleFoldLocksOnceAcrossHierarchyRebuilds() {
        val memory = MenuFoldPressGeometry()
        assertTrue(memory.pinForFold(full, true, .92f, noPull))
        val newChild = Rect(120f, 90f, 540f, 700f)
        val newStack = menuFlutterStackLayoutBounds(parent, listOf(newChild))
        assertFalse(memory.pinForFold(newStack, true, 1f, noPull))
        assertEquals(full, memory.panel(newStack))
        assertTrue(memory.unpinWhenResting(false, 0f, noPull))
        assertEquals(newStack, memory.panel(newStack))
    }

    @Test fun noActiveDeformationNeedsNoLockAndInvalidPanelsAreIgnored() {
        val memory = MenuFoldPressGeometry()
        assertFalse(memory.pinForFold(full, pressed = false, lift = 0f, pull = noPull))
        assertFalse(memory.pinForFold(Rect.Zero, pressed = true, lift = 1f, pull = noPull))
        assertFalse(memory.pinForFold(
            Rect(Float.NaN, 1f, 200f, 300f),
            pressed = true, lift = 1f, pull = noPull,
        ))
        assertEquals(parent, memory.panel(parent))
        assertEquals(null, memory.pinned)
    }

    @Test fun lockedTransformPreservesHitTestInverseOnBothSidesOfRemoval() {
        val memory = MenuFoldPressGeometry()
        val pull = Offset(8f, -12f)
        assertTrue(memory.pinForFold(full, true, .9f, pull))
        val currentPoint = Offset(370f, 220f)
        val matrices = listOf(
            menuElasticTransform(memory.panel(full), .9f, pull, 16f),
            menuElasticTransform(memory.panel(parent), .9f, pull, 16f),
        )
        assertEquals(matrices[0], matrices[1])
        for (matrix in matrices) {
            val rendered = Offset(
                matrix.center.x + (currentPoint.x - matrix.center.x) *
                    matrix.scaleX + matrix.pull.x,
                matrix.center.y + (currentPoint.y - matrix.center.y) *
                    matrix.scaleY + matrix.pull.y,
            )
            assertEquals(currentPoint.x, matrix.inverse(rendered).x, .0001f)
            assertEquals(currentPoint.y, matrix.inverse(rendered).y, .0001f)
        }
    }

    @Test fun firstAndNestedFoldCaptureBeforeRemovingChild() {
        val source = File(
            "src/main/java/dev/liquidglass/GlassMenu.kt",
        ).readText()
        val nested = source.substringAfter("fun popNested(")
            .substringBefore("LaunchedEffect(submenuOpen, submenuIndex)")
        val first = source.substringAfter("LaunchedEffect(submenuOpen, submenuIndex)")
            .substringBefore("BackHandler(enabled")
        assertTrue(nested.contains("menuFoldPressGeometry.pinForFold("))
        assertTrue(first.contains("menuFoldPressGeometry.pinForFold("))
        assertTrue(first.indexOf("menuFoldPressGeometry.pinForFold(") <
            first.indexOf("submenuMotion.animateTo(\n                0f"))
        assertTrue(source.contains("fun menuPressPanel(): Rect = menuFoldPressGeometry.panel("))
        assertTrue(source.contains("SideEffect { menuBouncePanel = menuPressPanel() }"))
        assertTrue(source.contains("menuFoldPressGeometry.unpinWhenResting("))
    }
}
