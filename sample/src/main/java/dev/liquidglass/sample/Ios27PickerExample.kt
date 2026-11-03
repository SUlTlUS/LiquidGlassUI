package dev.liquidglass.sample

import android.graphics.RectF
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import dev.liquidglass.ios27.ios27AndroidProgress
import dev.liquidglass.ios27.ios27AndroidPane
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.DpOffset
import java.text.DateFormatSymbols
import java.util.Calendar
import java.util.Locale
import com.kyant.shapes.RoundedRectangle
import com.zeaze.tianyinwallpaper.backdrop.Backdrop
import com.zeaze.tianyinwallpaper.backdrop.drawBackdrop
import com.zeaze.tianyinwallpaper.backdrop.effects.blur
import com.zeaze.tianyinwallpaper.backdrop.effects.lens
import com.zeaze.tianyinwallpaper.backdrop.effects.colorControls
import com.zeaze.tianyinwallpaper.backdrop.highlight.Highlight
import com.zeaze.tianyinwallpaper.backdrop.shadow.Shadow
import com.zeaze.tianyinwallpaper.catalog.components.WheelPicker
import com.example.liquidglass.GlassMaterial
import com.example.liquidglass.LiquidGlassView
import dev.liquidglass.ios27.Ios27DisclosureIndicator
import dev.liquidglass.ios27.Ios27DisclosureVariant
import dev.liquidglass.ios27.Ios27IndicatorGlyph
import dev.liquidglass.ios27.Ios27IndicatorVariant
import dev.liquidglass.ios27.Ios27ListRows
import dev.liquidglass.ios27.Ios27Tokens
import dev.liquidglass.ios27.LocalIos27PageColors
import dev.liquidglass.ios27.ios27PressedClickable

private val PickerAccessoryPillShape = RoundedCornerShape(percent = 50)
private val PickerInlineFrameRadius = 13.dp
private val PickerCompactFrameRadius = Ios27Tokens.GroupRadius.dp

private val PickerWeekdays = listOf("SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT")

private fun pickerMonthTitle(year: Int, month: Int): String =
    "${DateFormatSymbols(Locale.US).months[month]} $year"

private fun pickerDateLabel(year: Int, month: Int, day: Int): String =
    "${DateFormatSymbols(Locale.US).shortMonths[month]} $day, $year"

private fun Rect.matchesSource(other: Rect?, tolerancePx: Float = 1f): Boolean =
    other != null &&
        kotlin.math.abs(left - other.left) <= tolerancePx &&
        kotlin.math.abs(top - other.top) <= tolerancePx &&
        kotlin.math.abs(right - other.right) <= tolerancePx &&
        kotlin.math.abs(bottom - other.bottom) <= tolerancePx

private fun pickerMonthGrid(year: Int, month: Int): List<Int> {
    val calendar = Calendar.getInstance().apply {
        clear()
        set(year, month, 1)
    }
    val leadingEmpty = calendar.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY
    val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    return List(leadingEmpty) { 0 } + (1..daysInMonth)
}

