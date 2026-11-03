@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
package com.lladlam.melox.ui.player
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.*
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import com.lladlam.melox.ui.glass.*
import com.lladlam.melox.ui.settings.MeloXSettingsRuntime
import kotlin.math.roundToLong

internal const val MeloXNowPlayingControlsHeight = 279
@Composable
internal fun SceneGrabber(
    onDismiss: () -> Unit,
    dragModifier: Modifier,
) {
    val interaction = androidx.compose.runtime.remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.88f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium,
        ),
        label = "scene-grabber-scale",
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(30.dp)
            .then(dragModifier)
            .clickable(
                interactionSource = interaction,
                indication = null,
                onClick = onDismiss,
            ),
        contentAlignment = Alignment.TopCenter,
    ) {
        Box(
            modifier = Modifier
                .padding(top = 8.dp)
                .size(width = 60.dp, height = 5.dp)
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                }
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.52f)),
        )
    }
}

@Composable
internal fun ArtworkDetailsWithoutArtwork(
    state: MeloXPlaybackUiState,
    onShowActions: () -> Unit,
) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val artworkSize = maxOf(
            170.dp,
            minOf(maxWidth, maxHeight - 92.dp),
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.weight(1f))
            Spacer(Modifier.size(artworkSize))
            Spacer(Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = state.transitionDisplayTitle(),
                        color = Color.White,
                        fontSize = 20.sp,
                        lineHeight = 24.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = state.transitionDisplayArtist(),
                        color = Color.White.copy(alpha = 0.64f),
                        fontSize = 20.sp,
                        lineHeight = 24.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(top = 2.dp),
                    )
                }
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .clickable(onClick = onShowActions),
                    contentAlignment = Alignment.Center,
                ) {
                    MeloXSymbolIcon(MeloXSymbol.Ellipsis, Modifier.size(22.dp), Color.White)
                }
            }

            Spacer(Modifier.height(8.dp))
            Spacer(Modifier.height(MeloXNowPlayingControlsHeight.dp))
        }
    }
}

private fun MeloXPlaybackUiState.transitionDisplayTitle(): String =
    if (isInTransition) incomingTitle.ifBlank { title.ifBlank { "正在播放" } }
    else title.ifBlank { "正在播放" }

private fun MeloXPlaybackUiState.transitionDisplayArtist(): String =
    if (isInTransition) incomingArtist.ifBlank { artist } else artist
@Composable
internal fun SceneProgressControl(
    state: MeloXPlaybackUiState,
    onShowQuality: () -> Unit,
) {
    val displayedDurationMs = if (state.isInTransition && state.incomingDurationMs > 0L) {
        state.incomingDurationMs
    } else state.durationMs
    val displayedPositionMs = if (state.isInTransition) state.incomingPositionMs else state.positionMs
    val sourceProgress = if (displayedDurationMs > 0L) {
        (displayedPositionMs.toFloat() / displayedDurationMs.toFloat()).coerceIn(0f, 1f)
    } else {
        0f
    }
    var scrubbing by remember(state.mediaId) { mutableStateOf(false) }
    var localProgress by remember(state.mediaId) { mutableFloatStateOf(sourceProgress) }
    var scrubMediaId by remember(state.mediaId) { mutableStateOf<String?>(null) }
    var scrubDurationMs by remember(state.mediaId) { mutableLongStateOf(0L) }
    val trackHeight by animateDpAsState(
        targetValue = if (scrubbing) 6.dp else 4.dp,
        animationSpec = tween(120),
        label = "scene-progress-track-height",
    )

    LaunchedEffect(sourceProgress, scrubbing) {
        if (!scrubbing) localProgress = sourceProgress
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        Slider(
            enabled = !state.isInTransition,
            value = localProgress,
            onValueChange = {
                if (!scrubbing) {
                    scrubMediaId = state.mediaId
                    scrubDurationMs = state.durationMs
                }
                scrubbing = true
                localProgress = it.coerceIn(0f, 1f)
            },
            onValueChangeFinished = {
                if (scrubMediaId == state.mediaId && scrubDurationMs > 0L) {
                    state.seekTo((scrubDurationMs * localProgress).roundToLong())
                }
                scrubbing = false
                scrubMediaId = null
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp),
            thumb = { Spacer(Modifier.size(0.dp)) },
            track = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(trackHeight)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.20f)),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(localProgress)
                            .fillMaxHeight()
                            .background(Color.White.copy(alpha = 0.96f)),
                    )
                }
            },
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(26.dp),
        ) {
            val shownPosition = if (scrubbing) {
                (state.durationMs * localProgress).roundToLong()
            } else {
                displayedPositionMs
            }
            Text(
                text = sceneFormatDuration(shownPosition),
                modifier = Modifier.align(Alignment.CenterStart),
                color = Color.White.copy(alpha = 0.50f),
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Medium,
            )

            if (MeloXSettingsRuntime.showPlayerQualityTip) {
                // Music-service quality label is supplied by the host, not bundled.
            }

            Text(
                text = "−${sceneFormatDuration((displayedDurationMs - shownPosition).coerceAtLeast(0L))}",
                modifier = Modifier.align(Alignment.CenterEnd),
                color = Color.White.copy(alpha = 0.50f),
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Medium,
            )
        }
    }
}

