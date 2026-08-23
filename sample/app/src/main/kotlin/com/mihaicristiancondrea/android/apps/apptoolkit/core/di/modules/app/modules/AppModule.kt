/*
 * Copyright (©) 2026 Mihai-Cristian Condrea
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package com.mihaicristiancondrea.android.apps.apptoolkit.core.di.modules.app.modules

import com.mihaicristiancondrea.android.apps.apptoolkit.app.main.data.repositories.DefaultNavigationConfigurationRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.app.main.data.repositories.NavigationConfigurationRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.app.main.ui.MainViewModel
import com.mihaicristiancondrea.android.apps.apptoolkit.app.main.ui.navigation.AppNavigationItemsProvider
import com.mihaicristiancondrea.android.apps.apptoolkit.app.main.ui.navigation.NavigationItemsProvider
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.local.preferences.DefaultToolkitTilesPreferencesDataSource
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.local.preferences.ToolkitTilesPreferencesDataSource
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.local.caffeine.AndroidCaffeineServiceDataSource
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.local.caffeine.CaffeineServiceDataSource
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.local.haptics.AndroidBreathingHapticsDataSource
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.local.haptics.BreathingHapticsDataSource
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.local.quicksettings.AndroidQuickSettingsTilesLocalDataSource
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.local.quicksettings.QuickSettingsTilesLocalDataSource
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.local.sensors.AndroidSensorLocalDataSource
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.local.sensors.SensorLocalDataSource
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.local.system.AndroidSystemLocalDataSource
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.local.system.SystemLocalDataSource
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.local.torch.AndroidTorchDataSource
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.local.torch.TorchDataSource
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.repositories.BreathingRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.repositories.CaffeineRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.repositories.DefaultToolkitTilesRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.repositories.DefaultTorchRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.repositories.MorseRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.repositories.SensorRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.repositories.SosRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.repositories.SystemRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.repositories.ToolkitTilesRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.repositories.TorchRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.ui.BreathingToolViewModel
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.ui.CaffeineToolViewModel
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.ui.CoinFlipToolViewModel
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.ui.CompassToolViewModel
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.ui.CounterToolViewModel
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.ui.DiceRollToolViewModel
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.ui.FlashDimmerToolViewModel
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.ui.LevelToolViewModel
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.ui.LuxMeterToolViewModel
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.ui.MusicSearchToolViewModel
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.ui.MorseToolViewModel
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.ui.SosToolViewModel
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.ui.SoundModeToolViewModel
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.ui.ToolkitTilesViewModel
import com.mihaicristiancondrea.android.apps.apptoolkit.core.navigation.NavigationManager
import com.mihaicristiancondrea.android.libs.apptoolkit.app.review.domain.usecases.RequestInAppReviewUseCase
import com.mihaicristiancondrea.android.libs.apptoolkit.core.data.local.datastore.CommonDataStore
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule: Module = module {
    single { NavigationManager() }
    single<NavigationConfigurationRepository> {
        DefaultNavigationConfigurationRepository(dataStore = get())
    }
    single<NavigationItemsProvider> {
        AppNavigationItemsProvider(repository = get(), firebaseController = get())
    }
    single<TorchDataSource> { AndroidTorchDataSource(context = androidContext()) }
    single<SensorLocalDataSource> {
        AndroidSensorLocalDataSource(context = androidContext(), dispatchers = get())
    }
    single<SystemLocalDataSource> {
        AndroidSystemLocalDataSource(context = androidContext(), dispatchers = get())
    }
    single<BreathingHapticsDataSource> {
        AndroidBreathingHapticsDataSource(context = androidContext())
    }
    single<CaffeineServiceDataSource> {
        AndroidCaffeineServiceDataSource(context = androidContext())
    }
    single<QuickSettingsTilesLocalDataSource> {
        AndroidQuickSettingsTilesLocalDataSource(context = androidContext())
    }
    single<TorchRepository> { DefaultTorchRepository(dataSource = get(), dispatchers = get()) }
    single<ToolkitTilesPreferencesDataSource> {
        DefaultToolkitTilesPreferencesDataSource(
            dataStore = get<CommonDataStore>().dataStore,
        )
    }
    single<ToolkitTilesRepository> {
        DefaultToolkitTilesRepository(
            torchRepository = get(),
            preferencesDataSource = get(),
            quickSettingsDataSource = get(),
        )
    }
    single { MorseRepository(torchRepository = get(), dispatchers = get()) }
    single {
        SensorRepository(
            localDataSource = get(),
        )
    }
    single {
        BreathingRepository(
            hapticsDataSource = get(),
            dispatchers = get()
        )
    }
    single {
        CaffeineRepository(
            serviceDataSource = get(),
        )
    }
    single {
        SosRepository(
            morseRepository = get(),
        )
    }
    single {
        SystemRepository(
            localDataSource = get(),
        )
    }
    viewModel {
        ToolkitTilesViewModel(
            toolkitTilesRepository = get(),
            dispatchers = get(),
            firebaseController = get(),
        )
    }
    viewModel { CoinFlipToolViewModel() }
    viewModel { DiceRollToolViewModel() }
    viewModel { CounterToolViewModel() }
    viewModel { CompassToolViewModel(repository = get()) }
    viewModel { LevelToolViewModel(repository = get()) }
    viewModel { LuxMeterToolViewModel(repository = get()) }
    viewModel { BreathingToolViewModel(repository = get()) }
    viewModel { CaffeineToolViewModel(repository = get()) }
    viewModel { SoundModeToolViewModel(repository = get()) }
    viewModel { MusicSearchToolViewModel(repository = get()) }
    viewModel { SosToolViewModel(repository = get()) }
    viewModel { MorseToolViewModel(repository = get()) }
    viewModel { FlashDimmerToolViewModel(torchRepository = get(), morseRepository = get()) }

    viewModel {
        MainViewModel(
            navigationItemsProvider = get(),
            consentRepository = get(),
            requestInAppReviewUseCase = get<RequestInAppReviewUseCase>(),
            inAppUpdateRepository = get(),
            firebaseController = get(),
            dispatchers = get(),
        )
    }
}
