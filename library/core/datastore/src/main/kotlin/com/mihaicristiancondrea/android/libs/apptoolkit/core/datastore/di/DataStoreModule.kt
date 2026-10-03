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

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.local.CommonDataStoreCore
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.CommonDataStore
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.interfaces.AdsPreferencesDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.interfaces.AppStatePreferencesDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.interfaces.ChangelogPreferencesDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.interfaces.ConsentPreferencesDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.interfaces.DisplayPreferencesDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.interfaces.FavoritesPreferencesDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.interfaces.OnboardingPreferencesDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.interfaces.ReviewPreferencesDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.interfaces.SeasonalThemePreferencesDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.interfaces.ThemePreferencesDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.interfaces.UsageAndDiagnosticsPreferencesDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.repositories.DefaultDisplayPreferencesRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.repositories.DefaultSeasonalThemeRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.repositories.DefaultThemePreferencesRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.repositories.DisplayPreferencesRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.repositories.SeasonalThemeRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.repositories.ThemePreferencesRepository
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Binds the process-wide preference store and its repositories.
 *
 * Creates [CommonDataStore] eagerly through [CommonDataStore.getInstance] so Koin and Compose
 * share the same instance and ads default. Ads default to enabled in all builds unless a stored
 * choice overrides them.
 *
 * Data-source bindings reuse the store-owned instances, avoiding duplicate sharing coroutines.
 * State holders consume the repository bindings.
 */
fun dataStoreModule(): Module = module {
    single<CommonDataStore>(createdAtStart = true) {
        CommonDataStore.getInstance(
            context = get(),
            defaultAdsEnabled = true,
        )
    }

    single<CommonDataStoreCore> { get<CommonDataStore>() }

    single<ThemePreferencesDataSource> { get<CommonDataStore>().themePreferences }
    single<SeasonalThemePreferencesDataSource> { get<CommonDataStore>().seasonalThemePreferences }
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
