"""Render the production AGSL with Skia and check capsule-edge continuity.

Requires skia-python. No Android screen capture or visual acceptance is implied.
The constant input isolates highlight compositing from the Sketch material.
"""
import argparse
import json
import math
from pathlib import Path
import re
import struct

import numpy as np
import skia


ROOT = Path(__file__).resolve().parents[2]
DEFAULT_SHADER = ROOT / 'liquidglass-ios27/src/main/assets/shaders/flclash_control_highlight.agsl'


def capsule_curve():
    # Flutter ComputeNAndXj fixture: a=120px, r=91px, ratio lies in [2.5, 3].
    a, radius = 120.0, 91.0
    t = (2 * a / radius - 2.5) / .5
    n = 2.75148990 + (3.36298265 - 2.75148990) * t
    k = 1.53385819 + (1.98288283 - 1.53385819) * t
    x = (1 - 1 / k) * a
    y = (1 - (x / a) ** n) ** (1 / n) * a
    slope = (x / y) ** (n - 1)
    d = (x - slope * y) / (1 - slope)
    radius_arc = (a - d - (1 - math.sqrt(.5)) * radius) * math.sqrt(2)
    meet = a - (1 - math.sqrt(.5)) * radius
    dx, dy = meet - x, meet - y
    chord = math.hypot(dx, dy)
    reach = math.sqrt(radius_arc ** 2 - chord ** 2 / 4)
    center = [(x + meet) / 2 + dy / chord * reach,
              (y + meet) / 2 - dx / chord * reach]
    return [n, x, y, radius_arc], center


def render(source, hairline, side, rim):
    effect = skia.RuntimeEffect.MakeForShader(source)
    curve, center = capsule_curve()
    uniforms = {
        'uPanelSize': [1065.0, 182.0],
        'uDensity': 3.5,
        'uRim': [rim, 1.2 * 3.5, .17, 1.0],
        'uRimAdded': .38,
        'uEdge': [hairline, side, 0.0],
        'uRefraction': 60.0,
        'uCount': 4,
        'uPillCurves': curve * 6,
        'uPillCenters': center * 6,
        'uBlends': [28.0] * 4,
        'uGlow': [0.0, 0.0, 0.0, 1.0],
    }
    for i in range(4):
        uniforms['uPill' + str(i)] = [120.0 + 275 * i, 91.0, 120.0, 91.0]
    # SkRuntimeEffect stores scalar/vector arrays packed at 4-byte alignment.
    # The Python setUniform overload only accepts vectors, not arrays.
    data = bytearray()
    for kind, dimension, name, count in re.findall(
            r'uniform\s+(float|int)([234]?)\s+(\w+)(?:\[(\d+)\])?;', source):
        size = int(dimension or 1) * int(count or 1)
        value = uniforms.get(name, [0] * size)
        values = value if isinstance(value, list) else [value]
        assert len(values) == size, name
        data.extend(struct.pack('<' + ('i' if kind == 'int' else 'f') * size, *values))
    assert skia.RuntimeShaderBuilder(effect).uniforms().size() == len(data)
    builder = skia.RuntimeShaderBuilder(effect, skia.Data.MakeWithCopy(data))
    builder.setChild('uContent', skia.Shaders.Color(skia.Color4f(.2, .2, .2, 1)))
    surface = skia.Surface.MakeRaster(skia.ImageInfo.Make(
        1065, 182, skia.ColorType.kRGBA_F32_ColorType, skia.AlphaType.kPremul_AlphaType,
    ))
    surface.getCanvas().drawPaint(skia.Paint(
        Shader=builder.makeShader(), BlendMode=skia.BlendMode.kSrc,
    ))
    return surface.makeImageSnapshot().toarray(colorType=skia.ColorType.kRGBA_F32_ColorType)


def check(source):
    report = {}
    for name, hairline, side, rim in (
        ('dark_regular', 0.0, .75, .28),
        ('dark_tinted', 0.0, .73, .14),
        ('light_regular', .25, .75, .28),
        ('no_hairline', 0.0, 0.0, .28),
    ):
        pixels = render(source, hairline, side, rim)
        # Eight straight-to-curved joins across four pills, top AND bottom.
        joins = [x + i * 275 for i in range(4) for x in (91, 149)]
        steps = [float(np.max(np.abs(pixels[y, x - 1, :3] - pixels[y, x, :3])))
                 for y in (0, 181) for x in joins]
        centers = [float(pixels[y, 120 + i * 275, 0])
                   for y in (0, 181) for i in range(4)]
        # Zero directional darkening must still replace the outer rim with
        # the input material if the style enables a hairline on its sides.
        expected = .2 - .156 * hairline
        replacement_error = max(abs(v - expected) for v in centers)
        if side == 0:
            # A style with no line keeps its bright outer rim.
            coverage_ok = min(centers) > .35
        else:
            coverage_ok = replacement_error < .001
        inner = float(pixels[6, 120, 0])
        report[name] = {
            'max_join_step': max(steps),
            'outer_flat_pixel': centers[0],
            'inner_highlight_pixel': inner,
            'passed': bool(max(steps) < .002 and coverage_ok and inner > .21
                           and np.isfinite(pixels).all()),
        }
    return report


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--shader', type=Path, default=DEFAULT_SHADER)
    parser.add_argument('--report', type=Path)
    args = parser.parse_args()
    result = check(args.shader.read_text(encoding='utf-8'))
    output = json.dumps(result, indent=2)
    print(output)
    if args.report:
        args.report.write_text(output + '\n', encoding='utf-8')
    raise SystemExit(0 if all(case['passed'] for case in result.values()) else 1)
