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

import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.local.preferences.DefaultToolkitTilesPreferencesDataSource
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.local.quicksettings.QuickSettingsTilesLocalDataSource
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.ToolkitTileStatus
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakePreferencesDataStore
import kotlinx.coroutines.test.runTest
import kotlin.test.assertEquals
import org.junit.jupiter.api.Test

class ToolkitTilesMembershipTest {
    @Test
    fun `confirmed membership updates flow through the repository`() = runTest {
        val components = mutableSetOf<String>()
        val source = object : QuickSettingsTilesLocalDataSource {
            override val supportsAddTileRequest = true
            override suspend fun activeTileComponents() = components.toSet()
            override fun componentName(requestKey: String) = requestKey
            override fun recordTileAdded(componentName: String, added: Boolean) {
                if (added) components.add(componentName) else components.remove(componentName)
            }
        }
        val repository = DefaultToolkitTilesRepository(
            FakeTorchRepository(),
            DefaultToolkitTilesPreferencesDataSource(FakePreferencesDataStore()),
            source,
        )
        repository.recordTileMembership("coin_flip", true)
        val added = repository.currentTileCategories().flatMap { it.tiles }.single { it.requestKey == "coin_flip" }
        assertEquals(ToolkitTileStatus.Added, added.status)
        repository.recordTileMembership("coin_flip", false)
        val removed = repository.currentTileCategories().flatMap { it.tiles }.single { it.requestKey == "coin_flip" }
        assertEquals(ToolkitTileStatus.NotAdded, removed.status)
    }
}
