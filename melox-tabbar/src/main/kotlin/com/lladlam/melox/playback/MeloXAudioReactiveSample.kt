package com.lladlam.melox.playback

data class MeloXAudioReactiveSample(
    val energy: Float,
    val beat: Float,
    val downbeat: Float,
    val isPlaying: Boolean,
    /**
     * True only when a real beat analysis produced these values. Consumers
     * that must not react to the synthetic playback-clock fallback gate on
     * this flag instead of trusting [energy]/[beat].
     */
    val hasAnalysis: Boolean = false,
) {
    companion object {
        val Idle = MeloXAudioReactiveSample(0.18f, 0f, 0f, false, hasAnalysis = false)
    }
}
