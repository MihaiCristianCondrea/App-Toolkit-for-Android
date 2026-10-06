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

package com.mihaicristiancondrea.android.apps.apptoolkit.app.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** The Tiles tab. */
@Serializable
@SerialName("com.mihaicristiancondrea.android.apps.apptoolkit.app.navigation.ToolkitTilesRoute")
data object ToolkitTilesRoute : NavKey {
    /** Persisted identifier for this destination, shared by DI qualifiers and the startup-page setting. */
    const val ROUTE_ID: String = "toolkit_tiles"
}

/** The Apps tab. */
@Serializable
@SerialName("com.mihaicristiancondrea.android.apps.apptoolkit.app.navigation.AppsListRoute")
data object AppsListRoute : NavKey {
    /** Persisted identifier for this destination, shared by DI qualifiers and the startup-page setting. */
    const val ROUTE_ID: String = "apps_list"
}

/** The components showcase. */
@Serializable
@SerialName("com.mihaicristiancondrea.android.apps.apptoolkit.app.navigation.ComponentsRoute")
data object ComponentsRoute : NavKey {
    /** Persisted identifier for this destination, shared by DI qualifiers and the drawer entry. */
    const val ROUTE_ID: String = "components"
}

/** The article app bar demo, with the sample publisher's mark in the bar when [branded]. */
@Serializable
@SerialName("com.mihaicristiancondrea.android.apps.apptoolkit.app.navigation.ArticleDemoRoute")
data class ArticleDemoRoute(val branded: Boolean) : NavKey
