package com.d4rk.android.libs.apptoolkit.core.services.firebase.notifications

import android.util.Log
import com.d4rk.android.libs.apptoolkit.core.logging.FCM_LOG_TAG
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

/**
 * A service that extends [com.google.firebase.messaging.FirebaseMessagingService] to handle Firebase Cloud Messaging (FCM) events.
 *
 * This service is responsible for receiving new FCM registration tokens and handling incoming
 * push notifications.
 *
 * @see com.google.firebase.messaging.FirebaseMessagingService
 * @see com.google.firebase.messaging.RemoteMessage
 */
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