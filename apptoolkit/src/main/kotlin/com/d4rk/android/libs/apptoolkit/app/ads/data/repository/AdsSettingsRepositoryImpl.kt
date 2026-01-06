package com.d4rk.android.libs.apptoolkit.app.ads.data.repository

import com.d4rk.android.libs.apptoolkit.app.ads.domain.repository.AdsSettingsRepository
import com.d4rk.android.libs.apptoolkit.app.settings.utils.providers.BuildInfoProvider
import com.d4rk.android.libs.apptoolkit.data.local.datastore.CommonDataStore
import com.d4rk.android.libs.apptoolkit.core.domain.model.Result
import kotlinx.coroutines.flow.Flow

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
) : AdsSettingsRepository {

    override val defaultAdsEnabled: Boolean = !buildInfoProvider.isDebugBuild

    override fun observeAdsEnabled(): Flow<Boolean> =
        dataStore.ads(default = defaultAdsEnabled)

    override suspend fun setAdsEnabled(enabled: Boolean): Result<Unit> {
        dataStore.saveAds(isChecked = enabled)
        return Result.Success(Unit)
    }
}
