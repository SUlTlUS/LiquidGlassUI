package dev.liquidglass27.glass

import android.graphics.RectF
import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.RuntimeShader
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.withContext
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.min

/** A continuous platter + source droplet, all dimensions local physical pixels. */
data class GlassMorphShape(
    val platter: Rect,
    val radius: Float,
    val droplet: Rect,
    val blend: Float,
    /** 3.5 while a menu, smoothly converging to 2 at its source control. */
    val cornerPower: Float = 3.5f,
    /** Optional local-space contour shared with the owning route's shadow. */
    val nativePath: android.graphics.Path? = null,
)

/** Draw a changing card silhouette without relaying out its Canvas each frame. */
data class GlassOutlineRect(
    val bounds: Rect,
    val radius: Float,
    // Supplied by the owning menu so glass, rim and content share ONE
    // identical path even while the submenu springs between shapes.
    val nativePath: android.graphics.Path? = null,
    val cornerPower: Float = 3.5f,
)

/** The shader's pane rectangle lives in the bounds of this composable. */
@androidx.annotation.RequiresApi(33)
@Composable
fun GlassPane(
    modifier: Modifier = Modifier,
    style: GlassStyle,
    cornerDp: Float = 1000f,
    pills: List<GlassPill> = emptyList(),
    blendsDp: List<Float> = emptyList(),
    glow: Offset = Offset.Unspecified,
    glowStrength: Float = 0f,
    morph: GlassMorphShape? = null,
    outline: GlassOutlineRect? = null,
    /** Lets a menu draw its native rim only when the AGSL pane falls back. */
    onFallbackChanged: ((Boolean) -> Unit)? = null,
    /** Scene + previous menu levels; never includes this pane or later levels. */
    sketchBackdropOverride: com.zeaze.tianyinwallpaper.backdrop.Backdrop? = null,
    /** Panes in one group do not sample each other in the capture planner. */
    captureGroup: Any? = null,
    /** Only for opt-in GPU capture, not the visible pane opacity. */
    captureAlpha: Float = 1f,
    glowReachDp: Float? = null,
    glowWash: Float = 0f,
) {
    val sketchBackdrop = sketchBackdropOverride ?: LocalSketchGlassBackdrop.current
    if (sketchBackdrop != null) {
        // Sketch provides only backdrop/optics. The original FlClash menu
        // fallback rim stays enabled; ordinary rim uses its own shader pass.
        SideEffect { onFallbackChanged?.invoke(true) }
        SketchGlassPane(
            modifier, sketchBackdrop, style, cornerDp,
            pills, blendsDp, morph, outline, glow, glowStrength, glowReachDp, glowWash,
        )
        return
    }
    val frame = LocalGlassFrame.current
    val captureRegistry = LocalGlassCaptureRegistry.current
    val captureRoots = LocalGlassCaptureRoots.current
    val captureId = remember { Any() }
    val captureLayer = if (captureRegistry != null) rememberGraphicsLayer() else null
    if (captureRegistry != null) {
        DisposableEffect(captureRegistry, captureId) {
            onDispose { captureRegistry.remove(captureId) }
        }
    }
    val quality = LocalGlassQuality.current
    val veil = LocalGlassVeil.current
    val context = LocalContext.current
    val density = LocalDensity.current.density
    val shader = remember(context) {
        runCatching { GlassShaderPrograms.obtain(context, GlassShaderPrograms.Material) }.onFailure {
            Log.e("LiquidGlass27", "AGSL compilation failed", it)
        }.getOrNull()
    }
    val nativePaint = remember { Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG) }
    // A regular FlClash control lights its rim INSIDE the fragment shader.
    // Menus have a separate native nine-band rim, but ordinary controls do
    // not. Preserve the source control's rim equation in the no-AGSL path.
    val controlFallback = remember { GlassControlFallback() }
    // CPU geometry matches FlClash's path-union / adaptive bridge exactly;
    // AGSL shades the same silhouette, and both drawing paths clip to it.
    val goo = remember(morph, density, quality) {
        morph?.let { shape ->
            gooPairOutline(
                shape.platter, shape.radius, shape.droplet, shape.blend,
                density = density, coarse = quality == GlassQuality.Fast,
                platterCornerPower = shape.cornerPower,
            )
        }
    }
    val outlinePath = remember(outline) {
        outline?.let {
            it.nativePath ?: gooPlatterOutline(it.bounds, it.radius, it.cornerPower)
        }
    }
    // FlClash outlined-pane shader reads 64 radii of the REAL CPU Path,
    // not an analytic roundedBox that approximates its corners. For a Goo
    // morph enable this only if the union is star-shaped: early split lobes
    // can intersect a ray more than once and must keep Goo's SDF normals.
    val radialProfile = remember(goo, outlinePath, outline) {
        val path = goo?.outline ?: outlinePath
        if (path == null) null else {
            val center = if (goo == null && outline != null) {
                outline.bounds.center
            } else {
                val bounds = RectF()
                path.computeBounds(bounds, true)
                Offset(bounds.centerX(), bounds.centerY())
            }
            liquidGlassRadialProfile(path, center)
        }
    }
    // Only disable a RuntimeShader for the particular backdrop generation
    // on which a uniform/draw call failed. Previously a single transient
    // failure permanently disabled EVERY future frame of that control, even
    // after the scene was recaptured and valid inputs became available.
    var failedBackdrop by remember(shader) {
        mutableStateOf<GlassBackdropTextures?>(null)
    }
    var failedGpuAtlas by remember(shader) { mutableStateOf<androidx.compose.ui.graphics.ImageBitmap?>(null) }
    var origin by remember { mutableStateOf(Offset.Zero) }
    var captureBounds by remember { mutableStateOf(Rect.Zero) }
    var captureTransform by remember { mutableStateOf(GlassCaptureAffine()) }
    var paneSize by remember { mutableStateOf(IntSize.Zero) }
    val cornerCurves = remember(paneSize, cornerDp, density) {
        val width = paneSize.width.toFloat()
        val height = paneSize.height.toFloat()
        flutterUniformCornerCurves(
            width, height,
            min(cornerDp * density, min(width, height) * .5f),
        )
    }
    var asyncControlImage by remember { mutableStateOf<Bitmap?>(null) }
    val fallbackRequests = remember { Channel<GooFallbackRequest>(Channel.CONFLATED) }

    val backdrop = frame.textures
    val currentPlan = frame.liveCapturePlan
    val currentVersion = currentPlan?.backdropLayer?.version
    val currentRawBand = currentPlan?.bands?.firstOrNull { band ->
        band.key.kind == GlassCaptureBandKind.Raw &&
            band.tiles.any { it.paneId === captureId }
    }
    val currentFrostKeys = currentPlan?.bands?.filter { band ->
        band.key.kind != GlassCaptureBandKind.Raw &&
            band.tiles.any { it.paneId === captureId }
    }?.map { it.key }
    val residentPair = frame.gpuResidentAtlases.values.firstNotNullOfOrNull { atlas ->
        if (atlas.backgroundVersion != currentVersion ||
            atlas.image === failedGpuAtlas
        ) null else atlas.paneSamples[captureId]?.takeIf {
            currentRawBand != null && it.sourceKey == currentRawBand.key &&
                currentFrostKeys == it.frostKeys
        }?.let { atlas to it }
    }
    val residentAtlas = residentPair?.first
    val residentSamples = residentPair?.second
    val shaderDisabled = backdrop != null && failedBackdrop === backdrop &&
        residentAtlas == null
    // A hardware GPU atlas may take over without creating any CPU pixels.
    // The previous one-texture path remains the safe fallback.
    val androidImage = residentAtlas?.shader ?: backdrop?.combinedAtlas?.shader
        ?: backdrop?.shader
    val usesFallback = shaderDisabled || shader == null || androidImage == null
    val asyncGooFallback = usesFallback && pills.isNotEmpty() &&
        morph == null && outline == null && style.menuRimShine == 0f && style.rim > 0f
    var softwareBackdrop by remember { mutableStateOf<GlassBackdropPixels?>(null) }
    LaunchedEffect(fallbackRequests) {
        withContext(Dispatchers.Default) {
            for (request in fallbackRequests) {
                val result = try {
                    controlFallback.render(
                        request.size.width, request.size.height, request.style,
                        request.radius, request.cornerPower, request.density,
                        request.pills, request.blendsDp, request.backdrop,
                        request.origin,
                    )
                } catch (failure: RuntimeException) {
                    Log.w("LiquidGlass27", "Async control rim fallback failed", failure)
                    null
                }
                withContext(Dispatchers.Main.immediate) {
                    asyncControlImage = result
                }
            }
        }
    }
    LaunchedEffect(usesFallback, backdrop) {
        softwareBackdrop = null
        if (usesFallback && backdrop != null) {
            // Expensive GPU -> CPU conversion runs only when advanced AGSL
            // failed, on a background dispatcher. All panes share the
            // resulting source pixels through GlassBackdropTextures.
            softwareBackdrop = try {
                withContext(Dispatchers.Default) {
                    backdrop.pixelSnapshot()
                }
            } catch (failure: RuntimeException) {
                Log.w("LiquidGlass27", "Control fallback backdrop unavailable", failure)
                null
            }
        }
    }
    // FlClash GooSurface._outlineOf: both the CPU clip and the shader see
    // the same pills/blends. The actual path is refreshed only when their
    // geometry changes, never substituted by detached fallback rectangles.
    val gooRowPath = remember(pills, blendsDp, density) {
        if (pills.isNotEmpty()) gooRowOutline(pills, blendsDp, density)
        else null
    }
    SideEffect {
        if (captureRegistry != null && paneSize.width > 0 && paneSize.height > 0) {
            captureRegistry.upsert(
                GlassCapturePane(
                    id = captureId,
                    bounds = captureBounds,
                    transformScale = maxOf(
                        captureBounds.width / paneSize.width,
                        captureBounds.height / paneSize.height,
                    ).coerceAtLeast(0f),
                    sceneTransform = captureTransform,
                    blurSigmaPx = style.blur * density,
                    rimBlurSigmaPx = style.rimBlur * density,
                    refractionPx = style.refraction * density,
                    dispersionPx = style.dispersion * density,
                    hazeSigmaPx = if (style.haze > 0f) style.hazeBlur * density else 0f,
                    group = captureGroup,
                    captureAlpha = captureAlpha,
                    visualKey = listOf(
                        style, cornerDp, pills, blendsDp, glow,
                        glowStrength, morph, outline, quality, glowReachDp, glowWash,
                    ),
                    captureRoots = captureRoots,
                    renderLayer = captureLayer,
                )
            )
        }
        onFallbackChanged?.invoke(usesFallback)
        if (asyncGooFallback && paneSize.width > 0 && paneSize.height > 0) {
            fallbackRequests.trySend(
                GooFallbackRequest(
                    paneSize, style,
                    min(cornerDp * density,
                        min(paneSize.width, paneSize.height) * .5f),
                    density, pills.toList(), blendsDp.toList(),
                    softwareBackdrop, origin,
                )
            )
        }
    }
    val fallbackReason = when {
        shader == null -> "AGSL compile rejected"
        shaderDisabled -> "AGSL draw/uniform error"
        androidImage == null -> "backdrop capture not ready"
        else -> null
    }
    LaunchedEffect(fallbackReason, style.rim, style.menuRimShine) {
        if (fallbackReason != null && style.rim > 0f &&
            style.menuRimShine == 0f
        ) {
            Log.w(
                "LiquidGlass27",
                "Control rim using original-formula native fallback: $fallbackReason",
            )
        }
    }

    // Only the opt-in planner records a native GPU picture for this pane.
    // Replaying it is visually identical to drawContent; it is not a
    // readback or a second refracting copy of the underlying glass.
    val paneModifier = if (captureRegistry != null && captureLayer != null) {
        modifier.drawWithContent {
            captureLayer.record { this@drawWithContent.drawContent() }
            captureRegistry.painted(
                captureId, captureLayer,
                residentAtlas?.image ?: backdrop?.image,
            )
            drawLayer(captureLayer)
        }
    } else modifier
    Canvas(
        paneModifier.onSizeChanged { if (paneSize != it) paneSize = it }
            .onGloballyPositioned {
                val next = it.positionInRoot()
                if (next != origin) origin = next
                if (captureRegistry != null) {
                    val drawn = it.boundsInRoot()
                    if (drawn != captureBounds) captureBounds = drawn
                    if (it.size.width > 0 && it.size.height > 0) {
                        val top = it.localToRoot(Offset.Zero)
                        val x = it.localToRoot(Offset(it.size.width.toFloat(), 0f))
                        val y = it.localToRoot(Offset(0f, it.size.height.toFloat()))
                        val matrix = GlassCaptureAffine.fromPoints(
                            top, x, y, it.size.width.toFloat(), it.size.height.toFloat(),
                        )
                        if (captureTransform != matrix) captureTransform = matrix
                    }
                }
            },
    ) {
        if (size.width <= 0f || size.height <= 0f) return@Canvas
        val radius = min(cornerDp * density, min(size.width, size.height) * .5f)
        if (shader != null && androidImage != null && !shaderDisabled) {
            try {
            shader.setInputShader("uBackdrop", androidImage)
            val atlas = if (residentSamples == null) backdrop?.combinedAtlas else null
            val raw = residentSamples?.raw
            val rawWidth = raw?.sceneSlot?.width
                ?: backdrop?.image?.width?.toFloat() ?: 1f
            val rawHeight = raw?.sceneSlot?.height
                ?: backdrop?.image?.height?.toFloat() ?: 1f
            shader.setFloatUniform("uAtlasRawSize", rawWidth, rawHeight)
            shader.setFloatUniform(
                "uAtlasRawSlot",
                raw?.atlasRect?.left ?: 0f,
                raw?.atlasRect?.top ?: 0f,
                raw?.sceneSlot?.left ?: 0f,
                raw?.sceneSlot?.top ?: 0f,
            )
            val gpuBands = residentSamples?.frost.orEmpty()
            fun bandAt(index: Int): GlassAtlasRegion? = atlas?.regions?.getOrNull(index)
            for (index in 0..2) {
                val band = bandAt(index)
                val gpu = gpuBands.getOrNull(index)
                shader.setFloatUniform(
                    "uAtlasBand$index",
                    gpu?.atlasRect?.left ?: band?.x ?: 0f,
                    gpu?.atlasRect?.top ?: band?.y ?: 0f,
                    if (gpu != null) 1f else band?.scaleX ?: 0f,
                    if (gpu != null) 1f else band?.scaleY ?: 0f,
                )
                shader.setFloatUniform(
                    "uAtlasBandScene$index",
                    gpu?.sceneSlot?.left ?: 0f,
                    gpu?.sceneSlot?.top ?: 0f,
                    gpu?.sceneSlot?.width ?: rawWidth,
                    gpu?.sceneSlot?.height ?: rawHeight,
                )
            }
            shader.setFloatUniform(
                "uAtlasSigmas",
                gpuBands.getOrNull(0)?.sigmaPx ?: bandAt(0)?.sigmaPx ?: 0f,
                gpuBands.getOrNull(1)?.sigmaPx ?: bandAt(1)?.sigmaPx ?: 0f,
                gpuBands.getOrNull(2)?.sigmaPx ?: bandAt(2)?.sigmaPx ?: 0f,
                0f,
            )
            shader.setFloatUniform(
                "uOrigin",
                if (residentAtlas != null) captureTransform.tx else origin.x,
                if (residentAtlas != null) captureTransform.ty else origin.y,
            )
            shader.setFloatUniform(
                "uSceneBasis",
                if (residentAtlas != null) captureTransform.xx else 1f,
                if (residentAtlas != null) captureTransform.yx else 0f,
                if (residentAtlas != null) captureTransform.xy else 0f,
                if (residentAtlas != null) captureTransform.yy else 1f,
            )
            shader.setFloatUniform("uPanelSize", size.width, size.height)
            // Captured backdrop is premultiplied. The veil is straight alpha
            // and must be applied once in each optical sample branch.
            shader.setFloatUniform(
                "uVeil", veil.red, veil.green, veil.blue, veil.alpha,
            )
            shader.setIntUniform(
                "uNativeClip",
                if (goo != null || outlinePath != null || gooRowPath != null) 1 else 0,
            )
            shader.setIntUniform(
                "uProfileEnabled", if (radialProfile == null) 0 else 1,
            )
            val center = radialProfile?.center ?: Offset.Zero
            shader.setFloatUniform("uProfileCenter", center.x, center.y)
            shader.setFloatUniform(
                "uProfile", radialProfile?.radii ?: FloatArray(64),
            )
            shader.setFloatUniform(
                "uTint",
                style.tint.red,
                style.tint.green,
                style.tint.blue,
                style.tint.alpha,
            )
            shader.setFloatUniform(
                "uOptics",
                style.refraction * density,
                style.bevel * density,
                style.saturation,
                style.brightness,
            )
            shader.setFloatUniform(
                "uFrost",
                style.blur * density,
                style.haze,
                style.hazeBlur * density,
                style.dispersion * density,
            )
            shader.setFloatUniform(
                "uRim",
                style.rim,
                style.rimWidth * density,
                style.rimTail,
                style.rimFocus,
            )
            shader.setFloatUniform(
                "uRimExtra", style.rimAdded, style.rimBlur * density,
            )
            shader.setFloatUniform(
                "uMenuRim",
                style.menuRimSide, style.menuRimCorner,
                style.menuRimFlat, style.menuRimShine,
            )
            val highlightShape = outline?.bounds ?: morph?.platter ?: Rect(
                0f, 0f, size.width, size.height,
            )
            val highlightRadius = outline?.radius ?: morph?.radius ?: radius
            shader.setFloatUniform(
                "uRimBounds",
                highlightShape.center.x, highlightShape.center.y,
                highlightShape.width * .5f, highlightShape.height * .5f,
            )
            val turns = menuRimTurns(
                highlightShape.width, highlightShape.height, highlightRadius,
            )
            shader.setFloatUniform(
                "uShineTurns0", turns[0], turns[1], turns[2], turns[3],
            )
            shader.setFloatUniform(
                "uShineTurns1", turns[4], turns[5], turns[6], turns[7],
            )
            shader.setFloatUniform(
                "uShineLit0",
                style.menuRimShine,
                style.menuRimShine * .68f,
                style.menuRimShine * .48f,
                style.menuRimShine * .38f,
            )
            shader.setFloatUniform(
                "uShineLit1",
                style.menuRimShine * .17f,
                style.menuRimShine * .07f,
                style.menuRimShine * .03f,
                0f,
            )
            shader.setFloatUniform("uDensity", density)
            // FlClash uses contrast to thicken the tint selectively when
            // the background luma clashes with the pane's tint.
            shader.setFloatUniform("uContrast", style.contrast)
            shader.setFloatUniform(
                "uContrastRegion",
                highlightShape.center.x, highlightShape.center.y,
                highlightShape.width, highlightShape.height,
            )
            shader.setFloatUniform(
                "uEdge",
                style.hairline,
                style.hairlineSide,
                style.endShade,
                0f,
            )
            shader.setFloatUniform(
                "uGlow",
                if (glow != Offset.Unspecified) glow.x else size.width * .5f,
                if (glow != Offset.Unspecified) glow.y else size.height * .5f,
                glowStrength,
                glowReachDp?.times(density) ?: min(size.width, size.height) * .7f,
            )
            shader.setFloatUniform("uGlowWash", glowWash)
            shader.setFloatUniform("uRadius", radius)
            shader.setFloatUniform(
                "uCornerAlong", cornerCurves[0], cornerCurves[1],
                cornerCurves[2], cornerCurves[3],
            )
            shader.setFloatUniform(
                "uCornerAcross", cornerCurves[4], cornerCurves[5],
                cornerCurves[6], cornerCurves[7],
            )
            shader.setFloatUniform(
                "uCornerCenters", cornerCurves[8], cornerCurves[9],
                cornerCurves[10], cornerCurves[11],
            )
            shader.setIntUniform("uMorphEnabled", if (morph == null) 0 else 1)
            shader.setIntUniform("uOutlineEnabled", if (outline == null) 0 else 1)
            val outlineBounds = outline?.bounds ?: Rect.Zero
            shader.setFloatUniform(
                "uOutlineRect",
                outlineBounds.center.x, outlineBounds.center.y,
                outlineBounds.width * .5f, outlineBounds.height * .5f,
            )
            shader.setFloatUniform("uOutlineRadius", outline?.radius ?: 0f)
            val panel = morph?.platter ?: Rect.Zero
            val drop = morph?.droplet ?: Rect.Zero
            shader.setFloatUniform(
                "uMorphPlatter",
                panel.center.x, panel.center.y,
                panel.width * .5f, panel.height * .5f,
            )
            shader.setFloatUniform(
                "uMorphDroplet",
                drop.center.x, drop.center.y,
                drop.width * .5f, drop.height * .5f,
            )
            shader.setFloatUniform("uMorphSpec", morph?.radius ?: 0f, morph?.blend ?: 0f)
            shader.setFloatUniform(
                "uCornerPower",
                if (morph != null) morph.cornerPower
                else if (outline != null) outline.cornerPower
                else if (radius >= min(size.width, size.height) * .5f - .01f)
                    2f else 3.5f,
            )
            shader.setFloatUniform("uMode", if (pills.isEmpty()) 0f else 1f)
            shader.setIntUniform("uCount", min(6, pills.size))
            shader.setIntUniform("uFrostTaps", quality.frostTaps)
            val safe = pills.take(6)
            val empty = GlassPill(0f, 0f, 0f, 0f)
            for (index in 0..5) {
                val pill = safe.getOrNull(index) ?: empty
                shader.setFloatUniform(
                    "uPill$index",
                    pill.centerX, pill.centerY,
                    pill.halfWidth, pill.halfHeight,
                )
            }
            shader.setFloatUniform(
                "uBlends",
                (blendsDp.getOrNull(0) ?: 0f) * density,
                (blendsDp.getOrNull(1) ?: 0f) * density,
                (blendsDp.getOrNull(2) ?: 0f) * density,
                (blendsDp.getOrNull(3) ?: 0f) * density,
            )
            shader.setFloatUniform("uBlendLast", (blendsDp.getOrNull(4) ?: 0f) * density)
            nativePaint.shader = shader
            drawIntoCanvas { canvas ->
                val native = canvas.nativeCanvas
                val clip = goo?.outline ?: outlinePath ?: gooRowPath
                if (clip != null) {
                    val save = native.save()
                    native.clipPath(clip)
                    native.drawRect(0f, 0f, size.width, size.height, nativePaint)
                    native.restoreToCount(save)
                } else {
                    native.drawRect(0f, 0f, size.width, size.height, nativePaint)
                }
            }
            } catch (failure: RuntimeException) {
                if (residentAtlas != null) {
                    failedGpuAtlas = residentAtlas.image
                } else {
                    failedBackdrop = backdrop
                }
                Log.e(
                    "LiquidGlass27",
                    "AGSL rendering failed for current source; " +
                        "retry after atlas/background replacement",
                    failure,
                )
            }
        }
        if (shaderDisabled || shader == null || androidImage == null) {
            // FlClash's control highlights are part of liquid_glass.frag,
            // while menu highlights are a distinct LiquidGlassRim. When
            // the complex RuntimeShader cannot draw, keep the normal rim
            // mathematically consistent with that original control formula.
            // The source Goo outline still owns the exact clip/neck.
            val cornerPower = if (radius >=
                min(size.width, size.height) * .5f - .01f
            ) 2f else 3.5f
            val controlImage = if (asyncGooFallback) {
                asyncControlImage?.takeIf {
                    it.width == size.width.toInt() && it.height == size.height.toInt()
                }
            } else if (goo == null && outline == null &&
                style.menuRimShine == 0f && style.rim > 0f
            ) {
                try {
                    controlFallback.render(
                        size.width.toInt(), size.height.toInt(),
                        style, radius, cornerPower, density, pills, blendsDp,
                        softwareBackdrop, origin,
                    )
                } catch (failure: RuntimeException) {
                    Log.w("LiquidGlass27", "Native control rim fallback failed", failure)
                    null
                }
            } else null
            nativePaint.shader = null
            nativePaint.color = style.tint.toArgb()
            drawIntoCanvas { canvas ->
                val native = canvas.nativeCanvas
                val path = goo?.outline ?: outlinePath ?: gooRowPath
                if (controlImage != null) {
                    val save = native.save()
                    if (path != null) native.clipPath(path)
                    nativePaint.color = android.graphics.Color.WHITE
                    native.drawBitmap(controlImage, 0f, 0f, nativePaint)
                    native.restoreToCount(save)
                } else if (path != null) {
                    native.drawPath(path, nativePaint)
                } else if (pills.isEmpty()) {
                    native.drawRoundRect(
                        0f, 0f, size.width, size.height,
                        radius, radius, nativePaint,
                    )
                }
            }
        }
    }
}

