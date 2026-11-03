package com.lladlam.melox.ui.player

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.zIndex
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.lladlam.melox.playback.MeloXAudioReactiveSample
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch

enum class MeloXNowPlayingPage { Artwork, Lyrics, Queue }
internal val LocalMeloXForeground = staticCompositionLocalOf { true }
internal val LocalMeloXAudioSampler = staticCompositionLocalOf<(String?) -> MeloXAudioReactiveSample> {
    { MeloXAudioReactiveSample.Idle }
}

/** Original app's two-way player transition host. Put the supplied miniPlayer
 * into GlassTabBarScaffold.player; it must remain outside the background capture.
 * This extracts the Artwork transition, not accounts/lyrics/network playback.
 */
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun MeloXPlayerScaffold(
    state: MeloXPlaybackUiState,
    modifier: Modifier = Modifier,
    audioSample: (String?) -> MeloXAudioReactiveSample = {
        MeloXAudioReactiveSample.Idle.copy(isPlaying = state.isPlaying)
    },
    onShowActions: () -> Unit = {},
    content: @Composable (miniPlayer: @Composable (Float) -> Unit) -> Unit,
) {
    val playbackState = state
    val playerTransitionState = remember { SeekableTransitionState(false) }
    val playerTransition = rememberTransition(
        transitionState = playerTransitionState,
        label = "melox-player-transition",
    )
    val playerScope = rememberCoroutineScope()
    var playerTransitionJob by remember { mutableStateOf<Job?>(null) }
    var playerArtworkPageUsesScale by remember { mutableStateOf(false) }
    val openPlayer: () -> Unit = {
        if (playbackState.hasMedia) {
            playerTransitionJob?.cancel()
            playerTransitionJob = playerScope.launch {
                playerTransitionState.animateTo(targetState = true, animationSpec = playerAutomaticFractionSpec())
            }
        }
    }
    val closePlayer: () -> Unit = {
        playerTransitionJob?.cancel()
        playerTransitionJob = playerScope.launch {
            playerTransitionState.animateTo(targetState = false, animationSpec = playerAutomaticFractionSpec())
        }
    }
    LaunchedEffect(playbackState.hasMedia) {
        if (!playbackState.hasMedia) playerTransitionState.snapTo(false)
    }
    DisposableEffect(Unit) { onDispose { playerTransitionJob?.cancel() } }
    val lifecycle by LocalLifecycleOwner.current.lifecycle.currentStateFlow.collectAsState()
    CompositionLocalProvider(LocalMeloXForeground provides lifecycle.isAtLeast(Lifecycle.State.STARTED),
        LocalMeloXAudioSampler provides audioSample) {
        SharedTransitionLayout(modifier = modifier.fillMaxSize()) {
            val sharedScope = this
            content { compactProgress ->
                playerTransition.AnimatedVisibility(
                    visible = { value -> !value }, enter = EnterTransition.None, exit = ExitTransition.None,
                ) {
                    MeloXIOSMiniPlayer(state = playbackState, onExpand = openPlayer,
                        compactProgress = compactProgress, dynamicGlassEnabled = true,
                        sharedTransitionScope = sharedScope, animatedVisibilityScope = this,
                        applyPlayerArtworkScale = playerArtworkPageUsesScale, audioSample = audioSample)
                }
            }
            playerTransition.AnimatedVisibility(
                visible = { value -> value }, enter = EnterTransition.None, exit = ExitTransition.None,
                modifier = Modifier.fillMaxSize().zIndex(20f),
            ) {
                val fullPlayerAnimatedVisibilityScope = this
                // Original host reports its Artwork destination to the mini source.
                LaunchedEffect(Unit) { playerArtworkPageUsesScale = true }
                Box(Modifier.fillMaxSize().clickable(
                    interactionSource = remember { MutableInteractionSource() }, indication = null, onClick = {},
                )) {
                    MeloXIOSNowPlayingSharedHost(
                        state = playbackState, onDismiss = closePlayer,
                        onSeekCollapse = { fraction ->
                            playerTransitionJob?.cancelAndJoin()
                            playerTransitionJob = null
                            playerTransitionState.seekTo(fraction = fraction.coerceIn(0f, 0.999f), targetState = false)
                        },
                        onSettleCollapse = { collapse ->
                            playerTransitionJob?.cancelAndJoin()
                            playerTransitionJob = null
                            playerTransitionState.animateTo(targetState = !collapse, animationSpec = playerGestureSettleSpec())
                        },
                        sharedTransitionScope = sharedScope,
                        animatedVisibilityScope = fullPlayerAnimatedVisibilityScope,
                    ) {
                        Column(Modifier.fillMaxSize().meloXPlayerStatusBarsPadding()
                            .navigationBarsPadding().padding(horizontal = 32.dp)) {
                            SceneGrabber(onDismiss = closePlayer, dragModifier = Modifier)
                            Box(Modifier.weight(1f).fillMaxWidth()) {
                                ArtworkDetailsWithoutArtwork(playbackState, onShowActions)
                            }
                        }
                        Column(Modifier.align(Alignment.BottomCenter).navigationBarsPadding()
                            .padding(horizontal = 32.dp).height(MeloXNowPlayingControlsHeight.dp)) {
                            SceneProgressControl(playbackState, {})
                            Spacer(Modifier.height(19.dp))
                            SceneTransportControls(playbackState)
                            Spacer(Modifier.height(31.dp))
                            SceneVolumeControl(playbackState)
                        }
                    }
                }
            }
        }
    }
}
