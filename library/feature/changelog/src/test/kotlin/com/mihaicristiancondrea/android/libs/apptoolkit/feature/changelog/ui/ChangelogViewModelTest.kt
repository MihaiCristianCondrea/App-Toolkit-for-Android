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
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.platform.UiTextHelper
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.providers.BuildInfoProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakeTelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.UnconfinedDispatcherExtension
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.R as CoreUiR
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.Loadable
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.changelog.R
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.changelog.data.repositories.ChangelogRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.changelog.domain.usecases.GetChangelogUseCase
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.changelog.ui.contracts.ChangelogEvent
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class ChangelogViewModelTest {

    companion object {
        @JvmField
        @RegisterExtension
        val dispatcherExtension = UnconfinedDispatcherExtension()
    }

    private val telemetryRepository = FakeTelemetryRepository()

    private fun createViewModel(repository: ChangelogRepository): ChangelogViewModel =
        ChangelogViewModel(
            getChangelogUseCase = GetChangelogUseCase(repository = repository, buildInfoProvider = BuildInfo),
            telemetryRepository = telemetryRepository,
        )

    private fun advance() = dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()

    private val Loadable.Failed.resourceId: Int
        get() = (message as UiTextHelper.StringResource).resourceId

    @Test
    fun `the first load shows the current version's notes`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = createViewModel(FakeChangelogRepository(markdown = "# 2.0.0\n- New\n# 1.0.0\n- Old"))
        advance()

        assertEquals(Loadable.Ready("# 2.0.0\n- New"), viewModel.state.value.markdown)
    }

    @Test
    fun `a blank changelog shows the empty state`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = createViewModel(FakeChangelogRepository(markdown = "  "))
        advance()

        assertIs<Loadable.Empty>(viewModel.state.value.markdown)
    }

    @Test
    fun `a failure with no text of its own shows the changelog's text with a retry`() =
        runTest(dispatcherExtension.testDispatcher) {
            val viewModel = createViewModel(FakeChangelogRepository(failure = IllegalStateException("bug")))
            advance()

            val markdown = assertIs<Loadable.Failed>(viewModel.state.value.markdown)
            assertEquals(R.string.error_loading_changelog_message, markdown.resourceId)
            assertTrue(markdown.retryable)
            assertTrue(telemetryRepository.loggedEvents.any { it.name == "vm_op_error" })
        }

    @Test
    fun `being offline shows the offline text`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = createViewModel(
            FakeChangelogRepository(failure = NetworkException(NetworkException.Reason.NO_INTERNET)),
        )
        advance()

        val markdown = assertIs<Loadable.Failed>(viewModel.state.value.markdown)
        assertEquals(CoreUiR.string.screen_error_no_internet, markdown.resourceId)
    }

    @Test
    fun `retrying after a failure shows the notes`() = runTest(dispatcherExtension.testDispatcher) {
        val repository = FakeChangelogRepository(markdown = "# 2.0.0\n- New", failure = IllegalStateException("bug"))
        val viewModel = createViewModel(repository)
        advance()
        assertIs<Loadable.Failed>(viewModel.state.value.markdown)

        repository.failure = null
        viewModel.onEvent(ChangelogEvent.Load)
        advance()

        assertEquals(Loadable.Ready("# 2.0.0\n- New"), viewModel.state.value.markdown)
    }

    private object BuildInfo : BuildInfoProvider {
        override val appVersion: String = "2.0.0"
        override val appVersionCode: Int = 20
        override val packageName: String = "com.example.app"
        override val isDebugBuild: Boolean = false
    }

    private class FakeChangelogRepository(
        private val markdown: String = "",
        var failure: Throwable? = null,
    ) : ChangelogRepository {
        override suspend fun getChangelog(packageName: String): String {
            failure?.let { throw it }
            return markdown
        }
    }
}
