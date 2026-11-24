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
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.TelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.models.AnalyticsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.models.AnalyticsValue
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.UiText
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.analytics.SettingsAnalytics
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakeTelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.UnconfinedDispatcherExtension
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.di.developerOptionsModule
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.ui.contracts.DeveloperOptionsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.layout.ShellLayoutMode
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.data.repositories.ShellPreferencesRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.AccessoryMode
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.ShellSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import org.koin.core.parameter.parametersOf
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.R as CoreUiR

class DeveloperOptionsViewModelTest {
    companion object {
        @JvmField
        @RegisterExtension
        val dispatcherExtension = UnconfinedDispatcherExtension()
    }

    private val repository = TestPreferences()
    private val telemetry = FakeTelemetryRepository()

    private fun viewModel() = DeveloperOptionsViewModel(
        preferences = repository,
        telemetryRepository = telemetry,
    )

    private fun advance() = dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()

    private fun savedEvents(): List<AnalyticsEvent> =
        telemetry.loggedEvents.filter { event -> event.name == SettingsAnalytics.Events.ACTION }

    @Test
    fun `Koin uses the repository passed by the host screen`() = runTest(context = dispatcherExtension.testDispatcher) {
        val app = koinApplication {
            modules(developerOptionsModule, module { single<TelemetryRepository> { telemetry } })
        }
        try {
            val viewModel = app.koin.get<DeveloperOptionsViewModel> { parametersOf(repository) }
            viewModel.onEvent(event = DeveloperOptionsEvent.SetLayout(value = ShellLayoutMode.Rail))
            advance()
            assertEquals(ShellLayoutMode.Rail, repository.settings.value.layoutMode)
        } finally {
            app.close()
        }
    }

    @Test
    fun `state follows the host repository and saving reports after persistence`() =
        runTest(context = dispatcherExtension.testDispatcher) {
            val viewModel = viewModel()
            assertEquals(repository.defaults, viewModel.state.value.settings)

            viewModel.onEvent(event = DeveloperOptionsEvent.SetLayout(value = ShellLayoutMode.Rail))
            viewModel.onEvent(event = DeveloperOptionsEvent.SetBottomAccessory(value = AccessoryMode.BannerOnly))
            viewModel.onEvent(event = DeveloperOptionsEvent.SetStartOverride(value = 2))
            advance()

            assertEquals(ShellLayoutMode.Rail, viewModel.state.value.settings.layoutMode)
            assertEquals(AccessoryMode.BannerOnly, viewModel.state.value.settings.accessoryMode)
            assertEquals(2, viewModel.state.value.settings.startOverride)
            assertEquals(
                listOf(
                    AnalyticsValue.Str(value = "layout"),
                    AnalyticsValue.Str(value = "bottom_accessory"),
                    AnalyticsValue.Str(value = "start_override"),
                ),
                savedEvents().map { it.params[SettingsAnalytics.Params.PREFERENCE_KEY] },
            )
            assertTrue(viewModel.messages.value.isEmpty())
        }

    @Test
    fun `a failed save keeps settings and shows an error without a success event, then retries`() =
        runTest(context = dispatcherExtension.testDispatcher) {
            val viewModel = viewModel()
            repository.failure = StorageException(reason = StorageException.Reason.FULL)
            val event = DeveloperOptionsEvent.SetLayout(value = ShellLayoutMode.Rail)

            viewModel.onEvent(event = event)
            advance()

            assertEquals(repository.defaults, viewModel.state.value.settings)
            assertTrue(viewModel.messages.value.single().isError)
            assertTrue(savedEvents().isEmpty())
            assertEquals(1, telemetry.viewModelErrors.size)
            val error = telemetry.loggedEvents.single { it.name == "vm_op_error" }
            assertEquals(
                AnalyticsValue.Str(value = "layout"),
                error.params[SettingsAnalytics.Params.PREFERENCE_KEY],
            )

            repository.failure = null
            viewModel.onEvent(event = event)
            advance()
            assertEquals(ShellLayoutMode.Rail, viewModel.state.value.settings.layoutMode)
            assertEquals(1, savedEvents().size)
        }

