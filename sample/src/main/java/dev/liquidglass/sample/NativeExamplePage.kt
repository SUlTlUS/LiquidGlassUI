package dev.liquidglass.sample

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.type
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.zeaze.tianyinwallpaper.backdrop.backdrops.*
import com.zeaze.tianyinwallpaper.catalog.components.*
import com.zeaze.tianyinwallpaper.ui.commom.ProgressiveBlurContent
import dev.liquidglass.ios27.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect
import java.util.Calendar
import java.util.Locale
import top.yukonga.miuix.kmp.nav.gesture.PredictiveBackHandler as MiuixPredictiveBackHandler

/** Live framework pages. FigmaSceneView is intentionally only used by the separate reference route. */
@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun NativeExamplePage(entry: IphoneExample, onBack: () -> Unit, onReference: () -> Unit) {
    val colors = LocalIos27PageColors.current
    val dark = !MaterialTheme.colors.isLight
    val context = LocalContext.current
    val clipboard = LocalClipboardManager.current
    val scroll = rememberScrollState()
    val scope = rememberCoroutineScope()
    val haptic = LocalHapticFeedback.current
    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden, skipHalfExpanded = true)
    var sheetsOpen by rememberSaveable(entry.key) { mutableStateOf(entry.category == "Sheets (Modals)") }
    var panel by rememberSaveable { mutableStateOf("操作") }
    var dialog by rememberSaveable { mutableStateOf<String?>(null) }
    var alertOpen by rememberSaveable(entry.key) { mutableStateOf(false) }
    var actionSheetOpen by rememberSaveable(entry.key) { mutableStateOf(false) }
    var actionSheetMounted by rememberSaveable(entry.key) { mutableStateOf(false) }
    var activityViewOpen by rememberSaveable(entry.key) { mutableStateOf(entry.category == "Activity View") }
    var alertFirstValue by rememberSaveable(entry.key) { mutableStateOf("Value") }
    var alertSecondValue by rememberSaveable(entry.key) { mutableStateOf("Value") }
    var status by rememberSaveable { mutableStateOf("") }
    val popupBackground = rememberPopupBackground { status = it }
    val supportsPopupBackground = entry.category in setOf("Color Picker", "Picker", "Toolbars - Bottom", "Sheets (Modals)", "Action Sheet", "Activity View")
    var text by rememberSaveable(entry.key) {
        mutableStateOf(if (entry.category == "Menus") "Hello World" else "")
    }
    var note by rememberSaveable { mutableStateOf("") }
    var items by rememberSaveable { mutableStateOf(listOf<String>()) }
    var enabled by rememberSaveable { mutableStateOf(true) }
    var secondary by rememberSaveable { mutableStateOf(false) }
    var value by rememberSaveable { mutableFloatStateOf(0.5f) }
    var count by rememberSaveable { mutableIntStateOf(0) }
    var tab by rememberSaveable { mutableIntStateOf(0) }
    var searchOpen by rememberSaveable(entry.key) { mutableStateOf(false) }
    var menu by rememberSaveable(entry.key) { mutableStateOf(false) }
    var contextMenuMounted by remember(entry.key) { mutableStateOf(false) }
    var contextAreaPressed by remember(entry.key) { mutableStateOf(false) }
    var expanded by rememberSaveable { mutableStateOf(entry.variant == "Expanded=True") }
    var dismissed by rememberSaveable { mutableStateOf(false) }
    var auth by rememberSaveable { mutableStateOf(if (entry.variant == "State=Success") "演示成功" else "尚未开始") }
    val initialDateTime = remember(entry.key) { Calendar.getInstance() }
    var year by rememberSaveable(entry.key) { mutableIntStateOf(initialDateTime.get(Calendar.YEAR)) }
    var month by rememberSaveable(entry.key) { mutableIntStateOf(initialDateTime.get(Calendar.MONTH)) }
    var day by rememberSaveable(entry.key) { mutableIntStateOf(initialDateTime.get(Calendar.DAY_OF_MONTH)) }
    var hour by rememberSaveable(entry.key) { mutableIntStateOf(initialDateTime.get(Calendar.HOUR_OF_DAY)) }
    var minute by rememberSaveable(entry.key) { mutableIntStateOf(initialDateTime.get(Calendar.MINUTE)) }
    var colorPickerOpen by rememberSaveable(entry.key) { mutableStateOf(entry.category == "Color Picker") }
    var colorPickerGridIndex by rememberSaveable(entry.key) { mutableIntStateOf(6 * 12 + 3) }
    var colorPickerSwatchIndex by rememberSaveable(entry.key) { mutableIntStateOf(6) }
    var colorPickerOpacity by rememberSaveable(entry.key) { mutableFloatStateOf(1f) }
    val showPanel: (String) -> Unit = { title ->
        panel = title
        if (entry.category == "Sheets (Modals)") sheetsOpen = true else scope.launch { sheetState.show() }
    }
    val closePanel = {
        if (entry.category == "Sheets (Modals)") sheetsOpen = false else scope.launch { sheetState.hide() }
        Unit
    }
    val modalBackAction: (() -> Unit)? = when {
        sheetState.isVisible || sheetsOpen -> closePanel
        entry.category == "Action Sheet" && actionSheetOpen -> ({ actionSheetOpen = false })
        entry.category == "Activity View" && activityViewOpen -> ({ activityViewOpen = false })
        entry.category == "Color Picker" && colorPickerOpen -> ({ colorPickerOpen = false })
        entry.category == "Alert" && alertOpen -> ({ alertOpen = false })
        menu && entry.category in setOf("Menus", "Context Menu") -> ({ menu = false })
        else -> null
    }
    MiuixPredictiveBackHandler(
        enabled = modalBackAction != null,
        onProgress = { events -> events.collect { } },
        onCommit = { modalBackAction?.invoke() },
        onCancel = {},
    )
    val pickDate = {
        DatePickerDialog(context, { _, y, m, d ->
            year = y
            month = m
            day = d
        }, year, month, day).show()
        Unit
    }
    val pickTime = {
        TimePickerDialog(context, { _, h, m ->
            hour = h
            minute = m
        }, hour, minute, false).show()
        Unit
    }
    val shiftPickerMonth: (Int) -> Unit = { delta ->
        val target = Calendar.getInstance().apply {
            clear()
            set(year, month, 1)
            add(Calendar.MONTH, delta)
        }
        year = target.get(Calendar.YEAR)
        month = target.get(Calendar.MONTH)
        day = day.coerceAtMost(target.getActualMaximum(Calendar.DAY_OF_MONTH))
    }
    val pickerTimeLabel = String.format(
        Locale.US,
        "%d:%02d %s",
        (hour % 12).let { if (it == 0) 12 else it },
        minute,
        if (hour < 12) "AM" else "PM",
    )
    val hasTabs = entry.category == "Tab Bar"
    val hasSeparateSearch = hasTabs && entry.variant == "Split Search=True"
    val hasBottomToolbar = entry.category == "Toolbars - Bottom"
    val pageScrollModifier =
        if (entry.category in setOf("Empty States", "Menus")) Modifier else Modifier.verticalScroll(scroll)
    val emptyContentPage = rememberSceneContentEmpty(entry.file)
    val pageSurface = if (dark) {
        // Dark iOS pages use a true-black root canvas. Group surfaces (#1C1C1E)
        // belong to list cells/cards only; they must not tint an otherwise empty page.
        colors.page
    } else {
        when {
            entry.category in setOf("Menus", "Context Menu") -> colors.group
            entry.category == "Alert" -> colors.page
            entry.category == "Empty States" -> colors.group
            entry.category == "Action Sheet" -> colors.group
            entry.key == "12740-24224" || entry.key == "12740-24239" -> colors.group
            emptyContentPage -> colors.group
            else -> colors.page
        }
    }
    val backdrop = rememberExamplePageBackdrop(pageSurface)
    val pickerBackgroundBackdrop = rememberLayerBackdrop()
    val compact = entry.category in setOf("Slider", "Toggles (Switches)", "Steppers", "Text Fields", "Picker")
    val sectionedList = entry.key == "12740-24239"
    val fullWidthList = entry.key == "12740-24224"
    val fullBleedList = fullWidthList || sectionedList || entry.category == "Menus"
    var fullWidthSelectedAction by rememberSaveable(entry.key) { mutableIntStateOf(1) }
    val realDensity = LocalDensity.current
    val topInsetPx = with(realDensity) { WindowInsets.safeDrawing.asPaddingValues().calculateTopPadding().toPx() }
    val reveal = with(LocalDensity.current) { 30.dp.toPx() }
    LaunchedEffect(auth) {
        if (auth == "演示识别中") { delay(1200); auth = "演示成功" }
    }
    LaunchedEffect(entry.key) {
        android.util.Log.i("IphoneExamples", "NATIVE_READY ${entry.key}")
        if (entry.category == "Sheets (Modals)") sheetsOpen = true
    }
    // System scenes are full-bleed layouts without a list scaffold; geometry comes from the scene JSON.
    if (entry.category == "Control Center") {
        ExampleFullBleedDesignPage(background = colors.page) {
            Box(Modifier.fillMaxSize().semantics { contentDescription = "可操作示例 ${entry.key}" }) {
                ControlCenterScene(
                    entry,
                    if (Build.VERSION.SDK_INT >= 33) backdrop else null,
                    onReference,
                    enabled,
                    secondary,
                    value,
                    onEnabledChange = { enabled = it },
                    onSecondaryChange = { secondary = it },
                    onValueChange = { value = it },
                )
            }
        }
        return
    }

    // Home-screen scenes use the same full-bleed 402pt shell as Control Center.
    if (entry.category in setOf("Home Screen", "Home Screen Widgets", "Home Screen Quick Actions")) {
        ExampleFullBleedDesignPage(background = colors.page) {
            Box(Modifier.fillMaxSize().semantics { contentDescription = "可操作示例 ${entry.key}" }) {
                HomeScreenScene(entry, if (Build.VERSION.SDK_INT >= 33) backdrop else null, onReference)
            }
        }
        return
    }

    if (entry.category == "Menus") {
        ExampleDesignPage(
            background = pageSurface,
            fontScale = 1f,
        ) { topInset ->
            Ios27TextContextMenuHost(
                backdrop = if (Build.VERSION.SDK_INT >= 33) backdrop else null,
                colors = colors,
                dark = dark,
                modifier = Modifier.fillMaxSize(),
            ) {
                Box(Modifier.fillMaxSize()) {
                    ExampleBackdropLayer(
                        backdrop = backdrop,
                        background = pageSurface,
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        Ios27MenuEditablePage(
                            value = text,
                            onValueChange = { text = it },
                            modifier = Modifier.fillMaxSize()
                                .padding(top = topInset + 54.dp, bottom = 24.dp),
                        )
                    }

                    ExampleTopChrome(
                        backdrop = if (Build.VERSION.SDK_INT >= 33) backdrop else null,
                        isLightTheme = !dark,
                        toolbarFile = "12740-23997.json",
                        onBack = onBack,
                        onReference = onReference,
                        onTrailing = { status = "工具栏操作（演示）" },
                    )
                }
            }
        }
        return
    }

    if (entry.category == "Context Menu") {
        val contextAreaScale by androidx.compose.animation.core.animateFloatAsState(
            targetValue = if (contextAreaPressed) 0.97f else 1f,
            animationSpec = androidx.compose.animation.core.tween(
                durationMillis = if (contextAreaPressed) 110 else 180,
                easing = androidx.compose.animation.core.FastOutSlowInEasing,
            ),
            label = "contextAreaPress",
        )
        ExampleDesignPage(
            background = pageSurface,
            fontScale = 1f,
            modifier = Modifier.semantics { contentDescription = "可操作示例 ${entry.key}" },
        ) { topInset ->
            Box(Modifier.fillMaxSize()) {
                ExampleBackdropLayer(
                    backdrop = backdrop,
                    background = pageSurface,
                    modifier = Modifier.fillMaxSize(),
                ) {
                    val contentShape = RoundedCornerShape(30.dp)
                    Box(
                        Modifier
                            .offset(x = 16.dp, y = topInset + 9.5.dp)
                            .size(370.dp, 160.dp)
                            .shadow(
                                elevation = 32.dp,
                                shape = contentShape,
                                clip = false,
                                ambientColor = Color.Black.copy(alpha = 0.20f),
                                spotColor = Color.Black.copy(alpha = 0.20f),
                            )
                            .graphicsLayer {
                                scaleX = contextAreaScale
                                scaleY = contextAreaScale
                                transformOrigin = TransformOrigin.Center
                            }
                            .clip(contentShape)
                            .background(colors.group)
                            .onKeyEvent { event ->
                                if (event.key == Key.Enter && event.type == KeyEventType.KeyDown) {
                                    contextMenuMounted = true
                                    menu = true
                                    true
                                } else false
                            }
                            .focusable()
                            .semantics {
                                contentDescription = "Content Area"
                                onLongClick(label = "打开上下文菜单") {
                                    contextMenuMounted = true
                                    menu = true
                                    true
                                }
                            }
                            .pointerInput(entry.key, menu) {
                                detectTapGestures(
                                    onPress = {
                                        contextAreaPressed = true
                                        tryAwaitRelease()
                                        contextAreaPressed = false
                                    },
                                    onLongPress = {
                                        if (!menu) {
                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                            contextMenuMounted = true
                                            menu = true
                                        }
                                    },
                                )
                            },
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            "Content Area",
                            style = Ios27Tokens.Type.Body,
                            color = if (dark) colors.secondaryLabel
                            else Color.Black.copy(alpha = 0.60f),
                        )
                    }
                }

                if (contextMenuMounted) {
                    Ios27MenuExample(
                        entry = entry,
                        backdrop = if (Build.VERSION.SDK_INT >= 33) backdrop else null,
                        colors = colors,
                        dark = dark,
                        contextMenuTop = topInset + 185.5.dp,
                        visible = menu,
                        onDismiss = { menu = false },
                        onHidden = { contextMenuMounted = false },
                        onAction = { action ->
                            status = "已执行：$action"
                            menu = false
                        },
                    )
                }
            }
        }
        return
    }

    Ios27TextContextMenuHost(
        backdrop = if (Build.VERSION.SDK_INT >= 33) backdrop else null,
        colors = colors,
        dark = dark,
        modifier = Modifier.fillMaxSize(),
    ) {
    ModalBottomSheetLayout(modifier = Modifier.imePadding(), sheetState = sheetState, sheetBackgroundColor = colors.group,
        sheetContent = {
            Column(Modifier.fillMaxWidth().navigationBarsPadding().padding(16.dp)) {
                Text(panel, style = Ios27Tokens.Type.Title2, color = colors.label)
                Spacer(Modifier.height(16.dp))
                if (entry.category == "Sheets (Modals)") {
                    Ios27EditableTextField(
                        value = note,
                        onValueChange = { note = it },
                        description = "编辑内容输入框",
                        placeholder = "编辑内容",
                        backdrop = if (Build.VERSION.SDK_INT >= 33) backdrop else null,
                    )
                    Ios27ActionButton("保存", { text = note; status = "内容已保存"; closePanel() })
                } else {
                    Ios27ListRows(listOf("收藏", "复制内容", "移除")) { action ->
                        Ios27ListRow(action, onClick = {
                            when (action) {
                                "收藏" -> { enabled = true; status = "已收藏" }
                                "复制内容" -> { clipboard.setText(AnnotatedString(text.ifBlank { panel })); status = "已复制到剪贴板" }
                                else -> { items = emptyList(); dismissed = true; status = "已移除" }
                            }
                            closePanel()
                        })
                    }
                }
                TextButton(closePanel) { Text("取消") }
            }
        }) {
        ExampleDesignPage(
            background = pageSurface,
            fontScale = 1f,
            modifier = Modifier.semantics { contentDescription = "可操作示例 ${entry.key}" },
        ) { topInset ->
            if (entry.category == "Picker") {
                // Background-only sibling: no calendar or glass reader is recorded here.
                Box(Modifier.fillMaxSize().layerBackdrop(pickerBackgroundBackdrop)
                    .background(pageSurface).popupBackground(popupBackground.image))
            }
            Ios27PageContent(Modifier.fillMaxSize().layerBackdrop(backdrop)
                .ios27AndroidBackgroundBlocked(modalBackAction != null)
                .popupBackground(if (supportsPopupBackground && entry.category != "Picker") popupBackground.image else null)
                .then(pageScrollModifier)
                .padding(top = topInset + when {
                    entry.category == "Action Sheet" -> 44.dp
                    compact -> 64.dp
                    else -> 54.dp
                },
                    bottom = when {
                        hasTabs -> 170.dp
                        hasBottomToolbar -> 100.dp
                        else -> 88.dp
                    }),
                horizontalContentPadding = if (fullBleedList) 0.dp else Ios27Tokens.ContentMargin.dp,
            ) {
                val toolbar = rememberSceneToolbar(entry.file)
                val hideEmptyStateLargeSymbolToolbar = entry.category == "Empty States" &&
                    entry.variant == "Config=Large Symbol"
                if (entry.category != "Sheets (Modals)" &&
                    !compact && !sectionedList && !hasBottomToolbar && !hideEmptyStateLargeSymbolToolbar) {
                    if (toolbar?.largeTitle != null) {
                        if (fullWidthList) {
                            // 12740:24224 source geometry: title block y=116..177, then
                            // Content Area contributes 16pt before the list starts at y=193.
                            Box(Modifier.fillMaxWidth().height(67.dp)
                                .padding(horizontal = Ios27Tokens.ContentMargin.dp)) {
                                SceneGlyphText(
                                    toolbar.largeTitle,
                                    if (dark) colors.label else toolbar.largeTitle.color ?: colors.label,
                                )
                                toolbar.largeSubtitle?.let { subtitle ->
                                    Box(Modifier.offset(y = 41.dp)) {
                                        SceneGlyphText(
                                            subtitle,
                                            if (dark) colors.secondaryLabel else subtitle.color ?: colors.secondaryLabel,
                                        )
                                    }
                                }
                            }
                        } else {
                            // Original large-title block: glyph outlines at the reference position.
                            Column(Modifier.fillMaxWidth()
                                .padding(horizontal = if (fullBleedList) Ios27Tokens.ContentMargin.dp else 0.dp)
                                .padding(top = 10.dp,
                                bottom = if (entry.category == "Action Sheet") 0.dp else 6.dp)) {
                                SceneGlyphText(toolbar.largeTitle, if (dark) colors.label else toolbar.largeTitle.color ?: colors.label)
                                toolbar.largeSubtitle?.let { SceneGlyphText(it, if (dark) colors.label else it.color ?: colors.secondaryLabel) }
                            }
                        }
                    } else if (entry.category !in setOf("Toolbars - Top", "Color Picker")) {
                        Ios27PageHeader(categoryTitle(entry.category), entry.variant.substringAfter('='))
                    }
                }
                if (sectionedList) {
                    SectionedListExample(entry.file, Modifier.fillMaxWidth(), onSelect = { dialog = it })
                } else when (entry.category) {
                    "Slider" -> repeat(2) { group ->
                        Ios27ListSection {
                            Ios27ListRows((0..2).toList()) { index ->
                                var position by rememberSaveable { mutableFloatStateOf(index / 2f) }
                                val sliderLabel = "滑块 ${group * 3 + index + 1}"
                                // Group 1 rows keep the original min/max SF Symbol zones from the scene.
                                Ios27SliderRow(sliderLabel, position, { position = it }, showLabel = false,
                                    leading = if (group == 0) ({ SceneGlyphIcon(entry.file, "Symbol - Min", colors.secondaryLabel, Modifier.size(32.dp, 50.dp)) }) else null,
                                    trailing = if (group == 0) ({ SceneGlyphIcon(entry.file, "Symbol - Max", colors.secondaryLabel, Modifier.size(32.dp, 50.dp)) }) else null,
                                    ticks = group == 0,
                                    onValueChangeFinished = { status = "$sliderLabel 已设为 ${(position * 100).toInt()}%" })
                            }
                        }
                    }
                    "Toggles (Switches)" -> Ios27ListSection {
                        Ios27ListRows(listOf(0, 1)) { index ->
                            if (index == 0) Ios27SwitchRow("示例开关一", enabled, { enabled = it })
                            else Ios27SwitchRow("示例开关二", secondary, { secondary = it })
                        }
                    }
                    "Steppers" -> Ios27ListSection {
                        Ios27StepperExample(entry.file, count, { count = it })
                    }
                    "Text Fields" -> Ios27TextFieldsExample(
                        entry.key,
                        backdrop = if (Build.VERSION.SDK_INT >= 33) backdrop else null,
                    )
                    "Keyboard" -> {
                        val keyboard = when {
                            entry.variant.contains("Email") -> KeyboardType.Email
                            entry.variant.contains("URL") || entry.variant.contains("Web Search") -> KeyboardType.Uri
                            entry.variant.contains("Number") || entry.variant.contains("Numeric") -> KeyboardType.Number
                            else -> KeyboardType.Text
                        }
                        Ios27ListSection {
                            Ios27EditableTextField(
                                value = text,
                                onValueChange = { text = it },
                                description = "输入内容输入框",
                                placeholder = "输入内容",
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = keyboard,
                                    capitalization = if (entry.variant.contains("Uppercase"))
                                        KeyboardCapitalization.Characters else KeyboardCapitalization.None,
                                ),
                                backdrop = if (Build.VERSION.SDK_INT >= 33) backdrop else null,
                            )
                            if (entry.variant.contains("Replace"))
                                Ios27EditableTextField(
                                    value = note,
                                    onValueChange = { note = it },
                                    description = "替换内容输入框",
                                    placeholder = "替换内容",
                                    backdrop = if (Build.VERSION.SDK_INT >= 33) backdrop else null,
                                )
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Ios27ActionButton("提交", { status = "已提交：$text" })
                            Ios27ActionButton("清空", { text = ""; status = "已清空" })
                            if (entry.variant.contains("Replace")) Ios27ActionButton("替换", { text = note })
                        }
                        Text("点击输入框使用手机键盘；不安装或切换系统输入法。", color = colors.secondaryLabel)
                    }
                    "List" -> if (fullWidthList) {
                        FullWidthListExample(
                            selectedAction = fullWidthSelectedAction,
                            onRowClick = { dialog = it },
                            onActionSelected = {
                                fullWidthSelectedAction = it
                                status = "已选择 Action ${it + 1}"
                            },
                        )
                    } else {
                        Ios27ListSection {
                            Ios27ListRows((0..9).toList()) { row -> when (row) {
                                0 -> Ios27ListRow("Title", detail = "Detail", onClick = { dialog = "详情" })
                                1 -> Ios27ListRow("Title", subtitle = "Subtitle", onClick = { dialog = "编辑标题" })
                                2 -> Ios27SwitchRow("收藏", enabled, { enabled = it })
                                3 -> Ios27ListRow("日期", detail = "$year-${month + 1}-$day", onClick = pickDate)
                                4 -> Ios27ListRow("时间", detail = "%02d:%02d".format(hour, minute), onClick = pickTime)
                                5 -> Ios27StepperRow("数量", count, { count = it })
                                6 -> Ios27ListRow("Pop-up", detail = text.ifBlank { "选择" }, onClick = { showPanel("列表操作") })
                                7 -> Ios27ListRow("Button", onClick = { count++; status = "已点击 $count 次" })
                                8 -> Ios27EditableTextField(
                                    value = text,
                                    onValueChange = { text = it },
                                    description = "Title 输入框",
                                    placeholder = "Title",
                                    backdrop = if (Build.VERSION.SDK_INT >= 33) backdrop else null,
                                )
                                else -> Ios27ListRow("Action", onClick = { dialog = "确认操作" })
                            } }
                        }
                    }
                    "Picker" -> Ios27PickerExample(
                        file = entry.file,
                        variant = entry.variant,
                        background = pickerBackgroundBackdrop,
                        year = year,
                        month = month,
                        selectedDay = day,
                        hour = hour,
                        minute = minute,
                        timeLabel = pickerTimeLabel,
                        onSelectedDay = { day = it },
                        onPreviousMonth = { shiftPickerMonth(-1) },
                        onNextMonth = { shiftPickerMonth(1) },
                        onYearMonthChange = { nextYear, nextMonth ->
                            val target = Calendar.getInstance().apply {
                                clear()
                                set(nextYear, nextMonth, 1)
                            }
                            year = nextYear
                            month = nextMonth
                            day = day.coerceAtMost(target.getActualMaximum(Calendar.DAY_OF_MONTH))
                        },
                        onTimeChange = { nextHour, nextMinute ->
                            hour = nextHour
                            minute = nextMinute
                        },
                    )
                    "Menus" -> {
                        Ios27MenuEditablePage(
                            value = text,
                            onValueChange = { text = it },
                        )
                    }
                    "Alert" -> {
                        Ios27ListSection { Ios27ListRow("当前内容", detail = text.ifBlank { "尚未填写" }) }
                        Ios27ActionButton("显示提示弹窗", { alertOpen = true })
                    }
                    "Action Sheet" -> {
                        Text("Content area", style = Ios27Tokens.Type.Headline, color = colors.label,
                            modifier = Modifier.padding(vertical = 12.dp))
                    }
                    "Activity View" -> {
                        if (!activityViewOpen) {
                            Ios27ListSection { Ios27ListRow("内容", subtitle = text.ifBlank { "打开分享面板" }) }
                            Ios27ActionButton("打开分享面板", { activityViewOpen = true })
                        }
                    }
                    "Sheets (Modals)" -> Unit
                    "Color Picker" -> {
                        // Keep the real page content in the capture tree while the sheet is open.
                        // The sibling glass sheet reads this independent backdrop, so its surface
                        // changes with the content below instead of sampling a blank/static layer.
                        val color = colorPickerGridColor(colorPickerGridIndex)
                        Ios27ListSection {
                            Ios27ListRow("颜色", detail = "${(colorPickerOpacity * 100).toInt()}%",
                                trailing = { Box(Modifier.size(32.dp).clip(CircleShape).background(color)) })
                        }
                        Ios27ActionButton("打开颜色选择器", { colorPickerOpen = true })
                    }
                    "Empty States" -> {
                        if (items.isEmpty()) {
                            Ios27EmptyStateExample(
                                entry = entry,
                                colors = colors,
                                onCreate = { note = ""; dialog = "新建内容" },
                            )
                        } else {
                            Ios27ListSection { Ios27ListRows(items.withIndex().toList()) { item ->
                                Ios27ListRow(item.value, onClick = { items = items.filterIndexed { index, _ -> index != item.index } })
                            } }
                        }
                    }
                    "Notifications" -> if (dismissed) {
                        Text("通知已清除", color = colors.secondaryLabel)
                        Ios27ActionButton("恢复示例通知", { dismissed = false })
                    } else Ios27ListSection {
                        Ios27ListRow("Title", subtitle = if (expanded) "Notification Content：完整通知内容" else "Message",
                            onClick = { expanded = !expanded })
                        Row(Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Ios27ActionButton(if (expanded) "收起" else "展开", { expanded = !expanded })
                            Ios27ActionButton("清除通知", { dismissed = true })
                        }
                    }
                    "Face ID" -> {
                        Text(auth, style = Ios27Tokens.Type.Title2, color = colors.label)
                        if (auth == "演示识别中") CircularProgressIndicator()
                        Ios27ActionButton("开始演示", { auth = "演示识别中" })
                        Text("仅演示状态变化，不执行身份认证。", color = colors.secondaryLabel)
                    }
                    "Control Center" -> {
                        Ios27ListSection {
                            Ios27SwitchRow("无线连接（演示）", enabled, { enabled = it })
                            Ios27SwitchRow("静音（演示）", secondary, { secondary = it })
                            Ios27SliderRow("亮度（演示）", value, { value = it })
                        }
                        Text("仅修改示例状态，不改手机系统设置。", color = colors.secondaryLabel)
                    }
                    "Lock Screen" -> {
                        Text("9:41", style = Ios27Tokens.Type.LargeTitle, color = colors.label)
                        Ios27SwitchRow("手电筒（演示）", enabled, { enabled = it })
                        Ios27ActionButton("解锁示例", { dialog = "示例已解锁" })
                        Text("不控制真实锁屏、相机或手电筒。", color = colors.secondaryLabel)
                    }
                    "Toolbars - Bottom" -> {
                        // The original toolbar examples intentionally have an empty Content Area.
                        // Their controls are rendered as a separate bottom overlay below.
                        if (rememberSceneContentEmpty(entry.file)) return@Ios27PageContent
                    }
                    "Toolbars - Top", "Tab Bar" -> {
                        // Toolbar showcase pages have an empty Content Area in the originals.
                        if (rememberSceneContentEmpty(entry.file)) return@Ios27PageContent
                        val showSearchContent = if (hasTabs) hasSeparateSearch && searchOpen else entry.variant.contains("Search")
                        if (showSearchContent) Ios27ListSection {
                            Ios27EditableTextField(
                                value = text,
                                onValueChange = { text = it },
                                description = "搜索输入框",
                                placeholder = "搜索",
                                backdrop = if (Build.VERSION.SDK_INT >= 33) backdrop else null,
                            )
                        }
                        val contentTabLabel = if (hasTabs && hasSeparateSearch && searchOpen) "搜索"
                            else listOf("全部", "收藏", "搜索").getOrElse(tab) { "全部" }
                        Ios27ListSection {
                            Ios27ListRows((1..12).filter { text.isBlank() || "内容 $it".contains(text) }) { row ->
                                Ios27ListRow("$contentTabLabel · 内容 $row",
                                    onClick = { note = "内容 $row"; dialog = "编辑内容" })
                            }
                        }
                        Ios27ActionButton("添加内容", { note = ""; dialog = "新建内容" })
                        items.forEach { item -> Ios27ListRow(item, onClick = { note = item; dialog = "编辑内容" }) }
                    }
                    else -> error("Missing live example category: ${entry.category}")
                }
            }
            // Sibling glass chrome samples only the recorded content layer.
            ExampleTopChrome(
                backdrop = if (Build.VERSION.SDK_INT >= 33) backdrop else null,
                isLightTheme = !dark,
                toolbarFile = entry.file.takeUnless { entry.category == "Sheets (Modals)" },
                onBack = onBack,
                onReference = onReference,
                onTrailing = {
                    if (entry.category == "Action Sheet") {
                        actionSheetMounted = true
                        actionSheetOpen = true
                    } else status = "工具栏操作（演示）"
                },
                blurAlpha = (scroll.value / reveal).coerceIn(0f, 1f),
                scrollDriven = true,
                fallbackTitle = if (compact) categoryTitle(entry.category) else null,
                trailingOverflow = entry.category == "Empty States" &&
                    entry.variant == "Config=Title and Subtitle",
                showTrailing = !(entry.category == "Action Sheet" && actionSheetMounted),
            )
            // Feedback is an overlay, so finishing a drag never moves the track under the finger.
            if (status.isNotEmpty() && entry.category != "Action Sheet") Snackbar(
                modifier = Modifier.align(Alignment.BottomCenter).padding(horizontal = 16.dp)
                    .padding(bottom = when {
                        hasTabs || hasBottomToolbar -> 92.dp
                        else -> 12.dp
                    }).semantics { liveRegion = LiveRegionMode.Polite },
                backgroundColor = colors.group, contentColor = colors.label,
                shape = RoundedCornerShape(Ios27Tokens.GroupRadius.dp),
            ) { Text(status) }
            if (hasTabs) Ios27TabBarExample(
                file = entry.file,
                selectedTabIndex = tab,
                onTabSelected = { tab = it; if (hasSeparateSearch) searchOpen = false },
                backdrop = if (Build.VERSION.SDK_INT >= 33) backdrop else null,
                isLightTheme = !dark,
                splitSearch = hasSeparateSearch,
                onSearchClick = { searchOpen = true },
                modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
            )
            if (hasBottomToolbar) {
                Box(Modifier.align(Alignment.BottomStart).fillMaxWidth().height(84.dp)) {
                    Ios27ToolbarBottomExample(
                        file = entry.file,
                        variant = entry.variant,
                        backdrop = if (Build.VERSION.SDK_INT >= 33) backdrop else null,
                        onAction = { status = "$it（演示）" },
                    )
                }
            }
            if (entry.category == "Color Picker" && colorPickerOpen) {
                Box(Modifier.fillMaxSize()) {
                Ios27ColorPickerDialog(
                    file = entry.file,
                    backdrop = if (Build.VERSION.SDK_INT >= 33) backdrop else null,
                    colors = colors,
                    dark = dark,
                    selectedGridIndex = colorPickerGridIndex,
                    selectedSwatchIndex = colorPickerSwatchIndex,
                    opacity = colorPickerOpacity,
                    onGridSelected = {
                        colorPickerGridIndex = it
                        colorPickerSwatchIndex = -1
                    },
                    onSwatchSelected = {
                        colorPickerSwatchIndex = it
                    },
                    onOpacityChange = { colorPickerOpacity = it },
                    onDismiss = { colorPickerOpen = false },
                )
                }
            }
            if (entry.category == "Sheets (Modals)" && sheetsOpen) {
                Box(Modifier.fillMaxSize()) {
                Ios27SheetsModal(
                    file = entry.file,
                    variant = entry.variant,
                    backdrop = if (Build.VERSION.SDK_INT >= 33) backdrop else null,
                    colors = colors,
                    dark = dark,
                    onDismiss = { sheetsOpen = false },
                    onAction = { status = "模态页面操作（演示）" },
                )
                }
            }
            }
        }
    }
    if (entry.category == "Alert" && alertOpen) {
        Box(Modifier.fillMaxSize()) {
        Ios27AlertExample(
            file = entry.file,
            variant = entry.variant,
            backdrop = if (Build.VERSION.SDK_INT >= 33) backdrop else null,
            colors = colors,
            dark = dark,
            firstValue = alertFirstValue,
            secondValue = alertSecondValue,
            onFirstValueChange = { alertFirstValue = it },
            onSecondValueChange = { alertSecondValue = it },
            onDefault = {
                text = alertFirstValue
                status = "已确认提示弹窗"
                alertOpen = false
            },
            onDestructive = {
                text = ""
                status = "已执行破坏性操作"
                alertOpen = false
            },
            onDismiss = { alertOpen = false },
        )
        }
    }
    if (entry.category == "Action Sheet" && actionSheetMounted) {
        ExampleDesignOverlay {
            Ios27ActionSheetExample(
                backdrop = if (Build.VERSION.SDK_INT >= 33) backdrop else null,
                colors = colors,
                dark = dark,
                visible = actionSheetOpen,
                onAction = { action ->
                    status = "已执行：$action"
                    if (action == "Destructive") text = ""
                    actionSheetOpen = false
                },
                onDismiss = { actionSheetOpen = false },
                onHidden = { actionSheetMounted = false },
            )
        }
    }
    if (entry.category == "Activity View" && activityViewOpen) {
        ExampleDesignOverlay {
            Ios27ActivityViewExample(
                backdrop = if (Build.VERSION.SDK_INT >= 33) backdrop else null,
                colors = colors,
                dark = dark,
                thumbnail = popupBackground.image,
                onAction = { action ->
                    status = "已执行：$action"
                    if (action == "Close") activityViewOpen = false
                },
                onDismiss = { activityViewOpen = false },
            )
        }
    }
    if (dialog != null) AlertDialog(onDismissRequest = { dialog = null },
        title = { Text(dialog.orEmpty()) }, text = {
            Ios27EditableTextField(
                value = note,
                onValueChange = { note = it },
                description = "编辑内容输入框",
                placeholder = "编辑内容",
                backdrop = if (Build.VERSION.SDK_INT >= 33) backdrop else null,
            )
        },
        confirmButton = { TextButton(onClick = {
            text = note
            if (dialog == "新建内容") items = items + note.ifBlank { "新内容 ${items.size + 1}" }
            status = "已保存：${note.ifBlank { "内容" }}"; dialog = null
        }) { Text("保存") } }, dismissButton = { TextButton(onClick = { dialog = null }) { Text("取消") } })
}
