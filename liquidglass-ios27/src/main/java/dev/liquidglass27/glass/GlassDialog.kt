@file:OptIn(androidx.compose.ui.ExperimentalComposeUiApi::class)

package dev.liquidglass27.glass

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import kotlin.math.max

data class GlassDialogAction(val text: String, val enabled: Boolean = true, val onClick: () -> Unit)

internal fun dialogActionsSideBySide(width: Int, gap: Int, intrinsicWidths: List<Int>) =
    intrinsicWidths.size == 2 && intrinsicWidths.all { it <= (width - gap) / 2 }

/** A Compose slot API corresponding to CommonDialog and showFloatingDialog. */
@androidx.annotation.RequiresApi(33)
@Composable
fun GlassDialog(
    visible: Boolean,
    dark: Boolean,
    title: String,
    onDismissRequest: () -> Unit,
    actions: List<GlassDialogAction> = emptyList(),
    modifier: Modifier = Modifier,
    dismissible: Boolean = true,
    hasFields: Boolean = false,
    destructive: Boolean = false,
    contentPadding: PaddingValues? = null,
    trailing: (@Composable () -> Unit)? = null,
    onClosed: () -> Unit = {},
    content: @Composable ColumnScope.() -> Unit = {},
) {
    val opacity = remember { Animatable(0f) }
    var heldScale by remember { mutableStateOf<Float?>(null) }
    val closed by rememberUpdatedState(onClosed)
    val focus = remember { FocusRequester() }
    LaunchedEffect(visible) {
        heldScale = if (visible) null else alertEntryScale(opacity.value)
        val wasShowing = opacity.value > 0f
        opacity.animateTo(if (visible) 1f else 0f,
            tween(FlClashAlertEasing.durationMillis, easing = FlClashAlertEasing))
        if (!visible && wasShowing) closed()
    }
    if (!glassAlertShouldRender(visible, opacity.value)) return
    val colors = glassColors(dark)
    BackHandler { if (dismissible && visible) onDismissRequest() }
    LaunchedEffect(Unit) { focus.requestFocus() }
    Box(Modifier.fillMaxSize().semantics { isTraversalGroup = true }
        .background(colors.scrim.copy(alpha = alertScrimAlpha(dark) * opacity.value.coerceIn(0f, 1f)))
        .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) {
            if (dismissible && visible) onDismissRequest()
        }.safeDrawingPadding().imePadding().padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center) {
        Box(modifier.widthIn(max = 348.dp).fillMaxWidth()
            .semantics { paneTitle = title; isTraversalGroup = true }
            .focusProperties { onExit = { cancelFocusChange() } }.focusGroup()
            .focusRequester(focus).focusable()
            .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null, onClick = {})
            .graphicsLayer {
                alpha = opacity.value.coerceIn(0f, 1f)
                scaleX = heldScale ?: alertEntryScale(alpha); scaleY = scaleX
            }.glassPanelPress()) {
            GlassPane(Modifier.matchParentSize(), GlassStyle.alert(dark), cornerDp = 34f)
            Column(Modifier.fillMaxWidth().padding(top = 21.dp, bottom = 16.dp)) {
                val titleStyle = androidx.compose.ui.text.TextStyle(fontSize = 17.sp, lineHeight = 22.sp,
                    fontWeight = if (hasFields) FontWeight.Normal else FontWeight.SemiBold, color = colors.onSurface)
                Layout(modifier = Modifier.fillMaxWidth().padding(horizontal = 30.dp), content = {
                    Text(title, style = titleStyle)
                    Box { trailing?.invoke() }
                }) { measurables, constraints ->
                    val end = measurables[1].measure(constraints.copy(minWidth = 0, minHeight = 0))
                    val reserved = if (hasFields) end.width * 2 else end.width
                    val label = measurables[0].measure(constraints.copy(minWidth = 0, minHeight = 0,
                        maxWidth = (constraints.maxWidth - reserved).coerceAtLeast(0)))
                    val h = max(label.height, end.height)
                    layout(constraints.maxWidth, h) {
                        label.placeRelative(if (hasFields) (constraints.maxWidth - label.width) / 2 else 0,
                            (h - label.height) / 2)
                        end.placeRelative(constraints.maxWidth - end.width, (h - end.height) / 2)
                    }
                }
                val inset = if (hasFields) 16.dp else 30.dp
                CompositionLocalProvider(LocalContentColor provides colors.onSurfaceVariant.copy(alpha = if (dark) 1f else .85f)) {
                    ProvideTextStyle(androidx.compose.ui.text.TextStyle(fontSize = 15.sp, lineHeight = 20.sp)) {
                        Column(Modifier.weight(1f, fill = false).verticalScroll(rememberScrollState())
                            .padding(contentPadding ?: PaddingValues(start = inset, end = inset,
                                top = if (hasFields) 16.dp else 6.dp, bottom = 20.dp)), content = content)
                    }
                }
                GlassDialogActions(actions.map { it.copy(enabled = it.enabled && visible) },
                    dark, destructive, Modifier.padding(horizontal = 16.dp))
            }
        }
    }
}

