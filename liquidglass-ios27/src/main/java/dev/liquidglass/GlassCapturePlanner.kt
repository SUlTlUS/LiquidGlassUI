package dev.liquidglass

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.layer.GraphicsLayer
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.max

/**
 * Capture dependencies corresponding to FlClash's _gather/_place pass.
 * Order is back-to-front; without a shared offscreen root a pane may only
 * read a previously painted pane when their sampled regions intersect.
 */
/** Local-pixel to scene-root affine matrix, including parent transforms. */
internal data class GlassCaptureAffine(
    val xx: Float = 1f, val yx: Float = 0f,
    val xy: Float = 0f, val yy: Float = 1f,
    val tx: Float = 0f, val ty: Float = 0f,
) {
    fun isValid(): Boolean = listOf(xx, yx, xy, yy, tx, ty).all { it.isFinite() } &&
        kotlin.math.abs(xx * yy - xy * yx) > .00001f

    fun matrix(): Matrix = Matrix().apply {
        this[0, 0] = xx
        this[0, 1] = xy
        this[1, 0] = yx
        this[1, 1] = yy
        this[0, 3] = tx
        this[1, 3] = ty
    }

    companion object {
        fun fromPoints(topLeft: Offset, xAxis: Offset, yAxis: Offset,
                       width: Float, height: Float): GlassCaptureAffine =
            GlassCaptureAffine(
                xx = (xAxis.x - topLeft.x) / width,
                yx = (xAxis.y - topLeft.y) / width,
                xy = (yAxis.x - topLeft.x) / height,
                yy = (yAxis.y - topLeft.y) / height,
                tx = topLeft.x, ty = topLeft.y,
            )
    }
}

internal data class GlassCapturePane(
    val id: Any,
    val bounds: Rect,
    val blurSigmaPx: Float,
    val rimBlurSigmaPx: Float = 0f,
    val refractionPx: Float = 0f,
    val dispersionPx: Float = 0f,
    val hazeSigmaPx: Float = 0f,
    val group: Any? = null,
    /** Visibility/opacity of the owner, including hidden popup source. */
    val captureAlpha: Float = 1f,
    /** Maximum ancestor transform scale, in physical root coordinates. */
    val transformScale: Float = 1f,
    val sceneTransform: GlassCaptureAffine = GlassCaptureAffine(),
    /** Outer-to-inner offscreen root IDs. Empty means the GlassScene host. */
    val captureRoots: List<Any> = emptyList(),
    /** Recorded Compose display list, not a CPU screenshot. */
    val renderLayer: GraphicsLayer? = null,
    val renderRevision: Long = 0L,
    /** Shader and silhouette changes can occur without bounds changes. */
    val visualKey: Any? = Unit,
    val paintOrder: Long = 0L,
)

/** Explicit non-glass content painted within a capture root before a pane. */
internal data class GlassCaptureBehindContent(
    val id: Any,
    val bounds: Rect,
    val captureRoots: List<Any>,
    val layer: GraphicsLayer?,
    val transformScale: Float = 1f,
    val sceneTransform: GlassCaptureAffine = GlassCaptureAffine(),
    /** Parent-layer opacity not contained in the recorded display list. */
    val captureAlpha: Float = 1f,
    /** User-supplied content version for draw-only animation updates. */
    val contentVersion: Any? = Unit,
    val revision: Long = 0L,
    val paintOrder: Long = 0L,
)

internal enum class GlassCaptureBandKind { Raw, Material, Haze }

private data class GlassCaptureClass(
    val level: Int,
    val kind: GlassCaptureBandKind,
    val sigmaPx: Float,
    val isolatedPane: Any? = null,
)

internal data class GlassCapturePlacement(
    val pane: GlassCapturePane,
    val level: Int,
    val reach: Rect,
    val slot: Rect,
)

internal data class GlassCaptureBandKey(
    val backgroundVersion: Any?,
    /** Reflow/resizing invalidates even a pane's unchanged local slot. */
    val sceneBounds: Rect,
    val level: Int,
    val kind: GlassCaptureBandKind,
    val sigmaPx: Float,
    val slots: List<Pair<Any, Rect>>,
    val rootPaths: List<List<Any>>,
    /** Earlier pane/root content that contributes to this sampled band. */
    val underlayRevisions: List<Pair<Any, Long>>,
)

