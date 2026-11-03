package dev.liquidglass

import android.graphics.Path
import android.graphics.RectF
import android.graphics.RenderEffect
import android.graphics.RuntimeShader
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Constraints
import com.zeaze.tianyinwallpaper.backdrop.drawBackdrop
import com.zeaze.tianyinwallpaper.backdrop.effects.*
import dev.liquidglass.rendering.*
import kotlin.math.*

private data class OriginalButtonTransform(
    val scaleX: Float = 1f, val scaleY: Float = 1f,
    val x: Float = 0f, val y: Float = 0f, val press: Float = 0f,
)

private class FusionButton(val material: LiquidButtonFusionMaterial) {
    var bounds by mutableStateOf(Rect.Zero)
    var transform by mutableStateOf(OriginalButtonTransform())
    var coordinates: LayoutCoordinates? = null
    fun painted(): Rect {
        val t = transform
        val center = bounds.center
        val w = bounds.width * t.scaleX / 2f
        val h = bounds.height * t.scaleY / 2f
        return Rect(center.x + t.x - w, center.y + t.y - h,
            center.x + t.x + w, center.y + t.y + h)
    }
    fun pill(): GlassPill {
        val rect = painted()
        return GlassPill(rect.center.x, rect.center.y, rect.width / 2f, rect.height / 2f,
            material.buttonEffect, bounds.width / 2f, bounds.height / 2f)
    }
}

private class FusionRegistry {
    val buttons = mutableStateListOf<FusionButton>()
    var coordinates: LayoutCoordinates? = null
    fun locate(button: FusionButton) {
        val parent = coordinates?.takeIf { it.isAttached } ?: return
        val child = button.coordinates?.takeIf { it.isAttached } ?: return
        button.bounds = parent.localBoundingBoxOf(child, clipBounds = false)
    }
}

/** Original iOS26 buttons draw themselves. Only newly formed necks are painted above them. */
@RequiresApi(33)
@Composable
fun Ios26FusionGroup(modifier: Modifier = Modifier, content: @Composable BoxScope.() -> Unit) {
    val registry = remember { FusionRegistry() }
    val density = LocalDensity.current.density
    Box(modifier.onGloballyPositioned {
        registry.coordinates = it
        registry.buttons.forEach(registry::locate)
    }) {
        CompositionLocalProvider(LocalLiquidButtonFusion provides { material ->
            val button = remember(registry, material) { FusionButton(material) }
            DisposableEffect(button) {
                registry.buttons.add(button)
                onDispose { registry.buttons.remove(button) }
            }
            remember(button) {
                LiquidButtonFusionAttachment(Modifier.onGloballyPositioned {
                    button.coordinates = it
                    registry.locate(button)
                }) { sx, sy, x, y, press ->
                    if (sx.isFinite() && sy.isFinite() && x.isFinite() && y.isFinite() &&
                        press.isFinite() && sx > 0f && sy > 0f) {
                        button.transform = OriginalButtonTransform(sx, sy, x, y, press)
                    }
                }
            }
        }) { content() }

        val buttons = registry.buttons.filter { it.bounds.width > 0 && it.bounds.height > 0 }
        val rects = buttons.map { it.painted() }
        val pills = buttons.map { it.pill() }
        // Color does not divide geometry; optics and sampling boundaries still do.
        val rows = gooButtonBatches(pills, buttons.map {
            it.material.copy(tint = Color.Unspecified, surface = Color.Unspecified, buttonEffect = null)
        })
        for (row in rows) for (pair in row.zipWithNext()) {
            val a = rects[pair.first]
            val b = rects[pair.second]
            val press = max(buttons[pair.first].transform.press, buttons[pair.second].transform.press).coerceIn(0f, 1f)
            val blend = 8f + 8f * press
            if (b.left - a.right < blend * density) {
                key(buttons[pair.first], buttons[pair.second]) {
                    val cache = remember { GooRowPathCache() }
                    val pa = pills[pair.first]
                    val pb = pills[pair.second]
                    val connection = remember(pa, pb, blend, density) { fusionConnection(pa, pb, blend, density, cache) }
                    if (connection != null) Box(Modifier.matchParentSize()) {
                        FusionNeck(buttons[pair.first].material, buttons[pair.second].material, connection, blend)
                    }
                }
            }
        }
    }
}

private class FusionConnection(
    val bounds: Rect, val outline: Path, val pills: List<GlassPill>,
)

