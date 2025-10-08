plugins {
    kotlin("jvm") version "2.0.21"
    alias(libs.plugins.kotlin.serialization)
    //alias(libs.plugins.ksp)
}

group = "co.hashline"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {

    implementation(libs.jetbrains.kotlin.stdlib)

    // Coroutines for using Kotlin Flows
    implementation(libs.jetbrains.kotlinx.coroutines)
    implementation(libs.jetbrains.kotlinx.serialization)

    // Add kotlinx-datetime dependency
    implementation(libs.jetbrains.kotlinx.datetime)

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}