internal data class GlassCaptureAtlasTile(
    val paneId: Any,
    /** Full-scene capture coordinates, snapped out to whole texels. */
    val sampleSlot: Rect,
    /** Destination in its level's combined atlas, not in the band alone. */
    val atlasRect: Rect,
)

internal data class GlassCaptureBandPlan(
    val key: GlassCaptureBandKey,
    val widthPx: Int,
    val heightPx: Int,
    val atlasRect: Rect,
    val tiles: List<GlassCaptureAtlasTile>,
    /** True only when both source identity and sampling geometry match. */
    val unchanged: Boolean,
)

internal data class GlassCaptureLevelAtlas(
    val level: Int,
    val widthPx: Int,
    val heightPx: Int,
    val bandKeys: List<GlassCaptureBandKey>,
)

internal data class GlassCaptureBackdropLayer(
    val layer: GraphicsLayer,
    val bounds: Rect,
    val version: Any?,
)

internal data class GlassCaptureRootSnapshot(
    val id: Any,
    val parents: List<Any>,
    val bounds: Rect,
    val layer: GraphicsLayer,
    val revision: Long,
)

internal data class GlassCapturePlan(
    val placements: List<GlassCapturePlacement>,
    val bands: List<GlassCaptureBandPlan>,
    /** Actual recorded Compose offscreen layer handles; not yet sampled. */
    val roots: List<GlassCaptureRootSnapshot> = emptyList(),
    /** Per-level atlas allocations, with FlClash's 32-pixel sizing steps. */
    val atlases: List<GlassCaptureLevelAtlas> = emptyList(),
    /** The actual scene background GraphicsLayer, without any glass panes. */
    val backdropLayer: GlassCaptureBackdropLayer? = null,
    /** Explicit foreground content recorded ahead of individual glass panes. */
    val behindContent: List<GlassCaptureBehindContent> = emptyList(),
) {
    val levels: Int get() = (placements.maxOfOrNull { it.level } ?: -1) + 1
}

/**
 * Geometry, source dependencies and stable band packing for the separate
 * GlassGpuResidentAtlasCache GPU host. The plan never samples pixels itself;
 * only registered real Compose layers may enter behind-pane replay.
 */
