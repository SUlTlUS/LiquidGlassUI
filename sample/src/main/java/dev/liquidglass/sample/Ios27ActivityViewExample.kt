package dev.liquidglass.sample

import dev.liquidglass.*

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.WindowInsets
import com.zeaze.tianyinwallpaper.backdrop.Backdrop
import com.zeaze.tianyinwallpaper.backdrop.backdrops.rememberCanvasBackdrop
import dev.liquidglass.GlassButton
import dev.liquidglass.ios27.Ios27DisclosureIndicator
import dev.liquidglass.ios27.Ios27DisclosureVariant
import dev.liquidglass.ios27.Ios27PageColors
import dev.liquidglass.ios27.Ios27Tokens
import dev.liquidglass.sample.Ios26ModalLayer as LiquidDialogLayer
import dev.liquidglass.ios27.ios27PressedClickable
import dev.liquidglass.ios27.ios27AndroidPane
import androidx.compose.runtime.saveable.rememberSaveable

private val ActivitySheetShape = RoundedCornerShape(topStart = 38.dp, topEnd = 38.dp)

/** Examples/Activity View. The sheet is full-width and scrolls its source content below the grabber. */
@Composable
internal fun Ios27ActivityViewExample(
    backdrop: Backdrop?,
    colors: Ios27PageColors,
    dark: Boolean,
    thumbnail: ImageBitmap?,
    onAction: (String) -> Unit,
    onDismiss: () -> Unit,
    visible: Boolean = true,
) {
    val scroll = rememberScrollState()
    val sourceThumbnail = rememberSceneImage("63f9f677c21872ea31ed121ae185e6dae2e6248e")
    val collaborateGlyph = rememberSceneSubtreeNamedText("12740-35190.json", "Menu", "Symbol")
    val closeGlyph = rememberSceneSubtreeNamedText("12740-35190.json", "Close Button", "Symbol")
    val surface = if (dark) Color(0xFF1C1C1E) else Color.White
    val actionSurface = Color(0xFF787880).copy(alpha = if (dark) 0.20f else 0.10f)
    // Inline glass belongs to the share sheet, not to the page behind it.
    // Sampling the lower page leaks wallpaper colors through the sheet buttons.
    val buttonBackdrop = com.zeaze.tianyinwallpaper.backdrop.backdrops.rememberLayerBackdrop()
    var collaborate by rememberSaveable { mutableStateOf(false) }
    BoxWithConstraints(Modifier.fillMaxSize()) {
        val sheetTop = WindowInsets.safeDrawing.asPaddingValues().calculateTopPadding()
        val sheetHeight = (maxHeight - sheetTop).coerceAtLeast(1.dp)
        LiquidDialogLayer(
            solidSurface = true,
            transition = dev.liquidglass.GlassModalTransition.BottomSheet,
            visible = visible,
            // Shared 27 modal pane; toolbar buttons read only its exported material.
            backdrop = backdrop,
            exportedBackdrop = buttonBackdrop,
            onDismissRequest = onDismiss,
            panelModifier = Modifier.ios27AndroidPane("分享面板", onDismiss)
                .width(402.dp)
                .height(sheetHeight),
            alignment = Alignment.TopCenter,
            // Share the exact runtime top origin with the lower page toolbar,
            // then extend the sheet to the available bottom edge.
            offsetY = sheetTop,
            shape = ActivitySheetShape,
            surfaceColor = surface,
            fallbackSurface = surface,
            scrimColor = Color.Black.copy(alpha = if (dark) 0.30f else 0.20f),
            dark = dark,
            description = "分享面板",
        ) {
            Box(Modifier.fillMaxSize()) {
            Column(
                Modifier.fillMaxSize().verticalScroll(scroll).padding(bottom = 32.dp),
            ) {
                // Source Header is a fixed 402x161pt block. Keep every top element
                // in that coordinate space instead of stacking approximate Rows.
                Box(Modifier.fillMaxWidth().height(161.dp)) {
                    Box(
                        Modifier.offset(x = 182.dp, y = 5.dp).size(58.dp, 4.dp)
                            .clip(RoundedCornerShape(2.5.dp))
                            .background(if (dark) Color.White.copy(alpha = 0.28f)
                            else Color(0xFF3C3C43).copy(alpha = 0.30f)),
                    )

                    val thumbnailModifier = Modifier.offset(x = 28.dp, y = 30.dp)
                        .size(64.dp)
                        .shadow(8.dp, RoundedCornerShape(16.dp), clip = false)
                        .clip(RoundedCornerShape(16.dp))
                    val visibleThumbnail = sourceThumbnail ?: thumbnail
                    if (visibleThumbnail != null) {
                        Image(
                            visibleThumbnail,
                            "分享内容缩略图",
                            thumbnailModifier,
                            contentScale = ContentScale.Crop,
                        )
                    } else {
                        Canvas(thumbnailModifier) {
                            drawRect(if (dark) Color(0xFF45454A) else Color(0xFFB9D8EF))
                            drawCircle(
                                if (dark) Color(0xFF78A9E6) else Color(0xFF2156C8),
                                radius = size.minDimension * 0.32f,
                                center = center,
                            )
                        }
                    }

                    Box(Modifier.offset(x = 108.dp, y = 28.dp).size(218.dp, 20.dp)) {
                        Text(
                            "Title",
                            style = Ios27Tokens.Type.Body.copy(
                                lineHeight = 20.sp,
                                fontWeight = FontWeight.Medium,
                            ),
                            color = colors.label,
                        )
                    }
                    Box(Modifier.offset(x = 108.dp, y = 49.dp).size(215.dp, 18.dp)) {
                        Text(
                            "Subtitle",
                            style = Ios27Tokens.Type.Subheadline.copy(lineHeight = 18.sp),
                            color = colors.secondaryLabel,
                        )
                    }

                    ActivityGlassButton(
                        backdrop = buttonBackdrop,
                        modifier = Modifier.offset(x = 108.dp, y = 79.dp).size(152.dp, 34.dp),
                        height = 34.dp,
                        surfaceColor = actionSurface,
                        description = "Collaborate",
                        onClick = {
                            collaborate = !collaborate
                            onAction("Collaborate")
                        },
                    ) {
                        Box(Modifier.fillMaxSize()) {
                            if (collaborateGlyph != null) {
                                Box(Modifier.offset(x = 14.dp, y = 8.dp).size(24.dp, 18.dp)) {
                                    SceneGlyphText(collaborateGlyph, colors.label)
                                }
                            } else {
                                ActivityGlyph(
                                    ActivityIcon.People,
                                    colors.label,
                                    Modifier.offset(x = 14.dp, y = 8.dp).size(24.dp, 18.dp),
                                )
                            }
                            Text(
                                if (collaborate) "Collaborating" else "Collaborate",
                                style = Ios27Tokens.Type.Body.copy(
                                    lineHeight = 18.sp,
                                    fontWeight = FontWeight.SemiBold,
                                ),
                                color = colors.label,
                                modifier = Modifier.offset(x = 42.dp, y = 7.dp),
                                maxLines = 1,
                            )
                            Ios27DisclosureIndicator(
                                modifier = Modifier.offset(x = 130.dp, y = 8.dp),
                                color = colors.secondaryLabel,
                                variant = Ios27DisclosureVariant.Glass,
                            )
                        }
                    }

                    Row(
                        Modifier.offset(x = 108.dp, y = 125.dp).size(214.dp, 18.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                role = Role.Button,
                            ) {
                                onAction("Only invited people can edit.")
                            },
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            "Only invited people can edit.",
                            style = Ios27Tokens.Type.Subheadline.copy(lineHeight = 18.sp),
                            color = colors.secondaryLabel,
                            modifier = Modifier.width(199.dp),
                            maxLines = 1,
                        )
                        Ios27DisclosureIndicator(
                            modifier = Modifier.offset(x = 4.dp),
                            color = colors.secondaryLabel,
                            variant = Ios27DisclosureVariant.Secondary,
                        )
                    }

                    ActivityGlassButton(
                        backdrop = buttonBackdrop,
                        modifier = Modifier.offset(x = 342.dp, y = 16.dp).size(44.dp),
                        height = 44.dp,
                        surfaceColor = actionSurface,
                        description = "关闭分享面板",
                        onClick = onDismiss,
                    ) {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            if (closeGlyph != null) {
                                SceneGlyphText(closeGlyph, colors.label, Modifier.size(36.dp))
                            } else {
                                ActivityGlyph(ActivityIcon.Close, colors.label, Modifier.size(36.dp))
                            }
                        }
                    }
                }
                ActivitySeparator(dark)
                ActivityContactsRow(colors, dark, onAction)
                ActivitySeparator(dark)
                ActivityAppRow(colors, dark, onAction)
                ActivitySeparator(dark)
                ActivityQuickActionRow(colors, dark, onAction)
                ActivityActionsList(colors, dark, actionSurface, buttonBackdrop, onAction)
                }
            }
        }
    }
}

