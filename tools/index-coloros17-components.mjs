import fs from 'node:fs';
import path from 'node:path';
import crypto from 'node:crypto';
import { execFileSync } from 'node:child_process';

const root = path.resolve(import.meta.dirname, '..');
const output = path.join(root, 'docs/coloros17-extracted/settings-17.0.0');
const hash = bytes => crypto.createHash('sha256').update(bytes).digest('hex');
function walk(directory) {
    return fs.readdirSync(directory, { withFileTypes: true }).flatMap(entry => {
        const file = path.join(directory, entry.name);
        return entry.isDirectory() ? walk(file) : [file];
    }).sort();
}
const catalogText = fs.readFileSync(path.join(root,
    'liquidglass/src/main/java/dev/liquidglass/coloros17/ColorOs17ComponentCatalog.kt'), 'utf8');
const catalog = [...catalogText.matchAll(/item\("([^"]+)", "([^"]+)", "([^"]+)", "([^"]+)"(?:, ColorOs17ComponentKind\.(\w+))?\)/g)]
    .map(([, key, category, title, type, kind]) => ({ key: `coloros17-${key}`,
        category: `ColorOS 17 / ${category}`, title, className: `com.coui.appcompat.${type}`, kind: kind ?? 'View',
        source: `sources/com/coui/appcompat/${type.replaceAll('.', '/')}.java` }));
if (catalog.length !== 29) throw new Error(`Unexpected catalog count ${catalog.length}`);
const files = [...walk(path.join(output, 'sources')), ...walk(path.join(output, 'resources/raw'))]
    .map(file => {
        const bytes = fs.readFileSync(file);
        return { path: path.relative(output, file).replaceAll('\\', '/'), sha256: hash(bytes), bytes: bytes.length,
            ...(file.endsWith('.java') ? {
                decompileLimited: /JADX WARN|JADX ERROR|Method not decompiled/.test(bytes.toString('utf8')),
                publicSignatures: bytes.toString('utf8').split(/\r?\n/)
                    .filter(line => /^    public .+\(/.test(line))
                    .map(line => line.trim().replace(/ \{$/, '')),
            } : {}) };
    });
const resourceTable = path.join(output, 'resources/resource-table.txt');
if (fs.existsSync(resourceTable)) {
    const bytes = fs.readFileSync(resourceTable);
    files.push({ path: 'resources/resource-table.txt', sha256: hash(bytes), bytes: bytes.length });
}
for (const entry of catalog) if (!files.some(file => file.path === entry.source))
    throw new Error(`Missing original source: ${entry.source}`);
const manifest = {
    apk: 'D:/下载/设置_17.0.0.apk',
    apkSha256: 'e285088674907eedef89b4ba9862165dd070699cba4303c4b05b969f99c4261d',
    package: 'com.android.settings', version: '17.0.0', minSdk: 33,
    provenance: 'JADX 1.5.6 decompiled text, unchanged; resources are original ZIP-entry bytes',
    runtime: 'Installed platform-signed system Settings package, only on ColorOS 17. No bundled implementation.',
    limitations: 'Not original Kotlin, not a standalone library, not all COUI dependencies. Runtime/visual acceptance unverified.',
    categories: [...new Set(catalog.map(entry => entry.category))], catalog, files,
};
const json = JSON.stringify(manifest, null, 2) + '\n';
const indexFile = path.join(output, 'component-index.json');
if (process.argv.includes('--check')) {
    if (fs.readFileSync(indexFile, 'utf8') !== json) throw new Error('Extraction index drift');
} else {
    const apk = process.argv[2];
    const aapt2 = process.argv[3];
    if (!apk || !aapt2) throw new Error('Usage: node tools/index-coloros17-components.mjs <APK> <aapt2> | --check');
    if (hash(fs.readFileSync(apk)) !== manifest.apkSha256) throw new Error('APK hash mismatch');
    if (fs.existsSync(indexFile)) {
        // A regeneration must not bless modified reference files as a new baseline.
        const previous = JSON.parse(fs.readFileSync(indexFile, 'utf8'));
        for (const file of previous.files) {
            if (hash(fs.readFileSync(path.join(output, file.path))) !== file.sha256)
                throw new Error(`Refusing to refresh changed original extraction: ${file.path}`);
        }
    }
    const resources = execFileSync(aapt2, ['dump', 'resources', apk], { maxBuffer: 64 * 1024 * 1024 });
    const resourceFile = path.join(output, 'resources/resource-table.txt');
    if (fs.existsSync(resourceFile) && !fs.readFileSync(resourceFile).equals(resources))
        throw new Error('Refusing to overwrite different resource dump');
    fs.writeFileSync(resourceFile, resources);
    fs.writeFileSync(indexFile, json);
    fs.writeFileSync(path.join(output, 'catalog.md'), '# ColorOS 17 原组件调用目录\n\n' +
        '仅登记原类和原资源；不提供仿制实现。运行入口见 `docs/coloros17-components.md`。\n\n' +
        '| 分类 | 示例 | 原类 | 类型 |\n| --- | --- | --- | --- |\n' +
        catalog.map(entry => `| ${entry.category} | ${entry.title} | [${entry.className}](${entry.source}) | ${entry.kind} |`).join('\n') + '\n');
}
console.log(`ColorOS 17: ${catalog.length} entries, ${manifest.categories.length} categories, ` +
    `${files.filter(file => file.path.endsWith('.java')).length} unchanged Java files, ` +
    `${files.filter(file => file.path.startsWith('resources/raw/')).length} original resource entries; index verified`);
