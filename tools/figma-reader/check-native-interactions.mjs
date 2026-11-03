import fs from 'node:fs';
import path from 'node:path';
import assert from 'node:assert/strict';
import crypto from 'node:crypto';
import {execFileSync} from 'node:child_process';
const root = path.resolve(import.meta.dirname, '../..');
const device = process.argv[2] || 'emulator-5556';
const adb = 'C:/Users/29373/AppData/Local/Android/Sdk/platform-tools/adb.exe';
const out = path.join(root, 'docs/iphone-examples-native');
fs.mkdirSync(out, {recursive:true});
const call = (...args) => execFileSync(adb, ['-s',device,...args], {encoding:'utf8',timeout:30000});
const pause = ms => new Promise(r=>setTimeout(r,ms));
const dump = () => {
    for(let i=0;i<5;i++) {
        if(call('shell','uiautomator','dump','/sdcard/native-example.xml').includes('dumped'))
            return call('shell','cat','/sdcard/native-example.xml');
        call('shell','sleep','1');
    }
    throw new Error('UI hierarchy was not ready after five attempts');
};
const nodes = xml => [...xml.matchAll(/<node\b[^>]*>/g)].map(m=>Object.fromEntries([...m[0].matchAll(/([\w:-]+)="([^"]*)"/g)].map(a=>[a[1],a[2]])));
const bounds = n => {assert.ok(n, 'UI control not found'); return [...n.bounds.matchAll(/\d+/g)].map(m=>Number(m[0]));};
const tap = async label => {
    let n;
    for (let attempt = 0; attempt < 5 && !n; attempt++) {
        const all = nodes(dump());
        n = all.find(n => n['content-desc'] === label) ?? all.find(n => n.text === label);
        if (!n) await pause(250);
    }
    const [l,t,r,b]=bounds(n); call('shell','input','tap',String((l+r)>>1),String((t+b)>>1)); await pause(350);
};
const longPress = async (x, y) => {
    call('shell','input','swipe',String(x),String(y),String(x + 1),String(y + 1),'1200');
    await pause(700);
};
const shot = name => {call('shell','screencap','-p','/sdcard/native-example.png');call('pull','/sdcard/native-example.png',path.join(out,name+'.png'));};
const launch = async key => {
    call('shell','am','force-stop','dev.liquidglass.sample');
    call('shell','am','start','-W','-n','dev.liquidglass.sample/.MainActivity','--es','example_key',key);
    await pause(900);
    assert.ok(dump().includes(`可操作示例 ${key}`),`Not a live page: ${key}`);
};
const apkSha256=crypto.createHash('sha256').update(fs.readFileSync(path.join(root,'sample/build/outputs/apk/debug/sample-debug.apk'))).digest('hex');
const reportFile=path.join(out,'interaction-report.json');
const previous=process.argv.includes('--resume') && fs.existsSync(reportFile) ? JSON.parse(fs.readFileSync(reportFile)) : null;
const passed=previous?.apkSha256===apkSha256 ? previous.passed : [];
process.on('exit', code => fs.writeFileSync(path.join(out,'interaction-report.json'), JSON.stringify({device,
    checkedAt:new Date().toISOString(), completed:code===0, passed,
    apkSha256,
},null,2)));
if(!passed.includes('state survives reference round trip')) {
await launch('12740-35193');
await tap('示例开关一');
assert.equal(nodes(dump()).filter(n=>n.text==='已关闭').length,2);
shot('toggle-off'); passed.push('toggle updates real checked state');
await longPress(540, 127); await pause(700);
call('shell','input','keyevent','4'); await pause(500);
assert.equal(nodes(dump()).filter(n=>n.text==='已关闭').length,2);
passed.push('state survives reference round trip');
}

if(!passed.includes('stepper increments and decrements')) {
await launch('12740-35192');
const stepperDecrement = () => nodes(dump()).find(n=>n['content-desc']==='Stepper 减少');
assert.equal(stepperDecrement()?.enabled,'false');
await tap('Stepper 增加');
assert.equal(stepperDecrement()?.enabled,'true');
await tap('Stepper 减少');
assert.equal(stepperDecrement()?.enabled,'false');
shot('stepper'); passed.push('stepper increments and decrements');
}

if(!passed.includes('text fields show four states, accept input and clear typing')) {
await launch('12740-35194');
for(const state of ['Placeholder','Focused','Typing','Value Entered'])
    assert.ok(nodes(dump()).some(n=>n['content-desc']===`${state} 输入框`),state);
assert.ok(nodes(dump()).some(n=>n.text==='Placeholder'));
assert.ok(nodes(dump()).some(n=>n['content-desc']==='清除 Typing 输入框'));
await tap('清除 Typing 输入框');
assert.ok(!nodes(dump()).some(n=>n['content-desc']==='清除 Typing 输入框'));
await tap('Placeholder 输入框');
call('shell','input','text','framework-test'); call('shell','input','keyevent','4');
assert.ok(nodes(dump()).some(n=>n.text==='framework-test'));
shot('text-submit'); passed.push('text fields show four states, accept input and clear typing');
}

if(!passed.includes('alert fields reuse focus, clear and placeholder behavior')) {
await launch('12740-24488');
await tap('显示提示弹窗');
assert.ok(nodes(dump()).some(n=>n['content-desc']==='第一个输入框'));
await tap('第一个输入框');
assert.ok(nodes(dump()).some(n=>n['content-desc']==='清除 第一个输入框'));
await tap('清除 第一个输入框');
assert.ok(!nodes(dump()).some(n=>n['content-desc']==='清除 第一个输入框'));
await tap('第二个输入框');
assert.ok(nodes(dump()).some(n=>n.text==='Value'));
await tap('第一个输入框');
call('shell','input','text','dialog-test'); call('shell','input','keyevent','4');
assert.ok(dump().includes('dialog-test'));
shot('alert-text-field'); passed.push('alert fields reuse focus, clear and placeholder behavior');
}

if(!passed.includes('slider drag updates float state and calls finish callback')) {
await launch('12740-35186');
const slider=nodes(dump()).find(n=>n['content-desc']==='滑块 1');
const [l,t,r,b]=bounds(slider), y=(t+b)>>1;
call('shell','input','swipe',String(l+20),String(y),String(Math.round(l+(r-l)*0.8)),String(y),'650');
await pause(700);
const updated=nodes(dump()).find(n=>n.text?.startsWith('滑块 1 已设为'));
assert.ok(updated && Number(updated.text.match(/设为 (\d+)%/)?.[1]) > 0,JSON.stringify(updated));
shot('slider-drag'); passed.push('slider drag updates float state and calls finish callback');
}

if(!passed.includes('menu opens and invokes clipboard action')) {
await launch('12740-24157');
await tap('内容'); call('shell','input','text','menu-value'); call('shell','input','keyevent','4');
await tap('打开菜单'); await tap('Cut');
await tap('打开菜单');
assert.ok(nodes(dump()).some(n=>n.text==='Paste'));
await tap('Paste');
assert.ok(dump().includes('已粘贴'));
assert.ok(nodes(dump()).some(n=>n.text==='menu-value'));
shot('menu'); passed.push('menu opens and invokes clipboard action');
}

if(!passed.includes('sheet opens, action and dismiss are real')) {
await launch('12740-24098');
await tap('打开面板');
assert.ok(nodes(dump()).some(n=>n['content-desc']==='模态面板 Type=Fullscreen'));
assert.ok(nodes(dump()).some(n=>n['content-desc']==='关闭模态页面'));
await tap('模态页面操作');
assert.ok(nodes(dump()).some(n=>n['content-desc']==='模态页面操作 已触发'));
await tap('关闭模态页面');
assert.ok(!dump().includes('模态面板 Type=Fullscreen'));
shot('sheet-interaction'); passed.push('sheet opens, action and dismiss are real');
}

if(!passed.includes('empty state creates a real list item')) {
await launch('12740-24439');
await tap('新建内容');await tap('编辑内容');call('shell','input','text','first-note');call('shell','input','keyevent','4');await tap('保存');
assert.ok(nodes(dump()).some(n=>n.text==='first-note'));
shot('empty-created'); passed.push('empty state creates a real list item');
}

if(!passed.includes('glass tab changes displayed content')) {
await launch('12740-24077');
await tap('收藏');
assert.ok(dump().includes('收藏 · 内容 1'));
shot('tab-selected');passed.push('glass tab changes displayed content');
}

console.log(passed.map(p=>'PASS: '+p).join('\n'));
