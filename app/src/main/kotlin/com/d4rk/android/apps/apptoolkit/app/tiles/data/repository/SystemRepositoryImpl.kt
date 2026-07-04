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

package com.d4rk.android.apps.apptoolkit.app.tiles.data.repository

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import android.provider.Settings
import com.d4rk.android.apps.apptoolkit.app.tiles.domain.repository.RingerMode
import com.d4rk.android.apps.apptoolkit.app.tiles.domain.repository.SystemRepository
import com.d4rk.android.libs.apptoolkit.core.coroutines.dispatchers.DispatcherProvider
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn

class SystemRepositoryImpl(
    private val context: Context,
    private val dispatchers: DispatcherProvider,
) : SystemRepository {

    private val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    override fun getRingerMode(): Flow<RingerMode> = callbackFlow {
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.action == AudioManager.RINGER_MODE_CHANGED_ACTION) {
                    trySend(currentRingerMode())
                }
            }
        }

        context.registerReceiver(receiver, IntentFilter(AudioManager.RINGER_MODE_CHANGED_ACTION))
        trySend(currentRingerMode())

        awaitClose { context.unregisterReceiver(receiver) }
    }.flowOn(dispatchers.default)

    override fun setRingerMode(mode: RingerMode) {
        try {
            if (!notificationManager.isNotificationPolicyAccessGranted) {
                val intent = Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(intent)
                throw SecurityException("Do Not Disturb access not granted")
            }

            val systemMode = when (mode) {
                RingerMode.Normal -> AudioManager.RINGER_MODE_NORMAL
                RingerMode.Vibrate -> AudioManager.RINGER_MODE_VIBRATE
                RingerMode.Silent -> AudioManager.RINGER_MODE_SILENT
            }
            audioManager.ringerMode = systemMode
        } catch (_: SecurityException) {
            throw SecurityException("Do Not Disturb access not granted")
        } catch (_: Exception) {
            // General safety
        }
    }

    override fun launchMusicSearch() {
        try {
            val intent = Intent("com.google.android.googlequicksearchbox.MUSIC_SEARCH").apply {
                setPackage("com.google.android.googlequicksearchbox")
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (_: Exception) {
            // Fallback
        }
    }

    private fun currentRingerMode(): RingerMode = when (audioManager.ringerMode) {
        AudioManager.RINGER_MODE_VIBRATE -> RingerMode.Vibrate
        AudioManager.RINGER_MODE_SILENT -> RingerMode.Silent
        else -> RingerMode.Normal
    }
}
