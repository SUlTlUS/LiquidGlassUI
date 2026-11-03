package dev.liquidglass27.glass

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.spring
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.Image
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.setValue
import com.zeaze.tianyinwallpaper.backdrop.Backdrop
import com.zeaze.tianyinwallpaper.backdrop.backdrops.layerBackdrop
import com.zeaze.tianyinwallpaper.backdrop.backdrops.rememberCombinedBackdrop
import com.zeaze.tianyinwallpaper.backdrop.backdrops.rememberLayerBackdrop
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.paneTitle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.first
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.exp
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sign
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * One immutable, transparent foreground frame recorded without GlassPane.
 * Capture dimensions and monotonic timestamp travel together with the image
 * so a menu never reuses an old frame after resizing or theme changes.
 */
data class GlassSourceContentSnapshot(
    val image: ImageBitmap,
    val widthPx: Int,
    val heightPx: Int,
    val capturedAtNanos: Long,
    val styleKey: Int = 0,
)

data class GlassMenuAction(
    val text: String,
    val symbol: String = "",
    val destructive: Boolean = false,
    val selected: Boolean = false,
    val section: GlassMenuSection? = null,
    /** FlClash menu footnote: up to two lines beneath the title. */
    val subtitle: String? = null,
    /** Optional 20dp leading Glyph; the legacy symbol also serves as fallback. */
    val glyph: String? = null,
    /** A row with children unfolds a second glass card instead of dismissing. */
    val subActions: List<GlassMenuAction> = emptyList(),
    val vectorGlyph: GlassGlyph? = null,
    val onClick: (() -> Unit)? = null,
)

/** FlClash _buildItem: children open a level; a leaf without onPressed is disabled. */
internal fun menuActionEnabled(action: GlassMenuAction): Boolean =
    action.subActions.isNotEmpty() || action.onClick != null

/** FlClash _revealChecked / _buildLazyContent position checked rows near the middle. */
internal fun menuCheckedInitialScrollPx(
    checkedIndex: Int,
    itemCount: Int,
    rowHeightPx: Float,
    visibleHeightPx: Float,
): Int {
    if (checkedIndex !in 0 until itemCount || rowHeightPx <= 0f ||
        !rowHeightPx.isFinite() || !visibleHeightPx.isFinite() ||
        visibleHeightPx <= 0f) return 0
    val visibleRows = (visibleHeightPx / rowHeightPx).coerceAtLeast(1f)
    val rowsAbove = (minOf(7f, visibleRows).toInt() / 2)
    val maxScroll = (itemCount * rowHeightPx - visibleHeightPx).coerceAtLeast(0f)
    return ((checkedIndex - rowsAbove) * rowHeightPx)
        .coerceIn(0f, maxScroll).roundToInt()
}

/**
 * Full-scene overlay, so the closing platter shrinks into exactly the button
 * that opened it. [anchor] uses the GlassScene root's pixel coordinates.
 */
