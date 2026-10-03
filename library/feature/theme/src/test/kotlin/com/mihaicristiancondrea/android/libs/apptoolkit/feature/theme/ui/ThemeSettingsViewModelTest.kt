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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.ui

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.exceptions.StorageException
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.analytics.AnalyticsValue
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.theme.SeasonalThemeState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.theme.ThemePreferencesState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.theme.WeatherEffect
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.datastore.DataStoreNamesConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.platform.UiTextHelper
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakeTelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.UnconfinedDispatcherExtension
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.R as CoreUiR
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.GenericErrorText
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.Loadable
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.ui.contracts.ThemeSettingsEvent
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

class ThemeSettingsViewModelTest {

    companion object {
        @JvmField
        @RegisterExtension
        val dispatcherExtension = UnconfinedDispatcherExtension()
    }

    private val telemetryRepository = FakeTelemetryRepository()
    private val preferences = FakeThemePreferencesRepository()
    private val seasonal = FakeSeasonalThemeRepository()

    private fun createViewModel(): ThemeSettingsViewModel = ThemeSettingsViewModel(
        preferences = preferences,
        seasonal = seasonal,
        telemetryRepository = telemetryRepository,
    )

    private fun advance() = dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()

    private fun ThemeSettingsViewModel.loadedPreferences(): ThemePreferencesState =
        assertIs<Loadable.Ready<ThemePreferencesState>>(state.value.preferences).value

    @Test
    fun `initial load shows the stored preferences`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = createViewModel()
        advance()

