plugins {
    alias(libs.plugins.kotlin.jvm)
    `java-library`
}

dependencies {
    // use JUnit5
    testImplementation(libs.junit.api)
    testImplementation(libs.junit.engine)
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
