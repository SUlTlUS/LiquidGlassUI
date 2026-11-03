package dev.liquidglass.sample

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot

private class SceneOverlayEntry(
    val locals: State<CompositionLocalContext>,
    val content: State<@Composable (Offset) -> Unit>,
)

private val LocalSceneOverlays = staticCompositionLocalOf<MutableList<SceneOverlayEntry>?> { null }

/** Overlays are siblings of the recorded page, in the same Android window. */
@Composable
internal fun Ios26SceneOverlayHost(content: @Composable () -> Unit) {
    val entries = remember { mutableStateListOf<SceneOverlayEntry>() }
    var origin by remember { mutableStateOf<Offset?>(null) }
    Box(Modifier.fillMaxSize().onGloballyPositioned { origin = it.positionInRoot() }) {
        CompositionLocalProvider(LocalSceneOverlays provides entries, content = content)
        origin?.let { position ->
            entries.forEach { entry ->
                key(entry) {
                    CompositionLocalProvider(entry.locals.value) { entry.content.value(position) }
                }
            }
        }
    }
}

@Composable
internal fun Ios26SceneOverlay(content: @Composable (hostOrigin: Offset) -> Unit) {
    val entries = checkNotNull(LocalSceneOverlays.current) { "A picker requires Ios26SceneOverlayHost" }
    val current = rememberUpdatedState(content)
    val locals = rememberUpdatedState(currentCompositionLocalContext)
    val entry = remember { SceneOverlayEntry(locals, current) }
    DisposableEffect(entries, entry) {
        entries.add(entry)
        onDispose { entries.remove(entry) }
    }
}
