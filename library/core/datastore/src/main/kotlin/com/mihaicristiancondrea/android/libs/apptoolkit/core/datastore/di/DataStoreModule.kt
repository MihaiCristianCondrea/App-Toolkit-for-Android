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
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.interfaces.ThemePreferencesDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.interfaces.UsageAndDiagnosticsPreferencesDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.repositories.DefaultDisplayPreferencesRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.repositories.DefaultThemePreferencesRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.repositories.DisplayPreferencesRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.repositories.ThemePreferencesRepository
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Koin module for the data store.
 */
fun dataStoreModule(): Module = module {
    // Through `getInstance`, not the constructor, and eagerly: this is what makes the graph's store
    // and the one `rememberCommonDataStore()` hands to Compose the same object. Built lazily via
    // the constructor, Koin's copy never populated the companion, so the first `getInstance` caller
    // created a second store, and the two then disagreed about whether ads were on for any install
    // with no stored value. `createdAtStart` closes the race for good: Koin starts in
    // `Application.onCreate`, long before anything can compose.
    //
    // Ads default on in every build, debug included. The default used to be `!isDebugBuild`, which
    // meant a fresh debug install had no ads at all until someone found the switch, so the one build
    // where ad rendering is actually inspected was the one build that rendered none. A developer who
    // turns them off still has that stored and honoured; only the starting point changed.
    single<CommonDataStore>(createdAtStart = true) {
        CommonDataStore.getInstance(
            context = get(),
            defaultAdsEnabled = true,
        )
    }

    single<CommonDataStoreCore> { get<CommonDataStore>() }

    // Bound from the facade rather than constructed here so that exactly one instance of each
    // group exists per process. DefaultAdsPreferencesDataSource in particular starts an eager
    // collector, so a second copy would observe the same preference twice.
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
