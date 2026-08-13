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

package com.mihaicristiancondrea.android.libs.apptoolkit.app.settings.settings.ui

import com.google.common.truth.Truth.assertThat
import com.mihaicristiancondrea.android.libs.apptoolkit.app.settings.settings.domain.models.SettingsCategory
import com.mihaicristiancondrea.android.libs.apptoolkit.app.settings.settings.domain.models.SettingsConfig
import com.mihaicristiancondrea.android.libs.apptoolkit.app.settings.settings.ui.contracts.SettingsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.app.settings.utils.interfaces.SettingsProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakeFirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.TestDispatchers
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.UnconfinedDispatcherExtension
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.ScreenState
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

@OptIn(ExperimentalCoroutinesApi::class)

class SettingsViewModelTest {

    companion object {
        @JvmField
        @RegisterExtension
        val dispatcherExtension = UnconfinedDispatcherExtension()
    }

    private lateinit var viewModel: SettingsViewModel
    private lateinit var provider: SettingsProvider
    private val firebaseController = FakeFirebaseController()

    private fun setup(config: SettingsConfig) {
        provider = mockk()
        every { provider.provideSettingsConfig() } returns config
        val dispatchers = TestDispatchers(dispatcherExtension.testDispatcher)
        viewModel = SettingsViewModel(provider, dispatchers, firebaseController)
    }

    @Test
    fun `load settings success`() = runTest(dispatcherExtension.testDispatcher) {
        val config = SettingsConfig(
            title = "Title",
            categories = listOf(SettingsCategory(title = "c", preferences = emptyList()))
        )
        setup(config)

        viewModel.onEvent(SettingsEvent.Load)
        advanceUntilIdle()

        assertThat(viewModel.uiState.value.data?.title).isEqualTo("Title")
        assertThat(viewModel.uiState.value.screenState).isInstanceOf(ScreenState.Success::class.java)
    }

    @Test
    fun `load settings no data`() = runTest(dispatcherExtension.testDispatcher) {
        val config = SettingsConfig(title = "", categories = emptyList())
        setup(config)

        viewModel.onEvent(SettingsEvent.Load)
        advanceUntilIdle()

        assertThat(viewModel.uiState.value.screenState).isInstanceOf(ScreenState.NoData::class.java)
    }

    @Test
    fun `load settings clears previous errors on success`() =
        runTest(dispatcherExtension.testDispatcher) {
            val empty = SettingsConfig(title = "", categories = emptyList())
            val valid = SettingsConfig(
                title = "Title",
                categories = listOf(SettingsCategory(title = "c", preferences = emptyList()))
            )
            provider = mockk()
            every { provider.provideSettingsConfig() } returnsMany listOf(empty, valid)
            val dispatchers = TestDispatchers(dispatcherExtension.testDispatcher)
            viewModel = SettingsViewModel(provider, dispatchers, firebaseController)

            viewModel.onEvent(SettingsEvent.Load)
            advanceUntilIdle()
            assertThat(viewModel.uiState.value.screenState).isInstanceOf(ScreenState.NoData::class.java)
            assertThat(viewModel.uiState.value.errors).isNotEmpty()

            viewModel.onEvent(SettingsEvent.Load)
            advanceUntilIdle()
            assertThat(viewModel.uiState.value.screenState).isInstanceOf(ScreenState.Success::class.java)
            assertThat(viewModel.uiState.value.errors).isEmpty()
        }

    @Test
    fun `provider called once per load event`() = runTest(dispatcherExtension.testDispatcher) {
        val config = SettingsConfig(
            title = "Title",
            categories = listOf(SettingsCategory(title = "c", preferences = emptyList()))
        )
        provider = mockk()
        every { provider.provideSettingsConfig() } returns config
        val dispatchers = TestDispatchers(dispatcherExtension.testDispatcher)
        viewModel = SettingsViewModel(provider, dispatchers, firebaseController)

        viewModel.onEvent(SettingsEvent.Load)
        advanceUntilIdle()

        verify(exactly = 1) { provider.provideSettingsConfig() }
    }
}
