import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.compose)
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

dependencies {
    // common
    implementation(compose.runtime)
    implementation(compose.foundation)
    implementation(compose.material3)
    implementation("androidx.compose.material3:material3-common:1.0.0-alpha01")
    implementation("androidx.lifecycle:lifecycle-viewmodel:2.8.0")
    implementation(compose.ui)
    implementation(compose.components.uiToolingPreview)
    // // add library directory
    implementation(projects.library)

    // desktop
    implementation(compose.desktop.currentOs)

    // resources
    implementation(compose.components.resources)

    // serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")

    // neo4j
    implementation("org.neo4j.driver:neo4j-java-driver:5.19.0")

    // Voyager
    val voyagerVersion = "1.0.0"
    implementation("cafe.adriel.voyager:voyager-navigator:$voyagerVersion")
    implementation("cafe.adriel.voyager:voyager-screenmodel:$voyagerVersion")
    implementation("cafe.adriel.voyager:voyager-bottom-sheet-navigator:$voyagerVersion")
    implementation("cafe.adriel.voyager:voyager-tab-navigator:$voyagerVersion")
    implementation("cafe.adriel.voyager:voyager-transitions:$voyagerVersion")

    // koin
    val koin = "3.6.0-alpha3"
    implementation("io.insert-koin:koin-core:$koin")
    implementation("io.insert-koin:koin-compose:1.2.0-alpha3")

    // extend icons
    implementation(compose.materialIconsExtended)
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Deb, TargetFormat.Exe)
            packageName = "Graphses"
            packageVersion = "1.0.0"
        }
    }
}
