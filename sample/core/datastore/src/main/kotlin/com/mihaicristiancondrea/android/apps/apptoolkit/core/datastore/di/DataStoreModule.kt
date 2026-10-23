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

package com.mihaicristiancondrea.android.apps.apptoolkit.core.datastore.di

import com.mihaicristiancondrea.android.apps.apptoolkit.core.datastore.data.local.AppPreferencesDataSource
import com.mihaicristiancondrea.android.apps.apptoolkit.core.datastore.data.local.DefaultAppPreferencesDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.di.DataStoreQualifiers
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

/** Binds the sample's own preferences over the Toolkit's shared settings file. */
val dataStoreModule: Module = module {
    single<AppPreferencesDataSource> {
        DefaultAppPreferencesDataSource(
            dataStore = get(qualifier = named(DataStoreQualifiers.SETTINGS)),
            displayPreferences = get(),
            onboardingRepository = get(),
        )
    }
}
