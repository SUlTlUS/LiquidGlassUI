package dev.liquidglass27.glass

import android.graphics.Path as AndroidPath
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.zeaze.tianyinwallpaper.backdrop.Backdrop
import com.zeaze.tianyinwallpaper.backdrop.drawBackdrop
import kotlin.math.min

/** LayerBackdrop records only GlassScene's independent background sibling. */
internal val LocalSketchGlassBackdrop =
    staticCompositionLocalOf<Backdrop?> { null }

/**
 * Sketch's lens() requires CornerBasedShape for its four-corner optical
 * parameters. Its drawBackdrop() also caches the *layout-layer clipping*
 * shape. When Goo/menu geometry animates inside fixed bounds, that clip may
 * remain stale and produce a white rectangle or a trailing silhouette.
 *
 * Keep Sketch's internal layer clip RECTANGULAR and stable. The exact
 * FlClash Goo/RSuperellipse Path is clipped by the outer drawWithContent,
 * which updates at draw-time rather than requiring a new layout pass.
 */
internal class SketchGlassShape(
    private val nativePath: AndroidPath?,
    topStart: CornerSize,
    topEnd: CornerSize = topStart,
    bottomEnd: CornerSize = topStart,
    bottomStart: CornerSize = topStart,
) : CornerBasedShape(topStart, topEnd, bottomEnd, bottomStart) {
    override fun createOutline(
        size: Size,
        topStart: Float,
        topEnd: Float,
        bottomEnd: Float,
        bottomStart: Float,
        layoutDirection: LayoutDirection,
    ): Outline {
        // Do not clip by the old cached animation frame. The live FlClash
        // silhouette is applied outside drawBackdrop on every draw instead.
        return Outline.Rectangle(Rect(0f, 0f, size.width, size.height))
    }

    override fun copy(
        topStart: CornerSize,
        topEnd: CornerSize,
        bottomEnd: CornerSize,
        bottomStart: CornerSize,
    ): CornerBasedShape = SketchGlassShape(
        nativePath, topStart, topEnd, bottomEnd, bottomStart,
    )
}

/** Return the real FlClash radius, not the fallback 1000dp capsule radius. */
internal fun sketchOpticalCornerPx(
    outline: GlassOutlineRect?,
    morph: GlassMorphShape?,
    cornerDp: Float,
    density: Float,
): Float = outline?.radius ?: morph?.radius ?: cornerDp * density

/**
 * Actual LiquidGlass Sketch drawing pipeline, not a bitmap approximation.
 * Sketch's button and dialog presets are used deliberately; do not claim
 * these optical numbers are the original FlClash or Figma specifications.
 * The pane draws one material pass. FlClash's separate highlight is applied
 * on that result, without duplicating any Sketch material/refraction.
 */
@androidx.annotation.RequiresApi(33)
@Composable
internal fun SketchGlassPane(
    modifier: Modifier,
    backdrop: Backdrop,
    style: GlassStyle,
    cornerDp: Float,
    pills: List<GlassPill>,
    blendsDp: List<Float>,
    morph: GlassMorphShape?,
    outline: GlassOutlineRect?,
    glow: Offset,
    glowStrength: Float,
    glowReachDp: Float? = null,
    glowWash: Float = 0f,
) {
    val density = LocalDensity.current.density
    val materialCache = LocalSketchMaterialCache.current ?: remember { SketchMaterialCache() }
    // Geometry cache belongs to THIS composed pane, never to Sketch or a
    // global bitmap cache. Reuse unchanged neighbor contours during presses.
    val gooRowCache = remember(density) { GooRowPathCache() }
    // Glass optics belong to Sketch; do not install FlClash material shaders.
    // Only shape clipping and rim/highlight are supplied by FlClash.
    val path = remember(morph, outline, pills, blendsDp, density) {
        when {
            morph != null -> morph.nativePath ?: gooPairOutline(
                morph.platter, morph.radius, morph.droplet, morph.blend,
                density = density, coarse = false,
                platterCornerPower = morph.cornerPower,
            ).outline
            outline != null -> outline.nativePath ?: gooPlatterOutline(
                outline.bounds, outline.radius, outline.cornerPower,
            )
            pills.isNotEmpty() -> gooRowOutline(pills, blendsDp, density, gooRowCache)
            else -> null
        }
    }
    // Submenu callers use the default cornerDp=1000f while supplying an
    // outline with a ~24dp real radius. Passing 1000dp to Sketch lens
    // incorrectly treats the menu as a capsule. Use the actual outline.
    val actualRadiusPx = sketchOpticalCornerPx(outline, morph, cornerDp, density)
    // The inner Sketch shape is rectangular; only its optical radii vary.
    // The live outer clip owns the contour, so moving Goo does not recreate
    // Sketch's shape/effect/layout machinery.
    val shape = remember(actualRadiusPx, density) {
        SketchGlassShape(null, CornerSize((actualRadiusPx / density).dp))
    }
    // drawWithCache reads snapshot state so a new Goo path invalidates its
    // clip immediately even when the Canvas constraints never change.
    val livePathState = rememberUpdatedState(path)
    val menu = style.menuRimShine > .001f || outline != null ||
        (morph != null && morph.platter.width > 100f * density)
    val flClashRim = rememberFlClashControlHighlight(
        style, actualRadiusPx / density, pills, blendsDp, morph, outline,
        path, glow, glowStrength, glowReachDp, glowWash,
    )
    var paneSize by remember { mutableStateOf(IntSize.Zero) }
    Box(modifier) {
    Canvas(
        Modifier.fillMaxSize()
            .onSizeChanged { paneSize = it }
            // Dynamic clip MUST be outside Sketch's cached layout layer.
            // It also trims RenderEffect overshoot and stale right-edge
            // shadows while Goo is moving or buttons spring back.
            .drawWithCache {
                val liveContour = livePathState.value ?: gooPlatterOutline(
                    Rect(0f, 0f, size.width, size.height),
                    min(actualRadiusPx, min(size.width, size.height) * .5f),
                )
                val livePath = liveContour.asComposePath()
                onDrawWithContent {
                    clipPath(livePath) { this@onDrawWithContent.drawContent() }
                }
            }
            .then(flClashRim)
            .drawBackdrop(
            backdrop = backdrop,
            shape = { shape },
            effects = {
                // Native Sketch material only: one blur + one Sketch lens,
                // then Sketch's own color processing. Do not layer a custom
                // FlClash haze, dispersion, contrast or refraction shader.
                materialCache.apply(this, menu, actualRadiusPx)
            },
            // Optical material only. No Sketch highlight: retain FlClash.
            highlight = null,
            // Host owns any outer shadow, so there is no duplicate shadow.
            shadow = null,
            onDrawSurface = {
                // Only a single translucent color overlay, not FlClash's
                // separate material shader and never Sketch Highlight.
                if (style.tint.alpha > 0f) drawRect(style.tint)
            },
        )
    ) { /* Sketch draws the material; FlClash owns all rim lighting. */ }
    // Root and submenus draw FlClash's native menu rim in their own layer.
    // Alerts have no host rim, so preserve it here exactly once.
    if (style.menuRimShine > .001f && outline == null && morph == null) {
        GlassMenuRim(
            modifier = Modifier.fillMaxSize(),
            platter = Rect(0f, 0f, paneSize.width.toFloat(), paneSize.height.toFloat()),
            radius = min(cornerDp * density,
                min(paneSize.width, paneSize.height) * .5f),
            style = style,
            settled = 1f,
        )
    }
    }
}
