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

package com.mihaicristiancondrea.android.libs.apptoolkit.app.ads.data.repository

import com.mihaicristiancondrea.android.libs.apptoolkit.app.ads.domain.repository.AdsSettingsRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.data.local.datastore.CommonDataStore
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.model.Result
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.repository.FirebaseController
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onStart

/**
 * Concrete implementation of [AdsSettingsRepository].
 *
 * This class manages the persistence and retrieval of ad-related settings,
 * specifically whether ads are enabled or disabled. It uses [CommonDataStore]
 * for data persistence and [BuildInfoProvider] to determine the default ad state
 * based on the build type (e.g., disabling ads for debug builds).
 *
 * @param dataStore The data store used for persisting ad settings.
 * @param buildInfoProvider Provider for build-specific information, like whether it's a debug build.
 */
class AdsSettingsRepositoryImpl(
    private val dataStore: CommonDataStore,
    private val firebaseController: FirebaseController,
) : AdsSettingsRepository {

    // Deliberately delegated rather than recomputed. `AdsCoreManager` gates SDK initialization on
    // the same preference and the ad views observe it; a repository with its own default is how the
    // two came to disagree before, which made ad views request ads for an uninitialized SDK.
    override val defaultAdsEnabled: Boolean = dataStore.defaultAdsEnabled

    // The cold `ads(...)` flow rather than `adsEnabledFlow`: the settings screen needs IO errors and
    // cancellation to reach it, and the eagerly-started StateFlow swallows both into its own scope.
    // Only the default is shared — that is what used to diverge.
    override fun observeAdsEnabled(): Flow<Boolean> =
        dataStore.ads(default = defaultAdsEnabled)
            .onStart {
                firebaseController.logBreadcrumb(
                    message = "Ads settings observe",
                    attributes = mapOf("defaultAdsEnabled" to defaultAdsEnabled.toString()),
                )
            }

    override suspend fun setAdsEnabled(enabled: Boolean): Result<Unit> {
        firebaseController.logBreadcrumb(
            message = "Ads settings updated",
            attributes = mapOf("enabled" to enabled.toString()),
        )
        dataStore.saveAds(isChecked = enabled)
        return Result.Success(Unit)
    }
}

