package dev.liquidglass.sample

import dev.liquidglass.*

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
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
import dev.liquidglass.GlassButton
import com.zeaze.tianyinwallpaper.catalog.components.LiquidButtonStyle
import dev.liquidglass.ios27.Ios27PageColors
import dev.liquidglass.ios27.Ios27Tokens
import dev.liquidglass.ios27.ios27PressedClickable
import dev.liquidglass.ios27.ios27AndroidPane
import androidx.compose.runtime.saveable.rememberSaveable
import dev.liquidglass.rendering.rememberTransformAwareBackdrop
import dev.liquidglass.GlassStandard

private val SheetToolbarGlassStyle = LiquidButtonStyle(
    height = 44.dp,
    horizontalPadding = 0.dp,
    contentSpacing = 0.dp,
    blurRadius = 6.dp,
    lensRadiusX = 30.dp,
    lensRadiusY = 21.dp,
)

// The panel has already applied its color processing. Keep a second optical
// surface, but do not re-grade the panel colors beneath these iOS26 buttons.
private val SheetOnGlassButtonStyle = LiquidButtonStyle.Default.copy(preserveBackdropColors = true)

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
    visible: Boolean = true,
) {
    if (variant == "Type=Fullscreen Stack") {
        FullscreenStackModal(
            visible = visible,
            file = file,
            backdrop = backdrop,
            colors = colors,
            dark = dark,
            onDismiss = onDismiss,
            onAction = onAction,
        )
    } else {
        ColorPickerBackedModal(
            visible = visible,
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
    visible: Boolean,
) {
    val exportedSheetBackdrop = rememberLayerBackdrop()
    val ios26 = currentGlassMaterial() == GlassStandard.Ios26
    val paintedBackdrop = rememberTransformAwareBackdrop(exportedSheetBackdrop)
    val toolbarBackdrop: Backdrop? =
        if (backdrop != null) {
            if (ios26) paintedBackdrop else exportedSheetBackdrop
        } else null
    var actionActivated by rememberSaveable { mutableStateOf(false) }

    // Fullscreen / Inspector now use the exact same LiquidDialog hierarchy as Color Picker:
    // Root -> Scrim -> Panel glass -> Content. No extra sample-layer panel/clip exists.
    Ios27BottomSheetDialog(
        solidColor = null,
        visible = visible,
        backdrop = backdrop,
        dark = dark,
        height = 459.dp,
        scrimColor = Color.Black.copy(alpha = if (inspector) 0f else 0.20f),
        onDismissRequest = onDismiss,
        exportedBackdrop = exportedSheetBackdrop,
        // Export the processed material only; the panel rim must not enter the button lens.
        exportPaintedSurface = false,
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
            buttonStyle = if (ios26) SheetOnGlassButtonStyle else SheetToolbarGlassStyle,
        )
    }
}

@Composable
private fun FullscreenStackModal(
    file: String,
    backdrop: Backdrop?,
    colors: Ios27PageColors,
    dark: Boolean,
    onDismiss: () -> Unit,
    onAction: () -> Unit,
    visible: Boolean,
) {
    val exported = rememberLayerBackdrop()
    var actionActivated by rememberSaveable { mutableStateOf(false) }
    BoxWithConstraints(Modifier.fillMaxSize()) {
        val top = WindowInsets.safeDrawing.asPaddingValues().calculateTopPadding()
        Ios26ModalLayer(backdrop, onDismiss,
            solidSurface = true,
            transition = dev.liquidglass.GlassModalTransition.BottomSheet,
            visible = visible,
            panelModifier = Modifier.fillMaxWidth().height((maxHeight - top).coerceAtLeast(1.dp)),
            shape = StackShape, surfaceColor = colors.group,
            scrimColor = Color.Black.copy(alpha = if (dark) .30f else .20f), dark = dark,
            alignment = Alignment.BottomCenter, exportedBackdrop = exported,
            behindSurface = {
                Box(Modifier.matchParentSize().offset(y = (-10).dp)
                    .padding(horizontal = 16.dp).background(colors.group, StackShape))
            },
            description = "模态面板 Type=Fullscreen Stack") {
            OriginalSheetChrome(file, exported, colors, dark, actionActivated, onDismiss) {
                actionActivated = true; onAction()
            }
        }
    }
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
    buttonStyle: LiquidButtonStyle = SheetToolbarGlassStyle,
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
        style = buttonStyle,
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
        style = buttonStyle,
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
    style: LiquidButtonStyle,
    content: @Composable () -> Unit,
) {
    val accessible = modifier.semantics {
        contentDescription = description
        role = Role.Button
    }

    if (backdrop != null) {
        GlassButton(
            onClick = onClick,
            backdrop = backdrop,
            modifier = accessible,
            tint = tint,
            style = style,
            height = 44.dp,
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
