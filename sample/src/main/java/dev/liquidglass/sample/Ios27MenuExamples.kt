package dev.liquidglass.sample

import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zeaze.tianyinwallpaper.backdrop.Backdrop
import com.zeaze.tianyinwallpaper.backdrop.backdrops.LayerBackdrop
import dev.liquidglass.ios27.Ios27PageColors
import dev.liquidglass.ios27.Ios27Tokens
import dev.liquidglass.ios27.ios27PressedClickable
import dev.liquidglass.ios27.ios27AndroidPane
import dev.liquidglass.ios27.liquidDialogShadow
import dev.liquidglass.ios27.liquidDialogSurface

private val QweaDialogEnterEasing = Easing { fraction ->
    DecelerateInterpolator(1.6f).getInterpolation(fraction)
}
private val QweaDialogExitEasing = Easing { fraction ->
    AccelerateInterpolator(1.4f).getInterpolation(fraction)
}

/**
 * Native menu host for the Figma menu examples.
 *
 * The panel geometry is deliberately kept in the 402pt design coordinate space used by
 * NativeExamplePage: edit medium is 238x44 at (16,180), the regular menu is 250x338 at
 * (16,180), and the context menu is 250x278 at (16,247.5). The panel reads the page backdrop
 * as a sibling, so the menu can refract the actual content below without recording itself.
 */
@Composable
internal fun Ios27MenuExample(
    entry: IphoneExample,
    backdrop: LayerBackdrop?,
    colors: Ios27PageColors,
    dark: Boolean,
    onDismiss: () -> Unit,
    onAction: (String) -> Unit,
    contextMenuTop: Dp = 247.5.dp,
    visible: Boolean = true,
    onHidden: () -> Unit = {},
) {
    val editMedium = entry.category == "Menus" && entry.variant == "Type=Edit Medium"
    val contextMenu = entry.category == "Context Menu"
    val width = if (editMedium) 238.dp else 250.dp
    val height = when {
        editMedium -> 44.dp
        contextMenu -> 278.dp
        else -> 338.dp
    }
    val y = if (contextMenu) contextMenuTop else 180.dp
    val reveal = remember(contextMenu) { Animatable(if (contextMenu) 0f else 1f) }

    LaunchedEffect(visible, contextMenu) {
        if (contextMenu) {
            reveal.animateTo(
                targetValue = if (visible) 1f else 0f,
                animationSpec = tween(
                    durationMillis = if (visible) 250 else 150,
                    easing = if (visible) QweaDialogEnterEasing else QweaDialogExitEasing,
                ),
            )
            if (!visible) onHidden()
        }
    }

    val revealProgress = if (contextMenu) reveal.value else 1f
    val contextMenuScale = when {
        !contextMenu -> 1f
        visible -> 0.86f + 0.14f * revealProgress
        else -> 0.88f + 0.12f * revealProgress
    }
    val animatedWidth = if (contextMenu) width * contextMenuScale else width
    val animatedHeight = if (contextMenu) height * contextMenuScale else height
    val animatedX = if (contextMenu) {
        16.dp + (width - animatedWidth) / 2f
    } else {
        16.dp
    }
    val animatedY = if (contextMenu) {
        y + (height - animatedHeight) / 2f
    } else {
        y
    }
    val menuShape = RoundedCornerShape(34.dp)
    Box(Modifier.fillMaxSize()) {
        // Dismissal is outside the panel. The panel is a later sibling and therefore keeps
        // the hit target for every menu row.
        if (!contextMenu || visible) {
            Box(
                Modifier.fillMaxSize().clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onDismiss,
                ),
            )
        }
        if (contextMenu) {
            // Context Menu always owns its shadow on a separate sibling layer.
            // Keeping this structure unchanged at p=1 avoids a final-frame backdrop rebuild.
            Box(
                Modifier.offset(animatedX, animatedY)
                    .size(animatedWidth, animatedHeight)
                    .liquidDialogShadow(menuShape, revealProgress),
            )
        }

        Box(
            Modifier.offset(animatedX, animatedY)
                .size(animatedWidth, animatedHeight)
                .alpha(revealProgress)
                .then(if (contextMenu) Modifier.clip(menuShape) else Modifier)
                .semantics { contentDescription = if (contextMenu) "上下文菜单" else "菜单" }
                .ios27AndroidPane(if (contextMenu) "上下文菜单" else "菜单", onDismiss)
                .menuGlass(backdrop, colors, dark, showShadow = !contextMenu),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                Modifier.requiredSize(width, height)
                    .graphicsLayer {
                        if (contextMenu) {
                            scaleX = contextMenuScale
                            scaleY = contextMenuScale
                        }
                    },
            ) {
                when {
                    editMedium -> EditMediumMenu(entry.file, colors, dark, onAction)
                    contextMenu -> ContextMenu(entry.file, colors, dark, onAction)
                    else -> RegularMenu(
                        file = entry.file,
                        colors = colors,
                        dark = dark,
                        onAction = onAction,
                    )
                }
            }
        }

    }
}

