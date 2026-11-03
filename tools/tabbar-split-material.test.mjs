import assert from 'node:assert/strict';
import { readFileSync } from 'node:fs';
import test from 'node:test';

const read = relative => readFileSync(new URL(`../${relative}`, import.meta.url), 'utf8');
const adapter = read('liquidglass/src/main/java/dev/liquidglass/ios27/Ios27SplitSearchTabBar.kt');

test('split example uses the framework parity adapter, not the legacy default button material', () => {
  const sample = read('sample/src/main/java/dev/liquidglass/sample/Ios27TabBar.kt');
  assert.match(sample, /import dev\.liquidglass\.ios27\.Ios27BottomTabsSplitSearch as LiquidBottomTabsSplitSearch/);
  assert.match(sample, /searchHeight = 62\.dp/);
  assert.match(sample, /searchGap = 16\.dp/);
});

test('both pieces sample the same backdrop directly, without capturing glass again', () => {
  assert.equal((adapter.match(/backdrop = backdrop/g) ?? []).length, 2);
  assert.doesNotMatch(adapter, /layerBackdrop|rememberLayerBackdrop|drawBackdrop/);
  assert.match(adapter, /LiquidBottomTabsRegular\(/);
  assert.match(adapter, /LiquidButton\(/);
});

test('search optics and surface follow the caller track style in both themes', () => {
  assert.match(adapter, /blurRadius = track\.trackBlurRadius/);
  assert.match(adapter, /lensRadiusX = track\.trackLensRadius/);
  assert.match(adapter, /lensRadiusY = track\.trackLensRadius/);
  assert.match(adapter, /style = splitSearchButtonStyle\(style\)/);
  assert.match(adapter, /surfaceColor = style\.containerColor/);
  assert.match(adapter, /background\(style\.fallbackContainerColor\)/);
});

test('search slot remains inside the animated button and split geometry is unchanged', () => {
  assert.match(adapter, /Arrangement\.spacedBy\(searchGap, Alignment\.CenterHorizontally\)/);
  assert.match(adapter, /verticalAlignment = Alignment\.Top/);
  assert.match(adapter, /buttonHeight = searchHeight/);
  assert.match(adapter, /content = \{\s*Box\(Modifier\.fillMaxWidth\(\), contentAlignment = Alignment\.Center\) \{\s*searchContent\(\)/);
});
