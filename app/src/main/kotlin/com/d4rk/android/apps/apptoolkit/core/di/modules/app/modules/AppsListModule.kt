package com.d4rk.android.apps.apptoolkit.core.di.modules.app.modules

import com.d4rk.android.apps.apptoolkit.app.apps.favorites.data.local.FavoritesLocalDataSource
import com.d4rk.android.apps.apptoolkit.app.apps.favorites.data.local.FavoritesLocalDataSourceImpl
import com.d4rk.android.apps.apptoolkit.app.apps.favorites.data.repository.FavoritesRepositoryImpl
import com.d4rk.android.apps.apptoolkit.app.apps.favorites.domain.repository.FavoritesRepository
import com.d4rk.android.apps.apptoolkit.app.apps.favorites.domain.usecases.ObserveFavoriteAppsUseCase
import com.d4rk.android.apps.apptoolkit.app.apps.favorites.domain.usecases.ObserveFavoritesUseCase
import com.d4rk.android.apps.apptoolkit.app.apps.favorites.domain.usecases.ToggleFavoriteUseCase
import com.d4rk.android.apps.apptoolkit.app.apps.favorites.ui.FavoriteAppsViewModel
import com.d4rk.android.apps.apptoolkit.app.apps.list.data.repository.DeveloperAppsRepositoryImpl
import com.d4rk.android.apps.apptoolkit.app.apps.list.domain.repository.DeveloperAppsRepository
import com.d4rk.android.apps.apptoolkit.app.apps.list.domain.usecases.FetchDeveloperAppsUseCase
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
        )
    }

    single { FetchDeveloperAppsUseCase(repository = get()) }
    viewModel {
        AppsListViewModel(
            fetchDeveloperAppsUseCase = get(),
            observeFavoritesUseCase = get(),
            toggleFavoriteUseCase = get(),
            dispatchers = get(),
        )
    }

    single<FavoritesLocalDataSource> { FavoritesLocalDataSourceImpl(dataStore = get()) }
    single<FavoritesRepository> { FavoritesRepositoryImpl(local = get()) }

    single { ObserveFavoritesUseCase(repository = get()) }
    single { ToggleFavoriteUseCase(repository = get()) }
    single {
        ObserveFavoriteAppsUseCase(
            fetchDeveloperAppsUseCase = get(),
            observeFavoritesUseCase = get()
        )
    }

    viewModel {
        FavoriteAppsViewModel(
            observeFavoriteAppsUseCase = get(),
            observeFavoritesUseCase = get(),
            toggleFavoriteUseCase = get(),
            dispatchers = get(),
        )
    }
}