private fun Modifier.menuGlass(
    backdrop: Backdrop?,
    colors: Ios27PageColors,
    dark: Boolean,
    showShadow: Boolean = true,
): Modifier {
    val shape = RoundedCornerShape(34.dp)
    val surface = if (dark) Color(0xB51C1C1E) else Color.White.copy(alpha = 0.70f)

    // The source Menu and Fullscreen Sheet share the same authored glass recipe.
    // Reuse the common LiquidDialog surface so blur/refraction/highlight/shadow do not
    // drift into a second menu-only implementation. Menu keeps only its source surface mix.
    return liquidDialogSurface(
        backdrop = backdrop,
        shape = shape,
        surface = surface,
        fallbackSurface = surface,
        dark = dark,
        surfaceOverlay = if (dark) null else Color(0xFFBFBFBF).copy(alpha = 0.10f),
        surfaceOverlayBlendMode = BlendMode.Darken,
        showShadow = showShadow,
    )
}

@Composable
private fun EditMediumMenu(
    file: String,
    colors: Ios27PageColors,
    dark: Boolean,
    onAction: (String) -> Unit,
) {
    EditMediumMenuContent(
        file = file,
        colors = colors,
        dark = dark,
        onCut = { onAction("剪切") },
        onCopy = { onAction("复制") },
        onPaste = { onAction("粘贴") },
        onMore = { onAction("更多操作") },
    )
}

@Composable
internal fun Ios27TextSelectionMenu(
    colors: Ios27PageColors,
    dark: Boolean,
    backdrop: Backdrop?,
    onCut: (() -> Unit)?,
    onCopy: (() -> Unit)?,
    onPaste: (() -> Unit)?,
    onSelectAll: (() -> Unit)?,
    onMore: (() -> Unit)?,
    modifier: Modifier = Modifier,
) {
    val labels = selectionMenuLabels()
    Box(
        modifier.size(238.dp, 44.dp)
            .menuGlass(backdrop = backdrop, colors = colors, dark = dark),
    ) {
        if (labels != null) {
            LocalizedEditMediumMenuContent(
                file = "12740-24157.json",
                labels = labels,
                colors = colors,
                dark = dark,
                onCut = onCut,
                onCopy = onCopy,
                onPaste = onPaste,
                onSelectAll = onSelectAll,
                onMore = onMore,
            )
        } else {
            EditMediumMenuContent(
                file = "12740-24157.json",
                colors = colors,
                dark = dark,
                onCut = onCut,
                onCopy = onCopy,
                onPaste = onPaste,
                onMore = onMore,
            )
        }
    }
}

