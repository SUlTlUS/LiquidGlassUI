// Mechanical extraction, not a replacement renderer. --check never writes files.
import fs from 'node:fs';
import path from 'node:path';
import crypto from 'node:crypto';
import { execFileSync } from 'node:child_process';
import { fileURLToPath } from 'node:url';

export const COMMIT = '723aa764462386440b76aed5df362dadf63a096f';
const root = path.resolve(path.dirname(fileURLToPath(import.meta.url)), '..');
const vendor = path.join(root, 'vendor/MeloX-Android');
const sourceBase = 'android/app/src/main/';
const kotlinBase = 'kotlin/com/lladlam/melox/';
const outputBase = 'melox-tabbar/src/main/';
const check = process.argv.includes('--check');
const sha = data => crypto.createHash('sha256').update(data).digest('hex');
const records = [];
const outputs = [];
function emit(relative, data) {
  const target = path.join(root, relative);
  if (check) {
    if (!fs.existsSync(target) || !fs.readFileSync(target).equals(Buffer.from(data))) {
      throw new Error(`Extraction drift: ${relative}`);
    }
  } else {
    fs.mkdirSync(path.dirname(target), { recursive: true });
    fs.writeFileSync(target, data);
  }
  outputs.push({ path: relative, sha256: sha(data) });
}
function original(relative) {
  const data = fs.readFileSync(path.join(vendor, relative));
  if (!records.some(r => r.path === relative)) records.push({ path: relative, sha256: sha(data) });
  return data;
}
function textSource(name) {
  const relative = sourceBase + kotlinBase + name;
  const data = original(relative);
  emit('docs/melox-tabbar-extracted/source/' + name, data);
  return data.toString('utf8').replaceAll('\r\n', '\n');
}
function between(text, start, end) {
  const from = text.indexOf(start);
  if (from < 0) throw new Error(`Missing start: ${start}`);
  const to = end ? text.indexOf(end, from + start.length) : text.length;
  if (to < 0) throw new Error(`Missing end: ${end}`);
  return text.slice(from, to);
}
function kotlin(name, text) { emit(outputBase + kotlinBase + name, text); }
export function extract() {
  const actual = execFileSync('git', ['-C', vendor, 'rev-parse', 'HEAD'], { encoding: 'utf8' }).trim();
  if (actual !== COMMIT) throw new Error(`Expected ${COMMIT}, found ${actual}; review upstream before refreshing.`);
  execFileSync('git', ['-C', vendor, 'diff', '--quiet', COMMIT, '--', 'android/app/src/main', 'LICENSE']);
  const app = textSource('ui/MeloXApp.kt');
  const imports = `package com.lladlam.melox.ui

import androidx.annotation.StringRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.res.*
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.compose.ui.util.lerp
import androidx.compose.ui.zIndex
import com.kyant.backdrop.backdrops.*
import com.kyant.shapes.Capsule
import com.lladlam.melox.R
import com.lladlam.melox.ui.animation.*
import com.lladlam.melox.ui.glass.*
import com.lladlam.melox.ui.glass.publicdemo.*
import com.lladlam.melox.ui.settings.MeloXSettingsRuntime
import kotlinx.coroutines.launch
import kotlin.math.*

`;
  const enumText = between(app, 'enum class AppTab', '\n\n\n@OptIn');
  const geometryStart = app.lastIndexOf('/**', app.indexOf('private val ChromeExpandedSize'));
  let chrome = between(app.slice(geometryStart), '/**', 'private fun playerAutomaticFractionSpec()');
  chrome = chrome.replace('private fun MeloXBottomChrome(', 'internal fun MeloXBottomChrome(')
    .replace('    source: MusicSource,\n', '')
    .replaceAll('.titleFor(source)', '.titleFor()')
    .replace('private fun AppTab.titleFor(source: MusicSource): String = stringResource(titleRes)',
      'private fun AppTab.titleFor(): String = LocalMeloXTabLabels.current[this] ?: stringResource(titleRes)')
    .replace('val searchContentDescription = stringResource(R.string.tab_search)',
      'val searchContentDescription = AppTab.Search.titleFor()');
  const iconAnchor = '    // The home tab uses the custom house vector';
  chrome = chrome
    .replace('    visibleRootTabs: List<AppTab>,\n', '    visibleRootTabs: List<AppTab>,\n    searchEnabled: Boolean = true,\n')
    .replace('                                selected = false,\n', '                                selected = false,\n                                semanticSelected = tab == selectedTab,\n')
    .replace('                if (compactLayerAlpha > 0.001f) {\n', '                if (compactLayerAlpha > 0.001f) {\n                    val compactTitle = selectedTab.titleFor()\n')
    .replace('                                    Modifier.clickable { onSelect(selectedTab) }',
      '                                    Modifier.clickable(role = Role.Tab) { onSelect(selectedTab) }\n' +
      '                                        .semantics {\n' +
      '                                            contentDescription = compactTitle\n' +
      '                                            selected = selectedTab != AppTab.Search\n' +
      '                                        }')
    .replace('.clickable(role = Role.Button) { onSelect(AppTab.Search) }', '.clickable(enabled = searchEnabled, role = Role.Button) { onSelect(AppTab.Search) }')
    .replace('    selected: Boolean,\n', '    selected: Boolean,\n    semanticSelected: Boolean = selected,\n')
    .replace('                        this.selected = selected', '                        this.selected = semanticSelected');
  chrome = chrome.replace(iconAnchor,
    '    LocalMeloXTabIcons.current[AppTab.valueOf(glyph.name)]?.let { customIcon ->\n' +
    '        customIcon(modifier, color, selected)\n        return\n    }\n' + iconAnchor);
  kotlin('ui/MeloXBottomChrome.kt', imports + enumText + '\n\n' + chrome + between(app, 'private fun smoothStep', null));

  const glass = textSource('ui/glass/MeloXBackdropComponents.kt');
  kotlin('ui/glass/MeloXBackdropComponents.kt',
    between(glass, '/*', '/** Shared interaction state') +
    between(glass, '/** Shared interaction state', '/**\n * Official LiquidButton') +
    between(glass, '/** Applies the same optical', '/** Shared material entry') +
    between(glass, '/** Official LiquidBottomTabs-style outer panel.', null));
  const mini = textSource('ui/player/MeloXIOSMiniPlayer.kt');
  kotlin('ui/player/MeloXIOSMiniPlayer.kt', mini
    .replace('import com.lladlam.melox.playback.MeloXAudioReactiveRuntime\n', '')
    .replace('    applyPlayerArtworkScale: Boolean = true,\n',
      '    applyPlayerArtworkScale: Boolean = true,\n    audioSample: (String?) -> MeloXAudioReactiveSample = { MeloXAudioReactiveSample.Idle },\n')
    .replace('MeloXAudioReactiveRuntime.sample(state.mediaId)', 'audioSample(state.mediaId)'));
  for (const name of ['ui/player/MeloXPlayerTransitionSpec.kt', 'ui/player/MeloXPlayerTransitionKeys.kt']) {
    kotlin(name, textSource(name));
  }
  const player = textSource('ui/player/MeloXPlayerUi.kt');
  kotlin('ui/player/MeloXQueueEntry.kt', 'package com.lladlam.melox.ui.player\n\n' +
    between(player, 'enum class MeloXQueueOrigin', '@Stable'));
  kotlin('ui/player/MeloXArtwork.kt', `package com.lladlam.melox.ui.player
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.*
import coil3.compose.AsyncImage
import com.lladlam.melox.R
import com.lladlam.melox.ui.glass.*

` + between(player, '@Composable\ninternal fun Artwork(', '\nprivate fun formatDuration'));
  const reactive = textSource('playback/MeloXAudioReactiveRuntime.kt');
  kotlin('playback/MeloXAudioReactiveSample.kt', 'package com.lladlam.melox.playback\n\n' +
    between(reactive, 'data class MeloXAudioReactiveSample', null));
  // Animation wiring lived in the app host, not inside the tab-bar renderer.
  const scrollConnection = between(app, '    val tabBarMinimizeConnection = remember {', '\n    LaunchedEffect(openNowPlayingRequest');
  kotlin('ui/MeloXTabBarScrollState.kt', `package com.lladlam.melox.ui
import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.*
import com.lladlam.melox.ui.settings.MeloXSettingsRuntime

@Stable
class MeloXTabBarScrollState internal constructor() {
    var minimized by mutableStateOf(false)
    internal var scrollAccumulator by mutableFloatStateOf(0f)
    fun expand() { minimized = false; scrollAccumulator = 0f }
}
@Composable
fun rememberMeloXTabBarScrollState() = remember { MeloXTabBarScrollState() }
@Composable
fun MeloXTabBarScrollState.nestedScrollConnection(): NestedScrollConnection {
` + scrollConnection.replace('val tabBarMinimizeConnection = remember {', 'val tabBarMinimizeConnection = remember(this) {')
    .replace('tabBarMinimized = true', 'minimized = true').replace('tabBarMinimized = false', 'minimized = false') +
    '    return tabBarMinimizeConnection\n}\n');
  kotlin('ui/player/MeloXPlayerFractionSpecs.kt', `package com.lladlam.melox.ui.player
import androidx.compose.animation.core.*

` + between(app, 'private fun playerAutomaticFractionSpec()', 'private fun smoothStep')
    .replace('private fun playerAutomaticFractionSpec', 'internal fun playerAutomaticFractionSpec')
    .replace('private fun playerGestureSettleSpec', 'internal fun playerGestureSettleSpec'));
  const host = textSource('ui/player/MeloXIOSNowPlayingSharedHost.kt');
  // Keep the entire reversible shell/drag implementation, replace music-app scene
  // and overlays with a caller content slot. Default destination is Artwork only.
  let sharedHost = between(host, '@OptIn(ExperimentalSharedTransitionApi::class)', '\n@OptIn(ExperimentalSharedTransitionApi::class)\n@Composable\nprivate fun SharedArtworkDestination');
  sharedHost = sharedHost.slice(0, sharedHost.indexOf('    // A track transition')) +
    '    val page = MeloXNowPlayingPage.Artwork\n    val isLandscape = rememberMeloXWindowInfo().isLandscape\n' +
    between(sharedHost, '    var gestureCollapseProgress', '    val hostView = LocalView.current') +
    between(sharedHost, '    // Two distinct scenes', null);
  sharedHost = sharedHost.replace(/    onNavigateSearch:[^\n]*\n|    onOpenPlaybackSettings:[^\n]*\n|    onLocalMetadataChanged:[^\n]*\n|    onArtworkPageChanged:[^\n]*\n/g, '')
    .replace('    animatedVisibilityScope: AnimatedVisibilityScope,\n', '    animatedVisibilityScope: AnimatedVisibilityScope,\n    content: @Composable () -> Unit,\n')
    .replace('BackHandler(enabled = !showActions && !showQuality) {\n        if (showLandscapeSkyline) showLandscapeSkyline = false else onDismiss()\n    }', 'BackHandler { onDismiss() }');
  const backgroundStart = sharedHost.indexOf('                    if (\n                        page == MeloXNowPlayingPage.Lyrics');
  const backgroundEnd = sharedHost.indexOf('\n                }\n\n                CompositionLocalProvider', backgroundStart);
  sharedHost = sharedHost.slice(0, backgroundStart) +
    '                    MeloXFlowingLightBackdrop(mediaId = state.mediaId, artworkUrl = state.artworkUrl, isPlaying = expansionProgress > 0.72f)' + sharedHost.slice(backgroundEnd);
  const sceneStart = sharedHost.indexOf('                        if (MeloXSettingsRuntime.playerShell');
  const sceneEnd = sharedHost.indexOf('\n                    }\n                }', sceneStart);
  sharedHost = sharedHost.slice(0, sceneStart) + '                        content()' + sharedHost.slice(sceneEnd);
  const artworkStart = sharedHost.indexOf('        if (MeloXSettingsRuntime.playerShell');
  const artworkEnd = sharedHost.indexOf('\n        CompositionLocalProvider(LocalMeloXBackdrop provides actionsBackdrop)', artworkStart);
  sharedHost = sharedHost.slice(0, artworkStart) +
    '        SharedArtworkDestination(state, page, expansionProgress, sharedTransitionScope = sharedTransitionScope, animatedVisibilityScope = animatedVisibilityScope)\n' +
    '    }\n}\n';
  const destination = between(host, '@OptIn(ExperimentalSharedTransitionApi::class)\n@Composable\nprivate fun SharedArtworkDestination', '\n@Composable\nprivate fun TransitionOverlay');
  // UI adapter has no service crossfade; leave the original guarded branch valid.
  kotlin('ui/player/MeloXIOSNowPlayingSharedHost.kt', `package com.lladlam.melox.ui.player
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

` + sharedHost + destination.replace(/        \/\/ Transition overlay: dual cover slide animation[\s\S]*?\n    }\n}/, '\n    }\n}'));
  const scene = textSource('ui/player/MeloXIOSNowPlayingScene.kt');
  const core = textSource('ui/player/MeloXNowPlayingCoreControls.kt');
  kotlin('ui/player/MeloXArtworkSceneParts.kt', `@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
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
` + between(scene, '@Composable\nprivate fun SceneGrabber', 'private fun isDirectLyricsQueueTransition')
    .replace('private fun SceneGrabber', 'internal fun SceneGrabber')
    .replace('private fun ArtworkDetailsWithoutArtwork', 'internal fun ArtworkDetailsWithoutArtwork') +
    between(scene, 'private fun MeloXPlaybackUiState.transitionDisplayTitle', null) +
    between(core, '@Composable\nprivate fun SceneProgressControl', '@Composable\nprivate fun SceneQualityChip')
      .replace('private fun SceneProgressControl', 'internal fun SceneProgressControl')
      .replace('                SceneQualityChip(\n                    state = state,\n                    onShowQuality = onShowQuality,\n                    modifier = Modifier.align(Alignment.Center),\n                )', '                // Music-service quality label is supplied by the host, not bundled.') +
    between(core, '@Composable\nprivate fun SceneTransportControls', '@Composable\ninternal fun ScenePageSelector')
      .replace('private fun SceneTransportControls', 'internal fun SceneTransportControls')
      .replace('private fun SceneVolumeControl', 'internal fun SceneVolumeControl') +
    between(core, 'private enum class SceneGlyphKind', 'private fun AudioQualityTier.sceneTitle') +
    between(core, 'private fun sceneFormatDuration', null));
  const flowing = textSource('ui/player/MeloXFlowingLightBackdrop.kt');
  kotlin('ui/player/MeloXFlowingLightBackdrop.kt', flowing
    .replace('import com.lladlam.melox.playback.MeloXAudioReactiveRuntime\n', '')
    .replace('import com.lladlam.melox.MeloXAppVisibility\n', '')
    .replace('import com.lladlam.melox.ui.settings.MeloXLyricsRenderingQuality', 'import com.lladlam.melox.ui.player.LocalMeloXAudioSampler\nimport com.lladlam.melox.ui.player.LocalMeloXForeground\nimport com.lladlam.melox.ui.settings.MeloXLyricsRenderingQuality')
    .replaceAll('val appInForeground = MeloXAppVisibility.isForeground', 'val appInForeground = LocalMeloXForeground.current')
    .replace('if (!MeloXAppVisibility.isForeground) continue', 'if (!appInForeground) continue')
    .replace('    var targetPalette by remember', '    val audioSampler = LocalMeloXAudioSampler.current\n    var targetPalette by remember')
    .replace('com.lladlam.melox.playback.MeloXAudioAnalysisLoad.isBusy', 'false /* UI-only host has no background audio analysis service */')
    .replace('MeloXAudioReactiveRuntime.sample(mediaId)', 'audioSampler(mediaId)'));
  for (const name of ['ui/player/ArtworkDynamicPalette.kt', 'ui/player/MeloXPlayerInsets.kt', 'ui/layout/MeloXAdaptiveLayout.kt']) {
    kotlin(name, textSource(name).replace('com.lladlam.melox.core.network.MeloXHttpClient.shared', 'OkHttpClient()'));
  }
  const safety = textSource('ui/glass/MeloXGlassSafety.kt');
  kotlin('ui/glass/MeloXGlassSafety.kt', safety
    .replace('import com.lladlam.melox.core.diagnostics.MeloXCrashStore\n', '')
    .replace('    MeloXCrashStore.markGlassRendering()\n', ''));
  for (const name of [
    'ui/glass/BottomBarToneSampler.kt',
    'ui/glass/publicdemo/PublicDampedDragAnimation.kt',
    'ui/glass/publicdemo/PublicInteractiveHighlight.kt',
    'ui/animation/MeloXMotion.kt',
    'ui/glass/MeloXSymbol.kt',
  ]) kotlin(name, textSource(name));
  const tokens = textSource('ui/glass/MeloXGlassTokens.kt');
  kotlin('ui/glass/MeloXSystemColors.kt', 'package com.lladlam.melox.ui.glass\n\nimport androidx.compose.ui.graphics.Color\n\n' +
    between(tokens, '/** Semantic colors used', '/** Shared geometry'));
  const theme = textSource('ui/theme/MeloXTheme.kt');
  kotlin('ui/theme/MeloXTabBarColors.kt', `package com.lladlam.melox.ui.theme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance

` + between(theme, 'internal val LightColors', '// SF Pro cannot') + between(theme, '/**\n * Returns the app', null));
  for (const resource of ['font/sf_pro_subset.ttf', 'font/mi_lan_pro_vf.ttf', 'font/mi_lan_pro.xml',
    'drawable/melox_home_tab.xml', 'drawable/ic_melox_brand_gear.xml']) {
    emit(outputBase + 'res/' + resource, original(sourceBase + 'res/' + resource));
  }
  emit(outputBase + 'res/drawable-nodpi/melox_demo_artwork.png',
    original(sourceBase + 'res/mipmap-xxxhdpi/ic_launcher.png'));
  for (const locale of ['values', 'values-zh-rCN', 'values-zh-rTW', 'values-ja']) {
    const data = original(sourceBase + `res/${locale}/strings.xml`).toString('utf8');
    const strings = [...data.matchAll(/<string name="tab_(?:home|explore|library|podcasts|downloads|cloud|settings|search)">[^<]*<\/string>/g)].map(m => m[0]);
    if (strings.length !== 8) throw new Error(`Expected eight titles in ${locale}`);
    emit(outputBase + `res/${locale}/melox_tab_titles.xml`, '<?xml version="1.0" encoding="utf-8"?>\n<resources>\n' + strings.map(s => '    ' + s).join('\n') + '\n</resources>\n');
    const playerStrings = [...data.matchAll(/<string name="player_(?:now_playing|artwork|artwork_default)">[^<]*<\/string>/g)].map(m => m[0]);
    if (playerStrings.length !== 3) throw new Error(`Expected three player strings in ${locale}`);
    emit(outputBase + `res/${locale}/melox_player_strings.xml`, '<?xml version="1.0" encoding="utf-8"?>\n<resources>\n' + playerStrings.map(s => '    ' + s).join('\n') + '\n</resources>\n');
  }
  emit('melox-tabbar/LICENSE', original('LICENSE'));
  emit('melox-tabbar/LICENSE-AndroidLiquidGlass', fs.readFileSync(path.join(root, 'vendor/AndroidLiquidGlass/LICENSE')));
  emit(outputBase + 'resources/META-INF/melox-tabbar/LICENSE', original('LICENSE'));
  emit(outputBase + 'resources/META-INF/melox-tabbar/LICENSE-AndroidLiquidGlass', fs.readFileSync(path.join(root, 'vendor/AndroidLiquidGlass/LICENSE')));
  emit(outputBase + 'resources/META-INF/melox-tabbar/provenance.json', JSON.stringify({
    repository: 'https://github.com/lladlam/MeloX-Android', commit: COMMIT,
    sourceLicense: 'GPL-3.0', helpers: 'AndroidLiquidGlass/BiliNext Apache-2.0 (see source notices)',
    bundledThirdPartyFontRedistributionAuthorization: 'not verified; see docs/melox-tabbar.md before distribution',
  }, null, 2) + '\n');
  emit('docs/melox-tabbar-extracted/manifest.json', JSON.stringify({
    repository: 'https://github.com/lladlam/MeloX-Android', commit: COMMIT,
    originalFiles: records, extractedFiles: outputs,
    adaptations: [
      'Extract chrome and its used glass/color/theme helpers; omit app navigation and playback business code.',
      'Replace imports; make chrome internal; remove unused MusicSource argument; add optional label/icon providers.',
      'Expose searchEnabled; separate tab selection semantics from optical tint, and label the compact tab for accessibility. Geometry, shaders and springs are unchanged.',
      'Remove diagnostics-only crash-store call; preserve hardware gate and finite-value guards.',
      'Preference runtime is supplied by a per-instance CompositionLocal, not a music-app singleton.',
      'Mini-player renderer is copied intact except audio runtime is an injected sample callback; media service is replaced by an immutable UI state/callback adapter. Artwork and transition helpers remain original.',
      'Extract original scroll minimizer and reversible shared-bounds shell/gesture destination. Music services, lyrics/queue scenes and quality overlay are excluded; fullscreen content is a slot with original Artwork scene/transport helpers. Foreground and audio sampling are injected into unchanged FlowingLight renderer.',
      'Artwork palette HTTP uses a standalone OkHttpClient instead of the app account/DNS singleton; absent audio-analysis load service is idle. Default UI sampling reports playback isPlaying without inventing beat analysis.',
      'Copied text uses LF; binary resources are byte-identical. Renderer/animation bodies otherwise unchanged.',
    ],
  }, null, 2) + '\n');
  console.log(`MeloX ${COMMIT}: ${check ? 'verified' : 'extracted'} ${outputs.length} files.`);
}
if (process.argv[1] && path.resolve(process.argv[1]) === fileURLToPath(import.meta.url)) extract();
