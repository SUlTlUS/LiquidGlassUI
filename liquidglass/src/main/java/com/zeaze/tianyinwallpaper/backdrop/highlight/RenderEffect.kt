package com.zeaze.tianyinwallpaper.backdrop.highlight

import android.graphics.RenderEffect
import com.zeaze.tianyinwallpaper.backdrop.BackdropEffectScope

fun BackdropEffectScope.effect(effect: RenderEffect) {
    val currentEffect = renderEffect
    renderEffect =
        if (currentEffect != null) {
            RenderEffect.createChainEffect(effect, currentEffect)
        } else {
            effect
        }
}

fun BackdropEffectScope.effect(effect: androidx.compose.ui.graphics.RenderEffect) {
    effect(effect.asAndroidRenderEffect())
}
