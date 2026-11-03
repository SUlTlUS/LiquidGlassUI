package dev.liquidglass.sample

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.key
import dev.liquidglass.GlassStandard

@Composable
internal fun Ios27MechanismExamplePage(key: String, standard: GlassStandard, onBack: () -> Unit) {
    key(key, standard) {
            Ios27NativeMechanismPage(
                Ios27MechanismExample.entries.firstOrNull { it.key == key } ?: Ios27MechanismExample.Popup,
                onBack,
                standard,
            )
    }
}
