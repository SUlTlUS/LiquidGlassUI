package dev.liquidglass27.glass

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.focusable
import androidx.compose.ui.input.key.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.PointerType
import androidx.compose.ui.input.pointer.isPrimaryPressed
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.max
import kotlin.math.sqrt
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ElasticPressState internal constructor() {
    internal val motion = FlClashElasticMotion()
    private var pressedValue by mutableStateOf(false)
    var pressed: Boolean
        get() = pressedValue
        set(value) {
            if (pressedValue == value) return
            pressedValue = value
            if (value) motion.press() else motion.release()
        }
    var pressPosition by mutableStateOf(Offset.Unspecified)
    val pull: Offset get() = motion.pull
    var size by mutableStateOf(androidx.compose.ui.unit.IntSize.Zero)
    val lifted: Float get() = motion.lift.value
    internal val effectiveLift: Float get() = motion.effectiveLift
}

@androidx.annotation.RequiresApi(33)
@Composable
fun rememberElasticPressState(): ElasticPressState {
    val state = remember { ElasticPressState() }
    return state
}

/**
 * Mirrors FlClash ElasticPress: press swells before release, finger pull is
 * rubber-banded, and the glass silhouette can follow the painted dimensions.
 */
@androidx.annotation.RequiresApi(33)
@Composable
fun Modifier.elasticPress(
    state: ElasticPressState,
    onClick: () -> Unit,
    enabled: Boolean = true,
    maxGrowth: Dp = 16.dp,
    openOnPressDown: Boolean = false,
    onSourcePointer: ((GlassSourcePointerPhase, Offset) -> Unit)? = null,
    /** Emit coordinates in root space from the actual elastic pointer layer. */
    sourcePointerInRoot: Boolean = false,
    /** Flutter's kPressTimeout is 100ms inside a scrollable ancestor. */
    popupPressDelayMillis: Long = 0L,
): Modifier {
    LaunchedEffect(state) { state.motion.run() }
    val onClickUpdated by rememberUpdatedState(onClick)
    val onSourcePointerUpdated by rememberUpdatedState(onSourcePointer)
    val onPressDownUpdated by rememberUpdatedState(openOnPressDown)
    val density = LocalDensity.current.density
    var pointerCoordinates by remember { mutableStateOf<LayoutCoordinates?>(null) }
    fun reportPointer(phase: GlassSourcePointerPhase, point: Offset) {
        val callback = onSourcePointerUpdated ?: return
        if (!sourcePointerInRoot || phase == GlassSourcePointerPhase.Cancel) {
            callback(phase, point)
            return
        }
        // FlClash's press transforms paint only; pointers retain rest coordinates.
        val coords = pointerCoordinates?.takeIf { it.isAttached } ?: return
        callback(phase, coords.localToRoot(point))
    }
    val maxGrowthPx = maxGrowth.value * density
    return this
        .onSizeChanged { state.size = it }
        .onGloballyPositioned { pointerCoordinates = it }
        .pointerInput(state, enabled) {
            try {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent(PointerEventPass.Initial)
                        if (event.changes.any { it.type == PointerType.Mouse ||
                                it.type == PointerType.Stylus }) {
                            when (event.type) {
                                PointerEventType.Enter -> state.motion.hover(enabled)
                                PointerEventType.Exit -> state.motion.hover(false)
                            }
                        }
                    }
                }
            } finally {
                state.motion.hover(false)
            }
        }
        .semantics {
            role = Role.Button
            onClick {
                if (enabled) onClickUpdated()
                enabled
            }
        }
        .onKeyEvent {
            if (enabled && it.type == KeyEventType.KeyUp &&
                it.key in listOf(Key.Enter, Key.Spacebar, Key.DirectionCenter)) {
                onClickUpdated(); true
            } else false
        }.focusable(enabled)
        .pointerInput(state, enabled, openOnPressDown, popupPressDelayMillis,
            sourcePointerInRoot) {
            if (!enabled) return@pointerInput
            coroutineScope {
                awaitEachGesture {
                    val down = awaitFirstDown(requireUnconsumed = false)
                    if (down.type == PointerType.Mouse &&
                        !currentEvent.buttons.isPrimaryPressed) return@awaitEachGesture
                    // A competing ancestor may already own this Down.
                    // Unlike the old implementation, do not open a popup
                    // from a pointer whose gesture was consumed upstream.
                    if (down.isConsumed) return@awaitEachGesture
                    // Preserve the physical Down at its ORIGINAL timestamp.
                    // In a scrollable host we accept this press 100ms later;
                    // the elastic graphicsLayer has already swelled by then.
                    // Reprojecting the historical local Down through that
                    // newer matrix makes the whole fast drag appear offset.
                    val rootDown = if (sourcePointerInRoot) {
                        pointerCoordinates?.takeIf { it.isAttached }
                            ?.localToRoot(down.position)
                    } else down.position
                    state.pressPosition = down.position
                    state.pressed = true
                    var recognized = false
                    var rejected = false
                    var completed = false
                    var wasCancelled = false

                    // PopupPressRecognizer: outside a scrollable, accept on
                    // down. Inside a scrollable, start a kPressTimeout timer;
                    // movement beyond touch slop BEFORE it fires gives the
                    // gesture back to the scrolling parent. An early tap Up
                    // still accepts and opens the popup on release.
                    fun acceptPopup() {
                        if (!recognized && !rejected) {
                            recognized = true
                            rootDown?.let {
                                onSourcePointerUpdated?.invoke(GlassSourcePointerPhase.Down, it)
                            }
                            onClickUpdated()
                        }
                    }

                    var pending: Job? = null
                    if (onPressDownUpdated) {
                        if (popupPressDelayMillis <= 0L) {
                            acceptPopup()
                            down.consume()
                        } else {
                            pending = launch {
                                delay(popupPressDelayMillis)
                                acceptPopup()
                            }
                        }
                    } else {
                        rootDown?.let {
                            onSourcePointerUpdated?.invoke(GlassSourcePointerPhase.Down, it)
                        }
                    }

                    try {
                        while (true) {
                            val event = awaitPointerEvent()
                            // Flutter's press recognizer stops ownership if
                            // another finger joins. Do not turn a multi-touch
                            // gesture into a source click or carried menu Up.
                            if (event.changes.any { it.id != down.id && it.pressed }) {
                                rejected = true
                                wasCancelled = true
                                pending?.cancel()
                                break
                            }
                            val pointer = event.changes.firstOrNull { it.id == down.id }
                            if (pointer == null || !pointer.pressed) {
                                pending?.cancel()
                                if (onPressDownUpdated) {
                                    if (pointer != null && !rejected &&
                                        (recognized || !pointer.isConsumed)) {
                                        if (!recognized) acceptPopup()
                                        if (recognized) {
                                            reportPointer(
                                                GlassSourcePointerPhase.Up,
                                                pointer.position,
                                            )
                                            pointer.consume()
                                        }
                                    } else if (recognized) {
                                        reportPointer(
                                            GlassSourcePointerPhase.Cancel,
                                            down.position,
                                        )
                                    }
                                } else {
                                    if (pointer == null || pointer.isConsumed ||
                                        pointer.position.x !in 0f..size.width.toFloat() ||
                                        pointer.position.y !in 0f..size.height.toFloat()
                                    ) wasCancelled = true
                                    reportPointer(
                                        if (pointer == null)
                                            GlassSourcePointerPhase.Cancel
                                        else GlassSourcePointerPhase.Up,
                                        pointer?.position ?: down.position,
                                    )
                                }
                                completed = true
                                break
                            }

                            if (onPressDownUpdated && !recognized) {
                                // Do not consume during the delay. A real
                                // scroll is allowed to win, as in FlClash.
                                val scrolled = (pointer.position - down.position)
                                    .getDistance() > viewConfiguration.touchSlop
                                if (scrolled || pointer.isConsumed) {
                                    rejected = true
                                    pending?.cancel()
                                    break
                                }
                                // Another recognizer may consume its delta
                                // in the Final pass AFTER our Main pass.
                                // Respect the result before the 100ms
                                // popup timer gets to accept this pointer.
                                val finalEvent = awaitPointerEvent(PointerEventPass.Final)
                                val finalPointer = finalEvent.changes.firstOrNull {
                                    it.id == down.id
                                }
                                if (finalPointer == null || finalPointer.isConsumed) {
                                    rejected = true
                                    pending?.cancel()
                                    break
                                }
                                continue
                            }

                            reportPointer(GlassSourcePointerPhase.Move, pointer.position)
                            state.pressPosition = pointer.position
                            val delta = pointer.position - down.position
                            val limit = max(
                                1f,
                                minOf(state.size.width, state.size.height) * (7f / 32f),
                            )
                            state.motion.drag(delta, limit)
                            if (!onPressDownUpdated && pointer.isConsumed) {
                                wasCancelled = true
                            }
                            if (onPressDownUpdated) pointer.consume()
                        }
                    } finally {
                        pending?.cancel()
                        if (!completed) {
                            if (recognized || !onPressDownUpdated) {
                                reportPointer(
                                    GlassSourcePointerPhase.Cancel,
                                    down.position,
                                )
                            }
                        }
                        state.pressed = false
                    }
                    if (!wasCancelled && !onPressDownUpdated) onClickUpdated()
                }
            }
        }
        .graphicsLayer {
            val transform = menuElasticTransform(
                Rect(0f, 0f, size.width, size.height),
                state.effectiveLift, state.pull, maxGrowthPx,
            )
            scaleX = transform.scaleX
            scaleY = transform.scaleY
            translationX = transform.pull.x
            translationY = transform.pull.y
        }
}

