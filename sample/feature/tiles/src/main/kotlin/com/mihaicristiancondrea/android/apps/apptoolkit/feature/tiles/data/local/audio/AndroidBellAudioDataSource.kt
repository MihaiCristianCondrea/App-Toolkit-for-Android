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
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume

class AndroidBellAudioDataSource(
    private val context: Context,
    private val dispatchers: DispatcherProvider,
) : BellAudioDataSource {

    override suspend fun runSession(
        onAudioReady: (Boolean) -> Unit,
        session: suspend (play: suspend (volume: Float, rate: Float) -> Boolean) -> Unit,
    ) {
        val pool = withContext(dispatchers.io) {
            SoundPool.Builder()
                .setMaxStreams(MAX_STREAMS)
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_GAME)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build(),
                )
                .build()
        }
        try {
            val sampleId = withContext(dispatchers.io) { loadSample(pool = pool) }
            if (sampleId == null) {
                onAudioReady(false)
                return
            }
            onAudioReady(true)
            session { volume, rate ->
                withContext(dispatchers.io) {
                    pool.play(sampleId, volume, volume, PRIORITY, NO_LOOP, rate) != 0
                }
            }
        } finally {
            withContext(NonCancellable + dispatchers.io) {
                pool.setOnLoadCompleteListener(null)
                pool.release()
            }
        }
    }

    private suspend fun loadSample(pool: SoundPool): Int? =
        suspendCancellableCoroutine { continuation ->
            pool.setOnLoadCompleteListener { _, sampleId, status ->
                if (continuation.isActive) {
                    continuation.resume(if (status == 0) sampleId else null)
                }
            }
            continuation.invokeOnCancellation {
                pool.setOnLoadCompleteListener(null)
            }
            if (pool.load(context, R.raw.bell_strike, PRIORITY) == 0 && continuation.isActive) {
                continuation.resume(null)
            }
        }

    private companion object {
        const val MAX_STREAMS: Int = 3
        const val PRIORITY: Int = 1
        const val NO_LOOP: Int = 0
    }
}
