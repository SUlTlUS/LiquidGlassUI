param(
    [string]$ProjectRoot = (Split-Path -Parent $PSScriptRoot),
    [string]$ApkPath = '',
    [switch]$ApkOnly
)
$ErrorActionPreference = 'Stop'
Add-Type -AssemblyName System.IO.Compression.FileSystem
if (!$ApkPath) { $ApkPath = Join-Path $ProjectRoot 'sample/build/outputs/apk/debug/sample-debug.apk' }
$required = @(
    'dev/liquidglass/ios27/Ios27PageThemeKt',
    'dev/liquidglass/ios27/Ios27ListsKt',
    'dev/liquidglass/ios27/Ios27Tokens',
    'com/zeaze/tianyinwallpaper/ui/commom/ProgressiveBlurContentKt',
    'com/zeaze/tianyinwallpaper/backdrop/DrawBackdropModifierKt',
    'com/zeaze/tianyinwallpaper/catalog/components/LiquidBottomTabsKt',
    'com/zeaze/tianyinwallpaper/catalog/components/LiquidToggleKt',
    'com/zeaze/tianyinwallpaper/catalog/components/LiquidSliderKt'
)
$ios27Required = @(
    'dev/liquidglass27/glass/Ios27GlassKt',
    'dev/liquidglass27/glass/GlassSceneKt',
    'dev/liquidglass27/glass/GlassControlsKt',
    'dev/liquidglass27/glass/GooButtonGroupKt',
    'dev/liquidglass27/glass/GlassDockKt',
    'dev/liquidglass27/glass/Ios27NavigationDockKt',
    'dev/liquidglass27/glass/PopupContentTransformKt',
    'dev/liquidglass27/glass/GlassMenuKt',
    'dev/liquidglass27/glass/GlassPopupKt',
    'dev/liquidglass27/glass/GlassDialogKt',
    'dev/liquidglass27/glass/GlassShaderPrograms',
    'dev/liquidglass27/glass/SketchMaterialCache',
    'dev/liquidglass27/glass/GlassGlyphs'
)
$ios27Assets = @('flclash_control_highlight', 'flclash_glass', 'flclash_glass_compat', 'sketch_contrast_tint', 'sketch_goo_lens')
function Read-ZipEntry($entry) {
    $inputStream = $entry.Open()
    $buffer = [System.IO.MemoryStream]::new()
    try { $inputStream.CopyTo($buffer); return ,$buffer.ToArray() }
    finally { $inputStream.Dispose(); $buffer.Dispose() }
}

if (!$ApkOnly) {
    $aar = [System.IO.Compression.ZipFile]::OpenRead((Join-Path $ProjectRoot 'liquidglass/build/outputs/aar/liquidglass-debug.aar'))
    try {
        $entry = $aar.GetEntry('classes.jar')
        if (!$entry) { throw 'AAR is missing classes.jar; compilation success is not packaging success.' }
        $stream = [System.IO.MemoryStream]::new((Read-ZipEntry $entry))
        $jar = [System.IO.Compression.ZipArchive]::new($stream)
        try {
            foreach ($name in $required) {
                if (!$jar.GetEntry("$name.class")) { throw "AAR missing definition: $name" }
            }
        } finally { $jar.Dispose(); $stream.Dispose() }
        Write-Output "PASS: AAR contains all $($required.Count) required class definitions."
    } finally { $aar.Dispose() }
}

# Inspect DEX class_defs, not string occurrences: references alone do not define classes.
if (!$ApkOnly) {
    $aar27 = [System.IO.Compression.ZipFile]::OpenRead((Join-Path $ProjectRoot 'liquidglass-ios27/build/outputs/aar/liquidglass-ios27-debug.aar'))
    try {
        $entry27 = $aar27.GetEntry('classes.jar')
        if (!$entry27) { throw 'iOS 27 AAR missing classes.jar' }
        $stream27 = [System.IO.MemoryStream]::new((Read-ZipEntry $entry27))
        $jar27 = [System.IO.Compression.ZipArchive]::new($stream27)
        try {
            foreach ($name in $ios27Required) {
                if (!$jar27.GetEntry("$name.class")) { throw "iOS 27 AAR missing definition: $name" }
            }
        } finally { $jar27.Dispose(); $stream27.Dispose() }
        foreach ($asset in $ios27Assets) {
            if (!$aar27.GetEntry("assets/shaders/$asset.agsl")) { throw "iOS 27 AAR missing shader: $asset" }
        }
        Write-Output 'PASS: iOS 27 AAR includes mechanism classes and all five shaders.'
    } finally { $aar27.Dispose() }
}
$defined = [System.Collections.Generic.HashSet[string]]::new()
$apk = [System.IO.Compression.ZipFile]::OpenRead($ApkPath)
try {
    foreach ($asset in $ios27Assets) {
        if (!$apk.GetEntry("assets/shaders/$asset.agsl")) { throw "APK missing iOS 27 shader: $asset" }
    }
    foreach ($dexEntry in $apk.Entries | Where-Object { $_.FullName -match '^classes\d*\.dex$' }) {
        [byte[]]$dex = Read-ZipEntry $dexEntry
        if ([System.Text.Encoding]::ASCII.GetString($dex, 0, 4) -ne "dex`n") { throw 'Unsupported DEX format' }
        $stringsOffset = [BitConverter]::ToUInt32($dex, 60)
        $typesOffset = [BitConverter]::ToUInt32($dex, 68)
        $classCount = [BitConverter]::ToUInt32($dex, 96)
        $classesOffset = [BitConverter]::ToUInt32($dex, 100)
        for ($index = 0; $index -lt $classCount; $index++) {
            $classType = [BitConverter]::ToUInt32($dex, $classesOffset + 32 * $index)
            $descriptor = [BitConverter]::ToUInt32($dex, $typesOffset + 4 * $classType)
            $cursor = [BitConverter]::ToUInt32($dex, $stringsOffset + 4 * $descriptor)
            do { $value = $dex[$cursor++]; } while ($value -band 128)
            $end = $cursor
            while ($dex[$end] -ne 0) { $end++ }
            [void]$defined.Add([System.Text.Encoding]::UTF8.GetString($dex, $cursor, $end - $cursor))
        }
    }
} finally { $apk.Dispose() }
foreach ($name in $required + $ios27Required + @(
    'dev/liquidglass/sample/MainActivity',
    'dev/liquidglass/sample/Ios27MechanismExamplePageKt',
    'dev/liquidglass/sample/Ios27NativeExamplesKt',
    'dev/liquidglass/sample/Ios27MechanismExample'
)) {
    if (!$defined.Contains("L$name;")) { throw "APK missing DEX class definition: $name" }
}
Write-Output "PASS: APK contains all required class definitions ($($defined.Count) total DEX classes)."
if ($defined | Where-Object { $_.StartsWith('Ldev/liquidglass27/demo/') }) {
    throw 'APK still contains copied source demo screens'
}
