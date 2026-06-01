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

package com.d4rk.android.apps.apptoolkit.app.tiles.ui.navigation

import com.d4rk.android.apps.apptoolkit.app.main.ui.views.navigation.AppNavigationEntryContext
import com.d4rk.android.apps.apptoolkit.app.main.utils.constants.ToolkitTilesRoute
import com.d4rk.android.apps.apptoolkit.app.tiles.ui.ToolkitTilesRoute as ToolkitTilesScreenRoute
import com.d4rk.android.libs.apptoolkit.core.ui.model.navigation.StableNavKey
import com.d4rk.android.libs.apptoolkit.core.ui.navigation.NavigationEntryBuilder

/** Registers the Toolkit Tiles destination in the app navigation graph. */
fun toolkitTilesEntryBuilder(
    context: AppNavigationEntryContext,
): NavigationEntryBuilder<StableNavKey> = {
    entry<ToolkitTilesRoute>(clazzContentKey = { route -> route }) {
        ToolkitTilesScreenRoute(
            paddingValues = context.paddingValues,
        )
    }
}
