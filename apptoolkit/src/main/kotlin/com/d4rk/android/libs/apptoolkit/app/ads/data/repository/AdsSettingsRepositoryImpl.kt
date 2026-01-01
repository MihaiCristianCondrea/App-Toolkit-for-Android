package com.d4rk.android.libs.apptoolkit.app.ads.data.repository

import com.d4rk.android.libs.apptoolkit.app.ads.domain.repository.AdsSettingsRepository
import com.d4rk.android.libs.apptoolkit.app.settings.utils.providers.BuildInfoProvider
import com.d4rk.android.libs.apptoolkit.core.domain.model.Result
import com.d4rk.android.libs.apptoolkit.data.local.datastore.CommonDataStore
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow

/**
 * Repository implementation of [AdsSettingsRepository] backed by [CommonDataStore].
 */
class AdsSettingsRepositoryImpl(
    private val dataStore: CommonDataStore,
    buildInfoProvider: BuildInfoProvider,
) : AdsSettingsRepository {

    override val defaultAdsEnabled: Boolean = !buildInfoProvider.isDebugBuild

    override fun observeAdsEnabled(): Flow<Boolean> =
        dataStore.ads(default = defaultAdsEnabled)

    override suspend fun setAdsEnabled(enabled: Boolean): Result<Unit> =
        runCatching {
            dataStore.saveAds(isChecked = enabled)
        }.fold(
            onSuccess = { Result.Success(Unit) },
            onFailure = { throwable ->
                if (throwable is CancellationException) throw throwable
                Result.Error(throwable as? Exception ?: Exception(throwable))
            }
        )
}
