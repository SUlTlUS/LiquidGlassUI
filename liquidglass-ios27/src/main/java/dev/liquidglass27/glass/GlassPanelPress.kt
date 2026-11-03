package dev.liquidglass27.glass

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** Observes children without claiming their clicks, scrolling, or text selection. */
@Composable
fun Modifier.glassPanelPress(maxGrowth: Dp = 4.dp, maxPull: Dp? = 3.dp,
    panel: Rect? = null, enabled: Boolean = true): Modifier {
    val motion = remember { FlClashElasticMotion() }
    val density = LocalDensity.current
    val growth = with(density) { maxGrowth.toPx() }
    val pull = with(density) { maxPull?.toPx() }
    val panelNow by rememberUpdatedState(panel)
    LaunchedEffect(motion) { motion.run() }
    return expandedTouchInput(0.dp, motion, pull, enabled, shareWithSiblings = true) {
        if (!enabled) return@expandedTouchInput
        awaitEachGesture {
            val down = awaitFirstDown(false, PointerEventPass.Initial)
            if (down.type == PointerType.Mouse && !currentEvent.buttons.isPrimaryPressed) return@awaitEachGesture
            val rect = panelNow ?: Rect(0f, 0f, size.width.toFloat(), size.height.toFloat())
            if (!rect.contains(down.position)) return@awaitEachGesture
            motion.press()
            try {
                while (true) {
                    val event = awaitPointerEvent(PointerEventPass.Initial)
                    val change = event.changes.firstOrNull { it.id == down.id } ?: break
                    if (!change.pressed) break
                    val p = change.position
                    motion.drag(p - Offset(p.x.coerceIn(rect.left, rect.right),
                        p.y.coerceIn(rect.top, rect.bottom)), pull ?: minOf(rect.width, rect.height) * 7f / 32f)
                }
            } finally { motion.release() }
        }
    }.graphicsLayer {
        val rect = panel ?: Rect(Offset.Zero, size)
        val transform = menuElasticTransform(rect, motion.effectiveLift, motion.pull, growth)
        transformOrigin = TransformOrigin(rect.center.x / size.width, rect.center.y / size.height)
        scaleX = transform.scaleX; scaleY = transform.scaleY
        translationX = transform.pull.x; translationY = transform.pull.y
    }
}
