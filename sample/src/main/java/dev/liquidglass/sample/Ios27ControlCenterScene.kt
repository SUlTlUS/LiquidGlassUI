package dev.liquidglass.sample

import android.content.Context
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.kyant.shapes.RoundedRectangle
import com.zeaze.tianyinwallpaper.backdrop.backdrops.LayerBackdrop
import com.zeaze.tianyinwallpaper.backdrop.backdrops.layerBackdrop
import com.zeaze.tianyinwallpaper.backdrop.drawBackdrop
import com.zeaze.tianyinwallpaper.backdrop.effects.blur
import com.zeaze.tianyinwallpaper.backdrop.effects.lens
import com.zeaze.tianyinwallpaper.backdrop.effects.vibrancy
import dev.liquidglass.ios27.ios27PressedClickable
import org.json.JSONArray
import org.json.JSONObject

private const val ControlStatusBarPt = 62f
private const val ControlWallpaperHash = "9bddf87eae143962a7ecc5a5e617b20f45bacb69"

private data class ControlTile(
    val name: String,
    val x: Float,
    val y: Float,
    val w: Float,
    val h: Float,
    val radius: Float,
    val texts: List<Triple<Float, Float, SceneText>>,
)

private data class ControlCenterData(
    val tiles: List<ControlTile>,
    val music: ControlTile,
    val group: ControlTile,
    val lockScreenTexts: List<Triple<Float, Float, SceneText>>,
    val controlTexts: List<Triple<Float, Float, SceneText>>,
)

private fun directChild(parent: JSONObject, name: String): JSONObject? =
    parent.optJSONArray("children")?.let { children ->
        (0 until children.length()).map(children::getJSONObject).firstOrNull { it.optString("name") == name }
    }

private fun matrixValue(node: JSONObject, index: Int): Float =
    node.optJSONArray("matrix")?.optDouble(index, 0.0)?.toFloat() ?: 0f

private fun controlTile(node: JSONObject, name: String, radius: Float): ControlTile {
    val x = matrixValue(node, 2)
    val y = matrixValue(node, 5)
    val texts = ArrayList<Triple<Float, Float, SceneText>>()
    collectTexts(node, 0f, 0f, texts)
    return ControlTile(name, x, y, node.optDouble("w").toFloat(), node.optDouble("h").toFloat(), radius, texts)
}

private fun loadControlCenterData(context: Context, file: String): ControlCenterData? {
    val scene = loadScene(context, file)
    val root = scene.optJSONObject("scene") ?: scene
    val page = findNodePrefix(root, "Control Center - iPhone") ?: return null
    val lockScreen = directChild(page, "Lock Screen - iPhone") ?: return null
    val group = directChild(page, "Group") ?: return null
    val music = directChild(page, "Music") ?: return null
    val names = listOf(
        "1x1 - Camera", "1x1 - Silent Mode", "2x1 - Focus", "Brightness", "Volume",
        "2x2 - Floor Lamp", "1x1 - Rotation Lock", "1x1 - Screen Sharing", "1x1 - Battery",
    )
    val tiles = names.mapNotNull { name -> directChild(page, name)?.let { node ->
        controlTile(node, name, if (node.optDouble("w") > 100) 30f else 22f)
    } }
    val lockTexts = ArrayList<Triple<Float, Float, SceneText>>()
    collectTexts(lockScreen, 0f, 0f, lockTexts)
    // Controls are painted separately above the dimmed lock-screen content.
    lockTexts.removeAll { it.second >= 700f }
    val controls = findNode(lockScreen, "Controls")
    val controlTexts = ArrayList<Triple<Float, Float, SceneText>>()
    if (controls != null) collectTexts(controls, 0f, 0f, controlTexts)
    return ControlCenterData(tiles, controlTile(music, "Music", 30f), controlTile(group, "Group", 30f),
        lockTexts, controlTexts)
}

private fun Modifier.controlPos(x: Float, y: Float): Modifier =
    offset(x.dp, (y - ControlStatusBarPt).dp)

private fun Modifier.controlBox(x: Float, y: Float, w: Float, h: Float): Modifier =
    controlPos(x, y).requiredSize(w.dp, h.dp)

private fun DrawScope.drawControlWallpaper(image: ImageBitmap?) {
    if (image == null) return
    drawImage(
        image,
        dstOffset = androidx.compose.ui.unit.IntOffset(0, (-ControlStatusBarPt * density).toInt()),
        dstSize = androidx.compose.ui.unit.IntSize((402f * density).toInt(), (874f * density).toInt()),
    )
}

