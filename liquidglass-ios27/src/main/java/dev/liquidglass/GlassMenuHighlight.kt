package dev.liquidglass

import android.graphics.Path
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asComposePath

/** Menu lighting only; both variants share the same multilevel route and motion. */
enum class GlassMenuHighlight { Ios26, Ios27 }

internal val LocalGlassMenuHighlight = staticCompositionLocalOf { GlassMenuHighlight.Ios27 }

internal val LocalGlassMenuSurfaceTint = staticCompositionLocalOf { true }

/** Scope menu surfaces separately from dialogs and sheets sharing the same renderer. */
@Composable
fun GlassMenuMaterialScope(standard: GlassStandard = currentGlassMaterial(), content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalGlassMenuSurfaceTint provides (standard != GlassStandard.Ios26),
        content = content)
}

// A new immutable shape per contour invalidates the iOS 26 outline cache during Goo motion.
internal fun menuHighlightShape(path: Path): Shape {
    val outline = Outline.Generic(path.asComposePath())
    return object : Shape {
        override fun createOutline(
            size: androidx.compose.ui.geometry.Size,
            layoutDirection: androidx.compose.ui.unit.LayoutDirection,
            density: androidx.compose.ui.unit.Density,
        ): Outline = outline
    }
}
