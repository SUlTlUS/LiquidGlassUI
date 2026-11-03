import fs from 'node:fs';
import zlib from 'node:zlib';
import { decodeBinarySchema, compileSchema } from 'kiwi-schema';
const [input, output] = process.argv.slice(2);
const bytes = fs.readFileSync(input);
if (bytes.subarray(0, 8).toString() !== 'fig-kiwi') throw new Error('Not a fig-kiwi file');
const chunks = [];
for (let offset = 12; offset < bytes.length;) {
    const length = bytes.readUInt32LE(offset);
    offset += 4;
    chunks.push(bytes.subarray(offset, offset + length));
    offset += length;
}
const schema = compileSchema(decodeBinarySchema(zlib.inflateRawSync(chunks[0])));
const message = schema.decodeMessage(chunks[1].readUInt32LE(0) === 0xfd2fb528
    ? zlib.zstdDecompressSync(chunks[1]) : zlib.inflateRawSync(chunks[1]));
fs.writeFileSync(output, JSON.stringify(message, null, 2));
console.log(`Decoded ${message.nodeChanges.length} nodes`);