        assertEquals(FakeThemePreferencesRepository.DefaultPreferences, viewModel.loadedPreferences())
        assertFalse(viewModel.state.value.seasonalThemesUnlocked)
        assertEquals(WeatherEffect.Automatic, viewModel.state.value.weatherEffect)
    }

    /** The palette rows scroll to the first selection they see, so a placeholder would be wrong. */
    @Test
    fun `nothing is ready until the stored preferences arrive`() = runTest(dispatcherExtension.testDispatcher) {
        preferences.stored.value = null
        val viewModel = createViewModel()
        advance()
        assertEquals(Loadable.Loading, viewModel.state.value.preferences)

        preferences.stored.value = FakeThemePreferencesRepository.DefaultPreferences.copy(staticPaletteId = "rose")
        advance()

        assertEquals("rose", viewModel.loadedPreferences().staticPaletteId)
    }

    @Test
    fun `the state follows the seasonal themes unlock and the weather effect`() =
        runTest(dispatcherExtension.testDispatcher) {
            val viewModel = createViewModel()
            advance()

            seasonal.stored.value = SeasonalThemeState(unlocked = true, weatherEffect = WeatherEffect.Off)
            advance()

            assertTrue(viewModel.state.value.seasonalThemesUnlocked)
            assertEquals(WeatherEffect.Off, viewModel.state.value.weatherEffect)
        }

    @Test
    fun `a failed read shows the failure state with a retry`() = runTest(dispatcherExtension.testDispatcher) {
        preferences.readFailure = IllegalStateException("unreadable")
        val viewModel = createViewModel()
        advance()

        val failed = assertIs<Loadable.Failed>(viewModel.state.value.preferences)
        assertEquals(GenericErrorText, failed.message)
        assertTrue(failed.retryable)
        val error = telemetryRepository.loggedEvents.single { it.name == "vm_op_error" }
        assertEquals(AnalyticsValue.Str("observePreferences"), error.params["action"])
    }

    @Test
    fun `a corrupt store offers no retry`() = runTest(dispatcherExtension.testDispatcher) {
        preferences.readFailure = StorageException(StorageException.Reason.CORRUPT)
        val viewModel = createViewModel()
        advance()

        assertFalse(assertIs<Loadable.Failed>(viewModel.state.value.preferences).retryable)
    }

    @Test
    fun `retrying after a failed read shows the preferences`() = runTest(dispatcherExtension.testDispatcher) {
        preferences.readFailure = IllegalStateException("unreadable")
        val viewModel = createViewModel()
        advance()
        assertIs<Loadable.Failed>(viewModel.state.value.preferences)

        preferences.readFailure = null
        viewModel.onEvent(ThemeSettingsEvent.Load)
        advance()

        assertEquals(FakeThemePreferencesRepository.DefaultPreferences, viewModel.loadedPreferences())
    }

    @Test
    fun `selecting a theme mode saves it`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = createViewModel()
        advance()

        viewModel.onEvent(ThemeSettingsEvent.SelectThemeMode(DataStoreNamesConstants.THEME_MODE_LIGHT))
        advance()

        assertEquals(DataStoreNamesConstants.THEME_MODE_LIGHT, viewModel.loadedPreferences().themeMode)
    }

    @Test
    fun `toggling amoled saves it`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = createViewModel()
        advance()

        viewModel.onEvent(ThemeSettingsEvent.SetAmoledMode(enabled = true))
        advance()

        assertTrue(viewModel.loadedPreferences().amoledMode)
    }

    @Test
    fun `selecting a static palette saves it and turns wallpaper colors off`() =
        runTest(dispatcherExtension.testDispatcher) {
            val viewModel = createViewModel()
            advance()

            viewModel.onEvent(ThemeSettingsEvent.SelectStaticPalette("rose"))
            advance()

            val loaded = viewModel.loadedPreferences()
            assertEquals("rose", loaded.staticPaletteId)
            assertFalse(loaded.dynamicColors)
        }

    @Test
    fun `selecting a dynamic palette saves it and turns wallpaper colors on`() =
        runTest(dispatcherExtension.testDispatcher) {
            preferences.stored.value = FakeThemePreferencesRepository.DefaultPreferences.copy(dynamicColors = false)
            val viewModel = createViewModel()
            advance()

            viewModel.onEvent(ThemeSettingsEvent.SelectDynamicPalette(variant = 5))
            advance()

            val loaded = viewModel.loadedPreferences()
            assertEquals(5, loaded.dynamicPaletteVariant)
            assertTrue(loaded.dynamicColors)
        }

    @Test
    fun `choosing a weather effect saves it`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = createViewModel()
        advance()

        viewModel.onEvent(ThemeSettingsEvent.SetWeatherEffect(WeatherEffect.Rain))
        advance()

        assertEquals(WeatherEffect.Rain, seasonal.stored.value.weatherEffect)
        assertEquals(WeatherEffect.Rain, viewModel.state.value.weatherEffect)
    }

    @Test
    fun `a failed write shows an error message and keeps the page`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = createViewModel()
        advance()
        preferences.writeFailure = IllegalStateException("read-only")

        viewModel.onEvent(ThemeSettingsEvent.SelectStaticPalette("rose"))
        advance()

        val message = viewModel.messages.value.single()
        assertEquals(GenericErrorText, message.text)
        assertTrue(message.isError)
        assertEquals(FakeThemePreferencesRepository.DefaultPreferences, viewModel.loadedPreferences())
        val error = telemetryRepository.loggedEvents.single { it.name == "vm_op_error" }
        assertEquals(AnalyticsValue.Str("persistThemeSetting"), error.params["action"])
        assertEquals(AnalyticsValue.Str("static_palette"), error.params["setting"])
    }

    @Test
    fun `a failed write the user can act on shows its own text`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = createViewModel()
        advance()
        seasonal.failure = StorageException(StorageException.Reason.FULL)

        viewModel.onEvent(ThemeSettingsEvent.SetWeatherEffect(WeatherEffect.Snow))
        advance()

        assertEquals(
            UiTextHelper.StringResource(CoreUiR.string.screen_error_storage_full),
            viewModel.messages.value.single().text,
        )
    }

    @Test
    fun `a shown message leaves the queue`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = createViewModel()
        advance()
        preferences.writeFailure = IllegalStateException("read-only")
        viewModel.onEvent(ThemeSettingsEvent.SetAmoledMode(enabled = true))
        advance()

        viewModel.messageShown(viewModel.messages.value.single().id)

        assertTrue(viewModel.messages.value.isEmpty())
    }
}
