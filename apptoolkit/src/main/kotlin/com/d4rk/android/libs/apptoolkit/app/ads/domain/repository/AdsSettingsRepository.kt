package com.d4rk.android.libs.apptoolkit.app.ads.domain.repository

import com.d4rk.android.libs.apptoolkit.core.domain.model.Result
import kotlinx.coroutines.flow.Flow

/**
 * Repository that exposes and persists the ads display preference.
 */
interface AdsSettingsRepository {
    /** Default value used when the preference has not been set. */
    val defaultAdsEnabled: Boolean

    /** Observe whether ads are enabled. */
    fun observeAdsEnabled(): Flow<Boolean>

    /** Persist the ads enabled preference. */
    suspend fun setAdsEnabled(enabled: Boolean): Result<Unit>
}
