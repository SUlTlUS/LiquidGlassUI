package dev.liquidglass

import androidx.compose.animation.core.spring
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.exp
import kotlin.math.roundToInt
import kotlin.math.sign
import kotlinx.coroutines.flow.first

internal fun dockSpring(durationMs: Int, bounce: Float = 0f) = spring<Float>(
    stiffness = (4.0 * PI * PI / (durationMs / 1000.0).let { it * it }).toFloat(),
    dampingRatio = 1f - bounce,
    visibilityThreshold = .001f,
)

internal data class DockLayout(val pitch: Float, val lens: Float, val inset: Float)

internal fun dockLabelScale(room: Float, widest: Float): Float =
    if (widest <= room) 1f else maxOf(room / widest, .9f)

internal fun dockLayout(span: Float, count: Int, density: Float): DockLayout {
    val padding = 4f * density
    if (count == 1) return DockLayout(span - padding * 2, span, padding)
    val pitch = (span - padding * 2) / count
    if (pitch + padding * 2 >= 84f * density) {
        return DockLayout(pitch, pitch + padding * 2, padding)
    }
    val lens = minOf(84f * density, span)
    val closed = (span - lens) / (count - 1)
    return DockLayout(closed, lens, (lens - closed) / 2)
}

internal fun dockLensRect(
    position: Float, jelly: Float, layout: DockLayout, height: Float,
    lift: Float, density: Float,
): Rect {
    val growth = 16f * density * lift
    val width = (layout.lens + growth) * (1f + jelly)
    val h = (height + growth) * (1f - jelly * 1.4f)
    val center = layout.inset + (position + .5f) * layout.pitch
    return Rect(center - width / 2, (height - h) / 2,
        center + width / 2, (height + h) / 2)
}

internal fun dockPositionAt(x: Float, layout: DockLayout, count: Int, density: Float): Float {
    val position = (x - 4f * density - layout.inset) / layout.pitch - .5f
    return when {
        position < 0f -> menuElasticRubberBand(position, .35f)
        position > count - 1f -> count - 1f +
            menuElasticRubberBand(position - (count - 1f), .35f)
        else -> position
    }
}

internal fun dockFlingIndex(target: Float, pressed: Int, velocity: Float,
    pitch: Float, count: Int): Int =
    (target + velocity / pitch * .1f).roundToInt()
        .coerceIn(pressed - 1, pressed + 1).coerceIn(0, count - 1)

/** Continuous retargeting reuses the menu's spring driver; rendering stays in Compose. */
internal class FlClashDockMotion(initial: Int, val count: Int) {
    val lens = MenuRetargetSpring(initial.toFloat())
    val lift = MenuRetargetSpring()
    val swell = MenuRetargetSpring()
    val stretch = MenuRetargetSpring()
    val jelly = MenuRetargetSpring(rest = .003f)
    val hover = MenuRetargetSpring()
    val hoverShow = MenuRetargetSpring()
    val glow = MenuRetargetSpring()
    val glowSpread = MenuRetargetSpring()
    val parallax = List(count) { MenuRetargetSpring() }
    private val springs = listOf(lens, lift, swell, stretch, jelly, hover, hoverShow, glow, glowSpread) + parallax
    var pressedIndex: Int? by mutableStateOf(null)
        private set
    var glowAt by mutableStateOf(Offset.Zero)
        private set
    private var selected = initial
    private var pressX = 0f
    private var dragging = false
    private var dropping = false
    private var cursor: Float? = null
    private val travel = dockSpring(400, .15f)
    private val liftSpec = dockSpring(250)
    private val hoverSpec = dockSpring(260, .18f)
    private val follow = spring<Float>(.9f, 1024f, .001f)
    private val jellySpec = spring<Float>(.4f, 100f, .003f)

    fun select(index: Int) {
        if (selected == index) return
        selected = index
        if (pressedIndex == null) lens.springTo(restPosition(), travel)
    }

