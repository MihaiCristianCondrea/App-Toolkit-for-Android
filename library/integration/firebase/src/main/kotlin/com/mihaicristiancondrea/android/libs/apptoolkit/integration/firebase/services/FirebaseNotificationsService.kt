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

package com.mihaicristiancondrea.android.libs.apptoolkit.integration.firebase.services

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.logging.FCM_LOG_TAG

/**
 * Default [FirebaseMessagingService] entry point registered by `:library:integration:firebase`.
 *
 * Receives FCM registration callbacks and foreground messages without logging installation
 * identifiers to Logcat. This service does not render notifications on its own; hosts that handle
 * custom data payloads or foreground notifications can subclass or replace it in their manifest.
 */
class FirebaseNotificationsService : FirebaseMessagingService() {

    override fun onRegistered(installationId: String) {
        super.onRegistered(installationId)
        Log.d(FCM_LOG_TAG, "FCM installation registered.")
    }

    override fun onUnregistered(installationId: String) {
        super.onUnregistered(installationId)
        Log.d(FCM_LOG_TAG, "FCM installation unregistered.")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d(FCM_LOG_TAG, "Received FCM message (dataKeys=${message.data.size}).")
    }
}