@androidx.annotation.RequiresApi(33)
@Composable
fun GlassMorphMenu(
    expanded: Boolean,
    anchor: Rect,
    actions: List<GlassMenuAction>,
    dark: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    carriedPress: GlassCarriedPress? = null,
    /** Transparent source CONTENT only; never a screenshot of GlassPane. */
    sourceContentSnapshot: GlassSourceContentSnapshot? = null,
    /** Mirrors Flutter's sourceImage == null route; independent of capture delay. */
    returnIntoSource: Boolean = true,
    placement: GlassMenuPlacement = GlassMenuPlacement.OverAnchorEnd,
    avoid: Rect? = null,
    /** FlClash maxVisibleItems; explicit cap enables lazy leaf-only root rows. */
    maxVisibleItems: Int? = null,
    /** Called only after the reverse morph has completely left the scene. */
    onReturnComplete: () -> Unit = {},
    properties: GlassPopupProperties = GlassPopupProperties(),
    sourceContent: @Composable BoxScope.() -> Unit = {},
) {
    if (properties.elevated) {
        GlassSurfaceTheme(dark, true) {
            GlassMorphMenu(expanded, anchor, actions, dark, onDismiss, modifier, carriedPress,
                sourceContentSnapshot, returnIntoSource, placement, avoid, maxVisibleItems,
                onReturnComplete, properties.copy(elevated = false), sourceContent)
        }
        return
    }
    val returnIntoSource = returnIntoSource && properties.returnIntoSource
    // Sketch multi-level sampling: each level records only itself, AFTER
    // drawing, so the following level sees the earlier glass and content.
    // Capturing the entire popup host would recursively sample itself.
    require(maxVisibleItems == null || maxVisibleItems > 0) {
        "maxVisibleItems must be positive"
    }
    val rootIsLazy = menuBuildsLazily(maxVisibleItems, actions)
    val rootVisibleItems = maxVisibleItems ?: 7
    val sketchBase = LocalSketchGlassBackdrop.current
    val sketchRootLayer = rememberLayerBackdrop()
    val sketchFirstLayer = rememberLayerBackdrop()
    val sketchNestedLayers = listOf(
        rememberLayerBackdrop(), rememberLayerBackdrop(),
        rememberLayerBackdrop(), rememberLayerBackdrop(),
        rememberLayerBackdrop(), rememberLayerBackdrop(),
    )
    val sketchFirstInput = if (sketchBase != null) {
        rememberCombinedBackdrop(*precedingSketchLayers(
            background = sketchBase,
            root = sketchRootLayer,
            first = null,
            nested = sketchNestedLayers,
            nestedIndex = 0,
        ).toTypedArray())
    } else null
    val routeState = remember { MenuRouteState(expanded) }
    // FlClash CommonPopupRoute.controller: ONE normalized route position.
    // Its direction changes without ever snapping back to zero on reopen.
    val clock = remember { Animatable(0f) }
    var submenuIndex by remember { mutableIntStateOf(-1) }
    var submenuOpen by remember { mutableStateOf(false) }
    var rootHovered by remember { mutableIntStateOf(-1) }
    val rootScroll = rememberScrollState()
    // A real LazyListState is used ONLY for FlClash's capped flat root menu.
    // Other menus retain their existing scroll state and variable row heights.
    val rootLazyScroll = rememberLazyListState()
    // A different first-level owner gets a NEW ScrollState, so switching
    // between submenus cannot inherit the previous owner's offset.
    val firstSubScroll = remember(submenuIndex) { ScrollState(0) }
    val nestedSubScrolls = remember { mutableStateListOf<ScrollState>() }
    val scrollConflator = remember { MenuScrollConflator() }
    // Native Android spline decay matches FlClash's ClampingScrollPhysics
    // behavior: ballistic after release, clamped at both ends, no bounce.
    val flingBehavior = ScrollableDefaults.flingBehavior()
    var activeScrollFling by remember { mutableStateOf<kotlinx.coroutines.Job?>(null) }
    // Pointer drag and ballistic edge impact share ONE vertical spring.
    // The epoch rejects stale fling completions after touch/route changes.
    val menuMorphMomentum = remember { MenuMorphMomentum() }
    val menuPullY = menuMorphMomentum.pullY
    val menuPullReleaseSpec = remember { menuPressSpring() }
    var menuBouncePanel by remember { mutableStateOf(Rect.Zero) }
    val labelWidthCache = remember { MenuLabelWidthCache() }
    LaunchedEffect(expanded, scrollConflator, firstSubScroll, rootIsLazy) {
        // A new menu session cannot inherit queued moves from a dismissed one.
        scrollConflator.clear()
        if (expanded) {
            while (true) {
                val batch = scrollConflator.takeBatch()
                for (scrollEvent in batch.events) {
                    // A hierarchy transition may have invalidated a batch
                    // after takeBatch() but before / between scrollBy calls.
                    if (!scrollConflator.isCurrent(batch)) break
                    val state: androidx.compose.foundation.gestures.ScrollableState? =
                        when (scrollEvent.depth) {
                            0 -> if (rootIsLazy) rootLazyScroll else rootScroll
                            1 -> firstSubScroll
                            else -> nestedSubScrolls.getOrNull(scrollEvent.depth - 2)
                        }
                    if (scrollEvent.flingVelocityPxPerSecond != null) {
                        // Queued AFTER every final finger delta; run outside
                        // the consumer so the next drag can cancel it at once.
                        activeScrollFling?.cancel()
                        if (state != null) {
                            val velocity = scrollEvent.flingVelocityPxPerSecond
                            val flingTicket = menuMorphMomentum.startFling()
                            activeScrollFling = launch {
                                var remaining = 0f
                                state.scroll(MutatePriority.Default) {
                                    remaining = with(flingBehavior) {
                                        performFling(velocity)
                                    }
                                }
                                // DefaultFlingBehavior reports leftover velocity
                                // when its ScrollScope hits a clamped boundary.
                                // Reuse the existing menu-wide ElasticPress
                                // pull/stretch and spring, never overscroll rows.
                                val hitBoundary = when (state) {
                                    is androidx.compose.foundation.lazy.LazyListState ->
                                        if (remaining > 0f) !state.canScrollForward
                                        else !state.canScrollBackward
                                    is ScrollState ->
                                        if (remaining > 0f) !state.canScrollForward
                                        else !state.canScrollBackward
                                    else -> false
                                }
                                if (hitBoundary && kotlin.math.abs(remaining) > 50f) {
                                    val impact = menuEdgeImpactVelocityPx(
                                        remaining, menuBouncePanel,
                                    )
                                    if (impact != 0f) {
                                        // Join the existing Y pull's velocity
                                        // instead of adding a second animator.
                                        menuMorphMomentum.edgeImpact(
                                            flingTicket, remaining, menuBouncePanel,
                                            menuPullReleaseSpec,
                                        )
                                    }
                                }
                            }
                        }
                    } else {
                        // Exactly one active scrollBy at a time; no competing
                        // MutatorMutex jobs that cancel each other's deltas.
                        state?.scrollBy(-scrollEvent.dyPx)
                    }
                }
            }
        }
    }
    LaunchedEffect(expanded) {
        if (!expanded) {
            // Revoke pending impacts before canceling their coroutine.
            // Preserve the current physical displacement and velocity while
            // both axes return home through the ordinary release spring.
            menuMorphMomentum.settle(menuPullReleaseSpec)
            activeScrollFling?.cancel()
            scrollConflator.clear()
        }
    }
    var gestureDepth by remember { mutableIntStateOf(-1) }
    var gestureHovered by remember { mutableIntStateOf(-1) }
    val menuScope = rememberCoroutineScope()
    // FlClash ElasticPress surrounds the entire popup stack. Its press
    // spring must outlive a quick tap (finish swelling before releasing).
    // Only the MENU DRAWING is transformed; hit tests invert that matrix.
    val menuLift = remember { Animatable(0f) }
    // A folded child changes the Stack's layout bounds at removal time.
    // Keep the outgoing ElasticPress reference panel until its press/pull
    // springs actually rest; otherwise the parent pops larger on that frame.
    val menuFoldPressGeometry = remember { MenuFoldPressGeometry() }
    var menuPressActive by remember { mutableStateOf(false) }
    var menuPressJob by remember { mutableStateOf<kotlinx.coroutines.Job?>(null) }
    // Flutter's _Spring.springTo keeps ONE frame ticker, its instantaneous
    // velocity and its physical state across repeated PointerMove retargets.
    // animateFloatAsState retargeted on every move need not share that timing.
    val menuPullX = remember { MenuRetargetSpring() }
    val menuHover = remember { MenuRetargetSpring() }
    val menuPullTrackSpec = remember { menuTrackSpring() }
    val menuHoverSpec = remember { menuHoverSpring() }
    var menuHoverPresent by remember { mutableStateOf(false) }
    LaunchedEffect(menuPullX, menuPullY, menuHover) {
        driveMenuRetargetSprings(menuPullX, menuPullY, menuHover)
    }
    fun updateMenuHover(entered: Boolean) {
        if (menuHoverPresent == entered) return
        menuHoverPresent = entered
        menuHover.springTo(if (entered) .4f else 0f, menuHoverSpec)
    }
    LaunchedEffect(expanded) {
        if (!expanded) updateMenuHover(false)
    }
    fun startMenuPress() {
        if (menuPressActive) return
        menuPressActive = true
        // Both axes hand their CURRENT velocity to pointer tracking.
        // Do not let X continue an autonomous release while Y tracks touch.
        menuPullX.springTo(menuPullX.value, menuPullTrackSpec)
        menuMorphMomentum.press(menuPullTrackSpec)
        // FlClash _Spring.springTo retains instantaneous velocity even if
        // a previous release was already springing back toward zero.
        val inheritedVelocity = menuLift.velocity
        menuPressJob?.cancel()
        menuPressJob = menuScope.launch {
            menuLift.animateTo(
                1f, menuPressSpring(), initialVelocity = inheritedVelocity,
            )
        }
    }
    fun moveMenuPress(point: Offset, panel: Rect) {
        if (!menuPressActive) return
        val pull = menuElasticPull(panel, point)
        menuPullX.springTo(pull.x, menuPullTrackSpec)
        menuPullY.springTo(pull.y, menuPullTrackSpec)
    }
    fun endMenuPress() {
        if (!menuPressActive) return
        menuPressActive = false
        menuMorphMomentum.release()
        menuPullX.springTo(0f, menuPullReleaseSpec)
        menuPullY.springTo(0f, menuPullReleaseSpec)
        // FlClash does NOT cancel/restart the rising spring on finger-up.
        // It sets _releasing=true, keeps the existing spring moving and
        // reverses on the FIRST value >= .975. Waiting for animateTo(1f)
        // to complete adds several settling oscillations to a quick tap.
        val originalRise = menuPressJob
        menuPressJob = menuScope.launch {
            val liftAtSnap = snapshotFlow {
                menuLift.value to menuLift.velocity
            }.first { (lift, _) ->
                shouldSettleMenuPress(releasing = true, lift = lift)
            }
            // Capture the physical upward velocity BEFORE cancellation:
            // Animatable clears it when its running mutator stops.
            originalRise?.cancelAndJoin()
            menuLift.animateTo(
                0f, menuPressSpring(), initialVelocity = liftAtSnap.second,
            )
        }
    }
    LaunchedEffect(expanded) {
        // Dismissal can dispose the pointer overlay before its Up/Cancel
        // event arrives. Never leave lift in an active pressed state when a
        // session is closed; its return still uses the original .975 rule.
        if (!expanded && menuPressActive) endMenuPress()
    }
    DisposableEffect(menuLift) {
        onDispose { menuPressJob?.cancel() }
    }
    LaunchedEffect(menuFoldPressGeometry) {
        snapshotFlow {
            val lift = menuEffectiveLift(menuLift.value, menuHover.value)
            val pull = Offset(menuPullX.value, menuPullY.value)
            menuFoldPressGeometry.pinned != null && !menuPressActive &&
                kotlin.math.abs(lift) <= .001f &&
                kotlin.math.abs(pull.x) <= .001f &&
                kotlin.math.abs(pull.y) <= .001f
        }.collect { rested ->
            if (rested) menuFoldPressGeometry.unpinWhenResting(
                menuPressActive,
                menuEffectiveLift(menuLift.value, menuHover.value),
                Offset(menuPullX.value, menuPullY.value),
            )
        }
    }
    // Flutter popup.dart has exactly ONE unbounded _unfold controller for
    // the active top card; ancestors are settled and recede by depth.
    val submenuMotion = remember { Animatable(1f) }
    var nestedPath by remember { mutableStateOf<List<Int>>(emptyList()) }
    val nestedScrollOffsets = remember { mutableStateListOf<Float>() }
    val nestedMotionJobs = remember { NestedMenuMotionJobs() }
    DisposableEffect(nestedMotionJobs) {
        onDispose { nestedMotionJobs.cancelAll() }
    }
    var nestedFolding by remember { mutableStateOf(false) }
    // Flutter _pop() transfers a held header's _pressed index back to the
    // parent row that opened it, without creating another dwell timer.
    // The new level is published before Animatable.snapTo(0f) can suspend.
    // Guard its very first composition against the OLD settled spring=1.
    var pendingNestedDepth by remember { mutableIntStateOf(-1) }
    // A pop changes the path length and sets the shared spring to 1.
    // Keep ancestors' visual distance continuous across that suspend point.
    var parentRestoring by remember { mutableStateOf(false) }
    // A depth index can be recycled for a completely different submenu.
    // Invalidate queued scroll moves whenever the visible hierarchy changes.
    LaunchedEffect(submenuIndex, nestedPath, scrollConflator) {
        // Revoke before cancel so a racing completion cannot add a kick.
        menuMorphMomentum.invalidate()
        activeScrollFling?.cancel()
        scrollConflator.clear()
        // The pointer may remain held while the child unfolds. Do not reset
        // the shared finger-drag position.
        if (!menuMorphMomentum.pointerHeld) {
            menuPullY.springTo(0f, menuPullReleaseSpec)
        }
    }

    fun pushNested(index: Int, scrollPx: Float) {
        val parentMotion = submenuMotion.value
        if (submenuIndex !in actions.indices ||
            !canOpenAnotherMenuLevel(
                expanded = expanded, firstOpen = submenuOpen,
                parentProgress = parentMotion, folding = nestedFolding,
                currentNestedDepth = nestedPath.size,
            )) return
        var owner = actions[submenuIndex]
        for (step in nestedPath) {
            owner = owner.subActions.getOrNull(step) ?: return
        }
        val next = owner.subActions.getOrNull(index) ?: return
        if (next.subActions.isEmpty()) return
        scrollConflator.clear()
        // Original _push: append level, reset the sole _unfold to 0.
        nestedMotionJobs.cancelAll()
        nestedSubScrolls.add(ScrollState(0))
        nestedScrollOffsets.add(scrollPx)
        // FlClash sets _unfold.value=0 before setState(_path.add).
        // Compose cannot synchronously snap an Animatable in this callback:
        // mark the next depth pending BEFORE publishing the new path.
        pendingNestedDepth = nestedPath.size + 2
        nestedPath = nestedPath + index
        nestedMotionJobs.push(menuScope.launch {
            submenuMotion.snapTo(0f)
            pendingNestedDepth = -1
            submenuMotion.animateTo(
                1f, FlClashMenuSprings.unfold(),
                initialVelocity = submenuMotion.velocity,
            )
            // animateTo already lands at exactly 1 when it completes;
            // avoid another separate snap/recomposition at rebound end.
        })
    }

    fun popNested(allowHeaderReverse: Boolean = true) {
        if (nestedPath.isEmpty()) return
        val motion = submenuMotion
        // FlClash _pop ignores another Back while already folding;
        // only a deliberate _toggleTop (header tap) reverses the spring.
        if (ignoreRepeatedNestedBack(nestedFolding, allowHeaderReverse)) return
        scrollConflator.clear()
        if (nestedFolding) {
            // Tapping the header during its reverse re-opens the level
            // from the current spring velocity, as _toggleTop in FlClash.
            nestedFolding = false
            nestedMotionJobs.replaceLast {
                menuScope.launch {
                    motion.animateTo(
                        1f, FlClashMenuSprings.unfold(),
                        initialVelocity = motion.velocity,
                    )
                }
            }
        } else {
            menuFoldPressGeometry.pinForFold(
                menuBouncePanel, menuPressActive,
                menuEffectiveLift(menuLift.value, menuHover.value),
                Offset(menuPullX.value, menuPullY.value),
            )
            nestedFolding = true
            nestedMotionJobs.replaceLast {
                menuScope.launch {
                    motion.animateTo(
                        0f, FlClashMenuSprings.fold(),
                        initialVelocity = motion.velocity,
                    )
                    if (nestedFolding && nestedPath.isNotEmpty()) {
                        nestedMotionJobs.popCompleted()
                        if (nestedSubScrolls.isNotEmpty()) {
                            nestedSubScrolls.removeAt(nestedSubScrolls.lastIndex)
                        }
                        if (nestedScrollOffsets.isNotEmpty()) {
                            nestedScrollOffsets.removeAt(nestedScrollOffsets.lastIndex)
                        }
                        // In Flutter _pop(), a held header becomes the row
                        // that originally opened this exact child level.
                        // Publish before dropping the path so the same
                        // pointer never sees it as a fresh 400ms press.
                        val foldingDepth = nestedPath.size + 1
                        val returnRow = nestedPath.last()
                        nestedMotionJobs.foldPressHandoff.completed(
                            childDepth = foldingDepth,
                            openingRow = returnRow,
                        )
                        // _pop reassigns _pressed from its header to the
                        // opening parent row during the SAME rebuild. The
                        // parent mask must be ready before the child leaves.
                        if (menuPressActive && gestureDepth == foldingDepth &&
                            gestureHovered == -2) {
                            gestureDepth = foldingDepth - 1
                            gestureHovered = returnRow
                            rootHovered = -1
                        }
                        // Old top at 0 and new parent at 1 yield the SAME
                        // ancestor distance. Stage both state changes before
                        // the suspending snapTo so there is no intermediate
                        // one-frame enlargement/shrinkage in Compose.
                        parentRestoring = true
                        nestedPath = nestedPath.dropLast(1)
                        pendingNestedDepth = -1
                        nestedFolding = false
                        motion.snapTo(1f)
                        parentRestoring = false
                    }
                }
            }
        }
    }
    // FlClash freezes _closingSurface from the TOP submenu level before a
    // route dismissal. Do not fold the child and shrink the root concurrently.
    var latestNested by remember { mutableStateOf<LiveNestedSurface?>(null) }
    var closingNested by remember { mutableStateOf<ClosingNestedSurface?>(null) }

    // The first child is the same _unfold controller later reused by all
    // deeper levels. Reset only for a NEW owner, never for a reversal.
    var firstOwnerForMotion by remember { mutableIntStateOf(-1) }
    LaunchedEffect(submenuOpen, submenuIndex) {
        if (expanded && submenuOpen && submenuIndex >= 0) {
            if (firstOwnerForMotion != submenuIndex) {
                submenuMotion.snapTo(0f)
                firstOwnerForMotion = submenuIndex
            }
            if (nestedPath.isEmpty()) {
                submenuMotion.animateTo(
                    1f, FlClashMenuSprings.unfold(),
                    initialVelocity = submenuMotion.velocity,
                )
                // The spring lands at 1 itself; do not reset it a second time.
            }
        } else if (expanded && submenuIndex >= 0 && nestedPath.isEmpty()) {
            // The child is removed immediately after this spring settles.
            // Freeze the press basis BEFORE that layout change, not after.
            menuFoldPressGeometry.pinForFold(
                menuBouncePanel, menuPressActive,
                menuEffectiveLift(menuLift.value, menuHover.value),
                Offset(menuPullX.value, menuPullY.value),
            )
            submenuMotion.animateTo(
                0f, FlClashMenuSprings.fold(),
                initialVelocity = submenuMotion.velocity,
            )
            if (!submenuOpen) {
                nestedMotionJobs.foldPressHandoff.completed(
                    childDepth = 1, openingRow = submenuIndex,
                )
                if (menuPressActive && gestureDepth == 1 &&
                    gestureHovered == -2) {
                    gestureDepth = 0
                    gestureHovered = submenuIndex
                    rootHovered = submenuIndex
                }
                submenuIndex = -1
                firstOwnerForMotion = -1
                submenuMotion.snapTo(1f)
            }
        }
    }
    BackHandler(enabled = expanded && routeState.mounted && properties.dismissOnBackPress) {
        if (nestedPath.isNotEmpty()) popNested(allowHeaderReverse = false)
        else if (submenuIndex >= 0) submenuOpen = false
        else onDismiss()
    }

    // Flutter CommonPopupRoute drives BOTH directions with the SAME
    // normalized AnimationController. didPop overrides reverseDuration by
    // 1 / current value so the reverse travels its full 500/200ms even
    // when the popup was dismissed before the 450ms open had completed.
    LaunchedEffect(expanded) {
        if (expanded) {
            if (!routeState.mounted) {
                // A NEW route after dismissal: unlike interrupted reversal,
                // this is a completely new menu session.
                nestedPath = emptyList()
                pendingNestedDepth = -1
                nestedMotionJobs.cancelAll()
                nestedScrollOffsets.clear()
                nestedSubScrolls.clear()
                nestedFolding = false
                submenuOpen = false
                submenuIndex = -1
                firstOwnerForMotion = -1
                submenuMotion.snapTo(1f)
                clock.snapTo(0f)
            }
            routeState.mounted = true
            routeState.returning = false
            routeState.closingStartFrame = null
            closingNested = null
            routeState.pendingAction = null
            val remaining = FlClashPopupRouteMotion.openingRemainingMillis(clock.value)
            if (remaining > 0) {
                clock.animateTo(
                    1f, tween(remaining, easing = LinearEasing),
                    initialVelocity = clock.velocity,
                )
            } else {
                clock.snapTo(1f)
            }
        } else if (routeState.mounted) {
            // Flutter _RenderPopupMorph._handleStatusChanged(reverse):
            // freeze the opening frame and topmost surface at the transition.
            // Do NOT discard the actual menu path; a reverse->forward on
            // this same route restores that still-existing hierarchy.
            val top = latestNested?.takeIf {
                submenuIndex == it.index && submenuMotion.value > .001f
            }
            closingNested = top?.let {
                ClosingNestedSurface(
                    owner = it.owner,
                    geometry = it.geometry,
                    progress = it.progress,
                )
            }
            routeState.closingFrom = clock.value.coerceIn(0f, 1f)
            routeState.closingStartFrame = routeState.lastRenderedFrame
            routeState.returning = true
            val remaining = FlClashPopupRouteMotion.reverseTravelMillis(
                routeState.closingFrom, returnIntoSource,
            )
            if (remaining > 0) {
                clock.animateTo(
                    0f, tween(remaining, easing = LinearEasing),
                    initialVelocity = clock.velocity,
                )
            } else {
                clock.snapTo(0f)
            }
            // LaunchedEffect is cancelled on a new expanded=true; therefore
            // this cleanup runs ONLY after a fully completed dismissal.
            routeState.mounted = false
            routeState.returning = false
            nestedPath = emptyList()
            pendingNestedDepth = -1
            nestedMotionJobs.cancelAll()
            nestedScrollOffsets.clear()
            nestedSubScrolls.clear()
            nestedFolding = false
            submenuOpen = false
            submenuIndex = -1
            firstOwnerForMotion = -1
            submenuMotion.snapTo(1f)
            onReturnComplete()
            closingNested = null
            routeState.closingStartFrame = null
            latestNested = null
            val picked = routeState.pendingAction
            routeState.pendingAction = null
            picked?.invoke()
        }
    }
    if (!routeState.mounted || anchor.width <= 0f || anchor.height <= 0f || actions.isEmpty()) return

    // A composable member keeps captures on its receiver instead of generating
    // a content lambda with dozens of DEX parameters (rejected by ART).
    val menuContent = object {
        @androidx.annotation.RequiresApi(33)
        @Composable
        fun androidx.compose.foundation.layout.BoxWithConstraintsScope.Content() {
        val density = LocalDensity.current.density
        val menuFontScale = LocalDensity.current.fontScale
        val width = constraints.maxWidth.toFloat()
        val height = constraints.maxHeight.toFloat()
        val safeBounds = popupSafeBounds(width, height)
        val popupPin = remember(routeState.mounted) { GlassPopupPin() }
        val margin = 16f * density
        val rowHeightDp = menuRowHeightDp(LocalDensity.current.fontScale)
        val cardWidth = minOf(
            menuMeasuredWidth(
                actions, density, LocalDensity.current.fontScale, labelWidthCache,
            ),
            280f * density,
            safeBounds.width - margin * 2,
        ).coerceAtLeast(1f)
        // TextMeasurer resolves one vs two subtitle lines before layout:
        // the exact heights drive scrolling, morph anchors and hit testing.
        // Recreated onClick lambdas do not invalidate the row measurement.
        val rootRows = rememberMenuContentRows(
            actions, menuFontScale, cardWidth, density,
        )
        val cardHeight = minOf(
            (minOf(rootRows.totalHeightDp,
                (rootVisibleItems + .5f) * rowHeightDp) + 18f) * density,
            safeBounds.height - margin * 2,
        )
            .coerceAtLeast(1f)
        val cardPosition = menuPopupPosition(
            anchor, cardWidth, cardHeight, width, height, density,
            placement, avoid, safeBounds, popupPin,
        )
        val card = Rect(
            cardPosition.x, cardPosition.y,
            cardPosition.x + cardWidth, cardPosition.y + cardHeight,
        )
        val checkedRootIndex = actions.indexOfFirst { it.selected }
        val rootViewportHeightPx = (cardHeight - 18f * density).coerceAtLeast(0f)
        LaunchedEffect(
            expanded, checkedRootIndex, rootRows, cardHeight, density,
            rootIsLazy, rootVisibleItems,
        ) {
            if (expanded) {
                if (rootIsLazy) {
                    val initialPx = menuLazyCheckedInitialScrollPx(
                        checkedRootIndex, actions.size, rowHeightDp * density,
                        rootVisibleItems, rootViewportHeightPx,
                    )
                    val start = menuLazyItemPosition(
                        initialPx, actions.size, rowHeightDp * density,
                    )
                    if (actions.isNotEmpty()) {
                        rootLazyScroll.scrollToItem(start.index, start.scrollOffset)
                    }
                } else {
                    rootScroll.scrollTo(menuCheckedInitialScrollPx(
                        checkedRootIndex, rootRows, density, rootViewportHeightPx,
                    ))
                }
            }
        }
        // Gesture hit geometry, child origin, and Sketch content cache MUST
        // all use the same absolute scroll offset for either renderer.
        val rootScrollPx = if (rootIsLazy) menuLazyScrollOffsetPx(
            rootLazyScroll.firstVisibleItemIndex,
            rootLazyScroll.firstVisibleItemScrollOffset,
            rowHeightDp * density,
        ) else rootScroll.value.toFloat()
        val motion = remember(anchor, card, density, returnIntoSource, routeState, clock) {
            androidx.compose.runtime.derivedStateOf {
                val openingTime = FlClashPopupRouteMotion.openingTimeSeconds(
                    if (routeState.returning) routeState.closingFrom else clock.value,
                )
                val opening = menuOpeningFrame(openingTime, anchor, card, density)
                val closeTime = FlClashPopupRouteMotion.dismissalTimeSeconds(
                    clock.value, routeState.closingFrom, returnIntoSource,
                )
                val returnSource = if (routeState.returning && closingNested != null) {
                    val top = closingNested!!.geometry
                    MenuMorphFrame(
                        platter = top.panel,
                        radius = top.radius,
                        droplet = menuRect(
                            mixOffset(anchor.center, top.panel.center, .25f),
                            anchor.width * .25f, anchor.height * .25f,
                        ),
                        blend = 28f * density,
                        settled = 1f,
                        glyphAlpha = 0f,
                        contentAlpha = 1f,
                    )
                } else routeState.closingStartFrame ?: opening
                val frame = if (routeState.returning && !returnIntoSource) {
                    // The ordinary popup does NOT become a source droplet.
                    routeState.closingStartFrame ?: opening
                } else if (routeState.returning) {
                    menuReturningFrame(closeTime, returnSource, anchor, density)
                } else {
                    // FlClash _RenderPopupMorph.paint when status != reverse:
                    // _frame() reads the SAME controller.value, never a second
                    // crossfade or an independent opening spring.
                    opening
                }
                val active = if (routeState.returning && !returnIntoSource)
                    routeState.closingFrom < 1f else if (routeState.returning) returnIntoSource
                    else clock.value < 1f && submenuIndex < 0
                MenuRootMotion(frame, active,
                    menuRootCornerPower(frame.settled, routeState.returning, closeTime),
                    routeState.returning && returnIntoSource)
            }
        }
        // Only discrete visibility/hit thresholds participate in composition.
        val hitReady by remember(motion) { androidx.compose.runtime.derivedStateOf {
            motion.value.frame.settled > .90f
        } }
        val rowsReady by remember(motion) { androidx.compose.runtime.derivedStateOf {
            motion.value.frame.settled > .86f
        } }
        val glyphVisible by remember(motion) { androidx.compose.runtime.derivedStateOf {
            val f = motion.value.frame
            f.glyphAlpha > .002f && f.droplet.width > 0f && f.droplet.height > 0f
        } }
        val contentVisible by remember(motion) { androidx.compose.runtime.derivedStateOf {
            motion.value.frame.contentAlpha > .002f
        } }
        // The legacy bitmap renderer still needs values during composition.
        // Sketch reads the live frame in draw/measure/graphicsLayer instead.
        val frame = if (sketchBase == null) motion.value.frame else remember(anchor, card, density) {
            menuOpeningFrame(FlClashPopupRouteMotion.openingTimeSeconds(1f), anchor, card, density)
        }
        if (sketchBase == null) SideEffect { routeState.lastRenderedFrame = frame }

        // FlClash _buildLevel uses a shared unbounded top spring: every
        // ancestor recedes by (number of cards above - 1) + _unfold.value.
        val topMenuDepth = if (!routeState.returning && submenuIndex >= 0)
            1 + nestedPath.size else 0
        // FlClash resets its spring BEFORE publishing the new path.
        // Force the exact same first visual state while Compose awaits snapTo.
        val topUnfold = menuVisibleTopUnfold(
            springValue = submenuMotion.value,
            topDepth = topMenuDepth,
            firstIndex = submenuIndex,
            animatedFirstIndex = firstOwnerForMotion,
            firstOpening = submenuOpen,
            pendingNestedDepth = pendingNestedDepth,
            restoringParent = parentRestoring,
        )
        val nestedProgress = if (topMenuDepth > 0)
            menuRecedingDistance(0, topMenuDepth, topUnfold) else 0f
        val firstProgress = if (topMenuDepth >= 1)
            menuLevelProgress(1, topMenuDepth, topUnfold) else topUnfold
        val firstDistance = if (topMenuDepth > 1)
            menuRecedingDistance(1, topMenuDepth, topUnfold) else 0f
        // The original _platter disappears after the opening route settles:
        // the root PopupMenuCard owns the glass from then on. A submenu must
        // never receive both a full-screen popup platter and a card pane.
        // Keep the platter for reverse transitions into the source control.
        val rootPlatterActive by remember(motion) {
            androidx.compose.runtime.derivedStateOf { motion.value.active }
        }
        val selectedRow = if (submenuIndex >= 0) {
            val top = card.top + (9f + rootRows.itemTopDp(submenuIndex)) * density -
                rootScrollPx
            Rect(
                card.left + 8f * density, top,
                card.right - 8f * density,
                top + rootRows.itemHeightDp(submenuIndex) * density,
            )
        } else Rect.Zero
        // The underlying level recedes from the selected item's top-right.
        // This is local to the menu: the rest of the scene is not dimmed.
        // Flutter Transform(alignment: topRight, origin: step.itemTop).
        val pivotY = if (submenuIndex >= 0) selectedRow.top else card.top
        val gestureLevels = mutableListOf<GlassMenuHitLevel>()
        // Flutter ElasticPress uses the WHOLE menu Stack, not the top card.
        // Record final card layout bounds before any spring-driven painting;
        // the press pivot must not follow the moving child silhouette.
        val pressStackCards = mutableListOf(card)
        fun menuPressPanel(): Rect = menuFoldPressGeometry.panel(
            menuFlutterStackLayoutBounds(card, pressStackCards.drop(1)),
        )
        fun currentMenuPressTransform() = menuElasticTransform(
            menuPressPanel(), menuEffectiveLift(menuLift.value, menuHover.value),
            Offset(menuPullX.value, menuPullY.value),
            16f * density,
        )
        if (expanded && !routeState.returning && hitReady) {
            gestureLevels += GlassMenuHitLevel(
                depth = 0,
                bounds = card,
                actions = actions,
                rowTopPx = card.top + 9f * density - rootScrollPx,
                rowHeightPx = rowHeightDp * density,
                rowOffsetsPx = rootRows.rowStartOffsetsPx(density),
                rowHeightsPx = rootRows.rowHeightsPx(density),
                rowViewportTopPx = card.top + 9f * density,
                rowViewportBottomPx = card.bottom - 9f * density,
                scrollable = (rootRows.totalHeightDp + 18f) * density >
                    card.height + .5f,
                paintPivot = Offset(card.right, pivotY),
                paintScale = menuRecedingScale(nestedProgress),
            )
        }
        // Keep the surrounding scene at its original brightness.
        // This transparent hit target only dismisses taps outside the menu.
        if (properties.dismissOnClickOutside) GlassPopupDismissLayer(properties.modal, onDismiss)
        else if (properties.modal) GlassPopupDismissLayer(true) {}

        // FlClash wraps the whole Stack in ElasticPress(enabled=!platter).
        // One matrix swells all cards and their shadows together; the full
        // scene gesture recognizer below stays untransformed and inverses it.
        Box(Modifier.fillMaxSize().graphicsLayer {
            val matrix = currentMenuPressTransform()
            transformOrigin = TransformOrigin(
                (matrix.center.x / size.width.coerceAtLeast(1f)).coerceIn(0f, 1f),
                (matrix.center.y / size.height.coerceAtLeast(1f)).coerceIn(0f, 1f),
            )
            scaleX = matrix.scaleX
            scaleY = matrix.scaleY
            translationX = matrix.pull.x
            translationY = matrix.pull.y
        }) {
        Box(
            Modifier.fillMaxSize().graphicsLayer {
                transformOrigin = TransformOrigin(
                    (card.right / width).coerceIn(0f, 1f),
                    (pivotY / height).coerceIn(0f, 1f),
                )
                scaleX = menuRecedingScale(nestedProgress)
                scaleY = menuRecedingScale(nestedProgress)
            }.then(
                if (sketchBase != null) Modifier.layerBackdrop(sketchRootLayer)
                else Modifier
            ),
        ) {
        val p = frame.platter
        val d = frame.droplet
        if (sketchBase != null) {
            SketchMenuRootSurface(motion, card, androidx.compose.ui.geometry.Size(width, height),
                density, dark, sketchBase, topMenuDepth > 0, nestedProgress, routeState)
        } else {
        val rootPower = motion.value.power
        // FlClash _RenderPopupMorph._paintFrame draws the exterior shadow
        // throughout the morph, weighted by settled. Draw it once in the
        // FULL menu-root coordinate space, never inside the 36dp-padded
        // Sketch pane: 29dp blur spans farther and would be edge-clipped.
        // This keeps the glass pane/recording/sampling bounds unchanged.
        val rootShadowSurface = if (rootPlatterActive) p else card
        val rootShadowRadius = if (rootPlatterActive) frame.radius else 24f * density
        // _RenderPopupMorph._paintFrame/_paintReturn cast the shadow from
        // the SAME outer Goo silhouette as the painted glass, including the
        // traveling source droplet and neck before they merge. Once the
        // platter steps aside, use only the settled card outline.
        val rootShadowOutline = remember(
            rootShadowSurface, rootShadowRadius, rootPower,
            rootPlatterActive, d, frame.blend, density,
        ) {
            if (rootPlatterActive) {
                gooPairOutline(
                    rootShadowSurface, rootShadowRadius, d, frame.blend,
                    density = density, coarse = false,
                    platterCornerPower = rootPower,
                ).outline
            } else gooPlatterOutline(rootShadowSurface, rootShadowRadius, rootPower)
        }
        MenuCardShadow(
            path = rootShadowOutline,
            density = density,
            dark = dark,
            strength = menuCardShadowStrength(
                progress = frame.settled,
                receding = topMenuDepth > 0,
                distance = nestedProgress,
            ),
            modifier = Modifier.fillMaxSize(),
            clipInterior = rootPlatterActive,
        )
        // One AGSL distance field joins the swollen menu platter and the small
        // carried source droplet. The bounds are unchanged: Sketch samples
        // only the glass material, not the outer shadow's blur reach.
        val pad = 36f * density
        val bounds = Rect(
            (minOf(p.left, d.left) - pad).coerceAtLeast(0f),
            (minOf(p.top, d.top) - pad).coerceAtLeast(0f),
            (maxOf(p.right, d.right) + pad).coerceAtMost(width),
            (maxOf(p.bottom, d.bottom) + pad).coerceAtMost(height),
        )
        if (bounds.width > 0f && bounds.height > 0f) {
            Box(
                Modifier.offsetPx(bounds.left, bounds.top)
                    .size(
                        with(LocalDensity.current) { bounds.width.toDp() },
                        with(LocalDensity.current) { bounds.height.toDp() },
                    ),
            ) {
                val material = GlassStyle.interpolate(
                    GlassStyle.regular(dark),
                    GlassStyle.menu(dark),
                    frame.settled,
                )
                val localPlatter = Rect(
                    p.left - bounds.left, p.top - bounds.top,
                    p.right - bounds.left, p.bottom - bounds.top,
                )
                val stableCard = Rect(
                    card.left - bounds.left, card.top - bounds.top,
                    card.right - bounds.left, card.bottom - bounds.top,
                )
                // The original route's platter and its root card are
                // mutually exclusive. Both use the same pane, never two
                // refracting layers in the same rectangle.
                val rootSurface = if (rootPlatterActive) localPlatter else stableCard
                val rootRadius = if (rootPlatterActive) frame.radius else 24f * density
                // After the opening droplet is absorbed, the menu card outline
                // is stationary even while rows scroll or child springs run.
                // Do not refit its RSuperellipse on every unrelated frame.
                val stableCardOutline = remember(rootPlatterActive, rootSurface, rootRadius) {
                    if (rootPlatterActive) null else GlassOutlineRect(
                        rootSurface, rootRadius,
                        gooPlatterOutline(rootSurface, rootRadius),
                    )
                }
                val localMorphOutline = remember(rootShadowOutline, bounds, rootPlatterActive) {
                    if (rootPlatterActive) android.graphics.Path(rootShadowOutline).apply {
                        offset(-bounds.left, -bounds.top)
                    } else null
                }
                GlassPane(
                    Modifier.fillMaxSize(),
                    style = material.copy(
                        menuRimSide = 0f,
                        menuRimCorner = 0f,
                        menuRimFlat = 0f,
                        menuRimShine = 0f,
                    ),
                    morph = if (rootPlatterActive) GlassMorphShape(
                        platter = localPlatter,
                        radius = frame.radius,
                        droplet = Rect(
                            d.left - bounds.left, d.top - bounds.top,
                            d.right - bounds.left, d.bottom - bounds.top,
                        ),
                        blend = frame.blend,
                        cornerPower = rootPower,
                        nativePath = localMorphOutline,
                    ) else null,
                    outline = stableCardOutline,
                )
                // Always visible, even if AGSL/backdrop capture fails.
                // Follow exactly the same platter during opening and return.
                // Original _paintFrame paints the rim only once the source
                // droplet is absorbed into the platter, ramping over 8dp of
                // containment depth; never draw the platter's full rim
                // through the still-separated goo connection.
                val depth = gooPairDepth(p, frame.radius, d, frame.blend)
                val rimMerge = if (rootPlatterActive)
                    (depth / (8f * density)).coerceIn(0f, 1f) else 1f
                GlassMenuRim(
                    modifier = Modifier.fillMaxSize(),
                    platter = rootSurface,
                    radius = rootRadius,
                    style = material,
                    // The root recedes under an opened sublevel. Its own
                    // high-contrast hairline must disappear rather than
                    // tracing a second corner outside the child card.
                    settled = menuRootRimStrength(
                        settled = frame.settled,
                        merged = rimMerge,
                        returningToSource = routeState.returning && returnIntoSource,
                    ),
                    cornerPower = rootPower,
                )
            }
        }

        } // Legacy surface; Sketch animation stays in draw/measure.
        // The source snapshot contains ONLY foreground content, captured
        // outside the glass layer. FlClash similarly uses withoutPanes().
        // If the readback is unavailable, recompose sourceContent directly.
        // Never draw a second source GlassPane onto this Goo silhouette.
        if (glyphVisible) {
            Box(
                Modifier.offset {
                    val drop = motion.value.frame.droplet
                    IntOffset((drop.center.x - anchor.width * .5f).roundToInt(),
                        (drop.center.y - anchor.height * .5f).roundToInt())
                }
                    .size(
                        with(LocalDensity.current) { anchor.width.toDp() },
                        with(LocalDensity.current) { anchor.height.toDp() },
                    )
                    .graphicsLayer {
                        val live = motion.value.frame
                        scaleX = live.droplet.width / anchor.width
                        scaleY = live.droplet.height / anchor.height
                        alpha = live.glyphAlpha
                    },
                contentAlignment = Alignment.Center,
            ) {
                if (sourceContentSnapshot != null &&
                    kotlin.math.abs(sourceContentSnapshot.widthPx - anchor.width) <= 2f &&
                    kotlin.math.abs(sourceContentSnapshot.heightPx - anchor.height) <= 2f
                ) {
                    Image(
                        bitmap = sourceContentSnapshot.image,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.FillBounds,
                    )
                } else {
                    sourceContent()
                }
            }
        }

        // Keep the content at its final measured size, transforming the whole
        // layer into the spring-driven platter; don't reflow rows into tiny
        // 20px-wide layouts mid-transition.
        val childSource = if (routeState.returning) closingNested else null
        val childOpacity = frame.contentAlpha
        if (childSource != null && contentVisible) {
            GlassReturningSubmenuContent(
                geometry = childSource.geometry,
                owner = childSource.owner,
                dark = dark,
                framePlatter = frame.platter,
                contentAlpha = childOpacity,
                liveFrame = { motion.value.frame },
            )
        }
        val rootOpacity = if (routeState.returning && closingNested != null) 0f
            else frame.contentAlpha
        val rootActionVersion = remember(actions) {
            actions.map {
                listOf(it.text, it.subtitle, it.glyph, it.symbol,
                    it.selected.toString(), it.section?.title)
            }
        }
        if (contentVisible && !(routeState.returning && closingNested != null)) {
            GlassCaptureBehind(
                Modifier.offsetPx(card.left, card.top)
                    .size(
                        with(LocalDensity.current) { card.width.toDp() },
                        with(LocalDensity.current) { card.height.toDp() },
                    )
                    .graphicsLayer {
                        transformOrigin = TransformOrigin.Center
                        val live = motion.value.frame
                        val platter = live.platter
                        scaleX = (platter.width / card.width).coerceAtLeast(.001f)
                        scaleY = (platter.height / card.height).coerceAtLeast(.001f)
                        translationX = platter.center.x - card.center.x
                        translationY = platter.center.y - card.center.y
                        alpha = live.contentAlpha
                        clip = true
                        shape = RoundedCornerShape(24.dp)
                    },
                // The recording wrapper already includes this card's
                // graphicsLayer alpha and rounded-rectangle clip.
                contentVersion = listOf(
                    dark, submenuIndex, rootHovered, rootScrollPx,
                    nestedProgress, rootOpacity, frame.settled, p,
                    rootActionVersion,
                ),
            ) {
                // One shared row renderer: LazyColumn composes only visible
                // indices; regular menus keep their section/variable-height
                // Column and the same press/semantics/gesture behavior.
                val renderRootRow: @Composable (Int) -> Unit = { index ->
                        val action = actions[index]
                        MenuSectionDecoration(index, action.section, dark, menuFontScale)
                        val pressSource = remember(index) { MutableInteractionSource() }
                        val pressed by pressSource.collectIsPressedAsState()
                        Row(
                            Modifier.fillMaxWidth()
                                .height(rootRows.itemHeightDp(index).dp)
                                .menuCheckedSemantics(action)
                                .graphicsLayer {
                                    alpha = when {
                                        index == submenuIndex -> 0f
                                        !menuActionEnabled(action) -> .30f
                                        else -> 1f
                                    }
                                }
                                .clip(MenuPressedRowSuperellipse)
                                .background(
                                    if ((pressed || rootHovered == index) &&
                                        expanded && submenuIndex < 0)
                                        (if (dark) Color.White else Color.Black)
                                            .copy(alpha = if (dark) .14f else .075f)
                                    else Color.Transparent,
                                )
                                .clickable(
                                    interactionSource = pressSource,
                                    indication = null,
                                    role = Role.Button,
                                    enabled = expanded && rowsReady &&
                                        submenuIndex < 0 && menuActionEnabled(action),
                                ) {
                                    if (action.subActions.isNotEmpty()) {
                                        submenuIndex = index
                                        submenuOpen = true
                                    } else {
                                        action.onClick?.let {
                                            routeState.pendingAction = it
                                            onDismiss()
                                        }
                                    }
                                },
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            MenuItemContent(action, dark)
                        }
                }
                if (rootIsLazy) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                            .padding(horizontal = 8.dp, vertical = 9.dp),
                        state = rootLazyScroll,
                        // Gestures stay on the full-scene coordinator; using
                        // a second native scroll detector would race it.
                        userScrollEnabled = false,
                    ) {
                        items(count = actions.size, key = { it }) { index ->
                            renderRootRow(index)
                        }
                    }
                } else {
                    Column(
                        Modifier.fillMaxSize()
                            .padding(horizontal = 8.dp, vertical = 9.dp)
                            .verticalScroll(rootScroll),
                        verticalArrangement = Arrangement.Top,
                    ) {
                        actions.indices.forEach { index -> renderRootRow(index) }
                    }
                }
            }
        }
        if (submenuIndex >= 0 && nestedProgress > .001f) {
            val recedingOutline = remember(p.width, p.height, frame.radius) {
                gooPlatterOutline(
                    Rect(0f, 0f, p.width, p.height), frame.radius,
                ).asComposePath()
            }
            // A receding card gets its own faint scrim, not a global shade.
            Box(
                Modifier.offsetPx(p.left, p.top)
                    .size(
                        with(LocalDensity.current) { p.width.toDp() },
                        with(LocalDensity.current) { p.height.toDp() },
                    )
                    .drawWithContent {
                        clipPath(recedingOutline) {
                            this@drawWithContent.drawContent()
                        }
                    }
                    .background(
                        menuRecedingSurfaceColor(dark, glassColors(dark).surface)
                            .copy(alpha = menuRecedingDim(nestedProgress)),
                    )
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = { submenuOpen = false },
                    ),
            )
        }
        // Pointer ownership is handled by the stable full-scene overlay.
        } // The first level and its local back-layer form one receding surface.

        if (!routeState.returning && submenuIndex in actions.indices) {
            val firstOwner = actions[submenuIndex]
            val firstEstimatedTarget = remember(
                card, selectedRow, firstOwner.subActions, width, height,
                density, menuFontScale, labelWidthCache,
            ) {
                glassSubmenuGeometry(
                    card, selectedRow, firstOwner.subActions.size,
                    1f, width, height, density,
                    items = firstOwner.subActions,
                    fontScale = menuFontScale,
                    labelWidthCache = labelWidthCache,
                ).target
            }
            val firstRows = rememberMenuContentRows(
                firstOwner.subActions, menuFontScale, firstEstimatedTarget.width, density,
            )
            val firstHeaderHeightDp = rememberMenuContentRows(
                listOf(firstOwner), menuFontScale, firstEstimatedTarget.width, density,
            ).itemHeightDp(0)
            val firstTarget = remember(
                card, selectedRow, firstOwner.subActions, width, height,
                density, menuFontScale, labelWidthCache,
                firstRows, firstHeaderHeightDp,
            ) {
                glassSubmenuGeometry(
                    card, selectedRow, firstOwner.subActions.size,
                    1f, width, height, density,
                    items = firstOwner.subActions,
                    fontScale = menuFontScale,
                    labelWidthCache = labelWidthCache,
                    contentRows = firstRows,
                    headerHeightDp = firstHeaderHeightDp,
                ).target
            }
            pressStackCards.add(firstTarget)
            val firstGeometry = glassSubmenuInterpolatedShape(
                selectedRow, firstTarget, firstProgress, density,
            )
            // Freeze the actual already-calculated top card for a direct
            // return. Avoid a THIRD text-width/layout calculation per frame.
            if (expanded) {
                val snapshot = LiveNestedSurface(
                    submenuIndex, firstGeometry, firstProgress, firstOwner,
                )
                SideEffect { latestNested = snapshot }
            }
            GlassSubmenu(
                parent = card,
                origin = selectedRow,
                owner = firstOwner,
                dark = dark,
                progress = firstProgress,
                screenWidth = width,
                screenHeight = safeBounds.bottom,
                receding = firstDistance,
                interactive = nestedPath.isEmpty() && submenuOpen,
                sharedScroll = firstSubScroll,
                labelWidthCache = labelWidthCache,
                sketchBackdropOverride = sketchFirstInput,
                sketchRecordedBackdrop = if (sketchBase != null) sketchFirstLayer else null,
                gestureHoveredRow = if (gestureDepth == 1) gestureHovered else -1,
                onBack = {
                    if (nestedPath.isNotEmpty()) popNested()
                    else submenuOpen = !submenuOpen
                },
                onNested = { index, scroll -> pushNested(index, scroll) },
                onSelected = { picked ->
                    routeState.pendingAction = picked
                    onDismiss()
                },
            )
            // FlClash _toggleTop remains hittable during the fold. Keep
            // header hit-testing alive after the rows stop being interactive.
            if (firstProgress > .08f) {
                val rowsReady = submenuOpen && nestedPath.isEmpty() && firstProgress >= .87f
                val headerY = glassSubmenuHeaderTopPx(
                    selectedRow.top, firstGeometry.target.top,
                    firstProgress, 8f * density,
                )
                gestureLevels += GlassMenuHitLevel(
                    depth = 1,
                    bounds = firstGeometry.panel,
                    actions = firstOwner.subActions,
                    rowsEnabled = rowsReady,
                    rowTopPx = headerY +
                        (17f + firstHeaderHeightDp) * density - firstSubScroll.value,
                    rowHeightPx = rowHeightDp * density,
                    rowOffsetsPx = firstRows.rowStartOffsetsPx(density),
                    rowHeightsPx = firstRows.rowHeightsPx(density),
                    rowViewportTopPx = headerY + firstHeaderHeightDp * density,
                    rowViewportBottomPx = firstGeometry.panel.bottom - 8f * density,
                    headerTopPx = headerY,
                    headerHeightPx = firstHeaderHeightDp * density,
                    scrollable = (
                        firstRows.totalHeightDp + firstHeaderHeightDp + 33f
                    ) * density > firstGeometry.target.height + .5f,
                    paintPivot = Offset(firstGeometry.target.right, selectedRow.top),
                    paintScale = menuRecedingScale(firstDistance),
                )
            }
            // Further levels are siblings in the SAME root pixel space,
            // rather than nesting the drawing boxes and accumulating
            // translation errors. Each level has its own spring/pressed row.
            var parentCard = firstGeometry.target
            var parentActions = firstOwner.subActions
            var parentHeaderHeightDp = firstHeaderHeightDp
            for (level in nestedPath.indices) {
                val index = nestedPath[level]
                val owner = parentActions.getOrNull(index) ?: break
                val parentRows = rememberMenuContentRows(
                    parentActions, menuFontScale, parentCard.width, density,
                )
                val top = parentCard.top +
                    (25f + parentHeaderHeightDp + parentRows.itemTopDp(index)) * density -
                    (nestedScrollOffsets.getOrNull(level) ?: 0f)
                val row = Rect(
                    parentCard.left + 8f * density, top,
                    parentCard.right - 8f * density,
                    top + parentRows.itemHeightDp(index) * density,
                )
                val ownerEstimatedTarget = remember(
                    parentCard, row, owner.subActions, width, height,
                    density, menuFontScale, labelWidthCache,
                ) {
                    glassSubmenuGeometry(
                        parentCard, row, owner.subActions.size,
                        1f, width, height, density,
                        items = owner.subActions,
                        fontScale = menuFontScale,
                        labelWidthCache = labelWidthCache,
                    ).target
                }
                val ownerRows = rememberMenuContentRows(
                    owner.subActions, menuFontScale, ownerEstimatedTarget.width, density,
                )
                val ownerHeaderHeightDp = rememberMenuContentRows(
                    listOf(owner), menuFontScale, ownerEstimatedTarget.width, density,
                ).itemHeightDp(0)
                val cardDepth = level + 2
                val motion = menuLevelProgress(cardDepth, topMenuDepth, topUnfold)
                val distance = menuRecedingDistance(cardDepth, topMenuDepth, topUnfold)
                val target = remember(
                    parentCard, row, owner.subActions, width, height,
                    density, menuFontScale, labelWidthCache,
                    ownerRows, ownerHeaderHeightDp,
                ) {
                    glassSubmenuGeometry(
                        parentCard, row, owner.subActions.size,
                        1f, width, height, density,
                        items = owner.subActions,
                        fontScale = menuFontScale,
                        labelWidthCache = labelWidthCache,
                        contentRows = ownerRows,
                        headerHeightDp = ownerHeaderHeightDp,
                    ).target
                }
                pressStackCards.add(target)
                val geometry = glassSubmenuInterpolatedShape(
                    row, target, motion, density,
                )
                // All source panes are from PREVIOUS depths only. The
                // active card never samples itself or a future sibling.
                val sketchNestedInput = if (sketchBase != null) {
                    val previousLayers = precedingSketchLayers<Backdrop>(
                        background = sketchBase,
                        root = sketchRootLayer,
                        first = sketchFirstLayer,
                        nested = sketchNestedLayers,
                        nestedIndex = level,
                    )
                    rememberCombinedBackdrop(*previousLayers.toTypedArray())
                } else null
                GlassSubmenu(
                    parent = parentCard,
                    origin = row,
                    owner = owner,
                    dark = dark,
                    progress = motion,
                    screenWidth = width,
                    screenHeight = safeBounds.bottom,
                    receding = distance,
                    interactive = level == nestedPath.lastIndex && !nestedFolding,
                    sharedScroll = nestedSubScrolls.getOrNull(level),
                    labelWidthCache = labelWidthCache,
                    sketchBackdropOverride = sketchNestedInput,
                    sketchRecordedBackdrop = if (sketchBase != null)
                        sketchNestedLayers[level] else null,
                    gestureHoveredRow = if (gestureDepth == level + 2)
                        gestureHovered else -1,
                    onBack = { popNested() },
                    onNested = { index, scroll -> pushNested(index, scroll) },
                    onSelected = { picked ->
                        routeState.pendingAction = picked
                        onDismiss()
                    },
                )
                if (motion > .08f) {
                    val rowsReady = !nestedFolding &&
                        level == nestedPath.lastIndex && motion >= .87f
                    val rowScroll = nestedSubScrolls.getOrNull(level)?.value ?: 0
                    val headerY = glassSubmenuHeaderTopPx(
                        row.top, geometry.target.top, motion, 8f * density,
                    )
                    gestureLevels += GlassMenuHitLevel(
                        depth = level + 2,
                        bounds = geometry.panel,
                        actions = owner.subActions,
                        rowsEnabled = rowsReady,
                        rowTopPx = headerY +
                            (17f + ownerHeaderHeightDp) * density - rowScroll,
                        rowHeightPx = rowHeightDp * density,
                        rowOffsetsPx = ownerRows.rowStartOffsetsPx(density),
                        rowHeightsPx = ownerRows.rowHeightsPx(density),
                        rowViewportTopPx = headerY + ownerHeaderHeightDp * density,
                        rowViewportBottomPx = geometry.panel.bottom - 8f * density,
                        headerTopPx = headerY,
                        headerHeightPx = ownerHeaderHeightDp * density,
                        scrollable = (
                            ownerRows.totalHeightDp + ownerHeaderHeightDp + 33f
                        ) * density > geometry.target.height + .5f,
                        paintPivot = Offset(geometry.target.right, row.top),
                        paintScale = menuRecedingScale(distance),
                    )
                }
                if (motion > .001f) {
                    val snapshot = LiveNestedSurface(
                        submenuIndex, geometry, motion, owner,
                    )
                    SideEffect { latestNested = snapshot }
                }
                parentCard = geometry.target
                parentActions = owner.subActions
                parentHeaderHeightDp = ownerHeaderHeightDp
            }
        }

        } // End ElasticPress visual stack; hit overlay stays in root space.
        // All mounted target cards have now entered pressStackCards.
        SideEffect { menuBouncePanel = menuPressPanel() }

        // ONE pointerInput remains mounted above every card while the menu
        // navigates its hierarchy. Its pointer ID never changes during
        // 400ms dwell -> unfold -> drag into child -> release selection.
        if (expanded && !routeState.returning && hitReady) {
            fun selectAt(depth: Int, index: Int, onlyUnfold: Boolean) {
                if (!canActivateMenuRow(
                        levels = gestureLevels, depth = depth, index = index,
                        firstSubmenuIndex = submenuIndex,
                        firstOpen = submenuOpen, nestedDepth = nestedPath.size,
                        nestedFolding = nestedFolding,
                    )) return
                val level = gestureLevels.lastOrNull() ?: return
                val picked = level.actions[index]
                if (picked.subActions.isNotEmpty()) {
                    if (depth == 0) {
                        if (submenuIndex < 0) {
                            submenuIndex = index
                            submenuOpen = true
                        }
                    } else if (depth == nestedPath.size + 1) {
                        val scroll = if (depth == 1) firstSubScroll.value
                            else nestedSubScrolls.getOrNull(depth - 2)?.value ?: 0
                        pushNested(index, scroll.toFloat())
                    }
                } else if (!onlyUnfold) {
                    picked.onClick?.let {
                        routeState.pendingAction = it
                        onDismiss()
                    }
                }
            }
            GlassMenuGestureSurface(
                modifier = Modifier.fillMaxSize(),
                modal = properties.modal,
                dismissOnClickOutside = properties.dismissOnClickOutside,
                levels = gestureLevels,
                onHover = { depth, index ->
                    gestureDepth = depth
                    gestureHovered = index
                    rootHovered = if (depth == 0) index else -1
                },
                onSelect = { depth, index -> selectAt(depth, index, false) },
                onUnfold = { depth, index -> selectAt(depth, index, true) },
                onBack = {
                    // FlClash _pop: tapping the covered parent, like system
                    // Back, never reverses a menu already folding.
                    if (nestedPath.isNotEmpty()) popNested(allowHeaderReverse = false)
                    else if (submenuIndex >= 0 && submenuOpen) submenuOpen = false
                    else if (submenuIndex < 0) onDismiss()
                },
                onHeaderToggle = {
                    // FlClash _toggleTop: the CURRENT title folds on dwell
                    // or release, and re-opens if pressed mid-fold.
                    if (nestedPath.isNotEmpty()) popNested(allowHeaderReverse = true)
                    else if (submenuIndex >= 0) submenuOpen = !submenuOpen
                },
                headerFolding = if (nestedPath.isNotEmpty()) nestedFolding
                    else submenuIndex >= 0 && !submenuOpen,
                onDismiss = onDismiss,
                carriedPress = carriedPress,
                foldPressHandoff = nestedMotionJobs.foldPressHandoff,
                pressEnabled = !rootPlatterActive,
                pointToMenu = { point -> currentMenuPressTransform().inverse(point) },
                onPressStart = { startMenuPress() },
                onPressMove = { point ->
                    // Flutter _localOf converts global fingers through the
                    // CURRENT ElasticPress transform before pullPastEdges.
                    moveMenuPress(
                        currentMenuPressTransform().inverse(point),
                        menuPressPanel(),
                    )
                },
                onPressEnd = { endMenuPress() },
                onMenuPointerHover = { inside -> updateMenuHover(inside) },
                onScroll = { depth, delta ->
                    scrollConflator.offer(depth, delta)
                },
                onFling = { depth, velocity ->
                    scrollConflator.offerFling(depth, velocity)
                },
                onGestureStart = {
                    // A new Down interrupts the old momentum before it can
                    // drift the hit position under a fresh press/dwell.
                    activeScrollFling?.cancel()
                    scrollConflator.clear()
                    // Invalidate late fling callbacks before the next press;
                    // its existing Y position and velocity remain continuous.
                    menuMorphMomentum.beginGesture()
                },
            )
        }
        }
    }
    BoxWithConstraints(
        modifier.fillMaxSize()
            .expandedTouchInput(0.dp, properties.modal, shareWithSiblings = !properties.modal) {}
            .glassPopupFocus(properties.focusable, "菜单").graphicsLayer {
            if (routeState.returning && !returnIntoSource) {
                // Flutter's _PopupMorph.paint with no source image:
                // Curves.easeOut 200ms, scale 1 -> .8, opacity 1 -> 0.
                val vanish = menuNoSourceDismissFraction(
                    FlClashPopupRouteMotion.dismissalTimeSeconds(
                        clock.value, routeState.closingFrom, false,
                    ),
                )
                val fullWidth = size.width.coerceAtLeast(1f)
                val fullHeight = size.height.coerceAtLeast(1f)
                transformOrigin = TransformOrigin(
                    (anchor.center.x / fullWidth).coerceIn(0f, 1f),
                    (anchor.center.y / fullHeight).coerceIn(0f, 1f),
                )
                scaleX = 1f - .2f * vanish
                scaleY = 1f - .2f * vanish
                alpha = 1f - vanish
            } else {
                // A cancelled plain dismissal must restore these properties
                // on the very next forward frame (no sticky graphicsLayer).
                transformOrigin = TransformOrigin.Center
                scaleX = 1f
                scaleY = 1f
                alpha = 1f
            }
        },
    ) {
        with(menuContent) { Content() }
    }

}

