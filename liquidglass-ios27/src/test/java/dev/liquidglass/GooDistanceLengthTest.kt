package dev.liquidglass

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import java.util.Random
import kotlin.math.abs
import kotlin.math.hypot
import kotlin.math.max
import kotlin.math.min
import org.junit.Assert.assertEquals
import org.junit.Test

class GooDistanceLengthTest {
    @Test fun preparedRectRetainsTheOriginalSignedDistance() {
        val random = Random(2703)
        repeat(200) {
            val left = random.nextFloat() * 3000f - 1500f
            val top = random.nextFloat() * 3000f - 1500f
            val rect = Rect(left, top, left + random.nextFloat() * 1400f,
                top + random.nextFloat() * 2800f)
            val radius = random.nextFloat() * 800f
            val prepared = GooRoundedRectDistance(rect, radius)
            repeat(100) {
                val point = Offset(random.nextFloat() * 6000f - 3000f,
                    random.nextFloat() * 6000f - 3000f)
                val r = radius.coerceIn(0f, min(rect.width, rect.height) * .5f)
                val qx = abs(point.x - rect.center.x) - rect.width * .5f + r
                val qy = abs(point.y - rect.center.y) - rect.height * .5f + r
                val original = hypot(max(qx, 0f), max(qy, 0f)) +
                    min(max(qx, qy), 0f) - r
                assertEquals(original, prepared.at(point), 0f)
            }
        }
    }

    @Test fun flutterDistanceRetainsThePreviousPixelLength() {
        val points = listOf(0f, .00001f, .5f, 1f, 3.5f, 91f, 1272f, 2800f, 16384f)
        for (x in points) for (y in points) {
            assertEquals(hypot(x, y), gooDistanceLength(x, y), 0f)
            assertEquals(hypot(-x, y), gooDistanceLength(-x, y), 0f)
        }
        val random = Random(27)
        repeat(50_000) {
            val x = (random.nextFloat() - .5f) * 32768f
            val y = (random.nextFloat() - .5f) * 32768f
            assertEquals(hypot(x, y), gooDistanceLength(x, y), 0f)
        }
    }
}
