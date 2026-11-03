package dev.liquidglass

import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas

/**
 * Keep a single native Paint for the full animation. Android's
 * setShadowLayer creates costly native effect state; on the active top
 * card the blur radius/offset/color are identical for nearly every frame.
 * Never clear and recreate that effect after each draw.
 */
internal class MenuShadowPaintState {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = android.graphics.Color.argb(1, 0, 0, 0)
    }
    private var lastDensity = Float.NaN
    private var lastShadeColor = Int.MIN_VALUE
    private val shadowBounds = RectF()
    private val exteriorClip = Path()

    /** popup.dart::_paintShadows excludes the interior before drawing blur. */
    fun exteriorClipOf(outline: Path, density: Float): Path {
        outline.computeBounds(shadowBounds, true)
        // Flutter's shadow spread: blurSigma * 3 + offset.distance.
        val reach = (29f * .57735f + .5f) * 3f * density + 8f * density
        shadowBounds.inset(-reach, -reach)
        exteriorClip.rewind()
        exteriorClip.fillType = Path.FillType.EVEN_ODD
        exteriorClip.addRect(shadowBounds, Path.Direction.CW)
        exteriorClip.addPath(outline)
        return exteriorClip
    }

    fun configure(density: Float, shadeColor: Int) {
        if (lastDensity == density && lastShadeColor == shadeColor) return
        paint.setShadowLayer(
            29f * .57735f * density + .5f,
            0f,
            8f * density,
            shadeColor,
        )
        lastDensity = density
        lastShadeColor = shadeColor
    }
}

/**
 * FlClash menuShadows: one 29dp blur-radius, 8dp down shadow with
 * .118 light / .177 dark alpha, multiplied by _buildLevel's distance fade.
 *
 * This is a separate Canvas decoration BEHIND the Sketch material.
 * No Sketch sampling, lens, shader, or FlClash rim parameters change.
 */
@Composable
internal fun MenuCardShadow(
    path: Path,
    density: Float,
    dark: Boolean,
    strength: Float,
    modifier: Modifier = Modifier,
    clipInterior: Boolean = false,
) {
    val shadowPaint = remember { MenuShadowPaintState() }
    Canvas(modifier) {
        val shade = (.118f * (if (dark) 1.5f else 1f) * strength)
            .coerceIn(0f, 1f)
        if (shade <= 0f) return@Canvas
        // Android setShadowLayer accepts sigma-like blur radius; Flutter
        // converts BoxShadow.blurRadius 29 into sigma ~17.25.
        shadowPaint.configure(
            density, android.graphics.Color.argb((255f * shade).toInt(), 0, 0, 0),
        )
        drawIntoCanvas {
            val native = it.nativeCanvas
            if (clipInterior) {
                val save = native.save()
                native.clipPath(shadowPaint.exteriorClipOf(path, density))
                native.drawPath(path, shadowPaint.paint)
                native.restoreToCount(save)
            } else {
                native.drawPath(path, shadowPaint.paint)
            }
        }
    }
}
