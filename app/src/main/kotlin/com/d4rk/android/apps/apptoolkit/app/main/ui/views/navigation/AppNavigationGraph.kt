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

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Stable
import com.d4rk.android.apps.apptoolkit.app.apps.favorites.ui.navigation.favoriteAppsEntryBuilder
import com.d4rk.android.apps.apptoolkit.app.apps.list.ui.navigation.appsListEntryBuilder
import com.d4rk.android.apps.apptoolkit.app.components.ui.navigation.componentsEntryBuilder
import com.d4rk.android.libs.apptoolkit.app.main.ui.navigation.appToolkitNavigationEntryBuilders
import com.d4rk.android.libs.apptoolkit.core.ui.model.navigation.StableNavKey
import com.d4rk.android.libs.apptoolkit.core.ui.navigation.NavigationEntryBuilder
import com.d4rk.android.libs.apptoolkit.core.ui.window.AppWindowWidthSizeClass

/**
 * Context shared by all navigation entry builders in the app module.
 */
@Stable
data class AppNavigationEntryContext(
    val paddingValues: PaddingValues,
    val windowWidthSizeClass: AppWindowWidthSizeClass,
    val onRandomAppHandlerChanged: (StableNavKey, RandomAppHandler?) -> Unit,
) {
    fun registerRandomAppHandlerFor(route: StableNavKey): (RandomAppHandler?) -> Unit = { handler ->
        onRandomAppHandlerChanged(route, handler)
    }
}

/**
 * Default app navigation builders that can be extended with additional entries.
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
            favoriteAppsEntryBuilder(context),
            componentsEntryBuilder(context),
        )
    )
    addAll(appToolkitNavigationEntryBuilders(paddingValues = context.paddingValues))
}
