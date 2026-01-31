package com.d4rk.android.libs.apptoolkit.app.startup.ui

import com.d4rk.android.libs.apptoolkit.app.consent.domain.model.ConsentHost
import com.d4rk.android.libs.apptoolkit.app.consent.domain.repository.ConsentRepository
import com.d4rk.android.libs.apptoolkit.app.consent.domain.usecases.RequestConsentUseCase
import com.d4rk.android.libs.apptoolkit.app.startup.ui.contract.StartupAction
import com.d4rk.android.libs.apptoolkit.app.startup.ui.contract.StartupEvent
import com.d4rk.android.libs.apptoolkit.core.di.DispatcherProvider
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.DataState
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.Errors
import com.d4rk.android.libs.apptoolkit.core.ui.state.ScreenState
import com.d4rk.android.libs.apptoolkit.core.utils.FakeFirebaseController
import com.d4rk.android.libs.apptoolkit.core.utils.dispatchers.UnconfinedDispatcherExtension
import com.google.common.truth.Truth.assertThat
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

@OptIn(ExperimentalCoroutinesApi::class)
class StartupViewModelTest {

    companion object {
        @JvmField
        @RegisterExtension
        val dispatcherExtension = UnconfinedDispatcherExtension()
    }

    @Test
    fun `consent event updates state`() = runTest(dispatcherExtension.testDispatcher) {
        val firebaseController = FakeFirebaseController()
        val viewModel = StartupViewModel(
            requestConsentUseCase = RequestConsentUseCase(FakeConsentRepository(), firebaseController),
            dispatchers = testDispatcherProvider(),
            firebaseController = firebaseController,
        )
        viewModel.onEvent(StartupEvent.ConsentFormLoaded)
        val state = viewModel.uiState.value
        assertThat(state.screenState).isInstanceOf(ScreenState.Success::class.java)
        assertThat(state.data?.consentFormLoaded).isTrue()
    }

    @Test
    fun `continue event emits navigation action`() = runTest(dispatcherExtension.testDispatcher) {
        val firebaseController = FakeFirebaseController()
        val viewModel = StartupViewModel(
            requestConsentUseCase = RequestConsentUseCase(FakeConsentRepository(), firebaseController),
            dispatchers = testDispatcherProvider(),
            firebaseController = firebaseController,
        )
        val actions = mutableListOf<StartupAction>()
        val job = launch { viewModel.actionEvent.collect { actions.add(it) } }
        viewModel.onEvent(StartupEvent.Continue)
        advanceUntilIdle()
        assertThat(actions).containsExactly(StartupAction.NavigateNext)
        job.cancel()
    }

    @Test
    fun `request consent event completes startup state`() =
        runTest(dispatcherExtension.testDispatcher) {
            val firebaseController = FakeFirebaseController()
            val viewModel = StartupViewModel(
                requestConsentUseCase = RequestConsentUseCase(FakeConsentRepository(), firebaseController),
                dispatchers = testDispatcherProvider(),
                firebaseController = firebaseController,
            )

            viewModel.onEvent(StartupEvent.RequestConsent(host = FakeConsentHost()))
            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertThat(state.data?.consentFormLoaded).isTrue()
        }

    private fun testDispatcherProvider(): DispatcherProvider = object : DispatcherProvider {
        override val main = dispatcherExtension.testDispatcher
        override val io = dispatcherExtension.testDispatcher
        override val default = dispatcherExtension.testDispatcher
        override val unconfined = dispatcherExtension.testDispatcher
    }
}

private class FakeConsentRepository : ConsentRepository {
    override fun requestConsent(
        host: ConsentHost,
        showIfRequired: Boolean,
    ): Flow<DataState<Unit, Errors.UseCase>> = flowOf(
        DataState.Loading(),
        DataState.Success(Unit),
    )
}

private class FakeConsentHost : ConsentHost {
    override val activity = mockk<android.app.Activity>(relaxed = true)
}
