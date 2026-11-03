package dev.liquidglass

import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Shader
import android.util.Log
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.BlurEffect
import androidx.compose.ui.graphics.GraphicsContext
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Paint as ComposePaint
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import kotlinx.coroutines.CancellationException
import kotlin.math.roundToInt

internal data class GlassGpuAtlasStats(
    val reusedBands: Int = 0,
    val createdBands: Int = 0,
    val reusedLevelAtlases: Int = 0,
    val createdLevelAtlases: Int = 0,
) {
    operator fun plus(other: GlassGpuAtlasStats) = GlassGpuAtlasStats(
        reusedBands + other.reusedBands,
        createdBands + other.createdBands,
        reusedLevelAtlases + other.reusedLevelAtlases,
        createdLevelAtlases + other.createdLevelAtlases,
    )
}

/** GPU hardware-backed per-level image for the RuntimeShader's one child. */
internal data class GlassGpuResidentAtlas(
    val level: Int,
    val backgroundVersion: Any?,
    val image: ImageBitmap,
    val shader: BitmapShader,
    val paneSamples: Map<Any, GlassGpuPaneSamples>,
)

internal data class GlassGpuSampleTile(
    val atlasRect: Rect,
    val sceneSlot: Rect,
    val sigmaPx: Float,
)

internal data class GlassGpuPaneSamples(
    val raw: GlassGpuSampleTile,
    val frost: List<GlassGpuSampleTile>,
    /** Input identity, independent of the pane's own animated outline. */
    val sourceKey: GlassCaptureBandKey,
    val frostKeys: List<GlassCaptureBandKey>,
)

/**
 * Persistent atlas objects; all ImageBitmaps are Bitmap.Config.HARDWARE.
 * A band with an unchanged FlClash-style content key is never repainted.
 * The transient GraphicsLayer display lists are released immediately after
 * the GPU image is made; the immutable GPU bitmaps stay alive in the cache.
 */
