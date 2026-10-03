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

import com.mihaicristiancondrea.android.apps.apptoolkit.core.analytics.domain.models.AppScreenTracking
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.repositories.CounterRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.repositories.ToolkitTilesRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.ToolsSettingsEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.ToolsSettingsUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.TelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.LoggedScreenViewModel
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.coroutines.flow.map

/**
 * The tools settings page: resets the shared count and opens or closes every category of the
 * tools list. The categories are followed for the ViewModel's lifetime.
 */
class ToolsSettingsViewModel(
    private val counterRepository: CounterRepository,
    private val tilesRepository: ToolkitTilesRepository,
    telemetryRepository: TelemetryRepository,
) : LoggedScreenViewModel<ToolsSettingsUiState, ToolsSettingsEvent>(
    initialState = ToolsSettingsUiState(),
    telemetryRepository = telemetryRepository,
    screenName = AppScreenTracking.Screens.TOOLS_SETTINGS.name,
    viewModelName = "ToolsSettingsViewModel",
) {
    init {
        tilesRepository.tileCategories()
            .map { categories -> categories.map { it.id }.toImmutableSet() }
            .collectReport(action = Actions.OBSERVE_CATEGORIES) { ids ->
                setState { copy(categoryIds = ids) }
            }
    }

    override fun handleEvent(event: ToolsSettingsEvent) {
        when (event) {
            ToolsSettingsEvent.ResetCounter -> launchReport(action = Actions.RESET_COUNTER) {
                counterRepository.reset()
            }

            ToolsSettingsEvent.ExpandAllCategories -> launchReport(action = Actions.EXPAND_ALL) {
                tilesRepository.saveExpandedCategoryIds(state.value.categoryIds)
            }

            ToolsSettingsEvent.CollapseAllCategories -> launchReport(action = Actions.COLLAPSE_ALL) {
                tilesRepository.saveExpandedCategoryIds(emptySet())
            }
        }
    }

    private object Actions {
        const val OBSERVE_CATEGORIES: String = "observeCategories"
        const val RESET_COUNTER: String = "resetCounter"
        const val EXPAND_ALL: String = "expandAllCategories"
        const val COLLAPSE_ALL: String = "collapseAllCategories"
    }
}
