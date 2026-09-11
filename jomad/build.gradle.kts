plugins {
    kotlin("jvm") version "2.4.10"
    id("com.gradleup.shadow") version "9.6.1"
}

group = "com.github.moritisimor"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(25)
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "com.github.moritisimor.jomad.MainKt"
    }
}

tasks.test {
    useJUnitPlatform()
}