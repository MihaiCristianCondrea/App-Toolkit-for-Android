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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.diagnostics.data.repositories

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.exceptions.StorageException
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.BuildInfoRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.DiagnosticsReporter
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.UnconfinedDispatcherExtension
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.diagnostics.data.models.UsageAndDiagnosticsSettings
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.data.local.UsageAndDiagnosticsPreferencesDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.data.models.ConsentSettings
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.data.repositories.ConsentRepository
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import java.io.IOException
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

private class FakeUsageAndDiagnosticsPreferencesDataSource :
    UsageAndDiagnosticsPreferencesDataSource {
    var readFailure: Throwable? = null
    var writeFailure: Throwable? = null
    var lastRequestedDefault: Boolean? = null
    private val stored = MutableStateFlow(
        value = ConsentSettings(
            usageAndDiagnostics = true,
            analyticsConsent = true,
            adStorageConsent = true,
            adUserDataConsent = true,
            adPersonalizationConsent = true,
        ),
    )

    override fun settings(default: Boolean): Flow<ConsentSettings> {
        lastRequestedDefault = default
        val failure = readFailure ?: return stored
        return flow { throw failure }
    }

    override fun usageAndDiagnostics(default: Boolean) = stored.map { it.usageAndDiagnostics }
    override fun analyticsConsent(default: Boolean) = stored.map { it.analyticsConsent }
    override fun adStorageConsent(default: Boolean) = stored.map { it.adStorageConsent }
    override fun adUserDataConsent(default: Boolean) = stored.map { it.adUserDataConsent }
    override fun adPersonalizationConsent(default: Boolean) = stored.map { it.adPersonalizationConsent }

    override suspend fun saveUsageAndDiagnostics(isChecked: Boolean) = write {
        stored.value = stored.value.copy(usageAndDiagnostics = isChecked)
    }

    override suspend fun saveAnalyticsConsent(isGranted: Boolean) = write {
        stored.value = stored.value.copy(analyticsConsent = isGranted)
    }

    override suspend fun saveAdStorageConsent(isGranted: Boolean) = write {
        stored.value = stored.value.copy(adStorageConsent = isGranted)
    }

    override suspend fun saveAdUserDataConsent(isGranted: Boolean) = write {
        stored.value = stored.value.copy(adUserDataConsent = isGranted)
    }

    override suspend fun saveAdPersonalizationConsent(isGranted: Boolean) = write {
        stored.value = stored.value.copy(adPersonalizationConsent = isGranted)
    }

    override suspend fun saveAll(
        usageAndDiagnostics: Boolean,
        analyticsConsent: Boolean,
        adStorageConsent: Boolean,
        adUserDataConsent: Boolean,
        adPersonalizationConsent: Boolean,
    ) = write {
        stored.value = ConsentSettings(
            usageAndDiagnostics = usageAndDiagnostics,
            analyticsConsent = analyticsConsent,
            adStorageConsent = adStorageConsent,
            adUserDataConsent = adUserDataConsent,
            adPersonalizationConsent = adPersonalizationConsent,
        )
    }

    private fun write(change: () -> Unit) {
        writeFailure?.let { throw it }
        change()
    }
}

private class FakeBuildInfoRepository(
    override val isDebugBuild: Boolean = false,
) : BuildInfoRepository {
    override val appVersion: String = ""
    override val appVersionCode: Int = 0
    override val packageName: String = ""
}

@OptIn(ExperimentalCoroutinesApi::class)
class DefaultUsageAndDiagnosticsRepositoryTest {

    companion object {
        @JvmField
        @RegisterExtension
        val dispatcherExtension = UnconfinedDispatcherExtension()
    }

    private val consentRepository: ConsentRepository = mockk(relaxed = true)

