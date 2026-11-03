package dev.liquidglass27.glass

import android.graphics.RenderEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.LayoutDirection
import com.zeaze.tianyinwallpaper.backdrop.BackdropEffectScope
import com.zeaze.tianyinwallpaper.backdrop.effects.blur
import com.zeaze.tianyinwallpaper.backdrop.effects.colorControls
import com.zeaze.tianyinwallpaper.backdrop.effects.lens
import com.zeaze.tianyinwallpaper.backdrop.effects.vibrancy

internal val LocalSketchMaterialCache = staticCompositionLocalOf<SketchMaterialCache?> { null }

/** Shared immutable filters; backdrop pixels still come from Sketch each draw. */
internal class SketchMaterialCache {
    private data class Key(val size: Size, val radius: Float, val density: Float,
        val fontScale: Float, val direction: LayoutDirection, val menu: Boolean)
    private data class Value(val effect: RenderEffect?, val padding: Float)
    private val filters = object : LinkedHashMap<Key, Value>(32, .75f, true) {
        override fun removeEldestEntry(eldest: MutableMap.MutableEntry<Key, Value>?): Boolean = size > 32
    }

    fun apply(scope: BackdropEffectScope, menu: Boolean, radius: Float) = with(scope) {
        val key = Key(size, radius, density, fontScale, layoutDirection, menu)
        val cached = filters[key]
        if (cached != null) {
            renderEffect = cached.effect
            padding = cached.padding
        } else {
            // The same Sketch functions, ordering and parameters as each pane
            // previously used. No shared bitmap or preblur changes edge samples.
            blur((if (menu) 12f else 2f) * density)
            lens((if (menu) 24f else 12f) * density, (if (menu) 48f else 24f) * density)
            if (menu) colorControls(saturation = 1.15f) else vibrancy()
            filters[key] = Value(renderEffect, padding)
        }
    }
}
