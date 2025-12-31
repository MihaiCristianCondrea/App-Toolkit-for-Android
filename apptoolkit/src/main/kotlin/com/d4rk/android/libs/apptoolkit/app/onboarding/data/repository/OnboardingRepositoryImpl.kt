package com.d4rk.android.libs.apptoolkit.app.onboarding.data.repository

import com.d4rk.android.libs.apptoolkit.app.onboarding.data.local.OnboardingPreferencesDataSource
import com.d4rk.android.libs.apptoolkit.app.onboarding.domain.repository.OnboardingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class OnboardingRepositoryImpl(
    private val dataStore: OnboardingPreferencesDataSource,
) : OnboardingRepository {

    override fun observeOnboardingCompletion(): Flow<Boolean> =
        dataStore.startup
            .map { isFirstTime -> !isFirstTime }
            .distinctUntilChanged()

    override suspend fun setOnboardingCompleted() {
        dataStore.saveStartup(isFirstTime = false)
    }
}
