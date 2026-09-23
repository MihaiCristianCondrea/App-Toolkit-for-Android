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

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.theme.SeasonalThemeState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.repositories.SeasonalThemeRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.UnconfinedDispatcherExtension
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.ui.seasonal.contracts.SeasonalThemesEvent
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
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

    @Test
    fun `the state follows the stored seasonal themes`() = runTest {
        val viewModel = SeasonalThemesViewModel(seasonal = seasonal)
        assertEquals(SeasonalThemeState(unlocked = true), viewModel.uiState.value.data!!.seasonal)

        seasonalState.value = SeasonalThemeState(unlocked = true, allYear = true)
        assertEquals(true, viewModel.uiState.value.data!!.seasonal.allYear)
    }

    @Test
    fun `events reach the repository`() = runTest {
        val viewModel = SeasonalThemesViewModel(seasonal = seasonal)

        viewModel.onEvent(SeasonalThemesEvent.SetAllYear(true))
        viewModel.onEvent(SeasonalThemesEvent.SetSnowfall(false))

        coVerify { seasonal.setSeasonalThemesAllYear(true) }
        coVerify { seasonal.setSnowfallEnabled(false) }
    }
}
