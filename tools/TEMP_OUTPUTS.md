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
