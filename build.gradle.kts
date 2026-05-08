plugins {
    kotlin("jvm") version "2.3.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    google()
    mavenCentral()
    maven(url = "https://cache-redirector.jetbrains.com/intellij-dependencies")
    maven(url = "https://www.jetbrains.com/intellij-repository/releases")
    maven(url = "https://www.jetbrains.com/intellij-repository/snapshots")
    maven(url = "https://download.jetbrains.com/teamcity-repository")
    maven(url = "https://cache-redirector.jetbrains.com/packages.jetbrains.team/maven/p/grazi/grazie-platform-public")
}

configurations.all {
    resolutionStrategy.capabilitiesResolution.withCapability("org.lz4:lz4-java") {
        select("at.yawk.lz4:lz4-java:1.11.0")
    }
}

// The newest version requires JDK25
// val starterVersion = "LATEST-EAP-SNAPSHOT"
val starterVersion = "251.26927.53"

dependencies {
    // IDE Starter / Driver stack
    testImplementation("com.jetbrains.intellij.tools:ide-starter-squashed:$starterVersion")
    testImplementation("com.jetbrains.intellij.tools:ide-starter-junit5:$starterVersion")
    testImplementation("com.jetbrains.intellij.tools:ide-starter-driver:$starterVersion")
    testImplementation("com.jetbrains.intellij.driver:driver-client:$starterVersion")
    testImplementation("com.jetbrains.intellij.driver:driver-sdk:$starterVersion")
    testImplementation("com.jetbrains.intellij.driver:driver-model:$starterVersion")

    // JUnit 5 via BOM
    val junitBom = platform("org.junit:junit-bom:5.12.2")
    testImplementation(junitBom)
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // Other utilities used by tests
    testImplementation("org.kodein.di:kodein-di-jvm:7.20.2")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.10.1")
    testImplementation("com.fasterxml.jackson.core:jackson-databind:2.16.0")

    // FUS reporting
    testImplementation("com.jetbrains.fus.reporting:ap-validation:76")
    testImplementation("com.jetbrains.fus.reporting:model:76")
}

tasks.test {
    useJUnitPlatform()

    testLogging {
        events("passed", "skipped", "failed", "standardOut", "standardError")
    }
}


kotlin {
    jvmToolchain(21)
    sourceSets {
        getByName("test").kotlin.srcDirs("testSrc")
    }
}
