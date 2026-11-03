# Temporary tool output

Generated scratch output belongs under the repository's ignored `.tmp/` directory. Use `createTempOutputDir(scope)` from `tools/temp-output.mjs` to create a unique run directory at `.tmp/<scope>/<UTC timestamp>-<random id>/`.

For example, the native pixel comparison can write to a fresh temporary directory with:

```powershell
node tools/figma-reader/compare-native-pixels.mjs <device> --scratch [example-key ...]
```

The comparison tool keeps its stable `docs/iphone-examples-compare/` output by default. Temporary outputs can be removed by scope or all at once:

```powershell
node tools/temp-output.mjs clean iphone-examples-compare
node tools/temp-output.mjs clean
```

Keep reusable scripts in named tool files; do not commit one-off `tmp-*` scripts or scratch output in `docs/`. Save results intended as project evidence under a descriptive, stable `docs/` path.

## Directory ownership

- Module directories contain source and Gradle configuration. Their `build/` directories are generated; keep distributable APK/AAR files in `build/outputs/` or `dist/`.
- `dist/` contains local Maven packages and release deliveries; keep it separate from scratch cleanup.
- `docs/` contains specifications, frozen extraction baselines, audit manifests and retained evidence. Fullwidth screenshots previously at the repository root are in `docs/screenshots/fullwidth/`.
- `tools/` contains reusable scripts; `.tools/` holds local tool environments. `.codegraph/` holds the project index.
- `.gradle/` and `.kotlin/` are local caches. Avoid moving them while build daemons may use them.
- Do not extract dependency JAR contents (`androidx/`, `META-INF/`, `.class` files) into the repository root. Use a fresh `.tmp/` scope.

The 2026-11-03 cleanup records exact archived paths and hashes in `docs/workspace-cleanup-changes.json`. Scratch files were moved to a sibling archive for recovery, rather than permanently deleted. Existing source changes and release deliveries were preserved.
