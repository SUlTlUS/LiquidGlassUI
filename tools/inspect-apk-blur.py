"""Read APK DEX metadata without executing the app; generate an extraction index."""
import argparse
import hashlib
import json
from pathlib import Path
import re
import struct
import zipfile


def dex_metadata(data):
    def uint(offset):
        return struct.unpack_from('<I', data, offset)[0]

    def dex_string(offset):
        while data[offset] & 0x80:
            offset += 1
        offset += 1
        end = data.index(b'\0', offset)
        return data[offset:end].decode('utf-8', errors='replace')

    strings = [dex_string(uint(uint(0x3c) + i * 4)) for i in range(uint(0x38))]
    types = [strings[uint(uint(0x44) + i * 4)] for i in range(uint(0x40))]
    classes = []
    for i in range(uint(0x60)):
        offset = uint(0x64) + i * 32
        type_index, _, superclass = struct.unpack_from('<III', data, offset)
        classes.append({'name': types[type_index], 'super': types[superclass] if superclass != 0xffffffff else None})
    methods = []
    for i in range(uint(0x58)):
        owner, _, name = struct.unpack_from('<HHI', data, uint(0x5c) + i * 8)
        if re.search(r'GradientBlur|gradientBlur|setupHomepageGradientBlur|createGradientBlurEffect', strings[name]):
            methods.append({'owner': types[owner], 'name': strings[name]})
    return classes, strings, methods


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument('apk', type=Path)
    parser.add_argument('output', type=Path)
    args = parser.parse_args()
    args.output.mkdir(parents=True, exist_ok=True)
    result = {'apk': str(args.apk.resolve()), 'sha256': hashlib.file_digest(args.apk.open('rb'), 'sha256').hexdigest(), 'dex': []}
    with zipfile.ZipFile(args.apk) as archive:
        for entry in archive.infolist():
            if not re.fullmatch(r'classes\d*\.dex', entry.filename):
                continue
            data = archive.read(entry)
            classes, strings, methods = dex_metadata(data)
            selected = [c for c in classes if re.search(r'blur|frost|backdrop|appbar|toolbar|titlebar|scrim', c['name'], re.I)]
            item = {'file': entry.filename, 'sha256': hashlib.sha256(data).hexdigest(), 'classes': len(classes),
                    'candidates': selected, 'gradientMethods': methods,
                    'matchingStrings': [s for s in strings if re.search(r'progressive.*blur|blur.*progressive|set.*blur|gradient.*blur|blur.*gradient', s, re.I)]}
            result['dex'].append(item)
            print(entry.filename, 'classes=', len(classes), 'candidates=', len(selected))
            for cls in selected:
                if re.search(r'blur|frost', cls['name'], re.I):
                    print(' ', cls['name'])
        result['blurResources'] = [e.filename for e in archive.infolist() if re.search(r'blur|frost', e.filename, re.I)]
    (args.output / 'apk-dex-index.json').write_text(json.dumps(result, ensure_ascii=False, indent=2) + '\n', encoding='utf-8')


if __name__ == '__main__':
    main()
