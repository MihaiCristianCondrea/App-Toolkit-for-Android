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
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.theme.HolidaySeason
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.theme.SeasonalThemeState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.theme.ThemePreferencesState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.colorscheme.StaticPaletteIds
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.repositories.SeasonalThemeRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.repositories.ThemePreferencesRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakeFirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.UnconfinedDispatcherExtension
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.ui.seasonal.contracts.SeasonalThemeOverlayEvent
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class SeasonalThemeOverlayViewModelTest {

    companion object {
        @JvmField
        @RegisterExtension
        val dispatcherExtension = UnconfinedDispatcherExtension()
    }

    private val christmas = LocalDate.parse("2026-12-25")
    private val july = LocalDate.parse("2026-07-14")

    private val seasonalState = MutableStateFlow(SeasonalThemeState())
    private val themeState = MutableStateFlow(themeWith(StaticPaletteIds.CHRISTMAS, dynamic = false))

    private val seasonal: SeasonalThemeRepository = mockk(relaxed = true) {
        every { state } returns seasonalState
    }
    private val theme: ThemePreferencesRepository = mockk(relaxed = true) {
        every { preferencesState } returns themeState
    }
    private val firebaseController = FakeFirebaseController()

    @AfterEach
    fun tearDown() {
        HolidayGreetingPresence.release()
    }

    @Test
    fun `snow falls with the christmas palette during christmas`() = runTest {
        val viewModel = viewModel(today = christmas)

        assertTrue(viewModel.uiState.value.data!!.showSnowfall)
    }

    @Test
    fun `snow stops when snowfall is turned off or another palette is worn`() = runTest {
        val viewModel = viewModel(today = christmas)

        seasonalState.value = SeasonalThemeState(snowfallEnabled = false)
        assertFalse(viewModel.uiState.value.data!!.showSnowfall)

        seasonalState.value = SeasonalThemeState()
        themeState.value = themeWith(StaticPaletteIds.GOOGLE_BLUE, dynamic = false)
        assertFalse(viewModel.uiState.value.data!!.showSnowfall)

        themeState.value = themeWith(StaticPaletteIds.CHRISTMAS, dynamic = true)
        assertFalse(viewModel.uiState.value.data!!.showSnowfall, "wallpaper colors are on screen")
    }

    @Test
    fun `outside christmas only the easter egg keeps the snow`() = runTest {
        val viewModel = viewModel(today = july)
        assertFalse(viewModel.uiState.value.data!!.showSnowfall)

        seasonalState.value = SeasonalThemeState(unlocked = true)
        assertTrue(viewModel.uiState.value.data!!.showSnowfall)
    }

    @Test
    fun `an ended holiday theme is taken off before a greeting is looked up`() = runTest {
        viewModel(today = july)

        coVerifyOrder {
            seasonal.restoreThemeAfterHoliday(july)
            seasonal.pendingHolidayGreeting(july)
        }
    }

    @Test
    fun `a due greeting is shown and its answer is recorded`() = runTest {
        coEvery { seasonal.pendingHolidayGreeting(christmas) } returns HolidaySeason.CHRISTMAS
        val viewModel = viewModel(today = christmas)
        assertEquals(HolidaySeason.CHRISTMAS, viewModel.uiState.value.data!!.greeting)

        viewModel.onEvent(SeasonalThemeOverlayEvent.AnswerGreeting(useHolidayTheme = true))

        assertNull(viewModel.uiState.value.data!!.greeting)
        coVerify {
            seasonal.answerHolidayGreeting(HolidaySeason.CHRISTMAS, christmas, useHolidayTheme = true)
        }
        val answered = firebaseController.loggedEvents.single()
        assertEquals("holiday_greeting_answered", answered.name)
        assertEquals(AnalyticsValue.Str("christmas"), answered.params["season"])
        assertEquals(AnalyticsValue.Str("use_holiday_theme"), answered.params["choice"])
    }

    @Test
    fun `a second activity does not stack another greeting on the first`() = runTest {
        coEvery { seasonal.pendingHolidayGreeting(christmas) } returns HolidaySeason.CHRISTMAS
        val first = viewModel(today = christmas)
        val second = viewModel(today = christmas)

        assertEquals(HolidaySeason.CHRISTMAS, first.uiState.value.data!!.greeting)
        assertNull(second.uiState.value.data!!.greeting)

        first.onEvent(SeasonalThemeOverlayEvent.AnswerGreeting(useHolidayTheme = false))
        assertTrue(HolidayGreetingPresence.claim(), "answering frees the slot")
    }

    private fun viewModel(today: LocalDate) = SeasonalThemeOverlayViewModel(
        seasonal = seasonal,
        theme = theme,
        firebaseController = firebaseController,
        today = { today },
    )

    private fun themeWith(paletteId: String, dynamic: Boolean) = ThemePreferencesState(
        themeMode = "follow_system",
        dynamicColors = dynamic,
        amoledMode = false,
        dynamicPaletteVariant = 0,
        staticPaletteId = paletteId,
    )
}
