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

package com.mihaicristiancondrea.android.libs.apptoolkit.app.consent.data.repository

import com.mihaicristiancondrea.android.libs.apptoolkit.app.consent.data.remote.datasource.ConsentRemoteDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.app.consent.domain.model.ConsentHost
import com.mihaicristiancondrea.android.libs.apptoolkit.app.consent.domain.model.ConsentSettings
import com.mihaicristiancondrea.android.libs.apptoolkit.core.data.local.datastore.interfaces.ConsentPreferencesDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.domain.model.network.DataState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.domain.model.network.Errors
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.repository.FirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.UnconfinedDispatcherExtension
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.providers.BuildInfoProvider
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runCurrent
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
                override fun requestConsent(
                    host: ConsentHost,
                    showIfRequired: Boolean,
                ): Flow<DataState<Unit, Errors.UseCase>> =
                    flowOf(
                        DataState.Loading(),
                        DataState.Success(Unit),
                    )
            }
            val repository = ConsentRepositoryImpl(
                remote = remote,
                local = FakeConsentPreferencesDataSource(),
                configProvider = FakeBuildInfoProvider(isDebugBuild = false),
                firebaseController = mockk(relaxed = true),
            )

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
                override fun requestConsent(
                    host: ConsentHost,
                    showIfRequired: Boolean,
                ): Flow<DataState<Unit, Errors.UseCase>> =
                    flowOf(
                        DataState.Loading(),
                        DataState.Error(error = Errors.UseCase.FAILED_TO_LOAD_CONSENT_INFO),
                    )
            }
            val repository = ConsentRepositoryImpl(
                remote = remote,
                local = FakeConsentPreferencesDataSource(),
                configProvider = FakeBuildInfoProvider(isDebugBuild = false),
                firebaseController = mockk(relaxed = true),
            )

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

    @Test
    fun `applyInitialConsent reads persisted values and updates Firebase`() =
        runTest(dispatcherExtension.testDispatcher) {
            val firebaseController = mockk<FirebaseController>(relaxed = true)
            val repository = ConsentRepositoryImpl(
                remote = mockk(relaxed = true),
                local = FakeConsentPreferencesDataSource(
                    usageAndDiagnostics = true,
                    analyticsConsent = false,
                    adStorageConsent = true,
                    adUserDataConsent = false,
                    adPersonalizationConsent = true,
                ),
                configProvider = FakeBuildInfoProvider(isDebugBuild = false),
                firebaseController = firebaseController,
            )

            repository.applyInitialConsent()

            verify {
                firebaseController.updateConsent(
                    analyticsGranted = false,
                    adStorageGranted = true,
                    adUserDataGranted = false,
                    adPersonalizationGranted = true,
                )
                firebaseController.setAnalyticsEnabled(true)
                firebaseController.setCrashlyticsEnabled(true)
                firebaseController.setPerformanceEnabled(true)
            }
        }

    @Test
    fun `applyConsentSettings updates Firebase with provided settings`() =
        runTest(dispatcherExtension.testDispatcher) {
            val firebaseController = mockk<FirebaseController>(relaxed = true)
            val repository = ConsentRepositoryImpl(
                remote = mockk(relaxed = true),
                local = FakeConsentPreferencesDataSource(),
                configProvider = FakeBuildInfoProvider(isDebugBuild = true),
                firebaseController = firebaseController,
            )
            val settings = ConsentSettings(
                usageAndDiagnostics = false,
                analyticsConsent = false,
                adStorageConsent = true,
                adUserDataConsent = true,
                adPersonalizationConsent = false,
            )

            repository.applyConsentSettings(settings)

            verify {
                firebaseController.updateConsent(
                    analyticsGranted = false,
                    adStorageGranted = true,
                    adUserDataGranted = true,
                    adPersonalizationGranted = false,
                )
                firebaseController.setAnalyticsEnabled(false)
                firebaseController.setCrashlyticsEnabled(false)
                firebaseController.setPerformanceEnabled(false)
            }
        }

    @Test
    fun `applyInitialConsent falls back to defaults for debug builds`() =
        runTest(dispatcherExtension.testDispatcher) {
            val firebaseController = mockk<FirebaseController>(relaxed = true)
            val repository = ConsentRepositoryImpl(
                remote = mockk(relaxed = true),
                local = FakeConsentPreferencesDataSource(),
                configProvider = FakeBuildInfoProvider(isDebugBuild = true),
                firebaseController = firebaseController,
            )

            repository.applyInitialConsent()

            verify {
                firebaseController.updateConsent(
                    analyticsGranted = false,
                    adStorageGranted = false,
                    adUserDataGranted = false,
                    adPersonalizationGranted = false,
                )
                firebaseController.setAnalyticsEnabled(false)
                firebaseController.setCrashlyticsEnabled(false)
                firebaseController.setPerformanceEnabled(false)
            }
        }

    @Test
    fun `concurrent requests share a single UMP round trip`() =
        runTest(dispatcherExtension.testDispatcher) {
            val remote = CountingConsentRemoteDataSource()
            val repository = ConsentRepositoryImpl(
                remote = remote,
                local = FakeConsentPreferencesDataSource(),
                configProvider = FakeBuildInfoProvider(isDebugBuild = false),
                firebaseController = mockk(relaxed = true),
                requestScope = backgroundScope,
            )

            val first = async {
                repository.requestConsent(host = FakeConsentHost(), showIfRequired = true).toList()
            }
            val second = async {
                repository.requestConsent(host = FakeConsentHost(), showIfRequired = true).toList()
            }
            runCurrent()
            remote.complete(DataState.Success(Unit))

            val expected = listOf<DataState<Unit, Errors.UseCase>>(
                DataState.Loading(),
                DataState.Success(Unit),
            )
            assertEquals(expected, first.await())
            assertEquals(expected, second.await())
            assertEquals(1, remote.requestCount)
        }

    @Test
    fun `a request that forces the form does not attach to an implicit one`() =
        runTest(dispatcherExtension.testDispatcher) {
            val remote = CountingConsentRemoteDataSource()
            val repository = ConsentRepositoryImpl(
                remote = remote,
                local = FakeConsentPreferencesDataSource(),
                configProvider = FakeBuildInfoProvider(isDebugBuild = false),
                firebaseController = mockk(relaxed = true),
                requestScope = backgroundScope,
            )

            val implicit = async {
                repository.requestConsent(host = FakeConsentHost(), showIfRequired = true).toList()
            }
            val explicit = async {
                repository.requestConsent(host = FakeConsentHost(), showIfRequired = false).toList()
            }
            runCurrent()
            remote.complete(DataState.Success(Unit))

            implicit.await()
            explicit.await()
            assertEquals(2, remote.requestCount)
        }

    @Test
    fun `a later request starts a fresh round trip`() =
        runTest(dispatcherExtension.testDispatcher) {
            val remote = CountingConsentRemoteDataSource()
            val repository = ConsentRepositoryImpl(
                remote = remote,
                local = FakeConsentPreferencesDataSource(),
                configProvider = FakeBuildInfoProvider(isDebugBuild = false),
                firebaseController = mockk(relaxed = true),
                requestScope = backgroundScope,
            )

            val first = async {
                repository.requestConsent(host = FakeConsentHost(), showIfRequired = true).toList()
            }
            runCurrent()
            remote.complete(DataState.Success(Unit))
            first.await()

            val second = async {
                repository.requestConsent(host = FakeConsentHost(), showIfRequired = true).toList()
            }
            runCurrent()
            remote.complete(DataState.Success(Unit))
            second.await()

            assertEquals(2, remote.requestCount)
        }

    @Test
    fun `requests from a finishing host never reach UMP`() =
        runTest(dispatcherExtension.testDispatcher) {
            val remote = CountingConsentRemoteDataSource()
            val repository = ConsentRepositoryImpl(
                remote = remote,
                local = FakeConsentPreferencesDataSource(),
                configProvider = FakeBuildInfoProvider(isDebugBuild = false),
                firebaseController = mockk(relaxed = true),
                requestScope = backgroundScope,
            )

            val states = repository.requestConsent(
                host = FakeConsentHost(isFinishing = true),
                showIfRequired = true,
            ).toList()

            assertEquals(
                listOf<DataState<Unit, Errors.UseCase>>(
                    DataState.Loading(),
                    DataState.Error(error = Errors.UseCase.FAILED_TO_LOAD_CONSENT_INFO),
                ),
                states,
            )
            assertEquals(0, remote.requestCount)
        }
    /**
     * The breadcrumbs used to be contributed by pass-through use cases. They are part of the
     * repository's observable behaviour now, and operations depends on them.
     */
    @Test
    fun `applyInitialConsent logs a breadcrumb`() =
        runTest(dispatcherExtension.testDispatcher) {
            val firebaseController = mockk<FirebaseController>(relaxed = true)
            val repository = ConsentRepositoryImpl(
                remote = mockk(relaxed = true),
                local = FakeConsentPreferencesDataSource(),
                configProvider = FakeBuildInfoProvider(isDebugBuild = false),
                firebaseController = firebaseController,
            )

            repository.applyInitialConsent()

            verify { firebaseController.logBreadcrumb(message = "Applying initial consent") }
        }

    @Test
    fun `applyConsentSettings logs a breadcrumb with the applied values`() =
        runTest(dispatcherExtension.testDispatcher) {
            val firebaseController = mockk<FirebaseController>(relaxed = true)
            val repository = ConsentRepositoryImpl(
                remote = mockk(relaxed = true),
                local = FakeConsentPreferencesDataSource(),
                configProvider = FakeBuildInfoProvider(isDebugBuild = false),
                firebaseController = firebaseController,
            )

            repository.applyConsentSettings(
                ConsentSettings(
                    usageAndDiagnostics = true,
                    analyticsConsent = true,
                    adStorageConsent = false,
                    adUserDataConsent = false,
                    adPersonalizationConsent = true,
                )
            )

            verify {
                firebaseController.logBreadcrumb(
                    message = "Consent settings applied",
                    attributes = mapOf(
                        "usageAndDiagnostics" to "true",
                        "analyticsConsent" to "true",
                        "adStorageConsent" to "false",
                        "adUserDataConsent" to "false",
                        "adPersonalizationConsent" to "true",
                    ),
                )
            }
        }
}

