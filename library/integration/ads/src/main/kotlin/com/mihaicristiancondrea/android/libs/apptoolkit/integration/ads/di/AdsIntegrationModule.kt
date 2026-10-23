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

import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.di.DataStoreQualifiers
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.ads.ui.utils.AppOpenAdController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.AdsSettingsRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.ads.data.repositories.DefaultAdsSettingsRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.ads.ui.AdsSettingsViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.ads.data.local.AdsPreferencesDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.ads.data.local.DefaultAdsPreferencesDataSource
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.dsl.onClose

/**
 * Binds ad settings and the controller to one shared ads source. Hosts supply
 * placement configuration separately.
 *
 * @param defaultAdsEnabled Used only when storage contains no ads enablement choice.
 */
fun adsIntegrationModule(defaultAdsEnabled: Boolean = true): Module = module {
    single<AdsPreferencesDataSource>(createdAtStart = true) {
        DefaultAdsPreferencesDataSource(dataStore = get(qualifier = named(DataStoreQualifiers.SETTINGS)), dispatchers = get(), defaultAdsEnabled = defaultAdsEnabled)
    } onClose { source -> source?.close() }

    single<AppOpenAdController> {
        AppOpenAdController(
            context = get(),
            buildInfoRepository = get(),
            dispatchers = get(),
            adMobAppIdDataSource = get(),
            adsSettingsRepository = get(),
        )
    } onClose { controller -> controller?.close() }
    single<AdsSettingsRepository> {
        DefaultAdsSettingsRepository(
            preferences = get(),
            diagnostics = get(),
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
