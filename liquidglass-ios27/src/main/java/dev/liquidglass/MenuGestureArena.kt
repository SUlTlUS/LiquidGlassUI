package dev.liquidglass

import androidx.compose.ui.geometry.Offset
import kotlin.math.abs

/**
 * Flutter popup.dart::_RowPressRecognizer is a DEFERRED recognizer: it
 * observes a row press immediately, but accepts only on Up if a competing
 * vertical scroll recognizer has not won first.
 *
 * This small deterministic arbitration state machine mirrors that ordering
 * in Compose without claiming that Compose uses Flutter's Gesture Arena.
 * The caller consumes position changes only after the vertical drag wins.
 * Down and pending row moves are left unconsumed for competing recognizers.
 */
internal enum class MenuArenaWinner { Pending, Scroll, Rejected }

internal class MenuGestureArena(
    val down: MenuTouchTarget,
    private val initial: Offset,
    private val slop: Float,
    /** Captured once at Down; never taken from a subsequently hovered row. */
    val scrollOwner: Int?,
) {
    var winner = MenuArenaWinner.Pending
        private set
    var exceededTapSlop = false
        private set
    private var scrollEligible = true

    /**
     * After a 400ms dwell unfolds/folds a level, Flutter's original Down
     * hit path does not magically gain a recognizer for the NEW child's
     * scroll view. The old parent must not steal a carried row selection.
     */
    fun levelTransition() {
        scrollEligible = false
    }

    /** Record final Up displacement even when no intermediate Move arrived. */
    fun finishAt(point: Offset) {
        if (point != Offset.Unspecified && point.x.isFinite() && point.y.isFinite()) {
            if ((point - initial).getDistance() > slop) exceededTapSlop = true
        }
    }

    fun move(point: Offset, otherConsumed: Boolean = false): MenuArenaWinner {
        if (winner != MenuArenaWinner.Pending) return winner
        if (otherConsumed) return reject()
        if (!point.x.isFinite() || !point.y.isFinite()) return reject()
        val dx = point.x - initial.x
        val dy = point.y - initial.y
        finishAt(point)
        // Flutter VerticalDragGestureRecognizer wins by a vertical drag,
        // not a horizontal traverse across rows for gesture selection.
        if (scrollEligible && scrollOwner != null &&
            abs(dy) > slop && abs(dy) >= abs(dx)) {
            winner = MenuArenaWinner.Scroll
        }
        return winner
    }

    fun reject(): MenuArenaWinner {
        winner = MenuArenaWinner.Rejected
        return winner
    }

    /** Row only wins at Up; a scroll/rejected pointer cannot select. */
    fun mayAcceptRelease(): Boolean = winner == MenuArenaWinner.Pending

    fun mayDismissOutside(up: MenuTouchTarget): Boolean =
        mayAcceptRelease() && menuReleaseDismisses(down, up, exceededTapSlop)

    fun mayFoldParent(up: MenuTouchTarget): Boolean =
        mayAcceptRelease() && menuReleaseFoldsParent(down, up, exceededTapSlop)

    fun maySelectRow(): Boolean =
        mayAcceptRelease() && menuCanReleaseRowFrom(down)
}
