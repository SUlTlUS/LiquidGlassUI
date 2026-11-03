package dev.liquidglass27.glass

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.PointerType
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.isPrimaryPressed
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalViewConfiguration
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs

/** A source control reports its pointer stream in its own local coordinates. */
enum class GlassSourcePointerPhase { Down, Move, Up, Cancel }

internal data class GlassCarriedEvent(
    val phase: GlassSourcePointerPhase,
    val position: Offset,
    val generation: Long = 0L,
)

/**
 * The opening button retains its Compose pointer hit path after the popup
 * overlays it. Buffer its events until the popup animation can accept them.
 * This recreates the source-to-menu handoff without synthesizing PointerDown
 * events or demanding the finger lift and press again.
 */
class GlassCarriedPress {
    // Preserve Down / Up / Cancel in FIFO order. An UNLIMITED channel of
    // individual Move samples left the menu highlighting *behind* a fast
    // fingertip whenever Sketch rendering consumed a slow frame.
    internal val events = Channel<GlassCarriedEvent>(Channel.UNLIMITED)
    private var generation = 0L
    private var moveQueued = false
    private var latestMove = Offset.Unspecified
    private var active = false

    fun begin(position: Offset) {
        // A quick reopening must not replay a completed popup gesture.
        generation++
        while (events.tryReceive().isSuccess) { /* discard stale gesture */ }
        moveQueued = false
        latestMove = Offset.Unspecified
        active = true
        events.trySend(GlassCarriedEvent(GlassSourcePointerPhase.Down, position, generation))
    }

    fun move(position: Offset) {
        if (!active || position == Offset.Unspecified) return
        // Coalesce ONLY Moves: at most one pending Move token. A token reads
        // the latest sampled position when the menu coroutine processes it.
        latestMove = position
        if (!moveQueued) {
            moveQueued = true
            events.trySend(GlassCarriedEvent(
                GlassSourcePointerPhase.Move, Offset.Unspecified, generation,
            ))
        }
    }

    fun release(position: Offset) {
        if (!active) return
        active = false
        events.trySend(GlassCarriedEvent(GlassSourcePointerPhase.Up, position, generation))
    }

    fun cancel() {
        if (!active) return
        active = false
        events.trySend(GlassCarriedEvent(
            GlassSourcePointerPhase.Cancel, Offset.Unspecified, generation,
        ))
    }

    /** Resolve a coalesced Move without ever dropping the final Up or Down. */
    internal fun resolve(event: GlassCarriedEvent): GlassCarriedEvent? {
        if (event.generation != generation) return null
        if (event.phase != GlassSourcePointerPhase.Move) return event
        if (!moveQueued) return null
        moveQueued = false
        return event.copy(position = latestMove)
    }
}

/**
 * Full-scene hit geometry. Parent and child menu surfaces publish their
 * physical-pixel bounds into the SAME coordinate system as the gesture layer.
 * No individual child pointerInput owns the pointer after unfolding.
 */
internal data class GlassMenuHitLevel(
    val depth: Int,
    val bounds: Rect,
    val actions: List<GlassMenuAction>,
    val rowTopPx: Float,
    val rowHeightPx: Float,
    val scrollable: Boolean,
    /** The rendered start offset of each row; gaps are section decoration. */
    val rowOffsetsPx: List<Float>? = null,
    /** Real heights for rows containing a one- or two-line subtitle. */
    val rowHeightsPx: List<Float>? = null,
    /** The pinned header stays pressable while the card unfolds/folds.
     * Ordinary rows become interactive only when their content has landed.
     */
    val rowsEnabled: Boolean = true,
    /** Fixed clipping viewport; rowTopPx itself moves with the scroll. */
    val rowViewportTopPx: Float = Float.NEGATIVE_INFINITY,
    val rowViewportBottomPx: Float = Float.POSITIVE_INFINITY,
    val headerTopPx: Float = Float.NaN,
    val headerHeightPx: Float = 0f,
    /** Matches the native graphicsLayer shrinking a receding card. */
    val paintPivot: Offset = Offset.Unspecified,
    val paintScale: Float = 1f,
)

internal enum class TargetKind { Row, DisabledRow, Header, Back, Outside, Blank }

/** Flutter ElasticPress wraps the entire menu child, including card padding. */
internal fun menuCanElasticPress(target: MenuTouchTarget, enabled: Boolean): Boolean =
    enabled && target.kind != TargetKind.Outside

