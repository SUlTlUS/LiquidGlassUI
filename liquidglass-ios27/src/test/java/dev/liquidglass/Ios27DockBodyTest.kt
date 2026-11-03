package dev.liquidglass

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
        val optics = File("../melox-tabbar/src/main/kotlin/com/lladlam/melox/ui/glass/MeloXBackdropComponents.kt").readText()
        assertEquals(2, Regex("surfaceLayerBlock =").findAll(optics).count())
        assertFalse(optics.contains("layerBlock = decoration"))
    }
    @Test fun publicBarUsesSingleRendererWithoutLegacyDockAliases() {
        val bar = File("src/main/java/dev/liquidglass/GlassTabBar.kt").readText()
        assertTrue(bar.contains("MeloXTabBar("))
        assertFalse(File("src/main/java/dev/liquidglass/Ios27NavigationDock.kt").exists())
        assertFalse(File("src/main/java/dev/liquidglass/GlassDock.kt").exists())
        assertFalse(bar.contains("FlClashDockMotion("))
        assertFalse(bar.contains("pointerInput("))
        val legacy = File("../liquidglass/src/main/java/com/zeaze/tianyinwallpaper/catalog/components/LiquidBottomTabs.kt").readText()
        assertTrue(legacy.contains("enum class LiquidBottomTabsSurface"))
        assertFalse(legacy.contains("fun LiquidBottomTabs"))
    }

    @Test fun eachOfTheThreeSurfaceLayersOwnsItsHighlightShader() {
        val adapter = File("src/main/java/dev/liquidglass/DockHighlight.kt").readText()
        assertTrue(adapter.contains("List(LiquidBottomTabsSurface.entries.size) { ControlHighlightRenderer(context, dock = true) }"))
        assertTrue(adapter.contains("renderEffect = lights[i].effect("))
        assertTrue(adapter.contains("return DockAppearance(dockShape, layer)"))
        assertFalse(adapter.contains("profileSamples"))
        assertFalse(adapter.contains("asAndroidPath"))
    }
}
