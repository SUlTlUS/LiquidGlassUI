package dev.liquidglass27.glass

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class GlassMenuGestureHitTest {
    private fun actions(count: Int): List<GlassMenuAction> =
        List(count) { index ->
            GlassMenuAction(text = "Item $index", symbol = "•", onClick = {})
        }

    private fun root() = GlassMenuHitLevel(
        depth = 0,
        bounds = Rect(20f, 40f, 300f, 500f),
        actions = actions(5),
        rowTopPx = 60f,
        rowHeightPx = 50f,
        scrollable = true,
    )

    private fun child() = GlassMenuHitLevel(
        depth = 1,
        bounds = Rect(180f, 100f, 460f, 450f),
        actions = actions(4),
        rowTopPx = 160f,
        rowHeightPx = 53f,
        scrollable = true,
        headerTopPx = 110f,
        headerHeightPx = 50f,
    )

    @Test
    fun activeSubmenuHeaderWinsOverRows() {
        assertEquals(
            MenuTouchTarget(TargetKind.Header, depth = 1),
            hitMenu(listOf(root(), child()), Offset(220f, 145f)),
        )
        assertEquals(
            MenuTouchTarget(TargetKind.Row, depth = 1, row = 1),
            hitMenu(listOf(root(), child()), Offset(220f, 225f)),
        )
    }

    @Test
    fun touchingParentUnderOpenChildMeansBackNotSelection() {
        assertEquals(
            MenuTouchTarget(TargetKind.Back),
            hitMenu(listOf(root(), child()), Offset(60f, 86f)),
        )
        assertEquals(
            MenuTouchTarget(TargetKind.Outside),
            hitMenu(listOf(root(), child()), Offset(490f, 120f)),
        )
    }

    @Test
    fun recedingParentBackHitFollowsItsPaintedFivePercentScale() {
        val parent = root().copy(
            paintPivot = Offset(300f, 100f),
            paintScale = .95f,
        )
        val child = child()
        // A point only inside the UNscaled parent is actually outside
        // its drawn silhouette and must not trigger a phantom Back.
        assertEquals(MenuTouchTarget(TargetKind.Outside),
            hitMenu(listOf(parent, child), Offset(23f, 60f)))
        // A point in the visually shrunken parent still returns Back.
        assertEquals(MenuTouchTarget(TargetKind.Back),
            hitMenu(listOf(parent, child), Offset(45f, 90f)))
        assertEquals(Offset(20f, 40f),
            menuPointInPaintedLevel(parent, Offset(34f, 43f)))
    }

    @Test
    fun movingSubmenuHeaderTracksRenderedScaleInsteadOfUnscaledHitBox() {
        val child = child().copy(
            paintPivot = Offset(460f, 220f),
            paintScale = .95f,
        )
        val visualTitleTouch = Offset(300f, 220f + (145f - 220f) * .95f)
        assertEquals(MenuTouchTarget(TargetKind.Header, depth = 1),
            hitMenu(listOf(root(), child), visualTitleTouch))
        // A visual point outside the scaled left edge must not be treated
        // as if the card still had its full unsquashed width.
        assertEquals(MenuTouchTarget(TargetKind.Outside),
            hitMenu(listOf(child), Offset(181f, 110f)))
    }

    @Test
    fun shiftedRowTopTracksRealScrollOffset() {
        val moved = child().copy(rowTopPx = 107f)
        assertEquals(
            MenuTouchTarget(TargetKind.Row, depth = 1, row = 1),
            hitMenu(listOf(root(), moved), Offset(225f, 175f)),
        )
        assertEquals(
            MenuTouchTarget(TargetKind.Header, depth = 1),
            hitMenu(listOf(root(), moved), Offset(225f, 145f)),
        )
    }

    @Test
    fun scrolledRowsCannotBeSelectedBehindPinnedHeaderOrBottomPadding() {
        val clipped = child().copy(
            rowTopPx = 120f,
            rowViewportTopPx = 160f,
            rowViewportBottomPx = 425f,
        )
        val levels = listOf(root(), clipped)
        assertEquals(
            MenuTouchTarget(TargetKind.Header, depth = 1),
            hitMenu(levels, Offset(220f, 145f)),
        )
        // Row 0 is scrolled halfway behind the fixed submenu header. Only
        // its visible portion below 160px can be pressed or selected.
        assertEquals(
            MenuTouchTarget(TargetKind.Row, depth = 1, row = 0),
            hitMenu(levels, Offset(220f, 164f)),
        )
        assertEquals(
            MenuTouchTarget(TargetKind.Blank, depth = 1),
            hitMenu(levels, Offset(220f, 438f)),
        )
    }

    @Test
    fun rootScrollNeverSelectsRowsInsideItsBottomPadding() {
        val clipped = root().copy(
            rowTopPx = 450f,
            rowViewportTopPx = 49f,
            rowViewportBottomPx = 491f,
        )
        assertEquals(
            MenuTouchTarget(TargetKind.Row, depth = 0, row = 0),
            hitMenu(listOf(clipped), Offset(80f, 470f)),
        )
        assertEquals(
            MenuTouchTarget(TargetKind.Blank, depth = 0),
            hitMenu(listOf(clipped), Offset(80f, 495f)),
        )
    }

    @Test
    fun onlyTopScrollableRowMayOwnScroll() {
        val root = root()
        val child = child()
        val nested = listOf(root, child)
        assertEquals(0, menuScrollOwner(listOf(root), MenuTouchTarget(TargetKind.Row, 0, 1)))
        assertEquals(1, menuScrollOwner(nested, MenuTouchTarget(TargetKind.Row, 1, 2)))
        assertEquals(null, menuScrollOwner(nested, MenuTouchTarget(TargetKind.Row, 0, 1)))
        assertEquals(null, menuScrollOwner(nested, MenuTouchTarget(TargetKind.Header, 1)))
        assertEquals(null, menuScrollOwner(nested, MenuTouchTarget(TargetKind.Blank, 1)))
        assertEquals(null, menuScrollOwner(nested, MenuTouchTarget(TargetKind.Outside)))
        assertEquals(null, menuScrollOwner(listOf(root, child.copy(scrollable = false)),
            MenuTouchTarget(TargetKind.Row, 1, 1)))
    }

    @Test
    fun foldingSubmenuDoesNotRedirectItsDragToRootScroll() {
        val root = root()
        val child = child()
        val scrollOwner = menuScrollOwner(listOf(root, child),
            MenuTouchTarget(TargetKind.Row, 1, 2))
        assertTrue(menuScrollOwnerStillActive(listOf(root, child), scrollOwner))
        assertFalse(menuScrollOwnerStillActive(listOf(root), scrollOwner))
        assertFalse(menuScrollOwnerStillActive(emptyList(), scrollOwner))
        assertFalse(menuScrollOwnerStillActive(listOf(root), null))
    }

    @Test
    fun originalPressLandsOnTheNewTitleAfterOpeningChild() {
        val press = FlClashMenuPress()
        press.start(MenuTouchTarget(TargetKind.Row, depth = 0, row = 2))
        press.completeDwell(MenuDwellAction.OpenChild)
        assertEquals(MenuTouchTarget(TargetKind.Header, depth = 1), press.pressed)
        assertTrue(press.landed)
        assertFalse(press.canRunRelease())
        // Flutter _pressed = _headerIndex: unchanged title cannot immediately
        // open/fold again, however far the fingertip travels within that row.
        assertFalse(press.move(MenuTouchTarget(TargetKind.Header, depth = 1), 1))
        assertFalse(press.canRunRelease())
    }

    @Test
    fun childNotYetHitTestableCannotTurnTheLandedPressIntoParentBack() {
        val press = FlClashMenuPress()
        press.start(MenuTouchTarget(TargetKind.Row, depth = 0, row = 1))
        press.completeDwell(MenuDwellAction.OpenChild)
        assertFalse(press.move(MenuTouchTarget(TargetKind.Back), 0))
        assertTrue(press.landed)
        // Once the child is genuinely hit-testable a DIFFERENT row switches
        // _pressed and clears _landed. No invented post-dwell distance gate.
        assertTrue(press.move(MenuTouchTarget(TargetKind.Row, depth = 1, row = 2), 1))
        assertFalse(press.landed)
        assertTrue(press.canRunRelease())
    }

    @Test
    fun slidingAwayAndBackToHeaderStartsANewPressJustLikeFlClash() {
        val press = FlClashMenuPress()
        val header = MenuTouchTarget(TargetKind.Header, depth = 1)
        press.start(MenuTouchTarget(TargetKind.Row, depth = 0, row = 1))
        press.completeDwell(MenuDwellAction.OpenChild)
        assertFalse(press.move(header, 1))
        assertTrue(press.move(MenuTouchTarget(TargetKind.Row, depth = 1, row = 0), 1))
        assertTrue(press.move(header, 1))
        assertFalse(press.landed)
        assertEquals(MenuDwellAction.ToggleHeader,
            menuDwellAction(header, listOf(root(), child()), headerFolding = false))
    }

    @Test
    fun flClashSlideRecognizerComparesOnlyRowIndices() {
        val press = FlClashMenuPress()
        press.start(MenuTouchTarget(TargetKind.Header, depth = 1))
        assertEquals(-1, flClashPressedIndex(press.pressed))
        // _pressed is _headerIndex regardless of which menu is morphing.
        assertFalse(press.move(MenuTouchTarget(TargetKind.Header, depth = 2), 2))
        assertEquals(MenuTouchTarget(TargetKind.Header, 1), press.pressed)
        assertTrue(press.move(MenuTouchTarget(TargetKind.Row, depth = 2, row = 1), 2))
        // In FlClash, same item index stays held as the card changes depth.
        assertFalse(press.move(MenuTouchTarget(TargetKind.Row, depth = 3, row = 1), 3))
        assertTrue(press.move(MenuTouchTarget(TargetKind.Header, depth = 3), 3))
        assertFalse(press.move(MenuTouchTarget(TargetKind.Header, depth = 2), 3))
    }

    @Test
    fun flClashBlankAndCoveredParentAreEquivalentNoRow() {
        val press = FlClashMenuPress()
        press.start(MenuTouchTarget(TargetKind.Row, depth = 1, row = 1))
        assertTrue(press.move(MenuTouchTarget(TargetKind.Back), 1))
        assertFalse(press.move(MenuTouchTarget(TargetKind.Outside), 1))
        assertFalse(press.move(MenuTouchTarget(TargetKind.Blank, depth = 1), 1))
        assertEquals(null, flClashPressedIndex(press.pressed))
    }

    @Test
    fun submenuTitleHitTracksActualPaintedContentDuringSpring() {
        val origin = 260f
        val final = 80f
        val inset = 16f
        assertEquals(origin, glassSubmenuHeaderTopPx(origin, final, 0f, inset), .0001f)
        assertEquals(final + inset,
            glassSubmenuHeaderTopPx(origin, final, 1f, inset), .0001f)
        assertEquals(178f,
            glassSubmenuHeaderTopPx(origin, final, .5f, inset), .0001f)
        val intermediate = glassSubmenuHeaderTopPx(origin, final, .87f, inset)
        assertTrue(intermediate > final + inset)
        assertTrue(intermediate < origin)
        // This animated title is a Header hit, not a row at the final card.
        val moving = child().copy(
            bounds = Rect(180f, 60f, 460f, 450f),
            headerTopPx = intermediate,
            headerHeightPx = 53f,
        )
        assertEquals(MenuTouchTarget(TargetKind.Header, depth = 1),
            hitMenu(listOf(root(), moving), Offset(220f, intermediate + 20f)))
    }

    @Test
    fun scrollingIgnoresAccumulatedBackAndForthJitter() {
        for (y in listOf(1f, 6f, 2f, 6f, 1f, 5f, 0f, 7f)) {
            assertFalse(menuScrollExceededSlop(0f, y, 8f))
        }
        assertFalse(menuScrollExceededSlop(0f, 8f, 8f))
        assertTrue(menuScrollExceededSlop(0f, 8.1f, 8f))
        assertTrue(menuScrollExceededSlop(0f, -8.1f, 8f))
    }
}
