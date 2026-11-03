import fs from 'node:fs';
import path from 'node:path';
import assert from 'node:assert/strict';
import crypto from 'node:crypto';
import {execFileSync} from 'node:child_process';

const serial = process.argv[2];
assert.ok(serial, 'Pass a current adb serial');
const root = path.resolve(import.meta.dirname, '../..');
const out = path.join(root, 'docs/ios27/device/examples');
fs.mkdirSync(out, {recursive: true});
const call = (...args) => execFileSync('adb', ['-s', serial, ...args], {encoding: 'utf8', timeout: 30000});
const pause = ms => new Promise(r => setTimeout(r, ms));
const dump = () => {
    call('shell', 'uiautomator', 'dump', '/sdcard/ios27-examples.xml');
    return call('shell', 'cat', '/sdcard/ios27-examples.xml');
};
const nodes = xml => [...xml.matchAll(/<node\b[^>]*>/g)].map(m => Object.fromEntries(
    [...m[0].matchAll(/([\w:-]+)="([^"]*)"/g)].map(a => [a[1], a[2]])));
const waitText = async text => {
    for (let i = 0; i < 5; i++) {
        const xml = dump();
        if (xml.includes(text)) return xml;
        await pause(400);
    }
    throw new Error(`Expected visible state: ${text}`);
};
const tapNode = async node => {
    assert.ok(node, 'Expected control not found');
    const [l, t, r, b] = [...node.bounds.matchAll(/\d+/g)].map(m => Number(m[0]));
    call('shell', 'input', 'tap', String((l + r) >> 1), String((t + b) >> 1));
    await pause(700);
};
const tap = async label => tapNode(nodes(await waitText(label)).find(n => n.text === label || n['content-desc'] === label));
const shot = name => {
    fs.writeFileSync(path.join(out, `${name}.png`), execFileSync('adb', ['-s', serial, 'exec-out', 'screencap', '-p']));
    fs.writeFileSync(path.join(out, `${name}.xml`), dump());
};
const launch = async key => {
    call('shell', 'am', 'force-stop', 'dev.liquidglass.sample');
    call('shell', 'am', 'start', '-W', '-n', 'dev.liquidglass.sample/.MainActivity', '--es', 'example_key', key);
    await pause(1500);
};
const report = {
    serial, apkSha256: crypto.createHash('sha256').update(fs.readFileSync(path.join(root, 'sample/build/outputs/apk/debug/sample-debug.apk'))).digest('hex'),
    passed: [], completed: false, visualAcceptance: 'pending user review',
};
try {
    await launch('ios27-buttons');
    await waitText('弹性按钮'); shot('buttons');
    await tap('清透按钮 · 0'); await waitText('清透按钮已点击 1 次');
    await tap('清透按钮 · 1'); await waitText('清透按钮已点击 2 次');
    report.passed.push('elastic button increments actual state');
    await launch('ios27-goo');
    await tap('添加'); await waitText('已添加 1 项');
    await tap('重置'); await waitText('已重置操作组');
    report.passed.push('Goo callbacks add and reset state');
    await launch('ios27-dock');
    await tap('收藏'); await waitText('这里显示收藏的示例');
    report.passed.push('dock selection changes page content');
    await launch('ios27-menu');
    await tap('打开菜单'); await waitText('收藏示例'); shot('menu');
    await tap('排序'); await waitText('按最近使用');
    await tap('按最近使用'); await waitText('排序：按最近使用');
    report.passed.push('nested menu delivers leaf result and returns to page');

    await launch('ios27-dialog');
    await tap('填写名称');
    await tapNode(nodes(dump()).find(n => n.class === 'android.widget.EditText'));
    call('shell', 'input', 'text', 'Codex27');
    await tap('保存'); await waitText('名称已保存：Codex27');
    report.passed.push('dialog input submits actual typed text');
    await tap('填写名称'); await waitText('Codex27'); await tap('取消');
    await waitText('已取消，名称未更改');
    report.passed.push('saved name survives reopen and cancel');
    await launch('ios27-popup');
    await tap('编辑备注'); await waitText('示例备注'); shot('popup');
    await tapNode(nodes(dump()).find(n => n.class === 'android.widget.EditText'));
    call('shell', 'input', 'text', 'Note27');
    await tap('保存备注'); await waitText('备注已保存：Note27');
    await tap('编辑备注'); await waitText('Note27'); await tap('取消');
    await waitText('已取消，备注未更改');
    report.passed.push('popup save returns after animation; cancel preserves saved content');
    await launch('ios27-theme');
    await waitText('主题与图标');
    const styleNode = nodes(dump()).find(n => n.text?.startsWith('色彩风格：'));
    await tapNode(styleNode);
    assert.ok(!nodes(dump()).some(n => n.text === styleNode.text), 'Color style did not change');
    shot('theme');
    report.passed.push('host theme page changes style without a separate navigation shell');
    const pid = call('shell', 'pidof', 'dev.liquidglass.sample').trim();
    const log = call('logcat', '-d', `--pid=${pid}`, '-v', 'threadtime');
    fs.writeFileSync(path.join(out, 'logcat.txt'), log);
    assert.doesNotMatch(log, /FATAL EXCEPTION|VerifyError|Shader preparation failed|control highlight compile failed/);
    report.completed = true;
} catch (error) {
    report.failure = String(error); process.exitCode = 1;
    try { shot('failure'); } catch {}
} finally {
    fs.writeFileSync(path.join(out, 'report.json'), JSON.stringify(report, null, 2) + '\n');
    console.log(JSON.stringify(report, null, 2));
}
