package dev.liquidglass

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import java.io.File
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.sqrt
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/** FlClash popup.dart Stack / navigation_dock.dart ElasticPress #1–#4. */
class MenuElasticFullParityTest {
    private fun gestureSource() = File(
        "src/main/java/dev/liquidglass/GlassMenuGestures.kt",
    ).readText()
    private fun menuSource() = File(
        "src/main/java/dev/liquidglass/GlassMenu.kt",
    ).readText()

    @Test
    fun fullCardPaddingDisabledRowsAndCoveredParentArePressable() {
        val pressable = listOf(
            TargetKind.Row, TargetKind.DisabledRow, TargetKind.Header,
            TargetKind.Back, TargetKind.Blank,
        )
        for (kind in pressable) {
            assertTrue("$kind did not lift menu", menuCanElasticPress(
                MenuTouchTarget(kind), enabled = true,
            ))
            assertFalse(menuCanElasticPress(MenuTouchTarget(kind), enabled = false))
        }
        assertFalse(menuCanElasticPress(MenuTouchTarget(TargetKind.Outside), true))
        assertTrue(gestureSource().contains(
            "var elasticHeld = !canceled && menuCanElasticPress(held.pressed, canPress)",
        ))
        assertTrue(gestureSource().contains(
            "if (!elasticHeld && menuCanElasticPress(current, canPress))",
        ))
    }

    @Test
    fun originalStackLayoutUsesRootTopRightAndMaxChildLayoutExtent() {
        val root = Rect(280f, 120f, 540f, 420f)
        val first = Rect(210f, 160f, 535f, 700f)
        val second = Rect(205f, 300f, 550f, 760f)
        // Flutter Stack(alignment: topRight) uses largest child WIDTH,
        // largest child bottom and its root top/right anchor, not the
        // union of descendant paint rects or the full-screen overlay.
        assertEquals(
            Rect(195f, 120f, 540f, 760f),
            menuFlutterStackLayoutBounds(root, listOf(first, second)),
        )
        assertEquals(root, menuFlutterStackLayoutBounds(root, emptyList()))
        val formerUnion = menuPopupStackBounds(listOf(root, first, second))
        assertTrue(formerUnion.right != root.right)
        assertTrue(menuSource().contains(
            "fun menuPressPanel(): Rect = menuFoldPressGeometry.panel(",
        ))
        assertTrue(menuSource().contains(
            "menuFlutterStackLayoutBounds(card, pressStackCards.drop(1))",
        ))
    }

    @Test
    fun stackingPressAndHoverMatchesFlutterFormula() {
        assertEquals(.4f, menuEffectiveLift(0f, .4f), .000001f)
        assertEquals(1f, menuEffectiveLift(1f, .4f), .000001f)
        assertEquals(.76f, menuEffectiveLift(.6f, .4f), .000001f)
        assertEquals(0f, menuEffectiveLift(0f, 0f), 0f)
        assertTrue(menuSource().contains(
            "menuEffectiveLift(menuLift.value, menuHover.value)",
        ))
    }

    @Test
    fun hoverHasDedicatedFlClash500ms032Spring() {
        val spring = menuHoverSpring()
        assertEquals((4.0 * PI * PI / (.5 * .5)).toFloat(),
            spring.stiffness, .00001f)
        assertEquals(.68f, spring.dampingRatio, 0f)
        assertEquals(.001f, spring.visibilityThreshold ?: Float.NaN, 0f)
        assertTrue(menuSource().contains(
            "menuHover.springTo(if (entered) .4f else 0f, menuHoverSpec)",
        ))
        assertTrue(menuSource().contains(
            "onMenuPointerHover = { inside -> updateMenuHover(inside) }",
        ))
        val gesture = gestureSource()
        assertTrue(gesture.contains("pointer.type == PointerType.Mouse"))
        assertTrue(gesture.contains("pointer.type == PointerType.Stylus"))
        assertTrue(gesture.contains("event.type == PointerEventType.Exit"))
        assertTrue(gesture.contains("reportMenuPointerHover(false)"))
    }

    @Test
    fun continuousRetargetKeepsAdvancingWhileFingerMovesEveryFrame() {
        val channel = MenuRetargetSpring()
        val spec = menuTrackSpring()
        for (frame in 1..30) {
            channel.springTo(100f + frame * 3f, spec)
            channel.advance(1f / 120f)
        }
        assertTrue(channel.value > 80f)
        assertTrue(channel.value < 190f)
        assertTrue(channel.velocity > 0f)
        // A controller restarting an animation from t=0 on every Move
        // can stay frozen here; our continuous frame driver cannot.
        assertTrue(channel.moving)
        assertTrue(menuSource().contains("driveMenuRetargetSprings("))
    }

    @Test
    fun dragReleasePreservesVelocityAndNoFirstFrameSnap() {
        val channel = MenuRetargetSpring()
        channel.springTo(100f, menuTrackSpring())
        channel.advance(1f / 120f)
        val before = channel.value
        val oldVelocity = channel.velocity
        assertTrue(before > 0f && oldVelocity > 0f)
        channel.springTo(0f, menuPressSpring())
        assertEquals(before, channel.value, 0f)
        assertEquals(oldVelocity, channel.velocity, 0f)
        channel.advance(1f / 240f)
        // The original leaves the old tracking spring with outward
        // velocity: release first continues out, then turns back.
        assertTrue(channel.value > before)
        for (step in 0..240) channel.advance(1f / 120f)
        assertEquals(0f, channel.value, .002f)
        assertFalse(channel.moving)
    }

    @Test
    fun analyticSpringIndependentOf60And120HzFrames() {
        val sixty = MenuRetargetSpring()
        val high = MenuRetargetSpring()
        sixty.springTo(85f, menuPressSpring())
        high.springTo(85f, menuPressSpring())
        repeat(36) { sixty.advance(1f / 60f) }
        repeat(72) { high.advance(1f / 120f) }
        assertEquals(sixty.value, high.value, .002f)
        assertEquals(sixty.velocity, high.velocity, .02f)
    }

    @Test
    fun noSeparatePointerSpringJobAllocatedOnEachMove() {
        val source = menuSource()
        assertTrue(source.contains("menuPullX.springTo(pull.x, menuPullTrackSpec)"))
        assertTrue(source.contains("menuPullY.springTo(pull.y, menuPullTrackSpec)"))
        assertTrue(source.contains("menuPullX.springTo(0f, menuPullReleaseSpec)"))
        assertTrue(source.contains("menuPullY.springTo(0f, menuPullReleaseSpec)"))
        val frameDriver = File(
            "src/main/java/dev/liquidglass/MenuRetargetSpring.kt",
        ).readText()
        assertTrue(frameDriver.contains("snapshotFlow { springs.any { it.moving } }"))
        assertTrue(frameDriver.contains("withFrameNanos"))
        assertFalse(frameDriver.contains("menuPullX.animateTo("))
        assertFalse(frameDriver.contains("delay("))
    }
}
