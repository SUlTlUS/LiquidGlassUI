import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("maven-publish")
    id("com.android.library")
    id("org.jetbrains.kotlin.plugin.compose")
}
android {
    namespace = "com.lladlam.melox"
    compileSdk = 37
    defaultConfig { minSdk = 33 }
    buildFeatures { compose = true }
    publishing { singleVariant("release") { withSourcesJar() } }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}
tasks.withType<KotlinCompile>().configureEach {
    compilerOptions { jvmTarget.set(JvmTarget.JVM_21) }
}
dependencies {
    api(platform("androidx.compose:compose-bom:2026.06.00"))
    api("androidx.compose.foundation:foundation")
    api("androidx.compose.material3:material3")
    api(project(":liquidglass"))
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.10.0")
    implementation("androidx.core:core-ktx:1.17.0")
    implementation("io.coil-kt.coil3:coil-compose:3.5.0")
    implementation("io.coil-kt.coil3:coil-network-okhttp:3.5.0")
    implementation("androidx.activity:activity-compose:1.13.0")
    implementation("com.squareup.okhttp3:okhttp:5.3.0")
    testImplementation("junit:junit:4.13.2")
}
tasks.register<Exec>("verifyExtraction") {
    group = "verification"
    workingDir(rootDir)
    commandLine("node", "tools/extract-melox-tabbar.mjs", "--check")
}
