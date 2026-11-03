package com.lladlam.melox.ui.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf

// Adapter for the original preference reads. No mutable process-wide preference.
internal val LocalMeloXFrostedGlass = staticCompositionLocalOf { false }
internal enum class MeloXLyricsRenderingQuality { Low, Balanced, High }
internal object MeloXSettingsRuntime {
    // Original defaults; service/preferences are not bundled into this UI library.
    val reduceMotion = false
    val artworkMotionEnabled = true
    val playerTransitionDurationMs = 360
    val disableAutomaticTabBarShrink = false
    val playerBackgroundIsolationEnabled = true
    val transitionUiEnabled = false // No music-service automix in the UI-only sample.
    val immersivePlaybackEnabled = false
    val lyricRenderingQuality = MeloXLyricsRenderingQuality.High
    val lyricBackgroundFrameRate = 24
    val lyricReduceMotion = false
    val flowingLightSaturation = 1f
    val flowingLightBrightness = 1f
    val flowingLightSpeed = 1f
    val showPlayerQualityTip = false // Requires the excluded music-service quality metadata.
    val frostedGlassEnabled: Boolean
        @Composable get() = LocalMeloXFrostedGlass.current
}
