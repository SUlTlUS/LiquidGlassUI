package dev.liquidglass27.glass

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.zeaze.tianyinwallpaper.backdrop.Backdrop

/** Safe to call at application startup on every supported Android version. */
fun prepareIos27Glass(context: Context): Boolean {
    if (Build.VERSION.SDK_INT < 33) return false
    GlassShaderPrograms.prepare(context.applicationContext)
    return true
}

/** Standard entry: Sketch optics, FlClash geometry, interaction and highlight. */
@RequiresApi(33)
@Composable
fun Ios27GlassScene(
    modifier: Modifier = Modifier,
    background: @Composable BoxScope.() -> Unit,
    content: @Composable BoxScope.() -> Unit,
) {
    GlassScene(modifier = modifier, sketchMaterial = true, background = background, content = content)
}

/**
 * Connect an existing independent Sketch recording to iOS 27 controls.
 * The supplied backdrop MUST exclude this scope's controls and overlays.
 * Anchored popup hosts must span the root window used by positionInRoot().
 * No new capture or bitmap is created here; layout coordinates remain owned by Backdrop.
 */
@RequiresApi(33)
@Composable
fun Ios27BackdropScope(backdrop: Backdrop, content: @Composable () -> Unit) {
    val frame = remember { GlassFrame() }
    val materials = remember { SketchMaterialCache() }
    CompositionLocalProvider(
        LocalGlassFrame provides frame,
        LocalSketchGlassBackdrop provides backdrop,
        LocalSketchMaterialCache provides materials,
        LocalGlassCaptureRegistry provides null,
        content = content,
    )
}
