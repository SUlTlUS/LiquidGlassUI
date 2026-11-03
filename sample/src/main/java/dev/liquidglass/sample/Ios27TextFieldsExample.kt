package dev.liquidglass.sample

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.contextmenu.data.TextContextMenuItem
import androidx.compose.foundation.text.contextmenu.data.TextContextMenuKeys
import androidx.compose.foundation.text.contextmenu.data.TextContextMenuSession
import androidx.compose.foundation.text.contextmenu.provider.LocalTextContextMenuToolbarProvider
import androidx.compose.foundation.text.contextmenu.provider.TextContextMenuDataProvider
import androidx.compose.foundation.text.contextmenu.provider.TextContextMenuProvider
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.TextRange
import dev.liquidglass.*
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.compose.material.Text
import com.zeaze.tianyinwallpaper.backdrop.Backdrop
import dev.liquidglass.ios27.Ios27ListRows
import dev.liquidglass.ios27.Ios27ListSection
import dev.liquidglass.ios27.Ios27PageColors
import dev.liquidglass.ios27.Ios27Tokens
import dev.liquidglass.ios27.LocalIos27PageColors
import kotlinx.coroutines.CompletableDeferred
import kotlin.math.roundToInt

private data class Ios27TextContextMenuRequest(
    val session: Ios27TextContextMenuSession,
    val dataProvider: TextContextMenuDataProvider,
)

private class Ios27TextContextMenuSession(
    private val onClosed: () -> Unit,
) : TextContextMenuSession {
    private val closed = CompletableDeferred<Unit>()

    override fun close() {
        if (closed.complete(Unit)) onClosed()
    }

    suspend fun awaitClose() {
        closed.await()
    }
}

private class Ios27TextContextMenuProvider(
    private val onRequestChanged: (Ios27TextContextMenuRequest?) -> Unit,
) : TextContextMenuProvider {
    private var currentSession: Ios27TextContextMenuSession? = null

    override suspend fun showTextContextMenu(dataProvider: TextContextMenuDataProvider) {
        currentSession?.close()
        lateinit var session: Ios27TextContextMenuSession
        session = Ios27TextContextMenuSession {
            if (currentSession === session) {
                currentSession = null
                onRequestChanged(null)
            }
        }
        currentSession = session
        onRequestChanged(Ios27TextContextMenuRequest(session, dataProvider))
        try {
            session.awaitClose()
        } finally {
            if (currentSession === session) {
                currentSession = null
                onRequestChanged(null)
            }
        }
    }

    fun close() {
        currentSession?.close()
    }
}

@Composable
private fun Ios27SelectionContextMenu(
    request: Ios27TextContextMenuRequest,
    hostCoordinates: LayoutCoordinates,
    colors: Ios27PageColors,
    dark: Boolean,
    backdrop: Backdrop?,
    visible: Boolean,
    onHidden: () -> Unit,
) {
    val items = remember(request) { request.dataProvider.data().components.filterIsInstance<TextContextMenuItem>() }
    val bounds = remember(request) { request.dataProvider.contentBounds(hostCoordinates) }
    val point = remember(request) { request.dataProvider.position(hostCoordinates) }
    val anchor = if (bounds.width > 0 && bounds.height > 0) bounds
        else Rect(point.x, point.y, point.x + 1f, point.y + 1f)
    val popup = rememberGlassPopupState<Unit>()
    val focus = LocalFocusManager.current
    var invokedAction by remember(request.session) { mutableStateOf(false) }
    var moreOpen by remember(request.session) { mutableStateOf(false) }
    var moreMounted by remember(request.session) { mutableStateOf(false) }
    var moreAnchor by remember { mutableStateOf(anchor) }
    val primary = items.filter { it.key == TextContextMenuKeys.CutKey ||
        it.key == TextContextMenuKeys.CopyKey || it.key == TextContextMenuKeys.PasteKey }
    val more = items.filter { it !in primary }
    fun invoke(item: TextContextMenuItem) {
        invokedAction = true
        moreOpen = false
        item.onClick(request.session)
        popup.dismiss()
    }
    LaunchedEffect(request.session) { popup.open() }
    LaunchedEffect(visible) { if (!visible) { moreOpen = false; popup.dismiss() } }
    LaunchedEffect(popup.expanded, popup.mounted) {
        if (popup.mounted && !popup.expanded && !invokedAction) focus.clearFocus(force = true)
    }
    Ios26ModalScope(backdrop, dark) {
    dev.liquidglass.GlassMenuMaterialScope {
        GlassPopup(popup, remember(anchor) { GlassPopupAnchor(anchor) }, dark,
            properties = GlassPopupProperties(modal = true, focusable = false, returnIntoSource = false),
            placement = GlassMenuPlacement.BelowPoint, avoid = anchor,
            title = "文字操作", onResult = { request.session.close(); onHidden() }) {
            Row(Modifier.width(238.dp).height(44.dp), verticalAlignment = Alignment.CenterVertically) {
                primary.forEach { item ->
                    Box(Modifier.weight(1f).fillMaxHeight().clickable { invoke(item) },
                        contentAlignment = Alignment.Center) {
                        Text(item.label.toString(), color = colors.label, style = Ios27Tokens.Type.Body)
                    }
                }
                if (more.isNotEmpty()) Box(Modifier.width(44.dp).fillMaxHeight()
                    .onGloballyPositioned { moreAnchor = hostCoordinates.localBoundingBoxOf(it, clipBounds = false) }
                    .clickable { moreMounted = true; moreOpen = true }, contentAlignment = Alignment.Center) {
                    Text("›", color = colors.label, style = Ios27Tokens.Type.Title2)
                }
            }
        }
        if (moreMounted) GlassMorphMenu(moreOpen, moreAnchor,
            more.map { item -> GlassMenuAction(item.label.toString()) { invoke(item) } }, dark,
            onDismiss = { moreOpen = false }, onReturnComplete = { moreMounted = false },
            placement = GlassMenuPlacement.BelowAnchorStart,
            highlightStyle = currentGlassHighlight(), returnIntoSource = false,
            properties = GlassPopupProperties(modal = true, focusable = false))
    }
    }
}

