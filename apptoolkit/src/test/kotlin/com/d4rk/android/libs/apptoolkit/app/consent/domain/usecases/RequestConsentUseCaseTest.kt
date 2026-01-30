package com.d4rk.android.libs.apptoolkit.app.consent.domain.usecases

import com.d4rk.android.libs.apptoolkit.app.consent.domain.model.ConsentHost
import com.d4rk.android.libs.apptoolkit.app.consent.domain.repository.ConsentRepository
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.DataState
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.Errors
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
        }

        val useCase = RequestConsentUseCase(repository = repository)
        val result = useCase(host = host, showIfRequired = false).toList()
        val expected: List<DataState<Unit, Errors.UseCase>> = listOf(DataState.Success(Unit))

        assertEquals(expected, result)
    }
}
