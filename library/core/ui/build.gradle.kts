import com.mihaicristiancondrea.android.apptoolkit.buildlogic.VersioningExtension

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose)
    id("com.mihaicristiancondrea.android.apptoolkit.versioning")
    id("com.mihaicristiancondrea.android.apptoolkit.unit-test")
    id("com.mihaicristiancondrea.android.apptoolkit.jvm-target")
    id("com.mihaicristiancondrea.android.apptoolkit.library-publish")
}

val versioning = extensions.getByType<VersioningExtension>()

android {
    namespace = "com.mihaicristiancondrea.android.libs.apptoolkit.core.ui"
    compileSdk = versioning.compileSdk

    defaultConfig {
        minSdk = versioning.minSdk
    }

    buildFeatures {
        compose = true
    }
}

composeCompiler {
    // See the file itself for why each entry is there.
    stabilityConfigurationFiles.add(layout.projectDirectory.file("compose-stability.conf"))
}

dependencies {
    testImplementation(project(":library:core:testing"))
    api(project(":library:core:common"))
    api(project(":library:core:designsystem"))
    api(project(":library:navigation"))
    api(platform(libs.androidx.compose.bom))
    api(libs.bundles.androidx.compose)
    api(libs.androidx.compose.ui.tooling.preview)
    api(libs.bundles.androidx.navigation3)
    api(libs.bundles.androidx.lifecycle)
    api(libs.bundles.coil)
    api(libs.bundles.koin)
    api(libs.kotlinx.collections.immutable)
    api(libs.androidx.window)

    // Declares the Material theme attributes (colorPrimaryContainer, colorSecondaryContainer,
    // colorTertiary, …) that vector drawables in the feature modules reference as ?attr/. AppCompat
    // alone does not define them, so without this the release resource link fails with
    // "resource attr/colorPrimaryContainer not found". It used to arrive transitively from
    // :library:apptoolkit, which is the only module that pulls the `google` bundle; splitting the
    // features out left them linking against attributes nothing on their classpath declared.
    api(libs.google.material)
}
