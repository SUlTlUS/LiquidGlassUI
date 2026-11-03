import fs from 'node:fs';
import path from 'node:path';
import { fileURLToPath } from 'node:url';
const root = path.resolve(path.dirname(fileURLToPath(import.meta.url)), '..');
const read = file => fs.readFileSync(path.join(root, file), 'utf8');
const required = [
    ['AGENTS.md', 'LiquidGlass Sketch 项目规范'],
    ['docs/liquid-glass-merged-files.json', 'files'],
    ['docs/Untitled.fig', null],
    ['docs/figma-local-measurements.json', 'canvasSha256'],
];
for (const [file, marker] of required) {
    if (!fs.existsSync(path.join(root, file))) throw new Error(`Missing spec file: ${file}`);
    if (marker && !read(file).includes(marker)) throw new Error(`Spec marker missing: ${file}`);
}
const source = read('liquidglass/src/main/java/com/zeaze/tianyinwallpaper/catalog/components/LiquidSlider.kt');
for (const marker of ['sliderThumbTranslation', 'sliderAmbientShadow', 'sliderContactShadow']) {
    if (!source.includes(marker)) throw new Error(`Slider guard missing: ${marker}`);
}
const layout = read('liquidglass/src/main/java/dev/liquidglass/ios27/Ios27Lists.kt');
for (const marker of ['padding(horizontal = Ios27Tokens.SeparatorInset.dp)', 'ios27RowTopSeparator']) {
    if (!layout.includes(marker)) throw new Error(`List guard missing: ${marker}`);
}
console.log('PASS: LiquidGlass project spec, Figma evidence, Slider guards and list separator guards are present.');
