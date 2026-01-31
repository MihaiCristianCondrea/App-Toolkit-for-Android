package com.d4rk.android.libs.apptoolkit.app.onboarding.data.repository

import com.d4rk.android.libs.apptoolkit.app.onboarding.data.local.OnboardingPreferencesDataSource
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import com.d4rk.android.libs.apptoolkit.core.utils.dispatchers.UnconfinedDispatcherExtension
import com.google.common.truth.Truth.assertThat
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

private class FakeOnboardingPreferencesDataSource : OnboardingPreferencesDataSource {
    private val state = MutableStateFlow(true)
    override val startup = state
    override suspend fun saveStartup(isFirstTime: Boolean) {
        state.emit(isFirstTime)
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class TestOnboardingRepositoryImpl {

    companion object {
        @JvmField
        @RegisterExtension
        val dispatcherExtension = UnconfinedDispatcherExtension()
    }

    @Test
    fun `observeOnboardingCompletion reflects data source state`() =
        runTest(dispatcherExtension.testDispatcher) {
            val dataSource = FakeOnboardingPreferencesDataSource()
            val repository = OnboardingRepositoryImpl(
                dataStore = dataSource,
                firebaseController = mockk<FirebaseController>(relaxed = true),
            )

            assertThat(repository.observeOnboardingCompletion().first()).isFalse()

            dataSource.saveStartup(false)
            assertThat(repository.observeOnboardingCompletion().first()).isTrue()
        }

    @Test
    fun `setOnboardingCompleted updates data source`() =
        runTest(dispatcherExtension.testDispatcher) {
            val dataSource = FakeOnboardingPreferencesDataSource()
            val repository = OnboardingRepositoryImpl(
                dataStore = dataSource,
                firebaseController = mockk<FirebaseController>(relaxed = true),
            )

            repository.setOnboardingCompleted()
            advanceUntilIdle()

            assertThat(dataSource.startup.first()).isFalse()
            assertThat(repository.observeOnboardingCompletion().first()).isTrue()
        }
}
