package dev.liquidglass27.glass

import androidx.compose.ui.geometry.Offset
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

internal fun glassSidebarGlyph(progress: Float): GlassGlyph {
    val divider = 8.5f + 3f * progress
    return GlassGlyph(buildList {
        add(GlassGlyphShapes.box(3f, 4.5f, 21f, 19.5f, 3.5f))
        add(GlassGlyphShapes.polyline(GlassGlyphVertex(divider, 4.5f), GlassGlyphVertex(divider, 19.5f), role = GlassGlyphRole.Detail))
        for (y in listOf(8.5f, 11f, 13.5f)) add(GlassGlyphShapes.polyline(
            GlassGlyphVertex(5.5f, y), GlassGlyphVertex(divider - 2.5f, y), role = GlassGlyphRole.Detail))
    }, matchTextDirection = true)
}

internal fun glassPlayPauseGlyph(progress: Float): GlassGlyph {
    val fold = progress.coerceIn(0f, 1f)
    val angle = (PI / 2 * progress).toFloat()
    fun turned(p: Offset): GlassGlyphVertex {
        val q = p - Offset(12f, 12f)
        return GlassGlyphVertex(12f + q.x * cos(angle) - q.y * sin(angle),
            12f + q.x * sin(angle) + q.y * cos(angle), 1.2f)
    }
    if (fold == 0f) return GlassGlyph(listOf(GlassGlyphShapes.polyline(
        *listOf(Offset(8.3f, 5.6f), Offset(18.8f, 12f), Offset(8.3f, 18.4f)).map(::turned).toTypedArray(), closed = true)))
    val halves = listOf(
        listOf(Offset(8.3f, 5.6f), Offset(18.8f, 12f), Offset(18.8f, 12f), Offset(8.3f, 12f)) to
            listOf(Offset(5.9f, 6.9f), Offset(18.1f, 6.9f), Offset(18.1f, 9.1f), Offset(5.9f, 9.1f)),
        listOf(Offset(8.3f, 12f), Offset(18.8f, 12f), Offset(18.8f, 12f), Offset(8.3f, 18.4f)) to
            listOf(Offset(5.9f, 14.9f), Offset(18.1f, 14.9f), Offset(18.1f, 17.1f), Offset(5.9f, 17.1f)))
    return GlassGlyph(halves.map { (half, bar) -> GlassGlyphShapes.polyline(
        *half.indices.map { turned(half[it] + (bar[it] - half[it]) * fold) }.toTypedArray(), closed = true) })
}
