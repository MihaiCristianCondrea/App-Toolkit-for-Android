package com.d4rk.android.libs.apptoolkit.app.consent.data.repository

import com.d4rk.android.libs.apptoolkit.app.consent.data.local.ConsentPreferencesDataSource
import com.d4rk.android.libs.apptoolkit.app.consent.data.remote.datasource.ConsentRemoteDataSource
import com.d4rk.android.libs.apptoolkit.app.consent.domain.model.ConsentHost
import com.d4rk.android.libs.apptoolkit.app.consent.domain.model.ConsentSettings
import com.d4rk.android.libs.apptoolkit.app.settings.utils.providers.BuildInfoProvider
import com.d4rk.android.libs.apptoolkit.core.di.TestDispatchers
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.DataState
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.Errors
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import com.d4rk.android.libs.apptoolkit.core.utils.dispatchers.UnconfinedDispatcherExtension
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
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
                dispatchers = TestDispatchers(dispatcherExtension.testDispatcher),
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
                dispatchers = TestDispatchers(dispatcherExtension.testDispatcher),
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
                dispatchers = TestDispatchers(dispatcherExtension.testDispatcher),
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
                dispatchers = TestDispatchers(dispatcherExtension.testDispatcher),
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
                dispatchers = TestDispatchers(dispatcherExtension.testDispatcher),
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
}

private class FakeConsentHost : ConsentHost {
    override val activity = mockk<android.app.Activity>(relaxed = true)
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
    override val packageName: String = "com.d4rk.android.libs.apptoolkit.test"
}
