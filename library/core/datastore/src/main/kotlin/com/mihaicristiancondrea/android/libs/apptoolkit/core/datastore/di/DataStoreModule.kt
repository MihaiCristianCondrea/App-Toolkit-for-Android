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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.di

import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.CommonDataStore
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.AppStatePreferencesDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.ConsentPreferencesDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.DisplayPreferencesDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.FavoritesPreferencesDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.SeasonalThemePreferencesDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.ThemePreferencesDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.UsageAndDiagnosticsPreferencesDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.repositories.DefaultDisplayPreferencesRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.repositories.DefaultSeasonalThemeRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.repositories.DefaultThemePreferencesRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.repositories.DisplayPreferencesRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.repositories.SeasonalThemeRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.repositories.ThemePreferencesRepository
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import org.koin.core.module.Module
import org.koin.dsl.module

/** Binds the shared settings file and the cross-feature preference repositories. */
fun dataStoreModule(): Module = module {
    single<CommonDataStore>(createdAtStart = true) {
        CommonDataStore.getInstance(
            context = get(),
        )
    }

    single<DataStore<Preferences>> { get<CommonDataStore>().dataStore }

    single<ThemePreferencesDataSource> { get<CommonDataStore>().themePreferences }
    single<SeasonalThemePreferencesDataSource> { get<CommonDataStore>().seasonalThemePreferences }
    single<DisplayPreferencesDataSource> { get<CommonDataStore>().displayPreferences }
    single<AppStatePreferencesDataSource> { get<CommonDataStore>().appStatePreferences }
    single<FavoritesPreferencesDataSource> { get<CommonDataStore>().favoritesPreferences }
    single<ConsentPreferencesDataSource> { get<CommonDataStore>().diagnosticsPreferences }
    single<UsageAndDiagnosticsPreferencesDataSource> {
        get<CommonDataStore>().diagnosticsPreferences
    }

    single<ThemePreferencesRepository> {
        DefaultThemePreferencesRepository(preferences = get())
    }
    single<DisplayPreferencesRepository> {
        DefaultDisplayPreferencesRepository(preferences = get())
    }
    single<SeasonalThemeRepository> {
        DefaultSeasonalThemeRepository(seasonal = get(), theme = get())
    }
}
