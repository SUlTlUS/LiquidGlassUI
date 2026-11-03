package dev.liquidglass.sample

import android.graphics.BitmapFactory
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import com.kyant.shapes.RoundedRectangle
import com.zeaze.tianyinwallpaper.backdrop.backdrops.LayerBackdrop
import com.zeaze.tianyinwallpaper.backdrop.backdrops.layerBackdrop
import com.zeaze.tianyinwallpaper.backdrop.drawBackdrop
import com.zeaze.tianyinwallpaper.backdrop.effects.blur
import com.zeaze.tianyinwallpaper.backdrop.effects.lens
import com.zeaze.tianyinwallpaper.backdrop.effects.vibrancy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.roundToInt
import org.json.JSONArray
import org.json.JSONObject

/**
 * Pixel-faithful rebuild of the home-screen scene originals (Home Screen, Home Screen Widgets).
 * Geometry, colors, glyph texts and image hashes are all driven by the exported scene JSON;
 * rendering uses Compose plus the existing liquid-glass backdrop API. Scene pt = 1dp inside the
 * caller's pt-aligned density; the 402x874pt canvas is top-aligned and clipped like FigmaSceneView.
 */
internal class HomeIcon(val hash: String, val x: Float, val y: Float, val size: Float,
    val name: SceneText?, val nameX: Float, val nameY: Float, val badge: HomeBadge? = null)

/** Red app-icon badge: circle fill and its white number glyph, both taken from the scene JSON. */
internal class HomeBadge(val x: Float, val y: Float, val size: Float, val color: Color,
    val text: Triple<Float, Float, SceneText>?) {
    /** True when a text origin falls inside the badge, i.e. it is the number and not the App Name. */
    fun contains(tx: Float, ty: Float): Boolean = tx >= x - 1f && tx < x + size && ty >= y - 1f && ty < y + size
}

internal class HomeGlassPanel(val x: Float, val y: Float, val w: Float, val h: Float, val radius: Float)

/**
 * One authored vector fill, baked into the drawing space of its glass panel: pixel coordinates relative to
 * the panel origin, so rendering is a plain `drawPath` without any DrawScope transform.
 */
internal class HomeVector(val path: Path, val color: Color)

internal class HomeWidget(
    val x: Float, val y: Float, val w: Float, val h: Float, val radius: Float,
    val topColor: Color, val bottomColor: Color,
    val guideX: Float, val guideY: Float, val guideW: Float, val guideH: Float,
    val guideRadius: Float, val guideOpacity: Float,
    val texts: List<Triple<Float, Float, SceneText>>,
)

internal class HomeScreenSceneData(
    val wallpaperHash: String?,
    val wallpaperH: Float,
    // Authored height of the wallpaper geometry, which clips the viewport-fitted image draw.
    val wallpaperClipH: Float,
    val icons: List<HomeIcon>,
    val widget: HomeWidget?,
    val search: HomeGlassPanel?,
    val searchTexts: List<Triple<Float, Float, SceneText>>,
    val dock: HomeGlassPanel?,
    val dockIcons: List<HomeIcon>,
    // Quick Actions overlay: dim sheet, enlarged icon and the glass context menu (35187).
    val overlayAlpha: Float,
    // Authored height of the Overlay geometry: the dim stops there, the fitted tail stays bright.
    val overlayH: Float,
    val bigIcon: HomeIcon?,
    val menu: HomeGlassPanel?,
    val menuTexts: List<Triple<Float, Float, SceneText>>,
    val menuVectors: List<HomeVector>,
)

private fun jsonColor(node: JSONObject?): Color? = node?.let {
    Color(it.optDouble("r").toFloat(), it.optDouble("g").toFloat(), it.optDouble("b").toFloat(),
        it.optDouble("a", 1.0).toFloat())
}

private fun firstImageHash(node: JSONObject): String? {
    val fills = node.optJSONArray("fills")
    if (fills != null) for (i in 0 until fills.length()) {
        val fill = fills.optJSONObject(i) ?: continue
        if (fill.optString("type") == "IMAGE") return fill.optString("imageHash").ifBlank { null }
    }
    val children = node.optJSONArray("children") ?: return null
    for (i in 0 until children.length()) firstImageHash(children.getJSONObject(i))?.let { return it }
    return null
}

