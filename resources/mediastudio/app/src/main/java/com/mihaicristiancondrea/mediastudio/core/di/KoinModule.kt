package com.mihaicristiancondrea.mediastudio.core.di

import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import com.mihaicristiancondrea.mediastudio.app.player.domain.usecases.GetPlayerAssetsUseCase
import com.mihaicristiancondrea.mediastudio.app.main.ui.MainViewModel
import com.mihaicristiancondrea.mediastudio.app.player.ui.providers.MainPlaybackSessionIntentProvider
import com.mihaicristiancondrea.libs.mediaplayer.data.car.CarMediaLibrary
import com.mihaicristiancondrea.libs.mediaplayer.data.controller.Media3ControllerClient
import com.mihaicristiancondrea.libs.mediaplayer.data.resolver.PlaybackStreamResolverImpl
import com.mihaicristiancondrea.libs.mediaplayer.data.service.PlaybackPlayerFactory
import com.mihaicristiancondrea.libs.mediaplayer.data.service.PlaybackSessionIntentProvider
import com.mihaicristiancondrea.libs.mediaplayer.domain.interfaces.PlaybackAssetProvider
import com.mihaicristiancondrea.libs.mediaplayer.domain.interfaces.PlaybackController
import com.mihaicristiancondrea.libs.mediaplayer.domain.interfaces.PlaybackStreamResolver
import com.mihaicristiancondrea.libs.mediaplayer.domain.usecases.ConnectPlaybackControllerUseCase
import com.mihaicristiancondrea.libs.mediaplayer.domain.usecases.DisconnectPlaybackControllerUseCase
import com.mihaicristiancondrea.libs.mediaplayer.domain.usecases.ObservePlaybackSnapshotUseCase
import com.mihaicristiancondrea.libs.mediaplayer.domain.usecases.PausePlaybackUseCase
import com.mihaicristiancondrea.libs.mediaplayer.domain.usecases.SkipToNextUseCase
import com.mihaicristiancondrea.libs.mediaplayer.domain.usecases.SkipToPreviousUseCase
import com.mihaicristiancondrea.libs.mediaplayer.domain.usecases.TogglePlaybackAssetUseCase
import com.mihaicristiancondrea.libs.mediaplayer.domain.usecases.TogglePlaybackQueueUseCase
import com.mihaicristiancondrea.libs.mediaplayer.domain.usecases.TogglePlaybackUseCase
import com.mihaicristiancondrea.libs.mediaplayer.ui.contracts.PlayerViewProvider


val playerModule = module {

    single {
        PlaybackPlayerFactory()
    }

    single<PlaybackStreamResolver> {
        PlaybackStreamResolverImpl()
    }

    single {
        CarMediaLibrary(
            playbackAssetProvider = get() ,
        )
    }

    single {
        GetPlayerAssetsUseCase()
    }

    single<PlaybackAssetProvider> {
        get<GetPlayerAssetsUseCase>()
    }

    single<PlaybackSessionIntentProvider> {
        MainPlaybackSessionIntentProvider()
    }

    single {
        Media3ControllerClient(
            context = androidContext(),
        )
    }

    single<PlaybackController> {
        get<Media3ControllerClient>()
    }

    single<PlayerViewProvider> {
        get<Media3ControllerClient>()
    }

    factory {
        GetPlayerAssetsUseCase()
    }

    factory {
        ConnectPlaybackControllerUseCase(
            controller = get() ,
        )
    }

    factory {
        DisconnectPlaybackControllerUseCase(
            controller = get() ,
        )
    }

    factory {
        TogglePlaybackAssetUseCase(
            controller = get() ,
        )
    }

    factory {
        TogglePlaybackQueueUseCase(
            controller = get() ,
        )
    }

    factory {
        TogglePlaybackUseCase(
            controller = get() ,
        )
    }

    factory {
        SkipToPreviousUseCase(
            controller = get() ,
        )
    }

    factory {
        SkipToNextUseCase(
            controller = get() ,
        )
    }

    factory {
        PausePlaybackUseCase(
            controller = get() ,
        )
    }

    factory {
        ObservePlaybackSnapshotUseCase(
            controller = get() ,
        )
    }

    viewModel {
        MainViewModel(
            getPlayerAssetsUseCase = get() ,
            observePlaybackSnapshotUseCase = get() ,
            connectPlaybackControllerUseCase = get() ,
            disconnectPlaybackControllerUseCase = get() ,
            togglePlaybackAssetUseCase = get() ,
            togglePlaybackQueueUseCase = get() ,
            togglePlaybackUseCase = get() ,
            skipToPreviousUseCase = get() ,
            skipToNextUseCase = get() ,
            playerViewProvider = get() ,
        )
    }
}
