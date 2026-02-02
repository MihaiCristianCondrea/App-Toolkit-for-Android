package com.d4rk.android.libs.apptoolkit.app.consent.domain.usecases

import com.d4rk.android.libs.apptoolkit.app.consent.domain.repository.ConsentRepository
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController

/**
 * Applies persisted consent values at startup and logs a breadcrumb for diagnostics.
 */
class ApplyInitialConsentUseCase(
    private val repository: ConsentRepository,
    private val firebaseController: FirebaseController,
) {

    suspend operator fun invoke() {
        firebaseController.logBreadcrumb(message = "Applying initial consent")
        repository.applyInitialConsent()
    }
}
