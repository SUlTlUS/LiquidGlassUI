package dev.liquidglass27.glass

import android.graphics.Path
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.hypot
import kotlin.math.max
import kotlin.math.min

/**
 * Direct Android Path port of FlClash/lib/ui/goo.dart:
 *
 *   gooOutline(pills, blends) + _neck(a, b, blend)
 *
 * As in the Flutter source, each pill keeps its own RSuperellipse outline.
 * ONLY the flowing neck is unioned into the result. There is no invented
 * all-connecting background, no extra resting blend, and no secondary
 * shader-specific bridge. The resulting path is also the AGSL clip shape.
 *
 * Flutter RSuperellipse is approximated by the existing Android path port.
 * All distances here are pixels; Flutter's _neckStep=0.5 logical pixels
 * is converted using density.
 */
internal fun gooRowOutline(
    pills: List<GlassPill>,
    blendsDp: List<Float>,
    density: Float,
    cache: GooRowPathCache? = null,
): Path {
    val rects = pills.map { pill ->
        Rect(
            pill.centerX - pill.halfWidth,
            pill.centerY - pill.halfHeight,
            pill.centerX + pill.halfWidth,
            pill.centerY + pill.halfHeight,
        )
    }
    val pillOutline = Path().apply { fillType = Path.FillType.WINDING }
    for ((index, rect) in rects.withIndex()) {
        // A stationary sibling's Flutter RSuperellipse must not be rebuilt
        // just because the finger is animating a different pill.
        val contour = cache?.pill(index, rect) {
            gooPlatterOutline(rect, min(rect.width, rect.height) * .5f, 2f)
        } ?: gooPlatterOutline(rect, min(rect.width, rect.height) * .5f, 2f)
        pillOutline.addPath(contour)
    }
    var outline = pillOutline
    for (index in 1 until rects.size) {
        val blend = (blendsDp.getOrNull(index - 1) ?: 0f) * density
        val left = rects[index - 1]
        val right = rects[index]
        val neck = if (cache == null) {
            gooNeck(left, right, blend, density)
        } else {
            cache.neck(
                index - 1, GooNeckGeometryKey(left, right, blend, density),
            ) { gooNeck(left, right, blend, density) }
        } ?: continue
        // FlClash goo.dart does Path.combine(PathOperation.union, outline,
        // neck) for EVERY flowing connection. A WINDING-only addPath is not
        // equivalent: when a swollen pill overlaps the neck's polygons, its
        // oppositely directed segments can cancel into straight vertical
        // cuts and sharp white triangular voids. Boolean UNION removes those
        // internal seams, yielding one continuous external silhouette.
        val combined = Path()
        if (combined.op(outline, neck, Path.Op.UNION) && !combined.isEmpty) {
            outline = combined
        }
        // On an uncommon native Path.op failure, retain the last valid pill
        // silhouette. Adding the intersecting neck with addPath would
        // reintroduce a fill-rule hole, so do not fall back to that.
    }
    return outline
}

private fun gooNeck(a: Rect, b: Rect, blend: Float, density: Float): Path? {
    // Exact guards and facing-end sampling limits of Flutter's _neck.
    val from = a.right - min(a.width, a.height) * .5f
    val to = b.left + min(b.width, b.height) * .5f
    if (blend <= 0f || to <= from || b.left - a.right >= blend) return null

    fun distance(rect: Rect, point: Offset): Float {
        val radius = min(rect.width, rect.height) * .5f
        val qx = abs(point.x - rect.center.x) - rect.width * .5f + radius
        val qy = abs(point.y - rect.center.y) - rect.height * .5f + radius
        return hypot(max(qx, 0f), max(qy, 0f)) +
            min(max(qx, qy), 0f) - radius
    }

    fun field(point: Offset): Float {
        val da = distance(a, point)
        val db = distance(b, point)
        val h = (.5f + .5f * (db - da) / blend).coerceIn(0f, 1f)
        return db + (da - db) * h - blend * h * (1f - h)
    }

    fun axisAt(x: Float): Float {
        val t = (x - from) / (to - from)
        return a.center.y + (b.center.y - a.center.y) * t
    }

    val reach = max(a.height, b.height) * .5f + blend
    fun edge(x: Float, toward: Float): Float {
        val axis = axisAt(x)
        var inside = 0f
        var outside = reach
        repeat(16) {
            val middle = (inside + outside) * .5f
            if (field(Offset(x, axis + toward * middle)) < 0f)
                inside = middle else outside = middle
        }
        return axis + toward * (inside + outside) * .5f
    }

    fun tip(inside0: Float, outside0: Float): Float {
        var inside = inside0
        var outside = outside0
        repeat(16) {
            val middle = (inside + outside) * .5f
            if (field(Offset(middle, axisAt(middle))) < 0f)
                inside = middle else outside = middle
        }
        return (inside + outside) * .5f
    }

    fun flows(point: Offset): Boolean =
        abs(distance(a, point) - distance(b, point)) < blend

    val neck = Path()
    val step = (.5f * density).coerceAtLeast(.001f)
    val steps = max(2, ceil((to - from) / step).toInt())
    var top = mutableListOf<Offset>()
    var bottom = mutableListOf<Offset>()

    fun close() {
        if (top.isEmpty()) return
        val vertices = top + bottom.asReversed()
        neck.moveTo(vertices[0].x, vertices[0].y)
        for (point in vertices.drop(1)) neck.lineTo(point.x, point.y)
        neck.close()
        top = mutableListOf()
        bottom = mutableListOf()
    }

    var previous: Float? = null
    var wasInside = false
    for (index in 0..steps) {
        val x = from + (to - from) * index / steps
        val inside = field(Offset(x, axisAt(x))) < 0f
        val upper = if (inside) Offset(x, edge(x, -1f)) else null
        val lower = if (inside) Offset(x, edge(x, 1f)) else null
        if (upper != null && lower != null &&
            (flows(upper) || flows(lower))
        ) {
            if (top.isEmpty() && previous != null && !wasInside) {
                val start = tip(x, previous)
                top.add(Offset(start, axisAt(start)))
            }
            top.add(upper)
            bottom.add(lower)
        } else if (top.isNotEmpty()) {
            if (!inside && previous != null) {
                val end = tip(previous, x)
                top.add(Offset(end, axisAt(end)))
            }
            close()
        }
        previous = x
        wasInside = inside
    }
    close()
    return neck
}
