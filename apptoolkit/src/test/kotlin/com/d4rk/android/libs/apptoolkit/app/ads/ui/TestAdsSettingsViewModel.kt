package com.d4rk.android.libs.apptoolkit.app.ads.ui

import com.d4rk.android.libs.apptoolkit.app.ads.domain.repository.AdsSettingsRepository
import com.d4rk.android.libs.apptoolkit.app.ads.domain.usecases.ObserveAdsEnabledUseCase
import com.d4rk.android.libs.apptoolkit.app.ads.domain.usecases.SetAdsEnabledUseCase
import com.d4rk.android.libs.apptoolkit.app.ads.ui.contract.AdsSettingsEvent
import com.d4rk.android.libs.apptoolkit.core.di.DispatcherProvider
import com.d4rk.android.libs.apptoolkit.core.di.TestDispatchers
import com.d4rk.android.libs.apptoolkit.core.domain.model.Result
import com.d4rk.android.libs.apptoolkit.core.ui.state.ScreenState
import com.d4rk.android.libs.apptoolkit.core.utils.FakeFirebaseController
import com.d4rk.android.libs.apptoolkit.core.utils.dispatchers.UnconfinedDispatcherExtension
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class TestAdsSettingsViewModel {

    companion object {
        @JvmField
        @RegisterExtension
        val dispatcherExtension = UnconfinedDispatcherExtension()
    }

    private fun testDispatchers(): DispatcherProvider =
        TestDispatchers(dispatcherExtension.testDispatcher)

    private val firebaseController = FakeFirebaseController()

    private class FakeAdsSettingsRepository(
        override val defaultAdsEnabled: Boolean = true,
        var shouldFail: Boolean = false
    ) : AdsSettingsRepository {

        private val state = MutableStateFlow(defaultAdsEnabled)

        override fun observeAdsEnabled(): Flow<Boolean> = state

        override suspend fun setAdsEnabled(enabled: Boolean): Result<Unit> {
            if (shouldFail) throw IOException("fail")
            state.value = enabled
            return Result.Success(Unit)
        }
    }

    private fun createViewModel(repository: AdsSettingsRepository): AdsSettingsViewModel {
        val observeUseCase = ObserveAdsEnabledUseCase(repository)
        val setUseCase = SetAdsEnabledUseCase(repository)
        return AdsSettingsViewModel(
            observeAdsEnabled = observeUseCase,
            setAdsEnabled = setUseCase,
            repository = repository,
            dispatchers = testDispatchers(),
            firebaseController = firebaseController,
        )
    }

    @Test
    fun `initial state reflects repository value`() = runTest(dispatcherExtension.testDispatcher) {
        val repo = FakeAdsSettingsRepository(defaultAdsEnabled = true)
        val viewModel = createViewModel(repo)

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertThat(state.screenState).isInstanceOf(ScreenState.Success::class.java)
        assertThat(state.data?.adsEnabled).isTrue()
    }

    @Test
    fun `emission error sets default and error state`() =
        runTest(dispatcherExtension.testDispatcher) {
            val repo = object : AdsSettingsRepository {
                override val defaultAdsEnabled: Boolean = false
                override fun observeAdsEnabled(): Flow<Boolean> = flow { throw IOException("boom") }
                override suspend fun setAdsEnabled(enabled: Boolean): Result<Unit> =
                    Result.Success(Unit)
            }

            val viewModel = createViewModel(repo)

            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertThat(state.screenState).isInstanceOf(ScreenState.Error::class.java)
            assertThat(state.data?.adsEnabled).isFalse()
        }

    @Test
    fun `setAdsEnabled success updates state`() = runTest(dispatcherExtension.testDispatcher) {
        val repo = FakeAdsSettingsRepository(defaultAdsEnabled = true)
        val viewModel = createViewModel(repo)
        advanceUntilIdle()

        viewModel.onEvent(AdsSettingsEvent.SetAdsEnabled(false))
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertThat(state.screenState).isInstanceOf(ScreenState.Success::class.java)
        assertThat(state.data?.adsEnabled).isFalse()
    }

    @Test
    fun `setAdsEnabled error reverts state`() = runTest(dispatcherExtension.testDispatcher) {
        val repo = FakeAdsSettingsRepository(defaultAdsEnabled = true, shouldFail = true)

        val viewModel = createViewModel(repo)
        advanceUntilIdle()

        viewModel.onEvent(AdsSettingsEvent.SetAdsEnabled(false))
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertThat(state.screenState).isInstanceOf(ScreenState.Error::class.java)
        assertThat(state.data?.adsEnabled).isTrue()
    }
}
