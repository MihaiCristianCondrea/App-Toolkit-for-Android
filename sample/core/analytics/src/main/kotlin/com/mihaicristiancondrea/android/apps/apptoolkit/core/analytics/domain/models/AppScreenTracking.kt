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

package com.mihaicristiancondrea.android.apps.apptoolkit.core.analytics.domain.models

/** Stable name/class pair emitted for a sample screen. */
data class TrackedScreen(
    val name: String,
    val className: String,
)

/** Single source of truth for sample-app screen telemetry identifiers. */
object AppScreenTracking {
    object Screens {
        val MAIN = TrackedScreen(name = "Main", className = "MainScreen")
        val APPS_LIST = TrackedScreen(name = "AppsList", className = "AppsListScreen")
        val TOOLKIT_TILES = TrackedScreen(name = "ToolkitTiles", className = "ToolkitTilesScreen")
        val COMPONENTS = TrackedScreen(name = "Components", className = "ComponentsScreen")

        val all: List<TrackedScreen> = listOf(
            MAIN,
            APPS_LIST,
            TOOLKIT_TILES,
            COMPONENTS,
        )
    }
}
