package dev.liquidglass.sample

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import dev.liquidglass.components.LocalProgressiveBlurSurface
import dev.liquidglass.ios27.LocalIos27PageColors
import dev.liquidglass.ios27.Ios27AndroidDesignBehavior
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.zeaze.tianyinwallpaper.backdrop.Backdrop
import com.zeaze.tianyinwallpaper.backdrop.backdrops.LayerBackdrop
import com.zeaze.tianyinwallpaper.backdrop.backdrops.layerBackdrop
import com.zeaze.tianyinwallpaper.backdrop.backdrops.rememberLayerBackdrop
import com.zeaze.tianyinwallpaper.ui.commom.ProgressiveBlurContent

internal const val ExampleDesignWidth = 402f

/**
 * Canonical page recording source for glass overlays.
 *
 * The surface color is drawn into the captured layer before page content, so glass
 * samples the same visible background as the page instead of an implicit transparent layer.
 */
@Composable
internal fun rememberExamplePageBackdrop(surface: Color): LayerBackdrop =
    rememberLayerBackdrop {
        drawRect(surface)
        drawContent()
    }

/**
 * Shared coordinate shell for iPhone examples exported in the 402pt source space.
 *
 * Horizontal/bottom safe drawing is consumed before the remaining width is mapped to
 * 402 design points. Top safe inset stays available to the content so source toolbar
 * coordinates remain explicit instead of being shifted by an outer padding layer.
 */
@Composable
internal fun ExampleDesignPage(
    background: Color,
    modifier: Modifier = Modifier,
    designWidth: Float = ExampleDesignWidth,
    fontScale: Float? = null,
    content: @Composable BoxScope.(topInset: Dp) -> Unit,
) {
    val deviceDensity = LocalDensity.current
    val topInsetPx = with(deviceDensity) {
        WindowInsets.safeDrawing.asPaddingValues().calculateTopPadding().toPx()
    }

    BoxWithConstraints(
        modifier
            .fillMaxSize()
            .background(background)
            .windowInsetsPadding(
                WindowInsets.safeDrawing.only(
                    WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom,
                ),
            ),
    ) {
        val designDensity = Density(
            density = constraints.maxWidth / designWidth,
            fontScale = fontScale ?: deviceDensity.fontScale,
        )
        Ios27AndroidDesignBehavior(designDensity) {
            val topInset = with(designDensity) { topInsetPx.toDp() }
            Box(Modifier.fillMaxSize()) {
                CompositionLocalProvider(LocalProgressiveBlurSurface provides background) {
                    content(topInset)
                }
            }
        }
    }
}

/**
 * Coordinate-only overlay shell. It does not add background or safe-area padding;
 * callers use it for modal/overlay source geometry already positioned above a page.
 */
@Composable
internal fun ExampleDesignOverlay(
    modifier: Modifier = Modifier,
    designWidth: Float = ExampleDesignWidth,
    fontScale: Float = 1f,
    content: @Composable BoxScope.() -> Unit,
) {
    BoxWithConstraints(modifier.fillMaxSize()) {
        val designDensity = Density(
            density = constraints.maxWidth / designWidth,
            fontScale = fontScale,
        )
        Ios27AndroidDesignBehavior(designDensity) {
            Box(Modifier.fillMaxSize(), content = content)
        }
    }
}

/**
 * Full-bleed system-scene shell. Unlike [ExampleDesignPage], this consumes the top safe
 * inset before mapping the remaining viewport to the 402pt source space because those
 * scenes are exported as the visible device content area itself.
 */
@Composable
internal fun ExampleFullBleedDesignPage(
    background: Color,
    modifier: Modifier = Modifier,
    designWidth: Float = ExampleDesignWidth,
    fontScale: Float = 1f,
    content: @Composable BoxScope.() -> Unit,
) {
    BoxWithConstraints(
        modifier
            .fillMaxSize()
            .background(background)
            .safeDrawingPadding(),
    ) {
        val designDensity = Density(
            density = constraints.maxWidth / designWidth,
            fontScale = fontScale,
        )
        Ios27AndroidDesignBehavior(designDensity) {
            Box(Modifier.fillMaxSize(), content = content)
        }
    }
}

/**
 * Records only the non-glass page content. Glass chrome/overlays must remain siblings.
 */
@Composable
internal fun ExampleBackdropLayer(
    backdrop: LayerBackdrop?,
    background: Color,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier
            .background(background)
            .then(
                if (backdrop != null) Modifier.layerBackdrop(backdrop)
                else Modifier,
            ),
        content = content,
    )
}

/**
 * Shared recorded 402pt page shell.
 *
 * This is the common architecture for most source-driven examples:
 *
 * ExampleDesignPage
 * └─ recorded page surface (background + LayerBackdrop)
 *    └─ page-specific content
 * └─ overlay siblings (glass chrome, menus, sheets, etc.)
 *
 * Keeping recorded content and glass overlays as siblings prevents recursive backdrop
 * capture and removes the repeated Box/LayerBackdrop plumbing from every example.
 */