/**
 * Flutter's row recognizer starts only on a menu row; it cannot turn a
 * RELEASE outside the card into a popup dismiss. Dismissal belongs to the
 * separate background barrier, which recognizes a genuine outside TAP.
 * A finger that began on the menu remains owned by ElasticPress while it
 * drags past the edge, and must be able to spring home without closing.
 */
internal fun menuReleaseDismisses(
    down: MenuTouchTarget,
    up: MenuTouchTarget,
    movedBeyondTapSlop: Boolean,
): Boolean = down.kind == TargetKind.Outside &&
    up.kind == TargetKind.Outside && !movedBeyondTapSlop

/**
 * Flutter _RowPressRecognizer only tracks pointers whose DOWN hit _MenuRow.
 * Sliding IN from the backdrop or a card's blank padding must not select an
 * item on Up. A gesture started on a disabled row may still slide onto an
 * enabled row, exactly as the original row recognizer allows.
 */
internal fun menuCanReleaseRowFrom(down: MenuTouchTarget): Boolean = when (down.kind) {
    TargetKind.Row, TargetKind.DisabledRow, TargetKind.Header -> true
    else -> false
}

/** Parent-card folding requires a direct tap on that parent, not drag-off. */
internal fun menuReleaseFoldsParent(
    down: MenuTouchTarget,
    up: MenuTouchTarget,
    movedBeyondTapSlop: Boolean,
): Boolean = down.kind == TargetKind.Back &&
    up.kind == TargetKind.Back && !movedBeyondTapSlop

internal data class MenuTouchTarget(
    val kind: TargetKind,
    val depth: Int = -1,
    val row: Int = -1,
)

/** Invert exactly the card graphicsLayer scale before testing its rows. */
internal fun menuPointInPaintedLevel(level: GlassMenuHitLevel, point: Offset): Offset {
    val pivot = level.paintPivot
    val scale = level.paintScale
    if (pivot == Offset.Unspecified || !scale.isFinite() || scale <= .0001f ||
        scale == 1f) return point
    return pivot + (point - pivot) / scale
}

internal fun hitMenu(layers: List<GlassMenuHitLevel>, p: Offset): MenuTouchTarget {
    val topDepth = layers.lastOrNull()?.depth ?: return MenuTouchTarget(TargetKind.Outside)
    for (level in layers.asReversed()) {
        val local = menuPointInPaintedLevel(level, p)
        if (local !in level.bounds) continue
        if (level.depth != topDepth) return MenuTouchTarget(TargetKind.Back)
        if (level.headerHeightPx > 0f &&
            local.y >= level.headerTopPx &&
            local.y < level.headerTopPx + level.headerHeightPx) {
            return MenuTouchTarget(TargetKind.Header, level.depth)
        }
        // FlClash keeps the title alive while the spring moves, but the
        // fading/folding rows must not become selectable until settled.
        if (!level.rowsEnabled) return MenuTouchTarget(TargetKind.Blank, level.depth)
        // Items draw inside a fixed scroller, not on top of a pinned header
        // or in the bottom padding when a partially visible row scrolls.
        if (local.y < level.rowViewportTopPx || local.y >= level.rowViewportBottomPx) {
            return MenuTouchTarget(TargetKind.Blank, level.depth)
        }
        val y = local.y - level.rowTopPx
        if (y >= 0f && level.rowHeightPx > 0f) {
            val offsets = level.rowOffsetsPx
            val index = if (offsets == null) (y / level.rowHeightPx).toInt()
                else menuRowIndexAtOffset(offsets, y)
            if (index in level.actions.indices &&
                (offsets == null || y < offsets[index] +
                    (level.rowHeightsPx?.getOrNull(index) ?: level.rowHeightPx))) {
                return if (menuActionEnabled(level.actions[index]))
                    MenuTouchTarget(TargetKind.Row, level.depth, index)
                else MenuTouchTarget(TargetKind.DisabledRow, level.depth, index)
            }
        }
        return MenuTouchTarget(TargetKind.Blank, level.depth)
    }
    return MenuTouchTarget(TargetKind.Outside)
}

