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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.diagnostics.ui

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.exceptions.StorageException
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.analytics.AnalyticsValue
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.platform.UiTextHelper
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakeTelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.UnconfinedDispatcherExtension
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.R as CoreUiR
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.Loadable
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.UiMessage
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.diagnostics.R
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.diagnostics.data.repositories.UsageAndDiagnosticsRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.diagnostics.domain.models.UsageAndDiagnosticsSettings
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.diagnostics.ui.contracts.UsageAndDiagnosticsEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

class UsageAndDiagnosticsViewModelTest {

    companion object {
        @JvmField
        @RegisterExtension
        val dispatcherExtension = UnconfinedDispatcherExtension()
    }

    private val stored = UsageAndDiagnosticsSettings(
        usageAndDiagnostics = false,
        analyticsConsent = false,
        adStorageConsent = false,
        adUserDataConsent = false,
        adPersonalizationConsent = false,
    )

    private val telemetryRepository = FakeTelemetryRepository()

    private fun createViewModel(repository: UsageAndDiagnosticsRepository): UsageAndDiagnosticsViewModel =
        UsageAndDiagnosticsViewModel(repository = repository, telemetryRepository = telemetryRepository)

    private fun advance() = dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()

    private fun UsageAndDiagnosticsViewModel.readySettings(): UsageAndDiagnosticsSettings =
        assertIs<Loadable.Ready<UsageAndDiagnosticsSettings>>(state.value.settings).value

    private val UiMessage.resourceId: Int
        get() = (text as UiTextHelper.StringResource).resourceId

    @Test
    fun `initial load shows the stored choices`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = createViewModel(FakeUsageAndDiagnosticsRepository(stored))
        advance()

