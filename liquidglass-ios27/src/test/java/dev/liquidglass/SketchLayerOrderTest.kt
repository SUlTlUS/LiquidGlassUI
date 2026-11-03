package dev.liquidglass

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class SketchLayerOrderTest {
    private val nested = listOf("third", "fourth", "fifth", "sixth", "seventh", "eighth")

    @Test
    fun secondLevelReadsOnlyBackgroundAndRoot() {
        assertEquals(
            listOf("background", "root"),
            precedingSketchLayers("background", "root", null, nested, 0),
        )
    }

    @Test
    fun thirdLevelReadsBackgroundRootAndFirstButNotItself() {
        assertEquals(
            listOf("background", "root", "first"),
            precedingSketchLayers("background", "root", "first", nested, 0),
        )
    }

    @Test
    fun deeperLevelsCannotReadTheCurrentOrLaterSiblings() {
        for (level in nested.indices) {
            val inputs = precedingSketchLayers(
                "background", "root", "first", nested, level,
            )
            assertEquals(3 + level, inputs.size)
            assertEquals(nested.take(level), inputs.drop(3))
        }
    }

    @Test
    fun invalidDepthIsRejected() {
        assertThrows(IllegalArgumentException::class.java) {
            precedingSketchLayers("background", "root", "first", nested, 7)
        }
    }
}
