// Reconstruct local instances and export original drawing commands, not screenshots.
import fs from 'node:fs';
import path from 'node:path';
import crypto from 'node:crypto';
const root = path.resolve(import.meta.dirname, '../..');
const source = path.join(root, 'docs/figma-community-source');
const doc = JSON.parse(fs.readFileSync(path.join(source, 'decoded.json'), 'utf8'));
const id = g => g && `${g.sessionID}:${g.localID}`;
const nodes = new Map(doc.nodeChanges.map(n => [id(n.guid), n]));
const children = new Map();
for (const n of nodes.values()) {
    const p = id(n.parentIndex?.guid);
    if (!children.has(p)) children.set(p, []);
    children.get(p).push(n);
}
for (const list of children.values()) list.sort((a, b) => (a.parentIndex.position < b.parentIndex.position ? -1 : 1));
const output = path.join(root, 'sample/src/main/assets/iphone-examples');
fs.mkdirSync(output, { recursive: true });
const stats = { missingSymbols: [], unresolvedOverrides: [], paths: 0, glyphs: 0 };
const pathCache = new Map();
function commands(index) {
    if (pathCache.has(index)) return pathCache.get(index);
    const bytes = Buffer.from(Object.values(doc.blobs[index].bytes));
    const result = [];
    for (let o = 0; o < bytes.length;) {
        const op = bytes[o++];
        const count = [0, 2, 2, 4, 6][op];
        if (count === undefined) throw new Error(`Unknown path opcode ${op} at blob ${index}`);
        result.push(op);
        for (let i = 0; i < count; i++, o += 4) result.push(+bytes.readFloatLE(o).toFixed(5));
    }
    pathCache.set(index, result); stats.paths++;
    return result;
}
function find(tree, key) {
    if (tree.ids.includes(key)) return tree;
    for (const c of tree.children) { const result = find(c, key); if (result) return result; }
}
function target(tree, guids) {
    let current = tree;
    for (const g of guids) {
        current = find(current, id(g));
        if (!current) {
            // A deleted intermediate instance may remain in the stored path.
            // Recover only an unambiguous exact source-ID match, never by label.
            const matches = [];
            const last = id(guids.at(-1));
            const walk = t => { if (t.ids.includes(last)) matches.push(t); t.children.forEach(walk); };
            walk(tree);
            return matches.length === 1 ? matches[0] : undefined;
        }
    }
    return current;
}
function expand(n, stack = []) {
    const key = id(n.guid);
    if (stack.length > 60) throw new Error(`Recursive instance: ${key}`);
    const base = nodes.get(id(n.symbolData?.symbolID));
    let tree;
    if (base && id(base.guid) !== key) {
        tree = expand(base, [...stack, key]);
        tree.ids.push(key);
        tree.n = { ...tree.n, ...n };
    } else {
        if (n.type === 'INSTANCE' && !base) stats.missingSymbols.push(key);
        tree = { ids: [key], n: { ...n }, children: (children.get(key) ?? []).map(c => expand(c, [...stack, key])) };
    }
    const apply = (patch, isDerived) => {
        const t = target(tree, patch.guidPath?.guids ?? []);
        if (!t) { stats.unresolvedOverrides.push({ instance: key, path: patch.guidPath, derived: isDerived }); return; }
        if (patch.overriddenSymbolID) {
            const swap = nodes.get(id(patch.overriddenSymbolID));
            if (swap) {
                const replacement = expand(swap, [...stack, key]);
                t.children = replacement.children;
                t.ids.push(...replacement.ids);
                t.n = { ...replacement.n, guid: t.n.guid, transform: t.n.transform, size: t.n.size, visible: t.n.visible };
            }
        }
        const saved = t === tree ? { size: n.size, transform: n.transform } : {};
        t.n = { ...t.n, ...patch, ...saved };
    };
    for (const p of n.symbolData?.symbolOverrides ?? []) apply(p, false);
    for (const p of n.derivedSymbolData ?? []) apply(p, true);
    const assignments = new Map((n.componentPropAssignments ?? []).map(p => [id(p.defID), p.value ?? p.varValue?.value]));
    if (assignments.size) {
        const bind = t => {
            for (const ref of t.n.componentPropRefs ?? []) {
                const value = assignments.get(id(ref.defID));
                if (!value) continue;
                if (ref.componentPropNodeField === 'VISIBLE' && value.boolValue !== undefined) t.n.visible = value.boolValue;
                if (ref.componentPropNodeField === 'TEXT_DATA' && value.textValue !== undefined) t.n.textData = { ...t.n.textData, characters: value.textValue };
            }
            t.children.forEach(bind);
        };
        bind(tree);
    }
    return tree;
}
const identity = { m00: 1, m01: 0, m02: 0, m10: 0, m11: 1, m12: 0 };
function variableColor(binding, modes, seen = new Set()) {
    const value = binding?.value;
    if (value?.colorValue) return value.colorValue;
    const key = id(value?.alias?.guid);
    if (!key || seen.has(key)) return undefined;
    seen.add(key);
    const variable = nodes.get(key);
    const setID = id(variable?.variableSetID?.guid);
    const mode = modes.get(setID) ?? id(nodes.get(setID)?.variableSetModes?.[0]?.id);
    const entry = variable?.variableDataValues?.entries?.find(e => id(e.modeID) === mode);
    return variableColor(entry?.variableData, modes, seen);
}
const paint = (p, modes) => ({ type: p.type, color: variableColor(p.colorVar, modes) ?? p.color, opacity: p.opacity ?? 1,
    stops: p.stops, transform: p.transform, image: p.image, imageScaleMode: p.imageScaleMode,
    blendMode: p.blendMode });
