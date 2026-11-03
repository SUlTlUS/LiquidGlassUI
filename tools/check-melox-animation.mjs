import fs from 'node:fs';
import path from 'node:path';
import os from 'node:os';
import assert from 'node:assert/strict';
import { createRequire } from 'node:module';
import { execFileSync, spawn } from 'node:child_process';
import { fileURLToPath } from 'node:url';

// Run after check-melox-device.mjs; that leaves the real catalog route open.
// This records intermediate frames, not merely a post-animation screenshot.
const root = path.resolve(path.dirname(fileURLToPath(import.meta.url)), '..');
const device = process.argv[2];
const ffmpeg = process.argv[3] ?? 'ffmpeg';
assert.ok(device, 'Usage: node tools/check-melox-animation.mjs <adb serial> [ffmpeg]');
const out = path.join(root, 'docs/melox-tabbar-validation');
const run = (...args) => execFileSync('adb', ['-s', device, ...args], { encoding: 'utf8', timeout: 30000 });
const wait = ms => new Promise(resolve => setTimeout(resolve, ms));
const xml = fs.readFileSync(path.join(out, 'opened-from-root.xml'), 'utf8');
const nodes = [...xml.matchAll(/<node\b([^>]+)>/g)].map(m => Object.fromEntries(
  [...m[1].matchAll(/([\w-]+)="([^"]*)"/g)].map(a => [a[1], a[2]]),
));
const center = n => {
  const b = n.bounds.match(/\d+/g).map(Number);
  return [(b[0] + b[2]) / 2, (b[1] + b[3]) / 2].map(Math.round);
};
const title = nodes.find(n => n.text === '空耳');
assert.ok(title, 'Expected the final Home mini-player from the device regression.');
const titlePoint = center(title);
const artistPoint = center(nodes.find(n => n.text === 'ナナツカゼ'));
const miniY = (titlePoint[1] + artistPoint[1]) / 2;
const play = nodes.filter(n => n.clickable === 'true' && !n['content-desc'] &&
  Math.abs(center(n)[1] - miniY) < 20 && center(n)[0] > titlePoint[0] &&
  Number(n.bounds.match(/\d+/g)[2]) - Number(n.bounds.match(/\d+/g)[0]) < 200)
  .sort((a, b) => center(a)[0] - center(b)[0])[0];
assert.ok(play);
run('shell', 'input', 'tap', ...center(play).map(String)); // Actual UI playing state, no invented beat analysis.
const remote = '/sdcard/melox-animation.mp4';
const recording = spawn('adb', ['-s', device, 'shell', 'screenrecord', '--size', '540x1200',
  '--bit-rate', '2000000', '--time-limit', '20', remote]);
const completion = new Promise((resolve, reject) => {
  recording.on('error', reject);
  recording.on('exit', code => code === 0 ? resolve() : reject(new Error(`screenrecord exit ${code}`)));
});
const started = performance.now();
const events = [];
function action(name, ...args) {
  events.push({ name, atMs: Math.round(performance.now() - started) });
  run('shell', 'input', ...args.map(String));
}
await wait(800);
action('expand-shared-player', 'tap', ...titlePoint);
await wait(1300);
action('reverse-by-system-back', 'keyevent', 4);
await wait(1300);
action('expand-again', 'tap', ...titlePoint);
await wait(1000);
action('short-drag-and-spring-back', 'swipe', 540, 700, 540, 900, 2500);
await wait(1100);
action('long-drag-to-mini', 'swipe', 540, 550, 540, 2150, 4500);
await wait(1000);
action('scroll-up-auto-shrink', 'swipe', 540, 1600, 540, 800, 450);
await wait(1000);
action('scroll-down-auto-expand', 'swipe', 540, 800, 540, 1600, 450);
await completion;
run('pull', remote, path.join(out, 'animation.mp4'));

const frames = fs.mkdtempSync(path.join(os.tmpdir(), 'melox-animation-'));
execFileSync(ffmpeg, ['-y', '-hide_banner', '-loglevel', 'error', '-i', path.join(out, 'animation.mp4'),
  '-vf', 'fps=12,scale=360:800', path.join(frames, '%03d.png')], { timeout: 30000 });
const { PNG } = createRequire(path.join(root, 'tools/figma-reader/package.json'))('pngjs');
const samples = [];
for (const file of fs.readdirSync(frames).filter(n => n.endsWith('.png')).sort()) {
  const p = PNG.sync.read(fs.readFileSync(path.join(frames, file)));
  // Connected saturated-red regions isolate the demo artwork, not all red tint.
  const mask = new Uint8Array(p.width * p.height);
  for (let i = 0; i < mask.length; i++) {
    const j = i * 4;
    mask[i] = p.data[j] > 238 && p.data[j + 1] < 42 && p.data[j + 2] < 65 ? 1 : 0;
  }
  let largest = null;
  for (let i = 0; i < mask.length; i++) {
    if (!mask[i]) continue;
    mask[i] = 0;
    const q = [i]; let cursor = 0;
    let left = p.width, top = p.height, right = 0, bottom = 0;
    while (cursor < q.length) {
      const at = q[cursor++], x = at % p.width, y = Math.floor(at / p.width);
      left = Math.min(left, x); right = Math.max(right, x);
      top = Math.min(top, y); bottom = Math.max(bottom, y);
      for (const n of [x ? at - 1 : -1, x + 1 < p.width ? at + 1 : -1, at - p.width, at + p.width]) {
        if (n >= 0 && n < mask.length && mask[n]) { mask[n] = 0; q.push(n); }
      }
    }
    if (!largest || q.length > largest.count) largest = { count: q.length, left, top, right, bottom };
  }
  if (largest && largest.count > 30) samples.push({ file, atMs: Math.round((Number(file.slice(0, 3)) - 1) * 1000 / 12),
    width: largest.right - largest.left + 1, height: largest.bottom - largest.top + 1, ...largest });
}
const intermediate = samples.filter(s => s.width > 45 && s.width < 190 && Math.abs(s.width - s.height) < 45);
const distinctIntermediateSizes = new Set(intermediate.map(s => Math.round(s.width / 5))).size;
fs.writeFileSync(path.join(out, 'animation-report.json'), JSON.stringify({
  device, video: 'animation.mp4', events, decodedFrameDirectory: frames,
  measuredFrames: samples, intermediateFrames: intermediate.length, distinctIntermediateSizes,
  visualParityWithOriginalApk: 'not tested',
  scope: 'Continuity evidence during the long shared drag. Resampling to 12fps can repeat captured frames; count distinct sizes, not PNG count. Saturated-red component bounds are approximate artwork markers, not exact shared-element bounds. Not a timing benchmark or original APK pixel comparison.',
}, null, 2) + '\n');
assert.ok(distinctIntermediateSizes >= 3,
  'No multiple intermediate artwork sizes detected; do not claim a continuous shared flight.');
for (const [name, s] of [['flight-intermediate', intermediate[0]], ['drag-intermediate', intermediate.at(-1)]]) {
  fs.copyFileSync(path.join(frames, s.file), path.join(out, name + '.png'));
}
console.log(`PASS: continuous shared artwork flight (${intermediate.length} intermediate frames), gestures and scroll recorded.`);
