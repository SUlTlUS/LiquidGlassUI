package dev.liquidglass.rendering

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import com.zeaze.tianyinwallpaper.backdrop.ShapeProvider
import com.zeaze.tianyinwallpaper.backdrop.shadow.Shadow
import com.zeaze.tianyinwallpaper.backdrop.shadow.ShadowElement

/** Share the original button shadow renderer when its surface is owned by a morphing popup. */
fun Modifier.drawGlassShadow(shape: () -> Shape, shadow: () -> Shadow?): Modifier =
    then(ShadowElement(ShapeProvider(shape), shadow))
