@file:Suppress("unused")

package com.mihaicristiancondrea.libs.mediaplayer.data.cast

import android.content.Context
import com.google.android.gms.cast.framework.CastOptions
import com.google.android.gms.cast.framework.OptionsProvider
import com.google.android.gms.cast.framework.SessionProvider
import com.google.android.gms.cast.framework.media.CastMediaOptions
import com.mihaicristiancondrea.libs.mediaplayer.R

/*
False-positive unused error. The class is specified into the manifest of the library to ensure cast support.
*/
class CastOptionsProvider : OptionsProvider {

    override fun getCastOptions(context: Context): CastOptions {
        val castMediaOptions = CastMediaOptions.Builder()
                .setMediaSessionEnabled(true)
                .build()

        return CastOptions.Builder()
            .setReceiverApplicationId(context.getString(R.string.cast_app_id))
            .setCastMediaOptions(castMediaOptions)
            .setResumeSavedSession(true)
            .setEnableReconnectionService(true)
            .build()
    }

    override fun getAdditionalSessionProviders(context: Context): List<SessionProvider>? {
        return null
    }
}
