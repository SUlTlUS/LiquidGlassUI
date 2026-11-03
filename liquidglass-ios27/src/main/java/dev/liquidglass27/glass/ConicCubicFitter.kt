package dev.liquidglass27.glass

import kotlin.math.hypot
import kotlin.math.max

/**
 * API 33 fallback for Flutter's weighted conic segments.
 *
 * A rational quadratic cannot, in general, be expressed exactly as an
 * ordinary cubic. Keep exact endpoints and first derivatives and subdivide
 * only until the cubic differs from the rational curve by at most a small
 * fraction of a device pixel at the quarter, midpoint, and three-quarter
 * samples. The Android 34+ Path.conicTo path remains unchanged.
 */
internal data class ConicPoint(val x: Double, val y: Double) {
    operator fun plus(other: ConicPoint) = ConicPoint(x + other.x, y + other.y)
    operator fun minus(other: ConicPoint) = ConicPoint(x - other.x, y - other.y)
    operator fun times(scale: Double) = ConicPoint(x * scale, y * scale)
    operator fun div(scale: Double) = ConicPoint(x / scale, y / scale)
    fun distanceTo(other: ConicPoint): Double = hypot(x - other.x, y - other.y)
    fun isFinite(): Boolean = x.isFinite() && y.isFinite()
}

internal data class ConicCubic(
    val t0: Double,
    val t1: Double,
    val from: ConicPoint,
    val control1: ConicPoint,
    val control2: ConicPoint,
    val to: ConicPoint,
) {
    fun position(t: Double): ConicPoint {
        val u = 1.0 - t
        return from * (u * u * u) +
            control1 * (3.0 * u * u * t) +
            control2 * (3.0 * u * t * t) +
            to * (t * t * t)
    }
}

internal class RationalQuadratic(
    val from: ConicPoint,
    val control: ConicPoint,
    val to: ConicPoint,
    val weight: Double,
) {
    init {
        require(weight.isFinite() && weight > 0.0)
        require(from.isFinite() && control.isFinite() && to.isFinite())
    }

    fun position(t: Double): ConicPoint {
        val u = 1.0 - t
        val a = u * u
        val b = 2.0 * weight * u * t
        val c = t * t
        return (from * a + control * b + to * c) / (a + b + c)
    }

    fun derivative(t: Double): ConicPoint {
        val u = 1.0 - t
        val a = u * u
        val b = 2.0 * weight * u * t
        val c = t * t
        val da = -2.0 * u
        val db = 2.0 * weight * (1.0 - 2.0 * t)
        val dc = 2.0 * t
        val total = a + b + c
        val totalDerivative = da + db + dc
        val numerator = from * a + control * b + to * c
        val numeratorDerivative = from * da + control * db + to * dc
        return (numeratorDerivative * total -
            numerator * totalDerivative) / (total * total)
    }
}

/** The result is ordered, finite, and continuous with tangent-matched joins. */
internal fun fitRationalConic(
    conic: RationalQuadratic,
    maxErrorPx: Double = 0.025,
    maxDepth: Int = 7,
): List<ConicCubic> {
    require(maxErrorPx > 0 && maxErrorPx.isFinite())
    require(maxDepth in 0..10)
    val segments = ArrayList<ConicCubic>()

    fun fit(t0: Double, t1: Double, depth: Int) {
        val span = t1 - t0
        val p0 = conic.position(t0)
        val p1 = conic.position(t1)
        val result = ConicCubic(
            t0 = t0,
            t1 = t1,
            from = p0,
            control1 = p0 + conic.derivative(t0) * (span / 3.0),
            control2 = p1 - conic.derivative(t1) * (span / 3.0),
            to = p1,
        )
        var deviation = 0.0
        for (fraction in doubleArrayOf(0.25, 0.5, 0.75)) {
            val t = t0 + fraction * span
            deviation = max(
                deviation, result.position(fraction).distanceTo(conic.position(t))
            )
        }
        if (deviation > maxErrorPx && depth < maxDepth) {
            val middle = (t0 + t1) * 0.5
            fit(t0, middle, depth + 1)
            fit(middle, t1, depth + 1)
        } else {
            segments += result
        }
    }

    fit(0.0, 1.0, 0)
    return segments
}