/** Section gaps need true row offsets, without an O(N) scan on every move. */
internal fun menuRowIndexAtOffset(offsets: List<Float>, y: Float): Int {
    var lo = 0
    var hi = offsets.size
    while (lo < hi) {
        val middle = (lo + hi) ushr 1
        if (offsets[middle] <= y) lo = middle + 1 else hi = middle
    }
    return lo - 1
}

/**
 * Never redirect a nested menu's scrolling to the root when its level folds.
 * The owner is the current topmost scrollable row, captured on scroll start.
 */
internal fun menuScrollOwner(
    layers: List<GlassMenuHitLevel>, target: MenuTouchTarget,
    /** Optional local menu-root Down point, for scrollable section gaps. */
    point: Offset? = null,
): Int? = layers.lastOrNull()?.takeIf { top ->
    top.depth == target.depth && top.scrollable && (
        target.kind == TargetKind.Row || target.kind == TargetKind.DisabledRow ||
            (target.kind == TargetKind.Blank && point != null && top.rowsEnabled &&
                menuPointInPaintedLevel(top, point).let { local ->
                    local in top.bounds &&
                        local.y >= top.rowViewportTopPx &&
                        local.y < top.rowViewportBottomPx
                })
        )
}?.depth

internal fun menuScrollOwnerStillActive(
    layers: List<GlassMenuHitLevel>, depth: Int?,
): Boolean = depth != null && layers.lastOrNull()?.depth == depth

/** Slop measures displacement, not total accumulated fingertip jitter. */
internal fun menuScrollExceededSlop(startY: Float, currentY: Float, slop: Float): Boolean =
    abs(currentY - startY) > slop

/**
 * FlClash _hold/_endPress semantics, for both freshly pressed pointers and
 * the original source control's carried pointer. After a dwell changes the
 * hierarchy, _pressed becomes the NEW title and _landed suppresses Up. The
 * press must first move to a DIFFERENT row before _hold can arm a new dwell.
 * Unlike the previous Android approximation, this is not governed by a
 * second arbitrary distance/slop threshold after opening the child.
 */
/** FlClash _buildRow gives the submenu TITLE the same 400ms dwell
 * behavior as an expandable row. While a header is folding, _buildRow sets
 * togglesSubMenu=false, so only a short release calls _toggleTop to reverse.
 */
internal enum class MenuDwellAction { None, OpenChild, ToggleHeader }

internal fun menuDwellAction(
    target: MenuTouchTarget,
    levels: List<GlassMenuHitLevel>,
    headerFolding: Boolean,
): MenuDwellAction {
    val top = levels.lastOrNull() ?: return MenuDwellAction.None
    if (target.depth != top.depth) return MenuDwellAction.None
    return when (target.kind) {
        TargetKind.Header -> if (!headerFolding) MenuDwellAction.ToggleHeader
            else MenuDwellAction.None
        TargetKind.Row -> if (top.rowsEnabled && top.actions.getOrNull(target.row)
                ?.subActions?.isNotEmpty() == true) MenuDwellAction.OpenChild
            else MenuDwellAction.None
        else -> MenuDwellAction.None
    }
}

/** FlClash popup.dart compares only _MenuRow.index, not row identity. */
internal fun flClashPressedIndex(target: MenuTouchTarget): Int? = when (target.kind) {
    TargetKind.Row -> target.row.takeIf { it >= 0 }
    TargetKind.Header -> -1 // FlClash's unique _headerIndex
    else -> null
}

internal class FlClashMenuPress {
    var pressed: MenuTouchTarget = MenuTouchTarget(TargetKind.Blank)
        private set
    var landed: Boolean = false
        private set
    private var awaitingChildDepth: Int? = null
    // Flutter keeps _pressed == _headerIndex through the whole _foldSpring.
    // The Compose hit list temporarily removes the shrinking header before
    // the animation completes; that must NOT create a new _hold on its
    // underlying parent opener or clear the already-set _landed flag.
    private var foldingHeaderDepth: Int? = null
    // Fold completion and updated hit layers can land on different Compose
    // frames. Never reinterpret the outgoing child header as a fresh press
    // just after the parent Row handoff was delivered.
    private var restoringParentDepth: Int? = null

    fun start(target: MenuTouchTarget) {
        pressed = target
        landed = false
        awaitingChildDepth = null
        foldingHeaderDepth = null
        restoringParentDepth = null
    }

