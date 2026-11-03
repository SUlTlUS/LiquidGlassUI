package dev.liquidglass

import android.graphics.Matrix
import android.graphics.Path
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import com.kyant.shapes.Capsule
import kotlin.math.min

internal data class ButtonPaneContour(val path: Path, val highlightPill: GlassPill)
internal val LocalButtonPaneContour = staticCompositionLocalOf<ButtonPaneContour?> { null }

internal fun usesStretchedButtonContour(effect: GlassStandard?): Boolean = effect == GlassStandard.Ios26

/** Profile 26 stretches the original capsule; 27 redraws it at its live size. */
internal fun buttonPillOutline(pill: GlassPill): Path {
    val bounds = Rect(pill.centerX - pill.halfWidth, pill.centerY - pill.halfHeight,
        pill.centerX + pill.halfWidth, pill.centerY + pill.halfHeight)
    if (!usesStretchedButtonContour(pill.buttonEffect) || pill.restHalfWidth <= 0f || pill.restHalfHeight <= 0f) {
        return gooPlatterOutline(bounds, min(bounds.width, bounds.height) * .5f, 2f)
    }
    val size = Size(pill.restHalfWidth * 2f, pill.restHalfHeight * 2f)
    val outline = Capsule().createOutline(size, LayoutDirection.Ltr, Density(1f))
    val path = when (outline) {
        is Outline.Generic -> Path(outline.path.asAndroidPath())
        is Outline.Rounded -> androidx.compose.ui.graphics.Path().apply { addRoundRect(outline.roundRect) }.asAndroidPath()
        is Outline.Rectangle -> Path().apply { addRect(0f, 0f, size.width, size.height, Path.Direction.CW) }
    }
    path.transform(Matrix().apply {
        setScale(pill.halfWidth / pill.restHalfWidth, pill.halfHeight / pill.restHalfHeight)
        postTranslate(bounds.left, bounds.top)
    })
    return path
}

internal fun buttonLocalOutline(size: Size, scaleX: Float, scaleY: Float, effect: GlassStandard): Path {
    if (size.width <= 0f || size.height <= 0f) return Path()
    val path = buttonPillOutline(GlassPill(size.width / 2f, size.height / 2f,
        size.width * scaleX / 2f, size.height * scaleY / 2f, effect, size.width / 2f, size.height / 2f))
    // The owning graphics layer applies the same scale to material and foreground.
    path.transform(Matrix().apply { setScale(1f / scaleX, 1f / scaleY, size.width / 2f, size.height / 2f) })
    return path
}

/** Analytic rim in the same pre-transform coordinates as [buttonLocalOutline]. */
internal fun buttonLocalHighlightPill(size: Size, scaleX: Float, scaleY: Float, effect: GlassStandard): GlassPill {
    val halfWidth = size.width / 2f
    val halfHeight = size.height / 2f
    val stretch = usesStretchedButtonContour(effect)
    // The outer layer applies the motion scale. A redrawn contour must first
    // undo it locally; the shader's stretched-pill representation carries that
    // inverse transform without resampling the path into 64 radial directions.
    return GlassPill(halfWidth, halfHeight, halfWidth, halfHeight,
        GlassStandard.Ios26,
        halfWidth * if (stretch) 1f else scaleX,
        halfHeight * if (stretch) 1f else scaleY)
}

internal data class ButtonContourShape(private val effect: GlassStandard, private val sx: Float, private val sy: Float) :
    CornerBasedShape(CornerSize(50), CornerSize(50), CornerSize(50), CornerSize(50)) {
    fun highlightPill(size: Size): GlassPill = buttonLocalHighlightPill(size, sx, sy, effect)

    override fun createOutline(size: Size, topStart: Float, topEnd: Float, bottomEnd: Float,
        bottomStart: Float, layoutDirection: LayoutDirection): Outline =
        Outline.Generic(buttonLocalOutline(size, sx, sy, effect).asComposePath())

    override fun copy(topStart: CornerSize, topEnd: CornerSize, bottomEnd: CornerSize, bottomStart: CornerSize): CornerBasedShape =
        ButtonContourShape(effect, sx, sy)
}

internal fun buttonPill(bounds: Rect, frame: MenuElasticTransform, effect: GlassStandard) =
    GlassPill(bounds.center.x + frame.pull.x, bounds.center.y + frame.pull.y,
        bounds.width * frame.scaleX / 2f, bounds.height * frame.scaleY / 2f,
        effect, bounds.width / 2f, bounds.height / 2f)
