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
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.theme.WeatherEffect
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.colorscheme.StaticPaletteIds
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakeTelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.UnconfinedDispatcherExtension
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.ui.FakeSeasonalThemeRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.ui.FakeThemePreferencesRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.ui.seasonal.contracts.SeasonalThemeOverlayEvent
import java.time.LocalDate
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

    private val seasonal = FakeSeasonalThemeRepository()
    private val theme = FakeThemePreferencesRepository(initial = themeWith(StaticPaletteIds.CHRISTMAS, dynamic = false))
    private val telemetryRepository = FakeTelemetryRepository()

    @AfterEach
    fun tearDown() {
        HolidayGreetingPresence.release()
    }

    private fun advance() = dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()

    private fun viewModel(today: LocalDate): SeasonalThemeOverlayViewModel {
        val viewModel = SeasonalThemeOverlayViewModel(
            seasonal = seasonal,
            theme = theme,
            telemetryRepository = telemetryRepository,
            today = { today },
        )
        advance()
        return viewModel
    }

    private fun wear(paletteId: String, dynamic: Boolean) {
        theme.stored.value = themeWith(paletteId, dynamic)
        advance()
    }

    private fun setSeasonal(state: SeasonalThemeState) {
        seasonal.stored.value = state
        advance()
    }

    @Test
    fun `snow falls with the christmas palette during christmas`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = viewModel(today = christmas)

        assertTrue(viewModel.state.value.showSnowfall)
    }

    @Test
    fun `snow stops when another palette is worn`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = viewModel(today = christmas)

        wear(StaticPaletteIds.GOOGLE_BLUE, dynamic = false)
        assertFalse(viewModel.state.value.showSnowfall)

        wear(StaticPaletteIds.CHRISTMAS, dynamic = true)
        assertFalse(viewModel.state.value.showSnowfall, "wallpaper colors are on screen")
    }

    @Test
    fun `outside christmas only the easter egg keeps the snow`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = viewModel(today = july)
        assertFalse(viewModel.state.value.showSnowfall)

        setSeasonal(SeasonalThemeState(unlocked = true))

        assertTrue(viewModel.state.value.showSnowfall)
    }

    @Test
    fun `the off weather effect keeps the christmas palette without snow`() =
        runTest(dispatcherExtension.testDispatcher) {
            val viewModel = viewModel(today = christmas)

            setSeasonal(SeasonalThemeState(unlocked = true, weatherEffect = WeatherEffect.Off))

            assertFalse(viewModel.state.value.showSnowfall)
            assertFalse(viewModel.state.value.showRain)
        }

    @Test
    fun `the rain weather effect rains over any palette instead of snowing`() =
        runTest(dispatcherExtension.testDispatcher) {
            val viewModel = viewModel(today = july)

            setSeasonal(SeasonalThemeState(unlocked = true, weatherEffect = WeatherEffect.Rain))
            assertTrue(viewModel.state.value.showRain)
            assertFalse(viewModel.state.value.showSnowfall)

            wear(StaticPaletteIds.GOOGLE_BLUE, dynamic = true)
            assertTrue(viewModel.state.value.showRain)
        }

    @Test
    fun `the christmas theme turns rain into snow for the season`() = runTest(dispatcherExtension.testDispatcher) {
        seasonal.stored.value = SeasonalThemeState(unlocked = true, weatherEffect = WeatherEffect.Rain)

        val holidays = viewModel(today = christmas)
        assertTrue(holidays.state.value.showSnowfall)
        assertFalse(holidays.state.value.showRain)

        wear(StaticPaletteIds.GOOGLE_BLUE, dynamic = false)
        assertTrue(holidays.state.value.showRain, "another palette keeps the rain")
        assertFalse(holidays.state.value.showSnowfall)

        wear(StaticPaletteIds.CHRISTMAS, dynamic = false)
        val afterwards = viewModel(today = july)
        assertTrue(afterwards.state.value.showRain, "the season is over")
        assertFalse(afterwards.state.value.showSnowfall)
    }

    @Test
    fun `the snow weather effect snows over any palette all year`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = viewModel(today = july)
        wear(StaticPaletteIds.GOOGLE_BLUE, dynamic = true)

        setSeasonal(SeasonalThemeState(unlocked = true, weatherEffect = WeatherEffect.Snow))

        assertTrue(viewModel.state.value.showSnowfall)
        assertFalse(viewModel.state.value.showRain)
    }

    @Test
    fun `snow on any palette needs the easter egg`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = viewModel(today = july)
        wear(StaticPaletteIds.GOOGLE_BLUE, dynamic = false)

        setSeasonal(SeasonalThemeState(weatherEffect = WeatherEffect.Snow))

        assertFalse(viewModel.state.value.showSnowfall)
    }

    @Test
    fun `rain needs the easter egg`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = viewModel(today = july)

        setSeasonal(SeasonalThemeState(weatherEffect = WeatherEffect.Rain))

        assertFalse(viewModel.state.value.showRain)
    }

    @Test
    fun `the overlay follows the stored theme mode`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = viewModel(today = july)

        theme.stored.value = themeWith(StaticPaletteIds.CHRISTMAS, dynamic = false).copy(themeMode = "dark_mode")
        advance()

        assertEquals("dark_mode", viewModel.state.value.themeMode)
    }

    @Test
    fun `an ended holiday theme is taken off before a greeting is looked up`() =
        runTest(dispatcherExtension.testDispatcher) {
            viewModel(today = july)

            assertEquals(
                listOf("restoreThemeAfterHoliday($july)", "pendingHolidayGreeting($july)"),
                seasonal.calls,
            )
        }

    @Test
    fun `a failed restore still looks up the greeting and is reported`() =
        runTest(dispatcherExtension.testDispatcher) {
            seasonal.failure = IllegalStateException("store unavailable")

            val viewModel = viewModel(today = july)

            assertEquals(
                listOf("restoreThemeAfterHoliday($july)", "pendingHolidayGreeting($july)"),
                seasonal.calls,
            )
            assertNull(viewModel.state.value.greeting)
            val failedActions = telemetryRepository.loggedEvents
                .filter { it.name == "vm_op_error" }
                .map { it.params["action"] }
            assertEquals(
                listOf(AnalyticsValue.Str("restoreThemeAfterHoliday"), AnalyticsValue.Str("pendingHolidayGreeting")),
                failedActions,
            )
        }

    @Test
    fun `a due greeting is shown and its answer is recorded`() = runTest(dispatcherExtension.testDispatcher) {
        seasonal.pendingGreeting = HolidaySeason.CHRISTMAS
        val viewModel = viewModel(today = christmas)
        assertEquals(HolidaySeason.CHRISTMAS, viewModel.state.value.greeting)

        viewModel.onEvent(SeasonalThemeOverlayEvent.AnswerGreeting(useHolidayTheme = true))
        advance()

        assertNull(viewModel.state.value.greeting)
        assertEquals(listOf(Triple(HolidaySeason.CHRISTMAS, christmas, true)), seasonal.answers)
        val answered = telemetryRepository.loggedEvents.single { it.name == "holiday_greeting_answered" }
        assertEquals(AnalyticsValue.Str("christmas"), answered.params["season"])
        assertEquals(AnalyticsValue.Str("use_holiday_theme"), answered.params["choice"])
    }

    @Test
    fun `a second activity does not stack another greeting on the first`() =
        runTest(dispatcherExtension.testDispatcher) {
            seasonal.pendingGreeting = HolidaySeason.CHRISTMAS
            val first = viewModel(today = christmas)
            val second = viewModel(today = christmas)

            assertEquals(HolidaySeason.CHRISTMAS, first.state.value.greeting)
            assertNull(second.state.value.greeting)

            first.onEvent(SeasonalThemeOverlayEvent.AnswerGreeting(useHolidayTheme = false))
            advance()

            assertTrue(HolidayGreetingPresence.claim(), "answering frees the slot")
        }

    @Test
    fun `a failed answer still frees the greeting slot`() = runTest(dispatcherExtension.testDispatcher) {
        seasonal.pendingGreeting = HolidaySeason.CHRISTMAS
        val viewModel = viewModel(today = christmas)
        seasonal.failure = IllegalStateException("store unavailable")

        viewModel.onEvent(SeasonalThemeOverlayEvent.AnswerGreeting(useHolidayTheme = true))
        advance()

        assertNull(viewModel.state.value.greeting)
        assertTrue(HolidayGreetingPresence.claim(), "a failed save frees the slot")
        val error = telemetryRepository.loggedEvents.single { it.name == "vm_op_error" }
        assertEquals(AnalyticsValue.Str("answerHolidayGreeting"), error.params["action"])
    }

    private fun themeWith(paletteId: String, dynamic: Boolean) = ThemePreferencesState(
        themeMode = "follow_system",
        dynamicColors = dynamic,
        amoledMode = false,
        dynamicPaletteVariant = 0,
        staticPaletteId = paletteId,
    )
}
