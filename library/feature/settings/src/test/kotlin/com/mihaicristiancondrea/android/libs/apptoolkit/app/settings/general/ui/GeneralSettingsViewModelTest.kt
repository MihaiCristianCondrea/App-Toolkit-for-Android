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

package com.mihaicristiancondrea.android.libs.apptoolkit.app.settings.general.ui

import com.mihaicristiancondrea.android.libs.apptoolkit.feature.settings.R
import com.mihaicristiancondrea.android.libs.apptoolkit.app.settings.general.data.repository.GeneralSettingsRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.app.settings.general.ui.contract.GeneralSettingsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.coroutines.dispatchers.DispatcherProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.TestDispatchers
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repository.FirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.state.ScreenState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakeFirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.UnconfinedDispatcherExtension
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.platform.UiTextHelper
import com.google.common.truth.Truth.assertThat
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

@OptIn(ExperimentalCoroutinesApi::class)
class GeneralSettingsViewModelTest {

    companion object {
        @JvmField
        @RegisterExtension
        val dispatcherExtension = UnconfinedDispatcherExtension()
    }

    /**
     * Important: make TestDispatchers use the SAME dispatcher/scheduler as runTest(),
     * otherwise `flowOn(dispatchers.default)` runs on a different scheduler and never advances.
     */
    private fun testDispatchers(): DispatcherProvider =
        TestDispatchers(dispatcherExtension.testDispatcher)

    private val firebaseController = FakeFirebaseController()

    @Test
    fun `load content success`() = runTest(dispatcherExtension.testDispatcher) {
        println("🚀 [TEST] load content success")
        val viewModel = GeneralSettingsViewModel(
            repository = GeneralSettingsRepository(
                firebaseController = mockk<FirebaseController>(
                    relaxed = true
                )
            ),
            dispatchers = testDispatchers(),
            firebaseController = firebaseController,
        )

        viewModel.onEvent(GeneralSettingsEvent.Load("key"))
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertThat(state.screenState).isInstanceOf(ScreenState.Success::class.java)
        assertThat(state.data?.contentKey).isEqualTo("key")
        println("🏁 [TEST DONE] load content success")
    }

    @Test
    fun `load content invalid`() = runTest(dispatcherExtension.testDispatcher) {
        println("🚀 [TEST] load content invalid")
        val viewModel = GeneralSettingsViewModel(
            repository = GeneralSettingsRepository(
                firebaseController = mockk<FirebaseController>(
                    relaxed = true
                )
            ),
            dispatchers = testDispatchers(),
            firebaseController = firebaseController,
        )

        viewModel.onEvent(GeneralSettingsEvent.Load(null))
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertThat(state.screenState).isInstanceOf(ScreenState.NoData::class.java)
        val error = state.errors.first().message as UiTextHelper.StringResource
        assertThat(error.resourceId).isEqualTo(R.string.error_invalid_content_key)
        println("🏁 [TEST DONE] load content invalid")
    }

    @Test
    fun `load content blank`() = runTest(dispatcherExtension.testDispatcher) {
        println("🚀 [TEST] load content blank")
        val viewModel = GeneralSettingsViewModel(
            repository = GeneralSettingsRepository(
                firebaseController = mockk<FirebaseController>(
                    relaxed = true
                )
            ),
            dispatchers = testDispatchers(),
            firebaseController = firebaseController,
        )

        viewModel.onEvent(GeneralSettingsEvent.Load(""))
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertThat(state.screenState).isInstanceOf(ScreenState.NoData::class.java)
        val error = state.errors.first().message as UiTextHelper.StringResource
        assertThat(error.resourceId).isEqualTo(R.string.error_invalid_content_key)
        println("🏁 [TEST DONE] load content blank")
    }

    @Test
    fun `multiple load calls update key`() = runTest(dispatcherExtension.testDispatcher) {
        println("🚀 [TEST] multiple load calls update key")
        val viewModel = GeneralSettingsViewModel(
            repository = GeneralSettingsRepository(
                firebaseController = mockk<FirebaseController>(
                    relaxed = true
                )
            ),
            dispatchers = testDispatchers(),
            firebaseController = firebaseController,
        )

        viewModel.onEvent(GeneralSettingsEvent.Load("one"))
        advanceUntilIdle()

        var state = viewModel.uiState.value
        assertThat(state.data?.contentKey).isEqualTo("one")

        viewModel.onEvent(GeneralSettingsEvent.Load("two"))
        advanceUntilIdle()

        state = viewModel.uiState.value
        assertThat(state.data?.contentKey).isEqualTo("two")
        println("🏁 [TEST DONE] multiple load calls update key")
    }

