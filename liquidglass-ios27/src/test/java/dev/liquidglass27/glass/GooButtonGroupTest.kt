package dev.liquidglass27.glass

import org.junit.Assert.*
import org.junit.Test

class GooButtonGroupTest {
    private fun pill(x: Float, y: Float = 24f) = GlassPill(x, y, 22f, 24f)

    @Test fun compositionOrderDoesNotControlPhysicalConnections() {
        assertEquals(listOf(listOf(1, 2, 0)), gooButtonBatches(
            listOf(pill(140f), pill(20f), pill(80f)), listOf(1, 1, 1)))
    }
    @Test fun separateRowsNeverConnect() {
        assertEquals(listOf(listOf(0, 2), listOf(1, 3)), gooButtonBatches(
            listOf(pill(20f), pill(20f, 90f), pill(80f), pill(80f, 90f)), List(4) { 1 }))
    }
    @Test fun materialBoundaryCannotBridgeAcrossAnotherButton() {
        assertEquals(listOf(listOf(0), listOf(1), listOf(2, 3)), gooButtonBatches(
            List(4) { pill(20f + it * 60f) }, listOf(1, 2, 1, 1)))
    }
    @Test fun largerGroupsRetainEveryButtonWithoutExceedingShaderCapacity() {
        val groups = gooButtonBatches(List(14) { pill(it * 60f) }, List(14) { 1 })
        assertEquals(listOf(6, 6, 2), groups.map { it.size })
        assertEquals((0..13).toList(), groups.flatten())
    }
    @Test fun emptyAndSingleGroupsAreValid() {
        assertTrue(gooButtonBatches(emptyList(), emptyList<Int>()).isEmpty())
        assertEquals(listOf(listOf(0)), gooButtonBatches(listOf(pill(20f)), listOf(1)))
    }
    @Test fun staggeredRowsDoNotJoinByTransitiveOverlap() {
        val groups = gooButtonBatches(listOf(pill(20f, 0f), pill(80f, 20f), pill(140f, 40f)), List(3) { 1 })
        assertEquals(listOf(listOf(0, 1), listOf(2)), groups)
    }
}
