package com.d4rk.android.libs.apptoolkit.app.onboarding.data.repository

import com.d4rk.android.libs.apptoolkit.app.onboarding.data.local.OnboardingPreferencesDataSource
import com.d4rk.android.libs.apptoolkit.app.onboarding.domain.repository.OnboardingRepository
import com.d4rk.android.libs.apptoolkit.core.di.DispatcherProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class OnboardingRepositoryImpl(
    private val dataStore: OnboardingPreferencesDataSource,
    private val dispatchers: DispatcherProvider,
) : OnboardingRepository {

    override fun observeOnboardingCompletion(): Flow<Boolean> =
        dataStore.startup
            .map { isFirstTime -> !isFirstTime }
            .flowOn(dispatchers.io)

    override suspend fun setOnboardingCompleted() = withContext(dispatchers.io) {
        dataStore.saveStartup(isFirstTime = false)
    }
}
