package dev.liquidglass.sample

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.material.MaterialTheme
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.viewinterop.AndroidView
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.zeaze.tianyinwallpaper.backdrop.Backdrop
import com.zeaze.tianyinwallpaper.backdrop.backdrops.LayerBackdrop
import com.zeaze.tianyinwallpaper.backdrop.backdrops.rememberCanvasBackdrop
import dev.liquidglass.ios27.LocalIos27PageColors
import dev.liquidglass.*

@Composable
internal fun Ios26ModalScope(backdrop: Backdrop?, dark: Boolean, content: @Composable () -> Unit) {
    val colors = LocalIos27PageColors.current
    val fallback = rememberCanvasBackdrop { drawRect(colors.page) }
    GlassTheme(darkTheme = dark, accentColor = colors.accent) {
        GlassBackdropProvider(backdrop ?: fallback) {
            GlassHighlightScope(currentGlassHighlight(), content)
        }
    }
}

/** Adapter for authored iOS26 content; modal ownership and pane rendering belong to iOS27. */
@Composable
internal fun Ios26ModalLayer(
    backdrop: Backdrop?, onDismissRequest: () -> Unit, panelModifier: Modifier,
    shape: Shape, surfaceColor: Color, scrimColor: Color, dark: Boolean,
    modifier: Modifier = Modifier, alignment: Alignment = Alignment.Center,
    offsetX: Dp = 0.dp, offsetY: Dp = 0.dp, fallbackSurface: Color = surfaceColor,
    exportedBackdrop: LayerBackdrop? = null, description: String = "液态玻璃弹窗",
    visible: Boolean = true, onClosed: () -> Unit = {},
    behindSurface: (@Composable BoxScope.() -> Unit)? = null,
    solidSurface: Boolean = false,
    exportPaintedSurface: Boolean = false,
    transition: GlassModalTransition = GlassModalTransition.Alert,
    content: @Composable BoxScope.() -> Unit,
) {
    Ios26ModalScope(backdrop, dark) {
        GlassModal(visible, dark, description, onDismissRequest,
            modifier = modifier, panelModifier = Modifier.offset(offsetX, offsetY).then(panelModifier),
            alignment = alignment, scrimColor = scrimColor, onClosed = onClosed, transition = transition,
            surface = {
                behindSurface?.invoke(this)
                GlassModalSurface(shape, dark, Modifier.matchParentSize(), exportedBackdrop,
                    solidColor = surfaceColor.takeIf { solidSurface },
                    exportPaintedSurface = exportPaintedSurface)
            },
            content = content)
    }
}

@Composable
internal fun Ios26Modal(
    backdrop: Backdrop?, onDismissRequest: () -> Unit, width: Dp, height: Dp,
    modifier: Modifier = Modifier, alignment: Alignment = Alignment.Center,
    offsetX: Dp = 0.dp, offsetY: Dp = 0.dp, cornerRadius: Dp = 34.dp,
    surfaceColor: Color, scrimColor: Color, dark: Boolean, description: String = "液态玻璃弹窗",
    visible: Boolean = true,
    content: @Composable BoxScope.() -> Unit,
) = Ios26ModalLayer(backdrop, onDismissRequest, modifier.size(width, height), RoundedCornerShape(cornerRadius),
    surfaceColor, scrimColor, dark, alignment = alignment, offsetX = offsetX, offsetY = offsetY,
    description = description, visible = visible, content = content)

@Composable
internal fun Ios26AlertDialog(
    onDismissRequest: () -> Unit,
    confirmButton: @Composable () -> Unit,
    title: @Composable () -> Unit,
    text: (@Composable () -> Unit)? = null,
    dismissButton: (@Composable () -> Unit)? = null,
    backdrop: Backdrop? = null,
) {
    val dark = !MaterialTheme.colors.isLight
    Ios26ModalScope(backdrop, dark) {
        GlassModal(true, dark, "提示弹窗", onDismissRequest,
            modifier = Modifier.safeDrawingPadding().padding(16.dp),
            panelModifier = Modifier.widthIn(max = 348.dp).fillMaxWidth()) {
            Column(Modifier.fillMaxWidth().padding(20.dp)) {
                title()
                Column(Modifier.weight(1f, fill = false).verticalScroll(rememberScrollState())
                    .padding(vertical = 16.dp)) { text?.invoke() }
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    dismissButton?.invoke(); confirmButton()
                }
            }
        }
    }
}