    /** Returns true iff Flutter _slidePress would call _hold(newRow). */
    fun move(target: MenuTouchTarget, visibleTopDepth: Int?): Boolean {
        // _pop keeps its title in the Flutter render tree until the fold
        // completes. A Compose interim Blank/Back/parent Row is a layout
        // artifact, not a real new row press from this still-held finger.
        if (foldingHeaderDepth != null) return false
        val returningDepth = restoringParentDepth
        if (returningDepth != null && (visibleTopDepth ?: -1) > returningDepth) {
            return false
        }
        val waitingFor = awaitingChildDepth
        // Our Compose child title becomes hit-testable after its first
        // visible frames, but there is still a short grow transition. Never
        // interpret
        // the temporary parent Back/blank as a deliberate press change.
        if (waitingFor != null && (visibleTopDepth ?: -1) < waitingFor) {
            return false
        }
        // FlClash _slidePress checks `index == _pressed`, where index is
        // null for a nonactionable surface and -1 for ANY submenu title.
        // Comparing the full Compose (kind,depth,row) instead changes
        // presses spuriously while a card animates between levels.
        if (flClashPressedIndex(target) == flClashPressedIndex(pressed)) return false
        pressed = target
        landed = false
        awaitingChildDepth = null
        foldingHeaderDepth = null
        restoringParentDepth = null
        return true
    }

    fun completeDwell(action: MenuDwellAction) {
        if (action == MenuDwellAction.None) return
        landed = true
        if (action == MenuDwellAction.OpenChild) {
            // FlClash _hold sets _pressed = _headerIndex after _push.
            val childDepth = pressed.depth + 1
            pressed = MenuTouchTarget(TargetKind.Header, childDepth)
            awaitingChildDepth = childDepth
            foldingHeaderDepth = null
            restoringParentDepth = null
        } else {
            // A header already has index _headerIndex; its dwell toggles
            // _toggleTop and the subsequent Up cannot trigger it again.
            awaitingChildDepth = null
            foldingHeaderDepth = pressed.depth
        }
    }

    /**
     * Mirrors Flutter _pop() AFTER the folding spring has completed. Its
     * setState remaps _pressed from _headerIndex to the old submenu's parent
     * entry index, while _landed remains true. A subsequent Move on that
     * exact entry MUST NOT create a new _hold/dwell or reopen the child.
     */
    fun foldedToParent(event: MenuFoldPressReturn): Boolean {
        if (!landed || pressed.kind != TargetKind.Header ||
            pressed.depth != event.childDepth ||
            foldingHeaderDepth != event.childDepth) return false
        pressed = MenuTouchTarget(TargetKind.Row, event.parentDepth, event.openingRow)
        awaitingChildDepth = null
        foldingHeaderDepth = null
        restoringParentDepth = event.parentDepth
        return true
    }

    fun canRunRelease(): Boolean = !landed
}

/**
 * Continuous press ownership through root -> submenu -> deeper submenus.
 *
 * This ONE pointerInput node remains attached while the popup's individual
 * cards enter/leave composition. It dynamically reads the current top card,
 * so the same pointer ID can be carried across the expanding boundary.
 * A dwell enters a child after 400 ms; release on the original parent row
 * cannot immediately close the child. Drag within the new level to choose.
 *
 * Row composables retain clickable semantics for accessibility, but physical
 * input is routed by this full-scene layer rather than competing clickables.
 */
