package com.mihaicristiancondrea.libs.mediaplayer.domain.usecases

import com.mihaicristiancondrea.libs.mediaplayer.domain.interfaces.PlaybackController

class SkipToPreviousUseCase(
    private val controller: PlaybackController,
) {
    suspend operator fun invoke() {
        controller.skipToPrevious()
    }
}
