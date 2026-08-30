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

package com.mihaicristiancondrea.android.apps.apptoolkit.core.navigation

import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.models.NavigationDrawerItem
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.models.StableNavKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

/**
 * A contract for feature modules to contribute their navigation items to the application shell.
 */
interface NavigationItemContribution {
    /**
     * Emits the list of navigation items this feature provides.
     * Some items might be conditional (e.g., hidden until unlocked).
     */
    fun navigationItems(): Flow<List<NavigationDrawerItem>> = flowOf(emptyList())

    /**
     * Returns true if the given [itemRoute] (from [NavigationDrawerItem.route])
     * corresponds to the [currentRoute] being displayed.
     */
    fun isSelected(itemRoute: String, currentRoute: StableNavKey): Boolean = false
}
