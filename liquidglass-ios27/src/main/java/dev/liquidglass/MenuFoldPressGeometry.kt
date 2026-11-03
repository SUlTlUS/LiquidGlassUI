package dev.liquidglass

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import kotlin.math.abs

/**
 * Prevents the ElasticPress transform from changing reference panels when a
 * submenu is REMOVED after its fold. The previously visible panel includes
 * the child: removing it changes Stack dimensions instantly, while a held
 * press still has nonzero lift. Recomputing growthOf(longestSide) and center
 * from the smaller parent alone introduces a visible last-frame scale pop.
 *
 * Only lock across a FOLD, not while a child unfolds or when the card rests.
 * Retain this layout basis through the remaining press/pull return spring;
 * release it only once the transform is effectively identity. We do not
 * interfere with the real card layout, shadows, click regions or Sketch.
 */
internal class MenuFoldPressGeometry {
    var pinned by mutableStateOf<Rect?>(null)
        private set

    fun panel(live: Rect): Rect = pinned ?: live

    fun pinForFold(
        lastPanel: Rect,
        pressed: Boolean,
        lift: Float,
        pull: Offset,
    ): Boolean {
        if (pinned != null || !lastPanel.isUsable()) return false
        if (!pressed && isAtRest(lift, pull)) return false
        pinned = lastPanel
        return true
    }

    fun unpinWhenResting(pressed: Boolean, lift: Float, pull: Offset): Boolean {
        if (pinned == null || pressed || !isAtRest(lift, pull)) return false
        pinned = null
        return true
    }
}

private fun Rect.isUsable(): Boolean = width > 0f && height > 0f &&
    left.isFinite() && right.isFinite() && top.isFinite() && bottom.isFinite()

private fun isAtRest(lift: Float, pull: Offset): Boolean =
    abs(lift) <= .001f && abs(pull.x) <= .001f && abs(pull.y) <= .001f
