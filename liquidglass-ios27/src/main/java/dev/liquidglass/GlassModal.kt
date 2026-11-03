@file:OptIn(androidx.compose.ui.ExperimentalComposeUiApi::class)

package dev.liquidglass

import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.*
import androidx.compose.ui.unit.dp

enum class GlassModalTransition { Alert, BottomSheet }

/** Shared iOS 27 modal route: animation, focus containment, IME and outside/back dismissal. */
@RequiresApi(33)
@Composable
fun GlassModal(
    visible: Boolean,
    dark: Boolean,
    title: String,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    panelModifier: Modifier = Modifier,
    alignment: Alignment = Alignment.Center,
    dismissible: Boolean = true,
    scrimColor: Color = glassColors(dark).scrim.copy(alpha = alertScrimAlpha(dark)),
    highlightStyle: GlassMenuHighlight = LocalGlassMenuHighlight.current,
    onClosed: () -> Unit = {},
    transition: GlassModalTransition = GlassModalTransition.Alert,
    surface: @Composable BoxScope.() -> Unit = {
        GlassPane(Modifier.matchParentSize(), GlassStyle.alert(dark), cornerDp = 34f)
    },
    content: @Composable BoxScope.() -> Unit,
) {
    val opacity = remember { Animatable(0f) }
    var heldScale by remember { mutableStateOf<Float?>(null) }
    val closed by rememberUpdatedState(onClosed)
    val focus = remember { FocusRequester() }
    LaunchedEffect(visible, transition) {
        heldScale = if (visible) null else alertEntryScale(opacity.value)
        val wasShowing = opacity.value > 0f
        opacity.animateTo(if (visible) 1f else 0f,
            if (transition == GlassModalTransition.BottomSheet) {
                if (visible) FlClashMenuSprings.unfold() else FlClashMenuSprings.fold()
            } else tween(FlClashAlertEasing.durationMillis, easing = FlClashAlertEasing))
        if (!visible && wasShowing) closed()
    }
    if (!glassAlertShouldRender(visible, opacity.value)) return
    BackHandler { if (dismissible && visible) onDismissRequest() }
    LaunchedEffect(Unit) { focus.requestFocus() }
    CompositionLocalProvider(LocalGlassMenuHighlight provides highlightStyle) {
        Box(Modifier.fillMaxSize().semantics { isTraversalGroup = true }
            .background(scrimColor.copy(alpha = scrimColor.alpha * opacity.value.coerceIn(0f, 1f)))
            .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) {
                if (dismissible && visible) onDismissRequest()
            }.imePadding().then(modifier), contentAlignment = alignment) {
            Box(panelModifier.semantics { paneTitle = title; isTraversalGroup = true }
                .focusProperties { onExit = { cancelFocusChange() } }.focusGroup()
                .focusRequester(focus).focusable()
                .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null, onClick = {})
                .graphicsLayer {
                    if (transition == GlassModalTransition.BottomSheet) {
                        // Keep spring overshoot; the extra reach hides shadow/stack peek below the viewport.
                        translationY = (size.height + 64.dp.toPx()) * (1f - opacity.value)
                        alpha = 1f
                        scaleX = 1f; scaleY = 1f
                    } else {
                        translationY = 0f
                        alpha = opacity.value.coerceIn(0f, 1f)
                        scaleX = heldScale ?: alertEntryScale(alpha); scaleY = scaleX
                    }
                }) {
                surface()
                content()
            }
        }
    }
}

/** The same lighting policy also applies to anchored popups and nested panes. */
@Composable
fun GlassHighlightScope(style: GlassMenuHighlight, content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalGlassMenuHighlight provides style, content = content)
}
