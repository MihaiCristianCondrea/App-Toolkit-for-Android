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

package com.d4rk.android.apps.apptoolkit.app.tiles.domain.usecase

import com.d4rk.android.apps.apptoolkit.app.tiles.domain.repository.SensorRepository
import kotlinx.coroutines.flow.Flow

/**
 * Use cases for accessing real-time sensor data.
 */
class GetSensorDataUseCase(private val sensorRepository: SensorRepository) {
    fun getCompassAzimuth(): Flow<Float> = sensorRepository.getCompassAzimuth()
    fun getLevelOrientation(): Flow<Pair<Float, Float>> = sensorRepository.getLevelOrientation()
    fun getLuxLevel(): Flow<Float> = sensorRepository.getLuxLevel()
}
