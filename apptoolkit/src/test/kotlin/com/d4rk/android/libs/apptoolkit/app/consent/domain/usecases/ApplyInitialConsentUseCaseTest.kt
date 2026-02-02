package com.d4rk.android.libs.apptoolkit.app.consent.domain.usecases

import com.d4rk.android.libs.apptoolkit.app.consent.domain.repository.ConsentRepository
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import io.mockk.mockk
import io.mockk.coVerify
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class ApplyInitialConsentUseCaseTest {

    @Test
    fun `invoke logs breadcrumb and delegates to repository`() = runTest {
        val repository = mockk<ConsentRepository>(relaxed = true)
        val firebaseController = mockk<FirebaseController>(relaxed = true)
        val useCase = ApplyInitialConsentUseCase(
            repository = repository,
            firebaseController = firebaseController,
        )

        useCase()

        verify {
            firebaseController.logBreadcrumb(message = "Applying initial consent")
        }
        coVerify { repository.applyInitialConsent() }
    }
}