/**
 * A receding parent must never select an action while its child grows or
 * folds. Restrict selection to the actual current top level, independent of
 * how many levels have already crossed the 0.87f hit-test threshold.
 */
/** Flutter _pop ignores another Back during fold; _toggleTop reverses it. */
internal fun ignoreRepeatedNestedBack(
    folding: Boolean,
    headerToggle: Boolean,
): Boolean = folding && !headerToggle

internal fun canActivateMenuRow(
    levels: List<GlassMenuHitLevel>,
    depth: Int,
    index: Int,
    firstSubmenuIndex: Int,
    firstOpen: Boolean,
    nestedDepth: Int,
    nestedFolding: Boolean,
): Boolean {
    val top = levels.lastOrNull() ?: return false
    if (top.depth != depth || !top.rowsEnabled || index !in top.actions.indices ||
        !menuActionEnabled(top.actions[index])) return false
    if (firstSubmenuIndex >= 0 && depth == 0) return false
    if (depth > 0 && !firstOpen) return false
    if (nestedDepth > 0 && depth <= nestedDepth) return false
    if (nestedFolding) return false
    return true
}

/**
 * Nested rows become interactive only after their parent has unfolded.
 * The .87f threshold is the same one used to publish a submenu hit level.
 * This also blocks stale taps while a parent menu is folding/being dismissed.
 */
