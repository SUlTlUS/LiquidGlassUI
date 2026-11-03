import assert from 'node:assert/strict';
import fs from 'node:fs';
import path from 'node:path';
import crypto from 'node:crypto';

const root = path.resolve(import.meta.dirname, '..');
const read = name => fs.readFileSync(path.join(root, name), 'utf8');
const hash = name => crypto.createHash('sha256').update(fs.readFileSync(path.join(root, name))).digest('hex');
const manifest = JSON.parse(read('docs/ios27/merge-manifest.json'));
const imported = manifest.files;
assert.equal(imported.filter(f => f.source.startsWith('app/src/main/java/')).length, 69);
assert.equal(imported.filter(f => f.source.startsWith('app/src/main/assets/')).length, 5);
assert.equal(imported.filter(f => f.source.startsWith('app/src/test/')).length, 56);
assert.equal(manifest.demoFiles.length, 0, 'Source demo UI must not be imported');
for (const entry of [...imported, ...manifest.demoFiles, ...manifest.modifiedExisting, ...(manifest.addedFiles ?? [])]) {
    if (entry.removed) {
        assert.equal(entry.mergedSha256, null, `Removed file cannot have an active hash: ${entry.path}`);
        assert.ok(!fs.existsSync(path.join(root, entry.path)), `Removed implementation reappeared: ${entry.path}`);
        continue;
    }
    assert.equal(hash(entry.path), entry.mergedSha256, `Unreviewed merge drift: ${entry.path}`);
}
for (const entry of manifest.legacyRuntime) {
    const reviewed = (manifest.reviewedLegacyChanges ?? []).filter(change => change.path === entry.path);
    assert.ok(reviewed.length <= 1, `Duplicate review: ${entry.path}`);
    if (reviewed.length) assert.equal(reviewed[0].baselineSha256, entry.sha256, `Legacy baseline must stay frozen: ${entry.path}`);
    if (reviewed[0]?.removed) {
        assert.equal(reviewed[0].mergedSha256, null);
        assert.ok(!fs.existsSync(path.join(root, entry.path)), `Removed legacy file reappeared: ${entry.path}`);
        continue;
    }
    assert.equal(hash(entry.path), reviewed[0]?.mergedSha256 ?? entry.sha256, `Unreviewed iOS 26 runtime change: ${entry.path}`);
}
for (const entry of manifest.reviewedLegacyChanges ?? []) {
    assert.ok(manifest.legacyRuntime.some(base => base.path === entry.path), `Unknown legacy review: ${entry.path}`);
}
const build = read('liquidglass-ios27/build.gradle.kts');
assert.match(build, /minSdk = 33/);
assert.match(build, /api\(project\(":liquidglass"\)\)/);
assert.doesNotMatch(read('liquidglass/build.gradle.kts'), /project\(":liquidglass-ios27"\)/);
assert.doesNotMatch(read('settings.gradle.kts'), /\.\.\/liquidglass27|flclash-demo/);
const sources = fs.readdirSync(path.join(root, 'liquidglass-ios27/src/main/java/dev/liquidglass'));
for (const name of sources.filter(n => n.endsWith('.kt'))) {
    const text = read(`liquidglass-ios27/src/main/java/dev/liquidglass/${name}`);
    assert.doesNotMatch(text, /import dev\.liquidglass27\.(demo|R|BuildConfig)/, name);
    assert.ok(!text.includes('\uFFFD') && !text.startsWith('\uFEFF'), `Encoding: ${name}`);
}
assert.ok(!fs.existsSync(path.join(root, 'liquidglass-ios27/src/main/java/dev/liquidglass27/demo')));
const facade = read('liquidglass-ios27/src/main/java/dev/liquidglass/GlassBackdrop.kt');
assert.match(facade, /sketchMaterial = true/);
assert.match(facade, /fun prewarmGlass\(context: Context\)/);
assert.doesNotMatch(build, /overrideLibrary|disable.*NewApi/);
const host = read('sample/src/main/java/dev/liquidglass/sample/IphoneExamples.kt');
assert.match(host, /Ios27MechanismExamples/);
assert.match(host, /Ios27MechanismExamplePage\(entry.key, standard, popBack\)/);
const nativeExamples = read('sample/src/main/java/dev/liquidglass/sample/Ios27NativeExamples.kt');
assert.match(nativeExamples, /ExampleRecordedResponsivePage\(/);
assert.match(nativeExamples, /GlassBackdropProvider\(backdrop\)/);
assert.match(host, /Ios27MechanismExample.entries.map/);
assert.doesNotMatch(read('sample/src/main/java/dev/liquidglass/sample/Ios27MechanismExamplePage.kt'), /dev\.liquidglass27\.demo/);
for (const name of ['GlassShowcase.kt', 'GlassVerificationPage.kt']) {
    assert.ok(!fs.existsSync(path.join(root, 'sample/src/main/java/dev/liquidglass27/demo', name)));
}
console.log(`PASS: 69 mechanism provenance records, 5 shaders, 56 imported test records; ${manifest.legacyRuntime.length} frozen iOS 26 baselines, ${(manifest.reviewedLegacyChanges ?? []).length} authorized revisions; current host examples use the shared library API; no copied demo UI or external source dependency.`);
