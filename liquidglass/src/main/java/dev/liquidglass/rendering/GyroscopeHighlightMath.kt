package dev.liquidglass.rendering

import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

/** LiquidTab BloomTiltTracker / setBloomLight math, without Android dependencies. */
internal class GyroscopeHighlightMath {
    var gravityX = 0f
        private set
    var gravityY = 0f
        private set
    private var initialized = false
    private var lastX = 0f
    private var lastY = -1f

    fun sample(x: Float, y: Float): Boolean {
        if (!x.isFinite() || !y.isFinite()) return false
        if (!initialized) {
            gravityX = x
            gravityY = y
            initialized = true
        } else {
            gravityX += (x - gravityX) * .15f
            gravityY += (y - gravityY) * .15f
        }
        val direction = direction(gravityX, gravityY)
        val deltaX = direction.first - lastX
        val deltaY = direction.second - lastY
        if (deltaX * deltaX + deltaY * deltaY < .0007f) return false
        lastX = direction.first
        lastY = direction.second
        return true
    }

    fun reset() {
        gravityX = 0f
        gravityY = 0f
        initialized = false
        lastX = 0f
        lastY = -1f
    }

    fun light(rotation: Int, angleDegrees: Float): FloatArray {
        val (x, y) = when (rotation) {
            1 -> -gravityY to gravityX
            2 -> -gravityX to -gravityY
            3 -> gravityY to -gravityX
            else -> gravityX to gravityY
        }
        val (dx, dy) = direction(x, y)
        val radians = Math.toRadians(angleDegrees.toDouble())
        val c = cos(radians).toFloat()
        val s = sin(radians).toFloat()
        val length = sqrt(1f + .05f * .05f)
        return floatArrayOf((c * dx - s * dy) / length, (s * dx + c * dy) / length, -.05f / length)
    }

    private fun direction(x: Float, y: Float): Pair<Float, Float> {
        val magnitude = x * x + y * y
        return if (magnitude > .01f) {
            val inverse = 1f / sqrt(magnitude)
            x * inverse to y * inverse
        } else 0f to -1f
    }
}
