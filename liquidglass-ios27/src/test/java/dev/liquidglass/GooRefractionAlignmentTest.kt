package dev.liquidglass

import java.io.File
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Contract regression: Sketch owns glass/material, FlClash owns shape and
 * highlight. Do not reactivate the experimental FlClash optical shaders.
 */
class GooRefractionAlignmentTest {
    private fun code(relative: String): String {
        val a = File(relative)
        return (if (a.isFile) a else File("liquidglass-ios27", relative)).readText()
    }

    @Test
    fun paneUsesSketchNativeEffectsOnly() {
        val bridge = code("src/main/java/dev/liquidglass/SketchGlassBridge.kt")
        val surface = code("src/main/java/dev/liquidglass/SketchMenuRootSurface.kt")
        val effects = code("src/main/java/dev/liquidglass/SketchMaterialCache.kt")
        assertTrue(bridge.contains("materialCache.apply(this, menu, actualRadiusPx)"))
        assertTrue(surface.contains("materials.apply(this, g.menu, g.radius)"))
        assertTrue(effects.contains("blur("))
        assertTrue(effects.contains("lens("))
        assertTrue(effects.contains("vibrancy()"))
        assertTrue(effects.contains("colorControls("))
        assertFalse(effects.contains("gooAwareSketchLens("))
        assertFalse(effects.contains("flClashFrost("))
        assertFalse(effects.contains("flClashContrastTint("))
        assertFalse(effects.contains("exposureAdjustment("))
    }

    @Test
    fun gooShapeAndFlClashRimRemainIndependent() {
        val bridge = code("src/main/java/dev/liquidglass/SketchGlassBridge.kt")
        val outline = code("src/main/java/dev/liquidglass/GooRowOutline.kt")
        assertTrue(bridge.contains("gooPairOutline("))
        assertTrue(bridge.contains("gooRowOutline("))
        assertTrue(bridge.contains("rememberFlClashControlHighlight("))
        assertTrue(bridge.contains("highlight = null"))
        assertTrue(outline.contains("Path.Op.UNION"))
    }
}
