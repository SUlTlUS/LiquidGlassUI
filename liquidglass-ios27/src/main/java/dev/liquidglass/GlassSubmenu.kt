package dev.liquidglass

import android.graphics.BlurMaskFilter
import android.graphics.Paint
import android.graphics.RectF
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt
import kotlinx.coroutines.launch
import com.zeaze.tianyinwallpaper.backdrop.Backdrop
import com.zeaze.tianyinwallpaper.backdrop.backdrops.LayerBackdrop
import com.zeaze.tianyinwallpaper.backdrop.backdrops.layerBackdrop

/**
 * Geometry shared by the visible second-level card and the return transition.
 * On dismissal we freeze this exact outline, as FlClash's _closingSurface
 * freezes the topmost PopupMenuCard before _paintReturn starts.
 */
internal data class GlassSubmenuGeometry(
    val target: Rect,
    val panel: Rect,
    val radius: Float,
)

/**
 * Width from actual measured glyphs, not a fixed row count.  The original
 * PopupMenu clamps to 160..280dp with 8dp card insets and 12dp row padding.
 */
/** popup.dart::_rowExtentOf: 24dp padding + subheadline's 20dp leading. */
internal fun menuRowHeightDp(fontScale: Float): Float =
    24f + 20f * fontScale.coerceAtLeast(0f)

internal fun menuMeasuredWidth(
    items: List<GlassMenuAction>,
    density: Float,
    fontScale: Float,
    cache: MenuLabelWidthCache? = null,
): Float {
    fun measure(): Float {
        val paint = android.graphics.Paint(android.graphics.Paint.ANTI_ALIAS_FLAG).apply {
            textSize = 15f * density * fontScale
            typeface = android.graphics.Typeface.DEFAULT
        }
        val footnotePaint = android.graphics.Paint(paint).apply {
            textSize = 13f * density * fontScale
        }
        var largest = 160f * density
        for (item in items) {
            val text = max(
                paint.measureText(item.text),
                item.subtitle?.split('\n')?.maxOfOrNull { line ->
                    footnotePaint.measureText(line)
                } ?: 0f,
            )
            val hasMark = menuHasEndMark(item)
            val leading = if (menuHasLeadingGlyph(item)) 32f else 0f
            val horizontal = 16f + (if (hasMark) 20f else 24f) +
                leading + (if (hasMark) 24f else 0f)
            largest = max(largest, text + horizontal * density)
        }
        return largest
    }
    return cache?.width(items, density, fontScale, ::measure) ?: measure()
}

/** Allow reduced edge insets on very small windows without invalid ranges. */
/**
 * Actual screen-space top of the pinned submenu title during its spring.
 * GlassSubmenu's content starts at (originTop - inset) and moves toward
 * targetTop; the Column's inset places the title at this position.
 * Hit testing must use this animated position rather than the FINAL target,
 * as FlClash _rowAt follows the current painted card.
 */
internal fun glassSubmenuHeaderTopPx(
    originTop: Float,
    targetTop: Float,
    progress: Float,
    insetPx: Float,
): Float {
    // Flutter _RenderPopupMenuCard uses the unbounded _grow even while
    // overshooting 0 or 1; clamp only non-finite values, not spring ringing.
    val grow = if (progress.isFinite()) progress else 0f
    return originTop + (targetTop - originTop) * grow + insetPx * grow
}

internal fun clampMenuPlacement(
    desired: Float, extent: Float, screenExtent: Float, margin: Float,
): Float {
    if (!desired.isFinite() || !extent.isFinite() || !screenExtent.isFinite() ||
        !margin.isFinite()) return 0f
    val space = (screenExtent - extent).coerceAtLeast(0f)
    val inset = margin.coerceAtLeast(0f).coerceAtMost(space * .5f)
    return desired.coerceIn(inset, space - inset)
}

