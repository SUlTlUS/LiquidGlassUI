package com.zeaze.tianyinwallpaper.catalog.components

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Tianyin 的液态玻璃外观参数集中放在这里。
 *
 * 上游 AndroidLiquidGlass 的 catalog 组件只是示例组件，后续同步上游时优先替换结构代码，
 * 不要把这些颜色、尺寸、降级策略散落回组件内部。
 */
@Immutable
data class LiquidButtonStyle(
    val height: Dp = 48.dp,
    val horizontalPadding: Dp = 16.dp,
    val contentSpacing: Dp = 8.dp,
    val blurRadius: Dp = 2.dp,
    val lensRadiusX: Dp = 12.dp,
    val lensRadiusY: Dp = 24.dp,
    val pressedExpansion: Dp = 4.dp,
    val dragInitialDerivative: Float = 0.05f,
    /** For an already processed glass surface: retain its colors without a second color filter. */
    val preserveBackdropColors: Boolean = false
) {
    companion object {
        val Default = LiquidButtonStyle()
    }
}
