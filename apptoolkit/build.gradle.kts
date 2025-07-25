import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(notation = libs.plugins.android.library)
    alias(notation = libs.plugins.kotlin.android)
    alias(notation = libs.plugins.mannodermaus)
    alias(notation = libs.plugins.compose.compiler)
    alias(notation = libs.plugins.about.libraries)
    alias(notation = libs.plugins.kotlin.serialization)
    `maven-publish`
}

android {

    namespace = "com.d4rk.android.libs.apptoolkit"
    compileSdk = 36

    defaultConfig {
        minSdk = 23
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile(name = "proguard-android-optimize.txt") , "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }

    buildFeatures {
        compose = true
    }
    testOptions {
        unitTests.all {
            it.useJUnitPlatform()
            it.jvmArgs("-XX:+EnableDynamicAgentLoading")
        }
    }
}

dependencies {

    //AndroidX
    api(dependencyNotation = libs.androidx.core.ktx)
    api(dependencyNotation = libs.androidx.appcompat)
    api(dependencyNotation = libs.androidx.core.splashscreen)
    api(dependencyNotation = libs.androidx.multidex)
    api(dependencyNotation = libs.androidx.work.runtime.ktx)

    // Compose
    api(dependencyNotation = platform(libs.androidx.compose.bom))
    api(dependencyNotation = libs.androidx.ui)
    api(dependencyNotation = libs.androidx.activity.compose)
    api(dependencyNotation = libs.androidx.ui.graphics)
    api(dependencyNotation = libs.androidx.compose.runtime)
    api(dependencyNotation = libs.androidx.runtime.livedata)
    api(dependencyNotation = libs.androidx.ui.tooling.preview)
    api(dependencyNotation = libs.androidx.material3)
    api(dependencyNotation = libs.androidx.material.icons.extended)
    api(dependencyNotation = libs.androidx.foundation)
    api(dependencyNotation = libs.datastore.preferences.core)
    api(dependencyNotation = libs.androidx.datastore.preferences)
    api(dependencyNotation = libs.androidx.datastore)
    api(dependencyNotation = libs.androidx.datastore.core)
    api(dependencyNotation = libs.androidx.navigation.compose)
    api(dependencyNotation = libs.androidx.graphics.shapes)

    // Firebase
    api(dependencyNotation = platform(libs.firebase.bom))
    api(dependencyNotation = libs.firebase.analytics)
    api(dependencyNotation = libs.firebase.crashlytics)
    api(dependencyNotation = libs.firebase.perf)
    api(dependencyNotation = libs.firebase.appcheck.playintegrity)

    // Google
    api(dependencyNotation = libs.play.services.ads)
    api(dependencyNotation = libs.user.messaging.platform)
    api(dependencyNotation = libs.material)
    api(dependencyNotation = libs.app.update.ktx)
    api(dependencyNotation = libs.billing)
    api(dependencyNotation = libs.review.ktx)
    api(dependencyNotation = libs.integrity)

    // Images
    api(dependencyNotation = libs.coil.compose)
    api(dependencyNotation = libs.coil.gif)
    api(dependencyNotation = libs.coil.network.okhttp)

    // Kotlin
    api(dependencyNotation = libs.kotlinx.coroutines.android)
    api(dependencyNotation = libs.kotlinx.serialization.json)

    // Ktor
    api(dependencyNotation = platform(libs.ktor.bom))
    api(dependencyNotation = libs.ktor.client.android)
    api(dependencyNotation = libs.ktor.client.serialization)
    api(dependencyNotation = libs.ktor.client.logging)
    api(dependencyNotation = libs.ktor.client.content.negotiation)
    api(dependencyNotation = libs.ktor.serialization.kotlinx.json)

    // Koin
    api(dependencyNotation = libs.bundles.koin)

    // Konfetti
    api(dependencyNotation = libs.konfetti.compose)

    // Lottie
    api(dependencyNotation = libs.lottie.compose)

    // Lifecycle
    api(dependencyNotation = libs.androidx.lifecycle.runtime.ktx)
    api(dependencyNotation = libs.androidx.lifecycle.livedata.ktx)
    api(dependencyNotation = libs.androidx.lifecycle.process)
    api(dependencyNotation = libs.androidx.lifecycle.viewmodel.ktx)
    api(dependencyNotation = libs.androidx.lifecycle.viewmodel.compose)
    api(dependencyNotation = libs.androidx.lifecycle.runtime.compose)

    // About
    api(dependencyNotation = libs.aboutlibraries.compose.m3)
    api(dependencyNotation = libs.core)
    api(dependencyNotation = libs.compose.markdown)

    // Unit Tests
    testImplementation(dependencyNotation = libs.bundles.unitTest)
    testRuntimeOnly(dependencyNotation = libs.bundles.unitTestRuntime)

    // Instrumentation Tests
    androidTestImplementation(dependencyNotation = libs.bundles.instrumentationTest)
    debugImplementation(dependencyNotation = libs.androidx.ui.test.manifest)
}

publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = "com.github.MihaiCristianCondrea"
            artifactId = "App-Toolkit-for-Android"
            version = "1.1.0"

            afterEvaluate {
                from(components["release"])
            }
        }
    }
}