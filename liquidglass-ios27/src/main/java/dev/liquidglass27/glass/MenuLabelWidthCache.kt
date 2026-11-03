package dev.liquidglass27.glass

/**
 * Width of menu labels is stable across animation frames, pointer moves,
 * scrolling and submenu spring transitions. Creating an Android Paint and
 * measuring every label during EVERY recomposition is unnecessary work.
 *
 * Scoped to one GlassMorphMenu composition and capped to a small number
 * of submenus. Keys snapshot label text instead of action callback identity:
 * changing a title, density or fontScale invalidates only that measurement.
 *
 * No backdrop, bitmap, GlassStyle, Shader or Sketch state enters this cache.
 */
internal class MenuLabelWidthCache(capacity: Int = 24) {
    init { require(capacity > 0) }

    private data class Key(
        val labels: List<List<String>>,
        val density: Float,
        val fontScale: Float,
    )

    private val entries = object : LinkedHashMap<Key, Float>(capacity, .75f, true) {
        override fun removeEldestEntry(
            eldest: MutableMap.MutableEntry<Key, Float>?,
        ): Boolean = size > capacity
    }

    val size: Int get() = entries.size

    fun width(
        actions: List<GlassMenuAction>,
        density: Float,
        fontScale: Float,
        measure: () -> Float,
    ): Float {
        // The label list is immutable for this lookup. A changed selection or
        // onClick lambda with identical titles uses the same measured width.
        val key = Key(actions.map {
            listOf(
                it.text, it.subtitle.orEmpty(), menuLeadingGlyph(it).orEmpty(),
                menuHasLeadingGlyph(it).toString(),
                menuHasEndMark(it).toString(),
            )
        }, density, fontScale)
        val hit = entries[key]
        if (hit != null) return hit
        return measure().also { entries[key] = it }
    }

    fun clear() = entries.clear()
}
