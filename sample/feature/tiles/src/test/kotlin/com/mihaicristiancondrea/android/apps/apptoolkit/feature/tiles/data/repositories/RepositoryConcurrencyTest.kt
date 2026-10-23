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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.repositories

import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.local.preferences.DefaultToolkitTilesPreferencesDataSource
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.local.preferences.ToolkitTilesPreferencesDataSource
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.TorchCapabilities
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.TorchPreset
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.TorchState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.exceptions.StorageException
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakePreferencesDataStore
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.TestDispatchers
import java.io.IOException
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RepositoryConcurrencyTest {
    @Test
    fun `counter reports a failed write to its caller`() = runTest {
        val source = DefaultToolkitTilesPreferencesDataSource(FakePreferencesDataStore())
        val failing = object : ToolkitTilesPreferencesDataSource by source {
            override suspend fun incrementCounter() { throw IOException("disk failure") }
        }
        val repository = CounterRepository(failing, TestDispatchers(StandardTestDispatcher(testScheduler)))
        assertFailsWith<StorageException> { repository.increment() }
        assertEquals(0, source.counterValue.first())
    }

    @Test
    fun `counter finishes an accepted write after its caller disappears`() = runTest {
        val source = DefaultToolkitTilesPreferencesDataSource(FakePreferencesDataStore())
        val release = CompletableDeferred<Unit>()
        val delayed = object : ToolkitTilesPreferencesDataSource by source {
            override suspend fun incrementCounter() {
                release.await()
                source.incrementCounter()
            }
        }
        val repository = CounterRepository(delayed, TestDispatchers(StandardTestDispatcher(testScheduler)))
        val caller = launch { repository.increment() }
        runCurrent()
        caller.cancelAndJoin()
        release.complete(Unit)
        advanceUntilIdle()
        assertEquals(1, source.counterValue.first())
    }

    @Test
    fun `stopping Morse waits for an in-flight torch command and leaves it off`() = runTest {
        val lane = Executors.newSingleThreadExecutor().asCoroutineDispatcher()
        val entered = CountDownLatch(1)
        val release = CountDownLatch(1)
        val torch = object : TorchRepository {
            override val state = MutableStateFlow(TorchState(TorchCapabilities(true, 4)))
            override fun setLevel(level: Int) { state.value = state.value.copy(currentLevel = level) }
            override fun applyPreset(preset: TorchPreset) {
                entered.countDown()
                check(release.await(3, TimeUnit.SECONDS))
                setLevel(4)
            }
            override fun turnOff() = setLevel(0)
            override fun cyclePreset() {}
            override fun clearError() {}
        }
        val repository = MorseRepository(torch, object : com.mihaicristiancondrea.android.libs.apptoolkit.core.common.coroutines.dispatchers.DispatcherProvider {
            override val main = lane
            override val io = lane
            override val default = lane
            override val unconfined = lane
        })
        try {
            assertTrue(repository.start("E"))
            assertTrue(withContext(Dispatchers.IO) { entered.await(3, TimeUnit.SECONDS) })
            val stopping = async(start = CoroutineStart.UNDISPATCHED) { repository.stop() }
            assertFalse(stopping.isCompleted)
            release.countDown()
            stopping.await()
            assertFalse(repository.state.value.isActive)
            assertFalse(torch.state.value.isEnabled)
        } finally {
            release.countDown()
            repository.stop()
            lane.close()
        }
    }
}
