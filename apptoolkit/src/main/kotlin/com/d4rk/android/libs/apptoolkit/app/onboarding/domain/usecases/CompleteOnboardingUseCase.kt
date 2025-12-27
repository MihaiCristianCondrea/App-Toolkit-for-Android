package com.d4rk.android.libs.apptoolkit.app.onboarding.domain.usecases

import com.d4rk.android.libs.apptoolkit.app.onboarding.domain.repository.OnboardingRepository

class CompleteOnboardingUseCase(
    private val repository: OnboardingRepository,
) {

    suspend operator fun invoke() {
        repository.setOnboardingCompleted()
    }
}
