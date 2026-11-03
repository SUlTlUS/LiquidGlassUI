package dev.liquidglass27.glass

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.yield
import org.junit.Assert.*
import org.junit.Test

class GlassCompletionTest {
    @Test fun dialogStacksWhenEitherLabelDoesNotFitAndForNonPairs() {
        assertTrue(dialogActionsSideBySide(300, 8, listOf(140, 140)))
        assertFalse(dialogActionsSideBySide(300, 8, listOf(147, 40)))
        assertFalse(dialogActionsSideBySide(300, 8, listOf(40, 40, 40)))
        assertFalse(dialogActionsSideBySide(300, 8, emptyList()))
    }

    @Test fun labelsShrinkTogetherButStopAtNineSp() {
        assertEquals(1f, dockLabelScale(100f, 90f), 0f)
        assertEquals(.95f, dockLabelScale(95f, 100f), .0001f)
        assertEquals(.9f, dockLabelScale(10f, 100f), 0f)
    }

    @Test fun mouseReleaseRestoresHoverAndSpreadsLight() {
        val motion = FlClashDockMotion(0, 4)
        motion.hover(.1f)
        motion.press(Offset(20f, 10f), .1f)
        assertEquals(0f, motion.hoverShow.target, 0f)
        motion.release(true, 0f, 80f)
        assertEquals(1f, motion.hoverShow.target, 0f)
        assertEquals(1f, motion.glowSpread.target, 0f)
        repeat(240) { motion.advance(1f / 120) }
        assertEquals(0f, motion.glow.value, .001f)
        assertEquals(1f, motion.glowSpread.value, .001f)
    }

    @Test fun popupResultWaitsForReturnAnimation() = runBlocking {
        val state = GlassPopupState<String>()
        val result = async { state.show() }
        yield()
        assertTrue(state.expanded)
        state.dismiss("picked")
        state.dismiss("late click")
        assertTrue(state.mounted)
        assertFalse(result.isCompleted)
        assertEquals("picked", state.complete())
        assertEquals("picked", result.await())
        assertFalse(state.mounted)
    }

    @Test fun cancelAwaitDismissesWithoutLeavingAWaiter() = runBlocking {
        val state = GlassPopupState<String>()
        val first = async { state.show() }
        yield(); first.cancel(); first.join()
        assertFalse(state.expanded)
        state.complete()
        val next = async { state.show() }
        yield(); state.dismiss("second"); state.complete()
        assertEquals("second", next.await())
    }

    @Test fun pinRetainsTopRightUntilAnchorOrKeyboardChanges() {
        val pin = GlassPopupPin()
        var anchor = Rect(220f, 240f, 270f, 290f)
        var safe = Rect(0f, 30f, 400f, 800f)
        fun place(w: Float, h: Float) = menuPopupPosition(anchor, w, h, 400f, 800f, 1f,
            GlassMenuPlacement.OverAnchorStart, safeBounds = safe, pin = pin)
        val first = place(120f, 100f)
        val resized = place(180f, 200f)
        assertEquals(first.x + 120f, resized.x + 180f, .001f)
        assertEquals(first.y, resized.y, .001f)
        anchor = anchor.translate(0f, 200f)
        assertTrue(place(120f, 100f).y > first.y)
        safe = safe.copy(bottom = 400f)
        assertTrue(place(120f, 200f).y + 200f <= 384f)
    }

    @Test fun themeRolesFollowSeedAndPureBlackKeepsSurfaceHierarchy() {
        val blue = glassColorScheme(Color(0xFF0066D8), true)
        val green = glassColorScheme(Color(0xFF268648), true)
        assertNotEquals(blue.primary, green.primary)
        assertNotEquals(blue.secondary, green.secondary)
        val black = glassColorScheme(Color(0xFF0066D8), true, pureBlack = true)
        assertEquals(Color.Black.toArgb(), black.surface.toArgb())
        assertNotEquals(black.surface, black.surfaceContainer)
        assertNotEquals(black.surfaceContainer, black.surfaceContainerHigh)
    }
}
