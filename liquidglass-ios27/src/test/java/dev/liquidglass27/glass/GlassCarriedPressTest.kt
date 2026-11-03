package dev.liquidglass27.glass

import androidx.compose.ui.geometry.Offset
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class GlassCarriedPressTest {
    @Test
    fun aFreshDownDiscardsEventsFromThePreviousPopup() {
        val press = GlassCarriedPress()
        press.begin(Offset(1f, 1f))
        press.move(Offset(20f, 20f))
        press.release(Offset(20f, 20f))
        press.begin(Offset(80f, 90f))

        val onlyEvent = press.events.tryReceive().getOrNull()
        assertEquals(GlassSourcePointerPhase.Down, onlyEvent?.phase)
        assertEquals(Offset(80f, 90f), onlyEvent?.position)
        assertFalse(press.events.tryReceive().isSuccess)
    }

    @Test
    fun dragRetainsEventOrderThroughModalMount() {
        val press = GlassCarriedPress()
        press.begin(Offset(0f, 0f))
        press.move(Offset(32f, 48f))
        press.move(Offset(60f, 70f))
        press.release(Offset(60f, 70f))

        val down = press.events.tryReceive().getOrNull()?.let(press::resolve)
        val move = press.events.tryReceive().getOrNull()?.let(press::resolve)
        val up = press.events.tryReceive().getOrNull()?.let(press::resolve)
        assertEquals(GlassSourcePointerPhase.Down, down?.phase)
        assertEquals(GlassSourcePointerPhase.Move, move?.phase)
        assertEquals(Offset(60f, 70f), move?.position)
        assertEquals(GlassSourcePointerPhase.Up, up?.phase)
        assertEquals(Offset(60f, 70f), up?.position)
        assertFalse(press.events.tryReceive().isSuccess)
    }

    @Test
    fun tenThousandFastMovesKeepLatestTouchWithoutLaggingBehind() {
        val press = GlassCarriedPress()
        press.begin(Offset.Zero)
        repeat(10_000) { i -> press.move(Offset(i.toFloat(), i * 2f)) }
        press.release(Offset(10000f, 20000f))
        assertEquals(GlassSourcePointerPhase.Down,
            press.events.tryReceive().getOrNull()?.let(press::resolve)?.phase)
        val move = press.events.tryReceive().getOrNull()?.let(press::resolve)
        assertEquals(Offset(9999f, 19998f), move?.position)
        val up = press.events.tryReceive().getOrNull()?.let(press::resolve)
        assertEquals(Offset(10000f, 20000f), up?.position)
        assertFalse(press.events.tryReceive().isSuccess)
    }

    @Test
    fun moveCoalescingDoesNotMutateAnAlreadyConsumedPosition() {
        val press = GlassCarriedPress()
        press.begin(Offset.Zero)
        val down = press.events.tryReceive().getOrNull()?.let(press::resolve)
        assertEquals(GlassSourcePointerPhase.Down, down?.phase)
        press.move(Offset(10f, 10f))
        val first = press.events.tryReceive().getOrNull()?.let(press::resolve)
        press.move(Offset(20f, 20f))
        val second = press.events.tryReceive().getOrNull()?.let(press::resolve)
        assertEquals(Offset(10f, 10f), first?.position)
        assertEquals(Offset(20f, 20f), second?.position)
        press.cancel()
        assertEquals(GlassSourcePointerPhase.Cancel,
            press.events.tryReceive().getOrNull()?.let(press::resolve)?.phase)
    }

    @Test
    fun canceledSourcePressNeverBecomesASyntheticUp() {
        val press = GlassCarriedPress()
        press.begin(Offset(6f, 7f))
        press.cancel()
        assertEquals(
            GlassSourcePointerPhase.Down,
            press.events.tryReceive().getOrNull()?.phase,
        )
        val cancel = press.events.tryReceive().getOrNull()
        assertEquals(GlassSourcePointerPhase.Cancel, cancel?.phase)
        assertTrue(cancel?.position == Offset.Unspecified)
        assertFalse(press.events.tryReceive().isSuccess)
    }
}

class GlassAlertLifecycleTest {
    @Test
    fun closingKeepsModalMountedWhileSpringIsVisible() {
        assertTrue(glassAlertShouldRender(true, 0f))
        assertTrue(glassAlertShouldRender(false, 1f))
        assertTrue(glassAlertShouldRender(false, .1f))
        assertTrue(glassAlertShouldRender(false, .002f))
        assertFalse(glassAlertShouldRender(false, .001f))
        assertFalse(glassAlertShouldRender(false, 0f))
    }

    @Test
    fun reopeningBeforeDismissalHasNoDeadFrame() {
        assertTrue(glassAlertShouldRender(false, .4f))
        assertTrue(glassAlertShouldRender(true, .4f))
        assertTrue(glassAlertShouldRender(true, 0f))
    }
}
