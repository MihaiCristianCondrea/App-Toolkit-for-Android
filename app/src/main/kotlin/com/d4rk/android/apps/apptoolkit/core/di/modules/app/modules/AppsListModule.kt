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

package com.d4rk.android.apps.apptoolkit.core.di.modules.app.modules

import com.d4rk.android.apps.apptoolkit.app.apps.common.data.local.FavoritesLocalDataSource
import com.d4rk.android.apps.apptoolkit.app.apps.common.data.local.FavoritesLocalDataSourceImpl
import com.d4rk.android.apps.apptoolkit.app.apps.common.data.repository.DeveloperAppsRepositoryImpl
import com.d4rk.android.apps.apptoolkit.app.apps.common.data.repository.FavoritesRepositoryImpl
import com.d4rk.android.apps.apptoolkit.app.apps.common.domain.repository.DeveloperAppsRepository
import com.d4rk.android.apps.apptoolkit.app.apps.common.domain.repository.FavoritesRepository
import com.d4rk.android.apps.apptoolkit.app.apps.common.domain.usecases.FetchDeveloperAppsUseCase
import com.d4rk.android.apps.apptoolkit.app.apps.common.domain.usecases.ObserveFavoriteAppsUseCase
import com.d4rk.android.apps.apptoolkit.app.apps.common.domain.usecases.ObserveFavoritesUseCase
import com.d4rk.android.apps.apptoolkit.app.apps.common.domain.usecases.ToggleFavoriteUseCase
import com.d4rk.android.apps.apptoolkit.app.apps.favorites.ui.FavoriteAppsViewModel
import com.d4rk.android.apps.apptoolkit.app.apps.list.ui.AppsListViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appsListModule: Module = module {
    single<DeveloperAppsRepository> {
        DeveloperAppsRepositoryImpl(
            client = get(),
            baseUrl = get(qualifier = named(name = "developer_apps_api_url")),
            firebaseController = get(),
        )
    }

    single { FetchDeveloperAppsUseCase(repository = get(), firebaseController = get()) }
    viewModel {
        AppsListViewModel(
            fetchDeveloperAppsUseCase = get(),
            observeFavoritesUseCase = get(),
            toggleFavoriteUseCase = get(),
            dispatchers = get(),
            firebaseController = get(),
        )
    }

    single<FavoritesLocalDataSource> { FavoritesLocalDataSourceImpl(dataStore = get()) }
    single<FavoritesRepository> { FavoritesRepositoryImpl(local = get(), firebaseController = get()) }

    single { ObserveFavoritesUseCase(repository = get(), firebaseController = get()) }
    single { ToggleFavoriteUseCase(repository = get(), firebaseController = get()) }
    single {
        ObserveFavoriteAppsUseCase(
            fetchDeveloperAppsUseCase = get(),
            observeFavoritesUseCase = get(),
            firebaseController = get(),
        )
    }

    viewModel {
        FavoriteAppsViewModel(
            observeFavoriteAppsUseCase = get(),
            observeFavoritesUseCase = get(),
            toggleFavoriteUseCase = get(),
            dispatchers = get(),
            firebaseController = get(),
        )
    }
}
