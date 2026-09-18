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
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakeFirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.UnconfinedDispatcherExtension
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.ScreenState
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

    private fun createViewModel() =
        LicensesViewModel(firebaseController = FakeFirebaseController())

    @Test
    fun `starts out loading while the metadata is parsed`() =
        runTest(dispatcherExtension.testDispatcher) {
            val viewModel = createViewModel()
            dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()

            assertThat(viewModel.uiState.value.screenState)
                .isInstanceOf(ScreenState.IsLoading::class.java)
        }

    @Test
    fun `reports success with the parsed library count`() =
        runTest(dispatcherExtension.testDispatcher) {
            val viewModel = createViewModel()
            dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()

            viewModel.onEvent(LicensesEvent.LibrariesLoaded(libraryCount = 42))
            dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()

            assertThat(viewModel.uiState.value.screenState)
                .isInstanceOf(ScreenState.Success::class.java)
            assertThat(viewModel.uiState.value.data?.libraryCount).isEqualTo(42)
        }
}
