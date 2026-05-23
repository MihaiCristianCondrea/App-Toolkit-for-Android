package com.mihaicristiancondrea.libs.mediaplayer.domain.usecases

import com.mihaicristiancondrea.libs.mediaplayer.domain.interfaces.PlaybackController
import com.mihaicristiancondrea.libs.mediaplayer.domain.models.PlaybackAsset

class TogglePlaybackAssetUseCase(
    private val controller: PlaybackController,
) {
    suspend operator fun invoke(asset: PlaybackAsset) {
        controller.toggle(asset)
    }
}
