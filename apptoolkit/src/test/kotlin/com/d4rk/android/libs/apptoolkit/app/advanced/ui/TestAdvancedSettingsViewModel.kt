package com.d4rk.android.libs.apptoolkit.app.advanced.ui

import app.cash.turbine.test
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.app.advanced.data.CacheRepository
import com.d4rk.android.libs.apptoolkit.app.advanced.ui.contract.AdvancedSettingsEvent
import com.d4rk.android.libs.apptoolkit.core.domain.model.Result
import com.d4rk.android.libs.apptoolkit.core.ui.state.ScreenState
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Test

private class FakeCacheRepository(private val result: Result<Unit>) : CacheRepository {
    override fun clearCache(): Flow<Result<Unit>> = flowOf(result)
}

@OptIn(ExperimentalCoroutinesApi::class)
class TestAdvancedSettingsViewModel {

    @Test
    fun `onClearCache emits success message`() = runTest {
        val viewModel =
            AdvancedSettingsViewModel(repository = FakeCacheRepository(Result.Success(Unit)))

        viewModel.onEvent(AdvancedSettingsEvent.ClearCache)
        advanceUntilIdle()

        assertThat(viewModel.uiState.value.data?.cacheClearMessage).isEqualTo(R.string.cache_cleared_success)
        viewModel.onEvent(AdvancedSettingsEvent.MessageShown)
        advanceUntilIdle()
        assertThat(viewModel.uiState.value.data?.cacheClearMessage).isNull()
    }

    @Test
    fun `onClearCache emits error message when failure`() = runTest {
        val viewModel =
            AdvancedSettingsViewModel(repository = FakeCacheRepository(Result.Error(Exception("fail"))))

        viewModel.onEvent(AdvancedSettingsEvent.ClearCache)
        advanceUntilIdle()

        assertThat(viewModel.uiState.value.data?.cacheClearMessage).isEqualTo(R.string.cache_cleared_error)
    }

    @Test
    fun `clearCache emits messages for success and error`() = runTest {
        val repository = HotFakeCacheRepository()
        val viewModel = AdvancedSettingsViewModel(repository)

        viewModel.uiState.test {
            // Initial state
            assertThat(awaitItem().data?.cacheClearMessage).isNull()

            // Success emission
            viewModel.onEvent(AdvancedSettingsEvent.ClearCache)
            assertThat(awaitItem().screenState).isInstanceOf(ScreenState.IsLoading::class.java)
            repository.emit(Result.Success(Unit))
            assertThat(awaitItem().data?.cacheClearMessage).isEqualTo(R.string.cache_cleared_success)

            // Reset message
            viewModel.onEvent(AdvancedSettingsEvent.MessageShown)
            assertThat(awaitItem().data?.cacheClearMessage).isNull()

            // Error emission
            viewModel.onEvent(AdvancedSettingsEvent.ClearCache)
            assertThat(awaitItem().screenState).isInstanceOf(ScreenState.IsLoading::class.java)
            repository.emit(Result.Error(Exception("boom")))
            assertThat(awaitItem().data?.cacheClearMessage).isEqualTo(R.string.cache_cleared_error)

            cancelAndIgnoreRemainingEvents()
        }
    }
}

private class HotFakeCacheRepository : CacheRepository {
    private val flow = MutableSharedFlow<Result<Unit>>()
    override fun clearCache(): Flow<Result<Unit>> = flow
    suspend fun emit(result: Result<Unit>) = flow.emit(result)
}
