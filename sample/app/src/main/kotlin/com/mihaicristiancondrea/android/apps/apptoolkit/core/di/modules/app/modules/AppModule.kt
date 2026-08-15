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

import com.mihaicristiancondrea.android.apps.apptoolkit.app.main.data.repositories.AppNavigationRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.app.main.ui.MainViewModel
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.repositories.BreathingRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.repositories.CaffeineRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.repositories.DefaultToolkitTilesRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.repositories.SensorRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.repositories.SosRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.repositories.SystemRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.repositories.ToolkitTilesRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.ui.ToolkitTilesViewModel
import com.mihaicristiancondrea.android.apps.apptoolkit.core.navigation.NavigationManager
import com.mihaicristiancondrea.android.libs.apptoolkit.app.review.domain.usecases.RequestInAppReviewUseCase
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.data.repositories.NavigationRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule: Module = module {
    single { NavigationManager() }
    single<NavigationRepository> {
        AppNavigationRepository(
            dataStore = get(),
            firebaseController = get()
        )
    }
    single<ToolkitTilesRepository> { DefaultToolkitTilesRepository(context = androidContext()) }
    single {
        SensorRepository(
            context = androidContext(),
            dispatchers = get()
        )
    }
    single {
        BreathingRepository(
            context = androidContext(),
            dispatchers = get()
        )
    }
    single {
        CaffeineRepository(
            context = androidContext()
        )
    }
    single {
        SosRepository(
            context = androidContext(),
            dispatchers = get()
        )
    }
    single {
        SystemRepository(
            context = androidContext(),
            dispatchers = get()
        )
    }
    viewModel {
        ToolkitTilesViewModel(
            toolkitTilesRepository = get(),
            sensorRepository = get(),
            breathingRepository = get(),
            caffeineRepository = get(),
            systemRepository = get(),
            sosRepository = get(),
            dispatchers = get(),
            firebaseController = get(),
        )
    }

    viewModel {
        MainViewModel(
            navigationRepository = get(),
            consentRepository = get(),
            requestInAppReviewUseCase = get<RequestInAppReviewUseCase>(),
            inAppUpdateRepository = get(),
            firebaseController = get(),
            dispatchers = get(),
        )
    }
}
