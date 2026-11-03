package dev.liquidglass27.glass

import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Shader
import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap

/** Region in one BitmapShader: raw scene at (0, 0), reduced frost tiles to its right. */
internal data class GlassAtlasRegion(
    val sigmaPx: Float,
    val x: Float,
    val y: Float,
    val scaleX: Float,
    val scaleY: Float,
)

internal data class GlassCombinedAtlas(
    val image: ImageBitmap,
    val shader: BitmapShader,
    val regions: List<GlassAtlasRegion>,
)

/**
 * A single shader input shared by all panes; no additional AGSL child shaders.
 * Only static backgrounds use this CPU packing stage. It is not a replacement
 * for FlClash's GPU-resident host-layer atlas or its per-pane occlusion bands.
 */
@androidx.annotation.RequiresApi(33)
internal fun combineBackdropFrostAtlas(
    source: ImageBitmap,
    levels: List<GlassBackdropBand>,
): GlassCombinedAtlas? {
    val entries = levels.take(3)
    if (entries.isEmpty() || source.width <= 0 || source.height <= 0) return null
    val padding = 2
    val rightWidth = entries.maxOf { it.image.width }
    val rightHeight = entries.sumOf { it.image.height + padding }
    val width = source.width + padding + rightWidth
    val height = maxOf(source.height, rightHeight)
    if (width > 8192 || height > 8192 || width.toLong() * height > 20_000_000L) {
        return null
    }

    val atlas = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(atlas)
    val paint = Paint(Paint.FILTER_BITMAP_FLAG)
    fun copyForCanvas(image: ImageBitmap): Bitmap =
        image.asAndroidBitmap().let {
            if (it.config == Bitmap.Config.HARDWARE) {
                it.copy(Bitmap.Config.ARGB_8888, false)
                    ?: error("Backdrop atlas source is not CPU drawable")
            } else it
        }
    return try {
        val raw = copyForCanvas(source)
        try {
            canvas.drawBitmap(raw, 0f, 0f, paint)
        } finally {
            if (raw !== source.asAndroidBitmap()) raw.recycle()
        }
        val regions = ArrayList<GlassAtlasRegion>(entries.size)
        var y = 0
        for (level in entries) {
            val image = copyForCanvas(level.image)
            val x = source.width + padding
            try {
                canvas.drawBitmap(image, x.toFloat(), y.toFloat(), paint)
            } finally {
                if (image !== level.image.asAndroidBitmap()) image.recycle()
            }
            regions.add(
                GlassAtlasRegion(
                    sigmaPx = level.sigmaPx,
                    x = x.toFloat(),
                    y = y.toFloat(),
                    scaleX = level.image.width.toFloat() / source.width,
                    scaleY = level.image.height.toFloat() / source.height,
                )
            )
            y += level.image.height + padding
        }
        GlassCombinedAtlas(
            image = atlas.asImageBitmap(),
            shader = BitmapShader(atlas, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP),
            regions = regions,
        )
    } catch (failure: RuntimeException) {
        Log.w("LiquidGlass27", "Single-input frost atlas unavailable", failure)
        atlas.recycle()
        null
    }
}
