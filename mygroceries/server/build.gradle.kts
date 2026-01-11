import java.util.Properties

val ktorVersion = "3.3.3"
val exposedVersion = "1.0.0-rc-4"

plugins {
    kotlin("jvm")
    id("io.ktor.plugin") version "3.3.3"
}

repositories {
    mavenCentral()
    google()
}

dependencies {
    implementation(project(":shared"))
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty.jvm)
    implementation(libs.ktor.server.cors.jvm)
    implementation(libs.ktor.server.content.negotiation.jvm)
    implementation(libs.ktor.server.request.validation.jvm)
    implementation(libs.ktor.server.status.pages.jvm)
    implementation(libs.ktor.serialization.kotlinx.json.jvm)
    implementation(libs.logback.classic)

    // Exposed for database access
    implementation(platform(libs.exposed.bom))
    implementation(libs.exposed.core)
    implementation(libs.exposed.dao)
    implementation(libs.exposed.jdbc)
    implementation(libs.exposed.kotlin.datetime)

    // HikariCP for connection pooling
    implementation(libs.hikaricp)

    // PostgreSQL driver
    implementation(libs.postgresql)

    // Flyway for database migrations
    implementation(libs.flyway.core)
    implementation(libs.flyway.database.postgresql)

    testImplementation("io.mockk:mockk:1.13.10")
    testImplementation("io.kotest:kotest-runner-junit5:5.8.1")
    testImplementation("io.kotest:kotest-assertions-core:5.8.1")
    testImplementation("io.ktor:ktor-client-content-negotiation:3.3.1")
    testImplementation("io.ktor:ktor-server-test-host-jvm:3.3.1")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("com.h2database:h2:2.2.224")
    testImplementation("org.jetbrains.exposed:exposed-core")
    testImplementation("org.jetbrains.exposed:exposed-dao")
    testImplementation("org.jetbrains.exposed:exposed-jdbc")
    testImplementation("org.jetbrains.exposed:exposed-kotlin-datetime")
    testImplementation(project(":server"))
}

kotlin {
    sourceSets.all {
        languageSettings {
            optIn("kotlin.uuid.ExperimentalUuidApi")
            optIn("kotlin.time.ExperimentalTime")
        }
        compilerOptions {
            freeCompilerArgs.add("-Xcontext-parameters")
        }
    }
}

application {
    mainClass.set("com.safetymarcus.mygroceries.server.ApplicationKt")
}

val localPropertiesMap: Map<String, String> = Properties().apply {
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        localPropertiesFile.inputStream().use { load(it) }
    }
}.mapKeys { it.key.toString() }.mapValues { it.value.toString() }

tasks.withType<Test> {
    useJUnitPlatform()
    systemProperties(localPropertiesMap.filterKeys { it.startsWith("db.") })
}

tasks.withType<JavaExec> {
    systemProperties(localPropertiesMap.filterKeys { it.startsWith("db.") })
}

tasks.named<Test>("test") {
    useJUnitPlatform()
    testLogging {
        showStandardStreams = true
    }
    classpath += files(sourceSets.main.get().output.classesDirs, sourceSets.main.get().output.resourcesDir)
}

configurations.testRuntimeClasspath {
    extendsFrom(configurations.implementation.get(), configurations.testImplementation.get())
}