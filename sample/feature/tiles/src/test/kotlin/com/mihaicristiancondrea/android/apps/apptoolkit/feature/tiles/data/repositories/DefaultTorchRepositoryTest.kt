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

import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.local.torch.TorchDataSource
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.local.torch.TorchHardwareState
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.TorchCapabilities
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.TorchPreset
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.TestDispatchers
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DefaultTorchRepositoryTest {

    @Test
    fun `cycle uses each distinct supported level then turns off`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val source = FakeTorchDataSource(maximumLevel = 3)
        val repository = DefaultTorchRepository(source, TestDispatchers(dispatcher))

        repository.cyclePreset()
        repository.cyclePreset()
        repository.cyclePreset()
        repository.cyclePreset()

        assertEquals(listOf(1, 2, 3, 0), source.commands)
        assertEquals(0, repository.state.value.currentLevel)
    }

    @Test
    fun `binary device cycles as a simple toggle`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val source = FakeTorchDataSource(maximumLevel = 1)
        val repository = DefaultTorchRepository(source, TestDispatchers(dispatcher))

        repository.cyclePreset()
        repository.cyclePreset()

        assertEquals(listOf(1, 0), source.commands)
        assertFalse(repository.state.value.capabilities.supportsDimming)
    }

    @Test
    fun `hardware callback replaces optimistic state`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val source = FakeTorchDataSource(maximumLevel = 5)
        val repository = DefaultTorchRepository(source, TestDispatchers(dispatcher))
        runCurrent()

        source.events.emit(TorchHardwareState(isEnabled = true, currentLevel = 4))
        runCurrent()

        assertTrue(repository.state.value.isEnabled)
        assertEquals(4, repository.state.value.currentLevel)
    }

    @Test
    fun `preset values are clamped to device capability`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val source = FakeTorchDataSource(maximumLevel = 2)
        val repository = DefaultTorchRepository(source, TestDispatchers(dispatcher))

        repository.applyPreset(TorchPreset.Maximum)
        repository.setLevel(99)

        assertEquals(listOf(2, 2), source.commands)
    }
}

private class FakeTorchDataSource(maximumLevel: Int) : TorchDataSource {
    override val capabilities = TorchCapabilities(
        isAvailable = maximumLevel > 0,
        maximumLevel = maximumLevel,
    )
    val events = MutableSharedFlow<TorchHardwareState>(extraBufferCapacity = 1)
    override val hardwareState: Flow<TorchHardwareState> = events
    val commands = mutableListOf<Int>()

    override fun setLevel(level: Int) {
        commands += level
    }
}

