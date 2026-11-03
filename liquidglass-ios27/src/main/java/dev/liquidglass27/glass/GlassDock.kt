@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package dev.liquidglass27.glass

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.rememberTooltipState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.hideFromAccessibility
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import com.zeaze.tianyinwallpaper.backdrop.backdrops.layerBackdrop
import com.zeaze.tianyinwallpaper.backdrop.backdrops.rememberLayerBackdrop
import com.zeaze.tianyinwallpaper.backdrop.backdrops.rememberCombinedBackdrop
import kotlin.math.roundToInt

data class GlassDestination(val symbol: String, val title: String, val glyph: GlassGlyph? = null)

@androidx.annotation.RequiresApi(33)
@Composable
fun GlassNavigationDock(
    destinations: List<GlassDestination>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    dark: Boolean,
    modifier: Modifier = Modifier,
) {
    if (destinations.isEmpty()) {
        GlassPane(modifier.height(62.dp), GlassStyle.pill(dark))
        return
    }
    val density = LocalDensity.current
    val colors = glassColors(dark)
    val textMeasurer = rememberTextMeasurer()
    val rtl = LocalLayoutDirection.current == LayoutDirection.Rtl
    val haptics = LocalHapticFeedback.current
    val onSelectNow by rememberUpdatedState(onSelect)
    var focusedIndex by remember { mutableIntStateOf(-1) }
    val motion = remember(destinations.size) {
        FlClashDockMotion(selectedIndex.coerceIn(destinations.indices), destinations.size)
    }
    SideEffect { motion.select(selectedIndex.coerceIn(destinations.indices)) }
    LaunchedEffect(motion) { motion.run() }
    val pillShape = remember {
        GenericShape { size, _ ->
            addPath(gooPlatterOutline(Rect(Offset.Zero, size), size.minDimension / 2).asComposePath())
        }
    }
    val barLayer = rememberLayerBackdrop()
    val scene = LocalSketchGlassBackdrop.current
    val lensBackdrop = if (scene != null) rememberCombinedBackdrop(scene, barLayer) else null
    val height = (62f + (density.fontScale - 1f) * 10f).dp
    val shadowStrength = if (dark) 3f else 1f
    BoxWithConstraints(modifier.height(height), contentAlignment = Alignment.Center) {
        val width = if (destinations.size < 5)
            minOf(maxWidth, (destinations.size * 86 + 16).dp) else maxWidth
        val widthPx = with(density) { width.toPx() }
        val heightPx = with(density) { height.toPx() }
        val d = density.density
        val padding = 4f * d
        val layout = dockLayout(widthPx - padding * 2, destinations.size, d)
        val labelWidths = destinations.map { textMeasurer.measure(it.title,
            TextStyle(fontSize = 10.sp, fontWeight = FontWeight.Medium), maxLines = 1).size.width }
        val labelScale = dockLabelScale((layout.pitch - 4f * d).coerceAtLeast(0f),
            labelWidths.maxOrNull()?.toFloat() ?: 0f)
        val labelOverflows = labelWidths.map { it * labelScale > layout.pitch - 4f * d }
        fun logical(point: Offset) = if (rtl) Offset(widthPx - point.x, point.y) else point
        fun painted(rect: Rect): Rect = if (rtl)
            Rect(widthPx - rect.right, rect.top, widthPx - rect.left, rect.bottom) else rect
        val lens = painted(dockLensRect(
            motion.lens.value, motion.jelly.value, layout, heightPx - padding * 2,
            motion.lift.value, d,
        ).translate(padding, padding))
        val lensPath = remember(lens) {
            gooPlatterOutline(lens, minOf(lens.width, lens.height) / 2).asComposePath()
        }
        Box(
            Modifier.width(width).fillMaxHeight()
                .expandedTouchInput(8.dp, motion, widthPx, heightPx, rtl) {
                    awaitEachGesture {
                        val down = awaitFirstDown(requireUnconsumed = false)
                        if (down.type == PointerType.Mouse &&
                            !currentEvent.buttons.isPrimaryPressed) return@awaitEachGesture
                        val tracker = VelocityTracker()
                        val point = logical(down.position)
                        tracker.addPosition(down.uptimeMillis, point)
                        motion.press(point, dockPositionAt(point.x, layout, destinations.size, d))
                        down.consume()
                        var committed = false
                        try {
                            while (true) {
                                val event = awaitPointerEvent()
                                val change = event.changes.firstOrNull { it.id == down.id } ?: break
                                val position = logical(change.position)
                                tracker.addPosition(change.uptimeMillis, position)
                                if (!change.pressed) {
                                    val next = motion.release(!change.isConsumed, tracker.calculateVelocity().x, layout.pitch,
                                        dockPositionAt(position.x, layout, destinations.size, d))
                                    committed = true
                                    change.consume()
                                    next?.let(onSelectNow)
                                    break
                                }
                                if (motion.drag(position,
                                    dockPositionAt(position.x, layout, destinations.size, d),
                                    widthPx, heightPx, viewConfiguration.touchSlop)) {
                                    haptics.performHapticFeedback(HapticFeedbackType.SegmentTick)
                                }
                                change.consume()
                            }
                        } finally {
                            if (!committed) motion.release(false, 0f, layout.pitch)
                        }
                    }
                }
                .pointerInput(motion, widthPx, rtl) {
                    try {
                        awaitPointerEventScope {
                            while (true) {
                                val event = awaitPointerEvent(PointerEventPass.Initial)
                                val change = event.changes.firstOrNull() ?: continue
                                if (change.type == PointerType.Touch) continue
                                if (event.type == PointerEventType.Exit) motion.hover(null)
                                else if (!change.pressed && (event.type == PointerEventType.Move ||
                                        event.type == PointerEventType.Enter)) {
                                    motion.hover(dockPositionAt(logical(change.position).x,
                                        layout, destinations.size, d))
                                }
                            }
                        }
                    } finally { motion.hover(null) }
                }
                .graphicsLayer {
                    val transform = menuElasticTransform(Rect(0f, 0f, widthPx, heightPx),
                        motion.swell.value,
                        Offset(motion.stretch.value * if (rtl) -1f else 1f, 0f), 16f * d)
                    scaleX = transform.scaleX
                    scaleY = transform.scaleY
                    translationX = transform.pull.x
                }
                .dropShadow(pillShape, Shadow(24.dp, Color.Black.copy(alpha = .08f * shadowStrength),
                    offset = DpOffset(0.dp, 8.dp)))
                .dropShadow(pillShape, Shadow(3.dp, Color.Black.copy(alpha = .04f * shadowStrength),
                    offset = DpOffset(0.dp, 1.dp))),
        ) {
            Box(Modifier.fillMaxSize().then(if (scene != null) Modifier.layerBackdrop(barLayer) else Modifier)) {
                GlassPane(Modifier.fillMaxSize(), GlassStyle.pill(dark),
                    glow = if (rtl) Offset(widthPx - motion.glowAt.x, motion.glowAt.y) else motion.glowAt,
                    glowStrength = .06f * motion.glow.value * motion.glow.value,
                    glowReachDp = 28f * (1f + 3f * motion.glowSpread.value), glowWash = .7f)
                Canvas(Modifier.fillMaxSize()) {
                    val platter = glassTone(colors.secondary, if (dark) 4.0 else 90.0)
                    drawPath(lensPath, platter.copy(alpha = .85f * (1f - motion.lift.value.coerceIn(0f, 1f))))
                    val hoverRect = painted(Rect(
                        padding + layout.inset + (motion.hover.value + .5f) * layout.pitch - layout.lens / 2,
                        padding,
                        padding + layout.inset + (motion.hover.value + .5f) * layout.pitch + layout.lens / 2,
                        heightPx - padding,
                    ))
                    drawPath(gooPlatterOutline(hoverRect, hoverRect.height / 2).asComposePath(),
                        (if (dark) Color.White else Color.Black).copy(
                            alpha = .08f * motion.hoverShow.value.coerceIn(0f, 1f)))
                }
            }
            if (motion.lift.value > 0f) {
                Box(Modifier.absoluteOffset { IntOffset(lens.left.roundToInt(), lens.top.roundToInt()) }
                    .size(with(density) { lens.width.toDp() }, with(density) { lens.height.toDp() })
                    .dropShadow(pillShape, Shadow(5.dp,
                        Color.Black.copy(alpha = .055f * shadowStrength * motion.lift.value.coerceIn(0f, 1f)),
                        offset = DpOffset(0.dp, 8.dp)))) {
                    GlassPane(Modifier.fillMaxSize(), GlassStyle.lens(dark).materialize(motion.lift.value),
                        sketchBackdropOverride = lensBackdrop)
                }
            }
            DockItems(destinations, selectedIndex, onSelectNow, motion, layout, padding, dark,
                inside = false, lensPath = lensPath, labelScale = labelScale,
                labelOverflows = labelOverflows, onFocus = { focusedIndex = it })
            DockItems(destinations, selectedIndex, onSelectNow, motion, layout, padding, dark,
                inside = true, lensPath = lensPath, labelScale = labelScale,
                labelOverflows = labelOverflows, onFocus = {})
            if (focusedIndex in destinations.indices) Canvas(Modifier.fillMaxSize()) {
                val left = padding + layout.inset + focusedIndex * layout.pitch
                val rect = painted(Rect(left, padding, left + layout.pitch, heightPx - padding))
                drawPath(gooPlatterOutline(rect, rect.height / 2).asComposePath(),
                    colors.primary, style = Stroke(2f * d))
            }
        }
    }
}

