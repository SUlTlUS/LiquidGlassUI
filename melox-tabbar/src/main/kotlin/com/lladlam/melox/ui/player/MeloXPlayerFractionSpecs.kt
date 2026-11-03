package com.lladlam.melox.ui.player
import androidx.compose.animation.core.*

internal fun playerAutomaticFractionSpec() = tween<Float>(
    durationMillis = meloXPlayerTransitionDurationMillis,
    easing = LinearEasing,
)

internal fun playerGestureSettleSpec() = spring<Float>(
    dampingRatio = 1.0f,
    stiffness = 420f,
    visibilityThreshold = 0.001f,
)

