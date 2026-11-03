package com.zeaze.tianyinwallpaper.catalog.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.GraphicsLayerScope
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.util.fastCoerceAtMost
import androidx.compose.ui.util.lerp
import com.kyant.shapes.Capsule
import com.zeaze.tianyinwallpaper.backdrop.Backdrop
import com.zeaze.tianyinwallpaper.backdrop.drawBackdrop
import com.zeaze.tianyinwallpaper.backdrop.effects.blur
import com.zeaze.tianyinwallpaper.backdrop.effects.colorControls
import com.zeaze.tianyinwallpaper.backdrop.effects.lens
import com.zeaze.tianyinwallpaper.backdrop.effects.vibrancy
import com.zeaze.tianyinwallpaper.catalog.utils.AdaptiveLuminanceGlassState
import com.zeaze.tianyinwallpaper.catalog.utils.InteractiveHighlight
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tanh
import dev.liquidglass.rendering.LocalLiquidButtonFusion
import dev.liquidglass.rendering.LiquidButtonFusionMaterial
import dev.liquidglass.rendering.LocalGlassButtonLighting
import dev.liquidglass.rendering.LocalGlassButtonMotion
import com.zeaze.tianyinwallpaper.backdrop.highlight.Highlight

@Composable
internal fun LiquidButtonCore(
    onClick: () -> Unit,
    backdrop: Backdrop,
    modifier: Modifier,
    isInteractive: Boolean,
    tint: Color,
    surfaceColor: Color,
    luminanceState: AdaptiveLuminanceGlassState?,
    style: LiquidButtonStyle,
    buttonHeight: Dp,
    contentPadding: PaddingValues,
    @DrawableRes iconRes: Int?,
    iconContentDescription: String?,
    iconSize: Dp,
    iconTint: Color,
    iconOffsetX: Dp,
    iconOffsetY: Dp,
    content: (@Composable RowScope.() -> Unit)?,
    enabled: Boolean = true,
) {
    val animationScope = rememberCoroutineScope()
    val motion = LocalGlassButtonMotion.current
    val interactiveHighlight = remember(animationScope) {
        InteractiveHighlight(animationScope = animationScope)
    }
    var buttonSize by remember { mutableStateOf(IntSize.Zero) }
    val buttonDensity = LocalDensity.current.density
    val customFrame = motion?.transform(buttonSize.width.toFloat(), buttonSize.height.toFloat(),
        buttonDensity, interactiveHighlight.pressProgress, interactiveHighlight.offset,
        style.pressedExpansion.value * buttonDensity, style.dragInitialDerivative)
    val lighting = LocalGlassButtonLighting.current?.invoke(customFrame?.contour)
    val fusion = LocalLiquidButtonFusion.current?.invoke(
        LiquidButtonFusionMaterial(backdrop, style, tint, surfaceColor, luminanceState, customFrame?.effect))

    Row(
        modifier
            .onSizeChanged { buttonSize = it }
            .then(fusion?.modifier ?: Modifier)
            .drawBackdrop(
                backdrop = backdrop,
                shape = { customFrame?.contour ?: Capsule() },
                highlight = if (lighting == null) ({ Highlight.Default }) else null,
                surfaceLayerBlock = lighting,
                effects = {
                    if (!style.preserveBackdropColors) {
                        luminanceState?.let { state ->
                            colorControls(
                                brightness = state.brightness,
                                contrast = state.contrast,
                                saturation = state.saturation
                            )
                        } ?: vibrancy()
                    }
                    blur(style.blurRadius.toPx())
                    lens(style.lensRadiusX.toPx(), style.lensRadiusY.toPx())
                },
                layerBlock = if (isInteractive && enabled) {
                    {
                        val progress = interactiveHighlight.pressProgress
                        val custom = customFrame
                        if (custom != null) {
                            scaleX = custom.scaleX; scaleY = custom.scaleY
                            translationX = custom.offset.x; translationY = custom.offset.y
                        } else {
                            applyDefaultButtonTransform(interactiveHighlight, style)
                        }
                        fusion?.onTransform?.invoke(scaleX, scaleY, translationX, translationY, custom?.press ?: progress)
                    }
                } else null,
                onDrawSurface = {
                    if (tint.isSpecified) {
                        drawRect(tint, blendMode = BlendMode.Hue)
                        drawRect(tint.copy(alpha = 0.75f))
                    }
                    if (surfaceColor.isSpecified) drawRect(surfaceColor)
                }
            )
            .clickable(
                enabled = enabled,
                interactionSource = null,
                indication = if (isInteractive) null else LocalIndication.current,
                role = Role.Button,
                onClick = onClick
            )
            .then(
                if (isInteractive && enabled) {
                    Modifier.then(interactiveHighlight.modifier).then(interactiveHighlight.gestureModifier)
                } else Modifier
            )
            .height(buttonHeight)
            .padding(contentPadding),
        horizontalArrangement = Arrangement.spacedBy(style.contentSpacing, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        when {
            content != null -> content.invoke(this)
            iconRes != null -> Icon(
                painter = painterResource(id = iconRes),
                contentDescription = iconContentDescription,
                modifier = Modifier.size(iconSize).offset(x = iconOffsetX, y = iconOffsetY),
                tint = iconTint
            )
        }
    }
}

/** Standalone core controls retain their original motion without a theme injection. */
internal fun GraphicsLayerScope.applyDefaultButtonTransform(
    highlight: InteractiveHighlight,
    style: LiquidButtonStyle,
) {
    val width = size.width
    val height = size.height
    val scale = lerp(1f, 1f + style.pressedExpansion.toPx() / height, highlight.pressProgress)
    val maxOffset = size.minDimension
    val offset = highlight.offset
    translationX = maxOffset * tanh(style.dragInitialDerivative * offset.x / maxOffset)
    translationY = maxOffset * tanh(style.dragInitialDerivative * offset.y / maxOffset)
    val maxDragScale = style.pressedExpansion.toPx() / height
    val angle = atan2(offset.y, offset.x)
    scaleX = scale + maxDragScale * abs(cos(angle) * offset.x / size.maxDimension) *
        (width / height).fastCoerceAtMost(1f)
    scaleY = scale + maxDragScale * abs(sin(angle) * offset.y / size.maxDimension) *
        (height / width).fastCoerceAtMost(1f)
}
