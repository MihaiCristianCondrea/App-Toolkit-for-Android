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

package com.mihaicristiancondrea.android.libs.apptoolkit.app.onboarding.ui

import com.google.common.truth.Truth.assertThat
import com.mihaicristiancondrea.android.libs.apptoolkit.app.onboarding.data.repositories.OnboardingRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.app.onboarding.ui.contracts.OnboardingEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakeFirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.TestDispatchers
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.UnconfinedDispatcherExtension
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

private class FakeOnboardingRepository : OnboardingRepository {
    var completed = false
    var shouldFail = false
    private val completion = MutableStateFlow(false)

    override fun observeOnboardingCompletion(): Flow<Boolean> = completion

    override suspend fun setOnboardingCompleted() {
        if (shouldFail) throw RuntimeException("fail")
        completed = true
        completion.value = true
    }

    suspend fun emit(value: Boolean) {
        completion.emit(value)
    }
}

private class FailingOnboardingRepository : OnboardingRepository {
    override fun observeOnboardingCompletion(): Flow<Boolean> = flow {
        emit(true)
        throw IllegalStateException("boom")
    }

    override suspend fun setOnboardingCompleted() = Unit
}

@OptIn(ExperimentalCoroutinesApi::class)
class OnboardingViewModelTest {

    companion object {
        @JvmField
        @RegisterExtension
        val dispatcherExtension = UnconfinedDispatcherExtension()
    }

    private val firebaseController = FakeFirebaseController()

    @Test
    fun `initial state is not completed`() = runTest(dispatcherExtension.testDispatcher) {
        val repository = FakeOnboardingRepository()
        val viewModel = createViewModel(repository)
        assertThat(viewModel.uiState.value.data?.isOnboardingCompleted).isFalse()
    }

    @Test
    fun `current tab index mutates as expected`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = createViewModel(FakeOnboardingRepository())

        // Default value
        assertThat(viewModel.uiState.value.data?.currentTabIndex).isEqualTo(0)

        // Changing the index updates the state
        viewModel.onEvent(OnboardingEvent.UpdateCurrentTab(1))
        assertThat(viewModel.uiState.value.data?.currentTabIndex).isEqualTo(1)

        // Negative values are also accepted
        viewModel.onEvent(OnboardingEvent.UpdateCurrentTab(-1))
        assertThat(viewModel.uiState.value.data?.currentTabIndex).isEqualTo(-1)

        // Extremely large values do not break the model
        viewModel.onEvent(OnboardingEvent.UpdateCurrentTab(Int.MAX_VALUE))
        assertThat(viewModel.uiState.value.data?.currentTabIndex).isEqualTo(Int.MAX_VALUE)

        // Reset back to default
        viewModel.onEvent(OnboardingEvent.UpdateCurrentTab(0))
        assertThat(viewModel.uiState.value.data?.currentTabIndex).isEqualTo(0)
    }

    @Test
    fun `repeated index changes remain stable`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = createViewModel(FakeOnboardingRepository())

        repeat(5) { index ->
            viewModel.onEvent(OnboardingEvent.UpdateCurrentTab(index))
        }

        assertThat(viewModel.uiState.value.data?.currentTabIndex).isEqualTo(4)

        viewModel.onEvent(OnboardingEvent.UpdateCurrentTab(0))
        assertThat(viewModel.uiState.value.data?.currentTabIndex).isEqualTo(0)
    }

    @Test
    fun `repository completion updates state`() = runTest(dispatcherExtension.testDispatcher) {
        val repository = FakeOnboardingRepository()
        val viewModel = createViewModel(repository)

        repository.emit(true)
        advanceUntilIdle()

        assertThat(viewModel.uiState.value.data?.isOnboardingCompleted).isTrue()
    }

    @Test
    fun `repository failure resets completion state via onCompletion`() =
        runTest(dispatcherExtension.testDispatcher) {
            val viewModel = createViewModel(FailingOnboardingRepository())

            advanceUntilIdle()

            assertThat(viewModel.uiState.value.data?.isOnboardingCompleted).isFalse()
        }

    @Test
    fun `completeOnboarding sets completion state`() = runTest(dispatcherExtension.testDispatcher) {
        val repository = FakeOnboardingRepository()
        val viewModel = createViewModel(repository)

        viewModel.onEvent(OnboardingEvent.CompleteOnboarding)
        advanceUntilIdle()

        assertThat(repository.completed).isTrue()
        assertThat(viewModel.uiState.value.data?.isOnboardingCompleted).isTrue()
    }

    @Test
    fun `completeOnboarding failure resets completion`() =
        runTest(dispatcherExtension.testDispatcher) {
            val repository = FakeOnboardingRepository().apply { shouldFail = true }
            val viewModel = createViewModel(repository)

            viewModel.onEvent(OnboardingEvent.CompleteOnboarding)
            advanceUntilIdle()

            assertThat(repository.completed).isFalse()
            assertThat(viewModel.uiState.value.data?.isOnboardingCompleted).isFalse()
        }

    @Test
    fun `crashlytics dialog visibility toggles`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = createViewModel(FakeOnboardingRepository())

        assertThat(viewModel.uiState.value.data?.isCrashlyticsDialogVisible).isTrue()

        viewModel.onEvent(OnboardingEvent.HideCrashlyticsDialog)
        assertThat(viewModel.uiState.value.data?.isCrashlyticsDialogVisible).isFalse()

        viewModel.onEvent(OnboardingEvent.ShowCrashlyticsDialog)
        assertThat(viewModel.uiState.value.data?.isCrashlyticsDialogVisible).isTrue()
    }

    private fun createViewModel(repository: OnboardingRepository): OnboardingViewModel =
        OnboardingViewModel(
            onboardingRepository = repository,
            dispatchers = TestDispatchers(testDispatcher = dispatcherExtension.testDispatcher),
            firebaseController = firebaseController,
        )
}
