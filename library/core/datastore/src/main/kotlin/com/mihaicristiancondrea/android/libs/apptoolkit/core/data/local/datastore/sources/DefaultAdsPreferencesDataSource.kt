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


package com.mihaicristiancondrea.android.libs.apptoolkit.core.data.local.datastore.sources

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.coroutines.dispatchers.DispatcherProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.datastore.DataStoreNamesConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.data.local.datastore.interfaces.AdsPreferencesDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/**
 * Ads preference stored in the shared `settings` Preferences DataStore.
 *
 * Owns the single eagerly started [adsEnabled] StateFlow. Constructing this class more than once
 * per process starts a second collector over the same preference, so it is registered as a
 * singleton and reached through the graph rather than built on demand.
 */
class DefaultAdsPreferencesDataSource(
    private val dataStore: DataStore<Preferences>,
    dispatchers: DispatcherProvider,
    private val defaultAdsEnabled: Boolean,
) : AdsPreferencesDataSource {

    private val scope = CoroutineScope(SupervisorJob() + dispatchers.io)

    private val adsKey = booleanPreferencesKey(name = DataStoreNamesConstants.DATA_STORE_ADS)

    override fun ads(default: Boolean): Flow<Boolean> =
        dataStore.data.map { preferences: Preferences ->
            preferences[adsKey] ?: default
        }.distinctUntilChanged()

    override val adsEnabled: StateFlow<Boolean> = ads(default = defaultAdsEnabled).stateIn(
        scope = scope,
        started = SharingStarted.Eagerly,
        initialValue = defaultAdsEnabled,
    )

    override suspend fun saveAds(isChecked: Boolean) {
        dataStore.edit { preferences: MutablePreferences ->
            preferences[adsKey] = isChecked
        }
    }

    override fun close() {
        scope.cancel()
    }
}
