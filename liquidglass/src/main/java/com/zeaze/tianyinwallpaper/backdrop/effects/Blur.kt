package com.zeaze.tianyinwallpaper.backdrop.effects

import android.graphics.RenderEffect
import androidx.annotation.FloatRange
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.toAndroidTileMode
import com.zeaze.tianyinwallpaper.backdrop.BackdropEffectScope

fun BackdropEffectScope.blur(
    @FloatRange(from = 0.0) radius: Float,
    edgeTreatment: TileMode = TileMode.Clamp
) {
    if (radius <= 0f) return

    if (edgeTreatment != TileMode.Clamp || renderEffect != null) {
        if (radius > padding) {
            padding = radius
        }
    }

    val currentEffect = renderEffect
    renderEffect =
        if (currentEffect != null) {
            RenderEffect.createBlurEffect(
                radius,
                radius,
                currentEffect,
                edgeTreatment.toAndroidTileMode()
            )
        } else {
            RenderEffect.createBlurEffect(
                radius,
                radius,
                edgeTreatment.toAndroidTileMode()
            )
        }
}
