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

/**
 * Binds ad settings and the manager to the same [CommonDataStore] instance. Hosts supply
 * placement configuration separately.
 */
fun adsIntegrationModule(): Module = module {
    single<AdsCoreManager> {
        AdsCoreManager(
            context = get(),
            buildInfoProvider = get(),
            dispatchers = get(),
            adMobAppIdProvider = get(),
            dataStore = get(),
        )
    }
    single<AdsSettingsRepository> {
        DefaultAdsSettingsRepository(
            dataStore = get(),
            telemetryRepository = get(),
        )
    }

    viewModel {
        AdsSettingsViewModel(
            repository = get(),
            consentRepository = get(),
            telemetryRepository = get(),
        )
    }
}
