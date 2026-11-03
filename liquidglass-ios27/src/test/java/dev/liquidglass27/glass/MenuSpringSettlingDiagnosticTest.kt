package dev.liquidglass27.glass

import androidx.compose.animation.core.TargetBasedAnimation
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.spring
import java.io.File
import kotlin.math.abs
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Source: C:/flutter/packages/flutter/lib/src/physics/tolerance.dart
 * Tolerance.defaultTolerance uses 0.001 distance/velocity.
 * Compose Float spring's default visibilityThreshold is 0.01, which ends
 * the rebound at its >1 overshoot peak and cuts off the bottom edge.
 */
class MenuSpringSettlingDiagnosticTest {
    private fun motion(threshold: Float) = TargetBasedAnimation(
        animationSpec = spring<Float>(
            stiffness = FlClashMenuSprings.stiffnessFor(420),
            dampingRatio = FlClashMenuSprings.dampingRatioFor(.15f),
            visibilityThreshold = threshold,
        ),
        typeConverter = Float.VectorConverter,
        initialValue = 0f,
        targetValue = 1f,
    )

    @Test
    fun matchesFlutterDefaultPositionToleranceInsteadOfComposeDefault() {
        assertEquals(.001f, FlClashMenuSprings.FLUTTER_POSITION_TOLERANCE, 0f)
        assertEquals(
            FlClashMenuSprings.FLUTTER_POSITION_TOLERANCE,
            FlClashMenuSprings.unfold().visibilityThreshold ?: Float.NaN,
            0f,
        )
        assertEquals(
            FlClashMenuSprings.FLUTTER_POSITION_TOLERANCE,
            FlClashMenuSprings.fold().visibilityThreshold ?: Float.NaN,
            0f,
        )
    }

    @Test
    fun originalSpringMotionPreservedBeforeOvershootButTailNoLongerSnapsEarly() {
        val oldCompose = motion(.01f)
        val flutterTolerance = motion(FlClashMenuSprings.FLUTTER_POSITION_TOLERANCE)
        assertTrue(flutterTolerance.durationNanos > oldCompose.durationNanos + 100_000_000L)

        // Stiffness and damping remain unchanged, so the 0..400ms opening
        // has the exact SAME physical trajectory; only the end rule changes.
        for (ms in listOf(0L, 50L, 120L, 200L, 300L, 390L)) {
            val at = ms * 1_000_000L
            assertEquals(
                oldCompose.getValueFromNanos(at),
                flutterTolerance.getValueFromNanos(at),
                .0000001f,
            )
        }

        // At default Compose end its last frame sits near the 0.6% peak,
        // then jumps back. With Flutter tolerance the terminal frame is
        // an order of magnitude closer to final, hence no harsh bottom snap.
        val frameNs = 16_666_667L
        val oldResidual = abs(1f - oldCompose.getValueFromNanos(
            (oldCompose.durationNanos - frameNs).coerceAtLeast(0),
        ))
        val newResidual = abs(1f - flutterTolerance.getValueFromNanos(
            (flutterTolerance.durationNanos - frameNs).coerceAtLeast(0),
        ))
        assertTrue(oldResidual > .005f)
        assertTrue(newResidual < .002f)
        assertTrue(newResidual < oldResidual * .25f)
    }

    @Test
    fun allChildOpeningPathsUseCorrectSpecWithoutSecondCompletionSnap() {
        val source = File(
            "src/main/java/dev/liquidglass27/glass/GlassMenu.kt",
        ).readText()
        val first = source.substringAfter("LaunchedEffect(submenuOpen, submenuIndex)")
            .substringBefore("BackHandler(enabled")
        val deeper = source.substringAfter("fun pushNested(")
            .substringBefore("fun popNested(")
        assertTrue(first.contains("1f, FlClashMenuSprings.unfold()"))
        assertTrue(deeper.contains("1f, FlClashMenuSprings.unfold()"))
        // Fold completion MUST still restore the parent controller to 1;
        // only the unfolding completion must avoid an extra snap.
        val opening = first.substringAfter("if (expanded && submenuOpen")
            .substringBefore("} else if (expanded")
        assertTrue(!opening.contains("submenuMotion.snapTo(1f)"))
        assertTrue(!deeper.contains("submenuMotion.snapTo(1f)"))
    }
}
