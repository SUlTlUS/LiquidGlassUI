package com.lladlam.melox.ui.theme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance

internal val LightColors = lightColorScheme(
    primary = Color(0xFF007AFF),
    error = Color(0xFFFF3B30),
    background = Color(0xFFF7F7FA),
    surface = Color(0xFFFDFDFE),
    onPrimary = Color.White,
    onBackground = Color(0xFF17171A),
    onSurface = Color(0xFF17171A),
    surfaceVariant = Color(0xFFE9E9EE),
    onSurfaceVariant = Color(0xFF5D5D66),
)

internal val DarkColors = darkColorScheme(
    primary = Color(0xFF0A84FF),
    error = Color(0xFFFF453A),
    background = Color(0xFF0B0B0D),
    surface = Color(0xFF151518),
    onPrimary = Color.White,
    onBackground = Color(0xFFF5F5F7),
    onSurface = Color(0xFFF5F5F7),
    surfaceVariant = Color(0xFF252529),
    onSurfaceVariant = Color(0xFFB8B8C0),
)

/**
 * Returns the app's effective appearance rather than the device appearance.
 * Custom glass surfaces must follow an explicit Light/Dark override too.
 */
@Composable
fun isMeloXDarkTheme(): Boolean =
    MaterialTheme.colorScheme.background.luminance() < 0.5f
