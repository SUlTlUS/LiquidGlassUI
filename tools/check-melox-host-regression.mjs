// Compare legacy test failures with the host routing additions reversed IN MEMORY.
// Never restores or rewrites dirty workspace files.
import { spawnSync } from 'node:child_process';
import assert from 'node:assert/strict';
import path from 'node:path';
import { fileURLToPath } from 'node:url';

const root = path.resolve(path.dirname(fileURLToPath(import.meta.url)), '..');
const suite = path.join(root, 'tools/figma-reader/native-framework.test.mjs');
const undo = [
  ['            "MeloX TabBar",\n', ''],
  ['            "Listening Mode",\n', ''],
  ['${entries.count { it.file.isNotBlank() }} 个原稿变体 · ${entries.count { it.file.isBlank() }} 个源码示例', '${entries.size} 个原稿变体'],
  ['internal const val MeloXTabBarExampleKey = "melox-tabbar"\nprivate val MeloXTabBarExampleEntry = IphoneExample(\n    MeloXTabBarExampleKey, "MeloX TabBar", "原版交互", "",\n)\n', ''],
  ['internal const val ListeningModeExampleKey = "listening-mode"\nprivate val ListeningModeExampleEntry = IphoneExample(\n    ListeningModeExampleKey, "Listening Mode", "四档听音模式", "",\n)\n', ''],
  ['} + MeloXTabBarExampleEntry + ListeningModeExampleEntry', '}'],
  ['if (initialReference && initialEntry.file.isNotBlank())', 'if (initialReference)'],
  ['if (entry?.key == MeloXTabBarExampleKey) {\n                MeloXTabBarExamplePage(onBack = popBack)\n            } else if (entry?.key == ListeningModeExampleKey) {\n                ListeningModeExamplePage(onBack = popBack)\n            } else if (entry != null)', 'if (entry != null)'],
];
const loader = `
const fs = require('node:fs');
const real = fs.readFileSync.bind(fs);
const undo = ${JSON.stringify(undo)};
fs.readFileSync = function(p, ...args) {
  let data = real(p, ...args);
  if (String(p).replaceAll('\\\\', '/').endsWith('/IphoneExamples.kt') && typeof data === 'string') {
    data = data.replaceAll('\\r\\n', '\\n');
    for (const [from, to] of undo) {
      if (!data.includes(from)) throw new Error('Baseline reversal anchor missing: ' + from);
      data = data.replace(from, to);
    }
  }
  return data;
};
import(${JSON.stringify('file://' + suite.replaceAll('\\', '/'))});
`;
const before = spawnSync(process.execPath, ['--test-reporter=tap', '-e', loader], { cwd: root, encoding: 'utf8', maxBuffer: 8 * 1024 * 1024 });
const after = spawnSync(process.execPath, ['--test', '--test-reporter=tap', suite], { cwd: root, encoding: 'utf8', maxBuffer: 8 * 1024 * 1024 });
function failures(result) {
  const output = result.stdout + result.stderr;
  assert.ok(/# tests 31/.test(output), 'The complete old suite must run: ' + output.slice(-500));
  return [...output.matchAll(/^not ok \d+ - (.+)$/gm)].map(m => m[1]);
}
const baselineFailures = failures(before);
const currentFailures = failures(after);
assert.deepEqual(currentFailures, baselineFailures, 'New host routing changed the legacy failure set.');
console.log(`PASS: legacy failures unchanged (${baselineFailures.length}/31); no legacy files were restored or rewritten.`);
