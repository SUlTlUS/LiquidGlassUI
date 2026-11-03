package dev.liquidglass

import android.graphics.Path
import androidx.compose.ui.geometry.Rect

/**
 * Bounded slot-local memoization for a fixed Goo row (up to six buttons).
 * A moved/pressed pill invalidates only its own contour and the two adjacent
 * necks. Values are never mutated after being placed in a slot.
 *
 * This is deliberately NOT a shared/global Path cache: Android Path is mutable,
 * and cross-pane reuse would couple the lifetime of unrelated glass surfaces.
 */
internal class GooGeometrySlots<K, V>(capacity: Int) {
    private data class Entry<K, V>(val key: K, val value: V)
    private val entries = MutableList<Entry<K, V>?>(capacity) { null }

    fun get(index: Int, key: K, build: () -> V): V {
        require(index in entries.indices)
        val existing = entries[index]
        if (existing != null && existing.key == key) return existing.value
        return build().also { entries[index] = Entry(key, it) }
    }

    fun clear() {
        for (index in entries.indices) entries[index] = null
    }
}

internal data class GooNeckGeometryKey(
    val left: Rect,
    val right: Rect,
    val blendPx: Float,
    val density: Float,
    val leftPill: GlassPill? = null,
    val rightPill: GlassPill? = null,
)

/**
 * One cache per composed Goo row. 6 pills + 5 neck paths are the hard upper
 * bound even when an animation produces thousands of distinct frames.
 */
internal class GooRowPathCache {
    private val pills = GooGeometrySlots<Any, Path>(6)
    private val necks = GooGeometrySlots<GooNeckGeometryKey, Path?>(5)

    fun pill(index: Int, bounds: Any, builder: () -> Path): Path =
        pills.get(index, bounds, builder)

    fun neck(index: Int, key: GooNeckGeometryKey, builder: () -> Path?): Path? =
        necks.get(index, key, builder)

    fun clear() {
        pills.clear()
        necks.clear()
    }
}
