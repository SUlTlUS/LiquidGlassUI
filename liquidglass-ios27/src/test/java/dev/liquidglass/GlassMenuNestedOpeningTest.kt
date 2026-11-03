package dev.liquidglass

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class GlassMenuNestedOpeningTest {
    @Test
    fun allowsOnlyTheAlreadyInteractiveTopLevelToOpenAnother() {
        for (depth in 0..5) {
            assertTrue(
                canOpenAnotherMenuLevel(
                    expanded = true, firstOpen = true,
                    parentProgress = .87f, folding = false,
                    currentNestedDepth = depth,
                ),
            )
            assertTrue(
                canOpenAnotherMenuLevel(
                    expanded = true, firstOpen = true,
                    parentProgress = 1.15f, folding = false,
                    currentNestedDepth = depth,
                ),
            )
        }
    }

    @Test
    fun rejectsStaleActionsDuringFoldingOrOpeningOrDismissal() {
        val base = { expanded: Boolean, firstOpen: Boolean, progress: Float,
                     folding: Boolean, depth: Int ->
            canOpenAnotherMenuLevel(expanded, firstOpen, progress, folding, depth)
        }
        assertFalse(base(false, true, 1f, false, 0))
        assertFalse(base(true, false, 1f, false, 0))
        assertFalse(base(true, true, 1f, true, 0))
        assertFalse(base(true, true, .86f, false, 0))
        assertFalse(base(true, true, 0f, false, 1))
        assertFalse(base(true, true, Float.NaN, false, 1))
        assertFalse(base(true, true, Float.POSITIVE_INFINITY, false, 1))
        assertFalse(base(true, true, 1f, false, 6))
        assertFalse(base(true, true, 1f, false, -1))
    }
}
