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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.settings.ui

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.platform.UiTextHelper
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakeTelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.UnconfinedDispatcherExtension
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.settings.SettingsCategory
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.settings.SettingsConfig
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.settings.SettingsPreference
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.Loadable
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.settings.R
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.settings.ui.contracts.SettingsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.settings.ui.providers.SettingsProvider
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class SettingsViewModelTest {

    companion object {
        @JvmField
        @RegisterExtension
        val dispatcherExtension = UnconfinedDispatcherExtension()
    }

    private val telemetryRepository = FakeTelemetryRepository()

    private val filled = SettingsConfig(
        title = "Settings",
        categories = listOf(SettingsCategory(preferences = listOf(SettingsPreference(key = "display")))),
    )
    private val empty = SettingsConfig(title = "Settings", categories = emptyList())

    private fun createViewModel(provider: SettingsProvider): SettingsViewModel =
        SettingsViewModel(settingsProvider = provider, telemetryRepository = telemetryRepository)

    private fun advance() = dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()

    @Test
    fun `nothing loads before the list asks`() {
        val provider = FakeSettingsProvider(filled)
        val viewModel = createViewModel(provider)
        advance()

        assertEquals(Loadable.Loading, viewModel.state.value.config)
        assertEquals(0, provider.calls)
    }

    @Test
    fun `loading shows the provider's categories`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = createViewModel(FakeSettingsProvider(filled))

        viewModel.onEvent(SettingsEvent.Load)
        advance()

        assertEquals(Loadable.Ready(filled), viewModel.state.value.config)
    }

    @Test
    fun `a config with no category shows the empty state with its message`() =
        runTest(dispatcherExtension.testDispatcher) {
            val viewModel = createViewModel(FakeSettingsProvider(empty))

            viewModel.onEvent(SettingsEvent.Load)
            advance()

            val config = assertIs<Loadable.Empty>(viewModel.state.value.config)
            assertEquals(
                R.string.error_no_settings_found,
                (config.message as UiTextHelper.StringResource).resourceId,
            )
        }

    @Test
    fun `a provider that throws shows a failure and reports it`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = createViewModel(FakeSettingsProvider(filled, failure = IllegalStateException("bug")))

        viewModel.onEvent(SettingsEvent.Load)
        advance()

        assertIs<Loadable.Failed>(viewModel.state.value.config)
        assertTrue(telemetryRepository.loggedEvents.any { it.name == "vm_op_error" })
    }

    @Test
    fun `loading again replaces the empty state`() = runTest(dispatcherExtension.testDispatcher) {
        val provider = FakeSettingsProvider(empty)
        val viewModel = createViewModel(provider)
        viewModel.onEvent(SettingsEvent.Load)
        advance()
        assertIs<Loadable.Empty>(viewModel.state.value.config)

        provider.config = filled
        viewModel.onEvent(SettingsEvent.Load)
        advance()

        assertEquals(Loadable.Ready(filled), viewModel.state.value.config)
    }

    @Test
    fun `each load asks the provider once`() = runTest(dispatcherExtension.testDispatcher) {
        val provider = FakeSettingsProvider(filled)
        val viewModel = createViewModel(provider)

        viewModel.onEvent(SettingsEvent.Load)
        advance()

        assertEquals(1, provider.calls)
    }

    private class FakeSettingsProvider(
        var config: SettingsConfig,
        private val failure: Throwable? = null,
    ) : SettingsProvider {
        var calls: Int = 0
            private set

        override fun provideSettingsConfig(): SettingsConfig {
            calls++
            failure?.let { throw it }
            return config
        }
    }
}
