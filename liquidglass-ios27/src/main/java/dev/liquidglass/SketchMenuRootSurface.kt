package dev.liquidglass

import android.graphics.Path
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntSize
import com.zeaze.tianyinwallpaper.backdrop.Backdrop
import com.zeaze.tianyinwallpaper.backdrop.drawBackdrop
import dev.liquidglass.rendering.drawGlassHighlight
import dev.liquidglass.rendering.drawGlassShadow
import com.zeaze.tianyinwallpaper.backdrop.shadow.Shadow
import kotlin.math.roundToInt

internal data class MenuRootMotion(
    val frame: MenuMorphFrame,
    val active: Boolean,
    val power: Float,
    val returningToSource: Boolean,
)

internal fun menuRootCornerPower(settled: Float, returning: Boolean, closeTime: Float): Float {
    fun ease(t: Float): Float {
        val x = t.coerceIn(0f, 1f)
        return x * x * (3f - 2f * x)
    }
    val start = 2f + (3.5f - 2f) * ease(settled / .25f)
    return if (returning) start + (2f - start) * ease((closeTime / .5f - .50f) / .38f) else start
}

internal fun menuRootSampleBounds(frame: MenuMorphFrame, viewport: Size, density: Float): Rect {
    val p = frame.platter
    val d = frame.droplet
    val pad = 36f * density
    return Rect(
        (minOf(p.left, d.left) - pad).coerceAtLeast(0f),
        (minOf(p.top, d.top) - pad).coerceAtLeast(0f),
        (maxOf(p.right, d.right) + pad).coerceAtMost(viewport.width),
        (maxOf(p.bottom, d.bottom) + pad).coerceAtMost(viewport.height),
    )
}

/** One frame shared by measurement, shadow, material clip and rim. */
private class MenuRootGeometry(
    val motion: MenuRootMotion, card: Rect, viewport: Size, density: Float, dark: Boolean,
) {
    val bounds = menuRootSampleBounds(motion.frame, viewport, density)
    val radius = if (motion.active) motion.frame.radius else 24f * density
    val surface = if (motion.active) motion.frame.platter else card
    val shadowPath = if (motion.active) gooPairOutline(
        surface, radius, motion.frame.droplet, motion.frame.blend,
        density = density, coarse = false, platterCornerPower = motion.power,
    ).outline else gooPlatterOutline(surface, radius, motion.power)
    val localSurface = surface.translate(-bounds.left, -bounds.top)
    val localPath = if (motion.active) Path(shadowPath).apply {
        offset(-bounds.left, -bounds.top)
    } else gooPlatterOutline(localSurface, radius)
    val highlightShape = menuHighlightShape(localPath)
    val material = GlassStyle.interpolate(GlassStyle.regular(dark), GlassStyle.menu(dark), motion.frame.settled)
    val controlMaterial = material.copy(menuRimSide = 0f, menuRimCorner = 0f,
        menuRimFlat = 0f, menuRimShine = 0f)
    val morph = if (motion.active) GlassMorphShape(localSurface, radius,
        motion.frame.droplet.translate(-bounds.left, -bounds.top), motion.frame.blend,
        motion.power, localPath) else null
    val outline = if (motion.active) null else GlassOutlineRect(localSurface, radius, localPath)
    val menu = !motion.active || motion.frame.platter.width > 100f * density
    val rimStrength = menuRootRimStrength(motion.frame.settled,
        if (motion.active) (gooPairDepth(motion.frame.platter, motion.frame.radius,
            motion.frame.droplet, motion.frame.blend) / (8f * density)).coerceIn(0f, 1f) else 1f,
        motion.returningToSource)
}

