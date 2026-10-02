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

package com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.data.repositories

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.exceptions.StorageException
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.TelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.providers.BuildInfoProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.interfaces.ConsentPreferencesDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.UnconfinedDispatcherExtension
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.data.exceptions.ConsentException
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.data.remote.datasource.ConsentRemoteDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.domain.models.ConsentHost
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.domain.models.ConsentSettings
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import java.io.IOException
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class DefaultConsentRepositoryTest {

    companion object {
        @JvmField
        @RegisterExtension
        val dispatcherExtension = UnconfinedDispatcherExtension()
    }

    private fun createRepository(
        remote: ConsentRemoteDataSource = CountingConsentRemoteDataSource(),
        local: ConsentPreferencesDataSource = FakeConsentPreferencesDataSource(),
        isDebugBuild: Boolean = false,
        telemetryRepository: TelemetryRepository = mockk(relaxed = true),
        requestScope: CoroutineScope = CoroutineScope(dispatcherExtension.testDispatcher),
    ): DefaultConsentRepository = DefaultConsentRepository(
        remote = remote,
        local = local,
        configProvider = FakeBuildInfoProvider(isDebugBuild = isDebugBuild),
        telemetryRepository = telemetryRepository,
        requestScope = requestScope,
    )

    /** Starts a consent request that records its outcome instead of failing the test scope. */
    private fun TestScope.requestAsync(
        repository: ConsentRepository,
        host: ConsentHost = FakeConsentHost(),
        showIfRequired: Boolean = true,
    ): Deferred<Result<Unit>> = async {
        runCatching { repository.requestConsent(host = host, showIfRequired = showIfRequired) }
    }

    @Test
    fun `requestConsent returns once the round trip succeeds`() = runTest(dispatcherExtension.testDispatcher) {
        val remote = CountingConsentRemoteDataSource()
        val repository = createRepository(remote = remote, requestScope = backgroundScope)

        val request = requestAsync(repository)
        runCurrent()
        remote.complete()

        assertTrue(request.await().isSuccess)
        assertEquals(1, remote.requestCount)
    }

    @Test
    fun `requestConsent throws the round trip's consent exception`() = runTest(dispatcherExtension.testDispatcher) {
        val remote = CountingConsentRemoteDataSource()
        val repository = createRepository(remote = remote, requestScope = backgroundScope)

        val request = requestAsync(repository, showIfRequired = false)
        runCurrent()
        remote.fail(ConsentException(reason = ConsentException.Reason.FORM_FAILED, message = "form"))

        val failure = assertIs<ConsentException>(request.await().exceptionOrNull())
        assertEquals(ConsentException.Reason.FORM_FAILED, failure.reason)
    }

    @Test
    fun `concurrent requests share a single UMP round trip`() = runTest(dispatcherExtension.testDispatcher) {
        val remote = CountingConsentRemoteDataSource()
        val repository = createRepository(remote = remote, requestScope = backgroundScope)

        val first = requestAsync(repository)
        val second = requestAsync(repository)
        runCurrent()
        remote.complete()

        assertTrue(first.await().isSuccess)
        assertTrue(second.await().isSuccess)
        assertEquals(1, remote.requestCount)
    }

    @Test
    fun `a failed shared round trip fails every caller`() = runTest(dispatcherExtension.testDispatcher) {
        val remote = CountingConsentRemoteDataSource()
        val repository = createRepository(remote = remote, requestScope = backgroundScope)

        val first = requestAsync(repository)
        val second = requestAsync(repository)
        runCurrent()
        remote.fail(ConsentException(reason = ConsentException.Reason.REQUEST_FAILED, message = "update"))

        assertIs<ConsentException>(first.await().exceptionOrNull())
        assertIs<ConsentException>(second.await().exceptionOrNull())
        assertEquals(1, remote.requestCount)
    }

    @Test
    fun `a request that forces the form does not attach to an implicit one`() =
        runTest(dispatcherExtension.testDispatcher) {
            val remote = CountingConsentRemoteDataSource()
            val repository = createRepository(remote = remote, requestScope = backgroundScope)

            val implicit = requestAsync(repository)
            val explicit = requestAsync(repository, showIfRequired = false)
            runCurrent()
            remote.complete()

            implicit.await()
            explicit.await()
            assertEquals(2, remote.requestCount)
        }

    @Test
    fun `a later request starts a fresh round trip`() = runTest(dispatcherExtension.testDispatcher) {
        val remote = CountingConsentRemoteDataSource()
        val repository = createRepository(remote = remote, requestScope = backgroundScope)

        val first = requestAsync(repository)
        runCurrent()
        remote.complete()
        first.await()

        val second = requestAsync(repository)
        runCurrent()
        remote.complete()
        second.await()

        assertEquals(2, remote.requestCount)
    }

    /**
     * The second caller waits for the first round trip instead of overlapping it, then starts its
     * own instead of taking the answer meant for the destroyed host.
     */
    @Test
    fun `a request does not join one whose host was destroyed`() = runTest(dispatcherExtension.testDispatcher) {
        val remote = CountingConsentRemoteDataSource()
        val repository = createRepository(remote = remote, requestScope = backgroundScope)
        val rotatedAway = FakeConsentHost()

        val first = requestAsync(repository, host = rotatedAway)
        runCurrent()
        rotatedAway.destroyed = true
        val second = requestAsync(repository)
        runCurrent()
        assertEquals(1, remote.requestCount)

        remote.complete()
        first.await()
        runCurrent()
        remote.complete()

        assertTrue(second.await().isSuccess)
        assertEquals(2, remote.requestCount)
    }

    @Test
    fun `requests from a finishing host never reach UMP`() = runTest(dispatcherExtension.testDispatcher) {
        val remote = CountingConsentRemoteDataSource()
        val repository = createRepository(remote = remote, requestScope = backgroundScope)

        val failure = assertFailsWith<ConsentException> {
            repository.requestConsent(host = FakeConsentHost(isFinishing = true), showIfRequired = true)
        }

        assertEquals(ConsentException.Reason.HOST_UNAVAILABLE, failure.reason)
        assertEquals(0, remote.requestCount)
    }

    @Test
    fun `applyInitialConsent reads persisted values and updates Firebase`() =
        runTest(dispatcherExtension.testDispatcher) {
            val telemetryRepository = mockk<TelemetryRepository>(relaxed = true)
            val repository = createRepository(
                local = FakeConsentPreferencesDataSource(
                    usageAndDiagnostics = true,
                    analyticsConsent = false,
                    adStorageConsent = true,
                    adUserDataConsent = false,
                    adPersonalizationConsent = true,
                ),
                telemetryRepository = telemetryRepository,
            )

            repository.applyInitialConsent()

            verify {
                telemetryRepository.updateConsent(
                    analyticsGranted = false,
                    adStorageGranted = true,
                    adUserDataGranted = false,
                    adPersonalizationGranted = true,
                )
                telemetryRepository.setAnalyticsEnabled(true)
                telemetryRepository.setCrashlyticsEnabled(true)
                telemetryRepository.setPerformanceEnabled(true)
            }
        }

    @Test
    fun `applyInitialConsent grants unset choices in release builds`() = runTest(dispatcherExtension.testDispatcher) {
        val telemetryRepository = mockk<TelemetryRepository>(relaxed = true)
        val repository = createRepository(isDebugBuild = false, telemetryRepository = telemetryRepository)

        repository.applyInitialConsent()

        verify {
            telemetryRepository.updateConsent(
                analyticsGranted = true,
                adStorageGranted = true,
                adUserDataGranted = true,
                adPersonalizationGranted = true,
            )
            telemetryRepository.setAnalyticsEnabled(true)
        }
    }

    @Test
    fun `applyInitialConsent refuses unset choices in debug builds`() = runTest(dispatcherExtension.testDispatcher) {
        val telemetryRepository = mockk<TelemetryRepository>(relaxed = true)
        val repository = createRepository(isDebugBuild = true, telemetryRepository = telemetryRepository)

        repository.applyInitialConsent()

        verify {
            telemetryRepository.updateConsent(
                analyticsGranted = false,
                adStorageGranted = false,
                adUserDataGranted = false,
                adPersonalizationGranted = false,
            )
            telemetryRepository.setAnalyticsEnabled(false)
            telemetryRepository.setCrashlyticsEnabled(false)
            telemetryRepository.setPerformanceEnabled(false)
        }
    }

    @Test
    fun `applyInitialConsent throws a storage exception when the choices cannot be read`() =
        runTest(dispatcherExtension.testDispatcher) {
            val telemetryRepository = mockk<TelemetryRepository>(relaxed = true)
            val repository = createRepository(
                local = FakeConsentPreferencesDataSource(readFailure = IOException("disk")),
                telemetryRepository = telemetryRepository,
            )

            val failure = assertFailsWith<StorageException> { repository.applyInitialConsent() }

            assertEquals(StorageException.Reason.FAILED, failure.reason)
            verify(exactly = 0) { telemetryRepository.updateConsent(any(), any(), any(), any()) }
        }

    @Test
    fun `applyConsentSettings updates Firebase with provided settings`() =
        runTest(dispatcherExtension.testDispatcher) {
            val telemetryRepository = mockk<TelemetryRepository>(relaxed = true)
            val repository = createRepository(isDebugBuild = true, telemetryRepository = telemetryRepository)

            repository.applyConsentSettings(
                ConsentSettings(
                    usageAndDiagnostics = false,
                    analyticsConsent = false,
                    adStorageConsent = true,
                    adUserDataConsent = true,
                    adPersonalizationConsent = false,
                )
            )

            verify {
                telemetryRepository.updateConsent(
                    analyticsGranted = false,
                    adStorageGranted = true,
                    adUserDataGranted = true,
                    adPersonalizationGranted = false,
                )
                telemetryRepository.setAnalyticsEnabled(false)
                telemetryRepository.setCrashlyticsEnabled(false)
                telemetryRepository.setPerformanceEnabled(false)
            }
        }

    @Test
    fun `applyInitialConsent logs a breadcrumb`() = runTest(dispatcherExtension.testDispatcher) {
        val telemetryRepository = mockk<TelemetryRepository>(relaxed = true)
        val repository = createRepository(telemetryRepository = telemetryRepository)

        repository.applyInitialConsent()

        verify { telemetryRepository.logBreadcrumb(message = "Applying initial consent") }
    }

    @Test
    fun `applyConsentSettings logs a breadcrumb with the applied values`() =
        runTest(dispatcherExtension.testDispatcher) {
            val telemetryRepository = mockk<TelemetryRepository>(relaxed = true)
            val repository = createRepository(telemetryRepository = telemetryRepository)

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
                telemetryRepository.logBreadcrumb(
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
 * A remote source whose round trips stay open until [complete] or [fail] answers them, so tests
 * can count how many UMP requests the repository starts while one is in flight.
 */
private class CountingConsentRemoteDataSource : ConsentRemoteDataSource {
    private val answers: MutableList<CompletableDeferred<Unit>> = mutableListOf()

    val requestCount: Int
        get() = answers.size

    override suspend fun requestConsent(
        host: ConsentHost,
        showIfRequired: Boolean,
    ) {
        val answer = CompletableDeferred<Unit>()
        answers += answer
        answer.await()
    }

    /** Answers every open round trip with success. */
    fun complete() {
        answers.forEach { answer -> answer.complete(Unit) }
    }

    /** Answers every open round trip with [failure]. */
    fun fail(failure: ConsentException) {
        answers.forEach { answer -> answer.completeExceptionally(failure) }
    }
}

private class FakeConsentHost(isFinishing: Boolean = false) : ConsentHost {
    /** Set to true to stand for the activity being destroyed while a request is in flight. */
    var destroyed: Boolean = false

    override val activity = mockk<android.app.Activity>(relaxed = true).also {
        every { it.isFinishing } returns isFinishing
        every { it.isDestroyed } answers { destroyed }
    }
}

/** Stored choices, or [readFailure] thrown from every read when set. */
private class FakeConsentPreferencesDataSource(
    private val usageAndDiagnostics: Boolean? = null,
    private val analyticsConsent: Boolean? = null,
    private val adStorageConsent: Boolean? = null,
    private val adUserDataConsent: Boolean? = null,
    private val adPersonalizationConsent: Boolean? = null,
    private val readFailure: Throwable? = null,
) : ConsentPreferencesDataSource {
    override fun usageAndDiagnostics(default: Boolean): Flow<Boolean> = read(usageAndDiagnostics ?: default)

    override fun analyticsConsent(default: Boolean): Flow<Boolean> = read(analyticsConsent ?: default)

    override fun adStorageConsent(default: Boolean): Flow<Boolean> = read(adStorageConsent ?: default)

    override fun adUserDataConsent(default: Boolean): Flow<Boolean> = read(adUserDataConsent ?: default)

    override fun adPersonalizationConsent(default: Boolean): Flow<Boolean> = read(adPersonalizationConsent ?: default)

    private fun read(value: Boolean): Flow<Boolean> =
        readFailure?.let { failure -> flow { throw failure } } ?: flowOf(value)
}

private class FakeBuildInfoProvider(
    override val isDebugBuild: Boolean,
) : BuildInfoProvider {
    override val appVersion: String = "1.0.0-test"
    override val appVersionCode: Int = 1
    override val packageName: String = "com.mihaicristiancondrea.android.libs.apptoolkit.test"
}
