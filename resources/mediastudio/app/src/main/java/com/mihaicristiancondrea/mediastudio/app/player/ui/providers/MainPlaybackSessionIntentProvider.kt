package com.mihaicristiancondrea.mediastudio.app.player.ui.providers

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import com.mihaicristiancondrea.libs.mediaplayer.data.service.PlaybackSessionIntentProvider

// TODO: maybe making a bit different to be smaller code and be more fit
class MainPlaybackSessionIntentProvider : PlaybackSessionIntentProvider {
    override fun createSessionActivityIntent(context: Context): Intent {
        return Intent(Intent.ACTION_VIEW , "https://mihaicritiancondrea.ro/player".toUri()).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        }
    }
}