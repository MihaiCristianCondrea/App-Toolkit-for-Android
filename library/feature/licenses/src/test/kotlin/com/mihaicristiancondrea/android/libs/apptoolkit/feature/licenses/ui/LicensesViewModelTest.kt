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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.licenses.ui

import com.google.common.truth.Truth.assertThat
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.analytics.AnalyticsValue
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakeTelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.UnconfinedDispatcherExtension
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.Loadable
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.licenses.ui.contracts.LicensesEvent
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

class LicensesViewModelTest {

    companion object {
        @JvmField
        @RegisterExtension
        val dispatcherExtension = UnconfinedDispatcherExtension()
    }

    private val telemetryRepository = FakeTelemetryRepository()

    private fun createViewModel() = LicensesViewModel(telemetryRepository = telemetryRepository)

    @Test
    fun `starts out loading while the metadata is parsed`() =
        runTest(dispatcherExtension.testDispatcher) {
            val viewModel = createViewModel()

            assertThat(viewModel.state.value.libraryCount).isEqualTo(Loadable.Loading)
        }

    @Test
    fun `shows the parsed library count and reports it`() =
        runTest(dispatcherExtension.testDispatcher) {
            val viewModel = createViewModel()

            viewModel.onEvent(LicensesEvent.LibrariesLoaded(libraryCount = 42))

            assertThat(viewModel.state.value.libraryCount).isEqualTo(Loadable.Ready(42))
            val start = telemetryRepository.loggedEvents.single { it.name == "vm_op_start" }
            assertThat(start.params["action"]).isEqualTo(AnalyticsValue.Str("loadLibraries"))
            assertThat(start.params["libraryCount"]).isEqualTo(AnalyticsValue.Str("42"))
        }
}
