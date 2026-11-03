import fs from 'node:fs';
import path from 'node:path';
import assert from 'node:assert/strict';
import { execFileSync } from 'node:child_process';
import { fileURLToPath } from 'node:url';

const root = path.resolve(path.dirname(fileURLToPath(import.meta.url)), '..');
const device = process.argv[2];
assert.ok(device, 'Usage: node tools/check-listening-mode-device.mjs <adb serial>');
const out = path.join(root, 'docs/listening-mode-validation');
fs.mkdirSync(out, { recursive: true });
const run = (...args) => execFileSync('adb', ['-s', device, ...args], { encoding: 'utf8', timeout: 30000 });
const checks = [];
assert.doesNotMatch(run('shell', 'dumpsys', 'window', 'policy'), /^\s+showing=true\s*$/m,
  'Phone is locked. Ask the user to unlock it; do not claim UI acceptance.');
run('shell', 'am', 'start', '-W', '-f', '0x10008000', '-n', 'dev.liquidglass.sample/.MainActivity', '--es', 'example_key', 'listening-mode');
function dump(name, ready = () => true) {
  for (let attempt = 0; attempt < 5; attempt++) {
    run('shell', 'rm', '-f', '/sdcard/listening-mode-check.xml');
    run('shell', 'uiautomator', 'dump', '/sdcard/listening-mode-check.xml');
    const xml = run('shell', 'sh', '-c', '"cat /sdcard/listening-mode-check.xml 2>/dev/null || true"');
    const nodes = [...xml.matchAll(/<node\b([^>]+)>/g)].map(m => Object.fromEntries(
      [...m[1].matchAll(/([\w-]+)="([^"]*)"/g)].map(a => [a[1], a[2]]),
    ));
    if (nodes.length && ready(nodes)) {
      fs.writeFileSync(path.join(out, name + '.xml'), xml);
      return nodes;
    }
    Atomics.wait(new Int32Array(new SharedArrayBuffer(4)), 0, 0, 700);
  }
  throw new Error('No fresh ready UI tree: ' + name);
}
function shot(name) {
  run('shell', 'screencap', '-p', '/sdcard/listening-mode-check.png');
  run('pull', '/sdcard/listening-mode-check.png', path.join(out, name + '.png'));
}
function options(nodes, label) {
  return nodes.filter(n => n['content-desc'] === label && n.clickable === 'true')
    .sort((a, b) => Number(a.bounds.match(/\d+/g)[1]) - Number(b.bounds.match(/\d+/g)[1]));
}
function tap(n, bottomLabel = false) {
  assert.ok(n, 'Missing interactive mode');
  const [x0, y0, x1, y1] = n.bounds.match(/\d+/g).map(Number);
  run('shell', 'input', 'tap', String(Math.round((x0 + x1) / 2)),
    String(bottomLabel ? y1 - 12 : Math.round(y0 + (y1 - y0) * 0.35)));
}
function selection(nodes, label) {
  const controls = options(nodes, label);
  assert.equal(controls.length, 1, 'Exactly one host-themed instance must be present.');
  assert.ok(controls.every(n => n.selected === 'true'), 'Selection did not update.');
  assert.equal(nodes.filter(n => n.selected === 'true' && n['content-desc']).length, 1,
    'Multiple modes became selected.');
  assert.ok(nodes.some(n => n.text === '当前模式：' + label), 'Callback did not update host state.');
}
let nodes = dump('initial', ns => options(ns, '主动降噪').length === 1);
tap(options(nodes, '主动降噪')[0]);
nodes = dump('initial-selected'); selection(nodes, '主动降噪'); shot('initial');
checks.push('one host-themed instance rendered with the expected selection');
for (const [i, label] of ['关闭', '通透模式', '自适应', '主动降噪'].entries()) {
  tap(options(nodes, label)[0], i % 2 === 1);
  nodes = dump('selected-' + i, ns => options(ns, label).every(n => n.selected === 'true'));
  selection(nodes, label); shot('selected-' + i);
  checks.push(`mode=${label}; tap ${i % 2 ? 'external label' : 'icon'} updates host state`);
}
run('shell', 'input', 'keyevent', '4');
nodes = dump('category', ns => ns.some(n => n.text === '四档听音模式'));
const entry = nodes.find(n => n.text === '四档听音模式'); tap(entry);
nodes = dump('reopened', ns => options(ns, '主动降噪').length === 1);
selection(nodes, '主动降噪'); checks.push('category entry reopens the interactive component');
shot('reopened');
run('shell', 'input', 'keyevent', '4');
dump('category-after-return');
run('shell', 'input', 'keyevent', '4');
nodes = dump('root', ns => ns.some(n => n.text === '手机示例'));
const category = nodes.find(n => n.text === '听音模式');
assert.ok(category, 'Visible library entry is missing.'); tap(category);
nodes = dump('category-from-root', ns => ns.some(n => n.text === '四档听音模式'));
tap(nodes.find(n => n.text === '四档听音模式'));
nodes = dump('opened-from-root', ns => options(ns, '主动降噪').length === 1);
checks.push('visible root library entry navigates to the new component'); shot('opened-from-root');
const pid = run('shell', 'pidof', 'dev.liquidglass.sample').trim();
assert.ok(pid, 'Application exited.');
const log = run('logcat', '-d', '--pid=' + pid, '-t', '2000');
assert.doesNotMatch(log, /FATAL EXCEPTION|Fatal signal/, 'Application crash found.');
fs.writeFileSync(path.join(out, 'logcat.txt'), log);
fs.writeFileSync(path.join(out, 'interaction-report.json'), JSON.stringify({
  device, checks, source: 'user screenshot, newly implemented vectors; not Figma/Apple original paths',
  referencePixelParity: 'not tested',
}, null, 2) + '\n');
console.log(`PASS: ${checks.length} device checks on ${device}.`);
