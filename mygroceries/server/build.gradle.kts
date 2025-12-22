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
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-netty-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-request-validation-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-status-pages-jvm:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktorVersion")
    implementation("ch.qos.logback:logback-classic:1.4.11")

    // Exposed for database access
    implementation(platform("org.jetbrains.exposed:exposed-bom:$exposedVersion"))
    implementation("org.jetbrains.exposed:exposed-core")
    implementation("org.jetbrains.exposed:exposed-dao")
    implementation("org.jetbrains.exposed:exposed-jdbc")
    implementation("org.jetbrains.exposed:exposed-kotlin-datetime")

    // HikariCP for connection pooling
    implementation("com.zaxxer:HikariCP:5.1.0")

    // PostgreSQL driver
    implementation("org.postgresql:postgresql:42.7.1")

    // Flyway for database migrations
    implementation("org.flywaydb:flyway-core:10.15.0")
    implementation("org.flywaydb:flyway-database-postgresql:10.15.0")

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