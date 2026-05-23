package com.mihaicristiancondrea.libs.mediaplayer.domain.models


// TODO: separate all models based on their corelations, ensure only the related models remain in the same file(s)
enum class PlaybackAssetType {
    Radio,
    Video,
    Podcast,
    LiveVideo
}

enum class PlaybackStreamFormat {
    Unknown,
    Mp3,
    Mp4,
    Hls,
    Dash,
}

enum class PlaybackQueueType {
    None,
    Single,
    Radio,
    Podcast,
    Video,
}

sealed interface PlaybackAds {
    data object None : PlaybackAds
    data class ClientSideVast(val adTagUri: String) : PlaybackAds
}

data class PlaybackSettings(
    val supportsPiP: Boolean = false,
    val supportsCast: Boolean = false,
    val supportsSeek: Boolean = false,
    val supportsPrevious: Boolean = false,
    val supportsNext: Boolean = false,
    val supportsPlaybackSpeed: Boolean = false,
)

data class PlaybackSnapshot(
    val currentAssetId: String? = null,
    val isPlaying: Boolean = false,
    val isBuffering: Boolean = false,
    val queueType: PlaybackQueueType = PlaybackQueueType.None,
    val currentIndex: Int = -1,
    val itemCount: Int = 0,
    val hasPrevious: Boolean = false,
    val hasNext: Boolean = false,
    val isCasting: Boolean = false,
)

sealed interface PlaybackStreamSource {

    data class Direct(val uri: String, val format: PlaybackStreamFormat = PlaybackStreamFormat.Unknown) : PlaybackStreamSource

    data class Proxied(
        val proxyEndpoint: String,
        val headers: Map<String, String> = emptyMap(),
        val requiresResolution: Boolean = false,
        val format: PlaybackStreamFormat = PlaybackStreamFormat.Unknown,
    ) : PlaybackStreamSource

    data class ServerSideDai(
        val assetKey: String? = null,
        val contentSourceId: String? = null,
        val videoId: String? = null,
        val manifestUri: String? = null,
        val proxyEndpoint: String? = null,
        val headers: Map<String, String> = emptyMap(),
        val format: PlaybackStreamFormat = PlaybackStreamFormat.Hls,
    ) : PlaybackStreamSource
}

data class ResolvedPlaybackStream(
    val uri: String,
    val headers: Map<String, String> = emptyMap(),
    val format: PlaybackStreamFormat = PlaybackStreamFormat.Unknown,
)

data class PlaybackAsset(
    val id: String ,
    val title: String ,
    val subtitle: String ,
    val artworkUri: String? = null ,
    val thumbnailUri: String? = null ,
    val type: PlaybackAssetType ,
    val isLive: Boolean = false ,
    val capabilities: PlaybackSettings = PlaybackSettings() ,
    val ads: PlaybackAds = PlaybackAds.None ,
    val streamSource: PlaybackStreamSource ,
) {
    val supportsPiP: Boolean // FIXME: unused
        get() = capabilities.supportsPiP

    val supportsCast: Boolean // FIXME: Unused
        get() = capabilities.supportsCast
}

data class PlaybackQueue(
    val id: String,
    val type: PlaybackQueueType,
    val items: List<PlaybackAsset>,
) {
    fun startIndex(assetId: String): Int {
        return items.indexOfFirst { it.id == assetId }.takeIf { it >= 0 } ?: 0
    }
}
