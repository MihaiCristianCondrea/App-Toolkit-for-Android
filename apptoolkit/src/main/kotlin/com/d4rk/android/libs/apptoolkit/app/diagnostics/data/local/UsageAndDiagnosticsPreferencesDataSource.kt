package com.d4rk.android.libs.apptoolkit.app.diagnostics.data.local

import kotlinx.coroutines.flow.Flow

/**
 * Abstraction over storage operations for usage and diagnostics related
 * preferences. Implementations are responsible for persisting and
 * exposing the different consent values used by the diagnostics
 * feature.
 */
interface UsageAndDiagnosticsPreferencesDataSource {

    /** Emits whether usage and diagnostics collection is enabled. */
    fun usageAndDiagnostics(default: Boolean): Flow<Boolean>

    /** Persists whether usage and diagnostics collection is enabled. */
    suspend fun saveUsageAndDiagnostics(isChecked: Boolean)

    /** Emits the current analytics consent state. */
    fun analyticsConsent(default: Boolean): Flow<Boolean>

    /** Persists analytics consent. */
    suspend fun saveAnalyticsConsent(isGranted: Boolean)

    /** Emits the current ad storage consent state. */
    fun adStorageConsent(default: Boolean): Flow<Boolean>

    /** Persists ad storage consent. */
    suspend fun saveAdStorageConsent(isGranted: Boolean)

    /** Emits the current ad user data consent state. */
    fun adUserDataConsent(default: Boolean): Flow<Boolean>

    /** Persists ad user data consent. */
    suspend fun saveAdUserDataConsent(isGranted: Boolean)

    /** Emits the current ad personalization consent state. */
    fun adPersonalizationConsent(default: Boolean): Flow<Boolean>

    /** Persists ad personalization consent. */
    suspend fun saveAdPersonalizationConsent(isGranted: Boolean)
}