@androidx.annotation.RequiresApi(33)
@Composable
private fun GlassDialogActions(actions: List<GlassDialogAction>, dark: Boolean, destructive: Boolean,
    modifier: Modifier) {
    Layout(modifier = modifier.fillMaxWidth(), content = {
        actions.forEachIndexed { index, action ->
            GlassDialogButton(action, dark, isDefault = !destructive && index == actions.lastIndex,
                destructive = destructive && index == actions.lastIndex)
        }
    }) { measurables, constraints ->
        val gap = 8.dp.roundToPx()
        val width = constraints.maxWidth
        val side = dialogActionsSideBySide(width, gap, measurables.map { it.maxIntrinsicWidth(Constraints.Infinity) })
        val childWidth = if (side) (width - gap) / 2 else width
        val children = measurables.map { it.measure(Constraints.fixedWidth(childWidth)) }
        val h = if (side) children.maxOf { it.height }
            else children.sumOf { it.height } + gap * (children.size - 1).coerceAtLeast(0)
        layout(width, h) {
            var y = 0
            for (step in children.indices) {
                val index = if (side) step else (step + 1) % children.size
                val child = children[index]
                child.placeRelative(if (side) index * (childWidth + gap) else 0, if (side) 0 else y)
                y += child.height + gap
            }
        }
    }
}

@androidx.annotation.RequiresApi(33)
@Composable
private fun GlassDialogButton(action: GlassDialogAction, dark: Boolean, isDefault: Boolean, destructive: Boolean) {
    val colors = glassColors(dark)
    val interactions = remember { MutableInteractionSource() }
    val pressed by interactions.collectIsPressedAsState()
    val plain = GlassStyle.alert(dark).tint
    val background = if (isDefault) colors.primary else plain
    val share = if (!pressed) 1f else if (dark) { if (isDefault) .5f else 1f } else .4f
    val shape = remember { GenericShape { size, _ ->
        addPath(gooPlatterOutline(Rect(Offset.Zero, size), size.minDimension / 2).asComposePath())
    } }
    Box(Modifier.heightIn(min = 48.dp).background(background.copy(alpha = background.alpha * share), shape)
        .clickable(enabled = action.enabled, interactionSource = interactions, indication = null,
            role = Role.Button, onClick = action.onClick).padding(horizontal = 12.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center) {
        Text(action.text, fontSize = 17.sp, lineHeight = 22.sp,
            fontWeight = if (isDefault) FontWeight.SemiBold else FontWeight.Medium,
            color = (if (destructive) glassDestructive(dark) else if (isDefault) colors.onPrimary else colors.onSurface)
                .copy(alpha = if (action.enabled) 1f else .38f))
    }
}

/** Field styling used by hasFields dialogs, using Compose text/IME APIs. */
@androidx.annotation.RequiresApi(33)
@Composable
fun GlassDialogTextField(value: String, onValueChange: (String) -> Unit, dark: Boolean,
    modifier: Modifier = Modifier, placeholder: String = "", singleLine: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default) {
    val colors = glassColors(dark)
    val fill = GlassStyle.alert(dark).tint
    val radius = with(androidx.compose.ui.platform.LocalDensity.current) { 24.dp.toPx() }
    TextField(value, onValueChange, modifier.fillMaxWidth(), singleLine = singleLine,
        placeholder = { Text(placeholder) }, keyboardOptions = keyboardOptions, keyboardActions = keyboardActions,
        shape = GenericShape { size, _ -> addPath(gooPlatterOutline(Rect(Offset.Zero, size), radius).asComposePath()) },
        colors = TextFieldDefaults.colors(focusedContainerColor = fill, unfocusedContainerColor = fill,
            focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent,
            focusedTextColor = colors.onSurface, unfocusedTextColor = colors.onSurface))
}
