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

package com.mihaicristiancondrea.android.libs.apptoolkit.integration.ads.data.repositories

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.exceptions.StorageException
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.DiagnosticsReporter
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.ads.data.local.AdsPreferencesDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.AdsSettingsRepository
import kotlinx.coroutines.flow.StateFlow
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.extensions.storageCall
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.extensions.toStorageException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart

/**
 * Reads and writes the ad preferences in [AdsPreferencesDataSource], with the store's own default, which
 * the ads manager and the ad views share. It reads the cold preference flows rather than the
 * eagerly started [adsEnabled], so a read failure reaches the caller as a [StorageException].
 */
class DefaultAdsSettingsRepository(
    private val preferences: AdsPreferencesDataSource,
    private val diagnostics: DiagnosticsReporter,
) : AdsSettingsRepository {
    override val adsEnabled: StateFlow<Boolean> get() = preferences.adsEnabled

    override fun observeAdsEnabled(): Flow<Boolean> =
        preferences.ads(default = preferences.defaultAdsEnabled)
            .onStart {
                diagnostics.logBreadcrumb(
                    message = "Ads settings observe",
                    attributes = mapOf("defaultAdsEnabled" to preferences.defaultAdsEnabled.toString()),
                )
            }
            .asStorageFlow()

    override fun observeReduceAds(): Flow<Boolean> = preferences.reduceAds.asStorageFlow()

    override suspend fun setAdsEnabled(enabled: Boolean) =
        persistPreference(breadcrumb = "Ads settings updated", enabled = enabled) {
            preferences.saveAds(isChecked = enabled)
        }

    override suspend fun setReduceAds(enabled: Boolean) =
        persistPreference(breadcrumb = "Reduce ads setting updated", enabled = enabled) {
            preferences.saveReduceAds(isChecked = enabled)
        }

    private suspend fun persistPreference(
        breadcrumb: String,
        enabled: Boolean,
        save: suspend () -> Unit,
    ) {
        diagnostics.logBreadcrumb(
            message = breadcrumb,
            attributes = mapOf("enabled" to enabled.toString()),
        )
        storageCall { save() }
    }

    /**
     * Rethrows a storage failure of this flow as a [StorageException], the flow counterpart of
     * [storageCall]. Cancellation and other failures pass through unchanged.
     */
    private fun Flow<Boolean>.asStorageFlow(): Flow<Boolean> =
        catch { failure -> throw failure.toStorageException() ?: failure }
}
