import fs from 'node:fs';
import path from 'node:path';
import { Resvg } from '@resvg/resvg-js';
const root = path.resolve(import.meta.dirname, '../..');
const assets = path.join(root,'sample/src/main/assets/iphone-examples');
const output = path.join(root,'docs/iphone-examples-previews');
fs.mkdirSync(output,{recursive:true});
const catalog = JSON.parse(fs.readFileSync(path.join(assets,'catalog.json')));
const esc = s=>s.replaceAll('&','&amp;').replaceAll('"','&quot;').replaceAll('<','&lt;');
const color = c=>c?`rgba(${Math.round(c.r*255)},${Math.round(c.g*255)},${Math.round(c.b*255)},${c.a??1})`:'black';
function commands(a){let s='',i=0;const op=['Z','M','L','Q','C'],counts=[0,2,2,4,6];while(i<a.length){const k=a[i++];if(k!==0||s.length)s+=op[k]+a.slice(i,i+counts[k]).join(' ')+' ';i+=counts[k];}return s;}
function node(n){
 if(n.name?.startsWith('Scroll Edge Effect'))return '';
 const [a,c,e,b,d,f]=n.matrix;
 const bounds=`<rect width="${n.w}" height="${n.h}" rx="${n.radius}"/>`;
 let inner='';
 for(const fill of n.fills){
  let shape='';
  if(fill.imageHash){const bytes=fs.readFileSync(path.join(assets,'images',fill.imageHash));shape=`<image width="${n.w}" height="${n.h}" preserveAspectRatio="xMidYMid slice" href="data:image/png;base64,${bytes.toString('base64')}"/>`;}
  else if(n.glyphs.length)shape=n.glyphs.map(g=>`<path d="${commands(g.path)}" transform="translate(${g.x} ${g.y}) scale(${g.size} ${-g.size})"/>`).join('');
  else if(n.paths.length)shape=n.paths.map(p=>`<path d="${commands(p.commands)}" fill-rule="${p.rule==='ODD'?'evenodd':'nonzero'}"/>`).join('');
  else if(n.type!=='TEXT')shape=bounds;
  inner+=`<g fill="${color(fill.color)}" opacity="${fill.opacity}">${shape}</g>`;
 }
 for(const fill of n.strokes) inner+=`<g fill="${color(fill.color)}">${n.strokePaths.map(p=>`<path d="${commands(p.commands)}"/>`).join('')}</g>`;
 inner+=n.children.map(node).join('');
 return `<g transform="matrix(${a} ${b} ${c} ${d} ${e} ${f})" opacity="${n.opacity}">${inner}</g>`;
}
for(const entry of catalog){
 const tree=JSON.parse(fs.readFileSync(path.join(assets,entry.file)));
 const svg=`<svg xmlns="http://www.w3.org/2000/svg" width="402" height="812" viewBox="0 62 402 812">${node(tree)}</svg>`;
 fs.writeFileSync(path.join(output,entry.key+'.svg'),svg);
 fs.writeFileSync(path.join(output,entry.key+'.png'),new Resvg(svg).render().asPng());
}
console.log(`Rendered ${catalog.length} structural previews (native Android remains the visual acceptance target).`);
