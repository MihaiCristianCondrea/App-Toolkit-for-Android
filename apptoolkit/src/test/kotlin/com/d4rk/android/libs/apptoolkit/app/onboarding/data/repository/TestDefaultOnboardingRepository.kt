/*
 * Copyright (©) 2026 Mihai-Cristian Condrea
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package com.d4rk.android.libs.apptoolkit.app.onboarding.data.repository

import com.d4rk.android.libs.apptoolkit.app.onboarding.data.local.OnboardingPreferencesDataSource
import com.d4rk.android.libs.apptoolkit.core.utils.dispatchers.UnconfinedDispatcherExtension
import com.google.common.truth.Truth.assertThat
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
            val repository = OnboardingRepositoryImpl(dataStore = dataSource)

            assertThat(repository.observeOnboardingCompletion().first()).isFalse()

            dataSource.saveStartup(false)
            assertThat(repository.observeOnboardingCompletion().first()).isTrue()
        }

    @Test
    fun `setOnboardingCompleted updates data source`() =
        runTest(dispatcherExtension.testDispatcher) {
            val dataSource = FakeOnboardingPreferencesDataSource()
            val repository = OnboardingRepositoryImpl(dataStore = dataSource)

            repository.setOnboardingCompleted()
            advanceUntilIdle()

            assertThat(dataSource.startup.first()).isFalse()
            assertThat(repository.observeOnboardingCompletion().first()).isTrue()
        }
}
