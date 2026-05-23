package com.mihaicristiancondrea.libs.mediaplayer.domain.usecases

import com.mihaicristiancondrea.libs.mediaplayer.domain.interfaces.PlaybackController

class TogglePlaybackUseCase(
    private val controller: PlaybackController,
) {
    suspend operator fun invoke() {
        controller.togglePlayback()
    }
}
