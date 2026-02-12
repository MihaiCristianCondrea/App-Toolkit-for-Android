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

package com.d4rk.android.libs.apptoolkit.app.ads.data.repository

import com.d4rk.android.libs.apptoolkit.app.ads.domain.repository.AdsSettingsRepository
import com.d4rk.android.libs.apptoolkit.app.settings.utils.providers.BuildInfoProvider
import com.d4rk.android.libs.apptoolkit.core.data.local.datastore.CommonDataStore
import com.d4rk.android.libs.apptoolkit.core.domain.model.Result
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
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
    buildInfoProvider: BuildInfoProvider,
    private val firebaseController: FirebaseController,
) : AdsSettingsRepository {

    override val defaultAdsEnabled: Boolean = !buildInfoProvider.isDebugBuild

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
