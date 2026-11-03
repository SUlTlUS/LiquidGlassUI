# Liquid Glass 27 — FlClash → Jetpack Compose

Native Kotlin/Jetpack Compose demonstration and reusable glass controls,
derived from the locally bundled FlClash reference implementation.

The current demo keeps **Sketch backdrop sampling and material** and ports
FlClash geometry and interaction to Compose. Use `GlassScene(sketchMaterial = true)`
for that configuration. The original AGSL path remains optional; the optical
limitations below describe that path, not the active Sketch demo.

The current controls include continuous elastic press, a draggable dock with
velocity-driven lens deformation, Compose vector glyphs, nested morphing menus,
and an alert with FlClash entry/exit timing. See `plan.md` for implementation
coverage and remaining work. Visual equivalence is still subject to user acceptance.

## Source correspondence

| Upstream FlClash | Compose port |
| --- | --- |
| `lib/ui/liquid_glass.dart` (style, pane optics) | `glass/GlassStyle.kt`, `glass/GlassRenderer.kt` |
| `lib/ui/liquid_glass_capture.dart` (backdrop capture) | `glass/GlassScene.kt` |
| `shaders/liquid_glass.frag` (fragment optics) | `assets/shaders/flclash_glass.agsl` |
| `lib/ui/goo.dart` (smooth-min grouped pills) | `glass/GlassGeometry.kt`, `glass/GlassControls.kt` |
| `lib/ui/navigation_dock.dart` (elastic press, bar lens) | `glass/GlassControls.kt`, `glass/GlassDock.kt` |
| `lib/ui/popup.dart` (spring menu morph) | `glass/GlassMenu.kt` |
| `lib/ui/dialog.dart` (alert glass) | `glass/GlassMenu.kt` |

## Building

- Validated with JDK 21, Android SDK 37 and the Gradle 9.7.1 wrapper
- Android Gradle Plugin 9.3.1, Kotlin Compose Compiler Gradle plugin 2.4.10
- Android 13+ (`minSdk 33`) for native Android RuntimeShader (AGSL)
- Open the **root** `liquidglass27` folder in Android Studio (not `FlClash/android`).
- Local build without the project's QQ-send finalizer: `./gradlew :app:assembleDebug -x :app:sendNamedDebugApkToQQ`.
- Install to a connected device: `adb install -r app/build/outputs/apk/debug/app-debug.apk`.

The Android SDK path is set in `local.properties`. The `:sketch-glass` module
uses the sibling checkout `../liquidglass_sketch/liquidglass`.

For integration into the sibling Sketch project, see the [merge guide](docs/merge-into-liquidglass-sketch.md) (Chinese), including module setup, compatibility boundaries, performance safeguards and validation steps.

## Using the components

```kotlin
GlassScene(
    modifier = Modifier.fillMaxSize(),
    sketchMaterial = true,
    background = { MyBackdrop() },
) {
    ElasticGlassButton(
        modifier = Modifier.size(56.dp),
        style = GlassStyle.regular(dark = false),
        onClick = { /* action */ },
    ) { Text("Glass") }
}
```

Use `LiquidGlass` for a container, `GooGlassRow` for fused adjacent buttons,
`GlassNavigationDock` for the draggable floating navigation lens,
`GlassMorphMenu` for the expanding/collapsing menu, and `GlassAlert`
for a glass dialog.

`GlassDialog` exposes content and trailing-title slots, fields, destructive
actions, and adaptive action layout. `GlassPopupState<T>` and `GlassPopup`
support typed results, nonmodal input, tracked anchors, and keyboard insets.
Wrap content with `GlassTheme` for seeded colors and pure-black surface roles.
`GlassGlyphs` contains the complete 124-entry reference catalogue.

The demo menu's **完整机制验证** opens interactive coverage for these APIs.
See [the device checklist](docs/verification-2026-11-03.md) for acceptance steps.

The menu anchor is a `Rect` in **GlassScene root pixels**. Read it with
`onGloballyPositioned { it.positionInRoot() }`. Place menu/alert
composables after the normal content so they paint above it.

## Fidelity and limitations

The active `sketchMaterial = true` route intentionally retains Sketch's
sampling, blur, refraction and material. FlClash supplies the geometry,
continuous springs, gestures, source morphs, menu hierarchy, dialog layout,
theme roles and vector icons. Nested glass uses ordered backdrop layers;
source snapshots contain foreground content only. Popups live inside the
same Compose scene, allowing modal and nonmodal behavior without creating
an Android window that breaks backdrop sampling.

The optional non-Sketch renderer still exposes `GlassQuality.Fast`,
`Standard` and `Ultra` and uses capped GPU readback. Its optical output is
not the acceptance target for this project. Device visual and interaction
acceptance remains tracked in the checklist above; passing builds and unit
tests does not establish frame-for-frame visual parity.
The demo is a **component playground** rather than an implementation of
FlClash's VPN, profiles, settings, or state model.

## Attribution and license

Original FlClash by chen08209 and contributors; original source bundled in
`FlClash/` and licensed **GNU GPL-3.0** (`FlClash/LICENSE`). The optical
and interaction algorithms in this port are adapted from that code, so
redistribution of the adapted source must follow the terms of GPL-3.0.
Preserve the copyright and attribution notices when distributing it.