internal fun planGlassCapture(
    scene: Rect,
    panes: List<GlassCapturePane>,
    backgroundVersion: Any?,
    previous: GlassCapturePlan? = null,
    roots: List<GlassCaptureRootSnapshot> = emptyList(),
    behindContent: List<GlassCaptureBehindContent> = emptyList(),
): GlassCapturePlan {
    if (!scene.validArea()) return GlassCapturePlan(emptyList(), emptyList())
    val placements = ArrayList<GlassCapturePlacement>(panes.size)
    val dependencies = LinkedHashMap<Any, List<GlassCapturePlacement>>()
    for (pane in panes) {
        if (!pane.bounds.validArea() || !pane.blurSigmaPx.isFinite() ||
            !pane.rimBlurSigmaPx.isFinite() || !pane.refractionPx.isFinite() ||
            !pane.dispersionPx.isFinite() || !pane.hazeSigmaPx.isFinite() ||
            !pane.transformScale.isFinite() || pane.transformScale <= 0f ||
            !pane.sceneTransform.isValid() ||
            !pane.captureAlpha.isFinite() || pane.captureAlpha <= .001f
        ) continue
        if (!pane.bounds.intersects(scene)) continue

        val blur = max(0f, max(pane.blurSigmaPx, pane.hazeSigmaPx))
        val rim = max(0f, pane.rimBlurSigmaPx)
        val bend = max(0f, -pane.refractionPx) + max(0f, pane.dispersionPx)
        val stretch = pane.transformScale
        // FlClash's _levelReach (2.5) and capture reach (3); the
        // ancestor graphicsLayer's stretch scales the optical footprint.
        val reach = pane.bounds.expand((2.5f * (blur + rim) + bend) * stretch)
        val needed = pane.bounds.expand((3f * (blur + rim) + bend + 2f) * stretch)
            .intersectArea(scene)?.snapOutwardToPixels() ?: continue
        // Keep the previous capture slot while the animated pane fits.
        // The extra margin avoids reallocation at tiny spring motions.
        val held = previous?.placements?.firstOrNull {
            it.pane.id === pane.id && it.pane.captureRoots == pane.captureRoots
        }?.slot
        val slot = if (held != null && held.encloses(needed)) held
            else needed.expand(24f).intersectArea(scene)
                ?.snapOutwardToPixels() ?: continue
        // FlClash only promotes a pane above earlier content visible from
        // its nearest offscreen root. Sibling offscreen roots are isolated.
        val root = pane.captureRoots.lastOrNull()
        val underlays = placements.filter { behind ->
            (pane.group == null || behind.pane.group !== pane.group) &&
                (root == null || root in behind.pane.captureRoots) &&
                behind.pane.paintOrder < pane.paintOrder &&
                behind.pane.bounds.intersects(reach)
        }
        val level = underlays.maxOfOrNull { it.level + 1 } ?: 0
        dependencies[pane.id] = underlays
        placements.add(GlassCapturePlacement(pane, level, reach, slot))
    }
    val entries = LinkedHashMap<GlassCaptureClass,
        MutableList<GlassCapturePlacement>>()
    for (placed in placements) {
        val sigma = captureSigma(
            placed.pane.blurSigmaPx.coerceAtLeast(0f) *
                placed.pane.transformScale,
        )
        // Retain a separate unblurred tile for refraction, highlights and
        // low-sigma moments, independent of the preblurred material tile.
        entries.getOrPut(
            GlassCaptureClass(placed.level, GlassCaptureBandKind.Raw,
                0f, placed.pane.id)
        ) { ArrayList() }.add(placed)
        if (sigma > 0f) {
            entries.getOrPut(
                GlassCaptureClass(placed.level,
                    GlassCaptureBandKind.Material, sigma)
            ) { ArrayList() }.add(placed)
        }
        val hazeSigma = captureSigma(
            placed.pane.hazeSigmaPx.coerceAtLeast(0f) *
                placed.pane.transformScale,
        )
        // Gaussian blur depends on sigma, not on which optical branch
        // requested it. One tile serves material and haze when equal.
        if (hazeSigma > 0f && hazeSigma != sigma) {
            entries.getOrPut(
                GlassCaptureClass(placed.level, GlassCaptureBandKind.Haze,
                    hazeSigma)
            ) { ArrayList() }.add(placed)
        }
    }
    val previousKeys = previous?.bands?.map { it.key }?.toHashSet() ?: emptySet()
    val rootRevisions = roots.associate { it.id to it.revision }
    // A pane reflects *all* of its underlay, including underlays that are
    // visible only through an earlier refracting pane. Carry dependency
    // revisions transitively so a changing icon under glass A invalidates
    // glass B which sits above A, even if that icon misses B's own slot.
    val sourceDependencies = LinkedHashMap<Any, Map<Any, Long>>()
    for (placed in placements) {
        val deps = LinkedHashMap<Any, Long>()
        val root = placed.pane.captureRoots.lastOrNull()
        for (content in behindContent) {
            if (content.paintOrder < placed.pane.paintOrder &&
                (root == null || root in content.captureRoots) &&
                content.bounds.intersects(placed.reach)
            ) deps[content.id] = content.revision
        }
        for (behind in dependencies[placed.pane.id].orEmpty()) {
            deps[behind.pane.id] = behind.pane.renderRevision
            deps.putAll(sourceDependencies[behind.pane.id].orEmpty())
        }
        sourceDependencies[placed.pane.id] = deps
    }
    val bands = ArrayList<GlassCaptureBandPlan>(entries.size)
    val atlases = ArrayList<GlassCaptureLevelAtlas>()
    // FlClash _snapshot packs each equal-sigma class into a vertical band,
    // then vertically stacks bands of the same compositing level. The
    // destination rectangles are ready for GPU drawLayer + clip replay.
    for ((level, classes) in entries.entries.groupBy { it.key.level }) {
        var top = 0
        var atlasWidth = 0
        val levelBandKeys = ArrayList<GlassCaptureBandKey>()
        for ((kind, members) in classes) {
            val width = members.maxOf { it.slot.width }
            val height = members.sumOf { it.slot.height.toDouble() }.toFloat()
            val bandWidth = roundedAtlasExtent(width)
            val bandHeight = roundedAtlasExtent(height)
            var tileTop = top.toFloat()
            val tiles = members.map { member ->
                val slot = member.slot
                GlassCaptureAtlasTile(
                    paneId = member.pane.id,
                    sampleSlot = slot,
                    atlasRect = Rect(0f, tileTop, slot.width, tileTop + slot.height),
                ).also { tileTop += slot.height }
            }
            val underlayRevisions = LinkedHashMap<Any, Long>()
            for (member in members) {
                // A root may contain non-glass images or text that move
                // independently from registered glass panes.
                for (rootId in member.pane.captureRoots) {
                    val revision = rootRevisions[rootId]
                    if (revision != null) underlayRevisions[rootId] = revision
                }
                underlayRevisions.putAll(
                    sourceDependencies[member.pane.id].orEmpty()
                )
            }
            val key = GlassCaptureBandKey(
                backgroundVersion, scene, level, kind.kind, kind.sigmaPx,
                members.map { it.pane.id to it.slot },
                members.map { it.pane.captureRoots },
                underlayRevisions.toList(),
            )
            bands.add(
                GlassCaptureBandPlan(
                    key = key,
                    widthPx = bandWidth,
                    heightPx = bandHeight,
                    atlasRect = Rect(
                        0f, top.toFloat(), bandWidth.toFloat(),
                        (top + bandHeight).toFloat(),
                    ),
                    tiles = tiles,
                    // A matching content key is only a reuse candidate.
                    // A GPU host must still retain a corresponding picture.
                    unchanged = key in previousKeys,
                )
            )
            levelBandKeys.add(key)
            atlasWidth = max(atlasWidth, bandWidth)
            top += bandHeight
        }
        atlases.add(GlassCaptureLevelAtlas(level, atlasWidth, top, levelBandKeys))
    }
    return GlassCapturePlan(placements, bands, roots, atlases,
        behindContent = behindContent)
}

