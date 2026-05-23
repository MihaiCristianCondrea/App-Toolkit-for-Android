package com.mihaicristiancondrea.libs.mediaplayer.data.mappers

import android.net.Uri
import android.os.Bundle
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.MimeTypes
import com.mihaicristiancondrea.libs.mediaplayer.domain.models.PlaybackAds
import com.mihaicristiancondrea.libs.mediaplayer.domain.models.PlaybackAsset
import com.mihaicristiancondrea.libs.mediaplayer.domain.models.PlaybackAssetType
import com.mihaicristiancondrea.libs.mediaplayer.domain.models.PlaybackQueueType
import com.mihaicristiancondrea.libs.mediaplayer.domain.models.PlaybackStreamFormat
import com.mihaicristiancondrea.libs.mediaplayer.domain.models.ResolvedPlaybackStream

object PlaybackMediaItemExtras {
    const val PackageName = "com.mihaicristiancondrea.mediastudio"
    const val AssetType = "$PackageName.extra.ASSET_TYPE"
    const val QueueType = "$PackageName.extra.QUEUE_TYPE"
    const val SupportsSeek = "$PackageName.extra.SUPPORTS_SEEK"
    const val SupportsPrevious = "$PackageName.extra.SUPPORTS_PREVIOUS"
    const val SupportsNext = "$PackageName.extra.SUPPORTS_NEXT"
    const val SupportsCast = "$PackageName.extra.SUPPORTS_CAST"
    const val SupportsPiP = "$PackageName.extra.SUPPORTS_PIP"
}

fun PlaybackAsset.toMediaItem(
    resolvedStream: ResolvedPlaybackStream,
    queueType: PlaybackQueueType = PlaybackQueueType.Single,
): MediaItem {
    val builder = toMediaItem(queueType)
        .buildUpon()
        .setUri(resolvedStream.uri)
        .setMimeType(resolvedStream.format.toMimeType() ?: mimeType(resolvedStream.uri))

    when (val ads = ads) {
        PlaybackAds.None -> Unit
        is PlaybackAds.ClientSideVast -> {
            builder.setAdsConfiguration(
                MediaItem.AdsConfiguration.Builder(ads.adTagUri.toUri())
                    .setAdsId(ads.adTagUri)
                    .build()
            )
        }
    }

    return builder.build()
}

fun PlaybackAsset.toMediaItem(queueType: PlaybackQueueType = PlaybackQueueType.Single): MediaItem {
    return MediaItem.Builder()
        .setMediaId(id)
        .setMediaMetadata(
            MediaMetadata.Builder()
                .setTitle(title)
                .setSubtitle(subtitle)
                .setArtist(subtitle)
                .setIsPlayable(true)
                .setIsBrowsable(false)
                .setArtworkUri(artworkUri?.let(Uri::parse))
                .setExtras(toMediaMetadataExtras(queueType))
                .build()
        )
        .build()
}

private fun PlaybackAsset.toMediaMetadataExtras(queueType: PlaybackQueueType): Bundle {
    return Bundle().apply {
        putString(PlaybackMediaItemExtras.AssetType, type.name)
        putString(PlaybackMediaItemExtras.QueueType, queueType.name)
        putBoolean(PlaybackMediaItemExtras.SupportsSeek, capabilities.supportsSeek)
        putBoolean(PlaybackMediaItemExtras.SupportsPrevious, capabilities.supportsPrevious)
        putBoolean(PlaybackMediaItemExtras.SupportsNext, capabilities.supportsNext)
        putBoolean(PlaybackMediaItemExtras.SupportsCast, capabilities.supportsCast)
        putBoolean(PlaybackMediaItemExtras.SupportsPiP, capabilities.supportsPiP)
    }
}

private fun PlaybackAsset.mimeType(uri: String): String? {
    return when {
        uri.contains(".mpd", ignoreCase = true) -> MimeTypes.APPLICATION_MPD
        uri.contains(".m3u8", ignoreCase = true) -> MimeTypes.APPLICATION_M3U8
        uri.contains(".mp3", ignoreCase = true) -> MimeTypes.AUDIO_MPEG
        type == PlaybackAssetType.Podcast -> MimeTypes.AUDIO_MPEG
        type == PlaybackAssetType.Radio -> MimeTypes.AUDIO_MPEG
        else -> null
    }
}

private fun PlaybackStreamFormat.toMimeType(): String? {
    return when (this) {
        PlaybackStreamFormat.Hls -> MimeTypes.APPLICATION_M3U8
        PlaybackStreamFormat.Dash -> MimeTypes.APPLICATION_MPD
        PlaybackStreamFormat.Mp3 -> MimeTypes.AUDIO_MPEG
        PlaybackStreamFormat.Mp4 -> MimeTypes.VIDEO_MP4
        PlaybackStreamFormat.Unknown -> null
    }
}

fun PlaybackAsset.toCarMediaItem(): MediaItem {
    return MediaItem.Builder()
            .setMediaId(id)
            .setMediaMetadata(
                MediaMetadata.Builder()
                        .setIsBrowsable(false)
                        .setIsPlayable(true)
                        .setTitle(title)
                        .setSubtitle(subtitle)
                        .setArtist(subtitle)
                        .setArtworkUri(artworkUri?.let(Uri::parse))
                        .setExtras(toMediaMetadataExtras(PlaybackQueueType.None))
                        .build()
            )
            .build()
}
