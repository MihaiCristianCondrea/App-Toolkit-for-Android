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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.os.PowerManager
import androidx.core.app.NotificationCompat
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.repositories.CaffeineRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.core.ui.R as CoreUiR
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import kotlin.time.Duration.Companion.milliseconds

class CaffeineService : Service() {

    private val repository: CaffeineRepository by inject()
    private val serviceScope = CoroutineScope(Dispatchers.Main + Job())

    private var wakeLock: PowerManager.WakeLock? = null
    private var timerJob: Job? = null

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val durationMillis = intent?.getLongExtra(EXTRA_DURATION, -1L) ?: -1L

        startForeground(NOTIFICATION_ID, createNotification())
        acquireWakeLock()

        timerJob?.cancel()
        if (durationMillis > 0) {
            timerJob = serviceScope.launch {
                delay(durationMillis.milliseconds)
                repository.reset()
                stopSelf()
            }
        }

        return START_NOT_STICKY
    }

    /**
     * Holds the screen on for the caffeine tile.
     *
     * [PowerManager.SCREEN_BRIGHT_WAKE_LOCK] is deprecated in favour of
     * `WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON`, but that replacement needs a window and this
     * runs in a service with no UI of its own, keeping the screen awake for whatever app is in the
     * foreground is the whole point of the tile. The deprecated flag is still honoured, so it stays
     * with the suppression documented rather than silenced.
     */
    @Suppress("DEPRECATION")
    private fun acquireWakeLock() {
        if (wakeLock?.isHeld == true) return

        val powerManager = getSystemService(POWER_SERVICE) as PowerManager
        wakeLock = powerManager.newWakeLock(
            PowerManager.SCREEN_BRIGHT_WAKE_LOCK or PowerManager.ON_AFTER_RELEASE,
            WAKE_LOCK_TAG
        ).apply {
            acquire(WAKE_LOCK_TIMEOUT_MS)
        }
    }

    private fun releaseWakeLock() {
        wakeLock?.let {
            if (it.isHeld) {
                it.release()
            }
        }
        wakeLock = null
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            getString(R.string.caffeine_notification_channel_name),
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = getString(R.string.caffeine_notification_channel_description)
        }
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }

    private fun createNotification(): Notification {
        // Resolved through the package manager rather than naming the activity class: the launcher
        // activity lives in the app module, and a quick-tool service should not have to depend on
        // the whole app to put a tap target on its notification.
        val intent = packageManager.getLaunchIntentForPackage(packageName)
        val pendingIntent = intent?.let {
            PendingIntent.getActivity(this, 0, it, PendingIntent.FLAG_IMMUTABLE)
        }

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(getString(R.string.tile_caffeine_title))
            .setContentText(getString(R.string.caffeine_notification_text))
            .setSmallIcon(CoreUiR.drawable.ic_tile_caffeine)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()
    }

    override fun onDestroy() {
        super.onDestroy()
        releaseWakeLock()
        timerJob?.cancel()
    }

    companion object {
        private const val NOTIFICATION_ID = 1001
        private const val CHANNEL_ID = "caffeine_channel"
        private const val EXTRA_DURATION = "extra_duration"
        private const val WAKE_LOCK_TAG = "AppToolkit:CaffeineWakeLock"

        /** Safety net so a leaked lock cannot drain the battery indefinitely. */
        private const val WAKE_LOCK_TIMEOUT_MS = 10 * 60 * 1000L

        fun start(context: Context, durationMillis: Long?) {
            val intent = Intent(context, CaffeineService::class.java).apply {
                durationMillis?.let { putExtra(EXTRA_DURATION, it) }
            }
            context.startForegroundService(intent)
        }

        fun stop(context: Context) {
            val intent = Intent(context, CaffeineService::class.java)
            context.stopService(intent)
        }
    }
}