private fun fusionConnection(a: GlassPill, b: GlassPill, blend: Float, density: Float, cache: GooRowPathCache): FusionConnection? {
    val union = gooRowOutline(listOf(a, b), listOf(blend), density, cache)
    val bodies = Path().apply {
        addPath(cache.pill(0, a) { buttonPillOutline(a) })
        addPath(cache.pill(1, b) { buttonPillOutline(b) })
    }
    val neck = Path()
    if (!neck.op(union, bodies, Path.Op.DIFFERENCE) || neck.isEmpty) return null
    val area = RectF().also { neck.computeBounds(it, true) }
    if (area.width() < .1f || area.height() < .1f) return null
    // Include the feathered attachment plus refraction lookups. No hard patch edge is painted.
    val pad = (32f + 8f) * density
    val left = floor(area.left - pad)
    val top = floor(area.top - pad)
    val bounds = Rect(left, top, ceil(area.right + pad), ceil(area.bottom + pad))
    val shiftA = a.copy(centerX = a.centerX - left, centerY = a.centerY - top)
    val shiftB = b.copy(centerX = b.centerX - left, centerY = b.centerY - top)
    union.offset(-left, -top)
    return FusionConnection(bounds, union, listOf(shiftA, shiftB))
}

@Composable
private fun FusionNeck(material: LiquidButtonFusionMaterial, other: LiquidButtonFusionMaterial,
    geometry: FusionConnection, blend: Float) {
    val context = LocalContext.current
    val density = LocalDensity.current.density
    val shader = remember(context) {
        context.assets.open("shaders/sketch_goo_lens.agsl").bufferedReader().use { it.readText() }
    }
    val feather = remember(context) {
        RuntimeShader(context.assets.open("shaders/ios26_fusion_feather.agsl").bufferedReader().use { it.readText() })
    }
    val contour = remember(geometry) { menuHighlightShape(geometry.outline) }
    val lighting = if (currentGlassHighlight() == GlassMenuHighlight.Ios26)
        Modifier.drawGlassHighlight(shape = { contour })
    else rememberFlClashControlHighlight(GlassStyle.regular(isGlassDarkTheme()), 0f,
        geometry.pills, listOf(blend), null, null, geometry.outline,
        androidx.compose.ui.geometry.Offset.Unspecified, 0f)
    val opticalShape = remember { SketchGlassShape(null, CornerSize(0f)) }
    val mixedColors = material.tint != other.tint || material.surface != other.surface
    val bodyPaths = remember(geometry, mixedColors) {
        if (!mixedColors) emptyList() else geometry.pills.map { pill ->
            buttonPillOutline(pill).asComposePath()
        }
    }
    Canvas(Modifier.layout { measurable, constraints ->
        val bounds = geometry.bounds
        val child = measurable.measure(Constraints.fixed(bounds.width.toInt(), bounds.height.toInt()))
        layout(constraints.maxWidth, constraints.maxHeight) { child.place(bounds.left.toInt(), bounds.top.toInt()) }
    }.drawWithCache {
        val outline = geometry.outline.asComposePath()
        onDrawWithContent { clipPath(outline) { this@onDrawWithContent.drawContent() } }
    }.graphicsLayer {
        val a = geometry.pills[0]
        val b = geometry.pills[1]
        feather.setFloatUniform("uPillA", a.centerX, a.centerY, a.halfWidth, a.halfHeight)
        feather.setFloatUniform("uPillB", b.centerX, b.centerY, b.halfWidth, b.halfHeight)
        fun bindScale(name: String, pill: GlassPill) {
            val stretched = usesStretchedButtonContour(pill.buttonEffect)
            feather.setFloatUniform(name, if (stretched) pill.halfWidth / pill.restHalfWidth else 1f,
                if (stretched) pill.halfHeight / pill.restHalfHeight else 1f)
        }
        bindScale("uScaleA", a)
        bindScale("uScaleB", b)
        feather.setFloatUniform("uBlend", blend * density)
        feather.setFloatUniform("uDensity", density)
        // Feather material and highlight together; neither leaves a rectangular patch edge.
        renderEffect = RenderEffect.createRuntimeShaderEffect(feather, "uContent").asComposeRenderEffect()
    }.then(lighting)
        .drawBackdrop(material.backdrop, shape = { opticalShape }, effects = {
            if (!material.style.preserveBackdropColors) {
                material.luminance?.let { colorControls(brightness = it.brightness, contrast = it.contrast, saturation = it.saturation) }
                    ?: vibrancy()
            }
            blur(material.style.blurRadius.toPx())
            // This SDF refraction is restricted to the neck. Original button lens calls stay untouched.
            gooAwareSketchLens(shader, null, null, geometry.pills, listOf(blend), density,
                cornerPower = 2f, radiusPx = 0f, refractionDp = material.style.lensRadiusY.value,
                bevelDp = material.style.lensRadiusX.value, dispersionDp = 0f, endShade = 0f)
        }, highlight = null, shadow = null, onDrawSurface = {
            fun paintColors(body: LiquidButtonFusionMaterial) {
                if (body.tint.isSpecified) {
                    drawRect(body.tint, blendMode = BlendMode.Hue)
                    drawRect(body.tint.copy(alpha = .75f))
                }
                if (body.surface.isSpecified) drawRect(body.surface)
            }
            if (!mixedColors) paintColors(material) else {
                clipPath(bodyPaths[0]) { paintColors(material) }
                clipPath(bodyPaths[1]) { paintColors(other) }
            }
        })) {}
}
