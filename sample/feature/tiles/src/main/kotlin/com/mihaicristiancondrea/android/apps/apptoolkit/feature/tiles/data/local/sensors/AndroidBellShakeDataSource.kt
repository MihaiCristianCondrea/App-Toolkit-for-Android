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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.local.sensors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.coroutines.dispatchers.DispatcherProvider
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn

internal class AndroidBellShakeDataSource(
    context: Context,
    private val dispatchers: DispatcherProvider,
) {
    private val sensors = context.getSystemService(SensorManager::class.java)

    fun observe(): Flow<Float?> = callbackFlow<Float?> {
        val manager = sensors
        val linear = manager?.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)
        val sensor = linear ?: manager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        if (manager == null || sensor == null) {
            trySend(null)
            close()
            return@callbackFlow
        }

        val detector = BellShakeDetector()
        val gravity = FloatArray(3)
        var gravityInitialized = false
        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                val values = event.values
                if (linear == null) {
                    for (index in gravity.indices) {
                        gravity[index] = if (gravityInitialized) {
                            gravity[index] + 0.15f * (values[index] - gravity[index])
                        } else {
                            values[index]
                        }
                    }
                    gravityInitialized = true
                }
                detector.strikeStrength(
                    x = values[0] - gravity[0],
                    y = values[1] - gravity[1],
                    z = values[2] - gravity[2],
                    timeNanos = event.timestamp,
                )?.let { trySend(it) }
            }

            override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) = Unit
        }
        if (!manager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_GAME)) {
            trySend(null)
            close()
            return@callbackFlow
        }
        awaitClose { manager.unregisterListener(listener) }
    }.flowOn(dispatchers.io)
}
