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

import com.mihaicristiancondrea.android.apps.apptoolkit.app.main.data.repository.MainNavigationRepositoryImpl
import com.mihaicristiancondrea.android.apps.apptoolkit.app.main.domain.usecases.GetNavigationDrawerItemsUseCase
import com.mihaicristiancondrea.android.apps.apptoolkit.app.main.ui.MainViewModel
import com.mihaicristiancondrea.android.apps.apptoolkit.app.main.ui.navigation.NavigationManager
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.repository.BreathingRepositoryImpl
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.repository.CaffeineRepositoryImpl
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.repository.SensorRepositoryImpl
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.repository.SosRepositoryImpl
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.repository.SystemMonitorRepositoryImpl
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.repository.SystemRepositoryImpl
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.repository.ToolkitTilesRepositoryImpl
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.domain.repository.BreathingRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.domain.repository.CaffeineRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.domain.repository.SensorRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.domain.repository.SosRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.domain.repository.SystemMonitorRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.domain.repository.SystemRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.domain.repository.ToolkitTilesRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.domain.usecase.GetBreathingDataUseCase
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.domain.usecase.GetSensorDataUseCase
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.domain.usecase.GetSystemDataUseCase
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.domain.usecase.GetToolkitTilesUseCase
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.domain.usecase.SyncToolkitTileStatusesUseCase
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.ui.ToolkitTilesViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.app.main.domain.repository.NavigationRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.app.review.domain.usecases.RequestInAppReviewUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule: Module = module {
    single { NavigationManager() }
    single<NavigationRepository> {
        MainNavigationRepositoryImpl(
            dataStore = get(),
            firebaseController = get()
        )
    }
    single<GetNavigationDrawerItemsUseCase> {
        GetNavigationDrawerItemsUseCase(navigationRepository = get(), firebaseController = get())
    }
    single { GetToolkitTilesUseCase() }
    single { GetSensorDataUseCase(sensorRepository = get()) }
    single { GetBreathingDataUseCase(breathingRepository = get()) }
    single { GetSystemDataUseCase(repository = get()) }
    single { SyncToolkitTileStatusesUseCase(repository = get()) }
    single<ToolkitTilesRepository> { ToolkitTilesRepositoryImpl(context = androidContext()) }
    single<SensorRepository> {
        SensorRepositoryImpl(
            context = androidContext(),
            dispatchers = get()
        )
    }
    single<BreathingRepository> {
        BreathingRepositoryImpl(
            context = androidContext(),
            dispatchers = get()
        )
    }
    single<CaffeineRepository> {
        CaffeineRepositoryImpl(
            context = androidContext()
        )
    }
    single<SosRepository> {
        SosRepositoryImpl(
            context = androidContext(),
            dispatchers = get()
        )
    }
    single<SystemMonitorRepository> {
        SystemMonitorRepositoryImpl(
            context = androidContext(),
            dispatchers = get()
        )
    }
    single<SystemRepository> {
        SystemRepositoryImpl(
            context = androidContext(),
            dispatchers = get()
        )
    }
    viewModel {
        ToolkitTilesViewModel(
            getToolkitTilesUseCase = get(),
            syncToolkitTileStatusesUseCase = get(),
            getSensorDataUseCase = get(),
            getBreathingDataUseCase = get(),
            caffeineRepository = get(),
            systemRepository = get(),
            sosRepository = get(),
            dispatchers = get(),
            firebaseController = get(),
        )
    }

    viewModel {
        MainViewModel(
            getNavigationDrawerItemsUseCase = get(),
            applyInitialConsentUseCase = get(),
            requestConsentUseCase = get(),
            requestInAppReviewUseCase = get<RequestInAppReviewUseCase>(),
            requestInAppUpdateUseCase = get(),
            firebaseController = get(),
            dispatchers = get(),
        )
    }
}
