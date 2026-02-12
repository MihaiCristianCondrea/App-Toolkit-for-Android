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

package com.d4rk.android.libs.apptoolkit.app.permissions.ui

import com.d4rk.android.libs.apptoolkit.app.issuereporter.ui.IssueReporterViewModelTest
import com.d4rk.android.libs.apptoolkit.app.permissions.domain.repository.PermissionsRepository
import com.d4rk.android.libs.apptoolkit.app.permissions.ui.contract.PermissionsEvent
import com.d4rk.android.libs.apptoolkit.app.settings.settings.domain.model.SettingsCategory
import com.d4rk.android.libs.apptoolkit.app.settings.settings.domain.model.SettingsConfig
import com.d4rk.android.libs.apptoolkit.core.di.TestDispatchers
import com.d4rk.android.libs.apptoolkit.core.ui.state.ScreenState
import com.d4rk.android.libs.apptoolkit.core.utils.FakeFirebaseController
import com.d4rk.android.libs.apptoolkit.core.utils.dispatchers.UnconfinedDispatcherExtension
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

@OptIn(ExperimentalCoroutinesApi::class)
class TestPermissionsViewModel {

    companion object {
        @JvmField
        @RegisterExtension
        val dispatcherExtension = UnconfinedDispatcherExtension()
    }

    private lateinit var viewModel: PermissionsViewModel
    private lateinit var repository: PermissionsRepository
    private val firebaseController = FakeFirebaseController()

    private fun setup(config: SettingsConfig? = null, error: Throwable? = null) {
        repository = mockk()
        if (error != null) {
            every { repository.getPermissionsConfig() } returns flow { throw error }
        } else {
            every { repository.getPermissionsConfig() } returns flowOf(config!!)
        }
        val dispatchers =
            TestDispatchers(IssueReporterViewModelTest.dispatcherExtension.testDispatcher)
        viewModel = PermissionsViewModel(
            permissionsRepository = repository,
            dispatchers = dispatchers,
            firebaseController = firebaseController
        )
    }

    @Test
    fun `load permissions success`() = runTest(dispatcherExtension.testDispatcher) {
        val config = SettingsConfig(
            title = "P",
            categories = listOf(SettingsCategory(title = "c", preferences = emptyList()))
        )
        setup(config = config)

        viewModel.onEvent(PermissionsEvent.Load)
        advanceUntilIdle()

        assertThat(viewModel.uiState.value.data?.title).isEqualTo("P")
        assertThat(viewModel.uiState.value.screenState).isInstanceOf(ScreenState.Success::class.java)
    }

    @Test
    fun `load permissions error`() = runTest(dispatcherExtension.testDispatcher) {
        setup(error = RuntimeException("fail"))

        viewModel.onEvent(PermissionsEvent.Load)
        advanceUntilIdle()

        assertThat(viewModel.uiState.value.screenState).isInstanceOf(ScreenState.Error::class.java)
    }

    @Test
    fun `load permissions with empty categories`() = runTest(dispatcherExtension.testDispatcher) {
        val config = SettingsConfig(title = "", categories = emptyList())
        setup(config = config)

        viewModel.onEvent(PermissionsEvent.Load)
        advanceUntilIdle()

        assertThat(viewModel.uiState.value.screenState).isInstanceOf(ScreenState.NoData::class.java)
    }
}
