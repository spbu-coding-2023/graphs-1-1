plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.compose) apply false

    `java-library`

    // formatter
    id("com.ncorti.ktfmt.gradle") version "0.18.0"
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

ktfmt {
    // use official kotlin styles
    kotlinLangStyle()
}
