# ToScreen input integration, 2.0.1

User requested updating the component dependency and using its text fields for device credentials and its Inspector example for add-device UI.

`GlassDialogTextField` now forwards standard Compose `enabled`, `visualTransformation`, `isError`, `label`, `supportingText`, and `trailingIcon` parameters. Defaults preserve existing callers. This allows a real masked password field with reveal/conceal, validation, and busy state without consumer copies of the field renderer. Optical and motion code is unchanged.

- File: `liquidglass-ios27/src/main/java/dev/liquidglass/GlassDialog.kt`
- Before SHA-256: `eb5e1ea65fa1b1f51cb0dec2107de1f701a6debfa6cb5e962b1eafc3a1696ba7`
- After SHA-256: `7bf0a2deb24ab927e46d35149c493740f99b355dc32b54a4dd38416b7d9f78ad`
- `build.gradle.kts` publishes all three shared modules as 2.0.1 into `dist/maven`, keeping the 2.0.0 distribution available. Frozen baseline hashes remain unchanged.
- Consumer: `C:/IDE/Android/transfer/toscreen`, 0.3.5-preview. Consumer compilation and tests exercise the new API; device appearance requires acceptance. No library device interaction is performed.