@Composable
private fun MonthYearWheelPicker(
    year: Int,
    month: Int,
    onYearSelected: (Int) -> Unit,
    onMonthSelected: (Int) -> Unit,
) {
    val colors = LocalIos27PageColors.current
    Row(
        modifier = Modifier.fillMaxWidth().height(160.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        WheelPicker(
            count = 201,
            initialIndex = (year - 1900).coerceIn(0, 200),
            onItemSelected = { index -> onYearSelected(1900 + index) },
            contentColor = colors.label,
            label = "年",
            modifier = Modifier.weight(1f).ios27AndroidProgress(
                label = "年", value = year.toFloat(), range = 1900f..2100f, steps = 199,
                valueDescription = "$year 年",
                onValueChange = { onYearSelected(it.toInt()) },
            ),
            formatValue = { index -> "${1900 + index}" },
        )
        WheelPicker(
            count = 12,
            initialIndex = month.coerceIn(0, 11),
            onItemSelected = onMonthSelected,
            contentColor = colors.label,
            label = "月",
            modifier = Modifier.weight(1f).ios27AndroidProgress(
                label = "月", value = month.toFloat(), range = 0f..11f, steps = 10,
                valueDescription = "${month + 1} 月",
                onValueChange = { onMonthSelected(it.toInt()) },
            ),
            formatValue = { index -> "${index + 1}" },
        )
    }
}

@Composable
private fun TimeWheelPicker(
    hour: Int,
    minute: Int,
    onHourSelected: (Int) -> Unit,
    onMinuteSelected: (Int) -> Unit,
) {
    val colors = LocalIos27PageColors.current
    Row(
        modifier = Modifier.fillMaxWidth().height(160.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        WheelPicker(
            count = 24,
            initialIndex = hour.coerceIn(0, 23),
            onItemSelected = onHourSelected,
            contentColor = colors.label,
            label = "时",
            modifier = Modifier.weight(1f).ios27AndroidProgress(
                label = "时", value = hour.toFloat(), range = 0f..23f, steps = 22,
                valueDescription = "$hour 时",
                onValueChange = { onHourSelected(it.toInt()) },
            ),
        )
        WheelPicker(
            count = 60,
            initialIndex = minute.coerceIn(0, 59),
            onItemSelected = onMinuteSelected,
            contentColor = colors.label,
            label = "分",
            modifier = Modifier.weight(1f).ios27AndroidProgress(
                label = "分", value = minute.toFloat(), range = 0f..59f, steps = 58,
                valueDescription = "$minute 分",
                onValueChange = { onMinuteSelected(it.toInt()) },
            ),
        )
    }
}

@Composable
private fun PickerPillText(
    text: String,
    width: Float,
    colors: dev.liquidglass.ios27.Ios27PageColors,
    onClick: () -> Unit,
    onClickWithBounds: ((Rect) -> Unit)? = null,
    hiddenSourceBounds: Rect? = null,
) {
    val dark = colors.page == Ios27Tokens.PageDark
    val pill = if (dark) Ios27Tokens.PressedDark else Ios27Tokens.PressedLight
    var boundsInRoot by remember { mutableStateOf<Rect?>(null) }
    val sourceHidden = boundsInRoot?.matchesSource(hiddenSourceBounds) == true
    Box(
        Modifier.width(width.dp).height(34.dp)
            .onGloballyPositioned { coordinates ->
                val position = coordinates.positionInRoot()
                boundsInRoot = Rect(
                    left = position.x,
                    top = position.y,
                    right = position.x + coordinates.size.width,
                    bottom = position.y + coordinates.size.height,
                )
            }
            .alpha(if (sourceHidden) 0f else 1f)
            .ios27PressedClickable(
                shape = PickerAccessoryPillShape,
                enabled = !sourceHidden,
                role = Role.Button,
                onClick = {
                    val bounds = boundsInRoot
                    if (bounds != null && onClickWithBounds != null) onClickWithBounds(bounds)
                    else onClick()
                },
            )
            .semantics { contentDescription = text },
        contentAlignment = Alignment.Center,
    ) {
        Box(
            Modifier.fillMaxWidth().height(34.dp)
                .clip(PickerAccessoryPillShape)
                .background(pill),
            contentAlignment = Alignment.Center,
        ) {
            Text(text, color = colors.label, style = Ios27Tokens.Type.Body)
        }
    }
}

@Composable
private fun PickerCalendar(
    compact: Boolean,
    background: Backdrop,
    year: Int,
    month: Int,
    selectedDay: Int,
    timeLabel: String,
    onSelectedDay: (Int) -> Unit,
    onHeaderClick: () -> Unit,
    onHeaderBoundsChanged: (Rect) -> Unit,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onTimeClick: (Rect) -> Unit,
    hiddenMonthYearSourceBounds: Rect? = null,
    hiddenTimeSourceBounds: Rect? = null,
) {
    val colors = LocalIos27PageColors.current
    val dark = colors.page == Ios27Tokens.PageDark
    val horizontal = if (compact) 16.dp else 12.dp
    val frameRadius = if (compact) PickerCompactFrameRadius else PickerInlineFrameRadius
    val frameShape = if (compact) RoundedCornerShape(PickerCompactFrameRadius)
        else RoundedCornerShape(PickerInlineFrameRadius)
    val today = Calendar.getInstance()
    val todayYear = today.get(Calendar.YEAR)
    val todayMonth = today.get(Calendar.MONTH)
    val todayDay = today.get(Calendar.DAY_OF_MONTH)
    val cells = pickerMonthGrid(year, month)
    var headerBoundsInRoot by remember { mutableStateOf<Rect?>(null) }
    val headerHidden = headerBoundsInRoot?.matchesSource(hiddenMonthYearSourceBounds) == true

    Column(
        Modifier.fillMaxWidth().then(
            if (!compact && Build.VERSION.SDK_INT >= 33) Modifier.drawBackdrop(
                backdrop = background,
                shape = { RoundedRectangle(frameRadius) },
                effects = {
                    colorControls(brightness = if (dark) 0f else 0.2f, saturation = 1.5f)
                    blur(if (dark) 8.dp.toPx() else 16.dp.toPx())
                    lens(24.dp.toPx(), 48.dp.toPx(), depthEffect = true)
                },
                highlight = { Highlight.Default.copy(width = 0.75.dp, blurRadius = 1.dp, alpha = 1f) },
                shadow = {
                    Shadow(radius = 48.dp, offset = DpOffset(0.dp, 8.dp),
                        color = Color.Black.copy(alpha = 0.25f))
                },
                onDrawSurface = {
                    if (dark) {
                        drawRect(Color(0xFF121212).copy(alpha = 0.4f))
                    } else {
                        drawRect(Color(0xFFFAFAFA).copy(alpha = 0.6f))
                        drawRect(Color(0xFFBFBFBF).copy(alpha = 0.1f), blendMode = BlendMode.Darken)
                    }
                },
            ) else Modifier.background(colors.group, frameShape)
        )
            .clip(frameShape)
            .padding(horizontal = horizontal),
    ) {
        Row(Modifier.fillMaxWidth().height(48.dp), verticalAlignment = Alignment.Bottom) {
            Row(
                Modifier.weight(1f).padding(bottom = 8.dp)
                    .onGloballyPositioned { coordinates ->
                        val position = coordinates.positionInRoot()
                        val bounds = Rect(
                            left = position.x,
                            top = position.y,
                            right = position.x + coordinates.size.width,
                            bottom = position.y + coordinates.size.height,
                        )
                        headerBoundsInRoot = bounds
                        onHeaderBoundsChanged(bounds)
                    }
                    .alpha(if (headerHidden) 0f else 1f)
                    .ios27PressedClickable(
                        shape = RoundedCornerShape(10.dp),
                        enabled = !headerHidden,
                        onClick = onHeaderClick,
                    ),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    pickerMonthTitle(year, month),
                    style = Ios27Tokens.Type.Title2.copy(fontWeight = FontWeight.SemiBold),
                    color = colors.label,
                )
                Box(Modifier.padding(start = 4.dp)) {
                    Ios27DisclosureIndicator(
                        color = colors.accent,
                        variant = Ios27DisclosureVariant.PickerDisclosure,
                    )
                }
            }
            Box(
                Modifier.size(42.dp, 40.dp)
                    .ios27PressedClickable(
                        shape = RoundedCornerShape(20.dp),
                        onClick = onPreviousMonth,
                    ).semantics { contentDescription = "上个月" },
                contentAlignment = Alignment.Center,
            ) {
                Ios27IndicatorGlyph(
                    variant = Ios27IndicatorVariant.PickerPrevious,
                    color = colors.label,
                )
            }
            Box(
                Modifier.size(42.dp, 40.dp)
                    .ios27PressedClickable(
                        shape = RoundedCornerShape(20.dp),
                        onClick = onNextMonth,
                    ).semantics { contentDescription = "下个月" },
                contentAlignment = Alignment.Center,
            ) {
                Ios27DisclosureIndicator(
                    color = colors.label,
                    variant = Ios27DisclosureVariant.PickerNext,
                )
            }
        }
        Row(Modifier.fillMaxWidth().height(32.dp), verticalAlignment = Alignment.CenterVertically) {
            PickerWeekdays.forEach { weekday ->
                Text(
                    weekday,
                    Modifier.weight(1f),
                    color = colors.secondaryLabel,
                    style = Ios27Tokens.Type.Footnote.copy(fontWeight = FontWeight.SemiBold),
                    textAlign = TextAlign.Center,
                )
            }
        }
        Column(Modifier.fillMaxWidth().height(244.dp)) {
            cells.chunked(7).forEach { week ->
                Row(Modifier.fillMaxWidth().weight(1f)) {
                    week.forEach { day ->
                        Box(Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                            if (day != 0) {
                                val selected = day == selectedDay
                                val isToday = year == todayYear && month == todayMonth && day == todayDay
                                val todayFill = if (dark) colors.accent.copy(alpha = 0.24f) else Color(0x337EB8E8)
                                Box(
                                    Modifier.size(38.dp)
                                        .background(
                                            if (selected) colors.label else if (isToday) todayFill else Color.Transparent,
                                            RoundedCornerShape(19.dp),
                                        )
                                        .ios27PressedClickable(
                                            shape = RoundedCornerShape(19.dp),
                                            onClick = { onSelectedDay(day) },
                                        ).semantics {
                                            contentDescription = "$year 年 ${month + 1} 月 $day 日"
                                            role = Role.RadioButton
                                            this.selected = selected
                                        },
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Text(
                                        day.toString(),
                                        color = if (selected) colors.group else if (isToday) colors.accent else colors.label,
                                        style = Ios27Tokens.Type.Title2,
                                        textAlign = TextAlign.Center,
                                    )
                                }
                            }
                        }
                    }
                    repeat(7 - week.size) { Box(Modifier.weight(1f)) }
                }
            }
        }
        Box(
            Modifier.fillMaxWidth().height(Ios27Tokens.SeparatorRenderHeight.dp)
                .background(colors.separator),
        )
        Row(Modifier.fillMaxWidth().height(52.dp), verticalAlignment = Alignment.CenterVertically) {
            Text("Time", Modifier.weight(1f), style = Ios27Tokens.Type.Title3, color = colors.label)
            PickerPillText(
                text = timeLabel,
                width = 86f,
                colors = colors,
                onClick = {},
                onClickWithBounds = onTimeClick,
                hiddenSourceBounds = hiddenTimeSourceBounds,
            )
        }
    }
}

@Composable
private fun CompactAccessoryRows(
    timeLabel: String,
    dateLabel: String,
    onTimeClick: (Rect) -> Unit,
    onDateClick: (Rect) -> Unit,
    hiddenMonthYearSourceBounds: Rect? = null,
    hiddenTimeSourceBounds: Rect? = null,
) {
    val colors = LocalIos27PageColors.current
    Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(Ios27Tokens.GroupRadius.dp)).background(colors.group)) {
        Ios27ListRows(listOf(0, 1, 2)) { index ->
            Row(Modifier.fillMaxWidth().height(52.dp).padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                Text("Title", Modifier.weight(1f), style = Ios27Tokens.Type.Title3, color = colors.label)
                when (index) {
                    0 -> {
                        PickerPillText(
                            text = dateLabel,
                            width = 116f,
                            colors = colors,
                            onClick = {},
                            onClickWithBounds = onDateClick,
                            hiddenSourceBounds = hiddenMonthYearSourceBounds,
                        )
                        Spacer(Modifier.width(6.dp))
                        PickerPillText(
                            text = timeLabel,
                            width = 86f,
                            colors = colors,
                            onClick = {},
                            onClickWithBounds = onTimeClick,
                            hiddenSourceBounds = hiddenTimeSourceBounds,
                        )
                    }
                    1 -> PickerPillText(
                        text = timeLabel,
                        width = 86f,
                        colors = colors,
                        onClick = {},
                        onClickWithBounds = onTimeClick,
                        hiddenSourceBounds = hiddenTimeSourceBounds,
                    )
                    else -> PickerPillText(
                        text = dateLabel,
                        width = 116f,
                        colors = colors,
                        onClick = {},
                        onClickWithBounds = onDateClick,
                        hiddenSourceBounds = hiddenMonthYearSourceBounds,
                    )
                }
            }
        }
    }
}

