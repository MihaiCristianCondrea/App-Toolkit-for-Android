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
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MorseRepositoryTest {

    @Test
    fun `encoder supports the reference alphabet`() {
        assertEquals("...", MorseRepository.MORSE_ALPHABET.getValue('S'))
        assertEquals("---", MorseRepository.MORSE_ALPHABET.getValue('O'))
        assertEquals("-----", MorseRepository.MORSE_ALPHABET.getValue('0'))
        assertTrue(MorseRepository.isSupportedMessage("SOS 123"))
        assertFalse(MorseRepository.isSupportedMessage("SOS!"))
    }

    @Test
    fun `playback publishes current character and flashes maximum strength`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val torch = MorseRecordingTorchRepository()
        val repository = MorseRepository(torch, TestDispatchers(dispatcher))

        assertTrue(repository.start("e"))
        runCurrent()

        assertEquals('E', repository.state.value.currentCharacter)
        assertEquals(".", repository.state.value.currentCode)
        assertEquals("Maximum", torch.commands.last())

        advanceTimeBy(200)
        runCurrent()

        assertEquals("Off", torch.commands.last())
        repository.stop()
    }

    @Test
    fun `replacing playback prevents cancelled job from turning off new message`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val torch = MorseRecordingTorchRepository()
        val repository = MorseRepository(torch, TestDispatchers(dispatcher))

        repository.start("E")
        runCurrent()
        repository.start("T")
        runCurrent()

        assertTrue(repository.state.value.isActive)
        assertEquals("T", repository.state.value.message)
        assertEquals("Maximum", torch.commands.last())
        repository.stop()
    }
}

private class MorseRecordingTorchRepository : TorchRepository {
    private val mutableState = MutableStateFlow(
        TorchState(capabilities = TorchCapabilities(isAvailable = true, maximumLevel = 4))
    )
    override val state: StateFlow<TorchState> = mutableState
    val commands = mutableListOf<String>()

    override fun setLevel(level: Int) = Unit

    override fun applyPreset(preset: TorchPreset) {
        commands += preset.name
    }

    override fun cyclePreset() = Unit

    override fun turnOff() {
        commands += "Off"
    }

    override fun clearError() = Unit
}