// Reuse a stable preblur tile across adjacent animation frames; preserve
// the exact optical sigma by sampling the remaining variance in AGSL.
private fun captureSigma(px: Float): Float =
    floor(px.coerceAtLeast(0f) / 8f) * 8f

private fun roundedAtlasExtent(px: Float): Int =
    (ceil(px.coerceAtLeast(0f).toDouble() / 32.0).toInt() * 32)

private fun Rect.snapOutwardToPixels(): Rect = Rect(
    floor(left), floor(top), ceil(right), ceil(bottom),
)

private fun Rect.validArea(): Boolean =
    left.isFinite() && top.isFinite() && right.isFinite() &&
    bottom.isFinite() && width > 0f && height > 0f

private fun Rect.encloses(other: Rect): Boolean =
    left <= other.left && top <= other.top &&
    right >= other.right && bottom >= other.bottom

private fun Rect.intersects(other: Rect): Boolean =
    left < other.right && right > other.left &&
    top < other.bottom && bottom > other.top

private fun Rect.expand(amount: Float): Rect =
    Rect(left - amount, top - amount, right + amount, bottom + amount)

private fun Rect.intersectArea(other: Rect): Rect? {
    val clipped = Rect(
        maxOf(left, other.left), maxOf(top, other.top),
        minOf(right, other.right), minOf(bottom, other.bottom),
    )
    return clipped.takeIf { it.validArea() }
}

/** Keeps recorded GPU picture handles and composition-order pane metadata. */
internal class GlassCaptureRegistry {
    private data class RootRecord(
        val parents: List<Any>,
        val layer: GraphicsLayer,
        var bounds: Rect = Rect.Zero,
        var revision: Long = 0L,
    )

    private val panes = LinkedHashMap<Any, GlassCapturePane>()
    private val behindContent = LinkedHashMap<Any, GlassCaptureBehindContent>()
    private val orders = HashMap<Any, Long>()
    private var nextOrder = 0L
    private val roots = LinkedHashMap<Any, RootRecord>()
    private val paneRevisions = HashMap<Any, Long>()
    private val paneLayers = HashMap<Any, GraphicsLayer>()
    private val paneRenderedSource = HashMap<Any, Any?>()
    private var revision = 0L
    private var previous: GlassCapturePlan? = null
    private var hasPaintedBackground = false
    private var paintedBackgroundVersion: Any? = null

    /** Changes are conflated; recording a picture must never block a draw. */
    val changes = kotlinx.coroutines.channels.Channel<Unit>(
        kotlinx.coroutines.channels.Channel.CONFLATED,
    )

    private fun invalidated() {
        changes.trySend(Unit)
    }

    fun requestSnapshot() = invalidated()

    /** Only the draw recorder can confirm that a new backdrop was painted. */
    fun paintedBackground(version: Any?) {
        if (hasPaintedBackground && paintedBackgroundVersion == version) return
        hasPaintedBackground = true
        paintedBackgroundVersion = version
        invalidated()
    }

    fun hasBackground(): Boolean = hasPaintedBackground

