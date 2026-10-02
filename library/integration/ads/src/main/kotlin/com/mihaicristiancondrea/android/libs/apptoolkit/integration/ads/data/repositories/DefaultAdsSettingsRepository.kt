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

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.TelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.CommonDataStore
import com.mihaicristiancondrea.android.libs.apptoolkit.core.network.domain.models.network.DataState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.network.domain.models.network.Errors
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onStart

/**
 * Reads and writes persisted ad settings using the store-owned default shared by the manager
 * and ad views. Observation uses the cold preference flow so read failures and cancellation
 * reach the caller. Writes return errors as [DataState] values while preserving cancellation.
 */
class DefaultAdsSettingsRepository(
    private val dataStore: CommonDataStore,
    private val telemetryRepository: TelemetryRepository,
) : AdsSettingsRepository {

    override val defaultAdsEnabled: Boolean = dataStore.defaultAdsEnabled

    override fun observeAdsEnabled(): Flow<Boolean> =
        dataStore.ads(default = defaultAdsEnabled)
            .onStart {
                telemetryRepository.logBreadcrumb(
                    message = "Ads settings observe",
                    attributes = mapOf("defaultAdsEnabled" to defaultAdsEnabled.toString()),
                )
            }

    override fun observeReduceAds(): Flow<Boolean> = dataStore.reduceAds

    override suspend fun setAdsEnabled(enabled: Boolean): DataState<Unit, Errors.Database> =
        persistPreference(
            breadcrumb = "Ads settings updated",
            enabled = enabled,
        ) { dataStore.saveAds(isChecked = enabled) }

    override suspend fun setReduceAds(enabled: Boolean): DataState<Unit, Errors.Database> =
        persistPreference(
            breadcrumb = "Reduce ads setting updated",
            enabled = enabled,
        ) { dataStore.saveReduceAds(isChecked = enabled) }

    private suspend fun persistPreference(
        breadcrumb: String,
        enabled: Boolean,
        save: suspend () -> Unit,
    ): DataState<Unit, Errors.Database> {
        telemetryRepository.logBreadcrumb(
            message = breadcrumb,
            attributes = mapOf("enabled" to enabled.toString()),
        )
        return runCatching { save() }.fold(
            onSuccess = { DataState.Success(Unit) },
            onFailure = { throwable ->
                if (throwable is CancellationException) throw throwable
                telemetryRepository.recordNonFatal(throwable = throwable)
                DataState.Error(error = Errors.Database.DATABASE_OPERATION_FAILED)
            },
        )
    }
}
