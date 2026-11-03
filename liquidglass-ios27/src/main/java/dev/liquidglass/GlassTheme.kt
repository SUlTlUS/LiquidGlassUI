@file:Suppress("RestrictedApi")

package dev.liquidglass

import androidx.compose.material3.*
import androidx.compose.foundation.isSystemInDarkTheme
import dev.liquidglass.GlassStandard
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.google.android.material.color.utilities.*
import kotlin.math.min

enum class GlassColorStyle { Vivid, Tinted, Soft, Monochrome }

val LocalGlassStandard = staticCompositionLocalOf { GlassStandard.Ios26 }
private val LocalGlassDarkTheme = staticCompositionLocalOf<Boolean?> { null }
internal val LocalGlassBackgroundColor = staticCompositionLocalOf { Color(0xFFF2F2F7) }

@Composable
fun isGlassDarkTheme(): Boolean = LocalGlassDarkTheme.current ?: isSystemInDarkTheme()

@Composable
fun currentGlassHighlight(): GlassMenuHighlight =
    if (LocalGlassEffects.current.highlightFor(LocalGlassStandard.current) == GlassStandard.Ios26) GlassMenuHighlight.Ios26 else GlassMenuHighlight.Ios27

/** FlClash color.dart roles; optical Sketch presets remain independent. */
fun glassColorScheme(seed: Color, dark: Boolean, style: GlassColorStyle = GlassColorStyle.Vivid,
    pureBlack: Boolean = false): ColorScheme {
    val source = Hct.fromInt(seed.toArgb())
    val scheme = when (style) {
        GlassColorStyle.Vivid, GlassColorStyle.Tinted -> SchemeContent(source, dark, 0.0)
        GlassColorStyle.Soft -> SchemeTonalSpot(source, dark, 0.0)
        GlassColorStyle.Monochrome -> SchemeMonochrome(source, dark, 0.0)
    }
    val roles = MaterialDynamicColors()
    fun role(value: DynamicColor) = Color(value.getArgb(scheme))
    fun neutral(value: DynamicColor, chroma: Double = 3.0, darkTone: Double? = null): Color {
        val hct = Hct.fromInt(value.getArgb(scheme))
        val c = if (style == GlassColorStyle.Tinted) min(source.chroma / 6, 10.0)
            else min(hct.chroma, chroma)
        return Color(Hct.from(hct.hue, c, if (dark) darkTone ?: hct.tone else hct.tone).toInt())
    }
    val accent = style == GlassColorStyle.Vivid || style == GlassColorStyle.Tinted
    val tone = if (dark) (source.tone + 3).coerceIn(50.0, 75.0) else source.tone.coerceIn(35.0, 60.0)
    val primary = if (accent) Color(Hct.from(source.hue, source.chroma, tone).toInt()) else role(roles.primary())
    val paper = if (dark) 3.0 else 1.5
    val base = if (dark) darkColorScheme() else lightColorScheme()
    val colors = base.copy(
        primary = primary, surfaceTint = primary,
        onPrimary = if (accent) { if (tone <= 60) Color.White else
            Color(Hct.from(source.hue, source.chroma, 10.0).toInt()) } else role(roles.onPrimary()),
        primaryContainer = Color(Hct.from(scheme.primaryPalette.hue, min(scheme.primaryPalette.chroma, 48.0), if (dark) 30.0 else 90.0).toInt()),
        onPrimaryContainer = Color(Hct.from(scheme.primaryPalette.hue, min(scheme.primaryPalette.chroma, 48.0), if (dark) 90.0 else 30.0).toInt()),
        secondary = role(roles.secondary()), onSecondary = role(roles.onSecondary()),
        secondaryContainer = role(roles.secondaryContainer()), onSecondaryContainer = role(roles.onSecondaryContainer()),
        tertiary = role(roles.tertiary()), onTertiary = role(roles.onTertiary()),
        tertiaryContainer = role(roles.tertiaryContainer()), onTertiaryContainer = role(roles.onTertiaryContainer()),
        error = role(roles.error()), onError = role(roles.onError()),
        errorContainer = role(roles.errorContainer()), onErrorContainer = role(roles.onErrorContainer()),
        surface = neutral(roles.surface(), paper), background = neutral(roles.surface(), paper),
        onSurface = if (dark) Color(scheme.neutralPalette.tone(97)) else role(roles.onSurface()),
        onBackground = if (dark) Color(scheme.neutralPalette.tone(97)) else role(roles.onSurface()),
        onSurfaceVariant = role(roles.onSurfaceVariant()), surfaceVariant = role(roles.surfaceVariant()),
        inverseSurface = role(roles.inverseSurface()), inverseOnSurface = role(roles.inverseOnSurface()),
        inversePrimary = role(roles.inversePrimary()), outline = role(roles.outline()),
        outlineVariant = neutral(roles.outlineVariant(), 4.0), scrim = role(roles.scrim()),
        surfaceBright = neutral(roles.surfaceBright(), paper), surfaceDim = neutral(roles.surfaceDim()),
        surfaceContainerLowest = neutral(roles.surfaceContainerLowest()),
        surfaceContainerLow = neutral(roles.surfaceContainerLow()),
        surfaceContainer = neutral(roles.surfaceContainer(), darkTone = 15.0),
        surfaceContainerHigh = neutral(roles.surfaceContainerHigh(), darkTone = 21.0),
        surfaceContainerHighest = neutral(roles.surfaceContainerHighest(), darkTone = 27.0),
    )
    if (!dark || !pureBlack) return colors
    val shift = Hct.fromInt(colors.surface.toArgb()).tone
    fun lower(color: Color): Color {
        val hct = Hct.fromInt(color.toArgb())
        return Color(Hct.from(hct.hue, hct.chroma, (hct.tone - shift).coerceAtLeast(0.0)).toInt())
    }
    return colors.copy(surface = Color.Black, background = Color.Black, surfaceDim = Color.Black,
        surfaceContainerLowest = Color.Black, surfaceContainerLow = lower(colors.surfaceContainerLow),
        surfaceContainer = lower(colors.surfaceContainer), surfaceContainerHigh = lower(colors.surfaceContainerHigh),
        surfaceContainerHighest = lower(colors.surfaceContainerHighest), surfaceBright = lower(colors.surfaceBright))
}

