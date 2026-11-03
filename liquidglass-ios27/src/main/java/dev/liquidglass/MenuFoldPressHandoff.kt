package dev.liquidglass

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * popup.dart::_pop completion:
 *
 *   final step = _path.removeLast();
 *   if (_pressed == _headerIndex) _pressed = step.index;
 *
 * The Flutter recognizer retains _landed after this reassignment. Until
 * the finger actually moves to a DIFFERENT index, the parent opener is the
 * SAME press, not a new row with another 400 ms dwell timer.
 *
 * Compose renders the folded hierarchy in a separate coroutine from the
 * long-lived pointerInput recognizer, so report the completed pop with an
 * increasing generation. Neither the active pointer nor the gesture layer
 * is recreated when a card folds.
 */
internal data class MenuFoldPressReturn(
    val sequence: Long,
    val childDepth: Int,
    val parentDepth: Int,
    val openingRow: Int,
)

internal class MenuFoldPressHandoff {
    var latest by mutableStateOf<MenuFoldPressReturn?>(null)
        private set

    fun completed(childDepth: Int, openingRow: Int) {
        if (childDepth < 1 || openingRow < 0) return
        latest = MenuFoldPressReturn(
            sequence = (latest?.sequence ?: 0L) + 1L,
            childDepth = childDepth,
            parentDepth = childDepth - 1,
            openingRow = openingRow,
        )
    }

    fun sequence(): Long = latest?.sequence ?: 0L
}
