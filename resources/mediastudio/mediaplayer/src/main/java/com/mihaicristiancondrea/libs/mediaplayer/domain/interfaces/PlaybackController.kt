package com.mihaicristiancondrea.libs.mediaplayer.domain.interfaces

import kotlinx.coroutines.flow.StateFlow
import com.mihaicristiancondrea.libs.mediaplayer.domain.models.PlaybackAsset
import com.mihaicristiancondrea.libs.mediaplayer.domain.models.PlaybackQueue
import com.mihaicristiancondrea.libs.mediaplayer.domain.models.PlaybackSnapshot
import com.mihaicristiancondrea.libs.mediaplayer.domain.models.ResolvedPlaybackStream

interface PlaybackController {
    val playbackSnapshot: StateFlow<PlaybackSnapshot>
    suspend fun connect()
    suspend fun play(asset: PlaybackAsset)
    suspend fun playQueue(queue: PlaybackQueue, startAssetId: String)
    suspend fun toggle(asset: PlaybackAsset)
    suspend fun toggleQueue(queue: PlaybackQueue, startAssetId: String)
    suspend fun togglePlayback()
    suspend fun skipToNext()
    suspend fun skipToPrevious()
    suspend fun pause()
    suspend fun stop()
    fun disconnect()
}

interface PlaybackStreamResolver {
    suspend fun resolve(asset: PlaybackAsset): ResolvedPlaybackStream
}
