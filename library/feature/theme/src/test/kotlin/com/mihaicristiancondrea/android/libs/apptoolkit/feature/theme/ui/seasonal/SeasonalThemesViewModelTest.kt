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

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.analytics.AnalyticsValue
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.theme.SeasonalThemeState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.analytics.SettingsAnalytics
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.repositories.SeasonalThemeRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakeFirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.UnconfinedDispatcherExtension
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.ui.seasonal.contracts.SeasonalThemesEvent
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlin.test.assertTrue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import kotlin.test.assertEquals

class SeasonalThemesViewModelTest {

    companion object {
        @JvmField
        @RegisterExtension
        val dispatcherExtension = UnconfinedDispatcherExtension()
    }

    private val seasonalState = MutableStateFlow(SeasonalThemeState(unlocked = true))
    private val seasonal: SeasonalThemeRepository = mockk(relaxed = true) {
        every { state } returns seasonalState
    }
    private val firebaseController = FakeFirebaseController()

    private fun viewModel() =
        SeasonalThemesViewModel(seasonal = seasonal, firebaseController = firebaseController)

    @Test
    fun `the state follows the stored seasonal themes`() = runTest {
        val viewModel = viewModel()
        assertEquals(SeasonalThemeState(unlocked = true), viewModel.uiState.value.data!!.seasonal)

        seasonalState.value = SeasonalThemeState(unlocked = true, allYear = true)
        assertEquals(true, viewModel.uiState.value.data!!.seasonal.allYear)
    }

    @Test
    fun `events reach the repository`() = runTest {
        val viewModel = viewModel()

        viewModel.onEvent(SeasonalThemesEvent.SetAllYear(true))
        viewModel.onEvent(SeasonalThemesEvent.SetSnowfall(false))

        coVerify { seasonal.setSeasonalThemesAllYear(true) }
        coVerify { seasonal.setSnowfallEnabled(false) }
    }

    @Test
    fun `each switch is reported as a settings toggle with its new value`() = runTest {
        val viewModel = viewModel()

        viewModel.onEvent(SeasonalThemesEvent.SetAllYear(true))
        viewModel.onEvent(SeasonalThemesEvent.SetSnowfall(false))

        val toggles = firebaseController.loggedEvents
            .filter { it.name == SettingsAnalytics.Events.PREFERENCE_TOGGLE }
            .map { event ->
                event.params[SettingsAnalytics.Params.PREFERENCE_KEY] to
                    event.params[SettingsAnalytics.Params.ENABLED]
            }
        assertEquals(
            listOf(
                AnalyticsValue.Str("seasonal_themes_all_year") to AnalyticsValue.Str("true"),
                AnalyticsValue.Str("seasonal_themes_snowfall") to AnalyticsValue.Str("false"),
            ),
            toggles,
        )
    }

    @Test
    fun `a switch that could not be saved is not reported`() = runTest {
        coEvery { seasonal.setSnowfallEnabled(any()) } throws IllegalStateException("disk full")
        val viewModel = viewModel()

        viewModel.onEvent(SeasonalThemesEvent.SetSnowfall(true))

        assertTrue(firebaseController.loggedEvents.isEmpty())
    }
}
