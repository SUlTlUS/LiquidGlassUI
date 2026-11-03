package dev.liquidglass

import java.io.File
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Prevents the 2026-11-01 Goo regression: the lens and the FlClash rim
 * selected different normal fields when an animated union was star-shaped.
 */
class GooOpticsParityTest {
    @Test
    fun gooAlwaysUsesSmoothMinimumNotRadialProfile() {
        assertFalse(mayUseRadialProfileForHighlight(true, false, 2))
        assertFalse(mayUseRadialProfileForHighlight(true, false, 6))
        assertFalse(mayUseRadialProfileForHighlight(true, true, 0))
        assertFalse(mayUseRadialProfileForHighlight(false, false, 0))
        assertTrue(mayUseRadialProfileForHighlight(true, false, 0))
    }

    private fun shaderText(filename: String): String {
        val appDir = File("src/main/assets/shaders")
            .takeIf { it.isDirectory } ?: File("liquidglass-ios27/src/main/assets/shaders")
        val asset = File(appDir, filename)
        assertTrue("Shader source unavailable: ${asset.absolutePath}", asset.isFile)
        return asset.readText()
    }

    @Test
    fun bothGooShadersPrioritizeSmoothMinimumBeforeOptionalProfile() {
        val highlight = shaderText("flclash_control_highlight.agsl")
        val lens = shaderText("sketch_goo_lens.agsl")
        val controlEntry = highlight.substringAfter("void rimGeometry(")
        val lensEntry = lens.substringAfter("void gooGeometry(")

        val morph = controlEntry.indexOf("if (uMorphEnabled != 0)")
        val row = controlEntry.indexOf("if (uCount > 0)")
        val profile = controlEntry.indexOf("if (uProfileEnabled != 0)")
        assertTrue("Goo morph must take priority over radial outline", morph in 0 until profile)
        assertTrue("Goo row must take priority over radial outline", row in 0 until profile)

        // The shared smooth-min polynomial and their guarded normal blend
        // must be present in BOTH shaders; the original 64-ray profile is
        // only for non-Goo outline highlight.
        assertTrue(controlEntry.contains("distance = smoothMinimum(da, db, k)"))
        assertTrue(lensEntry.contains("distance = smoothMinimum(da, db, k)"))
        assertTrue(controlEntry.contains("float2 mixedNormal = mix(nextNormal, n, h)"))
        assertTrue(lensEntry.contains("float2 blended = mix(nextNormal, n, h)"))
        assertFalse(lensEntry.contains("uProfileEnabled"))
    }

    @Test
    fun sketchDoesNotReplaceFlClashHighlight() {
        val bridge = File("src/main/java/dev/liquidglass/SketchGlassBridge.kt")
            .takeIf { it.isFile } ?: File("liquidglass-ios27/src/main/java/dev/liquidglass/SketchGlassBridge.kt")
        val source = bridge.readText()
        assertTrue(source.contains("highlight = null"))
        assertTrue(source.contains("rememberFlClashControlHighlight"))
    }
}
