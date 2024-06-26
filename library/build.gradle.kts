plugins {
    alias(libs.plugins.kotlin.jvm)
    `java-library`
    jacoco
}

jacoco {
    toolVersion = "0.8.11"
}

dependencies {
    // use JUnit5
    testImplementation(libs.junit.api)
    testImplementation(libs.junit.engine)

    // gephi toolkit
    implementation(files("libs/gephi-toolkit-0.10.0-all.jar"))

    // neo4j
    implementation("org.neo4j.driver:neo4j-java-driver:5.19.0")

    // jackson
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.17.0")

    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
    testImplementation("org.mockito:mockito-core:5.12.0")
    testImplementation("org.hamcrest:hamcrest:2.2")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
    testImplementation("org.testcontainers:testcontainers:1.19.8")
    testImplementation("org.testcontainers:junit-jupiter:1.19.8")
    testImplementation("org.testcontainers:neo4j:1.19.8")

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

tasks.named<JacocoReport>("jacocoTestReport") {
    reports {
        xml.required = false
        csv.required = true
        html.required = true
    }
    dependsOn(tasks.test)
}

tasks.named<JacocoCoverageVerification>("jacocoTestCoverageVerification") {
    violationRules {
        rule {
            limit {
                minimum = "0.7".toBigDecimal()
            }
        }
    }
}

tasks.build {
    dependsOn("downloadGephiToolkit")
}

fun download(url: String, path: String){
    val destinationFile = File(path)
    ant.invokeMethod("get", mapOf("src" to url, "dest" to destinationFile))
}