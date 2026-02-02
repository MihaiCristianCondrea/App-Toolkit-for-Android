package com.d4rk.android.libs.apptoolkit.app.consent.domain.usecases

import com.d4rk.android.libs.apptoolkit.app.consent.domain.model.ConsentSettings
import com.d4rk.android.libs.apptoolkit.app.consent.domain.repository.ConsentRepository
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class ApplyConsentSettingsUseCaseTest {

    @Test
    fun `invoke logs breadcrumb and delegates to repository`() = runTest {
        val repository = mockk<ConsentRepository>(relaxed = true)
        val firebaseController = mockk<FirebaseController>(relaxed = true)
        val useCase = ApplyConsentSettingsUseCase(
            repository = repository,
            firebaseController = firebaseController,
        )
        val settings = ConsentSettings(
            usageAndDiagnostics = true,
            analyticsConsent = false,
            adStorageConsent = true,
            adUserDataConsent = false,
            adPersonalizationConsent = true,
        )

        useCase(settings)

        verify {
            firebaseController.logBreadcrumb(
                message = "Consent settings applied",
                attributes = mapOf(
                    "usageAndDiagnostics" to "true",
                    "analyticsConsent" to "false",
                    "adStorageConsent" to "true",
                    "adUserDataConsent" to "false",
                    "adPersonalizationConsent" to "true",
                ),
            )
            repository.applyConsentSettings(settings)
        }
    }
}
