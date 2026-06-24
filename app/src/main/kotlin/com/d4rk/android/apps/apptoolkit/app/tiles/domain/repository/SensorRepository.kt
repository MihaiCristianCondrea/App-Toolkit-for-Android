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

package com.d4rk.android.apps.apptoolkit.app.tiles.domain.repository

import kotlinx.coroutines.flow.Flow

/**
 * Repository for accessing real-time sensor data.
 */
interface SensorRepository {
    /** Emits azimuth degrees [0, 360). */
    fun getCompassAzimuth(): Flow<Float>

    /** Emits pitch and roll degrees for a bubble level. */
    fun getLevelOrientation(): Flow<Pair<Float, Float>>

    /** Emits illuminance in lux. */
    fun getLuxLevel(): Flow<Float>

    /** Checks if a specific sensor is available on the device. */
    fun isSensorAvailable(sensorType: Int): Boolean
}
