package com.d4rk.android.apps.apptoolkit.core.di.modules

import com.d4rk.android.apps.apptoolkit.BuildConfig
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
import com.d4rk.android.apps.apptoolkit.app.main.ui.MainViewModel
import com.d4rk.android.apps.apptoolkit.core.data.datastore.DataStore
import com.d4rk.android.libs.apptoolkit.app.main.data.repository.MainRepositoryImpl
import com.d4rk.android.libs.apptoolkit.app.main.domain.repository.NavigationRepository
import com.d4rk.android.libs.apptoolkit.core.data.firebase.FirebaseControllerImpl
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import com.d4rk.android.libs.apptoolkit.core.utils.constants.api.ApiLanguages
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.boolean.toApiEnvironment
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.string.developerAppsApiUrl
import com.d4rk.android.libs.apptoolkit.data.core.ads.AdsCoreManager
import com.d4rk.android.libs.apptoolkit.data.local.datastore.CommonDataStore
import com.d4rk.android.libs.apptoolkit.data.remote.client.KtorClient
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule: Module = module {
    single { DataStore(context = get(), dispatchers = get()) }
    single<CommonDataStore> { get<DataStore>() }
    single<AdsCoreManager> {
        AdsCoreManager(
            context = get(),
            buildInfoProvider = get(),
            dispatchers = get()
        )
    }
    single<FirebaseController> { FirebaseControllerImpl() }
    single { KtorClient.createClient(enableLogging = BuildConfig.DEBUG) }
    single<NavigationRepository> { MainRepositoryImpl(dispatchers = get()) }
    viewModel { MainViewModel(navigationRepository = get()) }

    single<String>(qualifier = named(name = "developer_apps_api_url")) {
        val environment = BuildConfig.DEBUG.toApiEnvironment()
        environment.developerAppsApiUrl(language = ApiLanguages.DEFAULT)
    }

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
