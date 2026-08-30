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

import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.navigation.AppsListRoute
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.components.ui.navigation.ComponentsRoute
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.navigation.ToolkitTilesRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.models.StableNavKey
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.HelpRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.LibraryExtrasRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.NavigationDrawerRoutes
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.SettingsRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.SupportRoute

/**
 * The app-side view of the route identifiers the features own.
 *
 * Each value is re-exported from its owning feature rather than re-declared, so a feature's DI
 * qualifier, its drawer item and the persisted startup-page value can never drift apart.
 */
object NavigationRoutes {
    const val ROUTE_APPS_LIST: String = AppsListRoute.ROUTE_ID
    const val ROUTE_TOOLKIT_TILES: String = ToolkitTilesRoute.ROUTE_ID
    const val ROUTE_COMPONENTS: String = ComponentsRoute.ROUTE_ID
}

fun String.toNavKeyOrDefault(): StableNavKey =
    when (this) {
        NavigationRoutes.ROUTE_APPS_LIST -> AppsListRoute
        NavigationRoutes.ROUTE_TOOLKIT_TILES -> ToolkitTilesRoute
        NavigationRoutes.ROUTE_COMPONENTS -> ComponentsRoute
        NavigationDrawerRoutes.ROUTE_SETTINGS -> SettingsRoute
        NavigationDrawerRoutes.ROUTE_HELP_AND_FEEDBACK -> HelpRoute
        NavigationDrawerRoutes.ROUTE_UPDATES -> LibraryExtrasRoute
        NavigationDrawerRoutes.ROUTE_SUPPORT -> SupportRoute
        else -> ToolkitTilesRoute
    }
