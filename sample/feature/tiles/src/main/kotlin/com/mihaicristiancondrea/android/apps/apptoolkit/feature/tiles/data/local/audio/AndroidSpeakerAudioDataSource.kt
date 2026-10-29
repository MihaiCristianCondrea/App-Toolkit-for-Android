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
import android.media.AudioFocusRequest
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.os.Handler
import android.os.Looper
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.coroutines.dispatchers.DispatcherProvider
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.job
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlin.math.PI
import kotlin.math.sin

class AndroidSpeakerAudioDataSource(context: Context, private val dispatchers: DispatcherProvider) : SpeakerAudioDataSource {
    private val manager = context.applicationContext.getSystemService(AudioManager::class.java)
    private val playback = Mutex()
    override suspend fun playCycle(onSecond: (Int) -> Unit) = playback.withLock {
        withContext(dispatchers.io) {
            val job = currentCoroutineContext().job
            val attributes = AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build()
            val focus = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT)
                .setAudioAttributes(attributes).setAcceptsDelayedFocusGain(false)
                .setOnAudioFocusChangeListener({ change -> if (change < 0) job.cancel() }, Handler(Looper.getMainLooper())).build()
            check(manager.requestAudioFocus(focus) == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) { "Audio focus unavailable" }
            var track: AudioTrack? = null
            try {
                val pcm = ShortArray(SAMPLE_RATE) { i -> (sin(2 * PI * FREQUENCY * i / SAMPLE_RATE) * Short.MAX_VALUE).toInt().toShort() }
                currentCoroutineContext().ensureActive()
                val player = AudioTrack.Builder().setAudioAttributes(attributes).setAudioFormat(AudioFormat.Builder()
                    .setSampleRate(SAMPLE_RATE).setEncoding(AudioFormat.ENCODING_PCM_16BIT).setChannelMask(AudioFormat.CHANNEL_OUT_MONO).build())
                    .setBufferSizeInBytes(pcm.size * 2).setTransferMode(AudioTrack.MODE_STATIC).build()
                track = player
                check(player.write(pcm,0,pcm.size) == pcm.size) { "Could not prepare speaker tone" }
                check(player.setLoopPoints(0, pcm.size, 29) == AudioTrack.SUCCESS) { "Could not prepare speaker cycle" }
                player.setVolume(.12f)
                player.play()
                for (remaining in 30 downTo 1) { onSecond(remaining); delay(1000) }
                onSecond(0)
            } finally {
                try { track?.stop() } finally { track?.release(); manager.abandonAudioFocusRequest(focus) }
            }
        }
    }
    private companion object { const val SAMPLE_RATE = 44100; const val FREQUENCY = 165 }
}
