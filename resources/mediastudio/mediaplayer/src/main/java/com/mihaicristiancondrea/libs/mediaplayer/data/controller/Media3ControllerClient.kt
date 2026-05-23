package com.mihaicristiancondrea.libs.mediaplayer.data.controller

import android.content.ComponentName
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.media3.common.C
import androidx.media3.common.DeviceInfo
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.Timeline
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.guava.await
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import com.mihaicristiancondrea.libs.mediaplayer.data.mappers.toMediaItem
import com.mihaicristiancondrea.libs.mediaplayer.data.mappers.PlaybackMediaItemExtras
import com.mihaicristiancondrea.libs.mediaplayer.data.service.PlaybackService
import com.mihaicristiancondrea.libs.mediaplayer.domain.interfaces.PlaybackController
import com.mihaicristiancondrea.libs.mediaplayer.domain.models.PlaybackAsset
import com.mihaicristiancondrea.libs.mediaplayer.domain.models.PlaybackQueue
import com.mihaicristiancondrea.libs.mediaplayer.domain.models.PlaybackQueueType
import com.mihaicristiancondrea.libs.mediaplayer.domain.models.PlaybackSnapshot
import com.mihaicristiancondrea.libs.mediaplayer.ui.contracts.PlayerViewProvider

/**
 * Connects the app UI. The real playback engine lives inside the service!!! This client only sends playback!!!
 * It also sends commands and exposes controller state back to the UI.
 * @see: [https://developer.android.com/media/media3/session/connect-to-media-app]
 */
