package com.lladlam.melox.ui.player

import org.junit.Assert.*
import org.junit.Test

class MeloXPlaybackUiStateTest {
    private val queue = listOf(
        MeloXQueueEntry(0, "a", "a", "A", "Artist A", null),
        MeloXQueueEntry(1, "b", "b", "B", "Artist B", null),
    )
    @Test fun fieldsFollowCurrentQueueEntryAndCallbacksDispatch() {
        var play = 0; var next = 0; var previous = 0
        val state = MeloXPlaybackUiState(queue, 0, false, 2, { play++ }, { next++ }, { previous++ })
        assertTrue(state.hasMedia); assertTrue(state.hasNext)
        assertEquals("A", state.title); assertEquals("Artist A", state.artist)
        state.togglePlayPause(); state.next(); state.nextFromMiniPlayer(); state.previousFromMiniPlayer()
        assertEquals(1, play); assertEquals(2, next); assertEquals(1, previous)
    }
    @Test fun disabledNextDoesNotDispatchButSwipeWrapDoes() {
        var next = 0
        val state = MeloXPlaybackUiState(queue, 1, false, 0, {}, { next++ }, {})
        assertFalse(state.hasNext); state.next(); assertEquals(0, next)
        state.nextFromMiniPlayer(); assertEquals(1, next)
    }
    @Test fun emptyQueueHasNoMedia() {
        assertFalse(MeloXPlaybackUiState(emptyList(), 0, false, 0, {}, {}, {}).hasMedia)
    }
    @Test(expected = IllegalArgumentException::class) fun rejectsInvalidQueuePosition() {
        MeloXPlaybackUiState(queue, 2, false, 0, {}, {}, {})
    }
}
