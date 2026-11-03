import assert from 'node:assert/strict';
import { createHash } from 'node:crypto';
import { existsSync, readFileSync, readdirSync } from 'node:fs';
import path from 'node:path';
import test from 'node:test';
import { fileURLToPath } from 'node:url';

const root = path.resolve(path.dirname(fileURLToPath(import.meta.url)), '..');
const read = name => readFileSync(path.join(root, name), 'utf8');
const hash = value => createHash('sha256').update(value).digest('hex');
const sample = 'sample/src/main/java/dev/liquidglass/sample/';
const library = 'liquidglass/src/main/java/dev/liquidglass/ios27/';
const visualFiles = [
  ...['ExamplePageScaffold.kt', 'NativeExamplePage.kt', 'Ios27ExampleComponents.kt',
    'FullWidthListExample.kt', 'Ios27PickerExample.kt', 'Ios27StepperExample.kt',
    'Ios27TextFieldsExample.kt', 'Ios27ColorPickerDialog.kt', 'Ios27ColorPickerSheetSurface.kt',
    'Ios27AlertExample.kt', 'Ios27ActionSheetExample.kt', 'Ios27ActivityViewExample.kt',
    'Ios27MenuExamples.kt', 'Ios27SheetsModal.kt'].map(name => sample + name),
  library + 'Ios27Lists.kt', library + 'Ios27InteractiveControls.kt',
];

