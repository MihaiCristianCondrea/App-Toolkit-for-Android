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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui

import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.local.preferences.FakeToolkitTilesPreferencesDataSource
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.ToolkitTileCategoryData
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.repositories.CounterRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.repositories.ToolkitTilesRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.ToolsSettingsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakeTelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.TestDispatchers
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.UnconfinedDispatcherExtension
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import kotlin.test.assertEquals

class ToolsSettingsViewModelTest {

    companion object {
        @JvmField
        @RegisterExtension
        val dispatcherExtension = UnconfinedDispatcherExtension()
    }

    private val preferences = FakeToolkitTilesPreferencesDataSource()
    private val tiles = FakeTilesRepository(categoryIds = listOf("decisions", "sensors"), initiallyExpanded = setOf("decisions"))

    private fun createViewModel() = ToolsSettingsViewModel(
        counterRepository = CounterRepository(preferences, TestDispatchers(dispatcherExtension.testDispatcher)),
        tilesRepository = tiles,
        telemetryRepository = FakeTelemetryRepository(),
    )

    private fun advance() = dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()

    @Test
    fun `the page knows every category of the tools list`() {
        val viewModel = createViewModel()
        advance()

        assertEquals(setOf("decisions", "sensors"), viewModel.state.value.categoryIds)
    }

    @Test
    fun `resetting the counter sets the shared count back to zero`() {
        preferences.counterValue.value = 7
        val viewModel = createViewModel()

        viewModel.onEvent(ToolsSettingsEvent.ResetCounter)
        advance()

        assertEquals(0, preferences.counterValue.value)
    }

    @Test
    fun `expanding opens every category`() {
        val viewModel = createViewModel()
        advance()

        viewModel.onEvent(ToolsSettingsEvent.ExpandAllCategories)
        advance()

        assertEquals(setOf("decisions", "sensors"), tiles.expanded.value)
    }

    @Test
    fun `collapsing closes every category`() {
        val viewModel = createViewModel()
        advance()

        viewModel.onEvent(ToolsSettingsEvent.CollapseAllCategories)
        advance()

        assertEquals(emptySet(), tiles.expanded.value)
    }

    private class FakeTilesRepository(categoryIds: List<String>, initiallyExpanded: Set<String>) : ToolkitTilesRepository {
        private val catalogue: ImmutableList<ToolkitTileCategoryData> =
            persistentListOf(*categoryIds.map { ToolkitTileCategoryData(id = it, tiles = persistentListOf()) }.toTypedArray())

        val expanded = MutableStateFlow(initiallyExpanded)

        override fun tileCategories(): Flow<ImmutableList<ToolkitTileCategoryData>> = flowOf(catalogue)

        override fun refreshTileCategories() = Unit

        override val expandedCategoryIds: Flow<Set<String>> = expanded

        override suspend fun saveExpandedCategoryIds(categoryIds: Set<String>) {
            expanded.value = categoryIds
        }

        override fun currentTileCategories(): ImmutableList<ToolkitTileCategoryData> = catalogue
    }
}
