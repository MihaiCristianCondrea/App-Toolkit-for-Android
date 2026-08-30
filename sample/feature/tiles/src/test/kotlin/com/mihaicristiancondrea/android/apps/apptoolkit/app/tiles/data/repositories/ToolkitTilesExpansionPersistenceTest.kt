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

package com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.repositories

import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.local.preferences.ToolkitTilesPreferencesDataSource
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.local.quicksettings.QuickSettingsTilesLocalDataSource
import io.mockk.mockk
import kotlin.test.assertEquals
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class ToolkitTilesExpansionPersistenceTest {

    @Test
    fun `missing preference uses the catalogue default while saved empty set stays empty`() = runTest {
        val storedIds = MutableStateFlow<Set<String>?>(null)
        val repository = repository(storedIds)

        assertEquals(setOf("sensors"), repository.expandedCategoryIds.first())

        storedIds.value = emptySet()

        assertEquals(emptySet(), repository.expandedCategoryIds.first())
    }

    @Test
    fun `saving expansion delegates the complete set to app storage`() = runTest {
        val preferencesDataSource = FakeToolkitTilesPreferencesDataSource()
        val repository = repository(preferencesDataSource)

        repository.saveExpandedCategoryIds(setOf("utilities", "system"))

        assertEquals(setOf("utilities", "system"), preferencesDataSource.savedIds.value)
    }

    private fun repository(storedIds: MutableStateFlow<Set<String>?>): ToolkitTilesRepository {
        return repository(FakeToolkitTilesPreferencesDataSource(storedIds))
    }

    private fun repository(
        preferencesDataSource: ToolkitTilesPreferencesDataSource,
    ): ToolkitTilesRepository =
        DefaultToolkitTilesRepository(
            torchRepository = mockk<TorchRepository>(relaxed = true),
            preferencesDataSource = preferencesDataSource,
            quickSettingsDataSource = mockk<QuickSettingsTilesLocalDataSource>(relaxed = true),
        )
}

private class FakeToolkitTilesPreferencesDataSource(
    override val expandedCategoryIds: MutableStateFlow<Set<String>?> = MutableStateFlow(null),
) : ToolkitTilesPreferencesDataSource {
    val savedIds = MutableStateFlow<Set<String>?>(null)

    override suspend fun saveExpandedCategoryIds(categoryIds: Set<String>) {
        savedIds.value = categoryIds
        expandedCategoryIds.value = categoryIds
    }
}