@Composable
internal fun Ios27TextContextMenuHost(
    backdrop: Backdrop?,
    colors: Ios27PageColors,
    dark: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    var request by remember { mutableStateOf<Ios27TextContextMenuRequest?>(null) }
    var closing by remember { mutableStateOf(false) }
    var hostCoordinates by remember { mutableStateOf<LayoutCoordinates?>(null) }
    val provider = remember {
        Ios27TextContextMenuProvider { nextRequest ->
            if (nextRequest != null) { request = nextRequest; closing = false }
            else closing = true
        }
    }

    Box(
        modifier.onGloballyPositioned { hostCoordinates = it },
    ) {
        CompositionLocalProvider(
            LocalTextContextMenuToolbarProvider provides provider,
        ) {
            content()
        }

        val activeRequest = request
        val coordinates = hostCoordinates
        if (activeRequest != null && coordinates != null && coordinates.isAttached) {
            Ios27SelectionContextMenu(
                request = activeRequest,
                hostCoordinates = coordinates,
                colors = colors,
                dark = dark,
                backdrop = backdrop,
                visible = !closing,
                onHidden = { if (request === activeRequest) request = null },
            )
        }
    }
}

@Composable
internal fun Ios27MenuEditablePage(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = LocalIos27PageColors.current
    val textStyle = Ios27Tokens.Type.Body.copy(color = colors.label)
    var field by remember { mutableStateOf(TextFieldValue(value)) }
    SideEffect { if (field.text != value) field = field.copy(text = value, selection = TextRange(value.length)) }

    BasicTextField(
        value = field,
        onValueChange = { field = it; if (it.text != value) onValueChange(it.text) },
        modifier = modifier
            .fillMaxSize()
            .onFocusChanged { if (!it.isFocused) field = field.copy(selection = TextRange(field.selection.end)) }
            .padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 20.dp)
            .semantics { contentDescription = "整页可编辑文本" },
        singleLine = false,
        textStyle = textStyle,
        cursorBrush = SolidColor(colors.accent),
        decorationBox = { innerTextField ->
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.TopStart) {
                innerTextField()
                if (value.isEmpty()) {
                    Text(
                        "Hello World",
                        style = textStyle,
                        color = colors.secondaryLabel.copy(alpha = 0.36f),
                    )
                }
            }
        },
    )
}

internal enum class Ios27TextFieldPreview(val label: String, val initialValue: String) {
    Placeholder("Placeholder", ""),
    Focused("Focused", ""),
    Typing("Typing", "Value"),
    ValueEntered("Value Entered", "Value"),
}

