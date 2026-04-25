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

import com.d4rk.android.libs.apptoolkit.core.ui.model.navigation.StableNavKey
import kotlinx.coroutines.flow.Flow

/**
 * App-owned datastore contract consumed by host app data/domain layers.
 *
 * This boundary prevents app features from depending on the shared [CommonDataStore] concrete type.
 */
interface DatastoreInterface {
    val startup: Flow<Boolean>
    val componentsShowcaseUnlocked: Flow<Boolean>
    val favoriteApps: Flow<Set<String>>
    val settingsInteracted: Flow<Boolean>
    val staticPaletteId: Flow<String>

    fun <T : StableNavKey> startupDestinationFlow(
        defaultRoute: String,
        mapToKey: (String) -> T
    ): Flow<T>

    suspend fun saveComponentsShowcaseUnlocked(isUnlocked: Boolean)
    suspend fun toggleFavoriteApp(packageName: String)
}
