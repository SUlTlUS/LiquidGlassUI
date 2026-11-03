package dev.liquidglass27.glass

import android.util.Log
import android.graphics.BitmapShader
import android.graphics.Bitmap
import android.graphics.Shader
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import com.zeaze.tianyinwallpaper.backdrop.backdrops.layerBackdrop
import com.zeaze.tianyinwallpaper.backdrop.backdrops.rememberLayerBackdrop
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.collect
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalGraphicsContext

enum class GlassQuality(val frostTaps: Int) {
    Fast(9),
    Standard(24),
    Ultra(64),
}

val LocalGlassQuality = staticCompositionLocalOf { GlassQuality.Standard }

/**
 * A straight-alpha scrim for pixels intentionally omitted by a backdrop
 * capture. Unlike pane tint, the veil must be applied to EVERY sampled texel,
 * including the contrast probe and the hairline's underlying backdrop.
 * A full-frame opaque GraphicsLayer capture needs no veil by default.
 */
val LocalGlassVeil = staticCompositionLocalOf { Color.Transparent }

/**
 * One immutable atlas entry shared by every pane. Native BitmapShaders are
 * constructed once per captured scene, not once per control or animation
 * frame. Keep both bitmaps alive for as long as their shaders are referenced.
 */
internal data class GlassBackdropBand(
    val sigmaPx: Float,
    val image: ImageBitmap,
    val shader: BitmapShader,
)

internal data class GlassBackdropPixels(
    val width: Int,
    val height: Int,
    val pixels: IntArray,
)

internal data class GlassBackdropTextures(
    val image: ImageBitmap,
    val shader: BitmapShader,
    /** Ordered from least to most blurred, tied to the same raw capture. */
    val frostBands: List<GlassBackdropBand> = emptyList(),
    val combinedAtlas: GlassCombinedAtlas? = null,
    val capturePlan: GlassCapturePlan? = null,
) {
    // FlClash controls use the backdrop in their control shine formula.
    // Only if native fallback is needed, prepare one shared CPU view of
    // the SAME background image, outside the UI drawing callback.
    @Volatile
    private var fallbackPixels: GlassBackdropPixels? = null

    @androidx.annotation.RequiresApi(33)
    internal fun pixelSnapshot(): GlassBackdropPixels {
        fallbackPixels?.let { return it }
        return synchronized(this) {
            fallbackPixels?.let { return@synchronized it }
            val original = image.asAndroidBitmap()
            val software = if (original.config == Bitmap.Config.HARDWARE) {
                original.copy(Bitmap.Config.ARGB_8888, false)
                    ?: error("Cannot read a software backdrop snapshot")
            } else original
            try {
                val data = IntArray(software.width * software.height)
                software.getPixels(
                    data, 0, software.width, 0, 0,
                    software.width, software.height,
                )
                GlassBackdropPixels(
                    software.width, software.height, data,
                ).also { fallbackPixels = it }
            } finally {
                if (software !== original) software.recycle()
            }
        }
    }
}

@Stable
class GlassFrame {
    internal var textures: GlassBackdropTextures? by mutableStateOf(null)
    /** Current live Compose picture hierarchy; independent of backdrop age. */
    internal var liveCapturePlan: GlassCapturePlan? by mutableStateOf(null)
    /** Immutable hardware-backed atlases per glass compositing level. */
    internal var gpuResidentAtlases: Map<Int, GlassGpuResidentAtlas> by
        mutableStateOf(emptyMap())
}

internal val LocalGlassCaptureRegistry =
    staticCompositionLocalOf<GlassCaptureRegistry?> { null }

/** The pane shader sees precisely [background], never previously drawn panes or UI. */
val LocalGlassFrame = staticCompositionLocalOf<GlassFrame> {
    error("Glass controls must be wrapped in GlassScene")
}

