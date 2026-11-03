package dev.liquidglass

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import java.io.File
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/** Physical input arbitration, modeled on Flutter popup.dart recognizers. */
class MenuGestureArenaParityTest {
    private val row = MenuTouchTarget(TargetKind.Row, 0, 1)
    private val disabled = MenuTouchTarget(TargetKind.DisabledRow, 0, 2)
    private val header = MenuTouchTarget(TargetKind.Header, 1)
    private val blank = MenuTouchTarget(TargetKind.Blank, 0)
    private val back = MenuTouchTarget(TargetKind.Back)
    private val outside = MenuTouchTarget(TargetKind.Outside)
    private fun arena(start: MenuTouchTarget = row, owner: Int? = 0) =
        MenuGestureArena(start, Offset(120f, 200f), 10f, owner)

    @Test fun rowOnlyAcceptsOnUpWhileMoveIsPending() {
        val a = arena(owner = 0)
        assertEquals(MenuArenaWinner.Pending, a.winner)
        assertEquals(MenuArenaWinner.Pending, a.move(Offset(125f, 205f)))
        assertTrue(a.mayAcceptRelease())
        assertTrue(a.maySelectRow())
        assertFalse(a.exceededTapSlop)
    }

    @Test fun verticalDragPastSlopMakesScrollWinBeforeRowSelection() {
        val a = arena()
        assertEquals(MenuArenaWinner.Pending, a.move(Offset(120f, 209f)))
        assertEquals(MenuArenaWinner.Scroll, a.move(Offset(120f, 211f)))
        assertFalse(a.maySelectRow())
        assertFalse(a.mayAcceptRelease())
        assertEquals(0, a.scrollOwner)
        assertEquals(MenuArenaWinner.Scroll, a.move(Offset(120f, 180f)))
    }

    @Test fun horizontalDragAcrossRowsDoesNotBecomeVerticalScroll() {
        val a = arena()
        assertEquals(MenuArenaWinner.Pending, a.move(Offset(160f, 207f)))
        assertEquals(MenuArenaWinner.Pending, a.move(Offset(170f, 235f)))
        assertTrue(a.exceededTapSlop)
        assertTrue(a.maySelectRow())
    }

    @Test fun scrollOwnerFrozenAtDownDespiteHoveredRowChanging() {
        val a = MenuGestureArena(
            MenuTouchTarget(TargetKind.Row, 2, 0), Offset(80f, 100f),
            8f, 2,
        )
        assertEquals(MenuArenaWinner.Scroll, a.move(Offset(80f, 109f)))
        assertEquals(2, a.scrollOwner)
        assertFalse(a.mayAcceptRelease())
    }

    @Test fun nonScrollableRowMayStillSelectAfterLargeDrag() {
        val a = arena(owner = null)
        assertEquals(MenuArenaWinner.Pending, a.move(Offset(120f, 270f)))
        assertTrue(a.exceededTapSlop)
        assertTrue(a.maySelectRow())
    }

    @Test fun postDwellChildSelectionCannotBeStolenByOldParentScroll() {
        val a = arena()
        val press = FlClashMenuPress()
        press.start(row)
        press.completeDwell(MenuDwellAction.OpenChild)
        a.levelTransition()
        assertEquals(MenuArenaWinner.Pending,
            a.move(Offset(120f, 245f)))
        assertFalse(press.canRunRelease())
        assertTrue(press.move(MenuTouchTarget(TargetKind.Row, 1, 3), 1))
        assertTrue(press.canRunRelease())
        assertTrue(a.maySelectRow())
        assertEquals(MenuArenaWinner.Pending,
            a.move(Offset(120f, 290f)))
    }

    @Test fun consumedMoveOrCompetingFinalPassRejectsWholeRowGesture() {
        val a = arena()
        assertEquals(MenuArenaWinner.Rejected,
            a.move(Offset(121f, 201f), otherConsumed = true))
        assertFalse(a.maySelectRow())
        assertFalse(a.mayDismissOutside(outside))
        assertEquals(MenuArenaWinner.Rejected,
            a.move(Offset(120f, 280f)))
    }

