package dev.liquidglass27.glass

/**
 * Source ordering for nested Sketch backdrops. The source for the next pane
 * includes only background and ALREADY PAINTED popup depths. In particular,
 * the active level's own recording is never an input (a RenderThread cycle).
 *
 * [nestedIndex] is 0 for the third-level popup, 1 for the fourth, etc.
 * When [first] is null, this produces the source of the second-level popup.
 */
internal fun <T : Any> precedingSketchLayers(
    background: T,
    root: T,
    first: T?,
    nested: List<T>,
    nestedIndex: Int,
): List<T> {
    require(nestedIndex in 0..nested.size)
    return buildList {
        add(background)
        add(root)
        if (first != null) add(first)
        addAll(nested.take(nestedIndex))
    }
}
