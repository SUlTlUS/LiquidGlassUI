import fs from 'node:fs';
import path from 'node:path';
import crypto from 'node:crypto';
import {execFileSync} from 'node:child_process';
import {PNG} from 'pngjs';
import {createTempOutputDir} from '../temp-output.mjs';

// Pixel comparison between the live native page and the offline vector reference
// (FigmaSceneView) of the same iPhone example, both captured on the same device.
// Usage: node compare-native-pixels.mjs <device> [key ...]
const root = path.resolve(import.meta.dirname, '../..');
const device = process.argv[2] || 'emulator-5556';
const args = process.argv.slice(3);
const scratch = args.includes('--scratch');
const requested = args.filter(arg => arg !== '--scratch');
const adb = 'C:/Users/29373/AppData/Local/Android/Sdk/platform-tools/adb.exe';
const out = scratch
    ? createTempOutputDir('iphone-examples-compare')
    : path.join(root, process.env.EXAMPLE_COMPARE_OUT || 'docs/iphone-examples-compare');
console.log(`Output: ${path.relative(root, out)}`);
for (const dir of ['native', 'reference', 'diff']) fs.mkdirSync(path.join(out, dir), {recursive: true});
const catalog = JSON.parse(fs.readFileSync(path.join(root, 'sample/src/main/assets/iphone-examples/catalog.json')));
const entries = requested.length ? catalog.filter(e => requested.includes(e.key)) : catalog;
const apkSha256 = crypto.createHash('sha256').update(fs.readFileSync(path.join(root,'sample/build/outputs/apk/debug/sample-debug.apk'))).digest('hex');
const call = (...args) => execFileSync(adb, ['-s', device, ...args], {encoding: 'utf8', timeout: 30000});
const pause = ms => new Promise(r => setTimeout(r, ms));

// The Figma originals are light designs; force light mode for the duration and restore after.
const originalNight = call('shell', 'cmd', 'uimode', 'night').match(/Night mode: (auto|yes|no|custom)/)?.[1];
if (originalNight && originalNight !== 'no') call('shell', 'cmd', 'uimode', 'night', 'no');
process.on('exit', () => {
    if (originalNight && originalNight !== 'no') try { call('shell', 'cmd', 'uimode', 'night', originalNight); } catch {}
});

const launch = async (entry, reference, marker) => {
    call('shell', 'am', 'force-stop', 'dev.liquidglass.sample');
    const args = ['shell', 'am', 'start', '-W', '-n', 'dev.liquidglass.sample/.MainActivity', '--es', 'example_key', entry.key];
    if (reference) args.push('--ez', 'example_reference', 'true');
    call(...args);
    const pid = call('shell', 'pidof', 'dev.liquidglass.sample').trim();
    for (let i = 0; i < 60; i++) {
        const log = call('logcat', '-d', `--pid=${pid}`, '-s', 'IphoneExamples:I');
        if (log.includes(marker)) return pid;
        await pause(250);
    }
    throw new Error(`${entry.key}: marker not seen: ${marker}`);
};
const shot = (file) => {
    call('shell', 'screencap', '-p', '/sdcard/compare.png');
    call('pull', '/sdcard/compare.png', file);
};

const compare = (nativeFile, referenceFile, diffFile) => {
    const a = PNG.sync.read(fs.readFileSync(nativeFile));
    const b = PNG.sync.read(fs.readFileSync(referenceFile));
    if (a.width !== b.width || a.height !== b.height)
        return {error: `size mismatch native ${a.width}x${a.height} vs reference ${b.width}x${b.height}`};
    const heat = new PNG({width: a.width, height: a.height});
    let sum = 0, over = 0;
    const n = a.width * a.height, THRESHOLD = 24;
    for (let i = 0; i < n * 4; i += 4) {
        const d = Math.max(
            Math.abs(a.data[i] - b.data[i]),
            Math.abs(a.data[i + 1] - b.data[i + 1]),
            Math.abs(a.data[i + 2] - b.data[i + 2]));
        sum += d;
        const hot = d > THRESHOLD;
        if (hot) over++;
        heat.data[i] = hot ? 255 : b.data[i];
        heat.data[i + 1] = hot ? 0 : b.data[i + 1];
        heat.data[i + 2] = hot ? 0 : b.data[i + 2];
        heat.data[i + 3] = 255;
    }
    fs.writeFileSync(diffFile, PNG.sync.write(heat));
    return {meanDiff: +(sum / n).toFixed(2), changedPercent: +(over / n * 100).toFixed(2), similarity: +((1-over/n)*100).toFixed(2)};
};

const results = [];
const save = () => {
 const sorted=[...results].sort((a,b)=>(a.similarity??-1)-(b.similarity??-1));
 fs.writeFileSync(path.join(out,'report.json'),JSON.stringify({device,checkedAt:new Date().toISOString(),apkSha256,
  completed:results.length===entries.length,expected:entries.length,
  note:'Similarity = percentage of pixels with max RGB delta <=24 against local FigmaSceneView. Includes system bars; blank background can inflate scores. Not official Figma fidelity or interaction acceptance.',results:sorted},null,2));
 fs.writeFileSync(path.join(out,'ranking.md'),'# 手机示例相似度（低分优先）\n\n设备：`'+device+'`；浅色、首屏；相似值=RGB 最大通道差≤24的像素占比。参考为本地 FigmaSceneView，不是 Figma 官方截图。空白背景会抬高数值；不能单凭分数认定像素一致。\n\n'+
 `已扫描 ${results.length}/${entries.length}。APK SHA-256：${apkSha256}\n\n`+
 '| 排名 | 示例 | 变体 | 相似值 | 平均差 | 对比图 |\n|---|---|---|---:|---:|---|\n'+
 sorted.map((r,i)=>`| ${i+1} | ${r.category} | ${r.variant} | ${r.error??r.similarity+'%'} | ${r.meanDiff??'-'} | [实现](native/${r.key}.png) / [参考](reference/${r.key}.png) / [差异](diff/${r.key}.png) |`).join('\n'));
};
for (const entry of entries) {
  try {
    await launch(entry, false, `NATIVE_READY ${entry.key}`);
    // NATIVE_READY is a composition marker, not GPU presentation completion.
    // Cold emulators may still be showing the previous directory frame at 600ms.
    await pause(4000);
    const nativeFile = path.join(out, 'native', entry.key + '.png');
    shot(nativeFile);
    await launch(entry, true, `SCENE_DRAWN ${entry.nodeId} `);
    await pause(2000);
    const referenceFile = path.join(out, 'reference', entry.key + '.png');
    shot(referenceFile);
    const diff = compare(nativeFile, referenceFile, path.join(out, 'diff', entry.key + '.png'));
    results.push({key: entry.key, category: entry.category, variant: entry.variant, ...diff});
    console.log(`${results.length}/${entries.length} ${entry.key} ${entry.category}: ${entry.variant} -> ${diff.error ?? `mean ${diff.meanDiff}, changed ${diff.changedPercent}%`}`);
  } catch(error) {
    results.push({key:entry.key,category:entry.category,variant:entry.variant,error:String(error.message)});
    console.log(`ERROR ${entry.key}: ${error.message}`);
  }
  save();
}
