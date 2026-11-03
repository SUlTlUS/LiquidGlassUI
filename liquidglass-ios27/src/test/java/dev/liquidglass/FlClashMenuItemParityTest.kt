package dev.liquidglass

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/** FlClash popup.dart::_buildItem / _revealChecked / _buildLazyContent. */
class FlClashMenuItemParityTest {
    private val disabled = GlassMenuAction(text = "Disabled", symbol = "○")
    private val selected = GlassMenuAction(text = "Selected", symbol = "●", selected = true) {}
    private val folder = GlassMenuAction(
        text = "Folder", symbol = "›", subActions = listOf(disabled),
    )
    private val actions = listOf(disabled, selected, folder)

    private val level = GlassMenuHitLevel(
        depth = 0,
        bounds = Rect(0f, 0f, 300f, 180f),
        actions = actions,
        rowTopPx = 10f,
        rowHeightPx = 50f,
        scrollable = false,
    )

    @Test
    fun leafWithoutCallbackIsDisabledButChildMenuRemainsActionable() {
        assertFalse(menuActionEnabled(disabled))
        assertTrue(menuActionEnabled(selected))
        assertTrue(menuActionEnabled(folder))
        assertEquals(MenuTouchTarget(TargetKind.DisabledRow, 0, 0),
            hitMenu(listOf(level), Offset(50f, 35f)))
        assertEquals(0, menuScrollOwner(listOf(level.copy(scrollable = true)),
            hitMenu(listOf(level), Offset(50f, 35f))))
        assertEquals(MenuTouchTarget(TargetKind.Row, 0, 1),
            hitMenu(listOf(level), Offset(50f, 85f)))
        assertEquals(MenuTouchTarget(TargetKind.Row, 0, 2),
            hitMenu(listOf(level), Offset(50f, 135f)))
    }

    @Test
    fun disabledLeafCannotActivateThroughStaleGesture() {
        val args = listOf(level)
        fun canPick(index: Int) = canActivateMenuRow(
            levels = args, depth = 0, index = index,
            firstSubmenuIndex = -1, firstOpen = false,
            nestedDepth = 0, nestedFolding = false,
        )
        assertFalse(canPick(0))
        assertTrue(canPick(1))
        assertTrue(canPick(2))
    }

    @Test
    fun checkedItemScrollsNearCenterOfSevenAndHalfRows() {
        assertEquals(0, menuCheckedInitialScrollPx(0, 20, 50f, 375f))
        assertEquals(0, menuCheckedInitialScrollPx(3, 20, 50f, 375f))
        assertEquals(150, menuCheckedInitialScrollPx(6, 20, 50f, 375f))
        assertEquals(625, menuCheckedInitialScrollPx(19, 20, 50f, 375f))
    }

    @Test
    fun checkedItemRespectsSmallWindowAndAbsentSelection() {
        assertEquals(100, menuCheckedInitialScrollPx(3, 20, 50f, 140f))
        assertEquals(0, menuCheckedInitialScrollPx(-1, 20, 50f, 375f))
        assertEquals(0, menuCheckedInitialScrollPx(4, 4, 50f, 375f))
        assertEquals(0, menuCheckedInitialScrollPx(1, 20, 0f, 375f))
        assertEquals(0, menuCheckedInitialScrollPx(1, 20, Float.NaN, 375f))
        assertEquals(0, menuCheckedInitialScrollPx(1, 20, 50f, Float.NaN))
    }
}
