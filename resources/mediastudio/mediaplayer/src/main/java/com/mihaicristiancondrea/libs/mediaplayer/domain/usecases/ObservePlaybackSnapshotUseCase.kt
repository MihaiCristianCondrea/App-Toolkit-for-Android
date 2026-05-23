package com.mihaicristiancondrea.libs.mediaplayer.domain.usecases

import com.mihaicristiancondrea.libs.mediaplayer.domain.interfaces.PlaybackController

class ObservePlaybackSnapshotUseCase(
    private val controller: PlaybackController,
) {
    operator fun invoke() = controller.playbackSnapshot
}
