import com.mihaicristiancondrea.android.apptoolkit.buildlogic.VersioningExtension

plugins {
    alias(libs.plugins.android.library)
    id("com.mihaicristiancondrea.android.apptoolkit.versioning")
    id("com.mihaicristiancondrea.android.apptoolkit.jvm-target")
    id("com.mihaicristiancondrea.android.apptoolkit.unit-test")
}

val versioning = extensions.getByType<VersioningExtension>()

android {
    namespace = "com.mihaicristiancondrea.android.libs.apptoolkit.core.data.remote.network"
    compileSdk = versioning.compileSdk

    defaultConfig {
        minSdk = versioning.minSdk
    }
}

dependencies {
    testImplementation(project(":library:core:testing"))
    api(project(":library:core:common"))
    api(platform(libs.ktor.bom))
    api(libs.ktor.client.android)
    api(libs.ktor.client.content.negotiation)
    api(libs.ktor.client.logging)
    api(libs.ktor.serialization.kotlinx.json)
}