    @Test fun multiPointerOrCancellationRejectsNoLateUpSelection() {
        val a = arena()
        assertEquals(MenuArenaWinner.Pending, a.winner)
        a.reject()
        a.finishAt(Offset(120f, 200f))
        assertFalse(a.maySelectRow())
        assertFalse(a.mayAcceptRelease())
    }

    @Test fun outsideTapOnlyDismissesAfterUndisputedClick() {
        val a = arena(outside, null)
        assertTrue(a.mayDismissOutside(outside))
        a.finishAt(Offset(180f, 200f))
        assertFalse(a.mayDismissOutside(outside))
        assertFalse(a.maySelectRow())
        val rejected = arena(outside, null)
        rejected.reject()
        assertFalse(rejected.mayDismissOutside(outside))
    }

    @Test fun releasePositionWithoutIntermediateMoveStillChecksTapSlop() {
        val a = arena(outside, null)
        a.finishAt(Offset(130.1f, 200f))
        assertTrue(a.exceededTapSlop)
        assertFalse(a.mayDismissOutside(outside))
        val underSlop = arena(outside, null)
        underSlop.finishAt(Offset(129f, 200f))
        assertTrue(underSlop.mayDismissOutside(outside))
    }

    @Test fun coveredParentRequiresDirectTapNotDragOrRowSlide() {
        assertTrue(arena(back, null).mayFoldParent(back))
        val a = arena(back, null)
        a.move(Offset(160f, 200f))
        assertFalse(a.mayFoldParent(back))
        assertFalse(arena(row).mayFoldParent(back))
        assertFalse(arena(outside, null).mayFoldParent(back))
    }

    @Test fun disabledRowCanStartCandidateButBlankCannot() {
        assertTrue(arena(disabled, 0).maySelectRow())
        assertTrue(arena(header, null).maySelectRow())
        assertFalse(arena(blank, null).maySelectRow())
        assertFalse(arena(back, null).maySelectRow())
    }

    @Test fun sectionGapWithinListCanScrollButCardPaddingCannot() {
        val level = GlassMenuHitLevel(
            depth = 0,
            bounds = Rect(20f, 40f, 300f, 500f),
            actions = emptyList(),
            rowTopPx = 60f,
            rowHeightPx = 50f,
            scrollable = true,
            rowViewportTopPx = 60f,
            rowViewportBottomPx = 480f,
        )
        assertEquals(0,
            menuScrollOwner(listOf(level), blank, Offset(100f, 250f)))
        assertEquals(null,
            menuScrollOwner(listOf(level), blank, Offset(100f, 50f)))
        assertEquals(null,
            menuScrollOwner(listOf(level), blank, Offset(100f, 495f)))
        assertEquals(null, menuScrollOwner(listOf(level), blank))
        assertEquals(null, menuScrollOwner(listOf(level.copy(scrollable = false)),
            blank, Offset(100f, 250f)))
    }

    @Test fun sourceAndMenuBothObserveCompetingConsumptionInsteadOfClaimingDown() {
        val gesture = File(
            "src/main/java/dev/liquidglass/GlassMenuGestures.kt",
        ).readText()
        val source = File(
            "src/main/java/dev/liquidglass/GlassControls.kt",
        ).readText()
        assertTrue(gesture.contains("val arena = MenuGestureArena("))
        assertTrue(gesture.contains("menuScrollOwner(currentLevels, downTarget,"))
        assertTrue(gesture.contains("otherConsumed = change.isConsumed"))
        assertTrue(gesture.contains("awaitPointerEvent(PointerEventPass.Final)"))
        assertTrue(gesture.contains("if (change.isConsumed && arena.winner == MenuArenaWinner.Pending)"))
        assertTrue(gesture.contains("arena.mayAcceptRelease()"))
        assertTrue(gesture.contains("arena.maySelectRow()"))
        assertTrue(gesture.contains("arena.levelTransition()"))
        assertTrue(gesture.contains("arena.mayDismissOutside(released)"))
        assertFalse(gesture.contains("if (!canceled) down.consume()"))
        assertTrue(source.contains("if (down.isConsumed) return@awaitEachGesture"))
        assertTrue(source.contains("awaitPointerEvent(PointerEventPass.Final)"))
        assertTrue(source.contains("(recognized || !pointer.isConsumed)"))
    }
}
