import test from 'node:test';
import assert from 'node:assert/strict';
import fs from 'node:fs';
import path from 'node:path';
import vm from 'node:vm';

const root = path.resolve(import.meta.dirname, '..');
const active = 'liquidglass/src/main/java/com/zeaze/tianyinwallpaper/';
const baseline = 'docs/liquid-glass-extracted/source/';
const read = file => fs.readFileSync(path.join(root, file), 'utf8').replaceAll('\r\n', '\n');
const source = read(active + 'backdrop/Shaders.kt');
const shaderNames = ['RoundedRectRefractionShaderString', 'RoundedRectRefractionWithDispersionShaderString',
    'DefaultHighlightShaderString', 'AmbientHighlightShaderString'];

// Execute the actual scalar/quadrant function from AGSL, not a handwritten replacement.
const radiusFunction = source.slice(source.indexOf('float radiusAt('), source.indexOf('\nfloat sdRoundedRect'))
    .replace('float radiusAt(float2 coord, float4 radii)', 'function radiusAt(coord, radii)');
const radiusAt = vm.runInNewContext(`(${radiusFunction.trim()})`);
const shaders = new Map([...source.matchAll(/internal (?:const val|val) (\w+) = """([\s\S]*?)"""/g)]
    .map(([, name, body]) => [name, body]));

function sampledRadius(name, point, size, offset, radii) {
    const shader = shaders.get(name);
    assert(shader, `Missing shader ${name}`);
    const assignment = shader.match(/float2 centeredCoord = ([^;]+);/)[1];
    // Explicitly reject an unmodelled coordinate transform rather than silently testing an approximation.
    assert(['(coord + offset) - halfSize', 'coord - halfSize'].includes(assignment));
    const centered = {
        x: point.x + (assignment.includes('offset') ? offset.x : 0) - size.x / 2,
        y: point.y + (assignment.includes('offset') ? offset.y : 0) - size.y / 2,
    };
    const input = shader.match(/float radius = radiusAt\((\w+), cornerRadii\);/)[1];
    assert(['coord', 'centeredCoord'].includes(input));
    return radiusAt(input === 'centeredCoord' ? centered : point, radii);
}

test('all four shaders select the four actual corner radii', () => {
    const radii = { x: 13, y: 27, z: 39, w: 51 };
    const size = { x: 386, y: 600 };
    for (const name of shaderNames) {
        for (const [point, expected] of [
            [{ x: 8, y: 8 }, 13], [{ x: 378, y: 8 }, 27],
            [{ x: 378, y: 592 }, 39], [{ x: 8, y: 592 }, 51],
        ]) assert.equal(sampledRadius(name, point, size, { x: 0, y: 0 }, radii), expected, name);
    }
});

test('sheet top corners stay 32dp when device-derived bottom corners differ', () => {
    for (const name of shaderNames) {
        for (const bottom of [0, 24, 48, 66]) {
            for (const x of [8, 378]) assert.equal(sampledRadius(name, { x, y: 8 },
                { x: 386, y: 600 }, { x: 0, y: 0 }, { x: 32, y: 32, z: bottom, w: bottom }), 32, name);
        }
    }
});

test('refraction quadrant selection includes recorded-layer padding offset', () => {
    for (const name of shaderNames.slice(0, 2)) {
        const size = { x: 100, y: 200 };
        const radii = { x: 11, y: 22, z: 33, w: 44 };
        assert.equal(sampledRadius(name, { x: 70, y: 110 }, size, { x: -40, y: -40 }, radii), 11);
        assert.equal(sampledRadius(name, { x: 110, y: 110 }, size, { x: -40, y: -40 }, radii), 22);
    }
});

test('uniform-radius buttons retain the same selected radius', () => {
    for (const name of shaderNames) for (const radius of [0, 13, 22, 32, 34]) {
        for (const point of [{ x: 1, y: 1 }, { x: 99, y: 1 }, { x: 99, y: 99 }, { x: 1, y: 99 }]) {
            assert.equal(sampledRadius(name, point, { x: 100, y: 100 }, { x: 0, y: 0 },
                { x: radius, y: radius, z: radius, w: radius }), radius);
        }
    }
});

test('highlight shader receives the real CornerBasedShape, not the caching wrapper', () => {
    const highlight = read(active + 'backdrop/highlight/HighlightModifier.kt');
    assert.match(highlight, /createShader\(\s*shape = shapeProvider\.innerShape,/);
    assert.match(highlight, /val outline = shapeProvider\.shape\.createOutline/);
});

test('shader fix changes only the four radius lookup arguments', () => {
    const original = read(baseline + 'backdrop/Shaders.kt');
    assert.equal((original.match(/radiusAt\(coord, cornerRadii\)/g) ?? []).length, 4);
    assert.equal(source, original.replaceAll('radiusAt(coord, cornerRadii)', 'radiusAt(centeredCoord, cornerRadii)'));
});

test('highlight preserves original clipping, blur, width and blending with sensor shading', () => {
    const original = read(baseline + 'backdrop/highlight/HighlightModifier.kt');
    const current = read(active + 'backdrop/highlight/HighlightModifier.kt');
    const between = (text, from, to) => text.slice(text.indexOf(from), text.indexOf(to));
    // Sensor subscription and shader selection are now intentional changes. Keep
    // the actual outline recording, clipping and layer composition byte-identical.
    assert.equal(between(current, '        drawContent()\n', '    override fun onAttach()'),
        between(original, '        drawContent()\n', '    override fun onAttach()'));
    assert.equal(between(current, '        paint.color =', '        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)'),
        between(original, '        paint.color =', '        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)'));
});