private val LocalGlassColors = staticCompositionLocalOf<ColorScheme?> { null }

// Standalone controls share the same fixed default palette. A per-call-site
// remember recomputes every HCT role for each newly mounted menu row, directly
// on the opening animation's UI frame. Seeded themes remain scoped below.
private val defaultLightGlassColors by lazy { glassColorScheme(Color(0xFF0066D8), false) }
private val defaultDarkGlassColors by lazy { glassColorScheme(Color(0xFF0066D8), true) }

@Composable
fun GlassTheme(darkTheme: Boolean = isSystemInDarkTheme(), accentColor: Color = Color(0xFF0066D8),
    colorStyle: GlassColorStyle = GlassColorStyle.Vivid, pureBlack: Boolean = false,
    standard: GlassStandard = LocalGlassStandard.current,
    backgroundColor: Color = if (darkTheme) Color.Black else Color(0xFFF2F2F7),
    effects: GlassEffects = LocalGlassEffects.current,
    content: @Composable () -> Unit) {
    val colors = remember(darkTheme, accentColor, colorStyle, pureBlack) { glassColorScheme(accentColor, darkTheme, colorStyle, pureBlack) }
    CompositionLocalProvider(LocalGlassColors provides colors,
        LocalGlassStandard provides standard, LocalGlassDarkTheme provides darkTheme,
        LocalGlassBackgroundColor provides backgroundColor) {
        GlassEffectScope(effects, standard) { MaterialTheme(colorScheme = colors, content = content) }
    }
}

@Composable
internal fun glassColors(dark: Boolean): ColorScheme = LocalGlassColors.current
    ?: if (dark) defaultDarkGlassColors else defaultLightGlassColors

internal fun glassTone(color: Color, tone: Double): Color {
    val hct = Hct.fromInt(color.toArgb())
    return Color(Hct.from(hct.hue, hct.chroma, tone).toInt())
}

internal fun glassDestructive(dark: Boolean) = Color(if (dark) 0xFFFF4245 else 0xFFFF383C)

@Composable
internal fun GlassSurfaceTheme(dark: Boolean, elevated: Boolean, content: @Composable () -> Unit) {
    val base = glassColors(dark)
    val colors = remember(base, dark, elevated) {
        if (!dark || !elevated) base else {
            val lift = Hct.fromInt(base.surfaceContainer.toArgb()).tone - Hct.fromInt(base.surface.toArgb()).tone
            fun raise(color: Color) = glassTone(color, (Hct.fromInt(color.toArgb()).tone + lift).coerceAtMost(100.0))
            base.copy(surface = base.surfaceContainer, surfaceDim = raise(base.surfaceDim),
                surfaceBright = raise(base.surfaceBright), surfaceContainerLowest = raise(base.surfaceContainerLowest),
                surfaceContainerLow = raise(base.surfaceContainerLow), surfaceContainer = base.surfaceContainerHigh,
                surfaceContainerHigh = base.surfaceContainerHighest,
                surfaceContainerHighest = glassTone(base.surfaceContainerHighest,
                    (2 * Hct.fromInt(base.surfaceContainerHighest.toArgb()).tone -
                        Hct.fromInt(base.surfaceContainerHigh.toArgb()).tone).coerceAtMost(100.0)))
        }
    }
    CompositionLocalProvider(LocalGlassColors provides colors) { MaterialTheme(colorScheme = colors, content = content) }
}
