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
    Ios26ModalScope(backdrop, dark) {
        dev.liquidglass.GlassDialog(
            visible = visible, dark = dark, title = "A Short Title Is Best",
            onDismissRequest = onDismiss, onClosed = onHidden, destructive = true,
            actions = listOf("Action 2", "Action 3", "Destructive").map { label ->
                dev.liquidglass.GlassDialogAction(label) { onAction(label) }
            },
        ) {
            Text("A message should be a short, complete sentence.",
                style = Ios27Tokens.Type.Body, color = colors.label)
        }
    }
}
