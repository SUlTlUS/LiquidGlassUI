plugins {
    id("com.android.library") version "9.3.1" apply false
    id("com.android.application") version "9.3.1" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.4.10" apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "2.4.10" apply false
}

// One consumer dependency; Gradle/Maven metadata carries the two shared rendering backends.
subprojects {
    if (name in setOf("liquidglass", "melox-tabbar", "liquidglass-ios27")) {
        val libraryProject = this
        group = "dev.liquidglass"
        version = "2.0.1"
        plugins.withId("maven-publish") {
            gradle.projectsEvaluated {
                libraryProject.extensions.configure<PublishingExtension> {
                    publications.create<MavenPublication>("release") {
                        from(libraryProject.components["release"])
                        artifactId = when (libraryProject.name) {
                            "liquidglass" -> "liquidglass-core"
                            "melox-tabbar" -> "liquidglass-navigation"
                            else -> "liquidglass-ui"
                        }
                        pom {
                            name.set("LiquidGlass UI")
                            description.set("Native Compose liquid glass components for Android 13+, with iOS 26 and 27 styles")
                            url.set("https://github.com/SUlTlUS/LiquidGlassUI")
                            licenses {
                                license {
                                    name.set(if (libraryProject.name == "liquidglass") "Apache License 2.0" else "GNU General Public License v3.0")
                                    url.set(if (libraryProject.name == "liquidglass") "https://www.apache.org/licenses/LICENSE-2.0" else "https://www.gnu.org/licenses/gpl-3.0.html")
                                }
                            }
                        }
                    }
                    repositories.maven {
                        name = "LocalDistribution"
                        url = uri(rootProject.layout.projectDirectory.dir("dist/maven"))
                    }
                }
            }
        }
    }
}

tasks.register("packageLibrary") {
    group = "distribution"
    description = "Build AARs, sources and transitive Maven metadata in dist/maven; does not upload."
    dependsOn(":liquidglass:publishReleasePublicationToLocalDistributionRepository",
        ":melox-tabbar:publishReleasePublicationToLocalDistributionRepository",
        ":liquidglass-ios27:publishReleasePublicationToLocalDistributionRepository")
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