@Composable
internal fun SceneTransportControls(state: MeloXPlaybackUiState) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(82.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Spacer(Modifier.weight(1f))
        SceneTransportButton(
            kind = SceneGlyphKind.Backward,
            visualSize = 34.dp,
            onClick = {
                if (state.hasPrevious) state.previous() else state.seekTo(0L)
            },
        )
        Spacer(Modifier.weight(1f))
        ScenePlayPauseButton(state)
        Spacer(Modifier.weight(1f))
        SceneTransportButton(
            kind = SceneGlyphKind.Forward,
            visualSize = 34.dp,
            onClick = state::next,
        )
        Spacer(Modifier.weight(1f))
    }
}

@Composable
private fun ScenePlayPauseButton(state: MeloXPlaybackUiState) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.86f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = 620f,
        ),
        label = "scene-play-pause-press",
    )

    Box(
        modifier = Modifier
            .size(64.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clip(CircleShape)
            .clickable(
                interactionSource = interaction,
                indication = null,
                onClick = state::togglePlayPause,
            ),
        contentAlignment = Alignment.Center,
    ) {
        AnimatedContent(
            targetState = state.isPlaying,
            transitionSpec = {
                (
                    fadeIn(tween(180)) +
                        scaleIn(initialScale = 0.78f, animationSpec = tween(200)) +
                        slideInVertically(tween(200)) { (it * 0.24f).toInt() }
                    ) togetherWith (
                    fadeOut(tween(150)) +
                        scaleOut(targetScale = 0.78f, animationSpec = tween(180)) +
                        slideOutVertically(tween(180)) { -(it * 0.24f).toInt() }
                    )
            },
            label = "scene-play-pause-symbol-replace",
        ) { playing ->
            SceneCupertinoGlyph(
                kind = if (playing) SceneGlyphKind.Pause else SceneGlyphKind.Play,
                modifier = Modifier.size(48.dp),
                color = Color.White,
            )
        }
    }
}

@Composable
private fun SceneTransportButton(
    kind: SceneGlyphKind,
    visualSize: Dp,
    onClick: () -> Unit,
) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.84f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = 620f,
        ),
        label = "scene-transport-press-${kind.name}",
    )

    Box(
        modifier = Modifier
            .size(64.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clip(CircleShape)
            .clickable(
                interactionSource = interaction,
                indication = null,
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center,
    ) {
        SceneCupertinoGlyph(
            kind = kind,
            modifier = Modifier.size(visualSize),
            color = Color.White,
        )
    }
}

@Composable
internal fun SceneVolumeControl(state: MeloXPlaybackUiState) {
    var dragging by remember { mutableStateOf(false) }
    var localVolume by remember { mutableFloatStateOf(state.volume) }
    val thumbSize by animateDpAsState(
        targetValue = if (dragging) 16.dp else 14.dp,
        animationSpec = tween(120),
        label = "scene-volume-thumb-size",
    )
    val trackHeight by animateDpAsState(
        targetValue = if (dragging) 4.dp else 3.dp,
        animationSpec = tween(120),
        label = "scene-volume-track-height",
    )

    LaunchedEffect(state.volume, dragging) {
        if (!dragging) localVolume = state.volume
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(42.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        SceneCupertinoGlyph(
            kind = SceneGlyphKind.SpeakerLow,
            modifier = Modifier.size(12.dp),
            color = Color.White.copy(alpha = 0.62f),
        )

        Slider(
            value = localVolume,
            onValueChange = {
                dragging = true
                localVolume = it.coerceIn(0f, 1f)
                state.changeVolume(localVolume)
            },
            onValueChangeFinished = { dragging = false },
            modifier = Modifier
                .weight(1f)
                .height(32.dp),
            thumb = {
                Box(
                    modifier = Modifier
                        .size(thumbSize)
                        .shadow(3.dp, CircleShape)
                        .clip(CircleShape)
                        .background(Color.White),
                )
            },
            track = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(trackHeight)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.20f)),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(localVolume)
                            .fillMaxHeight()
                            .background(Color.White.copy(alpha = 0.82f)),
                    )
                }
            },
        )

        SceneCupertinoGlyph(
            kind = SceneGlyphKind.SpeakerHigh,
            modifier = Modifier.size(15.dp),
            color = Color.White.copy(alpha = 0.62f),
        )
    }
}

