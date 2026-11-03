package dev.liquidglass27.glass

import java.io.File
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/** FlClash popup.dart:_buildsLazily/_buildLazyContent regression contract. */
class MenuLazyRowsParityTest {
    private fun action(
        text: String,
        selected: Boolean = false,
        subtitle: String? = null,
        section: GlassMenuSection? = null,
        subActions: List<GlassMenuAction> = emptyList(),
    ) = GlassMenuAction(
        text = text, symbol = "•", selected = selected,
        subtitle = subtitle, section = section, subActions = subActions,
        onClick = {},
    )

    @Test
    fun lazyRequiresExplicitVisibleCapAndRootLevel() {
        val actions = List(48) { action("Item $it", selected = it == 22) }
        assertFalse(menuBuildsLazily(null, actions))
        assertFalse(menuBuildsLazily(7, actions, isRoot = false))
        assertTrue(menuBuildsLazily(7, actions))
        assertTrue(menuBuildsLazily(4, actions))
    }

    @Test
    fun childSubtitleAndSectionDisqualifyLazyExactlyLikeFlClash() {
        val leaf = action("A")
        assertFalse(menuBuildsLazily(7, listOf(leaf.copy(
            subActions = listOf(action("child")),
        ))))
        assertFalse(menuBuildsLazily(7, listOf(leaf.copy(subtitle = ""))))
        assertFalse(menuBuildsLazily(7, listOf(leaf.copy(
            section = GlassMenuSection(),
        ))))
        // Checked, dangerous, disabled and glyph rows still share itemExtent.
        assertTrue(menuBuildsLazily(7, listOf(
            leaf.copy(selected = true),
            leaf.copy(destructive = true),
            leaf.copy(onClick = null),
            leaf.copy(glyph = "★"),
        )))
    }

    @Test
    fun checkedOffsetUsesWholeRowsAboveAndClampsToHalfRowViewport() {
        val count = 80
        val itemExtent = 44f
        val height = 7.5f * itemExtent
        assertEquals(0, menuLazyCheckedInitialScrollPx(-1, count, itemExtent, 7, height))
        assertEquals(0, menuLazyCheckedInitialScrollPx(2, count, itemExtent, 7, height))
        assertEquals(44, menuLazyCheckedInitialScrollPx(4, count, itemExtent, 7, height))
        assertEquals(1188, menuLazyCheckedInitialScrollPx(30, count, itemExtent, 7, height))
        assertEquals(3190, menuLazyCheckedInitialScrollPx(79, count, itemExtent, 7, height))
        assertEquals(0, menuLazyCheckedInitialScrollPx(1, 4, itemExtent, 7, height))
        assertEquals(0, menuLazyCheckedInitialScrollPx(30, count, 0f, 7, height))
    }

    @Test
    fun checkedOffsetUsesSpecifiedCapNotHardcodedSeven() {
        val count = 30
        val extent = 44f
        val height = 3.5f * extent
        // Visible=3 => one full row above checked, not floor(7/2).
        assertEquals(308, menuLazyCheckedInitialScrollPx(8, count, extent, 3, height))
        assertEquals(0, menuLazyCheckedInitialScrollPx(0, count, extent, 3, height))
        assertEquals(1166, menuLazyCheckedInitialScrollPx(29, count, extent, 3, height))
    }

    @Test
    fun lazyStateIndexOffsetRoundTripMatchesPointerGeometry() {
        val positions = listOf(0, 22, 43, 44, 87, 1000, 1188, 3190)
        for (px in positions) {
            val position = menuLazyItemPosition(px, 80, 44f)
            assertEquals(px.toFloat(), menuLazyScrollOffsetPx(
                position.index, position.scrollOffset, 44f,
            ), 0.0001f)
        }
        assertEquals(MenuLazyItemPosition(0, 0),
            menuLazyItemPosition(-5, 80, 44f))
        assertEquals(MenuLazyItemPosition(0, 0),
            menuLazyItemPosition(100, 0, 44f))
    }

    @Test
    fun composeLazyRootSharesScrollOffsetWithMorphHitAndSketchRecording() {
        val source = File(
            "src/main/java/dev/liquidglass27/glass/GlassMenu.kt",
        ).readText()
        assertTrue(source.contains("menuBuildsLazily(maxVisibleItems, actions)"))
        assertTrue(source.contains("(rootVisibleItems + .5f) * rowHeightDp"))
        assertTrue(source.contains("LazyColumn("))
        assertTrue(source.contains("items(count = actions.size"))
        assertTrue(source.contains("userScrollEnabled = false"))
        assertTrue(source.contains("if (rootIsLazy) rootLazyScroll else rootScroll"))
        assertTrue(source.contains("menuLazyScrollOffsetPx("))
        assertTrue(source.contains("rootLazyScroll.scrollToItem(start.index, start.scrollOffset)"))
        assertTrue(source.contains("rowTopPx = card.top + 9f * density - rootScrollPx"))
        assertTrue(source.contains("dark, submenuIndex, rootHovered, rootScrollPx"))
        assertTrue(source.contains("rootRows.itemTopDp(submenuIndex)"))
        assertTrue(source.contains("renderRootRow(index)"))
        assertTrue(source.contains(".verticalScroll(rootScroll)"))
    }
}
