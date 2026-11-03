package dev.liquidglass27.glass

import androidx.compose.animation.core.TargetBasedAnimation
import androidx.compose.animation.core.VectorConverter
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import java.io.File
import kotlin.math.abs
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Requested extension: when a menu list's *inertial* scroll collides with
 * its clamp, bend the complete ElasticPress stack, NOT the list contents.
 * FlClash's original ClampingScrollPhysics never overscrolls its rows.
 */
class MenuEdgeBounceTest {
    private val panel = Rect(40f, 80f, 360f, 600f)

    @Test
    fun momentumFromEitherEdgeMovesCardInTheFingerDirection() {
        // Scroll upward (positive offset) hits bottom => menu stretches up.
        val bottomImpact = menuEdgeImpactVelocityPx(2400f, panel)
        assertTrue(bottomImpact < 0f)
        // Scroll downward (negative offset) hits top => menu stretches down.
        val topImpact = menuEdgeImpactVelocityPx(-2400f, panel)
        assertTrue(topImpact > 0f)
        assertEquals(-bottomImpact, topImpact, 0f)
    }

    @Test
    fun impulseSharesWholeStackElasticLimitWithoutHugeOutliers() {
        val budget = panel.width * (7f / 32f)
        assertEquals(0f, menuEdgeImpactVelocityPx(0f, panel), 0f)
        assertEquals(-180f, menuEdgeImpactVelocityPx(1000f, panel), .0001f)
        assertEquals(budget * 14f, menuEdgeImpactVelocityPx(-1e7f, panel), .001f)
        assertTrue(abs(menuEdgeImpactVelocityPx(1e7f, panel)) <= budget * 14f)
        assertEquals(0f, menuEdgeImpactVelocityPx(Float.NaN, panel), 0f)
        assertEquals(0f, menuEdgeImpactVelocityPx(Float.POSITIVE_INFINITY, panel), 0f)
        assertEquals(0f, menuEdgeImpactVelocityPx(3000f, Rect.Zero), 0f)
    }

    @Test
    fun springStartsAtRestWithVelocityInsteadOfPositionJump() {
        val impact = menuEdgeImpactVelocityPx(-2800f, panel)
        val spring = TargetBasedAnimation(
            animationSpec = menuPressSpring(),
            typeConverter = Float.VectorConverter,
            initialValue = 0f,
            targetValue = 0f,
            initialVelocity = impact,
        )
        assertEquals(0f, spring.getValueFromNanos(0L), 0f)
        assertTrue(spring.getValueFromNanos(16_666_667L) > 0f)
        assertTrue(spring.getValueFromNanos(66_666_667L) > 0f)
        assertTrue(spring.durationNanos > 80_000_000L)
        // It remains a restrained whole-stack pull, not a screen jump.
        val samples = (0..20).map {
            abs(spring.getValueFromNanos(it * 16_666_667L))
        }
        assertTrue(samples.max() < panel.width * (7f / 32f))
    }

    @Test
    fun existingMenuElasticTransformAppliesToEntireCardAndInverseHit() {
        val pull = menuEdgeImpactVelocityPx(-3000f, panel) / 8f
        val base = menuElasticTransform(panel, 0f, Offset.Zero, 16f)
        val stretched = menuElasticTransform(panel, 0f, Offset(0f, pull), 16f)
        assertEquals(1f, base.scaleY, 0f)
        assertTrue(stretched.scaleY > base.scaleY)
        val before = Offset(230f, 225f)
        val painted = Offset(
            stretched.center.x + (before.x - stretched.center.x) *
                stretched.scaleX + stretched.pull.x,
            stretched.center.y + (before.y - stretched.center.y) *
                stretched.scaleY + stretched.pull.y,
        )
        assertEquals(before.y, stretched.inverse(painted).y, .0001f)
    }

    @Test
    fun wholeMenuBounceIsWiredOnlyToInertialBoundaryAndSharedGraphicsLayer() {
        val source = File(
            "src/main/java/dev/liquidglass27/glass/GlassMenu.kt",
        ).readText()
        assertTrue(source.contains("val menuMorphMomentum = remember { MenuMorphMomentum() }"))
        assertTrue(source.contains("val hitBoundary = when (state)"))
        assertTrue(source.contains("menuEdgeImpactVelocityPx("))
        assertTrue(source.contains("menuMorphMomentum.edgeImpact("))
        assertTrue(source.contains("Offset(menuPullX.value, menuPullY.value)"))
        assertTrue(source.contains("SideEffect { menuBouncePanel = menuPressPanel() }"))
        assertTrue(source.contains("if (!expanded) {\n            // Revoke pending impacts"))
        assertTrue(source.contains("menuMorphMomentum.settle(menuPullReleaseSpec)"))
        assertFalse(source.contains("menuEdgeBounceY.animateTo("))
        // Do not translate a root/nested LazyColumn separately or insert an
        // extra overscroll drawing layer into Sketch's glass capture.
        assertFalse(source.contains("overscrollEffect = "))
    }
}
