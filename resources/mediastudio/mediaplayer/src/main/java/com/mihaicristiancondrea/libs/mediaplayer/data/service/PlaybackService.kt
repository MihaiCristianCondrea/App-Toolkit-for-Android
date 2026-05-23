package com.mihaicristiancondrea.libs.mediaplayer.data.service

import android.app.PendingIntent
import android.util.Log
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.LibraryResult
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaSession
import androidx.media3.session.SessionError
import androidx.media3.session.SessionCommands
import com.google.common.collect.ImmutableList
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.guava.future
import org.koin.android.ext.android.inject
import com.mihaicristiancondrea.libs.mediaplayer.data.car.CarMediaLibrary
import com.mihaicristiancondrea.libs.mediaplayer.data.mappers.PlaybackMediaItemExtras
import com.mihaicristiancondrea.libs.mediaplayer.data.mappers.toMediaItem
import com.mihaicristiancondrea.libs.mediaplayer.domain.interfaces.PlaybackAssetProvider
import com.mihaicristiancondrea.libs.mediaplayer.domain.interfaces.PlaybackStreamResolver
import com.mihaicristiancondrea.libs.mediaplayer.domain.models.PlaybackQueueType

@OptIn(UnstableApi::class)
class PlaybackService : MediaLibraryService() { // MediaLibraryService extended for auto // old: MediaSessionService

    // Injections from the host app (host app decides everything
    private val playerFactory: PlaybackPlayerFactory by inject()
    private val carMediaLibrary: CarMediaLibrary by inject()
    private val sessionIntentProvider: PlaybackSessionIntentProvider by inject()
    private val playbackAssetProvider: PlaybackAssetProvider by inject()
    private val streamResolver: PlaybackStreamResolver by inject()
    private val controlPolicy = PlaybackControlPolicy()

    // Coroutine scope tied to the service (it lives as long as the service do)
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    private var mediaSession: MediaLibrarySession? = null
    private var player: Player? = null
    private val connectedSessionCommands = mutableMapOf<MediaSession.ControllerInfo, SessionCommands>()

    private companion object {
        const val SessionActivityRequestCode = 1001 // for redirecting lib to current app // TODO: maybe moving or removing... stil searching improvements but atm is good
    }

    // Media3 session callback for controller permissions, media buttons, and library browsing.
    private val callback = object : MediaLibrarySession.Callback {
        override fun onConnect(session: MediaSession, controller: MediaSession.ControllerInfo): MediaSession.ConnectionResult {
            val defaultResult = super.onConnect(session, controller)
            val sessionCommands = defaultResult.availableSessionCommands
            val defaultPlayerCommands = defaultResult.availablePlayerCommands
            val currentPlayer = player

            connectedSessionCommands[controller] = sessionCommands

            val playerCommands = currentPlayer?.let {
                controlPolicy.availablePlayerCommands(baseCommands = defaultPlayerCommands, player = it)
            } ?: defaultPlayerCommands

            val mediaButtonPreferences = currentPlayer?.let(controlPolicy::mediaButtonPreferences).orEmpty()

            return MediaSession.ConnectionResult.AcceptedResultBuilder(session)
                    .setAvailableSessionCommands(sessionCommands)
                    .setAvailablePlayerCommands(playerCommands)
                    .setMediaButtonPreferences(mediaButtonPreferences)
                    .build()
        }

        override fun onDisconnected(session: MediaSession, controller: MediaSession.ControllerInfo) {
            connectedSessionCommands.remove(controller)
        }

        // The root of the Android Auto tree
        override fun onGetLibraryRoot(session: MediaLibrarySession, browser: MediaSession.ControllerInfo, params: LibraryParams?): ListenableFuture<LibraryResult<MediaItem>> {
            return Futures.immediateFuture(LibraryResult.ofItem(carMediaLibrary.getRootItem() , params))
        }

        // Android Auto children (after the root) - root/podcasts | root/radios
        override fun onGetChildren(session: MediaLibrarySession, browser: MediaSession.ControllerInfo, parentId: String, page: Int, pageSize: Int, params: LibraryParams?): ListenableFuture<LibraryResult<ImmutableList<MediaItem>>> {
            return Futures.immediateFuture(LibraryResult.ofItemList(ImmutableList.copyOf(carMediaLibrary.getChildren(parentId)) , params))
        }

        // Android Auto items from the children ex: root/podcasts/podcast_1... etc
        override fun onGetItem(session: MediaLibrarySession, browser: MediaSession.ControllerInfo, mediaId: String): ListenableFuture<LibraryResult<MediaItem>> {
            return Futures.immediateFuture(carMediaLibrary.getItem(mediaId)?.let { item ->
                LibraryResult.ofItem(item , null)
            } ?: LibraryResult.ofError(SessionError.ERROR_BAD_VALUE))
        }

        // In Android Auto it converts the items into playable items
        override fun onAddMediaItems(mediaSession: MediaSession, controller: MediaSession.ControllerInfo, mediaItems: MutableList<MediaItem>): ListenableFuture<MutableList<MediaItem>> {
            return serviceScope.future {
                mediaItems.mapNotNull { requestedItem ->
                    resolvePlayableMediaItem(requestedItem)
                }.toMutableList()
            }
        }

        // Search override for Android Auto search for parents and children
        override fun onSearch(session: MediaLibrarySession, browser: MediaSession.ControllerInfo, query: String, params: LibraryParams?): ListenableFuture<LibraryResult<Void>> {
            return Futures.immediateFuture(LibraryResult.ofVoid(params))
        }

        // Android Auto after found the playable item
        override fun onGetSearchResult(session: MediaLibrarySession, browser: MediaSession.ControllerInfo, query: String, page: Int, pageSize: Int, params: LibraryParams?): ListenableFuture<LibraryResult<ImmutableList<MediaItem>>> {
            return Futures.immediateFuture(LibraryResult.ofItemList(ImmutableList.copyOf(carMediaLibrary.search(query)) , params))
        }
    }

