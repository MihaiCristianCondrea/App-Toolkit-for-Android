package com.mihaicristiancondrea.mediastudio.app.main.ui


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import com.mihaicristiancondrea.libs.mediaplayer.domain.models.PlaybackAsset
import com.mihaicristiancondrea.libs.mediaplayer.domain.models.PlaybackAssetType
import com.mihaicristiancondrea.libs.mediaplayer.domain.models.PlaybackQueue
import com.mihaicristiancondrea.libs.mediaplayer.domain.models.PlaybackQueueType
import com.mihaicristiancondrea.libs.mediaplayer.domain.usecases.ConnectPlaybackControllerUseCase
import com.mihaicristiancondrea.libs.mediaplayer.domain.usecases.DisconnectPlaybackControllerUseCase
import com.mihaicristiancondrea.mediastudio.app.player.domain.usecases.GetPlayerAssetsUseCase
import com.mihaicristiancondrea.mediastudio.app.player.ui.mappers.toCardUi
import com.mihaicristiancondrea.mediastudio.app.player.ui.mappers.toNowPlayingUi
import com.mihaicristiancondrea.libs.mediaplayer.domain.usecases.ObservePlaybackSnapshotUseCase
import com.mihaicristiancondrea.libs.mediaplayer.domain.usecases.SkipToNextUseCase
import com.mihaicristiancondrea.libs.mediaplayer.domain.usecases.SkipToPreviousUseCase
import com.mihaicristiancondrea.libs.mediaplayer.domain.usecases.TogglePlaybackAssetUseCase
import com.mihaicristiancondrea.libs.mediaplayer.domain.usecases.TogglePlaybackQueueUseCase
import com.mihaicristiancondrea.libs.mediaplayer.domain.usecases.TogglePlaybackUseCase
import com.mihaicristiancondrea.libs.mediaplayer.ui.contracts.PlayerViewProvider
import com.mihaicristiancondrea.libs.mediaplayer.ui.contracts.PlayerEvent
import com.mihaicristiancondrea.mediastudio.app.player.ui.models.PlaybackCardUiList
import com.mihaicristiancondrea.mediastudio.app.player.ui.states.PlayerUiState

class MainViewModel(
    getPlayerAssetsUseCase: GetPlayerAssetsUseCase ,
    observePlaybackSnapshotUseCase: ObservePlaybackSnapshotUseCase ,
    private val connectPlaybackControllerUseCase: ConnectPlaybackControllerUseCase ,
    private val disconnectPlaybackControllerUseCase: DisconnectPlaybackControllerUseCase ,
    private val togglePlaybackAssetUseCase: TogglePlaybackAssetUseCase ,
    private val togglePlaybackQueueUseCase: TogglePlaybackQueueUseCase ,
    private val togglePlaybackUseCase: TogglePlaybackUseCase ,
    private val skipToPreviousUseCase: SkipToPreviousUseCase ,
    private val skipToNextUseCase: SkipToNextUseCase ,
    playerViewProvider: PlayerViewProvider ,
) : ViewModel() {

    private val assets: List<PlaybackAsset> = getPlayerAssetsUseCase()
    private val radioQueue = PlaybackQueue(
        id = "radio",
        type = PlaybackQueueType.Radio,
        items = assets.filter { it.type == PlaybackAssetType.Radio },
    )

    val player = playerViewProvider.player

    private val pipState = MutableStateFlow(false)
    private val pipTransitionState = MutableStateFlow(false)

    val uiState: StateFlow<PlayerUiState> =
            combine(observePlaybackSnapshotUseCase(), pipState, pipTransitionState) { snapshot, isInPip, isTransitioningToPip ->
                val cards = assets.map { it.toCardUi(currentAssetId = snapshot.currentAssetId , isPlaying = snapshot.isPlaying) }
                val currentAsset = assets.firstOrNull { it.id == snapshot.currentAssetId }

                PlayerUiState(
                    radioCards = PlaybackCardUiList(cards.filter { it.type == PlaybackAssetType.Radio }) ,
                    videoCard = cards.firstOrNull { it.type == PlaybackAssetType.Video } ,
                    podcastCard = cards.firstOrNull { it.type == PlaybackAssetType.Podcast } ,
                    liveVideoCard = cards.firstOrNull { it.type == PlaybackAssetType.LiveVideo } ,
                    nowPlaying = currentAsset?.toNowPlayingUi(snapshot) ,
                    currentAssetId = snapshot.currentAssetId ,
                    isPlaying = snapshot.isPlaying ,
                    isBuffering = snapshot.isBuffering ,
                    isInPip = isInPip ,
                    isTransitioningToPip = isTransitioningToPip ,
                )
            }.stateIn(scope = viewModelScope , started = SharingStarted.WhileSubscribed(5_000) , initialValue = PlayerUiState())

    init {
        viewModelScope.launch {
            connectPlaybackControllerUseCase.invoke()
        }
    }

    override fun onCleared() {
        disconnectPlaybackControllerUseCase.invoke()
        super.onCleared()
    }

    fun onEvent(event: PlayerEvent) {
        when (event) {
            is PlayerEvent.RadioClicked -> playRadioById(event.assetId)
            is PlayerEvent.VideoClicked -> playByType(PlaybackAssetType.Video)
            is PlayerEvent.PodcastClicked -> playByType(PlaybackAssetType.Podcast)
            is PlayerEvent.LiveVideoClicked -> playByType(PlaybackAssetType.LiveVideo)
            is PlayerEvent.TogglePlaybackClicked -> toggleCurrentPlayback()
            is PlayerEvent.SkipToPreviousClicked -> skipToPrevious()
            is PlayerEvent.SkipToNextClicked -> skipToNext()

            is PlayerEvent.PiPModeChanged -> {
                pipState.value = event.isInPip
                pipTransitionState.value = event.isInPip
            }

            is PlayerEvent.PiPTransitionStarted -> {
                if (event.assetId == uiState.value.currentAssetId) {
                    pipTransitionState.value = true
                }
            }


            is PlayerEvent.NotificationOpened -> {
                // Potential navigation or UI expansion logic
                Log.d("MainViewModel", "Player event: NotificationOpened - Handled via Deep Link")
            }
        }
    }

    private fun playRadioById(assetId: String) {
        if (radioQueue.items.none { it.id == assetId }) return
        viewModelScope.launch {
            togglePlaybackQueueUseCase.invoke(radioQueue, assetId)
        }
    }

    private fun playByType(type: PlaybackAssetType) {
        val asset = assets.firstOrNull { it.type == type } ?: return
        viewModelScope.launch {
            togglePlaybackAssetUseCase.invoke(asset)
        }
    }

    private fun toggleCurrentPlayback() {
        viewModelScope.launch {
            togglePlaybackUseCase.invoke()
        }
    }

    private fun skipToPrevious() {
        viewModelScope.launch {
            skipToPreviousUseCase.invoke()
        }
    }

    private fun skipToNext() {
        viewModelScope.launch {
            skipToNextUseCase.invoke()
        }
    }
}
