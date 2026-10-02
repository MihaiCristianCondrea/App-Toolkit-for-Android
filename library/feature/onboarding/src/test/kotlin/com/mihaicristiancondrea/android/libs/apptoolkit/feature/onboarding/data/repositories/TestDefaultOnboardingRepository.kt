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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.data.repositories

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.exceptions.StorageException
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.interfaces.OnboardingPreferencesDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.UnconfinedDispatcherExtension
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import java.io.IOException
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

private class FakeOnboardingPreferencesDataSource : OnboardingPreferencesDataSource {
    private val state = MutableStateFlow(true)
    override val startup = state
    override suspend fun saveStartup(isFirstTime: Boolean) {
        state.emit(isFirstTime)
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class TestDefaultOnboardingRepository {

    companion object {
        @JvmField
        @RegisterExtension
        val dispatcherExtension = UnconfinedDispatcherExtension()
    }

    @Test
    fun `observeOnboardingCompletion reflects data source state`() =
        runTest(dispatcherExtension.testDispatcher) {
            val dataSource = FakeOnboardingPreferencesDataSource()
            val repository = DefaultOnboardingRepository(dataStore = dataSource)

            assertFalse(repository.observeOnboardingCompletion().first())

            dataSource.saveStartup(false)
            assertTrue(repository.observeOnboardingCompletion().first())
        }

    @Test
    fun `setOnboardingCompleted updates data source`() =
        runTest(dispatcherExtension.testDispatcher) {
            val dataSource = FakeOnboardingPreferencesDataSource()
            val repository = DefaultOnboardingRepository(dataStore = dataSource)

            repository.setOnboardingCompleted()
            advanceUntilIdle()

            assertFalse(dataSource.startup.first())
            assertTrue(repository.observeOnboardingCompletion().first())
        }

    @Test
    fun `a failed completion write throws a storage failure`() =
        runTest(dispatcherExtension.testDispatcher) {
            val repository = DefaultOnboardingRepository(dataStore = FailingOnboardingPreferencesDataSource())

            val failure = assertFailsWith<StorageException> { repository.setOnboardingCompleted() }

            assertEquals(StorageException.Reason.FAILED, failure.reason)
        }

    private class FailingOnboardingPreferencesDataSource : OnboardingPreferencesDataSource {
        override val startup = MutableStateFlow(true)

        override suspend fun saveStartup(isFirstTime: Boolean) {
            throw IOException("disk")
        }
    }
}

