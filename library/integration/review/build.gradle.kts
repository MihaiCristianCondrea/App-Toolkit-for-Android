import com.mihaicristiancondrea.android.apptoolkit.buildlogic.VersioningExtension

plugins {
    alias(libs.plugins.android.library)
    id("com.mihaicristiancondrea.android.apptoolkit.versioning")
}

val versioning = extensions.getByType<VersioningExtension>()

android {
    namespace = "com.mihaicristiancondrea.android.libs.apptoolkit.integration.review"
    compileSdk = versioning.compileSdk

    defaultConfig {
        minSdk = versioning.minSdk
    }
}

dependencies {
    api(project(":library:core:common"))
    api(project(":library:core:datastore"))
    api(libs.google.play.review.ktx)
}
