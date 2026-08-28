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
    alias(notation = libs.plugins.android.application) apply false
    alias(notation = libs.plugins.android.library) apply false
    alias(notation = libs.plugins.kotlin.compose) apply false
    alias(notation = libs.plugins.kotlin.serialization) apply false
    alias(notation = libs.plugins.google.mobile.services) apply false
    alias(notation = libs.plugins.firebase.crashlytics) apply false
    alias(notation = libs.plugins.firebase.performance) apply false
    alias(notation = libs.plugins.about.libraries) apply true
    alias(notation = libs.plugins.mannodermaus.android.junit5) apply false
}

val publishingGroupId = providers.gradleProperty("JITPACK_GROUP_ID")
val publishingVersion = providers.gradleProperty("PUBLISHING_VERSION")

subprojects {
    // The sample gets its own group. Project dependencies resolve by `group:name:version`, and the
    // sample mirrors the library's module names: `core:common`, `core:ui`, `core:datastore`. With
    // one shared group, `:sample:core:common` depending on `:library:core:common` produced the same
    // coordinates on both sides, and Gradle rejected it as a circular dependency on itself rather
    // than reporting a name clash. Only the library is published, so this affects nothing else.
    group = if (path.startsWith(":sample")) "${publishingGroupId.get()}.sample" else publishingGroupId.get()
    version = publishingVersion.get()
}
