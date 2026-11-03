package com.lladlam.melox.ui.player
import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.nestedscroll.*
import androidx.compose.ui.input.nestedscroll.NestedScrollSource.Companion.UserInput
import androidx.compose.ui.platform.*
import androidx.compose.ui.unit.*
import coil3.request.ImageRequest
import coil3.size.Precision
import com.kyant.backdrop.backdrops.*
import com.lladlam.melox.ui.glass.LocalMeloXBackdrop
import com.lladlam.melox.ui.settings.MeloXSettingsRuntime
import com.lladlam.melox.ui.layout.rememberMeloXWindowInfo
import kotlinx.coroutines.*
import kotlin.math.roundToInt

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun MeloXIOSNowPlayingSharedHost(
    state: MeloXPlaybackUiState,
    onDismiss: () -> Unit,
    onSeekCollapse: suspend (Float) -> Unit,
    onSettleCollapse: suspend (Boolean) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    content: @Composable () -> Unit,
) {
    val page = MeloXNowPlayingPage.Artwork
    val isLandscape = rememberMeloXWindowInfo().isLandscape
    var gestureCollapseProgress by remember { mutableFloatStateOf(0f) }
    var settleJob by remember { mutableStateOf<Job?>(null) }
    var seekJob by remember { mutableStateOf<Job?>(null) }
    val scope = rememberCoroutineScope()
    // Two distinct scenes avoid recursive glass sampling:
    // controls sample the flowing-light player scene; the actions overlay samples
    // the fully composed Now Playing scene from outside that recording layer.
    val playerControlBackdrop = rememberLayerBackdrop()
    val actionsBackdrop = rememberLayerBackdrop()

    val expansionProgress by animatedVisibilityScope.transition.animateFloat(
        transitionSpec = { meloXPlayerLinearFloatSpec() },
        label = "full-player-expansion-progress",
    ) { visibility ->
        if (visibility == EnterExitState.Visible) 1f else 0f
    }

    // Let the shared artwork establish the transition first. The background
    // and scene then take over on the same master timeline, which prevents a
    // black first frame from winning the z-order over MiniPlay.
    // Compose the player backdrop from the first frame. Delaying this alpha
    // leaves the shared container showing a black surface during the first half
    // of the expansion even though the destination is already present.
    val backdropAlpha = 1f
    val isolationAlpha = if (MeloXSettingsRuntime.playerBackgroundIsolationEnabled) {
        smoothStep(expansionProgress, 0.34f, 0.82f)
    } else {
        0f
    }
    val fullPlayerAlpha = smoothStep(expansionProgress, 0.04f, 0.52f)
    val collapseProgress = (1f - expansionProgress).coerceIn(0f, 1f)
    val latestCollapseProgress = rememberUpdatedState(collapseProgress)
    val latestPage = rememberUpdatedState(page)
    DisposableEffect(Unit) {
        onDispose {
            seekJob?.cancel()
            settleJob?.cancel()
        }
    }
    val cornerRadius = (32f * smoothStep(collapseProgress, 0f, 1f)).dp
    val sharedShellModifier = with(sharedTransitionScope) {
        Modifier.sharedBounds(
            sharedContentState = rememberSharedContentState(key = sharedPlayerShellKey()),
            animatedVisibilityScope = animatedVisibilityScope,
            enter = EnterTransition.None,
            exit = ExitTransition.None,
            boundsTransform = MeloXPlayerShellBoundsTransform,
            resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds,
        )
    }

    // NowPlaying owns the player-level Back handler. Child modal overlays are
    // composed later and temporarily disable this handler, so Back always unwinds
    // the topmost visual layer before the player itself is dismissed.
    BackHandler { onDismiss() }

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter,
    ) {
        val dragRangePx = (constraints.maxHeight * 0.86f).coerceAtLeast(1f)

        fun seekCollapseBy(delta: Float) {
            val old = gestureCollapseProgress
            val next = (old + delta / dragRangePx).coerceIn(0f, 0.999f)
            if (next == old) return
            gestureCollapseProgress = next
            seekJob?.cancel()
            seekJob = scope.launch(start = CoroutineStart.UNDISPATCHED) {
                onSeekCollapse(next)
            }
        }

        suspend fun beginCollapseDrag() {
            settleJob?.cancelAndJoin()
            settleJob = null
            seekJob?.cancelAndJoin()
            seekJob = null
            gestureCollapseProgress = latestCollapseProgress.value.coerceIn(0f, 0.999f)
            onSeekCollapse(gestureCollapseProgress)
        }

        fun settleFromCurrent(velocity: Float) {
            val releaseProgress = gestureCollapseProgress
            val shouldCollapse = releaseProgress >= 0.42f || velocity >= 1200f
            settleJob?.cancel()
            settleJob = scope.launch {
                seekJob?.cancelAndJoin()
                seekJob = null
                onSettleCollapse(shouldCollapse)
                if (!shouldCollapse) {
                    gestureCollapseProgress = 0f
                }
                settleJob = null
            }
        }

        val dragState = rememberDraggableState { delta -> seekCollapseBy(delta) }

        // Lyrics/Queue own scrollable content. Child scrolling wins normally;
        // leftover downward motion at the top edge begins player collapse. Once
        // collapse has started, this connection owns both directions so the user
        // can reverse the transition without lifting the finger.
        val alternatePageCollapseConnection = remember(dragRangePx) {
            object : NestedScrollConnection {
                override fun onPreScroll(
                    available: Offset,
                    source: NestedScrollSource,
                ): Offset {
                    if (source != UserInput || latestPage.value == MeloXNowPlayingPage.Artwork) {
                        return Offset.Zero
                    }
                    if (gestureCollapseProgress <= 0f) return Offset.Zero
                    seekCollapseBy(available.y)
                    return Offset(x = 0f, y = available.y)
                }

                override fun onPostScroll(
                    consumed: Offset,
                    available: Offset,
                    source: NestedScrollSource,
                ): Offset {
                    if (source != UserInput || latestPage.value == MeloXNowPlayingPage.Artwork) {
                        return Offset.Zero
                    }
                    if (available.y <= 0f && gestureCollapseProgress <= 0f) {
                        return Offset.Zero
                    }
                    if (gestureCollapseProgress <= 0f && available.y > 0f) {
                        settleJob?.cancel()
                        settleJob = null
                        gestureCollapseProgress = latestCollapseProgress.value.coerceIn(0f, 0.999f)
                    }
                    seekCollapseBy(available.y)
                    return Offset(x = 0f, y = available.y)
                }

                override suspend fun onPreFling(available: Velocity): Velocity {
                    if (latestPage.value == MeloXNowPlayingPage.Artwork ||
                        gestureCollapseProgress <= 0f
                    ) {
                        return Velocity.Zero
                    }
                    settleFromCurrent(available.y)
                    return available
                }
            }
        }

        // Alternate pages can always be dismissed by dragging the grabber even
        // when their list is currently scrolled away from the top.
        val alternateGrabberDragModifier = if (page != MeloXNowPlayingPage.Artwork) {
            Modifier.draggable(
                state = dragState,
                orientation = Orientation.Vertical,
                onDragStarted = { beginCollapseDrag() },
                onDragStopped = { velocity -> settleFromCurrent(velocity) },
            )
        } else {
            Modifier
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .layerBackdrop(actionsBackdrop),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .then(sharedShellModifier)
                    .clip(RoundedCornerShape(cornerRadius))
                    .nestedScroll(alternatePageCollapseConnection)
                    .draggable(
                        state = dragState,
                        orientation = Orientation.Vertical,
                        enabled = page == MeloXNowPlayingPage.Artwork,
                        onDragStarted = { beginCollapseDrag() },
                        onDragStopped = { velocity -> settleFromCurrent(velocity) },
                    ),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            // Keep the opaque fallback inside the shared
                            // surface. Putting it on the sharedBounds node
                            // makes the black fallback itself participate in
                            // the capsule-to-screen transform, which exposes
                            // four dark corners during expansion.
                            .then(
                                if (isolationAlpha > 0f) {
                                    Modifier.background(Color.Black.copy(alpha = isolationAlpha))
                                } else {
                                    Modifier
                                },
                            )
                            .layerBackdrop(playerControlBackdrop)
                            .graphicsLayer { alpha = backdropAlpha },
                ) {
                    MeloXFlowingLightBackdrop(mediaId = state.mediaId, artworkUrl = state.artworkUrl, isPlaying = expansionProgress > 0.72f)
                }

                CompositionLocalProvider(LocalMeloXBackdrop provides playerControlBackdrop) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer { alpha = fullPlayerAlpha },
                    ) {
                        content()
                    }
                }

            }
        }

        // Keep the artwork outside the player surface's clip and stacking
        // layer so the shared flight remains above the expanding backdrop.
        SharedArtworkDestination(state, page, expansionProgress, sharedTransitionScope = sharedTransitionScope, animatedVisibilityScope = animatedVisibilityScope)
    }
}
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun SharedArtworkDestination(
    state: MeloXPlaybackUiState,
    page: MeloXNowPlayingPage,
    expansionProgress: Float,
    hidden: Boolean = false,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
) {
    var transitionAnimationEligible by remember { mutableStateOf(false) }
    var previousTransitionActive by remember { mutableStateOf(false) }
    LaunchedEffect(state.isInTransition, page, MeloXSettingsRuntime.transitionUiEnabled) {
        if (state.isInTransition && !previousTransitionActive) {
            transitionAnimationEligible =
                page == MeloXNowPlayingPage.Artwork && MeloXSettingsRuntime.transitionUiEnabled
        } else if (state.isInTransition && page != MeloXNowPlayingPage.Artwork) {
            transitionAnimationEligible = false
        } else if (!state.isInTransition) {
            transitionAnimationEligible = false
        }
        previousTransitionActive = state.isInTransition
    }
    val isLandscape = rememberMeloXWindowInfo().isLandscape
    // Upstream keeps one artwork alive and animates its frame for 0.48s when
    // switching between artwork and alternate pages.
    val headerProgress by animateFloatAsState(
        targetValue = if (page == MeloXNowPlayingPage.Artwork) 0f else 1f,
        animationSpec = tween(
            durationMillis = 480,
            easing = FastOutSlowInEasing,
        ),
        label = "persistent-artwork-page-frame",
    )

    val playbackScale by animateFloatAsState(
        targetValue = if (MeloXSettingsRuntime.reduceMotion || !MeloXSettingsRuntime.artworkMotionEnabled || state.isPlaying) 1f else 0.74f,
        animationSpec = if (state.isPlaying) {
            spring(
                dampingRatio = 0.70f,
                stiffness = 280f,
                visibilityThreshold = 0.001f,
            )
        } else {
            spring(
                dampingRatio = 0.94f,
                stiffness = 360f,
                visibilityThreshold = 0.001f,
            )
        },
        label = "shared-artwork-playback-scale",
    )
    val shadowElevation by animateDpAsState(
        targetValue = if (state.isPlaying) 26.dp else 14.dp,
        animationSpec = spring(
            dampingRatio = 0.92f,
            stiffness = 320f,
        ),
        label = "shared-artwork-shadow",
    )

    val pageFrameProgress = if (isLandscape) 0f else headerProgress
    val fullScreenScaleBlend = smoothStep(expansionProgress, 0.30f, 0.88f)
    val effectiveScale = 1f +
        (playbackScale - 1f) * fullScreenScaleBlend * (1f - pageFrameProgress)

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .meloXPlayerStatusBarsPadding()
            .padding(horizontal = if (isLandscape) 20.dp else 32.dp),
    ) {
        val contentTop = 30.dp
        val portraitContentHeight = (maxHeight - contentTop - MeloXNowPlayingControlsHeight.dp)
            .coerceAtLeast(1.dp)
        val fullArtworkSize = if (isLandscape) {
            minOf(460.dp, (maxHeight - 42.dp).coerceAtLeast(1.dp), maxWidth * .43f).coerceAtLeast(1.dp)
        } else {
            minOf(maxWidth, portraitContentHeight - 92.dp).coerceAtLeast(1.dp)
        }
        val context = LocalContext.current
        val density = LocalDensity.current.density
        val fullArtworkSizePx = fullArtworkRequestSizePx(fullArtworkSize.value, density)
        val fullArtworkModel = remember(context, state.artworkUrl, fullArtworkSizePx) {
            state.artworkUrl?.takeIf(String::isNotBlank)?.let { artworkUrl ->
                ImageRequest.Builder(context)
                    .data(artworkUrl)
                    .size(fullArtworkSizePx, fullArtworkSizePx)
                    .precision(Precision.INEXACT)
                    .build()
            }
        }
        val artworkTitleGap = 20.dp
        val titleBlockHeight = 58.dp
        val fullX = if (isLandscape) {
            ((maxWidth * .43f - fullArtworkSize) / 2f).coerceAtLeast(0.dp)
        } else {
            ((maxWidth - fullArtworkSize) / 2f).coerceAtLeast(0.dp)
        }
        val rawFullY = if (isLandscape) {
            contentTop + ((maxHeight - contentTop - fullArtworkSize) / 2f).coerceAtLeast(0.dp)
        } else {
            // Center the artwork + title above the controls. Bottom-aligning pushed every
            // spare pixel above the (width-capped) square cover, so on tall displays the
            // cover read as sitting too low.
            val artworkBlockHeight = fullArtworkSize + artworkTitleGap + titleBlockHeight
            contentTop + ((portraitContentHeight - artworkBlockHeight) / 2f).coerceAtLeast(0.dp)
        }
        val fullY = rawFullY.coerceIn(
            contentTop,
            (maxHeight - fullArtworkSize).coerceAtLeast(contentTop),
        )

        val targetSize = lerpDp(fullArtworkSize, 72.dp, pageFrameProgress)
        val targetX = lerpDp(fullX, 0.dp, pageFrameProgress)
        val targetY = lerpDp(fullY, contentTop, pageFrameProgress)
        val targetRadius = 12.dp
        val sharedArtworkModifier = with(sharedTransitionScope) {
            Modifier.sharedElement(
                sharedContentState = rememberSharedContentState(
                    key = sharedPlayerArtworkKey(state.mediaId),
                ),
                animatedVisibilityScope = animatedVisibilityScope,
                boundsTransform = MeloXArtworkBoundsTransform,
                renderInOverlayDuringTransition = true,
                zIndexInOverlay = 100f,
            )
        }

        Box(
            modifier = Modifier
                .offset(x = targetX, y = targetY)
                .size(targetSize)
                .then(sharedArtworkModifier)
                .graphicsLayer {
                    alpha = if (hidden || transitionAnimationEligible) 0f else 1f
                }
        ) {
            Artwork(
                url = state.artworkUrl,
                model = fullArtworkModel,
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        scaleX = effectiveScale
                        scaleY = effectiveScale
                    }
                    .shadow(
                        elevation = shadowElevation *
                            fullScreenScaleBlend *
                            (1f - pageFrameProgress * 0.55f),
                        shape = RoundedCornerShape(targetRadius),
                        clip = false,
                        ambientColor = Color.Black.copy(alpha = 0.28f),
                        spotColor = Color.Black.copy(alpha = 0.28f),
                    )
                    .clip(RoundedCornerShape(targetRadius)),
            )
        }


    }
}

internal fun fullArtworkRequestSizePx(fullArtworkSizeDp: Float, density: Float): Int =
    (fullArtworkSizeDp * density).roundToInt().coerceAtLeast(1)

private fun lerpDp(start: androidx.compose.ui.unit.Dp, end: androidx.compose.ui.unit.Dp, fraction: Float) =
    start + (end - start) * fraction.coerceIn(0f, 1f)

private fun smoothStep(value: Float, start: Float, end: Float): Float {
    if (end <= start) return if (value >= end) 1f else 0f
    val t = ((value - start) / (end - start)).coerceIn(0f, 1f)
    return t * t * (3f - 2f * t)
}
