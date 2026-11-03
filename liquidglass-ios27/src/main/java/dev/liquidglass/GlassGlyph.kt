package dev.liquidglass

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlinx.coroutines.launch

/** FlClash glyph.dart / glyph_painter.dart, drawn with Compose paths (GPL-3.0). */
enum class GlassGlyphRole { Body, Detail, Line }

data class GlassGlyphShape(
    val path: Path,
    val role: GlassGlyphRole = GlassGlyphRole.Body,
    val solid: Boolean = false,
)

data class GlassGlyph(val shapes: List<GlassGlyphShape>, val matchTextDirection: Boolean = false)
data class GlassGlyphVertex(val x: Float, val y: Float, val radius: Float = 0f) {
    val offset: Offset get() = Offset(x, y)
}

object GlassGlyphShapes {
    fun box(left: Float, top: Float, right: Float, bottom: Float, radius: Float,
        role: GlassGlyphRole = GlassGlyphRole.Body) =
        GlassGlyphShape(gooPlatterOutline(Rect(left, top, right, bottom), radius).asComposePath(), role)

    fun oval(left: Float, top: Float, right: Float, bottom: Float,
        role: GlassGlyphRole = GlassGlyphRole.Body, solid: Boolean = false) =
        GlassGlyphShape(Path().apply { addOval(Rect(left, top, right, bottom)) }, role, solid)

    fun circle(x: Float, y: Float, radius: Float,
        role: GlassGlyphRole = GlassGlyphRole.Body, solid: Boolean = false) =
        oval(x - radius, y - radius, x + radius, y + radius, role, solid)

    fun arc(x: Float, y: Float, radius: Float, startRadians: Float, sweepRadians: Float,
        role: GlassGlyphRole = GlassGlyphRole.Body) = GlassGlyphShape(Path().apply {
            addArc(Rect(x - radius, y - radius, x + radius, y + radius),
                (startRadians * 180 / PI).toFloat(), (sweepRadians * 180 / PI).toFloat())
        }, role)

    fun link(from: Offset, to: Offset, radius: Float): GlassGlyphShape {
        val delta = to - from
        val inset = delta / delta.getDistance() * (radius + 1.8f)
        return GlassGlyphShape(Path().apply {
            moveTo((from + inset).x, (from + inset).y)
            lineTo((to - inset).x, (to - inset).y)
        }, GlassGlyphRole.Line)
    }

    fun svg(data: String, role: GlassGlyphRole = GlassGlyphRole.Body, solid: Boolean = false) =
        GlassGlyphShape(PathParser().parsePathString(data).toPath(), role, solid)

    fun polyline(vararg vertices: GlassGlyphVertex, closed: Boolean = false,
        role: GlassGlyphRole = GlassGlyphRole.Body): GlassGlyphShape {
        fun corner(index: Int): Pair<Offset, Offset> {
            val vertex = vertices[index]
            val previous = vertices[(index - 1 + vertices.size) % vertices.size].offset - vertex.offset
            val next = vertices[(index + 1) % vertices.size].offset - vertex.offset
            return (vertex.offset + previous / previous.getDistance() *
                min(vertex.radius, previous.getDistance() / 2)) to
                (vertex.offset + next / next.getDistance() * min(vertex.radius, next.getDistance() / 2))
        }
        val path = Path()
        val first = if (closed) corner(0).second else vertices.first().offset
        path.moveTo(first.x, first.y)
        val last = if (closed) vertices.size else vertices.size - 1
        for (i in 1..last) {
            val vertex = vertices[i % vertices.size]
            if (!closed && i == vertices.lastIndex) {
                path.lineTo(vertex.x, vertex.y)
            } else {
                val (enter, exit) = corner(i % vertices.size)
                path.lineTo(enter.x, enter.y)
                path.quadraticTo(vertex.x, vertex.y, exit.x, exit.y)
            }
        }
        if (closed) path.close()
        return GlassGlyphShape(path, role)
    }
}

@Composable
fun GlassGlyphIcon(
    glyph: GlassGlyph, color: Color, modifier: Modifier = Modifier, fill: Float = 0f,
) {
    val mirrored = glyph.matchTextDirection && LocalLayoutDirection.current == LayoutDirection.Rtl
    Canvas(modifier.size(24.dp).graphicsLayer {
        compositingStrategy = CompositingStrategy.Offscreen
        alpha = color.alpha
    }) {
        val opaque = color.copy(alpha = 1f)
        val stroke = Stroke(1.8f, cap = StrokeCap.Round, join = StrokeJoin.Round)
        val lineWidth = 1.8f + .4f * fill.coerceIn(0f, 1f)
        val line = Stroke(lineWidth, cap = StrokeCap.Round, join = StrokeJoin.Round)
        val cut = fill > .5f
        val detailScale = if (cut) (fill - .5f) * 2 else 1f - fill * 2
        scale(size.minDimension / 24f, pivot = Offset.Zero) {
            scale(if (mirrored) -1f else 1f, 1f, pivot = Offset(12f, 12f)) {
                for (shape in glyph.shapes) if (shape.role == GlassGlyphRole.Body && fill > 0f) {
                    if (fill >= 1f) drawPath(shape.path, opaque)
                    else {
                        val depth = shape.path.getBounds().minDimension / 2
                        clipPath(shape.path) {
                            drawPath(shape.path, opaque,
                                style = Stroke((.9f + (depth - .9f) * fill) * 2,
                                    cap = StrokeCap.Round, join = StrokeJoin.Round))
                        }
                    }
                }
                if (detailScale > 0f) for (shape in glyph.shapes) {
                    if (shape.role != GlassGlyphRole.Detail) continue
                    val blend = if (cut) BlendMode.Clear else BlendMode.SrcOver
                    if (shape.solid) scale(detailScale, pivot = shape.path.getBounds().center) {
                        drawPath(shape.path, opaque, blendMode = blend)
                    } else drawPath(shape.path, opaque, blendMode = blend,
                        style = Stroke(1.8f * detailScale, cap = StrokeCap.Round, join = StrokeJoin.Round))
                }
                for (shape in glyph.shapes) {
                    if (shape.role == GlassGlyphRole.Detail) continue
                    if (shape.solid && shape.role == GlassGlyphRole.Line) {
                        scale(lineWidth / 1.8f, pivot = shape.path.getBounds().center) {
                            drawPath(shape.path, opaque)
                        }
                    } else drawPath(shape.path, opaque, style = when {
                        shape.solid -> Fill
                        shape.role == GlassGlyphRole.Line -> line
                        else -> stroke
                    })
                }
            }
        }
    }
}

@Composable
fun AnimatedGlassGlyph(glyph: GlassGlyph, filled: Boolean, color: Color, modifier: Modifier = Modifier) {
    val progress = remember { Animatable(if (filled) 1f else 0f) }
    val pop = remember { Animatable(1f) }
    val scope = rememberCoroutineScope()
    var previous by remember { mutableStateOf(filled) }
    LaunchedEffect(filled) {
        if (filled && !previous && !pop.isRunning) scope.launch {
            pop.snapTo(0f)
            pop.animateTo(1f, tween(350, easing = LinearEasing))
        }
        previous = filled
        val target = if (filled) 1f else 0f
        progress.animateTo(target, tween((350 * abs(target - progress.value)).roundToInt(),
            easing = LinearEasing))
    }
    GlassGlyphIcon(glyph, color, modifier.graphicsLayer {
        scaleX = 1f + sin(PI * pop.value).toFloat() * .1f
        scaleY = scaleX
    }, fill = CubicBezierEasing(.645f, .045f, .355f, 1f).transform(progress.value))
}
