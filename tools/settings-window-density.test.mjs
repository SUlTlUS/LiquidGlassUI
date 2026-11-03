import assert from 'node:assert/strict';
import { readFileSync } from 'node:fs';
import test from 'node:test';

const read = relative => readFileSync(new URL(`../${relative}`, import.meta.url), 'utf8');
const host = read('sample/src/main/java/dev/liquidglass/sample/MainActivity.kt');
const density = read('liquidglass/src/main/java/dev/liquidglass/ios27/Ios27WindowDensity.kt');

test('window formula keeps the system font scale and clamps scaling', () => {
  assert.match(density, /minOf\(widthDp, heightDp\)/);
  assert.match(density, /\(shortSide \/ 402f\)\.coerceIn\(0\.85f, 1f\)/);
  assert.match(density, /Density\(systemDensity\.density \* scale, systemDensity\.fontScale\)/);
});

test('measure window before insets and before the scaled provider', () => {
  const system = host.indexOf('val systemDensity = LocalDensity.current');
  const window = host.indexOf('BoxWithConstraints(Modifier.fillMaxSize())');
  const calculation = host.indexOf('ios27WindowDensity(systemDensity, maxWidth.value, maxHeight.value)');
  const insets = host.indexOf('.windowInsetsPadding(');
  const provider = host.indexOf('Ios27AndroidDesignBehavior(settingsDensity');
  assert.ok(system >= 0 && system < window && window < calculation);
  assert.ok(calculation < insets && insets < provider);
});

test('only settings uses scaled behavior; system-sized chrome clearance is retained', () => {
  assert.match(host, /if \(page == 2\)\s*\{\s*Ios27AndroidDesignBehavior\(settingsDensity, content = pageContent\)/);
  assert.match(host, /else\s*\{\s*pageContent\(\)/);
  assert.match(host, /with\(systemDensity\) \{ topInset\.toPx\(\) \}\.toDp\(\)/);
  assert.match(host, /with\(systemDensity\) \{ 108\.dp\.toPx\(\) \}\.toDp\(\)/);
  assert.match(host, /\.padding\(top = contentTopInset, bottom = contentBottomInset\)/);
});

test('tablet settings reuse the existing capped content width', () => {
  assert.match(host, /Ios27PageContent\(/);
  const lists = read('liquidglass/src/main/java/dev/liquidglass/ios27/Ios27Lists.kt');
  assert.match(lists, /widthIn\(max = Ios27Tokens\.ReadableWidth\.dp\)\.fillMaxWidth\(\)/);
});
