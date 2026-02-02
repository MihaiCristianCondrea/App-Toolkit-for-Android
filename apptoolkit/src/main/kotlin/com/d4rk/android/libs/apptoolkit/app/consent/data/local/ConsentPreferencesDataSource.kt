package com.d4rk.android.libs.apptoolkit.app.consent.data.local

import kotlinx.coroutines.flow.Flow

/**
 * Abstraction over persisted consent preferences used by the consent feature.
 */
interface ConsentPreferencesDataSource {

    /** Emits whether usage and diagnostics collection is enabled. */
    fun usageAndDiagnostics(default: Boolean): Flow<Boolean>

    /** Emits the current analytics consent state. */
    fun analyticsConsent(default: Boolean): Flow<Boolean>

    /** Emits the current ad storage consent state. */
    fun adStorageConsent(default: Boolean): Flow<Boolean>

    /** Emits the current ad user data consent state. */
    fun adUserDataConsent(default: Boolean): Flow<Boolean>

    /** Emits the current ad personalization consent state. */
    fun adPersonalizationConsent(default: Boolean): Flow<Boolean>
}
