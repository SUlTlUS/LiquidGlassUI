package dev.liquidglass

import androidx.compose.ui.graphics.Color
import kotlin.math.pow
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SketchOpticalInputsTest {
    @Test
    fun blurUsesFlClashStyleRatherThanThemeDependentSketchPreset() {
        assertEquals(2.5f, sketchBaseBlurDp(GlassStyle.regular(false)), 0f)
        assertEquals(2.5f, sketchBaseBlurDp(GlassStyle.regular(true)), 0f)
        assertEquals(12f, sketchBaseBlurDp(GlassStyle.menu(false)), 0f)
        assertEquals(12f, sketchBaseBlurDp(GlassStyle.menu(true)), 0f)
        assertEquals(16f, sketchBaseBlurDp(GlassStyle.alert(true)), 0f)
        assertEquals(0f, sketchBaseBlurDp(GlassStyle.lens(false)), 0f)
        assertEquals(0f, sketchBaseBlurDp(GlassStyle.lens(true)), 0f)
    }

    @Test
    fun hazeAndBaseBlurAreIndependentLikeFlClash() {
        val lightPill = GlassStyle.pill(false)
        val darkPill = GlassStyle.pill(true)
        assertEquals(0f, sketchBaseBlurDp(lightPill), 0f)
        assertEquals(0f, sketchBaseBlurDp(darkPill), 0f)
        assertEquals(15.6f, lightPill.hazeBlur, 0f)
        assertEquals(.28f, flClashHazeWeights(lightPill).first, .00001f)
        assertEquals(.72f, flClashHazeWeights(lightPill).second, .00001f)
        assertEquals(.28f, flClashHazeWeights(darkPill).first, .00001f)
        assertEquals(.72f, flClashHazeWeights(darkPill).second, .00001f)
        assertEquals(0f, sketchBaseBlurDp(GlassStyle.lens(false)), 0f)
        assertEquals(0f, sketchBaseBlurDp(GlassStyle.regular(false).materialize(0f)), 0f)
    }

    @Test
    fun noHazeNeedsNoSecondGpuEffectBranch() {
        for (style in listOf(GlassStyle.regular(false), GlassStyle.menu(true),
                GlassStyle.alert(false), GlassStyle.lens(true))) {
            assertEquals(1f, flClashHazeWeights(style).first, 0f)
            assertEquals(0f, flClashHazeWeights(style).second, 0f)
        }
        assertEquals(0f, flClashHazeWeights(GlassStyle.pill(true).materialize(0f)).second, 0f)
    }

    @Test
    fun blueTintAndTransparentLensCannotInferThemeFromRedChannel() {
        val lightBlue = GlassStyle.tinted(Color(0xFF237EFF), false)
        val darkBlue = GlassStyle.tinted(Color(0xFF237EFF), true)
        assertTrue(lightBlue.tint.red < .45f)
        assertTrue(darkBlue.tint.red < .45f)
        assertEquals(lightBlue.tint.red, darkBlue.tint.red, 0f)
        assertFalse(GlassStyle.lens(false).tint.alpha > 0f)
        assertFalse(GlassStyle.lens(true).tint.alpha > 0f)
        // Theme is already encoded in the selected GlassStyle parameters;
        // no red-channel heuristic remains in the rendering pipeline.
    }

    @Test
    fun flutterBrightnessScalesSketchExposureByTheSameRgbMultiplier() {
        for (style in listOf(
            GlassStyle.regular(false), GlassStyle.regular(true),
            GlassStyle.menu(false), GlassStyle.menu(true),
            GlassStyle.lens(false), GlassStyle.lens(true),
        )) {
            val exposure = sketchBrightnessExposureEv(style)
            val outputMultiplier = 2f.pow(exposure / 2.2f)
            assertEquals(style.brightness, outputMultiplier, .0001f)
        }
    }

    @Test
    fun originalSaturationValuesAreNotReplacedByVibrancyConstant() {
        assertEquals(2.08f, sketchBaseSaturation(GlassStyle.regular(false)), .00001f)
        assertEquals(2.84f, sketchBaseSaturation(GlassStyle.regular(true)), .00001f)
        assertEquals(2f, sketchBaseSaturation(GlassStyle.menu(false)), .00001f)
        assertEquals(1f, sketchBaseSaturation(GlassStyle.lens(false)), .00001f)
    }
}
