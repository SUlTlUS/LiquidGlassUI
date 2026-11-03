package dev.liquidglass

import kotlinx.coroutines.Job
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test

class NestedMenuMotionJobsTest {
    @Test
    fun reversingTheHeaderCancelsOldSpringBeforeStartingNewOne() {
        val owned = NestedMenuMotionJobs()
        val fold = Job()
        val reopen = Job()
        owned.push(fold)
        assertEquals(1, owned.size)
        var canceledBeforeNewStart = false
        owned.replaceLast {
            canceledBeforeNewStart = fold.isCancelled
            reopen
        }
        assertTrue(canceledBeforeNewStart)
        assertTrue(fold.isCancelled)
        assertTrue(reopen.isActive)
        assertEquals(1, owned.size)
        owned.cancelAll()
        assertTrue(reopen.isCancelled)
        assertEquals(0, owned.size)
    }

    @Test
    fun dismissingOrReopeningRootCancelsAllOrphanChildSprings() {
        val owned = NestedMenuMotionJobs()
        val children = List(6) { Job() }
        children.forEach(owned::push)
        assertEquals(6, owned.size)
        owned.cancelAll()
        assertEquals(0, owned.size)
        children.forEach { assertTrue(it.isCancelled) }
        val newSession = Job()
        owned.push(newSession)
        assertTrue(newSession.isActive)
        owned.cancelAll()
        assertTrue(newSession.isCancelled)
    }

    @Test
    fun completedFoldDiscardsOnlyTheLastChildJob() {
        val owned = NestedMenuMotionJobs()
        val parent = Job()
        val folded = Job()
        owned.push(parent)
        owned.push(folded)
        folded.complete()
        owned.popCompleted()
        assertEquals(1, owned.size)
        assertFalse(parent.isCancelled)
        assertFalse(folded.isCancelled)
        owned.cancelAll()
        assertTrue(parent.isCancelled)
    }

    @Test
    fun invalidStackOperationsFailInsteadOfCorruptingOtherLevels() {
        val empty = NestedMenuMotionJobs()
        assertThrows(IllegalStateException::class.java) {
            empty.replaceLast { Job() }
        }
        assertThrows(IllegalStateException::class.java) {
            empty.popCompleted()
        }
        assertEquals(0, empty.size)
    }
}