@OptIn(androidx.compose.material.ExperimentalMaterialApi::class)
@Composable
internal fun Ios26BottomSheetLayout(
    modifier: Modifier = Modifier,
    sheetState: androidx.compose.material.ModalBottomSheetState,
    sheetBackgroundColor: Color,
    backdrop: Backdrop,
    sheetContent: @Composable ColumnScope.() -> Unit,
    content: @Composable () -> Unit,
) {
    val scope = rememberCoroutineScope()
    Box(modifier.fillMaxSize()) {
        content()
        Ios26ModalLayer(backdrop, { scope.launch { sheetState.hide() } },
            panelModifier = Modifier.fillMaxWidth().padding(8.dp), shape = RoundedCornerShape(32.dp),
            surfaceColor = sheetBackgroundColor, scrimColor = Color.Black.copy(alpha = .23f),
            dark = !MaterialTheme.colors.isLight, alignment = Alignment.BottomCenter,
            visible = sheetState.isVisible, description = "操作面板") {
            Column(Modifier.verticalScroll(rememberScrollState()), content = sheetContent)
        }
    }
}

/** System date/time widgets supply content only; the window, motion and lighting are shared 27/26. */
@Composable
internal fun Ios26DateTimeDialog(
    time: Boolean, backdrop: Backdrop, year: Int, month: Int, day: Int, hour: Int, minute: Int,
    onDismiss: () -> Unit, onDate: (Int, Int, Int) -> Unit, onTime: (Int, Int) -> Unit,
) {
    var y by remember { mutableIntStateOf(year) }
    var m by remember { mutableIntStateOf(month) }
    var d by remember { mutableIntStateOf(day) }
    var h by remember { mutableIntStateOf(hour) }
    var min by remember { mutableIntStateOf(minute) }
    val dark = !MaterialTheme.colors.isLight
    Ios26ModalScope(backdrop, dark) {
        GlassDialog(true, dark, if (time) "选择时间" else "选择日期", onDismiss,
            actions = listOf(GlassDialogAction("取消", onClick = onDismiss), GlassDialogAction("确定") {
                if (time) onTime(h, min) else onDate(y, m, d)
                onDismiss()
            })) {
            if (time) AndroidView(factory = { context -> android.widget.TimePicker(context).apply {
                setIs24HourView(true); this.hour = h; this.minute = min
                setOnTimeChangedListener { _, hh, mm -> h = hh; min = mm }
            } }, modifier = Modifier.fillMaxWidth().height(240.dp))
            else AndroidView(factory = { context -> android.widget.DatePicker(context).apply {
                init(y, m, d) { _, yy, mm, dd -> y = yy; m = mm; d = dd }
            } }, modifier = Modifier.fillMaxWidth().height(330.dp))
        }
    }
}

@Composable
internal fun Ios26PickerPopup(
    visible: Boolean, backdrop: Backdrop, dark: Boolean, title: String,
    onConfirm: () -> Unit, onCancel: () -> Unit, onHidden: () -> Unit,
    anchorBounds: androidx.compose.ui.geometry.Rect,
    sourceLabel: String,
    sourceImage: androidx.compose.ui.graphics.ImageBitmap? = null,
    sourceSurfaceImage: androidx.compose.ui.graphics.ImageBitmap? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    Ios26SceneOverlay { hostOrigin ->
        Ios26ModalScope(backdrop, dark) {
            val popup = rememberGlassPopupState<Unit>()
            val anchor = remember(anchorBounds, hostOrigin) { GlassPopupAnchor(anchorBounds.translate(-hostOrigin)) }
            val label = remember { sourceLabel }
            LaunchedEffect(visible) { if (visible && !popup.mounted) popup.open(sourceImage, sourceSurfaceImage) else if (!visible) popup.dismiss() }
            LaunchedEffect(popup.expanded, popup.mounted) {
                if (popup.mounted && !popup.expanded && visible) onCancel()
            }
            GlassPopup(popup, anchor, dark, title = title, onResult = { onHidden() },
                sourceContent = { androidx.compose.material.Text(label) }) {
                Column(Modifier.fillMaxWidth().padding(16.dp)) {
                    androidx.compose.material.Text(title)
                    content()
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        androidx.compose.material.TextButton(onClick = onCancel) { androidx.compose.material.Text("取消") }
                        androidx.compose.material.TextButton(onClick = onConfirm) { androidx.compose.material.Text("确定") }
                    }
                }
            }
        }
    }
}
