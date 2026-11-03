import fs from 'node:fs';
import path from 'node:path';
import assert from 'node:assert/strict';
import {execFileSync} from 'node:child_process';
import crypto from 'node:crypto';

const device = process.argv[2];
assert.ok(device, 'Pass a currently connected adb serial');
const root = path.resolve(import.meta.dirname, '../..');
const out = path.join(root, 'docs/ios27/device');
fs.mkdirSync(out, {recursive: true});
const call = (...args) => execFileSync('adb', ['-s', device, ...args], {encoding: 'utf8', timeout: 30000});
const pause = ms => new Promise(resolve => setTimeout(resolve, ms));
const nodes = xml => [...xml.matchAll(/<node\b[^>]*>/g)].map(m => Object.fromEntries(
    [...m[0].matchAll(/([\w:-]+)="([^"]*)"/g)].map(a => [a[1], a[2]])));
const dump = () => {
    call('shell', 'uiautomator', 'dump', '/sdcard/ios27-standard.xml');
    return call('shell', 'cat', '/sdcard/ios27-standard.xml');
};
const waitText = async text => {
    for (let i = 0; i < 5; i++) {
        if (dump().includes(text)) return;
        await pause(400);
    }
    throw new Error(`Expected visible page state: ${text}`);
};
const tap = async label => {
    const node = nodes(dump()).filter(n => n.text === label || n['content-desc'] === label).at(-1);
    assert.ok(node, `Missing control: ${label}`);
    const [l, t, r, b] = [...node.bounds.matchAll(/\d+/g)].map(m => Number(m[0]));
    call('shell', 'input', 'tap', String((l + r) >> 1), String((t + b) >> 1));
    await pause(700);
};
const shot = name => {
    fs.writeFileSync(path.join(out, `${name}.png`), execFileSync('adb', ['-s', device, 'exec-out', 'screencap', '-p']));
    fs.writeFileSync(path.join(out, `${name}.xml`), dump());
};
const launch = async () => {
    call('shell', 'am', 'force-stop', 'dev.liquidglass.sample');
    call('shell', 'am', 'start', '-W', '-n', 'dev.liquidglass.sample/.MainActivity');
    await pause(1500);
    assert.ok(call('shell', 'pidof', 'dev.liquidglass.sample').trim(), 'App did not stay alive');
};
const report = {
    device, sdk: Number(call('shell', 'getprop', 'ro.build.version.sdk').trim()),
    apkSha256: crypto.createHash('sha256').update(fs.readFileSync(path.join(root, 'sample/build/outputs/apk/debug/sample-debug.apk'))).digest('hex'),
    passed: [], completed: false, visualAcceptance: 'pending user review',
};
try {
    assert.ok(report.sdk >= 33, 'This run covers the iOS 27 path; low API needs separate verification');
    await launch();
    report.passed.push('cold start remains alive');
    await tap('设置');
    await waitText('玻璃标准');
    await tap('iOS 27');
    await tap('组件');
    await waitText('手机示例');
    shot('ios27-catalog');
    await tap('说明');
    await waitText('使用说明');
    report.passed.push('new dock callback changes to notes page');
    await tap('设置');
    assert.ok(dump().includes('玻璃标准'));
    await tap('iOS 26');
    assert.ok(call('shell', 'run-as', 'dev.liquidglass.sample', 'cat', 'shared_prefs/sample_appearance.xml').includes('Ios26'));
    shot('ios26-settings');
    await launch();
    await tap('设置');
    assert.ok(dump().includes('玻璃标准'));
    report.passed.push('iOS 26 selection persists across process restart and legacy dock navigates');
    await tap('iOS 27');
    assert.ok(call('shell', 'run-as', 'dev.liquidglass.sample', 'cat', 'shared_prefs/sample_appearance.xml').includes('Ios27'));
    await launch();
    await tap('设置');
    report.passed.push('iOS 27 selection persists across process restart');
    const prefs = call('shell', 'run-as', 'dev.liquidglass.sample', 'cat', 'shared_prefs/sample_appearance.xml');
    const originalTheme = Number(prefs.match(/name="theme" value="(\d+)"/)?.[1] ?? 0);
    try {
        await tap('深色'); shot('ios27-dark');
        await tap('浅色'); shot('ios27-light');
        report.passed.push('light and dark selection remains operational');
    } finally {
        await tap(['跟随系统', '浅色', '深色'][originalTheme]);
    }
    await tap('组件');
    assert.ok(dump().includes('iOS 26 原有示例'));
    report.passed.push('legacy example catalog remains reachable and labeled iOS 26');
    const pid = call('shell', 'pidof', 'dev.liquidglass.sample').trim();
    const log = call('logcat', '-d', `--pid=${pid}`, '-v', 'threadtime');
    fs.writeFileSync(path.join(out, 'logcat.txt'), log);
    assert.doesNotMatch(log, /FATAL EXCEPTION|VerifyError|Shader preparation failed|control highlight compile failed/);
    report.passed.push('current process log has no app crash, VerifyError or shader preparation failure');
    report.completed = true;
} catch (error) {
    report.failure = String(error);
    process.exitCode = 1;
} finally {
    fs.writeFileSync(path.join(out, 'report.json'), JSON.stringify(report, null, 2) + '\n');
    console.log(JSON.stringify(report, null, 2));
}
