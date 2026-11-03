package dev.liquidglass27.glass

import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FloatSpringSpec
import kotlin.math.PI

/** dialog.dart's 275ms critically damped spring sampled over a 404ms route. */
internal object FlClashAlertEasing : Easing {
    const val durationMillis = 404
    private const val durationNanos = 404_000_000L
    private val spring = FloatSpringSpec(
        dampingRatio = 1f,
        stiffness = (4.0 * PI * PI / (.275 * .275)).toFloat(),
        visibilityThreshold = .0000001f,
    )
    private val residual = 1f - spring.getValueFromNanos(durationNanos, 0f, 1f, 0f)
    override fun transform(fraction: Float): Float =
        spring.getValueFromNanos((fraction * durationNanos).toLong(), 0f, 1f, 0f) + residual * fraction
}

internal fun alertEntryScale(opacity: Float): Float = 1.2f - .2f * opacity
internal fun alertScrimAlpha(dark: Boolean): Float = if (dark) .48f else .2f
