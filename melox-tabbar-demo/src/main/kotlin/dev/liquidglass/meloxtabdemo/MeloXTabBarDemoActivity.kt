package dev.liquidglass.meloxtabdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.lladlam.melox.ui.*
import com.lladlam.melox.ui.player.*

class MeloXTabBarDemoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { Demo() }
    }
}

@Composable
private fun Demo() {
    val initialDark = isSystemInDarkTheme()
    var dark by rememberSaveable { mutableStateOf(initialDark) }
    var selected by rememberSaveable { mutableStateOf(AppTab.Home) }
    val scrollState = rememberMeloXTabBarScrollState()
    var minimized by scrollState::minimized
    val minimizeConnection = scrollState.nestedScrollConnection()
    var frosted by rememberSaveable { mutableStateOf(false) }
    var autoTone by rememberSaveable { mutableStateOf(true) }
    var playing by rememberSaveable { mutableStateOf(false) }
    var trackIndex by rememberSaveable { mutableIntStateOf(0) }
    var position by rememberSaveable { mutableLongStateOf(0L) }
    var volume by rememberSaveable { mutableFloatStateOf(0.5f) }
    var status by rememberSaveable { mutableStateOf("") }
    val queue = remember {
        val artwork = "android.resource://dev.liquidglass.meloxtabdemo/${com.lladlam.melox.R.drawable.melox_demo_artwork}"
        listOf(
            MeloXQueueEntry(0, "demo-0", "demo-0", "空耳", "ナナツカゼ", artwork),
            MeloXQueueEntry(1, "demo-1", "demo-1", "下一首示例", "MeloX", artwork),
        )
    }
    val playback = MeloXPlaybackUiState(queue, trackIndex, playing, 2,
        onTogglePlayPause = { playing = !playing },
        onNext = { trackIndex = (trackIndex + 1) % queue.size },
        onPrevious = { trackIndex = (trackIndex + queue.size - 1) % queue.size },
        positionMs = position, volume = volume,
        onSeek = { position = it }, onVolumeChange = { volume = it })
    MaterialTheme(colorScheme = if (dark) darkColorScheme() else lightColorScheme()) {
        val bg = if (dark) Color(0xFF0B0B0D) else Color(0xFFF7F7FA)
        MeloXPlayerScaffold(state = playback) { miniPlayer ->
        MeloXTabBarScaffold(
            selectedTab = selected,
            onTabSelected = { selected = it; scrollState.expand() },
            modifier = Modifier.fillMaxSize(),
            minimized = minimized, frostedGlass = frosted, automaticTone = autoTone,
            hasMedia = playback.hasMedia,
            miniPlayer = miniPlayer,
        ) {
            LazyColumn(
                Modifier.fillMaxSize().nestedScroll(minimizeConnection).background(bg),
                contentPadding = PaddingValues(top = 54.dp, bottom = 160.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                item {
                    Column(Modifier.padding(horizontal = 20.dp)) {
                        Text("MeloX 原版 TabBar", style = MaterialTheme.typography.headlineMedium)
                        Text("当前：${selected.name} · 拖动水珠可切换标签")
                        if (status.isNotEmpty()) Text(status)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(onClick = { minimized = !minimized }) { Text(if (minimized) "展开" else "收缩") }
                            Button(onClick = { dark = !dark }) { Text("切换深浅色") }
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            TextButton(onClick = { frosted = !frosted }) { Text(if (frosted) "改回液态" else "毛玻璃模式") }
                            TextButton(onClick = { autoTone = !autoTone }) { Text(if (autoTone) "关闭明暗采样" else "开启明暗采样") }
                        }
                    }
                }
                items((1..24).toList()) { index ->
                    val colors = if (index % 2 == 0) listOf(Color(0xFF193A60), Color(0xFF541660))
                        else listOf(Color(0xFFFFBD72), Color(0xFF82D3FA))
                    Box(Modifier.fillMaxWidth().height(120.dp).background(Brush.horizontalGradient(colors)).padding(20.dp)) {
                        Text("${selected.name} 内容 $index", color = if (index % 2 == 0) Color.White else Color.Black)
                    }
                }
            }
        }
        }
    }
}
