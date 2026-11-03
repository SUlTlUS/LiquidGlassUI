package dev.liquidglass

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import com.zeaze.tianyinwallpaper.backdrop.Backdrop
import kotlin.math.ceil
import kotlin.math.floor

/** Paint can outgrow the group's layout when drag resistance is disabled. */
internal fun gooPaintBounds(viewport: IntSize, pills: List<GlassPill>, blendsDp: List<Float>, density: Float): IntRect {
    val margin = (2f + (blendsDp.maxOrNull() ?: 0f).coerceAtLeast(0f) * .25f) * density
    var left = 0f
    var top = 0f
    var right = viewport.width.toFloat()
    var bottom = viewport.height.toFloat()
    for (pill in pills) {
        left = minOf(left, pill.centerX - pill.halfWidth - margin)
        top = minOf(top, pill.centerY - pill.halfHeight - margin)
        right = maxOf(right, pill.centerX + pill.halfWidth + margin)
        bottom = maxOf(bottom, pill.centerY + pill.halfHeight + margin)
    }
    return IntRect(floor(left).toInt(), floor(top).toInt(), ceil(right).toInt(), ceil(bottom).toInt())
}

/** Expand only the material layer. Input, foreground and group coordinates stay fixed. */
@Composable
internal fun GooPaintLayer(
    modifier: Modifier,
    style: GlassStyle,
    pills: List<GlassPill>,
    blendsDp: List<Float>,
    glow: Offset,
    glowStrength: Float,
    glowReachDp: Float,
    pillTints: List<Color> = emptyList(),
    sketchBackdropOverride: Backdrop? = null,
    captureGroup: Any? = null,
) {
    val density = LocalDensity.current.density
    BoxWithConstraints(modifier) {
        val bounds = gooPaintBounds(IntSize(constraints.maxWidth, constraints.maxHeight), pills, blendsDp, density)
        val origin = Offset(bounds.left.toFloat(), bounds.top.toFloat())
        val localPills = if (origin == Offset.Zero) pills else pills.map {
            it.copy(centerX = it.centerX - origin.x, centerY = it.centerY - origin.y)
        }
        GlassPane(
            Modifier.layout { measurable, constraints ->
                val child = measurable.measure(Constraints.fixed(bounds.width, bounds.height))
                layout(constraints.maxWidth, constraints.maxHeight) {
                    child.place(bounds.left, bounds.top)
                }
            },
            style = style,
            pills = localPills,
            blendsDp = blendsDp,
            glow = if (glow == Offset.Unspecified) glow else glow - origin,
            glowStrength = glowStrength,
            glowReachDp = glowReachDp,
            pillTints = pillTints,
            sketchBackdropOverride = sketchBackdropOverride,
            captureGroup = captureGroup,
        )
    }
}
