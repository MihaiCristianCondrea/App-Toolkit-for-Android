package com.d4rk.android.libs.apptoolkit.app.ads.data.repository

import com.d4rk.android.libs.apptoolkit.app.ads.domain.repository.AdsSettingsRepository
import com.d4rk.android.libs.apptoolkit.app.settings.utils.providers.BuildInfoProvider
import com.d4rk.android.libs.apptoolkit.data.local.datastore.CommonDataStore
import kotlinx.coroutines.flow.Flow

class AdsSettingsRepositoryImpl(
    private val dataStore: CommonDataStore,
    buildInfoProvider: BuildInfoProvider,
) : AdsSettingsRepository {

    override val defaultAdsEnabled: Boolean = !buildInfoProvider.isDebugBuild

    override fun observeAdsEnabled(): Flow<Boolean> =
        dataStore.ads(default = defaultAdsEnabled)

    override suspend fun setAdsEnabled(enabled: Boolean) {
        dataStore.saveAds(isChecked = enabled)
    }
}