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

package com.wstxda.toolkit.manager.sos

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class SosManager(context: Context) {

    private val flasher = SosFlasher(context)
    private val managerScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val _isActive = MutableStateFlow(false)
    val isActive = _isActive.asStateFlow()

    val isFlashAvailable: StateFlow<Boolean> = combine(
        flasher.isTorchAvailable, flasher.isTorchOn, _isActive
    ) { available, isOn, active ->
        if (active) {
            available
        } else {
            available && !isOn
        }
    }.stateIn(
        scope = managerScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = flasher.isTorchAvailable.value
    )

    fun hasFlashHardware(): Boolean = flasher.hasFlashHardware

    fun toggle() {
        if (!hasFlashHardware()) return
        if (!_isActive.value && !isFlashAvailable.value) return

        if (_isActive.value) {
            stopInternal()
        } else {
            startInternal()
        }
    }

    fun cleanup() {
        stopInternal()
        flasher.cleanup()
    }

    private fun startInternal() {
        if (!flasher.hasFlashHardware) return
        _isActive.value = true
        flasher.start()
    }

    private fun stopInternal() {
        _isActive.value = false
        flasher.stop()
    }
}