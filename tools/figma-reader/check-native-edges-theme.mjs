import fs from 'node:fs';
import path from 'node:path';
import assert from 'node:assert/strict';
import {execFileSync} from 'node:child_process';
const root=path.resolve(import.meta.dirname,'../..');
const device=process.argv[2] || 'emulator-5556';
assert.ok(device.startsWith('emulator-'),'This test changes theme only on a disposable emulator');
const adb='C:/Users/29373/AppData/Local/Android/Sdk/platform-tools/adb.exe';
const out=path.join(root,'docs/iphone-examples-native');
const call=(...args)=>execFileSync(adb,['-s',device,...args],{encoding:'utf8',timeout:30000});
const pause=ms=>new Promise(r=>setTimeout(r,ms));
const ui=async()=>{
 for(let i=0;i<5;i++) {
  if(call('shell','uiautomator','dump','/sdcard/native-edge.xml').includes('dumped')) return call('shell','cat','/sdcard/native-edge.xml');
  await pause(1000);
 }
 throw new Error('No UI hierarchy');
};
const nodes=xml=>[...xml.matchAll(/<node\b[^>]*>/g)].map(m=>Object.fromEntries([...m[0].matchAll(/([\w:-]+)="([^"]*)"/g)].map(a=>[a[1],a[2]])));
const screenshot=name=>{call('shell','screencap','-p','/sdcard/native-edge.png');call('pull','/sdcard/native-edge.png',path.join(out,name+'.png'));};
const original=call('shell','cmd','uimode','night').match(/Night mode: (auto|yes|no|custom)/)?.[1];
assert.ok(original,'Cannot safely restore emulator theme');
try {
 call('shell','cmd','uimode','night','yes');
 call('shell','am','force-stop','dev.liquidglass.sample');
 call('shell','am','start','-W','-n','dev.liquidglass.sample/.MainActivity','--es','example_key','12740-35186');
 await pause(1000);
 const control=nodes(await ui()).find(n=>n['content-desc']==='滑块 1');
 assert.ok(control);
 const [l,t,r,b]=[...control.bounds.matchAll(/\d+/g)].map(m=>Number(m[0]));
 const y=(t+b)>>1;
 call('shell','input','swipe',String(l+10),String(y),String(r-1),String(y),'650');
 await pause(600);
 assert.ok((await ui()).includes('滑块 1 已设为 100%'));
 screenshot('slider-dark-right');
 call('shell','input','swipe',String(r-10),String(y),String(l+1),String(y),'650');
 await pause(600);
 assert.ok((await ui()).includes('滑块 1 已设为 0%'));
 screenshot('slider-dark-left');
 fs.writeFileSync(path.join(out,'edges-theme-report.json'),JSON.stringify({device,checkedAt:new Date().toISOString(),
   passed:['slider reaches 100 percent','slider returns to zero'],themeRequested:'dark',
   note:'Inspect screenshots for theme colors and resting thumb edges; no automatic pixel-parity claim.'},null,2));
 console.log('PASS: slider endpoints in dark theme');
} finally {call('shell','cmd','uimode','night',original);}
