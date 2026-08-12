import com.mihaicristiancondrea.android.apptoolkit.buildlogic.VersioningExtension

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    id("com.mihaicristiancondrea.android.apptoolkit.versioning")
    id("com.mihaicristiancondrea.android.apptoolkit.unit-test")
    id("com.mihaicristiancondrea.android.apptoolkit.jvm-target")
}

val versioning = extensions.getByType<VersioningExtension>()

android {
    namespace = "com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter"
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
    api(project(":library:core:common"))
    api(project(":library:core:network"))
    api(project(":library:core:ui"))
    api(project(":library:navigation"))
    api(libs.kotlinx.serialization.json)
}
