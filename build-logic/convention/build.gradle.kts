/*
 * Copyright (©) 2026 Mihai-Cristian Condrea
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

plugins {
    // Applied by its full id with an explicit version rather than the `kotlin-dsl` accessor: JitPack
    // resolves this build twice, once for the included build and once for the publication, and the
    // accessor is not on its plugin classpath, so the build fails there without the coordinates
    // spelled out.
    //
    // The version must match the one bundled with the Gradle release in gradle-wrapper.properties.
    // Gradle warns ("expects version 'x' but version 'y' has been applied") on any mismatch, and the
    // mismatched plugin also drags in a different Kotlin than the embedded one, which the Kotlin DSL
    // does not support. When bumping Gradle, run any task and let the warning name the expected
    // version, Gradle 9.7 pairs with kotlin-dsl 6.7.3.
    //
    // The IDE's "a newer version is available" hint is wrong here for that reason, and acting on it
    // is not harmless: 6.7.9 resolves Kotlin 2.4.20-RC2 for the script compiler classpath, which is
    // the "different Kotlin than the embedded one" failure described above. Bump this only when the
    // Gradle wrapper moves and the warning names a new expected version.
    id("org.gradle.kotlin.kotlin-dsl") version "6.7.3"
}

group = "com.mihaicristiancondrea.android.apptoolkit.buildlogic"

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.mannodermaus.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("versioning") {
            id = "com.mihaicristiancondrea.android.apptoolkit.versioning"
            implementationClass = "com.mihaicristiancondrea.android.apptoolkit.buildlogic.VersioningPlugin"
        }
        register("jvmTarget") {
            id = "com.mihaicristiancondrea.android.apptoolkit.jvm-target"
            implementationClass = "com.mihaicristiancondrea.android.apptoolkit.buildlogic.JvmTargetPlugin"
        }
        register("unitTest") {
            id = "com.mihaicristiancondrea.android.apptoolkit.unit-test"
            implementationClass = "com.mihaicristiancondrea.android.apptoolkit.buildlogic.UnitTestPlugin"
        }
        register("libraryPublish") {
            id = "com.mihaicristiancondrea.android.apptoolkit.library-publish"
            implementationClass = "com.mihaicristiancondrea.android.apptoolkit.buildlogic.LibraryPublishPlugin"
        }
        register("sampleModule") {
            id = "com.mihaicristiancondrea.android.apptoolkit.sample-module"
            implementationClass = "com.mihaicristiancondrea.android.apptoolkit.buildlogic.SampleModulePlugin"
        }
    }
}
