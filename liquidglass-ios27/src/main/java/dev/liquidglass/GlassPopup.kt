@file:OptIn(androidx.compose.ui.ExperimentalComposeUiApi::class)

package dev.liquidglass

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.Image
import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.focus.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.*
import androidx.compose.ui.unit.*
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.first
import kotlin.math.roundToInt
import com.zeaze.tianyinwallpaper.backdrop.backdrops.layerBackdrop
import com.zeaze.tianyinwallpaper.backdrop.backdrops.rememberLayerBackdrop
import com.zeaze.tianyinwallpaper.backdrop.backdrops.rememberCombinedBackdrop
import dev.liquidglass.rendering.rememberTransformAwareBackdrop

data class GlassPopupProperties(
    val modal: Boolean = true,
    val focusable: Boolean = modal,
    val dismissOnBackPress: Boolean = true,
    val dismissOnClickOutside: Boolean = true,
    val elevated: Boolean = false,
    val returnIntoSource: Boolean = true,
)

@Stable
class GlassPopupState<T> {
    internal var sourceImage by mutableStateOf<ImageBitmap?>(null)
    internal var sourceSurfaceImage by mutableStateOf<ImageBitmap?>(null)
    internal var restoreSourceFocus = false
    var expanded by mutableStateOf(false)
        private set
    var mounted by mutableStateOf(false)
        private set
    private var result: T? = null
    private var awaiting: CompletableDeferred<T?>? = null
    fun open(sourceSnapshot: ImageBitmap? = null, sourceSurfaceSnapshot: ImageBitmap? = null) {
        result = null; sourceImage = sourceSnapshot; sourceSurfaceImage = sourceSurfaceSnapshot
        mounted = true; expanded = true
    }
    fun dismiss(result: T? = null) {
        if (!expanded) return
        this.result = result; expanded = false
    }
    suspend fun show(): T? {
        check(awaiting == null) { "This popup already has a waiting caller" }
        val completion = CompletableDeferred<T?>()
        awaiting = completion
        open()
        return try { completion.await() } finally {
            if (awaiting === completion) { awaiting = null; dismiss() }
        }
    }
    internal fun complete(): T? {
        mounted = false
        sourceImage = null
        sourceSurfaceImage = null
        val picked = result
        result = null
        val waiting = awaiting
        awaiting = null
        waiting?.complete(picked)
        return picked
    }
    internal fun dispose() { expanded = false; complete() }
}

@androidx.annotation.RequiresApi(33)
@Composable
fun <T> rememberGlassPopupState(): GlassPopupState<T> = remember { GlassPopupState<T>() }

/** Record only the source foreground, never the glass pane or backdrop. */
@androidx.annotation.RequiresApi(33)
@Composable
fun <T> GlassPopupSourceContent(state: GlassPopupState<T>, modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit) {
    val layer = rememberGraphicsLayer()
    LaunchedEffect(state.mounted) {
        if (state.mounted) state.sourceImage = layer.toImageBitmap()
    }
    // Capture outside caller padding so the foreground keeps its original inset and scale.
    Box(Modifier.drawWithContent { layer.record { this@drawWithContent.drawContent() }; drawLayer(layer) }.then(modifier),
        contentAlignment = androidx.compose.ui.Alignment.Center, content = content)
}

/** Arbitrary source control. Place GlassPopup after the page content in the same scene. */
@androidx.annotation.RequiresApi(33)
@Composable
fun <T> GlassPopupBox(state: GlassPopupState<T>, anchor: GlassPopupAnchor,
    modifier: Modifier = Modifier, hideWhileOpen: Boolean = true,
    content: @Composable BoxScope.(open: () -> Unit) -> Unit) {
    val focus = remember { FocusRequester() }
    var hadPopup by remember { mutableStateOf(false) }
    val mounted = state.mounted
    LaunchedEffect(state.mounted) {
        if (state.mounted) hadPopup = true
        else if (hadPopup) {
            if (state.restoreSourceFocus) focus.requestFocus()
            hadPopup = false
        }
    }
    Box(modifier.glassPopupAnchor(anchor).focusRequester(focus).focusGroup()
        .focusProperties { canFocus = !mounted }
        .graphicsLayer { alpha = if (hideWhileOpen && state.mounted) 0f else 1f }
        .then(if (hideWhileOpen && state.mounted) Modifier.clearAndSetSemantics { } else Modifier)) {
        // The shared Goo material is outside this source's alpha layer.
        // Unregister hidden sources until the popup has fully returned.
        CompositionLocalProvider(LocalGooButtonGroup provides
            if (hideWhileOpen && mounted) null else LocalGooButtonGroup.current,
            dev.liquidglass.rendering.LocalLiquidButtonFusion provides
                if (hideWhileOpen && mounted) null else dev.liquidglass.rendering.LocalLiquidButtonFusion.current) {
            content { state.open() }
        }
    }
}

