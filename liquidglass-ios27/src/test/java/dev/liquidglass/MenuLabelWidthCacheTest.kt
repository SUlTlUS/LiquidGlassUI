package dev.liquidglass

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class MenuLabelWidthCacheTest {
    private fun actions(vararg labels: String): List<GlassMenuAction> =
        labels.map { GlassMenuAction(text = it, symbol = "•", onClick = {}) }

    @Test
    fun staticLabelsAreMeasuredOnceAcrossThousandsOfAnimationFrames() {
        val cache = MenuLabelWidthCache()
        var measured = 0
        repeat(5000) {
            val width = cache.width(actions("Open", "Rename", "Close"), 2f, 1f) {
                measured++
                420f
            }
            assertEquals(420f, width, 0f)
        }
        assertEquals(1, measured)
        assertEquals(1, cache.size)
    }

    @Test
    fun changedTextDensityAndFontScaleInvalidateOnlyTheirOwnEntries() {
        val cache = MenuLabelWidthCache()
        var measured = 0
        fun width(labels: List<GlassMenuAction>, density: Float, scale: Float) =
            cache.width(labels, density, scale) { (++measured * 100).toFloat() }

        assertEquals(100f, width(actions("One"), 2f, 1f), 0f)
        assertEquals(100f, width(actions("One"), 2f, 1f), 0f)
        assertEquals(200f, width(actions("Two"), 2f, 1f), 0f)
        assertEquals(300f, width(actions("One"), 3f, 1f), 0f)
        assertEquals(400f, width(actions("One"), 2f, 1.3f), 0f)
        assertEquals(100f, width(actions("One"), 2f, 1f), 0f)
        assertEquals(4, measured)
    }

    @Test
    fun callbacksDoNotRemeasureButTrailingChecksAndGlyphsDo() {
        val cache = MenuLabelWidthCache()
        val original = actions("Option")
        val callbackOnly = original.map { it.copy(onClick = {}) }
        val selected = original.map { it.copy(selected = true) }
        val subtitle = selected.map { it.copy(subtitle = "Description") }
        val glyph = subtitle.map { it.copy(glyph = "✦") }
        var measured = 0
        for (menu in listOf(original, callbackOnly, selected, subtitle, glyph)) {
            cache.width(menu, 2f, 1f) { (++measured * 100).toFloat() }
        }
        assertEquals(4, measured)
    }

    @Test
    fun lruBoundEvictsOldLabelsAndRetainsRecentlyUsedEntry() {
        val cache = MenuLabelWidthCache(capacity = 2)
        var calls = 0
        fun read(label: String) = cache.width(actions(label), 1f, 1f) {
            (++calls).toFloat()
        }

        assertEquals(1f, read("A"), 0f)
        assertEquals(2f, read("B"), 0f)
        assertEquals(1f, read("A"), 0f)
        assertEquals(3f, read("C"), 0f) // evicts B, not A
        assertEquals(2, cache.size)
        assertEquals(1f, read("A"), 0f)
        assertEquals(4f, read("B"), 0f)
        assertEquals(2, cache.size)
        cache.clear()
        assertEquals(0, cache.size)
        assertEquals(5f, read("A"), 0f)
    }

    @Test
    fun vectorOnlyGlyphReservesLeadingSpaceAndInvalidatesWidth() {
        val cache = MenuLabelWidthCache()
        val plain = GlassMenuAction("Option", "", onClick = {})
        val vector = plain.copy(vectorGlyph = GlassGlyph(emptyList()))
        assertEquals(false, menuHasLeadingGlyph(plain))
        assertEquals(true, menuHasLeadingGlyph(vector))
        assertEquals(100f, cache.width(listOf(plain), 1f, 1f) { 100f }, 0f)
        assertEquals(132f, cache.width(listOf(vector), 1f, 1f) { 132f }, 0f)
        assertEquals(100f, cache.width(listOf(plain), 1f, 1f) { error("cached") }, 0f)
    }

    @Test
    fun cacheCannotBeUnboundedOrEmpty() {
        assertThrows(IllegalArgumentException::class.java) {
            MenuLabelWidthCache(capacity = 0)
        }
        val cache = MenuLabelWidthCache(capacity = 3)
        repeat(300) { i ->
            cache.width(actions("Item $i"), 2f, 1f) { 200f }
        }
        assertEquals(3, cache.size)
    }
}
