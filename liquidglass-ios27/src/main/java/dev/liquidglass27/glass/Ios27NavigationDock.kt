package dev.liquidglass27.glass

import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.*
import com.kyant.shapes.Capsule
import com.zeaze.tianyinwallpaper.catalog.components.*

/** iOS 26 body, optics and gestures with iOS 27 surface lighting. */
@RequiresApi(33)
@Composable
fun Ios27NavigationDock(
    destinations: List<GlassDestination>, selectedIndex: Int, onSelect: (Int) -> Unit,
    dark: Boolean, modifier: Modifier = Modifier,
) {
    if (destinations.isEmpty()) return
    val colors = glassColors(dark)
    val context = LocalContext.current
    val density = LocalDensity.current
    // Visible surfaces own separate shaders. The hidden text reference must have no rim.
    val lights = remember(context) {
        List(LiquidBottomTabsSurface.entries.size) { ControlHighlightRenderer(context, profileSamples = 256) }
    }
    val outlines = remember { arrayOfNulls<Pair<Size, android.graphics.Path>>(LiquidBottomTabsSurface.entries.size) }
    val layer: GraphicsLayerScope.(LiquidBottomTabsSurface, Float) -> Unit = surface@ { surface, progress ->
        // The indicator samples Content through CombinedBackdrop. Lighting that
        // full-width reference imports its top/bottom rim into the selected pill.
        if (surface == LiquidBottomTabsSurface.Content) {
            renderEffect = null
            return@surface
        }
        val i = surface.ordinal
        val path = outlines[i]?.takeIf { it.first == size }?.second ?: run {
            val outline = Capsule().createOutline(size, LayoutDirection.Ltr, density)
            Path().apply { addOutline(outline) }.asAndroidPath().also { outlines[i] = size to it }
        }
        val style = if (surface == LiquidBottomTabsSurface.Indicator) {
            val lens = GlassStyle.lens(dark)
            // materialize scales hairline but leaves hairlineSide at its base value.
            lens.materialize(progress).copy(hairlineSide = lens.hairlineSide * progress.coerceIn(0f, 1f))
        } else GlassStyle.pill(dark)
        renderEffect = lights[i].effect(ControlHighlightInput(
            style, size.minDimension / density.density / 2f, emptyList(), emptyList(),
            null, null, path, Offset.Unspecified, 0f, null, 0f, density.density,
            IntSize(size.width.toInt(), size.height.toInt()),
        ))
    }
    LiquidBottomTabsRegular(
        selectedTabIndex = { selectedIndex }, onTabSelected = onSelect,
        backdrop = LocalSketchGlassBackdrop.current, tabsCount = destinations.size,
        isLightTheme = !dark, itemWidth = 94.dp, itemSpacing = (-8).dp,
        modifier = modifier,
        style = LiquidBottomTabsStyle.default(!dark).copy(trackHeight = 62.dp, indicatorHeight = 54.dp),
        surfaceLayer = layer,
    ) {
        destinations.forEachIndexed { index, destination ->
            LiquidBottomTab(onClick = { onSelect(index) },
                modifier = Modifier.semantics { selected = selectedIndex == index }) {
                val tint = if (selectedIndex == index) colors.primary else colors.onSurfaceVariant
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    val glyph = destination.glyph ?: GlassGlyphs.forSymbol(destination.symbol)
                    if (glyph != null) GlassGlyphIcon(glyph, tint, Modifier.size(22.dp))
                    Text(destination.title, color = tint, fontSize = 11.sp)
                }
            }
        }
    }
}