@Composable
private fun BoxScope.QweaMonthYearPopup(
    visible: Boolean,
    year: Int,
    month: Int,
    colors: dev.liquidglass.ios27.Ios27PageColors,
    dark: Boolean,
    background: Backdrop,
    originBounds: Rect,
    hostWidthPx: Float,
    onYearSelected: (Int) -> Unit,
    onMonthSelected: (Int) -> Unit,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    onHidden: () -> Unit,
) {
    val density = LocalDensity.current
    val progress = remember { Animatable(0f) }

    LaunchedEffect(visible) {
        progress.animateTo(
            targetValue = if (visible) 1f else 0f,
            animationSpec = tween(durationMillis = 340, easing = FastOutSlowInEasing),
        )
        if (!visible) onHidden()
    }

    val p = progress.value.coerceIn(0f, 1f)
    fun lerpBy(start: Float, end: Float, amount: Float): Float =
        start + (end - start) * amount
    fun lerpDp(start: androidx.compose.ui.unit.Dp, end: androidx.compose.ui.unit.Dp) =
        start + (end - start) * p

    val panelWidthPx = with(density) { 302.dp.toPx() }
    val panelHeightPx = with(density) { 300.dp.toPx() }
    val finalLeftPx = ((hostWidthPx - panelWidthPx) / 2f).coerceAtLeast(0f)
    val finalTopPx = with(density) { 40.dp.toPx() }

    val animatedLeftPx = lerpBy(originBounds.left, finalLeftPx, p)
    val animatedTopPx = lerpBy(originBounds.top, finalTopPx, p)
    val animatedWidthPx = lerpBy(originBounds.width, panelWidthPx, p)
    val animatedHeightPx = lerpBy(originBounds.height, panelHeightPx, p)
    val animatedRightPx = animatedLeftPx + animatedWidthPx
    val animatedBottomPx = animatedTopPx + animatedHeightPx

    val animatedLeft = with(density) { animatedLeftPx.toDp() }
    val animatedTop = with(density) { animatedTopPx.toDp() }
    val animatedWidth = with(density) { animatedWidthPx.toDp() }
    val animatedHeight = with(density) { animatedHeightPx.toDp() }
    val animatedCorner = lerpDp(10.dp, 34.dp)

    val originRect = RectF(
        originBounds.left,
        originBounds.top,
        originBounds.right,
        originBounds.bottom,
    )
    val animatedRect = RectF(
        animatedLeftPx,
        animatedTopPx,
        animatedRightPx,
        animatedBottomPx,
    )
    val primaryMerge = ((p - 0.72f) / 0.28f).coerceIn(0f, 1f)
    val primaryRect = RectF(
        lerpBy(originRect.left, animatedRect.left, primaryMerge),
        lerpBy(originRect.top, animatedRect.top, primaryMerge),
        lerpBy(originRect.right, animatedRect.right, primaryMerge),
        lerpBy(originRect.bottom, animatedRect.bottom, primaryMerge),
    )
    val startCornerPx = with(density) { 10.dp.toPx() }
    val animatedCornerPx = with(density) { animatedCorner.toPx() }
    val bridgeStrength = (4f * p * (1f - p)).coerceIn(0f, 1f)
    val smoothingPx = with(density) { 16.dp.toPx() } * bridgeStrength

    val popupSurface = if (dark) Color(0xE62C2C2E) else Color.White.copy(alpha = 0.74f)
    val animatedShape = RoundedCornerShape(animatedCorner)
    val atPressedMaskEndpoint = p <= 0.10f

    Box(Modifier.fillMaxSize()) {
        // QWEA0 sits underneath the real window and only contributes the liquid bridge
        // during the shape transition. At the final frame it is fully covered.
        if (!atPressedMaskEndpoint) {
            AndroidView(
                factory = { context ->
                    LiquidGlassView(context).apply {
                        enableDynamicBackground = false
                        useShaderPipeline = true
                        material = GlassMaterial.CLEAR
                        enableAdaptiveTint = false
                        enableSensorHighlight = true
                        enableBackdropBlur = false
                        enableChromaticDispersion = true
                        bevelWidth = with(density) { 18.dp.toPx() }
                        refractionHeight = with(density) { 8.dp.toPx() }
                        refractionFalloff = 2f
                        dispersionStrength = 0.08f
                        glassTint = if (dark) {
                            android.graphics.Color.argb(138, 44, 44, 46)
                        } else {
                            android.graphics.Color.argb(150, 248, 248, 250)
                        }
                    }
                },
                update = { glass ->
                    glass.setPrimaryShape(primaryRect, startCornerPx)
                    glass.setSecondaryShape(animatedRect, animatedCornerPx, smoothingPx)
                    glass.invalidate()
                },
                modifier = Modifier.fillMaxSize(),
            )
        }

        // This is the original window itself. We animate only its geometry; the
        // renderer/material/content remain the same.
        Box(
            Modifier
                .offset(x = animatedLeft, y = animatedTop)
                .width(animatedWidth)
                .height(animatedHeight)
                .then(
                    when {
                        atPressedMaskEndpoint -> Modifier.background(colors.pressed, animatedShape)
                        Build.VERSION.SDK_INT >= 33 -> Modifier.drawBackdrop(
                            backdrop = background,
                            shape = { RoundedRectangle(animatedCorner) },
                            effects = {
                                colorControls(brightness = if (dark) 0f else 0.2f, saturation = 1.5f)
                                blur(if (dark) 8.dp.toPx() else 16.dp.toPx())
                                lens(24.dp.toPx(), 48.dp.toPx(), depthEffect = true)
                            },
                            highlight = { Highlight.Plain },
                            shadow = {
                                Shadow(
                                    radius = 48.dp,
                                    offset = DpOffset(0.dp, 8.dp),
                                    color = Color.Black.copy(alpha = 0.22f),
                                )
                            },
                            onDrawSurface = { drawRect(popupSurface) },
                        )
                        else -> Modifier.background(popupSurface, animatedShape)
                    },
                )
                .clip(animatedShape).ios27AndroidPane("选择年月", onCancel),
            contentAlignment = Alignment.TopCenter,
        ) {
            if (p > 0.40f) Column(
                Modifier.requiredSize(302.dp, 300.dp)
                    .padding(start = 16.dp, top = 20.dp, end = 16.dp, bottom = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "选择年月",
                    style = Ios27Tokens.Type.Title2.copy(fontWeight = FontWeight.SemiBold),
                    color = colors.label,
                )
                MonthYearWheelPicker(
                    year = year,
                    month = month,
                    onYearSelected = onYearSelected,
                    onMonthSelected = onMonthSelected,
                )
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    Box(
                        Modifier.weight(1f)
                            .height(48.dp)
                            .clip(PickerAccessoryPillShape)
                            .background(colors.accent)
                            .clickable(enabled = p > 0.98f, onClick = onConfirm),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text("确定", color = Color.White, style = Ios27Tokens.Type.Title3)
                    }
                    Box(
                        Modifier.weight(1f)
                            .height(48.dp)
                            .clip(PickerAccessoryPillShape)
                            .background(if (dark) Ios27Tokens.PressedDark else Ios27Tokens.PressedLight)
                            .clickable(enabled = p > 0.98f, onClick = onCancel),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text("取消", color = colors.label, style = Ios27Tokens.Type.Title3)
                    }
                }
            }
        }
    }
}

