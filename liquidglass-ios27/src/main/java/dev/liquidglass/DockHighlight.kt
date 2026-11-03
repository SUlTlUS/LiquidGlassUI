package dev.liquidglass

import androidx.annotation.RequiresApi
import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.*
import com.zeaze.tianyinwallpaper.catalog.components.*

internal class DockAppearance(
    val shape: Shape,
    val layer: GraphicsLayerScope.(LiquidBottomTabsSurface, Float) -> Unit,
)

/** Reuse 27 lighting with host-owned regular/split-search dock layouts. */
@RequiresApi(33)
@Composable
internal fun rememberDockAppearance(dark: Boolean, tone: () -> Float = { if (dark) 1f else 0f }): DockAppearance {
    val context = LocalContext.current
    val density = LocalDensity.current
    // Visible surfaces own separate shaders. The hidden text reference must have no rim.
    val lights = remember(context) {
        List(LiquidBottomTabsSurface.entries.size) { ControlHighlightRenderer(context, dock = true) }
    }
    val dockShape = remember { FlClashDockShape() }
    val layer: GraphicsLayerScope.(LiquidBottomTabsSurface, Float) -> Unit = surface@ { surface, progress ->
        // The indicator samples Content through CombinedBackdrop. Lighting that
        // full-width reference imports its top/bottom rim into the selected pill.
        if (surface == LiquidBottomTabsSurface.Content) {
            renderEffect = null
            return@surface
        }
        val i = surface.ordinal
        val style = if (surface == LiquidBottomTabsSurface.Indicator) {
            val lens = GlassStyle.interpolate(GlassStyle.lens(false), GlassStyle.lens(true), tone())
            // materialize scales hairline but leaves hairlineSide at its base value.
            lens.materialize(progress).copy(hairlineSide = lens.hairlineSide * progress.coerceIn(0f, 1f))
        } else GlassStyle.interpolate(GlassStyle.pill(false), GlassStyle.pill(true), tone())
        renderEffect = lights[i].effect(ControlHighlightInput(
            // Theme-specific bright-band width; automatic backdrop tone does not select it.
            if (dark) style.copy(rimWidth = style.rimWidth * .7f) else style,
            size.minDimension / density.density / 2f, emptyList(), emptyList(),
            // Original dock uses LiquidGlassCorners, never an outlined radial profile.
            null, null, null, Offset.Unspecified, 0f, null, 0f, density.density,
            IntSize(size.width.toInt(), size.height.toInt()),
        ))
    }
    return DockAppearance(dockShape, layer)
}

