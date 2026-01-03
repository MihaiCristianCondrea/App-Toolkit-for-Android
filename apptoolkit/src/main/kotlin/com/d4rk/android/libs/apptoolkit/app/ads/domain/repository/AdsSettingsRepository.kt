package com.d4rk.android.libs.apptoolkit.app.ads.domain.repository

import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for managing advertisement settings.
 *
 * This interface defines the contract for accessing and modifying
 * user preferences related to advertisements within the application.
 */
interface AdsSettingsRepository {
    val defaultAdsEnabled: Boolean
    fun observeAdsEnabled(): Flow<Boolean>
    suspend fun setAdsEnabled(enabled: Boolean)
}
