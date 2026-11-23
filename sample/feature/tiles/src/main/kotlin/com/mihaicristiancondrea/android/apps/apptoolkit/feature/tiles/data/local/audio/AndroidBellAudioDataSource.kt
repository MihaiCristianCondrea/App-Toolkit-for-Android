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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.local.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.R
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.coroutines.dispatchers.DispatcherProvider
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn

internal class AndroidBellAudioDataSource(
    private val context: Context,
    private val dispatchers: DispatcherProvider,
) {
    fun open(): Flow<((Float, Float) -> Boolean)?> = callbackFlow<((Float, Float) -> Boolean)?> {
        val pool = SoundPool.Builder()
            .setMaxStreams(3)
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build(),
            )
            .build()
        try {
            pool.setOnLoadCompleteListener { _, sample, status ->
                if (status == 0) {
                    trySend { volume, rate -> pool.play(sample, volume, volume, 1, 0, rate) != 0 }
                } else {
                    trySend(null)
                }
            }
            if (pool.load(context, R.raw.bell_strike, 1) == 0) trySend(null)
            awaitClose()
        } finally {
            pool.setOnLoadCompleteListener(null)
            pool.release()
        }
    }.flowOn(dispatchers.io)
}
