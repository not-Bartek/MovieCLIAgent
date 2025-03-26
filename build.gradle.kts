plugins {
    kotlin("jvm") version "2.1.10"
    kotlin("plugin.serialization") version "1.8.0"
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.github.ajalt.clikt:clikt:3.4.0") // For CLI
    implementation("com.squareup.moshi:moshi:1.12.0") // For JSON parsing
    implementation("com.squareup.moshi:moshi-kotlin:1.12.0")
    implementation("io.ktor:ktor-client-core:2.0.0") // For HTTP client
    implementation("io.ktor:ktor-client-cio:2.0.0") // For HTTP client engine
    implementation("io.ktor:ktor-client-content-negotiation:2.0.0") // For Content Negotiation plugin
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.0.0") // For JSON serialization

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4") // kotlin coroutines
    implementation("io.ktor:ktor-client-logging:2.0.0") // For Logging plugin
    implementation("io.github.cdimascio:dotenv-kotlin:6.2.2") // For loading .env file
    testImplementation(kotlin("test"))
    implementation("com.squareup.okhttp3:okhttp:4.9.3") // for Okhttp client
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0") // serializable
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass.set("MainKt")
}
kotlin {
    jvmToolchain(21)
}
tasks.getByName("run", JavaExec::class) {
    standardInput = System.`in`
} //bug fix with terminal not waiting for user