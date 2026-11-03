package dev.liquidglass.sample

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import kotlin.math.roundToInt

private const val QweaTabSettleDurationMillis = 380
private val QweaTabSettleInterpolator = OvershootInterpolator(1.1f)
private val QweaTabSettleEasing = Easing { fraction ->
    QweaTabSettleInterpolator.getInterpolation(fraction)
}

/**
 * Page transition synchronized to QWEA0 LiquidGlassTabBar.animateDropletTo().
 *
 * QWEA0 does not expose a page-transition API; its actual tab settle animation uses
 * ValueAnimator(380 ms) + OvershootInterpolator(1.1). Page content follows that exact
 * settle curve while the existing iOS27 glass Tab Bar keeps its authored appearance.
 */
@Composable
internal fun QweaTabPageTransition(
    selectedTabIndex: Int,
    modifier: Modifier = Modifier,
    content: @Composable (Int) -> Unit,
) {
    AnimatedContent(
        targetState = selectedTabIndex,
        modifier = modifier.clipToBounds(),
        transitionSpec = {
            val direction = if (targetState >= initialState) 1 else -1
            val enter = slideInHorizontally(
                animationSpec = tween(
                    durationMillis = QweaTabSettleDurationMillis,
                    easing = QweaTabSettleEasing,
                ),
                initialOffsetX = { width -> (direction * width * 0.12f).roundToInt() },
            ) + scaleIn(
                animationSpec = tween(
                    durationMillis = QweaTabSettleDurationMillis,
                    easing = QweaTabSettleEasing,
                ),
                initialScale = 0.985f,
            ) + fadeIn(animationSpec = tween(durationMillis = 180))

            val exit = slideOutHorizontally(
                animationSpec = tween(
                    durationMillis = QweaTabSettleDurationMillis,
                    easing = QweaTabSettleEasing,
                ),
                targetOffsetX = { width -> (-direction * width * 0.06f).roundToInt() },
            ) + scaleOut(
                animationSpec = tween(
                    durationMillis = QweaTabSettleDurationMillis,
                    easing = QweaTabSettleEasing,
                ),
                targetScale = 0.992f,
            ) + fadeOut(animationSpec = tween(durationMillis = 160))

            enter togetherWith exit
        },
        label = "QweaTabPageTransition",
    ) { page ->
        Box(Modifier.fillMaxSize()) {
            content(page)
        }
    }
}

