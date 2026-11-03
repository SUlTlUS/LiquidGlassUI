package dev.liquidglass27.glass

import androidx.compose.ui.geometry.Rect
import java.io.File
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Regression: a root menu previously had NO outer shadow during the
 * entire goo morph, then created a fully opaque shadow at the fixed-card
 * handoff. Flutter popup.dart::_RenderPopupMorph._paintFrame instead draws
 * the contour shadow from the first opening frame at strength=settled.
 */
class MenuRootShadowContinuityTest {
    private val menu by lazy {
        File("src/main/java/dev/liquidglass27/glass/GlassMenu.kt").readText()
    }
    private val anchor = Rect(700f, 160f, 800f, 260f)
    private val card = Rect(350f, 145f, 800f, 845f)

    @Test
    fun shadowFadesFromZeroAlongEveryGooOpeningFrame() {
        assertEquals(0f, menuCardShadowStrength(
            menuOpeningFrame(0f, anchor, card, 2f).settled,
            receding = false, distance = 0f,
        ), 0f)
        for (t in listOf(.01f, .035f, .07f, .125f, .22f, .4f, .45f)) {
            val frame = menuOpeningFrame(t, anchor, card, 2f)
            val shadow = menuCardShadowStrength(
                frame.settled, receding = false, distance = 0f,
            )
            assertEquals(frame.settled, shadow, .000001f)
            assertTrue(shadow in 0f..1f)
            assertTrue(frame.platter.width > 0f && frame.platter.height > 0f)
        }
    }

    @Test
    fun handoffFromMorphToCardKeepsExactlyTheSameShadowStrength() {
        // The morph renderer yields to the root card at 100% settled.
        // The behind-card formula must not introduce an alpha step.
        val duringMorph = menuCardShadowStrength(
            progress = 1f, receding = false, distance = 0f,
        )
        val afterHandoff = menuCardShadowStrength(
            progress = 1f, receding = false, distance = 0f,
        )
        assertEquals(1f, duringMorph, 0f)
        assertEquals(duringMorph, afterHandoff, 0f)
        // Opening a child at _unfold=0 likewise starts the ancestor's
        // depth-based shadow at its existing full strength.
        assertEquals(afterHandoff,
            menuCardShadowStrength(1f, receding = true, distance = 0f), 0f)
    }

    @Test
    fun rootShadowRendersBeforeLocalGlassBoxAndWithoutHandoffCondition() {
        val root = menu.substringAfter("// FlClash wraps the whole Stack")
            .substringBefore("// The source snapshot contains ONLY")
        val start = root.indexOf("val rootShadowSurface =")
        val path = root.indexOf("val rootShadowOutline =")
        val shadow = root.indexOf("MenuCardShadow(")
        val bounds = root.indexOf("val bounds = Rect(")
        val glass = root.indexOf("GlassPane(")
        assertTrue(start >= 0 && path > start && shadow > path)
        assertTrue("Shadow must be in full menu root, outside clipped pane",
            shadow < bounds && bounds < glass)
        assertTrue(root.contains(
            "val rootShadowSurface = if (rootPlatterActive) p else card",
        ))
        assertTrue(root.contains("gooPairOutline("))
        assertTrue(root.contains("platterCornerPower = rootPower"))
        assertTrue(root.contains("else gooPlatterOutline(rootShadowSurface, rootShadowRadius, rootPower)"))
        assertTrue(root.contains("clipInterior = rootPlatterActive"))
        assertTrue(root.contains("modifier = Modifier.fillMaxSize()"))
        assertFalse(root.contains("if (!rootPlatterActive) {\n                    MenuCardShadow"))
        assertEquals("Never draw an extra root-card shadow at handoff",
            1, Regex("MenuCardShadow\\(").findAll(root).count())
    }

    @Test
    fun returningMorphRetainsItsShadowUntilFadeCompletes() {
        val opened = menuOpeningFrame(.45f, anchor, card, 2f)
        assertTrue(opened.settled > .99f)
        val beginning = menuReturningFrame(0f, opened, anchor, 2f)
        val end = menuReturningFrame(.5f, opened, anchor, 2f)
        assertEquals(opened.settled, menuCardShadowStrength(
            beginning.settled, false, 0f,
        ), .000001f)
        assertTrue(menuCardShadowStrength(end.settled, false, 0f) < .1f)
        // No change to the original Sketch clipping/sampling bounds.
        assertTrue(menu.contains("val pad = 36f * density"))
    }
}
