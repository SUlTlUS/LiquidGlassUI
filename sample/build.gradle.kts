import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.plugin.compose")
    id("org.jetbrains.kotlin.plugin.serialization")
}
android {
    namespace = "dev.liquidglass.sample"
    compileSdk = 37
    defaultConfig {
        applicationId = "dev.liquidglass.sample"
        minSdk = 33
        targetSdk = 36
        versionCode = 20
        versionName = "1.0.19"
    }
    buildFeatures { compose = true }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}
tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_21)
        freeCompilerArgs.add("-Xcontext-parameters")
    }
}
dependencies {
    implementation(project(":liquidglass-ios27"))
    implementation("androidx.activity:activity-compose:1.13.0")
    implementation("androidx.startup:startup-runtime:1.2.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.11.0")
    implementation("top.yukonga.miuix.kmp:miuix-nav-android:0.9.4-rc01")
}

fun registerQqApkSendTask(
    taskName: String,
    buildType: String,
    verificationTasks: List<String> = emptyList(),
) = tasks.register(taskName) {
    if (verificationTasks.isNotEmpty()) {
        dependsOn(verificationTasks)
    }

    doLast {
        if (System.getenv("QQ_APK_AUTO_SEND") == "0") {
            logger.lifecycle("[QQ APK] Auto-send disabled by QQ_APK_AUTO_SEND=0.")
            return@doLast
        }

        val apkDirectory = layout.buildDirectory.dir("outputs/apk/$buildType").get().asFile
        val apk = apkDirectory
            .listFiles()
            ?.filter { it.isFile && it.extension.equals("apk", ignoreCase = true) }
            ?.maxByOrNull { it.lastModified() }

        if (apk == null) {
            logger.warn("[QQ APK] APK not found under: ${apkDirectory.absolutePath}")
            return@doLast
        }

        val script = rootProject.file("tools/send-apk-to-qq.ps1")
        val exitCode = ProcessBuilder(
            "powershell.exe",
            "-NoProfile",
            "-STA",
            "-ExecutionPolicy",
            "Bypass",
            "-File",
            script.absolutePath,
            "-ApkPath",
            apk.absolutePath,
        )
            .directory(rootProject.projectDir)
            .inheritIO()
            .start()
            .waitFor()

        if (exitCode != 0) {
            logger.warn("[QQ APK] Send failed with exit code $exitCode; APK build remains successful.")
        }
    }
}

val sendDebugApkToQq = registerQqApkSendTask(
    taskName = "sendDebugApkToQQ",
    buildType = "debug",
    verificationTasks = listOf(":verifyDebugArtifacts", ":verifyLiquidGlassSpec"),
)
val sendReleaseApkToQq = registerQqApkSendTask(
    taskName = "sendReleaseApkToQQ",
    buildType = "release",
)

// Distribution builds have no sending side effect; QQ tasks are explicitly invoked only.