/** Animation reads belong to draw/measure; the menu's content stays composed. */
@androidx.annotation.RequiresApi(33)
@Composable
internal fun SketchMenuRootSurface(
    motion: State<MenuRootMotion>, card: Rect, viewport: Size, density: Float,
    dark: Boolean, backdrop: Backdrop, receding: Boolean, distance: Float,
    route: MenuRouteState,
    sourceShadow: Shadow? = null,
) {
    val geometry = remember(motion, card, viewport, density, dark) {
        var previous: MenuRootGeometry? = null
        {
            val next = motion.value
            previous?.takeIf { it.motion == next } ?: MenuRootGeometry(
                next, card, viewport, density, dark,
            ).also { previous = it }
        }
    }
    val shadow = remember { MenuShadowPaintState() }
    val ios26 = LocalGlassMenuHighlight.current == GlassMenuHighlight.Ios26
    val tintSurface = LocalGlassMenuSurfaceTint.current
    val rim = if (!ios26) remember { GlassMenuRimRenderer() } else null
    val context = LocalContext.current
    val highlight = if (!ios26) remember(context) { ControlHighlightRenderer(context) } else null
    val materials = LocalSketchMaterialCache.current ?: remember { SketchMaterialCache() }
    val shape = remember(geometry, density) {
        SketchGlassShape(null, object : CornerSize {
            override fun toPx(shapeSize: Size, density: Density): Float =
                geometry().radius / density.density * density.density
        })
    }
    Box(Modifier.fillMaxSize()) {
        Canvas(Modifier.fillMaxSize()) {
            val g = geometry()
            route.lastRenderedFrame = g.motion.frame
            val strength = menuCardShadowStrength(g.motion.frame.settled, receding, distance)
            val shade = (.118f * (if (dark) 1.5f else 1f) * strength).coerceIn(0f, 1f)
            if (shade > 0f) {
                shadow.configure(density, android.graphics.Color.argb((255f * shade).toInt(), 0, 0, 0))
                drawIntoCanvas {
                    val native = it.nativeCanvas
                    val saved = native.save()
                    if (g.motion.active) native.clipPath(shadow.exteriorClipOf(g.shadowPath, density))
                    native.drawPath(g.shadowPath, shadow.paint)
                    native.restoreToCount(saved)
                }
            }
        }
        Box(Modifier.layout { measurable, constraints ->
            val bounds = geometry().bounds
            val child = measurable.measure(Constraints.fixed(
                bounds.width.roundToInt().coerceAtLeast(0), bounds.height.roundToInt().coerceAtLeast(0)))
            layout(constraints.maxWidth, constraints.maxHeight) {
                child.place(bounds.left.roundToInt(), bounds.top.roundToInt())
            }
        }) {
            Canvas(Modifier.fillMaxSize()
                // The host button is hidden throughout the morph. Carry its original shadow
                // outside the material clip until the menu shadow has taken over (and back).
                .then(if (sourceShadow == null) Modifier else Modifier.drawGlassShadow(
                    shape = { geometry().highlightShape },
                    shadow = {
                        val share = 1f - geometry().motion.frame.settled.coerceIn(0f, 1f)
                        if (share <= 0f) null else sourceShadow.copy(alpha = sourceShadow.alpha * share)
                    },
                ))
                .drawWithCache {
                    val clip = geometry().localPath.asComposePath()
                    onDrawWithContent { clipPath(clip) { this@onDrawWithContent.drawContent() } }
                }
                .then(if (ios26) Modifier.drawGlassHighlight(shape = { geometry().highlightShape })
                else Modifier.graphicsLayer {
                    val g = geometry()
                    renderEffect = highlight!!.effect(ControlHighlightInput(
                        g.controlMaterial, g.radius / density, emptyList(), emptyList(),
                        g.morph, g.outline, g.localPath, Offset.Unspecified, 0f, null, 0f,
                        density, IntSize(size.width.toInt(), size.height.toInt()),
                    ))
                })
                .drawBackdrop(backdrop, shape = { shape }, effects = {
                    val g = geometry()
                    materials.apply(this, g.menu, g.radius)
                }, highlight = null, shadow = null, onDrawSurface = {
                    val tint = geometry().material.tint
                    if (tintSurface && tint.alpha > 0f) drawRect(tint)
                }),
            ) {}
            if (rim != null) Canvas(Modifier.fillMaxSize()) {
                val g = geometry()
                drawIntoCanvas {
                    rim.draw(it.nativeCanvas, size.width, size.height, g.localSurface,
                        g.radius, g.material, g.rimStrength, density, cornerPower = g.motion.power)
                }
            }
        }
    }
}
