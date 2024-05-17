import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.psi.simpleNameExpressionVisitor

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.compose)
}

dependencies {
    // common
    implementation(compose.runtime)
    implementation(compose.foundation)
    implementation(compose.material3)
    implementation(compose.ui)
    implementation(compose.components.uiToolingPreview)
    // // add library directory
    implementation(projects.library)

    // desktop
    implementation(compose.desktop.currentOs)

    // resources
    implementation(compose.components.resources)

    // serialization
//    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")

    // Voyager
    val voyagerVersion = "1.0.0"
    implementation("cafe.adriel.voyager:voyager-navigator:$voyagerVersion")
    implementation("cafe.adriel.voyager:voyager-screenmodel:$voyagerVersion")
    implementation("cafe.adriel.voyager:voyager-bottom-sheet-navigator:$voyagerVersion")
    implementation("cafe.adriel.voyager:voyager-tab-navigator:$voyagerVersion")
    implementation("cafe.adriel.voyager:voyager-transitions:$voyagerVersion")
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