@Composable
private fun ControlGlass(
    x: Float,
    y: Float,
    w: Float,
    h: Float,
    radius: Float,
    backdrop: LayerBackdrop?,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit = {},
) {
    val shape = RoundedCornerShape(radius.dp)
    Box(
        Modifier.controlBox(x, y, w, h)
            .then(
                if (onClick != null) {
                    Modifier.ios27PressedClickable(
                        shape = shape,
                        onClick = onClick,
                    )
                } else {
                    Modifier.clip(shape)
                }
            ),
    ) {
        if (backdrop != null) {
            Box(Modifier.fillMaxSize().drawBackdrop(
                backdrop = backdrop,
                shape = { RoundedRectangle(radius.dp) },
                effects = { vibrancy(); blur(2.dp.toPx()); lens(12.dp.toPx(), 24.dp.toPx()) },
                highlight = { null },
                onDrawSurface = { drawRect(Color(0.6f, 0.6f, 0.6f, 0.33f)) },
            ))
        } else {
            Box(Modifier.fillMaxSize().background(Color(0x66111113)))
        }
        content()
    }
}

@Composable
private fun ControlTexts(
    texts: List<Triple<Float, Float, SceneText>>,
    originX: Float,
    originY: Float,
    fallback: Color = Color.White,
) {
    texts.forEach { (x, y, text) ->
        Box(Modifier.offset((x - originX).dp, (y - originY).dp)) { SceneGlyphText(text, text.color ?: fallback) }
    }
}

@Composable
private fun ControlTileView(
    tile: ControlTile,
    backdrop: LayerBackdrop?,
    enabled: Boolean,
    onClick: () -> Unit,
    fill: Float? = null,
) {
    ControlGlass(tile.x, tile.y, tile.w, tile.h, tile.radius, backdrop, onClick) {
        fill?.let { amount ->
            Box(
                Modifier.offset(0.dp, (tile.h * (1f - amount)).dp)
                    .requiredSize(tile.w.dp, (tile.h * amount).dp)
                    .clip(RoundedCornerShape(tile.radius.dp))
                    .background(Color.White.copy(alpha = if (enabled) 0.66f else 0.22f)),
            )
        }
        ControlTexts(tile.texts, tile.x, tile.y, if (enabled) Color.White else Color(0xFFBFBFBF))
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun ControlCenterScene(
    entry: IphoneExample,
    backdrop: LayerBackdrop?,
    onReference: () -> Unit,
    enabled: Boolean,
    secondary: Boolean,
    value: Float,
    onEnabledChange: (Boolean) -> Unit,
    onSecondaryChange: (Boolean) -> Unit,
    onValueChange: (Float) -> Unit,
) {
    val context = LocalContext.current
    Box(Modifier.fillMaxSize()) {
        val data = remember(entry.file) { loadControlCenterData(context, entry.file) } ?: return@Box
        val wallpaper = rememberSceneImage(ControlWallpaperHash)
        Box(Modifier.fillMaxSize().clipToBounds()) {
            Box(Modifier.fillMaxSize().then(if (backdrop != null) Modifier.layerBackdrop(backdrop) else Modifier)
                .drawBehind {
                    drawControlWallpaper(wallpaper)
                    // Control Center is presented over the lock screen with the authored 50% dim layer.
                    drawRect(Color.Black.copy(alpha = 0.5f))
                }
                .combinedClickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = {},
                    onLongClick = onReference,
                )) {
                ControlTexts(data.lockScreenTexts, 0f, 0f, Color.White.copy(alpha = 0.58f))
            }

            ControlTileView(data.group, backdrop, enabled, { onEnabledChange(!enabled) })
            ControlTileView(data.music, backdrop, secondary, { onSecondaryChange(!secondary) })
            data.tiles.forEach { tile ->
                val click = when (tile.name) {
                    "Brightness", "Volume" -> { { onValueChange(if (value < 0.5f) 1f else 0f) } }
                    "1x1 - Camera", "1x1 - Silent Mode", "2x1 - Focus" -> { { onSecondaryChange(!secondary) } }
                    else -> { { onEnabledChange(!enabled) } }
                }
                val fill = when (tile.name) {
                    "Brightness" -> value
                    "Volume" -> value
                    else -> null
                }
                ControlTileView(tile, backdrop, enabled, click, fill)
            }

            // The original page control is a narrow stack of five muted symbols. Keeping it outside the
            // tile hit targets preserves the native page swipe/tap behaviour while matching its rhythm.
            Canvas(Modifier.controlBox(372f, 290f, 21.33f, 240.55f)) {
                repeat(5) { index ->
                    drawRoundRect(
                        Color.White.copy(alpha = 0.62f),
                        topLeft = androidx.compose.ui.geometry.Offset(7.dp.toPx(), (2f + index * 55.2f).dp.toPx()),
                        size = androidx.compose.ui.geometry.Size(7.dp.toPx(), 14.dp.toPx()),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(4.dp.toPx()),
                    )
                }
            }

            ControlGlass(46f, 766f, 58f, 58f, 29f, backdrop, onClick = { onSecondaryChange(!secondary) })
            ControlGlass(298f, 766f, 58f, 58f, 29f, backdrop, onClick = { onEnabledChange(!enabled) })
            ControlTexts(data.controlTexts, 0f, 0f, Color.White.copy(alpha = 0.72f))
        }
    }
}
