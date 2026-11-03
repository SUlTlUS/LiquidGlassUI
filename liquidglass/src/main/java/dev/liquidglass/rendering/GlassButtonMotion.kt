package dev.liquidglass.rendering

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shape
import dev.liquidglass.GlassStandard

/** Motion injection keeps the original button's material and input owner intact. */
fun interface GlassButtonMotion {
    fun transform(
        width: Float, height: Float, density: Float, press: Float, drag: Offset,
        pressedExpansionPx: Float, dragInitialDerivative: Float,
    ): GlassButtonTransform
}

data class GlassButtonTransform(val scaleX: Float, val scaleY: Float, val offset: Offset, val press: Float,
    val contour: Shape? = null, val effect: GlassStandard? = null)

val LocalGlassButtonMotion = staticCompositionLocalOf<GlassButtonMotion?> { null }
