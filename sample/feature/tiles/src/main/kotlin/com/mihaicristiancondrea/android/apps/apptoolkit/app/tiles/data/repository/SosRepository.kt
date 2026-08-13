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

package com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.repository

import android.content.Context
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.coroutines.dispatchers.DispatcherProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/** Owns SOS signalling state and controls the device torch. */
class SosRepository(
    context: Context,
    private val dispatchers: DispatcherProvider,
) {

    private val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    private val scope = CoroutineScope(SupervisorJob() + dispatchers.default)
    private val unit = 200L

    private val _isActive = MutableStateFlow(false)
    val isActive: StateFlow<Boolean> = _isActive.asStateFlow()

    private var sosJob: Job? = null

    private val cameraId: String? = try {
        cameraManager.cameraIdList.find { id ->
            val c = cameraManager.getCameraCharacteristics(id)
            val hasFlash = c.get(CameraCharacteristics.FLASH_INFO_AVAILABLE) == true
            val isBack =
                c.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_BACK
            hasFlash && isBack
        }
    } catch (_: Exception) {
        null
    }

    fun toggle() {
        if (cameraId == null) return

        if (_isActive.value) {
            stop()
        } else {
            start()
        }
    }

    private fun start() {
        _isActive.value = true
        sosJob?.cancel()
        sosJob = scope.launch {
            try {
                while (isActive) {
                    delay(unit * 2)
                    sendS()
                    delay(unit * 2)
                    sendO()
                    delay(unit * 2)
                    sendS()
                    delay(unit * 4)
                }
            } finally {
                withContext(NonCancellable) {
                    setTorch(false)
                }
            }
        }
    }

    private fun stop() {
        _isActive.value = false
        sosJob?.cancel()
        sosJob = null
    }

    fun cleanup() {
        stop()
    }

    private suspend fun sendS() = repeat(3) {
        if (currentCoroutineContext().isActive) dot()
    }

    private suspend fun sendO() = repeat(3) {
        if (currentCoroutineContext().isActive) dash()
    }

    private suspend fun dot() {
        blink(unit)
        delay(unit)
    }

    private suspend fun dash() {
        blink(unit * 3)
        delay(unit)
    }

    private suspend fun blink(duration: Long) {
        if (!currentCoroutineContext().isActive) return
        setTorch(true)
        delay(duration)
        setTorch(false)
    }

    private fun setTorch(enabled: Boolean) {
        val id = cameraId ?: return
        try {
            cameraManager.setTorchMode(id, enabled)
        } catch (_: CameraAccessException) {
        } catch (_: Exception) {
        }
    }
}
