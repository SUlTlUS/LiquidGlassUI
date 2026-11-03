# Ordinary control press feedback

`Modifier.ios27PressedClickable` supplies the uniform pressed mask for ordinary
controls. Glass controls retain their existing `InteractiveHighlight`.

The modifier handles `PressInteraction` events directly. Press shows the mask
immediately, release fades it out over 120 ms, and cancellation clears it. This
avoids losing a short tap when press and release occur between Compose frames.
`Ios27ListRow` uses the same modifier instead of collecting a pressed Boolean.

Keep the modifier in composition when disabling a control after a click; update
its `enabled` parameter so the release animation can complete. The caller owns
the hit target and clipping shape.

The change is limited to feedback timing. It does not change list dimensions,
separators, tokens, disclosure indicators, or the glass rendering implementation.

Validation: the consuming soundbar 1.0.0 release, framework compilation, sample
debug build, token check, and framework specification check pass. The extraction
hash check is blocked by an existing unrelated `ProgressiveBlurContent.kt` change.
Physical tap feedback remains user-tested; a successful build is not visual
acceptance.
