package com.d4rk.android.libs.apptoolkit.app.advanced.ui

import app.cash.turbine.test
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.app.advanced.domain.repository.CacheRepository
import com.d4rk.android.libs.apptoolkit.app.advanced.ui.contract.AdvancedSettingsEvent
import com.d4rk.android.libs.apptoolkit.core.di.DispatcherProvider
import com.d4rk.android.libs.apptoolkit.core.di.TestDispatchers
import com.d4rk.android.libs.apptoolkit.core.domain.model.Result
import com.d4rk.android.libs.apptoolkit.core.utils.FakeFirebaseController
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Test

class FakeCacheRepository(private val result: Result<Unit>) : CacheRepository {
    override fun clearCache(): Flow<Result<Unit>> = flowOf(result)
}

class HotFakeCacheRepository : CacheRepository {
    private val flow = MutableSharedFlow<Result<Unit>>(replay = 0, extraBufferCapacity = 1)
    override fun clearCache(): Flow<Result<Unit>> = flow
    suspend fun emit(result: Result<Unit>) = flow.emit(result)
}


@OptIn(ExperimentalCoroutinesApi::class)
class TestAdvancedSettingsViewModel {

    private val testDispatchers: DispatcherProvider = TestDispatchers()
    private val firebaseController = FakeFirebaseController()

    @Test
    fun `onClearCache emits success message`() = runTest {
        val viewModel = AdvancedSettingsViewModel(
            repository = FakeCacheRepository(Result.Success(Unit)),
            dispatchers = testDispatchers,
            firebaseController = firebaseController,
        )

        viewModel.onEvent(AdvancedSettingsEvent.ClearCache)
        advanceUntilIdle()

        assertThat(viewModel.uiState.value.data?.cacheClearMessage)
            .isEqualTo(R.string.cache_cleared_success)

        viewModel.onEvent(AdvancedSettingsEvent.MessageShown)
        advanceUntilIdle()

        assertThat(viewModel.uiState.value.data?.cacheClearMessage).isNull()
    }

    @Test
    fun `onClearCache emits error message when failure`() = runTest {
        val viewModel = AdvancedSettingsViewModel(
            repository = FakeCacheRepository(Result.Error(Exception("fail"))),
            dispatchers = testDispatchers,
            firebaseController = firebaseController,
        )

        viewModel.onEvent(AdvancedSettingsEvent.ClearCache)
        advanceUntilIdle()

        assertThat(viewModel.uiState.value.data?.cacheClearMessage)
            .isEqualTo(R.string.cache_cleared_error)
    }

    @Test
    fun `clearCache emits messages for success and error`() = runTest {
        val repository = HotFakeCacheRepository()
        val viewModel = AdvancedSettingsViewModel(
            repository = repository,
            dispatchers = testDispatchers,
            firebaseController = firebaseController,
        )

        viewModel.uiState.test {
            assertThat(awaitItem().data?.cacheClearMessage).isNull()

            viewModel.onEvent(AdvancedSettingsEvent.ClearCache)
            advanceUntilIdle()
            repository.emit(Result.Success(Unit))
            assertThat(awaitItem().data?.cacheClearMessage)
                .isEqualTo(R.string.cache_cleared_success)

            viewModel.onEvent(AdvancedSettingsEvent.MessageShown)
            assertThat(awaitItem().data?.cacheClearMessage).isNull()

            viewModel.onEvent(AdvancedSettingsEvent.ClearCache)
            advanceUntilIdle()
            repository.emit(Result.Error(Exception("boom")))
            assertThat(awaitItem().data?.cacheClearMessage)
                .isEqualTo(R.string.cache_cleared_error)

            cancelAndIgnoreRemainingEvents()
        }
    }
}
