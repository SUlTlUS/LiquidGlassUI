package dev.liquidglass

import android.content.Context
import android.graphics.Path
import android.graphics.RenderEffect
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.IntSize

/** Native View bridge to the same contour, tint and lighting used by GlassButton.
 * The host supplies its already processed Sketch material as the RenderNode input.
 * Never apply this effect to foreground icons/text or the external shadow.
 * [context] must expose the component library's shader assets.
 */
class NativeGlassControlSurface(context: Context) {
    private val lighting = ControlHighlightRenderer(context)

    fun contour(width: Float, height: Float, radius: Float): Path =
        gooPlatterOutline(Rect(0f, 0f, width, height), radius)

    fun tint(dark: Boolean): Int = GlassStyle.regular(dark).tint.toArgb()

    fun lightingEffect(width: Int, height: Int, radius: Float, density: Float,
        dark: Boolean, contour: Path): RenderEffect? = lighting.effect(ControlHighlightInput(
            style = GlassStyle.regular(dark), cornerDp = radius / density,
            pills = emptyList(), blendsDp = emptyList(), morph = null, outline = null,
            nativePath = contour, glow = Offset.Unspecified, glowStrength = 0f,
            glowReachDp = null, glowWash = 0f, density = density, size = IntSize(width, height),
        ))?.asAndroidRenderEffect()
}
