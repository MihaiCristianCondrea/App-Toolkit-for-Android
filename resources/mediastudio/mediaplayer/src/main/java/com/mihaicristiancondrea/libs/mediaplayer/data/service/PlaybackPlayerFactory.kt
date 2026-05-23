package com.mihaicristiancondrea.libs.mediaplayer.data.service

import android.content.Context
import android.widget.FrameLayout
import androidx.annotation.OptIn
import androidx.media3.common.AdViewProvider
import androidx.media3.cast.CastPlayer
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.ima.ImaServerSideAdInsertionMediaSource
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory

@OptIn(UnstableApi::class)
class PlaybackPlayerFactory {

    private var ssaiAdsLoader: ImaServerSideAdInsertionMediaSource.AdsLoader? = null

    fun create(context: Context): Player {
        ssaiAdsLoader?.release()

        val audioAttributes = AudioAttributes.Builder()
            .setUsage(C.USAGE_MEDIA)
            .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
            .build()

        val dataSourceFactory = DefaultDataSource.Factory(context)
        val contentMediaSourceFactory = DefaultMediaSourceFactory(dataSourceFactory)
        val createdSsaiAdsLoader = ImaServerSideAdInsertionMediaSource.AdsLoader.Builder(context, DetachedAdViewProvider(context)).build()
        val mediaSourceFactory = DefaultMediaSourceFactory(dataSourceFactory).setServerSideAdInsertionMediaSourceFactory(ImaServerSideAdInsertionMediaSource.Factory(createdSsaiAdsLoader, contentMediaSourceFactory))

        val localPlayer = ExoPlayer.Builder(context).setMediaSourceFactory(mediaSourceFactory).build()
            .apply {
                setAudioAttributes(audioAttributes, true)
                setHandleAudioBecomingNoisy(true)
            }

        createdSsaiAdsLoader.setPlayer(localPlayer)
        ssaiAdsLoader = createdSsaiAdsLoader

        return CastPlayer.Builder(context).setLocalPlayer(localPlayer).build()
    }

    fun release(player: Player) {
        player.release()
        ssaiAdsLoader?.release()
        ssaiAdsLoader = null
    }

    private class DetachedAdViewProvider(context: Context) : AdViewProvider {
        private val adViewGroup = FrameLayout(context)

        override fun getAdViewGroup(): FrameLayout {
            return adViewGroup
        }
    }
}
