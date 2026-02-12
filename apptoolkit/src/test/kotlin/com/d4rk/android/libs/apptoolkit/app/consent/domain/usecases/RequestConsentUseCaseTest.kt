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

package com.d4rk.android.libs.apptoolkit.app.consent.domain.usecases

import com.d4rk.android.libs.apptoolkit.app.consent.domain.model.ConsentHost
import com.d4rk.android.libs.apptoolkit.app.consent.domain.model.ConsentSettings
import com.d4rk.android.libs.apptoolkit.app.consent.domain.repository.ConsentRepository
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.DataState
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.Errors
import com.d4rk.android.libs.apptoolkit.core.utils.FakeFirebaseController
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RequestConsentUseCaseTest {

    @Test
    fun `invoke delegates to repository`() = runTest {
        val host = object : ConsentHost {
            override val activity = mockk<android.app.Activity>(relaxed = true)
        }
        val repository = object : ConsentRepository {
            override fun requestConsent(
                host: ConsentHost,
                showIfRequired: Boolean,
            ): Flow<DataState<Unit, Errors.UseCase>> =
                flowOf(DataState.Success(Unit))

            override suspend fun applyInitialConsent() = Unit

            override suspend fun applyConsentSettings(settings: ConsentSettings) = Unit
        }

        val useCase = RequestConsentUseCase(
            repository = repository,
            firebaseController = FakeFirebaseController(),
        )
        val result = useCase(host = host, showIfRequired = false).toList()
        val expected: List<DataState<Unit, Errors.UseCase>> = listOf(DataState.Success(Unit))

        assertEquals(expected, result)
    }
}
