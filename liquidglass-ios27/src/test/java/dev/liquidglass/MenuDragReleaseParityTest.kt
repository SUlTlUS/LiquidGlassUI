package dev.liquidglass

import androidx.compose.animation.core.TargetBasedAnimation
import androidx.compose.animation.core.VectorConverter
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import java.io.File
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.exp
import kotlin.math.sin
import kotlin.math.sqrt
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/** FlClash/lib/ui/navigation_dock.dart _ElasticPressState and _Spring. */
class MenuDragReleaseParityTest {
    private fun originalRubberBand(overshoot: Float, limit: Float): Float {
        val k = abs(overshoot) * .55f / limit
        return limit * (1f - 1f / (k + 1f)) *
            if (overshoot > 0f) 1f else if (overshoot < 0f) -1f else 0f
    }

    @Test
    fun draggedMenuUsesOriginalReciprocalRubberBandNotTanh() {
        val limit = 320f * (7f / 32f)
        for (drag in listOf(-500f, -120f, -40f, 0f, 40f, 120f, 500f)) {
            assertEquals(
                originalRubberBand(drag, limit),
                menuElasticRubberBand(drag, limit),
                .000001f,
            )
        }
        assertEquals(0f, menuElasticRubberBand(Float.NaN, limit), 0f)
        assertEquals(0f, menuElasticRubberBand(100f, 0f), 0f)
        assertTrue(abs(menuElasticRubberBand(500f, limit)) < limit)

        // The old tanh implementation pulled more than 1.7x as far for
        // ordinary drags, making the menu violently spring back.
        val original = menuElasticRubberBand(100f, limit)
        val previous = GlassMotion.rubberBand(100f, limit)
        assertTrue(previous > original * 1.7f)

        val panel = Rect(0f, 0f, 320f, 500f)
        val pull = menuElasticPull(panel, Offset(150f, -100f))
        assertEquals(originalRubberBand(-100f, limit), pull.y, .000001f)
    }

    @Test
    fun whileFingerHeldUsesOriginal120msCriticallyDampedTracker() {
        val spring = menuTrackSpring()
        assertEquals((4.0 * PI * PI / (.12 * .12)).toFloat(), spring.stiffness, .0001f)
        assertEquals(1f, spring.dampingRatio, 0f)
        assertEquals(.001f, spring.visibilityThreshold ?: Float.NaN, 0f)
    }

    @Test
    fun releaseUsesOriginalUnderdamped311Ratio0593() {
        val spring = menuPressSpring()
        assertEquals(311f, spring.stiffness, 0f)
        assertEquals(.593f, spring.dampingRatio, 0f)
        assertEquals(.001f, spring.visibilityThreshold ?: Float.NaN, 0f)
        // Original return crosses equilibrium once and bounces back,
        // unlike the previous .8/350 spec shared with finger tracking.
        val motion = TargetBasedAnimation(
            animationSpec = spring,
            typeConverter = Float.VectorConverter,
            initialValue = 40f,
            targetValue = 0f,
        )
        val frames = (0L..75L).map { frame ->
            motion.getValueFromNanos(frame * 16_666_667L)
        }
        assertEquals(40f, frames.first(), .000001f)
        assertTrue(frames.any { it < -.5f })
        assertTrue(abs(frames.last()) < .02f)
    }

    @Test
    fun releasePreservesFingerTrackingVelocityAndExactPhysicalTrajectory() {
        // Flutter _Spring.springTo initializes its new SpringSimulation with
        // its current position AND nonzero velocity from _trackSpring.
        val from = 34f
        val velocity = -210f
        val spring = TargetBasedAnimation(
            animationSpec = menuPressSpring(),
            typeConverter = Float.VectorConverter,
            initialValue = from,
            targetValue = 0f,
            initialVelocity = velocity,
        )
        val omega = sqrt(311.0)
        val damping = .593
        val dampedOmega = omega * sqrt(1.0 - damping * damping)
        for (ms in listOf(0L, 16L, 50L, 90L, 150L, 235L, 320L)) {
            val t = ms / 1000.0
            val predicted = exp(-damping * omega * t) * (
                from * cos(dampedOmega * t) +
                    (velocity + damping * omega * from) / dampedOmega *
                    sin(dampedOmega * t)
            )
            assertEquals(predicted, spring.getValueFromNanos(ms * 1_000_000L).toDouble(), .012)
        }
    }

    @Test
    fun composeSwitchesPhysicsOnlyWhenPressReleasesWithoutResettingValue() {
        val source = File(
            "src/main/java/dev/liquidglass/GlassMenu.kt",
        ).readText()
        assertTrue(source.contains("val menuPullX = remember { MenuRetargetSpring() }"))
        assertTrue(source.contains("val menuPullY = menuMorphMomentum.pullY"))
        assertTrue(source.contains("driveMenuRetargetSprings(menuPullX, menuPullY, menuHover)"))
        assertTrue(source.contains("val pull = menuElasticPull(panel, point)"))
        assertTrue(source.contains("menuPullX.springTo(pull.x, menuPullTrackSpec)"))
        assertTrue(source.contains("menuPullY.springTo(pull.y, menuPullTrackSpec)"))
        assertTrue(source.contains("menuPullX.springTo(0f, menuPullReleaseSpec)"))
        assertTrue(source.contains("menuPullY.springTo(0f, menuPullReleaseSpec)"))
        assertFalse(source.contains(
            "animationSpec = spring(dampingRatio = .8f, stiffness = 350f)",
        ))
        // Leave other Sketch/background/hero popup springs untouched.
        assertTrue(source.contains("val menuLift = remember { Animatable(0f) }"))
    }
}
