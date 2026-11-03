package dev.liquidglass

import kotlinx.coroutines.Job

/**
 * Owns only the spring jobs for dynamically pushed menu levels.
 *
 * Animatable remains keyed by level as before. When the whole popup closes,
 * removing those Animatable objects from a snapshot list is not sufficient:
 * their independent menuScope coroutines keep animating off-screen until
 * their spring reaches its tolerance. Cancel the orphan work explicitly.
 *
 * A header press may reverse only the TOP spring. Replacing that job first
 * cancels the previous fold/unfold writer before launching its replacement.
 */
internal class NestedMenuMotionJobs {
    // Keep fold handoff with its already captured owner. Avoid introducing
    // another captured field into the very large GlassMorphMenu Compose
    // content lambda: D8/ART rejects the resulting DEX on Android 16.
    val foldPressHandoff = MenuFoldPressHandoff()
    private val jobs = ArrayList<Job>()

    val size: Int get() = jobs.size

    fun push(job: Job) {
        jobs.add(job)
    }

    fun replaceLast(start: () -> Job) {
        check(jobs.isNotEmpty()) { "No nested animation to reverse" }
        val index = jobs.lastIndex
        jobs[index].cancel()
        jobs[index] = start()
    }

    /** Called after the last fold has completed and its level is removed. */
    fun popCompleted() {
        check(jobs.isNotEmpty()) { "No nested animation to remove" }
        jobs.removeAt(jobs.lastIndex)
    }

    fun cancelAll() {
        jobs.forEach { it.cancel() }
        jobs.clear()
    }
}
