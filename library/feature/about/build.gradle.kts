import com.mihaicristiancondrea.android.apptoolkit.buildlogic.VersioningExtension

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.about.libraries)
    id("com.mihaicristiancondrea.android.apptoolkit.versioning")
}

val versioning = extensions.getByType<VersioningExtension>()

android {
    namespace = "com.mihaicristiancondrea.android.libs.apptoolkit.feature.about"
    compileSdk = versioning.compileSdk

    defaultConfig {
        minSdk = versioning.minSdk
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    api(project(":library:core:common"))
    api(project(":library:core:datastore"))
    api(project(":library:core:network"))
    api(project(":library:core:ui"))
    api(project(":library:navigation"))
    api(project(":library:integration:consent"))
    api(project(":library:integration:review"))
    api(project(":library:integration:update"))
    api(project(":library:feature:support"))

    api(libs.aboutlibraries.compose.m3)
    api(libs.konfetti.compose)
    api(libs.compose.markdown)
}
