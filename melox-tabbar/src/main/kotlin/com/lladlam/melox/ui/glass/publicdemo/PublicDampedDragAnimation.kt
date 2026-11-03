package com.lladlam.melox.ui.glass.publicdemo

/* Uses AndroidLiquidGlass' shared drag state (Apache-2.0). */

import androidx.compose.animation.core.spring
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntSize
import com.zeaze.tianyinwallpaper.catalog.utils.DampedDragState
import com.zeaze.tianyinwallpaper.catalog.utils.inspectDragGestures
import kotlinx.coroutines.CoroutineScope

class PublicDampedDragAnimation(
    animationScope: CoroutineScope,
    initialValue: Float,
    valueRange: ClosedRange<Float>,
    visibilityThreshold: Float,
    initialScale: Float,
    pressedScale: Float,
    val onDragStarted: PublicDampedDragAnimation.(position: Offset) -> Unit = {},
    val onDragStopped: PublicDampedDragAnimation.() -> Unit = {},
    // The tap uses the indicator's local coordinates and its current docking value.
    val onTap: PublicDampedDragAnimation.(position: Offset) -> Unit = {},
    val onDrag: PublicDampedDragAnimation.(size: IntSize, dragAmount: Offset) -> Unit,
) : DampedDragState(animationScope, initialValue, valueRange, visibilityThreshold, initialScale, pressedScale) {
    private val bouncyValueAnimationSpec = spring(0.6f, 380f, visibilityThreshold)
    private var downPosition = Offset.Zero
    private var movedDuringGesture = false

    override suspend fun awaitReleaseFrame() { withFrameNanos { } }

    val modifier: Modifier = Modifier.pointerInput(Unit) {
        inspectDragGestures(
            cancelOnConsumed = false,
            onDragStart = { down ->
                downPosition = down.position
                movedDuringGesture = false
                onDragStarted(down.position)
                press()
            },
            onDragEnd = {
                if (movedDuringGesture) onDragStopped() else onTap(downPosition)
                release()
            },
            onDragCancel = { onDragStopped(); release() },
        ) { change, dragAmount ->
            if (dragAmount != Offset.Zero) movedDuringGesture = true
            onDrag(size, dragAmount)
            change.consume()
        }
    }

    // Search return animates position only. Press scaling would exceed its capture buffer.
    fun animateToValueBouncy(value: Float) =
        animateValue(value, bouncyValueAnimationSpec, pressFeedback = false)
}