private fun nodeX(node: JSONObject): Float = (node.optJSONArray("matrix")?.optDouble(2) ?: 0.0).toFloat()
private fun nodeY(node: JSONObject): Float = (node.optJSONArray("matrix")?.optDouble(5) ?: 0.0).toFloat()

/** App-icon instances carry an Icon SYMBOL image plus an App Name text; Empty frames are placeholders. */
private fun collectAppIcons(node: JSONObject, ox: Float, oy: Float, out: MutableList<HomeIcon>) {
    val x = ox + nodeX(node)
    val y = oy + nodeY(node)
    if (node.optString("name").startsWith("App Icon") && node.optString("type") == "INSTANCE") {
        val icon = findNode(node, "Icon")
        val hash = icon?.let { firstImageHash(it) } ?: return
        // The badge is a sibling text inside the icon, so it must not be mistaken for the App Name.
        val badgeNode = findNode(node, "Badge")
        val badgeTexts = ArrayList<Triple<Float, Float, SceneText>>()
        val badge = badgeNode?.let {
            collectTexts(it, x, y, badgeTexts)
            HomeBadge(x + nodeX(it), y + nodeY(it), it.optDouble("w").toFloat(),
                jsonColor(it.optJSONArray("fills")?.optJSONObject(0)?.optJSONObject("color")) ?: Color(1f, 0.22f, 0.24f),
                badgeTexts.firstOrNull())
        }
        var name: SceneText? = null
        var nameX = 0f
        var nameY = 0f
        val texts = ArrayList<Triple<Float, Float, SceneText>>()
        // collectTexts adds the node's own matrix, so pass the parent origin.
        collectTexts(node, ox, oy, texts)
        texts.firstOrNull { entry -> badge?.contains(entry.first, entry.second) != true }
            ?.let { nameX = it.first; nameY = it.second; name = it.third }
        out.add(HomeIcon(hash, x, y, node.optDouble("w").toFloat(), name, nameX, nameY, badge))
        return
    }
    val children = node.optJSONArray("children") ?: return
    for (i in 0 until children.length()) collectAppIcons(children.getJSONObject(i), x, y, out)
}

private fun parseGlassPanel(node: JSONObject, ox: Float, oy: Float): HomeGlassPanel {
    val x = ox + nodeX(node)
    val y = oy + nodeY(node)
    val fill = findNode(node, "Fill + Shadow")
    return HomeGlassPanel(x, y, node.optDouble("w").toFloat(), node.optDouble("h").toFloat(),
        (fill?.optDouble("radius") ?: node.optDouble("radius")).toFloat())
}

/**
 * Same Figma command encoding as FigmaSceneView.path: 0 close, 1 move, 2 line, 3 quad, 4 cubic. The commands
 * are node-local pt, so they are scaled to pixels and shifted to the panel-relative origin while reading.
 */
private fun figmaPath(commands: JSONArray, scale: Float, dx: Float, dy: Float): Path = Path().apply {
    fun sx(v: Int) = commands.getDouble(v).toFloat() * scale + dx
    fun sy(v: Int) = commands.getDouble(v + 1).toFloat() * scale + dy
    var i = 0
    while (i < commands.length()) when (commands.getInt(i++)) {
        0 -> close()
        1 -> { moveTo(sx(i), sy(i)); i += 2 }
        2 -> { lineTo(sx(i), sy(i)); i += 2 }
        3 -> { quadraticTo(sx(i), sy(i), sx(i + 2), sy(i + 2)); i += 4 }
        4 -> { cubicTo(sx(i), sy(i), sx(i + 2), sy(i + 2), sx(i + 4), sy(i + 4)); i += 6 }
        else -> error("Unsupported Figma path command")
    }
}

/**
 * Collect the solid vector fills of a subtree in paint order (parent before child). Used for the context
 * menu's Widgets row, which is pure `paths` geometry: the 237 slot rounded rects and the SF Symbol outlines
 * carry no text and no image hash, so nothing else on the page draws them.
 */
