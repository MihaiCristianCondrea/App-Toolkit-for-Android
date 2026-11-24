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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.changelog.ui

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.exceptions.NetworkException
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.BuildInfoRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.UiText
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakeTelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.UnconfinedDispatcherExtension
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.Loadable
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.changelog.R
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.changelog.data.models.ChangelogRelease
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.changelog.data.repositories.ChangelogRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.changelog.domain.usecases.GetChangelogUseCase
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.changelog.ui.contracts.ChangelogEvent
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.R as CoreUiR

class ChangelogViewModelTest {

    companion object {
        @JvmField
        @RegisterExtension
        val dispatcherExtension = UnconfinedDispatcherExtension()
    }

    private val telemetryRepository = FakeTelemetryRepository()

    private val sampleReleases = listOf(
        ChangelogRelease(heading = "2.0.0", sections = listOf("- New"), isCurrent = true),
        ChangelogRelease(heading = "1.0.0", sections = listOf("- Old"), isCurrent = false),
    )

    private fun createViewModel(repository: ChangelogRepository): ChangelogViewModel =
        ChangelogViewModel(
            getChangelogUseCase = GetChangelogUseCase(
                repository = repository,
                buildInfoRepository = BuildInfo,
            ),
            telemetryRepository = telemetryRepository,
        )

    private fun advance() = dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()

    private val Loadable.Failed.resourceId: Int
        get() = (message as UiText.StringResource).resourceId

    @Test
    fun `the first load exposes mapped releases`() =
        runTest(dispatcherExtension.testDispatcher) {
            val viewModel = createViewModel(FakeChangelogRepository(releases = sampleReleases))
            advance()

            assertEquals(
                Loadable.Ready(value = persistentListOf(*sampleReleases.toTypedArray())),
                viewModel.state.value.releases,
            )
        }

    @Test
    fun `a load requested during the first fetch keeps the active fetch`() =
        runTest(dispatcherExtension.testDispatcher) {
            val response = CompletableDeferred<List<ChangelogRelease>>()
            var requests = 0
            val viewModel = createViewModel(
                object : ChangelogRepository {
                    override suspend fun getChangelog(
                        packageName: String,
                        currentVersion: String,
                    ): List<ChangelogRelease> {
                        requests++
                        return response.await()
                    }
                },
            )

            viewModel.onEvent(event = ChangelogEvent.Load)
            advance()
            assertEquals(1, requests)
            assertEquals(Loadable.Loading, viewModel.state.value.releases)

            val releases = listOf(
                ChangelogRelease(heading = "2.0.0", sections = listOf("- New"), isCurrent = true),
            )
            response.complete(value = releases)
            advance()
            assertEquals(
                Loadable.Ready(value = persistentListOf(releases.single())),
                viewModel.state.value.releases,
            )
        }

    @Test
    fun `an empty changelog shows the empty state`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = createViewModel(FakeChangelogRepository(releases = emptyList()))
        advance()

        assertIs<Loadable.Empty>(viewModel.state.value.releases)
    }

    @Test
    fun `a failure with no text of its own shows the changelog's text with a retry`() =
        runTest(dispatcherExtension.testDispatcher) {
            val viewModel =
                createViewModel(FakeChangelogRepository(failure = IllegalStateException("bug")))
            advance()

            val releases = assertIs<Loadable.Failed>(viewModel.state.value.releases)
            assertEquals(R.string.error_loading_changelog_message, releases.resourceId)
            assertTrue(releases.retryable)
            assertTrue(telemetryRepository.loggedEvents.any { it.name == "vm_op_error" })
        }

    @Test
    fun `being offline shows the offline text`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = createViewModel(
            FakeChangelogRepository(failure = NetworkException(reason = NetworkException.Reason.NO_INTERNET)),
        )
        advance()

        val releases = assertIs<Loadable.Failed>(viewModel.state.value.releases)
        assertEquals(CoreUiR.string.screen_error_no_internet, releases.resourceId)
    }

    @Test
    fun `a client error is not retryable`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = createViewModel(
            FakeChangelogRepository(failure = NetworkException(reason = NetworkException.Reason.CLIENT)),
        )
        advance()

        val releases = assertIs<Loadable.Failed>(viewModel.state.value.releases)
        assertEquals(R.string.error_loading_changelog_message, releases.resourceId)
        assertFalse(releases.retryable)
    }

    @Test
    fun `retrying after a failure shows the releases`() = runTest(dispatcherExtension.testDispatcher) {
        val repository = FakeChangelogRepository(
            releases = sampleReleases,
            failure = IllegalStateException("bug"),
        )
        val viewModel = createViewModel(repository)
        advance()
        assertIs<Loadable.Failed>(viewModel.state.value.releases)

        repository.failure = null
        viewModel.onEvent(event = ChangelogEvent.Load)
        advance()

        assertEquals(
            Loadable.Ready(value = persistentListOf(*sampleReleases.toTypedArray())),
            viewModel.state.value.releases,
        )
    }

    private object BuildInfo : BuildInfoRepository {
        override val appVersion: String = "2.0.0"
        override val appVersionCode: Int = 20
        override val packageName: String = "com.example.app"
        override val isDebugBuild: Boolean = false
    }

    private class FakeChangelogRepository(
        private val releases: List<ChangelogRelease> = emptyList(),
        var failure: Throwable? = null,
    ) : ChangelogRepository {
        override suspend fun getChangelog(
            packageName: String,
            currentVersion: String,
        ): List<ChangelogRelease> {
            failure?.let { throw it }
            return releases
        }
    }
}
