package com.zeaze.tianyinwallpaper.catalog.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.zeaze.tianyinwallpaper.backdrop.Backdrop
import com.zeaze.tianyinwallpaper.catalog.utils.AdaptiveLuminanceGlassState

/**
 * 天音液态玻璃按钮公开入口。
 *
 * iOS 26 rendering backend. Public cross-standard controls live in dev.liquidglass.
 */
@Composable
fun LiquidButton(
    onClick: () -> Unit,
    backdrop: Backdrop,
    modifier: Modifier = Modifier,
    isInteractive: Boolean = true,
    tint: Color = Color.Unspecified,
    surfaceColor: Color = Color.Unspecified,
    luminanceState: AdaptiveLuminanceGlassState? = null,
    style: LiquidButtonStyle = LiquidButtonStyle.Default,
    buttonHeight: Dp = style.height,
    contentPadding: PaddingValues = PaddingValues(horizontal = style.horizontalPadding),
    @DrawableRes iconRes: Int? = null,
    iconContentDescription: String? = null,
    iconSize: Dp = 20.dp,
    iconTint: Color = Color.Unspecified,
    iconOffsetX: Dp = 0.dp,
    iconOffsetY: Dp = 0.dp,
    enabled: Boolean = true,
    content: (@Composable RowScope.() -> Unit)? = null
) {
    LiquidButtonCore(
        onClick = onClick,
        backdrop = backdrop,
        modifier = modifier,
        isInteractive = isInteractive,
        tint = tint,
        surfaceColor = surfaceColor,
        luminanceState = luminanceState,
        style = style,
        buttonHeight = buttonHeight,
        contentPadding = contentPadding,
        iconRes = iconRes,
        iconContentDescription = iconContentDescription,
        iconSize = iconSize,
        iconTint = iconTint,
        iconOffsetX = iconOffsetX,
        iconOffsetY = iconOffsetY,
        content = content,
        enabled = enabled,
    )
}
