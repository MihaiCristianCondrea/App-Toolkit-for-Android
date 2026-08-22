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

package com.mihaicristiancondrea.android.libs.apptoolkit.app.onboarding.ui

import com.mihaicristiancondrea.android.libs.apptoolkit.app.onboarding.ui.contracts.OnboardingThemeEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.models.theme.ThemePreferencesState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.data.repositories.ThemePreferencesRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.UnconfinedDispatcherExtension
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import kotlin.test.assertEquals

class OnboardingThemeViewModelTest {

    companion object {
        @JvmField
        @RegisterExtension
        val dispatcherExtension = UnconfinedDispatcherExtension()
    }

    @Test
    fun `initialize observes theme and event persists amoled mode`() = runTest {
        val preferences = preferences()
        val viewModel = OnboardingThemeViewModel(preferences)

        assertEquals("system", viewModel.uiState.value.data?.themeMode)

        viewModel.onEvent(OnboardingThemeEvent.SetAmoledMode(true))

        coVerify { preferences.setAmoledMode(true) }
    }

    private fun preferences(): ThemePreferencesRepository = mockk(relaxed = true) {
        every { preferencesState } returns MutableStateFlow(
            ThemePreferencesState(
                themeMode = "system",
                dynamicColors = true,
                amoledMode = false,
                dynamicPaletteVariant = 0,
                staticPaletteId = "default",
            )
        )
    }
}