@Composable
internal fun Ios27TextSelectionExpandedMenu(
    colors: Ios27PageColors,
    dark: Boolean,
    backdrop: Backdrop?,
    onCut: (() -> Unit)?,
    onCopy: (() -> Unit)?,
    onPaste: (() -> Unit)?,
    onAutofill: (() -> Unit)?,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val labels = expandedSelectionMenuLabels()
    Box(
        modifier.size(250.dp, 338.dp)
            .menuGlass(backdrop = backdrop, colors = colors, dark = dark),
    ) {
        RegularMenu(
            file = "12740-24173.json",
            colors = colors,
            dark = dark,
            labels = labels,
        ) { action ->
            when (action) {
                "剪切" -> onCut?.invoke() ?: onDismiss()
                "复制" -> onCopy?.invoke() ?: onDismiss()
                "粘贴", "粘贴并匹配样式" -> onPaste?.invoke() ?: onDismiss()
                "自动填充" -> onAutofill?.invoke() ?: onDismiss()
                else -> onDismiss()
            }
        }
    }
}

/**
 * Text-selection menu backed directly by QWEA0/Liquid-Glass-Android's LiquidGlassView.
 *
 * The compact action body is shape 1. The trailing round "more" control is shape 2.
 * Expanding animates shape 2 from that 36dp circle into the 250×338 regular menu.
 * QWEA0's own AGSL pipeline performs the merge with sminPoly() through
 * setSecondaryShape(..., smoothing), so the bridge is a real SDF smooth-union rather
 * than a path morph or scale transform.
 */
@Composable
internal fun Ios27QweaLiquidSelectionMenu(
    expanded: Boolean,
    colors: Ios27PageColors,
    dark: Boolean,
    onCut: (() -> Unit)?,
    onCopy: (() -> Unit)?,
    onPaste: (() -> Unit)?,
    onSelectAll: (() -> Unit)?,
    onAutofill: (() -> Unit)?,
    onExpand: () -> Unit,
    onDismiss: () -> Unit,
    backdrop: Backdrop?,
    modifier: Modifier = Modifier,
) {
    val progress by animateFloatAsState(
        targetValue = if (expanded) 1f else 0f,
        animationSpec = tween(durationMillis = 340, easing = FastOutSlowInEasing),
        label = "qweaLiquidMenuMerge",
    )
    val compactLabels = selectionMenuLabels()
    val expandedLabels = expandedSelectionMenuLabels()

    fun lerpDp(start: Dp, end: Dp): Dp = start + (end - start) * progress
    val glassWidth = lerpDp(238.dp, 250.dp)
    val glassHeight = lerpDp(44.dp, 338.dp)

    val compactAlpha = (1f - progress / 0.30f).coerceIn(0f, 1f)
    val expandedAlpha = ((progress - 0.28f) / 0.72f).coerceIn(0f, 1f)

    Box(modifier.size(250.dp, 338.dp)) {
        Box(
            Modifier
                .size(glassWidth, glassHeight)
                .menuGlass(backdrop = backdrop, colors = colors, dark = dark),
        )

        if (progress < 0.999f) {
            Box(
                Modifier
                    .size(238.dp, 44.dp)
                    .alpha(compactAlpha),
            ) {
                if (compactLabels != null) {
                    LocalizedEditMediumMenuContent(
                        file = "12740-24157.json",
                        labels = compactLabels,
                        colors = colors,
                        dark = dark,
                        onCut = onCut,
                        onCopy = onCopy,
                        onPaste = onPaste,
                        onSelectAll = onSelectAll,
                        onMore = onExpand,
                        showMoreBackground = true,
                    )
                } else {
                    EditMediumMenuContent(
                        file = "12740-24157.json",
                        colors = colors,
                        dark = dark,
                        onCut = onCut,
                        onCopy = onCopy,
                        onPaste = onPaste,
                        onMore = onExpand,
                        showMoreBackground = true,
                    )
                }
            }
        }

        if (progress > 0.18f) {
            Box(Modifier.fillMaxSize().alpha(expandedAlpha)) {
                RegularMenu(
                    file = "12740-24173.json",
                    colors = colors,
                    dark = dark,
                    labels = expandedLabels,
                ) { action ->
                    when (action) {
                        "剪切" -> onCut?.invoke() ?: onDismiss()
                        "复制" -> onCopy?.invoke() ?: onDismiss()
                        "粘贴", "粘贴并匹配样式" -> onPaste?.invoke() ?: onDismiss()
                        "自动填充" -> onAutofill?.invoke() ?: onDismiss()
                        else -> onDismiss()
                    }
                }
            }
        }
    }
}

