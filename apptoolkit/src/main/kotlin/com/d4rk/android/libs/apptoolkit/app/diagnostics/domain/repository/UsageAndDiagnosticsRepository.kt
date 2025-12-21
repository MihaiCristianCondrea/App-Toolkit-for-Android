package com.d4rk.android.libs.apptoolkit.app.diagnostics.domain.repository

import com.d4rk.android.libs.apptoolkit.app.diagnostics.domain.model.UsageAndDiagnosticsSettings
import kotlinx.coroutines.flow.Flow

/**
 * Repository exposing usage and diagnostics related settings to the rest of
 * the application. Implementations should delegate to a data source to read
 * and persist the underlying values.
 */
interface UsageAndDiagnosticsRepository {
    /** Emits all usage and diagnostics related consent values. */
    fun observeSettings(): Flow<UsageAndDiagnosticsSettings>

    suspend fun setUsageAndDiagnostics(enabled: Boolean)
    suspend fun setAnalyticsConsent(granted: Boolean)
    suspend fun setAdStorageConsent(granted: Boolean)
    suspend fun setAdUserDataConsent(granted: Boolean)
    suspend fun setAdPersonalizationConsent(granted: Boolean)
}

