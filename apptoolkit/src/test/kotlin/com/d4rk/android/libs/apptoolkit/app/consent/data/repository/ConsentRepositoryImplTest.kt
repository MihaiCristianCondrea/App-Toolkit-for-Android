package com.d4rk.android.libs.apptoolkit.app.consent.data.repository

import com.d4rk.android.libs.apptoolkit.app.consent.data.remote.datasource.ConsentRemoteDataSource
import com.d4rk.android.libs.apptoolkit.app.consent.data.remote.model.ConsentRemoteResult
import com.d4rk.android.libs.apptoolkit.app.consent.domain.model.ConsentHost
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.DataState
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.Errors
import com.d4rk.android.libs.apptoolkit.core.utils.dispatchers.UnconfinedDispatcherExtension
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

@OptIn(ExperimentalCoroutinesApi::class)
class ConsentRepositoryImplTest {

    companion object {
        @JvmField
        @RegisterExtension
        val dispatcherExtension = UnconfinedDispatcherExtension()
    }

    @Test
    fun `requestConsent emits success when remote succeeds`() =
        runTest(dispatcherExtension.testDispatcher) {
            val remote = object : ConsentRemoteDataSource {
                override suspend fun requestConsent(
                    host: ConsentHost,
                    showIfRequired: Boolean,
                ): ConsentRemoteResult = ConsentRemoteResult.Success
            }
            val repository = ConsentRepositoryImpl(remote = remote)

            val states = repository.requestConsent(
                host = FakeConsentHost(),
                showIfRequired = true,
            ).toList()

            assertEquals(
                listOf<DataState<Unit, Errors.UseCase>>(
                    DataState.Loading(),
                    DataState.Success(Unit),
                ),
                states
            )
        }

    @Test
    fun `requestConsent emits error when remote fails`() =
        runTest(dispatcherExtension.testDispatcher) {
            val remote = object : ConsentRemoteDataSource {
                override suspend fun requestConsent(
                    host: ConsentHost,
                    showIfRequired: Boolean,
                ): ConsentRemoteResult =
                    ConsentRemoteResult.Failure(Errors.UseCase.FAILED_TO_LOAD_CONSENT_INFO)
            }
            val repository = ConsentRepositoryImpl(remote = remote)

            val states = repository.requestConsent(
                host = FakeConsentHost(),
                showIfRequired = false,
            ).toList()

            assertEquals(
                listOf<DataState<Unit, Errors.UseCase>>(
                    DataState.Loading(),
                    DataState.Error(error = Errors.UseCase.FAILED_TO_LOAD_CONSENT_INFO),
                ),
                states
            )
        }
}

private class FakeConsentHost : ConsentHost {
    override val activity = mockk<android.app.Activity>(relaxed = true)
}
