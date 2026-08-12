import com.mihaicristiancondrea.android.apptoolkit.buildlogic.VersioningExtension

plugins {
    alias(libs.plugins.android.library)
    id("com.mihaicristiancondrea.android.apptoolkit.versioning")
    id("com.mihaicristiancondrea.android.apptoolkit.unit-test")
    id("com.mihaicristiancondrea.android.apptoolkit.jvm-target")
}

val versioning = extensions.getByType<VersioningExtension>()

android {
    namespace = "com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent"
    compileSdk = versioning.compileSdk

    defaultConfig {
        minSdk = versioning.minSdk
    }
}

dependencies {
    testImplementation(project(":library:core:testing"))
    api(project(":library:core:common"))
    api(project(":library:core:datastore"))
    api(project(":library:core:network"))
    api(libs.google.user.messaging.platform)
}
