import io.gitlab.arturbosch.detekt.Detekt

plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.compose) apply false
    kotlin("plugin.serialization") version "1.6.10"
    `java-library`

    // formatter
    id("com.ncorti.ktfmt.gradle") version "0.18.0"

    // detekt
    alias(libs.plugins.detekt)
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

detekt {
    buildUponDefaultConfig = true // preconfigure defaults
    allRules = false // activate all available (even unstable) rules.
    config.setFrom("$projectDir/config/detekt.yml") // point to your custom config defining rules to run, overwriting default behavior
    baseline = file("$projectDir/config/baseline.xml") // a way of suppressing issues before introducing detekt
    source.setFrom("composeApp/src/main/kotlin", "composeApp/src/test/kotlin", "library/src/main/kotlin", "library/src/test/kotlin")
}

tasks.withType<Detekt>().configureEach {
    reports {
        html.required.set(true) // observe findings in your browser with structure and code snippets
        txt.required.set(true) // similar to the console output, contains issue signature to manually edit baseline files
        md.required.set(true) // simple Markdown format
    }
}

tasks.register("gitHooksCopy", Copy::class.java) {
    description = "Copies git hooks from scripts/git-hooks to .git folder."
    group = "git hooks"
    from("$rootDir/scripts/git-hooks/pre-commit")
    into("$rootDir/.git/hooks/")
}
tasks.register("gitHooksInstall", Exec::class.java) {
    description = "Installs the pre-commit git hook from scripts/git-hooks."
    group = "git hooks"
    workingDir = rootDir
    commandLine = listOf("chmod")
    args("-R", "+x", ".git/hooks/")
    dependsOn("gitHooksCopy")
    doLast {
        logger.info("Git hook installed successfully.")
    }
}
