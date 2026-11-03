package dev.liquidglass.sample

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.liquidglass.components.ListeningMode
import dev.liquidglass.components.ListeningModeSelector
import dev.liquidglass.ios27.LocalIos27PageColors
import dev.liquidglass.ios27.ios27AndroidPane

@Composable
internal fun ListeningModeExamplePage(onBack: () -> Unit) {
    var selected by rememberSaveable { mutableStateOf(ListeningMode.NoiseCancellation) }
    val colors = LocalIos27PageColors.current
    ExampleRecordedDesignPage(background = colors.page,
        modifier = Modifier.ios27AndroidPane("听音模式"),
        content = { topInset, _ ->
            Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState())
                .padding(top = topInset + 72.dp, bottom = 24.dp)) {
                Box(Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 14.dp)) {
                    ListeningModeSelector(selected, { selected = it })
                }
                Spacer(Modifier.height(24.dp))
                Text("当前模式：${selected.label}", Modifier.padding(horizontal = 36.dp), color = colors.label)
                Spacer(Modifier.height(8.dp))
                Text("跟随应用配色；这里只演示选择状态，不控制耳机。",
                    Modifier.padding(horizontal = 36.dp), color = colors.secondaryLabel)
            }
        },
        overlay = { topInset, backdrop ->
            ExampleTopChrome(backdrop, isLightTheme = MaterialTheme.colors.isLight,
                appBarHeight = topInset + 48.dp)
            Ios27BackButton(onBack, backdrop,
                Modifier.align(Alignment.TopStart).padding(start = 16.dp, top = topInset + 4.dp))
        })
}