@androidx.annotation.RequiresApi(33)
@Composable
internal fun Modifier.glassPopupFocus(active: Boolean, title: String): Modifier {
    val focus = remember { FocusRequester() }
    LaunchedEffect(active) { if (active) focus.requestFocus() }
    return if (!active) this else semantics { paneTitle = title; isTraversalGroup = true }
        .focusProperties { onExit = { cancelFocusChange() } }.focusGroup()
        .focusRequester(focus).focusable()
}

/** Nonmodal outside input is shared with the page and never consumed. */
@androidx.annotation.RequiresApi(33)
@Composable
internal fun GlassPopupDismissLayer(modal: Boolean, dismiss: () -> Unit) {
    val dismissNow by rememberUpdatedState(dismiss)
    if (modal) Box(Modifier.fillMaxSize().clickable(
        interactionSource = remember { MutableInteractionSource() }, indication = null, onClick = dismiss))
    else Box(Modifier.fillMaxSize().expandedTouchInput(0.dp, Unit, shareWithSiblings = true) {
        awaitPointerEventScope {
            while (true) {
                val event = awaitPointerEvent(PointerEventPass.Initial)
                if (event.type == PointerEventType.Press || event.type == PointerEventType.Scroll) dismissNow()
            }
        }
    })
}

/** Generic content shares the menu's source/drop/platter geometry and route clock. */
@androidx.annotation.RequiresApi(33)
@Composable
fun <T> GlassPopup(state: GlassPopupState<T>, anchor: GlassPopupAnchor, dark: Boolean,
    modifier: Modifier = Modifier, properties: GlassPopupProperties = GlassPopupProperties(),
    placement: GlassMenuPlacement = GlassMenuPlacement.OverAnchorEnd, avoid: Rect? = null,
    title: String = "弹出窗口", onResult: (T?) -> Unit = {},
    /** Live source paint, including its external shadow; use an independent backdrop. */
    sourceSurface: (@Composable BoxScope.() -> Unit)? = null,
    sourceContent: @Composable BoxScope.() -> Unit = {},
    content: @Composable ColumnScope.(dismiss: (T?) -> Unit) -> Unit) {
    val resultNow by rememberUpdatedState(onResult)
    SideEffect { state.restoreSourceFocus = properties.focusable }
    DisposableEffect(state) { onDispose { state.dispose() } }
    if (!state.mounted) return
    val colors = glassColors(dark)
    val density = LocalDensity.current
    var contentSize by remember { mutableStateOf(IntSize.Zero) }
    val clock = remember { Animatable(0f) }
    var latestFrame by remember { mutableStateOf<MenuMorphFrame?>(null) }
    var closingFrame by remember { mutableStateOf<MenuMorphFrame?>(null) }
    var closingFrom by remember { mutableFloatStateOf(1f) }
    val pin = remember { GlassPopupPin() }
    val rootRoute = remember { MenuRouteState(true) }
    var nativeRim by remember { mutableStateOf(false) }
    LaunchedEffect(state.expanded) {
        if (state.expanded) {
            snapshotFlow { contentSize }.first { it.width > 0 && it.height > 0 }
            closingFrame = null
            clock.animateTo(1f, tween(FlClashPopupRouteMotion.openingRemainingMillis(clock.value), easing = LinearEasing))
        } else {
            closingFrame = latestFrame
            closingFrom = clock.value
            clock.animateTo(0f, tween(FlClashPopupRouteMotion.reverseTravelMillis(clock.value,
                properties.returnIntoSource), easing = LinearEasing), initialVelocity = clock.velocity)
            resultNow(state.complete())
        }
    }
    BackHandler(enabled = properties.dismissOnBackPress) { state.dismiss() }
    BoxWithConstraints(Modifier.fillMaxSize()
        // Sharing must also reach the scene's direct child, not just the barrier.
        .expandedTouchInput(0.dp, properties.modal, shareWithSiblings = !properties.modal) {}
        .glassPopupFocus(properties.focusable, title)) {
        val w = constraints.maxWidth.toFloat(); val h = constraints.maxHeight.toFloat()
        val safe = popupSafeBounds(w, h)
        val origin = anchor.bounds.takeIf { it.width > 0f && it.height > 0f }
            ?: Rect(w / 2, h / 2, w / 2 + 1, h / 2 + 1)
        val position = menuPopupPosition(origin, contentSize.width.toFloat(), contentSize.height.toFloat(),
            w, h, density.density, placement, avoid, safe,
            pin.takeIf { contentSize != IntSize.Zero })
        val card = Rect(position, Size(contentSize.width.coerceAtLeast(1).toFloat(), contentSize.height.coerceAtLeast(1).toFloat()))
        val time = FlClashPopupRouteMotion.dismissalTimeSeconds(clock.value, closingFrom, properties.returnIntoSource)
        val opening = if (properties.returnIntoSource) {
            menuOpeningFrame(FlClashPopupRouteMotion.openingTimeSeconds(clock.value), origin, card, density.density)
        } else MenuMorphFrame(card, minOf(24f * density.density, card.height / 2f),
            Rect(card.center, Size(.001f, .001f)), 0f, 1f, 0f, 1f)
        val frame = if (!state.expanded && closingFrame != null) {
            if (properties.returnIntoSource) menuReturningFrame(time, closingFrame!!, origin, density.density)
            else closingFrame!!
        } else opening
        SideEffect { latestFrame = frame }
        val vanish = if (!state.expanded && !properties.returnIntoSource) menuNoSourceDismissFraction(time) else 0f
        val returning = !state.expanded && properties.returnIntoSource
        // Original source paint takes over while the final spring settles, not on disposal.
        val sourceSurfaceImage = state.sourceSurfaceImage
        val handoff = when {
            (sourceSurface == null && sourceSurfaceImage == null) || !properties.returnIntoSource -> 0f
            returning -> ((time - .32f) / .18f).coerceIn(0f, 1f).let { it * it * (3f - 2f * it) }
            else -> (1f - frame.settled / .25f).coerceIn(0f, 1f)
        }
        val appearance = if (properties.returnIntoSource) 1f else
            (clock.value * 5f).coerceIn(0f, 1f)
        val power = menuRootCornerPower(frame.settled, returning, time)
        val motion = rememberUpdatedState(MenuRootMotion(frame, true, power, returning))
        val sketchBackdrop = LocalSketchGlassBackdrop.current?.let { rememberTransformAwareBackdrop(it) }
        // Record only the painted popup surface, as a sibling of its interactive
        // content. Nested glass samples the surface it sits on, never itself.
        val popupSurfaceBackdrop = rememberLayerBackdrop()
        val contentBackdrop = if (sketchBackdrop != null)
            rememberCombinedBackdrop(sketchBackdrop, rememberTransformAwareBackdrop(popupSurfaceBackdrop)) else null
        Box(Modifier.fillMaxSize().glassPanelPress(16.dp, maxPull = null, panel = card,
            enabled = state.expanded && clock.value >= 1f).graphicsLayer {
            transformOrigin = TransformOrigin(card.center.x / w, card.center.y / h)
            scaleX = 1f - .2f * vanish - .08f * (1f - appearance)
            scaleY = scaleX; alpha = (1f - vanish) * appearance
        }) {
            if (properties.dismissOnClickOutside) GlassPopupDismissLayer(properties.modal) { state.dismiss() }
            else if (properties.modal) GlassPopupDismissLayer(true) {}
            Box(Modifier.fillMaxSize().graphicsLayer { alpha = 1f - handoff }
                .then(if (sketchBackdrop != null) Modifier.layerBackdrop(popupSurfaceBackdrop) else Modifier)) {
            if (sketchBackdrop != null) {
                // Share the menu's exact corner curve, live control light and progressive menu rim.
                SketchMenuRootSurface(motion, card, Size(w, h), density.density, dark,
                    sketchBackdrop, receding = false, distance = 0f, route = rootRoute)
            } else {
                val outline = remember(frame, power) { gooPairOutline(frame.platter, frame.radius, frame.droplet,
                    frame.blend, density = density.density, coarse = false, platterCornerPower = power).outline }
                val material = GlassStyle.interpolate(GlassStyle.regular(dark), GlassStyle.menu(dark), frame.settled)
                MenuCardShadow(outline, density.density, dark, frame.settled, Modifier.fillMaxSize(), clipInterior = true)
                GlassPane(Modifier.fillMaxSize(), material, morph = GlassMorphShape(
                    frame.platter, frame.radius, frame.droplet, frame.blend, power), onFallbackChanged = { nativeRim = it })
                if (nativeRim) GlassMenuRim(Modifier.fillMaxSize(), frame.platter,
                    frame.radius, material, menuRootRimStrength(frame.settled,
                        (gooPairDepth(frame.platter, frame.radius, frame.droplet, frame.blend) /
                            (8f * density.density)).coerceIn(0f, 1f), returning), cornerPower = power)
            }
            }
            val snapshot = state.sourceImage
            val sourceSize = snapshot?.let { Size(it.width.toFloat(), it.height.toFloat()) } ?: origin.size
            val sourceTransform = popupSourceContentTransform(sourceSize, origin.size, frame.droplet)
            Box(Modifier.size(with(density) { sourceSize.width.toDp() }, with(density) { sourceSize.height.toDp() })
                .graphicsLayer {
                    transformOrigin = TransformOrigin(0f, 0f)
                    translationX = sourceTransform.translation.x; translationY = sourceTransform.translation.y
                    scaleX = sourceTransform.scale; scaleY = sourceTransform.scale
                    alpha = frame.glyphAlpha * (1f - handoff)
                }, contentAlignment = androidx.compose.ui.Alignment.Center) {
                if (snapshot != null) Image(snapshot, null, Modifier.fillMaxSize(), contentScale = ContentScale.Fit)
                else sourceContent()
            }
            if (sourceSurface != null && handoff > 0f) {
                // An alpha layer is bounded even when clip=false. Measure the shadow
                // padding outside the button so the handoff never crops it to a rectangle.
                val shadow = com.zeaze.tianyinwallpaper.backdrop.shadow.Shadow.Default
                val shadowPadding = shadow.radius * 2f +
                    maxOf(kotlin.math.abs(shadow.offset.x.value), kotlin.math.abs(shadow.offset.y.value)).dp
                Box(Modifier.layout { measurable, constraints ->
                    val pad = shadowPadding.roundToPx()
                    val child = measurable.measure(Constraints.fixed(
                        origin.width.roundToInt() + pad * 2,
                        origin.height.roundToInt() + pad * 2,
                    ))
                    layout(constraints.maxWidth, constraints.maxHeight) {
                        child.place((frame.droplet.center.x - origin.width * .5f).roundToInt() - pad,
                            (frame.droplet.center.y - origin.height * .5f).roundToInt() - pad)
                    }
                }.graphicsLayer {
                    scaleX = frame.droplet.width / origin.width
                    scaleY = frame.droplet.height / origin.height
                    alpha = handoff
                }.padding(shadowPadding).clearAndSetSemantics {},
                    contentAlignment = androidx.compose.ui.Alignment.Center,
                    content = sourceSurface)
            } else if (sourceSurfaceImage != null && handoff > 0f) Image(sourceSurfaceImage, null,
                Modifier.size(with(density) { origin.width.toDp() }, with(density) { origin.height.toDp() })
                    .graphicsLayer {
                        transformOrigin = TransformOrigin(0f, 0f)
                        translationX = frame.droplet.left; translationY = frame.droplet.top
                        scaleX = frame.droplet.width / origin.width
                        scaleY = frame.droplet.height / origin.height
                        alpha = handoff
                    }, contentScale = ContentScale.FillBounds)
            val contentTransform = popupContentTransform(card.size, frame.platter)
            val contentScale = contentTransform.scale.coerceAtLeast(.001f)
            val shape = GenericShape { _, _ -> addPath(gooPlatterOutline(
                Rect((frame.platter.left - contentTransform.translation.x) / contentScale,
                    (frame.platter.top - contentTransform.translation.y) / contentScale,
                    (frame.platter.right - contentTransform.translation.x) / contentScale,
                    (frame.platter.bottom - contentTransform.translation.y) / contentScale),
                frame.radius / contentScale, power).asComposePath()) }
            GlassSurfaceTheme(dark, properties.elevated) {
            CompositionLocalProvider(
                LocalContentColor provides colors.onSurface,
                LocalSketchGlassBackdrop provides contentBackdrop,
                // A page's Goo group must not capture buttons from another surface.
                LocalGooButtonGroup provides null,
                dev.liquidglass.rendering.LocalLiquidButtonFusion provides null,
            ) {
                Column(modifier.widthIn(max = 348.dp)
                    .sizeIn(maxWidth = with(density) { (safe.width - 32.dp.toPx()).coerceAtLeast(1f).toDp() },
                        maxHeight = with(density) { (safe.height - 32.dp.toPx()).coerceAtLeast(1f).toDp() })
                    .onSizeChanged { contentSize = it }
                    .absoluteOffset { IntOffset(card.left.roundToInt(), card.top.roundToInt()) }
                    .graphicsLayer {
                        transformOrigin = TransformOrigin(0f, 0f)
                        translationX = contentTransform.translation.x - card.left
                        translationY = contentTransform.translation.y - card.top
                        scaleX = contentTransform.scale; scaleY = contentTransform.scale
                        alpha = if (contentSize == IntSize.Zero) 0f else frame.contentAlpha
                    }.clip(shape)
                    .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null, onClick = {})
                    .semantics { paneTitle = title; isTraversalGroup = true }) {
                    content(state::dismiss)
                }
            }
            }
        }
    }
}
