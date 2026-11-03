package dev.liquidglass.sample

import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.View
import android.os.Build
import org.json.JSONArray
import org.json.JSONObject

/** Offline vector scene. Text uses the original glyph outlines from the supplied file. */
internal class FigmaSceneView(context: Context) : View(context) {
    var scene: JSONObject? = null
        set(value) { field = value; layoutScene = null; paths.clear(); scrollYInScene = 0f; invalidate() }
    private var layoutScene: JSONObject? = null
    private var reportedScene: JSONObject? = null
    var onNodeTap: (String, String) -> Unit = { _, _ -> }
    var onSceneAction: (List<JSONObject>) -> Unit = {}
    var onShowVariants: () -> Unit = {}
    private val paths = HashMap<JSONArray, Path>()
    private val bitmaps = HashMap<String, Bitmap>()
    private val targets = ArrayList<Pair<RectF, List<JSONObject>>>()
    private val nodeStack = ArrayList<JSONObject>()
    private var longPressed = false
    private val longPress = Runnable { longPressed = true; onShowVariants(); performHapticFeedback(android.view.HapticFeedbackConstants.LONG_PRESS) }
    private var scale = 1f
    private var downX = 0f
    private var downY = 0f
    private var scrollYInScene = 0f
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)

    init { isClickable = true; setLayerType(LAYER_TYPE_SOFTWARE, null) }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val source = scene ?: return
        scale = width / 402f
        val root = layoutScene ?: fitToViewport(source).also { layoutScene = it }
        root.optJSONArray("children")?.let { children ->
            for (i in 0 until children.length()) {
                val child = children.getJSONObject(i)
                if (child.optString("name") == "Screen") {
                    child.optJSONArray("fills")?.optJSONObject(0)?.optJSONObject("color")?.let { canvas.drawColor(color(it)) }
                    break
                }
            }
        }
        targets.clear()
        nodeStack.clear()
        canvas.save()
        canvas.scale(scale, scale)
        canvas.translate(0f, -62f - scrollYInScene)
        drawNode(canvas, root, 1f, Matrix())
        canvas.restore()
        if (reportedScene !== source && context.applicationInfo.flags and android.content.pm.ApplicationInfo.FLAG_DEBUGGABLE != 0) {
            reportedScene = source
            android.util.Log.i("IphoneExamples", "SCENE_DRAWN ${source.optString("id")} ${width}x${height}")
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        layoutScene = null
        paths.clear()
    }

    private fun fitToViewport(source: JSONObject): JSONObject {
        // Preserve the source's top/bottom/center constraints without scaling text or row heights.
        // Android owns the first safe-area strip; the remaining scene fills the actual viewport.
        val root = JSONObject(source.toString())
        fun resize(n: JSONObject, parentDelta: Float, isRoot: Boolean = false) {
            val originalHeight = n.f("h")
            val constraint = n.optString("verticalConstraint")
            val ownDelta = if (isRoot || constraint == "STRETCH") parentDelta else 0f
            if (!isRoot) n.optJSONArray("matrix")?.let { transform ->
                val shift = when (constraint) { "MAX" -> parentDelta; "CENTER" -> parentDelta / 2; else -> 0f }
                transform.put(5, (transform.f(5) + shift).toDouble())
            }
            if (ownDelta != 0f) n.put("h", (originalHeight + ownDelta).coerceAtLeast(0f).toDouble())
            n.optJSONArray("children")?.let { children ->
                for (i in 0 until children.length()) resize(children.getJSONObject(i), ownDelta)
            }
        }
        resize(root, height / scale + 62f - source.f("h"), true)
        return root
    }

    private fun drawNode(canvas: Canvas, n: JSONObject, inheritedAlpha: Float, parent: Matrix) {
        val w = n.f("w"); val h = n.f("h")
        val alpha = inheritedAlpha * n.f("opacity", 1f)
        if (alpha <= 0f) return
        nodeStack.add(n)
        if (isFirstRowTopSeparator(n)) {
            nodeStack.removeAt(nodeStack.lastIndex)
            return
        }
        val matrix = n.optJSONArray("matrix")?.let {
            Matrix().apply { setValues(floatArrayOf(it.f(0), it.f(1), it.f(2), it.f(3), it.f(4), it.f(5), 0f, 0f, 1f)) }
        } ?: Matrix()
        val absolute = Matrix(parent).apply { preConcat(matrix) }
        canvas.save(); canvas.concat(matrix)
        val name = n.optString("name")
        val effects = n.optJSONArray("effects") ?: JSONArray()
        for (i in 0 until effects.length()) {
            val effect = effects.getJSONObject(i)
            if (effect.optString("type") == "DROP_SHADOW" && effect.f("radius") > 0) {
                paint.reset(); paint.isAntiAlias = true; paint.color = Color.TRANSPARENT
                val offset = effect.optJSONObject("offset")
                paint.setShadowLayer(effect.f("radius") / 2, offset?.f("x") ?: 0f, offset?.f("y") ?: 0f, color(effect.optJSONObject("color")))
                canvas.drawPath(outline(n), paint)
                paint.clearShadowLayer()
            }
        }
        if (n.optBoolean("clip")) canvas.clipPath(outline(n))
        // Edge masks belong to the composited blur layer. They are not opaque content.
        if (!name.startsWith("Scroll Edge Effect")) {
            val bounds = RectF(0f, 0f, w, h).apply { absolute.mapRect(this) }
            if (w >= 8 && h >= 8 && n.optString("type") in setOf("INSTANCE", "SYMBOL", "TEXT")) targets.add(bounds to nodeStack.toList())
            val fills = n.optJSONArray("fills") ?: JSONArray()
            val geometry = n.optJSONArray("paths") ?: JSONArray()
            val glyphs = n.optJSONArray("glyphs") ?: JSONArray()
            for (i in 0 until fills.length()) {
                val fill = fills.getJSONObject(i)
                configure(fill, w, h, alpha)
                val image = fill.optString("imageHash")
                if (image.isNotEmpty()) {
                    drawImage(canvas, image, w, h, fill, alpha, contentPath(n))
                } else if (glyphs.length() > 0) {
                    for (g in 0 until glyphs.length()) {
                        val glyph = glyphs.getJSONObject(g)
                        canvas.save(); canvas.translate(glyph.f("x"), glyph.f("y")); canvas.rotate(glyph.f("rotation"))
                        if (glyph.optString("emoji").isNotEmpty()) {
                            // The .fig contains only a monochrome placeholder for color emoji.
                            // Use the device's color glyph rather than painting that placeholder black.
                            paint.textSize = glyph.f("size", 1f)
                            paint.typeface = Typeface.DEFAULT
                            canvas.drawText(glyph.getString("emoji"), 0f, 0f, paint)
                        } else {
                            canvas.scale(glyph.f("size", 1f), -glyph.f("size", 1f))
                            canvas.drawPath(path(glyph.getJSONArray("path")), paint)
                        }
                        canvas.restore()
                    }
                } else if (geometry.length() > 0) {
                    for (g in 0 until geometry.length()) {
                        val shape = geometry.getJSONObject(g)
                        val p = path(shape.getJSONArray("commands"))
                        p.fillType = if (shape.optString("rule") == "ODD") Path.FillType.EVEN_ODD else Path.FillType.WINDING
                        canvas.drawPath(p, paint)
                    }
                } else if (n.optString("type") != "TEXT") canvas.drawPath(outline(n), paint)
            }
            val strokes = n.optJSONArray("strokes") ?: JSONArray()
            val strokeGeometry = n.optJSONArray("strokePaths") ?: JSONArray()
            for (i in 0 until strokes.length()) {
                configure(strokes.getJSONObject(i), w, h, alpha)
                if (strokeGeometry.length() > 0) {
                    for (g in 0 until strokeGeometry.length()) canvas.drawPath(path(strokeGeometry.getJSONObject(g).getJSONArray("commands")), paint)
                } else {
                    paint.style = Paint.Style.STROKE; paint.strokeWidth = n.f("strokeWidth", 1f)
                    canvas.drawPath(outline(n), paint)
                }
            }
            val children = n.optJSONArray("children") ?: JSONArray()
            // A boolean node already contains its flattened result. Drawing its operands
            // again fills holes and doubles antialiased edges.
            if (n.optString("type") != "BOOLEAN_OPERATION" || geometry.length() == 0) {
                drawChildren(canvas, children, alpha, absolute)
            }
        }
        canvas.restore()
        nodeStack.removeAt(nodeStack.lastIndex)
    }

    private fun isFirstRowTopSeparator(n: JSONObject): Boolean {
        if (n.optString("name") != "Separator" || n.f("h") > 1.5f) return false
        val groupIndex = nodeStack.indexOfLast { it.optString("name") == "Grouped List" }
        if (groupIndex < 0 || groupIndex + 1 >= nodeStack.size) return false
        val firstRow = nodeStack[groupIndex].optJSONArray("children")?.optJSONObject(0) ?: return false
        if (nodeStack[groupIndex + 1].optString("instancePath") != firstRow.optString("instancePath")) return false
        val y = nodeStack.drop(groupIndex + 1).sumOf { it.optJSONArray("matrix")?.optDouble(5) ?: 0.0 }
        return kotlin.math.abs(y) < 1.5
    }

    private fun drawChildren(canvas: Canvas, children: JSONArray, alpha: Float, absolute: Matrix) {
        var i = 0
        while (i < children.length()) {
            val child = children.getJSONObject(i++)
            if (!child.optBoolean("mask")) {
                drawNode(canvas, child, alpha, absolute)
                continue
            }
            // Figma masks apply to following siblings, not to the canvas behind the group.
            val group = canvas.saveLayer(null, null)
            while (i < children.length() && !children.getJSONObject(i).optBoolean("mask")) {
                drawNode(canvas, children.getJSONObject(i++), alpha, absolute)
            }
            val maskPaint = Paint().apply { xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN) }
            val maskLayer = canvas.saveLayer(null, maskPaint)
            drawNode(canvas, child, 1f, absolute)
            canvas.restoreToCount(maskLayer)
            canvas.restoreToCount(group)
        }
    }

    private fun contentPath(n: JSONObject): Path {
        val glyphs = n.optJSONArray("glyphs") ?: JSONArray()
        val geometry = n.optJSONArray("paths") ?: JSONArray()
        if (glyphs.length() == 0 && geometry.length() == 0) return outline(n)
        return Path().apply {
            for (i in 0 until glyphs.length()) {
                val g = glyphs.getJSONObject(i)
                val transform = Matrix().apply {
                    setTranslate(g.f("x"), g.f("y"))
                    preRotate(g.f("rotation"))
                    preScale(g.f("size", 1f), -g.f("size", 1f))
                }
                addPath(path(g.getJSONArray("path")), transform)
            }
            for (i in 0 until geometry.length()) {
                val shape = geometry.getJSONObject(i)
                if (shape.optString("rule") == "ODD") fillType = Path.FillType.EVEN_ODD
                addPath(path(shape.getJSONArray("commands")))
            }
        }
    }

    private fun outline(n: JSONObject): Path = Path().apply {
        val w = n.f("w"); val h = n.f("h")
        val radius = if (n.optString("name") == "Screen" && w == 402f) 0f else n.f("radius")
        if (n.optString("type") == "ELLIPSE") addOval(0f, 0f, w, h, Path.Direction.CW)
        else if (n.has("radii") && n.optString("name") != "Screen") {
            val radii = n.getJSONArray("radii")
            addRoundRect(RectF(0f, 0f, w, h), FloatArray(8) { radii.f(it / 2) }, Path.Direction.CW)
        }
        else addRoundRect(0f, 0f, w, h, radius, radius, Path.Direction.CW)
    }

    private fun configure(p: JSONObject, w: Float, h: Float, alpha: Float) {
        paint.reset(); paint.isAntiAlias = true; paint.isFilterBitmap = true
        paint.color = color(p.optJSONObject("color"))
        // The .fig export mirrors color alpha into paint.opacity; squaring it would fade tracks
        // to invisibility. Only trust opacity when the color itself is fully opaque.
        val fillOpacity = if (Color.alpha(paint.color) == 255) p.f("opacity", 1f) else 1f
        paint.alpha = (Color.alpha(paint.color) * fillOpacity * alpha).toInt().coerceIn(0, 255)
        if (Build.VERSION.SDK_INT >= 29) paint.blendMode = when (p.optString("blendMode")) {
            "LINEAR_DODGE" -> BlendMode.PLUS
            "MULTIPLY", "LINEAR_BURN" -> BlendMode.MULTIPLY
            "SCREEN" -> BlendMode.SCREEN
            "COLOR_BURN" -> BlendMode.COLOR_BURN
            "COLOR_DODGE" -> BlendMode.COLOR_DODGE
            "OVERLAY" -> BlendMode.OVERLAY
            "LUMINOSITY" -> BlendMode.LUMINOSITY
            "SOFT_LIGHT" -> BlendMode.SOFT_LIGHT
            else -> BlendMode.SRC_OVER
        }
        val stops = p.optJSONArray("stops")
        if (stops != null && stops.length() >= 2) {
            val colors = IntArray(stops.length()) { color(stops.getJSONObject(it).optJSONObject("color")) }
            val positions = FloatArray(stops.length()) { stops.getJSONObject(it).f("position") }
            val shader = if (p.optString("type") == "GRADIENT_RADIAL")
                RadialGradient(w / 2, h / 2, maxOf(w, h) / 2, colors, positions, Shader.TileMode.CLAMP)
            else LinearGradient(0f, 0f, w, 0f, colors, positions, Shader.TileMode.CLAMP)
            p.optJSONObject("transform")?.let { t ->
                val m = Matrix().apply { setValues(floatArrayOf(t.f("m00",1f),t.f("m01"),t.f("m02"),t.f("m10"),t.f("m11",1f),t.f("m12"),0f,0f,1f)) }
                val inverse = Matrix(); if (m.invert(inverse)) {
                    inverse.preScale(1 / w.coerceAtLeast(1f), 1 / h.coerceAtLeast(1f))
                    inverse.postScale(w, h); shader.setLocalMatrix(inverse)
                }
            }
            paint.shader = shader
            paint.alpha = (255 * p.f("opacity", 1f) * alpha).toInt().coerceIn(0,255)
        }
    }

    private fun drawImage(canvas: Canvas, key: String, w: Float, h: Float, p: JSONObject, alpha: Float, clip: Path) {
        val image = bitmaps.getOrPut(key) { context.assets.open("iphone-examples/images/$key").use { BitmapFactory.decodeStream(it) } ?: return }
        canvas.save(); canvas.clipPath(clip)
        val s = if (p.optString("imageScaleMode") == "FIT") minOf(w / image.width, h / image.height) else maxOf(w / image.width, h / image.height)
        val dw = image.width * s; val dh = image.height * s
        paint.shader = null; paint.alpha = (255 * p.f("opacity",1f) * alpha).toInt().coerceIn(0,255)
        canvas.drawBitmap(image, null, RectF((w-dw)/2,(h-dh)/2,(w+dw)/2,(h+dh)/2), paint)
        canvas.restore()
    }

    private fun path(a: JSONArray): Path = paths.getOrPut(a) {
        Path().apply {
            var i = 0
            while (i < a.length()) when (a.getInt(i++)) {
                0 -> close()
                1 -> { moveTo(a.f(i), a.f(i+1)); i += 2 }
                2 -> { lineTo(a.f(i), a.f(i+1)); i += 2 }
                3 -> { quadTo(a.f(i),a.f(i+1),a.f(i+2),a.f(i+3)); i += 4 }
                4 -> { cubicTo(a.f(i),a.f(i+1),a.f(i+2),a.f(i+3),a.f(i+4),a.f(i+5)); i += 6 }
                else -> error("Unsupported Figma path command")
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                downX = event.x; downY = event.y; longPressed = false
                postDelayed(longPress, android.view.ViewConfiguration.getLongPressTimeout().toLong())
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                if (kotlin.math.abs(event.x-downX) > 30 || kotlin.math.abs(event.y-downY) > 30) removeCallbacks(longPress)
            }
            MotionEvent.ACTION_CANCEL -> { removeCallbacks(longPress); return true }
            MotionEvent.ACTION_UP -> {
                removeCallbacks(longPress)
                if (longPressed) return true
                if (kotlin.math.abs(event.y-downY) > 30) {
                    scrollYInScene = (scrollYInScene + (downY-event.y)/scale).coerceIn(0f, (874f - 62 - height/scale).coerceAtLeast(0f))
                    invalidate()
                } else {
                    val x = event.x / scale; val y = event.y / scale + 62 + scrollYInScene
                    targets.asReversed().firstOrNull { it.first.contains(x,y) }?.let {
                        val node = it.second.last()
                        onNodeTap(node.optString("id"), node.optString("name"))
                        onSceneAction(it.second)
                    }
                    performClick()
                }
                return true
            }
        }
        return true
    }
    override fun onDetachedFromWindow() { removeCallbacks(longPress); super.onDetachedFromWindow() }
    override fun performClick(): Boolean { super.performClick(); return true }
    private fun color(c: JSONObject?): Int = if (c == null) Color.BLACK else Color.argb(
        (c.f("a",1f)*255).toInt().coerceIn(0,255),(c.f("r")*255).toInt().coerceIn(0,255),
        (c.f("g")*255).toInt().coerceIn(0,255),(c.f("b")*255).toInt().coerceIn(0,255))
}

internal fun JSONObject.f(key: String, fallback: Float = 0f) = optDouble(key, fallback.toDouble()).toFloat()
internal fun JSONArray.f(index: Int) = getDouble(index).toFloat()
