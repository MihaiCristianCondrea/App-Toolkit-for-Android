package com.d4rk.android.libs.apptoolkit.app.onboarding.domain.usecases

import com.d4rk.android.libs.apptoolkit.app.onboarding.domain.repository.OnboardingRepository
import kotlinx.coroutines.flow.Flow

class ObserveOnboardingCompletionUseCase(
    private val repository: OnboardingRepository,
) {

    operator fun invoke(): Flow<Boolean> = repository.observeOnboardingCompletion()
}
