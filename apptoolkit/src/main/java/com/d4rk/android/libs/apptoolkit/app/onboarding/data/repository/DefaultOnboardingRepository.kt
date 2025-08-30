package com.d4rk.android.libs.apptoolkit.app.onboarding.data.repository

import com.d4rk.android.libs.apptoolkit.app.onboarding.data.datasource.OnboardingPreferencesDataSource
import com.d4rk.android.libs.apptoolkit.app.onboarding.domain.repository.OnboardingRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

/**
 * Default implementation of [OnboardingRepository] backed by an [OnboardingPreferencesDataSource].
 */
class DefaultOnboardingRepository(
    private val dataStore: OnboardingPreferencesDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : OnboardingRepository {

    override fun observeOnboardingCompletion(): Flow<Boolean> =
        dataStore.startup
            .map { isFirstTime -> !isFirstTime }
            .flowOn(ioDispatcher)

    override suspend fun setOnboardingCompleted() = withContext(ioDispatcher) {
        dataStore.saveStartup(isFirstTime = false)
    }
}
