package dev.liquidglass

import java.io.File
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/** FlClash popup.dart::_buildLevel passes ColorScheme.surface to _dim. */
class MenuParentDimParityTest {
    @Test
    fun parentScrimMatchesFlutterDistanceCurveIncludingOvershoot() {
        assertEquals(0f, menuRecedingDim(0f), 0f)
        assertEquals(.1f, menuRecedingDim(.5f), .00001f)
        assertEquals(.2f, menuRecedingDim(1f), .00001f)
        assertEquals(.3f, menuRecedingDim(1.5f), .00001f)
        assertEquals(.4f, menuRecedingDim(2f), .00001f)
        assertEquals(.4f, menuRecedingDim(3f), .00001f)
        assertEquals(0f, menuRecedingDim(-.2f), .00001f)
    }

    @Test
    fun darkParentUsesDarkPageSurfaceNotLighterFloatingCard() {
        val dark = menuRecedingSurfaceColor(true)
        val light = menuRecedingSurfaceColor(false)
        assertTrue("Dark surface is near tone 6, not dark-card tone 15",
            dark.red < .10f && dark.green < .10f && dark.blue < .11f)
        assertTrue("Light surface is near tone 98, rather than pure white",
            light.red in .96f..1f && light.green in .96f..1f &&
                light.blue in .96f..1f)
    }

    @Test
    fun rootAndNestedParentsDrawSameSurfaceWithSameDistanceAlpha() {
        val root = File("src/main/java/dev/liquidglass/GlassMenu.kt").readText()
        val child = File("src/main/java/dev/liquidglass/GlassSubmenu.kt").readText()
        assertTrue(root.contains("menuRecedingSurfaceColor(dark, glassColors(dark).surface)"))
        assertTrue(root.contains(".copy(alpha = menuRecedingDim(nestedProgress))"))
        assertTrue(child.contains("menuRecedingSurfaceColor(dark, glassColors(dark).surface)"))
        assertTrue(child.contains(".copy(alpha = menuRecedingDim(receding))"))
        assertFalse(root.contains("Color(0xFF24252B)"))
        assertFalse(child.contains("Color(0xFF24252B)"))
    }
}
