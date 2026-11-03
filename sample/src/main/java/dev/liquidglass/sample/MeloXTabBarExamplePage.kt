package dev.liquidglass.sample

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.zeaze.tianyinwallpaper.backdrop.backdrops.layerBackdrop
import com.zeaze.tianyinwallpaper.ui.commom.ProgressiveBlurContent
import dev.liquidglass.ios27.LocalIos27PageColors
import dev.liquidglass.ios27.ios27AndroidPane
import com.lladlam.melox.ui.AppTab
import com.lladlam.melox.ui.MeloXTabBarScaffold
import com.lladlam.melox.ui.rememberMeloXTabBarScrollState
import com.lladlam.melox.ui.nestedScrollConnection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.lladlam.melox.ui.player.MeloXPlayerScaffold
import com.lladlam.melox.ui.player.MeloXPlaybackUiState
import com.lladlam.melox.ui.player.MeloXQueueEntry
import androidx.compose.material3.MaterialTheme as MeloXMaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme

/** Standalone catalog route; never nests the new bar inside its own capture layer. */
@Composable
internal fun MeloXTabBarExamplePage(onBack: () -> Unit) {
    val colors = LocalIos27PageColors.current
    val dark = !MaterialTheme.colors.isLight
    val topInset = WindowInsets.safeDrawing.asPaddingValues().calculateTopPadding()
    if (Build.VERSION.SDK_INT < 26) {
        Box(Modifier.fillMaxSize().background(colors.page)) {
            Ios27BackButton(onBack, null, Modifier.padding(start = 16.dp, top = topInset + 4.dp))
            Text("MeloX 原版组件需要 Android 8.0 或以上", Modifier.align(Alignment.Center), color = colors.label)
        }
        return
    }
    var selected by rememberSaveable { mutableStateOf(AppTab.Home) }
    val tabBarScrollState = rememberMeloXTabBarScrollState()
    var minimized by tabBarScrollState::minimized
    val tabBarMinimizeConnection = tabBarScrollState.nestedScrollConnection()
    var frosted by rememberSaveable { mutableStateOf(false) }
    var automaticTone by rememberSaveable { mutableStateOf(true) }
    var media by rememberSaveable { mutableStateOf(true) }
    var playing by rememberSaveable { mutableStateOf(false) }
    var trackIndex by rememberSaveable { mutableIntStateOf(0) }
    var position by rememberSaveable { mutableLongStateOf(0L) }
    var volume by rememberSaveable { mutableFloatStateOf(0.5f) }
    var status by rememberSaveable { mutableStateOf("") }
    val queue = remember {
        val artwork = "android.resource://dev.liquidglass.sample/${com.lladlam.melox.R.drawable.melox_demo_artwork}"
        listOf(
            MeloXQueueEntry(0, "demo-0", "demo-0", "空耳", "ナナツカゼ", artwork),
            MeloXQueueEntry(1, "demo-1", "demo-1", "下一首示例", "MeloX", artwork),
        )
    }
    val playback = MeloXPlaybackUiState(
        queue = queue, currentIndex = trackIndex, isPlaying = playing, repeatMode = 2,
        onTogglePlayPause = { playing = !playing; status = if (playing) "播放状态：播放" else "播放状态：暂停" },
        onNext = { trackIndex = (trackIndex + 1) % queue.size },
        onPrevious = { trackIndex = (trackIndex + queue.size - 1) % queue.size },
        positionMs = position, volume = volume,
        onSeek = { position = it }, onVolumeChange = { volume = it },
    )
    val popupBackground = rememberPopupBackground { status = it }
    // Existing back/blur use their own capture; no protected toolbar implementation changes.
    val backBackdrop = rememberExamplePageBackdrop(colors.page)
    val listState = androidx.compose.foundation.lazy.rememberLazyListState()
    Box(Modifier.fillMaxSize().semantics { contentDescription = "MeloX TabBar 独立示例" }
        .ios27AndroidPane("MeloX TabBar 独立示例")) {
        MeloXMaterialTheme(colorScheme = if (dark) darkColorScheme() else lightColorScheme()) {
            MeloXPlayerScaffold(state = playback, onShowActions = { status = "当前曲目：${playback.title}" }) { miniPlayer ->
            Box(Modifier.fillMaxSize()) {
            MeloXTabBarScaffold(
                modifier = Modifier.fillMaxSize(),
                selectedTab = selected,
                onTabSelected = { selected = it; tabBarScrollState.expand() },
                minimized = minimized, frostedGlass = frosted, automaticTone = automaticTone,
                hasMedia = media,
                miniPlayer = miniPlayer,
            ) {
                Box(Modifier.fillMaxSize().layerBackdrop(backBackdrop)
                    .background(colors.page).popupBackground(popupBackground.image)) {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize().nestedScroll(tabBarMinimizeConnection),
                        contentPadding = PaddingValues(start = 16.dp, end = 16.dp,
                            top = topInset + 62.dp, bottom = if (media) 180.dp else 120.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        item {
                            Column {
                                Text("MeloX TabBar", style = dev.liquidglass.ios27.Ios27Tokens.Type.LargeTitle, color = colors.label)
                                Text("当前：${selected.name} · 拖动水珠切换标签", color = colors.secondaryLabel)
                                if (status.isNotEmpty()) Text(status, color = colors.secondaryLabel)
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Button(onClick = { minimized = !minimized }) { Text(if (minimized) "展开" else "收缩") }
                                    TextButton(onClick = { frosted = !frosted }) { Text(if (frosted) "液态模式" else "毛玻璃模式") }
                                }
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    TextButton(onClick = { automaticTone = !automaticTone }) { Text(if (automaticTone) "关闭明暗采样" else "开启明暗采样") }
                                    TextButton(onClick = { media = !media }) { Text(if (media) "隐藏播放器" else "显示播放器") }
                                }
                            }
                        }
                        items((1..20).toList()) { index ->
                            val strip = if (index % 2 == 0) listOf(Color(0xFF173B61), Color(0xFF601A52))
                                else listOf(Color(0xFFFFBC72), Color(0xFF83D4FB))
                            Box(Modifier.fillMaxWidth().height(110.dp)
                                .background(Brush.horizontalGradient(strip)).padding(16.dp)) {
                                Text("${selected.name} · 内容 $index", color = if (index % 2 == 0) Color.White else Color.Black)
                            }
                        }
                    }
                }
            }
        // These overlays sample only non-glass content and stay outside both captures.
        ProgressiveBlurContent(
            backdrop = if (Build.VERSION.SDK_INT >= 33) backBackdrop else null,
            modifier = Modifier.align(Alignment.TopCenter).fillMaxWidth(),
            isLightTheme = !dark,
            appBarHeight = topInset + 48.dp,
            surfaceColor = Color.Black,
        )
        Ios27BackButton(onBack, if (Build.VERSION.SDK_INT >= 33) backBackdrop else null,
            Modifier.align(Alignment.TopStart).padding(start = 16.dp, top = topInset + 4.dp))
            }
            }
        }
    }
}
