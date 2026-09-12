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
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.local.quicksettings.QuickSettingsTilesLocalDataSource
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.ToolkitTileStatus
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.TorchCapabilities
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.TorchState
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ToolkitTilesPinningTest {
    @Test
    fun `only unpinned tile services are marked not added on supported Android versions`() {
        val tiles = repository(supportsPinning = true).currentTileCategories().flatMap { it.tiles }
        assertEquals(4, tiles.count { it.status == ToolkitTileStatus.NotAdded })
        assertTrue(tiles.filter { it.requestKey == null }.all { it.status == ToolkitTileStatus.Available })
    }

    @Test
    fun `older Android versions do not advertise adding tiles`() {
        val tiles = repository(supportsPinning = false).currentTileCategories().flatMap { it.tiles }
        assertTrue(tiles.all { it.status == ToolkitTileStatus.Available })
    }

    @Test
    fun `already added service stays added on either Android version`() {
        for (supportsPinning in listOf(false, true)) {
            val tiles = repository(supportsPinning, setOf("coin_flip")).currentTileCategories().flatMap { it.tiles }
            assertEquals(ToolkitTileStatus.Added, tiles.single { it.requestKey == "coin_flip" }.status)
        }
    }

    private fun repository(supportsPinning: Boolean, active: Set<String> = emptySet()): DefaultToolkitTilesRepository {
        val quickSettings = mockk<QuickSettingsTilesLocalDataSource> {
            every { supportsAddTileRequest } returns supportsPinning
            every { activeTileComponents() } returns active
            every { componentName(any()) } answers { firstArg<String>() }
        }
        val torch = mockk<TorchRepository> {
            every { state } returns MutableStateFlow(TorchState(TorchCapabilities(isAvailable = true)))
        }
        return DefaultToolkitTilesRepository(torch, mockk<ToolkitTilesPreferencesDataSource>(relaxed = true), quickSettings)
    }
}
