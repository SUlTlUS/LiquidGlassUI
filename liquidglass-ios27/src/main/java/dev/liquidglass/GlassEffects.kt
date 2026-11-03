package dev.liquidglass

import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.GraphicsLayerScope
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import dev.liquidglass.rendering.LocalGlassButtonLighting
import dev.liquidglass.rendering.LocalGlassButtonMotion
import dev.liquidglass.rendering.GlassButtonMotion

/** Material, light and button motion follow the overall standard unless overridden. */
@Immutable
data class GlassEffects(
    val material: GlassStandard? = null,
    val highlight: GlassStandard? = null,
    /** Entire button press/drag motion only; null follows the overall standard. */
    val buttonDragEffect: GlassStandard? = null,
    /** Limit button displacement and distortion together for both 26 and 27 effects. */
    val limitButtonDrag: Boolean = true,
) {
    fun materialFor(standard: GlassStandard) = material ?: standard
    fun highlightFor(standard: GlassStandard) = highlight ?: standard
    internal fun buttonAnimation(standard: GlassStandard) = buttonDragEffect ?: standard
}

val LocalGlassEffects = staticCompositionLocalOf { GlassEffects() }

@Composable
fun currentGlassMaterial(): GlassStandard = LocalGlassEffects.current.materialFor(LocalGlassStandard.current)

/** Override one subtree without replacing its palette, backdrop, or interaction state. */
@Composable
fun GlassEffectScope(
    effects: GlassEffects = LocalGlassEffects.current,
    standard: GlassStandard = LocalGlassStandard.current,
    content: @Composable () -> Unit,
) {
    val highlight = effects.highlightFor(standard)
    val lighting: (@Composable (Shape?) -> (GraphicsLayerScope.() -> Unit))? =
        if (highlight == GlassStandard.Ios27) ({ contour -> rememberButtonLighting27(contour) }) else null
    val buttonAnimation = effects.buttonAnimation(standard)
    val motion = remember(buttonAnimation, effects.limitButtonDrag) {
        GlassButtonMotion { width, height, density, press, drag, expansion, derivative ->
            val frame = buttonDragTransform(buttonAnimation,
                androidx.compose.ui.geometry.Rect(0f, 0f, width, height), press, drag, density,
                pressedExpansionPx = expansion, dragInitialDerivative = derivative,
                limitDrag = effects.limitButtonDrag)
            dev.liquidglass.rendering.GlassButtonTransform(frame.scaleX, frame.scaleY, frame.pull, press,
                ButtonContourShape(buttonAnimation,
                    if (usesStretchedButtonContour(buttonAnimation)) 1f else frame.scaleX,
                    if (usesStretchedButtonContour(buttonAnimation)) 1f else frame.scaleY), buttonAnimation)
        }
    }
    CompositionLocalProvider(
        LocalGlassStandard provides standard,
        LocalGlassEffects provides effects,
        LocalGlassMenuHighlight provides if (highlight == GlassStandard.Ios26) GlassMenuHighlight.Ios26 else GlassMenuHighlight.Ios27,
        LocalGlassButtonLighting provides lighting,
        LocalGlassButtonMotion provides motion,
        content = content,
    )
}

@Composable
private fun rememberButtonLighting27(contour: Shape?): GraphicsLayerScope.() -> Unit {
    val context = LocalContext.current
    val density = LocalDensity.current.density
    val renderer = remember(context) { ControlHighlightRenderer(context) }
    val style = GlassStyle.regular(isGlassDarkTheme())
    return remember(renderer, style, contour, density) {
        var outlineSize = Size.Unspecified
        var nativePath: android.graphics.Path? = null
        val block: GraphicsLayerScope.() -> Unit = {
            val buttonPill = (contour as? ButtonContourShape)?.highlightPill(size)
            if (buttonPill == null && contour != null && size != outlineSize) {
                outlineSize = size
                nativePath = (contour.createOutline(size, LayoutDirection.Ltr, Density(density)) as? Outline.Generic)
                    ?.path?.asAndroidPath()
            }
            renderEffect = renderer.effect(ControlHighlightInput(style, size.minDimension / density / 2f,
                buttonPill?.let { listOf(it) } ?: emptyList(), emptyList(), null, null,
                nativePath, Offset.Unspecified, 0f, null, 0f,
                density, IntSize(size.width.toInt(), size.height.toInt())))
        }
        block
    }
}