private data class GooFallbackRequest(
    val size: IntSize,
    val style: GlassStyle,
    val radius: Float,
    val density: Float,
    val pills: List<GlassPill>,
    val blendsDp: List<Float>,
    val backdrop: GlassBackdropPixels?,
    val origin: Offset,
) {
    val cornerPower: Float =
        if (radius >= min(size.width, size.height) * .5f - .01f) 2f else 3.5f
}

/**
 * Same eight polar stops as FlClash's LiquidGlassRim._turns.
 * The first ray is upward, the last toward the right side. The two axes
 * mirror those stops across all four quadrants in AGSL.
 */
internal fun menuRimTurns(width: Float, height: Float, radius: Float): FloatArray {
    val halfWidth = (width * .5f).coerceAtLeast(.001f)
    val halfHeight = (height * .5f).coerceAtLeast(.001f)
    val round = radius.coerceIn(0f, min(halfWidth, halfHeight))
    val along = floatArrayOf(1.53f, 1f, .3f, 0f, 0f, 0f)
    val down = floatArrayOf(0f, 0f, .3f, 1f, 1.5f, 2f)
    val stops = FloatArray(8)
    stops[0] = 0f
    for (i in 0 until 6) {
        val x = (halfWidth - min(along[i] * round, halfWidth)).coerceAtLeast(0f)
        val y = (halfHeight - min(down[i] * round, halfHeight)).coerceAtLeast(0f)
        stops[i + 1] = maxOf(stops[i], atan2(x, y))
    }
    stops[7] = (PI * .5).toFloat()
    return stops
}

/** Source-level equivalent to FlClash's LiquidGlass widget. */
@androidx.annotation.RequiresApi(33)
@Composable
fun LiquidGlass(
    modifier: Modifier = Modifier,
    style: GlassStyle,
    cornerDp: Float = 1000f,
    content: @Composable androidx.compose.foundation.layout.BoxScope.() -> Unit,
) {
    androidx.compose.foundation.layout.Box(modifier) {
        GlassPane(Modifier.matchParentSize(), style, cornerDp)
        GlassCaptureBehind(content = content)
    }
}