@Composable
private fun BoxScope.QweaTimePopup(
    visible: Boolean,
    hour: Int,
    minute: Int,
    colors: dev.liquidglass.ios27.Ios27PageColors,
    dark: Boolean,
    background: Backdrop,
    originBounds: Rect,
    hostWidthPx: Float,
    onHourSelected: (Int) -> Unit,
    onMinuteSelected: (Int) -> Unit,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    onHidden: () -> Unit,
) {
    val density = LocalDensity.current
    val progress = remember { Animatable(0f) }

    LaunchedEffect(visible) {
        progress.animateTo(
            targetValue = if (visible) 1f else 0f,
            animationSpec = tween(durationMillis = 340, easing = FastOutSlowInEasing),
        )
        if (!visible) onHidden()
    }

    val p = progress.value.coerceIn(0f, 1f)
    fun lerpBy(start: Float, end: Float, amount: Float): Float =
        start + (end - start) * amount

    val panelWidthPx = with(density) { 302.dp.toPx() }
    val panelHeightPx = with(density) { 300.dp.toPx() }
    val finalLeftPx = ((hostWidthPx - panelWidthPx) / 2f).coerceAtLeast(0f)
    val finalTopPx = with(density) { 40.dp.toPx() }

    val animatedLeftPx = lerpBy(originBounds.left, finalLeftPx, p)
    val animatedTopPx = lerpBy(originBounds.top, finalTopPx, p)
    val animatedWidthPx = lerpBy(originBounds.width, panelWidthPx, p)
    val animatedHeightPx = lerpBy(originBounds.height, panelHeightPx, p)
    val animatedRightPx = animatedLeftPx + animatedWidthPx
    val animatedBottomPx = animatedTopPx + animatedHeightPx

    val animatedLeft = with(density) { animatedLeftPx.toDp() }
    val animatedTop = with(density) { animatedTopPx.toDp() }
    val animatedWidth = with(density) { animatedWidthPx.toDp() }
    val animatedHeight = with(density) { animatedHeightPx.toDp() }
    val startCorner = with(density) { (originBounds.height / 2f).toDp() }
    val animatedCorner = startCorner + (34.dp - startCorner) * p

    val originRect = RectF(
        originBounds.left,
        originBounds.top,
        originBounds.right,
        originBounds.bottom,
    )
    val animatedRect = RectF(
        animatedLeftPx,
        animatedTopPx,
        animatedRightPx,
        animatedBottomPx,
    )
    val primaryMerge = ((p - 0.72f) / 0.28f).coerceIn(0f, 1f)
    val primaryRect = RectF(
        lerpBy(originRect.left, animatedRect.left, primaryMerge),
        lerpBy(originRect.top, animatedRect.top, primaryMerge),
        lerpBy(originRect.right, animatedRect.right, primaryMerge),
        lerpBy(originRect.bottom, animatedRect.bottom, primaryMerge),
    )
    val startCornerPx = originBounds.height / 2f
    val animatedCornerPx = with(density) { animatedCorner.toPx() }
    val bridgeStrength = (4f * p * (1f - p)).coerceIn(0f, 1f)
    val smoothingPx = with(density) { 16.dp.toPx() } * bridgeStrength

    val popupSurface = if (dark) Color(0xE62C2C2E) else Color.White.copy(alpha = 0.74f)
    val animatedShape = RoundedCornerShape(animatedCorner)
    val atPressedMaskEndpoint = p <= 0.10f

    Box(Modifier.fillMaxSize()) {
        if (!atPressedMaskEndpoint) {
            AndroidView(
                factory = { context ->
                    LiquidGlassView(context).apply {
                        enableDynamicBackground = false
                        useShaderPipeline = true
                        material = GlassMaterial.CLEAR
                        enableAdaptiveTint = false
                        enableSensorHighlight = true
                        enableBackdropBlur = false
                        enableChromaticDispersion = true
                        bevelWidth = with(density) { 18.dp.toPx() }
                        refractionHeight = with(density) { 8.dp.toPx() }
                        refractionFalloff = 2f
                        dispersionStrength = 0.08f
                        glassTint = if (dark) {
                            android.graphics.Color.argb(138, 44, 44, 46)
                        } else {
                            android.graphics.Color.argb(150, 248, 248, 250)
                        }
                    }
                },
                update = { glass ->
                    glass.setPrimaryShape(primaryRect, startCornerPx)
                    glass.setSecondaryShape(animatedRect, animatedCornerPx, smoothingPx)
                    glass.invalidate()
                },
                modifier = Modifier.fillMaxSize(),
            )
        }

        Box(
            Modifier
                .offset(x = animatedLeft, y = animatedTop)
                .width(animatedWidth)
                .height(animatedHeight)
                .then(
                    when {
                        atPressedMaskEndpoint -> Modifier.background(colors.pressed, animatedShape)
                        Build.VERSION.SDK_INT >= 33 -> Modifier.drawBackdrop(
                            backdrop = background,
                            shape = { RoundedRectangle(animatedCorner) },
                            effects = {
                                colorControls(brightness = if (dark) 0f else 0.2f, saturation = 1.5f)
                                blur(if (dark) 8.dp.toPx() else 16.dp.toPx())
                                lens(24.dp.toPx(), 48.dp.toPx(), depthEffect = true)
                            },
                            highlight = { Highlight.Plain },
                            shadow = {
                                Shadow(
                                    radius = 48.dp,
                                    offset = DpOffset(0.dp, 8.dp),
                                    color = Color.Black.copy(alpha = 0.22f),
                                )
                            },
                            onDrawSurface = { drawRect(popupSurface) },
                        )
                        else -> Modifier.background(popupSurface, animatedShape)
                    },
                )
                .clip(animatedShape).ios27AndroidPane("选择时间", onCancel),
            contentAlignment = Alignment.TopCenter,
        ) {
            if (p > 0.40f) Column(
                Modifier.requiredSize(302.dp, 300.dp)
                    .padding(start = 16.dp, top = 20.dp, end = 16.dp, bottom = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "选择时间",
                    style = Ios27Tokens.Type.Title2.copy(fontWeight = FontWeight.SemiBold),
                    color = colors.label,
                )
                TimeWheelPicker(
                    hour = hour,
                    minute = minute,
                    onHourSelected = onHourSelected,
                    onMinuteSelected = onMinuteSelected,
                )
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    Box(
                        Modifier.weight(1f)
                            .height(48.dp)
                            .clip(PickerAccessoryPillShape)
                            .background(colors.accent)
                            .clickable(enabled = p > 0.98f, onClick = onConfirm),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text("确定", color = Color.White, style = Ios27Tokens.Type.Title3)
                    }
                    Box(
                        Modifier.weight(1f)
                            .height(48.dp)
                            .clip(PickerAccessoryPillShape)
                            .background(if (dark) Ios27Tokens.PressedDark else Ios27Tokens.PressedLight)
                            .clickable(enabled = p > 0.98f, onClick = onCancel),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text("取消", color = colors.label, style = Ios27Tokens.Type.Title3)
                    }
                }
            }
        }
    }
}

