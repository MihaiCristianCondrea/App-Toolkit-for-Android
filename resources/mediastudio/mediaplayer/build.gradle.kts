plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.mihaicristiancondrea.libs.mediaplayer"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    buildFeatures {
        compose = true
    }

}

dependencies {
    api(platform(libs.androidx.compose.bom))

    api(libs.bundles.compose.ui)
    api(libs.bundles.androidx.common)
    api(libs.bundles.media3)
    api(libs.bundles.koin)
    api(libs.coil.compose)

    implementation(libs.kotlinx.coroutines.guava)
    implementation(libs.material)

    testImplementation(libs.junit)

    androidTestImplementation(libs.bundles.android.test)
}