package dev.liquidglass27.glass

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import kotlin.math.max
import kotlin.math.min

/** Flutter popup.dart: PopupPlacement. */
enum class GlassMenuPlacement {
    OverAnchorEnd,
    OverAnchorStart,
    BelowPoint,
}

/** Compose counterpart of _PopupLayoutDelegate.getPositionForChild. */
internal fun menuPopupPosition(
    anchor: Rect,
    cardWidth: Float,
    cardHeight: Float,
    screenWidth: Float,
    screenHeight: Float,
    density: Float,
    placement: GlassMenuPlacement,
    avoid: Rect? = null,
    safeBounds: Rect = Rect(0f, 0f, screenWidth, screenHeight),
    pin: GlassPopupPin? = null,
): Offset {
    val margin = 16f * density
    val overlap = 8f * density
    val maxY = safeBounds.bottom - margin - cardHeight
    val key = PopupPlacementKey(anchor, safeBounds, screenWidth, screenHeight, density, placement, avoid)
    val pinned = pin?.topRightFor(key)
    val x = pinned?.x?.minus(cardWidth) ?: when (placement) {
        GlassMenuPlacement.OverAnchorEnd -> anchor.right - cardWidth
        GlassMenuPlacement.OverAnchorStart -> anchor.left
        GlassMenuPlacement.BelowPoint -> anchor.left
    }
    val y = pinned?.y ?: when (placement) {
        GlassMenuPlacement.OverAnchorEnd,
        GlassMenuPlacement.OverAnchorStart -> anchor.top - overlap
        GlassMenuPlacement.BelowPoint -> {
            val above = avoid?.top?.minus(overlap + cardHeight)
            val below = avoid?.bottom?.plus(overlap)
            when {
                above != null && above >= safeBounds.top + margin -> above
                below != null && below <= maxY -> below
                anchor.bottom > maxY && anchor.top - cardHeight >= safeBounds.top + margin ->
                    anchor.top - cardHeight
                else -> anchor.bottom
            }
        }
    }
    // Overlapping a top-edge anchor is allowed, as in Flutter: don't force
    // the popup below the 16dp edge when its source itself starts above it.
    val minY = if (placement == GlassMenuPlacement.BelowPoint) safeBounds.top + margin
        else max(safeBounds.top, min(safeBounds.top + margin, y))
    val position = Offset(
        x.coerceIn(safeBounds.left + margin, max(safeBounds.left + margin, safeBounds.right - margin - cardWidth)),
        y.coerceIn(minY, max(minY, maxY)),
    )
    if (pinned == null) pin?.hold(key, position + Offset(cardWidth, 0f))
    return position
}

internal data class PopupPlacementKey(val anchor: Rect, val safe: Rect, val width: Float,
    val height: Float, val density: Float, val placement: GlassMenuPlacement, val avoid: Rect?)

/** Hold top/right across content-size changes; window/anchor changes invalidate it. */
internal class GlassPopupPin {
    private var key: PopupPlacementKey? = null
    private var topRight = Offset.Zero
    fun topRightFor(next: PopupPlacementKey): Offset? = topRight.takeIf { key == next }
    fun hold(next: PopupPlacementKey, point: Offset) { key = next; topRight = point }
}
