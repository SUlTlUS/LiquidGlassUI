package dev.liquidglass27.glass

import org.junit.Assert.assertEquals
import org.junit.Test

/** Per-level GPU atlas builds must aggregate stats for the entire frame. */
class GlassGpuAtlasStatsTest {
    @Test
    fun perLevelStatisticsAccumulateWithoutLosingEarlierLevels() {
        val first = GlassGpuAtlasStats(
            reusedBands = 4, createdBands = 2,
            reusedLevelAtlases = 1, createdLevelAtlases = 1,
        )
        val second = GlassGpuAtlasStats(
            reusedBands = 3, createdBands = 1,
            reusedLevelAtlases = 1, createdLevelAtlases = 0,
        )
        assertEquals(
            GlassGpuAtlasStats(7, 3, 2, 1),
            first + second,
        )
        assertEquals(first, first + GlassGpuAtlasStats())
    }
}
