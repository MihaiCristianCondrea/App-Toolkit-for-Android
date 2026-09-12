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

package com.mihaicristiancondrea.android.libs.apptoolkit.integration.ads.di

import com.mihaicristiancondrea.android.libs.apptoolkit.integration.ads.data.managers.AdsCoreManager
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.ads.data.repositories.AdsSettingsRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.ads.data.repositories.DefaultAdsSettingsRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.ads.ui.AdsSettingsViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/** Owns the ads manager and settings bindings; host ad placement configuration is supplied separately. */
fun adsIntegrationModule(): Module = module {
    single<AdsCoreManager> {
        AdsCoreManager(
            context = get(),
            buildInfoProvider = get(),
            dispatchers = get(),
            adMobAppIdProvider = get(),
            // Injected so the manager reads the same CommonDataStore the rest of the graph uses.
            // Its default falls back to the static singleton, which is a second wrapper over the
            // same preferences file with its own eagerly started adsEnabledFlow.
            dataStore = get(),
        )
    }
    single<AdsSettingsRepository> {
        DefaultAdsSettingsRepository(
            dataStore = get(),
            firebaseController = get(),
        )
    }

    viewModel {
        AdsSettingsViewModel(
            repository = get(),
            consentRepository = get(),
            dispatchers = get(),
            firebaseController = get(),
        )
    }
}
