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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.advanced.ui

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.advanced.data.repositories.CacheRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.advanced.ui.contracts.AdvancedSettingsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.coroutines.dispatchers.DispatcherProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.network.domain.models.network.DataState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.network.domain.models.network.Errors
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakeFirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.StandardDispatcherExtension
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.TestDispatchers
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.advanced.R
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

class FakeCacheRepository(private val result: DataState<Unit, Errors.Database>) : CacheRepository {
    override fun clearCache(): Flow<DataState<Unit, Errors.Database>> = flowOf(result)
}

class HotFakeCacheRepository : CacheRepository {
    private val flow =
        MutableSharedFlow<DataState<Unit, Errors.Database>>(replay = 0, extraBufferCapacity = 1)

    override fun clearCache(): Flow<DataState<Unit, Errors.Database>> = flow
    suspend fun emit(result: DataState<Unit, Errors.Database>) = flow.emit(result)
}


@OptIn(ExperimentalCoroutinesApi::class)
class AdvancedSettingsViewModelTest {

    companion object {
        // `viewModelScope` binds to `Dispatchers.Main.immediate`, and Lifecycle falls back to an
        // empty context, meaning `Dispatchers.Default`, when Main is missing instead of throwing.
        // Without this the ViewModel's coroutines run on real threads that `advanceUntilIdle()`
        // knows nothing about, so every assertion below races the work it is waiting for.
        @JvmField
        @RegisterExtension
        val dispatcherExtension = StandardDispatcherExtension()
    }

    // The same dispatcher backs `runTest`, `Dispatchers.Main` and `flowOn`, so one scheduler drives
    // every coroutine the ViewModel starts.
    private val testDispatchers: DispatcherProvider =
        TestDispatchers(dispatcherExtension.testDispatcher)
    private val firebaseController = FakeFirebaseController()

    @Test
    fun `onClearCache emits success message`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = AdvancedSettingsViewModel(
            repository = FakeCacheRepository(DataState.Success(Unit)),
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
    fun `onClearCache emits error message when failure`() =
        runTest(dispatcherExtension.testDispatcher) {
            val viewModel = AdvancedSettingsViewModel(
                repository = FakeCacheRepository(DataState.Error(error = Errors.Database.DATABASE_OPERATION_FAILED)),
                dispatchers = testDispatchers,
                firebaseController = firebaseController,
            )

            viewModel.onEvent(AdvancedSettingsEvent.ClearCache)
            advanceUntilIdle()

            assertThat(viewModel.uiState.value.data?.cacheClearMessage)
                .isEqualTo(R.string.cache_cleared_error)
        }

    @Test
    fun `clearCache emits messages for success and error`() =
        runTest(dispatcherExtension.testDispatcher) {
            val repository = HotFakeCacheRepository()
            val viewModel = AdvancedSettingsViewModel(
                repository = repository,
                dispatchers = testDispatchers,
                firebaseController = firebaseController,
            )

            // `expectMostRecentItem()` rather than `awaitItem()`: clearing the cache first flips the
            // screen to loading, so each step produces an intermediate state before the one under
            // test. Asserting on the latest keeps the test about the message, not the transitions.
            viewModel.uiState.test {
                assertThat(awaitItem().data?.cacheClearMessage).isNull()

                viewModel.onEvent(AdvancedSettingsEvent.ClearCache)
                advanceUntilIdle()
                repository.emit(DataState.Success(Unit))
                advanceUntilIdle()
                assertThat(expectMostRecentItem().data?.cacheClearMessage)
                    .isEqualTo(R.string.cache_cleared_success)

                viewModel.onEvent(AdvancedSettingsEvent.MessageShown)
                advanceUntilIdle()
                assertThat(expectMostRecentItem().data?.cacheClearMessage).isNull()

                viewModel.onEvent(AdvancedSettingsEvent.ClearCache)
                advanceUntilIdle()
                repository.emit(DataState.Error(error = Errors.Database.DATABASE_OPERATION_FAILED))
                advanceUntilIdle()
                assertThat(expectMostRecentItem().data?.cacheClearMessage)
                    .isEqualTo(R.string.cache_cleared_error)

                cancelAndIgnoreRemainingEvents()
            }
        }
}
