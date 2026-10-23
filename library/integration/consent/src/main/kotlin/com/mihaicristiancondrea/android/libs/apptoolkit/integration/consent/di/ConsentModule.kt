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

package com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.di

import com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.data.local.ConsentPreferencesDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.data.local.DefaultUsageAndDiagnosticsPreferencesDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.data.local.UsageAndDiagnosticsPreferencesDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.data.remote.ConsentRemoteDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.data.remote.UmpConsentRemoteDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.data.repositories.ConsentRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.data.repositories.DefaultConsentRepository
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Binds the stored consent choices, consent persistence coordination and the UMP remote source.
 * Requires foundation providers.
 *
 * One source holds the choices: the consent flow reads them through [ConsentPreferencesDataSource],
 * and the diagnostics settings write them through [UsageAndDiagnosticsPreferencesDataSource].
 */
fun consentModule(): Module = module {
    single { DefaultUsageAndDiagnosticsPreferencesDataSource(dataStore = get()) }
    single<ConsentPreferencesDataSource> { get<DefaultUsageAndDiagnosticsPreferencesDataSource>() }
    single<UsageAndDiagnosticsPreferencesDataSource> { get<DefaultUsageAndDiagnosticsPreferencesDataSource>() }
    single<ConsentRemoteDataSource> { UmpConsentRemoteDataSource(adMobAppIdDataSource = get()) }
    single<ConsentRepository> {
        DefaultConsentRepository(
            remote = get(),
            local = get(),
            buildInfoRepository = get(),
            diagnostics = get(),
            collection = get(),
        )
    }
}