private enum class ActivityIcon {
    Close, People, AirDrop, Messages, Mail, Notes,
    Copy, Star, Glasses, Bookmark, Home, Markup, Print,
}

@Composable
private fun ActivityContactsRow(
    colors: Ios27PageColors,
    dark: Boolean,
    onAction: (String) -> Unit,
) {
    val names = listOf(
        "Herland" to "Antezana",
        "Rigo" to "Rangel",
        "Magico and..." to "2 People",
        "Jenny" to "Court",
        "Alejandra" to "Delgado",
    )
    val contactImages = listOf(
        rememberSceneImage("323d5ba4a9609506101c298edb6faeb86a69d091"),
        rememberSceneImage("5d67c390be6d124dde3d1d6b0144855a2ff09292"),
        null,
        rememberSceneImage("cb0256903eb7e4b12541ad5d7e1fb8c59172776d"),
        rememberSceneImage("52a5049cc298e1b88140b3832bb9eec696aa8498"),
    )
    val groupPrimary = rememberSceneImage("7d1406b8b4582b69261e268d001009b1f72ea3c7")
    val groupSecondary = rememberSceneImage("a0c0ca52b7012766fb001f3ab3afce79eb1616cf")
    val messageBadge = rememberSceneImage("bc8f61b9838e3e9c076156dbd078a24a3fe3b45f")
    Row(
        Modifier.fillMaxWidth().height(134.dp).horizontalScroll(rememberScrollState())
            .padding(start = 24.dp, top = 14.dp, end = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        names.forEachIndexed { index, (first, second) ->
            val interactions = remember { MutableInteractionSource() }
            val pressed by interactions.collectIsPressedAsState()
            Box(
                Modifier.width(78.dp).height(104.dp)
                    .clickable(
                        interactionSource = interactions,
                        indication = null,
                        role = Role.Button,
                    ) { onAction("$first $second") },
            ) {
                if (index == 2) {
                    Box(Modifier.offset(x = 4.dp, y = (-1).dp).size(70.dp).clip(CircleShape)
                        .background(if (dark) Color(0xFF3A3A3C) else Color(0xFFE5E5EA))) {
                        groupPrimary?.let {
                            Image(
                                it,
                                null,
                                Modifier.offset(x = 9.dp, y = 9.dp).size(37.333.dp).clip(CircleShape),
                                contentScale = ContentScale.Crop,
                            )
                        }
                        groupSecondary?.let {
                            Image(
                                it,
                                null,
                                Modifier.offset(x = 39.dp, y = 39.dp).size(22.dp).clip(CircleShape),
                                contentScale = ContentScale.Crop,
                            )
                        }
                    }
                } else {
                    val avatar = contactImages[index]
                    if (avatar != null) {
                        Image(
                            avatar,
                            null,
                            Modifier.offset(x = 4.dp).size(70.dp).clip(CircleShape),
                            contentScale = ContentScale.Crop,
                        )
                    } else {
                        Box(
                            Modifier.offset(x = 4.dp).size(70.dp).clip(CircleShape)
                                .background(if (dark) Color(0xFF3A3A3C) else Color(0xFFE5E5EA)),
                        )
                    }
                }
                Box(
                    Modifier.offset(x = 4.dp, y = if (index == 2) (-1).dp else 0.dp)
                        .size(70.dp)
                        .clip(CircleShape)
                        .background(if (pressed) colors.pressed else Color.Transparent),
                )
                if (messageBadge != null) {
                    Image(
                        messageBadge,
                        null,
                        Modifier.offset(x = 54.dp, y = if (index == 2) 49.dp else 50.dp)
                            .size(20.dp).clip(RoundedCornerShape(5.dp)),
                        contentScale = ContentScale.Crop,
                    )
                } else {
                    Box(
                        Modifier.offset(x = 54.dp, y = if (index == 2) 49.dp else 50.dp)
                            .size(20.dp).clip(RoundedCornerShape(5.dp))
                            .background(if (dark) Ios27Tokens.GreenDark else Ios27Tokens.GreenLight),
                    )
                }
                Column(
                    Modifier.offset(y = 75.dp).width(78.dp).height(27.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        first,
                        style = Ios27Tokens.Type.Caption2,
                        color = colors.label,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center,
                    )
                    Text(
                        second,
                        style = Ios27Tokens.Type.Caption2,
                        color = if (index == 2) colors.secondaryLabel else colors.label,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}

@Composable
private fun ActivityAppRow(
    colors: Ios27PageColors,
    dark: Boolean,
    onAction: (String) -> Unit,
) {
    val apps = listOf(
        "AirDrop" to rememberSceneImage("d2df5b0fde6df570b0f3003ec3cfdd341bd80eac"),
        "Messages" to rememberSceneImage("bc8f61b9838e3e9c076156dbd078a24a3fe3b45f"),
        "Mail" to rememberSceneImage("29094775aa3ab48bd260ad8c99ab61497ebca3da"),
        "Notes" to rememberSceneImage("9d57330bf609e63bdf29326a40f4270afaeaee5f"),
        "Reminders" to rememberSceneImage("9393994c30cbc3dda91d1f40f661136afc0a2e67"),
    )
    Row(
        Modifier.fillMaxWidth().height(128.dp).horizontalScroll(rememberScrollState())
            .padding(start = 24.dp, top = 18.dp, end = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        apps.forEach { (label, image) ->
            val interactions = remember { MutableInteractionSource() }
            val pressed by interactions.collectIsPressedAsState()
            Box(
                Modifier.width(78.dp).height(90.dp)
                    .clickable(
                        interactionSource = interactions,
                        indication = null,
                        role = Role.Button,
                    ) { onAction(label) },
            ) {
                if (image != null) {
                    Image(
                        image,
                        null,
                        Modifier.offset(x = 6.dp).size(66.dp).clip(RoundedCornerShape(15.dp)),
                        contentScale = ContentScale.Crop,
                    )
                } else {
                    Box(
                        Modifier.offset(x = 6.dp).size(66.dp)
                            .clip(RoundedCornerShape(15.dp))
                            .background(if (dark) Color(0xFF45454A) else Color(0xFFEDEDED)),
                    )
                }
                Box(
                    Modifier.offset(x = 6.dp).size(66.dp)
                        .clip(RoundedCornerShape(15.dp))
                        .background(if (pressed) colors.pressed else Color.Transparent),
                )
                Text(
                    label,
                    style = Ios27Tokens.Type.Caption2,
                    color = colors.label,
                    modifier = Modifier.offset(y = 77.dp).width(78.dp).height(13.dp),
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                )
            }
        }
    }
}

@Composable
private fun ActivityQuickActionRow(
    colors: Ios27PageColors,
    dark: Boolean,
    onAction: (String) -> Unit,
) {
    val actions = listOf(
        Triple(
            "Copy",
            ActivityIcon.Copy,
            rememberSceneSubtreeNamedText("12740-35190.json", "Action 1", "Symbol"),
        ),
        Triple(
            "Add to Favorites",
            ActivityIcon.Star,
            rememberSceneSubtreeNamedText("12740-35190.json", "Action 2", "Symbol"),
        ),
        Triple(
            "Add to Reading List",
            ActivityIcon.Glasses,
            rememberSceneSubtreeNamedText("12740-35190.json", "Action 3", "Symbol"),
        ),
        Triple(
            "Add Bookmark",
            ActivityIcon.Bookmark,
            rememberSceneSubtreeNamedText("12740-35190.json", "Action 4", "Symbol"),
        ),
    )
    val surface = Color(0xFF787880).copy(alpha = if (dark) 0.28f else 0.16f)
    Row(
        Modifier.fillMaxWidth().height(120.dp).padding(start = 24.dp, top = 14.dp, end = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        actions.forEach { (label, fallbackIcon, sourceGlyph) ->
            Box(Modifier.width(78.dp).height(86.dp)) {
                Box(
                    Modifier.offset(x = 6.dp).size(66.dp)
                        .background(surface, CircleShape)
                        .ios27PressedClickable(
                            shape = CircleShape,
                            role = Role.Button,
                            onClick = { onAction(label) },
                        )
                        .semantics { contentDescription = label },
                ) {
                    if (sourceGlyph != null) {
                        SceneGlyphTextViewport(
                            sourceGlyph,
                            colors.label,
                            Modifier.fillMaxSize(),
                            offsetX = -2f,
                            offsetY = -2f,
                        )
                    } else {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            ActivityGlyph(fallbackIcon, colors.label, Modifier.size(38.dp))
                        }
                    }
                }
                Text(
                    label,
                    style = Ios27Tokens.Type.Caption2,
                    color = colors.label,
                    modifier = Modifier.offset(y = 73.dp).width(78.dp).height(13.dp),
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Composable
private fun ActivityActionsList(
    colors: Ios27PageColors,
    dark: Boolean,
    actionSurface: Color,
    backdrop: Backdrop?,
    onAction: (String) -> Unit,
) {
    val firstGroupGlyphs = listOf(
        rememberSceneSubtreeNamedText("12740-35190.json", "Action 5", "Symbol"),
        rememberSceneSubtreeNamedText("12740-35190.json", "Action 6", "Symbol"),
        rememberSceneSubtreeNamedText("12740-35190.json", "Action 7", "Symbol"),
        rememberSceneSubtreeNamedText("12740-35190.json", "Action 8", "Symbol"),
    )
    val secondGroupGlyphs = listOf(
        rememberSceneSubtreeNamedText("12740-35190.json", "Action 9", "Symbol"),
        rememberSceneSubtreeNamedText("12740-35190.json", "Action 10", "Symbol"),
    )
    Box(Modifier.fillMaxWidth().height(424.dp)) {
        ActivityActionGroup(
            labels = listOf("Add to Reading List", "Add Bookmark", "Add to Favorites", "Add to Home Screen"),
            icons = listOf(ActivityIcon.Glasses, ActivityIcon.Bookmark, ActivityIcon.Star, ActivityIcon.Home),
            sourceGlyphs = firstGroupGlyphs,
            colors = colors,
            dark = dark,
            onAction = onAction,
            modifier = Modifier.offset(x = 16.dp, y = 2.dp).size(370.dp, 208.dp),
        )
        ActivityActionGroup(
            labels = listOf("Markup", "Print"),
            icons = listOf(ActivityIcon.Markup, ActivityIcon.Print),
            sourceGlyphs = secondGroupGlyphs,
            colors = colors,
            dark = dark,
            onAction = onAction,
            modifier = Modifier.offset(x = 16.dp, y = 230.dp).size(370.dp, 104.dp),
        )
        ActivityGlassButton(
            backdrop = backdrop,
            modifier = Modifier.offset(x = 131.5.dp, y = 354.dp).size(139.dp, 50.dp),
            height = 50.dp,
            surfaceColor = actionSurface,
            description = "Edit Actions",
            onClick = { onAction("Edit Actions") },
        ) {
            Text(
                "Edit Actions",
                style = Ios27Tokens.Type.Body.copy(lineHeight = 18.sp),
                color = colors.label,
            )
        }
    }
}

@Composable
private fun ActivityGlassButton(
    backdrop: Backdrop?,
    modifier: Modifier,
    height: androidx.compose.ui.unit.Dp,
    surfaceColor: Color,
    description: String,
    onClick: () -> Unit,
    content: @Composable RowScope.() -> Unit,
) {
    if (backdrop != null) {
        GlassButton(
            onClick = onClick,
            backdrop = backdrop,
            modifier = modifier.semantics { contentDescription = description },
            surfaceColor = surfaceColor,
            height = height,
            contentPadding = PaddingValues(0.dp),
            content = content,
        )
    } else {
        Row(
            modifier = modifier.height(height)
                .background(surfaceColor, CircleShape)
                .ios27PressedClickable(
                    shape = CircleShape,
                    role = Role.Button,
                    onClick = onClick,
                )
                .semantics { contentDescription = description },
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            content = content,
        )
    }
}

@Composable
private fun ActivityActionGroup(
    labels: List<String>,
    icons: List<ActivityIcon>,
    sourceGlyphs: List<SceneText?>,
    colors: Ios27PageColors,
    dark: Boolean,
    onAction: (String) -> Unit,
    modifier: Modifier,
) {
    val surface = Color(0xFF787880).copy(alpha = if (dark) 0.28f else 0.16f)
    Column(modifier.clip(RoundedCornerShape(26.dp)).background(surface)) {
        labels.forEachIndexed { index, label ->
            val rowShape = when {
                labels.size == 1 -> RoundedCornerShape(26.dp)
                index == 0 -> RoundedCornerShape(topStart = 26.dp, topEnd = 26.dp)
                index == labels.lastIndex -> RoundedCornerShape(bottomStart = 26.dp, bottomEnd = 26.dp)
                else -> RoundedCornerShape(0.dp)
            }
            Box(
                Modifier.fillMaxWidth().height(52.dp)
                    .ios27PressedClickable(
                        shape = rowShape,
                        role = Role.Button,
                        onClick = { onAction(label) },
                    ),
            ) {
                val sourceGlyph = sourceGlyphs.getOrNull(index)
                if (sourceGlyph != null) {
                    Box(Modifier.offset(x = 8.dp).size(48.dp, 52.dp)) {
                        SceneGlyphText(sourceGlyph, colors.label)
                    }
                } else {
                    ActivityGlyph(
                        icons[index],
                        colors.label,
                        Modifier.offset(x = 8.dp).size(48.dp).padding(12.dp),
                    )
                }
                if (index > 0) {
                    Box(
                        Modifier.offset(x = 56.dp).width(298.dp).height(1.dp)
                            .background(if (dark) Color.White.copy(alpha = 0.14f) else Color(0xFFD1D1D6)),
                    )
                }
                Box(
                    Modifier.offset(x = 56.dp, y = 1.dp).width(298.dp).height(50.dp),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    Text(label, style = Ios27Tokens.Type.Body, color = colors.label, maxLines = 1)
                }
            }
        }
    }
}

@Composable
private fun ActivitySeparator(dark: Boolean) {
    Spacer(Modifier.fillMaxWidth().padding(horizontal = 24.dp).height(1.dp)
        .background(if (dark) Color.White.copy(alpha = 0.14f) else Color(0xFFE6E6E6)))
}

@Composable
private fun ActivityGlyph(icon: ActivityIcon, tint: Color, modifier: Modifier = Modifier) {
    Canvas(modifier) {
        val stroke = 2.4.dp.toPx()
        val cx = size.width / 2f
        val cy = size.height / 2f
        when (icon) {
            ActivityIcon.Close -> {
                drawLine(tint, Offset(cx - size.minDimension * .32f, cy - size.minDimension * .32f), Offset(cx + size.minDimension * .32f, cy + size.minDimension * .32f), stroke, StrokeCap.Round)
                drawLine(tint, Offset(cx + size.minDimension * .32f, cy - size.minDimension * .32f), Offset(cx - size.minDimension * .32f, cy + size.minDimension * .32f), stroke, StrokeCap.Round)
            }
            ActivityIcon.People -> {
                val headRadius = size.minDimension * .16f
                drawCircle(tint, headRadius, Offset(cx - size.width * .17f, cy - size.height * .20f))
                drawCircle(tint, headRadius * .88f, Offset(cx + size.width * .20f, cy - size.height * .12f))
                drawArc(
                    tint,
                    190f,
                    160f,
                    false,
                    Offset(cx - size.width * .42f, cy - size.height * .02f),
                    androidx.compose.ui.geometry.Size(size.width * .55f, size.height * .70f),
                    style = Stroke(stroke),
                )
                drawArc(
                    tint,
                    195f,
                    150f,
                    false,
                    Offset(cx - size.width * .02f, cy + size.height * .02f),
                    androidx.compose.ui.geometry.Size(size.width * .48f, size.height * .60f),
                    style = Stroke(stroke),
                )
            }
            ActivityIcon.Star -> {
                val path = androidx.compose.ui.graphics.Path()
                for (i in 0..9) { val angle = Math.PI * (-0.5 + i / 5.0); val r = if (i % 2 == 0) size.minDimension * .44f else size.minDimension * .19f; val p = Offset(cx + (kotlin.math.cos(angle) * r).toFloat(), cy + (kotlin.math.sin(angle) * r).toFloat()); if (i == 0) path.moveTo(p.x, p.y) else path.lineTo(p.x, p.y) }
                path.close(); drawPath(path, tint, style = Stroke(stroke))
            }
            ActivityIcon.Glasses -> { drawCircle(tint, size.minDimension * .22f, Offset(cx - size.minDimension * .24f, cy)); drawCircle(tint, size.minDimension * .22f, Offset(cx + size.minDimension * .24f, cy)); drawLine(tint, Offset(cx - size.minDimension * .02f, cy), Offset(cx + size.minDimension * .02f, cy), stroke); drawLine(tint, Offset(cx - size.minDimension * .46f, cy - 2.dp.toPx()), Offset(cx - size.minDimension * .32f, cy), stroke) }
            ActivityIcon.Bookmark -> { val p = androidx.compose.ui.graphics.Path(); p.moveTo(cx - 8.dp.toPx(), cy - 10.dp.toPx()); p.lineTo(cx + 8.dp.toPx(), cy - 10.dp.toPx()); p.lineTo(cx + 8.dp.toPx(), cy + 10.dp.toPx()); p.lineTo(cx, cy + 5.dp.toPx()); p.lineTo(cx - 8.dp.toPx(), cy + 10.dp.toPx()); p.close(); drawPath(p, tint, style = Stroke(stroke)) }
            ActivityIcon.Copy -> { drawRoundRect(tint, Offset(cx - 7.dp.toPx(), cy - 8.dp.toPx()), androidx.compose.ui.geometry.Size(13.dp.toPx(), 16.dp.toPx()), cornerRadius = androidx.compose.ui.geometry.CornerRadius(2.dp.toPx()), style = Stroke(stroke)); drawRoundRect(tint, Offset(cx - 2.dp.toPx(), cy - 12.dp.toPx()), androidx.compose.ui.geometry.Size(13.dp.toPx(), 16.dp.toPx()), cornerRadius = androidx.compose.ui.geometry.CornerRadius(2.dp.toPx()), style = Stroke(stroke)) }
            ActivityIcon.Home -> { drawRoundRect(tint, Offset(cx - 9.dp.toPx(), cy - 9.dp.toPx()), androidx.compose.ui.geometry.Size(18.dp.toPx(), 18.dp.toPx()), cornerRadius = androidx.compose.ui.geometry.CornerRadius(4.dp.toPx()), style = Stroke(stroke)); drawLine(tint, Offset(cx, cy - 4.dp.toPx()), Offset(cx, cy + 5.dp.toPx()), stroke); drawLine(tint, Offset(cx - 4.dp.toPx(), cy), Offset(cx + 4.dp.toPx(), cy), stroke) }
            ActivityIcon.Messages -> { drawRoundRect(tint, Offset(cx - 10.dp.toPx(), cy - 8.dp.toPx()), androidx.compose.ui.geometry.Size(20.dp.toPx(), 15.dp.toPx()), cornerRadius = androidx.compose.ui.geometry.CornerRadius(5.dp.toPx())); drawLine(tint, Offset(cx - 5.dp.toPx(), cy + 6.dp.toPx()), Offset(cx - 8.dp.toPx(), cy + 10.dp.toPx()), stroke) }
            ActivityIcon.Mail -> { drawRoundRect(tint, Offset(cx - 11.dp.toPx(), cy - 8.dp.toPx()), androidx.compose.ui.geometry.Size(22.dp.toPx(), 16.dp.toPx()), cornerRadius = androidx.compose.ui.geometry.CornerRadius(3.dp.toPx()), style = Stroke(stroke)); drawLine(tint, Offset(cx - 10.dp.toPx(), cy - 7.dp.toPx()), Offset(cx, cy + 1.dp.toPx()), stroke); drawLine(tint, Offset(cx, cy + 1.dp.toPx()), Offset(cx + 10.dp.toPx(), cy - 7.dp.toPx()), stroke) }
            ActivityIcon.Notes -> { drawRoundRect(tint, Offset(cx - 9.dp.toPx(), cy - 11.dp.toPx()), androidx.compose.ui.geometry.Size(18.dp.toPx(), 22.dp.toPx()), cornerRadius = androidx.compose.ui.geometry.CornerRadius(3.dp.toPx()), style = Stroke(stroke)); drawLine(tint, Offset(cx - 5.dp.toPx(), cy - 2.dp.toPx()), Offset(cx + 5.dp.toPx(), cy - 2.dp.toPx()), stroke); drawLine(tint, Offset(cx - 5.dp.toPx(), cy + 5.dp.toPx()), Offset(cx + 5.dp.toPx(), cy + 5.dp.toPx()), stroke) }
            ActivityIcon.AirDrop -> { drawCircle(tint, size.minDimension * .38f, Offset(cx, cy), style = Stroke(stroke)); drawCircle(tint, size.minDimension * .10f, Offset(cx, cy)); drawArc(tint, 35f, 110f, false, Offset(cx - size.minDimension * .4f, cy - size.minDimension * .4f), androidx.compose.ui.geometry.Size(size.minDimension * .8f, size.minDimension * .8f), style = Stroke(stroke)) }
            ActivityIcon.Markup -> { drawCircle(tint, size.minDimension * .38f, Offset(cx, cy), style = Stroke(stroke)); drawLine(tint, Offset(cx - 6.dp.toPx(), cy + 7.dp.toPx()), Offset(cx + 6.dp.toPx(), cy - 7.dp.toPx()), stroke) }
            ActivityIcon.Print -> {
                drawRoundRect(
                    tint,
                    Offset(cx - 9.dp.toPx(), cy - 2.dp.toPx()),
                    androidx.compose.ui.geometry.Size(18.dp.toPx(), 10.dp.toPx()),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(2.dp.toPx()),
                    style = Stroke(stroke),
                )
                drawRoundRect(
                    tint,
                    Offset(cx - 7.dp.toPx(), cy - 10.dp.toPx()),
                    androidx.compose.ui.geometry.Size(14.dp.toPx(), 9.dp.toPx()),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(1.dp.toPx()),
                    style = Stroke(stroke),
                )
                drawLine(
                    tint,
                    Offset(cx - 6.dp.toPx(), cy + 4.dp.toPx()),
                    Offset(cx + 6.dp.toPx(), cy + 4.dp.toPx()),
                    stroke,
                )
            }
        }
    }
}
