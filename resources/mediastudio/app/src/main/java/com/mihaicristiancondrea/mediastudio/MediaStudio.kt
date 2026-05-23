package com.mihaicristiancondrea.mediastudio

import android.app.Application
import androidx.annotation.OptIn
import androidx.media3.cast.Cast
import androidx.media3.common.util.UnstableApi
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import com.mihaicristiancondrea.mediastudio.core.di.playerModule

class MediaStudio : Application() {

    @OptIn(UnstableApi::class)
    override fun onCreate() {
        super.onCreate()

        runCatching {
            Cast.getSingletonInstance(this).initialize()
        }

        startKoin {
            androidLogger()
            androidContext(this@MediaStudio)
            modules(playerModule)
        }
    }
}
