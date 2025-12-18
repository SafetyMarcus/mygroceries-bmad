plugins {
    kotlin("multiplatform")
    id("com.android.library")

    alias(libs.plugins.kotlinxSerialization)
}

kotlin {
    compilerOptions {
        optIn.add("kotlin.uuid.ExperimentalUuidApi")
        optIn.add("kotlin.time.ExperimentalTime")
    }
    androidTarget {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_1_8)
        }
    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    jvm()
    js() {
        browser()
    }

    sourceSets {
        commonMain.dependencies {
            // Only truly common (non-UI) dependencies here
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.koin.core) // Koin core is multiplatform
        }
        androidMain.dependencies {
            implementation(libs.ktor.client.okhttp)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
            // Add iOS-specific Compose Multiplatform dependencies here if needed
        }
        jvmMain.dependencies {
            // JVM-specific dependencies (non-UI)
        }
        jsMain.dependencies {
            // No Compose dependencies here
        }
    }
}

android {
    namespace = "com.safetymarcus.mygroceries.shared"
    compileSdk = 34
    defaultConfig {
        minSdk = 24
    }
}