import fs from 'node:fs';
import path from 'node:path';
import assert from 'node:assert/strict';
import test from 'node:test';

const root = path.resolve(import.meta.dirname, '../..');
const assets = path.join(root, 'sample/src/main/assets/iphone-examples');
const read = name => JSON.parse(fs.readFileSync(path.join(assets, name), 'utf8'));
const catalog = read('catalog.json');
const flatten = n => [n, ...n.children.flatMap(flatten)];

test('all 56 iPhone examples have unique keys and their original 402 x 874 coordinate space', () => {
    assert.equal(catalog.length, 56);
    assert.equal(new Set(catalog.map(e => e.key)).size, 56);
    for (const e of catalog) {
        const scene = read(e.file);
        assert.equal(scene.w, 402); assert.equal(scene.h, 874);
        const nodes = flatten(scene);
        assert.equal(new Set(nodes.map(n => n.instancePath)).size, nodes.length, e.key);
        for (const n of nodes) {
            assert.equal(n.matrix.length, 6);
            assert.ok(n.matrix.every(Number.isFinite), `${e.key}: ${n.instancePath}`);
            assert.ok(!/^(Bezel|Status bar(?: - iPhone)?|Home Indicator)$/.test(n.name));
            for (const fill of n.fills) if (fill.imageHash) assert.ok(fs.existsSync(path.join(assets, 'images', fill.imageHash)));
        }
    }
});

test('number pad uses the Light mode variable, not stale white cached colors', () => {
    const numbers = flatten(read('12740-24382.json')).filter(n => n.type === 'TEXT' && /^\d$/.test(n.text));
    assert.equal(numbers.length, 10);
    for (const n of numbers) assert.deepEqual(n.fills[0].color, { r: 0, g: 0, b: 0, a: 1 });
});

test('emoji retains Unicode so the renderer can avoid monochrome placeholder squares', () => {
    const face = flatten(read('12740-24391.json')).find(n => n.text === '😂');
    assert.equal(face.glyphs[0].emoji, '😂');
});

test('tab label masks and original image-painted glyph outlines are retained', () => {
    assert.ok(flatten(read('12740-24077.json')).some(n => n.mask && n.name === 'Symbol and Label'));
    const genmoji = flatten(read('12740-24391.json')).find(n => n.name === 'Genmoji' && n.type === 'TEXT');
    assert.ok(genmoji.fills.some(p => p.imageHash));
    assert.ok(genmoji.glyphs.length > 0);
});

test('bottom keyboard constraints and independent corner radii survive export', () => {
    const nodes = flatten(read('12740-24382.json'));
    assert.equal(nodes.find(n => n.name === 'Keyboard').verticalConstraint, 'MAX');
    assert.ok(nodes.some(n => n.radii?.length === 4 && n.radii[0] !== n.radii[2]));
});