internal fun canOpenAnotherMenuLevel(
    expanded: Boolean,
    firstOpen: Boolean,
    parentProgress: Float,
    folding: Boolean,
    currentNestedDepth: Int,
): Boolean = expanded && firstOpen && !folding &&
    currentNestedDepth in 0 until 6 && parentProgress.isFinite() &&
    parentProgress >= .87f

private data class LiveNestedSurface(
    val index: Int,
    val geometry: GlassSubmenuGeometry,
    val progress: Float,
    val owner: GlassMenuAction,
)

private data class ClosingNestedSurface(
    val owner: GlassMenuAction,
    val geometry: GlassSubmenuGeometry,
    val progress: Float,
)

/** A single glass silhouette: menu platter and its traveling source droplet. */
internal data class MenuMorphFrame(
    val platter: Rect,
    val radius: Float,
    val droplet: Rect,
    val blend: Float,
    val settled: Float,
    val glyphAlpha: Float,
    val contentAlpha: Float,
)

internal fun menuOpeningFrame(
    time: Float,
    anchor: Rect,
    card: Rect,
    density: Float,
): MenuMorphFrame {
    val progress = springUnit(time, 400f, .754f)
    val settled = progress.coerceIn(0f, 1f)
    val scale = mixFloat(
        .41f * minOf(anchor.width, anchor.height) / card.width,
        1f, progress,
    )
    val height = mixFloat(card.width, card.height, progress)
    val toCard = card.center.y - anchor.center.y
    val distance = abs(toCard)
    val dip = sign(toCard) * (minOf(1f, distance / (30f * density)) *
        23f * density + .124f * distance) * riseAndFallMenu(time, .098f, 3.3f)
    val platterCenter = mixOffset(anchor.center, card.center, progress) + Offset(0f, dip)
    val platter = menuRect(
        platterCenter, (card.width * scale).coerceAtLeast(.001f),
        (height * scale).coerceAtLeast(.001f),
    )
    val cornerSpring = 1f - springUnit((time - .07f).coerceAtLeast(0f), 324f, .75f)
    val radius = (minOf(height * .5f, 24f * density +
        card.width * .48f * cornerSpring) * scale).coerceAtLeast(0f)

    val dropletExtent = mixFloat(1f, .25f, progress)
    val deflate = maxOf(anchor.width, anchor.height) * .125f + 36f * density
    val sunk = mixOffset(anchor.center, card.center, .25f)
    val target = if (card.width < deflate * 2f || card.height < deflate * 2f) {
        card.center
    } else {
        Offset(
            sunk.x.coerceIn(card.left + deflate, card.right - deflate),
            sunk.y.coerceIn(card.top + deflate, card.bottom - deflate),
        )
    }
    val droplet = menuRect(
        mixOffset(anchor.center, target, progress),
        (anchor.width * dropletExtent).coerceAtLeast(.001f),
        (anchor.height * dropletExtent).coerceAtLeast(.001f),
    )
    return MenuMorphFrame(
        platter = platter,
        radius = radius,
        droplet = droplet,
        blend = 28f * density,
        settled = settled,
        glyphAlpha = (1f - settled / .25f).coerceIn(0f, 1f),
        contentAlpha = ((settled - .28f) / .58f).coerceIn(0f, 1f),
    )
}

