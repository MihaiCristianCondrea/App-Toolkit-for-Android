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

package com.d4rk.android.apps.apptoolkit.app.main.data.repository

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.EventNote
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.Widgets
import com.d4rk.android.apps.apptoolkit.BuildConfig
import com.d4rk.android.apps.apptoolkit.R
import com.d4rk.android.apps.apptoolkit.app.main.utils.constants.NavigationRoutes
import com.d4rk.android.apps.apptoolkit.core.data.local.DataStore
import com.d4rk.android.libs.apptoolkit.app.main.domain.repository.NavigationRepository
import com.d4rk.android.libs.apptoolkit.app.main.utils.constants.NavigationDrawerRoutes
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import com.d4rk.android.libs.apptoolkit.core.ui.model.navigation.NavigationDrawerItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import com.d4rk.android.libs.apptoolkit.R as ToolkitR

class MainNavigationRepositoryImpl(
    private val dataStore: DataStore,
    private val firebaseController: FirebaseController,
) : NavigationRepository {
    override fun getNavigationDrawerItems(): Flow<List<NavigationDrawerItem>> =
        dataStore.componentsShowcaseUnlocked.map { isUnlocked ->
            buildList {
                if (BuildConfig.DEBUG || isUnlocked) {
                    add(
                        NavigationDrawerItem(
                            title = R.string.components_title,
                            selectedIcon = Icons.Outlined.Widgets,
                            route = NavigationRoutes.ROUTE_COMPONENTS,
                        )
                    )
                }
                add(
                    NavigationDrawerItem(
                        title = ToolkitR.string.settings,
                        selectedIcon = Icons.Outlined.Settings,
                        route = NavigationDrawerRoutes.ROUTE_SETTINGS,
                    ),
                )
                add(
                    NavigationDrawerItem(
                        title = ToolkitR.string.help_and_feedback,
                        selectedIcon = Icons.AutoMirrored.Outlined.HelpOutline,
                        route = NavigationDrawerRoutes.ROUTE_HELP_AND_FEEDBACK,
                    ),
                )
                add(
                    NavigationDrawerItem(
                        title = ToolkitR.string.updates,
                        selectedIcon = Icons.AutoMirrored.Outlined.EventNote,
                        route = NavigationDrawerRoutes.ROUTE_UPDATES,
                    ),
                )
                add(
                    NavigationDrawerItem(
                        title = ToolkitR.string.share,
                        selectedIcon = Icons.Outlined.Share,
                        route = NavigationDrawerRoutes.ROUTE_SHARE,
                    ),
                )
            }
        }.onStart {
            firebaseController.logBreadcrumb(
                message = "Navigation drawer items requested",
                attributes = mapOf("source" to "MainNavigationRepositoryImpl"),
            )
        }
}
