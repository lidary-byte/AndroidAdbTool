import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
}

group = "com.ohdu"
version = "1.0"

repositories {
    google()
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    maven("https://packages.jetbrains.team/maven/p/kpm/public/")
}

//java {
//    toolchain {
//        vendor = JvmVendorSpec.JETBRAINS
//        languageVersion = JavaLanguageVersion.of(17)
//    }
//}
dependencies {
    implementation(compose.desktop.currentOs)
    implementation(compose.material)
    implementation(compose.materialIconsExtended)
    implementation(compose.foundation)
    implementation(compose.animation)

    implementation("com.squareup.okio:okio:3.4.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.1.1")



    implementation("com.github.vidstige:jadb:v1.2.1")

    implementation("org.jetbrains.jewel:jewel-int-ui-standalone:0.15.0")
    // Optional, for custom decorated windows:
    implementation("org.jetbrains.jewel:jewel-int-ui-decorated-window:0.15.0")
    // https://github.com/Wavesonics/compose-multiplatform-file-picker
    implementation("com.darkrockstudios:mpfilepicker:3.1.0")
}



compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "AndroidAdbTool"
            packageVersion = "1.0.0"
        }
    }
}