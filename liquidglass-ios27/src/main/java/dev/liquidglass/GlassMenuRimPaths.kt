package dev.liquidglass

import android.graphics.Path
import androidx.compose.ui.geometry.Rect
import kotlin.math.min

internal val menuShineBands = floatArrayOf(
    0f, 1f / 3f, 1f,
    1f / 3f, 1f / 3f, .70f,
    2f / 3f, 1f / 3f, .47f,
    1f, 2f / 3f, .18f,
    5f / 3f, 2f / 3f, .12f,
    7f / 3f, 2f / 3f, .085f,
    3f, 2f / 3f, .065f,
    11f / 3f, 2f / 3f, .045f,
    13f / 3f, 2f / 3f, .035f,
)

/** Geometry outlives a light/opacity change, just like FlClash's retained pane. */
internal class GlassMenuRimPaths(
    private val platter: Rect,
    radius: Float,
    density: Float,
    private val cornerPower: Float,
    outlinePath: Path?,
) {
    private val limit = min(platter.width, platter.height) * .5f
    private val rounded = radius.coerceIn(0f, limit)
    private val boundaries = HashMap<Float, Path>()
    private fun boundary(depth: Float): Path = boundaries.getOrPut(depth) {
        gooPlatterOutline(platter.deflate(depth), (rounded - depth).coerceAtLeast(0f), cornerPower)
    }

    val outer: Path = outlinePath ?: boundary(0f)
    val line: Path = gooPlatterOutline(
        platter.inflate(.335f * density), rounded + .335f * density, cornerPower,
    )
    // Adjacent bands reuse a boundary instead of fitting it twice. Depths
    // retain the original float arithmetic, including subpixel bands.
    val rings: List<Path?> = (menuShineBands.indices step 3).map { i ->
        val start = menuShineBands[i] * density
        val end = start + menuShineBands[i + 1] * density
        if (start >= limit) null else Path().apply {
            fillType = Path.FillType.EVEN_ODD
            addPath(boundary(start))
            if (end < limit) addPath(boundary(end))
        }
    }

    init { boundaries.clear() }
}
