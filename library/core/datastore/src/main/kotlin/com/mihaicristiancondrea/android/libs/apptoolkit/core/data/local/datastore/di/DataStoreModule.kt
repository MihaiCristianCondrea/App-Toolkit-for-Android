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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.data.local.datastore.di

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.local.CommonDataStoreCore
import com.mihaicristiancondrea.android.libs.apptoolkit.core.data.local.datastore.CommonDataStore
import com.mihaicristiancondrea.android.libs.apptoolkit.core.data.local.datastore.interfaces.AdsPreferencesDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.data.local.datastore.interfaces.AppStatePreferencesDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.data.local.datastore.interfaces.ChangelogPreferencesDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.data.local.datastore.interfaces.ConsentPreferencesDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.data.local.datastore.interfaces.DisplayPreferencesDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.data.local.datastore.interfaces.FavoritesPreferencesDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.data.local.datastore.interfaces.OnboardingPreferencesDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.data.local.datastore.interfaces.ReviewPreferencesDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.data.local.datastore.interfaces.ThemePreferencesDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.data.local.datastore.interfaces.UsageAndDiagnosticsPreferencesDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.data.repositories.DefaultDisplayPreferencesRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.data.repositories.DefaultThemePreferencesRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.data.repositories.DisplayPreferencesRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.data.repositories.ThemePreferencesRepository
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Koin module for the data store.
 */
fun dataStoreModule(): Module = module {
    single<CommonDataStore> { CommonDataStore(context = get()) }

    single<CommonDataStoreCore> { get<CommonDataStore>() }

    // Bound from the facade rather than constructed here so that exactly one instance of each
    // group exists per process.
    single<ThemePreferencesDataSource> { get<CommonDataStore>().themePreferences }
    single<DisplayPreferencesDataSource> { get<CommonDataStore>().displayPreferences }
    single<AdsPreferencesDataSource> { get<CommonDataStore>().adsPreferences }
    single<ReviewPreferencesDataSource> { get<CommonDataStore>().reviewPreferences }
    single<ChangelogPreferencesDataSource> { get<CommonDataStore>().changelogPreferences }
    single<AppStatePreferencesDataSource> { get<CommonDataStore>().appStatePreferences }
    single<FavoritesPreferencesDataSource> { get<CommonDataStore>().favoritesPreferences }
    single<OnboardingPreferencesDataSource> { get<CommonDataStore>().onboardingPreferences }
    single<ConsentPreferencesDataSource> { get<CommonDataStore>().diagnosticsPreferences }
    single<UsageAndDiagnosticsPreferencesDataSource> {
        get<CommonDataStore>().diagnosticsPreferences
    }

    // The entry points state holders use. The data sources above stay internal to this layer.
    single<ThemePreferencesRepository> {
        DefaultThemePreferencesRepository(preferences = get())
    }
    single<DisplayPreferencesRepository> {
        DefaultDisplayPreferencesRepository(preferences = get())
    }
}
