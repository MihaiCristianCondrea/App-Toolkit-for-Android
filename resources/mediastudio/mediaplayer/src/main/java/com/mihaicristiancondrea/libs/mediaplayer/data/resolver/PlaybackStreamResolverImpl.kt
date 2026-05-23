package com.mihaicristiancondrea.libs.mediaplayer.data.resolver

import androidx.annotation.OptIn
import androidx.media3.common.C
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ima.ImaServerSideAdInsertionUriBuilder
import com.mihaicristiancondrea.libs.mediaplayer.domain.interfaces.PlaybackStreamResolver
import com.mihaicristiancondrea.libs.mediaplayer.domain.models.PlaybackAsset
import com.mihaicristiancondrea.libs.mediaplayer.domain.models.PlaybackStreamFormat
import com.mihaicristiancondrea.libs.mediaplayer.domain.models.PlaybackStreamSource
import com.mihaicristiancondrea.libs.mediaplayer.domain.models.ResolvedPlaybackStream

@OptIn(UnstableApi::class)
class PlaybackStreamResolverImpl : PlaybackStreamResolver {

    override suspend fun resolve(asset: PlaybackAsset): ResolvedPlaybackStream {
        return when (val source = asset.streamSource) {
            is PlaybackStreamSource.Direct -> ResolvedPlaybackStream(uri = source.uri, format = source.format)
            is PlaybackStreamSource.Proxied -> ResolvedPlaybackStream(uri = source.proxyEndpoint, headers = source.headers, format = source.format)
            is PlaybackStreamSource.ServerSideDai -> ResolvedPlaybackStream(uri = source.proxyEndpoint ?: source.manifestUri ?: source.toImaDaiUri(), headers = source.headers, format = source.format)

        }
    }

    // TODO: This is a mapper and should be set to mappers
    private fun PlaybackStreamSource.ServerSideDai.toImaDaiUri(): String {
        return ImaServerSideAdInsertionUriBuilder()
            .setAssetKey(assetKey)
            .setContentSourceId(contentSourceId)
            .setVideoId(videoId)
            .setFormat(format.toMedia3ContentType())
            .build()
            .toString()
    }


    // TODO: this is also a mapper and we should see where to move it
    @C.ContentType
    private fun PlaybackStreamFormat.toMedia3ContentType(): Int {
        return when (this) {
            PlaybackStreamFormat.Dash -> C.CONTENT_TYPE_DASH
            else -> C.CONTENT_TYPE_HLS
        }
    }
}