@androidx.annotation.RequiresApi(33)
internal class GlassGpuResidentAtlasCache(
    private val graphics: GraphicsContext,
) {
    private data class BandImage(
        val key: GlassCaptureBandKey,
        val width: Int,
        val height: Int,
        val image: ImageBitmap,
    )

    private data class LevelImage(
        val keys: List<GlassCaptureBandKey>,
        val width: Int,
        val height: Int,
        val image: ImageBitmap,
        val shader: BitmapShader,
    )

    private data class BackdropImage(
        val version: Any?,
        val bounds: Rect,
        val image: ImageBitmap,
    )

    @Volatile
    var lastStats: GlassGpuAtlasStats = GlassGpuAtlasStats()
        private set

    private var storedBackdrop: BackdropImage? = null
    private var storedBands = emptyMap<GlassCaptureBandKey, BandImage>()
    private var storedLevels = emptyMap<Int, LevelImage>()

    /** A frame can be built one compositing level at a time. */
    fun beginFrame() {
        lastStats = GlassGpuAtlasStats()
    }

    fun clear() {
        lastStats = GlassGpuAtlasStats()
        storedBackdrop = null
        storedBands = emptyMap()
        storedLevels = emptyMap()
    }

    /** Release obsolete keys after all active levels have been updated. */
    fun prune(plan: GlassCapturePlan) {
        if (plan.placements.isEmpty() || plan.atlases.isEmpty()) {
            clear()
            return
        }
        val active = plan.bands.mapTo(HashSet()) { it.key }
        val levels = plan.atlases.mapTo(HashSet()) { it.level }
        storedBands = storedBands.filterKeys { it in active }
        storedLevels = storedLevels.filterKeys { it in levels }
    }

    /**
     * Capture the source scene, then draw earlier glass in composition order.
     * The caller must provide a host that does not draw the current or later
     * panes into the input. Never sample a frame from a future glass level.
     */
    suspend fun build(
        plan: GlassCapturePlan,
        density: Density,
        layoutDirection: LayoutDirection,
        onlyLevel: Int? = null,
    ): Map<Int, GlassGpuResidentAtlas> {
        val background = plan.backdropLayer ?: return emptyMap()
        val scene = background.bounds
        // Freeze one GPU frame per background revision. Every raw/frost band
        // samples the same underlying pixels, even across separate captures.
        val frozenBackdrop = try {
            storedBackdrop?.takeIf {
                it.version == background.version && it.bounds == scene
            }?.image ?: requireHardware(background.layer.toImageBitmap())
                .also {
                    storedBackdrop = BackdropImage(background.version, scene, it)
                }
        } catch (cancelled: CancellationException) {
            throw cancelled
        } catch (failure: RuntimeException) {
            Log.w("LiquidGlass27", "GPU capture background unavailable", failure)
            return emptyMap()
        }
        val placements = plan.placements
        val bandImages = HashMap<GlassCaptureBandKey, BandImage>()
        val levelImages = HashMap<Int, LevelImage>()
        val output = HashMap<Int, GlassGpuResidentAtlas>()
        var reusedBands = 0
        var createdBands = 0
        var reusedLevels = 0
        var createdLevels = 0
        try {
            for (level in plan.atlases.sortedBy { it.level }) {
                if (onlyLevel != null && level.level != onlyLevel) continue
                if (level.widthPx <= 0 || level.heightPx <= 0 ||
                    level.widthPx.toLong() * level.heightPx > 16_000_000L
                ) continue

                val bandPlans = plan.bands.filter { it.key.level == level.level }
                for (band in bandPlans) {
                    val cached = storedBands[band.key]
                    val image = if (cached != null &&
                        cached.width == band.widthPx && cached.height == band.heightPx
                    ) {
                        reusedBands++
                        cached
                    } else {
                        val generated = buildBand(
                            band, placements, plan.behindContent,
                            scene, frozenBackdrop, density,
                            layoutDirection,
                        ) ?: continue
                        createdBands++
                        BandImage(band.key, band.widthPx, band.heightPx, generated)
                    }
                    bandImages[band.key] = image
                }
                if (bandPlans.any { it.key !in bandImages }) continue

                val keys = bandPlans.map { it.key }
                val last = storedLevels[level.level]
                val atlas = if (last != null && last.keys == keys &&
                    last.width == level.widthPx && last.height == level.heightPx &&
                    keys.all { storedBands[it] === bandImages[it] }
                ) {
                    reusedLevels++
                    last
                } else {
                    createdLevels++
                    val layer = graphics.createGraphicsLayer()
                    try {
                        layer.record(density, layoutDirection,
                            IntSize(level.widthPx, level.heightPx)) {
                            for (band in bandPlans) {
                                val content = bandImages[band.key] ?: continue
                                drawImage(
                                    content.image,
                                    dstOffset = androidx.compose.ui.unit.IntOffset(
                                        band.atlasRect.left.roundToInt(),
                                        band.atlasRect.top.roundToInt(),
                                    ),
                                )
                            }
                        }
                        val image = requireHardware(layer.toImageBitmap())
                        LevelImage(
                            keys, level.widthPx, level.heightPx, image,
                            BitmapShader(
                                image.asAndroidBitmap(),
                                Shader.TileMode.CLAMP, Shader.TileMode.CLAMP,
                            ),
                        )
                    } finally {
                        graphics.releaseGraphicsLayer(layer)
                    }
                }
                levelImages[level.level] = atlas
                val samples = LinkedHashMap<Any, GlassGpuPaneSamples>()
                for (placed in placements.filter { it.level == level.level }) {
                    val tiles = bandPlans.flatMap { band ->
                        band.tiles.filter { it.paneId == placed.pane.id }
                            .map { tile -> GlassGpuSampleTile(
                                tile.atlasRect, tile.sampleSlot, band.key.sigmaPx,
                            ) }
                    }
                    val raw = tiles.firstOrNull { it.sigmaPx == 0f }
                        ?: continue
                    val rawSourceKey = bandPlans.firstOrNull { band ->
                        band.key.kind == GlassCaptureBandKind.Raw &&
                            band.tiles.any { it.paneId === placed.pane.id }
                    }?.key ?: continue
                    samples[placed.pane.id] = GlassGpuPaneSamples(
                        raw = raw,
                        frost = tiles.filter { it.sigmaPx > 0f }
                            .sortedBy { it.sigmaPx }
                            .take(3),
                        sourceKey = rawSourceKey,
                        frostKeys = bandPlans.filter { band ->
                            band.key.kind != GlassCaptureBandKind.Raw &&
                                band.tiles.any { it.paneId === placed.pane.id }
                        }.map { it.key },
                    )
                }
                output[level.level] = GlassGpuResidentAtlas(
                    level = level.level,
                    backgroundVersion = background.version,
                    image = atlas.image,
                    shader = atlas.shader,
                    paneSamples = samples,
                )
            }
        } catch (cancelled: CancellationException) {
            throw cancelled
        } catch (failure: RuntimeException) {
            Log.w("LiquidGlass27", "GPU resident capture failed", failure)
            return emptyMap()
        }
        val batchStats = GlassGpuAtlasStats(
            reusedBands, createdBands, reusedLevels, createdLevels,
        )
        // Calling build(onlyLevel=...) once per compositing level must not
        // overwrite the frame's earlier cache-hit/miss measurements.
        lastStats = if (onlyLevel == null) batchStats else lastStats + batchStats
        // Carry only referenced bands forward; old GPU images are released
        // naturally once no pane / previous frame references them.
        if (onlyLevel == null) {
            storedBands = bandImages
            storedLevels = levelImages
        } else {
            storedBands = storedBands.filterKeys { it.level != onlyLevel } + bandImages
            storedLevels = storedLevels.filterKeys { it != onlyLevel } + levelImages
        }
        return output
    }

    private suspend fun buildBand(
        band: GlassCaptureBandPlan,
        placements: List<GlassCapturePlacement>,
        behindContent: List<GlassCaptureBehindContent>,
        scene: Rect,
        background: ImageBitmap,
        density: Density,
        layoutDirection: LayoutDirection,
    ): ImageBitmap? {
        if (band.widthPx <= 0 || band.heightPx <= 0) return null
        val contentLayer = graphics.createGraphicsLayer()
        try {
            contentLayer.record(density, layoutDirection,
                IntSize(band.widthPx, band.heightPx)) {
                for (tile in band.tiles) {
                    val sourcePaneIndex = placements.indexOfFirst {
                        it.pane.id === tile.paneId
                    }
                    if (sourcePaneIndex < 0) continue
                    val current = placements[sourcePaneIndex]
                    val dx = tile.atlasRect.left - tile.sampleSlot.left
                    val dy = tile.atlasRect.top - tile.sampleSlot.top
                    clipRect(
                        tile.atlasRect.left, tile.atlasRect.top,
                        tile.atlasRect.right, tile.atlasRect.bottom,
                    ) {
                        translate(dx, dy) {
                            translate(scene.left, scene.top) {
                                drawImage(background)
                            }
                            // Compose's explicit capture surfaces and glass
                            // panes replay in their original sibling order.
                            // Everything after the current pane is omitted.
                            val commonRoot = current.pane.captureRoots.lastOrNull()
                            val earlierGlass = placements.asSequence()
                                .filter { prior ->
                                    prior.pane.paintOrder < current.pane.paintOrder &&
                                        prior.level < current.level &&
                                        (current.pane.group == null ||
                                            current.pane.group !== prior.pane.group) &&
                                        (commonRoot == null ||
                                            commonRoot in prior.pane.captureRoots) &&
                                        intersects(prior.pane.bounds, current.reach) &&
                                        prior.pane.renderLayer != null
                                }.map { it.pane.paintOrder to it.pane }
                            val earlierContent = behindContent.asSequence()
                                .filter { content ->
                                    content.paintOrder < current.pane.paintOrder &&
                                        (commonRoot == null ||
                                            commonRoot in content.captureRoots) &&
                                        intersects(content.bounds, current.reach) &&
                                        content.layer != null
                                }.map { it.paintOrder to it }
                            for ((_, source) in (earlierGlass + earlierContent)
                                .sortedBy { it.first }
                            ) {
                                when (source) {
                                    is GlassCapturePane -> {
                                        val picture = source.renderLayer ?: continue
                                        drawCapturedLayer(
                                            picture, source.sceneTransform,
                                            source.captureAlpha,
                                        )
                                    }
                                    is GlassCaptureBehindContent -> {
                                        val picture = source.layer ?: continue
                                        drawCapturedLayer(
                                            picture, source.sceneTransform,
                                            source.captureAlpha,
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
            val sigma = band.key.sigmaPx
            val raw = requireHardware(contentLayer.toImageBitmap())
            if (sigma <= 0f) return raw
            // Blur each tile in its own GPU layer so sigma does not leak
            // from a neighboring pane's atlas slot. No CPU pixel copies.
            val tiles = ArrayList<Pair<GlassCaptureAtlasTile, ImageBitmap>>()
            for (tile in band.tiles) {
                val size = IntSize(tile.atlasRect.width.roundToInt(),
                    tile.atlasRect.height.roundToInt())
                if (size.width <= 0 || size.height <= 0) continue
                val blurLayer = graphics.createGraphicsLayer()
                try {
                    blurLayer.renderEffect = BlurEffect(
                        radiusX = sigma, radiusY = sigma,
                        edgeTreatment = TileMode.Clamp,
                    )
                    blurLayer.record(density, layoutDirection, size) {
                        drawImage(
                            image = raw,
                            srcOffset = IntOffset(
                                tile.atlasRect.left.roundToInt(),
                                tile.atlasRect.top.roundToInt(),
                            ),
                            srcSize = size,
                            dstSize = size,
                        )
                    }
                    tiles.add(tile to requireHardware(blurLayer.toImageBitmap()))
                } finally {
                    graphics.releaseGraphicsLayer(blurLayer)
                }
            }
            val packed = graphics.createGraphicsLayer()
            try {
                packed.record(density, layoutDirection,
                    IntSize(band.widthPx, band.heightPx)) {
                    for ((tile, image) in tiles) {
                        drawImage(image,
                            dstOffset = IntOffset(
                                tile.atlasRect.left.roundToInt(),
                                tile.atlasRect.top.roundToInt(),
                            ),
                        )
                    }
                }
                return requireHardware(packed.toImageBitmap())
            } finally {
                graphics.releaseGraphicsLayer(packed)
            }
        } finally {
            graphics.releaseGraphicsLayer(contentLayer)
        }
    }

    private fun requireHardware(image: ImageBitmap): ImageBitmap {
        check(image.asAndroidBitmap().config == Bitmap.Config.HARDWARE) {
            "GPU resident atlas requires hardware ImageBitmap"
        }
        return image
    }

    private fun DrawScope.drawCapturedLayer(
        picture: GraphicsLayer,
        affine: GlassCaptureAffine,
        alpha: Float,
    ) {
        if (!affine.isValid() || alpha <= .001f) return
        withTransform({ transform(affine.matrix()) }) {
            if (alpha < .999f) {
                drawContext.canvas.saveLayer(
                    Rect(0f, 0f, picture.size.width.toFloat(),
                        picture.size.height.toFloat()),
                    ComposePaint().apply { this.alpha = alpha },
                )
                try {
                    drawLayer(picture)
                } finally {
                    drawContext.canvas.restore()
                }
            } else {
                drawLayer(picture)
            }
        }
    }

    private fun intersects(a: Rect, b: Rect) =
        a.left < b.right && a.right > b.left &&
        a.top < b.bottom && a.bottom > b.top
}
