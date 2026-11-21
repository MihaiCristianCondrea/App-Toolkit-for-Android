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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.ui

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.exceptions.StorageException
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.models.AnalyticsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.analytics.SettingsAnalytics
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakeTelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.UnconfinedDispatcherExtension
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.ui.contracts.DeveloperOptionsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.layout.ShellLayoutMode
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.data.repositories.ShellPreferencesRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.ShellSettings
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.models.AnalyticsValue
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.UiText
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.emitAll
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.R as CoreUiR
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.di.developerOptionsModule
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.TelemetryRepository
import org.koin.dsl.koinApplication
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DeveloperOptionsViewModelTest {
    companion object {
        @JvmField
        @RegisterExtension
        val dispatcherExtension = UnconfinedDispatcherExtension()
    }

    private val repository = TestPreferences()
    private val telemetry = FakeTelemetryRepository()
    private fun viewModel() = DeveloperOptionsViewModel(repository, telemetry)
    private fun advance() = dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()
    private fun savedEvents(): List<AnalyticsEvent> =
        telemetry.loggedEvents.filter { it.name == SettingsAnalytics.Events.ACTION }

    @Test
    fun `Koin uses the repository passed by the host screen`() = runTest(dispatcherExtension.testDispatcher) {
        val app = koinApplication {
            modules(developerOptionsModule, module { single<TelemetryRepository> { telemetry } })
        }
        try {
            val viewModel = app.koin.get<DeveloperOptionsViewModel> { parametersOf(repository) }
            viewModel.onEvent(DeveloperOptionsEvent.SetLayout(ShellLayoutMode.Rail))
            advance()
            assertEquals(ShellLayoutMode.Rail, repository.settings.value.layoutMode)
        } finally {
            app.close()
        }
    }

    @Test
    fun `state follows the host repository and saving reports after persistence`() =
        runTest(dispatcherExtension.testDispatcher) {
            val viewModel = viewModel()
            assertEquals(repository.defaults, viewModel.state.value.settings)

            viewModel.onEvent(DeveloperOptionsEvent.SetLayout(ShellLayoutMode.Rail))
            advance()

            assertEquals(ShellLayoutMode.Rail, viewModel.state.value.settings.layoutMode)
            assertEquals(1, savedEvents().size)
            assertTrue(viewModel.messages.value.isEmpty())
        }

    @Test
    fun `a failed save keeps settings and shows an error without a success event, then retries`() =
        runTest(dispatcherExtension.testDispatcher) {
            val viewModel = viewModel()
            repository.failure = StorageException(StorageException.Reason.FULL)
            val event = DeveloperOptionsEvent.SetLayout(ShellLayoutMode.Rail)

            viewModel.onEvent(event)
            advance()

            assertEquals(repository.defaults, viewModel.state.value.settings)
            assertTrue(viewModel.messages.value.single().isError)
            assertTrue(savedEvents().isEmpty())
            assertEquals(1, telemetry.viewModelErrors.size)
            val error = telemetry.loggedEvents.single { it.name == "vm_op_error" }
            assertEquals(
                AnalyticsValue.Str("layout"),
                error.params[SettingsAnalytics.Params.PREFERENCE_KEY],
            )

            repository.failure = null
            viewModel.onEvent(event)
            advance()
            assertEquals(ShellLayoutMode.Rail, viewModel.state.value.settings.layoutMode)
            assertEquals(1, savedEvents().size)
        }

    @Test
    fun `a failed reset keeps choices and reports an error, then reset restores host defaults`() =
        runTest(dispatcherExtension.testDispatcher) {
            repository.settings.value = repository.defaults.copy(layoutMode = ShellLayoutMode.Rail)
            val viewModel = viewModel()
            repository.failure = StorageException(StorageException.Reason.FAILED)

            viewModel.onEvent(DeveloperOptionsEvent.Reset)
            advance()
            assertEquals(ShellLayoutMode.Rail, viewModel.state.value.settings.layoutMode)
            assertTrue(viewModel.messages.value.single().isError)
            assertTrue(savedEvents().isEmpty())

            repository.failure = null
            viewModel.onEvent(DeveloperOptionsEvent.Reset)
            advance()
            assertEquals(repository.defaults, viewModel.state.value.settings)
            assertEquals(1, savedEvents().size)
        }

    @Test
    fun `a failed read keeps settings and retry resumes a single live observer`() =
        runTest(dispatcherExtension.testDispatcher) {
            repository.settings.value = repository.defaults.copy(layoutMode = ShellLayoutMode.Rail)
            var attempts = 0
            var activeObservers = 0
            val preferences = object : ShellPreferencesRepository by repository {
                override val settings = flow {
                    attempts++
                    if (attempts == 1) throw StorageException(StorageException.Reason.FAILED)
                    activeObservers++
                    try {
                        emitAll(repository.settings)
                    } finally {
                        activeObservers--
                    }
                }
            }
            val viewModel = DeveloperOptionsViewModel(preferences, telemetry)
            advance()

            assertEquals(ShellLayoutMode.Rail, viewModel.state.value.settings.layoutMode)
            val message = viewModel.messages.value.single()
            assertTrue(message.isError)
            assertEquals(UiText.StringResource(CoreUiR.string.try_again), message.actionLabel)
            assertEquals(1, telemetry.viewModelErrors.size)

            viewModel.onEvent(DeveloperOptionsEvent.Load)
            advance()
            assertEquals(2, attempts)
            assertEquals(1, activeObservers)
            assertTrue(savedEvents().isEmpty())

            viewModel.onEvent(DeveloperOptionsEvent.Load)
            advance()
            assertEquals(3, attempts)
            assertEquals(1, activeObservers)
            repository.settings.value = repository.defaults
            advance()
            assertEquals(repository.defaults, viewModel.state.value.settings)
            assertEquals(1, telemetry.viewModelErrors.size)
        }

    private class TestPreferences : ShellPreferencesRepository {
        override val defaults = ShellSettings(hideTopBarOnScroll = true)
        override val settings = MutableStateFlow(defaults)
        override val lastKnown get() = settings.value
        var failure: Throwable? = null
        override suspend fun update(transform: (ShellSettings) -> ShellSettings) {
            failure?.let { throw it }
            settings.value = transform(settings.value)
        }
    }
}
