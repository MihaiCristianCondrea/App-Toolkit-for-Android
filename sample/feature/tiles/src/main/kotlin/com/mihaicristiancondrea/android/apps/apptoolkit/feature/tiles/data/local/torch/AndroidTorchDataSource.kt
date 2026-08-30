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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.local.torch

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.TorchCapabilities
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

/** Camera2-backed torch source. The app manifest declares the flashlight capability as optional. */
class AndroidTorchDataSource(context: Context) : TorchDataSource {

    private val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    private val cameraId: String? = findBackFlashCamera()

    override val capabilities: TorchCapabilities = TorchCapabilities(
        isAvailable = cameraId != null,
        maximumLevel = cameraId?.maximumStrengthLevel() ?: 0,
    )

    override val hardwareState: Flow<TorchHardwareState> = callbackFlow {
        val id = cameraId
        if (id == null) {
            trySend(TorchHardwareState(isEnabled = false, currentLevel = 0))
            close()
            return@callbackFlow
        }

        val callback = object : CameraManager.TorchCallback() {
            override fun onTorchModeChanged(changedCameraId: String, enabled: Boolean) {
                if (changedCameraId != id) return
                trySend(
                    TorchHardwareState(
                        isEnabled = enabled,
                        currentLevel = if (enabled) currentStrengthLevel(id) else 0,
                    )
                )
            }

            override fun onTorchModeUnavailable(changedCameraId: String) {
                if (changedCameraId == id) {
                    trySend(TorchHardwareState(isEnabled = false, currentLevel = 0))
                }
            }

            override fun onTorchStrengthLevelChanged(changedCameraId: String, newStrengthLevel: Int) {
                if (changedCameraId == id) {
                    trySend(TorchHardwareState(isEnabled = true, currentLevel = newStrengthLevel))
                }
            }
        }

        @Suppress("DEPRECATION")
        cameraManager.registerTorchCallback(callback, Handler(Looper.getMainLooper()))
        awaitClose { cameraManager.unregisterTorchCallback(callback) }
    }

    @SuppressLint("MissingPermission")
    override fun setLevel(level: Int) {
        val id = cameraId ?: error("No torch is available")
        val maximum = capabilities.maximumLevel
        val validLevel = level.coerceIn(0, maximum)
        when {
            validLevel == 0 -> cameraManager.setTorchMode(id, false)
            capabilities.supportsDimming && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ->
                cameraManager.turnOnTorchWithStrengthLevel(id, validLevel)
            else -> cameraManager.setTorchMode(id, true)
        }
    }

    private fun findBackFlashCamera(): String? = runCatching {
        cameraManager.cameraIdList.firstOrNull { id ->
            val characteristics = cameraManager.getCameraCharacteristics(id)
            characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE) == true &&
                characteristics.get(CameraCharacteristics.LENS_FACING) ==
                CameraCharacteristics.LENS_FACING_BACK
        }
    }.getOrNull()

    private fun String.maximumStrengthLevel(): Int {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return 1
        return runCatching {
            cameraManager.getCameraCharacteristics(this)
                .get(CameraCharacteristics.FLASH_INFO_STRENGTH_MAXIMUM_LEVEL)
                ?.coerceAtLeast(1)
                ?: 1
        }.getOrDefault(1)
    }

    private fun currentStrengthLevel(id: String): Int {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return 1
        return runCatching { cameraManager.getTorchStrengthLevel(id) }
            .getOrDefault(1)
            .coerceAtLeast(1)
    }
}

