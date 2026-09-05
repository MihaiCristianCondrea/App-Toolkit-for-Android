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

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.EventNote
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.Widgets
import com.mihaicristiancondrea.android.apps.apptoolkit.BuildConfig
import com.mihaicristiancondrea.android.apps.apptoolkit.core.shell.ui.navigation.NavigationItemsProvider
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.components.data.repositories.ComponentsShowcaseRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.components.ui.navigation.ComponentsRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.FirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.models.NavigationDrawerItem
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.NavigationDrawerRoutes
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.components.R as ComponentsR
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.R as ToolkitR

/**
 * Builds the drawer for this app: the toolkit's own entries plus the destinations this app adds.
 *
 * Naming the features here is deliberate. `:sample:app` is the only module that may see all of
 * them, so the drawer is assembled where the feature set is already known rather than through a
 * registration contract each feature has to opt into.
 */
class AppNavigationItemsProvider(
    private val componentsShowcaseRepository: ComponentsShowcaseRepository,
    private val firebaseController: FirebaseController,
) : NavigationItemsProvider {
    override fun items(): Flow<List<NavigationDrawerItem>> =
        componentsShowcaseRepository.isUnlocked.map { isShowcaseUnlocked ->
            buildList {
                if (BuildConfig.DEBUG || isShowcaseUnlocked) {
                    add(
                        NavigationDrawerItem(
                            title = ComponentsR.string.components_title,
                            icon = Icons.Outlined.Widgets,
                            route = ComponentsRoute.ROUTE_ID,
                        )
                    )
                }
                add(
                    NavigationDrawerItem(
                        title = ToolkitR.string.settings,
                        icon = Icons.Outlined.Settings,
                        route = NavigationDrawerRoutes.ROUTE_SETTINGS,
                    )
                )
                add(
                    NavigationDrawerItem(
                        title = ToolkitR.string.help_and_feedback,
                        icon = Icons.AutoMirrored.Outlined.HelpOutline,
                        route = NavigationDrawerRoutes.ROUTE_HELP_AND_FEEDBACK,
                    )
                )
                add(
                    NavigationDrawerItem(
                        title = ToolkitR.string.updates,
                        icon = Icons.AutoMirrored.Outlined.EventNote,
                        route = NavigationDrawerRoutes.ROUTE_UPDATES,
                    )
                )
                add(
                    NavigationDrawerItem(
                        title = ToolkitR.string.share,
                        icon = Icons.Outlined.Share,
                        route = NavigationDrawerRoutes.ROUTE_SHARE,
                    )
                )
            }
        }.onStart {
            firebaseController.logBreadcrumb(
                message = "Navigation drawer items requested",
                attributes = mapOf("source" to "AppNavigationItemsProvider"),
            )
        }
}
