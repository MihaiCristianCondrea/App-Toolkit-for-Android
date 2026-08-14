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

import com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.common.data.local.DefaultDeveloperAppsLocalDataSource
import com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.common.data.local.DefaultFavoritesLocalDataSource
import com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.common.data.local.DeveloperAppsLocalDataSource
import com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.common.data.local.FavoritesLocalDataSource
import com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.common.data.repositories.DefaultDeveloperAppsRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.common.data.repositories.DefaultFavoritesRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.common.data.repositories.DefaultInstalledAppsRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.common.data.repositories.DeveloperAppsRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.common.data.repositories.FavoritesRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.common.data.repositories.InstalledAppsRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.list.ui.AppsListViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.di.AppToolkitDiConstants
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appsListModule: Module = module {
    single<DeveloperAppsLocalDataSource> {
        DefaultDeveloperAppsLocalDataSource(
            cacheFile = androidContext().filesDir.resolve("developer_apps/catalogue.json"),
            json = Json { ignoreUnknownKeys = true },
            dispatchers = get(),
        )
    }

    single<DeveloperAppsRepository> {
        DefaultDeveloperAppsRepository(
            client = get(),
            baseUrl = get(qualifier = named(name = AppToolkitDiConstants.ANDROID_APPS_METADATA_API_BASE_URL)),
            firebaseController = get(),
            localDataSource = get(),
        )
    }

    single<InstalledAppsRepository> { DefaultInstalledAppsRepository(context = androidContext()) }

    viewModel {
        AppsListViewModel(
            developerAppsRepository = get(),
            installedAppsRepository = get(),
            favoritesRepository = get(),
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
}
