plugins {
    kotlin("multiplatform")
    id("com.android.kotlin.multiplatform.library")
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinxSerialization)
}

kotlin {
    compilerOptions {
        optIn.add("kotlin.uuid.ExperimentalUuidApi")
        optIn.add("kotlin.time.ExperimentalTime")
    }
    android {
        compilerOptions {
            namespace = "com.safetymarcus.mygroceries.shared"
            compileSdk = 34
            minSdk = 24
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
        }
    }
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
        }
    }
    jvm()
    js() {
        browser()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.serialization.kotlinx.json)
                implementation(libs.koin.core) // Koin core is multiplatform
                implementation(libs.kotlinx.datetime)
                implementation(compose.material3)
                implementation(libs.navigation.compose)
            }
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