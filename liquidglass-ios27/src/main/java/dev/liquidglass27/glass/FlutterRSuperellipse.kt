package dev.liquidglass27.glass

import android.graphics.Path
import android.os.Build
import androidx.compose.ui.geometry.Rect
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.hypot
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.roundToInt
import kotlin.math.sqrt
import kotlin.math.tan

/**
 * Flutter RSuperellipse's normalized uniform-radius contour.
 *
 * Uses the same octant fitting tables and rational quadratic + circular
 * Bézier construction as Flutter engine round_superellipse_param.cc.
 * The native Android Path has weighted conics on API 34+; on API 33 the
 * conics are approximated as a short sequence of tangent-matched cubics.
 * No arbitrary 12-point polygonal corner sampling is used.
 *
 * Flutter engine source is BSD-3-Clause (Copyright The Flutter Authors).
 */
private data class FlutterPathKey(val width: Int, val height: Int, val radius: Int)

private object FlutterContourCache {
    private val paths = object : LinkedHashMap<FlutterPathKey, Path>(50, .75f, true) {
        override fun removeEldestEntry(
            eldest: MutableMap.MutableEntry<FlutterPathKey, Path>,
        ): Boolean = size > 50
    }

    @Synchronized
    fun freshCopy(key: FlutterPathKey, create: () -> Path?): Path? {
        val result = paths[key] ?: create()?.also { paths[key] = it }
        return result?.let(::Path)
    }
}

internal fun flutterRSuperellipseOutline(bounds: Rect, radiusPx: Float): Path? {
    val width = bounds.width
    val height = bounds.height
    if (!width.isFinite() || !height.isFinite() ||
        !radiusPx.isFinite() || width <= 0f || height <= 0f
    ) return Path()
    // Like Flutter web's normalized RSuperellipse LRU, retain only 50
    // dimension/radius combinations. 0.01px quantization is below the
    // fidelity of the final device pixel, and shifting does not rebuild.
    if (width > 16000f || height > 16000f) return null
    val key = FlutterPathKey(
        (width * 100).roundToInt(), (height * 100).roundToInt(),
        (radiusPx.coerceIn(0f, min(width, height) * .5f) * 100).roundToInt(),
    )
    val normalized = FlutterContourCache.freshCopy(key) {
        val w = key.width / 100f
        val h = key.height / 100f
        buildNormalizedFlutterContour(Rect(-w / 2, -h / 2, w / 2, h / 2),
            key.radius / 100f)
    }
    normalized?.offset(bounds.center.x, bounds.center.y)
    return normalized
}

private fun buildNormalizedFlutterContour(bounds: Rect, radiusPx: Float): Path? {
    val width = bounds.width.toDouble()
    val height = bounds.height.toDouble()
    val radius = radiusPx.coerceIn(0f, min(bounds.width, bounds.height) * .5f)
        .toDouble()
    if (radius < .001) return Path().apply {
        addRect(bounds.left, bounds.top, bounds.right, bounds.bottom, Path.Direction.CW)
    }
    val halfW = width * .5
    val halfH = height * .5
    val c = halfW - halfH
    val top = flutterOctant(halfW, radius, Pt(0.0, -c)) ?: return null
    val right = flutterOctant(halfH, radius, Pt(c, 0.0)) ?: return null

    val out = Path()
    val cx = (bounds.left + bounds.right) * .5
    val cy = (bounds.top + bounds.bottom) * .5
    out.moveTo(cx.toFloat(), (cy + halfH).toFloat())

    fun quadrant(sx: Double, sy: Double, reverse: Boolean) {
        fun add(o: Octant, flip: Boolean, backwards: Boolean) {
            appendOctant(out, o, backwards) { p ->
                val q = if (flip) Pt(p.y, p.x) else p
                Pt(cx + sx * (q.x + o.offset.x),
                    cy + sy * (q.y + o.offset.y))
            }
        }
        if (!reverse) {
            add(top, flip = false, backwards = false)
            add(right, flip = true, backwards = true)
        } else {
            add(right, flip = true, backwards = false)
            add(top, flip = false, backwards = true)
        }
    }
    quadrant(+1.0, +1.0, reverse = false)
    quadrant(+1.0, -1.0, reverse = true)
    quadrant(-1.0, -1.0, reverse = false)
    quadrant(-1.0, +1.0, reverse = true)
    out.close()
    return out
}

private data class Pt(val x: Double, val y: Double) {
    operator fun plus(p: Pt) = Pt(x + p.x, y + p.y)
    operator fun minus(p: Pt) = Pt(x - p.x, y - p.y)
    operator fun times(s: Double) = Pt(x * s, y * s)
    operator fun div(s: Double) = Pt(x / s, y / s)
    val length: Double get() = hypot(x, y)
    fun rotation(r: Double) = Pt(x * cos(r) - y * sin(r),
        x * sin(r) + y * cos(r))
    fun finite() = x.isFinite() && y.isFinite()
}

