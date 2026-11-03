package com.lladlam.melox.ui

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.GraphicsLayerScope
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.Color
import com.zeaze.tianyinwallpaper.catalog.components.LiquidBottomTabsSurface
import dev.liquidglass.GlassTabBarPreset

internal val LocalNavigationPreset = staticCompositionLocalOf { GlassTabBarPreset.MeloX }
internal val LocalNavigationShape = staticCompositionLocalOf<Shape?> { null }
internal val LocalNavigationLight = staticCompositionLocalOf<(GraphicsLayerScope.(LiquidBottomTabsSurface, Float) -> Unit)?> { null }
internal val LocalNavigationAccent = staticCompositionLocalOf<Color?> { null }

