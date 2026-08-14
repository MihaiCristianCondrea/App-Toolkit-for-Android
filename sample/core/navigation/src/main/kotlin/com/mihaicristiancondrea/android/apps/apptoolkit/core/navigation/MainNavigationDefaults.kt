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

package com.mihaicristiancondrea.android.apps.apptoolkit.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Apps
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.rounded.Apps
import androidx.compose.material.icons.rounded.GridView
import com.mihaicristiancondrea.android.apps.apptoolkit.core.ui.R
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.navigation.BottomBarItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.navigation.StableNavKey
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

// Public rather than internal now that the shell consuming it lives in another module.
object MainNavigationDefaults {
    val fabSupportedRoutes: Set<StableNavKey> = setOf(
        AppsListRoute
    )

    val bottomBarItems: ImmutableList<BottomBarItem<StableNavKey>> = persistentListOf(
        BottomBarItem(
            route = ToolkitTilesRoute,
            icon = Icons.Outlined.GridView,
            selectedIcon = Icons.Rounded.GridView,
            title = R.string.tiles_title
        ),
        BottomBarItem(
            route = AppsListRoute,
            icon = Icons.Outlined.Apps,
            selectedIcon = Icons.Rounded.Apps,
            title = R.string.apps_tools_title
        )
    )
}
