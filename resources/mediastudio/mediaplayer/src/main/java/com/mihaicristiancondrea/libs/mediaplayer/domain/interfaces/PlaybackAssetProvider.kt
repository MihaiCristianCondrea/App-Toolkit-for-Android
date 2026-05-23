package com.mihaicristiancondrea.libs.mediaplayer.domain.interfaces

import com.mihaicristiancondrea.libs.mediaplayer.domain.models.PlaybackAsset

fun interface PlaybackAssetProvider {

    operator fun invoke(): List<PlaybackAsset>
}