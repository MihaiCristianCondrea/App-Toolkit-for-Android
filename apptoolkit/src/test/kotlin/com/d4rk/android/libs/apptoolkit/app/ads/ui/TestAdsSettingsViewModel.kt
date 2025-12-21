package com.d4rk.android.libs.apptoolkit.app.ads.ui

import com.d4rk.android.libs.apptoolkit.app.ads.domain.repository.AdsSettingsRepository
import com.d4rk.android.libs.apptoolkit.app.ads.ui.contract.AdsSettingsEvent
import com.d4rk.android.libs.apptoolkit.core.domain.model.Result
import com.d4rk.android.libs.apptoolkit.core.ui.state.ScreenState
import com.d4rk.android.libs.apptoolkit.core.utils.dispatchers.UnconfinedDispatcherExtension
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
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

    private class FakeAdsSettingsRepository(
        override val defaultAdsEnabled: Boolean = true
    ) : AdsSettingsRepository {
        private val flow =
            MutableSharedFlow<Boolean>(replay = 1).apply { tryEmit(defaultAdsEnabled) }
        var setResult: Result<Unit> = Result.Success(Unit)

        override fun observeAdsEnabled(): Flow<Boolean> = flow

        override suspend fun setAdsEnabled(enabled: Boolean): Result<Unit> {
            if (setResult is Result.Success) {
                flow.emit(enabled)
            }
            return setResult
        }
    }

    @Test
    fun `initial state reflects repository value`() = runTest(dispatcherExtension.testDispatcher) {
        println("\uD83D\uDE80 [TEST] initial state reflects repository value")
        val repo = FakeAdsSettingsRepository(defaultAdsEnabled = true)
        val viewModel = AdsSettingsViewModel(repo)

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertThat(state.screenState).isInstanceOf(ScreenState.Success::class.java)
        assertThat(state.data?.adsEnabled).isTrue()
    }

    @Test
    fun `emission error sets default and error state`() =
        runTest(dispatcherExtension.testDispatcher) {
            println("\uD83D\uDE80 [TEST] emission error sets default and error state")
            val repo = object : AdsSettingsRepository {
                override val defaultAdsEnabled: Boolean = false
                override fun observeAdsEnabled(): Flow<Boolean> = flow { throw IOException("boom") }
                override suspend fun setAdsEnabled(enabled: Boolean): Result<Unit> =
                    Result.Success(Unit)
            }
            val viewModel = AdsSettingsViewModel(repo)

            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertThat(state.screenState).isInstanceOf(ScreenState.Error::class.java)
            assertThat(state.data?.adsEnabled).isFalse()
        }

    @Test
    fun `setAdsEnabled success updates state`() = runTest(dispatcherExtension.testDispatcher) {
        println("\uD83D\uDE80 [TEST] setAdsEnabled success updates state")
        val repo = FakeAdsSettingsRepository(defaultAdsEnabled = true)
        val viewModel = AdsSettingsViewModel(repo)
        advanceUntilIdle()

        viewModel.onEvent(AdsSettingsEvent.SetAdsEnabled(false))
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertThat(state.screenState).isInstanceOf(ScreenState.Success::class.java)
        assertThat(state.data?.adsEnabled).isFalse()
    }

    @Test
    fun `setAdsEnabled error reverts state`() = runTest(dispatcherExtension.testDispatcher) {
        println("\uD83D\uDE80 [TEST] setAdsEnabled error reverts state")
        val repo = FakeAdsSettingsRepository(defaultAdsEnabled = true)
        repo.setResult = Result.Error(IOException("fail"))
        val viewModel = AdsSettingsViewModel(repo)
        advanceUntilIdle()

        viewModel.onEvent(AdsSettingsEvent.SetAdsEnabled(false))
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertThat(state.screenState).isInstanceOf(ScreenState.Error::class.java)
        assertThat(state.data?.adsEnabled).isTrue()
    }
}