private enum class SceneGlyphKind {
    Backward,
    Forward,
    Play,
    Pause,
    SpeakerLow,
    SpeakerHigh,
    Lyrics,
    AirPlay,
    Queue,
    Waveform,
}

@Composable
private fun SceneCupertinoGlyph(
    kind: SceneGlyphKind,
    modifier: Modifier,
    color: Color,
) {
    val sfSymbol = when (kind) {
        SceneGlyphKind.Backward -> MeloXSymbol.Previous
        SceneGlyphKind.Forward -> MeloXSymbol.Next
        SceneGlyphKind.Play -> MeloXSymbol.Play
        SceneGlyphKind.Pause -> MeloXSymbol.Pause
        SceneGlyphKind.SpeakerLow -> MeloXSymbol.Volume
        SceneGlyphKind.SpeakerHigh -> MeloXSymbol.Volume
        SceneGlyphKind.Queue -> MeloXSymbol.Queue
        SceneGlyphKind.Lyrics -> MeloXSymbol.Lyrics
        SceneGlyphKind.Waveform -> MeloXSymbol.AutoMix
        else -> null
    }
    if (sfSymbol != null) {
        MeloXSymbolIcon(
            symbol = sfSymbol,
            modifier = modifier,
            color = color,
            variant = if (kind == SceneGlyphKind.Play || kind == SceneGlyphKind.Pause) {
                com.lladlam.melox.ui.glass.MeloXSymbolVariant.Fill
            } else {
                com.lladlam.melox.ui.glass.MeloXSymbolVariant.Regular
            },
            iconSize = when (kind) {
                SceneGlyphKind.Play, SceneGlyphKind.Pause -> 48.sp
                SceneGlyphKind.Backward, SceneGlyphKind.Forward -> 34.sp
                SceneGlyphKind.Lyrics, SceneGlyphKind.Queue, SceneGlyphKind.Waveform -> 22.sp
                else -> 18.sp
            },
        )
        return
    }
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val min = size.minDimension
        val stroke = (min * 0.085f).coerceAtLeast(1.35f)

        when (kind) {
            SceneGlyphKind.Play -> {
                val p = Path().apply {
                    moveTo(w * 0.28f, h * 0.13f)
                    quadraticTo(w * 0.22f, h * 0.10f, w * 0.22f, h * 0.22f)
                    lineTo(w * 0.22f, h * 0.78f)
                    quadraticTo(w * 0.22f, h * 0.90f, w * 0.30f, h * 0.86f)
                    lineTo(w * 0.82f, h * 0.56f)
                    quadraticTo(w * 0.91f, h * 0.50f, w * 0.82f, h * 0.44f)
                    close()
                }
                drawPath(p, color)
            }

            SceneGlyphKind.Pause -> {
                drawRoundRect(
                    color = color,
                    topLeft = Offset(w * 0.24f, h * 0.10f),
                    size = Size(w * 0.18f, h * 0.80f),
                    cornerRadius = CornerRadius(w * 0.045f),
                )
                drawRoundRect(
                    color = color,
                    topLeft = Offset(w * 0.58f, h * 0.10f),
                    size = Size(w * 0.18f, h * 0.80f),
                    cornerRadius = CornerRadius(w * 0.045f),
                )
            }

            SceneGlyphKind.Backward,
            SceneGlyphKind.Forward -> {
                val forward = kind == SceneGlyphKind.Forward
                fun triangle(x0: Float, x1: Float) {
                    val p = Path()
                    if (forward) {
                        p.moveTo(x0, h * 0.17f)
                        p.lineTo(x1, h * 0.50f)
                        p.lineTo(x0, h * 0.83f)
                    } else {
                        p.moveTo(x1, h * 0.17f)
                        p.lineTo(x0, h * 0.50f)
                        p.lineTo(x1, h * 0.83f)
                    }
                    p.close()
                    drawPath(p, color)
                }
                triangle(w * 0.10f, w * 0.50f)
                triangle(w * 0.45f, w * 0.88f)
            }

            SceneGlyphKind.SpeakerLow,
            SceneGlyphKind.SpeakerHigh -> {
                val speaker = Path().apply {
                    moveTo(w * 0.08f, h * 0.41f)
                    lineTo(w * 0.29f, h * 0.41f)
                    lineTo(w * 0.54f, h * 0.22f)
                    quadraticTo(w * 0.58f, h * 0.19f, w * 0.58f, h * 0.27f)
                    lineTo(w * 0.58f, h * 0.73f)
                    quadraticTo(w * 0.58f, h * 0.81f, w * 0.54f, h * 0.78f)
                    lineTo(w * 0.29f, h * 0.59f)
                    lineTo(w * 0.08f, h * 0.59f)
                    close()
                }
                drawPath(speaker, color)

                if (kind == SceneGlyphKind.SpeakerHigh) {
                    drawArc(
                        color = color,
                        startAngle = -46f,
                        sweepAngle = 92f,
                        useCenter = false,
                        topLeft = Offset(w * 0.45f, h * 0.30f),
                        size = Size(w * 0.30f, h * 0.40f),
                        style = Stroke(width = stroke, cap = StrokeCap.Round),
                    )
                    drawArc(
                        color = color,
                        startAngle = -48f,
                        sweepAngle = 96f,
                        useCenter = false,
                        topLeft = Offset(w * 0.43f, h * 0.14f),
                        size = Size(w * 0.52f, h * 0.72f),
                        style = Stroke(width = stroke, cap = StrokeCap.Round),
                    )
                }
            }

            SceneGlyphKind.Lyrics -> {
                drawRoundRect(
                    color = color,
                    topLeft = Offset(w * 0.07f, h * 0.09f),
                    size = Size(w * 0.86f, h * 0.70f),
                    cornerRadius = CornerRadius(w * 0.18f),
                    style = Stroke(width = stroke, cap = StrokeCap.Round),
                )
                val tail = Path().apply {
                    moveTo(w * 0.61f, h * 0.78f)
                    lineTo(w * 0.52f, h * 0.93f)
                    lineTo(w * 0.72f, h * 0.79f)
                }
                drawPath(tail, color, style = Stroke(width = stroke, cap = StrokeCap.Round))

                drawRoundRect(
                    color = color,
                    topLeft = Offset(w * 0.28f, h * 0.31f),
                    size = Size(w * 0.10f, h * 0.18f),
                    cornerRadius = CornerRadius(w * 0.03f),
                )
                drawRoundRect(
                    color = color,
                    topLeft = Offset(w * 0.53f, h * 0.31f),
                    size = Size(w * 0.10f, h * 0.18f),
                    cornerRadius = CornerRadius(w * 0.03f),
                )
                drawLine(
                    color = color,
                    start = Offset(w * 0.28f, h * 0.49f),
                    end = Offset(w * 0.23f, h * 0.58f),
                    strokeWidth = stroke,
                    cap = StrokeCap.Round,
                )
                drawLine(
                    color = color,
                    start = Offset(w * 0.53f, h * 0.49f),
                    end = Offset(w * 0.48f, h * 0.58f),
                    strokeWidth = stroke,
                    cap = StrokeCap.Round,
                )
            }

            SceneGlyphKind.AirPlay -> {
                drawArc(
                    color = color,
                    startAngle = 205f,
                    sweepAngle = 130f,
                    useCenter = false,
                    topLeft = Offset(w * 0.08f, h * 0.02f),
                    size = Size(w * 0.84f, h * 0.72f),
                    style = Stroke(width = stroke, cap = StrokeCap.Round),
                )
                drawArc(
                    color = color,
                    startAngle = 205f,
                    sweepAngle = 130f,
                    useCenter = false,
                    topLeft = Offset(w * 0.24f, h * 0.18f),
                    size = Size(w * 0.52f, h * 0.44f),
                    style = Stroke(width = stroke, cap = StrokeCap.Round),
                )
                val output = Path().apply {
                    moveTo(w * 0.50f, h * 0.50f)
                    lineTo(w * 0.78f, h * 0.92f)
                    lineTo(w * 0.22f, h * 0.92f)
                    close()
                }
                drawPath(output, color)
            }

            SceneGlyphKind.Queue -> {
                listOf(0.27f, 0.50f, 0.73f).forEach { y ->
                    drawCircle(
                        color = color,
                        radius = stroke * 0.72f,
                        center = Offset(w * 0.17f, h * y),
                    )
                    drawLine(
                        color = color,
                        start = Offset(w * 0.33f, h * y),
                        end = Offset(w * 0.88f, h * y),
                        strokeWidth = stroke,
                        cap = StrokeCap.Round,
                    )
                }
            }

            SceneGlyphKind.Waveform -> {
                val xs = listOf(0.18f, 0.38f, 0.60f, 0.82f)
                val heights = listOf(0.36f, 0.72f, 0.55f, 0.30f)
                xs.zip(heights).forEach { (x, fraction) ->
                    val half = h * fraction * 0.5f
                    drawLine(
                        color = color,
                        start = Offset(w * x, h * 0.5f - half),
                        end = Offset(w * x, h * 0.5f + half),
                        strokeWidth = stroke,
                        cap = StrokeCap.Round,
                    )
                }
            }
        }
    }
}

private fun sceneFormatDuration(milliseconds: Long): String {
    val seconds = milliseconds.coerceAtLeast(0L) / 1_000L
    return "%d:%02d".format(seconds / 60L, seconds % 60L)
}
