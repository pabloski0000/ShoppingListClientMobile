
val ktor_version = "2.2.1"
val logback_version = "1.3.5"
val room_version = "2.4.3"
val version = "3.2.0"
val kmm_viewmodel_version = "1.0.0-ALPHA-8"

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    kotlin("plugin.serialization") version "1.8.21"
    id("org.jlleitschuh.gradle.ktlint") version "11.0.0"
    kotlin("kapt")

    //KMM-ViewModel
    id("com.google.devtools.ksp") version "1.7.22-1.0.8"
    id("com.rickclephas.kmp.nativecoroutines") version "1.0.0-ALPHA-8"
}

kotlin {
    android()
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
            linkerOpts("-undefined", "dynamic_lookup")
        }
    }

    sourceSets {
        all {
            languageSettings.optIn("kotlin.experimental.ExperimentalObjCName")
        }
        val commonMain by getting {
            dependencies {
                //Ktor
                implementation("io.ktor:ktor-client-core:$ktor_version")
                implementation("io.ktor:ktor-client-cio:$ktor_version")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
                implementation("io.ktor:ktor-client-content-negotiation:$ktor_version")
                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")
                implementation("io.ktor:ktor-client-logging:$ktor_version")
                //Logback
                implementation("ch.qos.logback:logback-classic:$logback_version")
                //KMM-ViewModel
                implementation("com.rickclephas.kmm:kmm-viewmodel-core:$kmm_viewmodel_version")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation("io.ktor:ktor-client-mock:$ktor_version")
            }
        }
        val androidMain by getting {
            dependencies {
                //KMM-ViewModel
                implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
            }
        }
        val androidTest by getting
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
            dependencies {
                implementation("io.ktor:ktor-client-darwin:2.2.1")
                implementation("com.rickclephas.kmp:nsexception-kt-crashlytics:0.1.7")
            }
        }
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }
    }
}

android {
    namespace = "main.shoppilientmobile"
    compileSdk = 32
    defaultConfig {
        minSdk = 29
        targetSdk = 32
    }
}
