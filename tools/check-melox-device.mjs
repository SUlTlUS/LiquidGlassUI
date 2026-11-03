import fs from 'node:fs';
import path from 'node:path';
import assert from 'node:assert/strict';
import { execFileSync } from 'node:child_process';
import { fileURLToPath } from 'node:url';

const root = path.resolve(path.dirname(fileURLToPath(import.meta.url)), '..');
const device = process.argv[2];
assert.ok(device, 'Usage: node tools/check-melox-device.mjs <adb serial> [adb executable]');
const adb = process.argv[3] ?? 'adb';
const warm = process.argv[4] === '--warm';
const out = path.join(root, 'docs/melox-tabbar-validation');
fs.mkdirSync(out, { recursive: true });
const run = (...args) => execFileSync(adb, ['-s', device, ...args], { encoding: 'utf8', timeout: 30000 });
const checks = [];
function dump(name, ready = () => true) {
  for (let attempt = 0; attempt < 5; attempt++) {
    run('shell', 'rm', '-f', '/sdcard/melox-check.xml');
    run('shell', 'uiautomator', 'dump', '/sdcard/melox-check.xml');
    const xml = run('shell', 'sh', '-c', '"cat /sdcard/melox-check.xml 2>/dev/null || true"');
    const nodes = [...xml.matchAll(/<node\b([^>]+)>/g)].map(m => Object.fromEntries(
      [...m[1].matchAll(/([\w-]+)="([^"]*)"/g)].map(a => [a[1], a[2]]),
    ));
    if (nodes.length && ready(nodes)) {
      fs.writeFileSync(path.join(out, name + '.xml'), xml);
      return nodes;
    }
    Atomics.wait(new Int32Array(new SharedArrayBuffer(4)), 0, 0, 600);
  }
  throw new Error(`No fresh ready UI tree: ${name}`);
}
function screenshot(name) {
  run('shell', 'screencap', '-p', '/sdcard/melox-check.png');
  run('pull', '/sdcard/melox-check.png', path.join(out, name + '.png'));
}
function node(nodes, field, value) {
  const found = nodes.find(n => n[field] === value);
  assert.ok(found, `Missing ${field}=${value}`);
  return found;
}
function center(n) {
  const b = n.bounds.match(/\d+/g).map(Number);
  return [Math.round((b[0] + b[2]) / 2), Math.round((b[1] + b[3]) / 2)];
}
function tap(n) { run('shell', 'input', 'tap', ...center(n).map(String)); }
function selected(nodes, name) {
  assert.ok(nodes.some(n => n.text?.startsWith(`当前：${name} ·`)), `Selection did not change to ${name}`);
  checks.push('selection=' + name);
}
function playerControls(nodes, title) {
  const y = (center(node(nodes, 'text', title))[1] + center(node(nodes, 'text', title === '空耳' ? 'ナナツカゼ' : 'MeloX'))[1]) / 2;
  return nodes.filter(n => n.clickable === 'true' && !n['content-desc'] &&
    Math.abs(center(n)[1] - y) < 20 && center(n)[0] > center(node(nodes, 'text', title))[0] &&
    Number(n.bounds.match(/\d+/g)[2]) - Number(n.bounds.match(/\d+/g)[0]) < 200)
    .sort((a, b) => center(a)[0] - center(b)[0]);
}
run('shell', 'am', 'start', ...(warm ? [] : ['-S']), '-W', '-f', '0x10008000', '-n', 'dev.liquidglass.sample/.MainActivity', '--es', 'example_key', 'melox-tabbar');
let nodes = dump('home', ns => ns.some(n => n.text?.startsWith('当前：Home ·')));
selected(nodes, 'Home');
assert.ok(nodes.some(n => n.text === 'ナナツカゼ'), 'Original mini-player artist is missing.');
screenshot('miniplayer-expanded');
tap(playerControls(nodes, '空耳')[0]);
nodes = dump('miniplayer-playing', ns => ns.some(n => n.text === '播放状态：播放'));
checks.push('mini-player play changes UI state'); screenshot('miniplayer-playing');
tap(playerControls(nodes, '空耳')[0]);
nodes = dump('miniplayer-paused', ns => ns.some(n => n.text === '播放状态：暂停'));
checks.push('mini-player pause changes UI state');
tap(playerControls(nodes, '空耳')[1]);
nodes = dump('miniplayer-next', ns => ns.some(n => n.text === '下一首示例'));
checks.push('mini-player next changes track'); screenshot('miniplayer-next');
const swipeY = center(node(nodes, 'text', '下一首示例'))[1];
run('shell', 'input', 'swipe', '650', String(swipeY), '240', String(swipeY), '450');
nodes = dump('miniplayer-swipe', ns => ns.some(n => n.text === '空耳'));
checks.push('mini-player horizontal swipe switches track');
tap(node(nodes, 'text', '空耳'));
nodes = dump('player-expanded', ns => !ns.some(n => n['content-desc'] === 'Search') &&
  ns.some(n => n.text === '空耳'));
