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

package com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.repositories

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.hardware.display.DisplayManager
import android.os.Build
import android.view.Display
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.coroutines.dispatchers.DispatcherProvider
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlin.math.atan2
import kotlin.math.roundToInt
import kotlin.math.sqrt

/** Converts Android sensor callbacks into streams consumed by tile previews. */
class SensorRepository(
    private val context: Context,
    private val dispatchers: DispatcherProvider,
) {

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val displayManager = context.getSystemService(Context.DISPLAY_SERVICE) as DisplayManager

    private val rotation: Int
        get() = try {
            val display = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                // On Android 11+, display manager is safer for background contexts
                displayManager.getDisplay(Display.DEFAULT_DISPLAY)
                    ?: displayManager.displays.firstOrNull()
            } else {
                @Suppress("DEPRECATION")
                displayManager.getDisplay(Display.DEFAULT_DISPLAY)
            }
            display?.rotation ?: 0
        } catch (_: Exception) {
            0
        }

    fun getCompassAzimuth(): Flow<Float> = callbackFlow {
        val rotationMatrix = FloatArray(9)
        val remappedMatrix = FloatArray(9)
        val orientation = FloatArray(3)

        val gravity = FloatArray(3)
        val geomagnetic = FloatArray(3)
        var hasGravity = false
        var hasGeomagnetic = false

        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                event ?: return
                when (event.sensor.type) {
                    Sensor.TYPE_ROTATION_VECTOR,
                    Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR -> {
                        SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)
                        remapRotationMatrix(rotationMatrix, rotation, remappedMatrix)
                        SensorManager.getOrientation(remappedMatrix, orientation)
                        val azimuth =
                            (Math.toDegrees(orientation[0].toDouble()).roundToInt() + 360) % 360f
                        trySend(azimuth)
                    }

                    Sensor.TYPE_ACCELEROMETER -> {
                        System.arraycopy(event.values, 0, gravity, 0, event.values.size)
                        hasGravity = true
                        calculateAzimuthFromAccMag()
                    }

                    Sensor.TYPE_MAGNETIC_FIELD -> {
                        System.arraycopy(event.values, 0, geomagnetic, 0, event.values.size)
                        hasGeomagnetic = true
                        calculateAzimuthFromAccMag()
                    }
                }
            }

            private fun calculateAzimuthFromAccMag() {
                if (hasGravity && hasGeomagnetic) {
                    if (SensorManager.getRotationMatrix(
                            rotationMatrix,
                            null,
                            gravity,
                            geomagnetic,
                        )
                    ) {
                        remapRotationMatrix(rotationMatrix, rotation, remappedMatrix)
                        SensorManager.getOrientation(remappedMatrix, orientation)
                        val azimuth =
                            (Math.toDegrees(orientation[0].toDouble()).roundToInt() + 360) % 360f
                        trySend(azimuth)
                    }
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
        }

        val rotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
            ?: sensorManager.getDefaultSensor(Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR)

        if (rotationSensor != null) {
            sensorManager.registerListener(listener, rotationSensor, SensorManager.SENSOR_DELAY_UI)
        } else {
            val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
            val magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
            if (accelerometer != null && magnetometer != null) {
                sensorManager.registerListener(
                    listener,
                    accelerometer,
                    SensorManager.SENSOR_DELAY_UI,
                )
                sensorManager.registerListener(
                    listener,
                    magnetometer,
                    SensorManager.SENSOR_DELAY_UI,
                )
            } else {
                close()
            }
        }

        awaitClose { sensorManager.unregisterListener(listener) }
    }.flowOn(dispatchers.default)

    fun getLevelOrientation(): Flow<Pair<Float, Float>> = callbackFlow {
        val rotationMatrix = FloatArray(9)
        val remappedMatrix = FloatArray(9)
        val orientation = FloatArray(3)

        val gravity = FloatArray(3)
        val geomagnetic = FloatArray(3)
        var hasGravity = false
        var hasGeomagnetic = false

        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                event ?: return
                when (event.sensor.type) {
                    Sensor.TYPE_ROTATION_VECTOR,
                    Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR -> {
                        SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)
                        remapRotationMatrix(rotationMatrix, rotation, remappedMatrix)
                        SensorManager.getOrientation(remappedMatrix, orientation)
                        val pitch = Math.toDegrees(orientation[1].toDouble()).toFloat()
                        val roll = Math.toDegrees(orientation[2].toDouble()).toFloat()
                        trySend(pitch to roll)
                    }

                    Sensor.TYPE_ACCELEROMETER -> {
                        System.arraycopy(event.values, 0, gravity, 0, event.values.size)
                        hasGravity = true
                        calculateOrientationFromAccMag()
                    }

                    Sensor.TYPE_MAGNETIC_FIELD -> {
                        System.arraycopy(event.values, 0, geomagnetic, 0, event.values.size)
                        hasGeomagnetic = true
                        calculateOrientationFromAccMag()
                    }
                }
            }

            private fun calculateOrientationFromAccMag() {
                if (hasGravity && hasGeomagnetic) {
                    if (SensorManager.getRotationMatrix(
                            rotationMatrix,
                            null,
                            gravity,
                            geomagnetic,
                        )
                    ) {
                        remapRotationMatrix(rotationMatrix, rotation, remappedMatrix)
                        SensorManager.getOrientation(remappedMatrix, orientation)
                        val pitch = Math.toDegrees(orientation[1].toDouble()).toFloat()
                        val roll = Math.toDegrees(orientation[2].toDouble()).toFloat()
                        trySend(pitch to roll)
                    }
                } else if (hasGravity && !hasGeomagnetic) {
                    // Fallback to accelerometer-only for pitch/roll if magnetometer is missing.
                    val normGravity =
                        gravity[0] * gravity[0] + gravity[1] * gravity[1] + gravity[2] * gravity[2]
                    if (normGravity > 0.1f) {
                        val pitch = Math.toDegrees(
                            atan2(gravity[1].toDouble(), gravity[2].toDouble()),
                        ).toFloat()
                        val roll = Math.toDegrees(
                            atan2(
                                -gravity[0].toDouble(),
                                sqrt((gravity[1] * gravity[1] + gravity[2] * gravity[2]).toDouble()),
                            ),
                        ).toFloat()
                        trySend(pitch to roll)
                    }
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
        }

        val rotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
            ?: sensorManager.getDefaultSensor(Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR)

        if (rotationSensor != null) {
            sensorManager.registerListener(listener, rotationSensor, SensorManager.SENSOR_DELAY_UI)
        } else {
            val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
            val magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

            if (accelerometer != null) {
                sensorManager.registerListener(
                    listener,
                    accelerometer,
                    SensorManager.SENSOR_DELAY_UI,
                )
            }
            if (magnetometer != null) {
                sensorManager.registerListener(
                    listener,
                    magnetometer,
                    SensorManager.SENSOR_DELAY_UI,
                )
            }

            if (accelerometer == null) {
                close()
            }
        }

        awaitClose { sensorManager.unregisterListener(listener) }
    }.flowOn(dispatchers.default)

    fun getLuxLevel(): Flow<Float> = callbackFlow {
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

    private fun remapRotationMatrix(
        rotationMatrix: FloatArray,
        displayRotation: Int,
        remappedMatrix: FloatArray,
    ) {
        val axisX = SensorManager.AXIS_X
        val axisY = SensorManager.AXIS_Y
        val (newX, newY) = when (displayRotation) {
            android.view.Surface.ROTATION_90 -> Pair(axisY, SensorManager.AXIS_MINUS_X)
            android.view.Surface.ROTATION_180 -> Pair(
                SensorManager.AXIS_MINUS_X,
                SensorManager.AXIS_MINUS_Y,
            )

            android.view.Surface.ROTATION_270 -> Pair(SensorManager.AXIS_MINUS_Y, axisX)
            else -> Pair(axisX, axisY)
        }
        SensorManager.remapCoordinateSystem(rotationMatrix, newX, newY, remappedMatrix)
    }
}
