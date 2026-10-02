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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.startup.ui

import android.app.Activity
import com.mihaicristiancondrea.android.libs.apptoolkit.core.network.domain.models.network.DataState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.network.domain.models.network.Errors
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakeTelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.UnconfinedDispatcherExtension
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.startup.ui.contracts.StartupEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.startup.ui.states.ConsentRequestStatus
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.data.repositories.ConsentRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.domain.models.ConsentHost
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.domain.models.ConsentSettings
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalCoroutinesApi::class)
class StartupViewModelTest {

    companion object {
        @JvmField
        @RegisterExtension
        val dispatcherExtension = UnconfinedDispatcherExtension()
    }

    private val telemetryRepository = FakeTelemetryRepository()

    private val host = object : ConsentHost {
        override val activity: Activity get() = error("The fake consent repository never reads the activity")
    }

    private fun createViewModel(consentRepository: ConsentRepository): StartupViewModel =
        StartupViewModel(consentRepository = consentRepository, telemetryRepository = telemetryRepository)

    private fun advance() = dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()

    @Test
    fun `consent is pending before the first resume`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = createViewModel(FakeConsentRepository { answered(DataState.Success(Unit)) })
        advance()

        assertEquals(ConsentRequestStatus.Pending, viewModel.state.value.consent)
    }

    @Test
    fun `a consent answer settles consent`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = createViewModel(FakeConsentRepository { answered(DataState.Success(Unit)) })

        viewModel.onEvent(StartupEvent.RequestConsent(host = host))
        advance()

        assertEquals(ConsentRequestStatus.Settled, viewModel.state.value.consent)
    }

    @Test
    fun `a failed consent form still settles consent`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = createViewModel(
            FakeConsentRepository { answered(DataState.Error(error = Errors.UseCase.FAILED_TO_LOAD_CONSENT_INFO)) }
        )

        viewModel.onEvent(StartupEvent.RequestConsent(host = host))
        advance()

        assertEquals(ConsentRequestStatus.Settled, viewModel.state.value.consent)
    }

    @Test
    fun `a consent request that throws is reported and settles consent`() =
        runTest(dispatcherExtension.testDispatcher) {
            val viewModel = createViewModel(
                FakeConsentRepository { flow<DataState<Unit, Errors.UseCase>> { throw IllegalStateException("ump") } }
            )

            viewModel.onEvent(StartupEvent.RequestConsent(host = host))
            advance()

            assertEquals(ConsentRequestStatus.Settled, viewModel.state.value.consent)
            assertTrue(telemetryRepository.loggedEvents.any { it.name == "vm_op_error" })
        }

    /**
     * The startup screen hides its only button until consent settles, so a consent round trip that
     * never reports back has to stop mattering at some point.
     */
    @Test
    fun `consent settles when the request never reports back`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = createViewModel(FakeConsentRepository { neverAnswered() })

        viewModel.onEvent(StartupEvent.RequestConsent(host = host))
        advanceTimeBy(1.seconds)

        assertEquals(ConsentRequestStatus.Pending, viewModel.state.value.consent)

        advance()

        assertEquals(ConsentRequestStatus.Settled, viewModel.state.value.consent)
    }

    @Test
    fun `a resume while waiting asks again`() = runTest(dispatcherExtension.testDispatcher) {
        val repository = FakeConsentRepository { neverAnswered() }
        val viewModel = createViewModel(repository)

        viewModel.onEvent(StartupEvent.RequestConsent(host = host))
        advanceTimeBy(1.seconds)
        repository.answer = { answered(DataState.Success(Unit)) }
        viewModel.onEvent(StartupEvent.RequestConsent(host = host))
        advanceTimeBy(1.seconds)

        assertEquals(2, repository.requests)
        assertEquals(ConsentRequestStatus.Settled, viewModel.state.value.consent)
    }

    @Test
    fun `a resume after consent settled asks for nothing`() = runTest(dispatcherExtension.testDispatcher) {
        val repository = FakeConsentRepository { answered(DataState.Success(Unit)) }
        val viewModel = createViewModel(repository)
        viewModel.onEvent(StartupEvent.RequestConsent(host = host))
        advance()

        viewModel.onEvent(StartupEvent.RequestConsent(host = host))
        advance()

        assertEquals(1, repository.requests)
        assertEquals(ConsentRequestStatus.Settled, viewModel.state.value.consent)
    }

    @Test
    fun `without an activity consent settles at once`() = runTest(dispatcherExtension.testDispatcher) {
        val repository = FakeConsentRepository { neverAnswered() }
        val viewModel = createViewModel(repository)

        viewModel.onEvent(StartupEvent.RequestConsent(host = null))

        assertEquals(0, repository.requests)
        assertEquals(ConsentRequestStatus.Settled, viewModel.state.value.consent)
    }

    private fun answered(result: DataState<Unit, Errors.UseCase>): Flow<DataState<Unit, Errors.UseCase>> =
        flowOf<DataState<Unit, Errors.UseCase>>(DataState.Loading(), result)

    private fun neverAnswered(): Flow<DataState<Unit, Errors.UseCase>> = flow<DataState<Unit, Errors.UseCase>> {
        emit(DataState.Loading())
        awaitCancellation()
    }

    private class FakeConsentRepository(
        var answer: () -> Flow<DataState<Unit, Errors.UseCase>>,
    ) : ConsentRepository {
        var requests: Int = 0

        override fun requestConsent(
            host: ConsentHost,
            showIfRequired: Boolean,
        ): Flow<DataState<Unit, Errors.UseCase>> {
            requests += 1
            return answer()
        }

        override suspend fun applyInitialConsent() = Unit

        override suspend fun applyConsentSettings(settings: ConsentSettings) = Unit
    }
}
