plugins {
    kotlin("js")

}

repositories {
    mavenCentral()
}

kotlin {
    js(IR) {
        browser {
            commonWebpackConfig { }
        }
        binaries.executable()
    }
    sourceSets {
        val main by getting {
            dependencies {
                implementation(project(":shared"))
            }
        }
    }
}


