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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.ui

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.exceptions.StorageException
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.analytics.AnalyticsValue
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.theme.ThemePreferencesState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.platform.UiTextHelper
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.repositories.DisplayPreferencesRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.repositories.ThemePreferencesRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakeTelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.UnconfinedDispatcherExtension
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.R as CoreUiR
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.Loadable
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.ui.contracts.DisplaySettingsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.ui.models.DisplaySettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

class DisplaySettingsViewModelTest {

    companion object {
        @JvmField
        @RegisterExtension
        val dispatcherExtension = UnconfinedDispatcherExtension()
    }

    private val telemetryRepository = FakeTelemetryRepository()
    private val display = FakeDisplayPreferencesRepository()
    private val theme = FakeThemePreferencesRepository()

    private fun createViewModel(): DisplaySettingsViewModel = DisplaySettingsViewModel(
        displayPreferences = display,
        themePreferences = theme,
        telemetryRepository = telemetryRepository,
    )

    private fun advance() = dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()

    private fun DisplaySettingsViewModel.readySettings(): DisplaySettings =
        assertIs<Loadable.Ready<DisplaySettings>>(state.value.settings).value

    @Test
    fun `initial load shows the stored preferences`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = createViewModel()
        advance()

        assertEquals(
            DisplaySettings(
                themeMode = "dark",
                dynamicColors = true,
                bouncyButtons = false,
                showBottomBarLabels = true,
                language = "ro",
                startupRoute = "home",
            ),
            viewModel.readySettings(),
        )
    }

    @Test
    fun `a failed read shows a retryable failure and reports it`() = runTest(dispatcherExtension.testDispatcher) {
        theme.readFailure = IllegalStateException("bug")
        val viewModel = createViewModel()
        advance()

        assertTrue(assertIs<Loadable.Failed>(viewModel.state.value.settings).retryable)
        assertTrue(
            telemetryRepository.loggedEvents.any { event ->
                event.name == "vm_op_error" && event.params["action"] == AnalyticsValue.Str("observePreferences")
            }
        )
    }

    @Test
    fun `a corrupt store shows a failure without a retry`() = runTest(dispatcherExtension.testDispatcher) {
        theme.readFailure = StorageException(reason = StorageException.Reason.CORRUPT)
        val viewModel = createViewModel()
        advance()

        assertFalse(assertIs<Loadable.Failed>(viewModel.state.value.settings).retryable)
    }

    @Test
    fun `retrying after a failed read shows the stored preferences`() = runTest(dispatcherExtension.testDispatcher) {
        theme.readFailure = IllegalStateException("bug")
        val viewModel = createViewModel()
        advance()

        theme.readFailure = null
        viewModel.onEvent(DisplaySettingsEvent.Load)
        advance()

        assertEquals("dark", viewModel.readySettings().themeMode)
    }

    @Test
    fun `each event stores its preference and the screen follows it`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = createViewModel()
        advance()

        viewModel.onEvent(DisplaySettingsEvent.ThemeModeChanged("light"))
        viewModel.onEvent(DisplaySettingsEvent.DynamicColorsChanged(false))
        viewModel.onEvent(DisplaySettingsEvent.BouncyButtonsChanged(true))
        viewModel.onEvent(DisplaySettingsEvent.BottomBarLabelsChanged(false))
        viewModel.onEvent(DisplaySettingsEvent.LanguageChanged("fr"))
        viewModel.onEvent(DisplaySettingsEvent.StartupRouteChanged("favorites"))
        advance()

        assertEquals(
            DisplaySettings(
                themeMode = "light",
                dynamicColors = false,
                bouncyButtons = true,
                showBottomBarLabels = false,
                language = "fr",
                startupRoute = "favorites",
            ),
            viewModel.readySettings(),
        )
        assertTrue(viewModel.messages.value.isEmpty())
    }

    @Test
    fun `a failed write keeps the preferences and shows an error`() = runTest(dispatcherExtension.testDispatcher) {
        display.writeFailure = IllegalStateException("bug")
        val viewModel = createViewModel()
        advance()

        viewModel.onEvent(DisplaySettingsEvent.BouncyButtonsChanged(true))
        advance()

        assertFalse(viewModel.readySettings().bouncyButtons)
        val message = viewModel.messages.value.single()
        assertTrue(message.isError)
        assertEquals(CoreUiR.string.screen_error_generic, (message.text as UiTextHelper.StringResource).resourceId)
        assertTrue(
            telemetryRepository.loggedEvents.any { event ->
                event.name == "vm_op_error" && event.params["action"] == AnalyticsValue.Str("setBouncyButtons")
            }
        )
    }

    @Test
    fun `a failed write on full storage says the storage is full`() = runTest(dispatcherExtension.testDispatcher) {
        theme.writeFailure = StorageException(reason = StorageException.Reason.FULL)
        val viewModel = createViewModel()
        advance()

        viewModel.onEvent(DisplaySettingsEvent.ThemeModeChanged("light"))
        advance()

        val message = viewModel.messages.value.single()
        assertEquals(CoreUiR.string.screen_error_storage_full, (message.text as UiTextHelper.StringResource).resourceId)
    }

    private class FakeDisplayPreferencesRepository : DisplayPreferencesRepository {
        var writeFailure: Throwable? = null
        private val labels = MutableStateFlow(true)
        private val bouncy = MutableStateFlow(false)
        private val languageTag = MutableStateFlow("ro")
        private val startup = MutableStateFlow("home")

        override val showBottomBarLabels: Flow<Boolean> = labels
        override val bouncyButtons: Flow<Boolean> = bouncy
        override val language: Flow<String> = languageTag

        override fun startupPage(default: String): Flow<String> = startup

        override suspend fun setShowBottomBarLabels(show: Boolean) = write { labels.value = show }

        override suspend fun setBouncyButtons(enabled: Boolean) = write { bouncy.value = enabled }

        override suspend fun setLanguage(language: String) = write { languageTag.value = language }

        override suspend fun setStartupPage(route: String) = write { startup.value = route }

        private fun write(change: () -> Unit) {
            writeFailure?.let { throw it }
            change()
        }
    }

    private class FakeThemePreferencesRepository : ThemePreferencesRepository {
        var readFailure: Throwable? = null
        var writeFailure: Throwable? = null
        private val mode = MutableStateFlow("dark")
        private val dynamic = MutableStateFlow(true)

        override val preferencesState: Flow<ThemePreferencesState> = emptyFlow()

        override val themeMode: Flow<String>
            get() {
                val failure = readFailure ?: return mode
                return flow { throw failure }
            }

        override val dynamicColors: Flow<Boolean> = dynamic

        override suspend fun selectThemeMode(mode: String) = write { this.mode.value = mode }

        override suspend fun setAmoledMode(enabled: Boolean) = Unit

        override suspend fun setDynamicColors(enabled: Boolean) = write { dynamic.value = enabled }

        override suspend fun selectDynamicPalette(variant: Int) = Unit

        override suspend fun selectStaticPalette(id: String) = Unit

        private fun write(change: () -> Unit) {
            writeFailure?.let { throw it }
            change()
        }
    }
}
