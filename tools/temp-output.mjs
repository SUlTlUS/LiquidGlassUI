import fs from 'node:fs';
import path from 'node:path';
import {fileURLToPath} from 'node:url';
import {randomBytes} from 'node:crypto';

const root = path.resolve(path.dirname(fileURLToPath(import.meta.url)), '..');
export const tempRoot = path.join(root, '.tmp');

const validateScope = (scope) => {
    if (typeof scope !== 'string' || !/^[a-z0-9]+(?:-[a-z0-9]+)*$/.test(scope)) {
        throw new Error(`Invalid temporary-output scope: ${scope}`);
    }
    return scope;
};

export function createTempOutputDir(scope) {
    const safeScope = validateScope(scope);
    const timestamp = new Date().toISOString().replace(/[:.]/g, '-');
    const runDir = path.join(tempRoot, safeScope, `${timestamp}-${randomBytes(3).toString('hex')}`);
    fs.mkdirSync(runDir, {recursive: true});
    return runDir;
}

export function cleanTempOutput(scope) {
    const target = scope ? path.join(tempRoot, validateScope(scope)) : tempRoot;
    const relative = path.relative(tempRoot, target);
    if (relative.startsWith('..') || path.isAbsolute(relative)) {
        throw new Error('Refusing to clean outside the temporary-output root.');
    }
    fs.rmSync(target, {recursive: true, force: true});
}

if (process.argv[1] && path.resolve(process.argv[1]) === fileURLToPath(import.meta.url)) {
    const [command, scope, ...extra] = process.argv.slice(2);
    if (command !== 'clean' || extra.length) {
        console.error('Usage: node tools/temp-output.mjs clean [scope]');
        process.exitCode = 2;
    } else {
        cleanTempOutput(scope);
        console.log(scope ? `Cleaned .tmp/${scope}` : 'Cleaned .tmp');
    }
}
