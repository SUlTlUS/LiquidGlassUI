package dev.liquidglass27.glass

import android.graphics.Path
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Android Path port of FlClash/lib/ui/goo.dart's gooPairOutline, gooPairDepth
 * and adaptive _bridge algorithm (GPL-3.0). All coordinates are physical px.
 *
 * The GPU evaluates the same polynomial smooth-min for refractive shading,
 * but the actual silhouette is clipped to the union + adaptive bridge.
 */
internal data class GooPairPath(
    val outline: Path,
    val platterDepth: Float,
    val dropletDepth: Float,
) {
    /** The droplet is entirely inside the platter and its blending halo. */
    val merged: Boolean get() = platterDepth >= 0f
}

/** Positive when [inner] lies at least [blend] inside [outer]. */
internal fun gooPairDepth(outer: Rect, radius: Float, inner: Rect, blend: Float): Float {
    val leftTop = roundedDistance(outer, radius, inner.topLeft)
    val rightTop = roundedDistance(outer, radius, inner.topRight)
    val leftBottom = roundedDistance(outer, radius, inner.bottomLeft)
    val rightBottom = roundedDistance(outer, radius, inner.bottomRight)
    return -max(max(leftTop, rightTop), max(leftBottom, rightBottom)) - blend
}

// Flutter Offset.distance uses sqrt(x*x + y*y). Double intermediates retain
// the former hypot-to-Float result for pixel coordinates without its scaling
// machinery in every ray step.
@Suppress("NOTHING_TO_INLINE")
internal inline fun gooDistanceLength(x: Float, y: Float): Float =
    sqrt(x.toDouble() * x + y.toDouble() * y).toFloat()

/** A ray fan evaluates the same two rectangles hundreds of times. */
internal class GooRoundedRectDistance(rect: Rect, radius: Float) {
    private val halfWidth = rect.width * .5f
    private val halfHeight = rect.height * .5f
    private val centerX = rect.center.x
    private val centerY = rect.center.y
    private val round = radius.coerceIn(0f, min(rect.width, rect.height) * .5f)

    fun at(point: Offset): Float {
        val qx = abs(point.x - centerX) - halfWidth + round
        val qy = abs(point.y - centerY) - halfHeight + round
        return gooDistanceLength(max(qx, 0f), max(qy, 0f)) +
            min(max(qx, qy), 0f) - round
    }
}

private fun roundedDistance(rect: Rect, radius: Float, p: Offset): Float {
    val r = radius.coerceIn(0f, min(rect.width, rect.height) * .5f)
    val qx = abs(p.x - rect.center.x) - rect.width * .5f + r
    val qy = abs(p.y - rect.center.y) - rect.height * .5f + r
    return gooDistanceLength(max(qx, 0f), max(qy, 0f)) + min(max(qx, qy), 0f) - r
}

private fun smoothMin(a: Float, b: Float, blend: Float): Float {
    if (blend <= .0001f) return min(a, b)
    val h = (.5f + .5f * (b - a) / blend).coerceIn(0f, 1f)
    return b + (a - b) * h - blend * h * (1f - h)
}

/**
 * The normal platter and pill use Flutter RSuperellipse's fitted octants:
 * two weighted conics joined to a circular arc per octant. Only custom
 * intermediate morph exponents retain the former parametric fallback.
 */
/** Shared contour for the painted glass, rim, and fallback. */
internal fun gooPlatterOutline(
    rect: Rect, radius: Float, cornerPower: Float = 3.5f,
): Path = squircleRect(rect, radius, cornerPower)