internal fun menuReturningFrame(
    time: Float,
    from: MenuMorphFrame,
    anchor: Rect,
    density: Float,
): MenuMorphFrame {
    val ringing = 1f - ((time - .4f) / .1f).coerceIn(0f, 1f)
        .let { it * it } // iOS settles its last ringing by 500ms.
    val progress = 1f + (springUnit(time, 165f, .8f) - 1f) * ringing
    val distance = from.platter.center.y - anchor.center.y
    val dip = minOf(.21f * abs(distance), 22f * density) *
        riseAndFallMenu(time, .072f, 2.4f)
    val lift = minOf(.25f * abs(distance), 19f * density) *
        riseAndFallMenu(time, .176f, 4.9f)
    val scale = mixFloat(
        1f,
        .41f * minOf(anchor.width, anchor.height) /
            maxOf(from.platter.width, from.platter.height),
        progress,
    )
    val platter = menuRect(
        mixOffset(from.platter.center, anchor.center, progress) +
            Offset(0f, sign(distance) * dip * ringing),
        (from.platter.width * scale).coerceAtLeast(.001f),
        (mixFloat(from.platter.height, from.platter.width, progress) * scale)
            .coerceAtLeast(.001f),
    )
    val droplet = menuRect(
        mixOffset(from.droplet.center, anchor.center, progress) -
            Offset(0f, sign(distance) * lift * ringing),
        mixFloat(from.droplet.width, anchor.width, progress).coerceAtLeast(.001f),
        mixFloat(from.droplet.height, anchor.height, progress).coerceAtLeast(.001f),
    )
    val absorbedFraction = ((time / .5f - .6f) / .3f).coerceIn(0f, 1f)
    val absorbed = absorbedFraction * absorbedFraction * (3f - 2f * absorbedFraction)
    val contentExit = (time / .1f).coerceIn(0f, 1f)
    val glyphIn = (time / .47f).coerceIn(0f, 1f)
    // FlClash captures the opening frame at the instant reversal starts.
    // Starting every return with settled=1, alpha=.2 and from.settled for
    // content would flash when the route closes at 50/150/300ms. Keep the
    // exact opening coverage and glyph in the first frame, then blend into
    // the normal FlClash return curve without changing its geometry.
    val handoff = (time / .075f).coerceIn(0f, 1f).let { it * it * (3f - 2f * it) }
    val returnGlyph = .2f + .8f * (glyphIn * glyphIn * (3f - 2f * glyphIn))
    return MenuMorphFrame(
        platter = platter,
        radius = (mixFloat(from.radius, from.platter.width * .5f, progress) * scale)
            .coerceAtLeast(0f),
        droplet = droplet,
        blend = mixFloat(
            from.blend,
            28f * density * (time / .05f).coerceIn(0f, 1f) * (1f - absorbed),
            handoff,
        ),
        settled = mixFloat(from.settled, (1f - progress).coerceIn(0f, 1f), handoff),
        glyphAlpha = mixFloat(from.glyphAlpha, returnGlyph, handoff),
        contentAlpha = from.contentAlpha * (1f - contentExit) * (1f - contentExit),
    )
}

