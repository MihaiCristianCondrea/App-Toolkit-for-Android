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

pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        gradlePluginPortal()
        mavenCentral()
        maven {
            setUrl("https://jitpack.io")
        }
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

@Suppress("UnstableApiUsage") dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            setUrl("https://jitpack.io")
        }
    }
}

rootProject.name = "App-Toolkit-for-Android"

include(":sample:app")
include(":sample:core:analytics")
include(":sample:core:apptoolkit")
include(":sample:core:common")
include(":sample:core:datastore")
include(":sample:core:navigation")
include(":sample:core:ui")
include(":sample:core:shell")
include(":sample:integration:ads")
include(":sample:feature:apps")
include(":sample:feature:components")
include(":sample:feature:onboarding")
include(":sample:feature:settings")
include(":sample:feature:tiles")
include(":sample:widget")
include(":library:apptoolkit")
include(":library:core:common")
include(":library:core:datastore")
include(":library:core:testing")
include(":library:core:network")
include(":library:core:ui")
include(":library:core:designsystem")
include(":library:navigation")
include(":library:feature:about")
include(":library:feature:help")
include(":library:feature:issuereporter")
include(":library:feature:onboarding")
include(":library:feature:permissions")
include(":library:feature:settings")
include(":library:feature:support")
include(":library:integration:ads")
include(":library:integration:billing")
include(":library:integration:consent")
include(":library:integration:firebase")
include(":library:integration:review")
include(":library:integration:update")