private fun collectVectors(
    node: JSONObject, ox: Float, oy: Float, originX: Float, originY: Float, density: Float,
    out: MutableList<HomeVector>,
) {
    val x = ox + nodeX(node)
    val y = oy + nodeY(node)
    val geometry = node.optJSONArray("paths")
    val fills = node.optJSONArray("fills")
    if (geometry != null && fills != null) for (f in 0 until fills.length()) {
        val fill = fills.optJSONObject(f) ?: continue
        if (fill.optString("type") != "SOLID") continue
        val color = jsonColor(fill.optJSONObject("color")) ?: continue
        for (g in 0 until geometry.length()) {
            val shape = geometry.optJSONObject(g) ?: continue
            val commands = shape.optJSONArray("commands") ?: continue
            out.add(HomeVector(
                figmaPath(commands, density, (x - originX) * density, (y - originY) * density).apply {
                    if (shape.optString("rule") == "ODD") fillType = PathFillType.EvenOdd
                }, color))
        }
    }
    val children = node.optJSONArray("children") ?: return
    for (i in 0 until children.length())
        collectVectors(children.getJSONObject(i), x, y, originX, originY, density, out)
}

/** Linear-dodge gradient on top of the solid base is pre-baked into endpoint colors. */
private fun dodge(base: Color, add: Color): Color = Color(
    (base.red + add.red).coerceAtMost(1f), (base.green + add.green).coerceAtMost(1f),
    (base.blue + add.blue).coerceAtMost(1f), 1f)

private fun parseWidget(node: JSONObject, ox: Float, oy: Float): HomeWidget? {
    val x = ox + nodeX(node)
    val y = oy + nodeY(node)
    val background = findNode(node, "Background") ?: return null
    val base = jsonColor(background.optJSONArray("fills")?.optJSONObject(0)?.optJSONObject("color"))
        ?: Color(0xFF6155F5)
    val stops = background.optJSONArray("fills")?.optJSONObject(1)?.optJSONArray("stops")
    val topAdd = jsonColor(stops?.optJSONObject(0)?.optJSONObject("color")) ?: Color.Black
    val bottomAdd = jsonColor(stops?.optJSONObject(1)?.optJSONObject("color")) ?: Color.Black
    val guide = findNode(node, "Padding Guide")
    val texts = ArrayList<Triple<Float, Float, SceneText>>()
    collectTexts(node, ox, oy, texts)
    return HomeWidget(
        x = x, y = y, w = node.optDouble("w").toFloat(), h = node.optDouble("h").toFloat(),
        radius = background.optDouble("radius", 28.0).toFloat(),
        topColor = dodge(base, topAdd), bottomColor = dodge(base, bottomAdd),
        guideX = x + (guide?.let { nodeX(it) } ?: 18f), guideY = y + (guide?.let { nodeY(it) } ?: 18f),
        guideW = guide?.optDouble("w")?.toFloat() ?: 0f, guideH = guide?.optDouble("h")?.toFloat() ?: 0f,
        guideRadius = guide?.optDouble("radius", 11.0)?.toFloat() ?: 11f,
        guideOpacity = guide?.optDouble("opacity", 0.3)?.toFloat() ?: 0.3f,
        texts = texts,
    )
}

/**
 * Same viewport fitting as FigmaSceneView.fitToViewport: the original canvas is 874pt tall, but the
 * reference renderer grows STRETCH nodes and slides MAX/CENTER anchored nodes so the scene fills the
 * real viewport. Without this the wallpaper crop and the dock drift by the delta (tens of pt).
 */
private fun fitSceneToViewport(node: JSONObject, parentDelta: Float, isRoot: Boolean): JSONObject {
    val copy = JSONObject(node.toString())
    val originalHeight = copy.optDouble("h", 0.0)
    val constraint = copy.optString("verticalConstraint")
    val ownDelta = if (isRoot || constraint == "STRETCH") parentDelta.toDouble() else 0.0
    if (!isRoot) {
        val shift = when (constraint) { "MAX" -> parentDelta; "CENTER" -> parentDelta / 2f; else -> 0f }
        copy.optJSONArray("matrix")?.let { it.put(5, it.optDouble(5) + shift.toDouble()) }
    }
    if (ownDelta != 0.0) copy.put("h", (originalHeight + ownDelta).coerceAtLeast(0.0))
    copy.optJSONArray("children")?.let { children ->
        for (i in 0 until children.length())
            children.put(i, fitSceneToViewport(children.getJSONObject(i), ownDelta.toFloat(), false))
    }
    return copy
}

