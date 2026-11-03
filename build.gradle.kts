plugins {
    id("com.android.library") version "9.3.1" apply false
    id("com.android.application") version "9.3.1" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.4.10" apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "2.4.10" apply false
}

tasks.register<Exec>("verifyDebugArtifacts") {
    group = "verification"
    description = "Reject AAR/APK files missing the framework's runtime class definitions."
    dependsOn(":liquidglass:assembleDebug", ":liquidglass-ios27:assembleDebug", ":sample:packageDebug")
    workingDir(rootDir)
    commandLine("powershell.exe", "-NoProfile", "-File", "tools/verify-android-artifacts.ps1")
}

tasks.register<Exec>("verifyIos27Merge") {
    group = "verification"
    description = "Verify the complete iOS 27 import and preserved iOS 26 runtime."
    workingDir(rootDir)
    commandLine("node", "tools/verify-ios27-merge.mjs")
}

tasks.register<Exec>("verifyLiquidGlassSpec") {
    group = "verification"
    description = "Verify the project-specific LiquidGlass modification boundaries and UI guards."
    workingDir(rootDir)
    commandLine("node", "tools/verify-liquidglass-spec.mjs")
}
