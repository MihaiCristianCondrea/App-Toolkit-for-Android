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

package com.mihaicristiancondrea.android.libs.apptoolkit.app.display.ui

import com.mihaicristiancondrea.android.libs.apptoolkit.app.display.ui.contracts.DisplaySettingsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.core.data.local.datastore.interfaces.DisplayPreferencesDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.data.local.datastore.interfaces.ThemePreferencesDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.UnconfinedDispatcherExtension
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import kotlin.test.assertEquals

class DisplaySettingsViewModelTest {

    companion object {
        @JvmField
        @RegisterExtension
        val dispatcherExtension = UnconfinedDispatcherExtension()
    }

    @Test
    fun `initialize event observes preferences`() = runTest {
        val display = displayPreferences()
        val theme = themePreferences()

        val viewModel = DisplaySettingsViewModel(display, theme)

        assertEquals("dark", viewModel.uiState.value.data?.themeMode)
        assertEquals(false, viewModel.uiState.value.data?.bouncyButtons)
        assertEquals("ro", viewModel.uiState.value.data?.language)
    }

    @Test
    fun `events persist through narrow preference sources`() = runTest {
        val display = displayPreferences()
        val theme = themePreferences()
        val viewModel = DisplaySettingsViewModel(display, theme)

        viewModel.onEvent(DisplaySettingsEvent.ThemeModeChanged("light"))
        viewModel.onEvent(DisplaySettingsEvent.BouncyButtonsChanged(true))

        coVerify { theme.saveThemeMode("light") }
        coVerify { display.saveBouncyButtons(true) }
    }

    private fun displayPreferences(): DisplayPreferencesDataSource = mockk(relaxed = true) {
        every { showBottomBarLabels } returns MutableStateFlow(true)
        every { bouncyButtons } returns MutableStateFlow(false)
        every { language } returns MutableStateFlow("ro")
        every { startupPage(any()) } returns MutableStateFlow("home")
    }

    private fun themePreferences(): ThemePreferencesDataSource = mockk(relaxed = true) {
        every { themeMode } returns MutableStateFlow("dark")
        every { dynamicColors } returns MutableStateFlow(true)
        every { amoledMode } returns MutableStateFlow(false)
        every { dynamicPaletteVariant } returns MutableStateFlow(0)
        every { staticPaletteId } returns MutableStateFlow("default")
    }
}
