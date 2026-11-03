// Locate horizontal bands of a target color in a screenshot; prints y-ranges in px and pt(÷3.164).
// Usage: node find-bands.mjs <png> <#hexcolor> [tolerance]
import fs from 'node:fs';
import {PNG} from 'pngjs';

const png = PNG.sync.read(fs.readFileSync(process.argv[2]));
const hex = process.argv[3].replace('#', '');
const tol = Number(process.argv[4] || 40);
const tr = parseInt(hex.slice(0, 2), 16), tg = parseInt(hex.slice(2, 4), 16), tb = parseInt(hex.slice(4, 6), 16);
const rows = [];
for (let y = 0; y < png.height; y++) {
    let count = 0;
    for (let x = 0; x < png.width; x++) {
        const i = (y * png.width + x) * 4;
        if (Math.abs(png.data[i] - tr) <= tol && Math.abs(png.data[i + 1] - tg) <= tol && Math.abs(png.data[i + 2] - tb) <= tol) count++;
    }
    rows.push(count);
}
const minCount = png.width * 0.1;
let start = -1;
for (let y = 0; y <= png.height; y++) {
    const hit = y < png.height && rows[y] > minCount;
    if (hit && start < 0) start = y;
    if (!hit && start >= 0) {
        console.log(`y ${start}..${y - 1}  center=${((start + y - 1) / 2).toFixed(0)}px  pt=${(((start + y - 1) / 2) / 3.164).toFixed(1)}`);
        start = -1;
    }
}
