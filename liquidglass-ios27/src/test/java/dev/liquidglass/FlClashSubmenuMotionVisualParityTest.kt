package dev.liquidglass

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

/** Flutter popup.dart::_buildLevel and navigation_dock.dart::_RenderPressTransform. */
class FlClashSubmenuMotionVisualParityTest {
    @Test
    fun flutterSubheadlineLeadingMakesMenuRowsFortyFourDpAtDefaultScale() {
        // Flutter _rowExtentOf = 12*2 padding + AppTextStyle.subheadline.leading(20).
        assertEquals(44f, menuRowHeightDp(1f), 0f)
        assertEquals(54f, menuRowHeightDp(1.5f), 0f)
        assertEquals(64f, menuRowHeightDp(2f), 0f)
    }

    @Test
    fun openingUsesOriginalSpringNotAnArtificiallyWeakerOrFasterOne() {
        val spring = FlClashMenuSprings.unfold()
        assertEquals(FlClashMenuSprings.stiffnessFor(420), spring.stiffness, .00001f)
        assertEquals(.85f, spring.dampingRatio, 0f)
    }

    @Test
    fun menuPressTransformStaysAnchoredToFinalStackDuringChildSpring() {
        val root = Rect(280f, 120f, 540f, 420f)
        val selected = Rect(288f, 170f, 532f, 214f)
        val target = Rect(190f, 160f, 540f, 620f)
        val fullStack = menuPopupStackBounds(listOf(root, target))
        assertEquals(Rect(190f, 120f, 540f, 620f), fullStack)
        val pressed = menuElasticTransform(fullStack, 1f, Offset.Zero, 16f)
        assertEquals(fullStack.center, pressed.center)
        for (progress in listOf(0f, .08f, .31f, .64f, .9f, 1f, 1.015f)) {
            val morph = glassSubmenuInterpolatedShape(selected, target, progress, 1f).panel
            // The moving card is a drawing shape, not the spring's panel.
            assertTrue(morph.width > 0f && morph.height > 0f)
            val current = menuElasticTransform(
                menuPopupStackBounds(listOf(root, target)),
                1f, Offset.Zero, 16f,
            )
            assertEquals(pressed.center, current.center)
            assertEquals(pressed.scaleX, current.scaleX, 0f)
            assertEquals(pressed.scaleY, current.scaleY, 0f)
        }
    }

    @Test
    fun stableStackScalesLessThanJustTheTinyEmergingCard() {
        val root = Rect(280f, 120f, 540f, 420f)
        val childTarget = Rect(190f, 160f, 540f, 620f)
        val emergingChild = Rect(288f, 170f, 532f, 214f)
        val whole = menuElasticTransform(
            menuPopupStackBounds(listOf(root, childTarget)), 1f, Offset.Zero, 16f)
        val wrong = menuElasticTransform(emergingChild, 1f, Offset.Zero, 16f)
        assertTrue(whole.scaleX < wrong.scaleX)
        assertTrue(whole.scaleY < wrong.scaleY)
    }

    @Test
    fun allMenuLevelsAreRegisteredUsingFinalTargetNotAnimatedPanel() {
        val source = File("src/main/java/dev/liquidglass/GlassMenu.kt").readText()
        assertTrue(source.contains("fun menuPressPanel(): Rect = menuFoldPressGeometry.panel("))
        assertTrue(source.contains("menuFlutterStackLayoutBounds(card, pressStackCards.drop(1))"))
        assertTrue(source.contains("pressStackCards.add(firstTarget)"))
        assertTrue(source.contains("pressStackCards.add(target)"))
    }
}
