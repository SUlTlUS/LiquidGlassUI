package dev.liquidglass27.glass

import androidx.compose.ui.geometry.Rect
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class GlassMenuTopLevelActivationTest {
    private fun level(depth: Int): GlassMenuHitLevel = GlassMenuHitLevel(
        depth = depth,
        bounds = Rect(0f, 0f, 200f, 400f),
        actions = List(3) { i ->
            GlassMenuAction(text = "Item " + i, symbol = "•", onClick = {})
        },
        rowTopPx = 0f,
        rowHeightPx = 53f,
        scrollable = false,
    )

    private fun allowed(
        visible: List<GlassMenuHitLevel>,
        depth: Int,
        firstIndex: Int,
        firstOpen: Boolean,
        nestedDepth: Int,
        folding: Boolean = false,
        row: Int = 0,
    ) = canActivateMenuRow(
        visible, depth, row, firstIndex, firstOpen, nestedDepth, folding,
    )

    @Test
    fun rootRowsAreBlockedWhileAChildIsOpeningOrFolding() {
        val root = listOf(level(0))
        assertTrue(allowed(root, 0, -1, false, 0))
        assertFalse(allowed(root, 0, 0, true, 0))
        assertFalse(allowed(root, 0, 0, false, 0))
        assertFalse(allowed(root, 0, 0, true, 1))
    }

    @Test
    fun onlyTheActuallyVisibleTopLayerCanActivateRows() {
        val root = level(0)
        val first = level(1)
        val second = level(2)
        assertTrue(allowed(listOf(root, first), 1, 0, true, 0))
        assertFalse(allowed(listOf(root, first), 0, 0, true, 0))
        assertFalse(allowed(listOf(root, first), 1, 0, false, 0))
        assertFalse(allowed(listOf(root, first), 1, 0, true, 1))
        assertTrue(allowed(listOf(root, first, second), 2, 0, true, 1))
        assertFalse(allowed(listOf(root, first, second), 1, 0, true, 1))
        assertFalse(allowed(listOf(root, first, second), 0, 0, true, 1))
    }

    @Test
    fun foldingDeepestCardMustNotFireLeafActions() {
        val levels = listOf(level(0), level(1), level(2))
        assertFalse(allowed(levels, 2, 0, true, 1, folding = true))
        assertFalse(allowed(levels, 2, 0, true, 1, row = -1))
        assertFalse(allowed(levels, 2, 0, true, 1, row = 3))
        assertFalse(allowed(emptyList(), 2, 0, true, 1))
    }

    @Test
    fun systemBackDoesNotReopenADeepMenuWhileFolding() {
        assertTrue(ignoreRepeatedNestedBack(folding = true, headerToggle = false))
        assertFalse(ignoreRepeatedNestedBack(folding = true, headerToggle = true))
        assertFalse(ignoreRepeatedNestedBack(folding = false, headerToggle = false))
        assertFalse(ignoreRepeatedNestedBack(folding = false, headerToggle = true))
    }
}
