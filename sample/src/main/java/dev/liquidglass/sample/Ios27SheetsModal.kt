package dev.liquidglass.sample

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.zeaze.tianyinwallpaper.backdrop.Backdrop
import com.zeaze.tianyinwallpaper.backdrop.backdrops.rememberCanvasBackdrop
import com.zeaze.tianyinwallpaper.backdrop.backdrops.rememberLayerBackdrop
import com.zeaze.tianyinwallpaper.backdrop.drawBackdrop
import com.zeaze.tianyinwallpaper.backdrop.shadow.Shadow
import com.zeaze.tianyinwallpaper.catalog.components.LiquidButton
import com.zeaze.tianyinwallpaper.catalog.components.LiquidButtonStyle
import dev.liquidglass.ios27.Ios27PageColors
import dev.liquidglass.ios27.Ios27Tokens
import dev.liquidglass.ios27.ios27PressedClickable
import dev.liquidglass.ios27.ios27AndroidPane
import androidx.compose.runtime.saveable.rememberSaveable

private val SheetToolbarGlassStyle = LiquidButtonStyle(
    height = 44.dp,
    horizontalPadding = 0.dp,
    contentSpacing = 0.dp,
    blurRadius = 6.dp,
    lensRadiusX = 30.dp,
    lensRadiusY = 21.dp,
)

private val StackShape = RoundedCornerShape(
    topStart = 38.dp,
    topEnd = 38.dp,
    bottomStart = 9.5.dp,
    bottomEnd = 9.5.dp,
)

/**
 * Native implementation of the three Sheets (Modals) source variants.
 *
 * Fullscreen and Inspector are rebuilt on top of the exact sheet surface extracted
 * from Color Picker. Fullscreen Stack remains its separate opaque stacked-page design.
 */
@Composable
internal fun Ios27SheetsModal(
    file: String,
    variant: String,
    backdrop: Backdrop?,
    colors: Ios27PageColors,
    dark: Boolean,
    onDismiss: () -> Unit,
    onAction: () -> Unit,
) {
    if (variant == "Type=Fullscreen Stack") {
        FullscreenStackModal(
            file = file,
            colors = colors,
            dark = dark,
            onDismiss = onDismiss,
            onAction = onAction,
        )
    } else {
        ColorPickerBackedModal(
            file = file,
            inspector = variant == "Type=Inspector",
            backdrop = backdrop,
            colors = colors,
            dark = dark,
            onDismiss = onDismiss,
            onAction = onAction,
        )
    }
}

@Composable
private fun ColorPickerBackedModal(
    file: String,
    inspector: Boolean,
    backdrop: Backdrop?,
    colors: Ios27PageColors,
    dark: Boolean,
    onDismiss: () -> Unit,
    onAction: () -> Unit,
) {
    val exportedSheetBackdrop = rememberLayerBackdrop()
    val toolbarBackdrop: Backdrop? =
        if (backdrop != null && Build.VERSION.SDK_INT >= 33) exportedSheetBackdrop else null
    var actionActivated by rememberSaveable { mutableStateOf(false) }

    // Fullscreen / Inspector now use the exact same LiquidDialog hierarchy as Color Picker:
    // Root -> Scrim -> Panel glass -> Content. No extra sample-layer panel/clip exists.
    Ios27BottomSheetDialog(
        backdrop = backdrop,
        dark = dark,
        height = 459.dp,
        scrimColor = Color.Black.copy(alpha = if (inspector) 0f else 0.20f),
        onDismissRequest = onDismiss,
        exportedBackdrop = exportedSheetBackdrop,
        description = if (inspector) "模态面板 Type=Inspector" else "模态面板 Type=Fullscreen",
    ) {
        OriginalSheetChrome(
            file = file,
            toolbarBackdrop = toolbarBackdrop,
            colors = colors,
            dark = dark,
            actionActivated = actionActivated,
            onDismiss = onDismiss,
            onAction = {
                actionActivated = true
                onAction()
            },
        )
    }
}

@Composable
private fun FullscreenStackModal(
    file: String,
    colors: Ios27PageColors,
    dark: Boolean,
    onDismiss: () -> Unit,
    onAction: () -> Unit,
) {
    val stackSurface = colors.group
    val stackBackdrop = rememberCanvasBackdrop { drawRect(stackSurface) }
    var actionActivated by rememberSaveable { mutableStateOf(false) }

    BoxWithConstraints(
        Modifier.fillMaxSize()
            .semantics { contentDescription = "模态面板 Type=Fullscreen Stack" }
            .ios27AndroidPane("模态面板 Type=Fullscreen Stack", onDismiss),
    ) {
        // Match Activity View's runtime top origin exactly.
        val sheetTop = WindowInsets.safeDrawing.asPaddingValues().calculateTopPadding()
        val sheetHeight = (maxHeight - sheetTop).coerceAtLeast(1.dp)
        val backgroundTop = (sheetTop - 10.dp).coerceAtLeast(0.dp)
        val backgroundHeight = (maxHeight - backgroundTop).coerceAtLeast(1.dp)

        Box(
            Modifier.fillMaxSize()
                .background(Color.Black.copy(alpha = if (dark) 0.30f else 0.20f))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onDismiss,
                ),
        )

        // Preserve the source's 10pt stacked-page reveal while aligning the front
        // sheet with Activity View's safe-area top.
        Box(
            Modifier.align(Alignment.TopCenter)
                .offset(y = backgroundTop)
                .width(370.dp)
                .height(backgroundHeight)
                .clip(StackShape)
                .background(stackSurface),
        )

        Box(
            Modifier.align(Alignment.TopCenter)
                .offset(y = sheetTop)
                .fillMaxWidth()
                .height(sheetHeight)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = {},
                )
                .stackSheetSurface(stackBackdrop, stackSurface),
        ) {
            OriginalSheetChrome(
                file = file,
                toolbarBackdrop = stackBackdrop,
                colors = colors,
                dark = dark,
                actionActivated = actionActivated,
                onDismiss = onDismiss,
                onAction = {
                    actionActivated = true
                    onAction()
                },
            )
        }
    }
}

