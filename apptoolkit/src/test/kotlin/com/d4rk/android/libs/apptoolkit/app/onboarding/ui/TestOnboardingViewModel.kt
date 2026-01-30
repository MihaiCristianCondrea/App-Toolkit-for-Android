package com.d4rk.android.libs.apptoolkit.app.onboarding.ui

import com.d4rk.android.libs.apptoolkit.app.consent.domain.model.ConsentHost
import com.d4rk.android.libs.apptoolkit.app.consent.domain.repository.ConsentRepository
import com.d4rk.android.libs.apptoolkit.app.consent.domain.usecases.RequestConsentUseCase
import com.d4rk.android.libs.apptoolkit.app.onboarding.domain.repository.OnboardingRepository
import com.d4rk.android.libs.apptoolkit.app.onboarding.domain.usecases.CompleteOnboardingUseCase
import com.d4rk.android.libs.apptoolkit.app.onboarding.domain.usecases.ObserveOnboardingCompletionUseCase
import com.d4rk.android.libs.apptoolkit.app.onboarding.ui.contract.OnboardingEvent
import com.d4rk.android.libs.apptoolkit.core.di.TestDispatchers
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.DataState
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.Errors
import com.d4rk.android.libs.apptoolkit.core.utils.FakeFirebaseController
import com.d4rk.android.libs.apptoolkit.core.utils.dispatchers.UnconfinedDispatcherExtension
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
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
class TestOnboardingViewModel {

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
            observeOnboardingCompletionUseCase = ObserveOnboardingCompletionUseCase(repository),
            completeOnboardingUseCase = CompleteOnboardingUseCase(repository),
            requestConsentUseCase = RequestConsentUseCase(FakeConsentRepository()),
            dispatchers = TestDispatchers(testDispatcher = dispatcherExtension.testDispatcher),
            firebaseController = firebaseController,
        )
}

private class FakeConsentRepository : ConsentRepository {
    override fun requestConsent(
        host: ConsentHost,
        showIfRequired: Boolean,
    ): Flow<DataState<Unit, Errors.UseCase>> = flowOf(DataState.Success(Unit))
}
