package dev.liquidglass.sample

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.text.font.FontWeight
import com.zeaze.tianyinwallpaper.backdrop.backdrops.*
import com.zeaze.tianyinwallpaper.catalog.components.LiquidButton
import com.zeaze.tianyinwallpaper.catalog.components.LiquidToggle
import com.zeaze.tianyinwallpaper.ui.commom.ProgressiveBlurContent
import dev.liquidglass.ios27.*
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.cos
import kotlin.math.sin

private const val ListExampleSceneFile = "12740-24203.json"

/** Examples/List (38:2882). Shared 16pt page margin, 370-wide group, 12 rows totaling 656. */
@Composable
internal fun ListDetailPage(dark: Boolean, onBack: () -> Unit) {
    ListDetailContent(
        dark = dark,
        onBack = onBack,
    )
}

@Composable
private fun ListDetailContent(dark: Boolean, onBack: () -> Unit) {
    val colors = LocalIos27PageColors.current
    val context = LocalContext.current
    val scroll = rememberScrollState()
    val supported = Build.VERSION.SDK_INT >= 33
    val reveal = with(LocalDensity.current) { 48.dp.toPx() }
    val toolbar = rememberSceneToolbar(ListExampleSceneFile)
    val popupLabelGlyph = rememberSceneText(ListExampleSceneFile, "Pop-up")
    val starGlyph = rememberSceneText(ListExampleSceneFile, "Symbol")
    val buttonGlyph = rememberSceneText(ListExampleSceneFile, "Button")
    val actionGlyph = rememberSceneSubtreeText(ListExampleSceneFile, "Row - Button")
    val backdrop = rememberExamplePageBackdrop(colors.page)
    val rowBackdrop = rememberCanvasBackdrop { drawRect(colors.group) }
    var starred by rememberSaveable { mutableStateOf(false) }
    var enabled by rememberSaveable { mutableStateOf(true) }
    var count by rememberSaveable { mutableIntStateOf(0) }
    var year by rememberSaveable { mutableIntStateOf(2024) }
    var month by rememberSaveable { mutableIntStateOf(5) }
    var day by rememberSaveable { mutableIntStateOf(1) }
    var hour by rememberSaveable { mutableIntStateOf(9) }
    var minute by rememberSaveable { mutableIntStateOf(41) }
    var popup by remember { mutableStateOf(false) }
    var choice by rememberSaveable { mutableStateOf("Pop-up") }
    var message by remember { mutableStateOf<String?>(null) }
    val dateLabel = SimpleDateFormat("MMMM yyyy", Locale.US).format(Calendar.getInstance().apply {
        set(year, month, day)
    }.time)
    val timeLabel = String.format(Locale.US, "%d:%02d %s", (hour % 12).let { if (it == 0) 12 else it }, minute, if (hour < 12) "AM" else "PM")
    val pickDate = {
        DatePickerDialog(context, { _, y, m, d -> year = y; month = m; day = d }, year, month, day).show()
    }
    val pickTime = {
        TimePickerDialog(context, { _, h, m -> hour = h; minute = m }, hour, minute, false).show()
    }
    ExampleRecordedDesignPage(
        background = colors.page,
        backdrop = backdrop,
        content = { topInset, _ ->
            Column(
                Modifier.fillMaxSize()
                    .verticalScroll(scroll)
                    .padding(top = topInset + 54.dp, bottom = 50.dp)
                    .padding(horizontal = Ios27Tokens.ContentMargin.dp),
            ) {
            Box(Modifier.heightIn(min = 67.dp)) {
                toolbar?.largeTitle?.let { title ->
                    SceneGlyphText(title, if (dark) colors.label else title.color ?: colors.label)
                } ?: Text(
                    "Title",
                    style = Ios27Tokens.Type.LargeTitle.copy(fontWeight = FontWeight.Bold),
                    color = colors.label,
                )
            }
            ExamplesListInset {
                Ios27ListRows((0..11).toList()) { index ->
                    if (index == 11) {
                        Box(
                            Modifier.fillMaxWidth().heightIn(min = 52.dp)
                                .ios27ListPressedClickable(
                                    shape = RoundedCornerShape(
                                        bottomStart = 26.dp,
                                        bottomEnd = 26.dp,
                                    ),
                                    onClick = { message = "Action" },
                                ),
                            Alignment.Center,
                        ) {
                            if (actionGlyph != null) {
                                SceneGlyphText(actionGlyph, colors.accent)
                            } else {
                                Text("Action", color = colors.accent, style = Ios27Tokens.Type.Body)
                            }
                        }
                    } else if (index == 2) {
                        Row(Modifier.fillMaxWidth().heightIn(min = 68.dp).padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically) {
                            CheckerImage()
                            Spacer(Modifier.width(8.dp))
                            Column(Modifier.weight(1f)) {
                                Text("Title", style = Ios27Tokens.Type.Body, color = colors.label)
                                Text("Subtitle", style = Ios27Tokens.Type.Subheadline, color = colors.secondaryLabel)
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.CenterVertically) {
                                Text("Detail", style = Ios27Tokens.Type.Body, color = colors.secondaryLabel)
                                Ios27DisclosureIndicator(color = colors.secondaryLabel)
                            }
                        }
                    } else if (index == 9) {
                        Ios27StepperExample(ListExampleSceneFile, count) { count = it }
                    } else {
                        ReferenceListRow(
                            subtitle = if (index == 1) "Subtitle" else null,
                            detail = if (index in 0..2 || index == 4) "Detail" else null,
                            disclosure = index in 0..2,
                            accessorySpacing = if (index == 4) 12.dp else 16.dp,
                            trailing = {
                                when (index) {
                                    0 -> Box(
                                        Modifier.size(22.dp)
                                            .ios27PressedClickable(
                                                shape = CircleShape,
                                                onClick = { starred = !starred },
                                            )
                                            .semantics { contentDescription = if (starred) "取消收藏" else "收藏" },
                                        Alignment.Center,
                                    ) {
                                        if (!starred && starGlyph != null) {
                                            SceneGlyphText(starGlyph, colors.accent)
                                        } else {
                                            StarIcon(colors.accent, starred)
                                        }
                                    }
                                    3 -> {
                                        val buttonClick = Modifier.clickable(
                                            interactionSource = remember { MutableInteractionSource() },
                                            indication = null,
                                            onClick = { message = "Button" },
                                        )
                                        if (buttonGlyph != null) {
                                            SceneGlyphText(buttonGlyph, colors.accent, buttonClick)
                                        } else {
                                            Text(
                                                "Button",
                                                color = colors.accent,
                                                style = Ios27Tokens.Type.Body,
                                                modifier = buttonClick,
                                            )
                                        }
                                    }
                                    4 -> Ios27DisclosureIndicator(
                                        color = colors.accent,
                                        variant = Ios27DisclosureVariant.Disclosure,
                                    )
                                    5 -> ValuePill(dateLabel, 106.dp) { pickDate() }
                                    6 -> Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                        ValuePill(dateLabel, 106.dp) { pickDate() }; ValuePill(timeLabel, 86.dp) { pickTime() }
                                    }
                                    7 -> Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                        ValuePill(timeLabel, 86.dp) { pickTime() }
                                        Ios27DragIndicator(color = colors.secondaryLabel)
                                    }
                                    8 -> Box {
                                        Row(
                                            Modifier.ios27PressedClickable(
                                                shape = RoundedCornerShape(percent = 50),
                                                onClick = { popup = true },
                                            ),
                                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                        ) {
                                            if (choice == "Pop-up" && popupLabelGlyph != null) {
                                                SceneGlyphText(popupLabelGlyph, colors.secondaryLabel)
                                            } else {
                                                Text(choice, color = colors.secondaryLabel, style = Ios27Tokens.Type.Body)
                                            }
                                            Ios27IndicatorGlyph(
                                                variant = Ios27IndicatorVariant.PopupMenu,
                                                color = colors.secondaryLabel,
                                            )
                                        }
                                        DropdownMenu(popup, onDismissRequest = { popup = false }) {
                                            listOf("First", "Second", "Third").forEach { option ->
                                                DropdownMenuItem(onClick = { choice = option; popup = false }) { Text(option) }
                                            }
                                        }
                                    }
                                    10 -> if (supported) LiquidToggle({ enabled }, { enabled = it }, rowBackdrop, !dark)
                                        else Switch(enabled, { enabled = it })
                                }
                            })
                    }
                }
            }
            }
        },
        overlay = { topInset, recordedBackdrop ->
            ExampleTopChrome(
                backdrop = if (supported) recordedBackdrop else null,
                isLightTheme = !dark,
                blurAlpha = (scroll.value / reveal).coerceIn(0f, 1f),
            )
            Row(
                Modifier.fillMaxWidth()
                    .padding(top = topInset)
                    .padding(horizontal = 16.dp)
                    .height(44.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Ios27BackButton(
                    onBack,
                    if (supported) recordedBackdrop else null,
                    backGlyph = {
                        SceneToolbarButtonContent(
                            button = toolbar?.buttons?.getOrNull(0),
                            tint = colors.label,
                            useSourceColor = !dark,
                        )
                    },
                )
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    if (supported) {
                        LiquidButton(
                            { message = "Toolbar" },
                            recordedBackdrop,
                            modifier = Modifier.width(44.dp),
                            buttonHeight = 44.dp,
                            contentPadding = PaddingValues(0.dp),
                        ) {
                            SceneToolbarButtonContent(
                                button = toolbar?.buttons?.getOrNull(1),
                                tint = colors.label,
                            )
                        }
                        LiquidButton(
                            { message = "Toolbar group" },
                            recordedBackdrop,
                            modifier = Modifier.width(160.dp),
                            buttonHeight = 44.dp,
                            contentPadding = PaddingValues(0.dp),
                        ) {
                            SceneToolbarButtonContent(
                                button = toolbar?.buttons?.getOrNull(2),
                                tint = colors.label,
                                onGlyphClick = { index ->
                                    message = "Toolbar ${index + 1}"
                                },
                            )
                        }
                    } else {
                        TextButton(onClick = { message = "Toolbar" }) {
                            Text("•••", color = colors.label)
                        }
                    }
                }
            }
        },
    )
    message?.let { text ->
        AlertDialog(onDismissRequest = { message = null }, title = { Text(text) },
            confirmButton = { TextButton(onClick = { message = null }) { Text("好") } })
    }
}

