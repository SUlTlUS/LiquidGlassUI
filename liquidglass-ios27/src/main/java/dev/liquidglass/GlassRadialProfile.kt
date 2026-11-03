package dev.liquidglass

import android.graphics.Path
import android.graphics.PathMeasure
import androidx.compose.ui.geometry.Offset
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin
import kotlin.math.atan2

/**
 * FlClash liquidGlassProfile(): 64 radii of the exact native clipping Path.
 * The source walks each path edge and visits ONLY angular rays that edge
 * actually crosses; the previous Android version instead compared every one
 * of 64 rays with EVERY segment, producing O(64 * segments) work each frame.
 *
 * The fast angular-sweep scan is O(segments + relevant ray crossings).
 * A ray with more than one distinct intersection means non-star-shaped Goo:
 * fall back to Goo's existing per-shape normals instead of invalid profile.
 */
internal data class GlassRadialProfile(
    val center: Offset,
    val radii: FloatArray,
)

internal fun liquidGlassRadialProfile(
    outline: Path,
    center: Offset,
): GlassRadialProfile? {
    if (!center.x.isFinite() || !center.y.isFinite()) return null

    val radii = FloatArray(64)
    val seen = BooleanArray(64)
    val step = 2.0 * PI / 64.0
    var invalid = false

    fun cross(ax: Float, ay: Float, bx: Float, by: Float) {
        if (invalid) return
        val dx = bx - ax
        val dy = by - ay
        if (abs(dx) + abs(dy) < .0001f) return
        val start = atan2(ay.toDouble(), ax.toDouble())
        var span = atan2(by.toDouble(), bx.toDouble()) - start
        if (span > PI) span -= 2.0 * PI
        if (span <= -PI) span += 2.0 * PI
        val from = ceil(min(start, start + span) / step).toInt()
        val to = floor(max(start, start + span) / step).toInt()
        val numerator = ax * dy - ay * dx
        for (turn in from..to) {
            val ray = turn * step
            val rx = cos(ray).toFloat()
            val ry = sin(ray).toFloat()
            val denominator = rx * dy - ry * dx
            if (abs(denominator) < 1e-7f) continue
            val distance = numerator / denominator
            if (!distance.isFinite() || distance <= .001f) continue
            val index = ((turn % 64) + 64) % 64
            if (seen[index] && abs(radii[index] - distance) > .08f) {
                invalid = true
                return
            }
            seen[index] = true
            radii[index] = max(radii[index], distance)
        }
    }

    val pathMeasure = PathMeasure(outline, false)
    val fromPos = FloatArray(2)
    val toPos = FloatArray(2)
    var sampled = false
    do {
        val length = pathMeasure.length
        if (length <= .001f || !length.isFinite()) continue
        // FlClash uses max(256, length.ceil()). Cap at 512 for Android
        // animated Goo; a 64-angle profile cannot usefully resolve the
        // additional segments of a many-thousand-pixel path per frame.
        val sampleCount = max(256, min(512, ceil(length.toDouble()).toInt()))
        if (!pathMeasure.getPosTan(0f, fromPos, null)) continue
        var ax = fromPos[0] - center.x
        var ay = fromPos[1] - center.y
        for (index in 1..sampleCount) {
            if (!pathMeasure.getPosTan(
                    length * index.toFloat() / sampleCount, toPos, null
                )
            ) continue
            val bx = toPos[0] - center.x
            val by = toPos[1] - center.y
            cross(ax, ay, bx, by)
            ax = bx
            ay = by
            if (invalid) return null
        }
        // The actual clipped Path can be implicitly open; close the sampled
        // contour so the ray profile matches the filled silhouette.
        if (!pathMeasure.isClosed &&
            pathMeasure.getPosTan(0f, toPos, null)
        ) cross(ax, ay, toPos[0] - center.x, toPos[1] - center.y)
        sampled = true
        if (invalid) return null
    } while (pathMeasure.nextContour())

    if (!sampled || seen.any { !it }) return null
    return GlassRadialProfile(center, radii)
}
