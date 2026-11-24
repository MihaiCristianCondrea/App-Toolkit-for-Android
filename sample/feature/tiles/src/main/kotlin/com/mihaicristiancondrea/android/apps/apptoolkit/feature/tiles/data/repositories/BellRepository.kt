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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.repositories

import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.local.audio.BellAudioDataSource
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.local.sensors.BellShakeDataSource
import kotlinx.coroutines.flow.Flow

/** Coordinates foreground bell audio playback and shake observations. */
class BellRepository(
    private val audio: BellAudioDataSource,
    private val sensors: BellShakeDataSource,
) {
    fun shakes(): Flow<Float?> = sensors.observe()

    suspend fun play(
        strikes: Flow<Float>,
        onAudioReady: (Boolean) -> Unit,
        onStrike: (Float) -> Unit,
    ) {
        var even = false
        audio.runSession(onAudioReady = onAudioReady) { playSample ->
            strikes.collect { strength ->
                val rate = if (even) 1.0052f else 0.9948f
                val volume = strength * if (even) 0.36f else 0.32f
                if (playSample(volume, rate)) {
                    even = !even
                    onStrike(rate)
                }
            }
        }
    }
}
