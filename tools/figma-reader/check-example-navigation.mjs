import fs from 'node:fs';
import path from 'node:path';
import assert from 'node:assert/strict';
import { execFileSync } from 'node:child_process';

const root = path.resolve(import.meta.dirname, '../..');
const device = process.argv[2] || 'emulator-5556';
const adb = 'C:/Users/29373/AppData/Local/Android/Sdk/platform-tools/adb.exe';
const call = (...args) => execFileSync(adb, ['-s', device, ...args], { encoding: 'utf8', timeout: 30000 });
const pause = ms => new Promise(resolve => setTimeout(resolve, ms));
let pid;
const drawn = async key => {
    for (let i = 0; i < 40; i++) {
        if (call('logcat', '-d', `--pid=${pid}`, '-s', 'IphoneExamples:I').includes(`SCENE_DRAWN ${key.replace('-', ':')} `)) return;
        await pause(250);
    }
    throw new Error(`Scene did not render: ${key}`);
};
const launch = async key => {
    call('shell', 'am', 'force-stop', 'dev.liquidglass.sample');
    call('shell', 'am', 'start', '-W', '-n', 'dev.liquidglass.sample/.MainActivity', '--es', 'example_key', key);
    pid = call('shell', 'pidof', 'dev.liquidglass.sample').trim();
    await drawn(key);
};
const ui = () => {
    call('shell', 'uiautomator', 'dump', '/sdcard/iphone-example-navigation.xml');
    return call('shell', 'cat', '/sdcard/iphone-example-navigation.xml');
};
const result = [];
await launch('12740-24428');
call('shell', 'input', 'tap', '540', '1200');
await drawn('12740-24418');
assert.ok(ui().includes('Notifications: Expanded=True'));
result.push('notification tap opens expanded variant');
call('shell', 'input', 'keyevent', '4');
assert.ok(ui().includes('手机示例'));
result.push('system back returns to example catalog');

await launch('12740-24382');
call('shell', 'input', 'swipe', '500', '600', '500', '600', '800');
const dialog = ui();
assert.ok(dialog.includes('返回示例目录') && dialog.includes('Uppercase'));
result.push('long press exposes variant navigation');
call('shell', 'input', 'keyevent', '4');
assert.ok(ui().includes('Keyboard: Type=Number Pad'));
result.push('dismiss variant menu keeps the same scene');

await launch('12740-24401');
await drawn('12740-24409');
assert.ok(ui().includes('Face ID: State=Success'));
result.push('simulated Face ID transitions to success');

fs.writeFileSync(path.join(root, 'docs/iphone-examples-android/navigation-report.json'), JSON.stringify({
    checkedAt: new Date().toISOString(), device, passed: result,
    scope: 'Navigation smoke only. No slider, toggle, text-entry or visual-parity acceptance.',
}, null, 2));
console.log(result.map(name => `PASS: ${name}`).join('\n'));
