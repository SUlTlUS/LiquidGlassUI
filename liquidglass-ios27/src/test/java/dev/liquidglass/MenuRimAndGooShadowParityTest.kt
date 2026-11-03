package dev.liquidglass

import java.io.File
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Original popup.dart keeps the root menu rim while its children unfold,
 * but never draws a rim on _RenderPopupMorph._paintReturn.
 * _paintShadows uses the entire Goo contour and an even-odd exterior clip.
 */
class MenuRimAndGooShadowParityTest {
    private val menu by lazy {
        File("src/main/java/dev/liquidglass/GlassMenu.kt").readText()
    }

    @Test
    fun rootRimDoesNotFadeJustBecauseAChildOpens() {
        // The source _buildLevel does NOT attenuate rim with level distance.
        assertEquals(1f, menuRootRimStrength(1f, 1f, false), 0f)
        assertEquals(.7f, menuRootRimStrength(.7f, 1f, false), 0f)
        assertEquals(.4f, menuRootRimStrength(.8f, .5f, false), .00001f)
        assertEquals(0f, menuRootRimStrength(1f, 1f, true), 0f)
        assertEquals(0f, menuRootRimStrength(.4f, .7f, true), 0f)
        assertEquals(0f, menuRootRimStrength(-.2f, 1f, false), 0f)
    }

    @Test
    fun rootRimUsesMergeProgressButNotSubmenuProgress() {
        val rimStart = menu.substringAfter("GlassMenuRim(")
            .substringBefore("cornerPower = rootPower")
        assertTrue(rimStart.contains("menuRootRimStrength("))
        assertTrue(rimStart.contains("returningToSource = routeState.returning && returnIntoSource"))
        assertFalse(rimStart.contains("nestedProgress"))
    }

    @Test
    fun checkedRowsAndReturnHeaderUseSourceSubheadlineWeight() {
        val child = File(
            "src/main/java/dev/liquidglass/GlassSubmenu.kt",
        ).readText()
        val itemContent = File(
            "src/main/java/dev/liquidglass/MenuItemContent.kt",
        ).readText()
        assertTrue(menu.contains("MenuItemContent(action, dark)"))
        assertTrue(child.contains("MenuItemContent(owner, dark, headerProgress = settled)"))
        assertTrue(child.contains("MenuItemContent(owner, dark, headerProgress = 1f)"))
        assertTrue(itemContent.contains("fontWeight = FontWeight.Normal"))
        assertFalse(itemContent.contains("FontWeight.SemiBold"))
    }

    @Test
    fun rootShadowUsesFullGooDuringMorphAndExteriorOnly() {
        val shadowSource = File(
            "src/main/java/dev/liquidglass/MenuCardShadow.kt",
        ).readText()
        val rootShadow = menu.substringAfter("val rootShadowOutline =")
            .substringBefore("// One AGSL distance field")
        assertTrue(rootShadow.contains("gooPairOutline("))
        assertTrue(rootShadow.contains("frame.blend"))
        assertTrue(rootShadow.contains("clipInterior = rootPlatterActive"))
        assertTrue(shadowSource.contains("Path.FillType.EVEN_ODD"))
        assertTrue(shadowSource.contains("native.clipPath("))
        assertTrue(shadowSource.contains("native.restoreToCount(save)"))
    }
}