private data class SelectionMenuLabels(
    val cut: String,
    val copy: String,
    val paste: String,
    val selectAll: String,
)

private data class ExpandedSelectionMenuLabels(
    val cut: String,
    val copy: String,
    val paste: String,
    val pasteAndMatchStyle: String,
    val addLink: String,
    val replace: String,
    val writingTools: String,
    val autofill: String,
)

@Composable
private fun selectionMenuLabels(): SelectionMenuLabels? {
    val locale = LocalConfiguration.current.locales[0]
    if (locale.language != "zh") return null
    val traditional = locale.script == "Hant" || locale.country in setOf("TW", "HK", "MO")
    return if (traditional) {
        SelectionMenuLabels("剪下", "拷貝", "貼上", "全選")
    } else {
        SelectionMenuLabels("剪切", "复制", "粘贴", "全选")
    }
}

@Composable
private fun expandedSelectionMenuLabels(): ExpandedSelectionMenuLabels? {
    val locale = LocalConfiguration.current.locales[0]
    if (locale.language != "zh") return null
    val traditional = locale.script == "Hant" || locale.country in setOf("TW", "HK", "MO")
    return if (traditional) {
        ExpandedSelectionMenuLabels(
            cut = "剪下",
            copy = "拷貝",
            paste = "貼上",
            pasteAndMatchStyle = "貼上並符合樣式",
            addLink = "加入連結",
            replace = "取代…",
            writingTools = "寫作工具",
            autofill = "自動填寫",
        )
    } else {
        ExpandedSelectionMenuLabels(
            cut = "剪切",
            copy = "复制",
            paste = "粘贴",
            pasteAndMatchStyle = "粘贴并匹配样式",
            addLink = "添加链接",
            replace = "替换…",
            writingTools = "写作工具",
            autofill = "自动填充",
        )
    }
}

@Composable
private fun LocalizedEditMediumMenuContent(
    file: String,
    labels: SelectionMenuLabels,
    colors: Ios27PageColors,
    dark: Boolean,
    onCut: (() -> Unit)?,
    onCopy: (() -> Unit)?,
    onPaste: (() -> Unit)?,
    onSelectAll: (() -> Unit)?,
    onMore: (() -> Unit)?,
    showMoreBackground: Boolean = true,
) {
    val separatorColor = if (dark) colors.separator else Color(0xFFCCCCCC)
    val enabledTint = colors.label
    val disabledTint = colors.secondaryLabel.copy(alpha = 0.48f)
    val actions = listOf(
        labels.cut to onCut,
        labels.copy to onCopy,
        labels.paste to onPaste,
        labels.selectAll to onSelectAll,
    )

    Box(Modifier.fillMaxSize()) {
        Row(
            Modifier.offset(x = 8.dp)
                .width(190.dp)
                .height(44.dp),
        ) {
            actions.forEach { (label, action) ->
                Box(
                    Modifier.weight(1f)
                        .height(44.dp)
                        .ios27PressedClickable(
                            RoundedCornerShape(12.dp),
                            enabled = action != null,
                            onClick = { action?.invoke() },
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        label,
                        color = if (action != null) enabledTint else disabledTint,
                        style = Ios27Tokens.Type.Body,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        softWrap = false,
                    )
                }
            }
        }

        listOf(55.5.dp, 103.dp, 150.5.dp).forEach { x ->
            Box(Modifier.offset(x, 13.dp).size(1.dp, 18.dp).background(separatorColor))
        }

        Box(
            Modifier.offset(198.dp, 4.dp)
                .size(36.dp)
                .clip(CircleShape)
                .then(
                    if (showMoreBackground) {
                        Modifier.background(
                            if (dark) Color.White.copy(alpha = 0.12f)
                            else Color(0xFFEDEDED),
                        )
                    } else {
                        Modifier
                    },
                )
                .ios27PressedClickable(
                    CircleShape,
                    enabled = onMore != null,
                    onClick = { onMore?.invoke() },
                ),
        ) {
            SceneGlyphIcon(
                file = file,
                nodeName = "􀆊",
                tint = if (onMore != null) enabledTint else disabledTint,
                modifier = Modifier.offset(y = 9.dp).size(36.dp, 18.dp),
            )
        }
    }
}

