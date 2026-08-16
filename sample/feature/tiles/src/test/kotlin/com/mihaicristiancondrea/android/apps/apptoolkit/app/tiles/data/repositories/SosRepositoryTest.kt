/*
 * Copyright (Â©) 2026 Mihai-Cristian Condrea
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.repositories

import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.models.TorchCapabilities
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.models.TorchPreset
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.models.TorchState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.TestDispatchers
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SosRepositoryTest {

    @Test
    fun `toggle starts and stops SOS through shared Morse playback`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val torch = RecordingTorchRepository()
        val morse = MorseRepository(torch, TestDispatchers(dispatcher))
        val repository = SosRepository(morse)

        repository.toggle()
        runCurrent()

        assertTrue(repository.isActive)
        assertEquals("SOS", repository.state.value.message)
        assertEquals("Maximum", torch.commands.last())

        repository.toggle()
        runCurrent()

        assertFalse(repository.isActive)
        assertEquals("Off", torch.commands.last())
    }

    @Test
    fun `stopping inactive SOS does not interrupt a custom Morse message`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val torch = RecordingTorchRepository()
        val morse = MorseRepository(torch, TestDispatchers(dispatcher))
        val repository = SosRepository(morse)

        morse.start("HELLO")
        repository.stop()

        assertFalse(repository.isActive)
        assertTrue(morse.state.value.isActive)
        assertEquals("HELLO", morse.state.value.message)

        morse.stop()
    }
}

private class RecordingTorchRepository : TorchRepository {
    private val mutableState = MutableStateFlow(
        TorchState(capabilities = TorchCapabilities(isAvailable = true, maximumLevel = 4))
    )
    override val state: StateFlow<TorchState> = mutableState
    val commands = mutableListOf<String>()

    override fun setLevel(level: Int) {
        commands += level.toString()
        mutableState.value = mutableState.value.copy(currentLevel = level)
    }

    override fun applyPreset(preset: TorchPreset) {
        commands += preset.name
        mutableState.value = mutableState.value.copy(
            currentLevel = if (preset == TorchPreset.Off) 0 else 4
        )
    }

    override fun cyclePreset() = Unit

    override fun turnOff() {
        commands += "Off"
        mutableState.value = mutableState.value.copy(currentLevel = 0)
    }

    override fun clearError() = Unit
}
