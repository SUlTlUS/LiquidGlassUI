package dev.liquidglass

/** One pane and its six Goo pills. Position, tint and glow do not affect a fit. */
internal class GlassCornerCurveCache {
    private class Entry(
        val width: Float,
        val height: Float,
        val radius: Float,
        val curve: FloatArray,
    )

    private val entries = arrayOfNulls<Entry>(7)

    fun curve(slot: Int, width: Float, height: Float, radius: Float): FloatArray {
        val previous = entries[slot]
        if (previous != null && previous.width == width &&
            previous.height == height && previous.radius == radius) return previous.curve
        val curve = flutterUniformCornerCurves(width, height, radius)
        entries[slot] = Entry(width, height, radius, curve)
        return curve
    }
}
