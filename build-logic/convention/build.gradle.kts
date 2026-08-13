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
    id("org.gradle.kotlin.kotlin-dsl") version "6.7.6"
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
