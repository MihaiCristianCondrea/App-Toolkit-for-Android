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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.permissions.ui

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.platform.UiTextHelper
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakeTelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.UnconfinedDispatcherExtension
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.settings.SettingsCategory
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.settings.SettingsConfig
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.settings.SettingsPreference
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.Loadable
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.permissions.R
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.permissions.data.repositories.PermissionsRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.permissions.ui.contracts.PermissionsEvent
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class PermissionsViewModelTest {

    companion object {
        @JvmField
        @RegisterExtension
        val dispatcherExtension = UnconfinedDispatcherExtension()
    }

    private val telemetryRepository = FakeTelemetryRepository()

    private val catalog = SettingsConfig(
        title = "Permissions",
        categories = listOf(
            SettingsCategory(title = "Normal", preferences = listOf(SettingsPreference(title = "Internet"))),
        ),
    )

    private fun createViewModel(repository: PermissionsRepository): PermissionsViewModel =
        PermissionsViewModel(permissionsRepository = repository, telemetryRepository = telemetryRepository)

    private fun advance() = dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()

    @Test
    fun `the first load shows the catalog`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = createViewModel(FakePermissionsRepository(catalog))
        advance()

        assertEquals(Loadable.Ready(catalog), viewModel.state.value.config)
    }

    @Test
    fun `a catalog with no category shows the empty state with its message`() =
        runTest(dispatcherExtension.testDispatcher) {
            val viewModel = createViewModel(
                FakePermissionsRepository(SettingsConfig(title = "", categories = emptyList())),
            )
            advance()

            val config = assertIs<Loadable.Empty>(viewModel.state.value.config)
            assertEquals(
                R.string.error_no_settings_found,
                (config.message as UiTextHelper.StringResource).resourceId,
            )
        }

    @Test
    fun `a repository that throws shows a retryable failure and reports it`() =
        runTest(dispatcherExtension.testDispatcher) {
            val viewModel = createViewModel(FakePermissionsRepository(catalog, failure = RuntimeException("fail")))
            advance()

            val config = assertIs<Loadable.Failed>(viewModel.state.value.config)
            assertTrue(config.retryable)
            assertTrue(telemetryRepository.loggedEvents.any { it.name == "vm_op_error" })
        }

    @Test
    fun `retrying after a failure shows the catalog`() = runTest(dispatcherExtension.testDispatcher) {
        val repository = FakePermissionsRepository(catalog, failure = RuntimeException("fail"))
        val viewModel = createViewModel(repository)
        advance()
        assertIs<Loadable.Failed>(viewModel.state.value.config)

        repository.failure = null
        viewModel.onEvent(PermissionsEvent.Load)
        advance()

        assertEquals(Loadable.Ready(catalog), viewModel.state.value.config)
    }

    private class FakePermissionsRepository(
        private val config: SettingsConfig,
        var failure: Throwable? = null,
    ) : PermissionsRepository {
        override fun getPermissionsConfig(): SettingsConfig {
            failure?.let { throw it }
            return config
        }
    }
}