@androidx.annotation.RequiresApi(33)
@Composable
fun ElasticGlassButton(
    modifier: Modifier = Modifier,
    style: GlassStyle,
    state: ElasticPressState = rememberElasticPressState(),
    cornerDp: Float = 1000f,
    onClick: () -> Unit,
    openOnPressDown: Boolean = false,
    onSourcePointer: ((GlassSourcePointerPhase, Offset) -> Unit)? = null,
    sourcePointerInRoot: Boolean = false,
    popupPressDelayMillis: Long = 0L,
    /** Optional source content capture: records ONLY foreground, never GlassPane. */
    sourceContentLayer: GraphicsLayer? = null,
    /** A stable key for foreground changes (text, selected state, theme). */
    captureContentVersion: Any? = Unit,
    /** Parent visibility when a popup hides its source button. */
    captureAlpha: Float = 1f,
    /** Automatically join the nearest GooGlassGroup; input and foreground stay local. */
    gooEnabled: Boolean = true,
    content: @Composable BoxScope.() -> Unit,
) {
    val density = LocalDensity.current.density
    val goo = gooButtonRegistration(state, style, gooEnabled && captureAlpha > 0f)
    // LiquidGlass animates light separately from ElasticPress's spring.
    val light by animateFloatAsState(
        if (state.pressed) 1f else 0f,
        tween(if (state.pressed) 120 else 360, easing = CubicBezierEasing(0f, 0f, .58f, 1f)),
        label = "glassPressLight",
    )
    LaunchedEffect(light) {
        if (light == 0f && !state.pressed) state.pressPosition = Offset.Unspecified
    }
    Box(
        modifier.then(goo?.modifier ?: Modifier).elasticPress(
            state, onClick,
            openOnPressDown = openOnPressDown,
            onSourcePointer = onSourcePointer,
            sourcePointerInRoot = sourcePointerInRoot,
            popupPressDelayMillis = popupPressDelayMillis,
        ),
        contentAlignment = Alignment.Center,
    ) {
        if (goo?.grouped != true) GlassPane(
            Modifier.matchParentSize(),
            style = style,
            cornerDp = cornerDp,
            glow = state.pressPosition,
            glowStrength = light * .16f,
            glowReachDp = state.size.height / density / sqrt(2f) *
                (1f + if (state.pressed) 0f else 3f * (1f - light)),
            captureAlpha = captureAlpha,
        )
        if (sourceContentLayer == null) {
            GlassCaptureBehind(
                contentAlignment = Alignment.Center,
                contentVersion = captureContentVersion,
                captureAlpha = captureAlpha,
                content = content,
            )
        } else {
            // FlClash's LiquidGlass.withoutPanes() excludes glass from the
            // source snapshot. Record a SEPARATE transparent foreground only.
            // The card and traveling droplet keep the sole refracting pane.
            GlassCaptureBehind(
                Modifier.matchParentSize(), contentAlignment = Alignment.Center,
                contentVersion = captureContentVersion,
                captureAlpha = captureAlpha,
            ) {
                Box(
                    Modifier.matchParentSize().drawWithContent {
                        sourceContentLayer.record {
                            this@drawWithContent.drawContent()
                        }
                        drawLayer(sourceContentLayer)
                    },
                    contentAlignment = Alignment.Center,
                    content = content,
                )
            }
        }
    }
}

