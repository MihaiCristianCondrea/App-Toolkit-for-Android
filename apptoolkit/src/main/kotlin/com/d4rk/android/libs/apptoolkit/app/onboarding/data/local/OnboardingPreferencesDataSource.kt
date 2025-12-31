package com.d4rk.android.libs.apptoolkit.app.onboarding.data.local

import kotlinx.coroutines.flow.Flow

/**
 * Abstraction over onboarding-related preference operations used by the repository.
 */
interface OnboardingPreferencesDataSource {
    /** Emits whether the app is being launched for the first time. */
    val startup: Flow<Boolean>

    /** Persists whether the app is being launched for the first time. */
    suspend fun saveStartup(isFirstTime: Boolean)
}
