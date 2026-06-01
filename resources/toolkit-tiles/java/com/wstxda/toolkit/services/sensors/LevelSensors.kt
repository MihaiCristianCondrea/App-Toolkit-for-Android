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

package com.wstxda.toolkit.services.sensors

import android.hardware.SensorEvent
import android.hardware.SensorManager
import android.view.Surface
import com.wstxda.toolkit.manager.level.LevelMode
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt

fun getOrientation(
    event: SensorEvent,
    displayRotation: Int,
    rotationMatrix: FloatArray,
    remappedMatrix: FloatArray,
    orientation: FloatArray
): Orientation {
    SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)

    remapRotationMatrix(rotationMatrix, displayRotation, remappedMatrix)

    SensorManager.getOrientation(remappedMatrix, orientation)

    val pitch = Math.toDegrees(orientation[1].toDouble()).toFloat()
    val roll = Math.toDegrees(orientation[2].toDouble()).toFloat()

    val mode = if (abs(pitch) > 45f || abs(roll) > 45f) LevelMode.Line else LevelMode.Dot

    val gx = remappedMatrix.getOrNull(8) ?: 0f
    val gy = remappedMatrix.getOrNull(9) ?: 0f

    val balance = Math.toDegrees(atan2(gx.toDouble(), gy.toDouble())).toFloat()
    val adjustedBalance = adjustBalance(balance)

    return Orientation(pitch, roll, adjustedBalance, mode)
}

fun getTilt(pitch: Float, roll: Float): Int {
    val magnitude = sqrt(pitch.pow(2) + roll.pow(2)).roundToInt()
    return if (abs(roll) >= abs(pitch)) {
        if (roll >= 0) magnitude else -magnitude
    } else {
        if (pitch >= 0) magnitude else -magnitude
    }
}

private fun adjustBalance(balance: Float): Float {
    val baseAngle = (balance / 90f).roundToInt() * 90f
    return if (baseAngle == 0f) balance else baseAngle - balance
}

private fun remapRotationMatrix(
    rotationMatrix: FloatArray, displayRotation: Int, remappedMatrix: FloatArray
) {
    val (newX, newY) = when (displayRotation) {
        Surface.ROTATION_90 -> Pair(SensorManager.AXIS_Y, SensorManager.AXIS_MINUS_X)
        Surface.ROTATION_180 -> Pair(SensorManager.AXIS_MINUS_X, SensorManager.AXIS_MINUS_Y)
        Surface.ROTATION_270 -> Pair(SensorManager.AXIS_MINUS_Y, SensorManager.AXIS_X)
        else -> Pair(SensorManager.AXIS_X, SensorManager.AXIS_Y)
    }
    SensorManager.remapCoordinateSystem(rotationMatrix, newX, newY, remappedMatrix)
}