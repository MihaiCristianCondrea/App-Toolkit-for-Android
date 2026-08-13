import com.mihaicristiancondrea.android.apptoolkit.buildlogic.VersioningExtension

plugins {
    alias(libs.plugins.android.library)
    id("com.mihaicristiancondrea.android.apptoolkit.versioning")
    id("com.mihaicristiancondrea.android.apptoolkit.jvm-target")
    id("com.mihaicristiancondrea.android.apptoolkit.library-publish")
}

val versioning = extensions.getByType<VersioningExtension>()

android {
    namespace = "com.mihaicristiancondrea.android.libs.apptoolkit.integration.firebase"
    compileSdk = versioning.compileSdk

    defaultConfig {
        minSdk = versioning.minSdk
    }
}

dependencies {
    api(project(":library:core:common"))
    api(platform(libs.firebase.bom))
    api(libs.firebase.analytics)
    api(libs.firebase.crashlytics)
    api(libs.firebase.perf)
    api(libs.firebase.messaging)
}
