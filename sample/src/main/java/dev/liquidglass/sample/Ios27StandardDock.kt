package dev.liquidglass.sample

import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.zeaze.tianyinwallpaper.backdrop.Backdrop
import dev.liquidglass27.glass.GlassDestination
import dev.liquidglass27.glass.Ios27NavigationDock
import dev.liquidglass27.glass.GlassTheme
import dev.liquidglass27.glass.Ios27BackdropScope

/** The existing host navigation uses the new mechanism without importing demo UI. */
@RequiresApi(33)
@Composable
internal fun Ios27StandardDock(backdrop: Backdrop, dark: Boolean, selected: Int, onSelect: (Int) -> Unit) {
    GlassTheme(dark = dark) {
        Ios27BackdropScope(backdrop) {
            Ios27NavigationDock(
                destinations = listOf(
                    GlassDestination("◈", "组件"),
                    GlassDestination("≡", "说明"),
                    GlassDestination("⚙", "设置"),
                ),
                selectedIndex = selected,
                onSelect = onSelect,
                dark = dark,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