@Composable
private fun EditMediumMenuContent(
    file: String,
    colors: Ios27PageColors,
    dark: Boolean,
    onCut: (() -> Unit)?,
    onCopy: (() -> Unit)?,
    onPaste: (() -> Unit)?,
    onMore: (() -> Unit)?,
    showMoreBackground: Boolean = true,
) {
    val cut = rememberSceneSubtreeNamedText(file, "Action 1", "Item")
    val copy = rememberSceneSubtreeNamedText(file, "Action 2", "Item")
    val paste = rememberSceneSubtreeNamedText(file, "Action 3", "Item")
    val separatorColor = if (dark) colors.separator else Color(0xFFCCCCCC)
    val enabledTint = colors.label
    val disabledTint = colors.secondaryLabel.copy(alpha = 0.48f)

    Box(Modifier.fillMaxSize()) {
        Box(
            Modifier.offset(20.dp, 0.dp).size(40.dp, 44.dp)
                .ios27PressedClickable(
                    RoundedCornerShape(12.dp),
                    enabled = onCut != null,
                    onClick = { onCut?.invoke() },
                ),
        )
        Box(
            Modifier.offset(60.dp, 0.dp).size(68.dp, 44.dp)
                .ios27PressedClickable(
                    RoundedCornerShape(12.dp),
                    enabled = onCopy != null,
                    onClick = { onCopy?.invoke() },
                ),
        )
        Box(
            Modifier.offset(128.dp, 0.dp).size(70.dp, 44.dp)
                .ios27PressedClickable(
                    RoundedCornerShape(12.dp),
                    enabled = onPaste != null,
                    onClick = { onPaste?.invoke() },
                ),
        )
        cut?.let {
            Box(Modifier.offset(20.dp, 13.dp)) {
                SceneGlyphText(
                    it,
                    if (onCut == null) disabledTint
                    else if (dark) enabledTint else it.color ?: enabledTint,
                )
            }
        }
        Box(Modifier.offset(60.dp, 13.dp).size(1.dp, 18.dp).background(separatorColor))
        copy?.let {
            Box(Modifier.offset(77.dp, 13.dp)) {
                SceneGlyphText(
                    it,
                    if (onCopy == null) disabledTint
                    else if (dark) enabledTint else it.color ?: enabledTint,
                )
            }
        }
        Box(Modifier.offset(128.dp, 13.dp).size(1.dp, 18.dp).background(separatorColor))
        paste?.let {
            Box(Modifier.offset(145.dp, 13.dp)) {
                SceneGlyphText(
                    it,
                    if (onPaste == null) disabledTint
                    else if (dark) enabledTint else it.color ?: enabledTint,
                )
            }
        }
        Box(
            Modifier.offset(198.dp, 4.dp)
                .size(36.dp)
                .clip(CircleShape)
                .then(
                    if (showMoreBackground) {
                        Modifier.background(
                            if (dark) Color.White.copy(alpha = 0.12f)
                            else Color(0xFFEDEDED),
                        )
                    } else {
                        Modifier
                    },
                )
                .ios27PressedClickable(
                    CircleShape,
                    enabled = onMore != null,
                    onClick = { onMore?.invoke() },
                ),
        ) {
            SceneGlyphIcon(
                file = file,
                nodeName = "􀆊",
                tint = if (onMore == null) disabledTint
                else if (dark) enabledTint else Color(0xFF1A1A1A),
                modifier = Modifier.offset(y = 9.dp).size(36.dp, 18.dp),
            )
        }
    }
}

