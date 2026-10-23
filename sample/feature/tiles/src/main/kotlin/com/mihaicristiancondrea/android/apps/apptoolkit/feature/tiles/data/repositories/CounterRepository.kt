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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.repositories

import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.local.preferences.ToolkitTilesPreferencesDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.coroutines.dispatchers.DispatcherProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.storageCall
import kotlinx.coroutines.async
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.SupervisorJob

/**
 * The one running count shared by the in-app Counter tool and its Quick Settings tile.
 *
 * The count is persisted because System UI unbinds tile services freely and the process may die
 * between taps. Writes run in this repository's own scope, so a tap is kept even when the tile
 * service that received it is destroyed straight afterwards. Active callers await completion and
 * receive storage failures; cancelling their wait does not cancel an accepted write.
 */
class CounterRepository(
    private val preferencesDataSource: ToolkitTilesPreferencesDataSource,
    dispatchers: DispatcherProvider,
) {
    private val scope = CoroutineScope(SupervisorJob() + dispatchers.default)

    val count: Flow<Int> = preferencesDataSource.counterValue

    suspend fun increment() {
        scope.async { storageCall { preferencesDataSource.incrementCounter() } }.await()
    }

    suspend fun reset() {
        scope.async { storageCall { preferencesDataSource.resetCounter() } }.await()
    }
}