    @Test
    fun `observeSettings reflects data source updates`() =
        runTest(context = dispatcherExtension.testDispatcher) {
            val dataSource = FakeUsageAndDiagnosticsPreferencesDataSource()
            val repository = DefaultUsageAndDiagnosticsRepository(
                dataSource = dataSource,
                buildInfoRepository = FakeBuildInfoRepository(),
                diagnostics = mockk<DiagnosticsReporter>(relaxed = true),
                consentRepository = consentRepository,
            )

            assertTrue(actual = repository.observeSettings().first().usageAndDiagnostics)

            repository.setUsageAndDiagnostics(enabled = false)
            advanceUntilIdle()

            assertFalse(actual = repository.observeSettings().first().usageAndDiagnostics)
        }

    /** The SDKs get the stored answer once, after the write, with no mix in between. */
    @Test
    fun `setAll stores every value of the bundle`() =
        runTest(context = dispatcherExtension.testDispatcher) {
            val diagnostics = mockk<DiagnosticsReporter>(relaxed = true)
            val repository = DefaultUsageAndDiagnosticsRepository(
                dataSource = FakeUsageAndDiagnosticsPreferencesDataSource(),
                buildInfoRepository = FakeBuildInfoRepository(),
                diagnostics = diagnostics,
                consentRepository = consentRepository,
            )
            val essentialOnly = UsageAndDiagnosticsSettings(
                usageAndDiagnostics = true,
                analyticsConsent = true,
                adStorageConsent = true,
                adUserDataConsent = false,
                adPersonalizationConsent = false,
            )

            repository.setAll(settings = essentialOnly)
            advanceUntilIdle()

            verify(exactly = 0) {
                diagnostics.logBreadcrumb(message = "Usage diagnostics observe", attributes = any())
            }
            assertEquals(expected = essentialOnly, actual = repository.observeSettings().first())
            coVerify(exactly = 1) {
                consentRepository.applyConsentSettings(
                    settings = ConsentSettings(
                        usageAndDiagnostics = true,
                        analyticsConsent = true,
                        adStorageConsent = true,
                        adUserDataConsent = false,
                        adPersonalizationConsent = false,
                    ),
                )
            }
        }

    @Test
    fun `observeSettings defaults to disabled in debug builds and enabled in release builds`() =
        runTest(context = dispatcherExtension.testDispatcher) {
            val debugSource = FakeUsageAndDiagnosticsPreferencesDataSource()
            DefaultUsageAndDiagnosticsRepository(
                dataSource = debugSource,
                buildInfoRepository = FakeBuildInfoRepository(isDebugBuild = true),
                diagnostics = mockk(relaxed = true),
                consentRepository = consentRepository,
            ).observeSettings().first()
            assertEquals(expected = false, actual = debugSource.lastRequestedDefault)

            val releaseSource = FakeUsageAndDiagnosticsPreferencesDataSource()
            DefaultUsageAndDiagnosticsRepository(
                dataSource = releaseSource,
                buildInfoRepository = FakeBuildInfoRepository(isDebugBuild = false),
                diagnostics = mockk(relaxed = true),
                consentRepository = consentRepository,
            ).observeSettings().first()
            assertEquals(expected = true, actual = releaseSource.lastRequestedDefault)
        }

    @Test
    fun `read and write IO errors map to StorageException`() =
        runTest(context = dispatcherExtension.testDispatcher) {
            val dataSource = FakeUsageAndDiagnosticsPreferencesDataSource()
            val repository = DefaultUsageAndDiagnosticsRepository(
                dataSource = dataSource,
                buildInfoRepository = FakeBuildInfoRepository(),
                diagnostics = mockk(relaxed = true),
                consentRepository = consentRepository,
            )

            dataSource.readFailure = IOException("read failed")
            val readError = runCatching { repository.observeSettings().first() }.exceptionOrNull()
            assertIs<StorageException>(value = readError)

            dataSource.readFailure = null
            dataSource.writeFailure = IOException("write failed")
            val writeError = runCatching { repository.setAnalyticsConsent(granted = false) }.exceptionOrNull()
            assertIs<StorageException>(value = writeError)
        }
}
