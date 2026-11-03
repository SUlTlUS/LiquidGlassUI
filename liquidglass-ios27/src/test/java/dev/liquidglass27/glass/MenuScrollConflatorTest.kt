package dev.liquidglass27.glass

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class MenuScrollConflatorTest {
    @Test
    fun flingAlwaysFollowsTheFinalMoveWithoutBeingMergedAway() = runBlocking {
        val queue = MenuScrollConflator()
        repeat(50) { queue.offer(0, -3f) }
        queue.offerFling(0, 2300f)
        queue.offer(0, 5f)
        assertEquals(
            listOf(
                MenuScrollDelta(0, -150f),
                MenuScrollDelta(0, 0f, 2300f),
                MenuScrollDelta(0, 5f),
            ),
            queue.takeBatch().events,
        )
    }

    @Test
    fun newPointerAndHierarchyChangesDiscardPendingMomentum() = runBlocking {
        val queue = MenuScrollConflator()
        queue.offer(0, 25f)
        queue.offerFling(0, -3000f)
        val inFlight = queue.takeBatch()
        queue.clear()
        assertFalse(queue.isCurrent(inFlight))
        queue.offer(0, 10f)
        assertEquals(listOf(MenuScrollDelta(0, 10f)), queue.takeBatch().events)
    }

    @Test
    fun flingVelocityMatchesFingerDirectionAndAndroidThresholds() {
        assertEquals(-3000f, menuScrollFlingVelocity(3000f, 50f, 8000f))
        assertEquals(3000f, menuScrollFlingVelocity(-3000f, 50f, 8000f))
        assertEquals(-8000f, menuScrollFlingVelocity(12000f, 50f, 8000f))
        assertEquals(null, menuScrollFlingVelocity(40f, 50f, 8000f))
        assertEquals(null, menuScrollFlingVelocity(Float.NaN, 50f, 8000f))
        assertEquals(null, menuScrollFlingVelocity(200f, 300f, 100f))
    }

    @Test
    fun consecutiveFingerMovesNeverLoseTotalDisplacement() = runBlocking {
        val queue = MenuScrollConflator()
        for (index in 0 until 120) queue.offer(1, .75f)
        assertEquals(
            listOf(MenuScrollDelta(1, 90f)),
            queue.takeBatch().events,
        )
    }

    @Test
    fun crossingLevelsPreservesEachDepthAndOrdering() = runBlocking {
        val queue = MenuScrollConflator()
        queue.offer(0, 6f)
        queue.offer(0, -1f)
        queue.offer(1, 12f)
        queue.offer(0, 3f)
        assertEquals(
            listOf(
                MenuScrollDelta(0, 5f),
                MenuScrollDelta(1, 12f),
                MenuScrollDelta(0, 3f),
            ),
            queue.takeBatch().events,
        )
    }

    @Test
    fun dismissedMenuDropsQueuedMovesAndBadDeltas() = runBlocking {
        val queue = MenuScrollConflator()
        queue.offer(2, 40f)
        queue.clear()
        queue.offer(2, Float.NaN)
        queue.offer(2, Float.POSITIVE_INFINITY)
        queue.offer(-1, 20f)
        queue.offer(2, 7f)
        assertEquals(listOf(MenuScrollDelta(2, 7f)), queue.takeBatch().events)
    }

    @Test
    fun laterScrollEventsAreStillDeliveredAfterAnEarlierBatch() = runBlocking {
        val queue = MenuScrollConflator()
        queue.offer(0, 1f)
        assertEquals(listOf(MenuScrollDelta(0, 1f)), queue.takeBatch().events)
        queue.offer(0, 2f)
        assertEquals(listOf(MenuScrollDelta(0, 2f)), queue.takeBatch().events)
    }

    @Test
    fun hierarchyChangesInvalidateBatchesAlreadyTakenByConsumer() = runBlocking {
        val queue = MenuScrollConflator()
        queue.offer(1, 15f)
        queue.offer(2, 25f)
        val inFlight = queue.takeBatch()
        assertTrue(queue.isCurrent(inFlight))
        assertEquals(2, inFlight.events.size)

        // This happens when a nested menu folds or the first owner changes.
        queue.clear()
        assertFalse(queue.isCurrent(inFlight))
        queue.offer(1, -5f)
        val newBatch = queue.takeBatch()
        assertTrue(queue.isCurrent(newBatch))
        assertEquals(listOf(MenuScrollDelta(1, -5f)), newBatch.events)
        assertTrue(newBatch.generation != inFlight.generation)
    }

    @Test
    fun highFrequencyDragsMergeAndDoNotChangeTakenBatches() = runBlocking {
        val queue = MenuScrollConflator()
        repeat(10_000) { queue.offer(1, .25f) }
        val frozen = queue.takeBatch()
        assertEquals(listOf(MenuScrollDelta(1, 2500f)), frozen.events)
        queue.offer(1, 10f)
        queue.offer(1, -10f)
        val canceled = queue.takeBatch()
        assertTrue(canceled.events.isEmpty())
        assertEquals(listOf(MenuScrollDelta(1, 2500f)), frozen.events)
        queue.offer(2, 6f)
        queue.offer(2, -2f)
        assertEquals(listOf(MenuScrollDelta(2, 4f)), queue.takeBatch().events)
    }

    @Test
    fun oldBatchStopsBetweenTwoDepthEventsAfterHierarchyChanges() = runBlocking {
        val queue = MenuScrollConflator()
        queue.offer(0, 4f)
        queue.offer(1, 8f)
        val inFlight = queue.takeBatch()
        val seen = mutableListOf<MenuScrollDelta>()
        for (move in inFlight.events) {
            if (!queue.isCurrent(inFlight)) break
            seen += move
            if (move.depth == 0) queue.clear()
        }
        assertEquals(listOf(MenuScrollDelta(0, 4f)), seen)
    }
}