private data class Octant(
    val a: Double,
    val n: Double,
    val joint: Pt,
    val circleCenter: Pt,
    val circleRadius: Double,
    val circleMaxAngle: Double,
    val offset: Pt,
)

/**
 * Same two fitted octants and circle centers as FlClash's
 * liquidGlassCornerCurves/_cornerCurve. The fourth corner is symmetric for
 * the uniform-radius glass controls in this Android port.
 *
 * Layout: along(n, Jx, Jy, R), across(n, Jx, Jy, R),
 * centers(alongX, alongY, acrossX, acrossY).
 */
internal fun flutterUniformCornerCurves(
    widthPx: Float, heightPx: Float, radiusPx: Float,
): FloatArray {
    val empty = FloatArray(12)
    if (!widthPx.isFinite() || !heightPx.isFinite() ||
        !radiusPx.isFinite() || min(widthPx, heightPx) <= 0f
    ) return empty
    val r = radiusPx.coerceIn(0f, min(widthPx, heightPx) * .5f)
    if (r <= .001f) return empty
    val along = flutterOctant(widthPx * .5, r.toDouble(), Pt(0.0, 0.0))
        ?: return empty
    val across = flutterOctant(heightPx * .5, r.toDouble(), Pt(0.0, 0.0))
        ?: return empty
    return floatArrayOf(
        along.n.toFloat(), along.joint.x.toFloat(),
        along.joint.y.toFloat(), along.circleRadius.toFloat(),
        across.n.toFloat(), across.joint.x.toFloat(),
        across.joint.y.toFloat(), across.circleRadius.toFloat(),
        along.circleCenter.x.toFloat(), along.circleCenter.y.toFloat(),
        across.circleCenter.x.toFloat(), across.circleCenter.y.toFloat(),
    )
}

private val octantRatios = doubleArrayOf(
    2.0, 2.1, 2.2, 2.3, 2.4, 2.5, 3.0, 3.5, 4.0, 4.5, 5.0,
)
private val octantN = doubleArrayOf(
    2.00000000, 2.18349805, 2.33888662, 2.48660575,
    2.62226596, 2.75148990, 3.36298265, 4.08649929,
    4.85481134, 5.62945551, 6.43023796,
)
private val octantJ = doubleArrayOf(
    1.13276676, 1.20311921, 1.28698796, 1.36351941,
    1.44717976, 1.53385819, 1.98288283, 2.23811846,
    2.47563463, 2.72948597, 2.98020421,
)

internal data class FlutterOctantFit(val exponent: Double, val jointFraction: Double)

/** Export the same fit as Flutter engine's ComputeNAndXj for pure JVM tests. */
internal fun flutterOctantFit(ratio: Double): FlutterOctantFit {
    if (ratio > 5.0) {
        val delta = ratio - 5.0
        val n = octantN.last() + 1.559599389 * delta
        val factor = octantJ.last() + .522807185 * delta
        return FlutterOctantFit(n, 1.0 - 1.0 / factor)
    }
    val bounded = ratio.coerceIn(2.0, 5.0)
    var index = 0
    while (index < octantRatios.lastIndex - 1 &&
        bounded > octantRatios[index + 1]) {
        index++
    }
    val amount = ((bounded - octantRatios[index]) /
        (octantRatios[index + 1] - octantRatios[index])).coerceIn(0.0, 1.0)
    val n = octantN[index] * (1 - amount) + octantN[index + 1] * amount
    val k = octantJ[index] * (1 - amount) + octantJ[index + 1] * amount
    return FlutterOctantFit(n, 1.0 - 1.0 / k)
}

private fun flutterOctant(a: Double, radius: Double, offset: Pt): Octant? {
    if (a <= 0.0 || radius <= 0.0) return null
    val fit = flutterOctantFit(2.0 * a / radius)
    val n = fit.exponent
    val x = fit.jointFraction * a
    val y = (1.0 - fit.jointFraction.pow(n)).coerceAtLeast(0.0).pow(1.0 / n) * a
    if (y < .00001) return null
    val tangent = (x / y).pow(n - 1)
    val d = (x - tangent * y) / (1.0 - tangent)
    val arcRadius = (a - d - (1.0 - sqrt(.5)) * radius) * sqrt(2.0)
    val joint = Pt(x, y)
    val meet = Pt(a - (1.0 - sqrt(.5)) * radius,
        a - (1.0 - sqrt(.5)) * radius)
    val ab = meet - joint
    val chord = ab.length
    val reachSquared = arcRadius * arcRadius - chord * chord / 4.0
    if (chord <= 1e-9 || reachSquared < -1e-6) return null
    val mid = (joint + meet) * .5
    val perpendicular = Pt(-ab.y, ab.x) / chord
    val center = mid - perpendicular * sqrt(max(0.0, reachSquared))
    val v1 = meet - center
    val v2 = joint - center
    val turn = atan2(v1.x * v2.y - v1.y * v2.x,
        v1.x * v2.x + v1.y * v2.y)
    val octant = Octant(a, n, joint, center, arcRadius, turn, offset)
    return octant.takeIf {
        it.joint.finite() && it.circleCenter.finite() &&
            it.circleMaxAngle.isFinite()
    }
}

