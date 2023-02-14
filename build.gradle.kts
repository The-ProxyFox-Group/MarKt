plugins {
    kotlin("jvm") version "1.8.0"
    `maven-publish`
}

group = "dev.proxyfox"
version = "1.3"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
    explicitApi()
}

publishing {
    publications {
        register<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }

    repositories {
        maven {
            url = uri("https://maven.proxyfox.dev")
            credentials.username = System.getenv("PF_MAVEN_USER")
            credentials.password = System.getenv("PF_MAVEN_PASS")
        }
    }
}
