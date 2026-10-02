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

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.exceptions.StorageException
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.platform.UiTextHelper
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakeTelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.UnconfinedDispatcherExtension
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.R as CoreUiR
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.UiMessage
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.advanced.R
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.advanced.data.repositories.CacheRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.advanced.ui.contracts.AdvancedSettingsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.advanced.ui.states.AdvancedSettingsUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.advanced.ui.states.CacheClearStatus
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AdvancedSettingsViewModelTest {

    companion object {
        @JvmField
        @RegisterExtension
        val dispatcherExtension = UnconfinedDispatcherExtension()
    }

    private val telemetryRepository = FakeTelemetryRepository()

    private fun createViewModel(
        repository: CacheRepository = FakeCacheRepository(),
        developerOptionsUnlocked: Flow<Boolean> = flowOf(false),
    ): AdvancedSettingsViewModel = AdvancedSettingsViewModel(
        repository = repository,
        telemetryRepository = telemetryRepository,
        developerOptionsUnlocked = developerOptionsUnlocked,
    )

    private fun advance() = dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()

    private fun AdvancedSettingsViewModel.onlyMessage(): UiMessage = messages.value.single()

    private val UiMessage.resourceId: Int
        get() = (text as UiTextHelper.StringResource).resourceId

    @Test
    fun `the page opens idle with the developer options hidden and no message`() =
        runTest(dispatcherExtension.testDispatcher) {
            val viewModel = createViewModel()
            advance()

            assertEquals(AdvancedSettingsUiState(), viewModel.state.value)
            assertTrue(viewModel.messages.value.isEmpty())
        }

    @Test
    fun `clearing the cache confirms it`() = runTest(dispatcherExtension.testDispatcher) {
        val repository = FakeCacheRepository()
        val viewModel = createViewModel(repository = repository)
        advance()

        viewModel.onEvent(AdvancedSettingsEvent.ClearCache)
        advance()

        assertEquals(1, repository.clears)
        assertEquals(CacheClearStatus.Idle, viewModel.state.value.cacheClear)
        val message = viewModel.onlyMessage()
        assertEquals(R.string.cache_cleared_success, message.resourceId)
        assertFalse(message.isError)
    }

    @Test
    fun `the page reports clearing while the cache is being deleted`() =
        runTest(dispatcherExtension.testDispatcher) {
            val gate = CompletableDeferred<Unit>()
            val viewModel = createViewModel(repository = FakeCacheRepository(gate = gate))
            advance()

            viewModel.onEvent(AdvancedSettingsEvent.ClearCache)
            advance()
            assertEquals(CacheClearStatus.Clearing, viewModel.state.value.cacheClear)

            gate.complete(Unit)
            advance()
            assertEquals(CacheClearStatus.Idle, viewModel.state.value.cacheClear)
        }

    @Test
    fun `a failure with no text of its own shows the page's text and keeps the rows`() =
        runTest(dispatcherExtension.testDispatcher) {
            val viewModel = createViewModel(
                repository = FakeCacheRepository(failure = StorageException(StorageException.Reason.FAILED)),
            )
            advance()

            viewModel.onEvent(AdvancedSettingsEvent.ClearCache)
            advance()

            assertEquals(CacheClearStatus.Failed, viewModel.state.value.cacheClear)
            val message = viewModel.onlyMessage()
            assertEquals(R.string.cache_cleared_error, message.resourceId)
            assertTrue(message.isError)
            assertTrue(telemetryRepository.loggedEvents.any { it.name == "vm_op_error" })
        }

    @Test
    fun `a full storage failure shows its own text`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = createViewModel(
            repository = FakeCacheRepository(failure = StorageException(StorageException.Reason.FULL)),
        )
        advance()

        viewModel.onEvent(AdvancedSettingsEvent.ClearCache)
        advance()

        assertEquals(CoreUiR.string.screen_error_storage_full, viewModel.onlyMessage().resourceId)
    }

    @Test
    fun `clearing again after a failure confirms it`() = runTest(dispatcherExtension.testDispatcher) {
        val repository = FakeCacheRepository(failure = IllegalStateException("fail"))
        val viewModel = createViewModel(repository = repository)
        advance()
        viewModel.onEvent(AdvancedSettingsEvent.ClearCache)
        advance()
        viewModel.messageShown(viewModel.onlyMessage().id)

        repository.failure = null
        viewModel.onEvent(AdvancedSettingsEvent.ClearCache)
        advance()

        assertEquals(CacheClearStatus.Idle, viewModel.state.value.cacheClear)
        assertEquals(R.string.cache_cleared_success, viewModel.onlyMessage().resourceId)
    }

    @Test
    fun `a shown message leaves the queue`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = createViewModel()
        advance()

        viewModel.onEvent(AdvancedSettingsEvent.ClearCache)
        advance()
        viewModel.messageShown(viewModel.onlyMessage().id)

        assertTrue(viewModel.messages.value.isEmpty())
    }

    @Test
    fun `the developer options are offered once the easter egg is found`() =
        runTest(dispatcherExtension.testDispatcher) {
            val unlocked = MutableStateFlow(false)
            val viewModel = createViewModel(developerOptionsUnlocked = unlocked)
            advance()
            assertFalse(viewModel.state.value.developerOptionsUnlocked)

            unlocked.value = true
            advance()

            assertTrue(viewModel.state.value.developerOptionsUnlocked)
        }

    /** Counts each clear; [failure] makes it throw, and [gate] holds it until completed. */
    private class FakeCacheRepository(
        var failure: Throwable? = null,
        private val gate: CompletableDeferred<Unit>? = null,
    ) : CacheRepository {
        var clears: Int = 0

        override suspend fun clearCache() {
            clears += 1
            gate?.await()
            failure?.let { throw it }
        }
    }
}