@Composable
internal fun ExampleRecordedDesignPage(
    background: Color,
    modifier: Modifier = Modifier,
    designWidth: Float = ExampleDesignWidth,
    fontScale: Float? = null,
    backdrop: LayerBackdrop = rememberExamplePageBackdrop(background),
    content: @Composable BoxScope.(topInset: Dp, backdrop: LayerBackdrop) -> Unit,
    overlay: @Composable BoxScope.(topInset: Dp, backdrop: LayerBackdrop) -> Unit = { _, _ -> },
) {
    ExampleDesignPage(
        background = background,
        modifier = modifier,
        designWidth = designWidth,
        fontScale = fontScale,
    ) { topInset ->
        Box(Modifier.fillMaxSize()) {
            ExampleBackdropLayer(
                backdrop = backdrop,
                background = background,
                modifier = Modifier.fillMaxSize(),
            ) {
                content(topInset, backdrop)
            }
            overlay(topInset, backdrop)
        }
    }
}

/**
 * Shared 402pt page with the standard progressive top blur + source toolbar.
 *
 * Callers own only their content/state. Geometry, recording and chrome ordering stay
 * identical across examples.
 */
@Composable
internal fun ExampleToolbarDesignPage(
    background: Color,
    isLightTheme: Boolean,
    toolbarFile: String?,
    modifier: Modifier = Modifier,
    designWidth: Float = ExampleDesignWidth,
    fontScale: Float? = null,
    onBack: () -> Unit = {},
    onReference: () -> Unit = {},
    onTrailing: () -> Unit = {},
    blurAlpha: Float = 1f,
    showBlur: Boolean = true,
    fallbackTitle: String? = null,
    trailingOverflow: Boolean = false,
    showTrailing: Boolean = true,
    content: @Composable BoxScope.(topInset: Dp, backdrop: LayerBackdrop) -> Unit,
    overlay: @Composable BoxScope.(topInset: Dp, backdrop: LayerBackdrop) -> Unit = { _, _ -> },
) {
    ExampleRecordedDesignPage(
        background = background,
        modifier = modifier,
        designWidth = designWidth,
        fontScale = fontScale,
        content = content,
        overlay = { topInset, backdrop ->
            ExampleTopChrome(
                backdrop = backdrop,
                isLightTheme = isLightTheme,
                toolbarFile = toolbarFile,
                onBack = onBack,
                onReference = onReference,
                onTrailing = onTrailing,
                blurAlpha = blurAlpha,
                showBlur = showBlur,
                fallbackTitle = fallbackTitle,
                trailingOverflow = trailingOverflow,
                showTrailing = showTrailing,
            )
            overlay(topInset, backdrop)
        },
    )
}

/**
 * Shared responsive page recorder for app-level pages that are not authored in 402pt
 * source coordinates (settings/root lists).
 */
@Composable
internal fun ExampleRecordedResponsivePage(
    background: Color,
    modifier: Modifier = Modifier,
    backdrop: LayerBackdrop = rememberExamplePageBackdrop(background),
    content: @Composable BoxScope.(topInset: Dp, backdrop: LayerBackdrop) -> Unit,
    overlay: @Composable BoxScope.(topInset: Dp, backdrop: LayerBackdrop) -> Unit = { _, _ -> },
) {
    val density = LocalDensity.current
    val topInset = WindowInsets.safeDrawing.asPaddingValues().calculateTopPadding()
    Box(
        modifier
            .fillMaxSize()
            .background(background)
            .windowInsetsPadding(
                WindowInsets.safeDrawing.only(
                    WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom,
                ),
            ),
    ) {
        ExampleBackdropLayer(
            backdrop = backdrop,
            background = background,
            modifier = Modifier.fillMaxSize(),
        ) {
            content(topInset, backdrop)
        }
        CompositionLocalProvider(LocalProgressiveBlurSurface provides background) {
            overlay(topInset, backdrop)
        }
    }
}

/**
 * Shared top overlay for native example pages.
 *
 * Progressive blur is always drawn before the toolbar and both remain outside the
 * recorded page layer, preventing recursive backdrop capture.
 */
@Composable
internal fun BoxScope.ExampleTopChrome(
    backdrop: Backdrop?,
    isLightTheme: Boolean,
    toolbarFile: String? = null,
    onBack: () -> Unit = {},
    onReference: () -> Unit = {},
    onTrailing: () -> Unit = {},
    blurAlpha: Float = 1f,
    showBlur: Boolean = true,
    fallbackTitle: String? = null,
    trailingOverflow: Boolean = false,
    showTrailing: Boolean = true,
    surfaceColor: Color = LocalProgressiveBlurSurface.current.let {
        if (it == Color.Unspecified) LocalIos27PageColors.current.page else it
    },
    appBarHeight: Dp? = null,
    scrollDriven: Boolean = false,
) {
    if (showBlur) {
        ProgressiveBlurContent(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth(),
            backdrop = backdrop,
            isLightTheme = isLightTheme,
            visibility = blurAlpha,
            surfaceColor = surfaceColor,
            appBarHeight = appBarHeight ?: (
                WindowInsets.safeDrawing.asPaddingValues().calculateTopPadding() +
                    if (toolbarFile != null) 48.dp else 0.dp
                ),
            scrollDriven = scrollDriven,
        )
    }

    if (toolbarFile != null) {
        ExampleNavBar(
            file = toolbarFile,
            onBack = onBack,
            onReference = onReference,
            onTrailing = onTrailing,
            backdrop = backdrop,
            fallbackTitle = fallbackTitle,
            trailingOverflow = trailingOverflow,
            showTrailing = showTrailing,
        )
    }
}