internal fun loadHomeScreenScene(
    context: android.content.Context, file: String, deltaPt: Float, density: Float,
): HomeScreenSceneData? {
    val loaded = loadScene(context, file)
    val source = loaded.optJSONObject("scene") ?: loaded
    val root = if (deltaPt == 0f) source else fitSceneToViewport(source, deltaPt, true)
    val home = findNodePrefix(root, "Home Screen - iPhone") ?: root.optJSONArray("children")?.optJSONObject(0) ?: return null
    if (findNode(home, "Wallpaper") == null) return null
    var wallpaperHash: String? = null
    var wallpaperH = 874f
    var wallpaperClipH = 874f
    val icons = ArrayList<HomeIcon>()
    var widget: HomeWidget? = null
    var search: HomeGlassPanel? = null
    var searchTexts = ArrayList<Triple<Float, Float, SceneText>>()
    var dock: HomeGlassPanel? = null
    val dockIcons = ArrayList<HomeIcon>()
    val hx = nodeX(home)
    val hy = nodeY(home)
    val children = home.optJSONArray("children") ?: return null
    // FigmaSceneView scales the wallpaper to the viewport-fitted box but clips it with the node's authored
    // `paths` rect (0..874pt), so the grown tail is cut off instead of shown: keep both heights.
    val authoredWallpaper = findNodePrefix(source, "Home Screen - iPhone")?.let { findNode(it, "Wallpaper") }
        ?: findNode(source, "Wallpaper")
    for (i in 0 until children.length()) {
        val child = children.getJSONObject(i)
        when (child.optString("name")) {
            "Wallpaper" -> {
                wallpaperHash = firstImageHash(child)
                wallpaperH = child.optDouble("h").toFloat()
                wallpaperClipH = (authoredWallpaper ?: child).optDouble("h").toFloat()
            }
            "App Icons" -> collectAppIcons(child, hx, hy, icons)
            "Search Field" -> {
                search = parseGlassPanel(child, hx, hy)
                collectTexts(child, hx, hy, searchTexts)
            }
            "Dock" -> {
                // The outer INSTANCE is also named Dock; the sized frame is its direct child.
                val frame = child.optJSONArray("children")?.let { c ->
                    (0 until c.length()).map(c::getJSONObject).firstOrNull { it.optString("name") == "Dock" }
                } ?: child
                dock = parseGlassPanel(frame, hx + nodeX(child), hy + nodeY(child))
                frame.optJSONArray("children")?.let { dockChildren ->
                    for (d in 0 until dockChildren.length()) {
                        val dockChild = dockChildren.getJSONObject(d)
                        if (dockChild.optString("name").startsWith("App Icons"))
                            collectAppIcons(dockChild, hx + nodeX(child) + nodeX(frame), hy + nodeY(child) + nodeY(frame), dockIcons)
                    }
                }
            }
            "Widget" -> widget = parseWidget(child, hx, hy)
        }
    }
    // Quick Actions pages layer a dim overlay, an enlarged icon and a glass menu above the home screen.
    var overlayAlpha = 0f
    var overlayH = 874f
    var bigIcon: HomeIcon? = null
    var menu: HomeGlassPanel? = null
    val menuTexts = ArrayList<Triple<Float, Float, SceneText>>()
    val menuVectors = ArrayList<HomeVector>()
    val overlay = findNode(root, "Overlay")
    if (overlay != null) {
        val fill = overlay.optJSONArray("fills")?.optJSONObject(0)
        overlayAlpha = (fill?.optDouble("opacity", 1.0) ?: 0.23).toFloat()
        overlayH = (findNode(source, "Overlay") ?: overlay).optDouble("h").toFloat()
        val page = root.optJSONArray("children")?.optJSONObject(0)
        val iconNode = page?.optJSONArray("children")?.let { c ->
            (0 until c.length()).map(c::getJSONObject).firstOrNull { it.optString("name") == "Icon" }
        }
        if (iconNode != null) {
            val hash = firstImageHash(iconNode)
            if (hash != null) bigIcon = HomeIcon(hash, hx + nodeX(iconNode), hy + nodeY(iconNode),
                iconNode.optDouble("w").toFloat(), null, 0f, 0f)
        }
        // The context menu sheet reuses the panel parser so it picks up the Fill + Shadow radius.
        val menuNode = findNode(root, "_Home Screen Quick Actions")
        if (menuNode != null) {
            val panel = parseGlassPanel(menuNode, hx, hy)
            menu = panel
            // Both walks start at Actions, whose own matrix carries the 10pt inset below the sheet top; the
            // row frames below it are positioned relative to that, so starting at Widgets would drop the 10pt.
            // The sheet's Fill + Shadow / Glass Effect frames are vector paths too, but they are the glass
            // surface, which HomeGlass already paints, so they stay outside this subtree.
            findNode(menuNode, "Actions")?.let { actions ->
                collectTexts(actions, panel.x, panel.y, menuTexts)
                collectVectors(actions, panel.x, panel.y, panel.x, panel.y, density, menuVectors)
            }
        }
    }
    return HomeScreenSceneData(wallpaperHash, wallpaperH, wallpaperClipH, icons, widget, search,
        searchTexts, dock, dockIcons, overlayAlpha, overlayH, bigIcon, menu, menuTexts, menuVectors)
}