    fun backgroundVersion(): Any? = paintedBackgroundVersion

    fun upsert(pane: GlassCapturePane) {
        val ordered = pane.copy(
            paintOrder = orders.getOrPut(pane.id) { ++nextOrder },
        )
        val previous = panes[pane.id]
        // A layer recording on a display refresh is not itself evidence of
        // content change. The Compose inputs changed when pane != previous.
        val signature = ordered.copy(renderLayer = null, renderRevision = 0L)
        val before = previous?.copy(renderLayer = null, renderRevision = 0L)
        if (before != signature) {
            revision++
            paneRevisions[pane.id] = revision
        }
        val current = ordered.copy(
            renderLayer = paneLayers[pane.id],
            renderRevision = paneRevisions[pane.id] ?: 0L,
        )
        if (previous != current) {
            panes[pane.id] = current
            invalidated()
        }
    }

    /**
     * A pane may redraw with identical geometry but different background
     * atlas pixels. An upper layer must not replay an obsolete lower pane.
     * Signal only a changed source identity, not every display refresh.
     */
    fun painted(id: Any, layer: GraphicsLayer, source: Any?) {
        val first = paneLayers[id] !== layer
        if (!first && paneRenderedSource.containsKey(id) &&
            paneRenderedSource[id] === source
        ) return
        paneLayers[id] = layer
        paneRenderedSource[id] = source
        revision++
        paneRevisions[id] = revision
        panes[id]?.let { panes[id] = it.copy(
            renderLayer = layer, renderRevision = revision,
        ) }
        invalidated()
    }

    fun remove(id: Any) {
        panes.remove(id)
        paneRevisions.remove(id)
        paneLayers.remove(id)
        paneRenderedSource.remove(id)
        orders.remove(id)
        invalidated()
    }

    fun upsertContent(content: GlassCaptureBehindContent) {
        val ordered = content.copy(
            paintOrder = orders.getOrPut(content.id) { ++nextOrder },
        )
        val previous = behindContent[content.id]
        if (previous?.copy(layer = null, revision = 0L) !=
            ordered.copy(layer = null, revision = 0L)
        ) {
            revision++
            paneRevisions[content.id] = revision
        }
        val current = ordered.copy(
            layer = paneLayers[content.id],
            revision = paneRevisions[content.id] ?: 0L,
        )
        if (previous != current) {
            behindContent[content.id] = current
            invalidated()
        }
    }

    fun paintedContent(id: Any, layer: GraphicsLayer) {
        if (paneLayers[id] === layer) return
        paneLayers[id] = layer
        revision++
        paneRevisions[id] = revision
        behindContent[id]?.let {
            behindContent[id] = it.copy(layer = layer, revision = revision)
        }
        invalidated()
    }

    fun removeContent(id: Any) {
        behindContent.remove(id)
        paneRevisions.remove(id)
        paneLayers.remove(id)
        orders.remove(id)
        invalidated()
    }

    fun addRoot(id: Any, parents: List<Any>, layer: GraphicsLayer) {
        roots[id] = RootRecord(parents.toList(), layer)
        invalidated()
    }

    fun rootBounds(id: Any, bounds: Rect) {
        val root = roots[id] ?: return
        if (root.bounds != bounds) {
            root.bounds = bounds
            invalidated()
        }
    }

    fun paintedRoot(id: Any) {
        val root = roots[id] ?: return
        // The content of the full root includes its glass children. Its
        // display-list redraw should not invalidate the atlas that caused
        // the redraw. Pane versions carry optical-content dependencies.
        if (root.revision == 0L) {
            revision++
            root.revision = revision
            invalidated()
        }
    }

    fun removeRoot(id: Any) {
        roots.remove(id)
        invalidated()
    }

    fun snapshot(
        scene: Rect,
        backgroundVersion: Any?,
        backdropLayer: GraphicsLayer? = null,
    ): GlassCapturePlan {
        val rootLayers = roots.mapNotNull { (id, root) ->
            if (root.bounds.validArea()) {
                GlassCaptureRootSnapshot(
                    id, root.parents, root.bounds, root.layer, root.revision,
                )
            } else null
        }
        return planGlassCapture(
            scene, panes.values.toList(), backgroundVersion, previous,
            rootLayers,
            behindContent.values.toList(),
        ).copy(
            backdropLayer = backdropLayer?.let {
                GlassCaptureBackdropLayer(it, scene, backgroundVersion)
            },
        ).also { previous = it }
    }
}
