package dev.liquidglass

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import kotlin.math.max
import kotlin.math.min

/**
 * The pressed ShapeDecoration in FlClash popup.dart::_buildRow belongs to
 * the CONTENT of PopupMenuCard. That card's grow/width/position morph takes
 * the pressed header with it, from the source row to the opened submenu.
 *
 * Keep the painted mask's geometry on EXACTLY the same path as the header
 * content, in BOTH directions. No progress >= .85/.94 visibility threshold:
 * the parent opener is hidden while the new child is already drawn at grow=0.
 *
 * Coordinates are menu-root pixels (before ElasticPress's global matrix).
 */
internal fun menuPressedHeaderMaskBounds(
    parent: Rect,
    sourceRow: Rect,
    childTarget: Rect,
    progress: Float,
    insetPx: Float,
    headerHeightPx: Float,
): Rect {
    val settled = progress.coerceIn(0f, 1f)
    val widthGrowth = (settled / .30f).coerceIn(0f, 1f)
    val contentWidth = parent.width + (childTarget.width - parent.width) * widthGrowth
    val contentRight = sourceRow.right + insetPx +
        (childTarget.right - sourceRow.right - insetPx) * widthGrowth
    val headerTop = glassSubmenuHeaderTopPx(
        sourceRow.top, childTarget.top, progress, insetPx,
    )
    return Rect(
        contentRight - contentWidth + insetPx,
        headerTop,
        contentRight - insetPx,
        headerTop + headerHeightPx,
    )
}

/**
 * Match FlClash's _itemRadius=AppCorner.md (16dp) and AppShape.all rather
 * than Android's circular 10dp RoundedCornerShape. The same Flutter fitted
 * RSuperellipse is already used for the glass card's exterior clip.
 */
internal object MenuPressedRowSuperellipse : Shape {
    override fun createOutline(
        size: Size, layoutDirection: LayoutDirection, density: Density,
    ): Outline = Outline.Generic(
        gooPlatterOutline(
            Rect(0f, 0f, size.width, size.height), 16f * density.density,
        ).asComposePath(),
    )
}

/**
 * The header content grows horizontally in the first 30% of unfolding,
 * while the glass platter grows for the whole spring. Its row fill may
 * therefore extend beyond the card, especially during FOLD. Draw the
 * VISIBLE pressed fill with a path entirely inside the current platter
 * bounds, using the same Flutter superellipse corner fit as the card.
 * The text/layout still follow the original early-width animation.
 */
internal fun menuPressedHeaderVisibleBounds(row: Rect, card: Rect): Rect {
    val left = max(row.left, card.left)
    val top = max(row.top, card.top)
    val right = min(row.right, card.right)
    val bottom = min(row.bottom, card.bottom)
    return if (right > left && bottom > top) Rect(left, top, right, bottom)
    else Rect.Zero
}

/**
 * Flutter popup.dart::_buildRow builds its held ShapeDecoration with the
 * CONSTANT _itemRadius (AppCorner.md = 16dp). Only PopupMenuCard._radius
 * interpolates from itemRadius to cardRadius as _grow animates.
 *
 * The visible header bounds may be clipped to the changing card to prevent
 * a square/cut-off corner, but that MUST NEVER feed back into the row's
 * corner radius. The only cap is the geometry's half-short-side, as required
 * by Flutter RSuperellipse itself. No menu progress or card radius input.
 */
internal fun menuPressedHeaderVisibleRadius(
    visible: Rect, itemRadiusPx: Float,
): Float = if (visible == Rect.Zero || !itemRadiusPx.isFinite()) 0f else
    itemRadiusPx.coerceAtLeast(0f)
        .coerceAtMost(min(visible.width, visible.height) * .5f)

/** _pressed follows the header during fold even when it stops accepting taps. */
internal fun menuPressedMaskHoverForLevel(
    interactive: Boolean,
    gestureHoveredRow: Int,
): Int = if (interactive || gestureHoveredRow == -2) gestureHoveredRow else -1
