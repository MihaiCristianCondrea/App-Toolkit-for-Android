package com.d4rk.android.apps.apptoolkit.core.di.modules

import android.content.Context
import com.d4rk.android.apps.apptoolkit.BuildConfig
import com.d4rk.android.apps.apptoolkit.R
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
import com.d4rk.android.apps.apptoolkit.app.onboarding.utils.interfaces.providers.AppOnboardingProvider
import com.d4rk.android.apps.apptoolkit.core.data.datastore.DataStore
import com.d4rk.android.apps.apptoolkit.core.data.favorites.FavoritesRepositoryImpl
import com.d4rk.android.libs.apptoolkit.app.main.data.repository.MainRepositoryImpl
import com.d4rk.android.libs.apptoolkit.app.main.domain.repository.NavigationRepository
import com.d4rk.android.libs.apptoolkit.app.onboarding.utils.interfaces.providers.OnboardingProvider
import com.d4rk.android.libs.apptoolkit.core.utils.constants.api.ApiConstants
import com.d4rk.android.libs.apptoolkit.core.utils.constants.api.ApiEnvironments
import com.d4rk.android.libs.apptoolkit.data.client.KtorClient
import com.d4rk.android.libs.apptoolkit.data.core.ads.AdsCoreManager
import com.d4rk.android.libs.apptoolkit.data.datastore.CommonDataStore
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule : Module = module {
    single { DataStore(context = get(), dispatchers = get()) }
    single<CommonDataStore> { get<DataStore>() }
    single<AdsCoreManager> { AdsCoreManager(context = get(), buildInfoProvider = get(), dispatchers = get()) }
    single { KtorClient.createClient(enableLogging = BuildConfig.DEBUG) }

    single<FavoritesRepository> { FavoritesRepositoryImpl(context = get(), dataStore = get(), dispatchers = get()) }
    single { ObserveFavoritesUseCase(repository = get()) }
    single { ToggleFavoriteUseCase(repository = get()) }
    single {
        ObserveFavoriteAppsUseCase(
            fetchDeveloperAppsUseCase = get(),
            observeFavoritesUseCase = get(),
            dispatchers = get(),
        )
    }

    single<List<String>>(qualifier = named(name = "startup_entries")) {
        get<Context>().resources.getStringArray(R.array.preference_startup_entries).toList()
    }

    single<List<String>>(qualifier = named(name = "startup_values")) {
        get<Context>().resources.getStringArray(R.array.preference_startup_values).toList()
    }

    single<OnboardingProvider> { AppOnboardingProvider() }

    single<NavigationRepository> { MainRepositoryImpl(dispatchers = get()) }

    viewModel { MainViewModel(navigationRepository = get()) }

    single<String>(qualifier = named(name = "developer_apps_base_url")) {
        val environment = ApiEnvironments.fromBuildType(isDebugBuild = BuildConfig.DEBUG)
        ApiConstants.developerAppsBaseUrl(environment = environment)
    }

    single<DeveloperAppsRepository> {
        DeveloperAppsRepositoryImpl(
            client = get(),
            baseUrl = get(qualifier = named(name = "developer_apps_base_url")),
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
    viewModel {
        FavoriteAppsViewModel(
            observeFavoriteAppsUseCase = get(),
            observeFavoritesUseCase = get(),
            toggleFavoriteUseCase = get(),
            dispatchers = get(),
        )
    }
}