@androidx.annotation.RequiresApi(33)
@Composable
private fun DockItems(
    destinations: List<GlassDestination>, selectedIndex: Int, onSelect: (Int) -> Unit,
    motion: FlClashDockMotion, layout: DockLayout, padding: Float, dark: Boolean,
    inside: Boolean, lensPath: Path, labelScale: Float,
    labelOverflows: List<Boolean>, onFocus: (Int) -> Unit,
) {
    val density = LocalDensity.current
    val colors = glassColors(dark)
    val direction = if (LocalLayoutDirection.current == LayoutDirection.Rtl) -1f else 1f
    Row(
        Modifier.fillMaxSize()
            .then(if (inside) Modifier.clearAndSetSemantics { hideFromAccessibility() } else Modifier)
            .drawWithContent {
                clipPath(lensPath, if (inside) ClipOp.Intersect else ClipOp.Difference) {
                    this@drawWithContent.drawContent()
                }
            }
            .padding(horizontal = with(density) { (padding + layout.inset).toDp() }, vertical = 4.dp),
    ) {
        for ((index, destination) in destinations.withIndex()) {
            val color = if (inside) {
                colors.primary
            } else colors.onSurfaceVariant
            Box(Modifier.weight(1f).fillMaxHeight()) {
            TooltipBox(modifier = Modifier.fillMaxSize(),
                positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                tooltip = { PlainTooltip { Text(destination.title) } }, state = rememberTooltipState(),
                enableUserInput = !inside && labelOverflows[index]) {
            Box(
                Modifier.fillMaxSize()
                    .onFocusChanged { if (!inside) onFocus(if (it.isFocused) index else -1) }
                    .then(if (inside) Modifier else Modifier.selectable(
                        selected = index == selectedIndex, role = Role.Tab,
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null, onClick = { onSelect(index) },
                    ))
                    .graphicsLayer {
                        scaleX = if (inside) 1f + .16f * motion.lift.value else 1f
                        scaleY = scaleX
                        translationX = motion.parallax[index].value * layout.pitch * direction
                    },
                contentAlignment = Alignment.Center,
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    val glyph = destination.glyph ?: GlassGlyphs.forSymbol(destination.symbol)
                    if (glyph != null) GlassGlyphIcon(glyph, color, Modifier.size(28.dp), fill = 1f)
                    else Text(destination.symbol, color = color, fontSize = 28.sp, lineHeight = 28.sp,
                        fontWeight = FontWeight.Normal, maxLines = 1)
                    Spacer(Modifier.height(2.dp))
                    Text(destination.title, color = color, fontSize = (10f * labelScale).sp, lineHeight = 14.5.sp,
                        fontWeight = FontWeight.Medium, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
            }
            }
            }
        }
    }
}