checks.push('mini-player title opens actual shared-transition player'); screenshot('player-expanded');
run('shell', 'input', 'keyevent', '4');
nodes = dump('player-back-to-mini', ns => ns.some(n => n['content-desc'] === 'Search'));
checks.push('player system back reverses shared transition'); screenshot('player-back-to-mini');
tap(node(nodes, 'text', '空耳'));
nodes = dump('player-before-short-drag', ns => !ns.some(n => n['content-desc'] === 'Search'));
run('shell', 'input', 'swipe', '540', '700', '540', '900', '1000');
nodes = dump('player-short-drag-restored', ns => !ns.some(n => n['content-desc'] === 'Search'));
assert.ok(nodes.some(n => n.text === '空耳'), 'Short drag must restore the full player.');
checks.push('short player drag springs back to full screen');
run('shell', 'input', 'swipe', '540', '550', '540', '2150', '1200');
nodes = dump('player-drag-dismissed', ns => ns.some(n => n['content-desc'] === 'Search'));
checks.push('long player drag settles to mini player'); screenshot('player-drag-dismissed');
tap(node(nodes, 'text', '隐藏播放器'));
nodes = dump('miniplayer-hidden', ns => ns.some(n => n.text === '显示播放器'));
assert.ok(!nodes.some(n => n.text === '空耳'), 'Hidden player left its content visible.');
tap(node(nodes, 'text', '显示播放器'));
nodes = dump('miniplayer-shown', ns => ns.some(n => n.text === '空耳'));
checks.push('mini-player hide/show controls real media slot');
tap(node(nodes, 'content-desc', 'Explore'));
nodes = dump('explore'); selected(nodes, 'Explore'); screenshot('explore');
tap(node(nodes, 'content-desc', 'Search'));
nodes = dump('search'); selected(nodes, 'Search'); screenshot('search');
tap(node(nodes, 'content-desc', 'Home'));
nodes = dump('return-home'); selected(nodes, 'Home');
const from = center(node(nodes, 'content-desc', 'Home'));
const to = center(node(nodes, 'content-desc', 'Library'));
run('shell', 'input', 'swipe', ...from.map(String), ...to.map(String), '650');
nodes = dump('drag-library'); selected(nodes, 'Library'); screenshot('drag-library');
tap(node(nodes, 'text', '收缩'));
nodes = dump('compact');
assert.ok(nodes.some(n => n.text === '展开'), 'Collapse control did not change state.');
assert.ok(!nodes.some(n => n.text === 'Explore'), 'Expanded labels should be hidden in compact state.');
checks.push('compact labels hidden'); screenshot('compact');
assert.equal(playerControls(nodes, '空耳').length, 1, 'Compact player must hide Next but keep Play.');
checks.push('compact player preserves artist and hides Next');
// Upstream compact glyph has no contentDescription. Find the original click target
// on the same baseline as Search, without adding new semantics to extracted code.
const searchCenter = center(node(nodes, 'content-desc', 'Search'));
const compactTarget = nodes.filter(n => n.clickable === 'true' && !n['content-desc'] &&
  Math.abs(center(n)[1] - searchCenter[1]) < 10 && center(n)[0] < searchCenter[0])
  .sort((a, b) => center(a)[0] - center(b)[0])[0];
assert.ok(compactTarget, 'Compact navigation click target is missing.');
tap(compactTarget);
nodes = dump('expand-by-tab');
assert.ok(nodes.some(n => n.text === '收缩'), 'Compact tap did not expand the bar.');
checks.push('compact tab tap expands');
tap(node(nodes, 'text', '毛玻璃模式'));
nodes = dump('frosted');
assert.ok(nodes.some(n => n.text === '液态模式'), 'Frosted toggle did not change state.');
checks.push('frosted toggle'); screenshot('frosted');
tap(node(nodes, 'text', '液态模式'));
nodes = dump('liquid');
run('shell', 'input', 'swipe', '540', '1600', '540', '800', '450');
nodes = dump('scroll');
assert.ok(!nodes.some(n => n.text === 'MeloX TabBar'), 'Page content did not scroll.');
assert.ok(!nodes.some(n => n.text === 'Explore'), 'Scroll did not trigger original automatic shrink.');
checks.push('upward content scroll automatically collapses bar');
checks.push('content scrolls beneath independent glass'); screenshot('scroll');
run('shell', 'input', 'swipe', '540', '800', '540', '1600', '450');
nodes = dump('scroll-down-expand', ns => ns.some(n => n.text === 'Explore'));
checks.push('downward content scroll automatically expands bar'); screenshot('scroll-down-expand');
run('shell', 'input', 'keyevent', '4');
nodes = dump('category');
tap(node(nodes, 'text', '原版交互'));
nodes = dump('catalog-open');
assert.ok(nodes.some(n => n['content-desc'] === 'MeloX TabBar 独立示例'), 'Catalog entry did not open its live page.');
checks.push('catalog variant opens dedicated page'); screenshot('catalog-open');
run('shell', 'input', 'keyevent', '4');
dump('return-category');
run('shell', 'input', 'keyevent', '4');
nodes = dump('root-catalog', ns => ns.some(n => n.text === '手机示例'));
tap(node(nodes, 'text', 'MeloX TabBar'));
nodes = dump('category-from-root', ns => ns.some(n => n.text === '原版交互'));
tap(node(nodes, 'text', '原版交互'));
nodes = dump('opened-from-root', ns => ns.some(n => n['content-desc'] === 'MeloX TabBar 独立示例'));
checks.push('visible root catalog entry navigates to the live page'); screenshot('opened-from-root');
const pid = run('shell', 'pidof', 'dev.liquidglass.sample').trim();
assert.ok(pid, 'Host process exited.');
const log = run('logcat', '-d', '--pid=' + pid, '-t', '3000');
assert.doesNotMatch(log, /FATAL EXCEPTION|Fatal signal/, 'Host crash found.');
fs.writeFileSync(path.join(out, 'host-logcat.txt'), log);
fs.writeFileSync(path.join(out, 'interaction-report.json'), JSON.stringify({
  device, package: 'dev.liquidglass.sample', launch: warm ? 'warm (already foreground)' : 'cold', checks,
  visualParityWithOriginalApk: 'not tested',
  notes: ['Runtime gestures and captures checked; not a same-device original APK pixel comparison.'],
}, null, 2) + '\n');
console.log(`PASS: ${checks.length} device checks on ${device}.`);
