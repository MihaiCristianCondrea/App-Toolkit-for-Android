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

import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.local.preferences.FakeToolkitTilesPreferencesDataSource
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.local.quicksettings.QuickSettingsTilesLocalDataSource
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.ToolkitTileStatus
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.mappers.toUiModels
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.utils.constants.ToolkitTileIds
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class AdditionalToolsCatalogTest {
    @Test
    fun `offline tools have visual mappings and do not request Quick Settings membership`() = runTest {
        val source = object : QuickSettingsTilesLocalDataSource {
            override val supportsAddTileRequest = true
            override suspend fun activeTileComponents() = emptySet<String>()
            override fun componentName(requestKey: String) = requestKey
            override fun recordTileAdded(componentName: String, added: Boolean) = Unit
        }
        val repository = DefaultToolkitTilesRepository(
            torchRepository = FakeTorchRepository(),
            preferencesDataSource = FakeToolkitTilesPreferencesDataSource(),
            quickSettingsDataSource = source,
        )
        val categories = repository.currentTileCategories()
        val expected = setOf(ToolkitTileIds.PROTRACTOR, ToolkitTileIds.BILL_SPLITTER, ToolkitTileIds.RANDOM_NUMBER)
        val tools = categories.flatMap { it.tiles }.filter { it.id in expected }
        assertEquals(expected, tools.map { it.id }.toSet())
        tools.forEach {
            assertNull(it.requestKey)
            assertEquals(ToolkitTileStatus.Available, it.status)
        }
        val visuals = categories.toUiModels().flatMap { it.tiles }.filter { it.id in expected }
        assertEquals(3, visuals.size)
        assertTrue(visuals.all { it.titleResId != 0 && it.summaryResId != 0 })
        assertEquals("sensors", categories.single { category -> category.tiles.any { it.id == ToolkitTileIds.PROTRACTOR } }.id)
        assertEquals("utilities", categories.single { category -> category.tiles.any { it.id == ToolkitTileIds.BILL_SPLITTER } }.id)
    }
}
