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

package com.wstxda.toolkit.manager.soundmode

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import android.os.Build
import com.wstxda.toolkit.permissions.PermissionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn

class SoundModeManager(context: Context) {

    private val appContext = context.applicationContext
    private val audioManager = appContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    private val permissionManager = PermissionManager(appContext)
    private val managerScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    val currentMode: StateFlow<SoundMode> = callbackFlow {
        trySend(getCurrentModeInternal())

        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (intent.action == AudioManager.RINGER_MODE_CHANGED_ACTION) {
                    trySend(getCurrentModeInternal())
                }
            }
        }

        appContext.registerReceiver(
            receiver,
            IntentFilter(AudioManager.RINGER_MODE_CHANGED_ACTION),
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                Context.RECEIVER_NOT_EXPORTED
            } else 0
        )

        awaitClose {
            try {
                appContext.unregisterReceiver(receiver)
            } catch (_: IllegalArgumentException) {
            }
        }
    }.distinctUntilChanged().stateIn(
        scope = managerScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = getCurrentModeInternal()
    )

    fun hasPermission(): Boolean = permissionManager.hasDoNotDisturbPermission()

    fun cycleMode() {
        if (!hasPermission()) return
        val newMode = when (getCurrentModeInternal()) {
            SoundMode.NORMAL -> SoundMode.VIBRATE
            SoundMode.VIBRATE -> SoundMode.SILENT
            SoundMode.SILENT -> SoundMode.NORMAL
        }
        audioManager.ringerMode = newMode.ringerMode
    }

    fun getCurrentModeInternal(): SoundMode = when (audioManager.ringerMode) {
        AudioManager.RINGER_MODE_VIBRATE -> SoundMode.VIBRATE
        AudioManager.RINGER_MODE_SILENT -> SoundMode.SILENT
        else -> SoundMode.NORMAL
    }

    fun cleanup() {
        managerScope.cancel()
    }
}