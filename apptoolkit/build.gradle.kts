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
        minSdk = 26
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile(name = "proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
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

    publishing {
        singleVariant("release") {}
    }
}

dependencies {

    // AndroidX
    api(dependencyNotation = libs.bundles.androidx.core)

    // Compose
    api(dependencyNotation = platform(libs.androidx.compose.bom))
    api(dependencyNotation = libs.bundles.androidx.compose)
    api(dependencyNotation = libs.androidx.material3.window.size)

    // Lifecycle
    api(dependencyNotation = libs.bundles.androidx.lifecycle)

    // Firebase
    api(dependencyNotation = platform(libs.firebase.bom))
    api(dependencyNotation = libs.bundles.firebase)

    // Google Play services & Play Store APIs
    api(dependencyNotation = libs.bundles.google.play)

    // Image loading
    api(dependencyNotation = libs.bundles.coil)

    // Kotlin Coroutines & Serialization
    api(dependencyNotation = libs.bundles.kotlinx)

    // Networking (Ktor)
    api(dependencyNotation = platform(libs.ktor.bom))
    api(dependencyNotation = libs.bundles.ktor)

    // Dependency Injection
    api(dependencyNotation = libs.bundles.koin)

    // UI utilities
    api(dependencyNotation = libs.bundles.ui.effects)
    api(dependencyNotation = libs.bundles.ui.richtext)

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
            version = "1.1.5"

            afterEvaluate {
                from(components["release"])
            }
        }
    }
}