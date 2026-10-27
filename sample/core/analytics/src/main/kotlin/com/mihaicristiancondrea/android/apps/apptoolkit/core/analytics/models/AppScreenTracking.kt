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

package com.mihaicristiancondrea.android.apps.apptoolkit.core.analytics.models

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.models.TrackedScreen

/** Single source of truth for sample-app screen telemetry identifiers. */
object AppScreenTracking {
    object Screens {
        val MAIN = TrackedScreen(name = "Main", className = "MainScreen")
        val APPS_LIST = TrackedScreen(name = "AppsList", className = "AppsListScreen")
        val TOOLKIT_TILES = TrackedScreen(name = "ToolkitTiles", className = "ToolkitTilesScreen")
        val NUMBERS = TrackedScreen(name = "Numbers", className = "NumbersScreen")
        val COMPONENTS = TrackedScreen(name = "Components", className = "ComponentsScreen")
        val ARTICLE_DEMO = TrackedScreen(name = "ArticleDemo", className = "ArticleDemoScreen")

        val all: List<TrackedScreen> = listOf(
            MAIN,
            APPS_LIST,
            TOOLKIT_TILES,
            NUMBERS,
            COMPONENTS,
            ARTICLE_DEMO,
        )
    }
}
