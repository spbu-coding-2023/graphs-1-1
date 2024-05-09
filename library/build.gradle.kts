plugins {
    alias(libs.plugins.kotlin.jvm)
    `java-library`
}

dependencies {
    // use JUnit5
    testImplementation(libs.junit.api)
    testImplementation(libs.junit.engine)

    // gephi toolkit
    implementation(files("libs/gephi-toolkit-0.10.0-all.jar"))
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

tasks.register("downloadGephiToolkit") {
    val path = "libs/gephi-toolkit-0.10.0-all.jar"
    val sourceUrl = "https://github.com/gephi/gephi-toolkit/releases/download/v0.10.0/gephi-toolkit-0.10.0-all.jar"

    val libsDirectory = File("library/libs")
    val jarFile = File(path)

    if (!libsDirectory.exists())
        libsDirectory.mkdir()

    if (!jarFile.exists())
        download(sourceUrl, path)
}

tasks.build {
    dependsOn("downloadGephiToolkit")
}

fun download(url: String, path: String){
    val destinationFile = File(path)
    ant.invokeMethod("get", mapOf("src" to url, "dest" to destinationFile))
}