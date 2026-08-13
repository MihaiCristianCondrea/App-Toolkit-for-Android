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
    id("com.mihaicristiancondrea.android.apptoolkit.sample-module")
}

android {
    namespace = "com.mihaicristiancondrea.android.apps.apptoolkit.core.ui"
}

dependencies {
    api(project(":sample:core:common"))
    api(project(":library:core:ui"))
    api(project(":library:core:designsystem"))
    api(project(":library:core:network"))

    // This module owns themes.xml, whose AppTheme.Starting style sets
    // windowSplashScreenBackground / windowSplashScreenAnimatedIcon / postSplashScreenTheme. Those
    // attributes are declared by core-splashscreen, which previously reached the theme transitively
    // through :library:apptoolkit; the theme moved here during modularization, the dependency did
    // not, and the release resource link could not resolve them.
    api(libs.androidx.core.splashscreen)
}
