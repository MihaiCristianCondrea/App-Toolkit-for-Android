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

import android.app.Activity
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.exceptions.StorageException
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.platform.UiTextHelper
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakeTelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.UnconfinedDispatcherExtension
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.R as CoreUiR
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.UiMessage
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.R
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.data.repositories.OnboardingRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.ui.contracts.OnboardingEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.ui.states.OnboardingCompletion
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.data.repositories.ConsentRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.domain.models.ConsentHost
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.domain.models.ConsentSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertSame
import kotlin.test.assertTrue

class OnboardingViewModelTest {

    companion object {
        @JvmField
        @RegisterExtension
        val dispatcherExtension = UnconfinedDispatcherExtension()
    }

    private val telemetryRepository = FakeTelemetryRepository()

    private val host = object : ConsentHost {
        override val activity: Activity get() = error("The fake consent repository never reads the activity")
    }

    private fun createViewModel(
        repository: OnboardingRepository = FakeOnboardingRepository(),
        consentRepository: ConsentRepository = FakeConsentRepository(),
    ): OnboardingViewModel = OnboardingViewModel(
        onboardingRepository = repository,
        consentRepository = consentRepository,
        telemetryRepository = telemetryRepository,
    )

    private fun advance() = dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()

    private val loggedEventNames: List<String>
        get() = telemetryRepository.loggedEvents.map { it.name }

    private val UiMessage.resourceId: Int
        get() = (text as UiTextHelper.StringResource).resourceId

    @Test
    fun `the first load shows the first page, pending, and begins the tutorial`() =
        runTest(dispatcherExtension.testDispatcher) {
            val viewModel = createViewModel()
            advance()

            val state = viewModel.state.value
            assertEquals(0, state.currentTabIndex)
            assertEquals(OnboardingCompletion.Pending, state.completion)
            assertFalse(state.isOnboardingCompleted)
            assertTrue("tutorial_begin" in loggedEventNames)
        }

    @Test
    fun `the stored completion flag reaches the state`() = runTest(dispatcherExtension.testDispatcher) {
        val repository = FakeOnboardingRepository()
        val viewModel = createViewModel(repository = repository)
        advance()

        repository.completion.value = true
        advance()

        assertTrue(viewModel.state.value.isOnboardingCompleted)
        assertEquals(OnboardingCompletion.Pending, viewModel.state.value.completion)
    }

    @Test
    fun `a failed completion read is reported and reads as not completed`() =
        runTest(dispatcherExtension.testDispatcher) {
            val viewModel = createViewModel(repository = FailingObservationRepository())
            advance()

            assertFalse(viewModel.state.value.isOnboardingCompleted)
            assertTrue("vm_op_error" in loggedEventNames)
        }

    @Test
    fun `selecting a page keeps its index`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = createViewModel()
        advance()

        viewModel.onEvent(OnboardingEvent.PageSelected(index = 3))

        assertEquals(3, viewModel.state.value.currentTabIndex)
    }

    @Test
    fun `completing saves it, logs tutorial_complete and asks to enter the shell`() =
        runTest(dispatcherExtension.testDispatcher) {
            val repository = FakeOnboardingRepository()
            val viewModel = createViewModel(repository = repository)
            advance()

            viewModel.onEvent(OnboardingEvent.CompleteOnboarding)
            advance()

            assertTrue(repository.completion.value)
            assertEquals(OnboardingCompletion.Saved, viewModel.state.value.completion)
            assertTrue("tutorial_complete" in loggedEventNames)
            assertTrue(viewModel.messages.value.isEmpty())
        }

    /** Finishing is the only way out of onboarding, so a failure has to say something. */
    @Test
    fun `a failed save shows the onboarding error and stays`() = runTest(dispatcherExtension.testDispatcher) {
        val repository = FakeOnboardingRepository(failure = IllegalStateException("bug"))
        val viewModel = createViewModel(repository = repository)
        advance()

        viewModel.onEvent(OnboardingEvent.CompleteOnboarding)
        advance()

        val message = viewModel.messages.value.single()
        assertEquals(R.string.onboarding_completion_failed, message.resourceId)
        assertTrue(message.isError)
        assertEquals(OnboardingCompletion.Failed, viewModel.state.value.completion)
        assertFalse(repository.completion.value)
        assertFalse("tutorial_complete" in loggedEventNames)
        assertTrue("vm_op_error" in loggedEventNames)
    }

    @Test
    fun `a full disk shows the storage text`() = runTest(dispatcherExtension.testDispatcher) {
        val repository = FakeOnboardingRepository(failure = StorageException(StorageException.Reason.FULL))
        val viewModel = createViewModel(repository = repository)
        advance()

        viewModel.onEvent(OnboardingEvent.CompleteOnboarding)
        advance()

        assertEquals(CoreUiR.string.screen_error_storage_full, viewModel.messages.value.single().resourceId)
    }

    @Test
    fun `finishing again after a failed save completes`() = runTest(dispatcherExtension.testDispatcher) {
        val repository = FakeOnboardingRepository(failure = IllegalStateException("bug"))
        val viewModel = createViewModel(repository = repository)
        advance()
        viewModel.onEvent(OnboardingEvent.CompleteOnboarding)
        advance()
        viewModel.messageShown(viewModel.messages.value.single().id)

        repository.failure = null
        viewModel.onEvent(OnboardingEvent.CompleteOnboarding)
        advance()

        assertEquals(OnboardingCompletion.Saved, viewModel.state.value.completion)
        assertTrue(viewModel.messages.value.isEmpty())
    }

    @Test
    fun `requesting consent asks the consent repository with the host`() =
        runTest(dispatcherExtension.testDispatcher) {
            val consentRepository = FakeConsentRepository()
            val viewModel = createViewModel(consentRepository = consentRepository)
            advance()

            viewModel.onEvent(OnboardingEvent.RequestConsent(host = host))
            advance()

            assertSame(host, consentRepository.requestedHosts.single())
            assertEquals(OnboardingCompletion.Pending, viewModel.state.value.completion)
        }

    @Test
    fun `a failed consent request is reported and changes nothing on screen`() =
        runTest(dispatcherExtension.testDispatcher) {
            val viewModel = createViewModel(consentRepository = FakeConsentRepository(fails = true))
            advance()
            val before = viewModel.state.value

            viewModel.onEvent(OnboardingEvent.RequestConsent(host = host))
            advance()

            assertEquals(before, viewModel.state.value)
            assertTrue(viewModel.messages.value.isEmpty())
            assertTrue("vm_op_error" in loggedEventNames)
        }

    private class FakeOnboardingRepository(var failure: Throwable? = null) : OnboardingRepository {
        val completion = MutableStateFlow(false)

        override fun observeOnboardingCompletion(): Flow<Boolean> = completion

        override suspend fun setOnboardingCompleted() {
            failure?.let { throw it }
            completion.value = true
        }
    }

    private class FailingObservationRepository : OnboardingRepository {
        override fun observeOnboardingCompletion(): Flow<Boolean> = flow {
            emit(true)
            throw IllegalStateException("boom")
        }

        override suspend fun setOnboardingCompleted() = Unit
    }

    private class FakeConsentRepository(private val fails: Boolean = false) : ConsentRepository {
        val requestedHosts = mutableListOf<ConsentHost>()

        override suspend fun requestConsent(
            host: ConsentHost,
            showIfRequired: Boolean,
        ) {
            requestedHosts += host
            if (fails) throw IllegalStateException("consent")
        }

        override suspend fun applyInitialConsent() = Unit

        override suspend fun applyConsentSettings(settings: ConsentSettings) = Unit
    }
}
