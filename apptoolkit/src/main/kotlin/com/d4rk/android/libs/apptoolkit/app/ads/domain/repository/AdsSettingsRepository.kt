package com.d4rk.android.libs.apptoolkit.app.ads.domain.repository

import kotlinx.coroutines.flow.Flow

interface AdsSettingsRepository {
    val defaultAdsEnabled: Boolean
    fun observeAdsEnabled(): Flow<Boolean>
    suspend fun setAdsEnabled(enabled: Boolean)
}
