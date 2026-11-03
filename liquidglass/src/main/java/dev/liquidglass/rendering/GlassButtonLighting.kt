package dev.liquidglass.rendering

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.GraphicsLayerScope
import androidx.compose.ui.graphics.Shape

/** Optional material-only light supplied by the high-level library; no reverse dependency. */
val LocalGlassButtonLighting = staticCompositionLocalOf<(@Composable (Shape?) -> (GraphicsLayerScope.() -> Unit))?> { null }
