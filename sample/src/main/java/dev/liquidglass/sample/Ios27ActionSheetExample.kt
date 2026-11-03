package dev.liquidglass.sample

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.zeaze.tianyinwallpaper.backdrop.Backdrop
import dev.liquidglass.ios27.Ios27PageColors
import dev.liquidglass.ios27.Ios27Tokens
import dev.liquidglass.ios27.LiquidDialogLayer
import dev.liquidglass.ios27.LiquidDialogButton
import dev.liquidglass.ios27.ios27AndroidPane

/** Examples/Action Sheet: 300x296 glass panel at (98, 62) in the 402pt scene. */
@Composable
internal fun Ios27ActionSheetExample(
    backdrop: Backdrop?,
    colors: Ios27PageColors,
    dark: Boolean,
    visible: Boolean,
    onAction: (String) -> Unit,
    onDismiss: () -> Unit,
    onHidden: () -> Unit,
) {
    val surface = if (dark) Color(0xCC2C2C2E) else Color.White.copy(alpha = 0.70f)
    val actionSurface = Color(0xFF787880).copy(alpha = if (dark) 0.28f else 0.16f)
    val progress = remember { Animatable(0f) }

    LaunchedEffect(visible) {
        progress.animateTo(
            targetValue = if (visible) 1f else 0f,
            animationSpec = tween(durationMillis = 340, easing = FastOutSlowInEasing),
        )
        if (!visible) onHidden()
    }

    val p = progress.value.coerceIn(0f, 1f)
    fun lerpDp(start: Dp, end: Dp): Dp = start + (end - start) * p

    // The source trailing toolbar button is a 44pt circle at x=342 in a 402pt canvas.
    // Begin exactly there, then grow left/down into the authored 300x296 action sheet.
    val panelWidth = lerpDp(44.dp, 300.dp)
    val panelHeight = lerpDp(44.dp, 296.dp)
    val panelCorner = lerpDp(22.dp, 34.dp)
    val panelOffsetX = lerpDp((-16).dp, (-4).dp)
    val contentAlpha = ((p - 0.34f) / 0.66f).coerceIn(0f, 1f)
    val scrimAlpha = 0.23f * p

    LiquidDialogLayer(
        backdrop = backdrop,
        onDismissRequest = onDismiss,
        panelModifier = Modifier.size(panelWidth, panelHeight).ios27AndroidPane("操作面板", onDismiss),
        shape = RoundedCornerShape(panelCorner),
        alignment = Alignment.TopEnd,
        offsetX = panelOffsetX,
        // ExampleNavBar starts at the safe-area top; the animated shell therefore
        // originates exactly on top of its trailing 44pt glass button.
        offsetY = WindowInsets.safeDrawing.asPaddingValues().calculateTopPadding(),
        surfaceColor = surface,
        scrimColor = Color(0xFF29293A).copy(alpha = scrimAlpha),
        dark = dark,
        description = "操作面板",
    ) {
        Column(Modifier.fillMaxSize().padding(14.dp).alpha(contentAlpha)) {
            Column(Modifier.fillMaxWidth().height(108.dp).padding(8.dp)) {
                Text("A Short Title Is Best", style = Ios27Tokens.Type.Headline, color = colors.label)
                Spacer(Modifier.height(10.dp))
                Text("A message should be a short, complete sentence.",
                    style = Ios27Tokens.Type.Body, color = colors.label)
            }
            Column(Modifier.fillMaxWidth().height(160.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Destructive", "Action 2", "Action 3").forEach { label ->
                    val color = if (label == "Destructive") Color(0xFFFF3B4E) else colors.label
                    LiquidDialogButton(
                        text = label,
                        bgColor = actionSurface,
                        textColor = color,
                        onClick = { onAction(label) },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = Ios27Tokens.Type.Headline.copy(color = color),
                    )
                }
            }
        }
    }
}
