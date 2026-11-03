import fs from 'node:fs';
import path from 'node:path';
import { execFileSync } from 'node:child_process';
import { Resvg } from '@resvg/resvg-js';
import crypto from 'node:crypto';
const root = path.resolve(import.meta.dirname, '../..');
const adb = 'C:/Users/29373/AppData/Local/Android/Sdk/platform-tools/adb.exe';
const device = process.argv[2] || 'emulator-5556';
const out = path.join(root,'docs/iphone-examples-android');
fs.mkdirSync(out,{recursive:true});
const entries = JSON.parse(fs.readFileSync(path.join(root,'sample/src/main/assets/iphone-examples/catalog.json')));
const call = (...args)=>execFileSync(adb,['-s',device,...args],{encoding:'utf8',timeout:30000});
const results = [];
const startedAt = new Date().toISOString();
for(const entry of entries){
 call('shell','am','force-stop','dev.liquidglass.sample');
 call('shell','am','start','-W','-n','dev.liquidglass.sample/.MainActivity','--es','example_key',entry.key);
 const pid=call('shell','pidof','dev.liquidglass.sample').trim();
 let drawn = false;
 for (let attempt = 0; attempt < 40; attempt++) {
  const log = call('logcat','-d',`--pid=${pid}`,'-s','IphoneExamples:I');
  if (log.includes(`SCENE_DRAWN ${entry.nodeId} `)) { drawn = true; break; }
  await new Promise(r=>setTimeout(r,250));
 }
 if (!drawn) throw new Error(`${entry.key}: process started but the requested scene was not drawn`);
 await new Promise(r=>setTimeout(r,150));
 call('shell','screencap','-p','/sdcard/iphone-example.png');
 call('pull','/sdcard/iphone-example.png',path.join(out,entry.key+'.png'));
 results.push({key:entry.key,category:entry.category,variant:entry.variant,pid,drawn});
 console.log(`${results.length}/${entries.length} ${entry.category} ${entry.variant}`);
}
fs.writeFileSync(path.join(out,'launch-report.json'),JSON.stringify(results,null,2));
fs.writeFileSync(path.join(out,'verification.json'),JSON.stringify({startedAt,finishedAt:new Date().toISOString(),device,
 apkSha256:crypto.createHash('sha256').update(fs.readFileSync(path.join(root,'sample/build/outputs/apk/debug/sample-debug.apk'))).digest('hex'),
 scenesDrawn:results.length,visualParity:false,physicalDevice:!device.startsWith('emulator-')},null,2));
for(let page=0;page<Math.ceil(entries.length/12);page++){
 const cells=entries.slice(page*12,page*12+12).map((e,i)=>{
  const x=(i%4)*260,y=Math.floor(i/4)*620;
  const bytes=fs.readFileSync(path.join(out,e.key+'.png'));
  return `<g transform="translate(${x} ${y})"><image width="250" height="565" href="data:image/png;base64,${bytes.toString('base64')}"/><text x="6" y="585" font-size="12">${e.category.replaceAll('&','&amp;')}</text><text x="6" y="603" font-size="11">${e.variant.replaceAll('&','&amp;')}</text></g>`;
 }).join('');
 const svg=`<svg xmlns="http://www.w3.org/2000/svg" width="1040" height="1860"><rect width="100%" height="100%" fill="#ddd"/>${cells}</svg>`;
 fs.writeFileSync(path.join(out,`contact-${page+1}.png`),new Resvg(svg).render().asPng());
}
