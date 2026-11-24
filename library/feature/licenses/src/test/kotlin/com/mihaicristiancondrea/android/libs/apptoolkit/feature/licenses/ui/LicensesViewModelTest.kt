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

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.models.AnalyticsValue
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakeTelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.UnconfinedDispatcherExtension
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.Loadable
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.licenses.ui.constants.LicensesOperationNames
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.licenses.ui.contracts.LicensesEvent
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import kotlin.test.assertEquals

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
        runTest(context = dispatcherExtension.testDispatcher) {
            val viewModel = createViewModel()

            assertEquals(Loadable.Loading, viewModel.state.value.libraryCount)
        }

    @Test
    fun `shows the parsed library count and reports it`() =
        runTest(context = dispatcherExtension.testDispatcher) {
            val viewModel = createViewModel()

            viewModel.onEvent(event = LicensesEvent.LibrariesLoaded(libraryCount = 42))

            assertEquals(Loadable.Ready(value = 42), viewModel.state.value.libraryCount)
            val start = telemetryRepository.loggedEvents.single { it.name == "vm_op_start" }
            assertEquals(
                AnalyticsValue.Str(value = LicensesOperationNames.LOAD_LIBRARIES),
                start.params["action"],
            )
            assertEquals(
                AnalyticsValue.Str(value = "42"),
                start.params[LicensesOperationNames.ExtraKeys.LIBRARY_COUNT],
            )
        }

    @Test
    fun `reports empty state when parsed metadata contains no libraries`() =
        runTest(context = dispatcherExtension.testDispatcher) {
            val viewModel = createViewModel()

            viewModel.onEvent(event = LicensesEvent.LibrariesLoaded(libraryCount = 0))

            assertEquals(Loadable.Empty(), viewModel.state.value.libraryCount)
            val start = telemetryRepository.loggedEvents.single { it.name == "vm_op_start" }
            assertEquals(
                AnalyticsValue.Str(value = LicensesOperationNames.LOAD_LIBRARIES),
                start.params["action"],
            )
            assertEquals(
                AnalyticsValue.Str(value = "0"),
                start.params[LicensesOperationNames.ExtraKeys.LIBRARY_COUNT],
            )
        }

    @Test
    fun `ignores repeated load reports with the same library count`() =
        runTest(context = dispatcherExtension.testDispatcher) {
            val viewModel = createViewModel()

            viewModel.onEvent(event = LicensesEvent.LibrariesLoaded(libraryCount = 42))
            viewModel.onEvent(event = LicensesEvent.LibrariesLoaded(libraryCount = 42))

            assertEquals(Loadable.Ready(value = 42), viewModel.state.value.libraryCount)
            assertEquals(
                1,
                telemetryRepository.loggedEvents.count { it.name == "vm_op_start" },
            )
        }
}
