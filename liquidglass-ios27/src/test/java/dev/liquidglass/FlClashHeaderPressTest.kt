package dev.liquidglass

import androidx.compose.ui.geometry.Rect
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/** FlClash popup.dart: _buildRow(header) -> _hold -> _toggleTop / _landed. */
class FlClashHeaderPressTest {
    private val child = GlassMenuHitLevel(
        depth = 1,
        bounds = Rect(0f, 0f, 280f, 400f),
        actions = listOf(
            GlassMenuAction(text = "Folder", symbol = "›", subActions = listOf(
                GlassMenuAction(text = "Leaf", symbol = "•", onClick = {}),
            ), onClick = {}),
            GlassMenuAction(text = "Leaf", symbol = "•", onClick = {}),
        ),
        rowTopPx = 65f,
        rowHeightPx = 53f,
        scrollable = false,
        headerTopPx = 8f,
        headerHeightPx = 53f,
    )
    private val header = MenuTouchTarget(TargetKind.Header, depth = 1)

    @Test
    fun pressAndHoldTitleTogglesAfterDwellEvenWithoutLift() {
        assertEquals(MenuDwellAction.ToggleHeader,
            menuDwellAction(header, listOf(child), headerFolding = false))
        val press = FlClashMenuPress()
        press.start(header)
        press.completeDwell(MenuDwellAction.ToggleHeader)
        assertTrue(press.landed)
        // Following Up must not call _toggleTop a SECOND time.
        assertFalse(press.canRunRelease())
    }

    @Test
    fun shortPressTitleTogglesOnReleaseWhenDwellDidNotFire() {
        val press = FlClashMenuPress()
        press.start(header)
        assertTrue(press.canRunRelease())
        assertEquals(MenuDwellAction.ToggleHeader,
            menuDwellAction(press.pressed, listOf(child), headerFolding = false))
    }

    @Test
    fun titleWhileFoldingCannotStartAnotherDwellButReleaseReverses() {
        val press = FlClashMenuPress()
        press.start(header)
        assertEquals(MenuDwellAction.None,
            menuDwellAction(header, listOf(child), headerFolding = true))
        assertTrue(press.canRunRelease())
    }

    @Test
    fun leafAndCoveredParentNeverArmHeaderOrSubmenuDwell() {
        assertEquals(MenuDwellAction.None,
            menuDwellAction(MenuTouchTarget(TargetKind.Row, 1, 1),
                listOf(child), headerFolding = false))
        assertEquals(MenuDwellAction.OpenChild,
            menuDwellAction(MenuTouchTarget(TargetKind.Row, 1, 0),
                listOf(child), headerFolding = false))
        assertEquals(MenuDwellAction.None,
            menuDwellAction(MenuTouchTarget(TargetKind.Back),
                listOf(child), headerFolding = false))
        assertEquals(MenuDwellAction.None,
            menuDwellAction(MenuTouchTarget(TargetKind.Header, 0),
                listOf(child), headerFolding = false))
    }

    @Test
    fun foldingCardRetainsHeaderHitButHidesItsRows() {
        // GlassMenu publishes the header at progress > 0.08 even after its
        // content stops being active. FlClash _toggleTop needs that hit.
        val headerOnly = child.copy(actions = emptyList())
        assertEquals(MenuTouchTarget(TargetKind.Header, depth = 1),
            hitMenu(listOf(headerOnly), androidx.compose.ui.geometry.Offset(40f, 24f)))
        assertEquals(MenuTouchTarget(TargetKind.Blank, depth = 1),
            hitMenu(listOf(headerOnly), androidx.compose.ui.geometry.Offset(40f, 102f)))
        assertEquals(MenuDwellAction.None,
            menuDwellAction(header, listOf(headerOnly), headerFolding = true))
        val press = FlClashMenuPress()
        press.start(header)
        assertTrue(press.canRunRelease())
    }

    @Test
    fun foldingAndGrowingTitlesRemainHittableWhileRowsAreNot() {
        val waitingRows = child.copy(rowsEnabled = false)
        val levels = listOf(waitingRows)
        val title = androidx.compose.ui.geometry.Offset(100f, 30f)
        val row = androidx.compose.ui.geometry.Offset(100f, 85f)
        assertEquals(header, hitMenu(levels, title))
        assertEquals(MenuTouchTarget(TargetKind.Blank, 1), hitMenu(levels, row))
        assertEquals(MenuDwellAction.ToggleHeader,
            menuDwellAction(header, levels, headerFolding = false))
        assertEquals(MenuDwellAction.None,
            menuDwellAction(MenuTouchTarget(TargetKind.Row, 1, 0), levels,
                headerFolding = false))
        // During a fold only releasing the header reverses; a 400ms title
        // dwell must not repeatedly toggle the half-folded spring.
        assertEquals(MenuDwellAction.None,
            menuDwellAction(header, levels, headerFolding = true))
    }

    @Test
    fun deeperHeaderTargetsOnlyItsCurrentMenuLevel() {
        val deep = child.copy(depth = 2)
        assertEquals(MenuDwellAction.ToggleHeader,
            menuDwellAction(MenuTouchTarget(TargetKind.Header, 2),
                listOf(child, deep), headerFolding = false))
        assertEquals(MenuDwellAction.None,
            menuDwellAction(header, listOf(child, deep), headerFolding = false))
    }

    @Test
    fun pressingAnotherRowClearsLandedOnlyAfterFoldCompletes() {
        val press = FlClashMenuPress()
        press.start(header)
        press.completeDwell(MenuDwellAction.ToggleHeader)
        assertFalse(press.canRunRelease())
        // When the title shrinks, the parent becomes hittable early in
        // Compose. Flutter's pressed title does not lose ownership yet.
        assertFalse(press.move(MenuTouchTarget(TargetKind.Row, 0, 0), 0))
        assertFalse(press.canRunRelease())
        val handoff = MenuFoldPressHandoff()
        handoff.completed(1, 0)
        assertTrue(press.foldedToParent(handoff.latest!!))
        assertFalse(press.move(MenuTouchTarget(TargetKind.Row, 0, 0), 0))
        assertTrue(press.move(MenuTouchTarget(TargetKind.Row, 0, 1), 0))
        assertTrue(press.canRunRelease())
        assertFalse(press.landed)
    }
}
