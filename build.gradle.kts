plugins {
    kotlin("jvm") version "2.0.21"
    alias(libs.plugins.kotlin.serialization)
    `maven-publish`
    `java-library`

    //id("com.vanniktech.maven.publish") version "0.34.0"

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