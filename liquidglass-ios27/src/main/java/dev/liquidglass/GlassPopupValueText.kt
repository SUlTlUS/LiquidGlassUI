package dev.liquidglass

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.EnterExitState
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.togetherWith
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

private const val popupValueDurationMs = 300
private const val popupValueBlurDp = 4f

/**
 * Compose counterpart of FlClash popup.dart:PopupValueText.
 *
 * The value changes in 300 ms; the new value fades from 30% of the timeline
 * with ease-out, the old value fades after 60% with ease-in. Each blurs from
 * / toward 4dp, while the size grows toward the NEW text with ease-out cubic.
 * Old and new text overlap; the size is never clipped during the transition.
 */
@Composable
fun GlassPopupValueText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = TextStyle.Default,
    alignment: Alignment = Alignment.Center,
) {
    val easeOut = CubicBezierEasing(0f, 0f, .58f, 1f)
    val easeIn = CubicBezierEasing(.42f, 0f, 1f, 1f)
    val easeOutCubic = CubicBezierEasing(.215f, .61f, .355f, 1f)
    val transition = updateTransition(targetState = text, label = "popupValueChange")
    transition.AnimatedContent(
        modifier = modifier,
        contentAlignment = alignment,
        transitionSpec = {
            (EnterTransition.None togetherWith ExitTransition.None).using(
                SizeTransform(clip = false) { _, _ ->
                    tween(popupValueDurationMs, easing = easeOutCubic)
                },
            )
        },
    ) { value ->
        // AnimatedContentScope exposes the individual value's visibility
        // transition; entering and leaving strings retain their own blur.
        val visibility by this.transition.animateFloat(
            transitionSpec = {
                if (targetState == EnterExitState.Visible) {
                    tween(210, delayMillis = 90, easing = easeOut)
                } else {
                    tween(120, delayMillis = 180, easing = easeIn)
                }
            },
            label = "popupValueBlurFade",
        ) { stage ->
            if (stage == EnterExitState.Visible) 1f else 0f
        }
        Text(
            text = value,
            modifier = Modifier
                .graphicsLayer { alpha = visibility }
                .blur(
                    radius = (popupValueBlurDp * (1f - visibility)).dp,
                    edgeTreatment = BlurredEdgeTreatment.Unbounded,
                ),
            style = style,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}
