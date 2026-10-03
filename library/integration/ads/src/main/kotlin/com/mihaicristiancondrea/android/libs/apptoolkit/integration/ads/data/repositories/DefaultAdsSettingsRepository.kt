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
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.TelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.CommonDataStore
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.extensions.storageCall
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.extensions.toStorageException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart

/**
 * Reads and writes the ad preferences in [CommonDataStore], with the store's own default, which
 * the ads manager and the ad views share. It reads the cold preference flows rather than the
 * eagerly started `adsEnabledFlow`, so a read failure reaches the caller as a [StorageException].
 */
class DefaultAdsSettingsRepository(
    private val dataStore: CommonDataStore,
    private val telemetryRepository: TelemetryRepository,
) : AdsSettingsRepository {

    override fun observeAdsEnabled(): Flow<Boolean> =
        dataStore.ads(default = dataStore.defaultAdsEnabled)
            .onStart {
                telemetryRepository.logBreadcrumb(
                    message = "Ads settings observe",
                    attributes = mapOf("defaultAdsEnabled" to dataStore.defaultAdsEnabled.toString()),
                )
            }
            .asStorageFlow()

    override fun observeReduceAds(): Flow<Boolean> = dataStore.reduceAds.asStorageFlow()

    override suspend fun setAdsEnabled(enabled: Boolean) =
        persistPreference(breadcrumb = "Ads settings updated", enabled = enabled) {
            dataStore.saveAds(isChecked = enabled)
        }

    override suspend fun setReduceAds(enabled: Boolean) =
        persistPreference(breadcrumb = "Reduce ads setting updated", enabled = enabled) {
            dataStore.saveReduceAds(isChecked = enabled)
        }

    private suspend fun persistPreference(
        breadcrumb: String,
        enabled: Boolean,
        save: suspend () -> Unit,
    ) {
        telemetryRepository.logBreadcrumb(
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
