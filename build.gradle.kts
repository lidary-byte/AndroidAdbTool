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
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation(compose.material)
    implementation(compose.materialIconsExtended)
    implementation(compose.foundation)
    implementation(compose.animation)

    implementation("com.squareup.okio:okio:3.4.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.1.1")

    implementation("moe.tlaster:precompose:1.5.10")
    implementation("moe.tlaster:precompose-viewmodel:1.5.10")
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