private fun squircleRect(
    rect: Rect, radius: Float, cornerPower: Float = 3.5f,
): Path {
    val path = Path()
    if (rect.width <= 0f || rect.height <= 0f) return path
    val r = radius.coerceIn(0f, min(rect.width, rect.height) * .5f)
    if (r < .001f) {
        path.addRect(rect.left, rect.top, rect.right, rect.bottom, Path.Direction.CW)
        return path
    }
    // FlClash paints both its platter and its pill using Flutter's native
    // RSuperellipse, which blends power curves into a circular diagonal arc.
    // The old 12-line exponent path could not reproduce that curvature.
    // Keep the intermediate analytic power contour only for custom morph
    // exponents between the source's nominal circular and platter states.
    if (abs(cornerPower - 3.5f) < .0001f ||
        abs(cornerPower - 2f) < .0001f
    ) {
        val native = flutterRSuperellipseOutline(rect, r)
        if (native != null) return native
    }
    if (cornerPower <= 2.0001f) {
        path.addRoundRect(
            rect.left, rect.top, rect.right, rect.bottom, r, r, Path.Direction.CW,
        )
        return path
    }
    val cornerExponent = 2f / cornerPower.coerceIn(2f, 6f)
    fun curve(cx: Float, cy: Float, a0: Double, a1: Double) {
        for (i in 1..12) {
            val angle = a0 + (a1 - a0) * i / 12.0
            val c = cos(angle).toFloat()
            val s = sin(angle).toFloat()
            val x = cx + (if (c < 0f) -1f else 1f) * r * abs(c).pow(cornerExponent)
            val y = cy + (if (s < 0f) -1f else 1f) * r * abs(s).pow(cornerExponent)
            path.lineTo(x, y)
        }
    }
    path.moveTo(rect.left + r, rect.top)
    path.lineTo(rect.right - r, rect.top)
    curve(rect.right - r, rect.top + r, -PI * .5, 0.0)
    path.lineTo(rect.right, rect.bottom - r)
    curve(rect.right - r, rect.bottom - r, 0.0, PI * .5)
    path.lineTo(rect.left + r, rect.bottom)
    curve(rect.left + r, rect.bottom - r, PI * .5, PI)
    path.lineTo(rect.left, rect.top + r)
    curve(rect.left + r, rect.top + r, PI, PI * 1.5)
    path.close()
    return path
}

internal fun gooPairOutline(
    platter: Rect,
    radius: Float,
    droplet: Rect,
    blend: Float,
    density: Float,
    coarse: Boolean,
    platterCornerPower: Float = 3.5f,
): GooPairPath {
    val platePath = squircleRect(platter, radius, platterCornerPower)
    val dropletRadius = min(droplet.width, droplet.height) * .5f
    // A traveling control droplet MUST remain circular/capsular. When the
    // platter is absorbed by it, this path becomes the final visible outline.
    val dropPath = squircleRect(droplet, dropletRadius, 2f)
    val plateDepth = gooPairDepth(platter, radius, droplet, blend)
    val dropDepth = gooPairDepth(droplet, dropletRadius, platter, blend)

    if (plateDepth >= 0f) return GooPairPath(platePath, plateDepth, dropDepth)
    if (dropDepth >= 0f) return GooPairPath(dropPath, plateDepth, dropDepth)

    val bridge = adaptiveBridge(
        platter, radius, droplet, blend,
        if (coarse) 64 else 128,
        (if (coarse) .15f else .05f) * density,
    )
    val outline = Path()
    if (bridge == null) {
        if (!outline.op(platePath, dropPath, Path.Op.UNION)) {
            outline.reset()
            // A native Path.op failure is unusual but must not produce a
            // wholly empty pane. Plain disjoint same-direction contours are
            // safe when there is NO crossing neck to create a winding hole.
            outline.addPath(platePath)
            outline.addPath(dropPath)
        }
    } else {
        // Match FlClash goo.dart exactly: combine the two plate contours
        // into a single input path, and UNION that input with the bridge.
        val contours = Path().apply {
            addPath(platePath)
            addPath(dropPath)
        }
        if (!outline.op(contours, bridge, Path.Op.UNION)) {
            outline.reset()
            // NEVER append an intersecting bridge with addPath; that was
            // the source of the white X/straight cut at a flowing neck.
            // Fall back to the two pills (with true union if available)
            // until the next frame yields a valid native boolean path.
            if (!outline.op(platePath, dropPath, Path.Op.UNION)) {
                outline.reset()
                outline.addPath(platePath)
                outline.addPath(dropPath)
            }
        }
    }
    return GooPairPath(outline, plateDepth, dropDepth)
}

private enum class Reach { DEEP, FLOWING, CLEAR }

/**
 * Original FlClash fan tracing: cast every fourth ray, recursively refine
 * only the flowing sectors, and simplify the resulting bridge polygon.
 */
