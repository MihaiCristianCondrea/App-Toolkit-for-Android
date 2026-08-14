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

package com.mihaicristiancondrea.android.apps.apptoolkit.app.main.data.repositories

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.EventNote
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.Widgets
import com.mihaicristiancondrea.android.apps.apptoolkit.core.data.local.datastore.DatastoreInterface
import com.mihaicristiancondrea.android.apps.apptoolkit.core.navigation.NavigationRoutes
import com.mihaicristiancondrea.android.apps.apptoolkit.core.ui.R
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.home.BuildConfig
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.FirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.models.NavigationDrawerItem
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.repositories.NavigationRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.NavigationDrawerRoutes
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.R as ToolkitR

class AppNavigationRepository(
    private val dataStore: DatastoreInterface,
    private val firebaseController: FirebaseController,
) : NavigationRepository {
    override fun getNavigationDrawerItems(): Flow<List<NavigationDrawerItem>> =
        dataStore.componentsShowcaseUnlocked.map { isUnlocked ->
            buildList {
                if (BuildConfig.DEBUG || isUnlocked) {
                    add(
                        NavigationDrawerItem(
                            title = R.string.components_title,
                            icon = Icons.Outlined.Widgets,
                            selectedIcon = Icons.Outlined.Widgets,
                            route = NavigationRoutes.ROUTE_COMPONENTS,
                        )
                    )
                }
                add(
                    NavigationDrawerItem(
                        title = ToolkitR.string.settings,
                        selectedIcon = Icons.Outlined.Settings,
                        icon = Icons.Outlined.Settings,
                        route = NavigationDrawerRoutes.ROUTE_SETTINGS,
                    ),
                )
                add(
                    NavigationDrawerItem(
                        title = ToolkitR.string.help_and_feedback,
                        selectedIcon = Icons.AutoMirrored.Outlined.HelpOutline,
                        icon = Icons.AutoMirrored.Outlined.HelpOutline,
                        route = NavigationDrawerRoutes.ROUTE_HELP_AND_FEEDBACK,
                    ),
                )
                add(
                    NavigationDrawerItem(
                        title = ToolkitR.string.updates,
                        selectedIcon = Icons.AutoMirrored.Outlined.EventNote,
                        icon = Icons.AutoMirrored.Outlined.EventNote,
                        route = NavigationDrawerRoutes.ROUTE_UPDATES,
                    ),
                )
                add(
                    NavigationDrawerItem(
                        title = ToolkitR.string.share,
                        selectedIcon = Icons.Outlined.Share,
                        icon = Icons.Outlined.Share,
                        route = NavigationDrawerRoutes.ROUTE_SHARE,
                    ),
                )
            }
        }.onStart {
            firebaseController.logBreadcrumb(
                message = "Navigation drawer items requested",
                attributes = mapOf("source" to "AppNavigationRepository"),
            )
        }
}
