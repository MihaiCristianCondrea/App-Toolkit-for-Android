package com.d4rk.android.libs.apptoolkit.app.onboarding.data.repository

import com.d4rk.android.libs.apptoolkit.app.onboarding.data.local.OnboardingPreferencesDataSource
import com.d4rk.android.libs.apptoolkit.app.onboarding.domain.repository.OnboardingRepository
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class OnboardingRepositoryImpl(
    private val dataStore: OnboardingPreferencesDataSource,
    private val firebaseController: FirebaseController,
) : OnboardingRepository {

    override fun observeOnboardingCompletion(): Flow<Boolean> =
        dataStore.startup
            .map { isFirstTime -> !isFirstTime }
            .distinctUntilChanged()
            .onStart {
                firebaseController.logBreadcrumb(
                    message = "Onboarding completion observe",
                    attributes = mapOf("source" to "OnboardingRepositoryImpl"),
                )
            }

    override suspend fun setOnboardingCompleted() {
        firebaseController.logBreadcrumb(
            message = "Onboarding completion updated",
            attributes = mapOf("completed" to "true"),
        )
        dataStore.saveStartup(isFirstTime = false)
    }
}
