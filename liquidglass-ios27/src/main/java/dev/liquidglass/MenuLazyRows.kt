package dev.liquidglass

import kotlin.math.floor
import kotlin.math.roundToInt

/**
 * Mirror FlClash popup.dart:_buildsLazily: ONLY an explicitly capped root
 * menu whose rows all have equal heights (no child menus, subtitle or section)
 * is built by ListView.builder / Compose LazyColumn.
 */
internal fun menuBuildsLazily(
    maxVisibleItems: Int?,
    items: List<GlassMenuAction>,
    isRoot: Boolean = true,
): Boolean = maxVisibleItems != null && isRoot && items.all { item ->
    item.subActions.isEmpty() && item.subtitle == null && item.section == null
}

/** Pixels consumed above the list's first visible item. */
internal fun menuLazyScrollOffsetPx(
    firstVisibleItemIndex: Int,
    firstVisibleItemScrollOffset: Int,
    rowExtentPx: Float,
): Float = (firstVisibleItemIndex.toFloat() * rowExtentPx +
    firstVisibleItemScrollOffset).coerceAtLeast(0f)

/**
 * FlClash _buildLazyContent ScrollController.initialScrollOffset:
 * keep an integer number of rows above checked, preserving the
 * 0.5-row scroll affordance at the foot.
 */
internal fun menuLazyCheckedInitialScrollPx(
    checkedIndex: Int,
    itemCount: Int,
    rowExtentPx: Float,
    visibleItems: Int,
    visibleHeightPx: Float,
): Int {
    if (checkedIndex !in 0 until itemCount ||
        visibleItems <= 0 ||
        !rowExtentPx.isFinite() || rowExtentPx <= 0f ||
        !visibleHeightPx.isFinite() || visibleHeightPx <= 0f) return 0
    val desired = (checkedIndex - visibleItems / 2) * rowExtentPx
    val maxOffset = (itemCount * rowExtentPx - visibleHeightPx).coerceAtLeast(0f)
    return desired.coerceIn(0f, maxOffset).roundToInt()
}

internal data class MenuLazyItemPosition(val index: Int, val scrollOffset: Int)

/**
 * LazyListState.scrollToItem takes an ITEM INDEX and a pixel offset inside
 * it, unlike Flutter's ScrollController which takes absolute pixels.
 */
internal fun menuLazyItemPosition(
    absoluteOffsetPx: Int,
    itemCount: Int,
    rowExtentPx: Float,
): MenuLazyItemPosition {
    if (itemCount <= 0 || rowExtentPx <= 0f || !rowExtentPx.isFinite())
        return MenuLazyItemPosition(0, 0)
    val position = absoluteOffsetPx.coerceAtLeast(0).toFloat()
    val index = floor(position / rowExtentPx).toInt().coerceIn(0, itemCount - 1)
    val inside = (position - index * rowExtentPx).roundToInt().coerceAtLeast(0)
    return MenuLazyItemPosition(index, inside)
}
