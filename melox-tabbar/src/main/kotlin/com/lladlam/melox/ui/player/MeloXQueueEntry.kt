package com.lladlam.melox.ui.player

enum class MeloXQueueOrigin { Base, Manual }

data class MeloXQueueEntry(
    val index: Int,
    val entryId: String,
    val mediaId: String,
    val title: String,
    val artist: String,
    val artworkUrl: String?,
    val durationMs: Long = 0L,
    val origin: MeloXQueueOrigin = MeloXQueueOrigin.Base,
)

