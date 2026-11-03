import fs from 'node:fs';
import path from 'node:path';
import { fileURLToPath } from 'node:url';
import assert from 'node:assert/strict';
import { test } from 'node:test';
const root = path.resolve(path.dirname(fileURLToPath(import.meta.url)), '..');
const read = relative => fs.readFileSync(path.join(root, relative), 'utf8');
const bridge = read('liquidglass/src/main/java/dev/liquidglass/components/ColorOs17ProgressiveBlur.kt');
const entry = read('liquidglass/src/main/java/com/zeaze/tianyinwallpaper/ui/commom/ProgressiveBlurContent.kt');

test('uses the vendor background interface and never substitutes own-content blur', () => {
    assert.ok(bridge.includes('com.oplus.graphics.OplusRenderEffect'));
    assert.ok(bridge.includes('com.oplus.view.OplusViewBackgroundRenderEffect'));
    assert.ok(bridge.includes('createGradientBlurEffect'));
    assert.ok(bridge.includes('setBackground.invoke(null, null, view)'));
    for (const forbidden of ['.setRenderEffect(', 'setHiddenApiExemptions', '.layerBackdrop(', 'drawPlainBackdrop('])
        assert.ok(!bridge.includes(forbidden), forbidden);
});

test('default entry selects the native adapter without removing the original fallback', () => {
    assert.ok(entry.includes('ColorOs17ProgressiveBlurOrFallback'));
    assert.ok(entry.includes('FallbackProgressiveBlurContent'));
    assert.ok(entry.includes('blur(16.dp.toPx())'));
    assert.ok(entry.includes('1.0 - smoothstep(0.0, 1.0, edge)'));
    assert.ok(entry.includes('strength: Float = 1f'));
    assert.match(read('liquidglass/build.gradle.kts'), /minSdk = 33/);
    assert.ok(bridge.includes('ColorOs17BlurPolicy.isColorOs17'));
    assert.ok(bridge.includes('Dispatchers.IO'));
});

test('switch observation, cleanup, and real API failure are fail-safe', () => {
    for (const required of ['system_material_blur_enable', 'disable_window_blurs',
        'persist.sys.oplus.anim_level', 'unregisterContentObserver(observer)',
        'onRelease = { it.release() }', 'reportActive(false)', 'catch (error: LinkageError)',
        'check(isHardwareAccelerated)', 'onFailure = { failed = true }'])
        assert.ok(bridge.includes(required), required);
});

test('host fades the native View independently from the blur kernel', () => {
    const host = read('sample/src/main/java/dev/liquidglass/sample/ExamplePageScaffold.kt');
    const call = host.slice(host.indexOf('if (showBlur)'), host.indexOf('if (toolbarFile != null)'));
    assert.ok(call.includes('visibility = blurAlpha'));
    assert.ok(!call.includes('.alpha('));
    assert.ok(bridge.includes('positions.reversedArray().map { 1f - it }'));
    assert.ok(bridge.includes('alpha = visibility'));
    assert.ok(call.includes('surfaceColor = surfaceColor'));
    assert.ok(call.includes('appBarHeight = appBarHeight'));
});

test('shared recording shells propagate actual background and root hosts use the official reveal distance', () => {
    const host = read('sample/src/main/java/dev/liquidglass/sample/ExamplePageScaffold.kt');
    assert.ok(host.includes('LocalProgressiveBlurSurface provides background'));
    assert.ok(entry.includes('surfaceColor: Color = LocalProgressiveBlurSurface.current'));
    for (const file of ['MainActivity.kt', 'NativeExamplePage.kt', 'IphoneExamples.kt']) {
        const text = read(`sample/src/main/java/dev/liquidglass/sample/${file}`);
        assert.ok(text.includes('30.dp.toPx()'), file);
        assert.ok(text.includes('scrollDriven = true'), file);
    }
});
