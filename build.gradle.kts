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
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4") // kotlin coroutines
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