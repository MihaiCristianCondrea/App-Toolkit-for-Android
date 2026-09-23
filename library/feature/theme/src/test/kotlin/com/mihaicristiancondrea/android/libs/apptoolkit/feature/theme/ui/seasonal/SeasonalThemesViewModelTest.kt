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

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.theme.HolidaySeason
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.theme.SeasonalThemeState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.theme.ThemePreferencesState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.colorscheme.StaticPaletteIds
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.repositories.SeasonalThemeRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.repositories.ThemePreferencesRepository
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
import kotlin.test.assertNull

class SeasonalThemesViewModelTest {

    companion object {
        @JvmField
        @RegisterExtension
        val dispatcherExtension = UnconfinedDispatcherExtension()
    }

    private val themeState = MutableStateFlow(themeWith(StaticPaletteIds.HALLOWEEN, dynamic = false))
    private val seasonal: SeasonalThemeRepository = mockk(relaxed = true) {
        every { state } returns MutableStateFlow(SeasonalThemeState(unlocked = true))
    }
    private val theme: ThemePreferencesRepository = mockk(relaxed = true) {
        every { preferencesState } returns themeState
    }

    @Test
    fun `the worn holiday follows the palette on screen`() = runTest {
        val viewModel = SeasonalThemesViewModel(seasonal = seasonal, theme = theme)
        assertEquals(HolidaySeason.HALLOWEEN, viewModel.uiState.value.data!!.wornHoliday)

        themeState.value = themeWith(StaticPaletteIds.HALLOWEEN, dynamic = true)
        assertNull(viewModel.uiState.value.data!!.wornHoliday, "wallpaper colors hide the palette")
    }

    @Test
    fun `events reach the repositories`() = runTest {
        val viewModel = SeasonalThemesViewModel(seasonal = seasonal, theme = theme)

        viewModel.onEvent(SeasonalThemesEvent.SetAllYear(true))
        viewModel.onEvent(SeasonalThemesEvent.SetSnowfall(false))
        viewModel.onEvent(SeasonalThemesEvent.WearHolidayTheme(HolidaySeason.CHRISTMAS))

        coVerify { seasonal.setSeasonalThemesAllYear(true) }
        coVerify { seasonal.setSnowfallEnabled(false) }
        coVerify { theme.selectStaticPalette(StaticPaletteIds.CHRISTMAS) }
    }

    private fun themeWith(paletteId: String, dynamic: Boolean) = ThemePreferencesState(
        themeMode = "follow_system",
        dynamicColors = dynamic,
        amoledMode = false,
        dynamicPaletteVariant = 0,
        staticPaletteId = paletteId,
    )
}
