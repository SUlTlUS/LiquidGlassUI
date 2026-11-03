package dev.liquidglass

import android.graphics.BlendMode
import android.graphics.Color
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.ColorSpace
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Picture
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.SweepGradient
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalDensity
import kotlin.math.PI
import kotlin.math.max
import kotlin.math.min
import kotlin.math.ceil

/**
 * Native overlay for FlClash's LiquidGlassRim. This is deliberately independent
 * of RuntimeShader: menus still receive the 9-band specular and graded hairline
 * when backdrop capture or AGSL compilation falls back to tinted glass.
 *
 * The original Flutter renderer uses a directional sweep + nine concentric
 * bands, not a single uniform white stroke.
 */
@androidx.annotation.RequiresApi(33)
@Composable
internal fun GlassMenuRim(
    modifier: Modifier,
    platter: Rect,
    radius: Float,
    style: GlassStyle,
    settled: Float,
    outlinePath: Path? = null,
    cornerPower: Float = 3.5f,
) {
    val density = LocalDensity.current.density
    val renderer = remember { GlassMenuRimRenderer() }
    Canvas(modifier) {
        drawIntoCanvas { canvas ->
            renderer.draw(canvas.nativeCanvas, size.width, size.height,
                platter, radius, style, settled, density, outlinePath, cornerPower)
        }
    }
}

/** Retain the complete drawing, including gradients/paints, as FlClash does. */
@androidx.annotation.RequiresApi(33)
internal class GlassMenuRimRenderer {
    private data class Geometry(
        val rect: Rect, val radius: Float, val density: Float,
        val outline: Path?, val power: Float,
    )
    private data class Light(
        val side: Float, val corner: Float, val flat: Float, val shine: Float,
        val strength: Float, val width: Float, val height: Float,
    )
    private var geometry: Geometry? = null
    private var light: Light? = null
    private var paths: GlassMenuRimPaths? = null
    private var picture: Picture? = null

    fun draw(
        target: android.graphics.Canvas, width: Float, height: Float,
        platter: Rect, radius: Float, style: GlassStyle, settled: Float,
        density: Float, outlinePath: Path? = null, cornerPower: Float = 3.5f,
    ) {
        val strength = settled.coerceIn(0f, 1f)
        if (strength <= .005f || platter.width <= 2f || platter.height <= 2f) return
        val nextGeometry = Geometry(platter, radius, density, outlinePath, cornerPower)
        val nextLight = Light(style.menuRimSide, style.menuRimCorner,
            style.menuRimFlat, style.menuRimShine, strength, width, height)
        val pad = ceil(density).toInt()
        val saved = target.save()
        target.translate(-pad.toFloat(), -pad.toFloat())
        if (geometry == nextGeometry && light == nextLight && picture != null) {
            target.drawPicture(picture!!)
            target.restoreToCount(saved)
            return
        }
        if (geometry != nextGeometry) {
            paths = GlassMenuRimPaths(platter, radius, density, cornerPower, outlinePath)
            geometry = nextGeometry
        }
        val paths = paths!!
        val recording = Picture()
        val native = recording.beginRecording(ceil(width).toInt() + pad * 2,
            ceil(height).toInt() + pad * 2)
        native.translate(pad.toFloat(), pad.toFloat())
        val panel = RectF(platter.left, platter.top, platter.right, platter.bottom)
        val rounded = radius.coerceIn(0f, min(panel.width(), panel.height()) * .5f)
        val lineWidth = .67f * density

        val turns = menuRimTurns(panel.width(), panel.height(), rounded)
        val directionalLight = floatArrayOf(1f, .68f, .48f, .38f, .17f, .07f, .03f, 0f)
        val sweepPositions = ArrayList<Float>(29)
        val sweepLights = ArrayList<Float>(29)
        fun stop(position: Float, index: Int) {
            sweepPositions += position.coerceIn(0f, 1f)
            sweepLights += directionalLight[index]
        }
        val tau = (2.0 * PI).toFloat()
        val halfPi = (PI * .5).toFloat()
        for (i in 7 downTo 0) stop((halfPi - turns[i]) / tau, i)
        for (i in 1..7) stop((halfPi + turns[i]) / tau, i)
        for (i in 6 downTo 0) stop((3f * halfPi - turns[i]) / tau, i)
        for (i in 1..7) stop((3f * halfPi + turns[i]) / tau, i)

        val maxLine = max(style.menuRimSide, max(style.menuRimCorner, style.menuRimFlat))
        val shine = style.menuRimShine * strength
        // The glass body and fallback rim now share Flutter's fitted
        // RSuperellipse outline. A separate round-rect would trace a
        // different edge and create displaced borders.
        val outerPath = paths.outer
        val colorSpace = ColorSpace.get(ColorSpace.Named.EXTENDED_SRGB)

        if (maxLine > .0001f) {
            // Dark edge is strongest at the sides, quieter at the corners,
            // weakest across the flat top and bottom, as in FlClash.
            val across = min(rounded / max(panel.width(), 1f), .4f)
            fun black(level: Float): Long =
                Color.pack(0f, 0f, 0f, level * strength, colorSpace)
            val edgePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                this.style = Paint.Style.STROKE
                strokeWidth = lineWidth
                shader = LinearGradient(
                    panel.left, panel.centerY(), panel.right, panel.centerY(),
                    longArrayOf(
                        black(style.menuRimSide),
                        black(style.menuRimCorner),
                        black(style.menuRimFlat),
                        black(style.menuRimFlat),
                        black(style.menuRimCorner),
                        black(style.menuRimSide),
                    ),
                    floatArrayOf(
                        0f, across / 4f, across, 1f - across,
                        1f - across / 4f, 1f,
                    ),
                    Shader.TileMode.CLAMP,
                )
            }
            // LiquidGlassRim.paintLine inflates by half a hairline:
            // the complete stroke sits OUTSIDE the pane, without a clip.
            native.drawPath(
                paths.line,
                edgePaint,
            )
        }

        if (shine > .0001f) {
            val bands = menuShineBands
            // The lens shape can be translucent. Do not replace the tint;
            // use additive light across only the inner five dp of glass.
            val specular = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                this.style = Paint.Style.FILL
                blendMode = BlendMode.PLUS
                shader = SweepGradient(
                    panel.centerX(), panel.centerY(),
                    LongArray(sweepLights.size) { index ->
                        Color.pack(1f, 1f, 1f, sweepLights[index], colorSpace)
                    },
                    sweepPositions.toFloatArray(),
                )
            }
            val shineRestore = native.save()
            native.clipPath(outerPath)
            for (i in bands.indices step 3) {
                val bandWeight = bands[i + 2]
                val ring = paths.rings[i / 3] ?: continue
                // Keep the band amplitude as a float. Encoding it into
                // ARGB8 first erased the faint inner bands completely.
                specular.colorFilter = ColorMatrixColorFilter(ColorMatrix().apply {
                    setScale(1f, 1f, 1f, shine * bandWeight)
                })
                native.drawPath(ring, specular)
            }
            native.restoreToCount(shineRestore)
        }
        recording.endRecording()
        picture = recording
        light = nextLight
        target.drawPicture(recording)
        target.restoreToCount(saved)
    }
}