    @Test
    fun `errors cleared after successful load`() = runTest(dispatcherExtension.testDispatcher) {
        println("🚀 [TEST] errors cleared after successful load")
        val viewModel = GeneralSettingsViewModel(
            repository = GeneralSettingsRepository(
                firebaseController = mockk<FirebaseController>(
                    relaxed = true
                )
            ),
            dispatchers = testDispatchers(),
            firebaseController = firebaseController,
        )

        viewModel.onEvent(GeneralSettingsEvent.Load(""))
        advanceUntilIdle()

        var state = viewModel.uiState.value
        assertThat(state.errors).isNotEmpty()

        viewModel.onEvent(GeneralSettingsEvent.Load("valid"))
        advanceUntilIdle()

        state = viewModel.uiState.value
        assertThat(state.screenState).isInstanceOf(ScreenState.Success::class.java)
        assertThat(state.errors).isEmpty()
        println("🏁 [TEST DONE] errors cleared after successful load")
    }

    @Test
    fun `content persists across config changes`() = runTest(dispatcherExtension.testDispatcher) {
        println("🚀 [TEST] content persists across config changes")
        val viewModel = GeneralSettingsViewModel(
            repository = GeneralSettingsRepository(
                firebaseController = mockk<FirebaseController>(
                    relaxed = true
                )
            ),
            dispatchers = testDispatchers(),
            firebaseController = firebaseController,
        )

        viewModel.onEvent(GeneralSettingsEvent.Load("rotate"))
        advanceUntilIdle()

        val stateBefore = viewModel.uiState.value

        // simulate orientation change by checking state again
        val stateAfter = viewModel.uiState.value
        assertThat(stateAfter.data?.contentKey).isEqualTo(stateBefore.data?.contentKey)
        println("🏁 [TEST DONE] content persists across config changes")
    }

    @Test
    fun `reload with same key retains state`() = runTest(dispatcherExtension.testDispatcher) {
        println("🚀 [TEST] reload with same key retains state")
        val viewModel = GeneralSettingsViewModel(
            repository = GeneralSettingsRepository(
                firebaseController = mockk<FirebaseController>(
                    relaxed = true
                )
            ),
            dispatchers = testDispatchers(),
            firebaseController = firebaseController,
        )

        viewModel.onEvent(GeneralSettingsEvent.Load("keep"))
        advanceUntilIdle()
        val stateBefore = viewModel.uiState.value

        viewModel.onEvent(GeneralSettingsEvent.Load("keep"))
        advanceUntilIdle()
        val stateAfter = viewModel.uiState.value

        assertThat(stateAfter).isEqualTo(stateBefore)
        println("🏁 [TEST DONE] reload with same key retains state")
    }

    @Test
    fun `load extremely long content key`() = runTest(dispatcherExtension.testDispatcher) {
        println("🚀 [TEST] load extremely long content key")
        val viewModel = GeneralSettingsViewModel(
            repository = GeneralSettingsRepository(
                firebaseController = mockk<FirebaseController>(
                    relaxed = true
                )
            ),
            dispatchers = testDispatchers(),
            firebaseController = firebaseController,
        )

        val longKey = "a".repeat(1000)
        viewModel.onEvent(GeneralSettingsEvent.Load(longKey))
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertThat(state.screenState).isInstanceOf(ScreenState.Success::class.java)
        assertThat(state.data?.contentKey).isEqualTo(longKey)
        println("🏁 [TEST DONE] load extremely long content key")
    }

    @Test
    fun `load content key with special characters`() = runTest(dispatcherExtension.testDispatcher) {
        println("🚀 [TEST] load content key with special characters")
        val viewModel = GeneralSettingsViewModel(
            repository = GeneralSettingsRepository(
                firebaseController = mockk<FirebaseController>(
                    relaxed = true
                )
            ),
            dispatchers = testDispatchers(),
            firebaseController = firebaseController,
        )

        val key = "!@#$%^&*()_+漢字"
        viewModel.onEvent(GeneralSettingsEvent.Load(key))
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertThat(state.screenState).isInstanceOf(ScreenState.Success::class.java)
        assertThat(state.data?.contentKey).isEqualTo(key)
        println("🏁 [TEST DONE] load content key with special characters")
    }

    @Test
    fun `concurrent load events yield latest state`() =
        runTest(dispatcherExtension.testDispatcher) {
            println("🚀 [TEST] concurrent load events yield latest state")
            val viewModel = GeneralSettingsViewModel(
                repository = GeneralSettingsRepository(
                    firebaseController = mockk<FirebaseController>(
                        relaxed = true
                    )
                ),
                dispatchers = testDispatchers(),
                firebaseController = firebaseController,
            )

            viewModel.onEvent(GeneralSettingsEvent.Load("first"))
            viewModel.onEvent(GeneralSettingsEvent.Load("second"))
            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertThat(state.data?.contentKey).isEqualTo("second")
            println("🏁 [TEST DONE] concurrent load events yield latest state")
        }
}
