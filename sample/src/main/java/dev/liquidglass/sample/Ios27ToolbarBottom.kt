package dev.liquidglass.sample

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.zeaze.tianyinwallpaper.backdrop.Backdrop
import com.zeaze.tianyinwallpaper.catalog.components.LiquidButton
import dev.liquidglass.ios27.Ios27Tokens
import dev.liquidglass.ios27.LocalIos27PageColors
import dev.liquidglass.ios27.ios27PressedClickable

/**
 * The iOS 27 Toolbar - Bottom examples.
 *
 * This is deliberately a different primitive from LiquidBottomTabs: the source has
 * independent 48pt glass buttons, no enclosing tab track, and no selected-tab layout.
 * Coordinates are the source 402 x 874 phone coordinates, with the toolbar itself at
 * y=790 and each control at y=4 inside that 84pt area.
 */
@Composable
internal fun Ios27ToolbarBottomExample(
    file: String,
    variant: String,
    backdrop: Backdrop?,
    onAction: (String) -> Unit,
) {
    val colors = LocalIos27PageColors.current
    val symbol = rememberSceneText(file, "Symbol")
    val search = rememberSceneText(file, "􀊫")
    val placeholder = rememberSceneText(file, "Label")
    val microphone = rememberSceneText(file, "􀊰")
    val foreground = colors.label
    // The selected Tint is translucent in the source; keep the glass surface visible through it.
    val selectedTint = colors.accent.copy(alpha = 0.78f)

    Box(Modifier.fillMaxSize()) {
        when {
            variant.contains("Search") -> {
                ToolbarBottomSearch(
                    modifier = Modifier.offset(28.dp, 4.dp),
                    backdrop = backdrop,
                    symbol = symbol,
                    search = search,
                    placeholder = placeholder,
                    microphone = microphone,
                    foreground = foreground,
                    onClick = { onAction("搜索工具栏") },
                    onClear = { onAction("清除搜索") },
                )
            }
            variant.contains("Symbols") -> {
                ToolbarBottomSymbolButton(Modifier.offset(28.dp, 4.dp), backdrop, symbol, foreground) {
                    onAction("左侧工具栏按钮一")
                }
                ToolbarBottomSymbolButton(Modifier.offset(88.dp, 4.dp), backdrop, symbol, foreground) {
                    onAction("左侧工具栏按钮二")
                }
                ToolbarBottomSymbolButton(Modifier.offset(266.dp, 4.dp), backdrop, symbol, foreground) {
                    onAction("右侧工具栏按钮一")
                }
                ToolbarBottomSymbolButton(Modifier.offset(326.dp, 4.dp), backdrop, symbol, Color.White,
                    tint = selectedTint) {
                    onAction("右侧工具栏按钮二")
                }
            }
            else -> {
                // The Text variant is two text actions, not a tab bar and not four icon buttons.
                ToolbarBottomTextButton(Modifier.offset(28.dp, 4.dp), backdrop, "Button", foreground) {
                    onAction("Button")
                }
                ToolbarBottomTextButton(Modifier.offset(295.dp, 4.dp), backdrop, "Label", Color.White,
                    tint = selectedTint) {
                    onAction("Label")
                }
            }
        }
    }
}

@Composable
private fun ToolbarBottomSearch(
    modifier: Modifier,
    backdrop: Backdrop?,
    symbol: SceneText?,
    search: SceneText?,
    placeholder: SceneText?,
    microphone: SceneText?,
    foreground: Color,
    onClick: () -> Unit,
    onClear: () -> Unit,
) {
    // The source frame starts at x=28 and is 346pt wide: 286pt search field + 12pt gap + 48pt button.
    Box(modifier.size(346.dp, 48.dp)) {
        ToolbarBottomGlassButton(
            modifier = Modifier.size(286.dp, 48.dp),
            backdrop = backdrop,
            description = "搜索",
            onClick = onClick,
            content = {
                Box(Modifier.fillMaxSize()) {
                    search?.let { Box(Modifier.offset(11.dp, 14.dp)) { SceneGlyphText(it, foreground) } }
                    placeholder?.let { Box(Modifier.offset(41.dp, 14.dp)) { SceneGlyphText(it, foreground) } }
                    microphone?.let { Box(Modifier.offset(258.dp, 14.dp)) { SceneGlyphText(it, foreground) } }
                }
            },
        )
        ToolbarBottomSymbolButton(Modifier.offset(298.dp, 0.dp), backdrop, symbol, foreground,
            onClick = onClear)
    }
}

@Composable
private fun ToolbarBottomSymbolButton(
    modifier: Modifier,
    backdrop: Backdrop?,
    symbol: SceneText?,
    glyphColor: Color,
    tint: Color = Color.Unspecified,
    onClick: () -> Unit,
) {
    ToolbarBottomGlassButton(
        modifier = modifier.size(48.dp),
        backdrop = backdrop,
        description = "工具栏按钮",
        tint = tint,
        onClick = onClick,
        content = {
            symbol?.let { SceneGlyphText(it, glyphColor) }
        },
    )
}

@Composable
private fun ToolbarBottomTextButton(
    modifier: Modifier,
    backdrop: Backdrop?,
    label: String,
    color: Color,
    tint: Color = Color.Unspecified,
    onClick: () -> Unit,
) {
    ToolbarBottomGlassButton(
        modifier = modifier.size(79.dp, 48.dp),
        backdrop = backdrop,
        description = label,
        tint = tint,
        onClick = onClick,
        content = { Text(label, color = color, style = Ios27Tokens.Type.Body) },
    )
}

@Composable
private fun ToolbarBottomGlassButton(
    modifier: Modifier,
    backdrop: Backdrop?,
    description: String,
    tint: Color = Color.Unspecified,
    onClick: () -> Unit,
    content: @Composable () -> Unit,
) {
    val colors = LocalIos27PageColors.current
    val accessibleModifier = modifier.semantics {
        contentDescription = description
        role = Role.Button
    }
    if (backdrop != null) {
        LiquidButton(
            onClick = onClick,
            backdrop = backdrop,
            modifier = accessibleModifier,
            tint = tint,
            buttonHeight = 48.dp,
            contentPadding = PaddingValues(0.dp),
            content = { Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { content() } },
        )
    } else {
        Box(
            accessibleModifier
                .background(
                    if (tint != Color.Unspecified) tint else colors.group,
                    RoundedCornerShape(24.dp),
                )
                .ios27PressedClickable(
                    shape = RoundedCornerShape(24.dp),
                    onClick = onClick,
                ),
            contentAlignment = Alignment.Center,
        ) { content() }
    }
}
