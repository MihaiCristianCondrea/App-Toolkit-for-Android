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

import app.cash.turbine.test
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.local.preferences.ToolkitTilesPreferencesDataSource
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.local.quicksettings.QuickSettingsTilesLocalDataSource
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.ToolkitTileCategoryData
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.ToolkitTileStatus
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.TorchCapabilities
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.TorchState
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.domain.utils.ToolkitTileIds
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

/**
 * The catalogue flow used to be a one-shot snapshot, so statuses read once at collection time
 * survived every later emission and a tile added while the screen was open never showed as added.
 */
class ToolkitTilesRefreshTest {

    @Test
    fun `refreshing re-reads quick settings membership`() = runTest {
        val active = MutableStateFlow<Set<String>>(emptySet())
        val quickSettings = mockk<QuickSettingsTilesLocalDataSource> {
            every { supportsAddTileRequest } returns true
            every { activeTileComponents() } answers { active.value }
            every { componentName(any()) } answers { firstArg<String>() }
        }
        val torch = mockk<TorchRepository> {
            every { state } returns MutableStateFlow(
                TorchState(TorchCapabilities(isAvailable = true)),
            )
        }
        val repository = DefaultToolkitTilesRepository(
            torchRepository = torch,
            preferencesDataSource = mockk<ToolkitTilesPreferencesDataSource>(relaxed = true),
            quickSettingsDataSource = quickSettings,
        )

        repository.tileCategories().test {
            assertEquals(ToolkitTileStatus.NotAdded, awaitItem().statusOfCoinFlip())

            active.value = setOf(ToolkitTileIds.COIN_FLIP)
            repository.refreshTileCategories()

            assertEquals(ToolkitTileStatus.Added, awaitItem().statusOfCoinFlip())
            cancelAndIgnoreRemainingEvents()
        }
    }
}

private fun List<ToolkitTileCategoryData>.statusOfCoinFlip(): ToolkitTileStatus =
    flatMap { it.tiles }.single { it.requestKey == ToolkitTileIds.COIN_FLIP }.status
