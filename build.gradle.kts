plugins {
    kotlin("jvm") version "2.0.21"
    alias(libs.plugins.kotlin.serialization)
    `maven-publish`
    `java-library`
    id("com.vanniktech.maven.publish") version "0.34.0"
}

group = "co.hashline"
version = "1.0"

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
    testImplementation(libs.jetbrains.kotlinx.coroutines.test)
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}

// Publishing configuration
publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            
            groupId = "co.hashline"
            artifactId = "events-kt"
            version = "1.0"
            
            pom {
                name.set("Events-KT")
                description.set("A lightweight, coroutine-based event bus library for Kotlin applications")
                url.set("https://github.com/hash-line/events-kt")
                
                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }
                
                developers {
                    developer {
                        id.set("hashline")
                        name.set("Hashline")
                        email.set("contact@hashline.co")
                    }
                }
                
                scm {
                    connection.set("scm:git:git://github.com/hash-line/events-kt.git")
                    developerConnection.set("scm:git:ssh://github.com:hash-line/events-kt.git")
                    url.set("https://github.com/hash-line/events-kt")
                }
            }
        }
    }
    
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/hash-line/events-kt")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("USERNAME")
                password = project.findProperty("gpr.key") as String? ?: System.getenv("TOKEN")
            }
        }
    }
}

// Maven Central publishing configuration
mavenPublishing {
    // Only sign when running in CI (GitHub Actions)
    if (System.getenv("CI") == "true") {
        signAllPublications()
    }

    coordinates("co.hashline", "events-kt", version.toString())

    pom {
        name = "Events-KT"
        description = "A lightweight, coroutine-based event bus library for Kotlin applications with support for reactive programming patterns."
        inceptionYear = "2025"
        url = "https://github.com/hash-line/events-kt"
        licenses {
            license {
                name = "MIT License"
                url = "https://opensource.org/licenses/MIT"
                distribution = "repo"
            }
        }
        developers {
            developer {
                id = "HashimAli09"
                name = "Hashim Ali"
                url = "https://github.com/HashimAli09"
            }
        }
        scm {
            url = "https://github.com/hash-line/events-kt"
            connection = "scm:git:git://github.com/hash-line/events-kt.git"
            developerConnection = "scm:git:ssh://github.com/hash-line/events-kt.git"
        }
    }
}

// Task for manual Maven Central publication
tasks.register("publishToMavenCentral") {
    group = "publishing"
    description = "Publishes the library to Maven Central"
    dependsOn("publishAllPublicationsToMavenCentralRepository")
}