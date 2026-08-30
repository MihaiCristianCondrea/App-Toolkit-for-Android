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

package com.mihaicristiancondrea.android.apps.apptoolkit.core.shell.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.EventNote
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.Widgets
import com.mihaicristiancondrea.android.apps.apptoolkit.core.shell.data.repositories.NavigationConfigurationRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.core.navigation.NavigationRoutes
import com.mihaicristiancondrea.android.apps.apptoolkit.core.shell.R
import com.mihaicristiancondrea.android.apps.apptoolkit.core.shell.BuildConfig
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.FirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.models.NavigationDrawerItem
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.NavigationDrawerRoutes
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.R as ToolkitR

/** Produces presentation-ready drawer items from source-neutral navigation configuration. */
class AppNavigationItemsProvider(
    private val repository: NavigationConfigurationRepository,
    private val firebaseController: FirebaseController,
) : NavigationItemsProvider {
    override fun items(): Flow<List<NavigationDrawerItem>> =
        repository.componentsShowcaseUnlocked.map { isUnlocked ->
            buildList {
                if (BuildConfig.DEBUG || isUnlocked) {
                    add(item(R.string.components_title, Icons.Outlined.Widgets, NavigationRoutes.ROUTE_COMPONENTS))
                }
                add(item(ToolkitR.string.settings, Icons.Outlined.Settings, NavigationDrawerRoutes.ROUTE_SETTINGS))
                add(item(ToolkitR.string.help_and_feedback, Icons.AutoMirrored.Outlined.HelpOutline, NavigationDrawerRoutes.ROUTE_HELP_AND_FEEDBACK))
                add(item(ToolkitR.string.updates, Icons.AutoMirrored.Outlined.EventNote, NavigationDrawerRoutes.ROUTE_UPDATES))
                add(item(ToolkitR.string.share, Icons.Outlined.Share, NavigationDrawerRoutes.ROUTE_SHARE))
            }
        }.onStart {
            firebaseController.logBreadcrumb(
                message = "Navigation drawer items requested",
                attributes = mapOf("source" to "AppNavigationItemsProvider"),
            )
        }
}

private fun item(
    title: Int,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    route: String,
) = NavigationDrawerItem(title = title, icon = icon, selectedIcon = icon, route = route)