@Composable
internal fun GlassMenuGestureSurface(
    modifier: Modifier,
    modal: Boolean = true,
    dismissOnClickOutside: Boolean = true,
    levels: List<GlassMenuHitLevel>,
    onHover: (depth: Int, row: Int) -> Unit,
    onSelect: (depth: Int, row: Int) -> Unit,
    onUnfold: (depth: Int, row: Int) -> Unit,
    onBack: () -> Unit,
    onHeaderToggle: () -> Unit,
    headerFolding: Boolean = false,
    onDismiss: () -> Unit,
    onScroll: (depth: Int, dy: Float) -> Unit,
    /** Release velocity is expressed in scroll coordinates (opposite finger). */
    onFling: (depth: Int, velocityPxPerSecond: Float) -> Unit = { _, _ -> },
    /** A fresh pointer cancels the previous ballistic scroll immediately. */
    onGestureStart: () -> Unit = {},
    carriedPress: GlassCarriedPress? = null,
    /** _pop() remaps held header to its original parent opener on completion. */
    foldPressHandoff: MenuFoldPressHandoff? = null,
    /** Same inverse matrix as the menu's ElasticPress graphicsLayer. */
    pointToMenu: (Offset) -> Offset = { it },
    pressEnabled: Boolean = true,
    onPressStart: () -> Unit = {},
    onPressMove: (Offset) -> Unit = {},
    onPressEnd: () -> Unit = {},
    /** Desktop/stylus hover affects the whole menu, never mobile touch. */
    onMenuPointerHover: (Boolean) -> Unit = {},
) {
    val currentLevels by rememberUpdatedState(levels)
    val transformTouch by rememberUpdatedState(pointToMenu)
    val canPress by rememberUpdatedState(pressEnabled)
    val beginPress by rememberUpdatedState(onPressStart)
    val dragPress by rememberUpdatedState(onPressMove)
    val finishPress by rememberUpdatedState(onPressEnd)
    val reportMenuPointerHover by rememberUpdatedState(onMenuPointerHover)
    fun hitAt(position: Offset) = hitMenu(currentLevels, transformTouch(position))
    val select by rememberUpdatedState(onSelect)
    val unfold by rememberUpdatedState(onUnfold)
    val hover by rememberUpdatedState(onHover)
    val back by rememberUpdatedState(onBack)
    val toggleHeader by rememberUpdatedState(onHeaderToggle)
    val isHeaderFolding by rememberUpdatedState(headerFolding)
    val dismiss by rememberUpdatedState(onDismiss)
    val scroll by rememberUpdatedState(onScroll)
    val fling by rememberUpdatedState(onFling)
    val beginGesture by rememberUpdatedState(onGestureStart)
    val platformConfig = android.view.ViewConfiguration.get(LocalContext.current)
    val minFlingVelocity = platformConfig.scaledMinimumFlingVelocity.toFloat()
    val maxFlingVelocity = platformConfig.scaledMaximumFlingVelocity.toFloat()
    val haptic = LocalHapticFeedback.current
    val touchSlop = LocalViewConfiguration.current.touchSlop

    // The source button receives Down/Move/Up for the same pointer after
    // opening the popup. The overlay sees no new Down for that gesture; this
    // channel is its missing path. Normal taps inside the already open menu
    // continue through pointerInput below.
    LaunchedEffect(carriedPress) {
        val carry = carriedPress ?: return@LaunchedEffect
        var origin = Offset.Unspecified
        var position = Offset.Unspecified
        var engaged = false
        var elasticHeld = false
        val held = FlClashMenuPress()
        var dwell: Job? = null
        var seenFoldSequence = foldPressHandoff?.sequence() ?: 0L

        fun applyCompletedFold() {
            val folded = foldPressHandoff?.latest ?: return
            if (folded.sequence <= seenFoldSequence) return
            seenFoldSequence = folded.sequence
            if (held.foldedToParent(folded)) {
                dwell?.cancel()
                dwell = null
                // Original _pop() highlights the original opening row,
                // without restarting its dwell or clearing _landed.
                hover(folded.parentDepth, folded.openingRow)
            }
        }

        fun clearHover() { hover(-1, -1) }
        fun stopDwell() {
            dwell?.cancel()
            dwell = null
        }
        fun armDwell() {
            stopDwell()
            if (!engaged || held.landed) return
            val original = held.pressed
            val action = menuDwellAction(original, currentLevels, isHeaderFolding)
            if (action == MenuDwellAction.None) return
            dwell = launch {
                delay(400L)
                if (engaged && held.pressed == original &&
                    menuDwellAction(original, currentLevels, isHeaderFolding) == action) {
                    // Mirrors _pressed = _headerIndex and _landed = true
                    // after either an unfolded row or the return title.
                    held.completeDwell(action)
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    when (action) {
                        MenuDwellAction.OpenChild -> unfold(original.depth, original.row)
                        MenuDwellAction.ToggleHeader -> toggleHeader()
                        MenuDwellAction.None -> Unit
                    }
                    // FlClash _pressed = _headerIndex: visually hand the
                    // held highlight to the new/current title as well.
                    hover(held.pressed.depth, -2)
                }
            }
        }
        for (queued in carry.events) {
            val event = carry.resolve(queued) ?: continue
            if (event.phase != GlassSourcePointerPhase.Down) applyCompletedFold()
            when (event.phase) {
                GlassSourcePointerPhase.Down -> {
                    beginGesture()
                    stopDwell()
                    seenFoldSequence = foldPressHandoff?.sequence() ?: 0L
                    origin = event.position
                    position = event.position
                    engaged = false
                    held.start(MenuTouchTarget(TargetKind.Blank))
                    clearHover()
                }
                GlassSourcePointerPhase.Move -> {
                    if (origin == Offset.Unspecified) continue
                    val physicallyMoved = event.position != position
                    position = event.position
                    // A short stationary press must leave the opened menu
                    // visible. Only a deliberate drag becomes a row selection.
                    if (!engaged && (position - origin).getDistance() >
                        touchSlop) engaged = true
                    if (!engaged) continue
                    val current = hitAt(position)
                    if (!elasticHeld && menuCanElasticPress(current, canPress)) {
                        beginPress()
                        elasticHeld = true
                    }
                    if (elasticHeld) dragPress(position)
                    if (physicallyMoved &&
                        held.move(current, currentLevels.lastOrNull()?.depth)) {
                        stopDwell()
                        when (current.kind) {
                            TargetKind.Row -> {
                                hover(current.depth, current.row)
                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            }
                            TargetKind.Header -> {
                                hover(current.depth, -2)
                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            }
                            else -> clearHover()
                        }
                        armDwell()
                    }
                }
                GlassSourcePointerPhase.Up -> {
                    if (elasticHeld) {
                        finishPress()
                        elasticHeld = false
                    }
                    if (origin != Offset.Unspecified && engaged && held.canRunRelease()) {
                        val selected = hitAt(event.position)
                        // FlClash _endPress: a dwell's _landed suppresses this
                        // Up, unless a new _hold replaced the pressed row.
                        when (selected.kind) {
                            TargetKind.Row -> select(selected.depth, selected.row)
                            TargetKind.Header -> toggleHeader()
                            // A carried Up outside never dismisses.
                            else -> Unit
                        }
                    }
                    stopDwell()
                    origin = Offset.Unspecified
                    position = Offset.Unspecified
                    held.start(MenuTouchTarget(TargetKind.Blank))
                    engaged = false
                    clearHover()
                }
                GlassSourcePointerPhase.Cancel -> {
                    if (elasticHeld) {
                        finishPress()
                        elasticHeld = false
                    }
                    stopDwell()
                    origin = Offset.Unspecified
                    position = Offset.Unspecified
                    held.start(MenuTouchTarget(TargetKind.Blank))
                    engaged = false
                    clearHover()
                }
            }
        }
        if (elasticHeld) finishPress()
    }

    Box(modifier
        // Independent, non-consuming hover channel. The long-running
        // Down/Move/Up recognizer below remains the only gesture owner.
        .expandedTouchInput(0.dp, modal, dismissOnClickOutside, shareWithSiblings = !modal) {
            awaitPointerEventScope {
                while (true) {
                    val event = awaitPointerEvent()
                    if (event.type == PointerEventType.Exit) {
                        reportMenuPointerHover(false)
                    } else if (event.type == PointerEventType.Enter ||
                        event.type == PointerEventType.Move) {
                        val pointer = event.changes.firstOrNull()
                        if (pointer != null && (pointer.type == PointerType.Mouse ||
                            pointer.type == PointerType.Stylus)) {
                            reportMenuPointerHover(
                                canPress && hitAt(pointer.position).kind != TargetKind.Outside,
                            )
                        }
                    }
                }
            }
        }
        .expandedTouchInput(0.dp, modal, dismissOnClickOutside, shareWithSiblings = !modal) {
        // Keep the coroutine alive throughout each gesture even when a child
        // menu recomposes or its glass pane is replaced by an animating card.
        coroutineScope {
            awaitEachGesture {
                val down = awaitFirstDown(requireUnconsumed = false)
                if (down.type == PointerType.Mouse && !currentEvent.buttons.isPrimaryPressed) return@awaitEachGesture
                beginGesture()
                val velocityTracker = VelocityTracker().apply {
                    addPosition(down.uptimeMillis, down.position)
                }
                var last = down.position
                val downTarget = hitAt(down.position)
                if (!modal && downTarget.kind == TargetKind.Outside) {
                    if (dismissOnClickOutside) dismiss()
                    return@awaitEachGesture
                }
                val held = FlClashMenuPress().apply {
                    start(downTarget)
                }
                var seenFoldSequence = foldPressHandoff?.sequence() ?: 0L
                val arena = MenuGestureArena(
                    downTarget, down.position, touchSlop,
                    menuScrollOwner(currentLevels, downTarget, transformTouch(down.position)),
                )
                // _RowPressRecognizer.addAllowedPointer refuses blank/Back
                // starts, even though ElasticPress itself still responds.
                val rowCandidate = menuCanReleaseRowFrom(downTarget)
                // Root padding, section spacers, top title and receding
                // cards are all part of ElasticPress's hit-tested child.
                var canceled = down.isConsumed
                var elasticHeld = !canceled && menuCanElasticPress(held.pressed, canPress)
                if (elasticHeld) beginPress()
                var scrolling = false
                var scrollingDepth: Int? = null
                var dwell: Job? = null

                fun applyCompletedFold() {
                    val folded = foldPressHandoff?.latest ?: return
                    if (folded.sequence <= seenFoldSequence) return
                    seenFoldSequence = folded.sequence
                    if (held.foldedToParent(folded)) {
                        dwell?.cancel()
                        dwell = null
                        hover(folded.parentDepth, folded.openingRow)
                    }
                }

                fun notify(target: MenuTouchTarget) {
                    when (target.kind) {
                        TargetKind.Row -> hover(target.depth, target.row)
                        TargetKind.Header -> hover(target.depth, -2)
                        else -> hover(-1, -1)
                    }
                }

                fun update(targetNow: MenuTouchTarget): Boolean {
                    if (!rowCandidate) return false
                    if (!held.move(targetNow, currentLevels.lastOrNull()?.depth)) {
                        return false
                    }
                    dwell?.cancel()
                    dwell = null
                    notify(targetNow)
                    if (targetNow.kind == TargetKind.Row ||
                        targetNow.kind == TargetKind.Header) {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    }
                    return true
                }

                fun armDwell() {
                    dwell?.cancel()
                    dwell = null
                    if (canceled || scrolling || held.landed || !rowCandidate) return
                    val start = held.pressed
                    val action = menuDwellAction(start, currentLevels, isHeaderFolding)
                    if (action == MenuDwellAction.None) return
                    dwell = launch {
                        delay(400L)
                        if (!canceled && !scrolling && !held.landed &&
                            held.pressed == start &&
                            menuDwellAction(start, currentLevels, isHeaderFolding) == action) {
                            held.completeDwell(action)
                            arena.levelTransition()
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            when (action) {
                                MenuDwellAction.OpenChild -> unfold(start.depth, start.row)
                                MenuDwellAction.ToggleHeader -> toggleHeader()
                                MenuDwellAction.None -> Unit
                            }
                            notify(held.pressed)
                        }
                    }
                }

                if (!canceled) {
                    notify(held.pressed)
                    armDwell()
                }
                // Flutter _RowPressRecognizer watches Down without
                // accepting. Do not consume it before scroll's arena claim.
                while (true) {
                    val event = awaitPointerEvent()
                    // pop() may have completed since the last physical
                    // event; reconcile before hit-testing this same finger.
                    applyCompletedFold()
                    val change = event.changes.firstOrNull { it.id == down.id }
                    if (event.changes.any { it.id != down.id && it.pressed }) {
                        // A second pointer cancels an in-flight row/dwell or
                        // scroll; a later Up must never select the old row.
                        canceled = true
                        arena.reject()
                        scrolling = false
                        dwell?.cancel()
                        dwell = null
                        if (elasticHeld) {
                            finishPress()
                            elasticHeld = false
                        }
                        notify(MenuTouchTarget(TargetKind.Blank))
                    }
                    if (change == null) {
                        canceled = true
                        break
                    }
                    val delta = change.position.y - last.y
                    if (!canceled) velocityTracker.addPosition(
                        change.uptimeMillis, change.position,
                    )
                    last = change.position
                    // The pending row recognizer does not own the Down or
                    // intermediate Moves. Another recognizer can consume
                    // them before our Main pass; a vertical drag can claim
                    // scroll only from the layer held at initial Down.
                    if (change.pressed && !canceled && !scrolling) {
                        when (arena.move(last, otherConsumed = change.isConsumed)) {
                            MenuArenaWinner.Rejected -> {
                                canceled = true
                                dwell?.cancel()
                                dwell = null
                                if (elasticHeld) {
                                    finishPress()
                                    elasticHeld = false
                                }
                                notify(MenuTouchTarget(TargetKind.Blank))
                            }
                            MenuArenaWinner.Scroll -> if (!held.landed) {
                                scrollingDepth = arena.scrollOwner
                                scrolling = true
                                dwell?.cancel()
                                dwell = null
                                notify(MenuTouchTarget(TargetKind.Blank))
                            }
                            MenuArenaWinner.Pending -> Unit
                        }
                    }
                    if (elasticHeld && change.pressed && !canceled) dragPress(last)
                    if (scrolling && change.pressed) {
                        if (menuScrollOwnerStillActive(currentLevels, scrollingDepth)) {
                            scrollingDepth?.let { scroll(it, delta) }
                        } else {
                            // Parent becomes top after a nested level folds:
                            // cancel the old drag rather than scrolling root.
                            canceled = true
                            scrolling = false
                        }
                        change.consume()
                    } else if (change.pressed && !canceled) {
                        // Flutter _slidePress only runs on actual pointer
                        // movement, not a stationary frame where the
                        // shrinking menu's hit geometry happened to move.
                        // Otherwise the held header can turn into a new
                        // root entry and rearm another 400ms dwell.
                        if (change.position != change.previousPosition &&
                            update(hitAt(last))) armDwell()
                    }
                    if (!change.pressed) {
                        // Another recognizer can win only on Up (for
                        // example a surrounding tap recognizer). Never
                        // deliver a duplicate row selection in that case.
                        if (change.isConsumed && arena.winner == MenuArenaWinner.Pending) {
                            arena.reject()
                            canceled = true
                        }
                        if (!canceled && scrolling &&
                            menuScrollOwnerStillActive(currentLevels, scrollingDepth)) {
                            val velocity = menuScrollFlingVelocity(
                                velocityTracker.calculateVelocity().y,
                                minFlingVelocity, maxFlingVelocity,
                            )
                            if (velocity != null) {
                                scrollingDepth?.let { fling(it, velocity) }
                            }
                        }
                        dwell?.cancel()
                        if (elasticHeld) {
                            finishPress()
                            elasticHeld = false
                        }
                        arena.finishAt(last)
                        val released = hitAt(last)
                        // FlClash _endPress: _landed suppresses the Up after
                        // a timed _push or _toggleTop, avoiding double action.
                        if (!canceled && !scrolling && arena.mayAcceptRelease() &&
                            held.canRunRelease()) {
                            when (released.kind) {
                                TargetKind.Row -> if (arena.maySelectRow()) {
                                    select(released.depth, released.row)
                                }
                                TargetKind.Header -> if (arena.maySelectRow()) {
                                    toggleHeader()
                                }
                                TargetKind.Back -> if (arena.mayFoldParent(released)) back()
                                TargetKind.Outside -> if (dismissOnClickOutside && arena.mayDismissOutside(released)) dismiss()
                                TargetKind.Blank, TargetKind.DisabledRow -> Unit
                            }
                        }
                        if (!canceled) change.consume()
                        break
                    }
                    // Final pass observes late consumption by a competing
                    // parent/child handler. Only a pending recognizer may
                    // lose the arena here; an accepted vertical scroll owns
                    // its movement even though it consumed the Main pass.
                    if (!canceled && arena.winner == MenuArenaWinner.Pending) {
                        val finalEvent = awaitPointerEvent(PointerEventPass.Final)
                        val finalChange = finalEvent.changes.firstOrNull { it.id == down.id }
                        if (finalChange == null || finalChange.isConsumed) {
                            canceled = true
                            arena.reject()
                            dwell?.cancel()
                            dwell = null
                            if (elasticHeld) {
                                finishPress()
                                elasticHeld = false
                            }
                            notify(MenuTouchTarget(TargetKind.Blank))
                        }
                    }
                }
                dwell?.cancel()
                if (elasticHeld) finishPress()
                notify(MenuTouchTarget(TargetKind.Blank))
            }
        }
    })

    // Deliberately NO second, higher-z clickable on the title. That used
    // to intercept a fresh Down, so the shared 400ms _hold-equivalent never
    // saw the header gesture. Both title press/release and title dwell now
    // belong to this one stable pointerInput recognizer. GlassSubmenu keeps
    // its own semantic clickable for accessibility services.
}
