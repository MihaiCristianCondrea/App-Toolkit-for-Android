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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.data.repositories

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.datastore.DataStoreNamesConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.data.local.datastore.interfaces.ThemePreferencesDataSource
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class DefaultThemePreferencesRepositoryTest {

    /**
     * A true-black surface means nothing in the light theme, and both the settings screen and the
     * onboarding page offer this choice: the rule belongs here, once, rather than in each of them.
     */
    @Test
    fun `switching to the light theme turns amoled off`() = runTest {
        val preferences = preferences(amoledMode = true)

        DefaultThemePreferencesRepository(preferences)
            .selectThemeMode(DataStoreNamesConstants.THEME_MODE_LIGHT)

        coVerifyOrder {
            preferences.saveThemeMode(DataStoreNamesConstants.THEME_MODE_LIGHT)
            preferences.saveAmoledMode(false)
        }
    }

    @Test
    fun `switching to the light theme leaves amoled alone when it is already off`() = runTest {
        val preferences = preferences(amoledMode = false)

        DefaultThemePreferencesRepository(preferences)
            .selectThemeMode(DataStoreNamesConstants.THEME_MODE_LIGHT)

        coVerify(exactly = 0) { preferences.saveAmoledMode(any()) }
    }

    @Test
    fun `switching to the dark theme keeps amoled on`() = runTest {
        val preferences = preferences(amoledMode = true)

        DefaultThemePreferencesRepository(preferences)
            .selectThemeMode(DataStoreNamesConstants.THEME_MODE_DARK)

        coVerify(exactly = 0) { preferences.saveAmoledMode(any()) }
    }

    @Test
    fun `picking a static palette turns dynamic colors off`() = runTest {
        val preferences = preferences()

        DefaultThemePreferencesRepository(preferences).selectStaticPalette("rose")

        coVerifyOrder {
            preferences.saveDynamicColors(false)
            preferences.saveStaticPaletteId("rose")
        }
    }

    @Test
    fun `picking a dynamic palette turns dynamic colors on`() = runTest {
        val preferences = preferences()

        DefaultThemePreferencesRepository(preferences).selectDynamicPalette(variant = 3)

        coVerifyOrder {
            preferences.saveDynamicColors(true)
            preferences.saveDynamicPaletteVariant(3)
        }
    }

    private fun preferences(amoledMode: Boolean = false): ThemePreferencesDataSource =
        mockk(relaxed = true) {
            every { this@mockk.amoledMode } returns MutableStateFlow(amoledMode)
            every { themeMode } returns MutableStateFlow(DataStoreNamesConstants.THEME_MODE_FOLLOW_SYSTEM)
            every { dynamicColors } returns MutableStateFlow(true)
            every { dynamicPaletteVariant } returns MutableStateFlow(0)
            every { staticPaletteId } returns MutableStateFlow("default")
        }
}
