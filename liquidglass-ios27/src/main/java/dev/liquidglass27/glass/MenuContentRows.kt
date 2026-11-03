package dev.liquidglass27.glass

import kotlin.math.roundToInt

/** FlClash CommonPopupMenuSection; a null title still starts a new section. */
data class GlassMenuSection(val title: String? = null)

/** Source popup.dart:_buildContent inserts a 17dp divider between sections,
 * plus an 8dp top / 2dp bottom footnote heading if the section has a title.
 * Offsets are shared by painting, scroll geometry, menu morph and hit testing.
 */
internal data class MenuContentRows(
    val itemTopsDp: List<Float>,
    val rowHeightDp: Float,
    val totalHeightDp: Float,
    /** Each subtitle adds 18dp/line to the default 44dp row. */
    val itemHeightsDp: List<Float>,
) {
    fun itemTopDp(index: Int): Float = itemTopsDp[index]
    fun itemHeightDp(index: Int): Float = itemHeightsDp[index]
    fun rowStartOffsetsPx(density: Float): List<Float> = itemTopsDp.map { it * density }
    fun rowHeightsPx(density: Float): List<Float> = itemHeightsDp.map { it * density }
}

/**
 * The normal menu uses Flutter's 44dp default row (24dp padding + 20dp
 * subheadline leading). Sections require a per-item offset rather than the
 * old index * rowHeight assumption.
 */
internal fun menuContentRows(
    items: List<GlassMenuAction>,
    fontScale: Float,
    measuredHeightsDp: List<Float>? = null,
): MenuContentRows {
    val rowHeight = menuRowHeightDp(fontScale)
    require(measuredHeightsDp == null || measuredHeightsDp.size == items.size)
    val tops = ArrayList<Float>(items.size)
    val heights = ArrayList<Float>(items.size)
    var offset = 0f
    items.forEachIndexed { index, item ->
        offset += menuSectionPrefixDp(index, item.section, fontScale)
        tops += offset
        val height = measuredHeightsDp?.get(index) ?: rowHeight
        heights += height
        offset += height
    }
    return MenuContentRows(tops, rowHeight, offset, heights)
}

internal fun menuSectionPrefixDp(
    index: Int,
    section: GlassMenuSection?,
    fontScale: Float,
): Float {
    if (section == null) return 0f
    val divider = if (index > 0) 17f else 0f
    val header = if (section.title != null) 10f + 18f * fontScale else 0f
    return divider + header
}

internal fun menuCheckedInitialScrollPx(
    checkedIndex: Int,
    rows: MenuContentRows,
    density: Float,
    visibleHeightPx: Float,
): Int {
    if (checkedIndex !in rows.itemTopsDp.indices || density <= 0f ||
        !density.isFinite() || visibleHeightPx <= 0f ||
        !visibleHeightPx.isFinite()) return 0
    val rowPx = rows.rowHeightDp * density
    val visibleRows = (visibleHeightPx / rowPx).coerceAtLeast(1f)
    val rowsAbove = minOf(7f, visibleRows).toInt() / 2
    val desired = (rows.itemTopDp(checkedIndex) - rowsAbove * rows.rowHeightDp) * density
    val maximum = (rows.totalHeightDp * density - visibleHeightPx).coerceAtLeast(0f)
    return desired.coerceIn(0f, maximum).roundToInt()
}
