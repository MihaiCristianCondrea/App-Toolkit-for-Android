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

package com.mihaicristiancondrea.android.apps.apptoolkit.app.main.ui

import android.app.Activity
import com.mihaicristiancondrea.android.apps.apptoolkit.app.main.ui.contracts.MainEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.analytics.AnalyticsValue
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.platform.UiTextHelper
import com.mihaicristiancondrea.android.apps.apptoolkit.R
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakeTelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.StandardDispatcherExtension
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.data.repositories.ConsentRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.domain.models.ConsentHost
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.domain.models.ConsentSettings
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.review.domain.models.ReviewHost
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.review.domain.models.ReviewOutcome
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.review.domain.usecases.RequestInAppReviewUseCase
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.update.data.repositories.InAppUpdateRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.update.domain.models.InAppUpdateHost
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.update.domain.models.InAppUpdateResult
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MainViewModelTest {

    companion object {
        @JvmField
        @RegisterExtension
        val dispatcherExtension = StandardDispatcherExtension()
    }

    private val telemetryRepository = FakeTelemetryRepository()

    private val activity: Activity = mockk(relaxed = true)

    private val consentHost = ConsentHost(activity = activity)

    private val updateHost = InAppUpdateHost(activity = activity, updateResultLauncher = mockk(relaxed = true))

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    private fun createViewModel(
        consentRepository: ConsentRepository = CountingConsentRepository(),
        requestInAppReviewUseCase: RequestInAppReviewUseCase = mockk(relaxed = true),
        inAppUpdateRepository: InAppUpdateRepository = mockk(relaxed = true),
    ): MainViewModel = MainViewModel(
        consentRepository = consentRepository,
        requestInAppReviewUseCase = requestInAppReviewUseCase,
        inAppUpdateRepository = inAppUpdateRepository,
        telemetryRepository = telemetryRepository,
    )

    private fun advance() = dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()

    private fun startsOf(action: String): Int = telemetryRepository.loggedEvents.count { event ->
        event.name == "vm_op_start" && event.params["action"] == AnalyticsValue.Str(action)
    }

    @Test
    fun `initialization applies persisted consent once`() = runTest(dispatcherExtension.testDispatcher) {
        val consentRepository = CountingConsentRepository()

        createViewModel(consentRepository = consentRepository)
        advance()

        assertEquals(1, consentRepository.applyInitialCount)
    }

    @Test
    fun `review is requested once however many times the host asks`() =
        runTest(dispatcherExtension.testDispatcher) {
            val requestInAppReviewUseCase = mockk<RequestInAppReviewUseCase>()
            coEvery { requestInAppReviewUseCase(any()) } returns ReviewOutcome.NotEligible
            val host = object : ReviewHost {
                override val activity: Activity = this@MainViewModelTest.activity
            }
            val viewModel = createViewModel(requestInAppReviewUseCase = requestInAppReviewUseCase)

            repeat(times = 3) {
                viewModel.onEvent(MainEvent.RequestReview(host = host))
                advance()
            }

            coVerify(exactly = 1) { requestInAppReviewUseCase(host = host) }
            assertEquals(1, startsOf(action = "requestReview"))
        }

    @Test
    fun `requestConsent skips overlapping calls while one is in progress`() =
        runTest(dispatcherExtension.testDispatcher) {
            val consentRepository = CountingConsentRepository(request = { awaitCancellation() })
            val viewModel = createViewModel(consentRepository = consentRepository)

            viewModel.onEvent(MainEvent.RequestConsent(host = consentHost))
            viewModel.onEvent(MainEvent.RequestConsent(host = consentHost))
            dispatcherExtension.testDispatcher.scheduler.runCurrent()

            assertEquals(1, consentRepository.requestCount)
        }

    @Test
    fun `consent is requested once even after the first request has completed`() =
        runTest(dispatcherExtension.testDispatcher) {
            val consentRepository = CountingConsentRepository()
            val viewModel = createViewModel(consentRepository = consentRepository)

            repeat(times = 3) {
                viewModel.onEvent(MainEvent.RequestConsent(host = consentHost))
                advance()
            }

            assertEquals(1, consentRepository.requestCount)
        }

    @Test
    fun `a successful consent request shows nothing`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = createViewModel()

        viewModel.onEvent(MainEvent.RequestConsent(host = consentHost))
        advance()

        assertTrue(viewModel.messages.value.isEmpty())
    }

    @Test
    fun `a failed consent request shows the consent error and is reported`() =
        runTest(dispatcherExtension.testDispatcher) {
            val viewModel = createViewModel(
                consentRepository = CountingConsentRepository(request = { throw IllegalStateException("consent") }),
            )

            viewModel.onEvent(MainEvent.RequestConsent(host = consentHost))
            advance()

            val message = viewModel.messages.value.single()
            assertTrue(message.isError)
            assertEquals(
                R.string.error_failed_to_load_consent_info,
                (message.text as UiTextHelper.StringResource).resourceId,
            )
            assertTrue(telemetryRepository.loggedEvents.any { it.name == "vm_op_error" })
        }

    @Test
    fun `a cancelled consent request does not show or report an error`() =
        runTest(dispatcherExtension.testDispatcher) {
            val viewModel = createViewModel(
                consentRepository = CountingConsentRepository(request = { throw CancellationException("cancelled") }),
            )

            viewModel.onEvent(MainEvent.RequestConsent(host = consentHost))
            advance()

            assertTrue(viewModel.messages.value.isEmpty())
            assertTrue(telemetryRepository.loggedEvents.none { it.name == "vm_op_error" })
        }

    @Test
    fun `update check stops repeating once play gives a settled answer`() =
        runTest(dispatcherExtension.testDispatcher) {
            val inAppUpdateRepository = mockk<InAppUpdateRepository>()
            every { inAppUpdateRepository.requestUpdate(any()) } returns flowOf(InAppUpdateResult.NotAvailable)
            val viewModel = createViewModel(inAppUpdateRepository = inAppUpdateRepository)

            repeat(times = 3) {
                viewModel.onEvent(MainEvent.RequestInAppUpdate(host = updateHost))
                advance()
            }

            verify(exactly = 1) { inAppUpdateRepository.requestUpdate(host = updateHost) }
        }

    @Test
    fun `update check repeats while an immediate update may still need resuming`() =
        runTest(dispatcherExtension.testDispatcher) {
            val inAppUpdateRepository = mockk<InAppUpdateRepository>()
            every { inAppUpdateRepository.requestUpdate(any()) } returns flowOf(InAppUpdateResult.Started)
            val viewModel = createViewModel(inAppUpdateRepository = inAppUpdateRepository)

            repeat(times = 3) {
                viewModel.onEvent(MainEvent.RequestInAppUpdate(host = updateHost))
                advance()
            }

            verify(exactly = 3) { inAppUpdateRepository.requestUpdate(host = updateHost) }
        }

    @Test
    fun `a failed update check settles the session and is reported`() =
        runTest(dispatcherExtension.testDispatcher) {
            val inAppUpdateRepository = mockk<InAppUpdateRepository>()
            every { inAppUpdateRepository.requestUpdate(any()) } returns flow { throw IllegalStateException("play") }
            val viewModel = createViewModel(inAppUpdateRepository = inAppUpdateRepository)

            repeat(times = 2) {
                viewModel.onEvent(MainEvent.RequestInAppUpdate(host = updateHost))
                advance()
            }

            verify(exactly = 1) { inAppUpdateRepository.requestUpdate(host = updateHost) }
            assertTrue(telemetryRepository.loggedEvents.any { it.name == "vm_op_error" })
        }

    private class CountingConsentRepository(
        private val request: suspend () -> Unit = {},
    ) : ConsentRepository {
        var requestCount: Int = 0
            private set

        var applyInitialCount: Int = 0
            private set

        override suspend fun requestConsent(host: ConsentHost, showIfRequired: Boolean) {
            requestCount++
            request()
        }

        override suspend fun applyInitialConsent() {
            applyInitialCount++
        }

        override suspend fun applyConsentSettings(settings: ConsentSettings) = Unit
    }
}
