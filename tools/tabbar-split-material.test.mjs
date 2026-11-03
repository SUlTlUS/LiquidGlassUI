import assert from 'node:assert/strict';
import { readFileSync, existsSync } from 'node:fs';
import test from 'node:test';

const read = relative => readFileSync(new URL(`../${relative}`, import.meta.url), 'utf8');
const chrome = read('melox-tabbar/src/main/kotlin/com/lladlam/melox/ui/MeloXBottomChrome.kt');

test('regular and search surfaces use one material and one backdrop provider', () => {
  const wrapper = read('melox-tabbar/src/main/kotlin/com/lladlam/melox/ui/MeloXTabBar.kt');
  assert.match(wrapper, /LocalMeloXBackdrop provides backdrop/);
  const search = chrome.slice(chrome.indexOf('if (layout.showSearchButton) {'));
  assert.match(search, /meloXLiquidBottomBar\(/);
  assert.match(search, /surfaceColor = bottomGlassSurfaceColor\(\)/);
  assert.match(search, /clickable\(enabled = searchEnabled/);
  assert.match(search, /onSelect\(AppTab.Search\)/);
  assert.doesNotMatch(search, /layerBackdrop\(/);
});

test('obsolete renderer is absent while shared segmented-selector items remain', () => {
  const legacy = read('liquidglass/src/main/java/com/zeaze/tianyinwallpaper/catalog/components/LiquidBottomTabs.kt');
  assert.doesNotMatch(legacy, /fun LiquidBottomTabs|drawBackdrop\(/);
  assert.equal(existsSync(new URL('../liquidglass/src/main/java/dev/liquidglass/ios27/Ios27SplitSearchTabBar.kt', import.meta.url)), false);
  const segmented = read('liquidglass/src/main/java/com/zeaze/tianyinwallpaper/catalog/components/LiquidSegmentedSelector.kt');
  assert.match(segmented, /LiquidBottomTab\(onClick = onClick\)/);
  assert.match(segmented, /LocalLiquidBottomTabScale provides/);
});
