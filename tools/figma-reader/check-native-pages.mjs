import fs from 'node:fs';
import path from 'node:path';
import crypto from 'node:crypto';
import assert from 'node:assert/strict';
import {execFileSync} from 'node:child_process';
const root=path.resolve(import.meta.dirname,'../..');
const device=process.argv[2] || 'emulator-5556';
const adb='C:/Users/29373/AppData/Local/Android/Sdk/platform-tools/adb.exe';
const out=path.join(root,'docs/iphone-examples-native/pages');
fs.mkdirSync(out,{recursive:true});
const catalog=JSON.parse(fs.readFileSync(path.join(root,'sample/src/main/assets/iphone-examples/catalog.json')));
const call=(...args)=>execFileSync(adb,['-s',device,...args],{encoding:'utf8',timeout:30000});
const passed=[];
for(const entry of catalog){
 call('shell','am','force-stop','dev.liquidglass.sample');
 call('shell','am','start','-W','-n','dev.liquidglass.sample/.MainActivity','--es','example_key',entry.key);
 const pid=call('shell','pidof','dev.liquidglass.sample').trim();
 let ready=false;
 for(let i=0;i<40;i++) {
  if(call('logcat','-d',`--pid=${pid}`,'-s','IphoneExamples:I').includes(`NATIVE_READY ${entry.key}`)) {ready=true;break;}
  await new Promise(r=>setTimeout(r,250));
 }
 assert.ok(ready,`Expected native framework page: ${entry.key}`);
 await new Promise(r=>setTimeout(r,500));
 call('shell','screencap','-p','/sdcard/native-page.png');
 call('pull','/sdcard/native-page.png',path.join(out,entry.key+'.png'));
 passed.push({key:entry.key,category:entry.category,variant:entry.variant,pid});
 console.log(`${passed.length}/${catalog.length} ${entry.category}: ${entry.variant}`);
}
fs.writeFileSync(path.join(out,'report.json'),JSON.stringify({device,checkedAt:new Date().toISOString(),
 apkSha256:crypto.createHash('sha256').update(fs.readFileSync(path.join(root,'sample/build/outputs/apk/debug/sample-debug.apk'))).digest('hex'),
 scope:'Native composition and route smoke; interaction outcomes and accessibility are verified separately.',passed},null,2));
