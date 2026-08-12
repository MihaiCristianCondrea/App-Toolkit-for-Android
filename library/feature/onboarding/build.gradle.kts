import com.mihaicristiancondrea.android.apptoolkit.buildlogic.VersioningExtension

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose)
    id("com.mihaicristiancondrea.android.apptoolkit.versioning")
}

val versioning = extensions.getByType<VersioningExtension>()

android {
    namespace = "com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding"
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
    api(project(":library:feature:settings"))

    api(libs.konfetti.compose)
    api(libs.lottie.compose)
}
