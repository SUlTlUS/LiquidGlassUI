package dev.liquidglass.rendering

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.zeaze.tianyinwallpaper.backdrop.Backdrop
import com.zeaze.tianyinwallpaper.catalog.components.LiquidButtonStyle
import com.zeaze.tianyinwallpaper.catalog.utils.AdaptiveLuminanceGlassState

data class LiquidButtonFusionMaterial(
    val backdrop: Backdrop,
    val style: LiquidButtonStyle,
    val tint: Color,
    val surface: Color,
    val luminance: AdaptiveLuminanceGlassState?,
    val buttonEffect: dev.liquidglass.GlassStandard? = null,
)

/** Observes the original painted transform. It cannot replace material, input or animation. */
class LiquidButtonFusionAttachment(
    val modifier: Modifier,
    val onTransform: (scaleX: Float, scaleY: Float, x: Float, y: Float, press: Float) -> Unit,
)

val LocalLiquidButtonFusion = staticCompositionLocalOf<
    (@Composable (LiquidButtonFusionMaterial) -> LiquidButtonFusionAttachment)?
> { null }