    @Test
    fun `a failed reset keeps choices and reports an error, then reset restores host defaults`() =
        runTest(context = dispatcherExtension.testDispatcher) {
            repository.settings.value = repository.defaults.copy(layoutMode = ShellLayoutMode.Rail)
            val viewModel = viewModel()
            repository.failure = StorageException(reason = StorageException.Reason.FAILED)

            viewModel.onEvent(event = DeveloperOptionsEvent.Reset)
            advance()
            assertEquals(ShellLayoutMode.Rail, viewModel.state.value.settings.layoutMode)
            assertTrue(viewModel.messages.value.single().isError)
            assertTrue(savedEvents().isEmpty())

            repository.failure = null
            viewModel.onEvent(event = DeveloperOptionsEvent.Reset)
            advance()
            assertEquals(repository.defaults, viewModel.state.value.settings)
            assertEquals(1, savedEvents().size)
        }

    @Test
    fun `a failed read keeps settings and retry resumes a single live observer`() =
        runTest(context = dispatcherExtension.testDispatcher) {
            repository.settings.value = repository.defaults.copy(layoutMode = ShellLayoutMode.Rail)
            var attempts = 0
            var activeObservers = 0
            val preferences = object : ShellPreferencesRepository by repository {
                override val settings = flow {
                    attempts++
                    if (attempts == 1) throw StorageException(reason = StorageException.Reason.FAILED)
                    activeObservers++
                    try {
                        emitAll(flow = repository.settings)
                    } finally {
                        activeObservers--
                    }
                }
            }
            val viewModel = DeveloperOptionsViewModel(
                preferences = preferences,
                telemetryRepository = telemetry,
            )
            advance()

            assertEquals(ShellLayoutMode.Rail, viewModel.state.value.settings.layoutMode)
            val message = viewModel.messages.value.single()
            assertTrue(message.isError)
            assertEquals(UiText.StringResource(resourceId = CoreUiR.string.try_again), message.actionLabel)
            assertEquals(1, telemetry.viewModelErrors.size)

            viewModel.onEvent(event = DeveloperOptionsEvent.Load)
            advance()
            assertEquals(2, attempts)
            assertEquals(1, activeObservers)
            assertTrue(savedEvents().isEmpty())

            viewModel.onEvent(event = DeveloperOptionsEvent.Load)
            advance()
            assertEquals(3, attempts)
            assertEquals(1, activeObservers)
            repository.settings.value = repository.defaults
            advance()
            assertEquals(repository.defaults, viewModel.state.value.settings)
            assertEquals(1, telemetry.viewModelErrors.size)
        }

    @Test
    fun `a non-retryable read failure keeps settings without offering Try again`() =
        runTest(context = dispatcherExtension.testDispatcher) {
            repository.settings.value = repository.defaults.copy(layoutMode = ShellLayoutMode.Rail)
            val preferences = object : ShellPreferencesRepository by repository {
                override val settings = flow<ShellSettings> {
                    throw StorageException(reason = StorageException.Reason.CORRUPT)
                }
            }

            val viewModel = DeveloperOptionsViewModel(
                preferences = preferences,
                telemetryRepository = telemetry,
            )
            advance()

            assertEquals(ShellLayoutMode.Rail, viewModel.state.value.settings.layoutMode)
            val message = viewModel.messages.value.single()
            assertTrue(message.isError)
            assertNull(message.actionLabel)
            assertEquals(1, telemetry.viewModelErrors.size)
        }

    private class TestPreferences : ShellPreferencesRepository {
        override val defaults = ShellSettings(hideTopBarOnScroll = true)
        override val settings = MutableStateFlow(value = defaults)
        override val lastKnown get() = settings.value
        var failure: Throwable? = null

        override suspend fun update(transform: (ShellSettings) -> ShellSettings) {
            failure?.let { throw it }
            settings.value = transform(settings.value)
        }
    }
}
