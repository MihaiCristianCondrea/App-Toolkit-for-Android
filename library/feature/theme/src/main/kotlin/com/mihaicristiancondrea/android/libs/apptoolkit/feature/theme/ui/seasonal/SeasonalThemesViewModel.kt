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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.ui.seasonal

import androidx.lifecycle.viewModelScope
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.FirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.analytics.AnalyticsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.repositories.SeasonalThemeRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.base.ScreenViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.base.handling.ActionEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.ScreenState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.UiStateScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.setSuccess
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.ui.seasonal.contracts.SeasonalThemesEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.ui.seasonal.states.SeasonalThemesUiState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

/**
 * Owns the seasonal themes controls on the theme screen: whether they show at all, and the easter
 * egg switches.
 */
class SeasonalThemesViewModel(
    private val seasonal: SeasonalThemeRepository,
    private val firebaseController: FirebaseController,
) : ScreenViewModel<SeasonalThemesUiState, SeasonalThemesEvent, ActionEvent>(
    // No data until the stored state arrives, so the theme page never filters palettes by a guess.
    initialState = UiStateScreen(screenState = ScreenState.IsLoading(), data = null),
) {

    init {
        seasonal.state.onEach { seasonalState ->
            val state = SeasonalThemesUiState(seasonal = seasonalState)
            updateStateThreadSafe { screenState.setSuccess(data = state) }
        }.launchIn(viewModelScope)
    }

    override fun onEvent(event: SeasonalThemesEvent) {
        when (event) {
            is SeasonalThemesEvent.SetAllYear -> persist(
                event = seasonalToggleEvent(
                    preferenceKey = SeasonalThemeAnalytics.PreferenceKeys.ALL_YEAR,
                    enabled = event.enabled,
                ),
            ) {
                seasonal.setSeasonalThemesAllYear(event.enabled)
            }

            is SeasonalThemesEvent.SetSnowfall -> persist(
                event = seasonalToggleEvent(
                    preferenceKey = SeasonalThemeAnalytics.PreferenceKeys.SNOWFALL,
                    enabled = event.enabled,
                ),
            ) {
                seasonal.setSnowfallEnabled(event.enabled)
            }
        }
    }

    /** Stores a switch and reports it; [event] is logged once the change has been saved. */
    private fun persist(event: AnalyticsEvent, block: suspend () -> Unit) {
        viewModelScope.launch {
            runCatching { block() }.onSuccess { firebaseController.logEvent(event) }
        }
    }
}
