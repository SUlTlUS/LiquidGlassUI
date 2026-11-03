import test from 'node:test';
import assert from 'node:assert/strict';
import fs from 'node:fs';
import path from 'node:path';
import crypto from 'node:crypto';
import { fileURLToPath } from 'node:url';

const root = path.resolve(path.dirname(fileURLToPath(import.meta.url)), '..');
const read = file => fs.readFileSync(path.join(root, file), 'utf8');
const prefix = 'melox-tabbar/src/main/kotlin/com/lladlam/melox/';
const manifest = JSON.parse(read('docs/melox-tabbar-extracted/manifest.json'));
test('extracted source and binary assets match their recorded hashes', () => {
  for (const file of manifest.extractedFiles) {
    const bytes = fs.readFileSync(path.join(root, file.path));
    assert.equal(crypto.createHash('sha256').update(bytes).digest('hex'), file.sha256, file.path);
  }
});
test('complete chrome is identical after only the recorded API adaptations', () => {
  const original = read('docs/melox-tabbar-extracted/source/ui/MeloXApp.kt').replaceAll('\r\n', '\n');
  const output = read(prefix + 'ui/MeloXBottomChrome.kt');
  const start = 'internal fun MeloXBottomChrome(';
  const stop = '\n@Composable\nprivate fun RowScope.RootTabButton';
  const extractedBody = output.slice(output.indexOf(start), output.indexOf(stop));
  const originalBody = original.slice(original.indexOf('private fun MeloXBottomChrome('), original.indexOf(stop))
    .replace('private fun MeloXBottomChrome(', start)
    .replace('    source: MusicSource,\n', '')
    .replaceAll('.titleFor(source)', '.titleFor()')
    .replace('val searchContentDescription = stringResource(R.string.tab_search)', 'val searchContentDescription = AppTab.Search.titleFor()');
  assert.equal(extractedBody, originalBody);
});
test('all three glass renderer bodies are identical to upstream', () => {
  const marker = '/** Official LiquidBottomTabs-style outer panel.';
  const original = read('docs/melox-tabbar-extracted/source/ui/glass/MeloXBackdropComponents.kt').replaceAll('\r\n', '\n');
  const output = read(prefix + 'ui/glass/MeloXBackdropComponents.kt');
  assert.equal(output.slice(output.indexOf(marker)), original.slice(original.indexOf(marker)));
});
test('mini-player is original source except explicitly injected audio sampling', () => {
  const original = read('docs/melox-tabbar-extracted/source/ui/player/MeloXIOSMiniPlayer.kt').replaceAll('\r\n', '\n')
    .replace('import com.lladlam.melox.playback.MeloXAudioReactiveRuntime\n', '')
    .replace('    applyPlayerArtworkScale: Boolean = true,\n',
      '    applyPlayerArtworkScale: Boolean = true,\n    audioSample: (String?) -> MeloXAudioReactiveSample = { MeloXAudioReactiveSample.Idle },\n')
    .replace('MeloXAudioReactiveRuntime.sample(state.mediaId)', 'audioSample(state.mediaId)');
  assert.equal(read(prefix + 'ui/player/MeloXIOSMiniPlayer.kt'), original);
  for (const file of ['ui/player/MeloXPlayerTransitionSpec.kt', 'ui/player/MeloXPlayerTransitionKeys.kt']) {
    assert.equal(read(prefix + file), read('docs/melox-tabbar-extracted/source/' + file).replaceAll('\r\n', '\n'));
  }
  const glass = read('docs/melox-tabbar-extracted/source/ui/glass/MeloXBackdropComponents.kt').replaceAll('\r\n', '\n');
  const output = read(prefix + 'ui/glass/MeloXBackdropComponents.kt');
  for (const [start, stop] of [['/** Shared interaction state', '/**\n * Official LiquidButton'],
    ['/** Applies the same optical', '/** Shared material entry']]) {
    assert.ok(output.includes(glass.slice(glass.indexOf(start), glass.indexOf(stop))));
  }
});
test('drag, shader highlight, motion, icons and tone sampler are unchanged', () => {
  for (const file of ['ui/glass/publicdemo/PublicDampedDragAnimation.kt', 'ui/glass/publicdemo/PublicInteractiveHighlight.kt',
    'ui/animation/MeloXMotion.kt', 'ui/glass/MeloXSymbol.kt', 'ui/glass/BottomBarToneSampler.kt']) {
    assert.equal(read(prefix + file), read('docs/melox-tabbar-extracted/source/' + file).replaceAll('\r\n', '\n'), file);
  }
});
test('safe scaffold captures content before drawing its sibling bar', () => {
  const source = read(prefix + 'ui/MeloXTabBar.kt');
  assert.match(source, /Box\(Modifier.fillMaxSize\(\).layerBackdrop\(background\), content = content\)\s+MeloXTabBar\(/);
  assert.match(source, /onDispose \{ toneState.release\(\) \}/);
  assert.match(source, /LocalMeloXFrostedGlass provides frostedGlass/);
});
test('host catalog routes the extracted bar to its own live page without editing Figma assets', () => {
  const router = read('sample/src/main/java/dev/liquidglass/sample/IphoneExamples.kt');
  assert.match(router, /filterNot \{ it.category in HiddenPhoneExampleCategories \} \+ MeloXTabBarExampleEntry/);
  assert.match(router, /entry\?\.key == MeloXTabBarExampleKey[\s\S]*MeloXTabBarExamplePage\(onBack = popBack\)/);
  assert.match(router, /val categoryGroups[\s\S]*"Tab Bar",\s*"MeloX TabBar",/);
  assert.match(router, /entries.count \{ it.file.isNotBlank\(\) \}.*entries.count \{ it.file.isBlank\(\) \}.*个源码示例/);
  const page = read('sample/src/main/java/dev/liquidglass/sample/MeloXTabBarExamplePage.kt');
  assert.match(page, /Build.VERSION.SDK_INT < 26/);
  assert.match(page, /MeloXTabBarScaffold\(/);
  assert.match(page, /onTabSelected = \{ selected = it; tabBarScrollState.expand\(\) \}/);
  assert.match(page, /popupBackground\(popupBackground.image\)/);
  assert.match(page, /MeloXPlayerScaffold\(state = playback/);
  assert.match(page, /miniPlayer = miniPlayer/);
  assert.match(page, /nestedScroll\(tabBarMinimizeConnection\)/);
  assert.doesNotMatch(page, /Slot demonstration|播放器插槽|点击关闭/);
  assert.doesNotMatch(page, /FigmaSceneView|Ios27TabBarExample\(/);
});
test('original host fraction curves, gesture settling and shared artwork are carried over', () => {
  const app = read('docs/melox-tabbar-extracted/source/ui/MeloXApp.kt').replaceAll('\r\n', '\n');
  const host = read('docs/melox-tabbar-extracted/source/ui/player/MeloXIOSNowPlayingSharedHost.kt').replaceAll('\r\n', '\n');
  const output = read(prefix + 'ui/player/MeloXIOSNowPlayingSharedHost.kt');
  const curves = read(prefix + 'ui/player/MeloXPlayerFractionSpecs.kt');
  assert.ok(curves.includes(app.slice(app.indexOf('private fun playerAutomaticFractionSpec()'), app.indexOf('private fun smoothStep'))
    .replace('private fun playerAutomaticFractionSpec', 'internal fun playerAutomaticFractionSpec')
    .replace('private fun playerGestureSettleSpec', 'internal fun playerGestureSettleSpec')));
  for (const [start, stop] of [
    ['    val expansionProgress by animatedVisibilityScope', '    // NowPlaying owns'],
    ['        val dragRangePx', '        Box(\n            modifier = Modifier'],
    ['    val playbackScale by animateFloatAsState', '        // Transition overlay: dual cover slide animation'],
  ]) {
    const from = host.indexOf(start), to = host.indexOf(stop, from);
    assert.ok(output.includes(host.slice(from, to)), `Original fragment missing: ${start}`);
  }
  const wiring = read(prefix + 'ui/player/MeloXPlayerScaffold.kt');
  assert.match(wiring, /SharedTransitionLayout/);
  assert.match(wiring, /playerTransition\.AnimatedVisibility/g);
  assert.match(wiring, /sharedTransitionScope = sharedScope, animatedVisibilityScope = this/);
  assert.match(wiring, /playerTransitionState\.seekTo\(fraction = fraction.coerceIn\(0f, 0.999f\), targetState = false\)/);
  assert.doesNotMatch(wiring, /status\s*=/);
});
test('automatic scroll shrink is the original 18px direction accumulator, not an onScroll approximation', () => {
  const app = read('docs/melox-tabbar-extracted/source/ui/MeloXApp.kt').replaceAll('\r\n', '\n');
  const original = app.slice(app.indexOf('    val tabBarMinimizeConnection = remember {'), app.indexOf('\n    LaunchedEffect(openNowPlayingRequest'))
    .replace('val tabBarMinimizeConnection = remember {', 'val tabBarMinimizeConnection = remember(this) {')
    .replace('tabBarMinimized = true', 'minimized = true').replace('tabBarMinimized = false', 'minimized = false');
  assert.ok(read(prefix + 'ui/MeloXTabBarScrollState.kt').includes(original));
});

test('play/pause transport animation and artwork springs are original source fragments', () => {
  const core = read('docs/melox-tabbar-extracted/source/ui/player/MeloXNowPlayingCoreControls.kt').replaceAll('\r\n', '\n');
  const parts = read(prefix + 'ui/player/MeloXArtworkSceneParts.kt');
  const from = core.indexOf('@Composable\nprivate fun SceneTransportControls');
  const to = core.indexOf('@Composable\ninternal fun ScenePageSelector', from);
  assert.ok(parts.includes(core.slice(from, to)
    .replace('private fun SceneTransportControls', 'internal fun SceneTransportControls')
    .replace('private fun SceneVolumeControl', 'internal fun SceneVolumeControl')));
  const mini = read(prefix + 'ui/player/MeloXIOSMiniPlayer.kt');
  assert.match(mini, /applyPlayerArtworkScale/);
  const scaffold = read(prefix + 'ui/player/MeloXPlayerScaffold.kt');
  assert.match(scaffold, /playerArtworkPageUsesScale by remember \{ mutableStateOf\(false\) \}/);
  assert.match(scaffold, /LaunchedEffect\(Unit\) \{ playerArtworkPageUsesScale = true \}/);
  assert.match(scaffold, /Idle.copy\(isPlaying = state.isPlaying\)/);
});

test('flowing-light rendering changes only audio/foreground service injection', () => {
  const original = read('docs/melox-tabbar-extracted/source/ui/player/MeloXFlowingLightBackdrop.kt').replaceAll('\r\n', '\n')
    .replace('import com.lladlam.melox.playback.MeloXAudioReactiveRuntime\n', '')
    .replace('import com.lladlam.melox.MeloXAppVisibility\n', '')
    .replace('import com.lladlam.melox.ui.settings.MeloXLyricsRenderingQuality', 'import com.lladlam.melox.ui.player.LocalMeloXAudioSampler\nimport com.lladlam.melox.ui.player.LocalMeloXForeground\nimport com.lladlam.melox.ui.settings.MeloXLyricsRenderingQuality')
    .replaceAll('val appInForeground = MeloXAppVisibility.isForeground', 'val appInForeground = LocalMeloXForeground.current')
    .replace('if (!MeloXAppVisibility.isForeground) continue', 'if (!appInForeground) continue')
    .replace('    var targetPalette by remember', '    val audioSampler = LocalMeloXAudioSampler.current\n    var targetPalette by remember')
    .replace('com.lladlam.melox.playback.MeloXAudioAnalysisLoad.isBusy', 'false /* UI-only host has no background audio analysis service */')
    .replace('MeloXAudioReactiveRuntime.sample(mediaId)', 'audioSampler(mediaId)');
  assert.equal(read(prefix + 'ui/player/MeloXFlowingLightBackdrop.kt'), original);
});
