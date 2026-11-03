package dev.liquidglass

import androidx.compose.ui.geometry.Rect
import java.io.File
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test

class GooGeometrySlotsTest {
    @Test
    fun stationaryPillsKeepTheirOwnContourWithoutRebuilding() {
        val cache = GooGeometrySlots<String, String>(6)
        var builds = 0
        fun geometry(index: Int, key: String) =
            cache.get(index, key) { index.toString() + "/" + key + "/" + (++builds) }

        val initial = (0..3).map { geometry(it, "rest") }
        assertEquals(4, builds)
        assertEquals(initial, (0..3).map { geometry(it, "rest") })
        assertEquals(4, builds)

        val pressed = (0..3).map {
            geometry(it, if (it == 2) "pressed" else "rest")
        }
        assertEquals(5, builds)
        assertEquals(initial[0], pressed[0])
        assertEquals(initial[1], pressed[1])
        assertFalse(initial[2] == pressed[2])
        assertEquals(initial[3], pressed[3])
        assertEquals(pressed, (0..3).map {
            geometry(it, if (it == 2) "pressed" else "rest")
        })
        assertEquals(5, builds)
    }

    @Test
    fun onlyTheTwoNecksBesideAMovingPillRecompute() {
        val bounds = (0..3).map { index ->
            Rect(index * 80f, 0f, index * 80f + 52f, 52f)
        }
        val changed = bounds.toMutableList().apply {
            this[2] = Rect(162f, 0f, 218f, 56f)
        }
        val cache = GooGeometrySlots<GooNeckGeometryKey, Int?>(3)
        var builds = 0
        fun keys(rects: List<Rect>) = (0..2).map { index ->
            val key = GooNeckGeometryKey(rects[index], rects[index + 1], 16f, 2f)
            cache.get(index, key) { ++builds }
        }
        val first = keys(bounds)
        assertEquals(listOf(1, 2, 3), first)
        assertEquals(first, keys(bounds))
        assertEquals(3, builds)
        val second = keys(changed)
        assertEquals(first[0], second[0])
        assertFalse(first[1] == second[1])
        assertFalse(first[2] == second[2])
        assertEquals(5, builds)
        assertEquals(second, keys(changed))
        assertEquals(5, builds)
    }

    @Test
    fun absentNeckIsCachedAndDensityAndBlendInvalidateIt() {
        val cache = GooGeometrySlots<GooNeckGeometryKey, String?>(5)
        val left = Rect(0f, 0f, 40f, 40f)
        val right = Rect(100f, 0f, 140f, 40f)
        var calls = 0
        fun absent(blend: Float, density: Float) =
            cache.get(0, GooNeckGeometryKey(left, right, blend, density)) {
                calls++
                null
            }
        assertEquals(null, absent(8f, 2f))
        assertEquals(null, absent(8f, 2f))
        assertEquals(1, calls)
        assertEquals(null, absent(16f, 2f))
        assertEquals(null, absent(16f, 3f))
        assertEquals(3, calls)
        cache.clear()
        assertEquals(null, absent(16f, 3f))
        assertEquals(4, calls)
    }

    @Test
    fun geometryMemoryIsBoundedToSixPillsAndFiveNecks() {
        val slots = GooGeometrySlots<Int, Int>(6)
        for (frame in 0..1000) {
            for (slot in 0..5) {
                assertEquals(frame, slots.get(slot, frame) { frame })
            }
        }
        assertThrows(IllegalArgumentException::class.java) {
            slots.get(6, 0) { 0 }
        }
        assertThrows(IllegalArgumentException::class.java) {
            slots.get(-1, 0) { 0 }
        }
    }

    @Test
    fun pairFallbackNeverAppendsAnIntersectingBridge() {
        val sourceFile = File("src/main/java/dev/liquidglass/GooPairOutline.kt")
            .takeIf { it.isFile } ?:
            File("liquidglass-ios27/src/main/java/dev/liquidglass/GooPairOutline.kt")
        val source = sourceFile.readText()
        assertTrue(source.contains("outline.op(contours, bridge, Path.Op.UNION)"))
        assertFalse(source.contains("outline.addPath(bridge)"))
        assertTrue(source.contains("outline.op(platePath, dropPath, Path.Op.UNION)"))
    }
}