private fun springUnit(time: Float, stiffness: Float, damping: Float): Float {
    if (time <= 0f) return 0f
    val frequency = sqrt(stiffness)
    val damped = frequency * sqrt(1f - damping * damping)
    val ratio = damping / sqrt(1f - damping * damping)
    return 1f - exp(-damping * frequency * time) *
        (cos(damped * time) + ratio * sin(damped * time))
}

private fun riseAndFallMenu(time: Float, peak: Float, sharpness: Float): Float {
    if (time <= 0f) return 0f
    val x = time / peak
    return x.pow(sharpness) * exp(sharpness * (1f - x))
}

private fun menuRect(center: Offset, width: Float, height: Float): Rect =
    Rect(
        center.x - width * .5f, center.y - height * .5f,
        center.x + width * .5f, center.y + height * .5f,
    )

private fun mixFloat(a: Float, b: Float, progress: Float): Float =
    a + (b - a) * progress

private fun mixOffset(a: Offset, b: Offset, progress: Float): Offset =
    Offset(mixFloat(a.x, b.x, progress), mixFloat(a.y, b.y, progress))

internal fun glassAlertShouldRender(visible: Boolean, amount: Float): Boolean =
    visible || (amount.isFinite() && amount > .001f)

@androidx.annotation.RequiresApi(33)
@Composable
fun GlassAlert(
    visible: Boolean,
    dark: Boolean,
    title: String,
    message: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit = onDismiss,
) {
    GlassDialog(visible, dark, title, onDismiss,
        actions = listOf(GlassDialogAction("取消", onClick = onDismiss),
            GlassDialogAction("确定", onClick = onConfirm))) { Text(message) }
}

private fun Modifier.offsetPx(x: Float, y: Float): Modifier =
    this.then(Modifier.offset {
        IntOffset(x.roundToInt(), y.roundToInt())
    })
