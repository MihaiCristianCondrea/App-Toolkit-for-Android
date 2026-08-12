import com.mihaicristiancondrea.android.apptoolkit.buildlogic.VersioningExtension

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose)
    id("com.mihaicristiancondrea.android.apptoolkit.versioning")
    id("com.mihaicristiancondrea.android.apptoolkit.unit-test")
    id("com.mihaicristiancondrea.android.apptoolkit.jvm-target")
}

val versioning = extensions.getByType<VersioningExtension>()

android {
    namespace = "com.mihaicristiancondrea.android.libs.apptoolkit.core.common"
    compileSdk = versioning.compileSdk

    defaultConfig {
        minSdk = versioning.minSdk
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    testImplementation(project(":library:core:testing"))
    api(platform(libs.androidx.compose.bom))
    api(libs.androidx.compose.material3)

    api(libs.kotlinx.coroutines.android)
    api(libs.kotlinx.coroutines.play.services)
    api(libs.koin.android)
    api(libs.androidx.multidex)
    api(libs.androidx.lifecycle.runtime.ktx)
    api(libs.androidx.lifecycle.process)
    
    api(platform(libs.firebase.bom))
    api(libs.firebase.analytics)
    api(libs.firebase.appcheck.playintegrity)
    api(libs.firebase.crashlytics)
    api(libs.firebase.perf)

    api(libs.google.ads.mobile.sdk)

    // api(project(":library:core:datastore")) // Removed to break circular dependency
}