internal fun glassSubmenuGeometry(
    parent: Rect,
    origin: Rect,
    itemCount: Int,
    progress: Float,
    screenWidth: Float,
    screenHeight: Float,
    density: Float,
    items: List<GlassMenuAction> = emptyList(),
    fontScale: Float = 1f,
    labelWidthCache: MenuLabelWidthCache? = null,
    contentRows: MenuContentRows? = null,
    headerHeightDp: Float = menuRowHeightDp(fontScale),
): GlassSubmenuGeometry {
    val inset = 8f * density
    val margin = 16f * density
    val fromWidth = parent.width
    val maxAvailable = (screenWidth - margin * 2f).coerceAtLeast(1f)
    val lower = min(fromWidth * 1.12f, maxAvailable)
    val upper = min(max(280f * density, lower), maxAvailable)
    val minContentWidth = if (items.isEmpty()) 160f * density else
        menuMeasuredWidth(items, density, fontScale, labelWidthCache)
    val width = max(lower, minContentWidth).coerceIn(lower, upper)
    val rowsHeight = contentRows?.totalHeightDp ?: if (items.isEmpty())
        itemCount * menuRowHeightDp(fontScale)
    else menuContentRows(items, fontScale).totalHeightDp
    val desiredHeight = (
        rowsHeight + headerHeightDp + 33f
    ) * density
    val finalHeight = min(
        desiredHeight,
        (screenHeight - margin * 2f).coerceAtLeast(1f),
    )
    val targetLeft = clampMenuPlacement(
        parent.right - width, width, screenWidth, margin,
    )
    val targetTop = clampMenuPlacement(
        origin.top - inset, finalHeight, screenHeight, margin,
    )
    val target = Rect(
        targetLeft, targetTop, targetLeft + width, targetTop + finalHeight,
    )
    return glassSubmenuInterpolatedShape(origin, target, progress, density)
}

/** Pure geometry: Flutter's unbounded _RenderPopupMenuCard._rect / _radius. */
internal fun glassSubmenuInterpolatedShape(
    origin: Rect,
    target: Rect,
    progress: Float,
    density: Float,
): GlassSubmenuGeometry {
    val grow = if (progress.isFinite()) progress else 0f
    val panel = Rect(
        origin.left + (target.left - origin.left) * grow,
        origin.top + (target.top - origin.top) * grow,
        origin.right + (target.right - origin.right) * grow,
        origin.bottom + (target.bottom - origin.bottom) * grow,
    )
    return GlassSubmenuGeometry(
        target = target,
        panel = panel,
        radius = (16f + 8f * grow).coerceAtLeast(0f) * density,
    )
}

/**
 * Compose port of FlClash PopupMenuCard's second-level geometry.
 *
 * One spring value owns everything: the source row --> platter outline,
 * the width of the constrained content, the 16dp --> 24dp corner, the glass
 * material, the source/header handoff, and the back/fold motion.
 *
 * Unlike the earlier implementation, the Canvas + clip layer has fixed
 * bounds throughout the transition. Moving only an SDF outline prevents
 * recomposition-driven resizing and backdrop/capture jumps on every frame.
 */
