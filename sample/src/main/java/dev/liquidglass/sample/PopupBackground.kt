package dev.liquidglass.sample

import android.content.Intent
import android.graphics.ImageDecoder
import android.graphics.BitmapFactory
import android.os.Build
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.max
import kotlin.math.roundToInt

internal class PopupBackground(val image: ImageBitmap?, val choose: () -> Unit, val clear: () -> Unit)

@Composable
internal fun rememberPopupBackground(onError: (String) -> Unit): PopupBackground {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("popup_background", 0) }
    var uri by remember { mutableStateOf(prefs.getString("uri", null)) }
    val image by produceState<ImageBitmap?>(null, uri) {
        value = null
        val selected = uri ?: return@produceState
        val result = withContext(Dispatchers.IO) {
            runCatching {
                if (Build.VERSION.SDK_INT < 28) {
                    val source = Uri.parse(selected)
                    val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }
                    context.contentResolver.openInputStream(source)?.use { BitmapFactory.decodeStream(it, null, options) }
                    while (max(options.outWidth, options.outHeight) / options.inSampleSize > 2048) options.inSampleSize *= 2
                    options.inJustDecodeBounds = false
                    requireNotNull(context.contentResolver.openInputStream(source)?.use {
                        BitmapFactory.decodeStream(it, null, options)
                    }).asImageBitmap()
                } else ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.contentResolver, Uri.parse(selected))) { decoder, info, _ ->
                    val scale = minOf(1f, 2048f / max(info.size.width, info.size.height))
                    decoder.setTargetSize(max(1, (info.size.width * scale).roundToInt()), max(1, (info.size.height * scale).roundToInt()))
                    decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
                }.asImageBitmap()
            }
        }
        value = result.getOrNull()
        if (result.isFailure) onError("背景图片无法读取，请重新选择")
    }
    val picker = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { selected ->
        if (selected != null) {
            runCatching { context.contentResolver.takePersistableUriPermission(selected, Intent.FLAG_GRANT_READ_URI_PERMISSION) }
            uri = selected.toString()
            prefs.edit().putString("uri", uri).apply()
        }
    }
    return PopupBackground(image, { picker.launch(arrayOf("image/*")) }, {
        uri = null
        prefs.edit().remove("uri").apply()
    })
}

internal fun Modifier.popupBackground(image: ImageBitmap?): Modifier = drawBehind {
    if (image != null && size.width > 0 && size.height > 0) {
        val scale = max(size.width / image.width, size.height / image.height)
        val width = (size.width / scale).roundToInt().coerceIn(1, image.width)
        val height = (size.height / scale).roundToInt().coerceIn(1, image.height)
        drawImage(image, srcOffset = IntOffset((image.width - width) / 2, (image.height - height) / 2),
            srcSize = IntSize(width, height), dstSize = IntSize(size.width.roundToInt(), size.height.roundToInt()))
    }
}
