package com.d4rk.android.libs.apptoolkit.app.diagnostics.domain.model

/**
 * Represents the persisted usage and diagnostics consents.
 * This model belongs to the domain layer and should not contain
 * any UI specific information.
 */
data class UsageAndDiagnosticsSettings(
    val usageAndDiagnostics: Boolean,
    val analyticsConsent: Boolean,
    val adStorageConsent: Boolean,
    val adUserDataConsent: Boolean,
    val adPersonalizationConsent: Boolean,
)