    private fun indexAt(position: Float) = position.roundToInt().coerceIn(0, count - 1)
    private fun leanAt(position: Float): Float {
        val index = indexAt(position)
        return index + (position - index) * .2f
    }
    private fun restPosition(): Float = cursor?.takeIf { indexAt(it) == selected }
        ?.let(::leanAt) ?: selected.toFloat()

    fun hover(position: Float?) {
        cursor = position
        if (pressedIndex != null) return
        if (position == null) hoverShow.springTo(0f, dockSpring(200))
        else {
            val target = leanAt(position)
            if (hoverShow.value == 0f && hoverShow.target == 0f) hover.jumpTo(target)
            else hover.springTo(target, hoverSpec)
            hoverShow.springTo(1f, dockSpring(200))
        }
        for ((i, channel) in parallax.withIndex()) {
            val offset = position?.minus(i) ?: 0f
            channel.springTo(if (abs(offset) <= .5f) offset * .1f else 0f, hoverSpec)
        }
        if (indexAt(lens.target) == selected) lens.springTo(restPosition(), hoverSpec)
        swell.springTo(if (position == null) 0f else .4f, menuHoverSpring())
    }

    fun press(point: Offset, position: Float) {
        pressX = point.x
        dragging = false
        dropping = false
        pressedIndex = indexAt(position)
        hoverShow.springTo(0f, dockSpring(200))
        for (channel in parallax) channel.springTo(0f, hoverSpec)
        swell.springTo(1f, menuPressSpring())
        lift.springTo(1f, liftSpec)
        lens.springTo(pressedIndex!!.toFloat(), travel)
        glowAt = point
        glowSpread.jumpTo(0f)
        glow.springTo(1f, dockSpring(100))
    }

    fun drag(point: Offset, position: Float, width: Float, height: Float, slop: Float): Boolean {
        val old = pressedIndex ?: return false
        glowAt = point
        if (!dragging && abs(point.x - pressX) < slop) return false
        dragging = true
        stretch.springTo(menuElasticRubberBand(
            point.x - point.x.coerceIn(0f, width), minOf(width, height) * 7f / 32f,
        ), menuTrackSpring())
        lens.springTo(position, follow)
        pressedIndex = indexAt(position)
        return pressedIndex != old
    }

    fun release(commit: Boolean, velocity: Float, pitch: Float, cursorPosition: Float? = null): Int? {
        val pressed = pressedIndex ?: return null
        if (cursor != null && cursorPosition != null) cursor = cursorPosition
        val next = if (commit && dragging)
            dockFlingIndex(lens.target, pressed, velocity, pitch, count) else pressed
        pressedIndex = null
        dragging = false
        dropping = true
        lens.springTo((if (commit) next else selected).toFloat(), travel)
        glow.springTo(0f, dockSpring(500))
        glowSpread.springTo(1f, dockSpring(500))
        stretch.springTo(0f, menuPressSpring())
        swell.springTo(if (cursor == null) 0f else .4f, menuPressSpring())
        if (cursor != null) hover(cursor)
        dropLift()
        return next.takeIf { commit && it != selected }
    }

    private fun dropLift() {
        if (dropping && pressedIndex == null && lift.value >= .975f &&
            abs(lens.target - lens.value) <= .04f) {
            dropping = false
            lift.springTo(0f, liftSpec)
        }
    }

    fun advance(seconds: Float) {
        lens.advance(seconds)
        fun saturate(value: Float) = (1.0 - 2.0 / (exp(2.0 * value) + 1.0)).toFloat()
        val braking = maxOf(0f, -lens.acceleration * sign(lens.velocity))
        val target = .2f * saturate(abs(lens.velocity) / 3f) - .15f * saturate(braking / 25f)
        if (target != jelly.target) jelly.springTo(target, jellySpec)
        for (channel in springs) if (channel !== lens) channel.advance(seconds)
        dropLift()
    }

    suspend fun run() {
        while (true) {
            snapshotFlow { springs.any { it.moving } }.first { it }
            var last = withFrameNanos { it }
            while (springs.any { it.moving }) {
                val now = withFrameNanos { it }
                advance((now - last) / 1_000_000_000f)
                last = now
            }
        }
    }
}
