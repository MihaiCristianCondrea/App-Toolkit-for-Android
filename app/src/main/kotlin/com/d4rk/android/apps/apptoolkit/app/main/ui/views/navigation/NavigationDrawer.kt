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

package com.d4rk.android.apps.apptoolkit.app.main.ui.views.navigation

import com.d4rk.android.apps.apptoolkit.app.main.utils.constants.AppsListRoute
import com.d4rk.android.apps.apptoolkit.app.main.utils.constants.ComponentsRoute
import com.d4rk.android.apps.apptoolkit.app.main.utils.constants.FavoriteAppsRoute
import com.d4rk.android.apps.apptoolkit.app.main.utils.constants.NavigationRoutes
import com.d4rk.android.libs.apptoolkit.app.main.utils.constants.GeneralSettingsRoute
import com.d4rk.android.libs.apptoolkit.app.main.utils.constants.HelpRoute
import com.d4rk.android.libs.apptoolkit.app.main.utils.constants.NavigationDrawerRoutes
import com.d4rk.android.libs.apptoolkit.app.main.utils.constants.SettingsRoute
import com.d4rk.android.libs.apptoolkit.app.main.utils.constants.SupportRoute
import com.d4rk.android.libs.apptoolkit.core.domain.model.analytics.AnalyticsEvent
import com.d4rk.android.libs.apptoolkit.core.domain.model.analytics.AnalyticsValue
import com.d4rk.android.libs.apptoolkit.core.ui.model.navigation.StableNavKey
import com.d4rk.android.libs.apptoolkit.core.utils.constants.analytics.SettingsAnalytics

fun isDrawerItemSelected(
    itemRoute: String,
    currentRoute: StableNavKey,
): Boolean =
    when (itemRoute) {
        NavigationRoutes.ROUTE_APPS_LIST -> currentRoute == AppsListRoute
        NavigationRoutes.ROUTE_FAVORITE_APPS -> currentRoute == FavoriteAppsRoute
        NavigationRoutes.ROUTE_COMPONENTS -> currentRoute == ComponentsRoute
        NavigationDrawerRoutes.ROUTE_SETTINGS -> currentRoute is SettingsRoute || currentRoute is GeneralSettingsRoute
        NavigationDrawerRoutes.ROUTE_HELP_AND_FEEDBACK -> currentRoute is HelpRoute
        NavigationDrawerRoutes.ROUTE_SUPPORT -> currentRoute is SupportRoute
        else -> false
    }

fun drawerItemClickEvent(route: String): AnalyticsEvent =
    AnalyticsEvent(
        name = SettingsAnalytics.Events.ACTION,
        params = mapOf(
            SettingsAnalytics.Params.SCREEN to AnalyticsValue.Str("MainNavigationDrawer"),
            SettingsAnalytics.Params.ACTION_NAME to AnalyticsValue.Str("navigation_item_click"),
            SettingsAnalytics.Params.NAVIGATION_ROUTE to AnalyticsValue.Str(route),
        ),
    )