@Composable
private fun RegularMenu(
    file: String,
    colors: Ios27PageColors,
    dark: Boolean,
    labels: ExpandedSelectionMenuLabels? = null,
    onAction: (String) -> Unit,
) {
    Box(Modifier.fillMaxSize()) {
        QuickActions(
            file = file,
            colors = colors,
            dark = dark,
            modifier = Modifier.offset(y = 10.dp),
            actions = listOf("剪切", "复制", "粘贴"),
            labels = labels?.let { listOf(it.cut, it.copy, it.paste) },
            onAction = onAction,
        )
        MenuSeparator(colors, Modifier.offset(24.dp, 76.dp))
        MenuRow(
            file, 5, labels?.pasteAndMatchStyle ?: "Paste and Match Style",
            colors.label, onAction,
            modifier = Modifier.offset(16.dp, 87.dp).width(218.dp),
            height = 60.dp, action = "粘贴并匹配样式",
        )
        MenuRow(
            file, 6, labels?.addLink ?: "Add Link", colors.label, onAction,
            modifier = Modifier.offset(16.dp, 147.dp).width(218.dp),
            action = "添加链接",
        )
        MenuSeparator(colors, Modifier.offset(24.dp, 197.dp))
        MenuRow(
            file, 7, labels?.replace ?: "Replace...", colors.label, onAction,
            modifier = Modifier.offset(16.dp, 208.dp).width(218.dp),
            chevronOccurrence = 0, action = "替换…",
        )
        MenuRow(
            file, 8, labels?.writingTools ?: "Writing Tools", colors.label, onAction,
            modifier = Modifier.offset(16.dp, 248.dp).width(218.dp),
            action = "写作工具",
        )
        MenuRow(
            file, 9, labels?.autofill ?: "AutoFill", colors.label, onAction,
            modifier = Modifier.offset(16.dp, 288.dp).width(218.dp),
            chevronOccurrence = 1, action = "自动填充",
        )
    }
}

@Composable
private fun ContextMenu(file: String, colors: Ios27PageColors, dark: Boolean, onAction: (String) -> Unit) {
    Box(Modifier.fillMaxSize()) {
        QuickActions(
            file = file,
            colors = colors,
            dark = dark,
            modifier = Modifier.offset(y = 10.dp),
            actions = listOf("Label", "Label", "删除"),
            labels = listOf(null, null, "Destructive"),
            destructiveIndex = 2,
            onAction = onAction,
        )
        MenuSeparator(colors, Modifier.offset(24.dp, 76.dp))
        MenuRow(
            file, 3, "Label", colors.label, onAction,
            modifier = Modifier.offset(16.dp, 87.dp).width(218.dp),
            sourceLabelOccurrence = 3,
        )
        MenuRow(
            file,
            4,
            "Disabled Action",
            if (dark) colors.secondaryLabel.copy(alpha = 0.48f) else Color(0xFFBFBFBF),
            onAction,
            modifier = Modifier.offset(16.dp, 127.dp).width(218.dp),
            enabled = false,
            sourceLabelOccurrence = 4,
        )
        MenuRow(
            file, 5, "Destructive Action", Color(0xFFFF383C), onAction,
            modifier = Modifier.offset(16.dp, 167.dp).width(218.dp),
            sourceLabelOccurrence = 5,
        )
        MenuSeparator(colors, Modifier.offset(24.dp, 217.dp))
        MenuRow(
            file = file,
            iconOccurrence = null,
            title = "Submenu",
            color = colors.label,
            onAction = onAction,
            modifier = Modifier.offset(16.dp, 228.dp).width(218.dp),
            chevronOccurrence = 0,
            leadingWithoutIcon = true,
            sourceLabelOccurrence = 7,
            action = "Submenu",
        )
    }
}

@Composable
private fun QuickActions(
    file: String,
    colors: Ios27PageColors,
    dark: Boolean,
    modifier: Modifier,
    actions: List<String>,
    labels: List<String?>? = null,
    onAction: (String) -> Unit,
    destructiveIndex: Int? = null,
) {
    Box(modifier.fillMaxWidth().height(56.dp)) {
        val x = listOf(10.dp, 88.6667.dp, 167.3333.dp)
        actions.forEachIndexed { index, action ->
            QuickAction(
                file = file,
                subtreeName = "Action " + (index + 1),
                tint = if (index == destructiveIndex) Color(0xFFFF383C) else colors.label,
                dark = dark,
                modifier = Modifier.offset(x[index], 0.dp).size(72.6667.dp, 56.dp),
                action = action,
                labelOverride = labels?.getOrNull(index),
                onAction = onAction,
            )
        }
    }
}