@Composable
internal fun rememberSceneImage(hash: String?): ImageBitmap? {
    val context = LocalContext.current
    return produceState<ImageBitmap?>(null, hash) {
        value = if (hash == null) null else withContext(Dispatchers.IO) {
            runCatching {
                context.assets.open("iphone-examples/images/$hash").use { BitmapFactory.decodeStream(it) }?.asImageBitmap()
            }.getOrNull()
        }
    }.value
}

/**
 * Canvas pt to viewport dp. Android owns the 62pt status-bar strip, so the scene's content origin is
 * 62pt below the canvas top. The shift is baked into every node instead of a container `offset`, because
 * a container offset both shrinks the child constraints and reports unreliable bounds in nested layouts.
 */
private const val StatusBarPt = 62f

private fun Modifier.scenePos(x: Float, y: Float): Modifier = offset(x.dp, (y - StatusBarPt).dp)

private fun Modifier.sceneBox(x: Float, y: Float, w: Float, h: Float): Modifier =
    scenePos(x, y).requiredSize(w.dp, h.dp)

/**
 * Wallpaper as an explicit destination rect instead of an `Image` node: FigmaSceneView.drawImage scales the
 * bitmap to fill the (viewport fitted) node box and centres the overflow, so the authored 402pt width is not
 * the drawn width. Layout constraints would clamp that overflow, hence the manual rect. The bitmap is then
 * clipped by the node's authored geometry (`paths`), so the fitted tail never reaches the screen.
 */
private fun Modifier.sceneWallpaper(image: ImageBitmap?, heightPt: Float, clipPt: Float): Modifier = drawBehind {
    if (image == null) return@drawBehind
    val scale = maxOf(402f / image.width, heightPt / image.height)
    val drawW = image.width * scale
    val drawH = image.height * scale
    val top = (heightPt - drawH) / 2f
    val visible = (clipPt - top).coerceIn(0f, drawH)
    drawImage(
        image,
        srcOffset = IntOffset.Zero,
        srcSize = IntSize(image.width, (visible / scale).roundToInt().coerceIn(1, image.height)),
        dstOffset = IntOffset(((402f - drawW) / 2f * density).roundToInt(), ((top - StatusBarPt) * density).roundToInt()),
        dstSize = IntSize((drawW * density).roundToInt(), (visible * density).roundToInt()),
        filterQuality = FilterQuality.High,
    )
}

