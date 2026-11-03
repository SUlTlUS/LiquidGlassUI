package dev.liquidglass27.glass

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import org.junit.Assert.assertEquals
import org.junit.Test

/** FlClash popup.dart:_buildContent with section headers and dividers. */
class MenuSectionParityTest {
    private val items = listOf(
        GlassMenuAction("One", "1", section = GlassMenuSection("Main")) {},
        GlassMenuAction("Two", "2") {},
        GlassMenuAction("Three", "3", section = GlassMenuSection()) {},
        GlassMenuAction("Four", "4", section = GlassMenuSection("Other")) {},
    )

    private val rows = menuContentRows(items, 1f)

    @Test
    fun sectionHeadersAndDividersTakeSpaceWithoutChangingActionIndices() {
        assertEquals(listOf(28f, 72f, 133f, 222f), rows.itemTopsDp)
        assertEquals(44f, rows.rowHeightDp, 0f)
        assertEquals(266f, rows.totalHeightDp, 0f)
        assertEquals(0f, menuSectionPrefixDp(1, null, 1f), 0f)
        assertEquals(17f, menuSectionPrefixDp(2, GlassMenuSection(), 1f), 0f)
        assertEquals(45f, menuSectionPrefixDp(3, GlassMenuSection("Other"), 1f), 0f)
    }

    @Test
    fun touchingASectionTitleOrDividerDoesNotActivateTheNearbyRow() {
        val top = GlassMenuHitLevel(
            depth = 0, bounds = Rect(0f, 0f, 300f, 290f),
            actions = items, rowTopPx = 10f, rowHeightPx = rows.rowHeightDp,
            rowOffsetsPx = rows.rowStartOffsetsPx(1f), scrollable = true,
        )
        fun touch(y: Float) = hitMenu(listOf(top), Offset(60f, y))
        assertEquals(MenuTouchTarget(TargetKind.Blank, 0), touch(15f))
        assertEquals(MenuTouchTarget(TargetKind.Row, 0, 0), touch(38f))
        assertEquals(MenuTouchTarget(TargetKind.Row, 0, 1), touch(90f))
        assertEquals(MenuTouchTarget(TargetKind.Blank, 0), touch(132f))
        assertEquals(MenuTouchTarget(TargetKind.Row, 0, 2), touch(152f))
        assertEquals(MenuTouchTarget(TargetKind.Blank, 0), touch(205f))
        assertEquals(MenuTouchTarget(TargetKind.Row, 0, 3), touch(240f))
    }

    @Test
    fun binarySearchReturnsPrecedingRowAndRejectsContentBeforeFirst() {
        assertEquals(-1, menuRowIndexAtOffset(rows.itemTopsDp, 0f))
        assertEquals(0, menuRowIndexAtOffset(rows.itemTopsDp, 28f))
        assertEquals(1, menuRowIndexAtOffset(rows.itemTopsDp, 100f))
        assertEquals(2, menuRowIndexAtOffset(rows.itemTopsDp, 134f))
        assertEquals(3, menuRowIndexAtOffset(rows.itemTopsDp, 300f))
    }

    @Test
    fun selectedIndexIsCenteredUsingRealSectionOffsets() {
        assertEquals(0, menuCheckedInitialScrollPx(0, rows, 1f, 170f))
        assertEquals(96, menuCheckedInitialScrollPx(3, rows, 1f, 170f))
    }
}
