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


package com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.sources

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.datastore.DataStoreNamesConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.interfaces.ConsentPreferencesDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.interfaces.UsageAndDiagnosticsPreferencesDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

/**
 * Consent and diagnostics preferences stored in the shared `settings` Preferences DataStore.
 *
 * Implements both contracts because they overlap on the usage-and-diagnostics toggle: the consent
 * feature reads it alongside the ad consents, the diagnostics feature also writes it.
 */
class DefaultDiagnosticsPreferencesDataSource(
    private val dataStore: DataStore<Preferences>,
) : UsageAndDiagnosticsPreferencesDataSource, ConsentPreferencesDataSource {

    private val usageAndDiagnosticsKey =
        booleanPreferencesKey(name = DataStoreNamesConstants.DATA_STORE_USAGE_AND_DIAGNOSTICS)
    private val analyticsConsentKey =
        booleanPreferencesKey(name = DataStoreNamesConstants.DATA_STORE_ANALYTICS_CONSENT)
    private val adStorageConsentKey =
        booleanPreferencesKey(name = DataStoreNamesConstants.DATA_STORE_AD_STORAGE_CONSENT)
    private val adUserDataConsentKey =
        booleanPreferencesKey(name = DataStoreNamesConstants.DATA_STORE_AD_USER_DATA_CONSENT)
    private val adPersonalizationConsentKey =
        booleanPreferencesKey(name = DataStoreNamesConstants.DATA_STORE_AD_PERSONALIZATION_CONSENT)

    private fun booleanFlow(key: Preferences.Key<Boolean>, default: Boolean): Flow<Boolean> =
        dataStore.data.map { preferences: Preferences ->
            preferences[key] ?: default
        }.distinctUntilChanged()

    private suspend fun save(key: Preferences.Key<Boolean>, value: Boolean) {
        dataStore.edit { preferences: MutablePreferences ->
            preferences[key] = value
        }
    }

    override fun usageAndDiagnostics(default: Boolean): Flow<Boolean> =
        booleanFlow(key = usageAndDiagnosticsKey, default = default)

    override suspend fun saveUsageAndDiagnostics(isChecked: Boolean) =
        save(key = usageAndDiagnosticsKey, value = isChecked)

    override fun analyticsConsent(default: Boolean): Flow<Boolean> =
        booleanFlow(key = analyticsConsentKey, default = default)

    override suspend fun saveAnalyticsConsent(isGranted: Boolean) =
        save(key = analyticsConsentKey, value = isGranted)

    override fun adStorageConsent(default: Boolean): Flow<Boolean> =
        booleanFlow(key = adStorageConsentKey, default = default)

    override suspend fun saveAdStorageConsent(isGranted: Boolean) =
        save(key = adStorageConsentKey, value = isGranted)

    override fun adUserDataConsent(default: Boolean): Flow<Boolean> =
        booleanFlow(key = adUserDataConsentKey, default = default)

    override suspend fun saveAdUserDataConsent(isGranted: Boolean) =
        save(key = adUserDataConsentKey, value = isGranted)

    override fun adPersonalizationConsent(default: Boolean): Flow<Boolean> =
        booleanFlow(key = adPersonalizationConsentKey, default = default)

    override suspend fun saveAdPersonalizationConsent(isGranted: Boolean) =
        save(key = adPersonalizationConsentKey, value = isGranted)
}
