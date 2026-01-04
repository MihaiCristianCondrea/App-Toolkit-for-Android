package com.d4rk.android.libs.apptoolkit.core.utils.platform

import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import com.d4rk.android.libs.apptoolkit.data.local.datastore.CommonDataStore
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkAll
import io.mockk.verify
import io.mockk.verifyOrder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module

class ConsentManagerHelperTest {

    @After
    fun tearDown() {
        unmockkAll()
        stopKoin()
    }

    @Test
    fun `updateConsent sets consent statuses for every boolean combination`() {
        val firebaseController = mockk<FirebaseController>(relaxed = true)
        startKoin {
            modules(
                module {
                    single<FirebaseController> { firebaseController }
                    single { mockk<com.d4rk.android.libs.apptoolkit.app.settings.utils.providers.BuildInfoProvider> { every { isDebugBuild } returns false } }
                }
            )
        }

        val combinations = mutableListOf<ConsentFlags>()
        listOf(false, true).forEach { analyticsGranted ->
            listOf(false, true).forEach { adStorageGranted ->
                listOf(false, true).forEach { adUserDataGranted ->
                    listOf(false, true).forEach { adPersonalizationGranted ->
                        combinations += ConsentFlags(
                            analyticsGranted = analyticsGranted,
                            adStorageGranted = adStorageGranted,
                            adUserDataGranted = adUserDataGranted,
                            adPersonalizationGranted = adPersonalizationGranted
                        )
                        ConsentManagerHelper.updateConsent(
                            analyticsGranted = analyticsGranted,
                            adStorageGranted = adStorageGranted,
                            adUserDataGranted = adUserDataGranted,
                            adPersonalizationGranted = adPersonalizationGranted
                        )
                    }
                }
            }
        }

        combinations.forEach { flags ->
            verify {
                firebaseController.updateConsent(
                    analyticsGranted = flags.analyticsGranted,
                    adStorageGranted = flags.adStorageGranted,
                    adUserDataGranted = flags.adUserDataGranted,
                    adPersonalizationGranted = flags.adPersonalizationGranted
                )
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `applyInitialConsent pulls flags then delegates to updateConsent and updateAnalyticsCollection`() =
        runTest {
            val dataStore = mockk<CommonDataStore>()
            val defaultValue = true
            every { dataStore.analyticsConsent(defaultValue) } returns flowOf(true)
            every { dataStore.adStorageConsent(defaultValue) } returns flowOf(false)
            every { dataStore.adUserDataConsent(defaultValue) } returns flowOf(true)
            every { dataStore.adPersonalizationConsent(defaultValue) } returns flowOf(false)

            mockkObject(ConsentManagerHelper)
            every { ConsentManagerHelper.defaultAnalyticsGranted } returns defaultValue
            coEvery { ConsentManagerHelper.applyInitialConsent(any()) } coAnswers { callOriginal() }
            every { ConsentManagerHelper.updateConsent(any(), any(), any(), any()) } answers { }
            coEvery { ConsentManagerHelper.updateAnalyticsCollectionFromDatastore(any()) } returns Unit

            ConsentManagerHelper.applyInitialConsent(dataStore)

            verify(exactly = 1) { dataStore.analyticsConsent(defaultValue) }
            verify(exactly = 1) { dataStore.adStorageConsent(defaultValue) }
            verify(exactly = 1) { dataStore.adUserDataConsent(defaultValue) }
            verify(exactly = 1) { dataStore.adPersonalizationConsent(defaultValue) }

            verify(exactly = 1) {
                ConsentManagerHelper.updateConsent(
                    analyticsGranted = true,
                    adStorageGranted = false,
                    adUserDataGranted = true,
                    adPersonalizationGranted = false
                )
            }

            coVerify(exactly = 1) {
                ConsentManagerHelper.updateAnalyticsCollectionFromDatastore(
                    dataStore
                )
            }
            coVerifyOrder {
                ConsentManagerHelper.updateConsent(
                    analyticsGranted = true,
                    adStorageGranted = false,
                    adUserDataGranted = true,
                    adPersonalizationGranted = false
                )
                ConsentManagerHelper.updateAnalyticsCollectionFromDatastore(dataStore)
            }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `updateAnalyticsCollectionFromDatastore toggles all Firebase SDKs`() = runTest {
        val dataStore = mockk<CommonDataStore>()
        val defaultValue = false

        mockkObject(ConsentManagerHelper)
        every { ConsentManagerHelper.defaultAnalyticsGranted } returns defaultValue
        coEvery { ConsentManagerHelper.updateAnalyticsCollectionFromDatastore(any()) } coAnswers { callOriginal() }

        every { dataStore.usageAndDiagnostics(defaultValue) } returnsMany listOf(
            flowOf(true),
            flowOf(false)
        )

        val firebaseController = mockk<FirebaseController>(relaxed = true)
        startKoin {
            modules(
                module {
                    single<FirebaseController> { firebaseController }
                    single { mockk<com.d4rk.android.libs.apptoolkit.app.settings.utils.providers.BuildInfoProvider> { every { isDebugBuild } returns !defaultValue } }
                }
            )
        }

        ConsentManagerHelper.updateAnalyticsCollectionFromDatastore(dataStore)
        ConsentManagerHelper.updateAnalyticsCollectionFromDatastore(dataStore)

        verify(exactly = 2) { dataStore.usageAndDiagnostics(defaultValue) }
        verifyOrder {
            firebaseController.setAnalyticsEnabled(true)
            firebaseController.setAnalyticsEnabled(false)
        }
        verifyOrder {
            firebaseController.setCrashlyticsEnabled(true)
            firebaseController.setCrashlyticsEnabled(false)
        }
        verifyOrder {
            firebaseController.setPerformanceEnabled(true)
            firebaseController.setPerformanceEnabled(false)
        }
    }

    private data class ConsentFlags(
        val analyticsGranted: Boolean,
        val adStorageGranted: Boolean,
        val adUserDataGranted: Boolean,
        val adPersonalizationGranted: Boolean
    )
}
