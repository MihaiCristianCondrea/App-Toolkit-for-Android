package com.mihaicristiancondrea.libs.mediaplayer.domain.usecases

import com.mihaicristiancondrea.libs.mediaplayer.domain.interfaces.PlaybackController

class DisconnectPlaybackControllerUseCase(
    private val controller: PlaybackController,
) {
    operator fun invoke() {
        controller.disconnect()
    }
}
