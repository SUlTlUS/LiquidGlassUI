package dev.liquidglass.rendering

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import com.zeaze.tianyinwallpaper.backdrop.ShapeProvider
import com.zeaze.tianyinwallpaper.backdrop.highlight.Highlight
import com.zeaze.tianyinwallpaper.backdrop.highlight.HighlightElement

/** Reuse the iOS 26 highlight, including tilt lifecycle, without another material pass. */
fun Modifier.drawGlassHighlight(
    shape: () -> Shape,
    highlight: () -> Highlight? = { Highlight.Default },
): Modifier = then(HighlightElement(ShapeProvider(shape), highlight))
