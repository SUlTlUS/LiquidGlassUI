import fs from 'node:fs';
import path from 'node:path';
import {PNG} from 'pngjs';
const root=path.resolve(import.meta.dirname,'../..');
const folder=path.resolve(root,process.argv[2]||'docs/iphone-examples-scan-2026-09-15');
const entries=JSON.parse(fs.readFileSync(path.join(root,'sample/src/main/assets/iphone-examples/catalog.json')));
for(let start=0;start<entries.length;start+=14){
 const sheet=new PNG({width:140*7,height:312*2}); sheet.data.fill(180);
 for(let j=0;j<14&&start+j<entries.length;j++){
  const file=path.join(folder,'native',entries[start+j].key+'.png');
  if(!fs.existsSync(file))continue;
  const src=PNG.sync.read(fs.readFileSync(file));
  for(let y=0;y<308;y++)for(let x=0;x<136;x++){
   const si=(Math.floor(y*src.height/308)*src.width+Math.floor(x*src.width/136))*4;
   const di=((Math.floor(j/7)*312+y)*sheet.width+j%7*140+x)*4;
   src.data.copy(sheet.data,di,si,si+4);
  }
 }
 const target=path.join(folder,`contact-${start+1}-${Math.min(start+14,entries.length)}.png`);
 fs.writeFileSync(target,PNG.sync.write(sheet)); console.log(target);
}
