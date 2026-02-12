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

package com.d4rk.android.apps.apptoolkit.app.main.utils.defaults

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Apps
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material.icons.rounded.Apps
import androidx.compose.material.icons.rounded.Star
import com.d4rk.android.apps.apptoolkit.R
import com.d4rk.android.apps.apptoolkit.app.main.utils.constants.AppNavKey
import com.d4rk.android.apps.apptoolkit.app.main.utils.constants.AppsListRoute
import com.d4rk.android.apps.apptoolkit.app.main.utils.constants.FavoriteAppsRoute
import com.d4rk.android.libs.apptoolkit.app.main.domain.model.BottomBarItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

internal object MainNavigationDefaults {
    val fabSupportedRoutes: Set<AppNavKey> = setOf(
        AppsListRoute,
        FavoriteAppsRoute
    )

    val bottomBarItems: ImmutableList<BottomBarItem<AppNavKey>> = persistentListOf(
        BottomBarItem(
            route = AppsListRoute,
            icon = Icons.Outlined.Apps,
            selectedIcon = Icons.Rounded.Apps,
            title = R.string.all_apps
        ),
        BottomBarItem(
            route = FavoriteAppsRoute,
            icon = Icons.Outlined.StarOutline,
            selectedIcon = Icons.Rounded.Star,
            title = R.string.favorite_apps
        )
    )
}