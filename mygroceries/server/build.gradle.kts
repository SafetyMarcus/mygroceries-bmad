import java.util.Properties

plugins {
    kotlin("jvm")
    id("io.ktor.plugin") version "2.3.9"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":shared"))
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("io.ktor:ktor-server-content-negotiation-jvm")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm")
    implementation("ch.qos.logback:logback-classic:1.4.11")

    // Exposed for database access
    implementation("org.jetbrains.exposed:exposed-core:0.49.0")
    implementation("org.jetbrains.exposed:exposed-dao:0.49.0")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.49.0")
    implementation("org.jetbrains.exposed:exposed-kotlin-datetime:0.49.0")

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
    testImplementation("io.ktor:ktor-server-tests-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("io.ktor:ktor-client-content-negotiation")
    testImplementation("com.h2database:h2:2.2.224")
    testImplementation("org.jetbrains.exposed:exposed-core:0.49.0")
    testImplementation("org.jetbrains.exposed:exposed-dao:0.49.0")
    testImplementation("org.jetbrains.exposed:exposed-jdbc:0.49.0")
    testImplementation("org.jetbrains.exposed:exposed-kotlin-datetime:0.49.0")
    testImplementation(sourceSets.main.get().output)
    testImplementation(project(":server"))
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
    extendsFrom(configurations.runtimeClasspath.get())
    files(sourceSets.main.get().output.classesDirs)
    files(sourceSets.main.get().output.resourcesDir)
}