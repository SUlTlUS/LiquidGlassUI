package dev.liquidglass

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.ui.Alignment
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.boundsInRoot

/**
 * Identifies a real Compose offscreen rendering root for panes below it.
 * Outer-to-inner IDs mirror FlClash's ancestor chain for capture planning.
 */
internal val LocalGlassCaptureRoots =
    staticCompositionLocalOf<List<Any>> { emptyList() }

/**
 * Opt-in GPU picture root for the resident atlas host. Its descendants
 * register independent glass and foreground layers; the GPU capture backend
 * replays only earlier layers for each sampling slot, never this complete
 * root (which would include glass from later siblings).
 *
 * Without planning or GPU residency enabled this is a normal Box.
 */
@Composable
fun GlassCaptureRoot(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    val registry = LocalGlassCaptureRegistry.current
    val parents = LocalGlassCaptureRoots.current
    val rootId = remember { Any() }
    val rootLayer = rememberGraphicsLayer()

    if (registry != null) {
        DisposableEffect(registry, rootId) {
            registry.addRoot(rootId, parents, rootLayer)
            onDispose { registry.removeRoot(rootId) }
        }
    }
    val recording = if (registry == null) modifier else {
        modifier.onGloballyPositioned {
            // Includes ancestor graphicsLayer transformations and clipping.
            registry.rootBounds(rootId, it.boundsInRoot())
        }.drawWithContent {
            rootLayer.record {
                this@drawWithContent.drawContent()
            }
            registry.paintedRoot(rootId)
            drawLayer(rootLayer)
        }
    }
    CompositionLocalProvider(
        LocalGlassCaptureRoots provides
            (if (registry == null) parents else parents + rootId),
    ) {
        Box(recording, content = content)
    }
}

/**
 * Marks arbitrary non-glass foreground content as a paint-order source.
 * This is important for a glass pane placed above icons, pictures or text
 * instead of directly above the scene background. The GPU host replays it
 * only if this content is *earlier* than the pane in the same capture root.
 */
@Composable
fun GlassCaptureBehind(
    modifier: Modifier = Modifier,
    /** Change when drawing changes without a layout or composition change. */
    contentVersion: Any? = Unit,
    captureAlpha: Float = 1f,
    contentAlignment: Alignment = Alignment.TopStart,
    content: @Composable BoxScope.() -> Unit,
) {
    val registry = LocalGlassCaptureRegistry.current
    val roots = LocalGlassCaptureRoots.current
    val id = remember { Any() }
    val layer = rememberGraphicsLayer()
    var bounds by remember { mutableStateOf(Rect.Zero) }
    var captureScale by remember { mutableStateOf(1f) }
    var sceneTransform by remember { mutableStateOf(GlassCaptureAffine()) }

    if (registry != null) {
        DisposableEffect(registry, id) {
            onDispose { registry.removeContent(id) }
        }
        SideEffect {
            if (bounds.width > 0f && bounds.height > 0f) {
                registry.upsertContent(
                    GlassCaptureBehindContent(
                        id = id, bounds = bounds,
                        captureRoots = roots, layer = layer,
                        transformScale = captureScale,
                        sceneTransform = sceneTransform,
                        captureAlpha = captureAlpha.coerceIn(0f, 1f),
                        contentVersion = contentVersion,
                    )
                )
            }
        }
    }
    val recording = if (registry == null) modifier else {
        // The capture wrapper comes BEFORE the supplied modifier, so its
        // display-list includes the actual clip, alpha, background and
        // translations instead of an unmasked copy of just the children.
        Modifier.onGloballyPositioned {
            val next = it.boundsInRoot()
            if (next != bounds) bounds = next
            if (it.size.width > 0 && it.size.height > 0) {
                val scale = maxOf(
                    next.width / it.size.width,
                    next.height / it.size.height,
                ).coerceAtLeast(0f)
                if (scale != captureScale) captureScale = scale
                val top = it.localToRoot(Offset.Zero)
                val axisX = it.localToRoot(Offset(it.size.width.toFloat(), 0f))
                val axisY = it.localToRoot(Offset(0f, it.size.height.toFloat()))
                val transform = GlassCaptureAffine.fromPoints(
                    top, axisX, axisY,
                    it.size.width.toFloat(), it.size.height.toFloat(),
                )
                if (transform != sceneTransform) sceneTransform = transform
            }
        }.drawWithContent {
            layer.record { this@drawWithContent.drawContent() }
            registry.paintedContent(id, layer)
            drawLayer(layer)
        }.then(modifier)
    }
    Box(recording, contentAlignment = contentAlignment, content = content)
}