        assertEquals(stored, viewModel.readySettings())
    }

    @Test
    fun `a failed read shows a retryable failure with the screen's text`() =
        runTest(dispatcherExtension.testDispatcher) {
            val repository = FakeUsageAndDiagnosticsRepository(stored, readFailure = IllegalStateException("bug"))
            val viewModel = createViewModel(repository)
            advance()

            val failed = assertIs<Loadable.Failed>(viewModel.state.value.settings)
            assertTrue(failed.retryable)
            assertEquals(R.string.error_an_error_occurred, (failed.message as UiTextHelper.StringResource).resourceId)
            assertTrue(
                telemetryRepository.loggedEvents.any { event ->
                    event.name == "vm_op_error" && event.params["action"] == AnalyticsValue.Str("observeConsents")
                }
            )
        }

    @Test
    fun `a corrupt store shows a failure without a retry`() = runTest(dispatcherExtension.testDispatcher) {
        val repository = FakeUsageAndDiagnosticsRepository(
            stored,
            readFailure = StorageException(reason = StorageException.Reason.CORRUPT),
        )
        val viewModel = createViewModel(repository)
        advance()

        assertFalse(assertIs<Loadable.Failed>(viewModel.state.value.settings).retryable)
    }

    @Test
    fun `retrying after a failed read shows the stored choices`() = runTest(dispatcherExtension.testDispatcher) {
        val repository = FakeUsageAndDiagnosticsRepository(stored, readFailure = IllegalStateException("bug"))
        val viewModel = createViewModel(repository)
        advance()

        repository.readFailure = null
        viewModel.onEvent(UsageAndDiagnosticsEvent.Load)
        advance()

        assertEquals(stored, viewModel.readySettings())
    }

    @Test
    fun `the reporting switch stores the choice and the screen follows it`() =
        runTest(dispatcherExtension.testDispatcher) {
            val viewModel = createViewModel(FakeUsageAndDiagnosticsRepository(stored))
            advance()

            viewModel.onEvent(UsageAndDiagnosticsEvent.SetUsageAndDiagnostics(enabled = true))
            advance()

            assertTrue(viewModel.readySettings().usageAndDiagnostics)
        }

    @Test
    fun `each consent switch stores only its own choice`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = createViewModel(FakeUsageAndDiagnosticsRepository(stored))
        advance()

        viewModel.onEvent(UsageAndDiagnosticsEvent.SetAnalyticsConsent(granted = true))
        viewModel.onEvent(UsageAndDiagnosticsEvent.SetAdStorageConsent(granted = true))
        viewModel.onEvent(UsageAndDiagnosticsEvent.SetAdUserDataConsent(granted = true))
        viewModel.onEvent(UsageAndDiagnosticsEvent.SetAdPersonalizationConsent(granted = true))
        advance()

        assertEquals(
            stored.copy(
                analyticsConsent = true,
                adStorageConsent = true,
                adUserDataConsent = true,
                adPersonalizationConsent = true,
            ),
            viewModel.readySettings(),
        )
    }

    @Test
    fun `allow all stores every consent and turns reporting on in one write`() =
        runTest(dispatcherExtension.testDispatcher) {
            val repository = FakeUsageAndDiagnosticsRepository(stored)
            val viewModel = createViewModel(repository)
            advance()

            viewModel.onEvent(UsageAndDiagnosticsEvent.AllowAllConsent)
            advance()

            val expected = UsageAndDiagnosticsSettings(
                usageAndDiagnostics = true,
                analyticsConsent = true,
                adStorageConsent = true,
                adUserDataConsent = true,
                adPersonalizationConsent = true,
            )
            assertEquals(listOf(expected), repository.bundles)
            assertEquals(expected, viewModel.readySettings())
        }

    @Test
    fun `allow essentials refuses the targeting consents`() = runTest(dispatcherExtension.testDispatcher) {
        val repository = FakeUsageAndDiagnosticsRepository(stored)
        val viewModel = createViewModel(repository)
        advance()

        viewModel.onEvent(UsageAndDiagnosticsEvent.AllowEssentialConsent)
        advance()

        assertEquals(
            listOf(
                UsageAndDiagnosticsSettings(
                    usageAndDiagnostics = true,
                    analyticsConsent = true,
                    adStorageConsent = true,
                    adUserDataConsent = false,
                    adPersonalizationConsent = false,
                )
            ),
            repository.bundles,
        )
    }

    @Test
    fun `a failed write keeps the choices and shows the screen's error`() =
        runTest(dispatcherExtension.testDispatcher) {
            val repository = FakeUsageAndDiagnosticsRepository(stored, writeFailure = IllegalStateException("bug"))
            val viewModel = createViewModel(repository)
            advance()

            viewModel.onEvent(UsageAndDiagnosticsEvent.SetUsageAndDiagnostics(enabled = true))
            advance()

            assertEquals(stored, viewModel.readySettings())
            val message = viewModel.messages.value.single()
            assertTrue(message.isError)
            assertEquals(R.string.error_an_error_occurred, message.resourceId)
        }

    @Test
    fun `a failed bundle write on full storage says the storage is full`() =
        runTest(dispatcherExtension.testDispatcher) {
            val repository = FakeUsageAndDiagnosticsRepository(
                stored,
                writeFailure = StorageException(reason = StorageException.Reason.FULL),
            )
            val viewModel = createViewModel(repository)
            advance()

            viewModel.onEvent(UsageAndDiagnosticsEvent.AllowAllConsent)
            advance()

            assertEquals(CoreUiR.string.screen_error_storage_full, viewModel.messages.value.single().resourceId)
        }

    @Test
    fun `a shown message leaves the queue`() = runTest(dispatcherExtension.testDispatcher) {
        val repository = FakeUsageAndDiagnosticsRepository(stored, writeFailure = IllegalStateException("bug"))
        val viewModel = createViewModel(repository)
        advance()
        viewModel.onEvent(UsageAndDiagnosticsEvent.SetAnalyticsConsent(granted = true))
        advance()

        viewModel.messageShown(viewModel.messages.value.single().id)

        assertTrue(viewModel.messages.value.isEmpty())
    }

    private class FakeUsageAndDiagnosticsRepository(
        initial: UsageAndDiagnosticsSettings,
        var readFailure: Throwable? = null,
        private val writeFailure: Throwable? = null,
    ) : UsageAndDiagnosticsRepository {
        private val settings = MutableStateFlow(initial)
        val bundles: MutableList<UsageAndDiagnosticsSettings> = mutableListOf()

        override fun observeSettings(): Flow<UsageAndDiagnosticsSettings> {
            val failure = readFailure ?: return settings
            return flow { throw failure }
        }

        override suspend fun setUsageAndDiagnostics(enabled: Boolean) =
            write { copy(usageAndDiagnostics = enabled) }

        override suspend fun setAnalyticsConsent(granted: Boolean) = write { copy(analyticsConsent = granted) }

        override suspend fun setAdStorageConsent(granted: Boolean) = write { copy(adStorageConsent = granted) }

        override suspend fun setAdUserDataConsent(granted: Boolean) = write { copy(adUserDataConsent = granted) }

        override suspend fun setAdPersonalizationConsent(granted: Boolean) =
            write { copy(adPersonalizationConsent = granted) }

        override suspend fun setAll(settings: UsageAndDiagnosticsSettings) {
            writeFailure?.let { throw it }
            bundles += settings
            this.settings.value = settings
        }

        private fun write(change: UsageAndDiagnosticsSettings.() -> UsageAndDiagnosticsSettings) {
            writeFailure?.let { throw it }
            settings.update(change)
        }
    }
}