@Composable
private fun HomeScreenIcon(icon: HomeIcon) {
    val bitmap = rememberSceneImage(icon.hash)
    Box(Modifier.sceneBox(icon.x, icon.y, icon.size, icon.size)) {
        if (bitmap != null) Image(bitmap, null, Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
    }
    icon.name?.let { text ->
        Box(Modifier.scenePos(icon.nameX, icon.nameY)) {
            SceneGlyphText(text, text.color ?: Color.White)
        }
    }
    icon.badge?.let { badge ->
        Box(Modifier.sceneBox(badge.x, badge.y, badge.size, badge.size)
            .background(badge.color, androidx.compose.foundation.shape.CircleShape))
        // collectTexts already resolved the absolute pt of the number inside the badge.
        badge.text?.let { (tx, ty, text) ->
            Box(Modifier.scenePos(tx, ty)) { SceneGlyphText(text, text.color ?: Color.White) }
        }
    }
}

/** One original glass surface (Dock, search pill, context menu): refraction via the existing backdrop API. */
@Composable
private fun HomeGlass(
    panel: HomeGlassPanel,
    backdrop: LayerBackdrop?,
    // Fill + Shadow sheet color; Materials use #999@33%, the context menu uses white@70% + #bfbfbf@10%.
    surface: Color = Color(0.6f, 0.6f, 0.6f, 0.33f),
    // The context menu is drawn over dense app icons; the vector original keeps them crisp, so refraction
    // (vibrancy/lens/blur) is only used for the thin Materials pills where it is visually negligible.
    refract: Boolean = true,
    // Quick Actions: the overlay dims the whole home screen, and these panels live below that sheet. The
    // recorded backdrop already carries the dim, so only the panel surface and its icons need one more pass.
    dim: Float = 0f,
    content: @Composable () -> Unit,
) {
    // requiredSize: offset() shrinks the incoming constraints by the offset amount, so a bottom-anchored
    // panel would otherwise have its height clamped by the remaining viewport instead of its own geometry.
    Box(Modifier.sceneBox(panel.x, panel.y, panel.w, panel.h)) {
        if (backdrop != null) {
            // The vector original has no specular rim on these sheets (only a 0.5pt light spread shadow inside the
            // Figma effect list), so drop the default 0.5dp white Highlight: it costs ~4 heat rows per edge.
            Box(Modifier.fillMaxSize().drawBackdrop(
                backdrop = backdrop,
                shape = { RoundedRectangle(panel.radius.dp) },
                effects = { if (refract) { vibrancy(); blur(2.dp.toPx()); lens(12.dp.toPx(), 24.dp.toPx()) } },
                highlight = { null },
                onDrawSurface = { drawRect(surface) },
            ))
        } else {
            Box(Modifier.fillMaxSize().clip(androidx.compose.foundation.shape.RoundedCornerShape(panel.radius.dp))
                .background(surface))
        }
        content()
        if (dim > 0f) Box(Modifier.fillMaxSize()
            .clip(androidx.compose.foundation.shape.RoundedCornerShape(panel.radius.dp))
            .background(Color.Black.copy(alpha = dim)))
    }
}

/** Gradient widget body with the original dashed padding guide; texts are glyph-accurate. */
@Composable
private fun HomeScreenWidget(widget: HomeWidget) {
    Box(Modifier.sceneBox(widget.x, widget.y, widget.w, widget.h)
        .clip(androidx.compose.foundation.shape.RoundedCornerShape(widget.radius.dp))
        .background(Brush.verticalGradient(listOf(widget.topColor, widget.bottomColor)))) {
        if (widget.guideW > 0f && widget.guideH > 0f) {
            Canvas(Modifier.offset((widget.guideX - widget.x).dp, (widget.guideY - widget.y).dp)
                .size(widget.guideW.dp, widget.guideH.dp)) {
                val stroke = Stroke(width = 1.dp.toPx(),
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(6.dp.toPx(), 5.dp.toPx())))
                drawRoundRect(Color.Black.copy(alpha = widget.guideOpacity), style = stroke,
                    cornerRadius = CornerRadius(widget.guideRadius.dp.toPx()))
            }
        }
    }
    widget.texts.forEach { (dx, dy, text) ->
        // The authored DROP_SHADOW travels with the text node, so labels on the wallpaper get their blob.
        Box(Modifier.scenePos(dx, dy)) { SceneGlyphText(text, text.color ?: Color.White) }
    }
}

