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

import com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.list.ui.navigation.appsListEntryBuilder
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.ui.navigation.toolkitTilesEntryBuilder
import com.mihaicristiancondrea.android.apps.apptoolkit.core.navigation.AppNavigationEntryContext
import com.mihaicristiancondrea.android.libs.apptoolkit.app.main.ui.navigation.appToolkitNavigationEntryBuilders
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.navigation.NavigationEntryBuilder
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.models.StableNavKey

/**
 * Assembles the app's navigation entries from every feature module.
 *
 * This is the one place that knows the full feature set, which is why it stays in `:sample:app`
 * rather than in the shell or in a feature: the shell renders whatever entries it is handed, and
 * each feature contributes only its own.
 */
fun appNavigationEntryBuilders(
    context: AppNavigationEntryContext,
    additionalEntryBuilders: List<NavigationEntryBuilder<StableNavKey>> = emptyList(),
): List<NavigationEntryBuilder<StableNavKey>> = buildList {
    addAll(defaultAppNavigationEntryBuilders(context))
    addAll(additionalEntryBuilders)
}

private fun defaultAppNavigationEntryBuilders(
    context: AppNavigationEntryContext
): List<NavigationEntryBuilder<StableNavKey>> = buildList {
    addAll(
        listOf(
            appsListEntryBuilder(context),
            toolkitTilesEntryBuilder(context),
        )
    )
    addAll(appToolkitNavigationEntryBuilders(paddingValues = context.paddingValues))
}
