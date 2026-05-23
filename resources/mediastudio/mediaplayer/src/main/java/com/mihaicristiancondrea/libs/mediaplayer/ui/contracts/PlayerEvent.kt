package com.mihaicristiancondrea.libs.mediaplayer.ui.contracts

sealed interface PlayerEvent {
    data class RadioClicked(val assetId: String) : PlayerEvent
    data object VideoClicked : PlayerEvent
    data object PodcastClicked : PlayerEvent
    data object LiveVideoClicked : PlayerEvent
    data object TogglePlaybackClicked : PlayerEvent
    data object SkipToPreviousClicked : PlayerEvent
    data object SkipToNextClicked : PlayerEvent
    data class PiPModeChanged(val isInPip: Boolean) : PlayerEvent
    data class PiPTransitionStarted(val assetId: String?) : PlayerEvent
    data object NotificationOpened : PlayerEvent
}
