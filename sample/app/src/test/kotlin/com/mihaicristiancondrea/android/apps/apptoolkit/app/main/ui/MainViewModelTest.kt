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

import com.mihaicristiancondrea.android.apps.apptoolkit.app.main.ui.contracts.MainEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.TelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.network.domain.models.network.DataState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.network.domain.models.network.Errors
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.StandardDispatcherExtension
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.TestDispatchers
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.data.repositories.ConsentRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.domain.models.ConsentHost
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.domain.models.ConsentSettings
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.review.domain.models.ReviewHost
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.update.data.repositories.InAppUpdateRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.update.domain.models.InAppUpdateHost
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.update.domain.models.InAppUpdateResult
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.review.domain.models.ReviewOutcome
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.review.domain.usecases.RequestInAppReviewUseCase
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import kotlin.test.assertEquals

class MainViewModelTest {

    companion object {
        @JvmField
        @RegisterExtension
        val dispatcherExtension = StandardDispatcherExtension()
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `review is requested once however many times the host asks`() =
        runTest(dispatcherExtension.testDispatcher) {
            // The host sends this from onResume, so it arrives again on every return from another
            // activity. The use case records a session per call, so answering each one would count
            // resumes as sessions and bring the prompt forward.
            val requestInAppReviewUseCase = mockk<RequestInAppReviewUseCase>(relaxed = true)
            coEvery { requestInAppReviewUseCase(any()) } returns ReviewOutcome.NotEligible
            val host = object : ReviewHost {
                override val activity: android.app.Activity = mockk(relaxed = true)
            }

            val viewModel = MainViewModel(
                consentRepository = FakeConsentRepository(),
                requestInAppReviewUseCase = requestInAppReviewUseCase,
                inAppUpdateRepository = mockk(relaxed = true),
                telemetryRepository = mockk<TelemetryRepository>(relaxed = true),
                dispatchers = TestDispatchers(testDispatcher = dispatcherExtension.testDispatcher),
            )

            repeat(times = 3) {
                viewModel.onEvent(event = MainEvent.RequestReview(host = host))
                runCurrent()
                advanceUntilIdle()
            }

            coVerify(exactly = 1) { requestInAppReviewUseCase(host = host) }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `initialization applies persisted consent once`() =
        runTest(dispatcherExtension.testDispatcher) {
            val consentRepository = mockk<ConsentRepository>(relaxed = true)

            MainViewModel(
                consentRepository = consentRepository,
                requestInAppReviewUseCase = mockk(relaxed = true),
                inAppUpdateRepository = mockk(relaxed = true),
                telemetryRepository = mockk<TelemetryRepository>(relaxed = true),
                dispatchers = TestDispatchers(testDispatcher = dispatcherExtension.testDispatcher),
            )

            runCurrent()
            advanceUntilIdle()

            coVerify(exactly = 1) { consentRepository.applyInitialConsent() }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `requestConsent skips overlapping calls while one is in progress`() =
        runTest(dispatcherExtension.testDispatcher) {
            val telemetryRepository = mockk<TelemetryRepository>(relaxed = true)
            val consentRepository = CountingConsentRepository(
                upstream = flow {
                    emit(DataState.Loading())
                    awaitCancellation()
                }
            )

            val viewModel = MainViewModel(
                consentRepository = consentRepository,
                requestInAppReviewUseCase = mockk(relaxed = true),
                inAppUpdateRepository = mockk(relaxed = true),
                telemetryRepository = telemetryRepository,
                dispatchers = TestDispatchers(dispatcherExtension.testDispatcher),
            )

            val host = object : ConsentHost {
                override val activity = mockk<android.app.Activity>(relaxed = true)
            }
            viewModel.onEvent(MainEvent.RequestConsent(host = host))
            viewModel.onEvent(MainEvent.RequestConsent(host = host))

            runCurrent()

            assertEquals(1, consentRepository.callCount)
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `consent is requested once even after the first request has completed`() =
        runTest(dispatcherExtension.testDispatcher) {
            // The in-flight guard does not cover this: the host resumes, the previous round trip has
            // already finished, and without a session guard a fresh UMP request starts every time.
            val consentRepository = CountingConsentRepository(
                upstream = flowOf(DataState.Success<Unit, Errors.UseCase>(Unit))
            )

            val viewModel = MainViewModel(
                consentRepository = consentRepository,
                requestInAppReviewUseCase = mockk(relaxed = true),
                inAppUpdateRepository = mockk(relaxed = true),
                telemetryRepository = mockk<TelemetryRepository>(relaxed = true),
                dispatchers = TestDispatchers(dispatcherExtension.testDispatcher),
            )

            val host = object : ConsentHost {
                override val activity = mockk<android.app.Activity>(relaxed = true)
            }

            repeat(times = 3) {
                viewModel.onEvent(MainEvent.RequestConsent(host = host))
                runCurrent()
                advanceUntilIdle()
            }

            assertEquals(1, consentRepository.callCount)
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `update check stops repeating once play gives a settled answer`() =
        runTest(dispatcherExtension.testDispatcher) {
            val inAppUpdateRepository = mockk<InAppUpdateRepository>(relaxed = true)
            every { inAppUpdateRepository.requestUpdate(any()) } returns
                    flowOf(InAppUpdateResult.NotAvailable)

            val viewModel = MainViewModel(
                consentRepository = FakeConsentRepository(),
                requestInAppReviewUseCase = mockk(relaxed = true),
                inAppUpdateRepository = inAppUpdateRepository,
                telemetryRepository = mockk<TelemetryRepository>(relaxed = true),
                dispatchers = TestDispatchers(dispatcherExtension.testDispatcher),
            )

            val host = InAppUpdateHost(
                activity = mockk(relaxed = true),
                updateResultLauncher = mockk(relaxed = true),
            )

            repeat(times = 3) {
                viewModel.onEvent(MainEvent.RequestInAppUpdate(host = host))
                runCurrent()
                advanceUntilIdle()
            }

            verify(exactly = 1) { inAppUpdateRepository.requestUpdate(host = host) }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `update check repeats while an immediate update may still need resuming`() =
        runTest(dispatcherExtension.testDispatcher) {
            // Started means the immediate update flow was launched. Backgrounding the app mid-update
            // and returning is how Play expects that update to be resumed, so the check has to run
            // again on the next resume rather than being guarded away.
            val inAppUpdateRepository = mockk<InAppUpdateRepository>(relaxed = true)
            every { inAppUpdateRepository.requestUpdate(any()) } returns
                    flowOf(InAppUpdateResult.Started)

            val viewModel = MainViewModel(
                consentRepository = FakeConsentRepository(),
                requestInAppReviewUseCase = mockk(relaxed = true),
                inAppUpdateRepository = inAppUpdateRepository,
                telemetryRepository = mockk<TelemetryRepository>(relaxed = true),
                dispatchers = TestDispatchers(dispatcherExtension.testDispatcher),
            )

            val host = InAppUpdateHost(
                activity = mockk(relaxed = true),
                updateResultLauncher = mockk(relaxed = true),
            )

            repeat(times = 3) {
                viewModel.onEvent(MainEvent.RequestInAppUpdate(host = host))
                runCurrent()
                advanceUntilIdle()
            }

            verify(exactly = 3) { inAppUpdateRepository.requestUpdate(host = host) }
        }
}

private class FakeConsentRepository : ConsentRepository {
    override fun requestConsent(
        host: ConsentHost,
        showIfRequired: Boolean,
    ) = flowOf(DataState.Success<Unit, Errors.UseCase>(Unit))

    override suspend fun applyInitialConsent() = Unit

    override suspend fun applyConsentSettings(settings: ConsentSettings) = Unit
}

private class CountingConsentRepository(
    private val upstream: Flow<DataState<Unit, Errors.UseCase>>,
) : ConsentRepository {
    var callCount: Int = 0
        private set

    override fun requestConsent(
        host: ConsentHost,
        showIfRequired: Boolean,
    ): Flow<DataState<Unit, Errors.UseCase>> {
        callCount++
        return upstream
    }

    override suspend fun applyInitialConsent() = Unit

    override suspend fun applyConsentSettings(settings: ConsentSettings) = Unit
}