/** Four editable states from Examples/Text Fields, kept in the source's 208pt inset group. */
@Composable
internal fun Ios27TextFieldsExample(
    sceneKey: String,
    backdrop: Backdrop? = null,
) {
    Ios27ListSection {
        Ios27ListRows(Ios27TextFieldPreview.entries) { preview ->
            var value by rememberSaveable(sceneKey, preview.name) { mutableStateOf(preview.initialValue) }
            Ios27EditableTextField(
                value = value,
                onValueChange = { value = it },
                description = "${preview.label} 输入框",
                preview = preview,
                backdrop = backdrop,
            )
        }
    }
}

@Composable
internal fun Ios27EditableTextField(
    value: String,
    onValueChange: (String) -> Unit,
    description: String,
    modifier: Modifier = Modifier,
    preview: Ios27TextFieldPreview = Ios27TextFieldPreview.ValueEntered,
    placeholder: String = "Placeholder",
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    singleLine: Boolean = true,
    backdrop: Backdrop? = null,
) {
    val colors = LocalIos27PageColors.current
    val focusRequester = remember { FocusRequester() }
    var touched by rememberSaveable(description, preview.name) { mutableStateOf(false) }
    var focused by remember { mutableStateOf(false) }
    val showPreviewCaret = !touched &&
        (preview == Ios27TextFieldPreview.Focused || preview == Ios27TextFieldPreview.Typing)
    val showPlaceholder = value.isEmpty() && !focused && !showPreviewCaret
    val showClear = value.isNotEmpty() && (focused || showPreviewCaret)
    val textStyle = Ios27Tokens.Type.Body.copy(color = colors.label)

    Row(
        modifier.fillMaxWidth().height(52.dp).padding(start = 16.dp, end = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BasicTextField(
            value = value,
            onValueChange = { onValueChange(it); touched = true },
            modifier = Modifier.weight(1f).fillMaxHeight()
                .focusRequester(focusRequester)
                .onFocusChanged { state ->
                    focused = state.isFocused
                    if (state.isFocused) touched = true
                }
                .semantics {
                    contentDescription = description
                    stateDescription = when {
                        focused -> if (value.isEmpty()) "已聚焦" else "正在输入"
                        showPreviewCaret -> preview.label
                        value.isEmpty() -> "未输入"
                        else -> "已输入"
                    }
                },
            singleLine = singleLine,
            keyboardOptions = keyboardOptions,
            textStyle = textStyle,
            cursorBrush = SolidColor(colors.accent),
            decorationBox = { innerTextField ->
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.CenterStart) {
                    Box(Modifier.alpha(if (showPreviewCaret) 0f else 1f)) { innerTextField() }
                    if (showPlaceholder) {
                        Text(
                            placeholder,
                            style = textStyle,
                            color = colors.secondaryLabel.copy(alpha = 0.30f),
                        )
                    }
                    if (showPreviewCaret) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (value.isNotEmpty()) Text(value, style = textStyle)
                            Box(
                                Modifier.padding(start = if (value.isEmpty()) 0.dp else 1.dp)
                                    .size(width = 2.dp, height = 22.dp)
                                    .background(colors.accent),
                            )
                        }
                    }
                }
            },
        )

        if (showClear) {
            val clearInteractions = remember { MutableInteractionSource() }
            val clearPressed by clearInteractions.collectIsPressedAsState()
            Box(
                Modifier.width(44.dp).fillMaxHeight()
                    .clickable(
                        interactionSource = clearInteractions,
                        indication = null,
                        role = Role.Button,
                        onClick = {
                            onValueChange("")
                            touched = true
                            focusRequester.requestFocus()
                        },
                    )
                    .semantics { contentDescription = "清除 $description" },
                contentAlignment = Alignment.CenterEnd,
            ) {
                val marker = colors.secondaryLabel.copy(alpha = 0.30f)
                Box(
                    Modifier.size(28.dp)
                        .clip(CircleShape)
                        .background(if (clearPressed) colors.pressed else Color.Transparent),
                    contentAlignment = Alignment.Center,
                ) {
                    Canvas(Modifier.size(20.dp)) {
                        drawCircle(marker)
                        val center = Offset(size.width / 2f, size.height / 2f)
                        val radius = 4.dp.toPx()
                        val stroke = 2.dp.toPx()
                        drawLine(
                            Color.White,
                            Offset(center.x - radius, center.y - radius),
                            Offset(center.x + radius, center.y + radius),
                            stroke,
                            cap = StrokeCap.Round,
                        )
                        drawLine(
                            Color.White,
                            Offset(center.x + radius, center.y - radius),
                            Offset(center.x - radius, center.y + radius),
                            stroke,
                            cap = StrokeCap.Round,
                        )
                    }
                }
            }
        }
    }
}
