package dev.liquidglass

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp

/** Optical parameters are dp, following FlClash's LiquidGlassStyle presets. */
@Immutable
data class GlassStyle(
    val tint: Color,
    val contrast: Float = 0f,
    val blur: Float = 2.5f,
    val haze: Float = 0f,
    val hazeBlur: Float = 0f,
    val refraction: Float = 60f,
    val bevel: Float = 20f,
    val saturation: Float = 1.5f,
    val brightness: Float = 1f,
    val rim: Float = .6f,
    val rimWidth: Float = 1.3f,
    val rimTail: Float = 0f,
    val rimFocus: Float = 0f,
    val rimAdded: Float = 0f,
    val rimBlur: Float = 0f,
    // Original LiquidGlassRim: independent menu/alert side line + banded shine.
    val menuRimSide: Float = 0f,
    val menuRimCorner: Float = 0f,
    val menuRimFlat: Float = 0f,
    val menuRimShine: Float = 0f,
    val dispersion: Float = 0f,
    val hairline: Float = 0f,
    val hairlineSide: Float = hairline,
    val endShade: Float = 0f,
) {
    fun materialize(amount: Float): GlassStyle {
        val t = amount.coerceIn(0f, 1f)
        return copy(
            tint = tint.copy(alpha = tint.alpha * t),
            contrast = contrast * t,
            blur = blur * t,
            haze = haze * t,
            refraction = refraction * t,
            saturation = 1f + (saturation - 1f) * t,
            brightness = 1f + (brightness - 1f) * t,
            rim = rim * t,
            menuRimSide = menuRimSide * t,
            menuRimCorner = menuRimCorner * t,
            menuRimFlat = menuRimFlat * t,
            menuRimShine = menuRimShine * t,
            rimBlur = rimBlur * t,
            endShade = endShade * t,
            dispersion = dispersion * t,
            hairline = hairline * t,
            hairlineSide = hairlineSide * t,
        )
    }

    companion object {
        private val darkGlass = Color(0xFF45454B)

        fun regular(dark: Boolean) = if (dark) GlassStyle(
            tint = darkGlass.copy(alpha = .62f),
            contrast = -.4f,
            saturation = 2.84f,
            rim = .28f,
            rimWidth = 1.2f,
            rimTail = .17f,
            rimFocus = 1f,
            rimAdded = .38f,
            hairlineSide = .75f,
        ) else GlassStyle(
            tint = Color(0xFFFDFDFD).copy(alpha = .54f),
            contrast = .4f,
            saturation = 2.08f,
            rim = .28f,
            rimWidth = 1.2f,
            rimTail = .17f,
            rimFocus = 1f,
            rimAdded = .38f,
            hairline = .25f,
            hairlineSide = .75f,
        )

        fun pill(dark: Boolean) = if (dark) GlassStyle(
            tint = darkGlass.copy(alpha = .48f),
            contrast = -.4f,
            blur = 0f,
            haze = .72f,
            hazeBlur = 15.6f,
            saturation = 1.925f,
            rim = .35f,
        ) else GlassStyle(
            tint = Color(0xFFF4F4F4).copy(alpha = .576f),
            contrast = .4f,
            blur = 0f,
            haze = .72f,
            hazeBlur = 15.6f,
            saturation = 2.1f,
            hairline = .35f,
        )

        fun menu(dark: Boolean) = if (dark) GlassStyle(
            tint = darkGlass.copy(alpha = .63f),
            contrast = -.53f,
            blur = 12f,
            saturation = 1.75f,
            brightness = 1.8f,
            rim = 0f,
            menuRimSide = .27f,
            menuRimCorner = .1f,
            menuRimFlat = .02f,
            menuRimShine = .22f,
        ) else GlassStyle(
            tint = Color(0xFFFDFDFD).copy(alpha = .55f),
            blur = 12f,
            saturation = 2f,
            brightness = 1.1f,
            rim = 0f,
            menuRimSide = .25f,
            menuRimCorner = .15f,
            menuRimFlat = .09f,
            menuRimShine = .16f,
        )

        fun alert(dark: Boolean) = menu(dark).copy(
            blur = 16f,
            menuRimSide = if (dark) .75f else .35f,
            menuRimCorner = if (dark) .1f else .22f,
            menuRimFlat = if (dark) .03f else .12f,
            menuRimShine = if (dark) .22f else .16f,
        )

        fun tinted(color: Color, dark: Boolean) = GlassStyle(
            tint = color.copy(alpha = .82f),
            saturation = 1.2f,
            rim = if (dark) .14f else .2f,
            rimWidth = 1.2f,
            rimTail = .17f,
            rimFocus = 1f,
            rimAdded = .38f,
            hairline = if (dark) 0f else .34f,
            hairlineSide = if (dark) .73f else .5f,
        )

        fun lens(dark: Boolean) = GlassStyle(
            tint = Color.Transparent,
            blur = 0f,
            refraction = -4.3f,
            bevel = 5.5f,
            saturation = 1f,
            brightness = if (dark) 1.33f else 1f,
            rim = if (dark) .3f else .6f,
            rimWidth = 1.1f,
            rimBlur = 1.5f,
            dispersion = 6.5f,
            hairline = .42f,
            endShade = .045f,
        )

        fun interpolate(from: GlassStyle, to: GlassStyle, amount: Float): GlassStyle {
            val t = amount.coerceIn(0f, 1f)
            fun mix(a: Float, b: Float) = a + (b - a) * t
            return GlassStyle(
                tint = lerp(from.tint, to.tint, t),
                contrast = mix(from.contrast, to.contrast),
                blur = mix(from.blur, to.blur),
                haze = mix(from.haze, to.haze),
                hazeBlur = mix(from.hazeBlur, to.hazeBlur),
                refraction = mix(from.refraction, to.refraction),
                bevel = mix(from.bevel, to.bevel),
                saturation = mix(from.saturation, to.saturation),
                brightness = mix(from.brightness, to.brightness),
                rim = mix(from.rim, to.rim),
                rimWidth = mix(from.rimWidth, to.rimWidth),
                rimTail = mix(from.rimTail, to.rimTail),
                rimFocus = mix(from.rimFocus, to.rimFocus),
                rimAdded = mix(from.rimAdded, to.rimAdded),
                rimBlur = mix(from.rimBlur, to.rimBlur),
                menuRimSide = mix(from.menuRimSide, to.menuRimSide),
                menuRimCorner = mix(from.menuRimCorner, to.menuRimCorner),
                menuRimFlat = mix(from.menuRimFlat, to.menuRimFlat),
                menuRimShine = mix(from.menuRimShine, to.menuRimShine),
                dispersion = mix(from.dispersion, to.dispersion),
                hairline = mix(from.hairline, to.hairline),
                hairlineSide = mix(from.hairlineSide, to.hairlineSide),
                endShade = mix(from.endShade, to.endShade),
            )
        }
    }
}
