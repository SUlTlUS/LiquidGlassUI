import test from 'node:test';
import assert from 'node:assert/strict';
import fs from 'node:fs';
import path from 'node:path';
import crypto from 'node:crypto';
const root = path.resolve(import.meta.dirname, '..');
const read = file => fs.readFileSync(path.join(root, file), 'utf8');
const base = 'docs/coloros17-extracted/settings-17.0.0/';
const manifest = JSON.parse(read(base + 'component-index.json'));
const bridge = read('liquidglass/src/main/java/dev/liquidglass/coloros17/ColorOs17SettingsComponents.kt');
const page = read('sample/src/main/java/dev/liquidglass/sample/ColorOs17ExamplePage.kt');

test('original extraction hashes and every catalog source remain intact', () => {
    for (const file of manifest.files) {
        const bytes = fs.readFileSync(path.join(root, base, file.path));
        assert.equal(crypto.createHash('sha256').update(bytes).digest('hex'), file.sha256, file.path);
    }
    assert.equal(manifest.catalog.length, 29);
    for (const entry of manifest.catalog) assert(manifest.files.some(file => file.path === entry.source));
});

test('native code source is ROM-gated, installed, system-marked and platform-signed', () => {
    assert.match(bridge, /ColorOs17BlurPolicy\.isColorOs17/);
    assert.match(bridge, /Build\.VERSION\.SDK_INT >= 33/);
    assert.match(bridge, /FLAG_SYSTEM or ApplicationInfo.FLAG_UPDATED_SYSTEM_APP/);
    assert.match(bridge, /checkSignatures\("android", SETTINGS_PACKAGE\)/);
    assert.match(bridge, /createPackageContext\(SETTINGS_PACKAGE/);
    assert.match(bridge, /Class\.forName\(name, false, context\.classLoader\)/);
    assert.doesNotMatch(bridge, /DexClassLoader|setHiddenApiExemptions|Runtime\.getRuntime|drawBackdrop/);
});

test('foreign theme/resources and host Activity stay distinct', () => {
    assert.match(bridge, /"Theme\.COUI\.Dark" else "Theme\.COUI"/);
    assert.match(bridge, /getResources\(\): Resources = source\.resources/);
    assert.match(bridge, /ContextThemeWrapper\(host, theme\)/);
    assert.match(bridge, /dialog\.setOwnerActivity\(activity\)/);
    assert.match(bridge, /call\(preference, "setPersistent", false\)/);
    assert.match(bridge, /closers\.asReversed\(\)/);
    assert.match(bridge, /catch \(error: LinkageError\)/);
});

test('examples are registered separately, use native View/popup calls and show failures', () => {
    const routes = read('sample/src/main/java/dev/liquidglass/sample/IphoneExamples.kt');
    assert.match(routes, /ColorOs17ComponentCatalog\.entries\.map/);
    assert.match(routes, /ColorOs17ExamplePage\(ColorOs17ComponentCatalog\.find/);
    assert.match(page, /session\.createView/);
    assert.match(page, /session\.showPopup/);
    assert.match(page, /无法调用原组件/);
    assert.match(page, /session\?\.close\(\)/);
    assert.doesNotMatch(page, /ExampleRecordedDesignPage|FigmaSceneView|Slider\(|Switch\(|AlertDialog\(/);
});

test('COUI native material is outside the recorded source and keeps system density', () => {
    assert.match(page, /Box\(Modifier\.fillMaxSize\(\)\.layerBackdrop\(backdrop\)\.background\(colors\.page\)\)/);
    assert.match(page, /Density\(host\.resources\.displayMetrics\.density, configuration\.fontScale\)/);
    assert.equal((page.match(/\.layerBackdrop\(/g) ?? []).length, 1);
});
