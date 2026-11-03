package dev.liquidglass27.glass

import androidx.compose.ui.graphics.Color
import org.junit.Assert.assertEquals
import org.junit.Test

class GlassHighlightStyleTest {
    @Test
    fun presetsRetainFlClashSideHairlineFallback() {
        // LiquidGlassStyle._hairlineSide is hairlineSide ?? hairline.
        assertEquals(.35f, GlassStyle.pill(false).hairlineSide, 0f)
        assertEquals(0f, GlassStyle.pill(true).hairlineSide, 0f)
        assertEquals(.42f, GlassStyle.lens(false).hairlineSide, 0f)
        assertEquals(.42f, GlassStyle.lens(true).hairlineSide, 0f)
        assertEquals(.75f, GlassStyle.regular(false).hairlineSide, 0f)
        assertEquals(.75f, GlassStyle.regular(true).hairlineSide, 0f)
    }

    @Test
    fun explicitZeroSideAndMaterializationPreserveTheirMeaning() {
        val uniform = GlassStyle(tint = Color.Transparent, hairline = .4f)
        val topOnly = GlassStyle(tint = Color.Transparent, hairline = .4f, hairlineSide = 0f)
        assertEquals(.2f, uniform.materialize(.5f).hairlineSide, 0f)
        assertEquals(.2f, topOnly.materialize(.5f).hairline, 0f)
        assertEquals(0f, topOnly.materialize(.5f).hairlineSide, 0f)
    }
}
