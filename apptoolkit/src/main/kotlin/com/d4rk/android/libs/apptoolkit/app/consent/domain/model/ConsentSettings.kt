package com.d4rk.android.libs.apptoolkit.app.consent.domain.model

/**
 * Aggregated consent flags used to configure analytics and ad-related collection.
 *
 * @property usageAndDiagnostics Whether analytics/crash/performance collection is enabled.
 * @property analyticsConsent Consent for analytics storage in Firebase.
 * @property adStorageConsent Consent for ad storage in Firebase.
 * @property adUserDataConsent Consent for ad user data usage in Firebase.
 * @property adPersonalizationConsent Consent for ad personalization in Firebase.
 */
data class ConsentSettings(
    val usageAndDiagnostics: Boolean,
    val analyticsConsent: Boolean,
    val adStorageConsent: Boolean,
    val adUserDataConsent: Boolean,
    val adPersonalizationConsent: Boolean,
)
