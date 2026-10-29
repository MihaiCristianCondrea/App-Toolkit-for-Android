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
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.models.MeasureTools
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.models.PlayTools
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.NumbersCalculateTools
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.NumbersChanceTools
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.NumbersSecondaryTools
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.mappers.toUiModels
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.utils.constants.ToolkitTileIds
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class AdditionalToolsCatalogTest {
    @Test
    fun `offline tools have visual mappings and calculation tools move to NumbersScreen`() = runTest {
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
        val catalogIds = categories.filter { it.id !in setOf("chance", "measure") }.flatMap { it.tiles }.map { it.id }.toSet()

        assertFalse(ToolkitTileIds.PROTRACTOR in catalogIds)
        assertFalse(ToolkitTileIds.BILL_SPLITTER in catalogIds)
        assertFalse(ToolkitTileIds.UNIT_CONVERTER in catalogIds)
        assertFalse(ToolkitTileIds.RANDOM_NUMBER in catalogIds)
        assertFalse(ToolkitTileIds.COUNTER in catalogIds)
        assertFalse(ToolkitTileIds.DICE_ROLL in catalogIds)

        val protractor = MeasureTools.single { it.id == ToolkitTileIds.PROTRACTOR }
        assertNull(protractor.requestKey)
        assertEquals(ToolkitTileStatus.Available, protractor.status)

        val visuals = MeasureTools.single { it.id == ToolkitTileIds.PROTRACTOR }
        assertTrue(visuals.titleResId != 0 && visuals.summaryResId != 0)
        assertFalse(catalogIds.any { id -> PlayTools.any { it.id == id } })
        assertTrue(ToolkitTileIds.WATER_EJECT in catalogIds)

        assertEquals(
            listOf(ToolkitTileIds.UNIT_CONVERTER, ToolkitTileIds.BILL_SPLITTER),
            NumbersCalculateTools.map { it.id },
        )
        assertEquals(
            listOf(ToolkitTileIds.COUNTER, ToolkitTileIds.REACTION_TEST),
            NumbersChanceTools.map { it.id },
        )
        assertTrue(NumbersSecondaryTools.all { it.titleResId != 0 && it.summaryResId != 0 })
    }
}
