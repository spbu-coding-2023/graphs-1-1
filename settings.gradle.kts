rootProject.name = "Graphses"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }

    versionCatalogs {
        create("libs") {
            val kotlin = version("kotlin", "1.9.20")
            val junit5 = version("junit5", "5.7.1")
            val compose = version("compose", "1.6.6")
            val composePlugin = version("compose-plugin", "1.6.2")
            val detekt = version("detekt", "1.23.6")

            // plugins
            plugin("kotlin-jvm", "org.jetbrains.kotlin.jvm").versionRef(kotlin)

            plugin("compose", "org.jetbrains.compose").versionRef(composePlugin)

            plugin("detekt", "io.gitlab.arturbosch.detekt").versionRef(detekt)

            // libraries
            library("kotlin-test", "org.jetbrains.kotlin", "kotlin-test").versionRef(kotlin)
            library("kotlin-test-junit", "org.jetbrains.kotlin", "kotlin-test-junit").versionRef(kotlin)

            library("junit-api", "org.junit.jupiter", "junit-jupiter-api").versionRef(junit5)
            library("junit-engine", "org.junit.jupiter", "junit-jupiter-engine").versionRef(junit5)

            library("compose-ui-tooling", "androidx.compose.ui", "ui-tooling").versionRef(compose)
            library("compose-ui-tooling-preview", "androidx.compose.ui", "ui-tooling-preview").versionRef(compose)
        }
    }

}

include("library")
include("composeApp")
