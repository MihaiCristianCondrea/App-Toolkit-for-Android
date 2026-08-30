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

package com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.ui.navigation

import com.mihaicristiancondrea.android.apps.apptoolkit.core.navigation.AppNavKey
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.models.NavigationDestinationType
import kotlinx.parcelize.Parcelize

@Parcelize
data object ToolkitTilesRoute : AppNavKey {
    override val destinationType: NavigationDestinationType
        get() = NavigationDestinationType.TopLevel
}
