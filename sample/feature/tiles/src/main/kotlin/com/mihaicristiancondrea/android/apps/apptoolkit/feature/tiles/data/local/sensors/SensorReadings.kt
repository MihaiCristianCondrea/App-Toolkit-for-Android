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

import kotlin.math.roundToInt

/**
 * Whether every value of a sensor reading is a real number. Some sensor drivers report NaN or
 * infinity, for example while they calibrate; such a reading carries no direction, and every angle
 * computed from it comes out NaN.
 */
internal fun FloatArray.isFiniteReading(): Boolean = all { it.isFinite() }

/**
 * The compass heading for an azimuth of [radians], as `SensorManager.getOrientation` gives it, in
 * whole degrees from 0 to 359. Null when the azimuth is not a real number, which `roundToInt`
 * would refuse.
 */
internal fun azimuthDegrees(radians: Float): Float? {
    if (!radians.isFinite()) return null
    val degrees = Math.toDegrees(radians.toDouble()).roundToInt()
    return (((degrees % FULL_TURN) + FULL_TURN) % FULL_TURN).toFloat()
}

/**
 * Pitch and roll in degrees for angles in radians, as `SensorManager.getOrientation` gives them.
 * Null when either is not a real number, so a level never draws a NaN tilt.
 */
internal fun tiltDegrees(pitchRadians: Float, rollRadians: Float): Pair<Float, Float>? {
    if (!pitchRadians.isFinite() || !rollRadians.isFinite()) return null
    return Math.toDegrees(pitchRadians.toDouble()).toFloat() to Math.toDegrees(rollRadians.toDouble()).toFloat()
}

private const val FULL_TURN = 360
