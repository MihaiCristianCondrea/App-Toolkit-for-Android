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

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

/**
 * Sensor readings a test emits by hand. [subscriptionCount] tells whether a tool is listening, and
 * [failure], when set, makes the next listener fail before its first reading.
 */
internal class FakeSensorLocalDataSource : SensorLocalDataSource {
    val azimuth: MutableSharedFlow<Float> = MutableSharedFlow(extraBufferCapacity = BUFFER)
    val orientation: MutableSharedFlow<Pair<Float, Float>> = MutableSharedFlow(extraBufferCapacity = BUFFER)
    var failure: Throwable? = null

    val subscriptionCount: Int
        get() = azimuth.subscriptionCount.value + orientation.subscriptionCount.value

    override fun getCompassAzimuth(): Flow<Float> = readings(azimuth)

    override fun getLevelOrientation(): Flow<Pair<Float, Float>> = readings(orientation)

    private fun <T> readings(source: MutableSharedFlow<T>): Flow<T> = flow {
        failure?.let { error -> throw error }
        emitAll(source)
    }

    private companion object {
        const val BUFFER: Int = 8
    }
}
