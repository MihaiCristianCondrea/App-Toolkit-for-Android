package com.d4rk.android.libs.apptoolkit.core.services

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.d4rk.android.libs.apptoolkit.core.logging.FCM_LOG_TAG

class FirebaseNotificationsService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(FCM_LOG_TAG, "Refreshed FCM token: $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d(FCM_LOG_TAG, "Received FCM message from ${message.from}")
        if (message.data.isNotEmpty()) {
            Log.d(FCM_LOG_TAG, "Message data payload size=${message.data.size}")
        }
    }
}