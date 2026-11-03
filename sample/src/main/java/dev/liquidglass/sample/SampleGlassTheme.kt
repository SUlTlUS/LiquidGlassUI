package dev.liquidglass.sample

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.zeaze.tianyinwallpaper.backdrop.backdrops.rememberCanvasBackdrop
import dev.liquidglass.*
import dev.liquidglass.ios27.LocalIos27PageColors
import dev.liquidglass.GlassStandard

/** Only host colors and localized action text remain in the sample. */
@Composable
internal fun SampleGlassTheme(standard: GlassStandard, effects: GlassEffects = LocalGlassEffects.current,
    content: @Composable () -> Unit) {
    val colors = LocalIos27PageColors.current
    GlassTheme(darkTheme = !MaterialTheme.colors.isLight, accentColor = colors.accent,
        standard = standard, effects = effects, backgroundColor = colors.page, content = content)
}

@Composable
internal fun DemoActionButton(label: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val colors = LocalIos27PageColors.current
    val fallback = rememberCanvasBackdrop { drawRect(colors.group) }
    GlassButton(onClick, currentGlassBackdrop() ?: fallback,
        modifier.semantics { contentDescription = label }) { Text(label, color = colors.label) }
}
