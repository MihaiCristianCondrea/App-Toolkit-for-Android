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

package com.mihaicristiancondrea.android.libs.apptoolkit.app.ads.data.repositories

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.FirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.data.local.datastore.CommonDataStore
import com.mihaicristiancondrea.android.libs.apptoolkit.core.domain.models.network.DataState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.domain.models.network.Errors
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onStart

/**
 * Concrete implementation of [AdsSettingsRepository].
 *
 * [CommonDataStore] owns the persistence, so this repository never recomputes a default; reducing
 * ads is an opt-in and its stored `false` is the only starting point.
 *
 * @param dataStore The data store used for persisting ad settings.
 */
class DefaultAdsSettingsRepository(
    private val dataStore: CommonDataStore,
    private val firebaseController: FirebaseController,
) : AdsSettingsRepository {

    // The cold flow rather than a shared one: the settings screen needs IO errors and cancellation
    // to reach it, and an eagerly-started StateFlow swallows both into its own scope.
    override fun observeReduceAds(): Flow<Boolean> =
        dataStore.reduceAds
            .onStart {
                firebaseController.logBreadcrumb(message = "Reduce ads settings observe")
            }

    // Returns the failure as a value rather than throwing: the settings screen renders it, and a
    // raw throw from a suspend call reaches the ViewModel's crash reporter instead of the snackbar.
    override suspend fun setReduceAds(enabled: Boolean): DataState<Unit, Errors.Database> {
        firebaseController.logBreadcrumb(
            message = "Reduce ads settings updated",
            attributes = mapOf("enabled" to enabled.toString()),
        )
        return runCatching { dataStore.saveReduceAds(isChecked = enabled) }.fold(
            onSuccess = { DataState.Success(Unit) },
            onFailure = { throwable ->
                if (throwable is CancellationException) throw throwable
                firebaseController.recordNonFatal(throwable = throwable)
                DataState.Error(error = Errors.Database.DATABASE_OPERATION_FAILED)
            },
        )
    }
}
