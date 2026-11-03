import fs from 'node:fs';
import assert from 'node:assert/strict';
import test from 'node:test';
const read = name => fs.readFileSync(new URL('../' + name, import.meta.url), 'utf8');
const lib = 'liquidglass/src/main/java/dev/liquidglass/ios27/';
const sample = 'sample/src/main/java/dev/liquidglass/sample/';

test('separator drawing and its inset/thickness remain at the original position', () => {
  const lists = read(lib + 'Ios27Lists.kt');
  assert.match(lists, /drawRect\(color, topLeft = Offset\(inset, 0f\),\s*size = Size\(\(size.width - 2 \* inset\).coerceAtLeast\(0f\), Ios27Tokens.SeparatorRenderHeight.dp.toPx\(\)\)\)/);
  assert.match(lists, /listPressMaskEdges\(Ios27Tokens.SeparatorRenderHeight.dp, topSeparator, bottomSeparator\)/);
  assert.match(lists, /index > 0 && showSeparator\(index\)/);
  assert.match(lists, /index < items.lastIndex && showSeparator\(index \+ 1\)/);
});

test('row mask owns half of each visible separator without adding measured padding', () => {
  const mask = read(lib + 'Ios27ListPressMask.kt');
  assert.match(mask, /top = if \(topSeparator\) thickness \/ 2 else 0.dp/);
  assert.match(mask, /bottom = if \(bottomSeparator\) thickness \/ 2 else 0.dp/);
  assert.match(mask, /drawPath\(path, color, alpha = alpha\)/);
  assert.doesNotMatch(mask, /\.padding\(|\.height\(|\.offset\(|\.layerBackdrop\(/);
});

test('full-row callbacks use list masks; embedded accessories keep independent bounds', () => {
  assert.match(read(lib + 'Ios27Lists.kt'), /Modifier.ios27ListPressedClickable\(/);
  assert.match(read(sample + 'ListDetailPage.kt'), /\.ios27ListPressedClickable\(/);
  assert.match(read(sample + 'ListDetailPage.kt'), /\.ios27PressedClickable\(\s*shape = CircleShape/);
  assert.match(read(sample + 'SectionedListExample.kt'), /maskEdges = Ios27PressMaskEdges/);
});

test('Full Width mask is behind foreground content and above the row clip; resting siblings cannot cover its extension', () => {
  const full = read(sample + 'FullWidthListExample.kt');
  assert.match(full, /\.ios27ListPressMask\(rowPressed, rowShape\)\s*\.clip\(rowShape\)/);
  assert.match(full, /\.background\(if \(isRevealing\) draggedSurface else if \(progress > 0f\) listSurface else Color.Transparent, rowShape\)/);
  assert.match(full, /modifier.fillMaxWidth\(\).background\(listSurface\)/);
  assert.match(read(lib + 'Ios27ListPressMask.kt'), /if \(pressed\) drawListPressMask\(shape, edges, color, 1f\)\s*drawContent\(\)/);
});

test('quick-tap fade and cancellation still use direct PressInteraction handling', () => {
  const feedback = read(lib + 'Ios27PressFeedback.kt');
  assert.match(feedback, /is PressInteraction.Press -> opacity.snapTo\(1f\)/);
  assert.match(feedback, /is PressInteraction.Release -> opacity.animateTo\(0f, tween\(120\)\)/);
  assert.match(feedback, /is PressInteraction.Cancel -> opacity.snapTo\(0f\)/);
  assert.match(feedback, /maskEdges = null, onClick = onClick/);
  assert.match(feedback, /clipPath\(contentPath\)/);
});
