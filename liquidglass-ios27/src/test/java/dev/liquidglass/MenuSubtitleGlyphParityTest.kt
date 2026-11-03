package dev.liquidglass

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import java.io.File
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/** popup.dart::_buildRow: subtitle footnote and 20dp leading glyph. */
class MenuSubtitleGlyphParityTest {
    private fun item(label: String, subtitle: String? = null,
                     section: GlassMenuSection? = null): GlassMenuAction =
        GlassMenuAction(label, "✦", subtitle = subtitle, section = section, onClick = {})

    @Test
    fun subtitleAddsPreciselyOneOrTwoLinesWithoutChangingOrdinaryRows() {
        assertEquals(44f, menuItemHeightDp(1f, 0), 0f)
        assertEquals(62f, menuItemHeightDp(1f, 1), 0f)
        assertEquals(80f, menuItemHeightDp(1f, 2), 0f)
        assertEquals(54f, menuItemHeightDp(1.5f, 0), 0f)
        assertEquals(81f, menuItemHeightDp(1.5f, 1), 0f)
        assertEquals(108f, menuItemHeightDp(1.5f, 2), 0f)
        assertEquals(80f, menuItemHeightDp(1f, 9), 0f)
    }

    @Test
    fun measuredRowHeightsDriveSectionOffsetsAndHitBounds() {
        val actions = listOf(
            item("Plain"),
            item("One line", "A sentence"),
            item("Two lines", "Long title", GlassMenuSection("Other")),
        )
        val rows = menuContentRows(actions, 1f, listOf(44f, 62f, 80f))
        // First row [0,44], second [44,106]; 17+28 section prefix.
        assertEquals(listOf(0f, 44f, 151f), rows.itemTopsDp)
        assertEquals(listOf(44f, 62f, 80f), rows.itemHeightsDp)
        assertEquals(231f, rows.totalHeightDp, 0f)
        val level = GlassMenuHitLevel(
            depth = 0, bounds = Rect(0f, 0f, 350f, 260f),
            actions = actions, rowTopPx = 0f, rowHeightPx = 44f,
            rowOffsetsPx = rows.rowStartOffsetsPx(1f),
            rowHeightsPx = rows.rowHeightsPx(1f),
            scrollable = true,
        )
        fun hit(y: Float) = hitMenu(listOf(level), Offset(80f, y))
        assertEquals(MenuTouchTarget(TargetKind.Row, 0, 0), hit(43f))
        assertEquals(MenuTouchTarget(TargetKind.Row, 0, 1), hit(85f))
        assertEquals(MenuTouchTarget(TargetKind.Blank, 0), hit(140f))
        assertEquals(MenuTouchTarget(TargetKind.Row, 0, 2), hit(211f))
        assertEquals(MenuTouchTarget(TargetKind.Blank, 0), hit(237f))
    }

    @Test
    fun submenuHeightUsesRealSubtitleAndHeaderHeights() {
        val actions = listOf(item("First", "First"), item("Second"))
        val rows = menuContentRows(actions, 1f, listOf(62f, 44f))
        val full = glassSubmenuGeometry(
            parent = Rect(100f, 100f, 330f, 400f),
            origin = Rect(110f, 130f, 320f, 192f),
            itemCount = actions.size, progress = 1f,
            screenWidth = 800f, screenHeight = 1200f, density = 1f,
            // Pure JVM geometry test: the already-measured contentRows
            // supplies all heights; no Android Paint instance is needed.
            items = emptyList(), fontScale = 1f,
            contentRows = rows, headerHeightDp = 62f,
        )
        assertEquals(201f, full.target.height, 0f) // 106 + 62 + 33
    }

    @Test
    fun leadingGlyphOverridesLegacySymbolAndEndMarkMatchesOriginal() {
        val ordinary = item("A")
        assertEquals("✦", menuLeadingGlyph(ordinary))
        assertEquals("★", menuLeadingGlyph(ordinary.copy(glyph = "★")))
        assertFalse(menuHasEndMark(ordinary))
        assertTrue(menuHasEndMark(ordinary.copy(selected = true)))
        assertTrue(menuHasEndMark(ordinary.copy(subActions = listOf(item("B")))))
        assertTrue(menuHasEndMark(ordinary, header = true))
        assertEquals(null, menuLeadingGlyph(ordinary.copy(glyph = null, symbol = "›")))
    }

    @Test
    fun rootAndNestedRenderersUseSharedMeasuredRowsAndTextComponent() {
        val root = File("src/main/java/dev/liquidglass/GlassMenu.kt").readText()
        val nested = File("src/main/java/dev/liquidglass/GlassSubmenu.kt").readText()
        val itemRenderer = File(
            "src/main/java/dev/liquidglass/MenuItemContent.kt",
        ).readText()
        assertTrue(root.contains("rowHeightsPx = rootRows.rowHeightsPx(density)"))
        assertTrue(root.contains("rowHeightsPx = firstRows.rowHeightsPx(density)"))
        assertTrue(root.contains("rowHeightsPx = ownerRows.rowHeightsPx(density)"))
        assertTrue(nested.contains("contentRows = itemRows"))
        assertTrue(nested.contains("headerHeightDp = headerHeightDp"))
        assertTrue(nested.contains("returnRows.itemHeightDp(index)"))
        assertTrue(itemRenderer.contains("maxLines = 2"))
        assertTrue(itemRenderer.contains("rememberTextMeasurer()"))
        assertTrue(itemRenderer.contains("TextOverflow.Ellipsis"))
    }
}