// Conservative source guard, not a screenshot comparison. Keep every existing
// drawing/layout/animation call byte-equivalent after whitespace normalization.
export function visualContract(source) {
  const starts = /\b(?:Color|RoundedCornerShape|size|requiredSize|height|heightIn|width|widthIn|padding|offset|background|clip|alpha|shadow|blur|lens|tween|spring|drawLine|drawRect|drawCircle|drawRoundRect)\s*\(/g;
  const calls = [];
  for (const match of source.matchAll(starts)) {
    let depth = 1, cursor = match.index + match[0].length;
    while (cursor < source.length && depth) {
      if (source[cursor] === '(') depth++;
      if (source[cursor] === ')') depth--;
      cursor++;
    }
    calls.push(source.slice(match.index, cursor).replace(/\s+/g, ''));
  }
  for (const match of source.matchAll(/\b(?:fontSize|lineHeight|fontFamily|fontWeight|strokeWidth|shape|animationSpec)\s*=\s*([^\n\r]+)/g)) {
    calls.push(match[0].replace(/\s+/g, ''));
  }
  return hash(calls.join('\n'));
}

function treeHash(relative) {
  const entries = [];
  function visit(dir) {
    for (const entry of readdirSync(path.join(root, dir), { withFileTypes: true }).sort((a,b) => a.name.localeCompare(b.name))) {
      const name = `${dir}/${entry.name}`;
      if (entry.isDirectory()) visit(name);
      else entries.push(`${name}:${hash(readFileSync(path.join(root, name)))}`);
    }
  }
  visit(relative);
  return hash(entries.join('\n'));
}

export function snapshot() {
  const originalDocs = ['AGENTS.md', 'docs/figma-page-adaptation.md', 'docs/ios27-page-adaptation.md',
    'docs/glass-backdrop-integration.md', 'docs/listening-mode-selector.md', 'docs/melox-tabbar.md',
    'docs/native-framework-examples.md', 'docs/liquid-glass-merged-files.json'];
  const protectedTrees = ['liquidglass/src/main/java/com/zeaze/tianyinwallpaper',
    'docs/liquid-glass-extracted/source', 'sample/src/main/assets/iphone-examples'];
  return {
    visual: Object.fromEntries(visualFiles.map(name => [name, visualContract(approvedPressMaskProjection(name, read(name)))])),
    unchanged: Object.fromEntries(originalDocs.map(name => [name, hash(readFileSync(path.join(root, name)))])),
    trees: Object.fromEntries(protectedTrees.map(name => [name, treeHash(name)])),
  };
}

// 2026-10-08 user-authorized correction: ONLY the Full Width pressed mask moved
// out of the clipped content row. Do not refresh the frozen visual baseline.
function approvedPressMaskProjection(name, source) {
  if (name !== sample + 'FullWidthListExample.kt') return source;
  const background = '.background(if (isRevealing) draggedSurface else if (progress > 0f) listSurface else Color.Transparent, rowShape)';
  const mask = '.ios27ListPressMask(rowPressed, rowShape)\n                    .clip(rowShape)';
  if (!source.includes(background)) return source;
  assert.ok(source.includes(mask), 'authorized mask must draw before clipped row content');
  return source.replace(background, '.clip(rowShape)\n                .background(if (isRevealing) draggedSurface else listSurface)')
    .replace(mask, '.background(if (rowPressed) colors.pressed else Color.Transparent)');
}

if (process.argv.includes('--snapshot')) {
  process.stdout.write(JSON.stringify(snapshot(), null, 2));
} else if (existsSync(path.join(root, 'docs/android-example-visual-contract.json'))) {
  test('visual calls stay frozen except the explicitly authorized list mask correction; original specifications/assets/glass are unchanged', () => {
    assert.deepEqual(snapshot(), JSON.parse(read('docs/android-example-visual-contract.json')));
  });
  test('all design-coordinate page and overlay shells apply the invisible Android touch policy', () => {
    assert.equal((read(sample + 'ExamplePageScaffold.kt').match(/Ios27AndroidDesignBehavior\(designDensity\)/g) ?? []).length, 3);
    assert.match(read(sample + 'Ios27ExampleComponents.kt'), /ios27AndroidPane\(spec.category/);
  });
  test('all 56 public source variants and both standalone demos keep their native routes', () => {
    const catalog = JSON.parse(read('sample/src/main/assets/iphone-examples/catalog.json'));
    const api = read(sample + 'Ios27ExampleComponents.kt');
    assert.equal(catalog.length, 56);
    for (const entry of catalog) assert.ok(api.includes(JSON.stringify(entry.key)), entry.key);
    assert.match(read(sample + 'IphoneExamples.kt'), /Ios27ExampleContent\(/);
    for (const name of ['ListeningModeExamplePage.kt', 'MeloXTabBarExamplePage.kt']) {
      assert.match(read(sample + name), /ios27AndroidPane/);
    }
  });
  test('popup semantics and saved state are additive rather than alternative visual implementations', () => {
    for (const name of ['Ios27AlertExample.kt', 'Ios27ActionSheetExample.kt',
      'Ios27ActivityViewExample.kt', 'Ios27MenuExamples.kt', 'Ios27SheetsModal.kt',
      'Ios27ColorPickerSheetSurface.kt']) assert.match(read(sample + name), /ios27AndroidPane/);
    assert.match(read(sample + 'NativeExamplePage.kt'), /ios27AndroidBackgroundBlocked\(modalBackAction != null\)/);
    assert.match(read(sample + 'Ios27ColorPickerDialog.kt'), /rememberSaveable\(stateSaver = AndroidPickerColorSaver\)/);
    assert.match(read(sample + 'Ios27ColorPickerDialog.kt'), /pickerMode by rememberSaveable/);
  });
  test('custom controls expose alternatives to dragging and swiping without replacing renderers', () => {
    assert.match(read(library + 'Ios27InteractiveControls.kt'), /ios27AndroidProgress/);
    assert.match(read(sample + 'FullWidthListExample.kt'), /CustomAccessibilityAction/);
    assert.match(read(sample + 'Ios27ColorPickerDialog.kt'), /CustomAccessibilityAction/);
    assert.match(read(sample + 'Ios27PickerExample.kt'), /contentDescription = "上个月"/);
    assert.match(read(sample + 'Ios27PickerExample.kt'), /contentDescription = "下个月"/);
  });
}