private fun Modifier.stackSheetSurface(
    backdrop: Backdrop,
    surface: Color,
): Modifier {
    return drawBackdrop(
        backdrop = backdrop,
        shape = { StackShape },
        effects = {},
        highlight = { null },
        shadow = {
            Shadow(
                radius = 75.dp,
                offset = DpOffset(0.dp, 15.dp),
                color = Color.Black.copy(alpha = 0.18f),
            )
        },
        onDrawSurface = { drawRect(surface) },
    ).clip(StackShape)
}

/**
 * Toolbar chrome reconstructed from each modal's source JSON.
 * Text and SF Symbol outlines come from the source scene instead of hard-coded icons.
 */
@Composable
private fun BoxScope.OriginalSheetChrome(
    file: String,
    toolbarBackdrop: Backdrop?,
    colors: Ios27PageColors,
    dark: Boolean,
    actionActivated: Boolean,
    onDismiss: () -> Unit,
    onAction: () -> Unit,
) {
    val leadingSource = rememberSceneSubtreeNamedText(file, "Leading", "Symbol")
    val trailingSource = rememberSceneSubtreeNamedText(file, "Trailing", "Symbol")
    val titleSource = rememberSceneSubtreeNamedText(file, "Title", "Title")
    val contentColor = if (dark) colors.label else Color(0xFF1A1A1A)

    Box(
        Modifier.align(Alignment.TopCenter)
            .offset(y = 5.dp)
            .size(58.dp, 4.dp)
            .clip(RoundedCornerShape(100.dp))
            .background(Color(0xFFCCCCCC)),
    )

    SourceToolbarButton(
        onClick = onDismiss,
        backdrop = toolbarBackdrop,
        modifier = Modifier.align(Alignment.TopStart).offset(x = 16.dp, y = 16.dp).size(73.dp, 44.dp),
        description = "关闭模态页面",
        tint = Color.Unspecified,
        colors = colors,
        dark = dark,
    ) {
        if (leadingSource != null) {
            SceneGlyphText(
                text = leadingSource,
                tint = if (dark) contentColor else leadingSource.color ?: contentColor,
            )
        }
    }

    Box(
        Modifier.align(Alignment.TopCenter).offset(y = 29.dp).size(36.dp, 22.dp),
        contentAlignment = Alignment.Center,
    ) {
        if (titleSource != null) {
            SceneGlyphText(
                text = titleSource,
                tint = if (dark) contentColor else titleSource.color ?: contentColor,
            )
        } else {
            Text("Title", style = Ios27Tokens.Type.Headline, color = contentColor)
        }
    }

    SourceToolbarButton(
        onClick = onAction,
        backdrop = toolbarBackdrop,
        modifier = Modifier.align(Alignment.TopEnd).offset(x = (-16).dp, y = 16.dp).size(44.dp),
        description = if (actionActivated) "模态页面操作 已触发" else "模态页面操作",
        tint = colors.accent,
        colors = colors,
        dark = dark,
    ) {
        if (trailingSource != null) {
            SceneGlyphText(
                text = trailingSource,
                tint = trailingSource.color ?: Color(0xFFF5F5F5),
            )
        }
    }
}

@Composable
private fun SourceToolbarButton(
    onClick: () -> Unit,
    backdrop: Backdrop?,
    modifier: Modifier,
    description: String,
    tint: Color,
    colors: Ios27PageColors,
    dark: Boolean,
    content: @Composable () -> Unit,
) {
    val accessible = modifier.semantics {
        contentDescription = description
        role = Role.Button
    }

    if (backdrop != null && Build.VERSION.SDK_INT >= 33) {
        LiquidButton(
            onClick = onClick,
            backdrop = backdrop,
            modifier = accessible,
            tint = tint,
            style = SheetToolbarGlassStyle,
            buttonHeight = 44.dp,
            contentPadding = PaddingValues(0.dp),
            content = {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    content()
                }
            },
        )
    } else {
        val shape = RoundedCornerShape(percent = 50)
        Box(
            accessible
                .background(
                    if (tint == Color.Unspecified) {
                        if (dark) colors.group.copy(alpha = 0.72f) else Color.White.copy(alpha = 0.72f)
                    } else {
                        tint
                    },
                    shape,
                )
                .ios27PressedClickable(shape = shape, onClick = onClick),
            contentAlignment = Alignment.Center,
        ) {
            content()
        }
    }
}
