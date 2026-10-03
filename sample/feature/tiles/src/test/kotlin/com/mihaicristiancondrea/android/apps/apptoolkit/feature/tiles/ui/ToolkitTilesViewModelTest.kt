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

import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.R
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.ToolkitTileCategoryData
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.repositories.ToolkitTilesRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.domain.utils.ToolkitTileIds
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.ToolkitTilesEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.ToolkitTilesFilter
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.platform.UiTextHelper
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakeTelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.TestDispatchers
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.UnconfinedDispatcherExtension
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.Loadable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ToolkitTilesViewModelTest {

    companion object {
        @JvmField
        @RegisterExtension
        val dispatcherExtension = UnconfinedDispatcherExtension()

        private const val CATEGORY_ID: String = "decisions"
    }

    private val telemetryRepository = FakeTelemetryRepository()

    private fun createViewModel(repository: ToolkitTilesRepository): ToolkitTilesViewModel = ToolkitTilesViewModel(
        toolkitTilesRepository = repository,
        dispatchers = TestDispatchers(dispatcherExtension.testDispatcher),
        telemetryRepository = telemetryRepository,
    )

    private fun advance() = dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()

    @Test
    fun `the first load shows the catalogue and the saved expanded categories`() {
        val viewModel = createViewModel(FakeToolkitTilesRepository(expanded = setOf(CATEGORY_ID)))
        advance()

        val categories = assertIs<Loadable.Ready<*>>(viewModel.state.value.categories)
        assertEquals(1, (categories.value as List<*>).size)
        assertEquals(setOf(CATEGORY_ID), viewModel.state.value.expandedCategoryIds)
    }

    @Test
    fun `a failed load shows a retryable failure with the tiles text`() {
        val viewModel = createViewModel(FakeToolkitTilesRepository(failure = IllegalStateException("catalogue")))
        advance()

        val categories = assertIs<Loadable.Failed>(viewModel.state.value.categories)
        assertEquals(
            R.string.tiles_error_failed_to_load,
            (categories.message as UiTextHelper.StringResource).resourceId,
        )
        assertTrue(categories.retryable)
        assertTrue(telemetryRepository.loggedEvents.any { it.name == "vm_op_error" })
    }

    @Test
    fun `retrying after a failed load shows the catalogue`() {
        val repository = FakeToolkitTilesRepository(failure = IllegalStateException("catalogue"))
        val viewModel = createViewModel(repository)
        advance()

        repository.failure = null
        viewModel.onEvent(ToolkitTilesEvent.Load)
        advance()

        assertIs<Loadable.Ready<*>>(viewModel.state.value.categories)
    }

    @Test
    fun `choosing a filter selects it`() {
        val viewModel = createViewModel(FakeToolkitTilesRepository())
        advance()

        viewModel.onEvent(ToolkitTilesEvent.FilterSelected(ToolkitTilesFilter.Added))

        assertEquals(ToolkitTilesFilter.Added, viewModel.state.value.selectedFilter)
    }

    @Test
    fun `toggling a category expands it and saves the set`() {
        val repository = FakeToolkitTilesRepository()
        val viewModel = createViewModel(repository)
        advance()

        viewModel.onEvent(ToolkitTilesEvent.CategoryToggled(CATEGORY_ID))
        advance()

        assertEquals(setOf(CATEGORY_ID), viewModel.state.value.expandedCategoryIds)
        assertEquals(setOf(CATEGORY_ID), repository.saved)
    }

    @Test
    fun `adding a tile asks the screen to request it until the request is launched`() {
        val viewModel = createViewModel(FakeToolkitTilesRepository())
        advance()

        viewModel.onEvent(ToolkitTilesEvent.AddTileClicked(requestKey = ToolkitTileIds.COUNTER))
        assertEquals(ToolkitTileIds.COUNTER, viewModel.state.value.pendingTileRequest)

        viewModel.onEvent(ToolkitTilesEvent.TileRequestLaunched)
        assertNull(viewModel.state.value.pendingTileRequest)
    }

    @Test
    fun `a tool without a tile shows the no tile message`() {
        val viewModel = createViewModel(FakeToolkitTilesRepository())
        advance()

        viewModel.onEvent(ToolkitTilesEvent.AddTileClicked(requestKey = null))

        val message = viewModel.messages.value.single()
        assertEquals(R.string.tiles_no_tile_message, (message.text as UiTextHelper.StringResource).resourceId)
        assertNull(viewModel.state.value.pendingTileRequest)
    }

    @Test
    fun `opening a tile's setup shows the no tile message`() {
        val viewModel = createViewModel(FakeToolkitTilesRepository())
        advance()

        viewModel.onEvent(ToolkitTilesEvent.TileSetupClicked(tileId = ToolkitTileIds.SOS))

        assertEquals(1, viewModel.messages.value.size)
    }

    @Test
    fun `a loaded ad is remembered until it unloads`() {
        val viewModel = createViewModel(FakeToolkitTilesRepository())
        advance()

        viewModel.onEvent(ToolkitTilesEvent.AdStatusChanged(adId = "ad_trailing", isLoaded = true))
        assertEquals(setOf("ad_trailing"), viewModel.state.value.loadedAdIds)

        viewModel.onEvent(ToolkitTilesEvent.AdStatusChanged(adId = "ad_trailing", isLoaded = false))
        assertTrue(viewModel.state.value.loadedAdIds.isEmpty())
    }

    private class FakeToolkitTilesRepository(
        expanded: Set<String> = emptySet(),
        var failure: Throwable? = null,
    ) : ToolkitTilesRepository {
        private val catalogue: ImmutableList<ToolkitTileCategoryData> =
            persistentListOf(ToolkitTileCategoryData(id = CATEGORY_ID, tiles = persistentListOf()))
        private val expandedIds = MutableStateFlow(expanded)

        var saved: Set<String>? = null
            private set

        override fun tileCategories(): Flow<ImmutableList<ToolkitTileCategoryData>> = flow {
            failure?.let { throw it }
            emit(catalogue)
        }

        override fun refreshTileCategories() = Unit

        override val expandedCategoryIds: Flow<Set<String>> = expandedIds

        override suspend fun saveExpandedCategoryIds(categoryIds: Set<String>) {
            saved = categoryIds
            expandedIds.value = categoryIds
        }

        override fun currentTileCategories(): ImmutableList<ToolkitTileCategoryData> = catalogue
    }
}
