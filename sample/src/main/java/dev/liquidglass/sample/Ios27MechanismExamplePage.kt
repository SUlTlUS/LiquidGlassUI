package dev.liquidglass.sample

import android.os.Build
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

@Composable
internal fun Ios27MechanismExamplePage(key: String, onBack: () -> Unit) {
    if (Build.VERSION.SDK_INT >= 33) {
        key(key) {
            Ios27NativeMechanismPage(
                Ios27MechanismExample.entries.firstOrNull { it.key == key } ?: Ios27MechanismExample.Buttons,
                onBack,
            )
        }
    } else {
        Column(Modifier.fillMaxSize().safeDrawingPadding().padding(16.dp)) {
            TextButton(onClick = onBack) { Text("返回") }
            Text("iOS 27 玻璃示例需要 Android 13 或更新版本。iOS 26 示例仍可使用。")
        }
    }
}
