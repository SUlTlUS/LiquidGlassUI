// Dump absolute scene coordinates (pt, after the reference view's -62pt top shift)
// for nodes of interest in an exported iPhone example scene.
// Usage: node measure-example.mjs <key> [nameFilterRegex]
import fs from 'node:fs';
import path from 'node:path';

const root = path.resolve(import.meta.dirname, '../..');
const key = process.argv[2];
const filter = new RegExp(process.argv[3] || '.');
const scene = JSON.parse(fs.readFileSync(path.join(root, `sample/src/main/assets/iphone-examples/${key}.json`)));
const rootNode = scene.scene || scene;
const I = [1, 0, 0, 0, 1, 0];

// 2x3 affine [a,b,tx,c,d,ty]: (x,y) -> (a*x + c*y + tx, b*x + d*y + ty)
const mapPt = (m, x, y) => [m[0] * x + m[3] * y + m[2], m[1] * x + m[4] * y + m[5]];
const compose = (p, c) => [
    p[0] * c[0] + p[3] * c[1],
    p[1] * c[0] + p[4] * c[1],
    p[0] * c[2] + p[3] * c[5] + p[2],
    p[0] * c[3] + p[3] * c[4],
    p[1] * c[3] + p[4] * c[4],
    p[1] * c[2] + p[4] * c[5] + p[5]];

const walk = (n, parent, depth) => {
    const world = compose(parent, n.matrix || I);
    const [x0, y0] = mapPt(world, 0, 0);
    const [x1, y1] = mapPt(world, n.w || 0, n.h || 0);
    const name = n.name || '';
    if (filter.test(name)) {
        const fills = (n.fills || []).map(f => f.color ? `#${[f.color.r, f.color.g, f.color.b].map(v => Math.round(v * 255).toString(16).padStart(2, '0')).join('')}@${(f.color.a ?? 1).toFixed(2)}` : f.type).join(',');
        console.log(`${' '.repeat(depth)}${name} [${n.type}] x=${x0.toFixed(1)} y=${(y0 - 62).toFixed(1)} w=${(x1 - x0).toFixed(1)} h=${(y1 - y0).toFixed(1)}${n.text !== undefined ? ` text=${JSON.stringify(n.text)}` : ''} ${fills}`);
    }
    (n.children || []).forEach(c => walk(c, world, depth + 1));
};
walk(rootNode, I, 0);
