package dev.liquidglass27.glass

import java.io.File
import org.junit.Assert.*
import org.junit.Test

class Ios27DockBodyTest {
    @Test fun surfaceEffectIsInsideShadowsAndOutsideTheMaterialOnly() {
        val source = File("../liquidglass/src/main/java/com/zeaze/tianyinwallpaper/backdrop/DrawBackdropModifier.kt").readText()
            .substringAfter("fun Modifier.drawBackdrop(")
        val shadow = source.indexOf("ShadowElement(")
        val effect = source.indexOf("Modifier.graphicsLayer(surfaceLayerBlock)")
        val material = source.indexOf("DrawBackdropElement(")
        assertTrue(shadow >= 0 && effect > shadow && material > effect)
        // The hook must not be forwarded to inverse backdrop-coordinate calculation.
        val drawNode = source.substringAfter("DrawBackdropElement(")
        assertFalse(drawNode.contains("layerBlock = surfaceLayerBlock"))
        val tabs = File("../liquidglass/src/main/java/com/zeaze/tianyinwallpaper/catalog/components/LiquidBottomTabs.kt").readText()
        assertEquals(3, Regex("surfaceLayerBlock =").findAll(tabs).count())
        assertFalse(tabs.contains("surfaceLayer?.invoke(this"))
    }
    @Test fun standardDockReusesLegacyBodyWithAnOptionalSurfaceHook() {
        val adapter = File("src/main/java/dev/liquidglass27/glass/Ios27NavigationDock.kt").readText()
        assertTrue(adapter.contains("LiquidBottomTabsRegular("))
        assertTrue(adapter.contains("surfaceLayer = layer"))
        assertFalse(adapter.contains("FlClashDockMotion("))
        val legacy = File("../liquidglass/src/main/java/com/zeaze/tianyinwallpaper/catalog/components/LiquidBottomTabs.kt").readText()
        assertTrue(legacy.contains("surfaceLayer: (GraphicsLayerScope.(LiquidBottomTabsSurface, Float) -> Unit)? = null"))
        assertTrue(legacy.contains("if (surfaceLayer == null) ({ Highlight.Default }) else null"))
        for (surface in listOf("Track", "Content", "Indicator")) {
            assertTrue(legacy.contains("LiquidBottomTabsSurface.$surface"))
        }
    }

    @Test fun eachOfTheThreeSurfaceLayersOwnsItsHighlightShader() {
        val adapter = File("src/main/java/dev/liquidglass27/glass/Ios27NavigationDock.kt").readText()
        assertTrue(adapter.contains("List(LiquidBottomTabsSurface.entries.size) { ControlHighlightRenderer(context, profileSamples = 256) }"))
        assertTrue(adapter.contains("renderEffect = lights[i].effect("))
        assertTrue(adapter.contains("Capsule().createOutline(size"))
    }
}
