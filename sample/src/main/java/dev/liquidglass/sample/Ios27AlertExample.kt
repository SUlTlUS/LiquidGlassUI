package dev.liquidglass.sample

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.zeaze.tianyinwallpaper.backdrop.Backdrop
import dev.liquidglass.sample.Ios26Modal as LiquidDialog
import dev.liquidglass.ios27.LiquidDialogButton
import dev.liquidglass.ios27.Ios27PageColors
import dev.liquidglass.ios27.Ios27Tokens
import dev.liquidglass.ios27.ios27AndroidPane

/**
 * iOS 27 Alert content. The reusable LiquidDialog owns the scrim and glass shell;
 * this host only supplies the source-specific fields and actions.
 */
@Composable
internal fun Ios27AlertExample(
    file: String,
    variant: String,
    backdrop: Backdrop?,
    colors: Ios27PageColors,
    dark: Boolean,
    firstValue: String,
    secondValue: String,
    onFirstValueChange: (String) -> Unit,
    onSecondValueChange: (String) -> Unit,
    onDefault: () -> Unit,
    onDestructive: () -> Unit,
    onDismiss: () -> Unit,
    visible: Boolean = true,
) {
    val stacked = variant == "Type=Stacked"
    val width = if (stacked) 298.dp else 300.dp
    val height = if (stacked) 419.dp else 307.dp
    val panelSurface = if (dark) Color(0xCC2C2C2E) else Color.White.copy(alpha = 0.70f)
    val overlay = Color(0xFF29293A).copy(alpha = if (dark) 0.52f else 0.23f)
    val fieldSurface = if (dark) Color.White.copy(alpha = 0.10f) else Color.Black.copy(alpha = 0.08f)
    val neutralButtonSurface = if (dark) Color.White.copy(alpha = 0.12f) else Color(0xFF787880).copy(alpha = 0.16f)
    val title = rememberSceneText(file, "A Short Title Is Best")
    val message = rememberSceneText(file, "A message should be a short, complete sentence.")

    LiquidDialog(
        visible = visible,
        backdrop = backdrop,
        onDismissRequest = onDismiss,
        width = width,
        height = height,
        alignment = if (stacked) Alignment.Center else Alignment.TopCenter,
        offsetX = if (stacked) 0.dp else 10.dp,
        offsetY = if (stacked) 0.dp else 222.dp,
        surfaceColor = panelSurface,
        scrimColor = overlay,
        dark = dark,
        description = "提示弹窗",
        modifier = Modifier.ios27AndroidPane("提示弹窗", onDismiss),
    ) {
            Column(Modifier.fillMaxSize().padding(14.dp)) {
                Column(Modifier.fillMaxWidth().height(108.dp).padding(8.dp)) {
                    title?.let { SceneGlyphText(it, colors.label) }
                        ?: androidx.compose.material.Text("A Short Title Is Best", color = colors.label,
                            style = Ios27Tokens.Type.Body)
                    Spacer(Modifier.height(10.dp))
                    message?.let { SceneGlyphText(it, colors.label) }
                        ?: androidx.compose.material.Text("A message should be a short, complete sentence.",
                            color = colors.label, style = Ios27Tokens.Type.Body)
                }
                AlertFields(
                    firstValue = firstValue,
                    secondValue = secondValue,
                    fieldSurface = fieldSurface,
                    colors = colors,
                    backdrop = backdrop,
                    onFirstValueChange = onFirstValueChange,
                    onSecondValueChange = onSecondValueChange,
                )
                if (stacked) {
                    Column(Modifier.fillMaxWidth().height(160.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        AlertButton("Default", colors.accent, Color.White, onDefault)
                        AlertButton("Destructive", neutralButtonSurface, Color(0xFFFF375F), onDestructive)
                        AlertButton("Cancel", neutralButtonSurface, colors.label, onDismiss)
                    }
                } else {
                    Row(Modifier.fillMaxWidth().height(48.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        AlertButton("Cancel", neutralButtonSurface, colors.label, onDismiss,
                            Modifier.width(132.dp))
                        AlertButton("Default", colors.accent, Color.White, onDefault,
                            Modifier.width(132.dp))
                    }
                }
            }
    }
}

@Composable
private fun AlertFields(
    firstValue: String,
    secondValue: String,
    fieldSurface: Color,
    colors: Ios27PageColors,
    backdrop: Backdrop?,
    onFirstValueChange: (String) -> Unit,
    onSecondValueChange: (String) -> Unit,
) {
    Box(Modifier.fillMaxWidth().height(123.dp)) {
        Column(
            Modifier.fillMaxWidth().height(104.dp)
                .clip(RoundedCornerShape(26.dp))
                .background(fieldSurface),
        ) {
            AlertField(firstValue, "第一个输入框", backdrop, onFirstValueChange)
            AlertField(secondValue, "第二个输入框", backdrop, onSecondValueChange)
        }
        Box(Modifier.fillMaxWidth().padding(horizontal = 16.dp).offset(y = 52.dp).height(1.dp)
            .background(colors.separator))
    }
}

@Composable
private fun AlertField(
    value: String,
    label: String,
    backdrop: Backdrop?,
    onValueChange: (String) -> Unit,
) {
    Ios27EditableTextField(
        value = value,
        onValueChange = onValueChange,
        description = label,
        modifier = Modifier.fillMaxWidth(),
        placeholder = "Value",
        backdrop = backdrop,
    )
}

@Composable
private fun AlertButton(
    label: String,
    surface: Color,
    textColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier.fillMaxWidth(),
) {
    LiquidDialogButton(
        text = label,
        bgColor = surface,
        textColor = textColor,
        onClick = onClick,
        modifier = modifier,
        textStyle = Ios27Tokens.Type.Body.copy(color = textColor),
    )
}
