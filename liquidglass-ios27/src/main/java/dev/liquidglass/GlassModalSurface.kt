package dev.liquidglass

import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.background
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.toSize
import com.zeaze.tianyinwallpaper.backdrop.backdrops.LayerBackdrop
import com.zeaze.tianyinwallpaper.backdrop.backdrops.layerBackdrop

/** Adapt authored modal geometry to the shared 27 pane, recording only its material for toolbar buttons. */
@RequiresApi(33)
@Composable
fun GlassModalSurface(
    shape: Shape,
    dark: Boolean,
    modifier: Modifier = Modifier,
    exportedBackdrop: LayerBackdrop? = null,
    solidColor: Color? = null,
    /** Export the final painted surface (including its rim), never the interactive content. */
    exportPaintedSurface: Boolean = false,
) {
    val density = LocalDensity.current
    val direction = LocalLayoutDirection.current
    var size by remember { mutableStateOf(IntSize.Zero) }
    val outline = remember(shape, size, density, direction) { shape.createOutline(size.toSize(), direction, density) }
    val path = remember(outline) {
        Path().apply {
            when (outline) {
                is Outline.Rectangle -> addRect(outline.rect)
                is Outline.Rounded -> addRoundRect(outline.roundRect)
                is Outline.Generic -> addPath(outline.path)
            }
        }.asAndroidPath()
    }
    val radius = (outline as? Outline.Rounded)?.roundRect?.topLeftCornerRadius?.x ?: 34f * density.density
    Box(modifier.onSizeChanged { size = it }) {
        if (size.width > 0 && size.height > 0) {
            MenuCardShadow(path, density.density, dark, 1f, Modifier.fillMaxSize())
            val materialModifier = Modifier.fillMaxSize().then(
                if (exportedBackdrop != null) Modifier.layerBackdrop(exportedBackdrop) else Modifier)
            if (solidColor != null) {
                // Opaque sheets export their fill only, without blur, refraction or a glass rim.
                Box(materialModifier.background(solidColor.copy(alpha = 1f), shape))
            } else {
                Box(if (exportPaintedSurface) materialModifier else Modifier.fillMaxSize()) {
                    GlassPane(Modifier.fillMaxSize(), style = GlassStyle.alert(dark),
                        exportedBackdrop = if (exportPaintedSurface) null else exportedBackdrop,
                        outline = GlassOutlineRect(Rect(Offset.Zero, size.toSize()), radius, path))
                }
            }
        }
    }
}
