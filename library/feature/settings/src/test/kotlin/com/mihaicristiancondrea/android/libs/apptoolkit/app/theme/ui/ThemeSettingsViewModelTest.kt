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

package com.mihaicristiancondrea.android.libs.apptoolkit.app.theme.ui

import com.mihaicristiancondrea.android.libs.apptoolkit.app.theme.ui.contracts.ThemeSettingsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.core.data.local.datastore.interfaces.ThemePreferencesDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.UnconfinedDispatcherExtension
import io.mockk.coVerifyOrder
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import kotlin.test.assertEquals

class ThemeSettingsViewModelTest {

    companion object {
        @JvmField
        @RegisterExtension
        val dispatcherExtension = UnconfinedDispatcherExtension()
    }

    @Test
    fun `initialize event observes theme preferences`() = runTest {
        val preferences = preferences()

        val viewModel = ThemeSettingsViewModel(preferences)

        assertEquals("dark", viewModel.uiState.value.data?.themeMode)
        assertEquals(2, viewModel.uiState.value.data?.dynamicPaletteVariant)
    }

    @Test
    fun `selecting static palette disables dynamic colors and saves id`() = runTest {
        val preferences = preferences()
        val viewModel = ThemeSettingsViewModel(preferences)

        viewModel.onEvent(ThemeSettingsEvent.SelectStaticPalette("rose"))

        coVerifyOrder {
            preferences.saveDynamicColors(false)
            preferences.saveStaticPaletteId("rose")
        }
    }

    private fun preferences(): ThemePreferencesDataSource = mockk(relaxed = true) {
        every { themeMode } returns MutableStateFlow("dark")
        every { dynamicColors } returns MutableStateFlow(true)
        every { amoledMode } returns MutableStateFlow(false)
        every { dynamicPaletteVariant } returns MutableStateFlow(2)
        every { staticPaletteId } returns MutableStateFlow("default")
    }
}