data class GooAction(
    val symbol: String,
    val label: String,
    val onClick: () -> Unit,
)

/** Up to six buttons are shaded as one continuous SDF, with press-driven necks. */
@androidx.annotation.RequiresApi(33)
@Composable
fun GooGlassRow(
    actions: List<GooAction>,
    modifier: Modifier = Modifier,
    dark: Boolean = false,
    height: Dp = 52.dp,
    gap: Dp = 10.dp,
    itemContent: @Composable BoxScope.(GooAction) -> Unit,
) {
    require(actions.size in 1..6) { "GooGlassRow supports 1..6 actions" }
    val density = LocalDensity.current.density
    val gapPx = gap.value * density
    val states = remember(actions.size) { List(actions.size) { ElasticPressState() } }
    val bleed = 8.dp
    val bleedPx = bleed.value * density
    BoxWithConstraints(modifier.height(height + bleed * 2)) {
        val totalWidth = constraints.maxWidth.toFloat()
        val totalHeight = constraints.maxHeight.toFloat()
        val contentHeight = height.value * density
        val cell = (totalWidth - bleedPx * 2 - gapPx * (actions.size - 1)) / actions.size
        val pills = actions.indices.map { i ->
            val transform = menuElasticTransform(
                Rect(0f, 0f, cell, contentHeight),
                states[i].effectiveLift, states[i].pull, 16f * density,
            )
            GlassPill(
                centerX = bleedPx + i * (cell + gapPx) + cell / 2f + states[i].pull.x,
                centerY = totalHeight / 2f + states[i].pull.y,
                halfWidth = cell / 2f * transform.scaleX,
                halfHeight = contentHeight / 2f * transform.scaleY,
            )
        }
        val blends = (1 until actions.size).map { index ->
            val nearby = max(states[index - 1].lifted, states[index].lifted)
            if (GlassMotion.inSameRow(pills[index - 1], pills[index])) {
                // FlClash/lib/ui/goo.dart:
                // _restBlend = 8; _pressBlend = 8.
                // No resting neck for buttons 10dp apart; the press first
                // swells their bounds, then the original blend joins them.
                8f + 8f * nearby
            } else 0f
        }
        val pressedIndex = states.indices.maxByOrNull { states[it].lifted } ?: 0
        val pressedPill = pills[pressedIndex]
        val pressedBounds = Rect(
            pressedPill.centerX - pressedPill.halfWidth,
            pressedPill.centerY - pressedPill.halfHeight,
            pressedPill.centerX + pressedPill.halfWidth,
            pressedPill.centerY + pressedPill.halfHeight,
        )
        val touch = states[pressedIndex].pressPosition
        val touchInRow = if (touch == Offset.Unspecified) pressedBounds.center else
            touch + Offset(bleedPx + pressedIndex * (cell + gapPx), bleedPx)
        GlassPane(
            Modifier.matchParentSize(),
            style = GlassStyle.regular(dark),
            pills = pills,
            blendsDp = blends,
            glow = if (pressedBounds.contains(touchInRow)) touchInRow else pressedBounds.center,
            glowStrength = states[pressedIndex].lifted * .16f,
            glowReachDp = pressedBounds.height / density / sqrt(2f),
        )
        Row(
            Modifier.fillMaxSize().padding(horizontal = bleed, vertical = bleed),
            horizontalArrangement = Arrangement.spacedBy(gap),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            actions.forEachIndexed { index, action ->
                Box(
                    Modifier.weight(1f).fillMaxHeight()
                        // FlClash wraps GooPane inside ElasticPress:
                        // the foreground follows the same press swell as
                        // the shared glass silhouette.
                        .elasticPress(states[index], action.onClick),
                    contentAlignment = Alignment.Center,
                ) {
                    GlassCaptureBehind(
                        Modifier.matchParentSize(),
                        contentAlignment = Alignment.Center,
                        contentVersion = action.symbol,
                    ) { itemContent(action) }
                }
            }
        }
    }
}
