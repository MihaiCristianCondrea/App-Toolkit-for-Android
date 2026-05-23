package com.mihaicristiancondrea.libs.mediaplayer.domain.usecases

import com.mihaicristiancondrea.libs.mediaplayer.domain.interfaces.PlaybackController
import com.mihaicristiancondrea.libs.mediaplayer.domain.models.PlaybackQueue

class TogglePlaybackQueueUseCase(
    private val controller: PlaybackController,
) {
    suspend operator fun invoke(queue: PlaybackQueue, startAssetId: String) {
        controller.toggleQueue(queue, startAssetId)
    }
}
