package com.lladlam.melox.ui.player

/** UI-only adapter for the original mini-player, without a MediaSession singleton.
 * Supply a new state when media/playback changes; callbacks must update the host.
 * Queue index must refer to the current item. Artwork accepts URI/URL strings.
 */
class MeloXPlaybackUiState(
    val queue: List<MeloXQueueEntry>,
    val currentIndex: Int,
    val isPlaying: Boolean,
    val repeatMode: Int = 0,
    private val onTogglePlayPause: () -> Unit,
    private val onNext: () -> Unit,
    private val onPrevious: () -> Unit,
    val positionMs: Long = 0L,
    val durationMs: Long = 180_000L,
    val volume: Float = 0.5f,
    private val onSeek: (Long) -> Unit = {},
    private val onVolumeChange: (Float) -> Unit = {},
) {
    init {
        require(queue.isEmpty() && currentIndex == 0 || currentIndex in queue.indices)
        require(repeatMode in 0..2)
    }
    private val current get() = queue.getOrNull(currentIndex)
    val hasMedia get() = current != null
    val mediaId get() = current?.mediaId
    val title get() = current?.title.orEmpty()
    val artist get() = current?.artist.orEmpty()
    val artworkUrl get() = current?.artworkUrl
    val hasNext get() = currentIndex < queue.lastIndex
    val hasPrevious get() = currentIndex > 0
    val isInTransition = false
    val incomingTitle = ""
    val incomingArtist = ""
    val incomingDurationMs = 0L
    val incomingPositionMs = 0L
    fun previous() = onPrevious()
    fun seekTo(position: Long) = onSeek(position.coerceIn(0, durationMs))
    fun changeVolume(value: Float) = onVolumeChange(value.coerceIn(0f, 1f))
    fun togglePlayPause() = onTogglePlayPause()
    fun next() { if (hasNext || repeatMode != 0) onNext() }
    fun nextFromMiniPlayer() = onNext()
    fun previousFromMiniPlayer() = onPrevious()
}