/** Secondary-page Inset uses the shared 16pt page margin, so the group is 370x656pt. */
@Composable
private fun ExamplesListInset(content: @Composable ColumnScope.() -> Unit) {
    val colors = LocalIos27PageColors.current
    Column(
        Modifier.fillMaxWidth().height(656.dp)
            .clip(RoundedCornerShape(26.dp))
            .background(colors.group),
        content = content,
    )
}

@Composable
private fun ValuePill(label: String, width: Dp, onClick: () -> Unit) {
    val colors = LocalIos27PageColors.current
    val shape = RoundedCornerShape(percent = 50)
    Box(
        Modifier.widthIn(min = width).heightIn(min = 34.dp)
            .background(colors.pressed, shape)
            .ios27PressedClickable(
                shape = shape,
                onClick = onClick,
            )
            .padding(horizontal = 11.dp),
        Alignment.Center,
    ) {
        Text(label, style = Ios27Tokens.Type.Body, color = colors.label)
    }
}

/** Original row has no vertical content padding; accessory gaps are 16, not the generic row's 8. */
@Composable
private fun ReferenceListRow(
    subtitle: String?,
    detail: String?,
    disclosure: Boolean,
    accessorySpacing: Dp = 16.dp,
    trailing: @Composable () -> Unit,
) {
    val colors = LocalIos27PageColors.current
    Row(Modifier.fillMaxWidth().heightIn(min = if (subtitle == null) 52.dp else 68.dp)
        .padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
        Column(Modifier.weight(1f)) {
            Text("Title", style = Ios27Tokens.Type.Body, color = colors.label)
            if (subtitle != null) Text(subtitle, style = Ios27Tokens.Type.Subheadline, color = colors.secondaryLabel)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(accessorySpacing), verticalAlignment = Alignment.CenterVertically) {
            if (detail != null) Text(detail, style = Ios27Tokens.Type.Body, color = colors.secondaryLabel)
            trailing()
            if (disclosure) {
                Ios27DisclosureIndicator(color = colors.secondaryLabel)
            }
        }
    }
}

@Composable
private fun CheckerImage() {
    val colors = LocalIos27PageColors.current
    Canvas(Modifier.size(68.dp).semantics { contentDescription = "原稿棋盘格占位图" }) {
        val step = size.width / 16
        for (y in 0..15) for (x in 0..15) drawRect(
            if ((x + y) % 2 == 0) colors.pressed else colors.group,
            Offset(x * step, y * step), androidx.compose.ui.geometry.Size(step, step))
    }
}

@Composable
private fun StarIcon(color: Color, filled: Boolean) {
    Canvas(Modifier.size(22.dp)) {
        val path = Path()
        repeat(10) { i ->
            val angle = Math.PI * i / 5 - Math.PI / 2
            val radius = size.minDimension * if (i % 2 == 0) 0.46f else 0.21f
            val x = center.x + cos(angle).toFloat() * radius
            val y = center.y + sin(angle).toFloat() * radius
            if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }
        path.close()
        if (filled) drawPath(path, color) else drawPath(path, color, style = Stroke(1.3.dp.toPx()))
    }
}