@androidx.annotation.RequiresApi(33)
@Composable
internal fun GlassSubmenu(
    parent: Rect,
    origin: Rect,
    owner: GlassMenuAction,
    dark: Boolean,
    progress: Float,
    screenWidth: Float,
    screenHeight: Float,
    onBack: () -> Unit,
    onSelected: (() -> Unit) -> Unit,
    onNested: (Int, Float) -> Unit = { _, _ -> },
    receding: Float = 0f,
    interactive: Boolean = true,
    gestureHoveredRow: Int = -1,
    sharedScroll: ScrollState? = null,
    /** Reuse the same label width measurements as the parent menu. */
    labelWidthCache: MenuLabelWidthCache? = null,
    /** Scene + prior popup glass layers, excluding this card. */
    sketchBackdropOverride: Backdrop? = null,
    /** Capture just this card for the next submenu level. */
    sketchRecordedBackdrop: LayerBackdrop? = null,
) {
    if (owner.subActions.isEmpty() || origin.width <= 0f) return

    val density = LocalDensity.current.density
    val dp = LocalDensity.current
    val inset = 8f * density
    val rowHeightDp = menuRowHeightDp(dp.fontScale)
    // One geometry calculation is also used by GlassMorphMenu to freeze the
    // topmost surface for a direct return into the original source control.
    // The DESTINATION never moves during one unfold; only the current
    // panel interpolates. Cache text width, clamp and target rectangle once
    // per owner/parent/layout change instead of twice per animation frame.
    val estimatedTarget = remember(
        parent, origin, owner.subActions, screenWidth, screenHeight,
        density, dp.fontScale, labelWidthCache,
    ) {
        glassSubmenuGeometry(
            parent, origin, owner.subActions.size, 1f,
            screenWidth, screenHeight, density,
            items = owner.subActions, fontScale = dp.fontScale,
            labelWidthCache = labelWidthCache,
        ).target
    }
    val itemRows = rememberMenuContentRows(
        owner.subActions, dp.fontScale, estimatedTarget.width, density,
    )
    val headerRows = rememberMenuContentRows(
        listOf(owner), dp.fontScale, estimatedTarget.width, density,
    )
    val headerHeightDp = headerRows.itemHeightDp(0)
    val target = remember(
        parent, origin, owner.subActions, screenWidth, screenHeight,
        density, dp.fontScale, labelWidthCache, itemRows, headerHeightDp,
    ) {
        glassSubmenuGeometry(
            parent, origin, owner.subActions.size, 1f,
            screenWidth, screenHeight, density,
            items = owner.subActions, fontScale = dp.fontScale,
            labelWidthCache = labelWidthCache,
            contentRows = itemRows,
            headerHeightDp = headerHeightDp,
        ).target
    }
    val layout = glassSubmenuInterpolatedShape(origin, target, progress, density)
    val panel = layout.panel
    val radius = layout.radius
    val settled = progress.coerceIn(0f, 1f)
    val glassIn = (settled / .35f).coerceIn(0f, 1f)
    val itemsIn = ((settled - .05f) / .35f).coerceIn(0f, 1f)
    // Pointer ownership lives in the stable full-screen GlassMenuGestureSurface.
    // This card only renders the shared hover state and keeps its scroll offset.
    // FlClash _pressed decoration stays on the title throughout its fold.
    // The header is not interactive while folding, but its pressed mask
    // still belongs to the morphing PopupMenuCard's content.
    val hoveredRow = menuPressedMaskHoverForLevel(interactive, gestureHoveredRow)
    val localScroll = rememberScrollState()
    val itemScroll = sharedScroll ?: localScroll

    // Stable paint envelope encompasses source row, final card, and spring
    // ringing. Only the internal rounded distance field changes each frame.
    // The native Flutter spring is unbounded. Reserve enough static margin
    // for a ringing contour even when the source row and target card are far
    // apart; otherwise removing the former +/-0.08 cap would cut corners.
    val travel = max(
        max(kotlin.math.abs(origin.left - target.left),
            kotlin.math.abs(origin.right - target.right)),
        max(kotlin.math.abs(origin.top - target.top),
            kotlin.math.abs(origin.bottom - target.bottom)),
    )
    val bleed = max(34f * density, travel * .20f)
    val envelope = Rect(
        max(0f, min(origin.left, target.left) - bleed),
        max(0f, min(origin.top, target.top) - bleed),
        min(screenWidth, max(origin.right, target.right) + bleed),
        min(screenHeight, max(origin.bottom, target.bottom) + bleed),
    )
    val local = panel.translate(-envelope.left, -envelope.top)
    // Freeze exactly one superellipse for the glass shader clip, the native
    // rim and the Compose content clip. Previously these three independently
    // used powers 2 / 3.5 / 2, causing a moving double silhouette.
    val sharedOutline = remember(local, radius) { gooPlatterOutline(local, radius) }
    val contentOutline = remember(sharedOutline) { sharedOutline.asComposePath() }
    // Only progress/hover/scroll/geometry change each animation frame.
    // Keep the action text/selection snapshot stable; constructing its pairs
    // every frame allocates work unrelated to motion or Sketch material.
    val contentActionVersion = remember(owner.subActions) {
        owner.subActions.map {
            listOf(it.text, it.subtitle, it.glyph, it.symbol,
                it.selected.toString(), it.section?.title)
        }
    }
    // Native bands MUST NOT render as a second independent rim over the
    // working AGSL pane. Their corner AA and coverage differed visibly,
    // particularly on pale backgrounds. Keep native bands as a fallback.
    var nativeRimFallback by remember { mutableStateOf(false) }
    val material = GlassStyle.menu(dark)
    val foreground = glassColors(dark).onSurface
    val heightDp = with(dp) { target.height.toDp() }
    val envelopeWidthDp = with(dp) { envelope.width.toDp() }
    val envelopeHeightDp = with(dp) { envelope.height.toDp() }

    if (envelope.width <= 0f || envelope.height <= 0f) return

    // All three layers below occupy IDENTICAL fixed pixel bounds.
    Box(
        Modifier.offsetPxSub(envelope.left, envelope.top)
            .size(envelopeWidthDp, envelopeHeightDp)
            .graphicsLayer {
                transformOrigin = TransformOrigin(
                    ((target.right - envelope.left) / envelope.width)
                        .coerceIn(0f, 1f),
                    ((origin.top - envelope.top) / envelope.height)
                        .coerceIn(0f, 1f),
                )
                scaleX = menuRecedingScale(receding)
                scaleY = menuRecedingScale(receding)
            }.then(
                if (sketchRecordedBackdrop != null)
                    Modifier.layerBackdrop(sketchRecordedBackdrop)
                else Modifier
            ),
    ) {
        // FlClash's shadow belongs to this card's silhouette and is
        // attenuated by its cumulative distance from the active top card.
        MenuCardShadow(
            path = sharedOutline,
            density = density,
            dark = dark,
            strength = menuCardShadowStrength(
                progress = progress,
                receding = receding > .0001f,
                distance = receding,
            ),
            modifier = Modifier.fillMaxSize(),
        )
        if (glassIn > .002f) {
            // Glass highlights follow exactly the same continuously
            // interpolated silhouette as the clip and its content.
            GlassPane(
                modifier = Modifier.fillMaxSize(),
                // Shader owns both the pane coverage and its dark/light
                // edge. uOutlineRadius, uCornerPower and uRimBounds are
                // derived from this exact panel, not the padded envelope.
                style = material.materialize(glassIn),
                outline = GlassOutlineRect(local, radius, sharedOutline),
                sketchBackdropOverride = sketchBackdropOverride,
                onFallbackChanged = { fallback ->
                    nativeRimFallback = fallback
                },
            )
            if (nativeRimFallback && LocalGlassMenuHighlight.current != GlassMenuHighlight.Ios26) {
                GlassMenuRim(
                    modifier = Modifier.fillMaxSize(),
                    platter = local,
                    radius = radius,
                    style = material,
                    settled = glassIn,
                    outlinePath = sharedOutline,
                )
            }
        }

        // Content constraints grow EARLY, from the exact width of the parent
        // card. The entire card itself keeps growing for the full spring.
        // Thus the header text starts precisely above the tapped source row.
        // A held title's rounded mask uses the SAME geometry as the
        // growing/shrinking content Row. FlClash's ShapeDecoration stays
        // attached during both _unfoldSpring and _foldSpring; it is never
        // gated on settling to 85% or 94% of the transition.
        val headerMask = menuPressedHeaderMaskBounds(
            parent, origin, target, progress, inset,
            headerHeightDp * density,
        )
        val contentWidth = headerMask.width + 2f * inset
        val contentLeft = headerMask.left - inset
        val contentTop = headerMask.top - inset
        // ShapeDecoration of the held header lives within PopupMenuCard's
        // continuously morphing silhouette. The row's early-width text
        // constraints can outgrow that silhouette, so construct the visible
        // fill itself INSIDE the card instead of clipping off its corners.
        val visibleHeaderMask = menuPressedHeaderVisibleBounds(headerMask, panel)
        // Flutter's held item uses _itemRadius throughout _unfold/_fold;
        // ONLY the surrounding PopupMenuCard grows from 16dp to 24dp.
        // Keep the previous visible-bounds fix, not its dynamic radius.
        val headerMaskPath = remember(visibleHeaderMask, envelope, density) {
            gooPlatterOutline(
                visibleHeaderMask.translate(-envelope.left, -envelope.top),
                menuPressedHeaderVisibleRadius(visibleHeaderMask, 16f * density),
            ).asComposePath()
        }
        val headerPress = remember { MutableInteractionSource() }
        val headerPressed by headerPress.collectIsPressedAsState()
        val pressedMaskColor = (if (dark) Color.White else Color.Black)
            .copy(alpha = if (dark) .14f else .075f)

        GlassCaptureBehind(
            Modifier.fillMaxSize(),
            contentVersion = listOf(
                dark, owner.text, contentActionVersion,
                hoveredRow, itemsIn, itemScroll.value, receding,
                panel, radius, contentOutline,
            ),
        ) {
            Box(Modifier.fillMaxSize().drawWithContent {
                clipPath(contentOutline) {
                    if ((headerPressed || hoveredRow == -2) &&
                        visibleHeaderMask != Rect.Zero) {
                        drawPath(headerMaskPath, pressedMaskColor)
                    }
                    this@drawWithContent.drawContent()
                }
            }) {
            Column(
                Modifier
                    .offsetPxSub(contentLeft - envelope.left, contentTop - envelope.top)
                    .size(
                        with(dp) { contentWidth.toDp() },
                        heightDp,
                    )
                    .padding(inset.let { with(dp) { it.toDp() } }),
                verticalArrangement = Arrangement.Top,
            ) {
                // At frame zero this header shares the source row's position.
                // It does not translate out of the centre of the new card.
                Row(
                    Modifier.fillMaxWidth()
                        .height(headerHeightDp.dp)
                        .clickable(
                            enabled = settled > .08f,
                            interactionSource = headerPress,
                            indication = null,
                            role = Role.Button,
                            onClick = onBack,
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    MenuItemContent(owner, dark, headerProgress = settled)
                }

                // Keep the return title pinned. Previously the *whole*
                // card scrolled, but the global hit test treated the title
                // as stationary, so taps after scrolling missed its bounds.
                Column(
                    Modifier.fillMaxWidth()
                        .weight(1f)
                        .verticalScroll(itemScroll)
                        .graphicsLayer { alpha = itemsIn },
                ) {
                    Box(
                        Modifier.fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .height(1.dp)
                            .background(
                                if (dark) Color.White.copy(alpha = .13f)
                                else Color.Black.copy(alpha = .08f),
                            ),
                    )
                    owner.subActions.forEachIndexed { index, action ->
                        MenuSectionDecoration(index, action.section, dark, dp.fontScale)
                        val rowPress = remember(index) { MutableInteractionSource() }
                        val pressed by rowPress.collectIsPressedAsState()
                        Row(
                            Modifier.fillMaxWidth()
                                .height(itemRows.itemHeightDp(index).dp)
                                .menuCheckedSemantics(action)
                                .graphicsLayer {
                                    alpha = if (menuActionEnabled(action)) 1f else .30f
                                }
                                .clip(MenuPressedRowSuperellipse)
                                .background(
                                    if (pressed || hoveredRow == index)
                                        (if (dark) Color.White else Color.Black)
                                            .copy(alpha = if (dark) .14f else .075f)
                                    else Color.Transparent,
                                )
                                .clickable(
                                    // The deepest card only owns row
                                    // activation while fully interactive.
                                    // Underlying/receding rows remain
                                    // semantic-inert during child transitions.
                                    enabled = interactive && settled >= .94f &&
                                        menuActionEnabled(action),
                                    interactionSource = rowPress,
                                    indication = null,
                                    role = Role.Button,
                                ) {
                                    // Keep accessibility activation equivalent to
                                    // touch: a row with children opens its next
                                    // level rather than silently running no-op.
                                    if (action.subActions.isNotEmpty()) {
                                        onNested(index, itemScroll.value.toFloat())
                                    } else {
                                        action.onClick?.let(onSelected)
                                    }
                                },
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            MenuItemContent(action, dark)
                        }
                    }
                }
            }
            }
        }

        if (!interactive && receding > .001f) {
            // The previous card becomes a local dimmed tappable back layer,
            // never a full-screen black modal barrier.
            val localDimPath = remember(local.width, local.height, radius) {
                gooPlatterOutline(
                    Rect(0f, 0f, local.width, local.height), radius,
                ).asComposePath()
            }
            Box(
                Modifier.offsetPxSub(local.left, local.top)
                    .size(
                        with(dp) { local.width.toDp() },
                        with(dp) { local.height.toDp() },
                    )
                    .drawWithContent {
                        clipPath(localDimPath) {
                            this@drawWithContent.drawContent()
                        }
                    }
                    .background(
                        menuRecedingSurfaceColor(dark, glassColors(dark).surface)
                            .copy(alpha = menuRecedingDim(receding)),
                    )
                    .clickable(onClick = onBack),
            )
        }
    }
}

/**
 * Content carried with the top card when a submenu is dismissed straight
 * into the source button. FlClash paints _closingSurface with _paintReturn;
 * do not mistakenly reveal root-level rows under a shrinking child surface.
 * The content leaves within the first 100ms, then only the liquid droplet
 * remains, and its glyph takes over.
 */
@androidx.annotation.RequiresApi(33)
@Composable
internal fun GlassReturningSubmenuContent(
    geometry: GlassSubmenuGeometry,
    owner: GlassMenuAction,
    dark: Boolean,
    framePlatter: Rect,
    contentAlpha: Float,
    liveFrame: (() -> MenuMorphFrame)? = null,
) {
    val panel = geometry.panel
    val target = geometry.target
    if (panel.width <= 1f || panel.height <= 1f ||
        framePlatter.width <= 0f || framePlatter.height <= 0f) return

    val density = LocalDensity.current
    val foreground = glassColors(dark).onSurface
    val sourceWidth = with(density) { panel.width.toDp() }
    val sourceHeight = with(density) { panel.height.toDp() }
    val contentWidth = with(density) { target.width.toDp() }
    val contentHeight = with(density) { target.height.toDp() }
    val radiusDp = with(density) { geometry.radius.toDp() }
    val returnRows = rememberMenuContentRows(
        owner.subActions, density.fontScale, target.width, density.density,
    )
    val returnHeaderHeightDp = rememberMenuContentRows(
        listOf(owner), density.fontScale, target.width, density.density,
    ).itemHeightDp(0)

    Box(
        Modifier.offsetPxSub(panel.left, panel.top)
            .size(sourceWidth, sourceHeight)
            .graphicsLayer {
                transformOrigin = androidx.compose.ui.graphics.TransformOrigin.Center
                // Original _contentTransform uses ONE uniform factor:
                // the smaller of the two ratios, never distorted letters.
                val live = liveFrame?.invoke()
                val framePlatter = live?.platter ?: framePlatter
                val uniform = min(
                    framePlatter.width / panel.width,
                    framePlatter.height / panel.height,
                )
                scaleX = uniform
                scaleY = uniform
                translationX = framePlatter.center.x - panel.center.x
                translationY = framePlatter.center.y - panel.center.y
                alpha = live?.contentAlpha ?: contentAlpha
                clip = true
                shape = androidx.compose.foundation.shape.RoundedCornerShape(radiusDp)
            },
    ) {
        Column(
            Modifier.offsetPxSub(
                target.left - panel.left,
                target.top - panel.top,
            )
                .size(contentWidth, contentHeight)
                .padding(8.dp),
            verticalArrangement = Arrangement.Top,
        ) {
            Row(
                Modifier.fillMaxWidth()
                    .height(returnHeaderHeightDp.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                MenuItemContent(owner, dark, headerProgress = 1f)
            }
            Box(
                Modifier.fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .height(1.dp)
                    .background(
                        if (dark) Color.White.copy(alpha = .13f)
                        else Color.Black.copy(alpha = .08f),
                    ),
            )
            owner.subActions.forEachIndexed { index, action ->
                MenuSectionDecoration(index, action.section, dark, density.fontScale)
                Row(
                    Modifier.fillMaxWidth()
                        .height(returnRows.itemHeightDp(index).dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    MenuItemContent(action, dark)
                }
            }
        }
    }
}

private fun Modifier.offsetPxSub(x: Float, y: Float): Modifier =
    then(Modifier.offset {
        IntOffset(x.roundToInt(), y.roundToInt())
    })
