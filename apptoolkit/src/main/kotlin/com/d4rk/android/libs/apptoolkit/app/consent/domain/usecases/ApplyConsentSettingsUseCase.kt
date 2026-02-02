package com.d4rk.android.libs.apptoolkit.app.consent.domain.usecases

import com.d4rk.android.libs.apptoolkit.app.consent.domain.model.ConsentSettings
import com.d4rk.android.libs.apptoolkit.app.consent.domain.repository.ConsentRepository
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController

/**
 * Applies consent settings to Firebase SDKs and records a breadcrumb for observability.
 */
class ApplyConsentSettingsUseCase(
    private val repository: ConsentRepository,
    private val firebaseController: FirebaseController,
) {

    suspend operator fun invoke(settings: ConsentSettings) {
        firebaseController.logBreadcrumb(
            message = "Consent settings applied",
            attributes = mapOf(
                "usageAndDiagnostics" to settings.usageAndDiagnostics.toString(),
                "analyticsConsent" to settings.analyticsConsent.toString(),
                "adStorageConsent" to settings.adStorageConsent.toString(),
                "adUserDataConsent" to settings.adUserDataConsent.toString(),
                "adPersonalizationConsent" to settings.adPersonalizationConsent.toString(),
            ),
        )
        repository.applyConsentSettings(settings)
    }
}
