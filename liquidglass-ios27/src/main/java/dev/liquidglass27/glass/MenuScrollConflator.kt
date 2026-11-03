package dev.liquidglass27.glass

import kotlinx.coroutines.channels.Channel
import kotlin.math.abs

/** Convert finger velocity to clamped Android scroll-coordinate momentum. */
internal fun menuScrollFlingVelocity(
    fingerVelocityY: Float,
    minimumPxPerSecond: Float,
    maximumPxPerSecond: Float,
): Float? {
    if (!fingerVelocityY.isFinite() || !minimumPxPerSecond.isFinite() ||
        !maximumPxPerSecond.isFinite() || minimumPxPerSecond < 0f ||
        maximumPxPerSecond <= 0f || maximumPxPerSecond < minimumPxPerSecond) return null
    val clamped = fingerVelocityY.coerceIn(
        -maximumPxPerSecond, maximumPxPerSecond,
    )
    return if (abs(clamped) >= minimumPxPerSecond && clamped != 0f) -clamped
    else null
}

internal data class MenuScrollDelta(
    val depth: Int,
    val dyPx: Float,
    /** Scroll-coordinate velocity; null denotes an ordinary finger delta. */
    val flingVelocityPxPerSecond: Float? = null,
)

/** A batch is valid only for the menu hierarchy that produced its events. */
internal data class MenuScrollBatch(
    val generation: Long,
    val events: List<MenuScrollDelta>,
)

/**
 * A single serial scroll consumer, rather than a new scrollBy coroutine
 * for every PointerMove. ScrollState's MutatorMutex would cancel older
 * scrollBy calls when new coroutines raced to acquire it, losing deltas.
 *
 * Adjacent moves for the same menu depth are summed. The wakeup channel is
 * conflated but the actual displacement is NOT lost. Both producers and the
 * consumer run on Compose's UI dispatcher.
 */
internal class MenuScrollConflator {
    private val wakeup = Channel<Unit>(Channel.CONFLATED)
    // Reuse the active mutable run during a 60/120Hz finger drag. The old
    // implementation allocated a new MenuScrollDelta and deque node on
    // EVERY pointer move, even when all moves belonged to the same level.
    private class PendingMove(
        val depth: Int,
        var dyPx: Float,
        val flingVelocityPxPerSecond: Float? = null,
    )
    private val pending = ArrayDeque<PendingMove>()
    private var generation = 0L

    fun offer(depth: Int, dyPx: Float) {
        if (depth < 0 || !dyPx.isFinite() || dyPx == 0f) return
        val last = pending.lastOrNull()
        if (last != null && last.depth == depth &&
            last.flingVelocityPxPerSecond == null) {
            val total = last.dyPx + dyPx
            if (!total.isFinite()) return
            if (total == 0f) pending.removeLast() else last.dyPx = total
        } else {
            pending.addLast(PendingMove(depth, dyPx))
        }
        wakeup.trySend(Unit)
    }

    /** Enqueued AFTER the final drag delta, never conflated with moves. */
    fun offerFling(depth: Int, velocityPxPerSecond: Float) {
        if (depth < 0 || !velocityPxPerSecond.isFinite() ||
            velocityPxPerSecond == 0f) return
        pending.addLast(PendingMove(
            depth, 0f, flingVelocityPxPerSecond = velocityPxPerSecond,
        ))
        wakeup.trySend(Unit)
    }

    suspend fun takeBatch(): MenuScrollBatch {
        wakeup.receive()
        // Copy into immutable values only ONCE per consumer batch; no
        // mutable pending object can escape and be changed by a later drag.
        val result = MenuScrollBatch(
            generation, pending.map {
                MenuScrollDelta(it.depth, it.dyPx, it.flingVelocityPxPerSecond)
            },
        )
        pending.clear()
        return result
    }

    fun isCurrent(batch: MenuScrollBatch): Boolean =
        batch.generation == generation

    fun clear() {
        // Invalidates not only queued moves, but also batches already taken
        // by a consumer that may be suspended between scrollBy calls.
        generation++
        pending.clear()
        while (wakeup.tryReceive().isSuccess) { /* clear stale wakeups */ }
    }
}
