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
import com.mihaicristiancondrea.android.libs.apptoolkit.core.data.local.datastore.interfaces.ConsentPreferencesDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.data.local.datastore.interfaces.OnboardingPreferencesDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.data.local.datastore.interfaces.UsageAndDiagnosticsPreferencesDataSource
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Koin module for the data store.
 */
fun dataStoreModule(isDebugBuild: Boolean): Module = module {
    single<CommonDataStore> {
        CommonDataStore(
            context = get(),
            dispatchers = get(),
            defaultAdsEnabled = !isDebugBuild,
        )
    }

    single<CommonDataStoreCore> { get<CommonDataStore>() }
    single<ConsentPreferencesDataSource> { get<CommonDataStore>() }
    single<OnboardingPreferencesDataSource> { get<CommonDataStore>() }
    single<UsageAndDiagnosticsPreferencesDataSource> { get<CommonDataStore>() }
}
