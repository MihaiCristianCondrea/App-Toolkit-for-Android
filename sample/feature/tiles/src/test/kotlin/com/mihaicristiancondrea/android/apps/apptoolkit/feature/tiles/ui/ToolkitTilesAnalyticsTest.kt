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

import com.mihaicristiancondrea.android.apps.apptoolkit.core.analytics.domain.contracts.AppGa4Contract
import com.mihaicristiancondrea.android.apps.apptoolkit.core.analytics.domain.contracts.AppGa4ContractValidator
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.ToolkitTileCategoryData
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.repositories.ToolkitTilesRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.domain.utils.ToolkitTileIds
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.ToolkitTilesEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.ToolkitTilesFilter
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.analytics.AnalyticsValue
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakeFirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.TestDispatchers
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.UnconfinedDispatcherExtension
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ToolkitTilesAnalyticsTest {

    companion object {
        @JvmField
        @RegisterExtension
        val dispatcherExtension = UnconfinedDispatcherExtension()

        private const val CATEGORY_ID: String = "decisions"
    }

    private val firebaseController = FakeFirebaseController()
    private val categories = MutableStateFlow<ImmutableList<ToolkitTileCategoryData>>(
        persistentListOf(ToolkitTileCategoryData(id = CATEGORY_ID, tiles = persistentListOf())),
    )
    private val repository: ToolkitTilesRepository = mockk(relaxed = true) {
        every { tileCategories() } returns categories
        every { expandedCategoryIds } returns MutableStateFlow(emptySet())
    }

    private fun viewModel() = ToolkitTilesViewModel(
        toolkitTilesRepository = repository,
        dispatchers = TestDispatchers(dispatcherExtension.testDispatcher),
        firebaseController = firebaseController,
    )

    private fun eventsNamed(name: String) =
        firebaseController.loggedEvents.filter { it.name == name }

    @Test
    fun `a tile request reports how Android answered it`() {
        val viewModel = viewModel()

        viewModel.onEvent(
            ToolkitTilesEvent.TileRequestFinished(
                requestKey = ToolkitTileIds.COUNTER,
                outcome = AppGa4Contract.TileRequestOutcome.DECLINED,
            ),
        )

        val request = eventsNamed(AppGa4Contract.EventName.QUICK_SETTINGS_TILE_REQUEST).single()
        assertEquals(
            AnalyticsValue.Str(ToolkitTileIds.COUNTER),
            request.params[AppGa4Contract.Param.TILE_ID],
        )
        assertEquals(
            AnalyticsValue.Str(AppGa4Contract.TileRequestOutcome.DECLINED),
            request.params[AppGa4Contract.Param.OUTCOME],
        )
        verify { repository.refreshTileCategories() }
    }

    @Test
    fun `tapping add tile reports nothing until the request has an outcome`() {
        val viewModel = viewModel()
        firebaseController.loggedEvents.clear()

        viewModel.onEvent(ToolkitTilesEvent.AddTileClicked(requestKey = ToolkitTileIds.COUNTER))

        // LoggedScreenViewModel's own operation telemetry (vm_op_*) is not product analytics.
        val productEvents = firebaseController.loggedEvents
            .filterNot { it.name.startsWith("vm_op_") }
        assertTrue(productEvents.isEmpty(), productEvents.toString())
    }

    @Test
    fun `opening a category is reported and closing it again is not`() {
        val viewModel = viewModel()

        viewModel.onEvent(ToolkitTilesEvent.CategoryToggled(CATEGORY_ID))
        viewModel.onEvent(ToolkitTilesEvent.CategoryToggled(CATEGORY_ID))

        assertEquals(1, eventsNamed(AppGa4Contract.EventName.SELECT_CONTENT).size)
    }

    @Test
    fun `a filter tap is one view_item_list and no select_content`() {
        val viewModel = viewModel()
        firebaseController.loggedEvents.clear()

        viewModel.onEvent(ToolkitTilesEvent.FilterSelected(ToolkitTilesFilter.entries.last()))

        assertEquals(
            listOf(AppGa4Contract.EventName.VIEW_ITEM_LIST),
            firebaseController.loggedEvents.map { it.name },
        )
    }

    @Test
    fun `every event satisfies the app GA4 contract`() {
        val viewModel = viewModel()
        viewModel.onEvent(ToolkitTilesEvent.CategoryToggled(CATEGORY_ID))
        viewModel.onEvent(ToolkitTilesEvent.FilterSelected(ToolkitTilesFilter.entries.last()))
        AppGa4Contract.TileRequestOutcome.all.forEach { outcome ->
            viewModel.onEvent(ToolkitTilesEvent.TileRequestFinished(ToolkitTileIds.SOS, outcome))
        }

        firebaseController.loggedEvents.forEach { event ->
            assertTrue(AppGa4ContractValidator.isValidEventName(event.name), event.name)
            assertEquals(
                emptySet(),
                AppGa4ContractValidator.missingRequiredParams(event.name, event.params.keys),
                event.name,
            )
            assertEquals(emptySet(), AppGa4ContractValidator.forbiddenParams(event.params.keys))
        }
    }
}
