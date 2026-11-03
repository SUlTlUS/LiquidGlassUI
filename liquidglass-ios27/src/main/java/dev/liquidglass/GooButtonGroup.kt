package dev.liquidglass

import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import com.zeaze.tianyinwallpaper.backdrop.Backdrop
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlin.math.sqrt

internal val LocalGooButtonGroup = staticCompositionLocalOf<GooButtonRegistry?> { null }

internal class GooButtonEntry(val state: ElasticPressState) {
    var bounds by mutableStateOf(Rect.Zero)
    var style by mutableStateOf<GlassStyle?>(null)
    var backdrop by mutableStateOf<Backdrop?>(null)
    var coordinates: LayoutCoordinates? = null
}

internal class GooButtonRegistry {
    val entries = mutableStateListOf<GooButtonEntry>()
    var coordinates: LayoutCoordinates? = null
    fun locate(entry: GooButtonEntry) {
        val parent = coordinates?.takeIf { it.isAttached } ?: return
        val child = entry.coordinates?.takeIf { it.isAttached } ?: return
        entry.bounds = parent.localBoundingBoxOf(child, clipBounds = false)
    }
}

/**
 * Any ElasticGlassButton in this scope can join Goo, including icon and popup sources.
 * Layout and input remain owned by the buttons. Neighbours sharing a backdrop
 * can connect across colors; mixed colors stay inside their own bodies. Goo uses capsule
 * silhouettes, as in GooGlassRow. Use gooEnabled=false for custom non-capsule shapes.
 * The group's backdrop must be an independent sibling, never its own recording.
 */
@RequiresApi(33)
@Composable
fun GooGlassGroup(modifier: Modifier = Modifier, content: @Composable BoxScope.() -> Unit) {
    val registry = remember { GooButtonRegistry() }
    val density = LocalDensity.current.density
    Box(modifier.onGloballyPositioned {
        registry.coordinates = it
        registry.entries.forEach(registry::locate)
    }) {
        val entries = registry.entries.filter { it.style != null && it.bounds.width > 0f && it.bounds.height > 0f }
        val pills = entries.map { entry ->
            val bounds = entry.bounds
            val transform = elasticButtonTransform(entry.state, bounds, density)
            buttonPill(bounds, transform, entry.state.animationStandard)
        }
        // Raw-source fallback retains its original style batches; Sketch owns both standards.
        val batches = gooButtonBatches(pills, entries.map { gooButtonSamplingKey(it.backdrop, it.style!!) })
        for (batch in batches) {
            val group = batch.map { entries[it] }
            val shapes = batch.map { pills[it] }
            val active = group.indices.maxBy { group[it].state.lifted }
            val state = group[active].state
            val styles = group.map { it.style!! }
            val mixed = styles.map { it.tint }.distinct().size > 1
            GooPaintLayer(
                Modifier.matchParentSize(), style = if (mixed) styles.first().copy(tint = Color.Transparent) else styles.first(),
                pills = shapes,
                pillTints = if (mixed) styles.map { it.tint } else emptyList(),
                sketchBackdropOverride = group.first().backdrop,
                blendsDp = (1 until group.size).map { i ->
                    8f + 8f * maxOf(group[i - 1].state.lifted, group[i].state.lifted)
                },
                glow = androidx.compose.ui.geometry.Offset(shapes[active].centerX, shapes[active].centerY),
                glowStrength = state.lifted * .16f,
                glowReachDp = shapes[active].halfHeight * 2f / density / sqrt(2f),
                captureGroup = registry,
            )
        }
        // Stable layout inset; GooPaintLayer separately expands for larger painted contours.
        Box(Modifier.padding(24.dp)) {
            CompositionLocalProvider(LocalGooButtonGroup provides registry) { content() }
        }
    }
}

/** Physical ordering, independent of composition order/RTL. Shader capacity is six. */
internal fun gooButtonSamplingKey(backdrop: Any?, style: GlassStyle): Any = backdrop ?: style

/** Equal adjacent overlays also cover their neck, even in a mixed-color batch. */
internal fun gooTintRuns(tints: List<Color>): List<IntRange> {
    if (tints.isEmpty()) return emptyList()
    val runs = mutableListOf<IntRange>()
    var start = 0
    for (i in 1 until tints.size) {
        if (tints[i] != tints[i - 1]) {
            runs.add(start until i)
            start = i
        }
    }
    runs.add(start until tints.size)
    return runs
}

internal fun <T> gooButtonBatches(pills: List<GlassPill>, materials: List<T>): List<List<Int>> {
    require(pills.size == materials.size)
    val rows = mutableListOf<MutableList<Int>>()
    for (index in pills.indices.sortedWith(compareBy({ pills[it].centerY }, { pills[it].centerX }))) {
        val row = rows.firstOrNull { candidate ->
            candidate.all { GlassMotion.inSameRow(pills[it], pills[index]) }
        }
        if (row == null) rows.add(mutableListOf(index)) else row.add(index)
    }
    return rows.flatMap { row ->
        val batches = mutableListOf<MutableList<Int>>()
        for (index in row.sortedBy { pills[it].centerX }) {
            val last = batches.lastOrNull()
            if (last == null || last.size == 6 || materials[last.last()] != materials[index]) {
                batches.add(mutableListOf(index))
            } else last.add(index)
        }
        batches
    }
}

@Composable
internal fun gooButtonRegistration(state: ElasticPressState, style: GlassStyle, enabled: Boolean): GooButtonRegistration? {
    val registry = LocalGooButtonGroup.current?.takeIf { enabled } ?: return null
    val entry = remember(registry, state) { GooButtonEntry(state) }
    val backdrop = LocalSketchGlassBackdrop.current
    SideEffect { entry.style = style; entry.backdrop = backdrop }
    DisposableEffect(registry, entry) {
        registry.entries.add(entry)
        onDispose { registry.entries.remove(entry) }
    }
    val modifier = Modifier.onGloballyPositioned {
        entry.coordinates = it
        registry.locate(entry)
    }
    return GooButtonRegistration(modifier, entry in registry.entries && entry.style != null &&
        entry.bounds.width > 0f && entry.bounds.height > 0f)
}

internal data class GooButtonRegistration(val modifier: Modifier, val grouped: Boolean)
