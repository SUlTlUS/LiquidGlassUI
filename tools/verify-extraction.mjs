import fs from 'node:fs';
import path from 'node:path';
import { createHash } from 'node:crypto';
import { fileURLToPath } from 'node:url';
const root = path.resolve(path.dirname(fileURLToPath(import.meta.url)), '..');
const baseline = path.join(root, 'docs/liquid-glass-extracted/source');
const active = path.join(root, 'liquidglass/src/main/java/com/zeaze/tianyinwallpaper');
function walk(folder, prefix = '') {
    return fs.readdirSync(folder, { withFileTypes: true }).flatMap(entry => {
        const relative = path.posix.join(prefix, entry.name);
        return entry.isDirectory() ? walk(path.join(folder, entry.name), relative) : [relative];
    }).sort();
}
const files = walk(baseline), activeFiles = walk(active);
if (files.length !== 48) throw new Error('Expected the frozen 48-file source baseline');
const sha = file => createHash('sha256').update(fs.readFileSync(file)).digest('hex');
const manifest = JSON.parse(fs.readFileSync(path.join(root, 'docs/liquid-glass-merged-files.json'), 'utf8'));
const reviewed = new Map(manifest.files.map(entry => [entry.path, entry]));
const expectedActive = files.filter(file => !reviewed.get(file)?.removed);
if (JSON.stringify(expectedActive) !== JSON.stringify(activeFiles)) throw new Error('Unexpected source additions or unaudited removals');
if (reviewed.size !== manifest.files.length || [...reviewed.keys()].some(file => !files.includes(file))) {
    throw new Error('Invalid reviewed merge paths');
}
for (const file of files) {
    const change = reviewed.get(file);
    const originalHash = sha(path.join(baseline, file));
    if (change && originalHash !== change.baseline) throw new Error(`Original baseline was changed: ${file}`);
    if (change?.removed) {
        if (change.merged !== null || fs.existsSync(path.join(active, file))) throw new Error(`Invalid reviewed removal: ${file}`);
        continue;
    }
    if (sha(path.join(active, file)) !== (change?.merged ?? originalHash)) throw new Error(`Unreviewed source change: ${file}`);
}
console.log(`PASS: ${files.length - reviewed.size} original files unchanged; ${reviewed.size} reviewed upstream/local changes verified against preserved baselines.`);