/**
 * Captures only the scene below glass overlays.
 * A non-positive [captureIntervalMs] enables one-shot capture per
 * [backgroundKey] or size change, avoiding repeated full-screen GPU readback.
 * For static scenes [preblurSigmaDp] produces a multi-sigma shared frost
 * pyramid off the UI thread. Positive [captureIntervalMs] retains periodic
 * capture. A negative interval enables event-driven dynamic capture:
 * [backgroundKey] must change when the background content changes; its
 * absolute value is the minimum milliseconds between GPU readbacks.
 * An opt-in dynamic GPU frost path can refresh a single matched blur band
 * alongside its raw image, without ever doing repeated CPU Gaussian blur.
 */
@androidx.annotation.RequiresApi(33)
@Composable
fun GlassScene(
    modifier: Modifier = Modifier,
    captureIntervalMs: Long = 85L,
    backgroundKey: Any? = Unit,
    preblurSigmaDp: Float = 0f,
    /** Experimental: GPU RenderEffect frost atlas, off by default until verified. */
    staticGpuFrost: Boolean = false,
    dynamicGpuFrost: Boolean = false,
    /** Stages FlClash-style capture dependencies; never changes paint output. */
    capturePlannerEnabled: Boolean = false,
    /** Retains blurred/clear band images in GPU hardware memory. */
    gpuResidentAtlas: Boolean = false,
    /** Use the real LiquidGlass Sketch backdrop and optical pipeline. */
    sketchMaterial: Boolean = false,
    quality: GlassQuality = GlassQuality.Standard,
    backdropVeil: Color = Color.Transparent,
    background: @Composable BoxScope.() -> Unit,
    content: @Composable BoxScope.() -> Unit,
) {
    val layer = rememberGraphicsLayer()
    val sketchBackdrop = rememberLayerBackdrop()
    val sketchMaterials = remember { SketchMaterialCache() }
    val frostLayer = rememberGraphicsLayer()
    val frame = remember { GlassFrame() }
    val captureRegistry = remember { GlassCaptureRegistry() }
    val plannerActive = !sketchMaterial &&
        (capturePlannerEnabled || gpuResidentAtlas)
    val graphicsContext = LocalGraphicsContext.current
    val atlasCache = remember(graphicsContext) {
        GlassGpuResidentAtlasCache(graphicsContext)
    }
    DisposableEffect(atlasCache) {
        onDispose { atlasCache.clear() }
    }
    var sceneOrigin by remember { mutableStateOf(Offset.Zero) }
    val localDensity = LocalDensity.current
    val layoutDirection = LocalLayoutDirection.current
    val density = localDensity.density
    var backgroundSize by remember { mutableStateOf(IntSize.Zero) }
    // The latest key is read by the long-lived event collector, so a
    // background changing every frame cannot repeatedly cancel readbacks.
    val latestBackgroundKey = rememberUpdatedState(backgroundKey)
    val effectKey = if (captureIntervalMs < 0L) Unit else backgroundKey

    // A static backdrop is captured once, but glass panes keep animating.
    // Refresh the *planning metadata* from actual picture-record events,
    // without performing a new GPU readback or redrawing other panes.
    LaunchedEffect(plannerActive, captureRegistry, backgroundSize, sceneOrigin) {
        if (!plannerActive || backgroundSize.width <= 0 ||
            backgroundSize.height <= 0
        ) {
            frame.liveCapturePlan = null
            return@LaunchedEffect
        }
        val scene = Rect(
            sceneOrigin.x, sceneOrigin.y,
            sceneOrigin.x + backgroundSize.width,
            sceneOrigin.y + backgroundSize.height,
        )
        frame.liveCapturePlan = if (captureRegistry.hasBackground()) {
            captureRegistry.snapshot(scene, captureRegistry.backgroundVersion(), layer)
        } else null
        launch {
            snapshotFlow { latestBackgroundKey.value }.collect {
                captureRegistry.requestSnapshot()
            }
        }
        for (update in captureRegistry.changes) {
            withFrameNanos { }
            frame.liveCapturePlan = if (captureRegistry.hasBackground()) {
                captureRegistry.snapshot(
                    scene, captureRegistry.backgroundVersion(), layer,
                )
            } else null
        }
    }

    LaunchedEffect(gpuResidentAtlas, sketchMaterial, atlasCache, localDensity, layoutDirection) {
        if (!gpuResidentAtlas || sketchMaterial) {
            frame.gpuResidentAtlases = emptyMap()
            atlasCache.clear()
            return@LaunchedEffect
        }
        // Do not cancel an in-flight GPU snapshot on every spring frame.
        // Serialize snapshots and conflate obsolete plans behind the reader.
        snapshotFlow { frame.liveCapturePlan }
            .conflate()
            .collect { plan ->
                if (plan == null || plan.placements.isEmpty() ||
                    plan.backdropLayer == null
                ) {
                    // Releasing the visible map alone retains the old
                    // hardware-backed bands and source snapshot in cache.
                    // Drop those references when the last pane disappears.
                    frame.gpuResidentAtlases = emptyMap()
                    atlasCache.clear()
                    return@collect
                }
                atlasCache.beginFrame()
                // Composite level 0 from the scene before painting the
                // higher levels. A later level must sample glass from the
                // newly rendered earlier level, never from its old atlas.
                val activeLevels = plan.atlases.mapTo(HashSet()) { it.level }
                val current = LinkedHashMap(
                    frame.gpuResidentAtlases.filter { (level, atlas) ->
                        level in activeLevels &&
                            atlas.backgroundVersion == plan.backdropLayer.version
                    }
                )
                for (level in plan.atlases.sortedBy { it.level }) {
                    val built = atlasCache.build(
                        plan, localDensity, layoutDirection,
                        onlyLevel = level.level,
                    )
                    if (built.isEmpty()) continue
                    current.putAll(built)
                    // Publish completed immutable GPU levels atomically.
                    frame.gpuResidentAtlases = current.toMap()
                    // Let Compose record the new lower-level pane image
                    // before an overlapping upper-level capture reads it.
                    withFrameNanos { }
                    withFrameNanos { }
                }
                // Release tiles from dismissed menus or removed layers.
                atlasCache.prune(plan)
                if (current.isEmpty()) frame.gpuResidentAtlases = emptyMap()
            }
    }

    LaunchedEffect(
        layer, frostLayer, captureIntervalMs, effectKey, backgroundSize,
        preblurSigmaDp, staticGpuFrost, dynamicGpuFrost,
        gpuResidentAtlas, sketchMaterial, density, layoutDirection,
    ) {
        if (sketchMaterial || backgroundSize.width <= 0 || backgroundSize.height <= 0) {
            frame.textures = null
            return@LaunchedEffect
        }
        var failedCaptures = 0
        var gpuFrostFailures = 0
        val staticBackdrop = captureIntervalMs == 0L
        val dynamicFrost = captureIntervalMs < 0L && dynamicGpuFrost &&
            preblurSigmaDp * density >= 1f

        suspend fun capture(): Boolean {
            if (layer.size.width <= 0 || layer.size.height <= 0) return false
            try {
                val captured = layer.toImageBitmap()
                val base = GlassBackdropTextures(
                    image = captured,
                    shader = BitmapShader(
                        captured.asAndroidBitmap(),
                        Shader.TileMode.CLAMP,
                        Shader.TileMode.CLAMP,
                    ),
                    capturePlan = if (plannerActive && captureRegistry.hasBackground()) {
                        captureRegistry.snapshot(
                            Rect(
                                sceneOrigin.x, sceneOrigin.y,
                                sceneOrigin.x + backgroundSize.width,
                                sceneOrigin.y + backgroundSize.height,
                            ),
                            captureRegistry.backgroundVersion(),
                            layer,
                        )
                    } else null,
                )
                failedCaptures = 0
                val sigmaPx = preblurSigmaDp.coerceAtLeast(0f) * density

                if (!gpuResidentAtlas && dynamicFrost && gpuFrostFailures < 2) {
                    // Publish RAW and FROST as ONE entry from the same source.
                    // Never combine a new raw frame with the previous blur:
                    // moving content would otherwise refract out of sync.
                    val level = captureGpuFrostBand(
                        captured, sigmaPx, frostLayer,
                        localDensity, layoutDirection,
                    )
                    if (level == null) gpuFrostFailures++ else {
                        gpuFrostFailures = 0
                        val band = runCatching {
                            GlassBackdropBand(
                                sigmaPx = level.sigmaPx,
                                image = level.image,
                                shader = BitmapShader(
                                    level.image.asAndroidBitmap(),
                                    Shader.TileMode.CLAMP,
                                    Shader.TileMode.CLAMP,
                                ),
                            )
                        }.onFailure {
                            Log.w("LiquidGlass27", "GPU frost shader failed", it)
                        }.getOrNull()
                        if (band != null) {
                            val atlas = withContext(Dispatchers.Default) {
                                combineBackdropFrostAtlas(captured, listOf(band))
                            }
                            if (atlas != null) {
                                frame.textures = base.copy(
                                    frostBands = listOf(band),
                                    combinedAtlas = atlas,
                                )
                                return true
                            }
                        }
                        gpuFrostFailures++
                    }
                    // Never publish a stale frost tile alongside the latest
                    // raw frame. After repeated failures, use the original
                    // unblurred input and Vogel samples for this effect.
                    frame.textures = base
                    return true
                }
                // Clear/low-frost panes should not wait while preparing a
                // STATIC three-band pyramid. Dynamic raw captures stay cheap.
                frame.textures = base
                if (!gpuResidentAtlas && staticBackdrop && sigmaPx >= 1f) {
                    // Experimental GPU RenderEffect can be explicitly
                    // enabled. Default to background-thread CPU preblur:
                    // triple GPU readback may be slower than the original
                    // capture on some devices. Never mix atlas generations.
                    var levels = if (staticGpuFrost) {
                        captureGpuFrostPyramid(
                            captured, sigmaPx, frostLayer,
                            localDensity, layoutDirection,
                        )
                    } else emptyList()
                    if (levels.isEmpty()) {
                        // On devices where GraphicsLayer's RenderEffect
                        // readback fails, preserve the previous tested CPU
                        // background-thread implementation as a fallback.
                        levels = withContext(Dispatchers.Default) {
                            createSharedBackdropBlurLevels(captured, sigmaPx)
                        }
                    }
                    if (isActive && frame.textures === base &&
                        levels.isNotEmpty()
                    ) {
                        val bands = levels.mapNotNull { level ->
                            runCatching {
                                GlassBackdropBand(
                                    sigmaPx = level.sigmaPx,
                                    image = level.image,
                                    shader = BitmapShader(
                                        level.image.asAndroidBitmap(),
                                        Shader.TileMode.CLAMP,
                                        Shader.TileMode.CLAMP,
                                    ),
                                )
                            }.onFailure {
                                Log.w("LiquidGlass27",
                                    "Shared frost band unavailable", it)
                            }.getOrNull()
                        }
                        if (bands.isNotEmpty()) {
                            val atlas = withContext(Dispatchers.Default) {
                                combineBackdropFrostAtlas(captured, bands)
                            }
                            if (isActive && frame.textures === base) {
                                frame.textures = base.copy(
                                    frostBands = bands,
                                    combinedAtlas = atlas,
                                )
                            }
                        }
                    }
                }
                return true
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (failure: RuntimeException) {
                failedCaptures++
                if (failedCaptures >= 3) {
                    Log.w(
                        "LiquidGlass27",
                        "Backdrop capture unavailable: retaining last scene",
                        failure,
                    )
                    // Keep the most recent valid atlas for visual continuity.
                    // Invalid captures never replace a good previous frame.
                }
                return false
            }
        }

        when {
            gpuResidentAtlas && captureIntervalMs < 0L -> {
                // The GPU atlas refreshes on its own invalidated layer keys.
                // Prepare just one matching unblurred fallback per backdrop
                // revision, not a timer-driven CPU or GPU snapshot stream.
                snapshotFlow { latestBackgroundKey.value }.conflate().collect {
                    if (failedCaptures >= 3) return@collect
                    withFrameNanos { }
                    capture()
                }
            }
            gpuResidentAtlas -> {
                // Stable background: exactly one fallback readback per
                // composition/key change; band allocation is independent.
                withFrameNanos { }
                withFrameNanos { }
                repeat(3) {
                    if (capture()) return@LaunchedEffect
                    delay(32L)
                }
            }
            captureIntervalMs < 0L -> {
                // Conflation drops obsolete revisions while a GPU readback is
                // in flight. Unlike collectLatest, it does not keep cancelling
                // the image currently being captured (60/120 fps safe).
                var previousCaptureNs = 0L
                snapshotFlow { latestBackgroundKey.value }
                    .conflate()
                    .collect {
                        if (failedCaptures >= 3) return@collect
                        // A single GPU frost readback costs more than a
                        // raw capture; cap it at 5 Hz under fast updates.
                        val interval = (-captureIntervalMs).coerceAtLeast(
                            if (dynamicFrost && gpuFrostFailures < 2) 200L
                            else 16L,
                        )
                        val since = (System.nanoTime() -
                            previousCaptureNs).coerceAtLeast(0L)
                        val remaining = interval * 1_000_000L - since
                        if (previousCaptureNs != 0L && remaining > 0L) {
                            delay((remaining + 999_999L) / 1_000_000L)
                        }
                        withFrameNanos { }
                        capture()
                        previousCaptureNs = System.nanoTime()
                    }
            }
            staticBackdrop -> {
                // The new theme/size must render before its snapshot.
                withFrameNanos { }
                withFrameNanos { }
                repeat(3) {
                    if (capture()) return@LaunchedEffect
                    delay(32L)
                }
            }
            else -> {
                while (isActive && failedCaptures < 3) {
                    withFrameNanos { }
                    capture()
                    delay(captureIntervalMs.coerceAtLeast(32L))
                }
            }
        }
    }

    Box(modifier) {
        Box(
            Modifier.fillMaxSize()
                .onSizeChanged { backgroundSize = it }
                .then(
                    if (plannerActive) Modifier.onGloballyPositioned {
                        sceneOrigin = it.positionInRoot()
                    } else Modifier
                )
                .then(if (sketchMaterial) Modifier.layerBackdrop(sketchBackdrop) else Modifier)
                .drawWithContent {
                    if (sketchMaterial) {
                        drawContent()
                    } else {
                        layer.record { this@drawWithContent.drawContent() }
                        if (plannerActive) {
                            captureRegistry.paintedBackground(latestBackgroundKey.value)
                        }
                        drawLayer(layer)
                    }
                },
            content = background,
        )
        CompositionLocalProvider(
            LocalGlassFrame provides frame,
            LocalSketchGlassBackdrop provides
                (if (sketchMaterial) sketchBackdrop else null),
            LocalSketchMaterialCache provides (if (sketchMaterial) sketchMaterials else null),
            LocalGlassQuality provides quality,
            LocalGlassVeil provides backdropVeil,
            LocalGlassCaptureRegistry provides
                (if (plannerActive) captureRegistry else null),
        ) {
            if (plannerActive) {
                // Native content layer becomes the root for panes and their
                // child offscreen groups, mirroring FlClash _gather roots.
                GlassCaptureRoot(Modifier.fillMaxSize()) { content() }
            } else {
                content()
            }
        }
    }
}
