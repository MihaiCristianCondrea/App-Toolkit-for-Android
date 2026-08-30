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

package com.mihaicristiancondrea.android.apps.apptoolkit.di

import com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.list.ui.navigation.AppsListRoute
import com.mihaicristiancondrea.android.apps.apptoolkit.app.navigation.AppNavigationItemsProvider
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.ui.navigation.ToolkitTilesRoute
import com.mihaicristiancondrea.android.apps.apptoolkit.core.shell.ui.navigation.NavigationItemsProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.di.AppToolkitDiConstants
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.R as AppsR
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.R as TilesR

/**
 * Main application module for `:sample:app`.
 *
 * Holds the wiring that needs to see more than one feature: the drawer, the startup-screen choices
 * and the toolkit extension points this app implements. Features stay unaware of each other.
 */
val appModule: Module = module {
    single<NavigationItemsProvider> {
        AppNavigationItemsProvider(
            componentsShowcaseRepository = get(),
            firebaseController = get(),
        )
    }

    // The startup picker offers the app's top-level destinations, in bottom-bar order. Entries and
    // values are parallel lists: index N of one describes index N of the other.
    single<List<String>>(qualifier = named(name = AppToolkitDiConstants.STARTUP_ENTRIES)) {
        listOf(
            androidContext().getString(TilesR.string.tiles_title),
            androidContext().getString(AppsR.string.apps_tools_title),
        )
    }

    single<List<String>>(qualifier = named(name = AppToolkitDiConstants.STARTUP_VALUES)) {
        listOf(ToolkitTilesRoute.ROUTE_ID, AppsListRoute.ROUTE_ID)
    }
}
