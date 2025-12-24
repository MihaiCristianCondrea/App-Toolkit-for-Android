package com.d4rk.android.libs.apptoolkit.app.ads.data.repository

import com.d4rk.android.libs.apptoolkit.app.ads.domain.repository.AdsSettingsRepository
import com.d4rk.android.libs.apptoolkit.app.settings.utils.providers.BuildInfoProvider
import com.d4rk.android.libs.apptoolkit.core.di.DispatcherProvider
import com.d4rk.android.libs.apptoolkit.core.domain.model.Result
import com.d4rk.android.libs.apptoolkit.data.datastore.CommonDataStore
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

/**
 * Default implementation of [AdsSettingsRepository] backed by [CommonDataStore].
 */
class DefaultAdsSettingsRepository(
    private val dataStore: CommonDataStore,
    buildInfoProvider: BuildInfoProvider,
    private val dispatchers: DispatcherProvider,
) : AdsSettingsRepository {

    override val defaultAdsEnabled: Boolean = !buildInfoProvider.isDebugBuild

    override fun observeAdsEnabled(): Flow<Boolean> =
        dataStore.ads(default = defaultAdsEnabled)
            .catch { throwable ->
                if (throwable is CancellationException) throw throwable
                emit(defaultAdsEnabled)
            }
            .flowOn(dispatchers.io)

    override suspend fun setAdsEnabled(enabled: Boolean): Result<Unit> =
        runCatching {
            withContext(dispatchers.io) {
                dataStore.saveAds(isChecked = enabled)
            }
        }.fold(
            onSuccess = { Result.Success(Unit) },
            onFailure = {
                if (it is Exception) Result.Error(it) else throw it
            }
        )
}
