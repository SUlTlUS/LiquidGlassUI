package dev.liquidglass

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.layout.layout
import androidx.compose.ui.semantics.*
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.offset
import com.zeaze.tianyinwallpaper.backdrop.Backdrop
import com.zeaze.tianyinwallpaper.backdrop.backdrops.rememberCanvasBackdrop
import com.zeaze.tianyinwallpaper.catalog.components.LiquidButton
import com.zeaze.tianyinwallpaper.catalog.components.LiquidButtonStyle
import com.zeaze.tianyinwallpaper.catalog.components.LiquidSlider
import com.zeaze.tianyinwallpaper.catalog.components.LiquidToggle
import com.zeaze.tianyinwallpaper.catalog.utils.AdaptiveLuminanceGlassState
import dev.liquidglass.GlassStandard

/** Shared source or an independent theme-colored fallback. Never captures a control's parent. */
@Composable
private fun resolveBackdrop(backdrop: Backdrop?): Backdrop {
    val background = LocalGlassBackgroundColor.current
    val fallback = rememberCanvasBackdrop { drawRect(background) }
    return backdrop ?: currentGlassBackdrop() ?: fallback
}

/** A button following GlassTheme's standard. [animatePress] controls motion, [enabled] controls input. */
@Composable
fun GlassButton(
    onClick: () -> Unit,
    backdrop: Backdrop? = currentGlassBackdrop(),
    modifier: Modifier = Modifier,
    animatePress: Boolean = true,
    tint: Color = Color.Unspecified,
    surfaceColor: Color = Color.Unspecified,
    luminanceState: AdaptiveLuminanceGlassState? = null,
    style: LiquidButtonStyle = LiquidButtonStyle.Default,
    height: Dp = style.height,
    contentPadding: PaddingValues = PaddingValues(horizontal = style.horizontalPadding),
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit,
) {
    val source = resolveBackdrop(backdrop)
    if (currentGlassMaterial() == GlassStandard.Ios26) {
        LiquidButton(onClick, source, modifier, animatePress, tint, surfaceColor, luminanceState,
            style, height, contentPadding, enabled = enabled, content = content)
        return
    }
    val dark = isGlassDarkTheme()
    val material = (if (tint.isSpecified) GlassStyle.tinted(tint, dark) else GlassStyle.regular(dark))
        .let { if (surfaceColor.isSpecified) it.copy(tint = surfaceColor) else it }
    GlassBackdropProvider(source) {
        val body: @Composable BoxScope.() -> Unit = {
            Row(Modifier.padding(contentPadding), horizontalArrangement = Arrangement.spacedBy(style.contentSpacing,
                Alignment.CenterHorizontally), verticalAlignment = Alignment.CenterVertically, content = content)
        }
        if (animatePress && enabled) ElasticGlassButton(modifier.height(height), material, onClick = onClick, content = body)
        else Box(modifier.height(height).clickable(enabled = enabled, onClick = onClick), contentAlignment = Alignment.Center) {
            GlassPane(Modifier.matchParentSize(), material)
            body()
        }
    }
}

/** Goo uses the selected standard, while the reserved paint margin leaves measured layout unchanged. */
@Composable
fun GlassButtonGroup(modifier: Modifier = Modifier, backdrop: Backdrop? = currentGlassBackdrop(),
    content: @Composable BoxScope.() -> Unit) {
    if (currentGlassMaterial() == GlassStandard.Ios26) Ios26FusionGroup(modifier, content)
    else GlassBackdropProvider(resolveBackdrop(backdrop)) {
        GooGlassGroup(modifier.layout { measurable, constraints ->
            val inset = 24.dp.roundToPx()
            val child = measurable.measure(constraints.offset(inset * 2, inset * 2))
            layout(child.width - inset * 2, child.height - inset * 2) { child.place(-inset, -inset) }
        }, content = content)
    }
}

/** Controlled switch; both standards intentionally use the same original switch implementation. */
@Composable
fun GlassSwitch(checked: Boolean, onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier, backdrop: Backdrop? = currentGlassBackdrop(),
    darkTheme: Boolean = isGlassDarkTheme(),
    onDragStart: () -> Unit = {}, onDragEnd: () -> Unit = {}) {
    LiquidToggle(selected = { checked }, onSelect = onCheckedChange, backdrop = resolveBackdrop(backdrop),
        isLightTheme = !darkTheme, modifier = modifier.semantics(mergeDescendants = true) {
            role = Role.Switch
            toggleableState = if (checked) ToggleableState.On else ToggleableState.Off
            onClick { onCheckedChange(!checked); true }
        }, onDragStarted = onDragStart, onDragFinished = onDragEnd)
}

/** Controlled value; callers need not supply a value lambda or an animation convergence threshold. */
@Composable
fun GlassSlider(value: Float, onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier, valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    backdrop: Backdrop? = currentGlassBackdrop(), darkTheme: Boolean = isGlassDarkTheme(),
    dragAnywhere: Boolean = true, onValueChangeFinished: () -> Unit = {}) {
    LiquidSlider(value = { value }, onValueChange = onValueChange, valueRange = valueRange,
        visibilityThreshold = .001f, backdrop = resolveBackdrop(backdrop), isLightTheme = !darkTheme,
        modifier = modifier.semantics {
            progressBarRangeInfo = ProgressBarRangeInfo(value.coerceIn(valueRange), valueRange)
            setProgress { target -> onValueChange(target.coerceIn(valueRange)); onValueChangeFinished(); true }
        }, dragWholeTrack = dragAnywhere, onValueChangeFinished = onValueChangeFinished)
}
