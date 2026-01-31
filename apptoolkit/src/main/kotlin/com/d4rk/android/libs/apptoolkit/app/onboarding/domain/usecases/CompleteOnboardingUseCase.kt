package com.d4rk.android.libs.apptoolkit.app.onboarding.domain.usecases

import com.d4rk.android.libs.apptoolkit.app.onboarding.domain.repository.OnboardingRepository
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController

class CompleteOnboardingUseCase(
    private val repository: OnboardingRepository,
    private val firebaseController: FirebaseController,
) {

    suspend operator fun invoke() {
        firebaseController.logBreadcrumb(
            message = "Complete onboarding",
            attributes = mapOf("source" to "CompleteOnboardingUseCase"),
        )
        repository.setOnboardingCompleted()
    }
}
