package dev.liquidglass27.glass

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import java.io.File
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MenuElasticPressTest {
    private fun menuSource() = File("src/main/java/dev/liquidglass27/glass/GlassMenu.kt")
        .readText()
    private fun gestureSource() = File("src/main/java/dev/liquidglass27/glass/GlassMenuGestures.kt")
        .readText()

    @Test
    fun topCardShadowFadesContinuouslyThroughFoldingSpring() {
        for (step in 0..100) {
            val progress = step / 100f
            assertEquals(
                progress, menuCardShadowStrength(
                    progress = progress, receding = false, distance = 0f,
                ), .000001f,
            )
        }
        assertEquals(0f, menuCardShadowStrength(-.2f, false, 0f), 0f)
        assertEquals(1f, menuCardShadowStrength(1.2f, false, 0f), 0f)
        // The covered parent uses depth decay, never the opening ramp.
        for (distance in listOf(0f, .3f, 1f, 2f, 5f)) {
            assertEquals(menuRecedingShadow(distance),
                menuCardShadowStrength(.02f, true, distance), .000001f)
        }
        assertTrue(menuSource().contains("menuCardShadowStrength("))
        assertTrue(File("src/main/java/dev/liquidglass27/glass/GlassSubmenu.kt")
            .readText().contains("strength = menuCardShadowStrength("))
    }

    @Test
    fun tappingCardGrowsWholeMenuUpToSixteenDpAcrossLongestSide() {
        val panel = Rect(50f, 200f, 310f, 600f)
        val noPress = menuElasticTransform(panel, 0f, Offset.Zero, 32f)
        val pressed = menuElasticTransform(panel, 1f, Offset.Zero, 32f)
        assertEquals(1f, noPress.scaleX, 0f)
        assertEquals(1f, noPress.scaleY, 0f)
        assertEquals(1.08f, pressed.scaleX, .000001f)
        assertEquals(1.08f, pressed.scaleY, .000001f)
        assertEquals(panel.center, pressed.center)
        // Flutter maxGrowth=16 dp means the growth is inversely related
        // to the LONGEST side of the entire popup, not row height.
        val wide = Rect(0f, 0f, 900f, 60f)
        assertEquals(1f + 32f / 900f,
            menuElasticTransform(wide, 1f, Offset.Zero, 32f).scaleX, .000001f)
    }

    @Test
    fun inverseTransformKeepsFastDragAndSubmenuHeaderHitAligned() {
        val panel = Rect(40f, 150f, 410f, 670f)
        for (lift in listOf(0f, .25f, .72f, 1f, 1.12f)) {
            for (pull in listOf(Offset.Zero, Offset(5f, -13f), Offset(-25f, 40f))) {
                val t = menuElasticTransform(panel, lift, pull, 40f)
                for (original in listOf(
                    panel.topLeft, panel.center, panel.bottomRight,
                    Offset(230f, 200f), Offset(390f, 620f),
                )) {
                    val painted = Offset(
                        t.center.x + (original.x - t.center.x) * t.scaleX + pull.x,
                        t.center.y + (original.y - t.center.y) * t.scaleY + pull.y,
                    )
                    val hit = t.inverse(painted)
                    assertEquals(original.x, hit.x, .0001f)
                    assertEquals(original.y, hit.y, .0001f)
                }
            }
        }
    }

    @Test
    fun pullStartsOnlyOutsideTheMenuAndHasElasticLimit() {
        val panel = Rect(20f, 50f, 340f, 500f)
        assertEquals(Offset.Zero, menuElasticPull(panel, panel.center))
        assertEquals(Offset.Zero, menuElasticPull(panel, Offset(20f, 50f)))
        val moved = menuElasticPull(panel, Offset(650f, -400f))
        val limit = panel.width * (7f / 32f)
        assertTrue(moved.x > 0f && moved.x < limit)
        assertTrue(moved.y < 0f && moved.y > -limit)
    }

    @Test
    fun quickTapReversesOnFirstNinetySevenPointFivePercentTick() {
        assertEquals(.975f, FLCLASH_MENU_LIFT_SNAP, 0f)
        assertTrue(!shouldSettleMenuPress(releasing = false, lift = 1.12f))
        assertTrue(!shouldSettleMenuPress(releasing = true, lift = .9749f))
        assertTrue(shouldSettleMenuPress(releasing = true, lift = .975f))
        assertTrue(shouldSettleMenuPress(releasing = true, lift = .989f))
        assertTrue(shouldSettleMenuPress(releasing = true, lift = 1.08f))
        // At a 60/120 Hz frame boundary the release must happen at the
        // first sample above _liftSnap, NOT after a full oscillation/settle.
        val risingSamples = listOf(.05f, .25f, .63f, .83f, .967f,
            .982f, 1.043f, 1.091f, .998f, 1f)
        val snapIndex = risingSamples.indexOfFirst {
            shouldSettleMenuPress(releasing = true, lift = it)
        }
        assertEquals(5, snapIndex)
        assertTrue(snapIndex < risingSamples.lastIndex)
        // Holding longer than the threshold returns immediately on Up.
        assertTrue(shouldSettleMenuPress(releasing = true, lift = 1f))
    }

    @Test
    fun releasingPreservesOriginalRisingVelocityAndInterruptsAtSnap() {
        val menu = menuSource()
        val release = menu.substringAfter("fun endMenuPress() {")
            .substringBefore("DisposableEffect(menuLift)")
        assertTrue(release.contains("val originalRise = menuPressJob"))
        assertTrue(release.contains("snapshotFlow {"))
        assertTrue(release.contains("menuLift.value to menuLift.velocity"))
        assertTrue(release.contains("shouldSettleMenuPress(releasing = true, lift = lift)"))
        assertTrue(release.contains("originalRise?.cancelAndJoin()"))
        assertTrue(release.contains("initialVelocity = liftAtSnap.second"))
        // Never await completion of the full 1.0 spring, or restart that
        // spring when the finger is released.
        assertTrue(!release.contains("menuLift.animateTo(1f"))
        assertTrue(!release.contains("menuPressJob?.cancel()"))
    }

    @Test
    fun quickRepressInheritsVelocityBeforeCancellingRelease() {
        val press = menuSource().substringAfter("fun startMenuPress() {")
            .substringBefore("fun moveMenuPress(")
        val capture = press.indexOf("val inheritedVelocity = menuLift.velocity")
        val cancel = press.indexOf("menuPressJob?.cancel()")
        assertTrue(capture >= 0 && cancel > capture)
        assertTrue(press.contains("initialVelocity = inheritedVelocity"))
    }

    @Test
    fun bothPointerChannelsUseInverseAndReleaseElasticMotion() {
        val source = gestureSource()
        assertTrue(source.contains("pointToMenu: (Offset) -> Offset"))
        assertTrue(source.contains("fun hitAt(position: Offset)"))
        assertTrue(source.contains("val current = hitAt(position)"))
        assertTrue(source.contains("val downTarget = hitAt(down.position)"))
        assertTrue(source.contains("start(downTarget)"))
        assertTrue(source.contains("val released = hitAt(last)"))
        assertTrue(source.contains("if (elasticHeld) finishPress()"))
        val menu = menuSource()
        assertTrue(menu.contains("currentMenuPressTransform().inverse(point)"))
        assertTrue(Regex("""menuLift\.animateTo\(\s*1f, menuPressSpring\(\)""").containsMatchIn(menu))
        assertTrue(Regex("""menuLift\.animateTo\(\s*0f, menuPressSpring\(\)""").containsMatchIn(menu))
    }
}
