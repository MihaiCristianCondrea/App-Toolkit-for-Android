package com.mihaicristiancondrea.mediastudio.app.player.ui.states

import androidx.compose.runtime.Immutable
import com.mihaicristiancondrea.mediastudio.app.player.ui.models.NowPlayingUi
import com.mihaicristiancondrea.mediastudio.app.player.ui.models.PlaybackCardUi
import com.mihaicristiancondrea.mediastudio.app.player.ui.models.PlaybackCardUiList

@Immutable
data class PlayerUiState(
    val radioCards: PlaybackCardUiList = PlaybackCardUiList() ,
    val videoCard: PlaybackCardUi? = null ,
    val podcastCard: PlaybackCardUi? = null ,
    val liveVideoCard: PlaybackCardUi? = null ,
    val nowPlaying: NowPlayingUi? = null ,
    val currentAssetId: String? = null ,
    val isPlaying: Boolean = false ,
    val isBuffering: Boolean = false ,
    val isInPip: Boolean = false ,
    val isTransitioningToPip: Boolean = false ,
) {
    val isPipPresentation: Boolean
        get() = isInPip || isTransitioningToPip

    val currentVideoCard: PlaybackCardUi?
        get() = listOfNotNull(videoCard, liveVideoCard).firstOrNull { it.id == currentAssetId }

    val canEnterPiP: Boolean
        get() = currentVideoCard?.supportsPiP == true && isPlaying
}
