package com.d4rk.android.libs.apptoolkit.app.diagnostics.ui.state

/**
 * Represents the state for [com.d4rk.android.libs.apptoolkit.app.diagnostics.ui.UsageAndDiagnosticsViewModel].
 *
 * @param usageAndDiagnostics whether usage and diagnostics collection is enabled
 * @param analyticsConsent user consent for analytics
 * @param adStorageConsent user consent for ad storage
 * @param adUserDataConsent user consent for ad user data
 * @param adPersonalizationConsent user consent for ad personalization
 */
data class UsageAndDiagnosticsUiState(
    val usageAndDiagnostics: Boolean = false,
    val analyticsConsent: Boolean = false,
    val adStorageConsent: Boolean = false,
    val adUserDataConsent: Boolean = false,
    val adPersonalizationConsent: Boolean = false,
)