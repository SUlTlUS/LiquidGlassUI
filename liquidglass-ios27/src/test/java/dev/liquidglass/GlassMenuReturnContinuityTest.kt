package dev.liquidglass

import androidx.compose.ui.geometry.Rect
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class GlassMenuReturnContinuityTest {
    private val anchor = Rect(680f, 210f, 780f, 310f)
    private val card = Rect(290f, 180f, 780f, 1040f)
    private val density = 2f
    private val route = FlClashPopupRouteMotion

    @Test
    fun quickCloseStartsAtTheExactVisibleOpeningFrame() {
        for (elapsed in listOf(0f, .05f, .15f, .30f, .45f)) {
            val opening = menuOpeningFrame(elapsed, anchor, card, density)
            val returned = menuReturningFrame(0f, opening, anchor, density)
            fun near(expected: Float, actual: Float, field: String) {
                assertEquals("$field at openTime=$elapsed", expected, actual, .001f)
            }
            near(opening.platter.left, returned.platter.left, "platter left")
            near(opening.platter.top, returned.platter.top, "platter top")
            near(opening.platter.width, returned.platter.width, "platter width")
            near(opening.platter.height, returned.platter.height, "platter height")
            near(opening.droplet.left, returned.droplet.left, "droplet left")
            near(opening.droplet.top, returned.droplet.top, "droplet top")
            near(opening.droplet.width, returned.droplet.width, "droplet width")
            near(opening.droplet.height, returned.droplet.height, "droplet height")
            near(opening.radius, returned.radius, "radius")
            near(opening.blend, returned.blend, "goo neck blend")
            near(opening.settled, returned.settled, "material transition")
            near(opening.glyphAlpha, returned.glyphAlpha, "source glyph")
            near(opening.contentAlpha, returned.contentAlpha, "menu content")
        }
    }

    @Test
    fun aReverseUsesTheWholeFiveHundredMillisecondsEvenWhenOpeningWasInterrupted() {
        // Exactly FlClash didPop(): reverseDuration = 500 / max(value,.001),
        // then AnimationController.reverse() uses that duration * value.
        for (openMillis in listOf(50f, 150f, 300f, 449f, 450f)) {
            val atPop = openMillis / 450f
            assertEquals(500f / atPop,
                route.reverseDurationOverrideMillis(atPop, true), .02f)
            assertEquals(500, route.reverseTravelMillis(atPop, true))
            assertEquals(openMillis / 1000f,
                route.openingTimeSeconds(atPop), .000001f)
        }
    }

    @Test
    fun reverseAndForwardShareOnePositionForFiftyOneFiftyThreeHundredMillisecondCuts() {
        // Close while opening at the listed timestamps; interrupt each close
        // after 50/150/300ms, then run the SAME controller forward from p.
        for (openedMs in listOf(50, 150, 300, 450)) {
            val atPop = openedMs / 450f
            val captured = menuOpeningFrame(
                route.openingTimeSeconds(atPop), anchor, card, density,
            )
            for (closedMs in listOf(0, 50, 150, 300)) {
                val fraction = closedMs / 500f
                val stillOpenValue = atPop * (1f - fraction)
                assertEquals(fraction,
                    route.dismissalFraction(stillOpenValue, atPop), .000001f)
                val seconds = route.dismissalTimeSeconds(
                    stillOpenValue, atPop, true,
                )
                assertEquals(closedMs / 1000f, seconds, .000001f)
                val returned = menuReturningFrame(seconds, captured, anchor, density)
                assertTrue(returned.platter.width > 0f)
                assertTrue(returned.platter.height > 0f)

                // Status switches back to forward. Neither routePosition nor
                // its opening time is reset to zero, and there is NO blend.
                val resumed = menuOpeningFrame(
                    route.openingTimeSeconds(stillOpenValue), anchor, card, density,
                )
                assertEquals(
                    menuOpeningFrame(stillOpenValue * .45f, anchor, card, density),
                    resumed,
                )
                assertTrue(
                    kotlin.math.abs(
                        ((1f - stillOpenValue) * 450f).toInt() -
                            route.openingRemainingMillis(stillOpenValue)
                    ) <= 1,
                )
            }
        }
    }

    @Test
    fun closeReopenCloseAgainRebasesReverseFromTheNewCurrentPosition() {
        val firstOpen = .8f
        val afterFirstDismiss = firstOpen * (1f - .15f / .5f)
        assertEquals(.56f, afterFirstDismiss, .000001f)
        val afterReopen = afterFirstDismiss + .12f / .45f
        assertEquals(.82666665f, afterReopen, .00001f)
        val secondCloseElapsed = .1f
        val midSecondClose = afterReopen * (1f - secondCloseElapsed / .5f)
        assertEquals(.2f, route.dismissalFraction(
            midSecondClose, afterReopen,
        ), .000001f)
        assertEquals(500, route.reverseTravelMillis(afterReopen, true))
        assertTrue(midSecondClose > 0f)
        assertTrue(midSecondClose < afterReopen)
    }

    @Test
    fun ordinaryPopupReversesForTwoHundredMillisWithoutSourceDroplet() {
        for (atPop in listOf(.15f, .5f, 1f)) {
            assertEquals(200, route.reverseTravelMillis(atPop, false))
            assertEquals(0f, route.dismissalTimeSeconds(atPop, atPop, false), 0f)
            assertEquals(.1f,
                route.dismissalTimeSeconds(atPop * .5f, atPop, false), .000001f)
            assertEquals(.75f, menuNoSourceDismissFraction(.1f), .000001f)
            assertEquals(.2f,
                route.dismissalTimeSeconds(0f, atPop, false), .000001f)
        }
    }

    @Test
    fun aCompletedRouteHasNoResidualAnimationToReopen() {
        assertEquals(0, route.reverseTravelMillis(0f, true))
        assertEquals(1f, route.dismissalFraction(0f, 0f), 0f)
        assertEquals(0, route.openingRemainingMillis(1f))
        assertEquals(450, route.openingRemainingMillis(0f))
        assertEquals(225, route.openingRemainingMillis(.5f))
        assertEquals(0.45f, route.openingTimeSeconds(1f), 0f)
    }

    @Test
    fun closeProgressDoesNotLeaveNonFiniteGeometry() {
        val opening = menuOpeningFrame(.13f, anchor, card, density)
        for (step in 0..50) {
            val current = menuReturningFrame(step / 100f, opening, anchor, density)
            assertTrue(current.platter.width > 0f)
            assertTrue(current.platter.height > 0f)
            assertTrue(current.droplet.width > 0f)
            assertTrue(current.droplet.height > 0f)
            assertTrue(current.radius.isFinite())
            assertTrue(current.blend.isFinite())
            assertTrue(current.settled in 0f..1f)
            assertTrue(current.glyphAlpha in 0f..1f)
            assertTrue(current.contentAlpha in 0f..1f)
        }
    }
}
