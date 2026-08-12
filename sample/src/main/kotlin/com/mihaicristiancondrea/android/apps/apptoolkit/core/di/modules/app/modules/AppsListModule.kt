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

import com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.common.data.local.FavoritesLocalDataSource
import com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.common.data.local.DefaultFavoritesLocalDataSource
import com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.common.data.repository.DefaultDeveloperAppsRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.common.data.repository.DefaultFavoritesRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.common.data.repository.DefaultInstalledAppsRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.common.data.repository.DeveloperAppsRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.common.data.repository.FavoritesRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.common.data.repository.InstalledAppsRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.common.domain.usecases.FetchDeveloperAppsUseCase
import com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.common.domain.usecases.FetchAppDetailsUseCase
import com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.common.domain.usecases.GetAppInstallInfoUseCase
import com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.common.domain.usecases.GetInstalledPackagesUseCase
import com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.common.domain.usecases.ObserveFavoritesUseCase
import com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.common.domain.usecases.ToggleFavoriteUseCase
import com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.list.ui.AppsListViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.di.AppToolkitDiConstants
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appsListModule: Module = module {
    single<DeveloperAppsRepository> {
        DefaultDeveloperAppsRepository(
            client = get(),
            baseUrl = get(qualifier = named(name = AppToolkitDiConstants.ANDROID_APPS_METADATA_API_BASE_URL)),
            firebaseController = get(),
        )
    }

    single { FetchDeveloperAppsUseCase(repository = get()) }
    single { FetchAppDetailsUseCase(repository = get()) }
    single<InstalledAppsRepository> { DefaultInstalledAppsRepository(context = androidContext()) }
    single { GetInstalledPackagesUseCase(repository = get()) }
    single { GetAppInstallInfoUseCase(repository = get()) }
    viewModel {
        AppsListViewModel(
            fetchDeveloperAppsUseCase = get(),
            fetchAppDetailsUseCase = get(),
            getInstalledPackagesUseCase = get(),
            getAppInstallInfoUseCase = get(),
            observeFavoritesUseCase = get(),
            toggleFavoriteUseCase = get(),
            dispatchers = get(),
            firebaseController = get(),
        )
    }

    single<FavoritesLocalDataSource> { DefaultFavoritesLocalDataSource(dataStore = get()) }
    single<FavoritesRepository> {
        DefaultFavoritesRepository(
            local = get(),
            firebaseController = get()
        )
    }

    single { ObserveFavoritesUseCase(repository = get(), firebaseController = get()) }
    single { ToggleFavoriteUseCase(repository = get(), firebaseController = get()) }
}
