import fs from 'node:fs';
import path from 'node:path';
import { fileURLToPath } from 'node:url';
import assert from 'node:assert/strict';
import { createHash } from 'node:crypto';
import test from 'node:test';
const root = path.resolve(path.dirname(fileURLToPath(import.meta.url)), '..');
const read = p => fs.readFileSync(path.join(root, p), 'utf8');
test('Android accessibility additions preserve the existing palette and vector paths', () => {
  const source = read('liquidglass/src/main/java/dev/liquidglass/components/ListeningModeSelector.kt').replace(/\r\n/g, '\n');
  const digest = s => createHash('sha256').update(s).digest('hex');
  assert.equal(digest(source.slice(source.indexOf('/** Scalable vector'))),
    'de8a88809cd83a0c3f566eef83ef910cb5cb5719f674970e9a3597a8e7780b21');
  assert.equal(digest(source.slice(source.indexOf('    val LightColors ='), source.indexOf('    fun showSeparator'))),
    '4b53f36dc623c515f2fe15f777d6a198ff3aafa0ddc6c043e4bccff4710c40a1');
  assert.match(source, /val BarHeight = 68.dp/);
  assert.match(source, /val SelectionInset = 4.dp/);
  assert.match(source, /val IconSize = 40.dp/);
  assert.match(source, /fontSize = 14.sp, lineHeight = 18.sp/);
  assert.match(source, /Spacer\(Modifier.height\(6.dp\)\)/);
  assert.match(source, /spring\(dampingRatio = 1f, stiffness = 500f\)/);
  assert.match(source, /if \(pressed\) 0.94f else 1f/);
});
test('Android interaction uses grouped focus, minimum targets and one accessibility label per item', () => {
  const source = read('liquidglass/src/main/java/dev/liquidglass/components/ListeningModeSelector.kt');
  assert.match(source, /selectableGroup\(\).focusGroup\(\)/);
  assert.match(source, /val MinimumTouchTarget = 48.dp/);
  assert.match(source, /enabled = contentWidth > maxWidth/);
  assert.match(source, /semantics \{ heading\(\) \}/);
  assert.match(source, /Text\(mode.label, Modifier.fillMaxWidth\(\).clearAndSetSemantics/);
  assert.match(source, /focused && inputModeManager.inputMode == InputMode.Keyboard/);
  assert.match(source, /HapticFeedbackType.SegmentTick/);
  assert.doesNotMatch(source, /HapticFeedbackType.TextHandleMove/);
});
test('listening selector is a public controlled component with selectable semantics', () => {
  const source = read('liquidglass/src/main/java/dev/liquidglass/components/ListeningModeSelector.kt');
  assert.match(source, /fun ListeningModeSelector\(/);
  assert.match(source, /selectedMode: ListeningMode/);
  assert.match(source, /onModeSelected: \(ListeningMode\) -> Unit/);
  assert.match(source, /role = Role.RadioButton/);
  assert.match(source, /enabled = enabled/);
  assert.match(source, /if \(mode != selectedMode\)/);
  assert.match(source, /FontFamily.Default/);
  assert.doesNotMatch(source, /layerBackdrop|drawBackdrop|SharedPreferences|android\.media/);
});
test('one instance follows the host theme and retains saveable state and navigation', () => {
  const page = read('sample/src/main/java/dev/liquidglass/sample/ListeningModeExamplePage.kt');
  assert.match(page, /rememberSaveable.*ListeningMode.NoiseCancellation/);
  assert.match(page, /ListeningModeSelector\(selected, \{ selected = it \}\)/);
  assert.equal([...page.matchAll(/ListeningModeSelector\(/g)].length, 1);
  assert.doesNotMatch(page, /darkTheme = (true|false)/);
  const component = read('liquidglass/src/main/java/dev/liquidglass/components/ListeningModeSelector.kt');
  assert.match(component, /darkTheme: Boolean = !MaterialTheme.colors.isLight/);
  assert.match(page, /ExampleRecordedDesignPage/);
  assert.match(page, /ExampleTopChrome/);
  assert.match(page, /Ios27BackButton/);
});
test('catalog exposes listening mode without counting it as a Figma original', () => {
  const router = read('sample/src/main/java/dev/liquidglass/sample/IphoneExamples.kt');
  assert.match(router, /\+ MeloXTabBarExampleEntry \+ ListeningModeExampleEntry/);
  assert.match(router, /entry\?\.key == ListeningModeExampleKey[\s\S]*ListeningModeExamplePage\(onBack = popBack\)/);
  assert.match(router, /initialReference && initialEntry.file.isNotBlank\(\)/);
  assert.match(router, /entries.count \{ it.file.isNotBlank\(\) \}/);
  assert.match(router, /"Listening Mode" to "听音模式"/);
});