private fun adaptiveBridge(
    a: Rect, aRadius: Float, b: Rect, blend: Float,
    turns: Int, tolerance: Float,
): Path? {
    if (blend <= 0f || b.width <= 0f || b.height <= 0f) return null
    val center = b.center
    val limit = max(b.width, b.height) * .5f + max(a.width, a.height) + blend
    val bRadius = min(b.width, b.height) * .5f
    val platterField = GooRoundedRectDistance(a, aRadius)
    val dropletField = GooRoundedRectDistance(b, bRadius)
    fun toA(point: Offset) = platterField.at(point)
    fun field(point: Offset) = smoothMin(
        toA(point), dropletField.at(point), blend,
    )
    fun castReach(angle: Double): Offset {
        val direction = Offset(cos(angle).toFloat(), sin(angle).toFloat())
        var inside = 0f
        var distance = 0f
        while (distance < limit) {
            val point = center + direction * distance
            val platterDistance = toA(point)
            if (platterDistance < -blend) return point
            // FlClash's _bridge.reach reuses this already measured distance.
            val depth = smoothMin(platterDistance, dropletField.at(point), blend)
            if (depth >= 0f) {
                var outside = distance
                repeat(4) {
                    val middle = (inside + outside) * .5f
                    if (field(center + direction * middle) < 0f) inside = middle
                    else outside = middle
                }
                break
            }
            inside = distance
            distance += max(.5f, min(-depth, platterDistance + blend))
        }
        return center + direction * inside
    }

    val ends = arrayOfNulls<Offset>(turns)
    val depths = FloatArray(turns)
    fun kind(index: Int): Reach {
        val depth = depths[index % turns]
        return when {
            depth < -blend -> Reach.DEEP
            depth < blend -> Reach.FLOWING
            else -> Reach.CLEAR
        }
    }
    fun cast(index: Int) {
        val end = castReach(index * 2.0 * PI / turns)
        ends[index % turns] = end
        depths[index % turns] = toA(end)
    }
    fun skip(from: Int, to: Int) {
        for (i in from + 1 until to) {
            if (ends[i % turns] == null) depths[i % turns] = depths[from % turns]
        }
    }
    fun offChord(from: Offset, to: Offset, point: Offset): Float {
        val chord = to - from
        val length = gooDistanceLength(chord.x, chord.y)
        val offset = point - from
        return if (length <= .00001f) gooDistanceLength(offset.x, offset.y)
        else abs(offset.x * chord.y - offset.y * chord.x) / length
    }
    fun refine(from: Int, to: Int) {
        if (to - from < 2) return
        val same = kind(from) == kind(to)
        if (same && kind(from) != Reach.FLOWING) {
            skip(from, to)
            return
        }
        val middle = (from + to) / 2
        cast(middle)
        if (same && kind(middle) == kind(from) &&
            offChord(ends[from % turns]!!, ends[to % turns]!!,
                ends[middle % turns]!!) <= tolerance) {
            skip(from, to)
            return
        }
        refine(from, middle)
        refine(middle, to)
    }

    for (i in 0 until turns step 4) cast(i)
    for (i in 0 until turns step 4) refine(i, i + 4)
    val flows = BooleanArray(turns) { depths[it] < blend }
    if (!flows.any { it }) return null
    val first = flows.indexOfFirst { !it }.let { if (it < 0) 0 else it }
    val path = Path()
    val fan = ArrayList<Int>()
    fun simplify(points: List<Offset>): List<Offset> {
        if (points.size < 3) return points
        val kept = BooleanArray(points.size)
        kept[0] = true
        kept[points.lastIndex] = true
        fun split(from: Int, to: Int) {
            var farthest = 0f
            var at = -1
            for (i in from + 1 until to) {
                val distance = offChord(points[from], points[to], points[i])
                if (distance > farthest) {
                    farthest = distance
                    at = i
                }
            }
            if (at >= 0 && farthest > tolerance) {
                kept[at] = true
                split(from, at)
                split(at, to)
            }
        }
        split(0, points.lastIndex)
        return points.filterIndexed { index, _ -> kept[index] }
    }
    fun closeFan() {
        if (fan.size > 1) {
            val rim = ArrayList<Offset>()
            for (i in fan.indices) {
                val turn = fan[i]
                val end = ends[turn]
                if (end != null && (i == 0 || i == fan.lastIndex ||
                    kind(fan[i - 1]) != Reach.DEEP ||
                    kind(turn) != Reach.DEEP ||
                    kind(fan[i + 1]) != Reach.DEEP)) {
                    rim += end
                }
            }
            if (rim.isNotEmpty()) {
                path.moveTo(center.x, center.y)
                for (point in simplify(rim)) path.lineTo(point.x, point.y)
                path.close()
            }
        }
        fan.clear()
    }
    for (i in 0..turns) {
        val turn = (first + i) % turns
        if (flows[turn]) fan += turn else closeFan()
    }
    closeFan()
    return path
}