const images = new Set();
function exportNode(t, isRoot = false, inheritedModes = new Map(), instancePath = 'root') {
    const n = t.n;
    if (n.visible === false || n.opacity === 0) return null;
    // Device chrome is provided by Android, not painted over the app.
    if (/^(Bezel|Status bar(?: - iPhone)?|Home Indicator)$/.test(n.name ?? '')) return null;
    const modes = new Map(inheritedModes);
    for (const entry of n.variableModeBySetMap?.entries ?? []) modes.set(id(entry.variableSetID?.guid), id(entry.variableModeID));
    const fills = (n.fillPaints ?? []).filter(p => p.visible !== false).map(p => paint(p, modes));
    for (const p of fills) if (p.image?.hash) {
        p.imageHash = Buffer.from(Object.values(p.image.hash)).toString('hex'); images.add(p.imageHash);
        delete p.image;
    }
    const m = isRoot ? identity : n.transform ?? identity;
    const out = { id: id(n.guid), instancePath, name: n.name, type: n.type, w: n.size?.x ?? 0, h: n.size?.y ?? 0,
        matrix: [m.m00, m.m01, m.m02, m.m10, m.m11, m.m12], opacity: n.opacity ?? 1,
        radius: n.cornerRadius ?? 0, clip: n.frameMaskDisabled === false,
        verticalConstraint: n.verticalConstraint,
        radii: n.rectangleCornerRadiiIndependent ? [n.rectangleTopLeftCornerRadius ?? n.cornerRadius ?? 0,
            n.rectangleTopRightCornerRadius ?? n.cornerRadius ?? 0, n.rectangleBottomRightCornerRadius ?? n.cornerRadius ?? 0,
            n.rectangleBottomLeftCornerRadius ?? n.cornerRadius ?? 0] : undefined,
        mask: n.mask === true, maskType: n.maskType, blend: n.blendMode,
        fills, strokes: (n.strokePaints ?? []).filter(p => p.visible !== false).map(p => paint(p, modes)),
        strokeWidth: n.strokeWeight ?? 0,
        paths: (n.fillGeometry ?? []).map(p => ({ commands: commands(p.commandsBlob), rule: p.windingRule })),
        strokePaths: (n.strokeGeometry ?? []).map(p => ({ commands: commands(p.commandsBlob), rule: p.windingRule })),
        effects: (n.effects ?? []).filter(e => e.visible !== false),
        text: typeof n.textData?.characters === 'object' ? n.textData.characters.characters : n.textData?.characters,
        glyphs: (n.derivedTextData?.glyphs ?? []).filter(g => g.commandsBlob !== undefined).map(g => {
            stats.glyphs++;
            return { path: commands(g.commandsBlob), emoji: g.emojiCodePoints?.length ? String.fromCodePoint(...g.emojiCodePoints) : undefined,
                x: g.position.x, y: g.position.y, size: g.fontSize, rotation: g.rotation ?? 0 };
        }),
        children: t.children.map((c, i) => exportNode(c, false, modes, `${instancePath}/${i}`)).filter(Boolean) };
    return out;
}
const section = (children.get('12740:23995') ?? []);
const catalog = [];
for (const group of section) {
    const examples = group.type === 'SYMBOL' ? [group] : (children.get(id(group.guid)) ?? []).filter(n => n.type === 'SYMBOL');
    for (const n of examples) {
        if (n.size?.x !== 402 || n.size?.y !== 874) throw new Error(`Unexpected phone dimensions: ${id(n.guid)}`);
        const tree = exportNode(expand(n), true);
        const key = id(n.guid).replace(':', '-');
        const entry = { key, nodeId: id(n.guid), category: group.name.replace('Examples/', ''),
            variant: n === group ? 'Default' : n.name, width: 402, height: 874, file: `${key}.json` };
        catalog.push(entry);
        fs.writeFileSync(path.join(output, entry.file), JSON.stringify(tree));
    }
}
for (const hash of images) {
    const file = path.join(source, 'images', hash);
    if (!fs.existsSync(file)) throw new Error(`Image missing: ${hash}`);
    fs.mkdirSync(path.join(output, 'images'), { recursive: true });
    fs.copyFileSync(file, path.join(output, 'images', hash));
}
fs.writeFileSync(path.join(output, 'catalog.json'), JSON.stringify(catalog, null, 2));
fs.writeFileSync(path.join(root, 'docs/iphone-examples-export-report.json'), JSON.stringify({
    canvasSha256: crypto.createHash('sha256').update(fs.readFileSync(path.join(source, 'canvas.fig'))).digest('hex'),
    count: catalog.length, images: images.size, ...stats, catalog,
}, null, 2));
console.log(JSON.stringify({ count: catalog.length, images: images.size, ...stats, unresolvedOverrides: stats.unresolvedOverrides.length }));