    override fun onCreate() {
        super.onCreate()

        val createdPlayer = playerFactory.create(this)

        createdPlayer.addListener(object : Player.Listener {
            override fun onPlayerError(error: PlaybackException) {
                Log.e("PlaybackService" , "Player error: code=${error.errorCodeName}, message=${error.message}" , error)
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                Log.d("PlaybackService" , "Playback state=$playbackState isPlaying=${createdPlayer.isPlaying}")
            }

            override fun onEvents(player: Player, events: Player.Events) {
                updateControlPolicy(player)
            }
        })

        player = createdPlayer
        mediaSession = MediaLibrarySession.Builder(this, createdPlayer, callback).setSessionActivity(createSessionActivityPendingIntent()).build()
        updateControlPolicy(createdPlayer)
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaLibrarySession? = mediaSession

    override fun onDestroy() {
        mediaSession?.run {
            playerFactory.release(player)
            release()
        }

        player = null
        mediaSession = null
        connectedSessionCommands.clear()

        serviceScope.cancel()

        super.onDestroy()
    }

    // used for the intent of the media3 notification
    private fun createSessionActivityPendingIntent(): PendingIntent {
        return PendingIntent.getActivity(this , SessionActivityRequestCode , sessionIntentProvider.createSessionActivityIntent(this) , PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
    }

    // TODO: make it extension or mapper or helper | currently left here (it does not bother atm)
    private suspend fun resolvePlayableMediaItem(requestedItem: MediaItem): MediaItem? {
        val asset = playbackAssetProvider().firstOrNull { it.id == requestedItem.mediaId } ?: return null
        val resolvedStream = streamResolver.resolve(asset)
        return asset.toMediaItem(resolvedStream, requestedItem.queueType())
    }

    private fun updateControlPolicy(currentPlayer: Player) {
        val session = mediaSession ?: return
        session.setMediaButtonPreferences(controlPolicy.mediaButtonPreferences(currentPlayer))
        session.connectedControllers.forEach { controller ->
            val sessionCommands = connectedSessionCommands[controller] ?: return@forEach
            session.setAvailableCommands(controller, sessionCommands, controlPolicy.availablePlayerCommands(currentPlayer.availableCommands, currentPlayer))
        }
    }

    private fun MediaItem.queueType(): PlaybackQueueType = mediaMetadata.extras?.getString(PlaybackMediaItemExtras.QueueType)?.let { runCatching { PlaybackQueueType.valueOf(it) }.getOrNull() } ?: PlaybackQueueType.None
}