@Composable
private fun QuickAction(
    file: String,
    subtreeName: String,
    tint: Color,
    dark: Boolean,
    modifier: Modifier,
    action: String,
    labelOverride: String? = null,
    onAction: (String) -> Unit,
) {
    val symbol = rememberSceneSubtreeNamedText(file, subtreeName, "Symbol")
    val label = rememberSceneSubtreeNamedText(file, subtreeName, "Label")
    val actualTint = if (dark && tint != Color(0xFFFF383C)) Color.White else tint

    Box(
        modifier.ios27PressedClickable(
            shape = RoundedCornerShape(20.dp),
            onClick = { onAction(action) },
        ),
    ) {
        symbol?.let {
            Box(Modifier.offset(4.dp, 6.dp)) { SceneGlyphText(it, actualTint) }
        }
        if (labelOverride != null) {
            Box(
                Modifier.offset(4.dp, 33.dp).size(64.6667.dp, 18.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    labelOverride,
                    color = actualTint,
                    style = Ios27Tokens.Type.Footnote,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    softWrap = false,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        } else {
            label?.let {
                Box(Modifier.offset(4.dp, 33.dp)) { SceneGlyphText(it, actualTint) }
            }
        }
    }
}

@Composable
private fun MenuRow(
    file: String,
    iconOccurrence: Int?,
    title: String,
    color: Color,
    onAction: (String) -> Unit,
    modifier: Modifier,
    height: androidx.compose.ui.unit.Dp = 40.dp,
    chevronOccurrence: Int? = null,
    enabled: Boolean = true,
    leadingWithoutIcon: Boolean = false,
    sourceLabelOccurrence: Int? = null,
    action: String = title,
) {
    Box(
        modifier.height(height)
            .ios27PressedClickable(
                shape = RoundedCornerShape(12.dp),
                enabled = enabled,
                onClick = { onAction(action) },
            ),
    ) {
        val centerY = (height - 20.dp) / 2
        if (iconOccurrence != null) {
            SceneGlyphIcon(file, "Symbol", color,
                Modifier.offset(6.dp, centerY).size(28.dp, 20.dp),
                occurrence = iconOccurrence)
        }
        val labelX = if (leadingWithoutIcon) 6.dp else 42.dp
        val labelY = if (height > 40.dp) 10.dp else centerY
        val sourceLabel = sourceLabelOccurrence?.let {
            rememberSceneTextOccurrence(file, "Label", it)
        }
        if (sourceLabel != null) {
            Box(Modifier.offset(labelX, labelY)) {
                SceneGlyphText(sourceLabel, color)
            }
        } else {
            Box(
                Modifier.offset(labelX, labelY)
                    .width(if (chevronOccurrence != null) 148.dp else 168.dp)
                    .height(if (height > 40.dp) 40.dp else 20.dp),
                contentAlignment = Alignment.CenterStart,
            ) {
                Text(
                    title,
                    color = color,
                    style = Ios27Tokens.Type.Body.copy(lineHeight = 20.sp),
                )
            }
        }
        chevronOccurrence?.let {
            SceneGlyphIcon(
                file,
                "􀆊",
                color,
                Modifier.offset(196.dp, centerY).size(14.dp, 20.dp),
                occurrence = it,
            )
        }
    }
}

@Composable
private fun MenuSeparator(colors: Ios27PageColors, modifier: Modifier) {
    Box(modifier.size(202.dp, 1.dp).background(colors.separator))
}

@Composable
private fun MenuGap(colors: Ios27PageColors) {
    Box(Modifier.fillMaxWidth().height(21.dp).padding(vertical = 10.dp)) {
        Spacer(Modifier.fillMaxWidth().height(1.dp).background(colors.separator))
    }
}
