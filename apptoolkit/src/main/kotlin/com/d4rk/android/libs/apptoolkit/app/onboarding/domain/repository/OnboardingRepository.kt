package com.d4rk.android.libs.apptoolkit.app.onboarding.domain.repository

import kotlinx.coroutines.flow.Flow

/**
 * Abstraction for onboarding-related data operations.
 */
interface OnboardingRepository {
    fun observeOnboardingCompletion(): Flow<Boolean>
    suspend fun setOnboardingCompleted()
}
