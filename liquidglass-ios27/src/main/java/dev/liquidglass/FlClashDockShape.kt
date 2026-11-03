package dev.liquidglass

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.unit.LayoutDirection

/** Flutter's AppShape.full / LiquidGlassCorners, with Sketch-readable optical radii. */
internal class FlClashDockShape(
    topStart: CornerSize = CornerSize(50),
    topEnd: CornerSize = topStart,
    bottomEnd: CornerSize = topStart,
    bottomStart: CornerSize = topStart,
) : CornerBasedShape(topStart, topEnd, bottomEnd, bottomStart) {
    override fun createOutline(
        size: Size, topStart: Float, topEnd: Float,
        bottomEnd: Float, bottomStart: Float, layoutDirection: LayoutDirection,
    ): Outline = Outline.Generic(gooPlatterOutline(
        Rect(0f, 0f, size.width, size.height), size.minDimension * .5f, cornerPower = 2f,
    ).asComposePath())

    override fun copy(topStart: CornerSize, topEnd: CornerSize,
        bottomEnd: CornerSize, bottomStart: CornerSize): CornerBasedShape =
        FlClashDockShape(topStart, topEnd, bottomEnd, bottomStart)
}