@Composable
internal fun Ios27PickerExample(
    file: String,
    variant: String,
    background: Backdrop,
    year: Int,
    month: Int,
    selectedDay: Int,
    hour: Int,
    minute: Int,
    timeLabel: String,
    onSelectedDay: (Int) -> Unit,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onYearMonthChange: (Int, Int) -> Unit,
    onTimeChange: (Int, Int) -> Unit,
) {
    val compact = variant.contains("Compact")
    val colors = LocalIos27PageColors.current
    val dark = colors.page == Ios27Tokens.PageDark
    var hostPositionInRoot by remember { mutableStateOf(Offset.Zero) }
    var hostWidthPx by remember { mutableStateOf(0f) }
    var headerBoundsInRoot by remember { mutableStateOf<Rect?>(null) }
    var animationOriginBounds by remember { mutableStateOf<Rect?>(null) }
    var monthYearSourceBoundsInRoot by remember { mutableStateOf<Rect?>(null) }
    var timeAnimationOriginBounds by remember { mutableStateOf<Rect?>(null) }
    var timeSourceBoundsInRoot by remember { mutableStateOf<Rect?>(null) }
    var monthYearPickerOpen by rememberSaveable(file, variant) { mutableStateOf(false) }
    var monthYearPickerMounted by rememberSaveable(file, variant) { mutableStateOf(false) }
    var pendingYear by rememberSaveable(file, variant) { mutableStateOf(year) }
    var pendingMonth by rememberSaveable(file, variant) { mutableStateOf(month) }
    var timePickerOpen by rememberSaveable(file, variant) { mutableStateOf(false) }
    var timePickerMounted by rememberSaveable(file, variant) { mutableStateOf(false) }
    var pendingHour by rememberSaveable(file, variant) { mutableStateOf(hour) }
    var pendingMinute by rememberSaveable(file, variant) { mutableStateOf(minute) }
    val openMonthYearPicker: (Rect?) -> Unit = { sourceBoundsInRoot ->
        val sourceBounds = sourceBoundsInRoot ?: headerBoundsInRoot
        if (sourceBounds != null && hostWidthPx > 0f) {
            monthYearSourceBoundsInRoot = sourceBounds
            animationOriginBounds = Rect(
                left = sourceBounds.left - hostPositionInRoot.x,
                top = sourceBounds.top - hostPositionInRoot.y,
                right = sourceBounds.right - hostPositionInRoot.x,
                bottom = sourceBounds.bottom - hostPositionInRoot.y,
            )
            pendingYear = year
            pendingMonth = month
            monthYearPickerMounted = true
            monthYearPickerOpen = true
        }
    }
    val closeMonthYearPicker = { monthYearPickerOpen = false }
    val toggleMonthYearPicker = {
        if (monthYearPickerOpen) closeMonthYearPicker() else openMonthYearPicker(headerBoundsInRoot)
    }
    val openTimePicker: (Rect) -> Unit = { boundsInRoot ->
        if (hostWidthPx > 0f) {
            timeSourceBoundsInRoot = boundsInRoot
            timeAnimationOriginBounds = Rect(
                left = boundsInRoot.left - hostPositionInRoot.x,
                top = boundsInRoot.top - hostPositionInRoot.y,
                right = boundsInRoot.right - hostPositionInRoot.x,
                bottom = boundsInRoot.bottom - hostPositionInRoot.y,
            )
            pendingHour = hour
            pendingMinute = minute
            timePickerMounted = true
            timePickerOpen = true
        }
    }
    val closeTimePicker = { timePickerOpen = false }

    Box(
        Modifier.fillMaxWidth()
            .onGloballyPositioned { coordinates ->
                hostPositionInRoot = coordinates.positionInRoot()
                hostWidthPx = coordinates.size.width.toFloat()
            },
    ) {
        Column(Modifier.fillMaxWidth()) {
            PickerCalendar(
                compact = compact,
                background = background,
                year = year,
                month = month,
                selectedDay = selectedDay,
                timeLabel = timeLabel,
                onSelectedDay = onSelectedDay,
                onHeaderClick = toggleMonthYearPicker,
                onHeaderBoundsChanged = { headerBoundsInRoot = it },
                onPreviousMonth = onPreviousMonth,
                onNextMonth = onNextMonth,
                onTimeClick = openTimePicker,
                hiddenMonthYearSourceBounds = monthYearSourceBoundsInRoot.takeIf { monthYearPickerMounted },
                hiddenTimeSourceBounds = timeSourceBoundsInRoot.takeIf { timePickerMounted },
            )
            if (compact) {
                Spacer(Modifier.height(Ios27Tokens.GroupGap.dp))
                CompactAccessoryRows(
                    timeLabel = timeLabel,
                    dateLabel = pickerDateLabel(year, month, selectedDay),
                    onTimeClick = openTimePicker,
                    onDateClick = { bounds -> openMonthYearPicker(bounds) },
                    hiddenMonthYearSourceBounds = monthYearSourceBoundsInRoot.takeIf { monthYearPickerMounted },
                    hiddenTimeSourceBounds = timeSourceBoundsInRoot.takeIf { timePickerMounted },
                )
            }
        }

        if (monthYearPickerMounted) {
            animationOriginBounds?.let { originBounds ->
                QweaMonthYearPopup(
                    visible = monthYearPickerOpen,
                    year = pendingYear,
                    month = pendingMonth,
                    colors = colors,
                    dark = dark,
                    background = background,
                    originBounds = originBounds,
                    hostWidthPx = hostWidthPx,
                    onYearSelected = { pendingYear = it },
                    onMonthSelected = { pendingMonth = it },
                    onConfirm = {
                        onYearMonthChange(pendingYear, pendingMonth)
                        closeMonthYearPicker()
                    },
                    onCancel = closeMonthYearPicker,
                    onHidden = {
                        monthYearPickerMounted = false
                        monthYearSourceBoundsInRoot = null
                        animationOriginBounds = null
                    },
                )
            }
        }

        if (timePickerMounted) {
            timeAnimationOriginBounds?.let { originBounds ->
                QweaTimePopup(
                    visible = timePickerOpen,
                    hour = pendingHour,
                    minute = pendingMinute,
                    colors = colors,
                    dark = dark,
                    background = background,
                    originBounds = originBounds,
                    hostWidthPx = hostWidthPx,
                    onHourSelected = { pendingHour = it },
                    onMinuteSelected = { pendingMinute = it },
                    onConfirm = {
                        onTimeChange(pendingHour, pendingMinute)
                        closeTimePicker()
                    },
                    onCancel = closeTimePicker,
                    onHidden = {
                        timePickerMounted = false
                        timeSourceBoundsInRoot = null
                        timeAnimationOriginBounds = null
                    },
                )
            }
        }
    }
}
