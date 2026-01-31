package com.d4rk.android.libs.apptoolkit.app.onboarding.domain.usecases

import com.d4rk.android.libs.apptoolkit.app.onboarding.domain.repository.OnboardingRepository
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onStart

class ObserveOnboardingCompletionUseCase(
    private val repository: OnboardingRepository,
    private val firebaseController: FirebaseController,
) {

    operator fun invoke(): Flow<Boolean> = repository.observeOnboardingCompletion()
        .onStart {
            firebaseController.logBreadcrumb(
                message = "Observe onboarding completion started",
                attributes = mapOf("source" to "ObserveOnboardingCompletionUseCase"),
            )
        }
}