/**
 * Full home-screen rebuild. The wallpaper, app icons and the widget body are recorded into the
 * backdrop layer; the Dock and search pill sample it as sibling glass surfaces (no self-recording).
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun HomeScreenScene(entry: IphoneExample, backdrop: LayerBackdrop?, onReference: () -> Unit) {
    val context = LocalContext.current
    androidx.compose.foundation.layout.BoxWithConstraints(Modifier.fillMaxSize()) {
        // 1dp == 1pt in the caller's aligned density, so the viewport height is directly in pt.
        val viewportPt = constraints.maxHeight / androidx.compose.ui.platform.LocalDensity.current.density
        val deltaPt = ((62f + viewportPt - 874f) * 4f).toInt() / 4f
        val density = androidx.compose.ui.platform.LocalDensity.current.density
        val scene = remember(entry.file, deltaPt, density) { loadHomeScreenScene(context, entry.file, deltaPt, density) }
            ?: return@BoxWithConstraints
        renderScene(scene, backdrop, onReference)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun renderScene(scene: HomeScreenSceneData, backdrop: LayerBackdrop?, onReference: () -> Unit) {
    // Android owns the status-bar strip: clip to the viewport exactly like the reference view does, and let
    // each node carry the 62pt shift (scenePos) instead of moving the whole container.
    Box(
        Modifier.fillMaxSize().combinedClickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = {},
            onLongClick = onReference,
        ),
    ) {
      Box(Modifier.fillMaxSize().clipToBounds()) {
        val wallpaper = rememberSceneImage(scene.wallpaperHash)
        // layerBackdrop must stay outermost: it only records what the node and its *inner* modifiers draw, so a
        // drawBehind placed before it would paint outside the recorded layer and the sibling glass would sample empty.
        Box(Modifier.fillMaxSize().then(if (backdrop != null) Modifier.layerBackdrop(backdrop) else Modifier)
            .sceneWallpaper(wallpaper, scene.wallpaperH, scene.wallpaperClipH)) {
            scene.icons.forEach { HomeScreenIcon(it) }
            scene.widget?.let { HomeScreenWidget(it) }
            // Quick Actions dims the recorded page: the glass panels refract the dimmed content, exactly like
            // the vector original where the Overlay is composited before the menu sheet is drawn on top. The dim
            // stops at the node's authored 874pt rect, so the fitted tail below keeps the bright page background.
            // Drawn as a rect instead of a sized Box: requiredSize coerces an over-tall child back to the
            // viewport and centres the overflow, which silently lifted this sheet's bottom edge by 37px.
            if (scene.overlayAlpha > 0f) {
                Box(Modifier.fillMaxSize().drawBehind {
                    drawRect(Color.Black.copy(alpha = scene.overlayAlpha),
                        Offset(0f, -StatusBarPt * density), Size(size.width, scene.overlayH * density))
                })
            }
        }
        scene.dock?.let { dock ->
            HomeGlass(dock, backdrop, dim = scene.overlayAlpha) {
                scene.dockIcons.forEach { icon ->
                    Box(Modifier.offset((icon.x - dock.x).dp, (icon.y - dock.y).dp)) {
                        val bitmap = rememberSceneImage(icon.hash)
                        if (bitmap != null) Image(bitmap, null, Modifier.size(icon.size.dp), contentScale = ContentScale.Crop)
                    }
                }
            }
        }
        scene.search?.let { search ->
            HomeGlass(search, backdrop, dim = scene.overlayAlpha) {
                scene.searchTexts.forEach { (dx, dy, text) ->
                    Box(Modifier.offset((dx - search.x).dp, (dy - search.y).dp)) {
                        SceneGlyphText(text, text.color ?: Color.White)
                    }
                }
            }
        }
        // The enlarged icon and the menu are the only layers above the dim, which is applied inside the
        // recorded page so the glass panels refract the dimmed content.
        scene.bigIcon?.let { icon ->
            if (scene.overlayAlpha > 0f) {
                val bitmap = rememberSceneImage(icon.hash)
                if (bitmap != null) Image(bitmap, null,
                    Modifier.sceneBox(icon.x, icon.y, icon.size, icon.size), contentScale = ContentScale.Crop)
            }
        }
        // Fill + Shadow is white@70% LIGHTEN then #bfbfbf@10% DARKEN, which composites to 0.27*dst + 179.8.
        scene.menu?.let { menu ->
            HomeGlass(menu, backdrop, surface = Color(0.966f, 0.966f, 0.966f, 0.73f)) {
                scene.menuTexts.forEach { (dx, dy, text) ->
                    Box(Modifier.offset((dx - menu.x).dp, (dy - menu.y).dp)) {
                        SceneGlyphText(text, text.color ?: Color(0xFF1A1A1A))
                    }
                }
                // The Widgets row is authored as raw vector geometry, so it needs the path fills directly:
                // one draw pass in collection order keeps each slot under its glyph. The paths are already in
                // panel-relative pixels, so no DrawScope transform is involved.
                Canvas(Modifier.fillMaxSize()) {
                    scene.menuVectors.forEach { drawPath(it.path, it.color) }
                }
            }
        }
      }
    }
}