/**
 * A remote source whose round trip stays open until [complete] is called, so tests can observe how
 * many UMP requests the repository actually starts while one is in flight.
 */
private class CountingConsentRemoteDataSource : ConsentRemoteDataSource {
    private val results = MutableSharedFlow<DataState<Unit, Errors.UseCase>>(replay = 1)

    var requestCount: Int = 0
        private set

    override fun requestConsent(
        host: ConsentHost,
        showIfRequired: Boolean,
    ): Flow<DataState<Unit, Errors.UseCase>> = flow {
        requestCount++
        emit(DataState.Loading())
        emit(results.first())
    }

    suspend fun complete(state: DataState<Unit, Errors.UseCase>) {
        results.emit(state)
    }
}

private class FakeConsentHost(isFinishing: Boolean = false) : ConsentHost {
    override val activity = mockk<android.app.Activity>(relaxed = true).also {
        every { it.isFinishing } returns isFinishing
        every { it.isDestroyed } returns false
    }
}

private class FakeConsentPreferencesDataSource(
    private val usageAndDiagnostics: Boolean? = null,
    private val analyticsConsent: Boolean? = null,
    private val adStorageConsent: Boolean? = null,
    private val adUserDataConsent: Boolean? = null,
    private val adPersonalizationConsent: Boolean? = null,
) : ConsentPreferencesDataSource {
    override fun usageAndDiagnostics(default: Boolean) =
        flowOf(usageAndDiagnostics ?: default)

    override fun analyticsConsent(default: Boolean) =
        flowOf(analyticsConsent ?: default)

    override fun adStorageConsent(default: Boolean) =
        flowOf(adStorageConsent ?: default)

    override fun adUserDataConsent(default: Boolean) =
        flowOf(adUserDataConsent ?: default)

    override fun adPersonalizationConsent(default: Boolean) =
        flowOf(adPersonalizationConsent ?: default)
}

private class FakeBuildInfoProvider(
    override val isDebugBuild: Boolean,
) : BuildInfoProvider {
    override val appVersion: String = "1.0.0-test"
    override val appVersionCode: Int = 1
    override val packageName: String = "com.mihaicristiancondrea.android.libs.apptoolkit.test"
}
