// Offline, read-only input: decode the embedded Kiwi schema, then retain layout evidence.
// Container layout reference: https://github.com/OpenFig-org/openfig-core/blob/main/docs/research.md
import fs from 'node:fs';
import zlib from 'node:zlib';
import { createHash } from 'node:crypto';
import { decodeBinarySchema, compileSchema } from 'kiwi-schema';
const [input, output] = process.argv.slice(2);
if (!input || !output) throw new Error('Usage: node read.mjs <extracted canvas.fig> <output.json>');
const bytes = fs.readFileSync(input);
if (bytes.subarray(0, 8).toString() !== 'fig-kiwi') throw new Error('Not a fig-kiwi file');
const chunks = [];
for (let offset = 12; offset < bytes.length;) {
    if (offset + 4 > bytes.length) throw new Error('Truncated chunk header');
    const length = bytes.readUInt32LE(offset);
    offset += 4;
    if (offset + length > bytes.length) throw new Error('Truncated chunk');
    chunks.push(bytes.subarray(offset, offset + length));
    offset += length;
}
const schema = compileSchema(decodeBinarySchema(zlib.inflateRawSync(chunks[0])));
const message = schema.decodeMessage(chunks[1].readUInt32LE(0) === 0xfd2fb528
    ? zlib.zstdDecompressSync(chunks[1]) : zlib.inflateRawSync(chunks[1]));
const id = value => value ? `${value.sessionID}:${value.localID}` : undefined;
const evidence = {
    canvasSha256: createHash('sha256').update(bytes).digest('hex'),
    nodeCount: message.nodeChanges.length,
    colors: Object.fromEntries(message.nodeChanges.filter(n => n.type === 'VARIABLE' && n.variableResolvedType === 'COLOR')
        .map(n => [n.name, Object.fromEntries((n.variableDataValues?.entries ?? [])
            .filter(e => e.modeID.sessionID === 404)
            .map(e => [e.modeID.localID === 0 ? 'light' : 'dark', e.variableData.value.colorValue]))])),
    nodes: message.nodeChanges.filter(n => n.guid.sessionID === 1 && n.guid.localID >= 770)
        .map(n => ({
            id: id(n.guid), parent: id(n.parentIndex?.guid), name: n.name, type: n.type,
            size: n.size, transform: n.transform, cornerRadius: n.cornerRadius,
            layout: Object.fromEntries(Object.entries(n).filter(([key]) => key.startsWith('stack'))),
            fills: n.fillPaints?.map(p => ({type: p.type, color: p.color, opacity: p.opacity})),
            separators: n.derivedSymbolData?.filter(d => d.size?.y === 1)
                .map(d => ({size: d.size})),
        })),
};
fs.writeFileSync(output, JSON.stringify(evidence, null, 2) + '\n');
console.log(JSON.stringify(evidence, null, 2));
