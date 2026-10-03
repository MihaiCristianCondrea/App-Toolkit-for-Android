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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.ui

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.analytics.AnalyticsValue
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.theme.HolidaySeason
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.theme.SeasonalThemeState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.theme.ThemePreferencesState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.theme.WeatherEffect
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.platform.UiTextHelper
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.repositories.SeasonalThemeRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.repositories.ThemePreferencesRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakeTelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.UnconfinedDispatcherExtension
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.R as CoreUiR
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.ui.contracts.OnboardingThemeEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.ui.states.OnboardingThemeUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import java.time.LocalDate
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class OnboardingThemeViewModelTest {

    companion object {
        @JvmField
        @RegisterExtension
        val dispatcherExtension = UnconfinedDispatcherExtension()
    }

    private val telemetryRepository = FakeTelemetryRepository()

    private val stored = ThemePreferencesState(
        themeMode = "dark",
        dynamicColors = false,
        amoledMode = true,
        dynamicPaletteVariant = 2,
        staticPaletteId = "blue",
    )

    private fun createViewModel(
        preferences: ThemePreferencesRepository,
        seasonal: FakeSeasonalThemeRepository = FakeSeasonalThemeRepository(),
    ): OnboardingThemeViewModel = OnboardingThemeViewModel(
        preferences = preferences,
        seasonal = seasonal,
        telemetryRepository = telemetryRepository,
    )

    private fun advance() = dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()

    @Test
    fun `the first load shows the stored preferences`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = createViewModel(FakeThemePreferencesRepository(initial = stored))
        advance()

        assertEquals(stored, viewModel.state.value.preferences)
        assertFalse(viewModel.state.value.seasonalThemesUnlocked)
    }

    @Test
    fun `found seasonal themes are offered all year`() = runTest(dispatcherExtension.testDispatcher) {
        val seasonal = FakeSeasonalThemeRepository()
        val viewModel = createViewModel(FakeThemePreferencesRepository(initial = stored), seasonal)
        advance()

        seasonal.state.value = SeasonalThemeState(unlocked = true)
        advance()

        assertTrue(viewModel.state.value.seasonalThemesUnlocked)
    }

    @Test
    fun `a stored change reaches the page`() = runTest(dispatcherExtension.testDispatcher) {
        val preferences = FakeThemePreferencesRepository(initial = stored)
        val viewModel = createViewModel(preferences)
        advance()

        preferences.state.value = stored.copy(themeMode = "light")
        advance()

        assertEquals("light", viewModel.state.value.preferences.themeMode)
    }

    @Test
    fun `a failed read is reported and the page keeps the defaults`() =
        runTest(dispatcherExtension.testDispatcher) {
            val viewModel = createViewModel(FakeThemePreferencesRepository(initial = stored, readFails = true))
            advance()

            assertEquals(OnboardingThemeUiState(), viewModel.state.value)
            assertTrue(viewModel.messages.value.isEmpty())
            assertTrue(telemetryRepository.loggedEvents.any { it.name == "vm_op_error" })
        }

    @Test
    fun `each event saves its own setting`() = runTest(dispatcherExtension.testDispatcher) {
        val preferences = FakeThemePreferencesRepository(initial = stored)
        val viewModel = createViewModel(preferences)
        advance()

        viewModel.onEvent(OnboardingThemeEvent.SelectThemeMode(mode = "light"))
        viewModel.onEvent(OnboardingThemeEvent.SetAmoledMode(enabled = false))
        viewModel.onEvent(OnboardingThemeEvent.SelectDynamicPalette(variant = 1))
        viewModel.onEvent(OnboardingThemeEvent.SelectStaticPalette(id = "green"))
        advance()

        assertEquals(
            listOf("themeMode=light", "amoled=false", "dynamicPalette=1", "staticPalette=green"),
            preferences.writes,
        )
        assertTrue(viewModel.messages.value.isEmpty())
    }

    @Test
    fun `a failed write shows an error and is reported with its setting`() =
        runTest(dispatcherExtension.testDispatcher) {
            val preferences = FakeThemePreferencesRepository(initial = stored, writeFails = true)
            val viewModel = createViewModel(preferences)
            advance()

            viewModel.onEvent(OnboardingThemeEvent.SetAmoledMode(enabled = false))
            advance()

            val message = viewModel.messages.value.single()
            assertEquals(
                CoreUiR.string.screen_error_generic,
                (message.text as UiTextHelper.StringResource).resourceId,
            )
            assertTrue(message.isError)
            val error = telemetryRepository.loggedEvents.single { it.name == "vm_op_error" }
            assertEquals(AnalyticsValue.Str("persistOnboardingTheme"), error.params["action"])
            assertEquals(AnalyticsValue.Str("amoled_mode"), error.params["setting"])
        }

    @Test
    fun `a write after a failed one saves`() = runTest(dispatcherExtension.testDispatcher) {
        val preferences = FakeThemePreferencesRepository(initial = stored, writeFails = true)
        val viewModel = createViewModel(preferences)
        advance()
        viewModel.onEvent(OnboardingThemeEvent.SelectThemeMode(mode = "light"))
        advance()
        viewModel.messageShown(viewModel.messages.value.single().id)

        preferences.writeFails = false
        viewModel.onEvent(OnboardingThemeEvent.SelectThemeMode(mode = "light"))
        advance()

        assertEquals(listOf("themeMode=light"), preferences.writes)
        assertTrue(viewModel.messages.value.isEmpty())
    }

    private class FakeThemePreferencesRepository(
        initial: ThemePreferencesState,
        private val readFails: Boolean = false,
        var writeFails: Boolean = false,
    ) : ThemePreferencesRepository {
        val state = MutableStateFlow(initial)
        val writes = mutableListOf<String>()

        override val preferencesState: Flow<ThemePreferencesState> =
            if (readFails) flow { throw IllegalStateException("read") } else state

        override val themeMode: Flow<String> = state.map { it.themeMode }

        override val dynamicColors: Flow<Boolean> = state.map { it.dynamicColors }

        override suspend fun selectThemeMode(mode: String) = write("themeMode=$mode")

        override suspend fun setAmoledMode(enabled: Boolean) = write("amoled=$enabled")

        override suspend fun setDynamicColors(enabled: Boolean) = write("dynamicColors=$enabled")

        override suspend fun selectDynamicPalette(variant: Int) = write("dynamicPalette=$variant")

        override suspend fun selectStaticPalette(id: String) = write("staticPalette=$id")

        private fun write(entry: String) {
            check(!writeFails) { "write" }
            writes += entry
        }
    }

    private class FakeSeasonalThemeRepository : SeasonalThemeRepository {
        override val state = MutableStateFlow(SeasonalThemeState())

        override suspend fun unlockSeasonalThemes(): Boolean = false

        override suspend fun setWeatherEffect(effect: WeatherEffect) = Unit

        override suspend fun pendingHolidayGreeting(today: LocalDate): HolidaySeason? = null

        override suspend fun answerHolidayGreeting(
            season: HolidaySeason,
            today: LocalDate,
            useHolidayTheme: Boolean,
        ) = Unit

        override suspend fun restoreThemeAfterHoliday(today: LocalDate) = Unit
    }
}
