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

package com.d4rk.android.apps.apptoolkit.core.data.local.datastore

import com.d4rk.android.libs.apptoolkit.core.data.local.datastore.CommonDataStore
import com.d4rk.android.libs.apptoolkit.core.data.local.datastore.startupDestinationFlow
import com.d4rk.android.libs.apptoolkit.core.ui.model.navigation.StableNavKey
import kotlinx.coroutines.flow.Flow

/**
 * Adapter that exposes app-owned preferences operations backed by [CommonDataStore].
 */
class DataStore(
    private val commonDataStore: CommonDataStore,
) : DatastoreInterface {
    override val startup: Flow<Boolean> = commonDataStore.startup
    override val componentsShowcaseUnlocked: Flow<Boolean> = commonDataStore.componentsShowcaseUnlocked
    override val favoriteApps: Flow<Set<String>> = commonDataStore.favoriteApps
    override val settingsInteracted: Flow<Boolean> = commonDataStore.settingsInteracted
    override val staticPaletteId: Flow<String> = commonDataStore.staticPaletteId

    override fun <T : StableNavKey> startupDestinationFlow(
        defaultRoute: String,
        mapToKey: (String) -> T
    ): Flow<T> =
        commonDataStore.startupDestinationFlow(
            defaultRoute = defaultRoute,
            mapToKey = mapToKey
        )

    override suspend fun saveComponentsShowcaseUnlocked(isUnlocked: Boolean) {
        commonDataStore.saveComponentsShowcaseUnlocked(isUnlocked)
    }

    override suspend fun toggleFavoriteApp(packageName: String) {
        commonDataStore.toggleFavoriteApp(packageName)
    }
}
