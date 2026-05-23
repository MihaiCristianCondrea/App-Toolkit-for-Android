package com.mihaicristiancondrea.mediastudio.app.player.ui.models

import androidx.compose.runtime.Immutable
import com.mihaicristiancondrea.libs.mediaplayer.domain.models.PlaybackAssetType
import com.mihaicristiancondrea.libs.mediaplayer.domain.models.PlaybackQueueType

@Immutable
data class PlaybackCardUi(
    val id: String ,
    val title: String ,
    val subtitle: String ,
    val artworkUri: String? ,
    val thumbnailUri: String? ,
    val type: PlaybackAssetType ,
    val supportsPiP: Boolean ,
    val supportsCast: Boolean ,
    val isSelected: Boolean ,
    val isPlaying: Boolean ,
    val shouldShowThumbnail: Boolean ,
    val shouldAttachPlayerView: Boolean ,
)

@Immutable
data class PlaybackCardUiList(
    val items: List<PlaybackCardUi> = emptyList(),
)

@Immutable
data class NowPlayingUi(
    val id: String,
    val title: String,
    val subtitle: String,
    val artworkUri: String?,
    val thumbnailUri: String?,
    val queueType: PlaybackQueueType,
    val currentIndex: Int,
    val itemCount: Int,
    val hasPrevious: Boolean,
    val hasNext: Boolean,
    val isPlaying: Boolean,
    val isBuffering: Boolean,
    val isCasting: Boolean,
)
