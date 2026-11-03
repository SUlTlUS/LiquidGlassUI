package com.zeaze.tianyinwallpaper.backdrop.effects

import android.graphics.RenderEffect
import com.kyant.backdrop.RuntimeShader
import com.kyant.backdrop.asAndroidRuntimeShader
import com.zeaze.tianyinwallpaper.backdrop.BackdropEffectScope
import com.zeaze.tianyinwallpaper.backdrop.highlight.effect
import org.intellij.lang.annotations.Language

fun BackdropEffectScope.runtimeShaderEffect(
    key: String,
    @Language("AGSL") shaderString: String,
    uniformShaderName: String,
    block: RuntimeShader.() -> Unit
) {
    val shader = obtainRuntimeShader(key, shaderString).apply(block)
    effect(RenderEffect.createRuntimeShaderEffect(shader.asAndroidRuntimeShader(), uniformShaderName))
}
