package com.d4rk.android.libs.apptoolkit.app.ads.ui

import com.d4rk.android.libs.apptoolkit.app.ads.domain.repository.AdsSettingsRepository
import com.d4rk.android.libs.apptoolkit.app.ads.ui.contract.AdsSettingsEvent
import com.d4rk.android.libs.apptoolkit.core.di.DispatcherProvider
import com.d4rk.android.libs.apptoolkit.core.di.TestDispatchers
import com.d4rk.android.libs.apptoolkit.core.domain.model.Result
import com.d4rk.android.libs.apptoolkit.core.ui.state.ScreenState
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

// TODO: We modified the flows and the use cases and VM and we need to re-update and add more tests here and fix the issues
@OptIn(ExperimentalCoroutinesApi::class)
class TestAdsSettingsViewModel {

    companion object {
        @JvmField
        @RegisterExtension
        val dispatcherExtension = UnconfinedDispatcherExtension()
    }

    private fun testDispatchers(): DispatcherProvider =
        TestDispatchers(dispatcherExtension.testDispatcher)

    private class FakeAdsSettingsRepository(
        override val defaultAdsEnabled: Boolean = true
    ) : AdsSettingsRepository {

        private val state = MutableStateFlow(defaultAdsEnabled)
        var setResult: Result<Unit> = Result.Success(Unit)

        override fun observeAdsEnabled(): Flow<Boolean> = state

        override suspend fun setAdsEnabled(enabled: Boolean): Result<Unit> { // FIXME: Return type of 'setAdsEnabled' is not a subtype of the return type of the overridden member 'suspend fun setAdsEnabled(enabled: Boolean): Unit' defined in 'com/d4rk/android/libs/apptoolkit/app/ads/domain/repository/AdsSettingsRepository'.
            if (setResult is Result.Success) {
                state.value = enabled
            }
            return setResult
        }
    }

    @Test
    fun `initial state reflects repository value`() = runTest(dispatcherExtension.testDispatcher) {
        val repo = FakeAdsSettingsRepository(defaultAdsEnabled = true)
        val viewModel = AdsSettingsViewModel(
            repository = repo,
            dispatchers = testDispatchers()
        ) // FIXME: No value passed for parameter 'observeAdsEnabled'.

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

            val viewModel = AdsSettingsViewModel(repository = repo, dispatchers = testDispatchers())

            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertThat(state.screenState).isInstanceOf(ScreenState.Error::class.java)
            assertThat(state.data?.adsEnabled).isFalse()
        }

    @Test
    fun `setAdsEnabled success updates state`() = runTest(dispatcherExtension.testDispatcher) {
        val repo = FakeAdsSettingsRepository(defaultAdsEnabled = true)
        val viewModel = AdsSettingsViewModel(repository = repo, dispatchers = testDispatchers())
        advanceUntilIdle()

        viewModel.onEvent(AdsSettingsEvent.SetAdsEnabled(false))
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertThat(state.screenState).isInstanceOf(ScreenState.Success::class.java)
        assertThat(state.data?.adsEnabled).isFalse()
    }

    @Test
    fun `setAdsEnabled error reverts state`() = runTest(dispatcherExtension.testDispatcher) {
        val repo = FakeAdsSettingsRepository(defaultAdsEnabled = true).apply {
            setResult = Result.Error(IOException("fail"))
        }

        val viewModel = AdsSettingsViewModel(repository = repo, dispatchers = testDispatchers())
        advanceUntilIdle()

        viewModel.onEvent(AdsSettingsEvent.SetAdsEnabled(false))
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertThat(state.screenState).isInstanceOf(ScreenState.Error::class.java)
        assertThat(state.data?.adsEnabled).isTrue()
    }
}
