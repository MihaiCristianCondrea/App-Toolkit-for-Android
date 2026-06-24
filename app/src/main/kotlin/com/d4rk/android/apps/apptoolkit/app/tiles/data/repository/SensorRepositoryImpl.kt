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

package com.d4rk.android.apps.apptoolkit.app.tiles.data.repository

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.view.WindowManager
import com.d4rk.android.apps.apptoolkit.app.tiles.domain.repository.SensorRepository
import com.d4rk.android.libs.apptoolkit.core.coroutines.dispatchers.DispatcherProvider
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlin.math.roundToInt

class SensorRepositoryImpl(
    private val context: Context,
    private val dispatchers: DispatcherProvider,
) : SensorRepository {

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

    override fun getCompassAzimuth(): Flow<Float> = callbackFlow {
        val rotationMatrix = FloatArray(9)
        val remappedMatrix = FloatArray(9)
        val orientation = FloatArray(3)

        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                event?.let {
                    SensorManager.getRotationMatrixFromVector(rotationMatrix, it.values)
                    val rotation =
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                            context.display.rotation
                        } else {
                            @Suppress("DEPRECATION")
                            windowManager.defaultDisplay.rotation
                        }
                    remapRotationMatrix(rotationMatrix, rotation, remappedMatrix)
                    SensorManager.getOrientation(remappedMatrix, orientation)
                    val azimuth =
                        (Math.toDegrees(orientation[0].toDouble()).roundToInt() + 360) % 360f
                    trySend(azimuth)
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
        }

        val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
            ?: sensorManager.getDefaultSensor(Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR)

        if (sensor != null) {
            sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_UI)
        } else {
            close()
        }

        awaitClose { sensorManager.unregisterListener(listener) }
    }.flowOn(dispatchers.default)

    override fun getLevelOrientation(): Flow<Pair<Float, Float>> = callbackFlow {
        val rotationMatrix = FloatArray(9)
        val remappedMatrix = FloatArray(9)
        val orientation = FloatArray(3)

        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                event?.let {
                    SensorManager.getRotationMatrixFromVector(rotationMatrix, it.values)
                    val rotation =
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                            context.display.rotation
                        } else {
                            @Suppress("DEPRECATION")
                            windowManager.defaultDisplay.rotation
                        }
                    remapRotationMatrix(rotationMatrix, rotation, remappedMatrix)
                    SensorManager.getOrientation(remappedMatrix, orientation)
                    val pitch = Math.toDegrees(orientation[1].toDouble()).toFloat()
                    val roll = Math.toDegrees(orientation[2].toDouble()).toFloat()
                    trySend(pitch to roll)
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
        }

        val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
        if (sensor != null) {
            sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_UI)
        } else {
            close()
        }

        awaitClose { sensorManager.unregisterListener(listener) }
    }.flowOn(dispatchers.default)

    override fun getLuxLevel(): Flow<Float> = callbackFlow {
        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                event?.let { trySend(it.values[0]) }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
        }

        val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
        if (sensor != null) {
            sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_UI)
        } else {
            close()
        }

        awaitClose { sensorManager.unregisterListener(listener) }
    }.flowOn(dispatchers.default)

    override fun isSensorAvailable(sensorType: Int): Boolean {
        return sensorManager.getDefaultSensor(sensorType) != null
    }

    private fun remapRotationMatrix(
        rotationMatrix: FloatArray, displayRotation: Int, remappedMatrix: FloatArray
    ) {
        val axisX = SensorManager.AXIS_X
        val axisY = SensorManager.AXIS_Y
        val (newX, newY) = when (displayRotation) {
            android.view.Surface.ROTATION_90 -> Pair(axisY, SensorManager.AXIS_MINUS_X)
            android.view.Surface.ROTATION_180 -> Pair(
                SensorManager.AXIS_MINUS_X,
                SensorManager.AXIS_MINUS_Y
            )

            android.view.Surface.ROTATION_270 -> Pair(SensorManager.AXIS_MINUS_Y, axisX)
            else -> Pair(axisX, axisY)
        }
        SensorManager.remapCoordinateSystem(rotationMatrix, newX, newY, remappedMatrix)
    }
}