private val conicA = doubleArrayOf(
    .7078, .7895, .8379, .8701, .8932, .9107, .9244,
    .9355, .9448, .9526, .9594, .9653, .9705,
)
private val conicB = doubleArrayOf(
    8.3194, 2.4523, 1.8528, 1.6891, 1.5806, 1.5043, 1.4470,
    1.4037, 1.3701, 1.3431, 1.3212, 1.3032, 1.2880,
)
private data class RationalConic(val from: Pt, val control: Pt,
                                  val to: Pt, val weight: Double)

private fun tangentIntersection(a: Pt, slopeA: Double,
                                b: Pt, slopeB: Double): Pt {
    if (abs(slopeA - slopeB) < 1e-5) return (a + b) * .5
    val x = (slopeA * a.x - slopeB * b.x + b.y - a.y) /
        (slopeA - slopeB)
    return Pt(x, slopeA * (x - a.x) + a.y)
}

private fun appendOctant(path: Path, octant: Octant,
                         reverse: Boolean, map: (Pt) -> Pt) {
    val a = octant.a
    val n = octant.n
    val j = octant.joint
    val t = (n - 2.0).coerceIn(0.0, 12.0)
    val i = min(floor(t).toInt(), 11)
    val fraction = t - i
    val f1 = conicA[i] * (1.0 - fraction) + conicA[i + 1] * fraction
    val f2 = conicB[i] * (1.0 - fraction) + conicB[i + 1] * fraction
    val hY = (sqrt(n) + j.y / a) / (sqrt(n) + 1.0)
    val h = Pt((1 - hY.pow(n)).coerceAtLeast(0.0).pow(1.0 / n) * a, hY * a)
    val start = Pt(0.0, a)
    val kH = -(h.x / h.y).pow(n - 1)
    val kJ = -(j.x / j.y).pow(n - 1)
    val first = RationalConic(start,
        tangentIntersection(start, 0.0, h, kH), h, f1 * sqrt(n))
    val second = RationalConic(h,
        tangentIntersection(h, kH, j, kJ), j, f2 * (j.x / a))

    val vector = j - octant.circleCenter
    val finalVector = vector.rotation(-octant.circleMaxAngle)
    val end = octant.circleCenter + finalVector
    val length = vector.length
    val factor = tan(octant.circleMaxAngle / 4.0) * (4.0 / 3.0)
    val c1 = j + Pt(vector.y, -vector.x) * factor
    val c2 = end + Pt(-finalVector.y, finalVector.x) * factor

    if (!reverse) {
        addConic(path, first, map, reversed = false)
        addConic(path, second, map, reversed = false)
        addCubic(path, map(c1), map(c2), map(end))
    } else {
        addCubic(path, map(c2), map(c1), map(j))
        addConic(path, second, map, reversed = true)
        addConic(path, first, map, reversed = true)
    }
}

private fun addCubic(path: Path, cp1: Pt, cp2: Pt, to: Pt) {
    path.cubicTo(cp1.x.toFloat(), cp1.y.toFloat(),
        cp2.x.toFloat(), cp2.y.toFloat(), to.x.toFloat(), to.y.toFloat())
}

private fun addConic(path: Path, c: RationalConic,
                     map: (Pt) -> Pt, reversed: Boolean) {
    val from = if (reversed) c.to else c.from
    val to = if (reversed) c.from else c.to
    val control = map(c.control)
    val end = map(to)
    if (Build.VERSION.SDK_INT >= 34) {
        path.conicTo(control.x.toFloat(), control.y.toFloat(),
            end.x.toFloat(), end.y.toFloat(), c.weight.toFloat())
        return
    }

    // Before API 34, adaptively fit the weighted conic instead of forcing
    // every curve into four cubics regardless of weight and on-screen size.
    // Endpoints and derivatives match; the 0.025px sample-error budget keeps
    // FlClash's superellipse corners continuous during animated resizing.
    fun Pt.conicPoint() = ConicPoint(x, y)
    fun ConicPoint.flutterPoint() = Pt(x, y)
    val rational = RationalQuadratic(
        from.conicPoint(), c.control.conicPoint(), to.conicPoint(), c.weight,
    )
    for (cubic in fitRationalConic(rational)) {
        addCubic(
            path,
            map(cubic.control1.flutterPoint()),
            map(cubic.control2.flutterPoint()),
            map(cubic.to.flutterPoint()),
        )
    }
}
