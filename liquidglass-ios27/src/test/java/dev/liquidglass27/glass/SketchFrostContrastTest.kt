package dev.liquidglass27.glass

import java.io.File
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/** Prevents accidental reintroduction of an independently composited glass. */
class SketchFrostContrastTest {
    private fun code(relative: String): String {
        val file = File(relative)
        return (if (file.isFile) file else File("liquidglass-ios27", relative)).readText()
    }

    @Test
    fun backdropIsStillSourcedFromTheSketchModule() {
        val pane = code("src/main/java/dev/liquidglass27/glass/SketchGlassBridge.kt")
        assertTrue(pane.contains("com.zeaze.tianyinwallpaper.backdrop.drawBackdrop"))
        assertTrue(pane.contains(".drawBackdrop("))
        assertTrue(pane.contains("backdrop = backdrop"))
        assertTrue(pane.contains("shadow = null"))
        assertTrue(pane.contains("highlight = null"))
    }

    @Test
    fun independentFlClashMaterialShadersAreNeverInvoked() {
        val pane = code("src/main/java/dev/liquidglass27/glass/SketchGlassBridge.kt")
        assertFalse(pane.contains("shaders/sketch_contrast_tint.agsl"))
        assertFalse(pane.contains("shaders/sketch_goo_lens.agsl"))
        assertFalse(pane.contains("flClashFrost("))
        assertFalse(pane.contains("flClashContrastTint("))
        assertFalse(pane.contains("gooAwareSketchLens("))
    }

    @Test
    fun sourceStyleTintIsAppliedOnceNotTwice() {
        val pane = code("src/main/java/dev/liquidglass27/glass/SketchGlassBridge.kt")
        assertTrue(pane.contains("if (style.tint.alpha > 0f) drawRect(style.tint)"))
        assertFalse(pane.contains("BlendMode.Hue"))
    }
}
