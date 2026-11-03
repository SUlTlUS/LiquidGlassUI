import test from 'node:test';
import assert from 'node:assert/strict';
import fs from 'node:fs';
import path from 'node:path';

const root = path.resolve(import.meta.dirname, '../..');
const read = file => fs.readFileSync(path.join(root, file), 'utf8');
const sample = 'sample/src/main/java/dev/liquidglass/sample/';
const pages = read(sample + 'Ios27NativeExamples.kt');
const router = read(sample + 'Ios27MechanismExamplePage.kt');
const catalog = read(sample + 'IphoneExamples.kt');

test('three common routes feed the existing category and page router', () => {
    const entries = [...pages.matchAll(/^    (\w+)\("(ios27-[^"]+)", "([^"]+)"/gm)];
    assert.equal(entries.length, 3);
    assert.deepEqual(entries.map(e => e[1]), ['Menu', 'Popup', 'Theme']);
    assert.equal(new Set(entries.map(e => e[2])).size, 3);
    for (const entry of entries) assert.match(pages,
        new RegExp(`Ios27MechanismExample.${entry[1]} ->`));
    assert.match(catalog, /Ios27MechanismExample.entries.map/);
    assert.match(catalog, /if \(it == Ios27MechanismExample.Menu\) "Menus" else "通用演示"/);
    assert.match(catalog, /Ios27MechanismExamplePage\(entry.key, standard, popBack\)/);
    assert.match(read('sample/build.gradle.kts'), /minSdk = 33/);
    assert.match(router, /key\(key, standard\)/);
});

test('native pages reuse host chrome and independent recorded background', () => {
    for (const api of ['ExampleRecordedResponsivePage', 'Ios27PageContent', 'Ios27PageHeader',
        'Ios27ListSection', 'ExampleTopChrome', 'Ios27BackButton', 'GlassBackdropProvider']) {
        assert.ok(new RegExp(`${api}(?:\\(|\\s*\\{)`).test(pages), `Missing host primitive: ${api}`);
    }
    assert.match(pages, /val previewBackdrop = rememberExamplePageBackdrop\(colors.page\)/);
    assert.match(pages, /Ios27ExamplePreview\(previewBackdrop\)/);
    const preview = pages.slice(pages.indexOf('private fun Ios27ExamplePreview'));
    assert.match(preview, /ExampleBackdropLayer\(backdrop, colors.page, Modifier.matchParentSize\(\)\) \{ Ios27ExampleBackdrop\(\) \}/);
    assert.match(preview, /GlassBackdropProvider\(backdrop\)/);
    assert.doesNotMatch(pages, /\.layerBackdrop\(|GlassShowcase|GlassVerificationPage|Color\(0x/);
    assert.match(pages, /val dark = !MaterialTheme.colors.isLight/);
    assert.match(pages, /LiveRegionMode.Polite/);
});

test('source demo UI is absent from every active source set', () => {
    for (const module of ['sample', 'liquidglass-ios27']) {
        const dir = path.join(root, module, 'src/main/java');
        for (const file of fs.readdirSync(dir, {recursive: true}).filter(f => f.endsWith('.kt'))) {
            const source = fs.readFileSync(path.join(dir, file), 'utf8');
            assert.doesNotMatch(source, /(?:package|import) dev\.liquidglass27\.demo/);
        }
    }
});

test('common sample uses adaptive buttons and keeps menu, popup and icons', () => {
    for (const api of ['GlassButton', 'GlassButtonGroup', 'GlassMorphMenu',
        'GlassPopup', 'GlassDialogTextField', 'AnimatedGlassGlyph']) {
        assert.match(pages, new RegExp(`${api}\\(`));
    }
    assert.match(pages, /GlassPopupSourceContent\(popup,/);
    assert.match(pages, /onReturnComplete = \{ menuMounted = false \}/);
    assert.match(pages, /if \(value != null\) \{ saved = value;/);
    assert.doesNotMatch(pages, /popupSourceLayer|sourceSurfaceSnapshot/);
    assert.match(pages, /sourceSurface = if \(ios26Menu\)/);
    const popup = read('liquidglass-ios27/src/main/java/dev/liquidglass/GlassPopup.kt');
    assert.match(popup, /origin.width.roundToInt\(\) \+ pad \* 2/);
    assert.match(popup, /origin.height.roundToInt\(\) \+ pad \* 2/);
    assert.match(popup, /\.padding\(shadowPadding\)\.clearAndSetSemantics/);
    assert.match(catalog, /SampleGlassTheme\(standard\)/);
    assert.match(catalog, /MeloXTabBarExampleKey, "Tab Bar", "标签栏"/);
    assert.match(catalog, /"Tab Bar" -> IphoneRoute.Example\(MeloXTabBarExampleKey\)/);
    assert.match(catalog, /Ios27ExampleKeys.TabBarDefault, Ios27ExampleKeys.TabBarSplitSearch -> MeloXTabBarExampleKey/);
    assert.doesNotMatch(catalog, /"MeloX TabBar" -> IphoneRoute.Example/);
    assert.match(catalog, /"Listening Mode" -> IphoneRoute.Example\(ListeningModeExampleKey\)/);
    assert.doesNotMatch(catalog, /SampleGlassTheme\(GlassStandard.Ios26\) \{ MeloXTabBarExamplePage/);
    assert.match(catalog, /SampleGlassTheme\(GlassStandard.Ios26\) \{ ListeningModeExamplePage/);
});