class Media3ControllerClient(
    private val context: Context,
) : PlaybackController , PlayerViewProvider {

    // Solution for when multiple events at once are sent
    private val mutex = Mutex()

    private var controllerFuture: ListenableFuture<MediaController>? = null
    private var controller: MediaController? = null
    private val mainHandler = Handler(Looper.getMainLooper())

    private val _player = MutableStateFlow<Player?>(null)
    override val player: StateFlow<Player?> = _player

    // Current playback snapshot exposed to ViewModels and use cases to respect UI Data Domain
    private val _playbackSnapshot = MutableStateFlow(PlaybackSnapshot())
    override val playbackSnapshot: StateFlow<PlaybackSnapshot> = _playbackSnapshot

    private val playerListener = object : Player.Listener {

        override fun onIsPlayingChanged(isPlaying: Boolean) {
            updateSnapshot()
        }

        override fun onPlaybackStateChanged(playbackState: Int) {
            Log.d("Media3ControllerClient" , "playbackState=$playbackState isPlaying=${controller?.isPlaying}")
            updateSnapshot()
        }

        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            Log.d("Media3ControllerClient" , "media transition id=${mediaItem?.mediaId}, reason=$reason")
            updateSnapshot()
        }

        override fun onTimelineChanged(timeline: Timeline, reason: Int) {
            updateSnapshot()
        }

        override fun onAvailableCommandsChanged(availableCommands: Player.Commands) {
            updateSnapshot()
        }

        override fun onDeviceInfoChanged(deviceInfo: DeviceInfo) {
            updateSnapshot()
        }

        override fun onPlayerError(error: PlaybackException) {
            Log.e("Media3ControllerClient" , "Controller player error: code=${error.errorCodeName}, message=${error.message}" , error)
            updateSnapshot()
        }
    }

    // Connects to [PlaybackService] without starting playback.
    override suspend fun connect() {
        getController()
    }

    override suspend fun play(asset: PlaybackAsset) {
        withContext(Dispatchers.Main.immediate) {
            runCatching {

                with(getController()) {
                    setMediaItem(asset.toMediaItem())
                    prepare()
                    play()
                }

                updateSnapshot()
            }.onFailure {
                Log.e("Media3ControllerClient", "Error in play(): ${it.message}")
            }
        }
    }

    override suspend fun playQueue(queue: PlaybackQueue, startAssetId: String) {
        if (queue.items.isEmpty()) return

        withContext(Dispatchers.Main.immediate) {
            runCatching {
                val mediaItems = queue.items.map { asset ->
                    asset.toMediaItem(queue.type)
                }

                with(getController()) {
                    setMediaItems(mediaItems, queue.startIndex(startAssetId), C.TIME_UNSET)
                    prepare()
                    play()
                }

                updateSnapshot()
            }.onFailure {
                Log.e("Media3ControllerClient", "Error in playQueue(): ${it.message}")
            }
        }
    }

    // toggle play pause simple as that
    override suspend fun toggle(asset: PlaybackAsset) {
        withContext(Dispatchers.Main.immediate) {
            runCatching {
                with(getController()) {
                    when {
                        currentMediaItem?.mediaId != asset.id -> play(asset)
                        isPlaying -> pause()
                        else -> play()
                    }
                }

                updateSnapshot()
            }.onFailure {
                Log.e("Media3ControllerClient", "Error in toggle(): ${it.message}")
            }
        }
    }

    override suspend fun toggleQueue(queue: PlaybackQueue, startAssetId: String) {
        withContext(Dispatchers.Main.immediate) {
            runCatching {
                when (getController().currentMediaItem?.mediaId) {
                    startAssetId if _playbackSnapshot.value.queueType == queue.type -> togglePlayback()
                    else -> playQueue(queue , startAssetId)
                }
                updateSnapshot()
            }.onFailure {
                Log.e("Media3ControllerClient", "Error in toggleQueue(): ${it.message}")
            }
        }
    }

    override suspend fun togglePlayback() {
        withContext(Dispatchers.Main.immediate) {
            runCatching {

                with(getController()) {
                    if (isPlaying) pause() else play()
                }

                updateSnapshot()
            }.onFailure {
                Log.e("Media3ControllerClient", "Error in togglePlayback(): ${it.message}")
            }
        }
    }

    override suspend fun skipToNext() {
        withContext(Dispatchers.Main.immediate) {
            runCatching {

                with(getController()) {
                    if (hasNextMediaItem()) seekToNextMediaItem()
                }

                updateSnapshot()
            }.onFailure {
                Log.e("Media3ControllerClient", "Error in skipToNext(): ${it.message}")
            }
        }
    }

    override suspend fun skipToPrevious() {
        withContext(Dispatchers.Main.immediate) {
            runCatching {

                with(getController()) {
                    if (hasPreviousMediaItem()) seekToPreviousMediaItem()

                }

                updateSnapshot()
            }.onFailure {
                Log.e("Media3ControllerClient", "Error in skipToPrevious(): ${it.message}")
            }
        }
    }

    override suspend fun pause() {
        withContext(Dispatchers.Main.immediate) {
            runCatching {
                getController().pause()
                updateSnapshot()
            }.onFailure {
                Log.e("Media3ControllerClient", "Error in pause(): ${it.message}")
            }
        }
    }

    override suspend fun stop() {
        withContext(Dispatchers.Main.immediate) {
            runCatching {

                with(getController()) {
                    stop()
                    clearMediaItems()
                }

                updateSnapshot()
            }.onFailure {
                Log.e("Media3ControllerClient", "Error in stop(): ${it.message}")
            }
        }
    }

    override fun disconnect() {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            releaseControllerConnection()
        } else {
            mainHandler.post(::releaseControllerConnection)
        }
    }

    private fun releaseControllerConnection() {
        controller?.removeListener(playerListener)
        controllerFuture?.let(MediaController::releaseFuture)

        controllerFuture = null
        controller = null
        _player.value = null
        _playbackSnapshot.value = PlaybackSnapshot()
    }

    private suspend fun getController(): MediaController {
        controller?.let { return it }

        return mutex.withLock {
            controller?.let { return it }

            val token = SessionToken(context, ComponentName(context, PlaybackService::class.java))
            val future = MediaController.Builder(context, token).buildAsync()
            controllerFuture = future

            val createdController = try {
                withContext(Dispatchers.Main.immediate) {
                    withTimeout(5000) { future.await() }
                }
            } catch (e: Exception) {
                Log.e("Media3ControllerClient", "Failed to connect to MediaController: ${e.message}")
                MediaController.releaseFuture(future)
                controllerFuture = null
                throw e
            }

            createdController.addListener(playerListener)
            controller = createdController
            _player.value = createdController

            updateSnapshot()

            createdController
        }
    }

    // This INTENTIONALLY performs a small state projection instead of exposingthe entire controller. keep things small and simple
    private fun updateSnapshot() {
        val mediaController = controller
        val currentItem = mediaController?.currentMediaItem

        _playbackSnapshot.value = PlaybackSnapshot(
            currentAssetId = currentItem?.mediaId,
            isPlaying = mediaController?.isPlaying == true,
            isBuffering = mediaController?.playbackState == Player.STATE_BUFFERING,
            queueType = currentItem?.queueType() ?: PlaybackQueueType.None,
            currentIndex = mediaController?.currentMediaItemIndex ?: -1,
            itemCount = mediaController?.mediaItemCount ?: 0,
            hasPrevious = mediaController?.hasPreviousMediaItem() == true,
            hasNext = mediaController?.hasNextMediaItem() == true,
            isCasting = mediaController?.deviceInfo?.playbackType == DeviceInfo.PLAYBACK_TYPE_REMOTE,
        )
    }

    // TODO: maybe moving to mapping or somewhere else more relevant
    private fun MediaItem.queueType(): PlaybackQueueType {
        val queueTypeName = mediaMetadata.extras?.getString(PlaybackMediaItemExtras.QueueType)
        return queueTypeName
            ?.let { runCatching { PlaybackQueueType.valueOf(it) }.getOrNull() }
            ?: PlaybackQueueType.None
    }
}
