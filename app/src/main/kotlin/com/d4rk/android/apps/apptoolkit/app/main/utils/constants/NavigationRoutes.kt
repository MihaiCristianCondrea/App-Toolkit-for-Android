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

package com.d4rk.android.apps.apptoolkit.app.main.utils.constants

import androidx.compose.runtime.Immutable
import com.d4rk.android.libs.apptoolkit.core.ui.model.navigation.StableNavKey
import com.d4rk.android.libs.apptoolkit.navigation.models.NavigationDestinationType
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.parcelize.Parcelize

@Immutable
sealed interface AppNavKey : StableNavKey {
    override val destinationType: NavigationDestinationType
        get() = NavigationDestinationType.ActivityLike
}

@Parcelize
data object AppsListRoute : AppNavKey {
    override val destinationType: NavigationDestinationType
        get() = NavigationDestinationType.TopLevel
}

@Parcelize
data object ToolkitTilesRoute : AppNavKey {
    override val destinationType: NavigationDestinationType
        get() = NavigationDestinationType.TopLevel
}

@Parcelize
data object ComponentsRoute : AppNavKey

object NavigationRoutes {
    const val ROUTE_APPS_LIST: String = "apps_list"
    const val ROUTE_TOOLKIT_TILES: String = "toolkit_tiles"
    const val ROUTE_COMPONENTS: String = "components"

    val topLevelRoutes: ImmutableSet<StableNavKey> =
        persistentSetOf<StableNavKey>(
            AppsListRoute,
            ToolkitTilesRoute,
        )
}

fun String.toNavKeyOrDefault(): StableNavKey =
    when (this) {
        NavigationRoutes.ROUTE_APPS_LIST -> AppsListRoute
        NavigationRoutes.ROUTE_TOOLKIT_TILES -> ToolkitTilesRoute
        NavigationRoutes.ROUTE_COMPONENTS -> ComponentsRoute
        else -> ToolkitTilesRoute
    }
