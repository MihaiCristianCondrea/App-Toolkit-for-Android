package com.mihaicristiancondrea.libs.mediaplayer.data.service

import android.content.Context
import android.content.Intent

// Used to sent the context of the host app activity used for the intent when the user taps the medai3 notification
fun interface PlaybackSessionIntentProvider {

    fun createSessionActivityIntent(context: Context): Intent
}