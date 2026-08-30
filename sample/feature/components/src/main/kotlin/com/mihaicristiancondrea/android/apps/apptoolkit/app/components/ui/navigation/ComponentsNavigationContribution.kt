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

package com.mihaicristiancondrea.android.apps.apptoolkit.app.components.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Widgets
import com.mihaicristiancondrea.android.apps.apptoolkit.core.navigation.NavigationItemContribution
import com.mihaicristiancondrea.android.apps.apptoolkit.app.components.data.repositories.ComponentsShowcaseRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.components.BuildConfig
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.components.R
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.models.NavigationDrawerItem
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.models.StableNavKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ComponentsNavigationContribution(
    private val repository: ComponentsShowcaseRepository,
) : NavigationItemContribution {
    override fun navigationItems(): Flow<List<NavigationDrawerItem>> =
        repository.isUnlocked.map { isUnlocked ->
            if (BuildConfig.DEBUG || isUnlocked) {
                listOf(
                    NavigationDrawerItem(
                        title = R.string.components_title,
                        icon = Icons.Outlined.Widgets,
                        selectedIcon = Icons.Outlined.Widgets,
                        route = "components"
                    )
                )
            } else {
                emptyList()
            }
        }

    override fun isSelected(itemRoute: String, currentRoute: StableNavKey): Boolean =
        itemRoute == "components" && currentRoute == ComponentsRoute
}
