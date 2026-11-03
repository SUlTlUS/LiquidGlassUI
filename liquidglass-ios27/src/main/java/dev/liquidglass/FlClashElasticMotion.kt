package dev.liquidglass

import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.geometry.Offset
import kotlinx.coroutines.flow.first

/** navigation_dock.dart::_ElasticPressState, including its first-crossing release. */
internal class FlClashElasticMotion {
    val lift = MenuRetargetSpring()
    val hover = MenuRetargetSpring()
    val x = MenuRetargetSpring()
    val y = MenuRetargetSpring()
    private var releasing = false
    val moving: Boolean get() = lift.moving || hover.moving || x.moving || y.moving
    val effectiveLift: Float get() = menuEffectiveLift(lift.value, hover.value)
    val pull: Offset get() = Offset(x.value, y.value)

    fun press() {
        releasing = false
        lift.springTo(1f, menuPressSpring())
    }

    fun drag(delta: Offset, limit: Float) {
        x.springTo(menuElasticRubberBand(delta.x, limit), menuTrackSpring())
        y.springTo(menuElasticRubberBand(delta.y, limit), menuTrackSpring())
    }

    fun release() {
        x.springTo(0f, menuPressSpring())
        y.springTo(0f, menuPressSpring())
        releasing = true
        settle()
    }

    fun hover(entered: Boolean) {
        hover.springTo(if (entered) .4f else 0f, menuHoverSpring())
    }

    private fun settle() {
        if (shouldSettleMenuPress(releasing, lift.value)) {
            releasing = false
            lift.springTo(0f, menuPressSpring())
        }
    }

    fun advance(seconds: Float) {
        lift.advance(seconds)
        settle()
        hover.advance(seconds)
        x.advance(seconds)
        y.advance(seconds)
    }

    suspend fun run() {
        while (true) {
            snapshotFlow { moving }.first { it }
            var previous = withFrameNanos { it }
            while (moving) {
                val now = withFrameNanos { it }
                advance((now - previous) / 1_000_000_000f)
                previous = now
            }
        }
    }
}
