package com.mihaicristiancondrea.mediastudio.app.player.ui.mappers

import com.mihaicristiancondrea.libs.mediaplayer.domain.models.PlaybackAsset
import com.mihaicristiancondrea.mediastudio.app.player.ui.models.PlaybackCardUi
import com.mihaicristiancondrea.mediastudio.app.player.ui.models.NowPlayingUi
import com.mihaicristiancondrea.libs.mediaplayer.domain.models.PlaybackAssetType
import com.mihaicristiancondrea.libs.mediaplayer.domain.models.PlaybackSnapshot

fun PlaybackAsset.toCardUi(currentAssetId: String?, isPlaying: Boolean): PlaybackCardUi {
    val selected = id == currentAssetId
    val isVideoSurface = type == PlaybackAssetType.Video || type == PlaybackAssetType.LiveVideo
    return PlaybackCardUi(
        id = id ,
        title = title ,
        subtitle = subtitle ,
        artworkUri = artworkUri ,
        thumbnailUri = thumbnailUri ,
        type = type ,
        supportsPiP = capabilities.supportsPiP ,
        supportsCast = capabilities.supportsCast ,
        isSelected = selected ,
        isPlaying = selected && isPlaying ,
        shouldShowThumbnail = isVideoSurface && (!selected || !isPlaying) ,
        shouldAttachPlayerView = isVideoSurface && selected ,
    )
}

fun PlaybackAsset.toNowPlayingUi(snapshot: PlaybackSnapshot): NowPlayingUi {
    return NowPlayingUi(
        id = id,
        title = title,
        subtitle = subtitle,
        artworkUri = artworkUri,
        thumbnailUri = thumbnailUri,
        queueType = snapshot.queueType,
        currentIndex = snapshot.currentIndex,
        itemCount = snapshot.itemCount,
        hasPrevious = snapshot.hasPrevious,
        hasNext = snapshot.hasNext,
        isPlaying = snapshot.isPlaying,
        isBuffering = snapshot.isBuffering,
        isCasting = snapshot.isCasting,
    )
}
