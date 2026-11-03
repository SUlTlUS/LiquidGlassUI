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

test('seven unique component routes feed the existing category and page router', () => {
    const entries = [...pages.matchAll(/^    (\w+)\("(ios27-[^"]+)", "([^"]+)"/gm)];
    assert.equal(entries.length, 7);
    assert.equal(new Set(entries.map(e => e[2])).size, 7);
    for (const entry of entries) assert.ok(pages.includes(`Ios27MechanismExample.${entry[1]} ->`));
    assert.match(catalog, /Ios27MechanismExample.entries.map\s*\{\s*IphoneExample\(it.key, "iOS 27", it.title, ""\)/);
    assert.match(catalog, /Ios27MechanismExamplePage\(entry.key, popBack\)/);
    assert.match(router, /SDK_INT >= 33/);
    assert.match(router, /key\(key\)/);
});

test('native pages reuse host chrome and independent recorded background', () => {
    for (const api of ['ExampleRecordedResponsivePage', 'Ios27PageContent', 'Ios27PageHeader',
        'Ios27ListSection', 'ExampleTopChrome', 'Ios27BackButton', 'Ios27BackdropScope']) {
        assert.ok(new RegExp(`${api}(?:\\(|\\s*\\{)`).test(pages), `Missing host primitive: ${api}`);
    }
    assert.match(pages, /val previewBackdrop = rememberExamplePageBackdrop\(colors.page\)/);
    assert.match(pages, /Ios27ExamplePreview\(previewBackdrop\)/);
    const preview = pages.slice(pages.indexOf('private fun Ios27ExamplePreview'));
    assert.match(preview, /ExampleBackdropLayer\(backdrop, colors.page, Modifier.matchParentSize\(\)\) \{ Ios27ExampleBackdrop\(\) \}/);
    assert.match(preview, /Ios27BackdropScope\(backdrop\)/);
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

test('sample uses new mechanisms and captures only popup source foreground', () => {
    for (const api of ['ElasticGlassButton', 'GooGlassRow', 'Ios27NavigationDock', 'GlassMorphMenu',
        'GlassPopup', 'GlassDialog', 'GlassDialogTextField', 'AnimatedGlassGlyph']) {
        assert.match(pages, new RegExp(`${api}\\(`));
    }
    assert.match(pages, /GlassPopupSourceContent\(popup,/);
    assert.match(pages, /onReturnComplete = \{ menuMounted = false \}/);
    assert.match(pages, /if \(value != null\) \{ saved = value;/);
    assert.match(pages, /enabled = dialogKind != 0 \|\| draft.isNotBlank\(\)/);
});
